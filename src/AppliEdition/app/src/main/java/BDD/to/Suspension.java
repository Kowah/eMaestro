package BDD.to;

/**
 * Created by guillaume on 07/04/16.
 */
public class Suspension extends Evenement {
    private int temps;
    private int duree;
    private int passage_reprise;

    public Suspension(){
        super();
    }

    public Suspension(int id, int idMusique,int mesure_debut, int temps, int duree, int passage_reprise){
        this.setId(id);
        this.setIdMusiquet(idMusique);
        this.setMesure_debut(mesure_debut);
        this.temps = temps;
        this.duree = duree;
        this.passage_reprise = passage_reprise;
    }
    public Suspension(int idMusique,int mesure_debut, int temps, int duree, int passage_reprise){
        this.setIdMusiquet(idMusique);
        this.setMesure_debut(mesure_debut);
        this.temps = temps;
        this.duree = duree;
        this.passage_reprise = passage_reprise;
    }

    public int getTemps() {return temps;}
    public int getDuree() {return duree;}
    public int getPassage_reprise() {return passage_reprise;}

    public void setTemps(int temps) {this.temps = temps;}
    public void setDuree(int duree) {this.duree = duree;}
    public void setPassage_reprise(int passage_reprise) {this.passage_reprise = passage_reprise;}
}
