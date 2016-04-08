package BDD.to;

import android.os.Parcel;

/**
 * Created by GalsenPro on 25/01/2016.
 */
public class VariationTemps {
    private int idVarTemps;
    private int idmusique;
    private int mesure_debut;
    private int temps_par_mesure;
    private int tempo;
    private int unite_pulsation;
    /**
     * Default constructor
     */
    public VariationTemps() {
        this.idmusique = -1;
        this.mesure_debut = -1;
        this.temps_par_mesure = -1;
        this.tempo = -1;
        this.unite_pulsation =-1;
    }
    public VariationTemps(int idmusique, int mesure_debut, int temps_par_mesure, int tempo, int unite_pulsation){
        this.idmusique = idmusique;
        this.mesure_debut = mesure_debut;
        this.temps_par_mesure = temps_par_mesure;
        this.tempo = tempo;
        this.unite_pulsation = unite_pulsation;
    }
    public VariationTemps(int idVarTemps, int idmusique, int mesure_debut, int temps_par_mesure, int tempo, int unite_pulsation){
       this.idVarTemps = idVarTemps;
        this.idmusique = idmusique;
        this.mesure_debut = mesure_debut;
        this.temps_par_mesure = temps_par_mesure;
        this.tempo = tempo;
        this.unite_pulsation = unite_pulsation;
    }
    public void setIdVarTemps(int idVarTemps){
        this.idVarTemps = idVarTemps;
    }

    public int getIDmusique() {
        return idmusique;
    }
    public void setMusique(int idmusique) {
        this.idmusique = idmusique;
    }

    public void setMesure_debut(int mesure_debut){
        this.mesure_debut = mesure_debut;
    }
    public void setTemps_par_mesure(int temps_par_mesure){this.temps_par_mesure = temps_par_mesure;}
    public void setTempo(int tempo) {this.tempo = tempo;}
    public void setUnite_pulsation(int unite_pulsation) {this.unite_pulsation=unite_pulsation;}
    public int getIdVarTemps(){
        return this.idVarTemps;
    }
    public int getMesure_debut() {return mesure_debut;}
    public int getTemps_par_mesure() {return temps_par_mesure;}
    public int getTempo(){return tempo;}
    public int getUnite_pulsation(){return unite_pulsation;}
    // Sera utilisée par ArrayAdapter dans la ListView
    @Override
    public String toString() {
        return "Variation n° : "+ this.idVarTemps +
                "Musique  : "+ this.idmusique+
                "mesure_debut"+ this.mesure_debut +
                "temps_par_mesure"+ this.temps_par_mesure +
                "tempo"+ this.tempo;
    }
}