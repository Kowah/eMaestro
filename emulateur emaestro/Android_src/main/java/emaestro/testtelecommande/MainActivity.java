package emaestro.testtelecommande;

import android.app.Activity;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends Activity {

    AnimationDrawable animation;
    ArrayList<Pair<String,Integer>> listeImages = new ArrayList();
    HashMap<String,Integer> banqueImages = new HashMap<>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        for(int i=1; i<=4; i++) {
            int id = getResources().getIdentifier("emaestro.testtelecommande:drawable/a" + 4 + "_" + i, null, null);
            banqueImages.put("a4_" + i, id);
        }

        listeImages.add(new Pair<String, Integer>("a4_1",400));
        listeImages.add(new Pair<String, Integer>("a4_2",400));
        listeImages.add(new Pair<String, Integer>("a4_3",400));
        listeImages.add(new Pair<String, Integer>("a4_4",400));

        /*
        Chargeur_partition chargeur = new Chargeur_partition(this);

        chargeur.charger_partition(2);
        listeImages = (ArrayList) chargeur.get_liste_de_lecture();
        banqueImages = (HashMap) chargeur.get_images_ephemeres();
        */
    }

    public void jouer(View view) {

        Resources res = this.getResources();

        AnimationDrawable anim = new AnimationDrawable();

        for(Pair<String,Integer> paire : listeImages){
            String image = paire.getLeft();
            int id = banqueImages.get(image);
            int temps = paire.getRight();
            BitmapDrawable drawable = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), id, null);
            drawable.setAntiAlias(false);
            drawable.setFilterBitmap(false);
            anim.addFrame(drawable, temps);
        }


        ImageView image = (ImageView) findViewById(R.id.affichage);
        if(Build.VERSION.SDK_INT >= 16) {
            image.setBackground(anim);
        }else{
            //noinspection deprecation
            image.setBackgroundDrawable(anim);
        }
        anim.setOneShot(false);
        anim.start();

    }
}
