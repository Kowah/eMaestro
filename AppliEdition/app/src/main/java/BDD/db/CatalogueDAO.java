package BDD.db;

import android.app.admin.DeviceAdminInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import BDD.to.Musique;
import BDD.to.VariationIntensite;
import BDD.to.VariationTemps;


public class CatalogueDAO extends DataBaseManager {

    private static final String WHERE_ID_EQUALS = DataBaseHelper.KEY_Musique
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
            values.put(DataBaseHelper.NAME_Musique, musique.getId());

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
                values.put(DataBaseHelper.NAME_Musique, m.getId());

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
            values.put(DataBaseHelper.KEY_Musique, id);

            return database.insert(DataBaseHelper.CATALOGUE_TABLE, null, values);
    }

    public long update(Musique Musique) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NAME_Musique, Musique.getName());
        values.put(DataBaseHelper.NB_MESURE, Musique.getNb_mesure());
        //values.put(DataBaseHelper.NB_PULSATION, Musique.getNb_pulsation());
        //values.put(DataBaseHelper.UNITE_PULSATION, Musique.getUnite_pulsation());
        //values.put(DataBaseHelper.NB_TEMPS_MESURE, Musique.getNb_temps_mesure());
        long result = database.update(DataBaseHelper.MUSIQUE_TABLE, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(Musique.getId()) });
        Log.d("Update Result:", "=" + result);
        return result;
    }

    public int deleteMusic(Musique musique) {
        return database.delete(DataBaseHelper.CATALOGUE_TABLE,
                WHERE_ID_EQUALS, new String[] { musique.getId() + "" });
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
            //musique.setNb_pulsation(cursor.getInt(3));
            //musique.setUnite_pulsation(cursor.getInt(4));
            //musique.setNb_temps_mesure(cursor.getInt(5));
        }
        return musique;
    }

    //Retourne la liste des musiques du catalogue
    //FIXME: a tester
    public ArrayList<Musique> getMusiques() {
        ArrayList<Musique> musiques = new ArrayList<Musique>();
        String query = "SELECT "+ DataBaseHelper.KEY_Musique +" FROM "
                + DataBaseHelper.CATALOGUE_TABLE;

        String queryMusique = "Select * FROM "+DataBaseHelper.MUSIQUE_TABLE+
                " WHERE " + WHERE_ID_EQUALS;
        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, null);
        Cursor curMusique;

        while (cursor.moveToNext()) {
            curMusique = database.rawQuery(queryMusique, new String[]{Integer.toString(cursor.getInt(0))});
            curMusique.moveToFirst();
            Musique musique = new Musique();
            musique.setId(curMusique.getInt(0));
            musique.setName(curMusique.getString(1));
            musique.setNb_mesure(curMusique.getInt(2));
            //musique.setNb_pulsation(curMusique.getInt(3));
            //musique.setUnite_pulsation(curMusique.getInt(4));
            //musique.setNb_temps_mesure(curMusique.getInt(5));

            musiques.add(musique);
        }
        return musiques;
    }

    public int synchronizer()  {
        List<Musique> musiques = this.getMusiques();
        List<VariationTemps> variationTemps;
        List<VariationIntensite> variationIntensites;
        Thread t = new Thread(new Synchronize(musiques, context));
        t.start();
        /*
        String queryMusic = "Insert into "+ DataBaseHelper.MUSIQUE_TABLE  + " ("
                +DataBaseHelper.NAME_Musique+","
                +DataBaseHelper.NB_MESURE+","
                +DataBaseHelper.NB_PULSATION+","
                +DataBaseHelper.UNITE_PULSATION+","
                +DataBaseHelper.NB_TEMPS_MESURE+")"
                +" values(?,?,?,?,?)";
        Connection co = ConnectonJDBC.getConnection();
        if(co == null){
            System.out.println("Hello World");
            return -1;
        }
        for(Musique m:musiques) {
            PreparedStatement st = co.prepareStatement(queryMusic);
            st.setString(1, m.getName());
            st.setInt(2,m.getNb_mesure());
            st.setInt(3,m.getNb_pulsation());
            st.setInt(4,m.getUnite_pulsation());
            st.setInt(5,m.getNb_temps_mesure());
            st.execute();
        }
        */
         return 0;
    }
}
