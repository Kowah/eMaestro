package BDD.to;


import BDD.db.DataBaseHelper;

public class Armature extends Evenement {

    public Armature(){
        super();
    }

    public Armature(int id, int idMusique,int mesure_debut, int temps_debut, int alteration, int passage_reprise){
        this.id = id;
        this.idMusique = idMusique;
        this.flag = DataBaseHelper.FLAG_ARMATURE;
        this.mesure_debut = mesure_debut;
        this.arg2 = temps_debut;
        this.passage_reprise = passage_reprise;
        this.arg3 = alteration;
    }
    public Armature(int idMusique,int mesure_debut, int temps_debut, int alteration, int passage_reprise){
        this.idMusique = idMusique;
        this.flag = DataBaseHelper.FLAG_ARMATURE;
        this.mesure_debut = mesure_debut;
        this.arg2 = temps_debut;
        this.passage_reprise = passage_reprise;
        this.arg3 = alteration;
    }

    public int getTemps_debut() {return arg2;}
    public int getAlteration() {return (int) arg3;}

    public void setTemps_debut(int temps_debut) {this.arg2 = temps_debut;}
    public void setAlteration(int alteration) {this.arg3 = alteration;}
}

