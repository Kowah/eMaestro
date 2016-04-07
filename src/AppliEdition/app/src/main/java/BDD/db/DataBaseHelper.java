package BDD.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "emaestro";
	private static final int DATABASE_VERSION = 2;
    //LES TABLES
	public static final String MUSIQUE_TABLE = "Musique";
	public static final String CATALOGUE_TABLE = "Catalogue";
	public static final String VarTemps_Table = "VariationTemps";
	public static final String VarIntensite_Table = "VariationIntensite";
	public static final String Partie_Table = "Partie";
	public static final String Mesures_non_lues_Table = "MesuresNonLues";
	public static final String Reprises_Table = "Reprise";
	public static final String Alerte_Table = "Alerte";
	public static final String Variation_Rythme_Table = "VariationRythme";
	public static final String Suspension_Table = "Suspension";
	public static final String Armature_Table = "Armature";



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

	/***********************************************************************/
	/****************************MesuresNonLues*****************************/
	/***********************************************************************/

	//Colonnes de la table MesuresNonLues
	public static final String IDMesuresNonLues = "id_messures_non_lues";
	//public static final String MESURE_DEBUT = "mesure_debut";
	public static final String MESURE_FIN = "mesure_fin";
	public static final String PASSAGE_REPRISE = "passage_reprise";

	//Construction de la table Mesures Non lues
	private static final String CREATE_MesuresNonLues_TABLE =
			"CREATE TABLE " + Mesures_non_lues_Table + " ("
					+ IDMesuresNonLues + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ IDMusique + " INTEGER, "
					+ MESURE_DEBUT + " INTEGER, "
					+ MESURE_FIN + " INTEGER, "
					+ PASSAGE_REPRISE + " INTEGER "
					+ ");";
	//Destruction de la table Mesure Non Lues
	private static final String MesuresNonLues_TABLE_DROP = "DROP TABLE IF EXISTS "
			+ Mesures_non_lues_Table;

	/***********************************************************************/
	/*******************************Reprises********************************/
	/***********************************************************************/
	//Colonnes
	public static final String IDReprises = "id_reprise";
	//public static final String MESURE_DEBUT = "mesure_debut";
	//public static final String MESURE_FIN = "mesure_fin";
	//Creation de la table
	private static final String CREATE_Reprise_TABLE =
			"CREATE TABLE " + Reprises_Table + " ("
			+ IDReprises + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ IDMusique + " INTEGER, "
			+ MESURE_DEBUT + " INTEGER, "
			+ MESURE_FIN + " INTEGER "
			+ ");";
	//Destruction de la table
	private static final String Reprise_TABLE_DROP = "DROP TABLE IF EXISTS "
			+ Reprises_Table;

	/***********************************************************************/
	/*******************************Alertes********************************/
	/***********************************************************************/
	//Colonnes
	public static final String IDAlertes = "id_reprise";
	//public static final String MESURE_DEBUT = "mesure_debut";
	//public static final String TEMPS_DEBUT = "temps_debut";
	public static final String Couleur = "couleur";
	//public static final String PASSAGE_REPRISE = "passage_reprise";

	//Creation de la table
	private static final String CREATE_Alerte_TABLE =
			"CREATE TABLE " + Reprises_Table + " ("
					+ IDAlertes + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ IDMusique + " INTEGER, "
					+ MESURE_DEBUT + " INTEGER, "
					+ TEMPS_DEBUT + " INTEGER, "
					+ Couleur + " INTEGER, "
					+ PASSAGE_REPRISE + " INTEGER"
					+ ");";
	//Destruction de la table
	private static final String Alerte_TABLE_DROP = "DROP TABLE IF EXISTS "
			+ Alerte_Table;


	/***********************************************************************/
	/***************************VariationRythme*****************************/
	/***********************************************************************/
	//Colonnes
	public static final String IDVarRythme = "id_reprise";
	//public static final String MESURE_DEBUT = "mesure_debut";
	//public static final String TEMPS_DEBUT = "temps_debut";
	public static final String Taux_Varitation = "taux_de_variation";
	//public static final String PASSAGE_REPRISE = "passage_reprise";

	//Creation de la table
	private static final String CREATE_VariationRythme_TABLE =
			"CREATE TABLE " + Variation_Rythme_Table + " ("
					+ IDVarRythme + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ IDMusique + " INTEGER, "
					+ MESURE_DEBUT + " INTEGER, "
					+ TEMPS_DEBUT + " INTEGER, "
					+ Taux_Varitation + " INTEGER, "
					+ PASSAGE_REPRISE + " INTEGER"
					+ ");";
	//Destruction de la table
	private static final String VaritationRythme_TABLE_DROP = "DROP TABLE IF EXISTS "
			+ Variation_Rythme_Table;

	/***********************************************************************/
	/***************************Suspension*****************************/
	/***********************************************************************/
	//Colonnes
	public static final String IDSuspension = "id_suspension";
	public static final String MESURE = "mesure";
	public static final String TEMPS = "temps";
	public static final String DUREE = "duree";
	//public static final String PASSAGE_REPRISE = "passage_reprise";

	//Creation de la table
	private static final String CREATE_Suspension_TABLE =
			"CREATE TABLE " + Suspension_Table + " ("
					+ IDSuspension + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ IDMusique + " INTEGER, "
					+ MESURE + " INTEGER, "
					+ TEMPS + " INTEGER, "
					+ DUREE + " INTEGER, "
					+ PASSAGE_REPRISE + " INTEGER"
					+ ");";
	//Destruction de la table
	private static final String Suspension_TABLE_DROP = "DROP TABLE IF EXISTS "
			+ Suspension_Table;

	/***********************************************************************/
	/******************************Armature*********************************/
	/***********************************************************************/
	//Colonnes
	public static final String IDArmature = "id_suspension";
	//public static final String MESURE_DEBUT = "mesure";
	//public static final String TEMPS_DEBUT = "temps";
	public static final String Alteration = "duree";
	//public static final String PASSAGE_REPRISE = "passage_reprise";

	//Creation de la table
	private static final String CREATE_Armature_TABLE =
			"CREATE TABLE " + Armature_Table + " ("
					+ IDArmature + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ IDMusique + " INTEGER, "
					+ MESURE_DEBUT + " INTEGER, "
					+ TEMPS_DEBUT + " INTEGER, "
					+ Alteration + " INTEGER, "
					+ PASSAGE_REPRISE + " INTEGER"
					+ ");";
	//Destruction de la table
	private static final String Armature_TABLE_DROP = "DROP TABLE IF EXISTS "
			+ Armature_Table;

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
		db.execSQL(CREATE_Partie_Table);
		db.execSQL(CREATE_MesuresNonLues_TABLE);
		db.execSQL(CREATE_Reprise_TABLE);
		db.execSQL(CREATE_Alerte_TABLE);
		db.execSQL(CREATE_VariationRythme_TABLE);
		db.execSQL(CREATE_Suspension_TABLE);
		db.execSQL(CREATE_Armature_TABLE);
	}

	//Supprime et recrée les tables si mises a jour de la base de données
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(MUSIQUE_TABLE_DROP);
		db.execSQL(CATALOGUE_TABLE_DROP);
		db.execSQL(VarTemps_TABLE_DROP);
		db.execSQL(VarIntensite_TABLE_DROP);
		db.execSQL(Partie_TABLE_DROP);
		db.execSQL(MesuresNonLues_TABLE_DROP);
		db.execSQL(Reprise_TABLE_DROP);
		db.execSQL(Alerte_TABLE_DROP);
		db.execSQL(VaritationRythme_TABLE_DROP);
		db.execSQL(Suspension_TABLE_DROP);
		db.execSQL(Armature_TABLE_DROP);
		this.onCreate(db);
	}
}
