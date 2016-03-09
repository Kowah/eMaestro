package BDD.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import BDD.to.Musique;
import BDD.to.VariationIntensite;
import java.util.ArrayList;



/**
 * Created by guillaume on 01/03/16.
 */
public class VariationIntensiteDAO extends EvenementDBDAO {

    private static final String WHERE_ID_EQUALS = DataBaseHelper.ID_COLUMN
            + " =?";

    public VariationIntensiteDAO(Context context) {
        super(context);
    }

    public long save(VariationIntensite varIntensite){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, varIntensite.getIdMusique());
        values.put(DataBaseHelper.TEMPS_DEBUT, varIntensite.getTempsDebut());
        values.put(DataBaseHelper.MESURE_DEBUT, varIntensite.getMesureDebut());
        values.put(DataBaseHelper.NB_TEMPS, varIntensite.getnb_temps());
        values.put(DataBaseHelper.INTENTSITE, varIntensite.getIntensite());

        return database.insert(DataBaseHelper.VarIntensite_Table, null, values);
    }

    public long update(VariationIntensite varIntensite){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, varIntensite.getIdMusique());
        values.put(DataBaseHelper.TEMPS_DEBUT, varIntensite.getTempsDebut());
        values.put(DataBaseHelper.MESURE_DEBUT, varIntensite.getMesureDebut());
        values.put(DataBaseHelper.NB_TEMPS, varIntensite.getnb_temps());
        values.put(DataBaseHelper.INTENTSITE, varIntensite.getIntensite());
        long result = database.update(DataBaseHelper.VarIntensite_Table, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(varIntensite.getIdVarIntensite()) });
        Log.d("Update Result:", "=" + result);
        return result;
    }
    //Supprime une variation d'intensite dans la table
    public int delete(VariationIntensite varIntensite) {
        return database.delete(DataBaseHelper.VarIntensite_Table,
                WHERE_ID_EQUALS, new String[] { varIntensite.getIdVarIntensite() + "" });
    }
    //Supprime toutes les variations d'intensite liées a une musique
    public int deleteMusique(Musique musique) {
        return database.delete(DataBaseHelper.VarIntensite_Table,
                WHERE_ID_EQUALS, new String[] { musique.getId() + "" });
    }

    public ArrayList<VariationIntensite> getVariationsTemps(Musique musique) {
        ArrayList<VariationIntensite> variationsI = new ArrayList<>();
        String[] musiqueID = new String[]{String.valueOf(musique.getId())};
        final String query = "SELECT * FROM "
                + DataBaseHelper.VarIntensite_Table
                + " WHERE " + DataBaseHelper.IDMusique +"="+ musiqueID + ";";


        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()) {
            VariationIntensite varIntensite = new VariationIntensite();
            varIntensite.setIdVarIntensite(cursor.getInt(0));
            varIntensite.setIdMusique(cursor.getInt(1));
            varIntensite.setMesureDebut(cursor.getInt(2));
            varIntensite.setTempsDebut(cursor.getInt(3));
            varIntensite.setNb_temps(cursor.getInt(4));
            varIntensite.setIntensite(cursor.getInt(5));


            variationsI.add(varIntensite);
        }
        return variationsI;
    }
}
