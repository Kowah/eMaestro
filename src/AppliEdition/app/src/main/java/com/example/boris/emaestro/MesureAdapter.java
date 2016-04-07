package com.example.boris.emaestro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MesureAdapter extends ArrayAdapter<Mesure> {
    String labelEvent;// pour drop
    int newTempo, mesureFin,mesureDebut;// changement de tempo
    String newNuance; // changement de nuance
    Partition partition;
    ArrayAdapter<String> nuanceAdapter;


    //partition est la liste des models à afficher
    public MesureAdapter(Context context, Partition partition, ArrayAdapter<String> nuanceAdapter) {
        super(context, 0, partition.getListMesures());
        this.partition = partition;
        this.nuanceAdapter = nuanceAdapter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mesure,parent, false);
        }

        MesureViewHolder viewHolder = (MesureViewHolder) convertView.getTag();
        Mesure mesure = partition.getMesure(position);

        //MAJ nuance
        switch(mesure.getNuance()){
            case "fortississimo":
                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.fortississimmo));
                break;
            case "fortissimo":
                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.fortissimo));
                break;
            case "forte":
                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.forte));
                break;
            case "mezzoforte":

                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.mezzoforte));
                break;
            case "neutre":

                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.neutre));
                break;
            case "mezzopiano":

                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.mezzopiano));
                break;
            case "piano":

                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.piano));
                break;
            case "pianissimo":

                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.pianissimo));
                break;
            case "pianississimo":

                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.pianississimo));
                break;

        };
        //Cadre jaune de selection
        //necessaire ici aussi car le recyclage des vues modifiait l'affichage des mesures selectionnees

        if(mesure.getSelec()) {
            convertView.findViewById(R.id.selection).setAlpha(0.7f);

        }else{
            convertView.findViewById(R.id.selection).setAlpha(0.0f);
        }
        //on met a jour l'id de la view de la mesure
        ((TextView)convertView.findViewById(R.id.id)).setText(String.valueOf(mesure.getId()));

        if(viewHolder == null){
            viewHolder = new MesureViewHolder();
            viewHolder.id = (TextView) convertView.findViewById(R.id.id);
            convertView.setTag(viewHolder);
        }
        //il ne reste plus qu'à remplir notre vue
        viewHolder.id.setText(String.valueOf(mesure.getId()));
        return convertView;
    }

    private class MesureViewHolder{
        public TextView id;
    }

}