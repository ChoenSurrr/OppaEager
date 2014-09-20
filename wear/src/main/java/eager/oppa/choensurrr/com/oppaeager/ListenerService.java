package eager.oppa.choensurrr.com.oppaeager;

import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by blueguy on 14. 9. 20.
 */
public class ListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.i("test", "onMessageReceived()");
        if(messageEvent.getPath().equals("bank/securecode")) {
            final String message = new String(messageEvent.getData());
            int pos = message.indexOf('/');

            String bank = message.substring(0, pos);
            String password = message.substring(pos + 1);
            Intent intent = new Intent(this, PasswordShowActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("bank", bank);
            intent.putExtra("password", password);
            startActivity(intent);

        } else {
            super.onMessageReceived(messageEvent);
        }
    }
}