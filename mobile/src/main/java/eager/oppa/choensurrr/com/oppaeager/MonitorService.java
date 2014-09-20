package eager.oppa.choensurrr.com.oppaeager;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;

import java.util.List;


public class MonitorService extends Service {

    private static final int MSG_CHECK_ACTIVITY = 0;
    private ActivityManager mAm;
    private ComponentName mTopActivity;

    private static final ComponentName SHB = new ComponentName("com.shinhan.sbanking",
            "com.shinhan.bank.sbank.activity.transfer.TransferConfirmActivity");

    private static final ComponentName SCB = new ComponentName("com.sc.danb.scbankapp",
            "com.sc.danb.scbankapp.native_activity.MainViewActivity");

    private static final ComponentName CITI = new ComponentName("com.kftc.citismb",
            "com.kftc.activity.common.SecurityCardActivity");

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MSG_CHECK_ACTIVITY:
                    checkTopActivity();
                    sendEmptyMessageDelayed(MSG_CHECK_ACTIVITY, 1 * 1000);
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        mAm = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        mHandler.sendEmptyMessage(MSG_CHECK_ACTIVITY);
        // set dummy component name
        mTopActivity = new ComponentName("", "");
    }

    private void checkTopActivity() {
        List<ActivityManager.RunningTaskInfo> info = mAm.getRunningTasks(1);
        ComponentName name = info.get(0).topActivity;

        if (mTopActivity.equals(name)) {
            return;
        }
        Log.v("Monitor", "topActivity = " + name);
        if (name.equals(SHB)) {
            Log.v("Monitor", "shinhan bank detected");
        } else if (name.equals(CITI)) {
            Log.v("Monitor", "citi bank detected");
            this.sendNotification(CITI);
        } else if (name.equals(SCB)) {
            Log.v("Monitor", "standard chartered bank detected");
        }
        mTopActivity = name;
    }

    private void sendNotification(ComponentName componentName) {
        int notificationId = 001;
        // Build intent for notification content
        Intent inputIntent = new Intent(this, InputActivity.class);
        //inputIntent.putExtra(EXTRA_EVENT_ID, eventId);
        PendingIntent inputPendingIntent =
                PendingIntent.getActivity(this, 0, inputIntent, 0);

        // Create the action
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.oppa_icon,
                        getString(R.string.monitor_citi_content_action), inputPendingIntent)
                        .build();

        // notification to mobile & wear
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.oppa_icon)
                        .setContentTitle(getString(R.string.monitor_citi_content_title))
                        .setContentText(getString(R.string.monitor_citi_content_text))
                        .setContentIntent(inputPendingIntent)
                        .addAction(R.drawable.oppa_icon, getString(R.string.monitor_citi_content_action), inputPendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    @Override
    public void onDestroy() {
        mHandler.removeMessages(MSG_CHECK_ACTIVITY);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
