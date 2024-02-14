package com.telemedicine.myclinic.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.TextUtil;

public class MoreInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextUtil.translation(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        findViewById(R.id.less_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.whatsapp_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsapp("0501228111");
            }
        });

        findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoWeb("https://web.facebook.com/MyClinicKSA/?_rdc=1&_rdr");
            }
        });

        findViewById(R.id.twitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoWeb("https://twitter.com/MyClinicKSA");
            }
        });

        findViewById(R.id.linkedin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoWeb("https://www.linkedin.com/company/myclinicksa");
            }
        });
        findViewById(R.id.insta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoWeb("https://www.instagram.com/myclinicksa/");
            }
        });
        findViewById(R.id.snap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.snapchat.com/add/myclinicksa"));
                    intent.setPackage("com.snapchat.android");
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.snapchat.com/add/myclinicksa")));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }


    void whatsapp(String number) {
        String url = "https://api.whatsapp.com/send?phone=" + number;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    void gotoWeb(String url) {
        Intent intent = new Intent(this, BrowserActivityPayment.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    public void Hide(View view) {
        onBackPressed();
    }
}
