package fr.istic.univ_rennes1.diengadama.mabdemaestro.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "BDeMaestro";
	private static final int DATABASE_VERSION = 1;
//LES TABLES
	public static final String EVENEMENT_TABLE = "evenement";
	public static final String MUSIQUE_TABLE = "musique";
	public static final String SYMBOLE_TABLE = "symboles";

	//Colonnes de la table musique
	public static final String KEY_Musique = "id_musique";
	public static final String NAME_Musique = "name";
	public static final String NB_MESURE = "nb_mesure";
	public static final String NB_PULSATION = "nb_pulsation";
	public static final String UNITE_PULSATION = "unite_pulsation";
	public static final String NB_TEMPS_MESURE = "nb_temps_mesure";

	//LES COLONNES DES TABLES
	public static final String ID_COLUMN = "id";
	public static final String ID_COLUMNS = "id";
	public static final String NAME_COLUMN = "name";
	public static final String NAME_COLUMNS = "name";
	//public static final String SYMBOLE_COLUMN = "nomsymb";
	public static final String EVENEMENT_SYMBOLE_ID = "symb_id";
	public static final String EVENEMENT_TEMPS = "nTemps";
	public static final String EVENEMENT_COULEUR = "couleur";
	public static final String EVENEMENT_MUSIQUE_ID = "music_id";
	//CREATION DE LA TABLE EVENEMENT
	/*
	public static final String CREATE_EVENEMENT_TABLE = "CREATE TABLE "

			+ EVENEMENT_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY, "
			+ EVENEMENT_SYMBOLE_ID + " INT,"
			+ EVENEMENT_COULEUR + " TEXT,"
			+ EVENEMENT_TEMPS + " INT,"
			+ EVENEMENT_MUSIQUE_ID + " INT,"
			+ "FOREIGN KEY(" + EVENEMENT_SYMBOLE_ID + ") REFERENCES "+ SYMBOLE_TABLE + "(id), "
			+ "FOREIGN KEY(" + EVENEMENT_MUSIQUE_ID + ") REFERENCES "+ MUSIQUE_TABLE + "(id))";
			*/
	/*CREATE TABLE track(
  trackid     INTEGER,
  trackname   TEXT,
  trackartist INTEGER,
  FOREIGN KEY(trackartist) REFERENCES artist(artistid)
);
 CREATE_EVENEMENT_TABLE = "CREATE TABLE "
			+ EVENEMENT_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY, "
			+ EVENEMENT_SYMBOLE_ID + " INT,"
			+ "FOREIGN KEY(" + EVENEMENT_SYMBOLE_ID + ") REFERENCES "
			+ SYMBOLE_TABLE + "(idS), " +  EVENEMENT_TEMPS  + "INT,"
			+ EVENEMENT_COULEUR + " TEXT, " + EVENEMENT_MUSIQUE_ID + " INT, "
			+ "FOREIGN KEY(" + EVENEMENT_MUSIQUE_ID + ") REFERENCES "
			+ MUSIQUE_TABLE + "(id) " + ")";
	*/
//Creation table musique
	private static final String CREATE_MUSIQUE_TABLE =
		           "CREATE TABLE " + MUSIQUE_TABLE + " ("
	               + KEY_Musique + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		           + NAME_Musique + " TEXT, "
	               + NB_MESURE + " INTEGER, "
				   + NB_PULSATION + " INTEGER, "
				   + UNITE_PULSATION + " INTEGER, "
				   + NB_TEMPS_MESURE + " INTEGER"
	               + ");";//CREATION DE LA TABLE MUSIQUE

	//CREATION DE LA TABLE SYMBOLE
	/*public static final String CREATE_SYMBOLE_TABLE = "CREATE TABLE "
			+ SYMBOLE_TABLE + "(" + ID_COLUMNS + " INTEGER PRIMARY KEY,"
			+ NAME_COLUMNS + ")";
			*/
//L'instance de notre base de données
	private static DataBaseHelper instance;
//Fonction qui retourne l'instance en cours1
	public static synchronized DataBaseHelper getHelper(Context context) {
		if (instance == null)
			instance = new DataBaseHelper(context);
		return instance;
	}
//Contructeur
	private DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			// Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_MUSIQUE_TABLE);
	//	db.execSQL(CREATE_SYMBOLE_TABLE);
	//	db.execSQL(CREATE_EVENEMENT_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}