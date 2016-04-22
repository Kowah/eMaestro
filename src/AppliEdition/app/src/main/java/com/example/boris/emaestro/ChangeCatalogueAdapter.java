package com.example.boris.emaestro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import BDD.db.CatalogueDAO;
import BDD.db.DataBaseManager;
import BDD.to.Catalogue;
import BDD.to.Musique;
import BDD.to.VariationIntensite;
import BDD.to.VariationTemps;
import emulateur.EmulateurActivity;

/**
 * Created by guillaume on 22/04/16.
 */
public class ChangeCatalogueAdapter extends ArrayAdapter<Musique> {
    final String EXTRA_NOMPARTITION="vide";
    final String EXTRA_NBMESURE="nbMesure";
    final String EXTRA_PULSATION="pulsation";

    final String EXTRA_DRAGACTIF="drag";
    final String EXTRA_ID_PARTITION="idMusique";
    final String EXTRA_NEW_PARTITION="new";

    String labelEvent;// pour drop
    int newTempo, mesureFin,mesureDebut;// changement de tempo
    String newNuance; // changement de nuance
    Partition partition;
    List<Musique> catalogue;
    Button addCatalogue;
    Button removeCatalogue;
    DataBaseManager bdd;

    //partition est la liste des models à afficher

    public ChangeCatalogueAdapter(Context context, List<Musique> catalogue) {
        super(context, 0, catalogue);
        this.catalogue = catalogue;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.partition_catalogue,parent, false);
        }

        CatalogueViewHolder viewHolder = (CatalogueViewHolder) convertView.getTag();
        final Musique musique = catalogue.get(position);


        //on met a jour l'id de la view de la mesure
        ((TextView)convertView.findViewById(R.id.id)).setText(musique.getName());
        addCatalogue = (Button) convertView.findViewById(R.id.add);
        removeCatalogue = (Button) convertView.findViewById(R.id.remove);
        CatalogueDAO bddCatalogue = new CatalogueDAO(getContext());
        bddCatalogue.open();
        List<Integer> idMusique = bddCatalogue.getIdMusiques();
        if(idMusique.contains(musique.getId())){addCatalogue.setVisibility(View.GONE);}
        else {removeCatalogue.setVisibility(View.GONE);}
        bddCatalogue.close();


        addCatalogue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CatalogueDAO bddCatalogue = new CatalogueDAO(v.getContext());
                bddCatalogue.open();
                bddCatalogue.save(musique);
                bddCatalogue.close();
                ChangeCatalogueAdapter adapter = new ChangeCatalogueAdapter(v.getContext(),catalogue);
                ChangeCatalogueActivity.mListView.setAdapter(adapter);
            }
        });
        removeCatalogue.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                CatalogueDAO bddCatalogue = new CatalogueDAO(v.getContext());
                bddCatalogue.open();
                bddCatalogue.delete(musique);
                bddCatalogue.close();
                ChangeCatalogueAdapter adapter = new ChangeCatalogueAdapter(v.getContext(),catalogue);
                ChangeCatalogueActivity.mListView.setAdapter(adapter);
            }
        });

        if(viewHolder == null){
            viewHolder = new CatalogueViewHolder();
            viewHolder.nom = (TextView) convertView.findViewById(R.id.id);
            convertView.setTag(viewHolder);
        }
        return convertView;
    }

    private class CatalogueViewHolder{
        public TextView nom;
    }
}
