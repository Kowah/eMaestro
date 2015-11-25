package com.example.projet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PartitionDAO extends DAOBase {
	
	public PartitionDAO(Context pContext) {
		super(pContext);
		// TODO Auto-generated constructor stub
	}

	private static final String TABLE_NAME = "eMaestro";
	private static final String KEY = "_id";
	private static final String NAME = "name";
	private static final String MESURE = "mesure";
	private static final String RYTHME = "rythme";

	public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
	  
	public void insert(Partition partition) {
		ContentValues value = new ContentValues();
		value.put(PartitionDAO.NAME, partition.getName());
		value.put(PartitionDAO.MESURE, partition.getMesures());
		value.put(PartitionDAO.RYTHME, partition.getRythme());
		mDb.insert(TABLE_NAME, null, value);
	}
	
	public void clean() {
		mDb.delete(TABLE_NAME, null, null);
	}
	
	public void update(Partition partition) {
		ContentValues value = new ContentValues();
		value.put(NAME, partition.getName());
		value.put(MESURE, partition.getMesures());
		value.put(RYTHME, partition.getRythme());
		mDb.update(TABLE_NAME, value, NAME + "=?", new String[] {String.valueOf(partition.getName())});
	}
	
	public Partition getPartiton(String name){
		Partition res;
		Cursor c = mDb.rawQuery("Select * From "+ TABLE_NAME + " WHERE " + NAME + " = ?", new String[] {name});
		if(c.moveToFirst()){
		res = new Partition(c.getString(1), c.getInt(2), c.getInt(3));
		}
		else{
			res = new Partition("undef", -1, -1);
		}
		c.close();
		return res;
	}

}
