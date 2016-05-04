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


    //partition est la liste des models à afficher
    public MesureAdapter(Context context, Partition partition) {
        super(context, 0, partition.getListMesures());
        this.partition = partition;
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
            case FORTISSISSIMO:
                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.fortississimo));
                break;
            case FORTISSIMO:
                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.fortissimo));
                break;
            case FORTE:
                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.forte));
                break;
            case MEZZOFORTE:
                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.mezzoforte));
                break;
            case NEUTRE:
                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.neutre));
                break;
            case MEZZOPIANO:
                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.mezzopiano));
                break;
            case PIANO:
                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.piano));
                break;
            case PIANISSIMO:
                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.pianissimo));
                break;
            case PIANISSISSIMO:
                convertView.findViewById(R.id.nuance).setBackgroundColor(this.getContext().getResources().getColor(R.color.pianississimo));
                break;

        };
        //Cadre jaune de selection
        //necessaire ici aussi car le recyclage des vues modifiait l'affichage des mesures selectionnees

       /* if(mesure.getSelec()) {
            convertView.findViewById(R.id.selection).setAlpha(0.7f);

        }else{
            convertView.findViewById(R.id.selection).setAlpha(0.0f);
        }*/

        if(mesure.getEventTpsSurMesure()){
            convertView.findViewById(R.id.tempoModif).setAlpha(1f);
            TextView t =(TextView)convertView.findViewById(R.id.tempoModif);
            t.setText(mesure.getTempo() + "\n" + mesure.getTempsMesure() + " temps");
        }else{
            convertView.findViewById(R.id.tempoModif).setAlpha(0f);
        }
        if(mesure.getDebutReprise()){
            convertView.findViewById(R.id.debutReprise).setAlpha(1f);

        }else{
            convertView.findViewById(R.id.debutReprise).setAlpha(0f);
        }

        if(mesure.getFinReprise()){
            convertView.findViewById(R.id.finReprise).setAlpha(1f);

        }else{
            convertView.findViewById(R.id.finReprise).setAlpha(0f);
        }
        if(mesure.getBarrePassage()){
            convertView.findViewById(R.id.barrePassage).setAlpha(1f);
        }else{
            convertView.findViewById(R.id.barrePassage).setAlpha(0f);
        }

        if(mesure.getDebutPassage()){
            convertView.findViewById(R.id.debutPassage).setAlpha(1f);
        }else{
            convertView.findViewById(R.id.debutPassage).setAlpha(0f);
        }
        if(mesure.getFinPassage()){
            convertView.findViewById(R.id.finPassage).setAlpha(1f);
        }else{
            convertView.findViewById(R.id.finPassage).setAlpha(0f);
        }
        if(mesure.getPremPassage()){
            convertView.findViewById(R.id.PrePassage).setAlpha(1f);
        }else{
            convertView.findViewById(R.id.PrePassage).setAlpha(0f);
        }
        if(mesure.getSecPassage()){
            convertView.findViewById(R.id.SecPassage).setAlpha(1f);
        }else{
            convertView.findViewById(R.id.SecPassage).setAlpha(0f);
        }

        if(mesure.getDiese()){
            convertView.findViewById(R.id.diese).setAlpha(1f);
        }else{
            convertView.findViewById(R.id.diese).setAlpha(0f);

        }
        if(mesure.getBemol()){
            convertView.findViewById(R.id.bemol).setAlpha(1f);
        }else{
            convertView.findViewById(R.id.bemol).setAlpha(0f);

        }
        if(mesure.getAlerte()){
            convertView.findViewById(R.id.alerte).setAlpha(1f);
        }else{
            convertView.findViewById(R.id.alerte).setAlpha(0f);

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