package eager.oppa.choensurrr.com.oppaeager;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by ganadist on 14. 9. 20.
 */
public class SecureTextView extends TextView {
    static final String UNKNOWN_VALUE = "X";
    static final String ENTERED_VALUE = "V";
    private Handler mHandler = new Handler();
    private String mValue = "";
    public SecureTextView(Context context) {
        this(context, null, 0);
    }

    public SecureTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SecureTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setText(UNKNOWN_VALUE);
    }

    private void updateSecureText() {
        mValue = (String) getText();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setText(ENTERED_VALUE);
            }
        }, 500);
    }
    public void setTextDelayed(int resId) {
        setText(resId);
        updateSecureText();
    }

    public void setTextDelayed(String text) {
        setText(text);
        updateSecureText();
    }

    public void clear() {
        setText(UNKNOWN_VALUE);
        mValue = "";
    }

    public int getValue() {
        return Integer.valueOf(mValue);
    }
}
