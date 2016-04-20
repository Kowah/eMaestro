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
import BDD.to.Reprise;
import BDD.to.VariationIntensite;

/**
 * Created by Boris on 20/04/2016.
 */
public class EventRepriseAdapter extends ArrayAdapter<Reprise> {

    List<Reprise> events;
    Reprise event;
    Button editer;
    Button supprimer;
    DataBaseManager bdd;

    public EventRepriseAdapter(Context context, List<Reprise> events) {
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
        ((TextView) convertView.findViewById(R.id.info)).setText("Reprise de la mesure n°"+event.getMesure_debut() + " à la mesure n°"+event.getMesure_fin());


        supprimer = (Button) convertView.findViewById(R.id.supprimer);
        supprimer.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                                 bdd = new DataBaseManager(v.getContext());
                                                 bdd.open();
                                                 bdd.delete(event);
                                                 bdd.close();
                                                 events.remove(event);
                                                 EventRepriseAdapter adapter = new EventRepriseAdapter(v.getContext(), events);
                                                 EditionActivity.eventRepriseListView.setAdapter(adapter);

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