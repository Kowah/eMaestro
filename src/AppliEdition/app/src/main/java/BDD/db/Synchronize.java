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

import BDD.to.Alertes;
import BDD.to.Armature;
import BDD.to.MesuresNonLues;
import BDD.to.Musique;
import BDD.to.Partie;
import BDD.to.Reprise;
import BDD.to.Suspension;
import BDD.to.VarRythmes;
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
        List<Partie> parties;
        List<MesuresNonLues> mnl;
        List<Reprise> reprises;
        List<Alertes> alertes;
        List<VarRythmes> varRythmes;
        List<Suspension> suspensions;
        List<Armature> armatures;
        DataBaseManager bdd = new DataBaseManager(context);
        bdd.open();

        CatalogueDAO bdCatalogue = new CatalogueDAO(context);
        bdCatalogue.open();
        musiques = bdCatalogue.getMusiques();

        String queryCleanMusique = "TRUNCATE TABLE " + DataBaseHelper.MUSIQUE_TABLE;
        String queryCleanVarTemps = "TRUNCATE TABLE " + DataBaseHelper.VarTemps_Table;
        String queryCleanVarIntensite = "TRUNCATE TABLE " + DataBaseHelper.VarIntensite_Table;
        String queryCleanPartie = "TRUNCATE TABLE " + DataBaseHelper.Partie_Table;
        String queryCleanMesuresNonLues = "TRUNCATE TABLE " + DataBaseHelper.Mesures_non_lues_Table;
        String queryCleanReprise = "TRUNCATE TABLE " + DataBaseHelper.Reprises_Table;
        String queryCleanAlerte = "TRUNCATE TABLE " + DataBaseHelper.Alerte_Table;
        String queryCleanVarRythme = "TRUNCATE TABLE " + DataBaseHelper.Variation_Rythme_Table;
        String queryCleanSuspension = "TRUNCATE TABLE " + DataBaseHelper.Suspension_Table;
        String queryCleanArmature = "TRUNCATE TABLE " + DataBaseHelper.Armature_Table;

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
        String queryMesuresNonLues = "Insert into " + DataBaseHelper.Mesures_non_lues_Table + " ("
                + DataBaseHelper.IDMesuresNonLues + ","
                + DataBaseHelper.IDMusique + ","
                + DataBaseHelper.MESURE_DEBUT + ","
                + DataBaseHelper.MESURE_FIN + ","
                + DataBaseHelper.PASSAGE_REPRISE + ")"
                +" values(?,?,?,?,?);";
        String queryReprises = "Insert into " + DataBaseHelper.Reprises_Table + " ("
                + DataBaseHelper.IDReprises + ","
                + DataBaseHelper.IDMusique + ","
                + DataBaseHelper.MESURE_DEBUT + ","
                + DataBaseHelper.MESURE_FIN + ")"
                +" values(?,?,?,?);";
        String queryAlerte = "Insert into " + DataBaseHelper.Alerte_Table + " ("
                + DataBaseHelper.IDAlertes + ","
                + DataBaseHelper.IDMusique + ","
                + DataBaseHelper.MESURE_DEBUT + ","
                + DataBaseHelper.TEMPS_DEBUT + ","
                + DataBaseHelper.Couleur + ","
                + DataBaseHelper.PASSAGE_REPRISE + ")"
                +" values(?,?,?,?,?,?);";
        String queryVariationRythme = "Insert into " + DataBaseHelper.Variation_Rythme_Table + " ("
                + DataBaseHelper.IDVarRythme + ","
                + DataBaseHelper.IDMusique + ","
                + DataBaseHelper.MESURE_DEBUT + ","
                + DataBaseHelper.TEMPS_DEBUT + ","
                + DataBaseHelper.Taux_Varitation + ","
                + DataBaseHelper.PASSAGE_REPRISE + ")"
                +" values(?,?,?,?,?,?);";
        String querySuspension = "Insert into " + DataBaseHelper.Suspension_Table + " ("
                + DataBaseHelper.IDSuspension + ","
                + DataBaseHelper.IDMusique + ","
                + DataBaseHelper.MESURE + ","
                + DataBaseHelper.TEMPS + ","
                + DataBaseHelper.DUREE + ","
                + DataBaseHelper.PASSAGE_REPRISE + ")"
                +" values(?,?,?,?,?,?);";
        String queryArmature = "Insert into " + DataBaseHelper.Armature_Table + " ("
                + DataBaseHelper.IDArmature + ","
                + DataBaseHelper.IDMusique + ","
                + DataBaseHelper.MESURE_DEBUT + ","
                + DataBaseHelper.TEMPS_DEBUT + ","
                + DataBaseHelper.Alteration  + ","
                + DataBaseHelper.PASSAGE_REPRISE + ")"
                +" values(?,?,?,?,?,?);";
        //Etape 0 : Connection a la bdd distante
        Connection co = ConnectonJDBC.getConnection();
        if (co == null) {
            result = false;
            System.err.println("Impossible de se connecter à la bdd distante");
        }
        else {
            PreparedStatement st = null;
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
                st = co.prepareStatement(queryCleanMesuresNonLues);
                st.execute();
                st = co.prepareStatement(queryCleanReprise);
                st.execute();
                st = co.prepareStatement(queryCleanAlerte);
                st.execute();
                st = co.prepareStatement(queryCleanVarRythme);
                st.execute();
                st = co.prepareStatement(queryCleanSuspension);
                st.execute();
                st = co.prepareStatement(queryCleanArmature);
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
                    mnl = bdd.getMesuresNonLues(m);
                    for (MesuresNonLues v : mnl) {
                        st = co.prepareStatement(queryMesuresNonLues);
                        st.setInt(1, v.getId());
                        st.setInt(2, v.getIdMusique());
                        st.setInt(3, v.getMesure_debut());
                        st.setInt(4, v.getMesure_fin());
                        st.setInt(5, v.getPassage_reprise());
                        st.execute();
                    }
                    reprises = bdd.getReprises(m);
                    for (Reprise v : reprises) {
                        st = co.prepareStatement(queryReprises);
                        st.setInt(1, v.getId());
                        st.setInt(2, v.getIdMusique());
                        st.setInt(3, v.getMesure_debut());
                        st.setInt(4, v.getMesure_fin());
                        st.execute();
                    }
                    alertes = bdd.getAlertes(m);
                    for (Alertes v : alertes) {
                        st = co.prepareStatement(queryAlerte);
                        st.setInt(1, v.getId());
                        st.setInt(2, v.getIdMusique());
                        st.setInt(3, v.getMesure_debut());
                        st.setInt(4, v.getTemps_debut());
                        st.setInt(5, v.getCouleur());
                        st.setInt(6, v.getPassage_reprise());
                        st.execute();
                    }
                    varRythmes = bdd.getVarRythmes(m);
                    for (VarRythmes v : varRythmes) {
                        st = co.prepareStatement(queryVariationRythme);
                        st.setInt(1, v.getId());
                        st.setInt(2, v.getIdMusique());
                        st.setInt(3, v.getMesure_debut());
                        st.setInt(4, v.getTemps_debut());
                        st.setFloat(5, v.getTauxVariation());
                        st.setInt(6, v.getPassage_reprise());
                        st.execute();
                    }
                    suspensions = bdd.getSuspension(m);
                    for (Suspension v : suspensions) {
                        st = co.prepareStatement(querySuspension);
                        st.setInt(1, v.getId());
                        st.setInt(2, v.getIdMusique());
                        st.setInt(3, v.getMesure_debut());
                        st.setInt(4, v.getTemps());
                        st.setInt(5, v.getDuree());
                        st.setInt(6, v.getPassage_reprise());
                        st.execute();
                    }
                    armatures = bdd.getArmature(m);
                    for (Armature v : armatures) {
                        st = co.prepareStatement(queryArmature);
                        st.setInt(1, v.getId());
                        st.setInt(2, v.getIdMusique());
                        st.setInt(3, v.getMesure_debut());
                        st.setInt(4, v.getTemps_debut());
                        st.setInt(5, v.getAlteration());
                        st.setInt(6, v.getPassage_reprise());
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


