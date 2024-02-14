package com.telemedicine.myclinic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.telemedicine.myclinic.App;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.RegularTextView;

import java.util.ArrayList;

import static com.telemedicine.myclinic.util.Const.EXISTING_PATIENT;
import static com.telemedicine.myclinic.util.Const.IS_CENTRE_OF_EXCELLENCE;
import static com.telemedicine.myclinic.util.Const.IS_QUICK_REG;
import static com.telemedicine.myclinic.util.Const.PAST_COUNT;
import static com.telemedicine.myclinic.util.Const.UPCOMING_COUNT;

public class AppointmentBottomActivity extends Activity {

    int upcomingCount = 0;
    int pastCount = 0;
    boolean isCenterOfExcellence = false;

    TinyDB tinyDB = null;
    RegularTextView  upcomingCountTv, pastCountTv;
    RelativeLayout  centerOfExcellency, upcoming_rltv, past_rltv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextUtil.translation(this);
        setContentView(R.layout.activity_appointment_bottom);
        tinyDB = new TinyDB(this);
        upcomingCountTv = findViewById(R.id.upcoming_count);
        pastCountTv = findViewById(R.id.pastCount);
        centerOfExcellency = findViewById(R.id.centerOfExcellency);
        past_rltv = findViewById(R.id.past_rltv);
        upcoming_rltv = findViewById(R.id.upcoming_rltv);

        Intent intent = getIntent();
        if (intent != null) {
            upcomingCount = intent.getIntExtra(UPCOMING_COUNT, 0);
            pastCount = intent.getIntExtra(PAST_COUNT, 0);
            isCenterOfExcellence = intent.getBooleanExtra(IS_CENTRE_OF_EXCELLENCE, false);
        }

        populateViews();

        centerOfExcellency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(AppointmentBottomActivity.this, CenterOfExcellencyActivity.class);
                startActivity(intent);
            }
        });

        upcoming_rltv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Intent intent = new Intent(AppointmentBottomActivity.this, UpcomingAppointmentsActivity.class);
                tinyDB.putBoolean(Const.BACK_TO_HOME, false);
                startActivity(intent);
            }
        });

        past_rltv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Intent intent = new Intent(AppointmentBottomActivity.this, PastAppointmentActivity.class);
                startActivity(intent);
            }
        });
    }

    private void populateViews() {
        upcomingCountTv.setText(""+upcomingCount);
        pastCountTv.setText(""+pastCount);
        if(isCenterOfExcellence){
            centerOfExcellency.setVisibility(View.VISIBLE);
        }else{
            centerOfExcellency.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_down);
    }

    public void Hide(View view) {
        onBackPressed();
    }
}