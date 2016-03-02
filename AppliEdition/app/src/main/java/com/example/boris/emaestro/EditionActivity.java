package com.example.boris.emaestro;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

/**
 * Created by Boris on 10/02/2016.
 */
public class EditionActivity  extends Activity {


    String EXTRA_NOMPARTITION="vide";
    String EXTRA_NBMESURE="nbMesure";
    String EXTRA_PULSATION="pulsation";
    String EXTRA_UNITE="unite";
    String EXTRA_TPSPARMESURE="nbTpsMesure";
    EditText nom = null;
    EditText mesure = null;
    Button envoyer = null;
    Button clean = null;
    TextView result = null;
    MusiqueDAO db = new MusiqueDAO(this);

    //grilleMesure
    GridView mGridView;
    Partition partition;

    //Drag
    ImageView mTempo;

    ImageView mNuance;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edition_mesures);

        //<drag
        LinearLayout menu =(LinearLayout) findViewById(R.id.menu);


        mTempo = (ImageView) menu.findViewById(R.id.tempo);
        mNuance = (ImageView) menu.findViewById(R.id.nuance);

        mTempo.setOnTouchListener(new BoutonListener("tempo"));
        mNuance.setOnTouchListener(new BoutonListener("nuance"));
        //drag>

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

        partition = new Partition(EXTRA_NBMESURE,EXTRA_PULSATION,EXTRA_TPSPARMESURE,EXTRA_UNITE);
        MesureAdapter adapter = new MesureAdapter(EditionActivity.this,partition);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(), String.valueOf(partition.getListMesures().get(position).getTempo()), Toast.LENGTH_SHORT).show();
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





