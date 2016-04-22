package BDD.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "emaestro";
	private static final int DATABASE_VERSION = 7;
    //LES TABLES
	public static final String MUSIQUE_TABLE = "Musique";
	public static final String CATALOGUE_TABLE = "Catalogue";
	public static final String VarTemps_Table = "VariationTemps";
	public static final String VarIntensite_Table = "VariationIntensite";
	public static final String Partie_Table = "Partie";
	public static final String Evenement_Table = "Evenement";

	public static final int FLAG_MNL = 0;
	public static final int FLAG_REPRISE = 1;
	public static final int FLAG_ALERTE = 2;
	public static final int FLAG_VARRYTHMES = 3;
	public static final int FLAG_SUSPENSION = 4;
	public static final int FLAG_ARMATURE = 5;




	/***********************************************************************/
	/********************************MUSIQUE********************************/
	/***********************************************************************/

	//Colonnes de la table musique
	public static final String IDMusique = "id_musique";
	public static final String NAME_Musique = "nom";
	public static final String NB_MESURE = "nb_mesures";
	//Creation table musique
	private static final String CREATE_MUSIQUE_TABLE =
			"CREATE TABLE " + MUSIQUE_TABLE + " ("
					+ IDMusique + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ NAME_Musique + " TEXT, "
					+ NB_MESURE + " INTEGER "
					+ ");";
	//Destruction de la table musique
	private static final String MUSIQUE_TABLE_DROP = "DROP TABLE IF EXISTS "
			+ MUSIQUE_TABLE +";";

	/***********************************************************************/
	/*******************************CATALOGUE*******************************/
	/***********************************************************************/

	//Colonnes de la table Catalogue
	public static final String IdCatalogue = "id_catalogue";
	//public static final String KEY_Musique = "id_musique";
	//Creation de la table catalogue
	private static final String CREATE_CATALOGUE_TABLE =
			"CREATE TABLE " + CATALOGUE_TABLE + " ("
					+ IdCatalogue + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ IDMusique + " INTEGER UNIQUE"
					+");";
	//Destruction de la table catalogue
	private static final String CATALOGUE_TABLE_DROP = "DROP TABLE IF EXISTS "
			+ CATALOGUE_TABLE +";";

	/***********************************************************************/
	/*******************************VarTemps********************************/
	/***********************************************************************/

	//Colonnes de la table variation temps
	public static final String IDVarTemps = "id_variation_temps";
	//public static final String IDMusique = "id_musique";
	public static final String MESURE_DEBUT = "mesure_debut";
	public static final String TEMPS_PAR_MESURE = "temps_par_mesure";
	public static final String TEMPO = "tempo";
	public static final String UNITE_PULSATION = "unite_pulsation";
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

	/***********************************************************************/
	/*****************************VarIntensite******************************/
	/***********************************************************************/

	//Colones de la table variation intensite
	public static final String IDIntensite = "id_variation_intensite";
	//public static final String IDMusique = "id_musique";
	//public static final String MESURE_DEBUT = "mesure_debut";
	public static final String TEMPS_DEBUT = "temps_debut";
	public static final String NB_TEMPS = "nb_temps";
	public static final String INTENTSITE = "intensite";
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

	/***********************************************************************/
	/********************************Partie*********************************/
	/***********************************************************************/

	//Colonnes de la table Partie
	public static final String IDPartie = "id_partie";
	//public static final String MESURE_DEBUT = "mesure_debut";
	public static final String Label = "label";
	//Construction de la table Partie
	private static final String CREATE_Partie_Table =
			"CREATE TABLE " + Partie_Table + " ("
			+ IDPartie + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ IDMusique + " INTEGER, "
			+ MESURE_DEBUT + " INTEGER, "
			+ Label + " TEXT"
			+ ");";
	//Destruction de la table Partie
	private static final String Partie_TABLE_DROP = "DROP TABLE IF EXISTS "
			+ Partie_Table;

	//Colonnes
	public static final String IDEvenement = "id_evenement";
	public static final String FLAG = "arg1";
	//public static final String MESURE_DEBUT = "mesure";
	public static final String ARG2 = "arg2";
	public static final String ARG3 = "arg3";
	public static final String PASSAGE_REPRISE = "passage_reprise";
	//Creation de la table
	private static final String CREATE_EVENEMENT_TABLE =
			"CREATE TABLE " + Evenement_Table + " ("
					+ IDEvenement + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ IDMusique + " INTEGER, "
					+ FLAG + " INTEGER, "
					+ MESURE_DEBUT + " INTEGER, "
					+ ARG2 + " INTEGER, "
					+ PASSAGE_REPRISE + " INTEGER, "
					+ ARG3 + " FLOAT "
					+ ");";
	//Destruction de la table
	private static final String EVENEMENT_TABLE_DROP = "DROP TABLE IF EXISTS "
			+ Evenement_Table;

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
		db.execSQL(CREATE_Partie_Table);
		db.execSQL(CREATE_EVENEMENT_TABLE);
	}

	//Supprime et recrée les tables si mises a jour de la base de données
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(MUSIQUE_TABLE_DROP);
		db.execSQL(CATALOGUE_TABLE_DROP);
		db.execSQL(VarTemps_TABLE_DROP);
		db.execSQL(VarIntensite_TABLE_DROP);
		db.execSQL(Partie_TABLE_DROP);
		db.execSQL(EVENEMENT_TABLE_DROP);
		this.onCreate(db);
	}
}
