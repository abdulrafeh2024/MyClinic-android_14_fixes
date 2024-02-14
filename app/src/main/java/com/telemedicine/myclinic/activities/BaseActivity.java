package com.telemedicine.myclinic.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.telemedicine.myclinic.App;
import com.telemedicine.myclinic.AppointmentDetailActivity;
import com.telemedicine.myclinic.base.BaseViewModel;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.injection.AppComponent;
import com.telemedicine.myclinic.listeners.LogoutListener;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.ApptValidationForCheckinDTO;
import com.telemedicine.myclinic.models.ApptValidationResponseModel;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.AppointmentEvent;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.MessageBox;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.webservices.RestClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;

import static com.telemedicine.myclinic.util.Const.WORKER_Count;
import static com.telemedicine.myclinic.util.Const.WORKER_EXECUTED;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseActivity extends AppCompatActivity implements LogoutListener {


    public App app;
    public AppComponent component;


    private BaseViewModel baseViewModel = null;
    TinyDB tinyDB = null;

    public ApptsMiniModel mApptsMiniModel;

    public void inject() {
    }

    public BaseViewModel setViewModel() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String name = this.getClass().getSimpleName();
        if (name.equalsIgnoreCase("LoginActivity") || name.equalsIgnoreCase("RegisterActivity")) {
            TextUtil.arabicNavigation1(this);
        } else
            TextUtil.arabicNavigation(this);

        super.onCreate(savedInstanceState);
        app = (App) getApplication();
        component = app.component;

        inject();

        tinyDB = new TinyDB(this);
        setContentView(layout());
        ButterKnife.bind(this);
        disableAutofill();

        boolean isSessionStarted = tinyDB.getBoolean("session");
        if (isSessionStarted) {
            tinyDB.putBoolean("session", false);
            String expiryDate = tinyDB.getString(Const.TOKEN_EXPIRY_KEY);
            long dateLong = LocaleDateHelper.convertDateFormatZone(expiryDate, "yyyy-MM-dd'T'HH:mm:ss");

            Date date = Calendar.getInstance().getTime();
            long mills = dateLong - date.getTime();

            ((App) getApplication()).startAlarm(mills);
            ((App) getApplication()).RenewToken(mills);
            // Toast.makeText(getApplicationContext(),tinyDB.getString(Const.TOKEN_KEY), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        baseViewModel = setViewModel();
        if (baseViewModel != null) {
            baseViewModel.isLoading().observe(this, isLoading -> {
                if (isLoading) {
                    showWaitDialog();
                } else {
                    hideWaitDialog();
                }
            });

            baseViewModel.getErrorMessage().observe(this, error -> {
                Toast.makeText(this, error.getDescription(), Toast.LENGTH_LONG).show();
            });


        }
    }

    protected abstract int layout();

    private Dialog dialog;

    public Dialog showWaitDialog() {
        if (dialog != null && dialog.isShowing()) {
            // showToast("showing");

        } else {
            dialog = MessageBox.showSplash(this);
        }
        dialog.setCancelable(false);

        return dialog;
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void hideWaitDialog() {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {

            }
        }
    }

    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.O)
    private void disableAutofill() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Do something for lollipop and above versions
            getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }
    }

    @Override
    public void onSessionLogout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("show", true);
        intent.putExtra("timeout", true);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // int count =   tinyDB.getInt(WORKER_Count);
        // Toast.makeText(getApplicationContext(), "base "+count, Toast.LENGTH_LONG).show();
        if (Const.ISLOGGOUT) {
            Const.ISLOGGOUT = false;
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("show", true);
            intent.putExtra("timeout", true);
            startActivity(intent);
            finish();
        }
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void checkServerValidation(ApptsMiniModel apptsMiniModel) {

        if (ConnectionUtil.isInetAvailable(this)) {
            //showWaitDialog();

            String companyId = tinyDB.getString(Const.COMPANY_ID);
            String securityToken = new TinyDB(this).getString(Const.TOKEN_KEY);

            ApptValidationForCheckinDTO apptValidationForCheckinDTO =
                    new ApptValidationForCheckinDTO(securityToken, apptsMiniModel.getApptId(), companyId);

            RestClient.getInstance().apptValidationForChecking(apptValidationForCheckinDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    // hideWaitDialog();

                    ApptValidationResponseModel apptsMiniModelLoc = (ApptValidationResponseModel) result;

                    if (apptsMiniModelLoc != null) {

                        MobileOpResult mobileOpResult = apptsMiniModelLoc.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                // apptForCheckIn();
                                // dialogSuccessCheckin();

                          /*      self_check_in_loading.setVisibility(View.GONE);
                                self_checkin_progress.setVisibility(View.GONE);
                                selfCheckin.setVisibility(View.VISIBLE);
                                payOnline.setVisibility(View.VISIBLE);
                                payOnline.setText(getString(R.string.check_in_online));*/

                                //ErrorMessage.getInstance().showSuccessGreen(BaseActivity.this,
                                //        "Check-in available for "+apptsMiniModel.getDoctorProfile().getNameEn()+". Please go to the appointment page to check-in.");

                                // .onValidationSuccess(apptsMiniModel.getDoctorProfile().getNameEn());
                               // showValidationSuccess(apptsMiniModel.getDoctorProfile().getNameEn());

                                EventBus.getDefault().post(
                                        new AppointmentEvent(
                                                apptsMiniModel.getDoctorProfile().getNameEn(),apptsMiniModel.getDoctorProfile().getNameAr(), ""));

                                ArrayList<ApptsMiniModel> appointmentList = new ArrayList<ApptsMiniModel>();

                                if (!tinyDB.getListAppointment(Const.APPOINTMENT_LIST).isEmpty()) {
                                    appointmentList = tinyDB.getListAppointment(Const.APPOINTMENT_LIST);
                                    appointmentList.add(apptsMiniModel);
                                    tinyDB.putListAppointments(Const.APPOINTMENT_LIST, appointmentList);
                                } else {
                                    appointmentList.add(apptsMiniModel);
                                    tinyDB.putListAppointments(Const.APPOINTMENT_LIST, appointmentList);
                                }

                            } else {

                               /* self_check_in_loading.setVisibility(View.GONE);
                                self_checkin_progress.setVisibility(View.GONE);
                                selfCheckin.setVisibility(View.GONE);
                                payOnline.setVisibility(View.GONE);*/

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(BaseActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(BaseActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                               // ErrorMessage.getInstance().showErrorYellow(BaseActivity.this, errorMesg);
                                EventBus.getDefault().post(
                                        new AppointmentEvent(
                                                "","", errorMesg));


                              /*  if (mobileOpResult.getResult() == 600 || mobileOpResult.getResult() == 700) {

                                    ErrorMessage.getInstance().showErrorYellow(BaseActivity.this, errorMesg);
                                } else {
                                    ErrorMessage.getInstance().showErrorYellow(BaseActivity.this, errorMesg);
                                }*/
                                // ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMesg);
                            }

                        } else {

                            ErrorMessage.getInstance().showErrorYellow(BaseActivity.this, errorMessage);
                        }
                    } else {

                        ErrorMessage.getInstance().showErrorYellow(BaseActivity.this, errorMessage);
                    }
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

}

