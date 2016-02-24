package com.example.boris.emaestro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
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
    List<Mesure> mesures;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edition_mesures);

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

        mesures = genererMesure(EXTRA_NBMESURE,EXTRA_PULSATION,EXTRA_TPSPARMESURE,EXTRA_UNITE);
        MesureAdapter adapter = new MesureAdapter(EditionActivity.this,mesures);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(), String.valueOf(mesures.get(position).getId()), Toast.LENGTH_SHORT).show();//TODO gestion à l'echelle de une mesure
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
    private List<Mesure> genererMesure(String nb,String tempo,String tpsParMesure,String unite){
        List<Mesure> mesuresL;
        int id=1;
        int nbM = (int)Integer.parseInt(nb);
        int tempoM = 0;//(int)Integer.parseInt(tempo);
        int tpsParMesureM = 0;//(int)Integer.parseInt(tpsParMesure);

        mesuresL = new ArrayList<Mesure>()  ;
        for(int i=0;i<nbM;i++){
            mesuresL.add(new Mesure(id,tempoM,tpsParMesureM,unite));
            id++;
        }
        return mesuresL;
    }


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





