package com.telemedicine.myclinic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.TextUtil;

import java.util.Locale;

public class FindClinicActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextUtil.translation(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_clinic);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    public void Hide(View view) {
        onBackPressed();
    }

    public void onPrinceSultan(View view) {
        String name = "My Clinic";
        String uri = String.format(Locale.ENGLISH, "geo:0,0?q=21.659198, 39.122600 (" + name + ")");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    public void onAlSafa(View view) {
        String name = "My Clinic Alsafa";
        String uri = String.format(Locale.ENGLISH, "geo:0,0?q=21.575774, 39.199812 (" + name + ")");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    public void onPrcClicked(View view) {
        String name = "My Clinic";
        String uri = String.format(Locale.ENGLISH, "geo:0,0?q=22.839538, 38.924132 (" + name + ")");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    public void onDentalClicked(View view) {
        String name = "My Clinic";
        String uri = String.format(Locale.ENGLISH, "geo:0,0?q=21.564087, 39.142686 (" + name + ")");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
}