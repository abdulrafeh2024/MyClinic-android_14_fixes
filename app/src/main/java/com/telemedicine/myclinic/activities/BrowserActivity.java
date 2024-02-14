package com.telemedicine.myclinic.activities;

import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.AppointmentEvent;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.LocaleUtils;
import com.telemedicine.myclinic.util.TextUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class BrowserActivity extends BaseActivity {


    @BindView(R.id.web)
    WebView webView;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (TextUtil.getEnglish(this))
            LocaleUtils.initialize(this, LocaleUtils.ENGLISH);
        else if (TextUtil.getArabic(this))
            LocaleUtils.initialize(this, LocaleUtils.ARABIC);

        getIntentValue();

        webView.setBackgroundResource(R.drawable.register_bg);
        webView.setBackgroundColor(0x00000000);
        toolbar_left_iv.setVisibility(View.INVISIBLE);
    }

    @Override
    protected int layout() {
        return R.layout.activity_browser;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AppointmentEvent appointmentEvent) {
        if (!appointmentEvent.getDoctorNameEn().equals("")) {
            if(TextUtil.getArabic(this)){
                ErrorMessage.getInstance().showSuccessGreen(BrowserActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameAr() + ". "+getString(R.string.please_press_to_continue));
            }else{
                ErrorMessage.getInstance().showSuccessGreen(BrowserActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameEn() + ". "+getString(R.string.please_press_to_continue));
            }} else {
            ErrorMessage.getInstance().showErrorYellow(BrowserActivity.this, appointmentEvent.getErrorMSg());
        }
    }

    void getIntentValue() {

        Intent intent = getIntent();
        if (intent != null) {

            String url = intent.getStringExtra("url");
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.setInitialScale(250);
            //This will zoom out the WebView
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.setWebViewClient(new WebViewClient() {
                                         public boolean shouldOverrideUrlLoading(WebView view, String url) {


                                             if (url.contains(getString(R.string.checkout_ui_callback_scheme)))
                                                 view.setVisibility(View.GONE);

                                             view.loadUrl(url);
                                             return false;
                                         }

                                         @Override
                                         public void onPageFinished(WebView view, String url) {

                                             if (url.contains(getString(R.string.checkout_ui_callback_scheme))) {
                                                 view.clearHistory();
                                                 Intent intent = new Intent("payment");
                                                 // You can also include some extra data.
                                                 intent.putExtra("payment", getString(R.string.checkout_ui_callback_scheme));
                                                 LocalBroadcastManager.getInstance(BrowserActivity.this).sendBroadcast(intent);
                                                 finish();
                                             }
                                         }
                                     }

            );

            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {

                    if (progress == 100) {
                        hideWaitDialog();
                    } else {
                        showWaitDialog();
                    }
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

     /*   new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setConfirmText(getString(R.string.vsee_kit_yes)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                finish();
            }
        })
                .setCancelText(getString(R.string.no)).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();

            }
        }).setTitleText("Do you want to exit.").show();*/
    }


    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }
}
