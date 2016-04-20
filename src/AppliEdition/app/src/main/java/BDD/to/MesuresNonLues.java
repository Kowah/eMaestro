package BDD.to;

import BDD.db.DataBaseHelper;

/**
 * Created by guillaume on 07/04/16.
 */
public class MesuresNonLues extends Evenement {

    public MesuresNonLues(){
        super();
    }

    public MesuresNonLues(int id, int idMusique,int mesure_debut ,int mesure_fin, int passage_reprise){
        this.id = id;
        this.idMusique = idMusique;
        this.flag = DataBaseHelper.FLAG_MNL;
        this.mesure_debut = mesure_debut;
        this.arg2 = mesure_fin;
        this.passage_reprise = passage_reprise;
        this.arg3 = -1;
    }

    public MesuresNonLues(int idMusique,int mesure_debut, int mesure_fin, int passage_reprise){
        this.idMusique = idMusique;
        this.flag = 1;
        this.mesure_debut = mesure_debut;
        this.arg2 = mesure_fin;
        this.passage_reprise = passage_reprise;
        this.arg3 = -1;
    }

    public int getMesure_fin() {return arg2;}

    public void setMesure_fin(int mesure_fin) {this.arg2 = mesure_fin;}
}
