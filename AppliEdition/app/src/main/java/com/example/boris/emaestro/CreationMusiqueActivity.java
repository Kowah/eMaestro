package com.example.boris.emaestro;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreationMusiqueActivity extends Activity {

    String EXTRA_NOMPARTITION="vide";
    String EXTRA_NBMESURE="nbMesure";
    String EXTRA_PULSATION="pulsation";
    String EXTRA_UNITE="unite";
    String EXTRA_TPSPARMESURE="tpsParMesure";
    String EXTRA_DRAGACTIF="false";

    EditText pulsation;
    EditText nomPartition;
    EditText nbMesure;
    //variable pour tempo, tps par mesure
    String unite="";
    String tpsParMesure="";
    Spinner tpsParMesureSpinner,  uniteSpinner;


    Button drag;
    boolean dragActive ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_musique);
        dragActive = false;
        pulsation = (EditText) findViewById(R.id.pulsation);
        nomPartition = (EditText) findViewById(R.id.nom);
        nbMesure = (EditText) findViewById(R.id.nbMesure);
        drag = (Button) findViewById(R.id.drag);
        uniteSpinner = (Spinner) findViewById(R.id.uniteTempo);
        tpsParMesureSpinner = (Spinner) findViewById(R.id.tempsParMesure);
        Button loginButton = (Button) findViewById(R.id.creer);

        //Boutonn choix entre drag et selection
        drag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dragActive = ! dragActive;
                if(dragActive){
                    drag.setText("Désactiver le drag n drop");

                }else{
                    drag.setText("Activer le drag n drop");
                }
            }
        });

        // Spinner Unité du tempo
        List<String> uniteList = new ArrayList<String>();
        uniteList.add("ronde");
        uniteList.add("blanche");
        uniteList.add("noire");
        uniteList.add("croche");
        uniteList.add("ronde pointée");
        uniteList.add("blanche pointée");
        uniteList.add("noire pointée");
        uniteList.add("croche pointée");

        List<String> tpsMesure = new ArrayList<String>();
        for(int i=1;i<=16;i++){
            tpsMesure.add(String.valueOf(i));
        }

        // Adapter pour le spinner, pour l'affichage
        ArrayAdapter<String> dataAdapterUnite = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,uniteList );
        ArrayAdapter<String> dataAdapterTpsParMesure = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,tpsMesure);

        // Drop down layout style - affichage "amélioré"
        dataAdapterUnite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterTpsParMesure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        uniteSpinner.setAdapter(dataAdapterUnite);
        tpsParMesureSpinner.setAdapter(dataAdapterTpsParMesure);

        //button listener pour création
        loginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                EXTRA_UNITE = uniteSpinner.getSelectedItem().toString();
                EXTRA_TPSPARMESURE = tpsParMesureSpinner.getSelectedItem().toString();
                // permet le passage de message dans un changement d'activité (startActivity)
                Intent intent = new Intent(CreationMusiqueActivity.this, EditionActivity.class);
                intent.putExtra(EXTRA_NOMPARTITION, nomPartition.getText().toString());
                intent.putExtra(EXTRA_NBMESURE, nbMesure.getText().toString());
                intent.putExtra(EXTRA_PULSATION, pulsation.getText().toString());
                intent.putExtra(EXTRA_TPSPARMESURE, tpsParMesure);
                intent.putExtra(EXTRA_UNITE, unite);

                if (dragActive) {
                    Toast.makeText(CreationMusiqueActivity.this, "Edition par drag and drop", Toast.LENGTH_SHORT).show();//TODO gestion à l'echelle de une mesure
                    intent.putExtra(EXTRA_DRAGACTIF, "true");
                } else {
                    Toast.makeText(CreationMusiqueActivity.this, "Edition par selection",Toast.LENGTH_SHORT).show();
                            intent.putExtra(EXTRA_DRAGACTIF, "false");
                }

                if (unite.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Veuillez choisir l'unité de tmps", Toast.LENGTH_SHORT).show();
                } else if (tpsParMesure.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Veuillez choisir le nombre de temps par mesure", Toast.LENGTH_SHORT).show();
                } else if (pulsation.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Veuillez choisir une pulsation", Toast.LENGTH_SHORT).show();
                } else if (nbMesure.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Veuillez choisir le nombre de mesure", Toast.LENGTH_SHORT).show();
                } else if (nomPartition.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Veuillez choisir un nom de partition", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(intent);
                }
            }
        });


        //listener pour choix de l'unite
        uniteSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                unite = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        //listener pour nbre de temps par mesure
        tpsParMesureSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                tpsParMesure = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });




    }




}
