package BDD.db;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import BDD.to.Musique;
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
       /* //String networkSSID = "\"wifsic-free\"";
        String networkSSID = "\"Maestro\"";
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        boolean exist = false;
        int res = -1;
        if(!wifi.getConnectionInfo().getSSID().equals(networkSSID)) {
            wifi.disconnect();
            for (WifiConfiguration i : wifi.getConfiguredNetworks()) {
                if (i.SSID != null && i.SSID.equals(networkSSID)) {
                    Toast.makeText(context, "SSID exist already: " + wifi.getConnectionInfo().getSSID(), Toast.LENGTH_LONG).show();
                    res = i.networkId;
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                WifiConfiguration conf = new WifiConfiguration();
                conf.SSID =  networkSSID;
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                res = wifi.addNetwork(conf);
                Toast.makeText(context, "SSID created: " + wifi.getConnectionInfo().getSSID(), Toast.LENGTH_LONG).show();
            }
            wifi.enableNetwork(res, true);
            while (!wifi.reconnect()) ;
        }
        else{
            Toast.makeText(context, "Already connected " + wifi.getConnectionInfo().getSSID(), Toast.LENGTH_LONG).show();
        }*/
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
        DataBaseManager bdd = new DataBaseManager(context);
        bdd.open();

        CatalogueDAO bdCatalogue = new CatalogueDAO(context);
        bdCatalogue.open();
        musiques = bdCatalogue.getMusiques();

        String queryCleanMusique = "TRUNCATE TABLE " + DataBaseHelper.MUSIQUE_TABLE;
        String queryCleanVarTemps = "TRUNCATE TABLE " + DataBaseHelper.VarTemps_Table;
        String queryCleanVarIntensite = "TRUNCATE " + DataBaseHelper.VarIntensite_Table;

        String queryMusic = "Insert into " + DataBaseHelper.MUSIQUE_TABLE + " ("
                + DataBaseHelper.KEY_Musique + ","
                + DataBaseHelper.NAME_Musique + ","
                + DataBaseHelper.NB_MESURE + ")"

                + " values(?,?,?)";
        String queryVarTemps = "Insert into " + DataBaseHelper.VarTemps_Table + " ("
                + DataBaseHelper.IDVarTemps + ","
                + DataBaseHelper.KEY_Musique + ","
                + DataBaseHelper.MESURE_DEBUT + ","
                + DataBaseHelper.TEMPS_PAR_MESURE + ","
                + DataBaseHelper.TEMPO + ","
                + DataBaseHelper.UNITE_PULSATION + ")"
                + " values(?,?,?,?,?,?)";
        String queryVarIntensite = "Insert into " + DataBaseHelper.VarIntensite_Table + " ("
                + DataBaseHelper.IDIntensite + ","
                + DataBaseHelper.KEY_Musique + ","
                + DataBaseHelper.MESURE_DEBUT + ","
                + DataBaseHelper.TEMPS_DEBUT + ","
                + DataBaseHelper.NB_TEMPS + ","
                + DataBaseHelper.INTENTSITE + ")"
                + " values(?,?,?,?,?,?)";
        //Etape 0 : Connection a la bdd distante
        Connection co = ConnectonJDBC.getConnection();
        if (co == null) {
            result = false;
            System.err.println("Impossible de se connecter à la bdd distante");
        } else {
            PreparedStatement st = null;
            //Etape 1 : Vider les tables
            try {
                st = co.prepareStatement(queryCleanMusique);
                st.execute();
                st = co.prepareStatement(queryCleanVarTemps);
                st.execute();
                st = co.prepareStatement(queryCleanVarIntensite);
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
        Toast.makeText(context, "Succées:Les donnés du catalogue ont étés envoyés vers la maestrobox", Toast.LENGTH_LONG).show();
        else{
            Toast.makeText(context, "Echec:Les donnés du catalogue n'ont pas étés envoyés vers la maestrobox", Toast.LENGTH_LONG).show();
        }
    }

}


