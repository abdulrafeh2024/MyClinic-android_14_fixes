package com.telemedicine.myclinic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.telemedicine.myclinic.myapplication.R;

public class BookAppointmentThreesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int layout() {
       return R.layout.activity_book_appointment_threes;
    }


    public void openFour(View view) {
        Intent intent = new Intent(BookAppointmentThreesActivity.this, BookAppointmentFourActivity.class);
        startActivity(intent);
    }
}