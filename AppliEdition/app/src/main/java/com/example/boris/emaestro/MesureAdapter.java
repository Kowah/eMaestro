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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MesureAdapter extends ArrayAdapter<Mesure> {
    String labelEvent;// pour drop
    int newTempo, mesureFin,mesureDebut;// changement de tempo
    int newNuance; // changement de nuance
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
        //Si on veut modifier le background viewHolder.m.setBackground(Drawable.createFromPath("@drawable/mesure"));
        convertView.setOnDragListener(new MesureDragListener(viewHolder));
        return convertView;
    }

    private class MesureViewHolder{
        public TextView id;
    }

//drag Listener
    private class MesureDragListener implements View.OnDragListener {
        private MesureViewHolder viewHolder;

    public MesureDragListener(MesureViewHolder a){
        viewHolder = a;
    }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    //TODO appliquer le bon effet pour chaque bouton
                  labelEvent = event.getClipData().getDescription().getLabel().toString();
                    switch(labelEvent) {
                        case "tempo":
                            View layout = LayoutInflater.from(getContext()).inflate(R.layout.popup_changement_tempo_drag, null);
                            final EditText editTempo = (EditText) layout.findViewById(R.id.tempo);
                            final EditText finTempo = (EditText) layout.findViewById(R.id.mesureFin);
                            new AlertDialog.Builder(getContext())
                                .setTitle("Changement de tempo")
                                .setView(layout)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        newTempo = Integer.parseInt(editTempo.getText().toString());
                                        mesureFin = Integer.parseInt(finTempo.getText().toString());
                                        mesureDebut = Integer.parseInt(viewHolder.id.getText().toString());
                                        //TODO ajouter evenement dans bdd
                                        if (mesureFin > partition.getListMesures().size()) {
                                            Toast.makeText(getContext(), "la Mesure de fin que vous avez choisie n'existe pas", Toast.LENGTH_SHORT).show();//TODO gestion à l'echelle de une mesure

                                        }
                                        else if(mesureFin<mesureDebut) {
                                            Toast.makeText(getContext(), "la Mesure de fin que vous avez choisie est située avant la mesure de début choisie", Toast.LENGTH_SHORT).show();//TODO gestion à l'echelle de une mesure
                                        }
                                        else {
                                            partition.setTempo(mesureDebut, mesureFin, newTempo);
                                            Toast.makeText(getContext(), "le tempo des mesures [" + mesureDebut + "," + mesureFin + "] = " + newTempo, Toast.LENGTH_SHORT).show();//TODO gestion à l'echelle de une mesure
                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                                break;
                        case "nuance" :
                            /* layout = LayoutInflater.from(getContext()).inflate(R.layout.popup_changement_nuance, null);
                             final EditText finNuance = (EditText) layout.findViewById(R.id.mesureFin);
                             new AlertDialog.Builder(getContext())
                                    .setTitle("Changement de Nuance")
                                    .setView(layout)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            newNuance = Integer.parseInt(editTempo.getText().toString());
                                           mesureFin = Integer.parseInt(finTempo.getText().toString());
                                            mesureDebut = Integer.parseInt(viewHolder.id.getText().toString());
                                            //TODO ajouter evenement dans bdd
                                            if (mesureFin > partition.getListMesures().size()) {
                                                Toast.makeText(getContext(), "la Mesure de fin que vous avez choisie n'existe pas", Toast.LENGTH_SHORT).show();//TODO gestion à l'echelle de une mesure

                                            } else {
                                                partition.setTempo(mesureDebut, mesureFin, newTempo);
                                                Toast.makeText(getContext(), "le tempo des mesures [" + mesureDebut + "," + mesureFin + "] = " + newTempo, Toast.LENGTH_SHORT).show();//TODO gestion à l'echelle de une mesure
                                            }
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();*/
                            break;

                    }
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        }
    }
}