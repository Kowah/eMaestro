package com.example.boris.emaestro;


import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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

    public Partition(String nbMesure){
        int id=1;
        int nbM = Integer.parseInt(nbMesure);

        int tpsParMesureM = 0;//Integer.parseInt(tpsParMesure);//TODO probleme
        partition = new ArrayList<Mesure>();

        for(int i=0;i<nbM;i++){
            partition.add(new Mesure(id));
            id++;
        }
    }

    public List<Mesure> getListMesures(){ return partition;}

    public Mesure getMesure(int id){ return partition.get(id);}

    public void setTempo(int mesureDebut, int mesureFin, int tempo){
        //mesureDebut est l'id de la mesure, donc toute première mesure id à 1
        //mesure de fin est inclus
        for(int i=mesureDebut-1;i<=mesureFin;i++){
            partition.get(i).setTempo(tempo);
        }
    }
    public void setNuance(int mesureDebut, int mesureFin, String nuance){

            //mesureDebut est l'id de la mesure, donc toute première mesure id à 1
        //mesure de fin est inclus
        for(int i=mesureDebut-1;i<=mesureFin;i++){
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

    public void setNbTempsAll(int nbTemps){
        for(int i=0;i<partition.size();i++){
            partition.get(i).setTempsMesure(nbTemps);
        }
    }

    public void setNbTemps(int mesureDebut, int mesureFin, int temps){
        //mesureDebut est l'id de la mesure, donc toute première mesure id à 1
        //mesure de fin est inclus
        for(int i=mesureDebut-1;i<=mesureFin;i++){
            partition.get(i).setTempsMesure(temps);
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
                mesureFin = l.get(i+1).getMesure_debut();//mesure juste avant le debut du prochain event
            }
            else{
                //sinon jusqu'à la fin de la partition
                mesureFin = this.partition.size()-1;
            }

            this.setTempo(vT.getMesure_debut(), mesureFin, vT.getTempo());
            this.setNbTemps(vT.getMesure_debut(), mesureFin, vT.getTemps_par_mesure());


        }

    }

    public void setTpsDebut(int mesureDebut, int mesureFin, int tpsDebut){
        //mesureDebut est l'id de la mesure, donc toute première mesure id à 1
        //mesure de fin est inclus
        for(int i=mesureDebut-1;i<=mesureFin;i++){
            if(i==mesureDebut){
                partition.get(i).setTpsDebutNuance(tpsDebut);
            }
            partition.get(i).setTpsDebutNuance(1);

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
                mesureFin = l.get(i+1).getMesureDebut();//mesure juste avant le debut du prochain event
            }
            else{
                //sinon jusqu'à la fin de la partition
                mesureFin = this.partition.size()-1;
            }
            nuance= convertNuanceIntStr(vT.getIntensite());
            this.setNuance(vT.getMesureDebut(), mesureFin, nuance);
            this.setTpsDebut(vT.getMesureDebut(), mesureFin, vT.getTempsDebut());

        }
    }


    public String convertUniteIntStr(int n){
        String s="";
        switch(n){
            case 1:
                s="ronde"; break;
            case 2:
                s="blanche"; break;
            case 4:
                s="noire"; break;
            case 8:
                s="croche"; break;
            case 11:
                s="ronde pointée"; break;
            case 21:
                s="blanche pointée"; break;
            case 41:
                s="noire pointée"; break;
            case 81:
                s="croche pointée"; break;
        }
        return s;
    }

    public int convertUniteStrInt(String n) {
        int s;
        switch (n) {
            case "ronde" :
                s = 1;
                break;
            case "blanche":
                s = 2;
                break;
            case "noire":
                s = 4;
                break;
            case "croche":
                s = 8;
                break;
            case "ronde pointee":
                s = 11;
                break;
            case "blanche pointee":
                s = 21;
                break;
            case "noire pointee":
                s = 41;
                break;
            case "croche pointee":
                s = 81;
                break;
            default:
                s = -1;
                break;

        }
        return s;

    }

    public String convertNuanceIntStr(int n) {
        String s;
        switch (n) {
            case 7:
                s = "fortississimo";
                break;
            case 6:
                s = "fortissimo";
                break;
            case 5:
                s = "forte";
                break;
            case 4:
                s = "mezzoforte";
                break;
            case 3:
                s = "mezzopiano";
                break;
            case 2:
                s = "piano";
                break;
            case 1:
                s = "pianissimo";
                break;
            case 0:
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
                s = 7;
                break;
            case "fortissimo":
                s = 6;
                break;
            case "forte":
                s = 5;
                break;
            case "mezzoforte":
                s = 4;
                break;
            case "mezzopiano":
                s = 3;
                break;
            case "piano":
                s = 2;
                break;
            case "pianissimo":
                s = 1;
                break;
            case "pianississimo":
                s = 0;
                break;
            default:
                s = -1;
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
