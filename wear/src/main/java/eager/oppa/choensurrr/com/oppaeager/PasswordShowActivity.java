package eager.oppa.choensurrr.com.oppaeager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

public class PasswordShowActivity extends Activity {

    private TextView mTextView;
    private TextView firstNumber;
    private TextView secondNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_show);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();

                mTextView = (TextView) stub.findViewById(R.id.text);
                mTextView.setText(bundle.getString("bank"));

                String password = bundle.getString("password");

                String s1 = password.substring(0, 2);
                String s2 = password.substring(2, 4);

                firstNumber = (TextView) stub.findViewById(R.id.firstNumber);
                firstNumber.setText(s1);
                secondNumber = (TextView) stub.findViewById(R.id.secondNumber);
                secondNumber.setText(s2);

            }
        });

    }
}