package com.telemedicine.myclinic.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.GsonBuilder;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.telemedicine.myclinic.AppointmentDetailActivity;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.hyperpay.CustomUIActivity;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.localnotification.NotificationHelper;
import com.telemedicine.myclinic.models.ApptValidationForCheckinDTO;
import com.telemedicine.myclinic.models.ApptValidationResponseModel;
import com.telemedicine.myclinic.models.CardDTO;
import com.telemedicine.myclinic.models.CardModel;
import com.telemedicine.myclinic.models.CardsListModel;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.UpdateInsuranceDTO;
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel;
import com.telemedicine.myclinic.models.bookAppointment.ApptGetTotalOutstandingDTO;
import com.telemedicine.myclinic.models.bookAppointment.ApptGetTotalOutstandingModel;
import com.telemedicine.myclinic.models.bookAppointment.ApptPaymentDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetInsuranceByIdDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetInsuranceModel;
import com.telemedicine.myclinic.models.cancelAppointment.ApptCancelDTO;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;
import com.telemedicine.myclinic.models.onlineConfig.OpsConfigsModel;
import com.telemedicine.myclinic.models.patientMiniModels.InsuranceCardModel;
import com.telemedicine.myclinic.models.payStage.PayStageOneDTO;
import com.telemedicine.myclinic.models.payStage.PayStageOneModel;
import com.telemedicine.myclinic.models.profileUpdate.GetProfileDTO;
import com.telemedicine.myclinic.models.profileUpdate.PatientRegistrationUpdate;
import com.telemedicine.myclinic.models.profileUpdate.ProfileUpdateModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.AppointmentEvent;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.vseeActivities.WaitingRoomActivity;
import com.telemedicine.myclinic.webservices.RestClient;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionUtils;

import static com.telemedicine.myclinic.util.Const.DATE;
import static com.telemedicine.myclinic.util.Const.DOCTOR_ID;
import static com.telemedicine.myclinic.util.Const.DOCTOR_IMAGE_URL;
import static com.telemedicine.myclinic.util.Const.DOCTOR_NAME;
import static com.telemedicine.myclinic.util.Const.DOCTOR_SPECIALITY;
import static com.telemedicine.myclinic.util.Const.INSURANCE_IMAGES;
import static com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY;
import static com.telemedicine.myclinic.util.Const.OREGID_KEY;
import static com.telemedicine.myclinic.util.Const.PATIENT_UPDATE_KEY;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_ID;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BookAppointmentFourActivity extends BaseActivity {


    @BindView(R.id.doctor_name)
    BoldTextView doctorNameTv;

    @BindView(R.id.self_checkin)
    LightButton selfCheckin;

    @BindView(R.id.self_check_in_loading)
    View self_check_in_loading;

    @BindView(R.id.self_checkin_progress)
    ProgressBar self_checkin_progress;

    @BindView(R.id.doctor_profession)
    LightTextView doctorProfession;

    @BindView(R.id.datetime_value)
    LightTextView dateTime;

    @BindView(R.id.visit_reception)
    LightTextView visit_reception;

    @BindView(R.id.visit_reception_error_tv)
    LightTextView visit_reception_error_tv;


    @BindView(R.id.visit_reception_layout)
    ConstraintLayout visit_reception_layout;

    @BindView(R.id.doctor_image)
    CircleImageView doctorImage;

    @BindView(R.id.price)
    BoldTextView price;

    @BindView(R.id.button_payment_check_in)
    LightButton button_payment_check_in;

    @BindView(R.id.cencel)
    LightButton cencel;

    @BindView(R.id.confirm)
    LightButton backtoDash;

    @BindView(R.id.add_calendar)
    LightButton add_calendar;

    @BindView(R.id.changeInsurance)
    LightButton changeInsurance;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @BindView(R.id.company_name)
    TextView companyName;

    @BindView(R.id.chat_bot_bg)
    RelativeLayout chatBotBg;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.chatBot_rounded)
    View chatBot_rounded;

    @BindView(R.id.chat_bot_web_view)
    WebView chatBotWebView;

    boolean isChatBotVisible = false;

    private static final long UPDATE_INTERVAL_IN_MIL = 1000;
    private static final long FASTES_UPDATE_INTERVAL_IN_MIL = UPDATE_INTERVAL_IN_MIL / 2;

    //location
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Handler serviceHandler;
    private Location mLocation;

    boolean apptGetTotalOutstandingCalled = false;
    double ammountDue = 0.0;
    String doctorName = "";
    String doctorImageUrl = "";
    String speciality = "";
    String date = "";
    String specialityId = "";
    String doctorId = "";
    boolean isTelemedcine = false;
    long apptBookType;
    long apptId = 0;
    TinyDB tinyDB = null;
    String apptNo = null;
    boolean isSecondary = false;
    boolean followUpAppt = false;

    ApptsMiniModel apptsMiniModel = null;

    boolean isTentative = false;

    String paidZero = "";//getString(R.string.paid) + " : " + "0" + " " + getString(R.string.sar);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeLocation();
        init();

       // getPatientData(true, false, false);
    }

    private void initializeLocation() {
        mLocation = new Location("Location");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
               /* onNewLocation(locationResult.getLastLocation());
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
*/
                hideWaitDialog();
                if(locationRequested){
                    locationRequested = false;
                    onNewLocation(locationResult.getLastLocation());
                }
                handler.removeCallbacks(runnable);
                handler = null;
                runnable = null;
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        };

        createLocationRequest();

    }

    public void createLocationRequest() {
        locationRequest = new com.google.android.gms.location.LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MIL);
        locationRequest.setFastestInterval(FASTES_UPDATE_INTERVAL_IN_MIL);
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void onNewLocation(Location lastLocation) {

        hideWaitDialog();
        String companyIdLoc = tinyDB.getString(Const.COMPANY_ID);

        if (companyIdLoc.equals("nc01")) {
            mLocation.setLatitude(21.658793);
            mLocation.setLongitude(39.1225838);

            if (lastLocation.distanceTo(mLocation) < 400) {
              //  validationofCheckIn();
                apptForCheckIn();
            } else {
                dialogLocationFarAway();
            }
            //  (lastLocation.distanceTo(mLocation));

        } else if (companyIdLoc.equals("safa")) {
            mLocation.setLatitude(21.575778);
            mLocation.setLongitude(39.199847);
            if (lastLocation.distanceTo(mLocation) < 400) {
                //validationofCheckIn();
                apptForCheckIn();
            } else {
                dialogLocationFarAway();
            }

        }else{
            mLocation.setLatitude(22.839535);
            mLocation.setLongitude(38.924132);

            if (lastLocation.distanceTo(mLocation) < 400) {
              //  validationofCheckIn();
                apptForCheckIn();
            } else {
                dialogLocationFarAway();
            }
        }

    }

    IOSDialog locationFarAwayDialog = null;

    private void dialogLocationFarAway() {
        locationFarAwayDialog = TextUtil.dialgoue(BookAppointmentFourActivity.this,
                getString(R.string.you_cannot_check_in), getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            locationFarAwayDialog.dismiss();
                        } catch (android.content.ActivityNotFoundException exception) {
                            //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.myclinic.ksa")));
                        }
                    }
                });
    }

    IOSDialog checkSuccessDialog = null;

    private void dialogSuccessCheckin() {
        checkSuccessDialog = TextUtil.dialgoue(BookAppointmentFourActivity.this,
                getString(R.string.You_have_successfully_CheckedIn), getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            checkSuccessDialog.dismiss();
                            setResult(RESULT_OK);
                            finish();
                        } catch (android.content.ActivityNotFoundException exception) {
                            //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.myclinic.ksa")));
                        }
                    }
                });
    }

    private void init() {

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

        transformView();

        tinyDB = new TinyDB(this);
        paidZero = getResources().getString(R.string.paid) + ": " + "0" + " " + getString(R.string.sar);
        isTentative = tinyDB.getBoolean(Const.IS_TENT);
        Intent intent = getIntent();

        if (intent != null) {

            doctorName = intent.getStringExtra(DOCTOR_NAME);
            speciality = intent.getStringExtra(DOCTOR_SPECIALITY);
            date = intent.getStringExtra(DATE);
            specialityId = intent.getStringExtra(SPECIALITY_ID);
            doctorId = intent.getStringExtra(DOCTOR_ID);
            isTelemedcine = intent.getBooleanExtra(Const.ISTELEMEDICINE_KEY, false);
            doctorImageUrl = intent.getStringExtra(DOCTOR_IMAGE_URL);
            apptBookType = intent.getLongExtra(Const.APPT_BOOK_TYPE, 0);
            apptId = intent.getLongExtra(Const.APPT_ID, 0);
            apptNo = intent.getStringExtra(Const.APP_NO);
            isSecondary = intent.getBooleanExtra(Const.IS_SECONDARY, false);
            followUpAppt = intent.getBooleanExtra(Const.FOLLOW_UP_TYPE, false);

            apptsMiniModel = new ApptsMiniModel();

            String apptsMiniModelJson =intent.getStringExtra(Const.APPTMODEL);
            apptsMiniModel = new GsonBuilder().create().fromJson(apptsMiniModelJson, ApptsMiniModel.class);

            if (isTentative) {
                changeInsurance.setVisibility(View.GONE);
            }

            if (isTelemedcine || followUpAppt) {

                if (!isTentative) {
                    apptGetTotalOutstanding(apptId, isTelemedcine, true);
                } else {
                    selfCheckin.setVisibility(View.GONE);
                    button_payment_check_in.setVisibility(View.GONE);
                    self_check_in_loading.setVisibility(View.GONE);
                    self_checkin_progress.setVisibility(View.GONE);
                }
        /*

         if (isTelemedcine) {
                    long apptTimeMilli = milliseconds(date);
                    long currentTimeMill = System.currentTimeMillis();

                    long diff = apptTimeMilli - currentTimeMill;

                    int diffmin = (int) (diff / (60 * 1000));

                    if (diffmin < 30 && diffmin > -30) {
                        button_payment_check_in.setVisibility(View.VISIBLE);
                    } else {
                        button_payment_check_in.setVisibility(View.GONE);
                    }
                }
                */

            } else {
                if (!isTentative) {
                    apptGetTotalOutstandingInsurance(apptId, false);
                } else {
                    selfCheckin.setVisibility(View.GONE);
                    button_payment_check_in.setVisibility(View.GONE);
                    self_check_in_loading.setVisibility(View.GONE);
                    self_checkin_progress.setVisibility(View.GONE);
                }

            }

            String companyNameId = tinyDB.getString(Const.COMPANY_ID);
            String companyNameSt = "";
            if (companyNameId.equals("nc01")) {
                companyNameSt = getString(R.string.prince_sultan);
            } else if (companyNameId.equals("safa")) {
                companyNameSt = getString(R.string.safa);
            } else if (companyNameId.equals("prc")) {
                companyNameSt = getString(R.string.prc);
            }

            if (!isTelemedcine) {
                companyName.setText(companyNameSt);
            }

            setDoctorDetail();
        }
    }

    private void selfCheckInEligibility(){

        if(LocaleDateHelper.expiredAptTime(date)){
            visit_reception_layout.setVisibility(View.VISIBLE);
            visit_reception.setText(R.string.check_in_period_expired);
            hideCheckinViews();
            return;
        }
        else if (!LocaleDateHelper.isSelfCheckingTimeStart(date)) {
            visit_reception_layout.setVisibility(View.VISIBLE);
            visit_reception.setText(R.string.one_hour_wait_message);
            hideCheckinViews();
            return;
        }
        else if(isAppointmentValidated()){
            visit_reception_layout.setVisibility(View.GONE);
            showSelfCheckIn();
        }
        else {
            hideCheckinViews();
            visit_reception_layout.setVisibility(View.VISIBLE);
            visit_reception.setText(R.string.eligibility_check_message);
            checkServerValidation(apptsMiniModel);
        }
    }

    private void showSelfCheckIn() {
        self_check_in_loading.setVisibility(View.GONE);
        self_checkin_progress.setVisibility(View.GONE);
        selfCheckin.setVisibility(View.VISIBLE);
        button_payment_check_in.setVisibility(View.VISIBLE);
        button_payment_check_in.setText(getString(R.string.check_in_online));
    }

    private boolean isAppointmentValidated(){
        if (!tinyDB.getListAppointment(Const.APPOINTMENT_LIST).isEmpty()) {
            for (ApptsMiniModel apptLocalModel: tinyDB.getListAppointment(Const.APPOINTMENT_LIST)) {
                if (apptLocalModel.getApptId() == apptsMiniModel.getApptId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void hideCheckinViews() {
        self_check_in_loading.setVisibility(View.GONE);
        self_checkin_progress.setVisibility(View.GONE);
        selfCheckin.setVisibility(View.GONE);
        button_payment_check_in.setVisibility(View.GONE);
        button_payment_check_in.setText(getString(R.string.check_in_online));
    }

    private void validationofCheckIn() {
        if (ConnectionUtil.isInetAvailable(this)) {
           // showWaitDialog();

            String companyId = tinyDB.getString(Const.COMPANY_ID);
            String securityToken = new TinyDB(this).getString(Const.TOKEN_KEY);

            ApptValidationForCheckinDTO apptValidationForCheckinDTO =
                    new ApptValidationForCheckinDTO(securityToken, apptId, companyId);

            RestClient.getInstance().apptValidationForChecking(apptValidationForCheckinDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();
                    ApptValidationResponseModel apptsMiniModelLoc = (ApptValidationResponseModel) result;

                    if (apptsMiniModelLoc != null) {
                        MobileOpResult mobileOpResult = apptsMiniModelLoc.getMobileOpResult();
                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                self_check_in_loading.setVisibility(View.GONE);
                                self_checkin_progress.setVisibility(View.GONE);
                                selfCheckin.setVisibility(View.VISIBLE);
                                button_payment_check_in.setVisibility(View.VISIBLE);
                                button_payment_check_in.setText(getString(R.string.check_in_online));

                            } else {
                                self_check_in_loading.setVisibility(View.GONE);
                                self_checkin_progress.setVisibility(View.GONE);
                                selfCheckin.setVisibility(View.GONE);
                                button_payment_check_in.setVisibility(View.GONE);

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(BookAppointmentFourActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(BookAppointmentFourActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                if(mobileOpResult.getResult() == 600 || mobileOpResult.getResult() == 700){
                                    ErrorMessage.getInstance().showErrorYellow(BookAppointmentFourActivity.this, errorMesg);
                                }else{
                                    ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMesg);
                                }

                               //ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMesg);

                            }

                        } else {

                            ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);
                        }

                    } else {

                        ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);
                    }
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    private void apptForCheckIn() {
        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog();

            String companyId = tinyDB.getString(Const.COMPANY_ID);
            String securityToken = new TinyDB(this).getString(Const.TOKEN_KEY);

            ApptValidationForCheckinDTO apptValidationForCheckinDTO =
                    new ApptValidationForCheckinDTO(securityToken, apptId, companyId);

            RestClient.getInstance().apptChecking(apptValidationForCheckinDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    MobileOpResult mobileOpResult = (MobileOpResult) result;

                    if (mobileOpResult != null) {

                        if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                            dialogSuccessCheckin();

                        } else {

                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                if (TextUtil.getEnglish(BookAppointmentFourActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                else if (TextUtil.getArabic(BookAppointmentFourActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }

                            ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMesg);

                        }

                    } else {

                        ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);
                    }
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    void setDoctorDetail() {
        doctorNameTv.setText(doctorName);
        doctorProfession.setText(speciality);

        dateTime.setText(date);

        if (!ValidationHelper.isNullOrEmpty(doctorImageUrl)) {
            Glide.with(this).load(doctorImageUrl).placeholder(R.drawable.doctr).into((ImageView) doctorImage);
        } else {
            if (isSecondary)
                Glide.with(this).load(R.drawable.xray).placeholder(R.drawable.doctr).into((ImageView) doctorImage);
        }
    }

    @Override
    protected int layout() {
        return R.layout.activity_book_appointment_four;
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
                ErrorMessage.getInstance().showSuccessWithoutNavigationGreen(BookAppointmentFourActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameAr() + ".");
            }else{
                ErrorMessage.getInstance().showSuccessWithoutNavigationGreen(BookAppointmentFourActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameEn() + ".");
            }
            visit_reception_layout.setVisibility(View.GONE);
            visit_reception_error_tv.setVisibility(View.GONE);

            showSelfCheckIn();
        } else {
            ErrorMessage.getInstance().showErrorYellow(BookAppointmentFourActivity.this, appointmentEvent.getErrorMSg());
            visit_reception_layout.setVisibility(View.GONE);
            visit_reception_error_tv.setVisibility(View.VISIBLE);
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


    @Override
    public void onBackPressed() {

        if (isChatBotVisible) {
            openChatBotBg(null);
            return;
        }
        super.onBackPressed();
    }

    void apptGetTotalOutstandingInsurance(long apptId, boolean isCheckedInsurance) {

        if (ConnectionUtil.isInetAvailable(this)) {
            TinyDB tinyDB = new TinyDB(this);

            showWaitDialog();
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            String companyId = tinyDB.getString(Const.COMPANY_ID);

            ApptGetTotalOutstandingDTO apptGetTotalOutstandingDTO
                    = new ApptGetTotalOutstandingDTO(securityToken, apptId, isTelemedcine, companyId);
            RestClient.getInstance().apptGetTotalOutstanding(apptGetTotalOutstandingDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    ApptGetTotalOutstandingModel apptGetTotalOutstandingModel = (ApptGetTotalOutstandingModel) result;

                    if (apptGetTotalOutstandingModel != null) {

                        MobileOpResult mobileOpResult = apptGetTotalOutstandingModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                ammountDue = apptGetTotalOutstandingModel.getAmountDue();
                                apptGetTotalOutstandingCalled = true;

                                if (ammountDue < 1) {

                                    if (!isTentative) {
                                        price.setVisibility(View.VISIBLE);
                                    }

                                    price.setText(paidZero);
                                    changeInsurance.setVisibility(View.GONE);

                                    button_payment_check_in.setVisibility(View.VISIBLE);

                                    if (!isTelemedcine) {
                                        button_payment_check_in.setText(getString(R.string.check_in_online));
                                        if(!tentativeHasMRN){
                                            self_check_in_loading.setVisibility(View.VISIBLE);
                                            self_checkin_progress.setVisibility(View.VISIBLE);
                                            validationofCheckIn();
                                            //selfCheckInEligibility();
                                           // selfCheckin.setVisibility(View.VISIBLE);
                                        }else{
                                            selfCheckin.setVisibility(View.GONE);
                                            visit_reception.setVisibility(View.VISIBLE);
                                            button_payment_check_in.setVisibility(View.GONE);
                                        }
                                       // selfCheckin.setVisibility(View.VISIBLE);
                                    } else {
                                        // button_payment_check_in.setText(getString(R.string.check_in_tele));
                                        button_payment_check_in.setVisibility(View.GONE);
                                        selfCheckin.setVisibility(View.GONE);
                                    }

                                    String mrnNo = tinyDB.getString(Const.MRN_NO);

                                    if (ValidationHelper.isNullOrEmpty(mrnNo) || mrnNo.equalsIgnoreCase("0")) {
                                        //for tentative patient hide self check and pay online button
                                        selfCheckin.setVisibility(View.GONE);
                                        button_payment_check_in.setVisibility(View.GONE);
                                    }
                                    String spec = tinyDB.getString(DOCTOR_SPECIALITY);
                                    if (ValidationHelper.isNullOrEmpty(mrnNo) || mrnNo.equalsIgnoreCase("0") ||
                                            spec.equals("Dental") || spec.equals("قسم طب الأسنان") || spec.equals("Dentist") || spec.equals("دكتورالاسنان")) {
                                        if (!isTelemedcine)
                                            button_payment_check_in.setVisibility(View.GONE);
                                    }
                                    confirmAppointment(apptId, isCheckedInsurance, false);

                                } else {

                                    ammountDue = Math.round(ammountDue);

                                    String ammount = new DecimalFormat("#.##").format(ammountDue);
                                    price.setVisibility(View.VISIBLE);
                                    String priceShow = getString(R.string.price) + " " + ammount + " " + getString(R.string.sar);
                                    price.setText(priceShow);
                                    button_payment_check_in.setText(getString(R.string.pay_online));
                                    if (!isTentative) {
                                        changeInsurance.setVisibility(View.VISIBLE);
                                    } else {
                                        changeInsurance.setVisibility(View.GONE);
                                    }

                                    button_payment_check_in.setVisibility(View.VISIBLE);
                                    selfCheckin.setVisibility(View.GONE);

                                }
                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(BookAppointmentFourActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(BookAppointmentFourActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                if(mobileOpResult.getResult() == 600 || mobileOpResult.getResult() == 700){
                                    ErrorMessage.getInstance().showErrorYellow(BookAppointmentFourActivity.this, errorMesg);
                                }else{
                                    ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMesg);
                                }

                               // ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);

                    } else {
                        ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);
                    }
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }

    }

    public void DashBoard(View view) {
        finish();
    }

    @OnClick(R.id.cencel)
    void cancel() {
        showWarningMessage(null, apptId);
    }

    void showWarningMessage(String apptNo, long apptId) {
        try {
            SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            alertDialog.setCancelable(false);
            alertDialog.setTitleText(getString(R.string.my_clininc)).setCancelText(getString(R.string.cancel))
                    .setContentText(getString(R.string.are_you_sure_cancel_the_apptment)).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            alertDialog.dismiss();
                        }
                    }).setConfirmText(getString(R.string.yes)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            alertDialog.dismiss();
                            if (ConnectionUtil.isInetAvailable(BookAppointmentFourActivity.this)) {

                                showWaitDialog();

                                TinyDB tinyDB = new TinyDB(BookAppointmentFourActivity.this);

                                String securityToken = tinyDB.getString(Const.TOKEN_KEY);
                                String companyId = tinyDB.getString(Const.COMPANY_ID);

                                ApptCancelDTO apptCancelDTO = new ApptCancelDTO(securityToken, String.valueOf(apptId), false, companyId);

                                RestClient.getInstance().apptCancel(apptCancelDTO, new OnResultListener() {
                                    @Override
                                    public void onResult(Object result, boolean status, String errorMessage) {

                                        hideWaitDialog();

                                        MobileOpResult mobileOpResult = (MobileOpResult) result;
                                        if (mobileOpResult != null) {

                                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                                NotificationHelper.getInstance().cancelNotification(BookAppointmentFourActivity.this, (int) apptId);
                                                sendBroadCast();
                                       /* Uri deleteUri = null;
                                        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(String.valueOf(apptId)));
                                        int rows = getContentResolver().delete(deleteUri, null, null);
                                        Toast.makeText(BookAppointmentFourActivity.this, "Event deleted", Toast.LENGTH_LONG).show();*/
                                                finish();
                                            } else {
                                                String errorMesg = "";

                                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                                    if (TextUtil.getEnglish(BookAppointmentFourActivity.this))
                                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                                    else if (TextUtil.getArabic(BookAppointmentFourActivity.this))
                                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                                }

                                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                                }

                                                ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMesg);
                                            }
                                        } else {
                                            ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);
                                        }

                                    }
                                });

                            } else {
                                ErrorMessage.getInstance().showWarning(BookAppointmentFourActivity.this, "Internet is not available");
                            }
                        }
                    }).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendBroadCast() {
        Intent intent = new Intent("update_appts");
        // You can also include some extra data.
        intent.putExtra("refresh", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    void writeToCalendar(String title, String comment) {

        try {

            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, milliseconds(date));
            values.put(CalendarContract.Events.DTEND, milliseconds(date));
            values.put(CalendarContract.Events.TITLE, title);
            values.put(CalendarContract.Events.DESCRIPTION, comment);
            values.put(CalendarContract.Events.CALENDAR_ID, 1);

            TimeZone timeZone = TimeZone.getDefault();
            values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

/*
            ContentValues reminder = new ContentValues();
            reminder.put(CalendarContract.Reminders.EVENT_ID, apptId);
            reminder.put(CalendarContract.Reminders.MINUTES, 15);
            reminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            cr.insert(CalendarContract.Reminders.CONTENT_URI, reminder)*/
            ;

            SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
            alertDialog.setTitleText(getString(R.string.my_clininc)).setConfirmText(getString(R.string.ok)).setContentText(
                    getString(R.string.event_added_successfully)
            ).show();

            Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
            btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long milliseconds(String date) {
        //String date_ = date;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm a");
        try {

            Date mDate = sdf.parse(date);

            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }

    @OnClick(R.id.add_calendar)
    void addToCalendarEvent() {
        String title = getString(R.string.appointment_with) + " " + doctorName + " " + getString(R.string.at_my_clinic);
        String msg = " " + tinyDB.getString(Const.PATIENT_NAME) + " " + getString(R.string.has_an_appointment_with) + " " + doctorName + " (" + speciality + ") " + getString(R.string.at) + " " + date +
                getString(R.string.please_remember_to_arrive);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            boolean writeGranted = PermissionUtils.isGranted(this, PermissionEnum.WRITE_CALENDAR);
            boolean readGranted = PermissionUtils.isGranted(this, PermissionEnum.READ_CALENDAR);
            if (!writeGranted && !readGranted) {
                TedPermission.with(this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR)
                        .check();
            } else {
                writeToCalendar(title, msg);
            }
        } else {
            writeToCalendar(title, msg);
        }
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

            try {

                String title = getString(R.string.appointment_with) + doctorName + getString(R.string.at_my_clinic);
                String msg = tinyDB.getString(Const.PATIENT_NAME) + getString(R.string.has_an_appointment_with) + doctorName + " (" + speciality + ") " + getString(R.string.at) + date +
                        getString(R.string.please_remember_to_arrive);
                writeToCalendar(title, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(BookAppointmentFourActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    @OnClick(R.id.self_checkin)
    void SelfCheckin() {
        ArrayList<Object> configsModels = tinyDB.getListObject(Const.ONLINE_CONFIG_KEY, OpsConfigsModel.class);

        boolean accessFineLocation = PermissionUtils.isGranted(this, PermissionEnum.ACCESS_FINE_LOCATION);
        boolean accessCourseLocation = PermissionUtils.isGranted(this, PermissionEnum.ACCESS_COARSE_LOCATION);

       // validationofCheckIn();
        //apptForCheckIn();
        // Check GPS is enabled
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            dialogGPSEnable();
            return;
        }

        checkLocationPermission();
      /*  if (!accessFineLocation || !accessCourseLocation) {
            checkLocationPermission();
        } else {
            requestLocationUpdates();
            *//*final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 1000);*//*
        }*/
   /*     for (Object configModel : configsModels)
        {
            OpsConfigsModel model = (OpsConfigsModel) configModel;

            if(model.getKey().equals("Self Check-in URL")){
       *//*         Intent browserIntent = new Intent(this, BrowserActivity.class);
                browserIntent.putExtra("url", model.getVal());
                startActivity(browserIntent);*//*

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getVal()));
                startActivity(browserIntent);
                break;
            }
        }*/
    }

    IOSDialog gpdEnaleDialog = null;

    private void dialogGPSEnable() {

        gpdEnaleDialog = TextUtil.dialgoue(BookAppointmentFourActivity.this,
                getString(R.string.Kindly_enable_your_location_to_conitinue), getString(R.string.settings), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            gpdEnaleDialog.dismiss();
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        } catch (android.content.ActivityNotFoundException exception) {
                            // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.myclinic.ksa")));
                        }

                    }
                });
    }

    boolean locationRequested = false;
    Handler handler = null;
    private  Runnable runnable;
    public void requestLocationUpdates() {
        try {
           /* showWaitDialog();
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
*/
            showWaitDialog();
            locationRequested = true;
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            handler = new Handler(Looper.getMainLooper());
            runnable = () -> {
                if (locationRequested) {
                    getLastLocation();
                }
            };

            if (handler == null || runnable == null) {
                return;
            }
            handler.postDelayed(runnable, 10000);

        } catch (SecurityException e) {
            hideWaitDialog();
           // Log.e("result", "Lost location request");
        }
    }

    private void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            hideWaitDialog();
            checkLocationPermission();
            return;
        }

        Task<Location> fusedLastLocation = fusedLocationProviderClient.getLastLocation();

        if(fusedLastLocation != null){
            locationRequested = false;

           // Toast.makeText(BookAppointmentFourActivity.this, "fused Last Location called", Toast.LENGTH_LONG).show();
            fusedLastLocation.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    hideWaitDialog();
                    if(task.getResult() != null){
                       // Toast.makeText(BookAppointmentFourActivity.this, "fused Last Location Received", Toast.LENGTH_LONG).show();
                        onNewLocation(task.getResult());
                    }
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideWaitDialog();
                }
            });

           // onNewLocation(fusedLastLocation.getResult());
            // fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {

        boolean accessFineLocation = PermissionUtils.isGranted(this, PermissionEnum.ACCESS_FINE_LOCATION);
        boolean accessCourseLocation = PermissionUtils.isGranted(this, PermissionEnum.ACCESS_COARSE_LOCATION);

        if(!accessCourseLocation || !accessFineLocation){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return false;
        }else{
            requestLocationUpdates();
            return true;
        }

    /*    if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
*/
         /*   // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showLocationDisabledDialog();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }*/
      /*      return false;
        } else {
            requestLocationUpdates();
            return true;
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        requestLocationUpdates();
                        /*LocalBroadcastManager.getInstance(AppointmentDetailActivity.this).registerReceiver(mMessageReceiver,
                                new IntentFilter("location"));

                        FusedLocationSingleton.getInstance().startLocationUpdates();*/
                    }else{
                        requestLocationUpdates();
                    }

                } else {
                    dialogPermissionEnable(getString(R.string.please_enable_location_to_confirm));

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
    IOSDialog permissionEnaleDialog = null;

    private void dialogPermissionEnable(String msg) {

        permissionEnaleDialog = TextUtil.dialgoue(BookAppointmentFourActivity.this,
                msg, getString(R.string.settings), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            permissionEnaleDialog.dismiss();
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } catch (android.content.ActivityNotFoundException exception) {
                            // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.myclinic.ksa")));
                        }

                    }
                });
    }

    IOSDialog locationDisabledDialog = null;

    private void showLocationDisabledDialog() {
        locationDisabledDialog = TextUtil.dialgoue(BookAppointmentFourActivity.this,
                getString(R.string.Kindly_enable_your_location_to_conitinue), getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            locationDisabledDialog.dismiss();
                            ActivityCompat.requestPermissions(BookAppointmentFourActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        } catch (android.content.ActivityNotFoundException exception) {
                            // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.myclinic.ksa")));
                        }

                    }
                });
    }

    @OnClick(R.id.button_payment_check_in)
    void payStageOne() {

        String exDate = LocaleDateHelper.convertDateStringFormat(date, "EEEE, dd MMMM yyyy hh:mm a", "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        if (!isTelemedcine) {
            if (LocaleDateHelper.expiredAptTime(exDate)) {
                showCheckinDialog();
                return;
            }
        }

        String buttonStr = button_payment_check_in.getText().toString().trim();

        String checkInStr = getString(R.string.pay_online).trim();

        String checkInStrKiosk = getString(R.string.check_in_online).trim();

        if (isTelemedcine && buttonStr.equalsIgnoreCase(checkInStr.trim())) {

            apptGetTotalOutstanding(apptId, isTelemedcine, false);

            return;
        }

        if (!isTelemedcine && buttonStr.equalsIgnoreCase(checkInStrKiosk)) {
            //discuss in meeting with shahbaz and nagwa to comment all the apptpayment from all places except payment stage 2
            if (ammountDue < 1 && apptGetTotalOutstandingCalled) {
                confirmAppointment(apptId, false, true);
                return;
            }

            Intent intent = new Intent(this, BoardingPassActivity.class);
            intent.putExtra(Const.DOCTOR_NAME, doctorName);
            intent.putExtra(Const.DOCTOR_SPECIALITY, speciality);
            intent.putExtra(Const.DATE, date);
            intent.putExtra(Const.APPT_ID, apptId);
            startActivity(intent);

            return;
        }

        if (ammountDue > 0) {

            String ammount = new DecimalFormat("#.##").format(ammountDue);
            String mesg = getString(R.string.the_cost_for_this_appt_will_be) + " " + ammount + " " + getString(R.string.sar) + " " + getString(R.string.you_will_need_a_credit_or_debit_card);
            SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            alertDialog.setCancelable(false);
            alertDialog.setTitleText(getString(R.string.my_clininc)).setCancelText(getString(R.string.cancel))
                    .setContentText(mesg).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            alertDialog.dismiss();
                        }
                    }).setConfirmText(getString(R.string._continue)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            alertDialog.dismiss();

                            if (ConnectionUtil.isInetAvailable(BookAppointmentFourActivity.this)) {
                                showWaitDialog();
                                String companyId = tinyDB.getString(Const.COMPANY_ID);

                                String customerEmail = tinyDB.getString(Const.EMAIL_KEY);
                                String apptIdStr = String.valueOf(apptId);
                                String ORegId = tinyDB.getString(Const.OREGID_KEY);
                                String ammount = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US)).format(ammountDue);

                                //ammount = ammount + "0";
                                PayStageOneDTO payStageOneDTO =
                                        new PayStageOneDTO(ammount, customerEmail, apptIdStr, companyId);

                                RestClient.getInstance().payStage1(payStageOneDTO, new OnResultListener() {
                                    @Override
                                    public void onResult(Object result, boolean status, String errorMessage) {

                                        hideWaitDialog();

                                        PayStageOneModel payStageOneModel = (PayStageOneModel) result;

                                        if (payStageOneModel != null) {

                                            MobileOpResult mobileOpResult = payStageOneModel.getMobileOpResult();

                                            if (mobileOpResult != null) {

                                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                                    String checkOutId = payStageOneModel.getResultVal();

                                                    //  Intent intent = new Intent(BookAppointmentFourActivity.this, CustomUIActivity.class);
                                     /*       Intent intent = new Intent(BookAppointmentFourActivity.this, CardListActivity.class);
                                            intent.putExtra("amount", String.valueOf(ammountDue));
                                            intent.putExtra(Const.APPT_ID, apptId);
                                            intent.putExtra(Const.CHECK_OUT_ID, checkOutId);
                                            startActivityForResult(intent, 500);*/

                                                    getCards(apptId, checkOutId, String.valueOf(ammountDue));
                                                } else {
                                                    String errorMesg = "";

                                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                                        if (TextUtil.getEnglish(BookAppointmentFourActivity.this))
                                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                                        else if (TextUtil.getArabic(BookAppointmentFourActivity.this))
                                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                                    }

                                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                                    }

                                                    ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMesg);

                                                }

                                            } else {
                                                hideWaitDialog();
                                                ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);
                                            }

                                        } else {
                                            hideWaitDialog();
                                            ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);
                                        }
                                    }
                                });

                            } else {
                                ErrorMessage.getInstance().showWarning(BookAppointmentFourActivity.this, getString(R.string.internet_connection));
                            }

                        }
                    }).show();
        }


    }

    IOSDialog iosDialog1 = null;

    private void showCheckinDialog() {
        iosDialog1 = TextUtil.dialgoue(BookAppointmentFourActivity.this, getString(R.string.check_in_eligible), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iosDialog1.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 500 && resultCode == RESULT_OK) {

            if (data != null) {
                boolean isPaid = data.getBooleanExtra(Const.CONFIRM, false);

                if (isPaid) {
                    changeInsurance.setVisibility(View.GONE);
                    ammountDue = Math.round(ammountDue);

                    String ammount = new DecimalFormat("#.##").format(ammountDue);

                    String priceShow = getString(R.string.paid) + ": " + ammount + " " + getString(R.string.sar);

                    price.setVisibility(View.VISIBLE);
                    price.setText(priceShow);
                    if (!isTelemedcine) {
                        button_payment_check_in.setText(getString(R.string.check_in_online));
                        //getPatientData(true, true, false);
                        self_check_in_loading.setVisibility(View.VISIBLE);
                        self_checkin_progress.setVisibility(View.VISIBLE);
                        validationofCheckIn();
                        //selfCheckInEligibility();
                      //  selfCheckin.setVisibility(View.VISIBLE);
                    } else {
                        //button_payment_check_in.setText(getString(R.string.check_in_tele));
                        button_payment_check_in.setVisibility(View.GONE);
                        selfCheckin.setVisibility(View.GONE);
                    }

                    // button_payment_check_in.setVisibility(View.GONE);
                    String mrnNo = tinyDB.getString(Const.MRN_NO);
                    String spec = tinyDB.getString(DOCTOR_SPECIALITY);
                    if (ValidationHelper.isNullOrEmpty(mrnNo) || mrnNo.equalsIgnoreCase("0") ||
                            spec.equals("Dental") || spec.equals("قسم طب الأسنان") || spec.equals("Dentist") || spec.equals("دكتورالاسنان")) {
                        if (!isTelemedcine)
                            button_payment_check_in.setVisibility(View.GONE);
                    }
                } else {
                    if (!isTentative) {
                        changeInsurance.setVisibility(View.VISIBLE);
                    } else {
                        changeInsurance.setVisibility(View.GONE);
                    }

                }
            }

        } else if (requestCode == 113 && resultCode == RESULT_OK) {

            if (data != null) {

                boolean isInsurance = data.getBooleanExtra(Const.IS_INSURANCE_KEY, false);
                long insuranceId = data.getLongExtra(Const.INSURANCE_ID, 0);
                double telePrice = data.getDoubleExtra(Const.TELE_PRICE, 0);

                updateInsurance(insuranceId);
            }
        }
    }

    void updateInsurance(long insuranceId) {
        try {

            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            String companyId = new TinyDB(this).getString(Const.COMPANY_ID);

            UpdateInsuranceDTO updateInsuranceByIdDTO = new UpdateInsuranceDTO(securityToken,
                    String.valueOf(apptId), insuranceId, companyId);

            if (ConnectionUtil.isInetAvailable(this)) {

                showWaitDialog();

                RestClient.getInstance().ApptInsUpdate(updateInsuranceByIdDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        hideWaitDialog();

                        MobileOpResult mobileOpResult = (MobileOpResult) result;

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                showInsuranceSuccessDialog();
                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(BookAppointmentFourActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(BookAppointmentFourActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);

                    }
                });

            } else {
                ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showInsuranceSuccessDialog() {
        SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        alertDialog.setCancelable(false);
        alertDialog.setTitleText(getString(R.string.my_clininc))
                .setContentText(getString(R.string.change_insurance_success))
                .setConfirmText(getString(R.string.ok)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        alertDialog.dismiss();
                        apptGetTotalOutstandingInsurance(apptId, true);
                    }
                }).show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }


    @OnClick(R.id.changeInsurance)
    void setChangeInsurance() {
        getInsurance();
    }


    void getInsurance() {
        try {
            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);

            GetInsuranceByIdDTO getInsuranceByIdDTO = new GetInsuranceByIdDTO(securityToken, String.valueOf(patientId));

            if (ConnectionUtil.isInetAvailable(this)) {

                showWaitDialog();

                RestClient.getInstance().getInsuranceByProfileId(getInsuranceByIdDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        hideWaitDialog();

                        GetInsuranceModel getInsuranceModel = (GetInsuranceModel) result;

                        if (getInsuranceModel != null) {

                            MobileOpResult mobileOpResult = getInsuranceModel.getMobileOpResult();

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                    ArrayList<InsuranceCardModel> getInsuranceModelsList = getInsuranceModel.getInsuranceCardModels();

                                    if (ValidationHelper.isNullOrEmpty(getInsuranceModelsList)) {
                                        showInsuranceDialog();
                                        return;
                                    }

                                    Intent intent = new Intent(BookAppointmentFourActivity.this, InsuranceCardCashCardActivity.class);
                                    intent.putExtra("insuranceCard", getInsuranceModelsList);
                                    intent.putExtra("noinsurance", specialityId);
                                    intent.putExtra("doctorId", doctorId);
                                    intent.putExtra(Const.ISTELEMEDICINE_KEY, isTelemedcine);
                                    intent.putExtra(Const.Is_CHANGE_INSURANCE, true);
                                    startActivityForResult(intent, 113);
                                    //  }

                                } else {


                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(BookAppointmentFourActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(BookAppointmentFourActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMesg);
                                }

                            } else
                                ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);

                        } else {
                            ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);
                        }
                    }
                });

            } else {
                ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void showInsuranceDialog() {
        try {
           /* final Dialog dialog = new Dialog(this, R.style.NewDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.myclinic_dialogue);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            RegularTextView dontRemindAgain = (RegularTextView) dialog.findViewById(R.id.dont_remind);

            LightTextView msg = (LightTextView) dialog.findViewById(R.id.add_sugar);

            msg.setText(getString(R.string.change_insurance_msg));
            RegularTextView dialogButton = dialog.findViewById(R.id.cancel);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();*/


            String cancel1 = getString(R.string.no);
            String yes = getString(R.string.yes);

            IOSDialog dialogView = new IOSDialog.Builder(this)
                    .setTitle(R.string.my_clininc)
                    .setMessage(getString(R.string.change_insurance_msg))
                    .setPositiveButton(cancel1, (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setNegativeButton(yes, (dialog, which) -> {
                        dialog.dismiss();
                        getPatientData(false, false, false);
                    }).show();

            String font = "GothamMedium.otf";
            String font_bold = "GothamBold.otf";

            Button btnConfirm = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.confirm_btn);
            btnConfirm.setText(cancel1);

            Button cancel = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.cancel_btn);
            cancel.setText(yes);

            TextView tvTitle = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.title);
            TextView tvMessage = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.message);
            Typeface tf = Typeface.createFromAsset(this.getAssets(), font);
            Typeface tf_title = Typeface.createFromAsset(this.getAssets(), font_bold);
            btnConfirm.setTypeface(tf);
            btnConfirm.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            cancel.setTypeface(tf);
            cancel.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            tvTitle.setTypeface(tf_title);
            tvMessage.setTypeface(tf);
            tvMessage.setTextColor(this.getResources().getColor(R.color.black));
        } catch (Exception e) {

        }
    }

    void getPatientData(boolean tentativeCheck, boolean isFromPayment, boolean boardingPass) {
        try {
            if (ConnectionUtil.isInetAvailable(this)) {

                showWaitDialog();

                if (tinyDB != null) {

                    long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
                    String securityToken = tinyDB.getString(Const.TOKEN_KEY);
                    String oRegId = tinyDB.getString(Const.OREGID_KEY);

                    GetProfileDTO getProfileDTO = new GetProfileDTO(String.valueOf(patientId), securityToken, oRegId);
                    RestClient.getInstance().profileGetById(getProfileDTO, new OnResultListener() {
                        @Override
                        public void onResult(Object result, boolean status, String errorMessage) {

                            hideWaitDialog();

                            ProfileUpdateModel profileUpdateModel = (ProfileUpdateModel) result;

                            if (profileUpdateModel != null) {
                                INSURANCE_IMAGES = "";

                                MobileOpResult mobileOpResult = profileUpdateModel.getMobileOpResult();

                                if (mobileOpResult != null) {

                                    if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                        PatientRegistrationUpdate patientRegistrationUpdate = profileUpdateModel.getPatientRegistrationUpdate();

                                        if (patientRegistrationUpdate != null) {
                                            try {
                                                if(tentativeCheck){
                                                    if(patientRegistrationUpdate.isTentativePatient()){
                                                        hideCheckInBtn(boardingPass);

                                                    }else{
                                                        if(boardingPass){
                                                            Intent intent = new Intent(BookAppointmentFourActivity.this, BoardingPassActivity.class);
                                                            intent.putExtra(Const.DOCTOR_NAME, doctorName);
                                                            intent.putExtra(Const.DOCTOR_SPECIALITY, speciality);
                                                            intent.putExtra(Const.DATE, date);
                                                            intent.putExtra(Const.APPT_ID, apptId);
                                                            startActivity(intent);
                                                        }
                                                        else if(isFromPayment){
                                                          /*  self_check_in_loading.setVisibility(View.VISIBLE);
                                                            self_checkin_progress.setVisibility(View.VISIBLE);
                                                            validationofCheckIn();*/
                                                            //selfCheckInEligibility();
                                                           // selfCheckin.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                }else{
                                                if (patientRegistrationUpdate.getInsurance() != null) {
                                                    INSURANCE_IMAGES = patientRegistrationUpdate.getInsurance().getInsuranceCardScanBase64();
                                                    patientRegistrationUpdate.getInsurance().setInsuranceCardScanBase64("");
                                                }

                                                Intent intent = new Intent(BookAppointmentFourActivity.this, AddInsuranceActivity.class);
                                                intent.putExtra(PATIENT_UPDATE_KEY, patientRegistrationUpdate);
                                                startActivityForResult(intent, 99);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    } else {
                                        String errorMesg = "";

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                            if(mobileOpResult.getBusinessErrorMessageEn().
                                                    contains("verified")){
                                                button_payment_check_in.setVisibility(View.GONE);
                                            }
                                            if (TextUtil.getEnglish(BookAppointmentFourActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                            else if (TextUtil.getArabic(BookAppointmentFourActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";

                                        }

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                            errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                        }

                                        if(mobileOpResult.getResult() == 600 || mobileOpResult.getResult() == 700){
                                            ErrorMessage.getInstance().showErrorYellow(BookAppointmentFourActivity.this, errorMesg);
                                        }else{
                                            ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMesg);
                                        }

                                       // ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMesg);
                                    }
                                } else
                                    ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);
                            } else
                                ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);
                        }
                    });
                }
            } else {
                ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean tentativeHasMRN = false;
    private void hideCheckInBtn(boolean boardingPass) {
        String mrnNo = tinyDB.getString(Const.MRN_NO);

        String btnText =  button_payment_check_in.getText().toString().trim();
        if (!ValidationHelper.isNullOrEmpty(mrnNo) && !mrnNo.equalsIgnoreCase("0") && !btnText.equals(getString(R.string.pay_online))) {
            //for tentative patient we hide self check and pay online button
            //but in this case if patient has mrn but is tentative then we will also hide check in btn
            tentativeHasMRN = true;
            selfCheckin.setVisibility(View.GONE);
            button_payment_check_in.setVisibility(View.GONE);
          //  visit_reception.setVisibility(View.VISIBLE);

            /*if(boardingPass){
                Intent intent = new Intent(BookAppointmentFourActivity.this, BoardingPassActivity.class);
                intent.putExtra(Const.DOCTOR_NAME, doctorName);
                intent.putExtra(Const.DOCTOR_SPECIALITY, speciality);
                intent.putExtra(Const.DATE, date);
                intent.putExtra(Const.APPT_ID, apptId);
                startActivity(intent);
            }*/
        }

        else if (boardingPass){
            Intent intent = new Intent(BookAppointmentFourActivity.this, BoardingPassActivity.class);
            intent.putExtra(Const.DOCTOR_NAME, doctorName);
            intent.putExtra(Const.DOCTOR_SPECIALITY, speciality);
            intent.putExtra(Const.DATE, date);
            intent.putExtra(Const.APPT_ID, apptId);
            startActivity(intent);
        }
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    void confirmAppointment(long mApptId, boolean isChangeInsurance, boolean isBoardingPass) {

        showWaitDialog();

        String securityToken = new TinyDB(this).getString(Const.TOKEN_KEY);
        String companyId = new TinyDB(this).getString(Const.COMPANY_ID);

        if (!ValidationHelper.isNullOrEmpty(securityToken) && mApptId != 0) {

            ApptPaymentDTO apptPaymentDTO = new ApptPaymentDTO(securityToken, mApptId, companyId);

            RestClient.getInstance().apptPayment(apptPaymentDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    MobileOpResult mobileOpResult = (MobileOpResult) result;

                    if (mobileOpResult != null) {

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                if (!isChangeInsurance) {
                                    if(isBoardingPass){
                                        Intent intent = new Intent(BookAppointmentFourActivity.this, BoardingPassActivity.class);
                                        intent.putExtra(Const.DOCTOR_NAME, doctorName);
                                        intent.putExtra(Const.DOCTOR_SPECIALITY, speciality);
                                        intent.putExtra(Const.DATE, date);
                                        intent.putExtra(Const.APPT_ID, apptId);
                                        startActivity(intent);
                                    }

                                   // getPatientData(true, false, isBoardingPass);

                                }

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(BookAppointmentFourActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(BookAppointmentFourActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);

                    } else {
                        ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);
                    }
                }
            });
        }
    }

    void transformView() {
        if (TextUtil.getArabic(this)) {
            toolbar_left_iv.setRotation(180);
            button_payment_check_in.setCompoundDrawablesWithIntrinsicBounds(R.drawable.grey_left_arrow, 0, 0, 0);
            selfCheckin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.grey_left_arrow, 0, 0, 0);
            changeInsurance.setCompoundDrawablesWithIntrinsicBounds(R.drawable.grey_left_arrow, 0, 0, 0);
            cencel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.grey_left_arrow, 0, 0, 0);
            backtoDash.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            add_calendar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.grey_left_arrow, 0, 0, 0);
        }
    }

    void apptGetTotalOutstanding(long apptId, boolean isTelemedicine, boolean isCheckJustDue) {

        if (ConnectionUtil.isInetAvailable(this)) {
            TinyDB tinyDB = new TinyDB(this);

            showWaitDialog();
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            String companyId = tinyDB.getString(Const.COMPANY_ID);
            ApptGetTotalOutstandingDTO apptGetTotalOutstandingDTO
                    = new ApptGetTotalOutstandingDTO(securityToken, apptId, isTelemedicine, companyId);
            RestClient.getInstance().apptGetTotalOutstanding(apptGetTotalOutstandingDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    ApptGetTotalOutstandingModel apptGetTotalOutstandingModel = (ApptGetTotalOutstandingModel) result;

                    if (apptGetTotalOutstandingModel != null) {

                        MobileOpResult mobileOpResult = apptGetTotalOutstandingModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                double ammountDue = apptGetTotalOutstandingModel.getAmountDue();
                                ammountDue = Math.round(ammountDue);

                                if (isCheckJustDue) {
                                    double telePrice = ammountDue;
                                    telePrice = Math.round(telePrice);

                                    String ammount = new DecimalFormat("#.##").format(telePrice);

                                    paidZero = getResources().getString(R.string.paid) + ": " + ammount + " " + getString(R.string.sar);
                                    price.setVisibility(View.VISIBLE);
                                    price.setText(paidZero);
                                    changeInsurance.setVisibility(View.GONE);

                                    if (!isTelemedcine) {
                                        button_payment_check_in.setText(getString(R.string.check_in_online));
                                        if(!tentativeHasMRN){
                                            self_check_in_loading.setVisibility(View.VISIBLE);
                                            self_checkin_progress.setVisibility(View.VISIBLE);
                                            validationofCheckIn();
                                           // selfCheckInEligibility();
                                           // selfCheckin.setVisibility(View.VISIBLE);
                                        }else{
                                            selfCheckin.setVisibility(View.GONE);
                                            button_payment_check_in.setVisibility(View.GONE);
                                        }
                                      //  selfCheckin.setVisibility(View.VISIBLE);
                                    } else {
                                        if (telePrice > 0) {
                                            button_payment_check_in.setVisibility(View.VISIBLE);
                                            button_payment_check_in.setText(getString(R.string.pay_online));
                                        } else {
                                            button_payment_check_in.setVisibility(View.GONE);
                                        }

                                        selfCheckin.setVisibility(View.GONE);
                                    }

                                    //commented while working on self progress blue view on validation
                                  /*  if (!isTelemedcine) {
                                        if(!tentativeHasMRN){
                                            selfCheckin.setVisibility(View.VISIBLE);
                                        }else{
                                            selfCheckin.setVisibility(View.GONE);
                                            button_payment_check_in.setVisibility(View.GONE);
                                        }
                                      //  selfCheckin.setVisibility(View.VISIBLE);
                                    } else {
                                        selfCheckin.setVisibility(View.GONE);
                                    }*/
                                } else {
                                    if (ammountDue > 0) {
                                        dialgoue(apptId, ammountDue);
                                    } else {
                                        return;
                                    }
                                }
                             /*   if (ammountDue > 0) {
                                    dialgoue(apptId, ammountDue);
                                } else {
                                    return;
                                   *//* Intent intent = new Intent(BookAppointmentFourActivity.this, WaitingRoomActivity.class);
                                    intent.putExtra(Const.DOCTOR_NAME, doctorName);
                                    intent.putExtra(Const.DOCTOR_IMAGE_URL, doctorImageUrl);
                                    intent.putExtra(Const.DOCTOR_SPECIALITY, speciality);
                                    intent.putExtra(Const.DATE, date);
                                    intent.putExtra(Const.APP_NO, apptNo);
                                    intent.putExtra(Const.APPT_ID, apptId);
                                    startActivityForResult(intent, 1000);*//*
                                }*/
                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(BookAppointmentFourActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(BookAppointmentFourActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                if(mobileOpResult.getResult() == 600 || mobileOpResult.getResult() == 700){
                                    ErrorMessage.getInstance().showErrorYellow(BookAppointmentFourActivity.this, errorMesg);
                                }else{
                                    ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMesg);
                                }
                               // ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMesg);

                            }

                        } else
                            ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);

                    } else {
                        ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);
                    }
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, "Internet is not available");
        }
    }

    void dialgoue(long apptId, double ammountDue) {

        String ammount = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US)).format(ammountDue);

        String mesg = getString(R.string.the_cost_for_this_appt_will_be) + " " + ammount + " " + getString(R.string.sar) + " " + getString(R.string.you_will_need_a_credit_or_debit_card);
        SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setCancelable(false);
        alertDialog.setTitleText(getString(R.string.my_clininc)).setCancelText(getString(R.string.cancel))
                .setContentText(mesg).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        alertDialog.dismiss();
                    }
                }).setConfirmText(getString(R.string._continue)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        alertDialog.dismiss();
                        if (ConnectionUtil.isInetAvailable(BookAppointmentFourActivity.this)) {
                            payStageOne(apptId, ammountDue);
                        } else {
                            ErrorMessage.getInstance().showWarning(BookAppointmentFourActivity.this, getString(R.string.internet_connection));
                        }
                    }
                }).show();
    }

    void payStageOne(long apptId, double ammountDue) {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            if (ammountDue > 0) {
                String companyId = tinyDB.getString(Const.COMPANY_ID);
                String customerEmail = tinyDB.getString(Const.EMAIL_KEY);
                String apptIdStr = String.valueOf(apptId);
                String ORegId = tinyDB.getString(Const.OREGID_KEY);
                String ammount = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US)).format(ammountDue);

                PayStageOneDTO payStageOneDTO =
                        new PayStageOneDTO(ammount, customerEmail, apptIdStr, companyId);

                RestClient.getInstance().payStage1(payStageOneDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        hideWaitDialog();

                        PayStageOneModel payStageOneModel = (PayStageOneModel) result;

                        if (payStageOneModel != null) {

                            MobileOpResult mobileOpResult = payStageOneModel.getMobileOpResult();

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                    String checkOutId = payStageOneModel.getResultVal();

                                    //  Intent intent = new Intent(BookAppointmentFourActivity.this, CustomUIActivity.class);
                                   /* Intent intent = new Intent(BookAppointmentFourActivity.this, CardListActivity.class);
                                    intent.putExtra("amount", String.valueOf(ammountDue));
                                    intent.putExtra(Const.APPT_ID, apptId);
                                    intent.putExtra(Const.CHECK_OUT_ID, checkOutId);
                                    startActivityForResult(intent, 500);*/

                                    getCards(apptId, checkOutId, String.valueOf(ammountDue));

                                } else {

                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(BookAppointmentFourActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(BookAppointmentFourActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMesg);
                                }

                            } else {
                                ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);
                            }

                        } else {
                            ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);
                        }
                    }
                });
            }
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }

    }

    void getCards(long apptId, String checkOutId, String mAmount) {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();
            TinyDB tinyDB = new TinyDB(this);

            String oRegId = tinyDB.getString(OREGID_KEY);
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            long OregIdLong = Long.parseLong(oRegId);

            CardDTO cardDTO = new CardDTO(securityToken, OregIdLong);

            RestClient.getInstance().paymentCardById(cardDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    CardsListModel payStageOneModel = (CardsListModel) result;

                    if (payStageOneModel != null) {

                        MobileOpResult mobileOpResult = payStageOneModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                ArrayList<CardModel> cards = payStageOneModel.getCardsModel();

                                if (cards.isEmpty()) {
                                    //   Intent intent = new Intent(AppointmentDetailActivity.this, CustomUIActivity.class);
                                    Intent intent = new Intent(BookAppointmentFourActivity.this, CustomUIActivity.class);
                                    intent.putExtra("amount", mAmount);
                                    intent.putExtra(Const.APPT_ID, apptId);
                                    //   intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcine);
                                    // intent.putExtra(SPECIALITY_ID, specialityId);
                                    //  intent.putExtra(DOCTOR_ID, doctorId);
                                    //   intent.putExtra(DATE, date);
                                    //   intent.putExtra(Const.APPT_BOOK_TYPE, apptBookType);
                                    intent.putExtra(Const.IS_FROM_CARD_LISTING, false);
                                    intent.putExtra(Const.CHECK_OUT_ID, checkOutId);
                                    startActivityForResult(intent, 500);
                                } else {
                                    //   Intent intent = new Intent(AppointmentDetailActivity.this, CustomUIActivity.class);
                                    Intent intent = new Intent(BookAppointmentFourActivity.this, CardListActivity.class);
                                    intent.putExtra("amount", mAmount);
                                    intent.putExtra(Const.APPT_ID, apptId);
                                    //   intent.putExtra(SPECIALITY_ID, specialityId);
                                    //   intent.putExtra(DOCTOR_ID, doctorId);
                                    //   intent.putExtra(DATE, date);
                                    //   intent.putExtra(Const.APPT_BOOK_TYPE, apptBookType);
                                    //   intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcine);
                                    intent.putExtra(Const.CHECK_OUT_ID, checkOutId);
                                    startActivityForResult(intent, 500);
                                }


                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(BookAppointmentFourActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(BookAppointmentFourActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMesg);

                            }

                        } else {

                            ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);
                        }

                    } else {

                        ErrorMessage.getInstance().showError(BookAppointmentFourActivity.this, errorMessage);
                    }
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }

    }
}
