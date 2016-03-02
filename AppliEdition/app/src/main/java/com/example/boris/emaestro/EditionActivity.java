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

    EditText nom = null;
    EditText mesure = null;
    Button envoyer = null;
    Button clean = null;
    TextView result = null;
    MusiqueDAO db = new MusiqueDAO(this);
    ArrayList<Integer> mesuresSelec;
    LinearLayout menu; // view du menu
    //grilleMesure
    GridView mGridView;
    Partition partition;
    Context context;
    //Drag
    ImageView mTempo;
     Button mActivationDrag;
    ImageView mNuance;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edition_mesures);
        context = EditionActivity.this;

        //modification par selection
        mesuresSelec = new ArrayList<>();
        menu =(LinearLayout) findViewById(R.id.menu);
        //activation ou non du drag
        mTempo = (ImageView) menu.findViewById(R.id.tempo);
        mNuance = (ImageView) menu.findViewById(R.id.nuance);



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
            mTempo.setOnTouchListener((new BoutonListener("tempo")));
            mNuance.setOnTouchListener(new BoutonListener("nuance"));

        } else {
            //selection
            mTempo.setOnClickListener(TempoListener);
        }

        partition = new Partition(EXTRA_NBMESURE,EXTRA_PULSATION,EXTRA_TPSPARMESURE,EXTRA_UNITE);
        MesureAdapter adapter = new MesureAdapter(EditionActivity.this,partition);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Mesure m =partition.getMesure(position);
                Toast.makeText(getApplicationContext(), String.valueOf(m.getTempo()), Toast.LENGTH_SHORT).show();

                //Edition par selection
                if( EXTRA_DRAGACTIF.equals("false")) {
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

        //pour bdd, à jour ?
        nom = (EditText) findViewById(R.id.nom);
        mesure = (EditText) findViewById(R.id.mesure);
        envoyer = (Button) findViewById(R.id.envoie);
        clean = (Button) findViewById(R.id.clean);
        result = (TextView) findViewById(R.id.result);
        db.open();
        envoyer.setOnClickListener(envoyerListener);
        clean.setOnClickListener(cleanListener);
        nom.addTextChangedListener(textWatcher);


    }

    //<Drag
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

    //selection
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


    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            result.setText("Bien continue :)");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private OnClickListener envoyerListener = new OnClickListener() {
        @Override
        public void onClick(View v)
        {
            String name = nom.getText().toString();
            String nb_mesures = mesure.getText().toString();
            int nb_mesure = stringToInt(nb_mesures);
            db.insert(new Musique(name, nb_mesure));
            result.setText(db.getMusique(name).getName()+"/"+db.getMusique(name).getMesures());
        }
    };

    private OnClickListener cleanListener = new OnClickListener() {
        @Override
        public void onClick(View v)
        {
            db.clean();
        }
    };

    private int stringToInt(String str){
        int res = 0;
        for(int i = 0; i < str.length(); i++) {
            res += (str.charAt(str.length()-i-1)-'0')*Math.pow(10, i);
        }
        return res;
    }




    }





