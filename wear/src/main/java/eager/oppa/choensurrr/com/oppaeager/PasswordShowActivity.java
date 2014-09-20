package eager.oppa.choensurrr.com.oppaeager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

public class PasswordShowActivity extends Activity {

    private TextView mTextView;
    private TextView firstNumber;
    private TextView secondNumber;

    private boolean mLayoutInflateCompleted = false;
    private boolean mResumed = false;
    private PowerManager mPm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_show);
        mPm = (PowerManager)getSystemService(Context.POWER_SERVICE);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mLayoutInflateCompleted = true;
                updateView();
            }
        });

    }

    private static final String TAG = "PasswordActivity";
    private void updateView() {
        Log.v(TAG, "resumed = " + mResumed + ", inflated = " + mLayoutInflateCompleted);
        if (mResumed && mLayoutInflateCompleted) {
            Intent intent = getIntent();
            Log.v(TAG, "intent = " + intent);
            if (intent == null)
                return;
            String bank = intent.getStringExtra("bank");
            String password = intent.getStringExtra("password");

            final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
            if (bank != null) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mTextView.setText(bank);
            }

            if (password != null) {
                String s1 = password.substring(0, 2);
                String s2 = password.substring(2, 4);

                firstNumber = (TextView) stub.findViewById(R.id.firstNumber);
                firstNumber.setText(s1);
                secondNumber = (TextView) stub.findViewById(R.id.secondNumber);
                secondNumber.setText(s2);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mResumed = true;
        updateView();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 15 * 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mResumed = false;
    }
}