package com.telemedicine.myclinic.activities;

import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.toxbox.ToxBoxActivity;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;


/**
 * @author Asad Abbas
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class PhoneCallActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCallNumberLabel;
    private TextView tvCallNumber;
    private TextView tvPickUp;
    private TextView tvHangUp;
    private String phoneNumber;
    private int callingTime;


    String tokenId = null;
    String sessionId = null;
    String name = "";
    String apptID = "";
    String patientName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_call);
        initData();
        initView();
    }

    private void initData() {
        if (getIntent() != null) {
            tokenId = getIntent().getStringExtra("tokenId");
            sessionId = getIntent().getStringExtra("sessionId");
            name = getIntent().getStringExtra("name");
            apptID = getIntent().getStringExtra("apptId");
            patientName = getIntent().getStringExtra("PatientName");
        }
    }

    private void initView() {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //hide navigationBar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        tvCallNumberLabel = findViewById(R.id.tv_call_number_label);
        tvCallNumberLabel.setText(TextUtil.name);
        tvCallNumber = findViewById(R.id.tv_call_number);
        tvCallNumber.setText(TextUtil.speciality);
        tvPickUp = findViewById(R.id.tv_phone_pick_up);
        tvHangUp = findViewById(R.id.tv_phone_hang_up);

        tvPickUp.setOnClickListener(this);
        tvHangUp.setOnClickListener(this);

        // turn on and unlock screen
        wakeUpAndUnlock();
    }

    public void wakeUpAndUnlock() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        if (pm == null) {
            return;
        }

        boolean screenOn = pm.isInteractive();
        if (!screenOn) {
            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP, getPackageName() + "TAG");
            wl.acquire(10000);
            wl.release();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            if (keyguardManager == null) {
                return;
            }
            keyguardManager.requestDismissKeyguard(this, null);
        } else {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_phone_pick_up) {
            closeNotification();
            callToxBox();
        } else if (v.getId() == R.id.tv_phone_hang_up) {
            closeNotification();
            finish();
        }
    }

    void callToxBox() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            if (keyguardManager == null) {
                return;
            }
            keyguardManager.requestDismissKeyguard(this, null);
        } else {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }

        // Intent intent = new Intent(PhoneCallActivity.this, ToxBoxActivity.class);
        Intent intent = new Intent(this, ZoomMeetingActivity.class);
        // zoomIntent.putExtra("tokenId", tokenId);
        // zoomIntent.putExtra("sessionId", sessionId);
        intent.putExtra("tokenId", tokenId);
        intent.putExtra("sessionId", sessionId);
        intent.putExtra("name", name);
        intent.putExtra("apptId", apptID);
        intent.putExtra("patientName", patientName);
        //  Toast.makeText(this, "yokenid"+tokenId,Toast.LENGTH_LONG).show();
        startActivity(intent);
        finish();

       /* if(isTaskRoot()){
            // Intent intent = new Intent(PhoneCallActivity.this, ToxBoxActivity.class);
            Intent intent = new Intent(this, HomeActivity.class);
            // zoomIntent.putExtra("tokenId", tokenId);
            // zoomIntent.putExtra("sessionId", sessionId);
            intent.putExtra("tokenId", tokenId);
            intent.putExtra("sessionId", sessionId);
            intent.putExtra("name", name);
            //  Toast.makeText(this, "yokenid"+tokenId,Toast.LENGTH_LONG).show();
            startActivity(intent);
            finish();
        }else{

        }*/

    }

    void closeNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(0);
        ValidationHelper.stopRingtone();
    }

    @Override
    public void onBackPressed() {
    }
}
