package BDD.to;

/**
 * Created by guillaume on 07/04/16.
 */
public class Partie extends Evenement {
    private String label;

    public Partie(){
        super();
    }

    public Partie(int id, int idMusique, int mesure_debut, String label){
        this.setId(id);
        this.setIdMusiquet(idMusique);
        this.setMesure_debut(mesure_debut);
        this.label = label;
    }

    public Partie(int idMusique, int mesure_debut, String label){
        this.setIdMusiquet(idMusique);
        this.setMesure_debut(mesure_debut);
        this.label = label;
    }

    public void setLabel(String label) {this.label = label;}
    public String getLabel() {return label;}
}
