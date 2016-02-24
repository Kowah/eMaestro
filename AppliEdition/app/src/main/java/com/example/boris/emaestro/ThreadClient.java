package com.example.boris.emaestro;

/**
 * Created by Boris on 10/02/2016.
 */
//télécommande
//créé avant les vacances, non fonctionnel
// mettre à jour avec version de guillaume
public class ThreadClient extends Thread {
    ClienteMaestro c;

    ThreadClient(ClienteMaestro c){
        this.c=c;
    }

    @Override
   public void run(){
        c.connect();
    }
}
