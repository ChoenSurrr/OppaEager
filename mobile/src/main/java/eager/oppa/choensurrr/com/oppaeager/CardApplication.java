package eager.oppa.choensurrr.com.oppaeager;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by ganadist on 14. 9. 20.
 */
public class CardApplication extends Application {
    private static final String TAG = "CardApp";

    private GoogleApiClient mGoogleAppiClient;

    private void GmsInit() {
        mGoogleAppiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG, "onConnected: " + connectionHint);
                        // Now you can use the data layer API
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "onConnectionFailed: " + result);
                    }
                })
                .addApi(Wearable.API)
                .build();
        mGoogleAppiClient.connect();
    }

    GoogleApiClient getClient() {
        return mGoogleAppiClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent startService = new Intent(this, MonitorService.class);
        startService(startService);
        GmsInit();
    }
}
