package eager.oppa.choensurrr.com.oppaeager;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by ganadist on 14. 9. 20.
 */
public class CardApplication extends Application {
    private static final String TAG = "CardApp";

    private GoogleApiClient mGoogleAppiClient;

    static final int BANK_UNKNOWN = -1;
    static final int BANK_SHB = 0;
    static final int BANK_SCB = 1;
    static final int BANK_CITI = 2;

    private int mBank = BANK_UNKNOWN;

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

    void setBank(int bank) {
        mBank = bank;
        Log.d(TAG, "update band id :" + bank);
    }

    int getBank() {
        return mBank;
    }


    void sendMessage(String bank, String number) {
        final GoogleApiClient client = getClient();

        final String msg = bank + "/" + number;

        if (client.isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NodeApi.GetConnectedNodesResult nodes =
                            Wearable.NodeApi.getConnectedNodes(client).await();
                    for (Node node : nodes.getNodes()) {
                        MessageApi.SendMessageResult result =
                                Wearable.MessageApi.sendMessage(client, node.getId(),
                                        "bank/securecode",
                                        msg.getBytes()).await();
                        if(!result.getStatus().isSuccess()){
                            Log.e(TAG, "error");
                        } else {
                            Log.i(TAG, "success!! sent to: " + node.getDisplayName());
                        }
                    }
                }
            }).start();
        } else {
            Log.i(TAG, "google api client is not connected");
        }
    }
}
