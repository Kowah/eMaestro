package BDD.to;


public class Alertes extends Evenement{

    private int temps_debut;
    private int couleur;

    public Alertes(){
        super();
    }
    public Alertes(int id, int idMusique,int mesure_debut, int temps_debut, int couleur){
        this.setId(id);
        this.setIdMusiquet(idMusique);
        this.setMesure_debut(mesure_debut);
        this.temps_debut = temps_debut;
        this.couleur = couleur;
    }
    public Alertes(int idMusique,int mesure_debut, int temps_debut, int couleur){
        this.setIdMusiquet(idMusique);
        this.setMesure_debut(mesure_debut);
        this.temps_debut = temps_debut;
        this.couleur = couleur;
    }

    public int getTemps_debut() {return temps_debut;}
    public int getCouleur() {return couleur;}

    public void setTemps_debut(int temps_debut) {this.temps_debut = temps_debut;}
    public void setCouleur(int couleur) {this.couleur = couleur;}
}
