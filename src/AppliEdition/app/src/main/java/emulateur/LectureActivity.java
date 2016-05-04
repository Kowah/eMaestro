package emulateur;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.example.boris.emaestro.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import BDD.db.DataBaseManager;
import BDD.to.Alertes;
import BDD.to.Armature;
import BDD.to.MesuresNonLues;
import BDD.to.Reprise;
import BDD.to.VariationIntensite;
import BDD.to.VariationTemps;
import util.Pair;

/**
 * Created by Guillaume on 24/03/2016.
 */
public class LectureActivity extends Activity implements ViewSwitcher.ViewFactory{

    DataBaseManager bdd;
    ImageSwitcher switcher;
    Runnable runnable;
    int idMusique;
    int mesureDebut;
    int mesureFin;
    HashMap<Integer,Integer> mapMesures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);

        idMusique = getIntent().getIntExtra("idMusique",1);
        mesureDebut = getIntent().getIntExtra("mesureDebut",1);
        mesureFin = getIntent().getIntExtra("mesureFin",1);

        bdd = new DataBaseManager(this);
        bdd.open();


        mapMesures = creerMapMesureTemps();

        //mapCercle
        // format : nbTemps -> (idImage,tempsAffichage)
        //maps pour les autres informations
        // format : nbTemps -> idImage ou image
        final HashMap<Integer,Pair<Integer,Integer>> mapCercle = creerMapCercle();
        final HashMap<Integer,Bitmap> mapMesure = creerMapMesure();
        final HashMap<Integer,Integer> mapNuance = creerMapNuance();
        final HashMap<Integer,Bitmap> mapArmature = creerMapArmature();
        final HashMap<Integer,Integer> mapAlerte = creerMapAlerte();
        //format : nbTemps Debut Reprise -> nbTemps Apres Reprise
        final HashMap<Integer,Integer> mapRepetition = creerMapRepetition();

        //lancement de l'animation
        switcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        switcher.setFactory(this);

        runnable = new Runnable() {

            int tempsDebut = mapMesures.get(mesureDebut);
            int tempsMesure2 = mapMesures.get(mesureDebut+1);
            int decompteMax = tempsMesure2 - tempsDebut;
            int nbDecompte = 1;

            int numeroTemps = tempsDebut;
            int numeroTempsFin = mapMesures.get(mesureFin+1);

            HashMap<Integer,Integer> mapReprises = creerMapReprise();
            HashMap<Integer,Pair<Integer,Integer>> mapNonLues = creerMapNonLues();
            int numeroPassageReprise = 1;

            Bitmap bitmapCercle = null;
            Bitmap bitmapMesure = null;
            Bitmap bitmapNuance = null;
            Bitmap bitmapArmature = null;
            Bitmap bitmapAlerte = null;
            Bitmap bitmapRepetition = null;
            int numeroRepetition = 0;
            int prochaineFinReprise = -1;
            int passageDecompte = 0;

            @Override
            public void run() {

                if(passageDecompte<2){
                    //afficher le decompte
                    int temps = 60000/ mapCercle.get(-nbDecompte).getRight();
                    switcher.postDelayed(this, temps);

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mapCercle.get(-nbDecompte).getLeft());
                    if(nbDecompte == decompteMax){
                        nbDecompte = 1;
                        passageDecompte++;
                    }
                    else{
                        nbDecompte++;
                    }
                    BitmapDrawable draw = new BitmapDrawable(getResources(),bitmap);
                    switcher.setImageDrawable(draw);
                }
                else if(numeroTemps == numeroTempsFin){
                    //afficher la fin
                    int idFin = getResources().getIdentifier("fin","drawable",getPackageName());
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), idFin);
                    BitmapDrawable draw = new BitmapDrawable(getResources(),bitmap);
                    switcher.setImageDrawable(draw);
                    switcher.removeCallbacks(runnable);
                }
                else{

                    int temps = 0 ;

                    //Recuperation des differentes images pour le temps courant
                    if(mapCercle.containsKey(numeroTemps)){
                        bitmapCercle = BitmapFactory.decodeResource(getResources(), mapCercle.get(numeroTemps).getLeft());
                        temps = 60000/mapCercle.get(numeroTemps).getRight();
                    }

                    switcher.postDelayed(this, temps);

                    if(mapMesure.containsKey(numeroTemps)){
                        bitmapMesure = mapMesure.get(numeroTemps);
                    }
                    if(mapNuance.containsKey(numeroTemps)){
                        int idNuance = mapNuance.get(numeroTemps);
                        if(idNuance != -1){
                            bitmapNuance = BitmapFactory.decodeResource(getResources(), mapNuance.get(numeroTemps));
                        }
                        else{
                            bitmapNuance = null;
                        }
                    }
                    if(mapArmature.containsKey(numeroTemps)){
                        bitmapArmature = mapArmature.get(numeroTemps);
                    }
                    if(mapAlerte.containsKey(numeroTemps)){
                        bitmapAlerte = BitmapFactory.decodeResource(getResources(), mapAlerte.get(numeroTemps));
                    }
                    else{
                        bitmapAlerte = null;
                    }
                    //Gestion du cas des repetitions
                    if(mapRepetition.containsKey(numeroTemps) && numeroRepetition == 0){
                        numeroRepetition = 1;
                        int idImg1 = getResources().getIdentifier("passage1","drawable",getPackageName());
                        bitmapRepetition = BitmapFactory.decodeResource(getResources(), idImg1);
                        prochaineFinReprise = mapRepetition.get(numeroTemps);
                    }
                    else if(mapRepetition.containsKey(numeroTemps) && numeroRepetition == 1){
                        numeroRepetition = 2;
                        int idImg2 = getResources().getIdentifier("passage2","drawable",getPackageName());
                        bitmapRepetition = BitmapFactory.decodeResource(getResources(), idImg2);
                    }
                    //Effacer l'info de repetition
                    if(numeroTemps == prochaineFinReprise){
                        numeroRepetition = 0;
                        bitmapRepetition = null;
                    }

                    Bitmap bitmapFinal;
                    bitmapFinal = assemblerParties(bitmapCercle, bitmapNuance, bitmapMesure, bitmapArmature, bitmapAlerte, bitmapRepetition);

                    BitmapDrawable draw = new BitmapDrawable(getResources(),bitmapFinal);
                    switcher.setImageDrawable(draw);


                    //verifier les sauts liés aux reprises et mesures non lues

                    if(mapReprises.containsKey(numeroTemps)){
                        if(numeroPassageReprise == 1){
                            //saut de reprise
                            numeroTemps = mapReprises.get(numeroTemps);
                            numeroPassageReprise = 2;
                        }
                        else{
                            //on ne fait le saut qu'une fois
                            mapReprises.remove(numeroTemps);
                            numeroPassageReprise = 1;
                            numeroTemps++;
                        }
                    }
                    else if(mapNonLues.containsKey(numeroTemps)){
                        Pair<Integer,Integer> tempsEtPassage = mapNonLues.get(numeroTemps);
                        if(numeroPassageReprise == tempsEtPassage.getRight()){
                            //on effectue le saut
                            numeroTemps = tempsEtPassage.getLeft();
                            numeroPassageReprise = 1;
                        }
                        else{
                            //on est pas dans le bon passage donc on lit
                            numeroTemps++;
                        }
                    }
                    else{
                        numeroTemps++;
                    }


                }
            }
        };

        switcher.postDelayed(runnable,500);

    }

    private HashMap<Integer,Integer> creerMapMesureTemps(){
        HashMap<Integer,Integer> map = new HashMap<>();

        ArrayList<VariationTemps> listVariationTemps = bdd.getVariationsTemps(bdd.getMusique(idMusique));
        Collections.sort(listVariationTemps, new Comparator<VariationTemps>() {
            @Override
            public int compare(VariationTemps lhs, VariationTemps rhs) {
                return lhs.getMesure_debut() - rhs.getMesure_debut();
            }
        });

        int indexVarTemps=0;
        VariationTemps varTempsCourant = listVariationTemps.get(indexVarTemps);
        int nbTempsMesure = varTempsCourant.getTemps_par_mesure();

        int numTempsGlobal = 1;

        for(int mesure=1; mesure<=mesureFin+1; mesure++){
            map.put(mesure,numTempsGlobal);

            //mise a jour du nb de temps par mesure et avance dans les events
            if(mesure == varTempsCourant.getMesure_debut()){
                nbTempsMesure = varTempsCourant.getTemps_par_mesure();
                if(indexVarTemps+1 < listVariationTemps.size()){
                    indexVarTemps++;
                    varTempsCourant = listVariationTemps.get(indexVarTemps);
                }
            }

            //avance dans les temps de la partition
            numTempsGlobal += nbTempsMesure;
        }

        return map;
    }

    private HashMap<Integer,Integer> creerMapReprise(){
        //format : nbTemps -> nbTemps apres Jump
        HashMap<Integer,Integer> map = new HashMap<>();

        ArrayList<Reprise> reprises = bdd.getReprises(bdd.getMusique(idMusique));

        for(Reprise rep : reprises){
            int mesureDebut = rep.getMesure_debut();
            int mesureFin = rep.getMesure_fin();

            int premierTempsReprise = mapMesures.get(mesureDebut);
            int dernierTempsReprise = mapMesures.get(mesureFin+1)-1;

            map.put(dernierTempsReprise,premierTempsReprise);
        }
        return map;
    }

    private HashMap<Integer,Integer> creerMapRepetition(){
        //format : 1er de la reprise -> 1er temps apres la reprise
        HashMap<Integer,Integer> map = new HashMap<>();

        ArrayList<Reprise> reprises = bdd.getReprises(bdd.getMusique(idMusique));

        for(Reprise rep : reprises){
            int mesureDebut = rep.getMesure_debut();
            int mesureFin = rep.getMesure_fin();

            int premierTempsReprise = mapMesures.get(mesureDebut);
            int premierTempsApres = mapMesures.get(mesureFin+1);

            map.put(premierTempsReprise,premierTempsApres);
        }

        return map;
    }

    private HashMap<Integer,Pair<Integer,Integer>> creerMapNonLues(){
        //format : nbTemps -> (nbTemps apres Jump, passage reprise)
        HashMap<Integer,Pair<Integer,Integer>> map = new HashMap<>();

        ArrayList<MesuresNonLues> mesuresNonLues = bdd.getMesuresNonLues(bdd.getMusique(idMusique));

        for(MesuresNonLues mnl : mesuresNonLues){
            int mesureDebut = mnl.getMesure_debut();
            int mesureFin = mnl.getMesure_fin();
            int passage = mnl.getPassage_reprise();

            int premierTemps = mapMesures.get(mesureDebut);
            int tempsJump = mapMesures.get(mesureFin+1)-1;

            map.put(premierTemps, new Pair<>(tempsJump,passage));
        }

        return map;
    }

    private HashMap<Integer, Integer> creerMapNuance() {
        HashMap<Integer, Integer> map = new HashMap<>();
        ArrayList<VariationIntensite> variations = bdd.getVariationsIntensite(bdd.getMusique(idMusique));
        Collections.sort(variations, new Comparator<VariationIntensite>() {
            @Override
            public int compare(VariationIntensite lhs, VariationIntensite rhs) {
                int tempslhs = mapMesures.get(lhs.getMesureDebut())+lhs.getTempsDebut();
                int tempsrhs = mapMesures.get(rhs.getMesureDebut())+rhs.getTempsDebut();
                return tempslhs - tempsrhs;
            }
        });

        int tempsFin = mapMesures.get(mesureFin+1);

        int indexVar = 0;
        VariationIntensite varCourante = variations.get(indexVar);
        int tempsVar = mapMesures.get(varCourante.getMesureDebut()) + varCourante.getTempsDebut() - 1;
        int idImageCourante=-1;

        for(int temps=1; temps<tempsFin; temps++){

            if(temps==tempsVar){
                //maj de l'image courante
                int intensite = varCourante.getIntensite();
                idImageCourante = getResources().getIdentifier("intensite"+intensite,"drawable",getPackageName());

                //chargement de la prochaine variation
                if(indexVar+1 < variations.size()){
                    indexVar++;
                    varCourante = variations.get(indexVar);
                    tempsVar = mapMesures.get(varCourante.getMesureDebut()) + varCourante.getTempsDebut() - 1;
                }
            }

            map.put(temps,idImageCourante);
        }

        return map;
    }

    private HashMap<Integer, Bitmap> creerMapMesure() {
        //une image de mesure par temps de debut
        HashMap<Integer, Bitmap> map = new HashMap<>();

        for(int m=1; m<=mesureFin; m++){
            int tempsDebut = mapMesures.get(m);
            Bitmap imageM = creerBitmapMesure(m);
            map.put(tempsDebut, imageM);
        }
        return map;
    }

    private Bitmap creerBitmapMesure(int nMesure) {

        Bitmap bitmapMesure = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("mesureexemple","drawable",getPackageName()));

        Bitmap bitmap = Bitmap.createBitmap(bitmapMesure.getWidth(), bitmapMesure.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        int unite = nMesure%10;
        nMesure=nMesure/10;
        int dizaine = nMesure%10;
        nMesure=nMesure/10;
        int centaine = nMesure%10;

        Bitmap bitunite = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("mesure" + unite, "drawable", getPackageName()));
        Bitmap bitdizaine = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("mesure"+dizaine, "drawable", getPackageName()));
        Bitmap bitcentaine = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("mesure"+centaine, "drawable", getPackageName()));

        canvas.drawBitmap(bitunite,bitmap.getWidth()*2/3,0,null);
        canvas.drawBitmap(bitdizaine,bitmap.getWidth()/3,0,null);
        canvas.drawBitmap(bitcentaine,0,0,null);

        return bitmap;
    }

    private HashMap<Integer,Bitmap> creerMapArmature() {
        HashMap<Integer, Bitmap> map = new HashMap<>();
        ArrayList<Armature> listeVariations = bdd.getArmature(bdd.getMusique(idMusique));
        Collections.sort(listeVariations, new Comparator<Armature>() {
            @Override
            public int compare(Armature lhs, Armature rhs) {
                return lhs.getMesure_debut() - rhs.getMesure_debut();
            }
        });

        if(listeVariations.size() > 0) {

            int indexVar = 0;
            Armature varCourante = listeVariations.get(indexVar);
            int tempsVar = mapMesures.get(varCourante.getMesure_debut()) + varCourante.getTemps_debut() - 1;

            Bitmap imageCourante = null;

            int tempsFin = mapMesures.get(mesureFin + 1);
            for (int temps = 1; temps < tempsFin; temps++) {

                if (temps == tempsVar) {
                    //maj de l'image

                    imageCourante = null;
                    if (varCourante.getAlteration() != 0) {
                        imageCourante = creerBitmapArmature(varCourante.getAlteration());
                    }

                    //chargement de la prochaine variation
                    if (indexVar + 1 < listeVariations.size()) {
                        indexVar++;
                        varCourante = listeVariations.get(indexVar);
                        tempsVar = mapMesures.get(varCourante.getMesure_debut());
                    }
                }

                map.put(temps, imageCourante);
            }
        }
            return map;

    }

    private Bitmap creerBitmapArmature(int alteration) {
        Bitmap bitmapArmature = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("armatureexemple","drawable",getPackageName()));

        Bitmap bitmap = Bitmap.createBitmap(bitmapArmature.getWidth(), bitmapArmature.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Bitmap armature;
        if(alteration < 0){
            //Bemol
            armature = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("bemol","drawable",getPackageName()));
            alteration = 0 - alteration;
        }
        else{
            //Diese
            armature = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("diese","drawable",getPackageName()));
        }

        Bitmap nbAlt = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("armature" + alteration,"drawable",getPackageName()));

        canvas.drawBitmap(nbAlt,0,0,null);
        canvas.drawBitmap(armature,bitmap.getWidth()/2,0,null);

        return bitmap;
    }

    private HashMap<Integer,Integer> creerMapAlerte(){
        HashMap<Integer, Integer> map = new HashMap<>();

        ArrayList<Alertes> alertes = bdd.getAlertes(bdd.getMusique(idMusique));
        for (Alertes al : alertes) {
            int mesure = al.getMesure_debut();
            int tempsMesure = mapMesures.get(mesure);
            int tpsDebut = al.getTemps_debut();
            int i = al.getCouleur();
            int idImage = -1;
            if(i != -1) {
                idImage = getResources().getIdentifier("alerte" + i, "drawable", getPackageName());
            }
            map.put(tempsMesure+tpsDebut-1,idImage);
        }
        return map;
    }

    private HashMap<Integer,Pair<Integer,Integer>> creerMapCercle(){
        // format : nbTemps -> (idImage,tempo)
        HashMap<Integer,Pair<Integer,Integer>> map = new HashMap<>();

        ArrayList<VariationTemps> variationTemps = bdd.getVariationsTemps(bdd.getMusique(idMusique));
        Collections.sort(variationTemps, new Comparator<VariationTemps>() {
            @Override
            public int compare(VariationTemps lhs, VariationTemps rhs) {
                return lhs.getMesure_debut() - rhs.getMesure_debut();
            }
        });

        int indexVarTemps = 0;
        VariationTemps varTemps = variationTemps.get(indexVarTemps);
        int prochainChangement = varTemps.getMesure_debut();
        int nbTemps = varTemps.getTemps_par_mesure();
        int tempo = varTemps.getTempo();

        int numTempsGlobal = 1;
        int tempoMesureDebut = tempo;

        for(int mesure=1; mesure<=mesureFin; mesure++){
            if(mesure == prochainChangement){
                nbTemps = varTemps.getTemps_par_mesure();
                tempo = varTemps.getTempo();

                if(indexVarTemps+1 < variationTemps.size()){
                    indexVarTemps++;
                    varTemps = variationTemps.get(indexVarTemps);
                    prochainChangement = varTemps.getMesure_debut();
                }
            }

            for(int temps = 1; temps <= nbTemps; temps++){
                //recup les images de cercle pour chaque temps et les ajouter a la map
                int idImage = getResources().getIdentifier("a"+nbTemps+"_"+temps, "drawable", getPackageName());
                map.put(numTempsGlobal, new Pair<>(idImage,tempo));

                numTempsGlobal++;
            }

            if(mesure == mesureDebut){
                tempoMesureDebut = tempo;
            }
        }

        //ajouter les images de decompte et de fin
        for(int decompte=1; decompte <= 8; decompte++){
            int idDecompte = getResources().getIdentifier("d"+decompte, "drawable", getPackageName());
            map.put(-decompte, new Pair<>(idDecompte, tempoMesureDebut));
        }

        int idFin = getResources().getIdentifier("fin", "drawable", getPackageName());
        map.put(0, new Pair<>(idFin,1));

        return map;
    }


    private Bitmap assemblerParties(Bitmap cercle, Bitmap nuance, Bitmap mesure, Bitmap armature, Bitmap alerte, Bitmap repetition){
        //cercle n'est pas null

        Bitmap bitmap = Bitmap.createBitmap(cercle.getWidth(), cercle.getHeight(), cercle.getConfig());
        Canvas canvas = new Canvas(bitmap);

        canvas.drawBitmap(cercle,new Matrix(),null);
        //taille 128x64

        if(nuance != null){
            canvas.drawBitmap(nuance,(cercle.getWidth()/4),(cercle.getHeight()/4),null);
            //taille 32x32
        }
        if(repetition != null){
            canvas.drawBitmap(repetition,0,(3*cercle.getHeight()/4),null);
            //taille 16x16
        }
        if(mesure != null){
            canvas.drawBitmap(mesure,(5*cercle.getWidth()/8),0,null);
            //taille 48x16
        }
        if(armature != null){
            canvas.drawBitmap(armature,(6*cercle.getWidth()/8),(cercle.getHeight()/2),null);
            //taille 32x16
        }
        if(alerte != null){
            canvas.drawBitmap(alerte,(5*cercle.getWidth()/8),(3*cercle.getHeight()/4),null);
            //taille 16x16
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