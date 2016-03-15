package BDD.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import BDD.to.Musique;

import java.util.ArrayList;


public class MusiqueDAO extends DataBaseManager {

	private static final String WHERE_ID_EQUALS = DataBaseHelper.KEY_Musique
			+ " =?";

	public MusiqueDAO(Context context) {
		super(context);
	}

	public void clean(){
		database.delete(DataBaseHelper.MUSIQUE_TABLE, null, null);
	}
	public long save(Musique Musique) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.NAME_Musique, Musique.getName());
		values.put(DataBaseHelper.NB_MESURE, Musique.getNb_mesure());
		values.put(DataBaseHelper.NB_PULSATION, Musique.getNb_pulsation());
		values.put(DataBaseHelper.UNITE_PULSATION, Musique.getUnite_pulsation());
		values.put(DataBaseHelper.NB_TEMPS_MESURE, Musique.getNb_temps_mesure());

		return database.insert(DataBaseHelper.MUSIQUE_TABLE, null, values);
	}

	public long update(Musique Musique) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.NAME_Musique, Musique.getName());
		values.put(DataBaseHelper.NB_MESURE, Musique.getNb_mesure());
		values.put(DataBaseHelper.NB_PULSATION, Musique.getNb_pulsation());
		values.put(DataBaseHelper.UNITE_PULSATION, Musique.getUnite_pulsation());
		values.put(DataBaseHelper.NB_TEMPS_MESURE, Musique.getNb_temps_mesure());
		long result = database.update(DataBaseHelper.MUSIQUE_TABLE, values,
				WHERE_ID_EQUALS,
				new String[] { String.valueOf(Musique.getId()) });
		Log.d("Update Result:", "=" + result);
		return result;
	}

	public int deleteMusic(Musique Musique) {
		return database.delete(DataBaseHelper.MUSIQUE_TABLE,
				WHERE_ID_EQUALS, new String[] { Musique.getId() + "" });
	}

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
			musique.setNb_pulsation(cursor.getInt(3));
			musique.setUnite_pulsation(cursor.getInt(4));
			musique.setNb_temps_mesure(cursor.getInt(5));
			}
		return musique;
		}

	public Musique getMusique(String name){
		String query = "SELECT * FROM "
				+ DataBaseHelper.MUSIQUE_TABLE
				+" WHERE " + DataBaseHelper.NAME_Musique + "= ?";
		Log.d("query", query);
		Cursor cursor = database.rawQuery(query, new String[] {name});
		Musique musique = new Musique("",-1,-1,-1,-1);
		if(cursor.moveToFirst()) {
			musique.setId(cursor.getInt(0));
			musique.setName(cursor.getString(1));
			musique.setNb_mesure(cursor.getInt(2));
			musique.setNb_pulsation(cursor.getInt(3));
			musique.setUnite_pulsation(cursor.getInt(4));
			musique.setNb_temps_mesure(cursor.getInt(5));
		}
		return musique;
	}
	// METHOD 1
	// Uses rawQuery() to query multiple tables
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
			musique.setNb_pulsation(cursor.getInt(3));
			musique.setUnite_pulsation(cursor.getInt(4));
			musique.setNb_temps_mesure(cursor.getInt(5));

			musiques.add(musique);
		}
		return musiques;
	}
}
