package com.example.boris.emaestro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
//BDD de guillaume
public class MusiqueDAO extends DAOBase {
	
	public MusiqueDAO(Context pContext) {
		super(pContext);
	}

	private static final String TABLE_NAME = "Musique";
	private static final String KEY = "id_musique";
	private static final String NAME = "name";
	private static final String NB_MESURE = "nb_mesure";

	public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
	  
	public void insert(Musique musique) {
		ContentValues value = new ContentValues();
		value.put(MusiqueDAO.NAME, musique.getName());
		value.put(MusiqueDAO.NB_MESURE, musique.getMesures());
		mDb.insert(TABLE_NAME, null, value);
	}
	
	public void clean() {
		mDb.delete(TABLE_NAME, null, null);
	}
	
	public void update(Musique musique) {
		ContentValues value = new ContentValues();
		value.put(NAME, musique.getName());
		value.put(NB_MESURE, musique.getMesures());
		mDb.update(TABLE_NAME, value, NAME + "=?", new String[] {String.valueOf(musique.getName())});
	}
	
	public Musique getMusique(String name){
		Musique res;
		Cursor c = mDb.rawQuery("Select * From "+ TABLE_NAME + " WHERE " + NAME + " = ?", new String[] {name});
		if(c.moveToFirst()){
		res = new Musique(c.getString(1), c.getInt(2));
		}
		else{
			res = new Musique("undef", -1);
		}
		c.close();
		return res;
	}

}
