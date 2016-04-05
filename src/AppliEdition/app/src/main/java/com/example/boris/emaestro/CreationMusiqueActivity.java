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

import BDD.db.DataBaseManager;
import BDD.to.*;
import BDD.to.Musique;

public class CreationMusiqueActivity extends Activity {

    final Activity thisActivity = this;

    final String EXTRA_NOMPARTITION="vide";
    final String EXTRA_NBMESURE="nbMesure";
    final String EXTRA_PULSATION="pulsation";
    final String EXTRA_UNITE="unite";
    final String EXTRA_TPSPARMESURE="nbTpsMesure";
    final String EXTRA_DRAGACTIF="drag";
    final String EXTRA_ID_PARTITION="idMusique";
    final String EXTRA_NEW_PARTITION="new";

    EditText pulsation;
    EditText nomPartitionE;
    EditText nbMesureE;

    String unite="";
    String nomPartition="";
    String nbMesure="";
    String nbPulsation="";
    String tpsParMesure="";

    Spinner tpsParMesureSpinner,  uniteSpinner;

    //BDD
    //final MusiqueDAO bddMusique = new MusiqueDAO(this);
    DataBaseManager bdd = new DataBaseManager(this);

    Button drag;
    boolean dragActive ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_musique);

        bdd.open();
        dragActive = false;
        pulsation = (EditText) findViewById(R.id.pulsation);
        nomPartitionE = (EditText) findViewById(R.id.nom);
        nbMesureE = (EditText) findViewById(R.id.nbMesure);
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
        for(int i=2;i<=8;i++){
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
        tpsParMesureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                tpsParMesure = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        //button listener pour création
        loginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                nbMesure = nbMesureE.getText().toString();
                unite = uniteSpinner.getSelectedItem().toString();
                Partition p = new Partition();
                unite = String.valueOf(p.convertUniteStrInt(unite));
                nbPulsation = pulsation.getText().toString();
                nomPartition = nomPartitionE.getText().toString();
                // permet le passage de message dans un changement d'activité (startActivity)
                Intent intent = new Intent(CreationMusiqueActivity.this, EditionActivity.class);
                intent.putExtra(EXTRA_NOMPARTITION, nomPartition);
                intent.putExtra(EXTRA_NBMESURE, nbMesure);
                intent.putExtra(EXTRA_PULSATION, nbPulsation);
                intent.putExtra(EXTRA_TPSPARMESURE, tpsParMesure);
                intent.putExtra(EXTRA_UNITE, unite);
                intent.putExtra(EXTRA_ID_PARTITION, "-1");
                intent.putExtra(EXTRA_NEW_PARTITION, "true");

                if (dragActive) {
                    Toast.makeText(CreationMusiqueActivity.this, "Edition par drag and drop", Toast.LENGTH_SHORT).show();//TODO gestion à l'echelle de une mesure
                    intent.putExtra(EXTRA_DRAGACTIF, "true");
                } else {
                    Toast.makeText(CreationMusiqueActivity.this, "Edition par selection", Toast.LENGTH_SHORT).show();
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

                    Musique musiqueDejaPresente = bdd.getMusique(nomPartition);
                    if (!musiqueDejaPresente.getName().equals(nomPartition)) {
                        //si le nom de la musique n'existe pas deja on ajoute la musique dans la BDD

                        int id = bdd.getMusiques().size() + 1;
                        //TODO: seulement trois champs pour musique
                        long err = bdd.save(new Musique(id, nomPartition, Integer.parseInt(nbMesure)));//, Integer.parseInt(nbPulsation), Integer.parseInt(unite), Integer.parseInt(tpsParMesure)));

                        //on crée les  eventVarTemps et eventVarIntensite initiaux
                        //    bdd.save(new VariationIntensite(id,-1,0,0,0));
                        //   bdd.save(new VariationTemps(id,0,Integer.parseInt(tpsParMesure),Integer.parseInt(nbPulsation), Integer.valueOf(unite)));//TODO : Gerer l'unite pulsation

                        if (err == -1) {
                            Toast.makeText(getApplicationContext(), "Erreur lors de l'ajout de la partition dans la base de donnée", Toast.LENGTH_SHORT).show();
                        } else {
                            if (dragActive) {
                                Toast.makeText(getApplicationContext(), "Drag and drop désactivé, utilisez mode selection", Toast.LENGTH_SHORT).show();
                            }
                            startActivity(intent);
                            thisActivity.finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "La partition " + nomPartition + " existe déjà", Toast.LENGTH_SHORT).show();
                    }
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
