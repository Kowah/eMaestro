package BDD.to;

import BDD.db.DataBaseHelper;

/**
 * Created by guillaume on 07/04/16.
 */
public class Reprise extends Evenement {

    public Reprise(){
        super();
    }
    public Reprise(int id, int idMusique,int mesure_debut, int mesure_fin){
            this.id = id;
            this.idMusique = idMusique;
            this.flag = DataBaseHelper.FLAG_REPRISE;
            this.mesure_debut = mesure_debut;
            this.arg2 = mesure_fin;
            this.passage_reprise = -1;
            this.arg3 = -1;
    }
    public Reprise(int idMusique,int mesure_debut, int mesure_fin){
        this.idMusique = idMusique;
        this.flag = DataBaseHelper.FLAG_REPRISE;
        this.mesure_debut = mesure_debut;
        this.arg2 = mesure_fin;
        this.passage_reprise = -1;
        this.arg3 = -1;
    }

    public int getMesure_fin() {return arg2;}
    public void setMesure_fin(int mesure_fin) {this.arg2 = mesure_fin;}
}
