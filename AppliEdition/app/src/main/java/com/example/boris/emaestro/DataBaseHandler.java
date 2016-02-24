package com.example.boris.emaestro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

	
	//Colonnes de la table musique
	private static final String TABLE_Musique = "Musique";
	private static final String KEY_Musique = "id_musique";
	private static final String NAME_Musique = "name";
	private static final String NB_MESURE = "nb_mesure";

	//Colonnes table catalogue
	private static final String Table_Catalogue = "Catalogue";
	private static final String KEY_Catalogue = "id_Catalogue";
	
	//Colonnes table variation rythmes
	private static final String Table_Tempo = "Variation_temps";
	private static final String KEY_TEMPO = "id_variation_temps";
	private static final String Mesure_Deb = "Mesure_Debut";	//Mesure de debut de la variation
	private static final String Temps_Mesure = "Temps_Mesure";	//Temps dans la mesure debut variation
	private static final String Tempo = "Tempo";				//Nouveau tempo
	
	//Creation table musique
	private static final String CREATE_TABLE_MUSIQUE = 
			"CREATE TABLE " + TABLE_Musique + " (" 
			+ KEY_Musique + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NAME_Musique + " TEXT, " 
			+ NB_MESURE + " INTEGER"
			+ ");";

	//Creation table catalogue
	private static final String CREATE_TABLE_CATALOGUE =
			"CREATE TABLE " + Table_Catalogue + " ("
			+ KEY_Catalogue + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ KEY_Musique + " INTEGER"
			+ ");";
	
	//creation table variation rythmes
	private static final String Create_Table_Tempo =
			"CREATE TABLE " + Table_Tempo + " ("
			+ KEY_TEMPO + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ KEY_Musique + "INTEGER, "
			+ Mesure_Deb + " INTEGER, "
			+ Temps_Mesure + " INTEGER, "
			+ Tempo + "INTEGER "
			+ ");";
			
	//Suppressions des tables
	public static final String DROP_TABLE_MUSIQUE =  "DROP TABLE IF EXISTS " + TABLE_Musique + ";";
	public static final String DROP_TABLE_CATALOGUE = "DROP TABLE IF EXISTS " + Table_Catalogue + ";";
	public static final String DROP_TABLE_TEMPO = "DROP TABLE IF EXISTS " + Table_Tempo + ";";
	
	public DataBaseHandler(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}
		
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CREATE_TABLE_MUSIQUE);
		db.execSQL(CREATE_TABLE_CATALOGUE);
		db.execSQL(Create_Table_Tempo);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	  db.execSQL(DROP_TABLE_MUSIQUE);
	  db.execSQL(DROP_TABLE_CATALOGUE);
	  db.execSQL(DROP_TABLE_TEMPO);
	  onCreate(db);
	}
}
