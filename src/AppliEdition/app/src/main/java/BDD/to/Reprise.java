package BDD.to;

/**
 * Created by guillaume on 07/04/16.
 */
public class Reprise extends Evenement {

    private int mesure_fin;

    public Reprise(){
        super();
    }
    public Reprise(int id, int idMusique,int mesure_debut, int mesure_fin){
        this.setId(id);
        this.setIdMusiquet(idMusique);
        this.setMesure_debut(mesure_debut);
        this.mesure_fin = mesure_fin;
    }
    public Reprise(int idMusique,int mesure_debut, int mesure_fin){
        this.setIdMusiquet(idMusique);
        this.setMesure_debut(mesure_debut);
        this.mesure_fin = mesure_fin;
    }

    public int getMesure_fin() {return mesure_fin;}
    public void setMesure_fin(int mesure_fin) {this.mesure_fin = mesure_fin;}
}
