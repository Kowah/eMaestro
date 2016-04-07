package BDD.to;

/**
 * Created by guillaume on 07/04/16.
 */
public class MesuresNonLues extends Evenement {
    private int mesure_fin;
    private int passage_reprise;

    public MesuresNonLues(){
        super();
    }

    public MesuresNonLues(int id, int idMusique,int mesure_debut ,int mesure_fin, int passage_reprise){
        this.setId(id);
        this.setIdMusiquet(idMusique);
        this.setMesure_debut(mesure_debut);
        this.mesure_fin = mesure_fin;
        this.passage_reprise = passage_reprise;
    }

    public MesuresNonLues(int idMusique,int mesure_debut, int mesure_fin, int passage_reprise){
        this.setIdMusiquet(idMusique);
        this.setMesure_debut(mesure_debut);
        this.mesure_fin = mesure_fin;
        this.passage_reprise = passage_reprise;
    }

    public int getMesure_fin() {return mesure_fin;}
    public int getPassage_reprise() {return passage_reprise;}

    public void setMesure_fin(int mesure_fin) {this.mesure_fin = mesure_fin;}
    public void setPassage_reprise(int passage_reprise) {this.passage_reprise = passage_reprise;}
}
