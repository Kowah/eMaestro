package fr.istic.univ_rennes1.diengadama.mabdemaestro.to;

/**
 * Created by GalsenPro on 25/01/2016.
 */
public class Evments {
    /**
     * Default constructor
     */
    private int idEvenement;
    private int idMusique;
    private int idSymbole;
    private int nTemps;
    private String couleur;

    public Evments() {
        super();
    }
    public  Evments(int idMusique, int idSymbole, int nTemps, String couleur){
        this.idMusique = idMusique;
        this.idSymbole= idSymbole;
        this.nTemps = nTemps;
        this.couleur = couleur;
    }
    public Evments(int idEvenement, int idMusique, int idSymbole, int nTemps){
        this.idEvenement = idEvenement;
        this.idMusique = idMusique;
        this.idSymbole= idSymbole;
        this.nTemps = nTemps;
        this.couleur = couleur;
    }
    //private List Couleur;
    public void setIdEvenement(int idEvenement){
        this.idEvenement = idEvenement;
    }
    public void setIdMusique(int idMusique){
        this.idMusique = idMusique;
    }
    public void setIdSymbole(int idSymbole){
        this.idSymbole = idSymbole;
    }
    public void setnTemps(int nTemps){
        this.nTemps = nTemps;
    }
    public void setcouleur(String couleur){
        this.couleur = couleur;
    }
    public int getIdEvenement(){
        return this.idEvenement;
    }
    public int getIdMusique(){
        return this.idMusique;
    }
    public int getIdSymbole(){
        return this.idMusique;
    }
    public int getnTemps(){
        return this.nTemps;
    }
    public String getcouleur(){ return  this.couleur;}
    // Sera utilisée par ArrayAdapter dans la ListView
    @Override
    public String toString() {
        return "Evenement n° : "+ this.idEvenement +
                "Musique n° : "+ this.idMusique+
                "Symbole n°: "+ this.idSymbole+
                "Couleur : "+ this.couleur;
    }

}