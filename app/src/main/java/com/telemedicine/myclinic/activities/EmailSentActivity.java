package com.telemedicine.myclinic.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.telemedicine.myclinic.myapplication.R;

public class EmailSentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int layout() {
        return R.layout.activity_email_sent;
    }



    public void OK(View view) {

        finish();
    }
}
