package BDD.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import BDD.to.Alertes;
import BDD.to.MesuresNonLues;
import BDD.to.Musique;
import BDD.to.Partie;
import BDD.to.Reprise;
import BDD.to.VariationIntensite;
import BDD.to.VariationTemps;

public class DataBaseManager {

	private static final String WHERE_ID_MUSIQUE_EQUALS = DataBaseHelper.IDMusique
			+ " =?";
	private static final String WHERE_ID_VARTEMPS_EQUALS = DataBaseHelper.IDVarTemps
			+ " =?";
	private static final String WHERE_ID_VARINTENSITE_EQUALS = DataBaseHelper.IDIntensite
			+ " =?";
	private static final String WHERE_ID_PARTIE_EQUALS = DataBaseHelper.IDPartie
			+ " =?";
    private static final String WHERE_ID_MESURE_NON_LUE_EQUALS = DataBaseHelper.IDMesuresNonLues
            + " =?";
    private static final String WHERE_ID_Reprise_EQUALS = DataBaseHelper.IDReprises
            + " =?";
    private static final String WHERE_ID_ALERTES_EQUALS = DataBaseHelper.IDAlertes
            + " =?";


    protected SQLiteDatabase database;
	private BDD.db.DataBaseHelper dbHelper;
	private Context mContext;

	public DataBaseManager(Context context) {
		this.mContext = context;
		dbHelper = new BDD.db.DataBaseHelper(mContext);
	}

	//Ouvre la connection avec la bdd
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	//Vide toutes les tables
	public void clean() {
		database.delete(DataBaseHelper.MUSIQUE_TABLE, null, null);
		database.delete(DataBaseHelper.VarIntensite_Table, null, null);
		database.delete(DataBaseHelper.VarTemps_Table, null, null);
		database.delete(DataBaseHelper.CATALOGUE_TABLE, null, null);
	}

	//Ferme la connection avec la bdd
	public void close() {
		dbHelper.close();
		database = null;
	}

	/******************
	 * Save
	 ****************/
	//Permet de sauvegarder la musique passé en paramétres dans la base
	//@return l'id dans la BDD
	public long save(Musique musique) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.NAME_Musique, musique.getName());
		values.put(DataBaseHelper.NB_MESURE, musique.getNb_mesure());

		return database.insert(DataBaseHelper.MUSIQUE_TABLE, null, values);
	}

	//Permet de sauvegarder une Variation de temps dans la table var temps
	//@return l'id dans la BDD
	public long save(VariationTemps varTemps) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.IDMusique, varTemps.getIDmusique());
		values.put(DataBaseHelper.MESURE_DEBUT, varTemps.getMesure_debut());
		values.put(DataBaseHelper.TEMPS_PAR_MESURE, varTemps.getTemps_par_mesure());
		values.put(DataBaseHelper.TEMPO, varTemps.getTempo());
		values.put(DataBaseHelper.UNITE_PULSATION, varTemps.getUnite_pulsation());

		return database.insert(DataBaseHelper.VarTemps_Table, null, values);
	}

	//Permet de sauvegarder une variation d'intensite dans la table var_intensite
	//@return l'id dans la BDD
	public long save(VariationIntensite varIntensite) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.IDMusique, varIntensite.getIdMusique());
		values.put(DataBaseHelper.TEMPS_DEBUT, varIntensite.getTempsDebut());
		values.put(DataBaseHelper.MESURE_DEBUT, varIntensite.getMesureDebut());
		values.put(DataBaseHelper.NB_TEMPS, varIntensite.getnb_temps());
		values.put(DataBaseHelper.INTENTSITE, varIntensite.getIntensite());

		return database.insert(DataBaseHelper.VarIntensite_Table, null, values);
	}
	//Permet de sauvegarder la partie passé en paramétres dans la base
	//@return l'id dans la BDD
	public long save(Partie partie) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.IDMusique, partie.getIdMusique());
		values.put(DataBaseHelper.MESURE_DEBUT, partie.getMesure_debut());
		values.put(DataBaseHelper.Label, partie.getLabel());

		return database.insert(DataBaseHelper.Partie_Table, null, values);
	}
    //Permet de sauvegarder les mesures non lues en paramétres dans la base
    //@return l'id dans la BDD
    public long save(MesuresNonLues m) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, m.getIdMusique());
        values.put(DataBaseHelper.MESURE_DEBUT, m.getMesure_debut());
        values.put(DataBaseHelper.MESURE_FIN, m.getMesure_fin());
        values.put(DataBaseHelper.PASSAGE_REPRISE, m.getPassage_reprise());

        return database.insert(DataBaseHelper.Mesures_non_lues_Table, null, values);
    }
    //Permet de sauvegarder les reprises dans la base
    //@return l'id dans la BDD
    public long save(Reprise reprise) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, reprise.getIdMusique());
        values.put(DataBaseHelper.MESURE_DEBUT, reprise.getMesure_debut());
        values.put(DataBaseHelper.MESURE_FIN, reprise.getMesure_fin());

        return database.insert(DataBaseHelper.Reprises_Table, null, values);
    }

    //Permet de sauvegarder les alertes dans la base
    //@return l'id dans la BDD
    public long save(Alertes alertes) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, alertes.getIdMusique());
        values.put(DataBaseHelper.MESURE_DEBUT, alertes.getMesure_debut());
        values.put(DataBaseHelper.MESURE_FIN, alertes.getTemps_debut());
        values.put(DataBaseHelper.Couleur, alertes.getCouleur());

        return database.insert(DataBaseHelper.Alerte_Table, null, values);
    }

	/****************
	 * Update
	 ****************/
	//Permet de mettre a jour la musique passé en paramétre dans la table musique
	public long update(Musique Musique) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.NAME_Musique, Musique.getName());
		values.put(DataBaseHelper.NB_MESURE, Musique.getNb_mesure());

		long result = database.update(DataBaseHelper.MUSIQUE_TABLE, values,
				WHERE_ID_MUSIQUE_EQUALS,
				new String[]{String.valueOf(Musique.getId())});
		Log.d("Update Result:", "=" + result);
		return result;
	}

	//Permet de mettre a jour la variation de temps dans la table variation temps
	public long update(VariationTemps varTemps) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.IDMusique, varTemps.getIDmusique());
		values.put(DataBaseHelper.MESURE_DEBUT, varTemps.getMesure_debut());
		values.put(DataBaseHelper.TEMPS_PAR_MESURE, varTemps.getTemps_par_mesure());
		values.put(DataBaseHelper.TEMPO, varTemps.getTempo());
		values.put(DataBaseHelper.UNITE_PULSATION, varTemps.getUnite_pulsation());
		long result = database.update(DataBaseHelper.VarTemps_Table, values,
				WHERE_ID_VARTEMPS_EQUALS,
				new String[]{String.valueOf(varTemps.getIdVarTemps())});
		Log.d("Update Result:", "=" + result);
		return result;
	}

	//Permet de mettre a jour la variation d'intensité dans la table variation_instensité
	public long update(VariationIntensite varIntensite) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.IDMusique, varIntensite.getIdMusique());
		values.put(DataBaseHelper.MESURE_DEBUT, varIntensite.getMesureDebut());
		values.put(DataBaseHelper.TEMPS_DEBUT, varIntensite.getTempsDebut());
		values.put(DataBaseHelper.NB_TEMPS, varIntensite.getnb_temps());
		values.put(DataBaseHelper.INTENTSITE, varIntensite.getIntensite());
		long result = database.update(DataBaseHelper.VarIntensite_Table, values,
				WHERE_ID_VARINTENSITE_EQUALS,
				new String[]{String.valueOf(varIntensite.getIdVarIntensite())});
		Log.d("Update Result:", "=" + result);
		return result;
	}

	//Permet de mettre a jour la partie passé en paramétre dans la table partie
	public long update(Partie partie) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.IDMusique, partie.getIdMusique());
		values.put(DataBaseHelper.MESURE_DEBUT, partie.getMesure_debut());
		values.put(DataBaseHelper.Label, partie.getLabel());

		long result = database.update(DataBaseHelper.Partie_Table, values,
				WHERE_ID_PARTIE_EQUALS,
				new String[]{String.valueOf(partie.getId())});
		Log.d("Update Result:", "=" + result);
		return result;
	}
    //Permet de mettre a jour la mesure non lue passé en paramétre dans la table mesure non lue
    public long update(MesuresNonLues m) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, m.getIdMusique());
        values.put(DataBaseHelper.MESURE_DEBUT, m.getMesure_debut());
        values.put(DataBaseHelper.MESURE_FIN, m.getMesure_fin());
        values.put(DataBaseHelper.PASSAGE_REPRISE, m.getPassage_reprise());

        long result = database.update(DataBaseHelper.Mesures_non_lues_Table, values,
                WHERE_ID_MESURE_NON_LUE_EQUALS,
                new String[]{String.valueOf(m.getId())});
        Log.d("Update Result:", "=" + result);
        return result;
    }
    //Permet de mettre a jour la reprise passé en paramétre dans la table mesure non lue
    public long update(Reprise reprise) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, reprise.getIdMusique());
        values.put(DataBaseHelper.MESURE_DEBUT, reprise.getMesure_debut());
        values.put(DataBaseHelper.MESURE_FIN, reprise.getMesure_fin());

        long result = database.update(DataBaseHelper.Reprises_Table, values,
                WHERE_ID_Reprise_EQUALS,
                new String[]{String.valueOf(reprise.getId())});
        Log.d("Update Result:", "=" + result);
        return result;
    }

    //Permet de mettre a jour l'alerte passé en paramétre dans la table mesure non lue
    public long update(Alertes alertes) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, alertes.getIdMusique());
        values.put(DataBaseHelper.MESURE_DEBUT, alertes.getMesure_debut());
        values.put(DataBaseHelper.TEMPS_DEBUT, alertes.getTemps_debut());
        values.put(DataBaseHelper.Couleur, alertes.getCouleur());

        long result = database.update(DataBaseHelper.Alerte_Table, values,
                WHERE_ID_ALERTES_EQUALS,
                new String[]{String.valueOf(alertes.getId())});
        Log.d("Update Result:", "=" + result);
        return result;
    }

	/***************
	 * Delete
	 *****************/
	//Permet de supprimer une musique de la table Musique
	public int delete(Musique musique) {
		database.delete(DataBaseHelper.CATALOGUE_TABLE, WHERE_ID_MUSIQUE_EQUALS, new String[]{musique.getId() + ""});
		this.deleteVarTemps(musique);
		this.deleteVarIntensite(musique);
		this.deletePartie(musique);
		return database.delete(DataBaseHelper.MUSIQUE_TABLE,
				WHERE_ID_MUSIQUE_EQUALS, new String[]{musique.getId() + ""});
	}

	//Permet de supprimer toute les variations de temps associés à une musique
	public int deleteVarTemps(Musique musique) {
		return database.delete(DataBaseHelper.VarTemps_Table,
				WHERE_ID_MUSIQUE_EQUALS, new String[]{musique.getId() + ""});
	}

	//Permet de supprimer une variation de temps de la table VarTemps
	public int delete(VariationTemps varTemps) {
		return database.delete(DataBaseHelper.VarTemps_Table,
				WHERE_ID_VARTEMPS_EQUALS, new String[]{varTemps.getIdVarTemps() + ""});
	}

	public int deleteVarIntensite(Musique musique) {
		return database.delete(DataBaseHelper.VarIntensite_Table,
				WHERE_ID_MUSIQUE_EQUALS, new String[]{musique.getId() + ""});
	}

	public int delete(VariationIntensite varIntense) {
		return database.delete(DataBaseHelper.VarIntensite_Table,
				WHERE_ID_VARINTENSITE_EQUALS, new String[]{varIntense.getIdVarIntensite() + ""});
	}
	public int deletePartie(Musique musique) {
		return database.delete(DataBaseHelper.Partie_Table,
				WHERE_ID_MUSIQUE_EQUALS, new String[]{musique.getId() + ""});
	}
	public int delete(Partie partie){
		return database.delete(DataBaseHelper.Partie_Table,
				WHERE_ID_PARTIE_EQUALS, new String[]{partie.getId() + ""});
	}

    public int deleteMesureNonLue(Musique musique) {
        return database.delete(DataBaseHelper.Mesures_non_lues_Table,
                WHERE_ID_MUSIQUE_EQUALS, new String[]{musique.getId() + ""});
    }
    public int delete(MesuresNonLues m){
        return database.delete(DataBaseHelper.Mesures_non_lues_Table,
                WHERE_ID_MESURE_NON_LUE_EQUALS, new String[]{m.getId() + ""});
    }

    public int deleteReprise(Musique musique) {
        return database.delete(DataBaseHelper.Reprises_Table,
                WHERE_ID_MUSIQUE_EQUALS, new String[]{musique.getId() + ""});
    }
    public int delete(Reprise reprise){
        return database.delete(DataBaseHelper.Reprises_Table,
                WHERE_ID_Reprise_EQUALS, new String[]{reprise.getId() + ""});
    }

    public int deleteAlerte(Musique musique) {
        return database.delete(DataBaseHelper.Alerte_Table,
                WHERE_ID_MUSIQUE_EQUALS, new String[]{musique.getId() + ""});
    }
    public int delete(Alertes alertes){
        return database.delete(DataBaseHelper.Alerte_Table,
                WHERE_ID_ALERTES_EQUALS, new String[]{alertes.getId() + ""});
    }

	/****************
	 * GET
	 *************/
	//Permet d'obtenir une musique à partir de son id
	public Musique getMusique(int id) {
		String query = "SELECT * FROM "
				+ DataBaseHelper.MUSIQUE_TABLE
				+ " WHERE " + DataBaseHelper.IDMusique + "= ?";
		Log.d("query", query);
		Cursor cursor = database.rawQuery(query, new String[]{Integer.toString(id)});
		Musique musique = new Musique();
		if (cursor.moveToFirst()) {
			musique.setId(cursor.getInt(0));
			musique.setName(cursor.getString(1));
			musique.setNb_mesure(cursor.getInt(2));
		}
		return musique;
	}

	//Permet d'obtenir une musique à partir de son nom
	public Musique getMusique(String name) {
		String query = "SELECT * FROM "
				+ DataBaseHelper.MUSIQUE_TABLE
				+ " WHERE " + DataBaseHelper.NAME_Musique + "= ?";
		Log.d("query", query);
		Cursor cursor = database.rawQuery(query, new String[]{name});
		Musique musique = new Musique();
		if (cursor.moveToFirst()) {
			musique.setId(cursor.getInt(0));
			musique.setName(cursor.getString(1));
			musique.setNb_mesure(cursor.getInt(2));
		}
		return musique;
	}

	//Permet d'obtenir la liste de toutes les musiques de la table musique
	public ArrayList<Musique> getMusiques() {
		ArrayList<Musique> musiques = new ArrayList<Musique>();
		String query = "SELECT * FROM "
				+ DataBaseHelper.MUSIQUE_TABLE;


		Log.d("query", query);
		Cursor cursor = database.rawQuery(query, null);
		while (cursor.moveToNext()) {
			Musique musique = new Musique();
			musique.setId(cursor.getInt(0));
			musique.setName(cursor.getString(1));
			musique.setNb_mesure(cursor.getInt(2));

			musiques.add(musique);
		}
		return musiques;
	}

	//Permet d'obtenir toutes les variations de temps associés à cette musique
	public ArrayList<VariationTemps> getVariationsTemps(Musique musique) {
		ArrayList<VariationTemps> variationsT = new ArrayList<>();
		final String query = "SELECT * FROM "
				+ DataBaseHelper.VarTemps_Table
				+ " WHERE " + DataBaseHelper.IDMusique + "= ?;";


		Log.d("query", query);
		if (musique != null) {
			Cursor cursor = database.rawQuery(query, new String[]{Integer.toString(musique.getId())});
			while (cursor.moveToNext()) {
				VariationTemps varTemps = new VariationTemps();
				varTemps.setIdVarTemps(cursor.getInt(0));
				varTemps.setMusique(cursor.getInt(1));
				varTemps.setMesure_debut(cursor.getInt(2));
				varTemps.setTemps_par_mesure(cursor.getInt(3));
				varTemps.setTempo(cursor.getInt(4));
				varTemps.setUnite_pulsation(cursor.getInt(5));


				variationsT.add(varTemps);
			}
		}
		return variationsT;
	}

	//Permet d'obtenir toutes les variations d'intensités asscociés à cette musqiue
	public ArrayList<VariationIntensite> getVariationsIntensite(Musique musique) {
		ArrayList<VariationIntensite> variationsI = new ArrayList<>();
		final String query = "SELECT * FROM "
				+ DataBaseHelper.VarIntensite_Table
				+ " WHERE " + DataBaseHelper.IDMusique + "=? ;";


		Log.d("query", query);
		Cursor cursor = database.rawQuery(query, new String[]{Integer.toString(musique.getId())});
		while (cursor.moveToNext()) {
			VariationIntensite varIntensite = new VariationIntensite();
			varIntensite.setIdVarIntensite(cursor.getInt(0));
			varIntensite.setIdMusique(cursor.getInt(1));
			varIntensite.setMesureDebut(cursor.getInt(2));
			varIntensite.setTempsDebut(cursor.getInt(3));
			varIntensite.setNb_temps(cursor.getInt(4));
			varIntensite.setIntensite(cursor.getInt(5));


			variationsI.add(varIntensite);
		}
		return variationsI;
	}
	//Permet d'obtenir toutes les parties asscociés à cette musqiue
	public ArrayList<Partie> getParties(Musique musique) {
		ArrayList<Partie> parties = new ArrayList<>();
		final String query = "SELECT * FROM "
				+ DataBaseHelper.Partie_Table
				+ " WHERE " + DataBaseHelper.IDMusique + "=? ;";


		Log.d("query", query);
		Cursor cursor = database.rawQuery(query, new String[]{Integer.toString(musique.getId())});
		while (cursor.moveToNext()) {
			Partie partie = new Partie();
			partie.setId(cursor.getInt(0));
			partie.setIdMusiquet(cursor.getInt(1));
			partie.setMesure_debut(cursor.getInt(2));
			partie.setLabel(cursor.getString(3));

			parties.add(partie);
		}
		return parties;
	}
    //Permet d'obtenir toutes les mesures non lues asscociés à cette musqiue
    public ArrayList<MesuresNonLues> getMesuresNonLues(Musique musique) {
        ArrayList<MesuresNonLues> mnl = new ArrayList<>();
        final String query = "SELECT * FROM "
                + DataBaseHelper.Mesures_non_lues_Table
                + " WHERE " + DataBaseHelper.IDMusique + "=? ;";


        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, new String[]{Integer.toString(musique.getId())});
        while (cursor.moveToNext()) {
            MesuresNonLues m = new MesuresNonLues();
            m.setId(cursor.getInt(0));
            m.setIdMusiquet(cursor.getInt(1));
            m.setMesure_debut(cursor.getInt(2));
            m.setMesure_fin(cursor.getInt(3));
            m.setPassage_reprise(cursor.getInt(4));

            mnl.add(m);
        }
        return mnl;
    }

    //Permet d'obtenir toutes les reprises non lues asscociés à cette musqiue
    public ArrayList<Reprise> getReprises(Musique musique) {
        ArrayList<Reprise> reprises = new ArrayList<>();
        final String query = "SELECT * FROM "
                + DataBaseHelper.Mesures_non_lues_Table
                + " WHERE " + DataBaseHelper.IDMusique + "=? ;";


        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, new String[]{Integer.toString(musique.getId())});
        while (cursor.moveToNext()) {
            Reprise reprise = new Reprise();
            reprise.setId(cursor.getInt(0));
            reprise.setIdMusiquet(cursor.getInt(1));
            reprise.setMesure_debut(cursor.getInt(2));
            reprise.setMesure_fin(cursor.getInt(3));

            reprises.add(reprise);
        }
        return reprises;
    }

    //Permet d'obtenir toutes les reprises non lues asscociés à cette musqiue
    public ArrayList<Alertes> getAlertes(Musique musique) {
        ArrayList<Alertes> alertes = new ArrayList<>();
        final String query = "SELECT * FROM "
                + DataBaseHelper.Alerte_Table
                + " WHERE " + DataBaseHelper.IDMusique + "=? ;";


        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, new String[]{Integer.toString(musique.getId())});
        while (cursor.moveToNext()) {
            Alertes alerte = new Alertes();
            alerte.setId(cursor.getInt(0));
            alerte.setIdMusiquet(cursor.getInt(1));
            alerte.setMesure_debut(cursor.getInt(2));
            alerte.setTemps_debut(cursor.getInt(3));
            alerte.setCouleur(cursor.getInt(4));

            alertes.add(alerte);
        }
        return alertes;
    }


	public static void connect(Context c) {
		//String networkSSID = "\"wifsic-free\"";
		String networkSSID = "\"Maestro\"";
		WifiManager wifi = (WifiManager) c.getSystemService(c.WIFI_SERVICE);
		ConnectivityManager wifi_manager = (ConnectivityManager) c.getSystemService(c.CONNECTIVITY_SERVICE);
		boolean exist = false;
		int res = -1;
		if (!wifi.getConnectionInfo().getSSID().equals(networkSSID)) {
			wifi.disconnect();
			for (WifiConfiguration i : wifi.getConfiguredNetworks()) {
				if (i.SSID != null && i.SSID.equals(networkSSID)) {
					Toast.makeText(c, "SSID exist already: " + wifi.getConnectionInfo().getSSID(), Toast.LENGTH_LONG).show();
					res = i.networkId;
					exist = true;
					break;
				}
			}
			if (!exist) {
				WifiConfiguration conf = new WifiConfiguration();
				conf.SSID = networkSSID;
				conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
				res = wifi.addNetwork(conf);
				Toast.makeText(c, "SSID created: " + wifi.getConnectionInfo().getSSID(), Toast.LENGTH_LONG).show();
			}
			if (!wifi_manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
				Toast.makeText(c, "Impossible de se connecter a la maestrobox", Toast.LENGTH_LONG).show();
			}
			while (!wifi.reconnect()) ;
		} else {
			if (!wifi_manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
				Toast.makeText(c, " Impossible de se connecter a  " + wifi.getConnectionInfo().getSSID(), Toast.LENGTH_LONG).show();

			} else {
				Toast.makeText(c, "Already connected " + wifi.getConnectionInfo().getSSID(), Toast.LENGTH_LONG).show();
			}
		}
	}

	public static void disconnect(Context c) {
		//String networkSSID = "\"wifsic-free\"";
		String networkSSID = "\"Maestro\"";
		WifiManager wifi = (WifiManager) c.getSystemService(c.WIFI_SERVICE);
		ConnectivityManager wifi_manager = (ConnectivityManager) c.getSystemService(c.CONNECTIVITY_SERVICE);
		if(wifi.getConnectionInfo().getSSID().equals(networkSSID)) {
			wifi.removeNetwork(wifi.getConnectionInfo().getNetworkId());
		}
	}
}




