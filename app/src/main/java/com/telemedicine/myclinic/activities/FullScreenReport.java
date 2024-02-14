package com.telemedicine.myclinic.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.telemedicine.myclinic.myapplication.R;

import butterknife.BindView;

public class FullScreenReport extends BaseActivity {


    @BindView(R.id.web)
    WebView hc1WebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hc1WebView.getSettings().setJavaScriptEnabled(true);
        String chartHtml = "";
        Intent intent = getIntent();
        if (intent != null) {
            chartHtml = intent.getStringExtra("url");
        }

        hc1WebView.loadDataWithBaseURL("file:///android_asset/", chartHtml,
                "text/html", "utf-8", null);
    }

    @Override
    protected int layout() {
        return R.layout.activity_full_screen_report;
    }

}
