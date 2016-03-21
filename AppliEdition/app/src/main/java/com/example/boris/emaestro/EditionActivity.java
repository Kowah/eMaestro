package com.example.boris.emaestro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import BDD.db.DataBaseManager;

import BDD.to.Musique;
import BDD.to.VariationIntensite;
import BDD.to.VariationTemps;

public class EditionActivity  extends Activity {


    String EXTRA_NOMPARTITION = "vide";
    String EXTRA_NBMESURE = "nbMesure";
    String EXTRA_PULSATION = "pulsation";
    String EXTRA_UNITE = "unite";
    String EXTRA_TPSPARMESURE = "nbTpsMesure";
    String EXTRA_DRAGACTIF = "drag";
    String EXTRA_ID_PARTITION = "idMusique";
    DataBaseManager bdd = new DataBaseManager(this);
    Musique partitionCourante;
    int idMusique;
    List<VariationTemps> varTempsList;
    List<VariationIntensite> varIntensiteList;

    int mesureDebut;
    int mesureFin;

    ArrayList<Integer> mesuresSelec;
    LinearLayout menu; // view du menu
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
    ArrayAdapter<String> dataAdapterNuance;
    String nuance;
    Mesure[] intervalMesureSelec = new Mesure[2];
    int nbMesureSelec = 0;
    //selection
    boolean selectionOn;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edition_mesures);
        context = EditionActivity.this;
        bdd.open();

        //modification par selection
        mesuresSelec = new ArrayList<>();
        selectionOn = false;

        //Barre de menu
        menu = (LinearLayout) findViewById(R.id.menu);
        mTempo = (ImageView) menu.findViewById(R.id.tempo);
        mNuance = (ImageView) menu.findViewById(R.id.nuance);
        mSelection = (ImageView) menu.findViewById(R.id.selection);
        mSupprimerSelection = (ImageView) menu.findViewById(R.id.selection_delete);

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

        dataAdapterNuance = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nuanceList);
        dataAdapterNuance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //gestion grille mesure
        GridLayout mesuresGrid = (GridLayout) findViewById(R.id.mesures);
        mGridView = (GridView) mesuresGrid.findViewById(R.id.gridView);
        Intent intent = getIntent();

        if (intent != null) {
            //TODO insertion dans la bdd de la musique à partir des infos rentré par l'utilisateur
        }
        EXTRA_NOMPARTITION = intent.getStringExtra(EXTRA_NOMPARTITION);
        EXTRA_NBMESURE = intent.getStringExtra(EXTRA_NBMESURE);
        EXTRA_PULSATION = intent.getStringExtra(EXTRA_PULSATION);
        EXTRA_UNITE = intent.getStringExtra(EXTRA_UNITE);
        EXTRA_TPSPARMESURE = intent.getStringExtra(EXTRA_TPSPARMESURE);
        EXTRA_DRAGACTIF = intent.getStringExtra(EXTRA_DRAGACTIF);
        EXTRA_ID_PARTITION = intent.getStringExtra(EXTRA_ID_PARTITION);
        //on recupere l'instance dans la bdd de la partition qu'on edite
        //   partitionCourante = bddMusique.getMusique(EXTRA_NOMPARTITION);
        idMusique = Integer.parseInt(EXTRA_ID_PARTITION);
        partition = new Partition(EXTRA_NBMESURE, EXTRA_PULSATION, EXTRA_TPSPARMESURE, EXTRA_UNITE);
        if (idMusique > -1) {
            //on recupère les données associées à la musique
            Toast.makeText(getApplicationContext(), "chargement musique", Toast.LENGTH_SHORT).show();

            varIntensiteList = bdd.getVariationsIntensite(bdd.getMusique(idMusique));
            varTempsList = bdd.getVariationsTemps(bdd.getMusique(idMusique));//TODO pb : toujours vide

            //on met ajour tempo et intensite
            partition.setTempo(varTempsList);
            partition.setNuance(varIntensiteList);
        }


        if (EXTRA_DRAGACTIF.equals("true")) {
            //drag
            mSelection.setVisibility(View.INVISIBLE); // on affiche pas le bouton de selection
            mSupprimerSelection.setVisibility((View.INVISIBLE));
            mTempo.setOnTouchListener((new BoutonListener("tempo")));
            mNuance.setOnTouchListener(new BoutonListener("nuance"));

        } else {
            //selection
            mTempo.setOnClickListener(TempoListener);
            mSelection.setOnClickListener(SelectionListener);
            mSupprimerSelection.setOnClickListener(SupprimerSelectionListener);
            mNuance.setOnClickListener(NuanceListener);
        }




        adapter = new MesureAdapter(EditionActivity.this, partition, dataAdapterNuance);
        mGridView.setAdapter(adapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Mesure m = partition.getMesure(position);
                Toast.makeText(getApplicationContext(), "Le tempo de la mesure" + m.getId() + " est :" + String.valueOf(m.getTempo()), Toast.LENGTH_SHORT).show();

                //Edition par selection
                if (EXTRA_DRAGACTIF.equals("false") && selectionOn) {

                  selectionHandler(m,v);

                }
            }
        });


    }


    private void selectionHandler(Mesure m,View v){
        if(nbMesureSelec<2){
            intervalMesureSelec[nbMesureSelec]=m;
            nbMesureSelec++;
            if(nbMesureSelec==2){
                afficheSelection(intervalMesureSelec[0],intervalMesureSelec[1],v);
            }
           // System.out.println("selection de mesure n : "+nbMesureSelec);

        }else{
            intervalMesureSelec[0]=intervalMesureSelec[1];
            intervalMesureSelec[1]=m;
            SupprimerSelection(true);
            afficheSelection(intervalMesureSelec[0],intervalMesureSelec[1],v);
        }


    }
    private void afficheSelection(Mesure mDebut, Mesure mFin,View v){
        Mesure temp;
        if(mDebut.getId()>mFin.getId()){
            temp = mFin;
            mFin=mDebut;
            mDebut=temp;
        }
        int debut =  partition.partition.indexOf(mDebut);
        int fin = partition.partition.indexOf(mFin);
        for( int i =debut; i<=fin; i++){
            temp =partition.partition.get(i);
            temp.toggleSelec();
            if (temp.getSelec()) {
                mesuresSelec.add(new Integer(temp.getId()));
            } else {
                mesuresSelec.remove(new Integer(temp.getId()));
            }
        }
        adapter = new MesureAdapter(EditionActivity.this, partition, dataAdapterNuance);
        mGridView.setAdapter(adapter);
    }

    //<listener pour Drag
    private final class BoutonListener implements View.OnTouchListener {
        private String typeEvent;

        public BoutonListener(String type) {
            super();
            typeEvent = type;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                String mType = "";
                ClipData data = ClipData.newPlainText(typeEvent, mType);//donne un label au clipdata
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            } else {
                return false;
            }
        }
    }


    //Drag>

    //<listeners selection


    //debut selection
    private OnClickListener SelectionListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //change l'image en fonction de letat de selectionOn
            if (selectionOn) {
                ((ImageView) v.findViewById(R.id.selection)).setBackgroundResource(R.drawable.selection_off);
            } else {
                ((ImageView) v.findViewById(R.id.selection)).setBackgroundResource(R.drawable.selection_on);
            }
            //on passe a l'état contraire
            selectionOn = !selectionOn;
        }
    };
    //supprimer selection
    private OnClickListener SupprimerSelectionListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            SupprimerSelection(false);
            adapter = new MesureAdapter(EditionActivity.this, partition, dataAdapterNuance);
            mGridView.setAdapter(adapter);


        }
    };

    private void SupprimerSelection(boolean miseAJourSelection){
        if(!miseAJourSelection){
            nbMesureSelec=0;
            intervalMesureSelec = new Mesure[2];
        }
        partition.unselectAll();
        mesuresSelec.clear();

    }

    //tempo
    private OnClickListener TempoListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            View layout = LayoutInflater.from(context).inflate(R.layout.popup_changement_tempo, null);
            final EditText editTempo = (EditText) layout.findViewById(R.id.tempo);

            if (mesuresSelec.size() > 0) {
                mesureDebut = mesuresSelec.get(0).intValue();
                mesureFin = mesuresSelec.get(mesuresSelec.size() - 1).intValue();
                new AlertDialog.Builder(context)
                        .setTitle("Changement de tempo")
                        .setView(layout)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int newTempo = Integer.parseInt(editTempo.getText().toString());
                                //TODO ajouter evenement dans bdd
                                partition.setTempo(mesuresSelec, newTempo);
                                Toast.makeText(context, "le tempo des mesures [" + mesureDebut + "," + mesureFin + "] = " + newTempo, Toast.LENGTH_SHORT).show();//TODO gestion à l'echelle de une mesure
                                idMusique = bdd.getMusique(EXTRA_NOMPARTITION).getId();
                                //idMusique=bddMusique.getMusique("debug").getId();
                                //FIXME: Fonctionne pour créer de nouvelles variations (l'erreur venait d'un champs en trop, d'apres les autres mesures fin ne vas pas dans la BDD)
                                long t = bdd.save(new VariationTemps(idMusique, mesureDebut, 1, newTempo));
                                if(mesureDebut != 0) {
                                    //Ajout de l'evenement de fin de variation
                                    int oldTempo = partition.getMesure(mesureDebut - 1).getTempo();
                                    long t2 = bdd.save(new VariationTemps(idMusique, mesureFin, 1, oldTempo));
                                }
                                Toast.makeText(getApplicationContext(), "Lmidr r, e" + t, Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                Toast.makeText(context, "Merci de sélectionner des mesures", Toast.LENGTH_SHORT).show();

            }
        }
    };


    //nuance
    private OnClickListener NuanceListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            View layout = LayoutInflater.from(context).inflate(R.layout.popup_changement_nuance, null);

            //nuance selection
            mNuanceSpinner = (Spinner) layout.findViewById(R.id.nuance);

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

            if (mesuresSelec.size() > 0) {
                mesureDebut = mesuresSelec.get(0).intValue();

                new AlertDialog.Builder(context)
                        .setTitle("Changement de nuance")
                        .setView(layout)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                partition.setNuance(mesuresSelec, nuance);
                                adapter = new MesureAdapter(EditionActivity.this, partition, dataAdapterNuance);
                                mGridView.setAdapter(adapter);
                                idMusique = bdd.getMusique(EXTRA_NOMPARTITION).getId();

                               long t = bdd.save(new VariationIntensite(idMusique, partition.convertNuanceStrInt(nuance), 1,mesureDebut,1/* Integer.parseInt(EXTRA_TPSPARMESURE)*/));

                                //  Toast.makeText(context, "le tempo des mesures [" + mesureDebut + "," + mesureFin + "] = " + newTempo, Toast.LENGTH_SHORT).show();//TODO gestion à l'echelle de une mesure

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                Toast.makeText(context, "Merci de sélectionner des mesures", Toast.LENGTH_SHORT).show();

            }
        }
//selection>

    };
}





