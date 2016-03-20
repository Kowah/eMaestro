package emulateur;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boris.emaestro.R;

import java.util.ArrayList;

import BDD.db.MusiqueDAO;
import BDD.db.VariationTempsDAO;
import BDD.to.Musique;
import util.Pair;

/**
 * Created by Boris on 16/03/2016.
 */
public class EmulateurActivity extends Activity {



    int idMusique=-1;
    MusiqueDAO bddMusique ;
    VariationTempsDAO bddTemps ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emulateur);


        bddMusique = new MusiqueDAO(this);
        bddMusique.open();
        bddTemps = new VariationTempsDAO(this);
        bddTemps.open();


        initSpinnerMusique();

    }

    @Override
    protected void onDestroy() {
        bddMusique.close();
        bddTemps.close();
        super.onDestroy();
    }

    private void initSpinnerMusique() {

        final ArrayList<Musique> listMusique = bddMusique.getMusiques();

        final ArrayAdapter<Musique> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listMusique);

        final Spinner spinner = (Spinner)findViewById(R.id.spinnerMusique);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //TODO a corriger
               /* Pair<Integer, String> selection = (Pair<Integer, String>) parent.getItemAtPosition(position);

                idMusique = selection.getLeft();
                spinner.setSelection(position);

                //Afficher le nombre de mesures du morceau selectionne
                TextView textNbMesures = (TextView)findViewById(R.id.textNbMesures);
                int mesureFin = bddMusique.getMusique(idMusique).getNb_mesure();
                textNbMesures.setText(""+mesureFin);

                //Mise par defaut les mesures de debut et de fin
                EditText editMesureDebut = (EditText)findViewById(R.id.editMesureDebut);
                EditText editMesureFin = (EditText)findViewById(R.id.editMesureFin);

                editMesureDebut.setText("1");


                editMesureFin.setText(""+mesureFin);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void jouer(View view) {
        if(idMusique != -1) {//Si un morceau a ete choisi
            int mesureFinMusique =  bddMusique.getMusique(idMusique).getNb_mesure();
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
