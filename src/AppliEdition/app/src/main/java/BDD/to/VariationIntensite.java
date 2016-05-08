package BDD.to;

/**
 * Created by GalsenPro on 25/01/2016.
 */
public class VariationIntensite {
    private int idVarIntensite;
    private int idmusique;
    private int mesure_debut;
    private int tempsDebut;
    private int nb_temps;
    private int intensite;
    /**
     * Default constructor
     */
    public VariationIntensite() {super();
    }
    public VariationIntensite(int idmusique, int intensite, int tempsDebut, int mesure_debut, int nb_temps){
        this.idmusique = idmusique;
        this.intensite = intensite;
        this.tempsDebut = tempsDebut;
        this.mesure_debut = mesure_debut;
        this.nb_temps = nb_temps;
    }
    public VariationIntensite(int idVarIntensite, int idmusique, int intensite, int tempsDebut, int mesure_debut, int nb_temps){
        this.idVarIntensite = idVarIntensite;
        this.idmusique = idmusique;
        this.intensite = intensite;
        this.tempsDebut = tempsDebut;
        this.mesure_debut = mesure_debut;
        this.nb_temps = nb_temps;
    }
    public void setIdVarIntensite(int idVarIntensite){
        this.idVarIntensite = idVarIntensite;
    }
    public void setIdMusique(int idMusique){
        this.idmusique = idMusique;
    }
    //Setters
    public void setIDMusique(int idMusique) {this.idmusique = idMusique;}
    public void setIntensite(int intensite){
        this.intensite = intensite;
    }
    public void setTempsDebut(int tempsDebut){
        this.tempsDebut = tempsDebut;
    }
    public void setMesureDebut(int mesureDebut) { this.mesure_debut = mesureDebut; }
    public void setNb_temps(int nb_temps) {this.nb_temps = nb_temps;}
    //Getters
    public int getIdVarIntensite(){
        return this.idVarIntensite;
    }
    public int getIdMusique(){
        return this.idmusique;
    }
    public int getIntensite(){
        return this.intensite;
    }
    public int getTempsDebut(){
        return this.tempsDebut;
    }
    public int getMesureDebut() {return this.mesure_debut; }
    public int getnb_temps() {return this.nb_temps;}

    public String toString(){
        return "Var Intensité n° : "+ this.idVarIntensite
                +" Musique n° : "+this.idmusique
                +" Intensité : "+ this.intensite
                +" Temps de Début : "+ this.tempsDebut;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VariationIntensite other = (VariationIntensite) obj;
        if (idVarIntensite != other.idVarIntensite || idmusique != other.idmusique || mesure_debut != other.mesure_debut || tempsDebut != other.tempsDebut || nb_temps != other.nb_temps || intensite != other.intensite)
            return false;
        return true;
    }
}