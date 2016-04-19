package com.example.boris.emaestro;


import util.Nuance;

public class Mesure {
    private int id;
    private int tempo;
    private Nuance nuance;
    private int tempsMesure;
    private String unite;
    private int tpsDebutNuance;
    private int armature;
    private boolean eventTpsSurMesure;//indique si un event de temps est présent sur la mesure, sert pour l'affichage

    boolean selectionne;

    public Mesure( int id){
        this.id = id;
        this.nuance = Nuance.NEUTRE;
        selectionne = false;
        this.unite = "1";
        this.tpsDebutNuance=1;
        this.armature=0;

    }
//TODO gestion des events

    public void setEventTpsSurMesure(boolean present){
        this.eventTpsSurMesure = present;
    }
    public boolean getEventTpsSurMesure(){
        return this.eventTpsSurMesure;
    }

    public void setArmature(int armature){
        this.armature = armature;
    }
    public void setId(int newId){
        this.id = newId;
    }
    public int getId(){return id;}

    public void setTempo(int newTempo){
        this.tempo = newTempo;
    }
    public int getTempsMesure(){
        return  tempsMesure;
    }
    public Nuance getNuance(){
        return nuance;
    }
    public void setNuance(Nuance newNuance){ this.nuance = newNuance;}
    public int getTempo(){return tempo;}
    public void setTempsMesure(int newTempsMesure){
        this.tempsMesure = newTempsMesure;
    }
    public void setUnite(String newUnite){this.unite = newUnite;}
    public String getUnite() {return unite;}
    public void setTpsDebutNuance(int temps){this.tpsDebutNuance = temps;}



}
