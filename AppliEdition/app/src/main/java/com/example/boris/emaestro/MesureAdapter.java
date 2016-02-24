package com.example.boris.emaestro;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MesureAdapter extends ArrayAdapter<Mesure> {


    //mesures est la liste des models à afficher
    public MesureAdapter(Context context, List<Mesure> mesures) {
        super(context, 0, mesures);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mesure,parent, false);
        }

        MesureViewHolder viewHolder = (MesureViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new MesureViewHolder();
            viewHolder.id = (TextView) convertView.findViewById(R.id.id);
           // viewHolder.m = (ImageView) convertView.findViewById(R.id.m);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Mesure> mesures
        Mesure mesure = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.id.setText(String.valueOf(mesure.getId()));
        //Si on veut modifier le background viewHolder.m.setBackground(Drawable.createFromPath("@drawable/mesure"));

        return convertView;
    }

    private class MesureViewHolder{
        public TextView id;
       // public ImageView m;
    }
}