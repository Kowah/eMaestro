package BDD.db;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import BDD.to.Evenement;
import BDD.to.Musique;
import BDD.to.Partie;
import BDD.to.VariationIntensite;
import BDD.to.VariationTemps;

/**
 * Created by guillaume on 23/03/16.
 */
public class Synchronize extends AsyncTask<Void, String, Boolean> {

    Context context;

    public Synchronize(Context c){
        this.context = c;
    }


    //On se connecte au reseau maestro avant de lancer le thread
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        this.publishProgress("Sleeping...");
        Boolean result = true;
        List<Musique> musiques;
        List<VariationTemps> vartemps;
        List<VariationIntensite> varIntens;
        List<Partie> parties;
        List<Evenement> evenements;
        DataBaseManager bdd = new DataBaseManager(context);
        bdd.open();

        CatalogueDAO bdCatalogue = new CatalogueDAO(context);
        bdCatalogue.open();
        musiques = bdCatalogue.getMusiques();

        String queryCleanMusique = "TRUNCATE TABLE " + DataBaseHelper.MUSIQUE_TABLE;
        String queryCleanVarTemps = "TRUNCATE TABLE " + DataBaseHelper.VarTemps_Table;
        String queryCleanVarIntensite = "TRUNCATE TABLE " + DataBaseHelper.VarIntensite_Table;
        String queryCleanPartie = "TRUNCATE TABLE " + DataBaseHelper.Partie_Table;
        String queryCleanEvenement = "TRUNCATE TABLE " + DataBaseHelper.Evenement_Table;

        String queryMusic = "Insert into " + DataBaseHelper.MUSIQUE_TABLE + " ("
                + DataBaseHelper.IDMusique + ","
                + DataBaseHelper.NAME_Musique + ","
                + DataBaseHelper.NB_MESURE + ")"
                + " values(?,?,?)";
        String queryVarTemps = "Insert into " + DataBaseHelper.VarTemps_Table + " ("
                + DataBaseHelper.IDVarTemps + ","
                + DataBaseHelper.IDMusique + ","
                + DataBaseHelper.MESURE_DEBUT + ","
                + DataBaseHelper.TEMPS_PAR_MESURE + ","
                + DataBaseHelper.TEMPO + ","
                + DataBaseHelper.UNITE_PULSATION + ")"
                + " values(?,?,?,?,?,?)";
        String queryVarIntensite = "Insert into " + DataBaseHelper.VarIntensite_Table + " ("
                + DataBaseHelper.IDIntensite + ","
                + DataBaseHelper.IDMusique + ","
                + DataBaseHelper.MESURE_DEBUT + ","
                + DataBaseHelper.TEMPS_DEBUT + ","
                + DataBaseHelper.NB_TEMPS + ","
                + DataBaseHelper.INTENTSITE + ")"
                + " values(?,?,?,?,?,?)";
        String queryParties = "Insert into " + DataBaseHelper.Partie_Table + " ("
                + DataBaseHelper.IDPartie + ","
                + DataBaseHelper.IDMusique + ","
                + DataBaseHelper.MESURE_DEBUT + ","
                + DataBaseHelper.Label + ")"
                +" values(?,?,?,?);";

        String queryEvenement = "Insert into " + DataBaseHelper.Evenement_Table + " ("
                + DataBaseHelper.IDEvenement + ","
                + DataBaseHelper.IDMusique + ","
                + DataBaseHelper.FLAG + ","
                + DataBaseHelper.MESURE_DEBUT + ","
                + DataBaseHelper.ARG2 + ","
                + DataBaseHelper.PASSAGE_REPRISE  + ","
                + DataBaseHelper.ARG3 + ")"
                +" values(?,?,?,?,?,?,?);";
        //Etape 0 : Connection a la bdd distante
        Connection co = ConnectonJDBC.getConnection();
        if (co == null) {
            result = false;
        }
        else {
            PreparedStatement st;
            //Etape 1 : Vider les tables
            try {
                st = co.prepareStatement(queryCleanMusique);
                st.execute();
                st = co.prepareStatement(queryCleanVarTemps);
                st.execute();
                st = co.prepareStatement(queryCleanVarIntensite);
                st.execute();
                st = co.prepareStatement(queryCleanPartie);
                st.execute();
                st = co.prepareStatement(queryCleanEvenement);
                st.execute();

                //Etape 2: remplir la table musique
                for (Musique m : musiques) {
                    st = co.prepareStatement(queryMusic);
                    st.setInt(1, m.getId());
                    st.setString(2, m.getName());
                    st.setInt(3, m.getNb_mesure());

                    st.execute();
                    //Etape 3: recuperer les variations associés à une musique et les inserer sur le serveur
                    vartemps = bdd.getVariationsTemps(m);
                    for (VariationTemps v : vartemps) {
                        st = co.prepareStatement(queryVarTemps);
                        st.setInt(1, v.getIdVarTemps());
                        st.setInt(2, v.getIDmusique());
                        st.setInt(3, v.getMesure_debut());
                        st.setInt(4, v.getTemps_par_mesure());
                        st.setInt(5, v.getTempo());
                        st.setInt(6, v.getUnite_pulsation());
                        st.execute();
                    }
                    varIntens = bdd.getVariationsIntensite(m);
                    for (VariationIntensite v : varIntens) {
                        st = co.prepareStatement(queryVarIntensite);
                        st.setInt(1, v.getIdVarIntensite());
                        st.setInt(2, v.getIdMusique());
                        st.setInt(3, v.getMesureDebut());
                        st.setInt(4, v.getTempsDebut());
                        st.setInt(5, v.getnb_temps());
                        st.setInt(6, v.getIntensite());
                        st.execute();
                    }
                    parties = bdd.getParties(m);
                    for (Partie v : parties) {
                        st = co.prepareStatement(queryParties);
                        st.setInt(1, v.getId());
                        st.setInt(2, v.getIdMusique());
                        st.setInt(3, v.getMesure_debut());
                        st.setString(4, v.getLabel());
                        st.execute();
                    }

                    evenements = bdd.getEvenement(m);
                    for (Evenement e : evenements){
                        st = co.prepareStatement(queryEvenement);
                        st.setInt(1, e.getId());
                        st.setInt(2, e.getIdMusique());
                        st.setInt(3, e.getFlag());
                        st.setInt(4, e.getMesure_debut());
                        st.setInt(5, e.getArg2());
                        st.setInt(6, e.getPassage_reprise());
                        st.setFloat(7, e.getArg3());
                        st.execute();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                result = false;

            }
            }
            return result;
        }


    @Override
    protected void onPostExecute(Boolean result) {
        if(result)
        Toast.makeText(context, "Succées:Les ePartitions du catalogue ont étés envoyés vers la maestrobox", Toast.LENGTH_LONG).show();
        else{
            Toast.makeText(context, "Echec:Les ePartitions du catalogue n'ont pas pu êtres envoyées vers la maestrobox, vérifiez votre connexion", Toast.LENGTH_LONG).show();
        }
    }

}


