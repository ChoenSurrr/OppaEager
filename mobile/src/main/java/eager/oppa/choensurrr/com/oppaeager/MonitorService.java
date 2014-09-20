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

import java.util.List;


/**
 * Created by ganadist on 14. 9. 19.
 * Last modified by Ian.
 *
 * Monitors running Internet backing application and
 *  detects the activity inputting security card number
 */
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

        if (name.getPackageName().equals(getPackageName())) {
            return;
        }
        if (mTopActivity.equals(name)) {
            return;
        }
        int bankId = CardApplication.BANK_UNKNOWN;
        Log.v("Monitor", "topActivity = " + name);
        if (name.equals(SHB)) {
            bankId = CardApplication.BANK_SHB;
            Log.v("Monitor", "shinhan bank detected");
            this.sendNotification(bankId);
        } else if (name.equals(CITI)) {
            bankId = CardApplication.BANK_CITI;
            Log.v("Monitor", "citi bank detected");
            this.sendNotification(bankId);
        } else if (name.equals(SCB)) {
            bankId = CardApplication.BANK_SCB;
            Log.v("Monitor", "standard chartered bank detected");
            this.sendNotification(bankId);
        } else {
            Log.v("Monitor", "bank unknown");
        }
        mTopActivity = name;

        ((CardApplication)getApplication()).setBank(bankId);
    }

    /**
     * sendNotification: send notifications to mobile & wear
     *
     * @param bankId    defined in CardApplication class
     */
    private void sendNotification(int bankId) {
        int notificationId = 001;
        // Build intent for notification content
        Intent inputIntent = new Intent(this, InputActivity.class);
        //inputIntent.putExtra(EXTRA_EVENT_ID, eventId);
        PendingIntent inputPendingIntent =
                PendingIntent.getActivity(this, 0, inputIntent, 0);

        // get contentAction, contentTitle & contentText
        int contentAction = -1;
        int contentTitle = -1;
        int contentText = -1;
        contentAction = R.string.monitor_content_action;
        switch (bankId) {
            case CardApplication.BANK_SHB:
                contentTitle = R.string.monitor_shb_content_title;
                contentText = R.string.monitor_shb_content_text;
                break;
            case CardApplication.BANK_CITI:
                contentTitle = R.string.monitor_citi_content_title;
                contentText = R.string.monitor_citi_content_text;
                break;
            case CardApplication.BANK_SCB:
                contentTitle = R.string.monitor_scb_content_title;
                contentText = R.string.monitor_scb_content_text;
                break;
            case CardApplication.BANK_UNKNOWN:
            default:
                contentTitle = R.string.monitor_scb_content_title;
                contentText = R.string.monitor_scb_content_text;
                break;
        }
        // Create the action
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.oppa_icon,
                        getString(contentAction), inputPendingIntent)
                        .build();

        // notification to mobile & wear
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.oppa_icon)
                        .setContentTitle(getString(contentTitle))
                        .setContentText(getString(contentText))
                        .setContentIntent(inputPendingIntent)
                        .addAction(R.drawable.oppa_icon, getString(contentAction), inputPendingIntent);

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
