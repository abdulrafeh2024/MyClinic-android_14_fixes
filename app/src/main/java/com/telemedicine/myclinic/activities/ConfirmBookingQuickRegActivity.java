package com.telemedicine.myclinic.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.appointments.OrdersRadModel;
import com.telemedicine.myclinic.models.bookAppointment.ApptCreateDTO;
import com.telemedicine.myclinic.models.bookAppointment.ApptCreateModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.webservices.RestClient;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.telemedicine.myclinic.util.Const.COMPANY_NAME_AR;
import static com.telemedicine.myclinic.util.Const.COMPANY_NAME_EN;
import static com.telemedicine.myclinic.util.Const.DATE;
import static com.telemedicine.myclinic.util.Const.DOCTOR_ID;
import static com.telemedicine.myclinic.util.Const.DOCTOR_IMAGE_URL;
import static com.telemedicine.myclinic.util.Const.DOCTOR_NAME;
import static com.telemedicine.myclinic.util.Const.DOCTOR_SPECIALITY;
import static com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY;
import static com.telemedicine.myclinic.util.Const.PATIENT_ID;
import static com.telemedicine.myclinic.util.Const.SECONDARY_APPT_REFRESH;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_ID;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_NAME_AR;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_NAME_EN;

public class ConfirmBookingQuickRegActivity extends BaseActivity {

    @BindView(R.id.doctor_image)
    CircleImageView doctorImageView;

    @BindView(R.id.doctor_name)
    BoldTextView doctorNameTv;

    @BindView(R.id.datetime_value)
    LightTextView datetimeValueTv;

    @BindView(R.id.location_name)
    BoldTextView location_name;

    @BindView(R.id.doctor_profession)
    LightTextView doctor_profession;

    @BindView(R.id.confirm)
    LightButton confirm;


    @BindView(R.id.chat_bot_bg)
    RelativeLayout chatBotBg;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.chatBot_rounded)
    View chatBot_rounded;

    @BindView(R.id.chat_bot_web_view)
    WebView chatBotWebView;


    String doctorName = "";
    String doctorDate = "";
    String doctorTime = "";
    String doctorImage = "";
    int authProvider = 0;

    String speciality = "";
    String date = "";
    String specialityId = "";
    String doctorId = "";
    String specialityNameEn= "";
    String specialityNameAr = "";
    String companyName = "";
    boolean isTelemedicine = false;
    long apptBookType;
    boolean isSecondary = false;
    boolean isInsurance = false;
    boolean followUpAppt = false;
    OrdersRadModel ordersRadModel = null;
    long apptId = 0;
    String patientId = "";
    String weekDay = "";

    boolean isChatBotVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected int layout() {
        return R.layout.activity_confirm_booking_quick_reg;
    }


    private void init() {

        Intent intent = getIntent();
        tinyDB = new TinyDB(this);
        if (intent != null) {
            doctorDate = intent.getStringExtra("Date_local");
            doctorName = intent.getStringExtra(DOCTOR_NAME);
            speciality = intent.getStringExtra(DOCTOR_SPECIALITY);
            date = intent.getStringExtra(DATE);
            patientId = intent.getStringExtra(PATIENT_ID);
            specialityId = intent.getStringExtra(SPECIALITY_ID);
            doctorId = intent.getStringExtra(DOCTOR_ID);
            weekDay = intent.getStringExtra("week_Day");
            specialityNameEn = intent.getStringExtra(SPECIALITY_NAME_EN);
            specialityNameAr = intent.getStringExtra(SPECIALITY_NAME_AR);
            companyName = intent.getStringExtra(COMPANY_NAME_EN);
            isTelemedicine = intent.getBooleanExtra(Const.ISTELEMEDICINE_KEY, false);
            doctorImage = intent.getStringExtra(DOCTOR_IMAGE_URL);
            doctorTime = intent.getStringExtra(Const.SELECTED_TIME);
            apptBookType = intent.getIntExtra(Const.APPT_BOOK_TYPE, 0);
            apptBookType = 10;
            isSecondary = intent.getBooleanExtra(Const.IS_SECONDARY, false);
            ordersRadModel = intent.getParcelableExtra(Const.ORDER_RAD);
            isInsurance = intent.getBooleanExtra(Const.IS_INSURANCE_KEY, false);
            followUpAppt = intent.getBooleanExtra(Const.FOLLOW_UP_TYPE, false);
            apptId = intent.getLongExtra(Const.APPT_ID, 0);

            authProvider = intent.getIntExtra(Const.AUTH_PROVIDER, -1);

        }

        chatBotWebView.getSettings().setJavaScriptEnabled(true);
        chatBotWebView.setHorizontalScrollBarEnabled(false);

        chatBotWebView.getSettings().setLoadWithOverviewMode(true);
        chatBotWebView.getSettings().setUseWideViewPort(true);

        chatBotWebView.getSettings().setSupportZoom(true);
        chatBotWebView.getSettings().setBuiltInZoomControls(true);
        chatBotWebView.getSettings().setDisplayZoomControls(false);

        chatBotWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        chatBotWebView.setScrollbarFadingEnabled(false);
        loadChatBot();
        populateViews();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmBookings();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isChatBotVisible) {
            openChatBotBg(null);
            return;
        }else{
            super.onBackPressed();
        }
    }

    public void openChatBot(View view) {
        chatBotBg.setVisibility(View.VISIBLE);
        isChatBotVisible = true;
        loadChatBot();
     /*   Intent intent = new Intent(this, BrowserActivityPayment.class);
        intent.putExtra("calendar", false);
        intent.putExtra("url", "https://myclinic.hellotars.com/conv/Rg2PDe/");
        //  intent.putExtra("url", getString(R.string.contact_us_url));
        intent.putExtra(Const.TERMS_CONDITIONS_KEY, termsConditionTxt);
        startActivity(intent);*/
    }

    public void openChatBotBg(View view) {
        chatBotBg.setVisibility(View.GONE);
        isChatBotVisible = false;
    }


    private void loadChatBot() {

        chatBotWebView.loadUrl("https://myclinic.hellotars.com/conv/Rg2PDe/");
        chatBotWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
    }
    private void populateViews() {
        Glide.with(this).load(doctorImage).placeholder(R.drawable.doctr).into((ImageView) doctorImageView);
        doctorNameTv.setText(doctorName);

        String time = doctorTime;
        if (TextUtil.getArabic(this)) {
            time = time.replaceAll(" AM", " ص");
            time = time.replaceAll(" PM", " م");
        }

        datetimeValueTv.setText(weekDay+"  "+doctorDate + ", " + time);
        if(TextUtil.getArabic(ConfirmBookingQuickRegActivity.this)){
            doctor_profession.setText(specialityNameAr);
        }else{
            doctor_profession.setText(specialityNameEn);
        }
        location_name.setText(companyName);

        if(isTelemedicine){
            location_name.setVisibility(View.GONE);
        }else{
            location_name.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    private void launchAppointmentConfirm() {
        tinyDB.putBoolean(Const.BIOMETRIC_ENABLED, false);
        tinyDB.putBoolean(Const.BIOMETRIC_REMINDER, false);
        Intent intent = new Intent(this, AppointmentConfirmedAnonymousActivity.class);
        intent.putExtra("Date_local", doctorDate);
        intent.putExtra("DoctorName", doctorName);
        intent.putExtra("Time", doctorTime);
        intent.putExtra("image", doctorImage);
        intent.putExtra("week_Day", weekDay);
        intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicine);
        intent.putExtra(SPECIALITY_NAME_EN, specialityNameEn);
        intent.putExtra(SPECIALITY_NAME_AR, specialityNameAr);
        intent.putExtra(COMPANY_NAME_EN, companyName);
        startActivity(intent);
        Intent intent1 = new Intent();
        setResult(RESULT_OK, intent1);
        finish();
    }

    public void confirmBookings() {
        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        String companyId = tinyDB.getString(Const.COMPANY_ID);

        if (followUpAppt)
            apptBookType = 20;

        ApptCreateDTO apptCreateDTO = new ApptCreateDTO(securityToken, patientId, specialityId, doctorId,
                String.valueOf(apptBookType),date, isTelemedicine, isInsurance, 0, companyId);

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            RestClient.getInstance().apptCreate(apptCreateDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    ApptCreateModel apptCreateModel = (ApptCreateModel) result;

                    if (apptCreateModel != null) {

                        MobileOpResult mobileOpResult = apptCreateModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                             /*   apptNo = apptCreateModel.getApptNo();
                                long apptId = apptCreateModel.getApptId();

                                showSuceessFulMessage(apptNo, apptId);*/

                                if(authProvider == 0 || authProvider == 1 || authProvider == 2){
                                    showSuccessDialog();
                                }else{
                                    launchAppointmentConfirm();
                                }


                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(ConfirmBookingQuickRegActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(ConfirmBookingQuickRegActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                if(mobileOpResult.getResult() == 600 || mobileOpResult.getResult() == 700){
                                    ErrorMessage.getInstance().showErrorYellow(ConfirmBookingQuickRegActivity.this, errorMesg);
                                }else{
                                    ErrorMessage.getInstance().showError(ConfirmBookingQuickRegActivity.this, errorMesg);
                                }

                                //ErrorMessage.getInstance().showError(ConfirmBookingQuickRegActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(ConfirmBookingQuickRegActivity.this, errorMessage);

                    } else {
                        ErrorMessage.getInstance().showError(ConfirmBookingQuickRegActivity.this, errorMessage);
                    }
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, "Internet is not available");
        }
    }

    private void showSuccessDialog() {

        SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        alertDialog.setCancelable(false);
        alertDialog.setTitleText(getResources().getString(R.string.my_clininc))
                .setContentText(getString(R.string.registered_successfully))
                .setConfirmText(getString(R.string.ok)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                launchAppointmentConfirm();
            }
        }).show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }
}