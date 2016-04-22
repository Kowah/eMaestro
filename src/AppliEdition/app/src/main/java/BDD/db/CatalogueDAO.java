package BDD.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import BDD.to.Musique;


public class CatalogueDAO extends DataBaseManager {

    private static final String WHERE_ID_EQUALS = DataBaseHelper.IDMusique
            + " =?";
    Context context;

    public CatalogueDAO(Context context) {
        super(context);
        this.context = context;
    }
    public void clean(){
        database.delete(DataBaseHelper.CATALOGUE_TABLE, null, null);
    }

    public long save(Musique musique) {
        long res;
        if(musique.getId() > 0) {
            ContentValues values = new ContentValues();
            values.put(DataBaseHelper.IDMusique, musique.getId());

            res = database.insert(DataBaseHelper.CATALOGUE_TABLE, null, values);
        }
        else{
            res = -1;
        }
        return res;
    }
    //Permet d'enregistrer une liste de musiques, retourne 0 si succes, -1 sinon
    public int save(List<Musique> musiques){
        for(Musique m:musiques){
            ContentValues values = new ContentValues();
            if(m.getId() > 0){
                values.put(DataBaseHelper.IDMusique, m.getId());

                if(database.insert(DataBaseHelper.CATALOGUE_TABLE, null, values) < 0){
                    System.err.println("Erreur lors de l'insertion de données dans ala table catalogue");
                    return -1;
                }
            }
            else{
                System.err.println("Erreur, musique avec un identifiant invalide a enregistrer");
                return -1;
            }
        }
        return 0;
    }

    public long save(long id) {
            ContentValues values = new ContentValues();
            values.put(DataBaseHelper.IDMusique, id);

            return database.insert(DataBaseHelper.CATALOGUE_TABLE, null, values);
    }

    public long update(Musique Musique) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NAME_Musique, Musique.getName());
        values.put(DataBaseHelper.NB_MESURE, Musique.getNb_mesure());
        long result = database.update(DataBaseHelper.MUSIQUE_TABLE, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(Musique.getId()) });
        Log.d("Update Result:", "=" + result);
        return result;
    }
    @Override
    public int delete(Musique musique) {
        return database.delete(DataBaseHelper.CATALOGUE_TABLE,
                WHERE_ID_EQUALS, new String[] { musique.getId() + "" });
    }

    public Musique getMusique(int id){
        String query = "SELECT * FROM "
                + DataBaseHelper.MUSIQUE_TABLE
                +" WHERE " + DataBaseHelper.IDMusique + "= ?";
        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, new String[] {Integer.toString(id)});
        Musique musique = new Musique();
        if(cursor.moveToFirst()) {
            musique.setId(cursor.getInt(0));
            musique.setName(cursor.getString(1));
            musique.setNb_mesure(cursor.getInt(2));
        }
        cursor.close();
        return musique;
    }

    //Retourne la liste des musiques du catalogue
    public ArrayList<Musique> getMusiques() {
        ArrayList<Musique> musiques = new ArrayList<>();
        String query = "SELECT "+ DataBaseHelper.IDMusique +" FROM "
                + DataBaseHelper.CATALOGUE_TABLE;

        String queryMusique = "Select * FROM "+DataBaseHelper.MUSIQUE_TABLE+
                " WHERE " + WHERE_ID_EQUALS;
        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, null);
        Cursor curMusique;

        while (cursor.moveToNext()) {
            curMusique = database.rawQuery(queryMusique, new String[]{Integer.toString(cursor.getInt(0))});
            if (curMusique.moveToFirst()) {
                Musique musique = new Musique();
                musique.setId(curMusique.getInt(0));
                musique.setName(curMusique.getString(1));
                musique.setNb_mesure(curMusique.getInt(2));

                musiques.add(musique);
            }
            curMusique.close();
        }
        cursor.close();

        return musiques;
    }

    public ArrayList<Integer> getIdMusiques() {
        ArrayList<Integer> ident = new ArrayList<>();
        String query = "SELECT "+ DataBaseHelper.IDMusique +" FROM "
                + DataBaseHelper.CATALOGUE_TABLE;

        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, null);

        while (cursor.moveToNext()) {
                ident.add(cursor.getInt(0));
        }
        cursor.close();
        return ident;
    }

    public int synchronizer()  {
        Synchronize s = new Synchronize(context);
        s.execute();
        return 0;
    }
}
