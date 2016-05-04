package com.example.boris.emaestro;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import BDD.db.DataBaseManager;
import BDD.to.Musique;

public class ChangeCatalogueActivity extends Activity {
    ChangeCatalogueAdapter adapter;
    static public ListView mListView;
    TextView mText;
    List<Musique> catalogue = new ArrayList<>();
    DataBaseManager bdd = new DataBaseManager(this);
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musique_catalogue);
        bdd.open();
        catalogue = bdd.getMusiques();
        mListView = (ListView) findViewById(R.id.listView);
        mText = (TextView) findViewById(R.id.id);
        mText.setText("Séléctionnez les ePartitions que vous souhaitez ajouter/enlever du catalogue de ePartitions à envoyer sur la MaestroBox");
        bdd.close();
        adapter = new ChangeCatalogueAdapter(this,catalogue);
        mListView.setAdapter(adapter);
    }

}
