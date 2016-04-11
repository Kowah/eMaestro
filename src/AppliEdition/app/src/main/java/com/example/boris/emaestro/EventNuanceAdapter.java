package com.example.boris.emaestro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import BDD.db.DataBaseManager;
import BDD.to.VariationIntensite;

/**
 * Created by Boris on 06/04/2016.
 */
public class EventNuanceAdapter extends ArrayAdapter<VariationIntensite>{

    List<VariationIntensite> events;
    List<VariationIntensite> eventDeLaMesure;
    VariationIntensite event;
    Button editer;
    Button supprimer;
    DataBaseManager bdd;

    public EventNuanceAdapter(Context context, List<VariationIntensite> events) {
        super(context, 0, events);
        this.events = events;
    }





    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.evenement,parent, false);
        }

        EventViewHolder viewHolder = (EventViewHolder) convertView.getTag();
        event = events.get(position);
        ((TextView) convertView.findViewById(R.id.info)).setText(event.getIntensite() + " sur le temps " + event.getTempsDebut()+1);


        editer = (Button)convertView.findViewById(R.id.editer);
        editer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO a faire



            }
        });

        supprimer = (Button) convertView.findViewById(R.id.supprimer);
        supprimer.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             bdd = new DataBaseManager(v.getContext());
                                             bdd.open();
                                             bdd.delete(event);
                                             bdd.close();
                                             events.remove(event);
                                             EventNuanceAdapter adapter = new EventNuanceAdapter(v.getContext(), events);
                                             EditionActivity.eventNuanceListView.setAdapter(adapter);

                                         }
                                     }
        );


        if(viewHolder == null){
            viewHolder = new EventViewHolder();
            viewHolder.nom = (TextView) convertView.findViewById(R.id.id);
            convertView.setTag(viewHolder);
        }

        return convertView;
    }

    private class EventViewHolder{
        public TextView nom;
    }
}
