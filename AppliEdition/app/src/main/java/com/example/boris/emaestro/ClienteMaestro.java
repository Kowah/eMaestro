package com.example.boris.emaestro;

import android.util.Log;
import android.widget.Toast;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
//télécommande
//créé avant les vacances, non fonctionnel
// mettre à jour avec version de guillaume
public class ClienteMaestro {

    private String serverMessage;
    public static final String SERVERIP = "148.60.13.97"; //your computer IP address
    public static final int SERVERPORT = 15555;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    Socket socket;
    PrintWriter out;
    BufferedReader in;

    ClienteMaestro (){
        ThreadClient t = new ThreadClient(this);
        t.start();
    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }

    public void stopClient(){
        mRun = false;
    }

    public void connect() {

        try {
            socket = new Socket(SERVERIP, SERVERPORT);

        }catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }
    }

    public void disconnect(){
       try{
           socket.close();
       }
       catch (Exception e) {
           Log.e("TCP","pb socket", e);
       }

    }

    public void run() {



    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}