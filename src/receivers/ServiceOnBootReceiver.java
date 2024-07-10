package receivers;

import service.CallService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import components.Preferences;

public class ServiceOnBootReceiver extends BroadcastReceiver {

	private Preferences preferences;

	@Override
	public void onReceive(Context context, Intent intent) {

		// run service at boot time as requested by user
		preferences = new Preferences(context);
		if (preferences.isRunAtStartup()) {
			Intent callServiceIntent = new Intent(context, CallService.class);
			context.startService(callServiceIntent);
		}
	}

}
