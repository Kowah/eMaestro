package com.example.boris.emaestro;


import android.widget.ImageButton;

public class Mesure {
    private int id;
    private int tempo;
    private String nuance;
    private int tempsMesure;
    private String unite;

    boolean selectionne;

    public Mesure( int id){
        this.id = id;
        this.nuance = "neutre";
        selectionne = false;

    }
//TODO gestion des events

    public void setId(int newId){
        this.id = newId;
    }
    public int getId(){return id;}

    public void setTempo(int newTempo){
        this.tempo = newTempo;
    }
    public String getNuance(){
        return nuance;
    }
    public void setNuance(String newNuance){ this.nuance = newNuance;}
    public int getTempo(){return tempo;}
    public void setTempsMesure(int newTempsMesure){
        this.tempsMesure = newTempsMesure;
    }
    public void setUnite(String newUnite){
        this.unite = newUnite
        ;
    }
    public void toggleSelec(){
        selectionne = ! selectionne;
    }
    public boolean getSelec(){
        return selectionne;
    }

}
