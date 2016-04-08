package com.example.boris.emaestro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import BDD.db.DataBaseManager;
import BDD.to.Musique;
import BDD.to.VariationIntensite;
import BDD.to.VariationTemps;

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




    static ListView eventNaunceListView;
    EventNuanceAdapter adapterEventNuance;

    List<VariationIntensite> varIntensiteListSurMesureCour;

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

              //  partition.setNbTempsAll(Integer.parseInt(EXTRA_TPSPARMESURE));
            }

        }else{

            debug.setText("Erreur interne");

        }

        //on recupère les données associées à la musique
        varIntensiteList = bdd.getVariationsIntensite(bdd.getMusique(EXTRA_NOMPARTITION));
        varTempsList = bdd.getVariationsTemps(bdd.getMusique(EXTRA_NOMPARTITION));

        //On trie nos listes en ordre croissant d'id de mesure
        triListVarIntensite();
        triListVarTemps();


        //-----------------------
        //debug msg
        //-----------------------
        for(int i = 0; i<varIntensiteList.size();i++){
            debug.setText(debug.getText().toString() + varIntensiteList.size() + "\n nouveau event debut à :" + (varIntensiteList.get(i).getMesureDebut() ) + " nuance : " + partition.convertNuanceIntStr(varIntensiteList.get(i).getIntensite()));
        }
        for(int i = 0; i<varTempsList.size();i++){
            debug.setText(debug.getText().toString() + varTempsList.size() + "\n nouveau event debut à :" + (varTempsList.get(i).getMesure_debut() ) + " tempo : " + varTempsList.get(i).getTempo() + " nb temps " +varTempsList.get(i).getTemps_par_mesure());
        }
        //-----------------------
        //debug msg
        //-----------------------

        //on met ajour tempo et intensite
        partition.setTempo(varTempsList);
        partition.setNuance(varIntensiteList);

        adapter = new MesureAdapter(EditionActivity.this, partition, dataAdapterNuance);
        mGridView.setAdapter(adapter);

        dataAdapterNuanceTpsMesure = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tempsMesure);
        dataAdapterNuanceTpsMesure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                final Mesure m = partition.getMesure(position);
                final int oldTempo = m.getTempo();
                final int  oldTempsMesure = m.getTempsMesure();



                AlertDialog.Builder popup = new AlertDialog.Builder(context);

                popup.setTitle("Informations de la mesure " + (position + 1));
                LayoutInflater inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_liste_event, null);
                popup.setView(popupView);

                varIntensiteListSurMesureCour = eventsNuanceDeLaMesure(m.getId());
                adapterEventNuance = new EventNuanceAdapter(context,varIntensiteListSurMesureCour); // TODO autre liste d'event
                eventNaunceListView = (ListView) popupView.findViewById(R.id.listEventNuance);
                eventNaunceListView.setAdapter(adapterEventNuance);



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

                //nuance
                Spinner spinnerModifNuance = (Spinner) popupView.findViewById(R.id.spinnerModifNuance);
                String[] nuancesTab = {"neutre","fortississimo","fortissimo","forte","mezzo forte","mezzo piano","piano","pianissimo","pianississimo"};
                List<String> nuances = Arrays.asList(nuancesTab);
                ArrayAdapter<String> adapterModifNuance = new ArrayAdapter<>(popupView.getContext(),android.R.layout.simple_spinner_item, nuances);
                adapterModifNuance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerModifNuance.setAdapter(adapterModifNuance);
                spinnerModifNuance.setSelection(nuances.indexOf(m.getNuance()));



                popup.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // on ne modifie rien
                    }
                });
                popup.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int newTempo = Integer.parseInt(textModifTempo.getText().toString());
                        int newNbTempsMesure = Integer.parseInt(spinnerModifNbTemps.getSelectedItem().toString());
                        if (newTempo != oldTempo || newNbTempsMesure != oldTempsMesure) {
                            VariationTemps eventSurMesure = eventTempsDeLaMesure(m.getId());
                            VariationTemps eventTemps = new VariationTemps(bdd.getMusique(EXTRA_NOMPARTITION).getId(), m.getId(), newNbTempsMesure, newTempo, 1);//TODO mettre la bonne unite
                            bdd.open();
                            if (eventSurMesure.getMesure_debut() != -1) {
                                //event existe deja, on fait une update
                                eventSurMesure.setTempo(newTempo);
                                eventSurMesure.setTemps_par_mesure(newNbTempsMesure);
                                bdd.update(eventSurMesure);
                            } else {
                                //on cree l'event
                                bdd.save(eventTemps);
                            }
                            varIntensiteList = bdd.getVariationsIntensite(bdd.getMusique(idMusique));
                            triListVarIntensite();
                            partition.setTempo(varTempsList);
                            bdd.close();
                        }
                        //TODO recuperer les infos modifiees et creer les events qui correspondent aux nouvelles infos (ne pas creer d'event doublon ou redondant)
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





