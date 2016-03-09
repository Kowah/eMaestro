package BDD.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManager {

	protected SQLiteDatabase database;
	private BDD.db.DataBaseHelper dbHelper;
	private Context mContext;

	public DataBaseManager(Context context) {
		this.mContext = context;
		dbHelper = new BDD.db.DataBaseHelper(mContext);
				//com.example.guillaume.debug.db.DataBaseHelper.getHelper(mContext);
		//open();

	}

	public void open() throws SQLException {
		//if(dbHelper == null)
		//	dbHelper = com.example.guillaume.debug.db.DataBaseHelper.getHelper(mContext);
		database = dbHelper.getWritableDatabase();
	}

	public void clean(){
		database.delete(DataBaseHelper.MUSIQUE_TABLE,null,null);
		database.delete(DataBaseHelper.VarIntensite_Table,null,null);
		database.delete(DataBaseHelper.VarTemps_Table,null,null);
	}
	public void close() {
		dbHelper.close();
		database = null;
	}

}
