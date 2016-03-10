package com.example.boris.emaestro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import BDD.db.MusiqueDAO;
import BDD.db.VariationTempsDAO;
import BDD.to.Catalogue;
import BDD.to.Musique;

public class CatalogueActivity extends AppCompatActivity {
    CatalogueAdapter adapter;
    ListView mListView;
    List<Musique> catalogue = new ArrayList<>();
    MusiqueDAO bddMusique = new MusiqueDAO(this);
    VariationTempsDAO b = new VariationTempsDAO(this);
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musique_catalogue);
        bddMusique.open();
        //b.open();
        catalogue = bddMusique.getMusiques();
        mListView = (ListView) findViewById(R.id.listView);
        for (int i =0; i <catalogue.size();i++){
            Toast.makeText(getApplicationContext(), "chargement musique "+  String.valueOf(catalogue.get(i).getId())+" nb variations temps :"+b.getVariationsTemps(catalogue.get(i)).size(), Toast.LENGTH_SHORT).show();

        }

        adapter = new CatalogueAdapter(this,catalogue);
        mListView.setAdapter(adapter);










    }

}
