package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import emaestro.emulateur.Triple;

/**
 * Created by Guillaume on 08/03/2016.
 */
public class MaestroBDD {

    private SQLiteDatabase bdd;
    private DatabaseHelper baseSQLite;

    public MaestroBDD(Context context){
        baseSQLite = new DatabaseHelper(context,"eMaestroDB",null,1);
    }

    public void open(){
        bdd = baseSQLite.getWritableDatabase();
    }

    public void close(){
        bdd.close();
    }

    public SQLiteDatabase getBdd(){
        return bdd;
    }

    public void clearTable(String tableName){
        bdd.execSQL("DELETE FROM "+tableName);
    }

    public void insertVariationTemps(int id, int idMusique, int mesureDebut, int tempsParMesure, int tempo){
        ContentValues values = new ContentValues();
        values.put("id_variation_temps",id);
        values.put("id_musique",idMusique);
        values.put("mesure_debut",mesureDebut);
        values.put("temps_par_mesure",tempsParMesure);
        values.put("tempo",tempo);

        bdd.insert("Variation_temps", null, values);
    }

    public void insertMusique(int id, int nbMesures){
        ContentValues values = new ContentValues();
        values.put("id_musique",id);
        values.put("nb_mesures", nbMesures);

        bdd.insert("Musique",null,values);
    }

    public void deleteVariationMusique(int id){
        bdd.delete("Variation_temps", "id_musique = ?", new String[]{"" + id});
    }

    public ArrayList<Triple<Integer, Integer, Integer>> selectAllVariationMesures(int idMusique){
        ArrayList<Triple<Integer, Integer, Integer>> listeVariation = new ArrayList<>();

        String query = "SELECT mesure_debut, temps_par_mesure, tempo FROM Variation_temps WHERE id_musique = "+idMusique;
        Cursor cursor = bdd.rawQuery(query,null);

        int mesure_debut=0;
        int temps_par_mesure=0;
        int tempo=0;

        if(cursor.moveToFirst()){
            do {
                mesure_debut = Integer.parseInt(cursor.getString(0));
                temps_par_mesure = Integer.parseInt(cursor.getString(1));
                tempo = Integer.parseInt(cursor.getString(2));

                listeVariation.add(new Triple<Integer,Integer,Integer>(mesure_debut, temps_par_mesure, tempo));
            }while(cursor.moveToNext());
        }
        cursor.close();

        //On trie la liste dans l'ordre des mesures de debut
        Collections.sort(listeVariation, new Comparator<Triple<Integer, Integer, Integer>>() {
            @Override
            public int compare(Triple<Integer, Integer, Integer> lhs, Triple<Integer, Integer, Integer> rhs) {
                return lhs.getLeft().compareTo(rhs.getLeft());
            }
        });

        return listeVariation;
    }

    public int selectMesureFin(int idMusique){
        String query = "SELECT nb_mesures FROM Musique WHERE id_musique = "+idMusique;
        Cursor cursor = bdd.rawQuery(query,null);

        int mesure_fin = 0;
        if(cursor.moveToFirst()){
            mesure_fin = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();

        return mesure_fin;
    }

}
