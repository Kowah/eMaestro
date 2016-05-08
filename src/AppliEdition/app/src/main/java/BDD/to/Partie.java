package BDD.to;

/**
 * Created by guillaume on 07/04/16.
 */
public class Partie {
    private int id;
    private int idMusique;
    private int mesure_debut;
    private String label;

    public Partie(){
        super();
    }

    public Partie(int id, int idMusique, int mesure_debut, String label){
        this.id = id;
        this.idMusique = idMusique;
        this.mesure_debut =mesure_debut;
        this.label = label;
    }

    public Partie(int idMusique, int mesure_debut, String label){
        this.idMusique = idMusique;
        this.mesure_debut =mesure_debut;
        this.label = label;
    }
    public void setId(int id) {this.id = id;}
    public int getId() {return id;}
    public void setIdMusique(int idMusique) {this.idMusique = idMusique;}
    public int getIdMusique() {return idMusique;}
    public void setMesure_debut(int mesure_debut) {this.mesure_debut = mesure_debut;}
    public int getMesure_debut() {return mesure_debut;}
    public void setLabel(String label) {this.label = label;}
    public String getLabel() {return label;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Partie other = (Partie) obj;
        if (id != other.id && idMusique != other.idMusique || mesure_debut != other.mesure_debut || !label.equals(other.label))
            return false;
        return true;
    }
}
