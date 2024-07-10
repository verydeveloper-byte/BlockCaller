package dbo;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class TelefonoDao {
	DBOpenHelper helper;
	SQLiteDatabase db;
	private final String DB_NAME = "telefonos.sqlite";


	public TelefonoDao(Context context) {
		helper = new DBOpenHelper(context, DB_NAME, 1);
	}

	public boolean insertarNumTelefono(String tlf) {
		ContentValues contentTlf;
		long res = -1;

		try {
			db = helper.getWritableDatabase();
			contentTlf = new ContentValues();
			contentTlf.put("telefono", tlf);
			res = db.insert(DBOpenHelper.TLF_TABLE_NAME, null, contentTlf);
		} catch (SQLiteException e) {
			Log.e("Call Blocker", "DB error ...");
		}
		return (res != -1)? true : false;
	}

	public boolean eliminarNumTelefono(String tlf) {
		int res = 0;
		try {
			db = helper.getWritableDatabase();
			res = db.delete(DBOpenHelper.TLF_TABLE_NAME, "telefono = '" + tlf + "'", null);
		} catch (SQLiteException e) {
			Log.e("Call Blocker", "DB error ...");
		}
		return (res != 0)? true : false;
	}

	public ArrayList<String> listarNumTelefonos() {
		ArrayList<String> listaTelefonos = new ArrayList<String>();
		Cursor c;
		try {
			db = helper.getReadableDatabase();
			c = db.query(DBOpenHelper.TLF_TABLE_NAME, // tabla
					new String[] { DBOpenHelper.TLF_COL_NAME }, // columna
					null,	// where
					null, // where args
					null,	// group by
					null, // having
					null);	// order by
			if (c.moveToFirst()) {
				do {
					String tlf = c.getString(0);
					listaTelefonos.add(tlf);
				} while (c.moveToNext());
			}
		} catch (SQLiteException e) {
			Log.e("CALLBLOCKER", "DB error ...");
		}
		return listaTelefonos;
	}

	public void eliminarTodo() {
		try {
			db = helper.getWritableDatabase();
			db.delete(DBOpenHelper.TLF_TABLE_NAME, "1", null);
		} catch (SQLiteException e) {
			Log.e("CALLBLOCKER", "DB error ...");
		}
	}

	public boolean existsNumber(String phoneNumber) {
		Cursor c;
		db = helper.getReadableDatabase();
		
		c = db.query(DBOpenHelper.TLF_TABLE_NAME, 
				new String[] { DBOpenHelper.TLF_COL_NAME },
				DBOpenHelper.TLF_COL_NAME + "=" + phoneNumber, 
				null, 
				null, 
				null, 
				null);
		
		return c.moveToFirst();
	}
}
