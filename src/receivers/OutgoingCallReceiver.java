/*
 * Quick test outgoing call:
 * adb shell am start -a android.intent.action.CALL tel:1112223333
 */
package receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OutgoingCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String numero = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		Log.d("BAZURA", numero);
//		Toast.makeText(context, 
//				"estas llamando a: " + numero, 
//				Toast.LENGTH_LONG).show();

	}

}
