package eager.oppa.choensurrr.com.oppaeager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class InputActivity extends Activity implements View.OnClickListener {

    private String SHB[] = {
            "1248", "2324", "1315", "8495", "2092",
            "9282", "4824", "8153", "8242", "0575",
            "2256", "1224", "8564", "8013", "1292",
            "4195", "2025", "4664", "1223", "2558",
            "2313", "1250", "2102", "2482", "1089",
            "4502", "8501", "2352", "0485", "2121",
    };

    private String SCB[] = {
            "1248", "2324", "1315", "8495", "2092",
            "9282", "4824", "8153", "8242", "0575",
            "2256", "1224", "8564", "8013", "1292",
            "4195", "2025", "4664", "1223", "2558",
            "2313", "1250", "2102", "2482", "1089",
            "4502", "8501", "2352", "0485", "2121",
    };

    private String CITI[] = {
            "1248", "2324", "1315", "8495", "2092",
            "9282", "4824", "8153", "8242", "0575",
            "2256", "1224", "8564", "8013", "1292",
            "4195", "2025", "4664", "1223", "2558",
            "2313", "1250", "2102", "2482", "1089",
            "4502", "8501", "2352", "0485", "2121",
    };



    private int mPosition = 0;
    private static final int MAX_NUMBER_ITEM = 4;

    private static final int [] BUTTON_IDS = {
            R.id.number_0,
            R.id.number_1,
            R.id.number_2,
            R.id.number_3,
            R.id.number_4,
            R.id.number_5,
            R.id.number_6,
            R.id.number_7,
            R.id.number_8,
            R.id.number_9,
            R.id.number_clear,
    };

    private static final String TAG = "InputActivity";
    private SecureTextView[] mInputText = new SecureTextView[MAX_NUMBER_ITEM];
    private Button[] mButtons = new Button[BUTTON_IDS.length];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        for (int i = 0; i < BUTTON_IDS.length; i++) {
            int id = BUTTON_IDS[i];
            Button b = (Button)findViewById(id);
            mButtons[i] = b;
            b.setOnClickListener(this);
        }

        ViewGroup g = (ViewGroup)findViewById(R.id.numbers);
        assert(g.getChildCount() == MAX_NUMBER_ITEM);
        for (int i = 0; i < MAX_NUMBER_ITEM; i++) {
            mInputText[i] = (SecureTextView)g.getChildAt(i);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        clear();
        updateInputEntries();
    }

    @Override
    protected void onPause() {
        clear();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clear() {
        for(SecureTextView v: mInputText) {
            v.clear();
        }
        mPosition = 0;
    }

    private void updateInputEntries() {
        SparseBooleanArray list = new SparseBooleanArray();
        int [] enableAllItems = {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
        };
        int [] enableItems ;

        if (mPosition == 0 || mPosition == 2) {
            int [] items = {
                    0, 1, 2, 3, 10 //clear
            };
            enableItems = items;

        } else {
            int v = mInputText[mPosition - 1].getValue();
            if (v == 3) {
                int[] items = {
                        0, 10//clear
                };
                enableItems = items;
            } else {
                enableItems = enableAllItems;
            }
        }

        for (int i: enableItems) {
            list.append(i, true);
        }

        for (int i: enableAllItems) {
            mButtons[i].setEnabled(list.get(i, false));
        }
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        int number = -1;
        switch(id) {
            case R.id.number_0:
                number = 0;
                break;
            case R.id.number_1:
                number = 1;
                break;
            case R.id.number_2:
                number = 2;
                break;
            case R.id.number_3:
                number = 3;
                break;
            case R.id.number_4:
                number = 4;
                break;
            case R.id.number_5:
                number = 5;
                break;
            case R.id.number_6:
                number = 6;
                break;
            case R.id.number_7:
                number = 7;
                break;
            case R.id.number_8:
                number = 8;
                break;
            case R.id.number_9:
                number = 9;
                break;
            case R.id.number_clear:
                clear();
                return;
            default:
                return;
        }
        if (mPosition >=  MAX_NUMBER_ITEM) {
            return;
        }


        mInputText[mPosition].setTextDelayed(Integer.toString(number));

        mPosition += 1;


        if (mPosition == MAX_NUMBER_ITEM) {
            int first = mInputText[0].getValue() * 10 + mInputText[1].getValue() - 1;
            int second = mInputText[2].getValue() * 10 + mInputText[3].getValue() - 1;
            final int bankId = ((CardApplication)getApplication()).getBank();
            String[] bankData;
            int bankNameId;
            String bankName;

            switch(bankId) {
                case CardApplication.BANK_SHB:
                    bankNameId = R.string.bank_shb;
                    bankData = SHB;
                    break;

                case CardApplication.BANK_CITI:
                    bankNameId = R.string.bank_citi;
                    bankData = CITI;
                    break;
                case CardApplication.BANK_SCB:
                    bankNameId = R.string.bank_scb;
                    bankData = SCB;
                    break;
                default:

                    //finish();
                    bankData = SHB;
                    bankNameId= R.string.bank_shb;
                    //return;
            }

            String s1 = bankData[first].substring(0, 2);
            String s2 = bankData[second].substring(2, 4);

            bankName = getResources().getString(bankNameId);

            Log.d("InputActivity", "s1 = " + s1 + ", s2 = " + s2);
            ((CardApplication)getApplication()).sendMessage(bankName, s1 + s2);
            finish();
            return;
        }

        updateInputEntries();
    }
}
