package com.example.projet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public abstract class DAOBase {
	  // Nous sommes à la première version de la base
	  // Si je décide de la mettre à jour, il faudra changer cet attribut
	  protected final static int VERSION = 5;
	  // Le nom du fichier qui représente ma base
	  protected final static String NOM = "eMaestro.db";
	    
	  protected SQLiteDatabase mDb = null;
	  protected DataBaseHandler mHandler = null;
	    
	  public DAOBase(Context pContext) {
	    this.mHandler = new DataBaseHandler(pContext, NOM, null, VERSION);
	  }
	    
	  public SQLiteDatabase open() {
	    // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
	    mDb = mHandler.getWritableDatabase();
	    return mDb;
	  }
	    
	  public void close() {
	    mDb.close();
	  }
	    
	  public SQLiteDatabase getDb() {
	    return mDb;
	  }
}


