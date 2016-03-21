package BDD.db;

import android.content.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import BDD.to.Musique;
import BDD.to.VariationTemps;

/**
 * Created by guillaume on 15/03/16.
 */
public class Synchronize implements Runnable {

    private List<Musique> musiques;
    Context context;

    public Synchronize(List<Musique> musiques, Context context) {
        this.musiques = musiques;
        this.context = context;
    }

    @Override
    public void run() {
        List<VariationTemps> vartemps;
        DataBaseManager bdd = new DataBaseManager(context);
        bdd.open();

        String queryCleanMusique = "TRUNCATE TABLE "+ DataBaseHelper.MUSIQUE_TABLE;
        String queryCleanVarTemps = "TRUNCATE TABLE "+ DataBaseHelper.VarTemps_Table;


        String queryMusic = "Insert into " + DataBaseHelper.MUSIQUE_TABLE + " ("
                + DataBaseHelper.KEY_Musique + ","
                + DataBaseHelper.NAME_Musique + ","
                + DataBaseHelper.NB_MESURE + ")"
                //+ DataBaseHelper.NB_PULSATION + ","
                //+ DataBaseHelper.UNITE_PULSATION + ","
                //+ DataBaseHelper.NB_TEMPS_MESURE + ")"
                + " values(?,?,?)";
        String queryVarTemps = "Insert into " + DataBaseHelper.VarTemps_Table + " ("
                + DataBaseHelper.IDVarTemps + ","
                + DataBaseHelper.KEY_Musique+ ","
                + DataBaseHelper.MESURE_DEBUT + ","
                + DataBaseHelper.TEMPS_PAR_MESURE +","
                + DataBaseHelper.TEMPO +","
                + DataBaseHelper.UNITE_PULSATION + ")"
                + " values(?,?,?,?,?,?)";
        //Etape 0 : Connection a la bdd distante
        Connection co = ConnectonJDBC.getConnection();
        if (co == null) {
            System.err.println("Impossible de se connecter à la bdd distante");
        } else {
            PreparedStatement st = null;
            //Etape 1 : Vider les tables
            try {
                st = co.prepareStatement(queryCleanMusique);
                st.execute();
                st = co.prepareStatement(queryCleanVarTemps);
                st.execute();

                //Etape 2: remplir la table musique
                for (Musique m : musiques) {
                    System.out.println("sql: name = " + m.getName());
                    st = co.prepareStatement(queryMusic);
                    st.setInt(1, m.getId());
                    st.setString(2, m.getName());
                    st.setInt(3, m.getNb_mesure());
                    //st.setInt(3, m.getNb_pulsation());
                    //st.setInt(4, m.getUnite_pulsation());
                    //st.setInt(5, m.getNb_temps_mesure());
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

                }
            }catch (SQLException e) {
                    e.printStackTrace();


            }
        }
    }
}