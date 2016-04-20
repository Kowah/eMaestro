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
import BDD.to.Armature;
import BDD.to.Evenement;
import BDD.to.MesuresNonLues;
import BDD.to.Musique;
import BDD.to.Partie;
import BDD.to.Reprise;
import BDD.to.Suspension;
import BDD.to.VarRythmes;
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
    private static final String WHERE_ID_VAR_RYTHMES_EQUALS = DataBaseHelper.IDVarRythme
            + " =?";
    private static final String WHERE_ID_SUSPENSION_EQUALS = DataBaseHelper.IDSuspension
            + " =?";
    private static final String WHERE_ID_ARMATURE_EQUALS = DataBaseHelper.IDArmature
            + " =?";
    private static final String WHERE_ID_EVENEMENT_EQUALS = DataBaseHelper.IDEvenement
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
        database.delete(DataBaseHelper.Evenement_Table, null, null);
        database.delete(DataBaseHelper.Partie_Table, null, null);
		database.delete(DataBaseHelper.CATALOGUE_TABLE, null, null);
        database.delete(DataBaseHelper.Armature_Table, null, null);
        database.delete(DataBaseHelper.Suspension_Table, null, null);
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
    /*
    //Permet de sauvegarder les mesures non lues en paramétres dans la base
    //@return l'id dans la BDD
    public long save(MesuresNonLues m) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, m.getIdMusique());
        values.put(DataBaseHelper.FLAG, m.getFlag());
        values.put(DataBaseHelper.MESURE_DEBUT, m.getMesure_debut());
        values.put(DataBaseHelper.ARG2, m.getArg2());
        values.put(DataBaseHelper.PASSAGE_REPRISE, m.getPassage_reprise());

        return database.insert(DataBaseHelper.Mesures_non_lues_Table, null, values);
    }
    //Permet de sauvegarder les reprises dans la base
    //@return l'id dans la BDD
    public long save(Reprise reprise) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, reprise.getIdMusique());
        values.put(DataBaseHelper.FLAG, reprise.getFlag());
        values.put(DataBaseHelper.MESURE_DEBUT, reprise.getMesure_debut());
        values.put(DataBaseHelper.ARG2, reprise.getArg2());
        values.put(DataBaseHelper.PASSAGE_REPRISE, reprise.getPassage_reprise());

        return database.insert(DataBaseHelper.Reprises_Table, null, values);
    }

    //Permet de sauvegarder les alertes dans la base
    //@return l'id dans la BDD
    public long save(Alertes alertes) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, alertes.getIdMusique());
        values.put(DataBaseHelper.MESURE_DEBUT, alertes.getMesure_debut());
        values.put(DataBaseHelper.TEMPS_DEBUT, alertes.getTemps_debut());
        values.put(DataBaseHelper.Couleur, alertes.getCouleur());
        values.put(DataBaseHelper.PASSAGE_REPRISE, alertes.getPassage_reprise());

        return database.insert(DataBaseHelper.Alerte_Table, null, values);
    }

    //Permet de sauvegarder les variations de rythmes dans la base
    //@return l'id dans la BDD
    public long save(VarRythmes varRythmes) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, varRythmes.getIdMusique());
        values.put(DataBaseHelper.MESURE_DEBUT, varRythmes.getMesure_debut());
        values.put(DataBaseHelper.TEMPS_DEBUT, varRythmes.getTemps_debut());
        values.put(DataBaseHelper.Taux_Varitation, varRythmes.getTauxVariation());
        values.put(DataBaseHelper.PASSAGE_REPRISE, varRythmes.getPassage_reprise());


        return database.insert(DataBaseHelper.Variation_Rythme_Table, null, values);
    }

    //Permet de sauvegarder les suspensions dans la base
    //@return l'id dans la BDD
    public long save(Suspension suspension) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, suspension.getIdMusique());
        values.put(DataBaseHelper.MESURE, suspension.getMesure_debut());
        values.put(DataBaseHelper.TEMPS, suspension.getTemps());
        values.put(DataBaseHelper.DUREE, suspension.getDuree());
        values.put(DataBaseHelper.PASSAGE_REPRISE, suspension.getPassage_reprise());


        return database.insert(DataBaseHelper.Suspension_Table, null, values);
    }

    //Permet de sauvegarder les armatures dans la base
    //@return l'id dans la BDD
    public long save(Armature armature) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, armature.getIdMusique());
        values.put(DataBaseHelper.MESURE_DEBUT, armature.getMesure_debut());
        values.put(DataBaseHelper.TEMPS_DEBUT, armature.getTemps_debut());
        values.put(DataBaseHelper.Alteration, armature.getAlteration());
        values.put(DataBaseHelper.PASSAGE_REPRISE, armature.getPassage_reprise());


        return database.insert(DataBaseHelper.Armature_Table, null, values);
    }*/
    public long save(Evenement e){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, e.getIdMusique());
        values.put(DataBaseHelper.FLAG, e.getFlag());
        values.put(DataBaseHelper.MESURE_DEBUT, e.getMesure_debut());
        values.put(DataBaseHelper.ARG2, e.getArg2());
        values.put(DataBaseHelper.PASSAGE_REPRISE, e.getPassage_reprise());
        values.put(DataBaseHelper.ARG3, e.getArg3());
        return database.insert(DataBaseHelper.Evenement_Table, null, values);
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
    /*
    //Permet de mettre a jour la mesure non lue passé en paramétre dans la table mesure non lue
    public long update(MesuresNonLues m) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, m.getIdMusique());
        values.put(DataBaseHelper.MESURE_DEBUT, m.getMesure_debut());
        values.put(DataBaseHelper.ARG2, m.getArg2());
        values.put(DataBaseHelper.PASSAGE_REPRISE, m.getPassage_reprise());

        long result = database.update(DataBaseHelper.Mesures_non_lues_Table, values,
                WHERE_ID_MESURE_NON_LUE_EQUALS,
                new String[]{String.valueOf(m.getId())});
        Log.d("Update Result:", "=" + result);
        return result;
    }
    //Permet de mettre a jour la reprise passé en paramétre dans la table reprise
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

    //Permet de mettre a jour l'alerte passé en paramétre dans la table alerte
    public long update(Alertes alertes) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, alertes.getIdMusique());
        values.put(DataBaseHelper.MESURE_DEBUT, alertes.getMesure_debut());
        values.put(DataBaseHelper.TEMPS_DEBUT, alertes.getTemps_debut());
        values.put(DataBaseHelper.Couleur, alertes.getCouleur());
        values.put(DataBaseHelper.PASSAGE_REPRISE, alertes.getPassage_reprise());

        long result = database.update(DataBaseHelper.Alerte_Table, values,
                WHERE_ID_ALERTES_EQUALS,
                new String[]{String.valueOf(alertes.getId())});
        Log.d("Update Result:", "=" + result);
        return result;
    }
    //Permet de mettre a jour la variation de rythme passé en paramétre dans la table VarRythmes
    public long update(VarRythmes varRythmes) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, varRythmes.getIdMusique());
        values.put(DataBaseHelper.MESURE_DEBUT, varRythmes.getMesure_debut());
        values.put(DataBaseHelper.TEMPS_DEBUT, varRythmes.getTemps_debut());
        values.put(DataBaseHelper.Taux_Varitation, varRythmes.getTauxVariation());
        values.put(DataBaseHelper.PASSAGE_REPRISE, varRythmes.getPassage_reprise());

        long result = database.update(DataBaseHelper.Variation_Rythme_Table, values,
                WHERE_ID_VAR_RYTHMES_EQUALS,
                new String[]{String.valueOf(varRythmes.getId())});
        Log.d("Update Result:", "=" + result);
        return result;
    }

    //Permet de mettre a jour la suspension passé en paramétre dans la table suspension
    public long update(Suspension suspension){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, suspension.getIdMusique());
        values.put(DataBaseHelper.MESURE, suspension.getMesure_debut());
        values.put(DataBaseHelper.TEMPS, suspension.getTemps());
        values.put(DataBaseHelper.DUREE, suspension.getDuree());
        values.put(DataBaseHelper.PASSAGE_REPRISE, suspension.getPassage_reprise());

        long result = database.update(DataBaseHelper.Suspension_Table, values,
                WHERE_ID_SUSPENSION_EQUALS,
                new String[]{String.valueOf(suspension.getId())});
        Log.d("Update Result:", "=" + result);
        return result;
    }
    //Permet de mettre a jour l'armature passé en paramétre dans la table armature
    public long update(Armature armature){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, armature.getIdMusique());
        values.put(DataBaseHelper.MESURE, armature.getMesure_debut());
        values.put(DataBaseHelper.TEMPS, armature.getTemps_debut());
        values.put(DataBaseHelper.DUREE, armature.getAlteration());
        values.put(DataBaseHelper.PASSAGE_REPRISE, armature.getPassage_reprise());

        long result = database.update(DataBaseHelper.Armature_Table, values,
                WHERE_ID_ARMATURE_EQUALS,
                new String[]{String.valueOf(armature.getId())});
        Log.d("Update Result:", "=" + result);
        return result;
    }
    */
    //Permet de mettre a jour l'evenement passé en paramétre dans la table evenement
    public long update(Evenement e) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, e.getIdMusique());
        values.put(DataBaseHelper.FLAG, e.getFlag());
        values.put(DataBaseHelper.MESURE_DEBUT, e.getMesure_debut());
        values.put(DataBaseHelper.ARG2, e.getArg2());
        values.put(DataBaseHelper.PASSAGE_REPRISE, e.getPassage_reprise());
        values.put(DataBaseHelper.ARG3, e.getArg3());

        long result = database.update(DataBaseHelper.Evenement_Table, values,
                WHERE_ID_EVENEMENT_EQUALS,
                new String[]{String.valueOf(e.getId())});
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
        this.deleteEvenement(musique);
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

    /*
    public int delete(MesuresNonLues m){
        return database.delete(DataBaseHelper.Mesures_non_lues_Table,
                WHERE_ID_MESURE_NON_LUE_EQUALS, new String[]{m.getId() + ""});
    }
    public int delete(Reprise reprise){
        return database.delete(DataBaseHelper.Reprises_Table,
                WHERE_ID_Reprise_EQUALS, new String[]{reprise.getId() + ""});
    }
    public int delete(Alertes alertes){
        return database.delete(DataBaseHelper.Alerte_Table,
                WHERE_ID_ALERTES_EQUALS, new String[]{alertes.getId() + ""});
    }
    public int delete(VarRythmes varRythmes){
        return database.delete(DataBaseHelper.Variation_Rythme_Table,
                WHERE_ID_VAR_RYTHMES_EQUALS, new String[]{varRythmes.getId() + ""});
    }
    public int delete(Suspension suspension){
        return database.delete(DataBaseHelper.Suspension_Table,
                WHERE_ID_SUSPENSION_EQUALS, new String[]{suspension.getId() + ""});
    }
     public int delete(Armature armature){
        return database.delete(DataBaseHelper.Armature_Table,
                WHERE_ID_ARMATURE_EQUALS, new String[]{armature.getId() + ""});
    }*/
    //Permet de supprimer un evenement particulier
    public int delete(Evenement e) {
        return database.delete(DataBaseHelper.Evenement_Table,
                WHERE_ID_EVENEMENT_EQUALS, new String[]{e.getId() + ""});
    }
/********************************/
    String and = " AND " + DataBaseHelper.FLAG + " =?";
    public int deleteEvenement(Musique musique) {
        return database.delete(DataBaseHelper.Evenement_Table,
                WHERE_ID_MUSIQUE_EQUALS, new String[]{musique.getId() + ""});
    }
    public int deleteMesureNonLue(Musique musique) {
        return database.delete(DataBaseHelper.Evenement_Table,
                WHERE_ID_MUSIQUE_EQUALS + and, new String[]{musique.getId() + "", String.valueOf(DataBaseHelper.FLAG_MNL)});
    }
    public int deleteReprise(Musique musique) {
        return database.delete(DataBaseHelper.Evenement_Table,
                WHERE_ID_MUSIQUE_EQUALS + and, new String[]{musique.getId() + "", String.valueOf(DataBaseHelper.FLAG_REPRISE)});
    }
    public int deleteArmature(Musique musique) {
        return database.delete(DataBaseHelper.Evenement_Table,
                WHERE_ID_MUSIQUE_EQUALS + and, new String[]{musique.getId() + "", String.valueOf(DataBaseHelper.FLAG_ARMATURE)});
    }
    public int deleteSuspension(Musique musique) {
        return database.delete(DataBaseHelper.Evenement_Table,
                WHERE_ID_MUSIQUE_EQUALS + and, new String[]{musique.getId() + "", String.valueOf(DataBaseHelper.FLAG_SUSPENSION)});
    }
    public int deleteVarRythmes(Musique musique) {
        return database.delete(DataBaseHelper.Evenement_Table,
                WHERE_ID_MUSIQUE_EQUALS + and, new String[]{musique.getId() + "", String.valueOf(DataBaseHelper.FLAG_VARRYTHMES)});
    }
    public int deleteAlerte(Musique musique) {
        return database.delete(DataBaseHelper.Evenement_Table,
                WHERE_ID_MUSIQUE_EQUALS + and, new String[]{musique.getId() + "", String.valueOf(DataBaseHelper.FLAG_ALERTE)});
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
        cursor.close();
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
        cursor.close();
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
        cursor.close();
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
            cursor.close();
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
        cursor.close();
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
			partie.setIdMusique(cursor.getInt(1));
			partie.setMesure_debut(cursor.getInt(2));
			partie.setLabel(cursor.getString(3));

			parties.add(partie);
		}
        cursor.close();
		return parties;
	}
    //Permet d'obtenir toutes les mesures non lues asscociés à cette musqiue
    public ArrayList<MesuresNonLues> getMesuresNonLues(Musique musique) {
        ArrayList<MesuresNonLues> mnl = new ArrayList<>();
        final String query = "SELECT * FROM "
                + DataBaseHelper.Evenement_Table
                + " WHERE " + DataBaseHelper.IDMusique + "=?  AND "+ DataBaseHelper.FLAG+" =?;";


        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, new String[]{Integer.toString(musique.getId()), Integer.toString(DataBaseHelper.FLAG_MNL)});
        while (cursor.moveToNext()) {
            MesuresNonLues m = new MesuresNonLues();
            m.setId(cursor.getInt(0));
            m.setIdMusique(cursor.getInt(1));
            m.setFlag(cursor.getInt(2));
            m.setMesure_debut(cursor.getInt(3));
            m.setArg2(cursor.getInt(4));
            m.setPassage_reprise(cursor.getInt(5));

            mnl.add(m);
        }
        cursor.close();
        return mnl;
    }

    //Permet d'obtenir toutes les reprises non lues asscociés à cette musqiue
    public ArrayList<Reprise> getReprises(Musique musique) {
        ArrayList<Reprise> reprises = new ArrayList<>();
        final String query = "SELECT * FROM "
                + DataBaseHelper.Evenement_Table
                + " WHERE " + DataBaseHelper.IDMusique + "=?  AND "+ DataBaseHelper.FLAG+" =?;";


        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, new String[]{Integer.toString(musique.getId()), Integer.toString(DataBaseHelper.FLAG_REPRISE)});
        while (cursor.moveToNext()) {
            Reprise reprise = new Reprise();
            reprise.setId(cursor.getInt(0));
            reprise.setIdMusique(cursor.getInt(1));
            reprise.setFlag(cursor.getInt(2));
            reprise.setMesure_debut(cursor.getInt(3));
            reprise.setMesure_fin(cursor.getInt(4));

            reprises.add(reprise);
        }
        cursor.close();
        return reprises;
    }

    //Permet d'obtenir toutes les reprises non lues asscociés à cette musqiue
    public ArrayList<Alertes> getAlertes(Musique musique) {
        ArrayList<Alertes> alertes = new ArrayList<>();
        final String query = "SELECT * FROM "
                + DataBaseHelper.Evenement_Table
                + " WHERE " + DataBaseHelper.IDMusique + "=?  AND "+ DataBaseHelper.FLAG+" =?;";


        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, new String[]{Integer.toString(musique.getId()), Integer.toString(DataBaseHelper.FLAG_ALERTE)});
        while (cursor.moveToNext()) {
            Alertes alerte = new Alertes();
            alerte.setId(cursor.getInt(0));
            alerte.setIdMusique(cursor.getInt(1));
            alerte.setFlag(cursor.getInt(2));
            alerte.setMesure_debut(cursor.getInt(3));
            alerte.setTemps_debut(cursor.getInt(4));
            alerte.setCouleur(cursor.getInt(5));
            alerte.setPassage_reprise(cursor.getInt(6));

            alertes.add(alerte);
        }
        cursor.close();
        return alertes;
    }
    //Permet d'obtenir toutes les reprises non lues asscociés à cette musqiue
    public ArrayList<VarRythmes> getVarRythmes(Musique musique) {
        ArrayList<VarRythmes> varRythmes = new ArrayList<>();
        final String query = "SELECT * FROM "
                + DataBaseHelper.Evenement_Table
                + " WHERE " + DataBaseHelper.IDMusique + "=?  AND "+ DataBaseHelper.FLAG+" =?;";


        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, new String[]{Integer.toString(musique.getId()), Integer.toString(DataBaseHelper.FLAG_VARRYTHMES)});
        while (cursor.moveToNext()) {
            VarRythmes varRythme = new VarRythmes();
            varRythme.setId(cursor.getInt(0));
            varRythme.setIdMusique(cursor.getInt(1));
            varRythme.setFlag(cursor.getInt(2));
            varRythme.setMesure_debut(cursor.getInt(3));
            varRythme.setTemps_debut(cursor.getInt(4));
            varRythme.setTauxVariation(cursor.getInt(5));
            varRythme.setPassage_reprise(cursor.getInt(6));

            varRythmes.add(varRythme);
        }
        cursor.close();
        return varRythmes;
    }
    //Permet d'obtenir toutes les suspensions asscociés à cette musqiue
    public ArrayList<Suspension> getSuspension(Musique musique) {
        ArrayList<Suspension> suspensions = new ArrayList<>();
        final String query = "SELECT * FROM "
                + DataBaseHelper.Evenement_Table
                + " WHERE " + DataBaseHelper.IDMusique + "=?  AND "+ DataBaseHelper.FLAG+" =?;";


        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, new String[]{Integer.toString(musique.getId()), Integer.toString(DataBaseHelper.FLAG_SUSPENSION)});
        while (cursor.moveToNext()) {
            Suspension suspension = new Suspension();
            suspension.setId(cursor.getInt(0));
            suspension.setIdMusique(cursor.getInt(1));
            suspension.setFlag(cursor.getInt(2));
            suspension.setMesure_debut(cursor.getInt(3));
            suspension.setTemps(cursor.getInt(4));
            suspension.setDuree(cursor.getInt(5));
            suspension.setPassage_reprise(cursor.getInt(6));

            suspensions.add(suspension);
        }
        cursor.close();
        return suspensions;
    }
    //Permet d'obtenir toutes les armatures asscociés à cette musqiue
    public ArrayList<Armature> getArmature(Musique musique) {
        ArrayList<Armature> armatures = new ArrayList<>();
        final String query = "SELECT * FROM "
                + DataBaseHelper.Evenement_Table
                + " WHERE " + DataBaseHelper.IDMusique + "=?  AND "+ DataBaseHelper.FLAG+" =?;";


        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, new String[]{Integer.toString(musique.getId()), Integer.toString(DataBaseHelper.FLAG_ARMATURE)});
        while (cursor.moveToNext()) {
            Armature armature = new Armature();
            armature.setId(cursor.getInt(0));
            armature.setIdMusique(cursor.getInt(1));
            armature.setFlag(cursor.getInt(2));
            armature.setMesure_debut(cursor.getInt(3));
            armature.setTemps_debut(cursor.getInt(4));
            armature.setAlteration(cursor.getInt(5));
            armature.setPassage_reprise(cursor.getInt(6));

            armatures.add(armature);
        }
        cursor.close();
        return armatures;
    }
    //Permet d'obtenir toutes les evenenements asscociés à cette musqiue
    public ArrayList<Evenement> getEvenement(Musique musique) {
        ArrayList<Evenement> events = new ArrayList<>();
        final String query = "SELECT * FROM "
                + DataBaseHelper.Evenement_Table
                + " WHERE " + DataBaseHelper.IDMusique + "=? ;";


        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, new String[]{Integer.toString(musique.getId())});
        while (cursor.moveToNext()) {
            Evenement e = new Evenement();
            e.setId(cursor.getInt(0));
            e.setIdMusique(cursor.getInt(1));
            e.setFlag(cursor.getInt(2));
            e.setMesure_debut(cursor.getInt(3));
            e.setArg2(cursor.getInt(4));
            e.setPassage_reprise(cursor.getInt(5));
            e.setArg3(cursor.getInt(6));

            events.add(e);
        }
        cursor.close();
        return events;
    }

	public static void connect(Context c, String Ssid) {
		//String networkSSID = "\"wifsic-free\"";
		String networkSSID = "\""+Ssid+"\"";
		WifiManager wifi = (WifiManager) c.getSystemService(c.WIFI_SERVICE);
		ConnectivityManager wifi_manager = (ConnectivityManager) c.getSystemService(c.CONNECTIVITY_SERVICE);
        Toast.makeText(c, "Connection a : " + Ssid, Toast.LENGTH_LONG).show();

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
		WifiManager wifi = (WifiManager) c.getSystemService(c.WIFI_SERVICE);
        wifi.disconnect();
		}
	}





