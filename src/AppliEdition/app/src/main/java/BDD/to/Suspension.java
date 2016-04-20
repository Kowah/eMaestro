package BDD.to;

import BDD.db.DataBaseHelper;

/**
 * Created by guillaume on 07/04/16.
 */
public class Suspension extends Evenement {

    public Suspension(){
        super();
    }

    public Suspension(int id, int idMusique,int mesure_debut, int temps, int duree, int passage_reprise){
        this.id = id;
        this.idMusique = idMusique;
        this.flag = DataBaseHelper.FLAG_SUSPENSION;
        this.mesure_debut = mesure_debut;
        this.arg2 = temps;
        this.passage_reprise = passage_reprise;
        this.arg3 = duree;
    }
    public Suspension(int idMusique,int mesure_debut, int temps, int duree, int passage_reprise){
        this.idMusique = idMusique;
        this.flag = DataBaseHelper.FLAG_SUSPENSION;
        this.mesure_debut = mesure_debut;
        this.arg2 = temps;
        this.passage_reprise = passage_reprise;
        this.arg3 = duree;
    }

    public int getTemps() {return arg2;}
    public int getDuree() {return (int) arg3;}
    public int getPassage_reprise() {return passage_reprise;}

    public void setTemps(int temps) {this.arg2 = temps;}
    public void setDuree(int duree) {this.arg3 = duree;}
    public void setPassage_reprise(int passage_reprise) {this.passage_reprise = passage_reprise;}
}
