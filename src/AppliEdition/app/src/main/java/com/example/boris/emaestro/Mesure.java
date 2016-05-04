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
    private boolean debutReprise;// affichage double barre double point en début
    private boolean barrePassage; //barre horizontale
    private boolean debutPassage;//pour afficher petite barre verticale en debut de passage
    private boolean premPassage;//mesure joué pendant 1passage
    private boolean finReprise;// affichage double barre double point en fin
    private boolean secPassage;//mesure joué pendant 2eme passage
    private boolean finPassage;//pour afficher petite barre verticale en fin de passage

    //armature
    private boolean diese;
    private boolean bemol;


    //Alertes
    private int couleur;
    private boolean alertePresente;
    private int alerteTpsDebut;

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

        this.diese=false;
        this.bemol=false;

        this.couleur=-1;
        this.alertePresente = false;
        this.alerteTpsDebut = 0;

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

    public void setDiese(boolean visible){this.diese=visible;
        this.bemol=!visible;}
    public void setBemol(boolean visible){this.bemol=visible;
        this.diese=!visible;}

public void setSansArmature(){
    this.bemol=false;
    this.diese=false;
}

    public void setAllReprise(boolean visible){
        this.setBarrePassage(visible);
        this.setDebutPassage(visible);
        this.setPremPassage(visible);
        this.setFinPassage(visible);
        this.setSecPassage(visible);
        this.setDebutReprise(visible);
        this.setFinReprise(visible);
    }

    public boolean getDebutReprise(){return debutReprise;}
    public boolean getBarrePassage(){return this.barrePassage;}
    public boolean getDebutPassage(){return this.debutPassage;}
    public boolean getPremPassage(){return this.premPassage;}
    public boolean getFinReprise(){return this.finReprise;}
    public boolean getSecPassage(){return this.secPassage;}
    public boolean getFinPassage(){return this.finPassage;}
    public boolean getDiese(){return this.diese;}
    public boolean getBemol(){return  this.bemol;}
    public void setCouleur(int couleur){this.couleur=couleur;}
    public void setAlertePresente(boolean present){ this.alertePresente = present;}
    public void setAlerteTpsDebut(int alerteTpsDebut){ this.alerteTpsDebut = alerteTpsDebut;}

    public  boolean getAlerte(){return this.alertePresente;}


}

