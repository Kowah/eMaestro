package BDD.db;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class EvenementDBDAO {

	protected SQLiteDatabase database;
	private BDD.db.DataBaseHelper dbHelper;
	private Context mContext;

	public EvenementDBDAO(Context context) {
		this.mContext = context;
		dbHelper = new DataBaseHelper(mContext);
				//com.example.guillaume.debug.db.DataBaseHelper.getHelper(mContext);
		//open();

	}

	public void open() throws SQLException {
		//if(dbHelper == null)
		//	dbHelper = com.example.guillaume.debug.db.DataBaseHelper.getHelper(mContext);
		database = dbHelper.getWritableDatabase();
	}

	/*public void close() {
		dbHelper.close();
		database = null;
	}*/

}
