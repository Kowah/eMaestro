package BDD.to;

/**
 * Created by GalsenPro on 25/01/2016.
 */
public class VariationIntensite {
    private int idVarIntensite;
    private int idMusique;
    private int intensite;
    private int tempsDebut;
    private int mesureDebut;
    private int nb_temps;
    /**
     * Default constructor
     */
    public VariationIntensite() {super();
    }
    public VariationIntensite(int idMusique, int intensite, int tempsDebut, int mesureDebut, int nb_temps){
        this.idMusique = idMusique;
        this.intensite = intensite;
        this.tempsDebut = tempsDebut;
        this.mesureDebut = mesureDebut;
        this.nb_temps = nb_temps;
    }
    public VariationIntensite(int idVarIntensite, int idMusique, int intensite, int tempsDebut, int mesureDebut, int nb_temps){
        this.idVarIntensite = idVarIntensite;
        this.idMusique = idMusique;
        this.intensite = intensite;
        this.tempsDebut = tempsDebut;
        this.mesureDebut = mesureDebut;
        this.nb_temps = nb_temps;
    }
    public void setIdVarIntensite(int idVarIntensite){
        this.idVarIntensite = idVarIntensite;
    }
    public void setIdMusique(int idMusique){
        this.idMusique = idMusique;
    }
    //Setters
    public void setIDMusique(int idMusique) {this.idMusique = idMusique;}
    public void setIntensite(int intensite){
        this.intensite = intensite;
    }
    public void setTempsDebut(int tempsDebut){
        this.tempsDebut = tempsDebut;
    }
    public void setMesureDebut(int mesureDebut) { this.mesureDebut = mesureDebut; }
    public void setNb_temps(int nb_temps) {this.nb_temps = nb_temps;}
    //Getters
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
    public int getMesureDebut() {return this.mesureDebut; }
    public int getnb_temps() {return this.nb_temps;}

    public String toString(){
        return "Var Intensité n° : "+ this.idVarIntensite
                +" Musique n° : "+this.idMusique
                +" Intensité : "+ this.intensite
                +" Temps de Début : "+ this.tempsDebut;
    }
}