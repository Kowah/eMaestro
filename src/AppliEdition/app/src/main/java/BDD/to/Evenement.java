package BDD.to;


public class Evenement {
    private int id = -1;
    private int idMusique;
    private int mesure_debut;

    public void setId(int id){this.id = id;}
    public void setIdMusiquet(int idMusique) {this.idMusique = idMusique;}
    public void setMesure_debut(int mesure_debut) {this.mesure_debut = mesure_debut;}
    public int getId() {return id;}
    public int getIdMusique() {return idMusique;}
    public int getMesure_debut() {return mesure_debut;}

}
