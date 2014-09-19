package eager.oppa.choensurrr.com.oppaeager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ganadist on 14. 9. 19.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startService = new Intent(context, MonitorService.class);
        context.startService(startService);
    }
}
