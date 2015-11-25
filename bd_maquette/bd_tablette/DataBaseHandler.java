package com.example.projet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

	private static final String TABLE_NAME = "eMaestro";
	private static final String KEY = "_id";
	private static final String NAME = "name";
	private static final String MESURE = "mesure";
	private static final String RYTHME = "rythme";

	private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" 
			+ KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NAME + " TEXT, " 
			+ MESURE + " INTEGER, "
			+ RYTHME + " INTEGER "
			+ ");";

	public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
	
	public DataBaseHandler(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}
		
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	  db.execSQL(TABLE_DROP);
	  onCreate(db);
	}
}
