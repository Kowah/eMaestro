package com.example.boris.emaestro;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import BDD.to.VariationIntensite;
import BDD.to.VariationTemps;

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
    public void setNuance(int mesureDebut, int mesureFin, String nuance){
        for(int i=mesureDebut;i<mesureFin;i++){
            partition.get(i).setNuance(nuance);
        }
    }
    public void setTempo(List<Integer> l,int tempo){
        Integer j;
        for(int i=0;i<l.size();i++){
            j = l.get(i).intValue();
            partition.get(j-1).setTempo(tempo);
        }
    }
    public void setTempo(List<VariationTemps> l){
        VariationTemps vT;
        int mesureFin;
        int i;
        for(i =0; i<l.size();i++){
            vT = l.get(i);
            if(i+1<l.size()){
                //si ya dautre event, alors changmeent de tempo se fait jusqu'à jusqu'à l'arrivée du prochain
                mesureFin = l.get(i+1).getMesure_debut()-1;
            }
            else{
                //sinon jusqu'à la fin de la partition
                mesureFin = this.partition.size();
            }

            this.setTempo(vT.getMesure_debut(), mesureFin, vT.getTempo());

        }

    }

    public void setNuance(List<VariationIntensite> l){

        VariationIntensite vT;
        int mesureFin;
        int i;
        String nuance;
        for(i =0; i<l.size();i++){
            vT = l.get(i);
            if(i+1<l.size()){
                //si ya dautre event, alors changmeent de nuance se fait jusqu'à l'arrivée du prochain
                mesureFin = l.get(i+1).getMesureDebut()-1;
            }
            else{
                //sinon jusqu'à la fin de la partition
                mesureFin = this.partition.size();
            }
            nuance= convertNuanceIntStr(vT.getIntensite());
            this.setNuance(vT.getMesureDebut(), mesureFin, nuance);

        }
    }

    public String convertNuanceIntStr(int n) {
        String s;
        switch (n) {
            case 0:
                s = "fortississimo";
                break;
            case 1:
                s = "fortissimo";
                break;
            case 2:
                s = "forte";
                break;
            case 3:
                s = "mezzoforte";
                break;
            case 4:
                s = "mezzopiano";
                break;
            case 5:
                s = "piano";
                break;
            case 6:
                s = "pianissimo";
                break;
            case 7:
                s = "pianississimo";
                break;
            default:
                s = "neutre";
                break;

        }
        return s;
    }
    public int convertNuanceStrInt(String n) {
        int s;
        switch (n) {
            case "fortississimo":
                s = 0;
                break;
            case "fortissimo":
                s = 1;
                break;
            case "forte":
                s = 2;
                break;
            case "mezzoforte":
                s = 3;
                break;
            case "mezzopiano":
                s = 4;
                break;
            case "piano":
                s = 5;
                break;
            case "pianissimo":
                s = 6;
                break;
            case "pianississimo":
                s = 7;
                break;
            default:
                s = -10;
                break;

        }
        return s;


    }

    public void setNuance(List<Integer> l,String nuance){
        Integer j;
        for(int i=0;i<l.size();i++){
            j = l.get(i).intValue();
            partition.get(j-1).setNuance(nuance);
        }
    }
    public void unselectAll(){
        for(int i=0;i<partition.size();i++){
            partition.get(i).selectionne=false;
        }
    }
}
