package com.telemedicine.myclinic.activities;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.toxbox.ToxBoxActivity;
import com.telemedicine.myclinic.views.BoldTextView;

public class ConsultantRoomActivity extends BaseActivity {

    @SerializedName("time")
    BoldTextView timeView;

    String timeStr = null;
    String tokeinId = null;
    String sessionId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        if (intent != null) {
            timeStr = intent.getStringExtra("time");
            tokeinId = intent.getStringExtra("tokenId");
            sessionId = intent.getStringExtra("sessionId");
            //callToxBox();

           /* if (!ValidationHelper.isNullOrEmpty(timeStr))
                timeView.setText(timeStr);*/

            a();
        }
    }

    @Override
    protected int layout() {
        return R.layout.activity_consultant_room;
    }


    void callToxBox() {
        Intent intent = new Intent(ConsultantRoomActivity.this, ToxBoxActivity.class);
        intent.putExtra("tokenId", tokeinId);
        intent.putExtra("sessionId", sessionId);
        startActivity(intent);
    }

    void a() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ConsultantRoomActivity.this, PhoneCallActivity.class));
            }
        }, 5000);

    }
}
