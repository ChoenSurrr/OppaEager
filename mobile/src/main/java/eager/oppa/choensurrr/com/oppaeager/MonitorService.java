package eager.oppa.choensurrr.com.oppaeager;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.util.List;


public class MonitorService extends Service {

    private static final int MSG_CHECK_ACTIVITY = 0;
    private ActivityManager mAm;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MSG_CHECK_ACTIVITY:
                    checkActivity();
                    sendEmptyMessageDelayed(MSG_CHECK_ACTIVITY, 5 * 1000);
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        mAm = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        mHandler.sendEmptyMessage(MSG_CHECK_ACTIVITY);
    }

    private void checkActivity() {
        List<ActivityManager.RunningTaskInfo> info = mAm.getRunningTasks(1);
        ComponentName name = info.get(0).topActivity;

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
