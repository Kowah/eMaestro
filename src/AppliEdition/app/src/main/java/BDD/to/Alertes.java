package BDD.to;


import BDD.db.DataBaseHelper;

public class Alertes extends Evenement{


    public Alertes(){
        super();
    }

    public Alertes(int id, int idMusique,int mesure_debut, int temps_debut, int couleur, int passage_reprise){
        this.id = id;
        this.idMusique = idMusique;
        this.flag = DataBaseHelper.FLAG_ALERTE;
        this.mesure_debut = mesure_debut;
        this.arg2 = temps_debut;
        this.passage_reprise = passage_reprise;
        this.arg3 = couleur;
    }
    public Alertes(int idMusique,int mesure_debut, int temps_debut, int couleur, int passage_reprise){
        this.idMusique = idMusique;
        this.flag = DataBaseHelper.FLAG_ALERTE;
        this.mesure_debut = mesure_debut;
        this.arg2 = temps_debut;
        this.passage_reprise = passage_reprise;
        this.arg3 = couleur;
    }

    public int getTemps_debut() {return arg2;}
    public int getCouleur() {return (int) arg3;}

    public void setTemps_debut(int temps_debut) {this.arg2 = temps_debut;}
    public void setCouleur(int couleur) {this.arg3 = couleur;}
}
