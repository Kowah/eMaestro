package com.example.boris.emaestro;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import BDD.db.DataBaseManager;
import BDD.to.Catalogue;
import BDD.to.Musique;
import BDD.to.VariationIntensite;
import BDD.to.VariationTemps;

/**
 * Created by Boris on 09/03/2016.
 */
public class CatalogueAdapter extends ArrayAdapter<Musique> {

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
    Button editer;
    Button supprimer;
    Button jouer;
    //VariationTempsDAO bddTempsVar;
    //VariationIntensiteDAO bddIntensiteVar;
    DataBaseManager bdd;
    List<VariationIntensite> IntensiteList;
    List <VariationTemps> TempsList;
    //partition est la liste des models à afficher

    public CatalogueAdapter(Context context, List<Musique> catalogue) {
        super(context, 0, catalogue);
        this.catalogue = catalogue;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.partition,parent, false);
        }

        CatalogueViewHolder viewHolder = (CatalogueViewHolder) convertView.getTag();
         final Musique musique = catalogue.get(position);


        //on met a jour l'id de la view de la mesure
        ((TextView)convertView.findViewById(R.id.id)).setText(musique.getName());
        editer = (Button)convertView.findViewById(R.id.editer);
        editer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getContext(), EditionActivity.class);
                intent.putExtra(EXTRA_NOMPARTITION,musique.getName() );
                intent.putExtra(EXTRA_NBMESURE, String.valueOf(musique.getNb_mesure()));
                //intent.putExtra(EXTRA_PULSATION, String.valueOf(musique.getNb_pulsation()));
                //intent.putExtra(EXTRA_TPSPARMESURE,String.valueOf( musique.getNb_temps_mesure()));
                //intent.putExtra(EXTRA_UNITE, String.valueOf(musique.getUnite_pulsation()));
                intent.putExtra(EXTRA_ID_PARTITION,String.valueOf(musique.getId()));

                intent.putExtra(EXTRA_NEW_PARTITION,"false");
                Toast.makeText(getContext(), "id musique" + String.valueOf(musique.getId()), Toast.LENGTH_SHORT).show();

                getContext().startActivity(intent);
            }
        });
        supprimer = (Button) convertView.findViewById(R.id.supprimer);
        supprimer.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             //TODO Confirmation de suppression
                                             bdd = new DataBaseManager(v.getContext());
                                             bdd.open();
                                             bdd.delete(musique);
                                             bdd.close();
                                             catalogue.remove(musique);


                                             CatalogueAdapter adapter = new CatalogueAdapter(v.getContext(),catalogue);
                                             CatalogueActivity.mListView.setAdapter(adapter);
                                             //FIXME: Relance l'application pour mettre a jour la liste? Bonne idée?
                                             //Intent intent = new Intent(v.getContext() , CatalogueActivity.class);
                                             //v.getContext().startActivity(intent);
                                         }
                                     }
        );
        //TODO et le bouton Jouer ?

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
