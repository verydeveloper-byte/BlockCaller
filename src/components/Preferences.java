package components;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Preferences {

	private Context context;
	private SharedPreferences prefs;
	public final static String PREFS_FILE = "Preferencias";
	public final static String RUN_AT_STARTUP_PREF_NAME = "RunAtStartup";

	public Preferences(Context context) {
		this.context = context;
		prefs = this.context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
	}

	public boolean isRunAtStartup() {
		String run = prefs.getString(RUN_AT_STARTUP_PREF_NAME, "YES");

		if (run.equals("NO"))	return false;
		else return true;
	}

	/**
	 * Activa el servicio de BlockCaller en el inicio de Android.
	 * 
	 * @param bool activar/desactivar arranque al inicio.
	 */
	public void setRunAtStartup(boolean bool) {

		if (bool) {
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(RUN_AT_STARTUP_PREF_NAME, "YES");
			editor.commit();
			Log.d("CALLBLOCKER", prefs.getString(RUN_AT_STARTUP_PREF_NAME, "default"));
		} else {
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(RUN_AT_STARTUP_PREF_NAME, "NO");
			editor.commit();
			Log.d("CALLBLOCKER", prefs.getString(RUN_AT_STARTUP_PREF_NAME, "default"));
		}

	}
}
