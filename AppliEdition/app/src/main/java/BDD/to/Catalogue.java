package BDD.to;

/**
 * Created by GalsenPro on 25/01/2016.
 */
public class Catalogue {
    private int idCatalogue;
    private int idMusique;
    /**
     * Default constructor
     */
    public Catalogue() {
        super();
    }
    public  Catalogue(int idMusique) {
        this.idMusique = idMusique;
    }
    public  Catalogue(int idCatalogue, int idMusique){
        this.idCatalogue= idCatalogue;
        this.idMusique = idMusique;
    }
    public void setIdCatalogue(int idCatalogue){
        this.idCatalogue = idCatalogue;
    }
    public void setIdMusique(int idMusique){
        this.idMusique = idMusique;
    }
    public int getIdCatalogue(){
        return this.idCatalogue;
    }
    public int getIdMusique(){
        return this.idMusique;
    }
    // Sera utilisée par ArrayAdapter dans la ListView
    @Override
    public String toString() {
        return "Catalogue n° : "+ this.idCatalogue + "Musique n° : "+ this.idMusique;
    }
}