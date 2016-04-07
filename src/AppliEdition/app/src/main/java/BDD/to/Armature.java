package BDD.to;


public class Armature extends Evenement {
    private int temps_debut;
    private int alteration;
    private int passage_reprise;

    public Armature(){
        super();
    }

    public Armature(int id, int idMusique,int mesure_debut, int temps_debut, int alteration, int passage_reprise){
        this.setId(id);
        this.setIdMusiquet(idMusique);
        this.setMesure_debut(mesure_debut);
        this.temps_debut = temps_debut;
        this.alteration = alteration;
        this.passage_reprise = passage_reprise;
    }
    public Armature(int idMusique,int mesure_debut, int temps_debut, int alteration, int passage_reprise){
        this.setIdMusiquet(idMusique);
        this.setMesure_debut(mesure_debut);
        this.temps_debut = temps_debut;
        this.alteration = alteration;
        this.passage_reprise = passage_reprise;
    }

    public int getTemps_debut() {return temps_debut;}
    public int getAlteration() {return alteration;}
    public int getPassage_reprise() {return passage_reprise;}

    public void setTemps_debut(int temps_debut) {this.temps_debut = temps_debut;}
    public void setAlteration(int alteration) {this.alteration = alteration;}
    public void setPassage_reprise(int passage_reprise) {this.passage_reprise = passage_reprise;}
}

