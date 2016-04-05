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
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

    //menu edition
    ImageView mTempo;
    ImageView mNuance;
    ImageView mSelection;
    ImageView mSupprimerSelection;

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

    //spinner chgt nbTps par mesure
    List<String> nbTempsMesure = new ArrayList<>();
    ArrayAdapter<String> dataAdapterNbTpsMesure;
    List<VariationTemps> chevaucheTempsList;

    //debug
    TextView debug;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edition_mesures);
        context = EditionActivity.this;
        bdd.open();


        //Barre de menu
        menu = (LinearLayout) findViewById(R.id.menu);
        mTempo = (ImageView) menu.findViewById(R.id.tempo);
        mNuance = (ImageView) menu.findViewById(R.id.nuance);
        mSelection = (ImageView) menu.findViewById(R.id.selection);
        mSupprimerSelection = (ImageView) menu.findViewById(R.id.selection_delete);

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

        //Temps par mesure
        nbTempsMesure = new ArrayList<>();
        for(int i=0; i<9;i++){
            nbTempsMesure.add(i+"");
        }

        dataAdapterNbTpsMesure = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nbTempsMesure);
        dataAdapterNbTpsMesure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Variables d'edition
        varIntensiteList = new ArrayList<>();
        varTempsList = new ArrayList<>();
        chevaucheNuanceList = new ArrayList<>();
        chevaucheTempsList = new ArrayList<>();

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

            //selection
            mTempo.setOnClickListener(TempoListener);
            mNuance.setOnClickListener(NuanceListener);

        adapter = new MesureAdapter(EditionActivity.this, partition, dataAdapterNuance);
        mGridView.setAdapter(adapter);

        dataAdapterNuanceTpsMesure = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tempsMesure);
        dataAdapterNuanceTpsMesure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Mesure m = partition.getMesure(position);
                Toast.makeText(getApplicationContext(), "Le tempo de la mesure" + m.getId() + " est :" + String.valueOf(m.getTempo()), Toast.LENGTH_SHORT).show();
                //TODO affiche un popUP

            }
        });


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

    //tempo
    private OnClickListener TempoListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            View layout = LayoutInflater.from(context).inflate(R.layout.popup_changement_tempo, null);
            final EditText editTempo = (EditText) layout.findViewById(R.id.tempo);
            final Spinner nbTpsMesureSpinner = (Spinner) layout.findViewById(R.id.nbTempsMesure);
            nbTpsMesureSpinner.setAdapter(dataAdapterNbTpsMesure);


                new AlertDialog.Builder(context)
                        .setTitle("Changement de tempo")
                        .setView(layout)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO do something
                            }}
                        )
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
    };

    private VariationTemps leventPresentSurMemeMesure(VariationTemps vT){
        VariationTemps res = new VariationTemps();
        VariationTemps tempTemps;
        for(int i=0;i<varTempsList.size() ;i++){
            tempTemps=varTempsList.get(i);
            if(tempTemps.getMesure_debut() == vT.getMesure_debut()){
                return tempTemps;
            }
        }
        return res;
    }

    private VariationIntensite leventPresentSurMemeMesure(VariationIntensite vI){
       VariationIntensite res = new VariationIntensite();
        VariationIntensite tempIntens;
        for(int i=0;i<varIntensiteList.size() ;i++){
            tempIntens=varIntensiteList.get(i);
            if(tempIntens.getMesureDebut() == vI.getMesureDebut()){
                return tempIntens;
            }
        }
        return res;
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





