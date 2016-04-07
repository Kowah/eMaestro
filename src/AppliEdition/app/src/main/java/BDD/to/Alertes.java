package BDD.to;


public class Alertes extends Evenement{

    private int temps_debut;
    private int couleur;
    private int passage_reprise;

    public Alertes(){
        super();
    }

    public Alertes(int id, int idMusique,int mesure_debut, int temps_debut, int couleur, int passage_reprise){
        this.setId(id);
        this.setIdMusiquet(idMusique);
        this.setMesure_debut(mesure_debut);
        this.temps_debut = temps_debut;
        this.couleur = couleur;
        this.passage_reprise = passage_reprise;
    }
    public Alertes(int idMusique,int mesure_debut, int temps_debut, int couleur, int passage_reprise){
        this.setIdMusiquet(idMusique);
        this.setMesure_debut(mesure_debut);
        this.temps_debut = temps_debut;
        this.couleur = couleur;
        this.passage_reprise = passage_reprise;
    }

    public int getTemps_debut() {return temps_debut;}
    public int getCouleur() {return couleur;}
    public int getPassage_reprise() {return passage_reprise;}

    public void setTemps_debut(int temps_debut) {this.temps_debut = temps_debut;}
    public void setCouleur(int couleur) {this.couleur = couleur;}
    public void setPassage_reprise(int passage_reprise) {this.passage_reprise = passage_reprise;}
}
