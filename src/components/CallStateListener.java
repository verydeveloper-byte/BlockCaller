/*
 * Clase listener que detecta llamadas entrantes usando la
 * clase base PhoneStateListener.
 * En esta clase sobreescribimos los metodos para los que deseamos
 * recibir informacion. Luego pasamos un objeto de esta clase
 * a TelephonyManager.listen() junto con los eventos que queremos
 * manejar.
 * 
 */
package components;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import dbo.DBOpenHelper;
import dbo.TelefonoDao;

public class CallStateListener extends PhoneStateListener {

	Context context;
	DBOpenHelper dbHelper;
	SQLiteDatabase db;
	
	public CallStateListener(Context context) {
		this.context = context;
	}

	/*
	 * callback que se ejecuta cuando hay un cambio
	 * en el estado de la llamada en el movil.
	 * CALL_STATE_IDLE -> normal, sin llamada.
	 * CALL_STATE_RINGING -> hay una llamada, el tlf suena
	 * CALL_STATE_OFFHOOK -> llamada saliente.
	 */
	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);

		switch (state) {
		case TelephonyManager.CALL_STATE_RINGING:
			ManageCall manageCall = new ManageCall(context);
			manageCall.notifyIncomingNumber(incomingNumber);
			manageCall.endCallIfBlackListed(incomingNumber);
			break;
		}
	}

}
