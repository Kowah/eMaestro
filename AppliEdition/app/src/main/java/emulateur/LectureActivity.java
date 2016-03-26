package emulateur;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.widget.ImageView;


import com.example.boris.emaestro.R;

import java.util.ArrayList;
import java.util.HashMap;

import BDD.db.DataBaseManager;
import util.Pair;

/**
 * Created by Guillaume on 09/03/2016.
 */
public class LectureActivity extends Activity {

    DataBaseManager bdd;

    int idMusique; //Donnees recuperees de la MainActivity
    int mesureDebut;
    int mesureFin;

    Handler handler; //Gere le lancement des animations au bon moment
    Runnable runnable; //Lance les animations
    HashMap<Integer,Integer> mapMesures; //Map entre le numero d'une mesure et son temps de debut

    ArrayList<Pair<String,Integer>> listeImages = new ArrayList(); //Liste des images correspondant aux temps de la musique
    HashMap<String,Integer> banqueImages = new HashMap<>(); //Banque des images affichables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);

        idMusique = getIntent().getIntExtra("idMusique",1);
        mesureDebut = getIntent().getIntExtra("mesureDebut",1);
        mesureFin = getIntent().getIntExtra("mesureFin",1);

        bdd = new DataBaseManager(this);
        bdd.open();

        //chargement

        Chargeur_partition chargeur = new Chargeur_partition(this,bdd);
        chargeur.charger_partition(idMusique);

        mapMesures = (HashMap) chargeur.get_map_mesures_temps();

        listeImages = (ArrayList) chargeur.get_liste_de_lecture();
        banqueImages = (HashMap) chargeur.get_images_ephemeres();

        //lecture

        final ArrayList<Pair<AnimationDrawable,Integer>> listeAnimations = genererListeAnimations();

        handler = new Handler();

        runnable = new Runnable() {
            int i=0;

            @Override
            public void run() {
                if(i>=listeAnimations.size()){
                    handler.removeCallbacks(runnable);
                }else {
                    Pair<AnimationDrawable, Integer> paireAnim = listeAnimations.get(i);
                    AnimationDrawable anim = paireAnim.getLeft();

                    ImageView image = (ImageView) findViewById(R.id.affichage);
                    if (Build.VERSION.SDK_INT >= 16) {
                        image.setBackground(anim);
                    } else {
                        //noinspection deprecation
                        image.setBackgroundDrawable(anim);
                    }
                    anim.setOneShot(true);
                    anim.start();
                    int runTime = paireAnim.getRight();
                    i++;
                    handler.postDelayed(this, runTime);
                }
            }
        };

        handler.postDelayed(runnable, 0);

/*  //Solution avec 1 seule animation
    //Moins fiable niveau temps

        AnimationDrawable anim = new AnimationDrawable();

        for(Pair<String,Integer> paire : listeImages){
            String image = paire.getLeft();
            int ident = banqueImages.get(image); //On recupere l'id de l'image indique dans la liste
            int temps = paire.getRight(); //On recupere le bpm indique dans la liste
            temps = 60000/temps; //Conversion du BPM en ms
            BitmapDrawable drawable = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), ident, null); //Creation d'une frame d'animation
            drawable.setAntiAlias(false);
            drawable.setFilterBitmap(false);
            anim.addFrame(drawable, temps); //Ajout de la frame a l'animation finale
        }


        ImageView image = (ImageView) findViewById(R.id.affichage);
        if(Build.VERSION.SDK_INT >= 16) {
            image.setBackground(anim);
        }else{
            //noinspection deprecation
            image.setBackgroundDrawable(anim);
        }
        anim.setOneShot(true);
        anim.start(); //Demarrage de l'animation
*/
    }

    private ArrayList<Pair<AnimationDrawable,Integer>> genererListeAnimations(){

        ArrayList<Pair<AnimationDrawable,Integer>> listeAnimations = new ArrayList<>();

        listeAnimations.add(createAnimDecompte());

        for(int i=mesureDebut; i<= mesureFin; i++) {
            //Boucle sur toutes les mesures
            Pair<AnimationDrawable,Integer> paireAnim = createAnimMesure(i);
            listeAnimations.add(paireAnim);
        }
        listeAnimations.add(createAnimFin());
        return listeAnimations;
    }

    private Pair<AnimationDrawable,Integer> createAnimMesure(int numeroMesure){

        AnimationDrawable anim = new AnimationDrawable();
        int runTime=0;
        int tempsDebutMesure = mapMesures.get(numeroMesure);
        int tempsMesureSuivante;

        if(mapMesures.containsKey(numeroMesure+1)){
            tempsMesureSuivante = mapMesures.get(numeroMesure+1);
        }
        else{
            tempsMesureSuivante = listeImages.size();
        }

        for(int j=tempsDebutMesure; j<tempsMesureSuivante; j++){
            //Boucle sur les temps de la mesure pour ajouter les images a l'animation de la mesure
            Pair<String,Integer>  paireTemps = listeImages.get(j);
            runTime += 60000/paireTemps.getRight(); //Ajoute la duree du temps a celle de la mesure
            int ident = banqueImages.get(paireTemps.getLeft()); //On recupere l'id de l'image indique dans la liste
            BitmapDrawable drawable = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), ident, null); //Creation d'une frame d'animation
            drawable.setAntiAlias(false);
            drawable.setFilterBitmap(false);
            anim.addFrame(drawable, 60000 / paireTemps.getRight()); //Ajout de la frame a l'animation finale
        }
        return new Pair(anim,runTime);
    }

    private Pair<AnimationDrawable,Integer> createAnimFin(){

        AnimationDrawable anim = new AnimationDrawable();
        int idFin = banqueImages.get("fin");
        BitmapDrawable drawable = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), idFin, null);
        drawable.setAntiAlias(false);
        drawable.setFilterBitmap(false);
        anim.addFrame(drawable,1);

        return new Pair(anim,1);
    }

    private Pair<AnimationDrawable,Integer> createAnimDecompte(){
        //Cree une animation du decompte de depart avec le nombre de temps de mesureDebut et son tempo
        AnimationDrawable anim = new AnimationDrawable();
        int runTime=0;

        int tempsDebutMesure = mapMesures.get(mesureDebut);
        int tempsMesureSuivante;

        if(mapMesures.containsKey(mesureDebut+1)){
            tempsMesureSuivante = mapMesures.get(mesureDebut+1);
        }
        else{
            tempsMesureSuivante = listeImages.size();
        }

        int nbDecompte = tempsMesureSuivante - tempsDebutMesure;
        int tempo = listeImages.get(tempsDebutMesure).getRight();

        for(int n=nbDecompte; n>0; n--){
            int id = getResources().getIdentifier("d"+n, "drawable", getPackageName());

            BitmapDrawable drawable = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), id, null); //Creation d'une frame d'animation
            drawable.setAntiAlias(false);
            drawable.setFilterBitmap(false);
            anim.addFrame(drawable, 60000 / tempo);

            runTime += 60000/tempo;
        }

        return new Pair(anim,runTime);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        bdd.close();
        super.onDestroy();
    }
}
