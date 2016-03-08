package emaestro.emulateur;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

import database.MaestroBDD;
import database.RemplissageBdd;
import emaestro.testtelecommande.R;

public class MainActivity extends Activity {

    AnimationDrawable animation;
    ArrayList<Pair<String,Integer>> listeImages = new ArrayList();
    HashMap<String,Integer> banqueImages = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaestroBDD bdd = new MaestroBDD(this);
        bdd.open();
        RemplissageBdd.remplirDonneesTest(bdd);

        Chargeur_partition chargeur = new Chargeur_partition(this,bdd);
        chargeur.charger_partition(1);
        bdd.close();

        listeImages = (ArrayList) chargeur.get_liste_de_lecture();
        banqueImages = (HashMap) chargeur.get_images_ephemeres();

        int id = this.getResources().getIdentifier("emaestro.testtelecommande:drawable/black", null, null);
        listeImages.add(new Pair<String, Integer>("black", 1));
        banqueImages.put("black",id);

    }

    public void jouer(View view) {
        Resources res = this.getResources();

        AnimationDrawable anim = new AnimationDrawable();

        for(Pair<String,Integer> paire : listeImages){
            String image = paire.getLeft();
            int id = banqueImages.get(image); //On recupere l'id de l'image indique dans la liste
            int temps = paire.getRight(); //On recupere le bpm indique dans la liste
            temps = 60000/temps; //Conversion du BPM en ms
            BitmapDrawable drawable = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), id, null); //Creation d'une frame d'animation
            drawable.setAntiAlias(false);
            drawable.setFilterBitmap(false);
            anim.addFrame(drawable, temps); //Ajout de la frame a l'animation finale
        }


        ImageView image = (ImageView) findViewById(R.id.affichage);
        if(Build.VERSION.SDK_INT >= 16) {
            image.setBackground(anim);
        }else{
            //noinspection deprecation
            image.setBackgroundDrawable(anim);
        }
        anim.setOneShot(true);
        anim.start(); //Demarrage de l'animation



    }
}
