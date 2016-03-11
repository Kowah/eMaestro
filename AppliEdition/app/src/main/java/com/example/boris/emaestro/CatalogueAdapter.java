package com.example.boris.emaestro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import BDD.to.Catalogue;
import BDD.to.Musique;

/**
 * Created by Boris on 09/03/2016.
 */
public class CatalogueAdapter extends ArrayAdapter<Musique> {

    String labelEvent;// pour drop
    int newTempo, mesureFin,mesureDebut;// changement de tempo
    String newNuance; // changement de nuance
    Partition partition;

   List<Musique> catalogue;


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
         Musique musique = catalogue.get(position);



        //on met a jour l'id de la view de la mesure
        ((TextView)convertView.findViewById(R.id.id)).setText(musique.getName());

        if(viewHolder == null){
            viewHolder = new CatalogueViewHolder();
            viewHolder.nom = (TextView) convertView.findViewById(R.id.id);
            convertView.setTag(viewHolder);
        }
        //il ne reste plus qu'à remplir notre vue
       // viewHolder.id.setText(String.valueOf(mesure.getId()));
        //Si on veut modifier le background viewHolder.m.setBackground(Drawable.createFromPath("@drawable/mesure"));
      //TODO  convertView.setOnClickListener
        return convertView;
    }

    private class CatalogueViewHolder{
        public TextView nom;
    }



}
