package BDD.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import BDD.to.Musique;
import BDD.to.VariationTemps;

import java.util.ArrayList;


/**
 * Created by guillaume on 01/03/16.
 */
public class VariationTempsDAO extends DataBaseManager {

    private static final String WHERE_ID_EQUALS = DataBaseHelper.IDVarTemps
            + " =?";

    public VariationTempsDAO(Context context) {
        super(context);
    }

    public long save(VariationTemps varTemps){
        ContentValues values = new ContentValues(); 
        values.put(DataBaseHelper.IDMusique, varTemps.getIDmusique());
        values.put(DataBaseHelper.MESURE_DEBUT, varTemps.getMesure_debut());
        values.put(DataBaseHelper.TEMPS_PAR_MESURE, varTemps.getTemps_par_mesure());
        values.put(DataBaseHelper.TEMPO, varTemps.getTempo());

        return database.insert(DataBaseHelper.VarTemps_Table, null, values);
    }

    public long update(VariationTemps varTemps){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.IDMusique, varTemps.getIDmusique());
        values.put(DataBaseHelper.MESURE_DEBUT, varTemps.getMesure_debut());
        values.put(DataBaseHelper.TEMPS_PAR_MESURE, varTemps.getTemps_par_mesure());
        values.put(DataBaseHelper.TEMPO, varTemps.getTempo());
        long result = database.update(DataBaseHelper.VarTemps_Table, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(varTemps.getIdVarTemps()) });
        Log.d("Update Result:", "=" + result);
        return result;
    }

    public int delete(VariationTemps varTemps) {
        return database.delete(DataBaseHelper.VarTemps_Table,
                WHERE_ID_EQUALS, new String[] { varTemps.getIdVarTemps() + "" });
    }

    public int deleteMusique(Musique musique) {
        return database.delete(DataBaseHelper.VarTemps_Table,
                WHERE_ID_EQUALS, new String[] { musique.getId() + "" });
    }

    public ArrayList<VariationTemps> getVariationsTemps(Musique musique) {
        ArrayList<VariationTemps> variationsT = new ArrayList<>();
        final String query = "SELECT * FROM "
                + DataBaseHelper.VarTemps_Table
                + " WHERE " + DataBaseHelper.IDMusique +"= ?;";


        Log.d("query", query);
        if(musique != null) {
            Cursor cursor = database.rawQuery(query, new String[]{Integer.toString(musique.getId())});
            while (cursor.moveToNext()) {
                VariationTemps varTemps = new VariationTemps();
                varTemps.setIdVarTemps(cursor.getInt(0));
                varTemps.setMusique(cursor.getInt(1));
                varTemps.setMesure_debut(cursor.getInt(2));
                varTemps.setTemps_par_mesure(cursor.getInt(3));
                varTemps.setTempo(cursor.getInt(4));


                variationsT.add(varTemps);
            }
        }
        return variationsT;
    }
}
