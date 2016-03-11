package BDD.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import BDD.to.Evenement;
import BDD.to.Musique;
import BDD.to.Symboles;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;



//import android.database.sqlite.SQLiteQueryBuilder;

public class EvenementDAO extends EvenementDBDAO {

	public static final String EVENEMENT_ID_WITH_PREFIX = "even.id";
	//public static final String EVENEMENT_NAME_WITH_PREFIX = "even.name";
	public static final String MUSIC_NAME_WITH_PREFIX = "music.name";
	public static final String SYMB_NAME_WITH_PREFIX = "symb.name";

	private static final String WHERE_ID_EQUALS = DataBaseHelper.ID_COLUMN + " =?";

	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.ENGLISH);

	public EvenementDAO(Context context) {
		super(context);
	}

	public long save(Evenement evenement) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.EVENEMENT_SYMBOLE_ID, evenement.getSymboles().getId());
		values.put(DataBaseHelper.EVENEMENT_COULEUR, evenement.getCouleur());
		values.put(DataBaseHelper.EVENEMENT_TEMPS, evenement.getnTemps());
		values.put(DataBaseHelper.EVENEMENT_MUSIQUE_ID, evenement.getMusiquet().getId());

		return database.insert(DataBaseHelper.EVENEMENT_TABLE, null, values);
	}

	public long update(Evenement evenement) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.EVENEMENT_SYMBOLE_ID, evenement.getSymboles().getId());
		//values.put(DataBaseHelper.EMPLOYEE_DOB, formatter.format(employee.getDateOfBirth()));
		values.put(DataBaseHelper.EVENEMENT_TEMPS, evenement.getnTemps());
		values.put(DataBaseHelper.EVENEMENT_COULEUR, evenement.getCouleur());
		values.put(DataBaseHelper.EVENEMENT_MUSIQUE_ID, evenement.getMusiquet().getId());

		long result = database.update(DataBaseHelper.EVENEMENT_TABLE, values,
				WHERE_ID_EQUALS,
				new String[] { String.valueOf(evenement.getId()) });
		Log.d("Update Result:", "=" + result);
		return result;
	}

	public int deleteEvenement(Evenement evenement) {
		return database.delete(DataBaseHelper.EVENEMENT_TABLE, WHERE_ID_EQUALS,
				new String[] { evenement.getId() + "" });
	}

	// METHOD 1
	// Uses rawQuery() to query multiple tables
	public ArrayList<Evenement> getEvenements() {
		ArrayList<Evenement> evenements = new ArrayList<Evenement>();
		String query = "SELECT " + EVENEMENT_ID_WITH_PREFIX + ","
				+ DataBaseHelper.EVENEMENT_SYMBOLE_ID + ","
				+ SYMB_NAME_WITH_PREFIX + ","
				+ DataBaseHelper.EVENEMENT_COULEUR + ","
				+ DataBaseHelper.EVENEMENT_TEMPS + ","
				+ DataBaseHelper.EVENEMENT_MUSIQUE_ID + ","
				+ MUSIC_NAME_WITH_PREFIX + " FROM "
				+ DataBaseHelper.EVENEMENT_TABLE + " even, "
				+ DataBaseHelper.SYMBOLE_TABLE + " symb, "
				+ DataBaseHelper.MUSIQUE_TABLE + " music WHERE even."
				+ DataBaseHelper.EVENEMENT_MUSIQUE_ID + " = music."
				+ DataBaseHelper.ID_COLUMN +" AND even."
				+ DataBaseHelper.EVENEMENT_SYMBOLE_ID + " = symb."
				+ DataBaseHelper.ID_COLUMNS;


		Log.d("query", query);
		Cursor cursor = database.rawQuery(query, null);
		while (cursor.moveToNext()) {
			Evenement evenement = new Evenement();
			evenement.setId(cursor.getInt(0));

			Symboles symbole = new Symboles();
			symbole.setId(cursor.getInt(1));
			symbole.setName(cursor.getString(2));
			evenement.setSymboles(symbole);


			evenement.setnTemps(cursor.getInt(4));
			evenement.setCouleur(cursor.getString(3));

			//Car les champs sont inversés
			Musique musique = new Musique();
			musique.setId(cursor.getInt(5));
			musique.setName(cursor.getString(6));

			evenement.setMusique(musique);

			evenements.add(evenement);
		}
		return evenements;
	}
}
