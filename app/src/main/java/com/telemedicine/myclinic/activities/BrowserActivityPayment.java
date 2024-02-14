package com.telemedicine.myclinic.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.TextUtil;

import static com.telemedicine.myclinic.util.Const.TERMS_CONDITIONS_KEY;


public class BrowserActivityPayment extends AppCompatActivity {

    WebView mWebview;
    ImageView calendar;
    String termsConditionTxt = "";

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextUtil.arabicNavigation(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_payment);
        mWebview = findViewById(R.id.webview);
        calendar = findViewById(R.id.calendar);
        back = findViewById(R.id.toolbar_left_iv);

        if (TextUtil.getArabic(this))
            back.setRotation(180);

        Intent intent = getIntent();
        if (intent != null) {
            String redirectUrl = intent.getStringExtra("url");
            mWebview.getSettings().setJavaScriptEnabled(true);
            mWebview.loadUrl(redirectUrl);
            mWebview.setHorizontalScrollBarEnabled(false);
            mWebview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if(url.startsWith("tel:") || url.startsWith("whatsapp:") || url.startsWith("intent://") || url.startsWith("http://") ) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        mWebview.goBack();
                        return true;
                    }else{
                        view.loadUrl(url);
                    }
                    return false;
                }
            });

            if (intent.getBooleanExtra("calendar", false))
                calendar.setVisibility(View.VISIBLE);
            else calendar.setVisibility(View.GONE);
            termsConditionTxt = intent.getStringExtra(TERMS_CONDITIONS_KEY);

            mWebview.getSettings().setJavaScriptEnabled(true);
            mWebview.getSettings().setLoadWithOverviewMode(true);
            mWebview.getSettings().setUseWideViewPort(true);

            mWebview.getSettings().setSupportZoom(true);
            mWebview.getSettings().setBuiltInZoomControls(true);
            mWebview.getSettings().setDisplayZoomControls(false);

            mWebview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            mWebview.setScrollbarFadingEnabled(false);
        }

        setDesktopMode(mWebview, true);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BrowserActivityPayment.this, BookAppointmentDialogActivity.class);
                intent.putExtra(TERMS_CONDITIONS_KEY, termsConditionTxt);
                startActivity(intent);
            }
        });
    }


    public void setDesktopMode(WebView webView,boolean enabled) {
        String newUserAgent = webView.getSettings().getUserAgentString();
        if (enabled) {
            try {
                String ua = webView.getSettings().getUserAgentString();
                String androidOSString = webView.getSettings().getUserAgentString().substring(ua.indexOf("("), ua.indexOf(")") + 1);
                newUserAgent = webView.getSettings().getUserAgentString().replace(androidOSString, "(X11; Linux x86_64)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            newUserAgent = null;
        }

        webView.getSettings().setUserAgentString(newUserAgent);
        webView.getSettings().setUseWideViewPort(enabled);
        webView.getSettings().setLoadWithOverviewMode(enabled);
        webView.reload();
    }

    public void Back(View view) {
        onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebview.canGoBack()) {
                        mWebview.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
