package com.telemedicine.myclinic.activities;

import android.content.Intent;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.forgotPassword.ForgetPasswordDTO;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.RegularEdittext;
import com.telemedicine.myclinic.webservices.RestClient;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgotActivity extends BaseActivity {

    @BindView(R.id.email_edt)
    RegularEdittext email_edt;

    @BindView(R.id.email_edt_til)
    TextInputLayout email_edt_til;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @BindView(R.id.reset_email)
    LightButton reset_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transformViews();

    }

    @Override
    protected int layout() {
        return R.layout.activity_forgot;
    }


    public void Reset(View view) {
        String email = email_edt.getText().toString();

        if (ConnectionUtil.isInetAvailable(this)) {

            if (!ValidationHelper.isNullOrEmpty(email)) {

                if (!ValidationHelper.isEmailValid(email)) {
                    ErrorMessage.getInstance().showValidationMessage(this, email_edt, getString(R.string.please_enter_valid_email));
                    return;
                } else {

                    String locality = new TinyDB(this).getString(Const.LOCALITY);
                    ForgetPasswordDTO forgetPasswordDTO = new ForgetPasswordDTO(email.trim(), Integer.parseInt(locality));

                    showWaitDialog();
                    RestClient.getInstance().forgotPassword(forgetPasswordDTO, new OnResultListener() {
                        @Override
                        public void onResult(Object result, boolean status, String errorMessage) {
                            hideWaitDialog();
                            MobileOpResult mobileOpResult = (MobileOpResult) result;

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                    Intent intent = new Intent(ForgotActivity.this, EmailSentActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {

                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(ForgotActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(ForgotActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(ForgotActivity.this, errorMesg);
                                }

                            } else {
                                ErrorMessage.getInstance().showError(ForgotActivity.this, errorMessage);

                            }
                        }
                    });

                }

            } else {
                ErrorMessage.getInstance().showValidationMessage(this, email_edt, getString(R.string.please_enter_email));
                /*TextUtil.tILError(this, email_edt_til, email_edt);*/
            }
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));

        }
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    void transformViews() {
        if (TextUtil.getArabic(this)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar_left_iv.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            toolbar_left_iv.setLayoutParams(params);
            toolbar_left_iv.setRotation(180);

            reset_email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);

        }
    }
}
