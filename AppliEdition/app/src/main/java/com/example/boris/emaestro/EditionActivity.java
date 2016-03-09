package com.example.boris.emaestro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class EditionActivity  extends Activity {


    String EXTRA_NOMPARTITION="vide";
    String EXTRA_NBMESURE="nbMesure";
    String EXTRA_PULSATION="pulsation";
    String EXTRA_UNITE="unite";
    String EXTRA_TPSPARMESURE="nbTpsMesure";
    String EXTRA_DRAGACTIF="false";

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
    ImageView  mSupprimerSelection;

    //modif nuance
    Spinner mNuanceSpinner;
    ArrayAdapter<String> dataAdapterNuance;
    String nuance;
    //selection
    boolean selectionOn;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edition_mesures);
        context = EditionActivity.this;

        //modification par selection
        mesuresSelec = new ArrayList<>();
        selectionOn =false;

        //Barre de menu
        menu =(LinearLayout) findViewById(R.id.menu);
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

        dataAdapterNuance = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,nuanceList );
        dataAdapterNuance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //gestion grille mesure
        GridLayout mesuresGrid =(GridLayout) findViewById(R.id.mesures);
        mGridView = (GridView) mesuresGrid.findViewById(R.id.gridView);
        Intent intent = getIntent();
        if (intent != null) {
            //TODO insertion dans la bdd de la musique à partir des infos rentré par l'utilisateur
        }
        EXTRA_NBMESURE=intent.getStringExtra(EXTRA_NBMESURE);
        EXTRA_PULSATION=intent.getStringExtra(EXTRA_PULSATION);
        EXTRA_UNITE=intent.getStringExtra(EXTRA_UNITE);
        EXTRA_TPSPARMESURE=intent.getStringExtra(EXTRA_TPSPARMESURE);
        EXTRA_DRAGACTIF = intent.getStringExtra(EXTRA_DRAGACTIF);

        if( EXTRA_DRAGACTIF.equals("true")){
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
            mNuance.setOnClickListener( NuanceListener);
        }

        partition = new Partition(EXTRA_NBMESURE,EXTRA_PULSATION,EXTRA_TPSPARMESURE,EXTRA_UNITE);
        adapter = new MesureAdapter(EditionActivity.this,partition,dataAdapterNuance);
        mGridView.setAdapter(adapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Mesure m = partition.getMesure(position);
                Toast.makeText(getApplicationContext(), "Le tempo de la mesure" + m.getId() + " est :" + String.valueOf(m.getTempo()), Toast.LENGTH_SHORT).show();

                //Edition par selection
                if (EXTRA_DRAGACTIF.equals("false") && selectionOn) {

                    m.toggleSelec();
                    if (m.getSelec()) {
                        v.findViewById(R.id.selection).setAlpha(0.7f);
                        mesuresSelec.add(new Integer(m.getId()));
                    } else {
                        v.findViewById(R.id.selection).setAlpha(0.0f);
                        mesuresSelec.remove(new Integer(m.getId()));
                    }
                }
            }
        });




    }

    //<listener pour Drag
    private final class BoutonListener implements View.OnTouchListener {
        private String typeEvent;
        public BoutonListener(String type){
            super();
            typeEvent = type;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                String mType="";
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
        public void onClick(View v)
        {
            //change l'image en fonction de letat de selectionOn
            if(selectionOn){
                ((ImageView)v.findViewById(R.id.selection)).setBackgroundResource(R.drawable.selection_off);
            }else{
                ((ImageView)v.findViewById(R.id.selection)).setBackgroundResource(R.drawable.selection_on);
            }
            //on passe a l'état contraire
            selectionOn =!selectionOn;
        }
    };
//supprimer selection
    private OnClickListener SupprimerSelectionListener = new OnClickListener() {
        @Override
        public void onClick(View v)
        {

            partition.unselectAll();
            mesuresSelec.clear();
            adapter = new MesureAdapter(EditionActivity.this,partition,dataAdapterNuance);
            mGridView.setAdapter(adapter);


        }
    };

    //tempo
    private OnClickListener TempoListener = new OnClickListener() {
        @Override
        public void onClick(View v)
        {

            View layout = LayoutInflater.from(context).inflate(R.layout.popup_changement_tempo, null);
            final EditText editTempo = (EditText) layout.findViewById(R.id.tempo);
            final EditText finTempo = (EditText) layout.findViewById(R.id.mesureFin);
            new AlertDialog.Builder(context)
                    .setTitle("Changement de tempo")
                    .setView(layout)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            int newTempo = Integer.parseInt(editTempo.getText().toString());
                            //TODO ajouter evenement dans bdd
                            partition.setTempo(mesuresSelec,newTempo);
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
        }
    };



//nuance
    private OnClickListener NuanceListener = new OnClickListener() {
        @Override
        public void onClick(View v)
        {

            View layout = LayoutInflater.from(context).inflate(R.layout.popup_changement_nuance, null);

            //nuance selection
            mNuanceSpinner = (Spinner)layout.findViewById(R.id.nuance);

            // attaching data adapter to spinner
            mNuanceSpinner.setAdapter(dataAdapterNuance);

            mNuanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // On selecting a spinner item
                    nuance = parent.getItemAtPosition(position).toString();
                    nuance = nuance.replace(" ","");

                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
            new AlertDialog.Builder(context)
                    .setTitle("Changement de nuance")
                    .setView(layout)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            partition.setNuance(mesuresSelec, nuance);
                            adapter = new MesureAdapter(EditionActivity.this,partition,dataAdapterNuance);
                            mGridView.setAdapter(adapter);
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
        }
    };


//selection>

    }





