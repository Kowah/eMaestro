package com.example.projet;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class MusiqueDAO extends DAOBase {
	
	public MusiqueDAO(Context pContext) {
		super(pContext);
	}

	private static final String TABLE_NAME = "Musique";
	private static final String KEY = "id_musique";
	private static final String NAME = "name";
	private static final String NB_MESURE = "nb_mesure";
	private static final String TEMPO = "tempo";

	public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
	//INSERT
	public void insert(Musique musique) {
		ContentValues value = new ContentValues();
		value.put(MusiqueDAO.NAME, musique.getName());
		value.put(MusiqueDAO.NB_MESURE, musique.getMesures());
		value.put(TEMPO, musique.getTempo());
		long _id = mDb.insert(TABLE_NAME, null, value);
		musique.setId((int) _id);	
	}
	
	public int delete(int id){
		String whereClause = KEY +" =?";
		String[] whereArgs = new String[] { String.valueOf(id) };
		return mDb.delete(TABLE_NAME, whereClause, whereArgs);
	}
	
	
	public void clean() {
		mDb.delete(TABLE_NAME, null, null);
	}
	//UPDATE
	public void update(Musique musique) {
		ContentValues value = new ContentValues();
		value.put(NAME, musique.getName());
		value.put(NB_MESURE, musique.getMesures());
		value.put(TEMPO, musique.getTempo());
		mDb.update(TABLE_NAME, value, NAME + "=?", new String[] {String.valueOf(musique.getName())});
	}
	//SELECT
	public Musique getMusique(String name){
		Musique res;
		Cursor c = mDb.rawQuery("Select * From "+ TABLE_NAME + " WHERE " + NAME + " = ?", new String[] {name});
		if(c.moveToFirst()){
		res = new Musique(c.getString(1), c.getInt(2), c.getInt(3), c.getInt(0));
		}
		else{
			res = new Musique("undef", -1, -1);
		}
		c.close();
		return res;
	}
	//SELECT_ALL
	public List<Musique> getAllMusique(){
		List<Musique> res = new ArrayList<>();
		Cursor c = mDb.rawQuery("Select * From " + TABLE_NAME, null);
		if(c.moveToFirst()){
			do{
				Musique musique = new Musique(c.getString(1), c.getInt(2), c.getInt(3), c.getInt(0));
				res.add(musique);
			}while(c.moveToNext());
		}
		c.close();
		return res;
	}

}
