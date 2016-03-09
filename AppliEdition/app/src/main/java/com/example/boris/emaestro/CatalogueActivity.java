package com.example.boris.emaestro;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import BDD.db.MusiqueDAO;
import BDD.to.Catalogue;
import BDD.to.Musique;

public class CatalogueActivity extends AppCompatActivity {
    CatalogueAdapter adapter;
    GridView mGridView;
    List<Musique> catalogue = new ArrayList<>();
    MusiqueDAO bddMusique = new MusiqueDAO(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musique_catalogue);
        bddMusique.open();
        catalogue = bddMusique.getMusiques();
        mGridView = (GridView) findViewById(R.id.gridView);

        adapter = new CatalogueAdapter(this,catalogue);
        mGridView.setAdapter(adapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                        //TODO afficher un popup proposant l'edition, suppression, ajout dans liste de lecture
            }
        });








    }

}
