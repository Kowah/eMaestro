package BDD.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import BDD.to.Musique;
import BDD.to.VariationIntensite;
import BDD.to.VariationTemps;

public class DataBaseManager {

	private static final String WHERE_ID_MUSIQUE_EQUALS = DataBaseHelper.KEY_Musique
			+ " =?";
	private static final String WHERE_ID_VARTEMPS_EQUALS = DataBaseHelper.IDVarTemps
			+ " =?";
	private static final String WHERE_ID_VARINTENSITE_EQUALS = DataBaseHelper.IDIntensite
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
	public void clean(){
		database.delete(DataBaseHelper.MUSIQUE_TABLE,null,null);
		database.delete(DataBaseHelper.VarIntensite_Table,null,null);
		database.delete(DataBaseHelper.VarTemps_Table, null, null);
		database.delete(DataBaseHelper.CATALOGUE_TABLE,null,null);
	}
	//Ferme la connection avec la bdd
	public void close() {
		dbHelper.close();
		database = null;
	}

	/******************Save****************/
	//Permet de sauvegarder la musique passé en paramétres dans la base
	//@return l'id dans la BDD
	public long save(Musique Musique) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.NAME_Musique, Musique.getName());
		values.put(DataBaseHelper.NB_MESURE, Musique.getNb_mesure());
		//values.put(DataBaseHelper.NB_PULSATION, Musique.getNb_pulsation());
		//values.put(DataBaseHelper.UNITE_PULSATION, Musique.getUnite_pulsation());
		//values.put(DataBaseHelper.NB_TEMPS_MESURE, Musique.getNb_temps_mesure());

		return database.insert(DataBaseHelper.MUSIQUE_TABLE, null, values);
	}
	//Permet de sauvegarder une Variation de temps dans la table var temps
	//@return l'id dans la BDD
	public long save(VariationTemps varTemps){
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
	public long save(VariationIntensite varIntensite){
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.IDMusique, varIntensite.getIdMusique());
		values.put(DataBaseHelper.TEMPS_DEBUT, varIntensite.getTempsDebut());
		values.put(DataBaseHelper.MESURE_DEBUT, varIntensite.getMesureDebut());
		values.put(DataBaseHelper.NB_TEMPS, varIntensite.getnb_temps());
		values.put(DataBaseHelper.INTENTSITE, varIntensite.getIntensite());

		return database.insert(DataBaseHelper.VarIntensite_Table, null, values);
	}


	/****************Update****************/
	//Permet de mettre a jour la musique passé en paramétre dans la table musique
	public long update(Musique Musique) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.NAME_Musique, Musique.getName());
		values.put(DataBaseHelper.NB_MESURE, Musique.getNb_mesure());

		long result = database.update(DataBaseHelper.MUSIQUE_TABLE, values,
				WHERE_ID_MUSIQUE_EQUALS,
				new String[] { String.valueOf(Musique.getId()) });
		Log.d("Update Result:", "=" + result);
		return result;
	}
	//Permet de mettre a jour la variation de temps dans la table variation temps
	public long update(VariationTemps varTemps){
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.IDMusique, varTemps.getIDmusique());
		values.put(DataBaseHelper.MESURE_DEBUT, varTemps.getMesure_debut());
		values.put(DataBaseHelper.TEMPS_PAR_MESURE, varTemps.getTemps_par_mesure());
		values.put(DataBaseHelper.TEMPO, varTemps.getTempo());
		values.put(DataBaseHelper.UNITE_PULSATION, varTemps.getUnite_pulsation());
		long result = database.update(DataBaseHelper.VarTemps_Table, values,
				WHERE_ID_VARTEMPS_EQUALS,
				new String[] { String.valueOf(varTemps.getIdVarTemps()) });
		Log.d("Update Result:", "=" + result);
		return result;
	}
	//Permet de mettre a jour la variation d'intensité dans la table variation_instensité
	public long update(VariationIntensite varIntensite){
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.IDMusique, varIntensite.getIdMusique());
		values.put(DataBaseHelper.MESURE_DEBUT, varIntensite.getMesureDebut());
		values.put(DataBaseHelper.TEMPS_DEBUT, varIntensite.getTempsDebut());
		values.put(DataBaseHelper.NB_TEMPS, varIntensite.getnb_temps());
		values.put(DataBaseHelper.INTENTSITE, varIntensite.getIntensite());
		long result = database.update(DataBaseHelper.VarIntensite_Table, values,
				WHERE_ID_VARINTENSITE_EQUALS,
				new String[] { String.valueOf(varIntensite.getIdVarIntensite()) });
		Log.d("Update Result:", "=" + result);
		return result;
	}

	/***************Delete*****************/
	//Permet de supprimer une musique de la table Musique
	public int delete(Musique musique) {
		database.delete(DataBaseHelper.CATALOGUE_TABLE, WHERE_ID_MUSIQUE_EQUALS, new String[] { musique.getId() + "" });
		this.deleteVarTemps(musique);
		this.deleteVarIntensite(musique);
		return database.delete(DataBaseHelper.MUSIQUE_TABLE,
				WHERE_ID_MUSIQUE_EQUALS, new String[] { musique.getId() + "" });
	}
	//Permet de supprimer toute les variations de temps associés à une musique
	public int deleteVarTemps(Musique musique) {
		return database.delete(DataBaseHelper.VarTemps_Table,
				WHERE_ID_MUSIQUE_EQUALS, new String[] { musique.getId() + "" });
	}
	//Permet de supprimer une variation de temps de la table VarTemps
	public int delete(VariationTemps varTemps) {
		return database.delete(DataBaseHelper.VarTemps_Table,
				WHERE_ID_VARTEMPS_EQUALS, new String[] { varTemps.getIdVarTemps() + "" });
	}
	public int deleteVarIntensite(Musique musique){
		return database.delete(DataBaseHelper.VarIntensite_Table,
				WHERE_ID_MUSIQUE_EQUALS, new String[] { musique.getId() + "" });
	}

	/****************GET*************/
	//Permet d'obtenir une musique à partir de son id
	public Musique getMusique(int id){
		String query = "SELECT * FROM "
				+ DataBaseHelper.MUSIQUE_TABLE
				+" WHERE " + DataBaseHelper.KEY_Musique + "= ?";
		Log.d("query", query);
		Cursor cursor = database.rawQuery(query, new String[] {Integer.toString(id)});
		Musique musique = new Musique();
		if(cursor.moveToFirst()) {
			musique.setId(cursor.getInt(0));
			musique.setName(cursor.getString(1));
			musique.setNb_mesure(cursor.getInt(2));
			//musique.setNb_pulsation(cursor.getInt(3));
			//musique.setUnite_pulsation(cursor.getInt(4));
			//musique.setNb_temps_mesure(cursor.getInt(5));
		}
		return musique;
	}
	//Permet d'obtenir une musique à partir de son nom
	public Musique getMusique(String name){
		String query = "SELECT * FROM "
				+ DataBaseHelper.MUSIQUE_TABLE
				+" WHERE " + DataBaseHelper.NAME_Musique + "= ?";
		Log.d("query", query);
		Cursor cursor = database.rawQuery(query, new String[] {name});
		Musique musique = new Musique();
		if(cursor.moveToFirst()) {
			musique.setId(cursor.getInt(0));
			musique.setName(cursor.getString(1));
			musique.setNb_mesure(cursor.getInt(2));
			//musique.setNb_pulsation(cursor.getInt(3));
			//musique.setUnite_pulsation(cursor.getInt(4));
			//musique.setNb_temps_mesure(cursor.getInt(5));
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
			//musique.setNb_pulsation(cursor.getInt(3));
			//musique.setUnite_pulsation(cursor.getInt(4));
			//musique.setNb_temps_mesure(cursor.getInt(5));

			musiques.add(musique);
		}
		return musiques;
	}
	//Permet d'obtenir toutes les variations de temps associés à cette musique
	public ArrayList<VariationTemps> getVariationsTemps(Musique musique) {
		ArrayList<VariationTemps> variationsT = new ArrayList<>();
		final String query = "SELECT * FROM "
				+ DataBaseHelper.VarTemps_Table
				+ " WHERE " + DataBaseHelper.IDMusique +"= ?;";


		Log.d("query", query);
		if(musique != null) {
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
				+ " WHERE " + DataBaseHelper.IDMusique +"=? ;";


		Log.d("query", query);
		Cursor cursor = database.rawQuery(query, new String[] {Integer.toString(musique.getId())});
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
}




