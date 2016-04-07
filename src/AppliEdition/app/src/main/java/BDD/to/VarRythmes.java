package BDD.to;


public class VarRythmes extends Evenement{

    private int temps_debut;
    private float delta;
    private int passage_reprise;

    public VarRythmes(){
        super();
    }

    public VarRythmes(int id, int idMusique,int mesure_debut, int temps_debut, float delta, int passage_reprise){
        this.setId(id);
        this.setIdMusiquet(idMusique);
        this.setMesure_debut(mesure_debut);
        this.temps_debut = temps_debut;
        this.delta = delta;
        this.passage_reprise = passage_reprise;
    }
    public VarRythmes(int idMusique,int mesure_debut, int temps_debut, float delta, int passage_reprise){
        this.setIdMusiquet(idMusique);
        this.setMesure_debut(mesure_debut);
        this.temps_debut = temps_debut;
        this.delta = delta;
        this.passage_reprise = passage_reprise;
    }

    public int getTemps_debut() {return temps_debut;}
    public float getTauxVariation() {return delta;}
    public int getPassage_reprise() {return passage_reprise;}


    public void setTemps_debut(int temps_debut) {this.temps_debut = temps_debut;}
    public void setTauxVariation(int delta) {this.delta = delta;}
    public void setPassage_reprise(int passage_reprise) {this.passage_reprise = passage_reprise;}

}
