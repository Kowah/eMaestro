package guillaume.telecommande;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Socket socketServeur = null;
    PrintWriter printerServeur = null;
    final String adresseIP = "192.168.103.1";
    final int port = 8192;
    ArrayList<Button> buttons = new ArrayList<>();
    Button connectButton=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
                jouer();
            }
        });

        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });

        Button quitButton = (Button) findViewById(R.id.quitButton);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quit();
            }
        });

        buttons.add(playButton);
        buttons.add(stopButton);
        buttons.add(quitButton);

        setConnected(false);

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

    public void setConnected(boolean connected){
        for(Button b : buttons) {
            b.setClickable(connected);
        }
        connectButton.setClickable(!connected);
    }

    public void jouer() {
        if (printerServeur != null) {
            printerServeur.write("PLAY");
            printerServeur.flush();
        }
    }

    public void stop(){
        if (printerServeur != null) {
            printerServeur.write("PAUSE");
            printerServeur.flush();
        }
    }

    public void quit(){
        if (printerServeur != null) {
            printerServeur.write("QUIT");
            printerServeur.flush();
        }

        if(socketServeur != null)
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
        super.onDestroy();
    }

    public void showToast(final String toast){
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, toast, Toast.LENGTH_LONG).show();
            }
        });
    }
}
