package BDD.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import BDD.to.Musique;

/**
 * Created by guillaume on 15/03/16.
 */
public class Synchronize implements Runnable {

    private List<Musique> musiques;

    public Synchronize(List<Musique> musiques) {
        this.musiques = musiques;
    }

    @Override
    public void run() {
        System.out.println("Start the races");

        String queryMusic = "Insert into " + DataBaseHelper.MUSIQUE_TABLE + " ("
                + DataBaseHelper.NAME_Musique + ","
                + DataBaseHelper.NB_MESURE + ","
                + DataBaseHelper.NB_PULSATION + ","
                + DataBaseHelper.UNITE_PULSATION + ","
                + DataBaseHelper.NB_TEMPS_MESURE + ")"
                + " values(?,?,?,?,?)";
        Connection co = ConnectonJDBC.getConnection();
        if (co == null) {
            System.out.println("Hello World");
        } else {
            System.out.println("Succesfull");
            //for (Musique m : musiques) {
                Musique m = new Musique("debug",11,12,13,14);
                PreparedStatement st = null;
                try {
                    st = co.prepareStatement(queryMusic);
                    st.setString(1, m.getName());
                    st.setInt(2, m.getNb_mesure());
                    st.setInt(3, m.getNb_pulsation());
                    st.setInt(4, m.getUnite_pulsation());
                    st.setInt(5, m.getNb_temps_mesure());
                    st.execute();
                } catch (SQLException e) {
                    e.printStackTrace();

              //  }
            }
        }
    }
}