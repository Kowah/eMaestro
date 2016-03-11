package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Guillaume on 08/03/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS Variation_temps");
        db.execSQL("DROP TABLE IF EXISTS Musique");

        db.execSQL("CREATE TABLE Variation_temps(id_variation_temps int, id_musique int, mesure_debut int, temps_par_mesure int, tempo int)");
        db.execSQL("CREATE TABLE Musique(id_musique int, name text, nb_mesures int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
