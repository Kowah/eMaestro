package com.example.boris.emaestro;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import BDD.db.MusiqueDAO;
import BDD.db.VariationTempsDAO;
import BDD.to.Musique;

public class CatalogueActivity extends Activity {
    CatalogueAdapter adapter;
    ListView mListView;
    List<Musique> catalogue = new ArrayList<>();
    //MusiqueDAO bddMusique = new MusiqueDAO(this);
    //VariationTempsDAO b = new VariationTempsDAO(this);
    DataBaseManager bdd = new DataBaseManager(this);
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musique_catalogue);
        //bddMusique.open();
        //b.open();
        bdd.open();
        catalogue = bdd.getMusiques();
        mListView = (ListView) findViewById(R.id.listView);
        for (int i =0; i <catalogue.size();i++){
            System.out.println(catalogue.get(i).getName());
            Toast.makeText(getApplicationContext(), "chargement musique "+  String.valueOf(catalogue.get(i).getId())+" nb variations temps :"+bdd.getVariationsTemps(catalogue.get(i)).size(), Toast.LENGTH_SHORT).show();

        }
        bdd.close();
        adapter = new CatalogueAdapter(this,catalogue);
        mListView.setAdapter(adapter);




    }

}
