package BDD.to;

/**
 * Created by GalsenPro on 25/01/2016.
 */
public class VariationIntensite {
    private int idVarIntensite;
    private int idMusique;
    private int intensite;
    private int tempsDebut;
    /**
     * Default constructor
     */
    public VariationIntensite() {super();
    }
    public VariationIntensite(int idMusique, int intensite, int tempsDebut){
        this.idMusique = idMusique;
        this.intensite = intensite;
        this.tempsDebut = tempsDebut;
    }
    public VariationIntensite(int idVarIntensite, int idMusique, int intensite, int tempsDebut){
        this.idVarIntensite = idVarIntensite;
        this.idMusique = idMusique;
        this.intensite = intensite;
        this.tempsDebut = tempsDebut;
    }
    public void setIdVarIntensite(int idVarIntensite){
        this.idVarIntensite = idVarIntensite;
    }
    public void setIdMusique(int idMusique){
        this.idMusique = idMusique;
    }
    public void setIntensite(int intensite){
        this.intensite = intensite;
    }
    public void setTempsDebut(int tempsDebut){
        this.tempsDebut = tempsDebut;
    }
    public int getIdVarIntensite(){
        return this.idVarIntensite;
    }
    public int getIdMusique(){
        return this.idMusique;
    }
    public int getIntensite(){
        return this.intensite;
    }
    public int getTempsDebut(){
        return this.tempsDebut;
    }
    public String toString(){
        return "Var Intensité n° : "+ this.idVarIntensite
                +" Musique n° : "+this.idMusique
                +" Intensité : "+ this.intensite
                +" Temps de Début : "+ this.tempsDebut;
    }
}