package components;

import java.lang.reflect.Method;

import android.content.Context;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import dbo.TelefonoDao;


public class ManageCall {

	private Context context;
	private ITelephony telephonyService;
	private TelefonoDao tlfDao;

	public ManageCall(Context context) {
		this.context = context;
		try {
			TelephonyManager telephonymanager = (TelephonyManager)
					this.context.getSystemService(context.TELEPHONY_SERVICE);

			/*
			 * Java reflection to gain access to the internal 
			 * ITelephony interface and be able to access to
			 * the endCall() method. 
			 */
			Class telephonyManagerClass = Class.forName(telephonymanager.getClass().getName());
			Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
			getITelephonyMethod.setAccessible(true);
			telephonyService = (ITelephony) getITelephonyMethod.invoke(telephonymanager);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void notifyIncomingNumber(String number) {
		Toast.makeText(context, 
				"llamada entrante " + number, 
				Toast.LENGTH_SHORT)
				.show();
	}

	public void endCallIfBlackListed(String phoneNumber) {
		tlfDao = new TelefonoDao(context);

		/*
		 * Colgar si el tlf esta en la lista negra.
		 */
		if (tlfDao.existsNumber(phoneNumber)) {
			if (telephonyService != null) {
				try {
					telephonyService.endCall();
					Toast.makeText(context, 
							"Llamada entrante bloqueada " + "(" + phoneNumber +")", 
							Toast.LENGTH_LONG)
							.show();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
