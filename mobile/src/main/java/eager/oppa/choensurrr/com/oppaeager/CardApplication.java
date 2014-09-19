package eager.oppa.choensurrr.com.oppaeager;

import android.app.Application;
import android.content.Intent;

/**
 * Created by ganadist on 14. 9. 20.
 */
public class CardApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Intent startService = new Intent(this, MonitorService.class);
        startService(startService);
    }
}
