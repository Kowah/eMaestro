package BDD.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "BDeMaestro";
	private static final int DATABASE_VERSION = 8;
//LES TABLES
	public static final String EVENEMENT_TABLE = "evenement";
	public static final String MUSIQUE_TABLE = "musique";
	public static final String CATALOGUE_TABLE = "catalogue";
	public static final String SYMBOLE_TABLE = "symboles";
	public static final String VarTemps_Table = "VarTemps";
	public static final String VarIntensite_Table = "VarIntensite";

	//Colonnes de la table musique
	public static final String KEY_Musique = "id_musique";
	public static final String NAME_Musique = "nom";
	public static final String NB_MESURE = "nb_mesures";
	public static final String NB_PULSATION = "nb_pulsation";

	//Colonnes de la table Catalogue
	public static final String IdCatalogue = "id_catalogue";
	//public static final String KEY_Musique = "id_musique";

	//Colonnes de la table variation temps
	public static final String IDVarTemps = "id_variation_temps";
	public static final String IDMusique = "id_musique";
	public static final String MESURE_DEBUT = "mesure_debut";
	public static final String TEMPS_PAR_MESURE = "temps_par_mesure";
	public static final String TEMPO = "tempo";
	public static final String UNITE_PULSATION = "unite_pulsation";

	//Colones de la table variation intensite
	public static final String IDIntensite = "id_variation_intensite";
	//public static final String IDMusique = "id_musique";
	//public static final String MESURE_DEBUT = "mesure_debut";
	public static final String TEMPS_DEBUT = "temps_debut";
	public static final String NB_TEMPS = "nb_temps";
	public static final String INTENTSITE = "intensite";

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

	//Creation table musique
	private static final String CREATE_MUSIQUE_TABLE =
			"CREATE TABLE " + MUSIQUE_TABLE + " ("
					+ KEY_Musique + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ NAME_Musique + " TEXT, "
					+ NB_MESURE + " INTEGER "
					+ ");";
	//Destruction de la table musique
	private static final String MUSIQUE_TABLE_DROP = "DROP TABLE IF EXISTS "
			+ MUSIQUE_TABLE +";";

	//Creation de la table catalogue
	private static final String CREATE_CATALOGUE_TABLE =
			"CREATE TABLE " + CATALOGUE_TABLE + " ("
					+ IdCatalogue + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEY_Musique + " INTEGER "
					+");";
	//Destruction de la table catalogue
	private static final String CATALOGUE_TABLE_DROP = "DROP TABLE IF EXISTS "
			+ CATALOGUE_TABLE +";";

	//Creation table Variation temps
	private static final String CREATE_VarTemps_TABLE =
			"CREATE TABLE " + VarTemps_Table + " ("
					+ IDVarTemps + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ IDMusique + " INTEGER, "
					+ MESURE_DEBUT + " INTEGER, "
					+ TEMPS_PAR_MESURE + " INTEGER, "
					+ TEMPO + " INTEGER, "
					+ UNITE_PULSATION + " INTEGER "
					+ ");";
	//Destruction de la table VarTemps
	private static final String VarTemps_TABLE_DROP = "DROP TABLE IF EXISTS "
			+ VarTemps_Table +";";

	//Creation de la table variation intensite
	private static final String CREATE_VarIntensite_Table =
			"CREATE TABLE " + VarIntensite_Table + " ("
					+ IDIntensite + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ IDMusique + " INTEGER, "
					+ MESURE_DEBUT + " INTEGER, "
					+ TEMPS_DEBUT + " INTEGER, "
					+ NB_TEMPS + " INTEGER, "
					+ INTENTSITE + " INTEGER"
					+ ");";
	//Destruction de la table VarIntensite
	private static final String VarIntensite_TABLE_DROP = "DROP TABLE IF EXISTS "
			+ VarIntensite_Table +";";

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
		//CREATION DE LA TABLE SYMBOLE
		/*public static final String CREATE_SYMBOLE_TABLE = "CREATE TABLE "
			+ SYMBOLE_TABLE + "(" + ID_COLUMNS + " INTEGER PRIMARY KEY,"
			+ NAME_COLUMNS + ")";
			*/
	/*
	//L'instance de notre base de données
		private static DataBaseHelper instance;
	//Fonction qui retourne l'instance en cours1
		public static synchronized DataBaseHelper getHelper(Context context) {
			if (instance == null)
				instance = new DataBaseHelper(context);
			return instance;
		}
	*/

	//Contructeur
	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		System.out.println(CREATE_VarIntensite_Table);
		super.onOpen(db);
		if (!db.isReadOnly()) {
			// Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_MUSIQUE_TABLE);
		db.execSQL(CREATE_CATALOGUE_TABLE);
		db.execSQL(CREATE_VarTemps_TABLE);
		db.execSQL(CREATE_VarIntensite_Table);
	//	db.execSQL(CREATE_SYMBOLE_TABLE);
	//	db.execSQL(CREATE_EVENEMENT_TABLE);
	}

	//Supprime et recrée les tables si mises a jour de la base de données
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(MUSIQUE_TABLE_DROP);
		db.execSQL(CATALOGUE_TABLE_DROP);
		db.execSQL(VarTemps_TABLE_DROP);
		db.execSQL(VarIntensite_TABLE_DROP);
		this.onCreate(db);
	}
}
