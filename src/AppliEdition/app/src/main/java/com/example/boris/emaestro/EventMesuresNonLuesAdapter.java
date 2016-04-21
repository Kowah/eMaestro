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
import BDD.to.Armature;
import BDD.to.MesuresNonLues;

/**
 * Created by Boris on 21/04/2016.
 */
public class EventMesuresNonLuesAdapter extends ArrayAdapter<MesuresNonLues> {

    List<MesuresNonLues> events;
    MesuresNonLues event;
    Button supprimer;
    DataBaseManager bdd;

    public EventMesuresNonLuesAdapter(Context context, List<MesuresNonLues> events) {
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
        ((TextView) convertView.findViewById(R.id.info)).setText("Mesures n°"+event.getMesure_debut()+" à n°"+event.getMesure_fin()+ " sont non jouées");


        supprimer = (Button) convertView.findViewById(R.id.supprimer);
        supprimer.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             bdd = new DataBaseManager(v.getContext());
                                             bdd.open();
                                             bdd.delete(event);
                                             bdd.close();
                                             events.remove(event);
                                             EventMesuresNonLuesAdapter adapter = new EventMesuresNonLuesAdapter(v.getContext(), events);
                                             EditionActivity.eventMesuresNonLuesListView.setAdapter(adapter);
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