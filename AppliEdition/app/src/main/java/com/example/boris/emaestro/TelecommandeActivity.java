package com.example.boris.emaestro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
//télécommande
//créé avant les vacances, non fonctionnel
// mettre à jour avec version de guillaume

public class TelecommandeActivity extends Activity {

    ClienteMaestro c =null;

    /*public void connect(){
        if(!c.connect()){
            Toast.makeText(this,"Erreur de connexion",Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this,"Connexion établie",Toast.LENGTH_SHORT).show();
        }
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telecommande);
        c = new ClienteMaestro();
        //connect();

       /* final ImageButton reconnexion = (ImageButton) findViewById(R.id.reconnexion);
        reconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();

            }
        });*/

        final ImageButton play = (ImageButton) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.sendMessage("PLAY");

            }
        });

        final ImageButton stop = (ImageButton) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.sendMessage("STOP");

            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        c.disconnect();
    }


}
