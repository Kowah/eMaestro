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

    //variable pour affichage reprise
    private boolean debutReprise;
    private boolean barrePassage;
    private boolean debutPassage;
    private boolean premPassage;
    private boolean finReprise;
    private boolean secPassage;
    private boolean finPassage;

    public Mesure( int id){
        this.id = id;
        this.nuance = Nuance.NEUTRE;
        this.unite = "1";
        this.tpsDebutNuance=1;
        this.armature=0;

        this.debutPassage=false;
        this.debutReprise=false;
        this.barrePassage = false;
        this.premPassage = false;
        this.finPassage=false;
        this.finReprise = false;
        this.secPassage= false;

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

    public void setDebutReprise(boolean visible){
        this.debutReprise=visible;
    }
    public void setBarrePassage(boolean visible){
        this.barrePassage=visible;
    }
    public void setDebutPassage(boolean visible){
        this.debutPassage = visible;
    }
    public void setPremPassage(boolean visible){
        this.premPassage = visible;
    }
    public void setFinReprise(boolean visible){
        this.finReprise = visible;
    }
    public void setSecPassage(boolean visible){
        this.secPassage = visible;
    }
    public void setFinPassage(boolean visible){
        this.finPassage = visible;
    }

    public boolean getDebutReprise(){return debutReprise;}
    public boolean getBarrePassage(){return this.barrePassage;}
    public boolean getDebutPassage(){return this.debutPassage;}
    public boolean getPremPassage(){return this.premPassage;}
    public boolean getFinReprise(){return this.finReprise;}
    public boolean getSecPassage(){return this.secPassage;}
    public boolean getFinPassage(){return this.finPassage;}
}
