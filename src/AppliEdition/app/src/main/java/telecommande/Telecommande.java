package telecommande;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boris.emaestro.R;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import BDD.db.CatalogueDAO;
import BDD.db.DataBaseManager;
import BDD.to.Musique;

public class Telecommande extends AppCompatActivity {

    Socket socketServeur = null;
    PrintWriter printerServeur = null;
    final String adresseIP = "192.168.103.1";
    final int port = 8192;
    int musiqueID = -1;
    ArrayList<Button> buttons = new ArrayList<>();
    Button connectButton=null;
    Spinner spinner =  null;
    CatalogueDAO bdd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telecommande);

        spinner = (Spinner)findViewById(R.id.musiqueSpinner);

        connectButton = (Button) findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });

        Button playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musiqueID = ((Musique)spinner.getSelectedItem()).getId();
                action("PLAY");
            }
        });

        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action("PAUSE");
            }
        });

        Button quitButton = (Button) findViewById(R.id.quitButton);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action("QUIT");
            }
        });

        Button shutdownButton = (Button) findViewById(R.id.shutdownButton);
        shutdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action("SHUTDOWN");
            }
        });

        buttons.add(playButton);
        buttons.add(stopButton);
        buttons.add(quitButton);
        buttons.add(shutdownButton);

        setConnected(false);

        bdd = new CatalogueDAO(this);
        bdd.open();
        final ArrayList<Musique> listMusique = bdd.getMusiques();

        final ArrayAdapter<Musique> arrayAdapter = new ArrayAdapter<Musique>(this,android.R.layout.simple_spinner_item, listMusique);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Musique selection = (Musique) parent.getItemAtPosition(position);

                musiqueID = selection.getId();
                spinner.setSelection(position);

                //Afficher le nombre de mesures du morceau selectionne
                TextView textNbMesures = (TextView) findViewById(R.id.textNbMesures);
                int mesureFin = bdd.getMusique(musiqueID).getNb_mesure();
                textNbMesures.setText("" + mesureFin);


                //Mise par defaut les mesures de debut et de fin
                EditText editMesureDebut = (EditText) findViewById(R.id.editMesureDebut);
                EditText editMesureFin = (EditText) findViewById(R.id.editMesureFin);

                editMesureDebut.setText("1");


                editMesureFin.setText("" + mesureFin);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void connect(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socketServeur = new Socket(adresseIP, port);
                    printerServeur = new PrintWriter(socketServeur.getOutputStream());

                    setConnected(true);
                    showToast("Connecté à" + adresseIP + ":" +port);

                } catch (IOException e) {
                    setConnected(false);
                    showToast("Impossible de se connecter à" + adresseIP + ":" + port);
                }
            }
        });
        thread.start();

    }

    public void setConnected(boolean connected) {
        for (Button b : buttons) {
            b.setClickable(connected);
        }
        spinner.setClickable(connected);
        connectButton.setClickable(!connected);

    }

    public void action(String message){

        if(message.equals("PLAY") && printerServeur != null) {
            if (musiqueID != -1) {//Si un morceau a ete choisi
                int mesureFinMusique = bdd.getMusique(musiqueID).getNb_mesure();
                EditText editMesureDebut = (EditText) findViewById(R.id.editMesureDebut);
                EditText editMesureFin = (EditText) findViewById(R.id.editMesureFin);
                int mesureDebut = Integer.parseInt(editMesureDebut.getText().toString());
                int mesureFin = Integer.parseInt(editMesureFin.getText().toString());

                //Erreur dans les indications de mesures pour la lecture
                if (mesureDebut <= 0 || mesureDebut > mesureFin || mesureFin > mesureFinMusique) {
                    if (mesureDebut <= 0) {
                        Toast.makeText(this, "La mesure de début doit valoir au minimum 1", Toast.LENGTH_LONG).show();
                    }
                    if (mesureDebut > mesureFin) {
                        Toast.makeText(this, "La mesure de début doit précéder celle de fin", Toast.LENGTH_LONG).show();
                    }
                    if (mesureFin > mesureFinMusique) {
                        Toast.makeText(this, "La mesure de fin doit au plus être la dernière mesure du morceau", Toast.LENGTH_LONG).show();
                    }
                } else {
                    printerServeur.write(musiqueID + "," + mesureDebut + "," + mesureFin);
                    printerServeur.flush();
                }

            }
        }
        if (printerServeur != null) {
            printerServeur.write(message);
            printerServeur.flush();
        }

        if(socketServeur != null && (message.equals("QUIT") || message.equals("SHUTDOWN")))
        {
            try {
                socketServeur.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setConnected(false);
            showToast("Deconnecté");
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if(socketServeur != null) {
                socketServeur.close();
            }
        } catch (IOException e) {
        }
        bdd.close();
        super.onDestroy();
    }

    public void showToast(final String toast){
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(Telecommande.this, toast, Toast.LENGTH_LONG).show();
            }
        });
    }
}
