package com.speedata.smallscreen;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.speedata.smallscreenlib.SmallScreen;

public class SmallScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnWriteCH, btnWriteEN, btnClear;
    private ToggleButton toggleTime;
    private SmallScreen smallScreen;
    private EditText edvWriteString;
    private EditText edvAsci;
    private EditText edvPeriod;
    private CheckBox checkCyle;
    private Button btnStopRefash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_screen);
        btnClear = (Button) findViewById(R.id.btn_clear);
        edvWriteString = (EditText) findViewById(R.id.edv_string);
        edvWriteString.setText("天气降温，注意保暖！");
        btnWriteCH = (Button) findViewById(R.id.btn_write_ch);
        btnWriteEN = (Button) findViewById(R.id.btn_write_en);
        btnStopRefash = (Button) findViewById(R.id.btn_stop_refash);
        toggleTime = (ToggleButton) findViewById(R.id.btn_sync_time);
        edvAsci = (EditText) findViewById(R.id.edv_asic);
        edvPeriod = (EditText) findViewById(R.id.edv_period);
        checkCyle = (CheckBox) findViewById(R.id.checkbox_sync);
        btnStopRefash.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnWriteCH.setOnClickListener(this);
        btnWriteEN.setOnClickListener(this);
        smallScreen = SmallScreen.getInstance(this);
        toggleTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    if (b)
                        smallScreen.startClock();
                    else
                        smallScreen.stopClock();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

//        registerSmallScreenBroadcast();
    }

//    private void registerSmallScreenBroadcast() {
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(ACTION_START_CLOCK);
//        intentFilter.addAction(ACTION_STOP_CLOCK);
//        registerReceiver(smallScreemReceiver, intentFilter);
//    }

    private String ACTION_START_CLOCK = "speedata_smallscreen_startclock";
    private String ACTION_STOP_CLOCK = "speedata_smallscreen_stopclock";

    @Override
    public void onClick(View view) {
        if (view == btnClear) {
            try {
                smallScreen.clearScreen();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (view == btnWriteCH) {
            String writeString = edvWriteString.getText().toString();

            if (writeString.length() >= 6) {
                btnStopRefash.setEnabled(true);
            } else {
                btnStopRefash.setEnabled(false);
            }
            try {
                smallScreen.writeString(writeString, checkCyle
                        .isChecked(), Integer.parseInt(edvPeriod.getText().toString()), true);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (view == btnStopRefash) {
            //停止刷新
            smallScreen.stopCirculation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smallScreen.isCirculation()) {
            smallScreen.stopCirculation();
        }
//        unregisterReceiver(smallScreemReceiver);
    }

//    private BroadcastReceiver smallScreemReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(ACTION_START_CLOCK)) {
//                SystemProperties.set("persist.sys.smallscreen","true");
//                try {
//                    mSmallScreenManager.startClock();
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            } else if (action.equals(ACTION_STOP_CLOCK)) {
//                SystemProperties.set("persist.sys.smallscreen","false");
//                try {
//                    mSmallScreenManager.stopClock();
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };

}
