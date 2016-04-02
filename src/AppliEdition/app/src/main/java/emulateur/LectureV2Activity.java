package emulateur;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.example.boris.emaestro.R;

import java.util.ArrayList;
import java.util.HashMap;

import BDD.db.DataBaseManager;
import BDD.to.VariationIntensite;
import BDD.to.VariationTemps;
import util.Pair;

/**
 * Created by Guillaume on 24/03/2016.
 */
public class LectureV2Activity extends Activity implements ViewSwitcher.ViewFactory{

    DataBaseManager bdd;
    ImageSwitcher switcher;
    Runnable runnable;
    int idMusique;
    int mesureDebut;
    int mesureFin;
    HashMap<Integer,Integer> mapMesures;
    ArrayList<Pair<String,Integer>> listeImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturev2);

        idMusique = getIntent().getIntExtra("idMusique",1);
        mesureDebut = getIntent().getIntExtra("mesureDebut",1);
        mesureFin = getIntent().getIntExtra("mesureFin",1);

        bdd = new DataBaseManager(this);
        bdd.open();

        //chargement

        Chargeur_partition chargeur = new Chargeur_partition(this,bdd);
        chargeur.charger_partition(idMusique);

        mapMesures = (HashMap) chargeur.get_map_mesures_temps(); //Map entre le numero d'une mesure et son temps de debut
        listeImages = (ArrayList) chargeur.get_liste_de_lecture(); //Liste des images correspondant aux temps de la musique
        final HashMap<String,Integer> banqueImages = (HashMap) chargeur.get_images_ephemeres(); //Banque des images affichables

        final ArrayList<Pair<String,Integer>> listelecture = new ArrayList<>();

        //selection de la partie demandee

        int premierTemps = mapMesures.get(mesureDebut);
        int dernierTemps;
        if(mapMesures.containsKey(mesureFin+1)){
            dernierTemps=mapMesures.get(mesureFin+1);
        }
        else{
            dernierTemps=listeImages.size();
        }

        for(int i=premierTemps; i<dernierTemps; i++){
            listelecture.add(listeImages.get(i));
        }

        //ajout du decompte et de la fin

        int tempsMesure2;

        if(mapMesures.containsKey(mesureDebut+1)){
            tempsMesure2 = mapMesures.get(mesureDebut+1);
        }
        else{
            tempsMesure2 = listeImages.size();
        }

        int nbDecompte = tempsMesure2 - premierTemps;
        int tempo = listeImages.get(premierTemps).getRight();

        for(int n=1; n<=nbDecompte; n++){
            listelecture.add(0,new Pair<>("d"+n,tempo));
        }


        listelecture.add(new Pair<>("fin",1));


        //maps pour les autres informations
        // format : nbTemps -> id image

        final HashMap<Integer,Bitmap> mapMesure = creerMapMesure();
        final HashMap<Integer,Integer> mapNuance = creerMapNuance();
        final HashMap<Integer,Integer> mapSection = creerMapSection();
        final HashMap<Integer,Integer> mapRepetition = creerMapRepetition();
        final HashMap<Integer,Integer> mapBemol = creerMapBemol();
        final HashMap<Integer,Integer> mapSignature = creerMapSignature();


        //lancement de l'animation

        switcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        switcher.setFactory(this);

        runnable = new Runnable() {
            int tempsDebut = mapMesures.get(mesureDebut);
            int tempsMesure2 = (mapMesures.containsKey(mesureDebut+1)) ? mapMesures.get(mesureDebut+1) : listeImages.size();
            int nbDecompte = tempsMesure2 - tempsDebut;

            int index = 0;
            int numeroTemps= tempsDebut-nbDecompte;

            Bitmap bitmapMesure = null;
            Bitmap bitmapNuance = null;

            @Override
            public void run() {

                if(index >= listelecture.size()){
                    switcher.removeCallbacks(runnable);
                }
                else{
                    String key = listelecture.get(index).getLeft();
                    int temps = listelecture.get(index).getRight();
                    temps = 60000/temps;
                    int id = banqueImages.get(key);

                    Bitmap bitmapCercle = BitmapFactory.decodeResource(getResources(), id);

                    if(mapMesure.containsKey(numeroTemps)){
                        bitmapMesure = mapMesure.get(numeroTemps);
                    }
                    if(mapNuance.containsKey(numeroTemps)){
                        bitmapNuance = BitmapFactory.decodeResource(getResources(), mapNuance.get(numeroTemps));
                    }
                    Bitmap bitmapSignature = (mapSignature.containsKey(numeroTemps)) ? BitmapFactory.decodeResource(getResources(), mapSignature.get(numeroTemps)) : null;
                    Bitmap bitmapRepetition = (mapRepetition.containsKey(numeroTemps)) ? BitmapFactory.decodeResource(getResources(), mapRepetition.get(numeroTemps)) : null;
                    Bitmap bitmapSection = (mapSection.containsKey(numeroTemps)) ? BitmapFactory.decodeResource(getResources(), mapSection.get(numeroTemps)) : null;
                    Bitmap bitmapBemol = (mapBemol.containsKey(numeroTemps)) ? BitmapFactory.decodeResource(getResources(), mapBemol.get(numeroTemps)) : null;

                    Bitmap bitmapFinal = assemblerParties(bitmapCercle,bitmapNuance,bitmapSignature,bitmapRepetition,bitmapMesure,bitmapSection,bitmapBemol);

                    BitmapDrawable draw = new BitmapDrawable(getResources(),bitmapFinal);
                    switcher.setImageDrawable(draw);

                    index++;
                    numeroTemps++;

                    switcher.postDelayed(this, temps);
                }
            }
        };

        switcher.postDelayed(runnable,500);

    }

    private HashMap<Integer, Integer> creerMapSignature() {
        return new HashMap<>();
    }

    private HashMap<Integer, Integer> creerMapRepetition() {
        return new HashMap<>();
    }

    private HashMap<Integer, Integer> creerMapSection() {
        return new HashMap<>();
    }

    private HashMap<Integer, Integer> creerMapNuance() {
        HashMap<Integer, Integer> map = new HashMap<>();

        ArrayList<VariationIntensite> variations = bdd.getVariationsIntensite(bdd.getMusique(idMusique));
        for (VariationIntensite var : variations) {
            int mesure = var.getMesureDebut();
            int tempsMesure = mapMesures.get(mesure);
            int i = var.getIntensite();
            int idImage = getResources().getIdentifier("intensite"+i,"drawable",getPackageName());
            map.put(tempsMesure,idImage);
        }
        return map;
    }

    private HashMap<Integer, Bitmap> creerMapMesure() {
        //une image de mesure par temps de debut
        HashMap<Integer, Bitmap> map = new HashMap<>();

        for(int m=mesureDebut; m<=mesureFin; m++){
            int tempsDebut = mapMesures.get(m);
            Bitmap imageM = creerBitmapMesure(m);
            map.put(tempsDebut, imageM);
        }
        return map;
    }

    private Bitmap creerBitmapMesure(int nMesure) {

        Bitmap bitmapMesure = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("mesureexemple","drawable",getPackageName()));

        Bitmap bitmap = Bitmap.createBitmap(bitmapMesure.getWidth(),bitmapMesure.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        int unite = nMesure%10;
        nMesure=nMesure/10;
        int dizaine = nMesure%10;
        nMesure=nMesure/10;
        int centaine = nMesure%10;

        Bitmap bitunite = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("mesure"+unite, "drawable", getPackageName()));
        Bitmap bitdizaine = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("mesure"+dizaine, "drawable", getPackageName()));
        Bitmap bitcentaine = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("mesure"+centaine, "drawable", getPackageName()));

        canvas.drawBitmap(bitunite,bitmap.getWidth()*2/3,0,null);
        canvas.drawBitmap(bitdizaine,bitmap.getWidth()/3,0,null);
        canvas.drawBitmap(bitcentaine,0,0,null);

        return bitmap;
    }

    private HashMap<Integer,Integer> creerMapBemol() {
        return new HashMap<>();
    }

    private Bitmap assemblerParties(Bitmap cercle, Bitmap nuance, Bitmap signature, Bitmap repetition, Bitmap mesure, Bitmap section, Bitmap bemol){
        //cercle n'est pas null

        Bitmap bitmap = Bitmap.createBitmap(cercle.getWidth(), cercle.getHeight(), cercle.getConfig());
        Canvas canvas = new Canvas(bitmap);

        canvas.drawBitmap(cercle,new Matrix(),null);
        //taille 128x64

        if(nuance != null){
            canvas.drawBitmap(nuance,(cercle.getWidth()/4),(cercle.getHeight()/4),null);
            //taille 32x32
        }
        if(signature != null){
            canvas.drawBitmap(signature,0,0,null);
            //taille 16x32
        }
        if(repetition != null){
            canvas.drawBitmap(repetition,0,(3*cercle.getHeight()/4),null);
            //taille 16x16
        }
        if(mesure != null){
            canvas.drawBitmap(mesure,(5*cercle.getWidth()/8),0,null);
            //taille 48x16
        }
        if(section != null){
            canvas.drawBitmap(section,(5*cercle.getWidth()/8),(cercle.getHeight()/4),null);
            //taille 16x16
        }
        if(bemol != null){
            canvas.drawBitmap(bemol,(6*cercle.getWidth()/8),(cercle.getHeight()/2),null);
            //taille 32x16
        }

        return bitmap;
    }

    @Override
    protected void onDestroy() {
        switcher.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public View makeView() {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(0xFF000000);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(
                new ImageSwitcher.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }
}