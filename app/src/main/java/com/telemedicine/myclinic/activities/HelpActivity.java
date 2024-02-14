package com.telemedicine.myclinic.activities;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.telemedicine.myclinic.adapters.HelpAdapter;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.views.LightButton;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.BindView;

public class HelpActivity extends BaseActivity {

    @BindView(R.id.pager)
    ViewPager mViewPager;

    @BindView(R.id.indicator)
    CirclePageIndicator indicator;

    private int _currentPage = 0;

    @BindView(R.id.next_btn)
    LightButton mNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager.setAdapter(new HelpAdapter(this));
        indicator.setViewPager(mViewPager);

        if (TextUtil.getArabic(this)) {
            _currentPage = HelpAdapter.ar_help.length -1;//7;
            mViewPager.setCurrentItem(HelpAdapter.ar_help.length -1);//7
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {

            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                _currentPage = position;
            }

            public void onPageSelected(int position) {
                if (TextUtil.getArabic(HelpActivity.this)) {

                    if(0 == position){
                        mNext.setText(getString(R.string.text_finish));
                    }else{
                        mNext.setText(getString(R.string.next));
                    }

                }else{
                    if(HelpAdapter.eng_help.length -1 == position){
                        mNext.setText(getString(R.string.text_finish));
                    }else{
                        mNext.setText(getString(R.string.next));
                    }
                }
            }
        });

        boolean show = tinyDB.getBoolean("isHelpShow");
        if (!show) {
            tinyDB.putBoolean("isHelpShow", true);
            checkBioMetricSupport();
        }
    }

    private void checkBioMetricSupport() {

        boolean isBiometricSupport = tinyDB.getBoolean(Const.BIOMETRIC_SUPPORT);
        int loggedInType = tinyDB.getInt(Const.LOGIN_TYPE);
        boolean isEnabled = tinyDB.getBoolean(Const.BIOMETRIC_ENABLED);
        boolean isReminder = tinyDB.getBoolean(Const.BIOMETRIC_REMINDER);
        boolean isFromLogin = tinyDB.getBoolean(Const.IS_FROM_LOGIN);

        if (isBiometricSupport) {
            if (loggedInType == Const.RG_CUSTOM) {
                if (!isEnabled){
                    if(!isReminder){
                        BiometricDialog();
                    }
                }
            }
        }

    }

    private void BiometricDialog() {
        Dialog dialog = new Dialog(this,R.style.ios_dialog_style);
        dialog.setContentView(R.layout.biometric_dashboard_dialog);
        dialog.setCancelable(false);
        LightButton smartTouchBtn = dialog.findViewById(R.id.smart_touch_btn);
        LightButton notNowBtn = dialog.findViewById(R.id.not_now_btn);
        LightButton doNotRemindBtn = dialog.findViewById(R.id.do_not_remind_btn);
//        // if button is clicked, close the custom dialog

        smartTouchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(SettingsActivity.Companion.getLaunchIntent(HelpActivity.this));
            }
        });

        notNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        doNotRemindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinyDB.putBoolean(Const.BIOMETRIC_REMINDER, true);
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    protected int layout() {
        return R.layout.activity_help;
    }

    public void Skip(View view) {
        finish();
    }

    public void Next(View view) {
        if (TextUtil.getEnglish(this)) {
            if (_currentPage == HelpAdapter.eng_help.length - 1) {
                finish();
            } else {
                _currentPage = _currentPage + 1;
            }
        }
        else{
            if(_currentPage == 0){
                finish();
            }else{
                _currentPage = _currentPage - 1;
            }
        }


        mViewPager.setCurrentItem(_currentPage, true);
    }
}
