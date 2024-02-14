package com.telemedicine.myclinic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.TextUtil;

public class ContactUsPopActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextUtil.translation(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us_pop);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    public void Hide(View view) {
        onBackPressed();
    }

    public void onWhatsapp(View view) {
        whatsapp("0501228111");
    }

    public void onCallUs(View view) {
        String phone = "92 00 22 811";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    void whatsapp(String number) {
        String url = "https://api.whatsapp.com/send?phone=" + number;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}