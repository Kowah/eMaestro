package BDD.to;

/**
 * Created by GalsenPro on 25/01/2016.
 */
public class VariationTemps {
    private int idVarTemps;
    private int idMusique;
    private int nbrTempos;
    /**
     * Default constructor
     */
    public VariationTemps() {super();
    }
    public VariationTemps(int idMusique, int nbrTempos){
        this.idMusique = idMusique;
        this.nbrTempos = nbrTempos;
    }
    public VariationTemps(int idVarTemps, int idMusique, int nbrTempos){
       this.idVarTemps = idVarTemps;
        this.idMusique = idMusique;
        this.nbrTempos = nbrTempos;
    }
    public void setIdVarTemps(int idVarTemps){
        this.idVarTemps = idVarTemps;
    }
    public void setIdMusique(int idMusique){
        this.idMusique = idMusique;
    }
    public void setNbrTempos(int nbrTempos){
        this.nbrTempos = nbrTempos;
    }
    public int getIdVarTemps(){
        return this.idVarTemps;
    }
    public int getIdMusique(){
        return this.idMusique;
    }
    public int getNbrTempos(){
        return this.nbrTempos;
    }
    // Sera utilisée par ArrayAdapter dans la ListView
    @Override
    public String toString() {
        return "Variation n° : "+ this.idVarTemps +
                "Musique n° : "+ this.idMusique+
                "Nbr Temps : "+ this.nbrTempos;
    }
}