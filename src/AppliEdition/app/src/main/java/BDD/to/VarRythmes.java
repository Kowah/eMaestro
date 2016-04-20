package BDD.to;


import BDD.db.DataBaseHelper;

public class VarRythmes extends Evenement{

    public VarRythmes(){
        super();
    }

    public VarRythmes(int id, int idMusique,int mesure_debut, int temps_debut, float delta, int passage_reprise){
        this.id = id;
        this.idMusique = idMusique;
        this.flag = DataBaseHelper.FLAG_VARRYTHMES;
        this.mesure_debut = mesure_debut;
        this.arg2 = temps_debut;
        this.passage_reprise = passage_reprise;
        this.arg3 = delta;
    }
    public VarRythmes(int idMusique,int mesure_debut, int temps_debut, float delta, int passage_reprise){
        this.idMusique = idMusique;
        this.flag = DataBaseHelper.FLAG_VARRYTHMES;
        this.mesure_debut = mesure_debut;
        this.arg2 = temps_debut;
        this.passage_reprise = passage_reprise;
        this.arg3 = delta;
    }

    public int getTemps_debut() {return arg2;}
    public float getTauxVariation() {return arg3;}

    public void setTemps_debut(int temps_debut) {this.arg2 = temps_debut;}
    public void setTauxVariation(float delta) {this.arg3 = delta;}

}
