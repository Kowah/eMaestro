package emaestro.emulateur;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import database.MaestroBDD;
import database.RemplissageBdd;
import util.Pair;

public class MainActivity extends AppCompatActivity {

    int idMusique=-1;
    MaestroBDD bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bdd = new MaestroBDD(this);
        bdd.open();
        RemplissageBdd.remplirDonneesTest(bdd);

        initSpinnerMusique();

    }

    @Override
    protected void onDestroy() {
        bdd.close();
        super.onDestroy();
    }

    private void initSpinnerMusique() {

        final ArrayList<Pair<Integer,String>> listMusique = bdd.selectAllMusique();

        final ArrayAdapter<Pair<Integer,String>> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listMusique);

        final Spinner spinner = (Spinner)findViewById(R.id.spinnerMusique);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Pair<Integer, String> selection = (Pair<Integer, String>) parent.getItemAtPosition(position);

                idMusique = selection.getLeft();
                spinner.setSelection(position);

                //Afficher le nombre de mesures du morceau selectionne
                TextView textNbMesures = (TextView)findViewById(R.id.textNbMesures);
                textNbMesures.setText(""+bdd.selectMesureFin(idMusique));

                //Mise par defaut les mesures de debut et de fin
                EditText editMesureDebut = (EditText)findViewById(R.id.editMesureDebut);
                EditText editMesureFin = (EditText)findViewById(R.id.editMesureFin);

                editMesureDebut.setText("1");

                int mesureFin = bdd.selectMesureFin(idMusique);
                editMesureFin.setText(""+mesureFin);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void jouer(View view) {
        if(idMusique != -1) {//Si un morceau a ete choisi
            int mesureFinMusique = bdd.selectMesureFin(idMusique);
            EditText editMesureDebut = (EditText)findViewById(R.id.editMesureDebut);
            EditText editMesureFin = (EditText)findViewById(R.id.editMesureFin);
            int mesureDebut = Integer.parseInt(editMesureDebut.getText().toString());
            int mesureFin = Integer.parseInt(editMesureFin.getText().toString());

            //Erreur dans les indications de mesures pour la lecture
            if(mesureDebut<=0 || mesureDebut>mesureFin || mesureFin>mesureFinMusique){
                if(mesureDebut<=0){
                    Toast.makeText(this, "La mesure de début doit valoir au minimum 1", Toast.LENGTH_LONG).show();
                }
                if(mesureDebut>mesureFin){
                    Toast.makeText(this, "La mesure de début doit précéder celle de fin", Toast.LENGTH_LONG).show();
                }
                if(mesureFin>mesureFinMusique){
                    Toast.makeText(this, "La mesure de fin doit au plus être la dernière mesure du morceau", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Intent intent = new Intent(this, LectureActivity.class);
                intent.putExtra("idMusique", idMusique);
                intent.putExtra("mesureDebut", mesureDebut);
                intent.putExtra("mesureFin", mesureFin);
                startActivity(intent);
            }
        }
    }
}
