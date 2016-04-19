package com.example.boris.emaestro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import BDD.db.DataBaseManager;
import BDD.to.MesuresNonLues;
import BDD.to.Reprise;
import BDD.to.Armature;
import BDD.to.Evenement;
import BDD.to.VariationIntensite;
import BDD.to.VariationTemps;
import util.Nuance;

public class EditionActivity  extends Activity {

    DataBaseManager bdd = new DataBaseManager(this);

    String EXTRA_NOMPARTITION = "vide";
    String EXTRA_NBMESURE = "nbMesure";
    String EXTRA_PULSATION = "pulsation";
    String EXTRA_UNITE = "unite";
    String EXTRA_TPSPARMESURE = "nbTpsMesure";
    String EXTRA_ID_PARTITION = "idMusique";
    String EXTRA_NEW_PARTITION = "new";
    int idMusique;

    List<VariationTemps> varTempsList;
    List<VariationIntensite> varIntensiteList;
    List<Armature> varArmatureList;


    // view du menu
    LinearLayout menu;
    //grilleMesure
    MesureAdapter adapter;
    GridView mGridView;
    Partition partition;
    Context context;


    //modif nuance
    Spinner mNuanceSpinner;
    Spinner mNuanceDebutSpinner;
    ArrayAdapter<String> dataAdapterNuance;
    String nuance;
    List<VariationIntensite> chevaucheNuanceList;
    int tpsDebut;
    //spinner chgt tempsMesure Nuance
    List<String> tempsMesure = new ArrayList<>();
    ArrayAdapter<String> dataAdapterNuanceTpsMesure;




    static ListView eventNuanceListView;
    EventNuanceAdapter adapterEventNuance;

    List<VariationIntensite> varIntensiteListSurMesureCour;
    List<Armature> varArmatureListSurMesureCour;

    //debug
    TextView debug;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edition_mesures);
        context = EditionActivity.this;
        bdd.open();

        //Debug
        debug = (TextView) findViewById(R.id.debug);

        //nuance selection
        List<String> nuanceList = new ArrayList<String>();
        nuanceList.add("fortississimo");
        nuanceList.add("fortissimo");
        nuanceList.add("forte");
        nuanceList.add("mezzo forte");
        nuanceList.add("mezzo piano");
        nuanceList.add("piano");
        nuanceList.add("pianissimo");
        nuanceList.add("pianississimo");


        //Variables d'edition
        varIntensiteList = new ArrayList<>();
        varTempsList = new ArrayList<>();
        varArmatureList = new ArrayList<>();

        dataAdapterNuance = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nuanceList);
        dataAdapterNuance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //gestion grille mesure
        GridLayout mesuresGrid = (GridLayout) findViewById(R.id.mesures);
        mGridView = (GridView) mesuresGrid.findViewById(R.id.gridView);
        Intent intent = getIntent();

        if (intent != null) {

            EXTRA_NOMPARTITION = intent.getStringExtra(EXTRA_NOMPARTITION);
            EXTRA_NBMESURE = intent.getStringExtra(EXTRA_NBMESURE);
            EXTRA_PULSATION = intent.getStringExtra(EXTRA_PULSATION);
            EXTRA_UNITE = intent.getStringExtra(EXTRA_UNITE);
            EXTRA_TPSPARMESURE = intent.getStringExtra(EXTRA_TPSPARMESURE);
            EXTRA_ID_PARTITION = intent.getStringExtra(EXTRA_ID_PARTITION);
            EXTRA_NEW_PARTITION = intent.getStringExtra(EXTRA_NEW_PARTITION);

            idMusique = Integer.parseInt(EXTRA_ID_PARTITION);
            partition = new Partition(EXTRA_NBMESURE);

            if(EXTRA_NEW_PARTITION.equals("true")){
                int id = bdd.getMusique(EXTRA_NOMPARTITION).getId();
                bdd.save(new VariationIntensite(id,-1,0,1,0));
                bdd.save(new VariationTemps(id, 1, Integer.parseInt(EXTRA_TPSPARMESURE), Integer.parseInt(EXTRA_PULSATION), Integer.valueOf(EXTRA_UNITE)));//TODO : Gerer l'unite pulsation

            }

        }else{

            debug.setText("Erreur interne");

        }

        //on recupère les données associées à la musique
        varIntensiteList = bdd.getVariationsIntensite(bdd.getMusique(EXTRA_NOMPARTITION));
        varTempsList = bdd.getVariationsTemps(bdd.getMusique(EXTRA_NOMPARTITION));
        varArmatureList = bdd.getArmature(bdd.getMusique(EXTRA_NOMPARTITION));

        //On trie nos listes en ordre croissant d'id de mesure
        triListVarIntensite();
        triListVarTemps();
        triListVarArmature();


        //-----------------------
        //debug msg
        //-----------------------
        for(int i = 0; i<varIntensiteList.size();i++){
            debug.setText(debug.getText().toString() + varIntensiteList.size() + "\n nouveau event debut à :" + (varIntensiteList.get(i).getMesureDebut() ) + " nuance : " + partition.ConvertNuanceFromInt(varIntensiteList.get(i).getIntensite()));
        }
        for(int i = 0; i<varTempsList.size();i++){
            debug.setText(debug.getText().toString() + varTempsList.size() + "\n nouveau event debut à :" + (varTempsList.get(i).getMesure_debut() ) + " tempo : " + varTempsList.get(i).getTempo() + " nb temps " +varTempsList.get(i).getTemps_par_mesure());
        }

        for(int i = 0; i<varArmatureList.size();i++){
            debug.setText(debug.getText().toString() + varArmatureList.size() + "\n nouveau event debut à :" + (varArmatureList.get(i).getMesure_debut() ) + " alteration : " + varArmatureList.get(i).getAlteration());
        }
        //-----------------------
        //debug msg
        //-----------------------

        //on met ajour tempo et intensite
        partition.setTempo(varTempsList);
        partition.setNuance(varIntensiteList);
        partition.setArmature(varArmatureList);


        adapter = new MesureAdapter(EditionActivity.this, partition);
        mGridView.setAdapter(adapter);

        dataAdapterNuanceTpsMesure = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tempsMesure);
        dataAdapterNuanceTpsMesure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                final Mesure m = partition.getMesure(position);
                final int oldTempo = m.getTempo();
                final int  oldTempsMesure = m.getTempsMesure();
                final String oldUnite = m.getUnite();


                AlertDialog.Builder popup = new AlertDialog.Builder(context);

                popup.setTitle("Informations de la mesure " + (position + 1));
                LayoutInflater inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_liste_event, null);
                popup.setView(popupView);



                //affiche les event de nuance present sur la mesure
                varIntensiteListSurMesureCour = eventsNuanceDeLaMesure(m.getId());
                eventNuanceListView = (ListView) popupView.findViewById(R.id.listEventNuance);
                adapterEventNuance = new EventNuanceAdapter(context,varIntensiteListSurMesureCour);
                eventNuanceListView.setAdapter(adapterEventNuance);


                Button newEvent = (Button) popupView.findViewById(R.id.newEvent);
                newEvent.setOnClickListener(new OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    AlertDialog.Builder popup = new AlertDialog.Builder(context);
                                                    popup.setTitle("Choix de l'evenement");
                                                    LayoutInflater inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
                                                    View popupView = inflater.inflate(R.layout.popup_choix_event, null);

                                                    Button newNuance = (Button) popupView.findViewById(R.id.newNuance);
                                                    newNuance.setOnClickListener(new OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            AlertDialog.Builder popup = new AlertDialog.Builder(context);
                                                            popup.setTitle("Evenement Nuance");
                                                            LayoutInflater inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
                                                            final View popupView = inflater.inflate(R.layout.edition_nuance, null);
                                                            popup.setView(popupView);

                                                            //nuance
                                                            final Spinner spinnerModifNuance = (Spinner) popupView.findViewById(R.id.spinnerModifNuance);
                                                            final Spinner spinnerModifNuanceTpsDebut = (Spinner) popupView.findViewById(R.id.spinnerModifTempsDebut);
                                                            String[] nuancesTab = Nuance.getAllNuances();
                                                            List<String> nuances = Arrays.asList(nuancesTab);
                                                            ArrayAdapter<String> adapterModifNuance = new ArrayAdapter<>(popupView.getContext(),android.R.layout.simple_spinner_item, nuances);
                                                            adapterModifNuance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                            spinnerModifNuance.setAdapter(adapterModifNuance);
                                                            spinnerModifNuance.setSelection(nuances.indexOf(m.getNuance()));
                                                            //nuance tps debut
                                                            List<String>nbTempsMesure = new ArrayList<>();
                                                            for(int i=1; i<=m.getTempsMesure();i++){
                                                                nbTempsMesure.add(i+"");
                                                            }
                                                            ArrayAdapter<String> adapterModifNuanceTpsDebut = new ArrayAdapter<>(popupView.getContext(),android.R.layout.simple_spinner_item, nbTempsMesure);
                                                            adapterModifNuanceTpsDebut.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                            spinnerModifNuanceTpsDebut.setAdapter(adapterModifNuanceTpsDebut);
                                                            spinnerModifNuanceTpsDebut.setSelection(0);

                                                            popup.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    // on ne modifie rien
                                                                }
                                                            });
                                                            popup.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    String nouvNuance = spinnerModifNuance.getSelectedItem().toString();
                                                                    int newNuance = partition.convertNuanceToInt(Nuance.convertStringToNuance(nouvNuance));
                                                                    int newTempsDebut =Integer.parseInt(spinnerModifNuanceTpsDebut.getSelectedItem().toString());
                                                                    List<VariationIntensite> eventPresents = eventsNuanceDeLaMesure(m.getId());
                                                                        VariationIntensite eventCour=new VariationIntensite();
                                                                        boolean eventDejaPresent=false;
                                                                        for(int i =0; i< eventPresents.size() && !eventDejaPresent;i++){
                                                                            eventCour = eventPresents.get(i);
                                                                            if(eventCour.getTempsDebut()==newTempsDebut){
                                                                                eventDejaPresent = true;
                                                                            }
                                                                        }

                                                                        if(eventDejaPresent){
                                                                            eventCour.setIntensite(newNuance);
                                                                            eventCour.setTempsDebut(newTempsDebut);
                                                                            bdd.update(eventCour);
                                                                        }else{
                                                                        eventCour = new VariationIntensite(bdd.getMusique(EXTRA_NOMPARTITION).getId(),newNuance,newTempsDebut,m.getId(),0);
                                                                        bdd.save(eventCour);
                                                                    }
                                                                    varIntensiteList = bdd.getVariationsIntensite(bdd.getMusique(idMusique));
                                                                    triListVarIntensite();
                                                                    partition.setNuance(varIntensiteList);
                                                                    //MAJ affichage
                                                                    adapter = new MesureAdapter(EditionActivity.this, partition);
                                                                    mGridView.setAdapter(adapter);
                                                                    //maj liste events
                                                                    varIntensiteListSurMesureCour = eventsNuanceDeLaMesure(m.getId());
                                                                    adapterEventNuance = new EventNuanceAdapter(context,varIntensiteListSurMesureCour);
                                                                    eventNuanceListView.setAdapter(adapterEventNuance);

                                                                }
                                                            });
                                                            popup.show();

                                                        }
                                                    });

                                                    Button newReprise = (Button) popupView.findViewById(R.id.newReprise);
                                                    newReprise.setOnClickListener(new OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            AlertDialog.Builder popup = new AlertDialog.Builder(context);
                                                            popup.setTitle("Evenement Reprise");
                                                            LayoutInflater inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
                                                            View popupView = inflater.inflate(R.layout.edition_reprise, null);
                                                            popup.setView(popupView);


                                                            //Gestion des elements du popup
                                                            final TextView textMesureDebutRepet = (TextView) popupView.findViewById(R.id.textMesureDebutRepet);
                                                            final EditText textMesureFinRepet = (EditText) popupView.findViewById(R.id.textMesureFinRepet);
                                                            final EditText textMesureDebut2eRepet = (EditText) popupView.findViewById(R.id.textMesureDebut2eRepet);
                                                            final TextView textMesureFin2eRepet = (TextView) popupView.findViewById(R.id.textMesureFin2eRepet);

                                                            //Modif la fin de la partie non lue lors de la 2e repet en meme temps que la fin de la repet car elles doivent etre egales
                                                            textMesureFinRepet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                                @Override
                                                                public void onFocusChange(View v, boolean hasFocus) {
                                                                    textMesureFin2eRepet.setText(textMesureFinRepet.getText());
                                                                }
                                                            });

                                                            //le debut de la repetition est à la mesure sélectionnée
                                                            textMesureDebutRepet.setText("" + m.getId());
                                                            textMesureFinRepet.setText("" + m.getId());
                                                            textMesureDebut2eRepet.setText("" + m.getId());
                                                            textMesureFin2eRepet.setText("" + m.getId());

                                                            //Gestion des boutons du popup Reprise
                                                            popup.setNegativeButton("Annuler", null);
                                                            popup.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    //TODO Enregistrement d'un event reprise (à tester)
                                                                    //TODO gérer la liste des events de reprise (affichage, edition, suppression)
                                                                    //Lors de l'utilisation de l'appli, les mesures non lues lors du second passage seront en fait une plage de mesures non lues avec passage_reprise à 2
                                                                    int mesureDebut = Integer.parseInt(textMesureDebutRepet.getText().toString());
                                                                    int mesureFin = Integer.parseInt(textMesureFinRepet.getText().toString());
                                                                    int mesureDebutNonLu = Integer.parseInt(textMesureDebut2eRepet.getText().toString());
                                                                    int mesureFinNonLu = Integer.parseInt(textMesureFin2eRepet.getText().toString());

                                                                    //Conditions d'acceptation d'un event Reprise
                                                                    if(mesureDebut < mesureDebutNonLu && mesureDebutNonLu <= mesureFin) {

                                                                        Reprise eventUpdate = null;
                                                                        ArrayList<Reprise> eventsReprise = bdd.getReprises(bdd.getMusique(EXTRA_NOMPARTITION));
                                                                        for(Reprise rep : eventsReprise){
                                                                            if(rep.getMesure_debut() == mesureDebut){
                                                                                eventUpdate = rep;
                                                                            }
                                                                        }
                                                                        if(eventUpdate != null){
                                                                            ArrayList<MesuresNonLues> eventsMesures = bdd.getMesuresNonLues(bdd.getMusique(EXTRA_NOMPARTITION));
                                                                            MesuresNonLues eventMesureUpdate = null;
                                                                            for(MesuresNonLues eventM : eventsMesures){
                                                                                //FIXME Pas sur que ce soit le bon event qu'on récup. Ajouter un id reprise dans mesuresNonLues ?
                                                                                if(eventM.getMesure_debut() == mesureDebutNonLu){
                                                                                    eventMesureUpdate = eventM;
                                                                                }
                                                                            }
                                                                            eventUpdate.setMesure_fin(mesureFin);
                                                                            bdd.update(eventUpdate);
                                                                            eventMesureUpdate.setMesure_fin(mesureFinNonLu);
                                                                            eventMesureUpdate.setPassage_reprise(2);
                                                                            bdd.update(eventMesureUpdate);
                                                                        }else{
                                                                            Reprise nouvelEvent = new Reprise(bdd.getMusique(EXTRA_NOMPARTITION).getId(), mesureDebut, mesureFin);
                                                                            bdd.save(nouvelEvent);
                                                                            MesuresNonLues eventNonLues = new MesuresNonLues(bdd.getMusique(EXTRA_NOMPARTITION).getId(),mesureDebutNonLu, mesureFinNonLu, 2);
                                                                            bdd.save(eventNonLues);
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                            popup.show();

                                                        }
                                                    });

                                                    Button newArmature = (Button) popupView.findViewById(R.id.newArmature);
                                                    newArmature.setOnClickListener(new OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            AlertDialog.Builder popup = new AlertDialog.Builder(context);
                                                            popup.setTitle("Evenement Armature");
                                                            LayoutInflater inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
                                                            View popupView = inflater.inflate(R.layout.edition_armature, null);
                                                            //Gestion des elements du popup
                                                            //Spinner du choix du type d'alteration
                                                            final Spinner spinnerAlteration = (Spinner) popupView.findViewById(R.id.spinnerChoixAlteration);
                                                            String[] alterations = {"Bémol","Dièse"};
                                                            ArrayAdapter<String> adapterAlteration = new ArrayAdapter<String>(popupView.getContext(),android.R.layout.simple_spinner_item, alterations);
                                                            adapterAlteration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                            spinnerAlteration.setAdapter(adapterAlteration);

                                                            //Spinner du choix du nombre d'alterations
                                                            final Spinner spinnerNbAlt = (Spinner) popupView.findViewById(R.id.spinnerNombreAlteration);
                                                            Integer[] nombreAlt = {0,1,2,3,4,5,6,7};
                                                            ArrayAdapter<Integer> adapterNbAlt = new ArrayAdapter<Integer>(popupView.getContext(),android.R.layout.simple_spinner_item,nombreAlt);
                                                            adapterNbAlt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                            spinnerNbAlt.setAdapter(adapterNbAlt);
                                                            popup.setView(popupView);
                                                            popup.setNegativeButton("Annuler",null);
                                                            popup.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    //alteration : (-7 à -1 pour 7 à 1 bémols, 0 pour non affichage, 1 à 7 pour 1 à 7 dièses)
                                                                    int nouvArmature = Integer.parseInt(spinnerNbAlt.getSelectedItem().toString());
                                                                    String signeArmature = spinnerAlteration.getSelectedItem().toString();
                                                                    if(signeArmature == "Bémol"){
                                                                        nouvArmature=nouvArmature*(-1);
                                                                    }

                                                                    List<Armature> eventPresents = eventsArmatureDeLaMesure(m.getId());
                                                                    Armature eventCour=new Armature();
                                                                    boolean eventDejaPresent=false;
                                                                    for(int i =0; i< eventPresents.size() && !eventDejaPresent;i++){
                                                                        eventCour = eventPresents.get(i);
                                                                        if(eventCour.getMesure_debut()==m.getId()){
                                                                            eventDejaPresent = true;
                                                                        }
                                                                    }

                                                                    if(eventDejaPresent){
                                                                        eventCour.setAlteration(nouvArmature);
                                                                        //TODO temps debut
                                                                        bdd.update(eventCour);
                                                                    }else{
                                                                        eventCour = new Armature(bdd.getMusique(EXTRA_NOMPARTITION).getId(),m.getId(),1,nouvArmature,1);//TODO passage reprise et temps debut
                                                                        bdd.save(eventCour);
                                                                    }
                                                                    varArmatureList = bdd.getArmature(bdd.getMusique(idMusique));
                                                                    triListVarArmature();
                                                                    partition.setArmature(varArmatureList);
                                                                    //MAJ affichage
                                                                    adapter = new MesureAdapter(EditionActivity.this, partition);
                                                                    mGridView.setAdapter(adapter);
                                                                    //maj liste events
                                                                    varArmatureListSurMesureCour = eventsArmatureDeLaMesure(m.getId());
                                                                    //TODO faire pour les aramture
                                                                    //adapterEventNuance = new EventNuanceAdapter(context,varIntensiteListSurMesureCour);
                                                                    //eventNuanceListView.setAdapter(adapterEventNuance);
                                                                }
                                                            });
                                                            popup.show();



                                                        }
                                                    });

                                                    popup.setView(popupView);
                                                    popup.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                        }
                                                    });
                                                    popup.show();



                                                }
                                            }
                );


                //tempo
                final TextView textModifTempo = (TextView) popupView.findViewById(R.id.textModifTempo);
                textModifTempo.setText("" + oldTempo);


                //nb de temps
                final Spinner spinnerModifNbTemps = (Spinner) popupView.findViewById(R.id.spinnerModifNbTemps);
                List<Integer> tpsMesure = new ArrayList<Integer>();
                for(int i=2;i<=8;i++){ tpsMesure.add(i); }
                ArrayAdapter<Integer> adapterModifNbTemps = new ArrayAdapter<>(popupView.getContext(),android.R.layout.simple_spinner_item, tpsMesure);
                adapterModifNbTemps.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerModifNbTemps.setAdapter(adapterModifNbTemps);
                spinnerModifNbTemps.setSelection(tpsMesure.indexOf(oldTempsMesure));


                //unite
                final Spinner spinnerModifUnite = (Spinner) popupView.findViewById(R.id.spinnerModifUnite);
                String[] uniteArray = {"ronde","blanche","noire","croche","ronde pointée","blanche pointée","noire pointée","croche pointée"};
                List<String> uniteList = Arrays.asList(uniteArray);
                ArrayAdapter<String> dataAdapterUnite = new ArrayAdapter<String>(popupView.getContext() , android.R.layout.simple_spinner_item, uniteList);
                dataAdapterUnite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerModifUnite.setAdapter(dataAdapterUnite);
                String unite = Partition.convertUniteIntStr(Integer.parseInt(m.getUnite()));
                spinnerModifUnite.setSelection(uniteList.indexOf(unite));

                popup.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //MAJ affichage
                        adapter = new MesureAdapter(EditionActivity.this, partition);
                        mGridView.setAdapter(adapter);
                        // on ne modifie rien
                    }
                });
                popup.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int newTempo = Integer.parseInt(textModifTempo.getText().toString());
                        int newNbTempsMesure = Integer.parseInt(spinnerModifNbTemps.getSelectedItem().toString());
                        String newUnite = spinnerModifUnite.getSelectedItem().toString();
                        //TODO tester oldUnite aussi
                        if (newTempo != oldTempo || newNbTempsMesure != oldTempsMesure) {
                            VariationTemps eventSurMesure = eventTempsDeLaMesure(m.getId());

                            VariationTemps eventTemps = new VariationTemps(bdd.getMusique(EXTRA_NOMPARTITION).getId(), m.getId(), newNbTempsMesure, newTempo, 1);//TODO mettre la bonne unite

                            if (eventSurMesure.getMesure_debut() != -1) {
                                //event existe deja, on fait une update
                                eventSurMesure.setTempo(newTempo);
                                eventSurMesure.setTemps_par_mesure(newNbTempsMesure);
                                eventSurMesure.setUnite_pulsation(partition.convertUniteStrInt(newUnite));
                                bdd.update(eventSurMesure);
                            } else {
                                //on cree l'event
                                bdd.save(eventTemps);
                            }
                            varTempsList = bdd.getVariationsTemps(bdd.getMusique(idMusique));
                            triListVarTemps();
                            partition.setTempo(varTempsList);
                            //MAJ affichage
                            adapter = new MesureAdapter(EditionActivity.this, partition);
                            mGridView.setAdapter(adapter);

                        }
                    }
                });
                popup.show();

            }
        });

    }

    private VariationTemps eventTempsDeLaMesure(int numMesure){
        VariationTemps res = new VariationTemps();
        VariationTemps event;
        boolean trouve = false;
        for(int i =0; i<varTempsList.size() && ! trouve ;i++){
            event = varTempsList.get(i);
            if(event.getMesure_debut() == numMesure){
                res=event;
                trouve = true;
            }

        }

        return res;
    }

    private List<VariationIntensite> eventsNuanceDeLaMesure(int numMesure){
        List<VariationIntensite> res = new ArrayList<>();
        VariationIntensite event;
        for(int i =0; i<varIntensiteList.size();i++){
            event = varIntensiteList.get(i);
            if(event.getMesureDebut() == numMesure){
                res.add(event);
            }

        }

        return res;
    }

    private List<Armature> eventsArmatureDeLaMesure(int numMesure){
        List<Armature> res = new ArrayList<>();
        Armature event;
        for(int i =0; i<varArmatureList.size();i++){
            event = varArmatureList.get(i);
            if(event.getMesure_debut() == numMesure){
                res.add(event);
            }
        }
        return res;
    }


    //tri la liste de variations d'intensité
    private void triListVarIntensite(){
        Collections.sort(varIntensiteList, new Comparator<VariationIntensite>() {
            @Override
            public int compare(VariationIntensite lhs, VariationIntensite rhs) {
                int t;
                if(lhs.getMesureDebut() < rhs.getMesureDebut()){
                    t=-1;
                }else{
                    t=1;
                }
                return  t;
            }
        });
    }


    //tri la liste de variations temps
    private void triListVarTemps(){
        Collections.sort(varTempsList, new Comparator<VariationTemps>() {
            @Override
            public int compare(VariationTemps lhs, VariationTemps rhs) {
                int t;
                if(lhs.getMesure_debut() < rhs.getMesure_debut()){
                    t=-1;
                }else{
                    t=1;
                }
                return  t;
            }
        });
    }

    //tri la liste de variations d'intensité
    private void triListVarArmature(){
        Collections.sort(varArmatureList, new Comparator<Armature>() {
            @Override
            public int compare(Armature lhs, Armature rhs) {
                int t;
                if(lhs.getMesure_debut()< rhs.getMesure_debut()){
                    t=-1;
                }else{
                    t=1;
                }
                return  t;
            }
        });
    }


    //nuance
    private OnClickListener NuanceListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            View layout = LayoutInflater.from(context).inflate(R.layout.popup_changement_nuance, null);

            //nuance selection
            mNuanceSpinner = (Spinner) layout.findViewById(R.id.nuance);
            mNuanceDebutSpinner = (Spinner) layout.findViewById(R.id.tempsDebut);
            // attaching data adapter to spinner

            mNuanceSpinner.setAdapter(dataAdapterNuance);
            mNuanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // On selecting a spinner item
                    nuance = parent.getItemAtPosition(position).toString();
                    nuance = nuance.replace(" ", "");
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            mNuanceDebutSpinner.setAdapter(dataAdapterNuanceTpsMesure);
            mNuanceDebutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tpsDebut = Integer.parseInt(parent.getItemAtPosition(position).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


                new AlertDialog.Builder(context)
                        .setTitle("Changement de nuance")
                        .setView(layout)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                               //TODO do something
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

    };


    @Override
    public void onBackPressed() {
        bdd.close();
        finish();
        return;
    }
}





