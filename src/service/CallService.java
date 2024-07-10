package service;

import activity.MainActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import components.CallStateListener;

public class CallService extends Service {

	private final int NOTIF_ID = 1;
	public static boolean serviceRunning;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int res = super.onStartCommand(intent, flags, startId);

		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		CallStateListener listener = new CallStateListener(this);
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		Notification notification = buildNotify();
		startForeground(NOTIF_ID, notification);
		serviceRunning = true;

		return res;
	}

	private Notification buildNotify() {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).
				setTicker("Call Blocker Running ...");
		
		Intent openMainActivity = new Intent(this, MainActivity.class);
		PendingIntent pendingMainActivity = PendingIntent.getActivity(this, 0, openMainActivity, 0);
		mBuilder.setContentIntent(pendingMainActivity);

		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = mBuilder.build();
		mNotificationManager.notify(NOTIF_ID, notification);

		return notification;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		serviceRunning = false;
	}
}
