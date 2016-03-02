package com.example.boris.emaestro;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Boris on 02/03/2016.
 */
public class Partition {

    List<Mesure> partition;

    public Partition(){
        partition = new ArrayList<>();
    }

    public Partition(String nbMesure,String tempo,String tpsParMesure,String unite){
        int id=1;
        int nbM = Integer.parseInt(nbMesure);
        int tempoM =Integer.parseInt(tempo);
        int tpsParMesureM = 0;//Integer.parseInt(tpsParMesure);//TODO probleme
        partition = new ArrayList<Mesure>();

        for(int i=0;i<nbM;i++){
            partition.add(new Mesure(id, tempoM, tpsParMesureM, unite));
            id++;
        }
    }

    public List<Mesure> getListMesures(){ return partition;}

    public Mesure getMesure(int id){ return partition.get(id);}
    public void setTempo(int mesureDebut, int mesureFin, int tempo){
        for(int i=mesureDebut-1;i<mesureFin;i++){
            partition.get(i).setTempo(tempo);
        }
    }
    public void setTempo(List<Integer> l,int tempo){
        for(int i=0;i<l.size();i++){
            partition.get(i).setTempo(tempo);
        }
    }
}
