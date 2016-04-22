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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    ArrayList<Pair<String,Integer>> listeImages;

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


        //mapCercle
        // format : nbTemps -> (idImage,tempsAffichage)
        //maps pour les autres informations
        // format : nbTemps -> idImage ou image

        final HashMap<Integer,Pair<Integer,Integer>> mapCercle = creerMapCercle2();
        final HashMap<Integer,Bitmap> mapMesure = creerMapMesure();
        final HashMap<Integer,Integer> mapNuance = creerMapNuance();
        final HashMap<Integer,Integer> mapSection = creerMapSection();
        final HashMap<Integer,Integer> mapRepetition = creerMapRepetition();
        final HashMap<Integer,Bitmap> mapArmature = creerMapArmature();
        final HashMap<Integer,Integer> mapSignature = creerMapSignature();
        final HashMap<Integer,Integer> mapAlerte = creerMapAlerte();

        //lancement de l'animation

        switcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        switcher.setFactory(this);

        int tempsDebut = mapMesures.get(mesureDebut);
        Bitmap bitmapNuance = null;
        Bitmap bitmapArmature = null;
        Bitmap bitmapAlerte = null;

        //initialisation de la nuance. On recupere la derniere image de nuance avant le temps de debut
        //idem pour l'armature et les alertes
        for(int numeroTemps=1; numeroTemps<tempsDebut; numeroTemps++){
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
                int idAlerte = mapAlerte.get(numeroTemps);
                if(idAlerte != -1){
                    bitmapAlerte = BitmapFactory.decodeResource(getResources(), mapAlerte.get(numeroTemps));
                }
                else{
                    bitmapAlerte = null;
                }
            }
        }

        runnable = new Runnable() {

            int tempsDebut = mapMesures.get(mesureDebut);
            int tempsMesure2 = (mapMesures.containsKey(mesureDebut+1)) ? mapMesures.get(mesureDebut+1) : listeImages.size()+1;
            int nbDecompte = tempsMesure2 - tempsDebut;

            int numeroTemps = tempsDebut;
            int numeroTempsFin = (mapMesures.containsKey(mesureFin+1)) ? mapMesures.get(mesureFin+1) : listeImages.size()+1;

            HashMap<Integer,Integer> mapReprises = creerMapReprise();
            HashMap<Integer,Pair<Integer,Integer>> mapNonLues = creerMapNonLues();
            int numeroPassageReprise = 1;

            Bitmap bitmapCercle = null;
            Bitmap bitmapMesure = null;
            Bitmap bitmapNuance = null;
            Bitmap bitmapArmature = null;
            Bitmap bitmapAlerte = null;

            public Runnable init(Bitmap bitNuanceInit, Bitmap bitArmature, Bitmap bitAlerte){
                bitmapNuance = bitNuanceInit;
                bitmapArmature = bitArmature;
                bitmapAlerte = bitAlerte;
                return this;

            }

            @Override
            public void run() {

                if(nbDecompte > 0){

                    //afficher le decompte
                    int temps = 60000/ mapCercle.get(-nbDecompte).getRight();
                    switcher.postDelayed(this, temps);

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mapCercle.get(-nbDecompte).getLeft());
                    nbDecompte--;
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
                    Bitmap bitmapSignature = (mapSignature.containsKey(numeroTemps)) ? BitmapFactory.decodeResource(getResources(), mapSignature.get(numeroTemps)) : null;
                    Bitmap bitmapRepetition = (mapRepetition.containsKey(numeroTemps)) ? BitmapFactory.decodeResource(getResources(), mapRepetition.get(numeroTemps)) : null;
                    Bitmap bitmapSection = (mapSection.containsKey(numeroTemps)) ? BitmapFactory.decodeResource(getResources(), mapSection.get(numeroTemps)) : null;
                    Bitmap bitmapFinal;
                    bitmapFinal = assemblerParties(bitmapCercle, bitmapNuance, bitmapSignature, bitmapRepetition, bitmapMesure, bitmapSection, bitmapArmature, bitmapAlerte);

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
        }.init(bitmapNuance,bitmapArmature,bitmapAlerte);//initialisation avec les images de départ récupérées avant le premier temps

        switcher.postDelayed(runnable,500);

    }

    private HashMap<Integer,Integer> creerMapReprise(){
        //format : nbTemps -> nbTemps apres Jump
        HashMap<Integer,Integer> map = new HashMap<>();

        ArrayList<Reprise> reprises = bdd.getReprises(bdd.getMusique(idMusique));

        for(Reprise rep : reprises){
            int mesureDebut = rep.getMesure_debut();
            int mesureFin = rep.getMesure_fin();

            int premierTempsReprise = mapMesures.get(mesureDebut);
            int dernierTempsReprise;
            if(mapMesures.containsKey(mesureFin+1)){
                dernierTempsReprise = mapMesures.get(mesureFin+1)-1;
            }
            else{
                dernierTempsReprise = listeImages.size()-1;
            }

            map.put(dernierTempsReprise,premierTempsReprise);
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

            int premierTemps = mapMesures.get(mesureDebut)-1;
            int tempsJump;
            if(mapMesures.containsKey(mesureFin+1)){
                tempsJump = mapMesures.get(mesureFin+1);
            }
            else{
                tempsJump = listeImages.size();
            }

            map.put(premierTemps, new Pair(tempsJump,passage));
        }

        return map;
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
            int tpsDebut = var.getTempsDebut();
            int i = var.getIntensite();
            int idImage = -1;
            if(i != -1) {
                idImage = getResources().getIdentifier("intensite" + i, "drawable", getPackageName());
            }
            map.put(tempsMesure+tpsDebut-1,idImage);
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

    private HashMap<Integer,Bitmap> creerMapArmature() {
        HashMap<Integer,Bitmap> mapArmature = new HashMap<>();

        ArrayList<Armature> listeArmature = bdd.getArmature(bdd.getMusique(idMusique));

        for(Armature arm : listeArmature){
            int tpsDebut = arm.getTemps_debut();
            int alteration = arm.getAlteration();
            int mesure = arm.getMesure_debut();

            int tpsMesure = mapMesures.get(mesure);
            //creer l'image
            Bitmap image = null;
            if(alteration != 0){
                image = creerBitmapArmature(alteration);
            }

            mapArmature.put(tpsMesure+tpsDebut-1, image);
        }


        return mapArmature;
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

        Bitmap nbAlt = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("mesure" + alteration,"drawable",getPackageName()));

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

    private HashMap<Integer,Pair<Integer,Integer>> creerMapCercle(ArrayList<Pair<String, Integer>> listelecture, HashMap<String, Integer> banqueImages){
        // format : nbTemps -> (idImage,tempsAffichage)
        HashMap<Integer,Pair<Integer,Integer>> map = new HashMap<>();

        for(int index=0; index<listelecture.size(); index++){
            String key = listelecture.get(index).getLeft();
            int temps = listelecture.get(index).getRight();
            int id = banqueImages.get(key);

            map.put(index,new Pair<>(id,temps));
        }
        return map;
    }

    private HashMap<Integer,Pair<Integer,Integer>> creerMapCercle2(){
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
                map.put(numTempsGlobal, new Pair(idImage,tempo));

                numTempsGlobal++;
            }

            if(mesure == mesureDebut){
                tempoMesureDebut = tempo;
            }
        }

        //ajouter les images de decompte et de fin
        for(int decompte=1; decompte <= 8; decompte++){
            int idDecompte = getResources().getIdentifier("d"+decompte, "drawable", getPackageName());
            map.put(-decompte, new Pair(idDecompte, tempoMesureDebut));
        }

        int idFin = getResources().getIdentifier("fin", "drawable", getPackageName());
        map.put(0, new Pair(idFin,1));

        return map;
    }


    private Bitmap assemblerParties(Bitmap cercle, Bitmap nuance, Bitmap signature, Bitmap repetition, Bitmap mesure, Bitmap section, Bitmap armature, Bitmap alerte){
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

    private ArrayList<Integer> getOrdreLectureMesures(){
        //TODO A Tester avec reprises et mesures non lues
        //Donne l'ordre de lecture en prenant en compte les reprises et les mesures non lues
        //Considere que les reprises et les parties non lues sont correctement créées et ne s'entrelacent pas

        //Recuperation des reprises et des mesures non lues de la musique
        ArrayList<Reprise> reprises = bdd.getReprises(bdd.getMusique(idMusique));
        Collections.sort(reprises, new Comparator<Reprise>() {
            @Override
            public int compare(Reprise lhs, Reprise rhs) {
                return lhs.getMesure_debut() - rhs.getMesure_debut();
            }
        });
        ArrayList<MesuresNonLues> mesuresNonLues = bdd.getMesuresNonLues(bdd.getMusique(idMusique));
        Collections.sort(mesuresNonLues, new Comparator<MesuresNonLues>() {
            @Override
            public int compare(MesuresNonLues lhs, MesuresNonLues rhs) {
                return lhs.getMesure_debut() - rhs.getMesure_debut();
            }
        });

        int indexReprise = 0;
        int indexMesureNL = 0;
        Reprise prochaineReprise = reprises.get(indexReprise);
        MesuresNonLues prochaineMesuresNL = mesuresNonLues.get(indexMesureNL);
        int repetitionCourante = 1;
        boolean enregistrer = true;

        ArrayList<Integer> ordreLecture = new ArrayList<>();
        int m=mesureDebut;
        while(m<=mesureFin){

            //Si la mesure est au debut d'une partie non lue
            if(prochaineMesuresNL.getMesure_debut() == m &&
                    prochaineMesuresNL.getPassage_reprise() == repetitionCourante){
                //on saute les mesures non lues
                m = prochaineMesuresNL.getMesure_fin();
                enregistrer = false;
            }

            if(enregistrer){
                //On lit la mesure m
                ordreLecture.add(m);
            }
            enregistrer = true;

            //Si la mesure est une fin de reprise
            if(prochaineReprise.getMesure_fin() == m){
                if(repetitionCourante == 1){
                    //premier passage dans la reprise

                    //retourne au debut de la reprise
                    m = prochaineReprise.getMesure_debut();
                    repetitionCourante = 2;
                }
                else {
                    //on est passé deux fois dans la reprise, on passe a la suite
                    m++;
                    repetitionCourante = 1;
                    if (indexReprise + 1 < reprises.size()) {
                        indexReprise++;
                        prochaineReprise = reprises.get(indexReprise);
                    }
                }
            }
            else{
                m++;
            }

        }
        return ordreLecture;
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