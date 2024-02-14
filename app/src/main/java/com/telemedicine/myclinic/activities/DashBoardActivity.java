package com.telemedicine.myclinic.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.telemedicine.myclinic.App;
import com.telemedicine.myclinic.activities.profile.AddProfileActivity;
import com.telemedicine.myclinic.adapters.AdapterDashboardUpcomingAppointments;
import com.telemedicine.myclinic.adapters.AdapterUpcomingAppointments;
import com.telemedicine.myclinic.adapters.BannersAdapter;
import com.telemedicine.myclinic.adapters.DoctorsProfileAdapter;
import com.telemedicine.myclinic.adapters.HomeUpcomingAdapter;
import com.telemedicine.myclinic.adapters.SwitchDoctorsAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.fragments.CompanySelectionFragment;
import com.telemedicine.myclinic.hyperpay.CustomUIActivity;
import com.telemedicine.myclinic.injection.ViewModelFactory;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.NotificationDTO;
import com.telemedicine.myclinic.models.NotificationMiniModel;
import com.telemedicine.myclinic.models.NotificationModel;
import com.telemedicine.myclinic.models.appointments.AppointmentsDTO;
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel;
import com.telemedicine.myclinic.models.appointments.ApptsModel;
import com.telemedicine.myclinic.models.company.Company;
import com.telemedicine.myclinic.models.dashboardModels.ApptGetFollowUpsModel;
import com.telemedicine.myclinic.models.dashboardModels.ApptGetSummaryDTO;
import com.telemedicine.myclinic.models.dashboardModels.ApptGetSummaryModel;
import com.telemedicine.myclinic.models.dashboardModels.ApptMinisModel;
import com.telemedicine.myclinic.models.dashboardModels.MarketingClickableUrlsModel;
import com.telemedicine.myclinic.models.logonmodels.LogonDTO;
import com.telemedicine.myclinic.models.logonmodels.LogonModel;
import com.telemedicine.myclinic.models.logonmodels.SecurityToken;
import com.telemedicine.myclinic.models.patientMiniModels.InsuranceCardModel;
import com.telemedicine.myclinic.models.patientMiniModels.PatientsMiniModel;
import com.telemedicine.myclinic.models.profileUpdate.GetProfileDTO;
import com.telemedicine.myclinic.models.profileUpdate.PatientRegistrationUpdate;
import com.telemedicine.myclinic.models.profileUpdate.ProfileUpdateModel;
import com.telemedicine.myclinic.models.profilecreatoinmodels.SecurityTokenDTO;
import com.telemedicine.myclinic.myapplication.ApptTermsConditionsActivity;
import com.telemedicine.myclinic.myapplication.BuildConfig;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.AppointmentEvent;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.viewmodels.DashboardViewModel;
import com.telemedicine.myclinic.views.ArabicRegularTextView;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.webservices.RestClient;
import com.viewpagerindicator.CirclePageIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.telemedicine.myclinic.util.Const.APPT_ID;
import static com.telemedicine.myclinic.util.Const.COMPANY_NAME_AR;
import static com.telemedicine.myclinic.util.Const.COMPANY_NAME_EN;
import static com.telemedicine.myclinic.util.Const.DATE;
import static com.telemedicine.myclinic.util.Const.DATE_OF_BIRTH;
import static com.telemedicine.myclinic.util.Const.DOCTOR_ID;
import static com.telemedicine.myclinic.util.Const.DOCTOR_IMAGE_URL;
import static com.telemedicine.myclinic.util.Const.DOCTOR_NAME;
import static com.telemedicine.myclinic.util.Const.DOCTOR_SPECIALITY;
import static com.telemedicine.myclinic.util.Const.EMAIL_KEY;
import static com.telemedicine.myclinic.util.Const.EXISTING_PATIENT;
import static com.telemedicine.myclinic.util.Const.FIRST_NAME;
import static com.telemedicine.myclinic.util.Const.GENDER;
import static com.telemedicine.myclinic.util.Const.INSURANCE_IMAGES;
import static com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY;
import static com.telemedicine.myclinic.util.Const.IS_CENTRE_OF_EXCELLENCE;
import static com.telemedicine.myclinic.util.Const.IS_FROM_DASHBOARD;
import static com.telemedicine.myclinic.util.Const.IS_FROM_SWITCH_PATIENT;
import static com.telemedicine.myclinic.util.Const.IS_LAB;
import static com.telemedicine.myclinic.util.Const.IS_QUICK_REG;
import static com.telemedicine.myclinic.util.Const.IS_TENT;
import static com.telemedicine.myclinic.util.Const.LAST_NAME;
import static com.telemedicine.myclinic.util.Const.LOGGED_IN_MODE;
import static com.telemedicine.myclinic.util.Const.MIDDLE_NAME;
import static com.telemedicine.myclinic.util.Const.OREGID_KEY;
import static com.telemedicine.myclinic.util.Const.PASSWORD_KEY;
import static com.telemedicine.myclinic.util.Const.PAST_COUNT;
import static com.telemedicine.myclinic.util.Const.PATIENT_GENDER;
import static com.telemedicine.myclinic.util.Const.PATIENT_NAME;
import static com.telemedicine.myclinic.util.Const.PATIENT_UPDATE_KEY;
import static com.telemedicine.myclinic.util.Const.SERVICE_DATE;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_ID;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_NAME_AR;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_NAME_EN;
import static com.telemedicine.myclinic.util.Const.TMPASSWORD_KEY;
import static com.telemedicine.myclinic.util.Const.TMUSERNAME_KEY;
import static com.telemedicine.myclinic.util.Const.TOKEN_EXPIRY_KEY;
import static com.telemedicine.myclinic.util.Const.TOKEN_KEY;
import static com.telemedicine.myclinic.util.Const.UPCOMING_COUNT;

public class DashBoardActivity extends BaseActivity implements SwitchDoctorsAdapter.OnCardClickListner,
        CompanySelectionFragment.OnCompanySelectListener, AdapterUpcomingAppointments.OnCardClickListner {


    @BindView(R.id.pager)
    ViewPager mViewPager;

    @BindView(R.id.indicator)
    CirclePageIndicator indicator;

    @BindView(R.id.reports_trasparent_layout)
    ConstraintLayout reports_trasparent_layout;

    @BindView(R.id.book_now_trasparent_layout)
    ConstraintLayout book_now_trasparent_layout;

    @BindView(R.id.patientRId)
    BoldTextView patientRId;

    @BindView(R.id.chat_bot_bg)
    RelativeLayout chatBotBg;


    @BindView(R.id.placeholder)
    BoldTextView placeholder;

    @BindView(R.id.chevron_right)
    ImageView chevron_right;

    @BindView(R.id.chevron_left)
    ImageView chevron_left;

    @BindView(R.id.upcoming_apts_recycler_view)
    RecyclerView upcomingApptsRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.progress_bar_upcoming)
    ProgressBar progress_bar_upcoming;


    @BindView(R.id.chatBot_rounded)
    View chatBot_rounded;

    @BindView(R.id.chat_bot_web_view)
    WebView chatBotWebView;

    @BindView(R.id.switch_container)
    LinearLayout switchProfile;

    @BindView(R.id.switch_profile_recycler)
    RecyclerView switchProfileRecycler;

    @BindView(R.id.switch_profile_btn)
    LightButton switch_profile_btn;

    @BindView(R.id.book_now_btn)
    LightButton book_now_btn;

    @BindView(R.id.book_now_btn_clinic)
    LightButton book_now_btn_clinic;

    @BindView(R.id.upcoming_count)
    RegularTextView upcomingCountsEdt;

    @BindView(R.id.past_count)
    RegularTextView pastCountEdt;

    @BindView(R.id.availableFollowUps)
    BoldTextView availableFollowUps;

    @BindView(R.id.user_name)
    RegularTextView userName;

    @BindView(R.id.user_image)
    CircleImageView userImage;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.centerOfExcellency)
    RelativeLayout centerOfExcellency;

    @BindView(R.id.toolbar_right_iv)
    ImageView toolbar_right_iv_back;

    @BindView(R.id.notification_view)
    ConstraintLayout notification_view;

    @BindView(R.id.notification_alert_view)
    TextView notification_alert_view;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv_refresh;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.english)
    RegularTextView english;

    @BindView(R.id.arabic)
    ArabicRegularTextView arabic;

    @BindView(R.id.eng_ar_btns_container)
    LinearLayout eng_ar_btns_container;

    @BindView(R.id.switch_title)
    RegularTextView switch_title;

    @BindView(R.id.upcoming_title)
    RegularTextView upcoming_title;

    @BindView(R.id.past_title)
    RegularTextView past_title;

    @BindView(R.id.lab_report_title)
    RegularTextView lab_report_title;

    @BindView(R.id.rad_report_title)
    RegularTextView rad_report_title;

    @BindView(R.id.centerOfExcellencyTxt)
    RegularTextView centerOfExcellencyTxt;

    @BindView(R.id.my_acount_btn)
    LightButton my_acount_btn;

    @BindView(R.id.add_patient_btn)
    LightButton add_patient_btn;

    @BindView(R.id.logout_btn)
    LightButton logout_btn;

    @BindView(R.id.setting_view)
    LinearLayout settingView;


    @BindView(R.id.clinic_visit_tv)
    BoldTextView clinic_visit_tv;

    @BindView(R.id.virtual_tv)
    BoldTextView virtual_tv;

    @BindView(R.id.lab_report_tv)
    BoldTextView lab_report_tv;

    @BindView(R.id.rad_reports_tv)
    BoldTextView rad_reports_tv;

    @BindView(R.id.patient_digital_view)
    LinearLayout patientDigitalView;

    boolean isChatBotVisible = false;

    TinyDB tinyDB = null;

    String termsConditionTxt = "";

    private int _currentPage = 0;

    private int _numPages = 0;

    boolean samePage = false;

    boolean langaugeChanged = false;

    boolean isCenterOfExcellence = false;
    int upcommingAptsCount = 0;
    int pastAptsCount = 0;
    ArrayList<MarketingClickableUrlsModel> getMarketingClickableUrlsModels = null;
    @Inject
    ViewModelFactory viewModelFactory;

    DashboardViewModel viewModel;


    long insuranceIdPrev = 0;
    String mrnNoPrev = "";
    long patientIdPrev = 0;
    String patientNamePrev = "";
    String patientGenderPrev = "";
    String dateOfBirthPrev = "";

    boolean isQuickReg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Toast.makeText(this, "dashboard on Create", Toast.LENGTH_LONG).show();
        viewModel = new ViewModelProvider(this, viewModelFactory).get(DashboardViewModel.class);
        init();
         notification_view.setVisibility(View.VISIBLE);

        viewModel.wipedOutAppointmentData();

        Intent intent = getIntent();
        boolean isFromProfileUpdate = intent.getBooleanExtra("profile_update", false);
        boolean isFromProfileUpdateTent = intent.getBooleanExtra("profile_update_tent", false);
        boolean callLogiin = false;
        if(isFromProfileUpdate){
            if(isFromProfileUpdateTent){
                callLogiin = true;
            }
        }
        if (!samePage) {
            callOfServices();
            loadDoctors(true, callLogiin);
        } else {
            loadDoctors(false, false);
        }

        loadCompany();

        if(callLogiin){
            logonApi(false, true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getNotifications();
            }
        }, 600);
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
                ErrorMessage.getInstance().showSuccessGreen(DashBoardActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameAr() + ". "+getString(R.string.please_press_to_continue));
            }else{
                ErrorMessage.getInstance().showSuccessGreen(DashBoardActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameEn() + ". "+getString(R.string.please_press_to_continue));
            }
        } else {
            ErrorMessage.getInstance().showErrorYellow(DashBoardActivity.this, appointmentEvent.getErrorMSg());
        }
    }

    void getNotifications() {
        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            String securityToken = new TinyDB(this).getString(Const.TOKEN_KEY);
            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
            String oRegId = tinyDB.getString(Const.OREGID_KEY);

            if (!ValidationHelper.isNullOrEmpty(securityToken)) {

                NotificationDTO notificationDTO = new NotificationDTO(securityToken, String.valueOf(patientId));

                RestClient.getInstance().getNotificationList(notificationDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        hideWaitDialog();

                        NotificationModel notificatinModel = (NotificationModel) result;

                        if (notificatinModel != null) {

                            MobileOpResult mobileOpResult = notificatinModel.getMobileOpResult();

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                    ArrayList<NotificationMiniModel> notificationMiniModel = notificatinModel.getPushNotifications();

                                    if (!ValidationHelper.isNullOrEmpty(notificationMiniModel)) {
                                        loadNotificationList(notificationMiniModel);
                                    }

                                } else {
                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(DashBoardActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(DashBoardActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMesg);
                                }
                            } else {
                                ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMessage);
                            }
                        } else {
                            ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMessage);
                        }
                    }
                });
            }
        } else {
            ErrorMessage.getInstance().showError(this, getString(R.string.internet_connection));
        }
    }

    int unReadNotificationCount = 0;
    private void loadNotificationList(ArrayList<NotificationMiniModel> notificationMiniModel) {
        unReadNotificationCount = 0;
        if(notificationMiniModel != null){
            if(!notificationMiniModel.isEmpty()){
                for (NotificationMiniModel notificationModel : notificationMiniModel){
                    if(!notificationModel.getIsRead()){
                        unReadNotificationCount += 1;
                    }
                }

                if(unReadNotificationCount != 0){
                    notification_alert_view.setVisibility(View.VISIBLE);
                    notification_alert_view.setText(String.valueOf(unReadNotificationCount));
                }else{
                    notification_alert_view.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private void checkBioMetricSupport() {

        boolean isBiometricSupport = tinyDB.getBoolean(Const.BIOMETRIC_SUPPORT);
        int loggedInType = tinyDB.getInt(Const.LOGIN_TYPE);
        boolean isEnabled = tinyDB.getBoolean(Const.BIOMETRIC_ENABLED);
        boolean isReminder = tinyDB.getBoolean(Const.BIOMETRIC_REMINDER);
        boolean isFromLogin = tinyDB.getBoolean(Const.IS_FROM_LOGIN);

        //  if(isFromLogin){
        //   tinyDB.putBoolean(Const.IS_FROM_LOGIN, false);
        if (isBiometricSupport) {
            if (loggedInType == Const.RG_CUSTOM) {
                if (!isEnabled) {
                    if (!isReminder) {
                        BiometricDialog();
                    }
                }
            }
        }
        // }

    }

    @Override
    public void inject() {
        super.inject();
        component.inject(this);
    }

    private void loadCompany() {

        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        viewModel.loadCompanies(new SecurityTokenDTO(securityToken));

    }

    LinearLayoutManager linearLayoutManager = null;

    private void init() {
        tinyDB = new TinyDB(this);
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

        insuranceIdPrev = Long.parseLong(tinyDB.getString(Const.INSURANCE_ID));
        mrnNoPrev = tinyDB.getString(Const.MRN_NO);
        patientIdPrev = tinyDB.getLong(Const.PATIENT_ID, 0);
        patientNamePrev = tinyDB.getString(Const.PATIENT_NAME);
        patientGenderPrev = tinyDB.getString(Const.PATIENT_GENDER);
        dateOfBirthPrev = tinyDB.getString(Const.DATE_BIRTH);

       boolean isTent = tinyDB.getBoolean(IS_TENT);
       if(isTent){
           patientRId.setVisibility(View.GONE);
       }else{
           patientRId.setVisibility(View.VISIBLE);
       }

        Intent intent = getIntent();
        if (intent != null) {
            patientsMiniModels = intent.getParcelableArrayListExtra(EXISTING_PATIENT);
            isQuickReg = intent.getBooleanExtra(IS_QUICK_REG, false);
            if (ValidationHelper.isNullOrEmpty(patientsMiniModels)) {
                patientsMiniModels = new ArrayList<>();
            }

            String upcoming = intent.getStringExtra("upcoming");
            String past = intent.getStringExtra("past");

            if (!ValidationHelper.isNullOrEmpty(upcoming)) {
                upcomingCountsEdt.setText(upcoming);
            }

            if (!ValidationHelper.isNullOrEmpty(past)) {
                pastCountEdt.setText(past);
            }

            samePage = intent.getBooleanExtra("same", false);
            getMarketingClickableUrlsModels = intent.getParcelableArrayListExtra("banners");

            if (!ValidationHelper.isNullOrEmpty(getMarketingClickableUrlsModels))
                loadSlidersImages(getMarketingClickableUrlsModels);
        }


        if (isCenterOfExcellency()) {
            centerOfExcellency.setVisibility(View.VISIBLE);
            isCenterOfExcellence = true;
        } else {
            centerOfExcellency.setVisibility(View.GONE);
            isCenterOfExcellence = false;
        }

        int loggedInType = tinyDB.getInt(Const.LOGIN_TYPE);

        if (loggedInType == Const.RG_FACEBOOK) {
            settingView.setVisibility(View.GONE);
        } else if (loggedInType == Const.RG_GOOGLE) {
            settingView.setVisibility(View.GONE);
        } else {
            settingView.setVisibility(View.VISIBLE);
        }


        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("update_appts"));


        mDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {

            }

            @Override
            public void onDrawerOpened(View view) {

            }

            @Override
            public void onDrawerClosed(View view) {

                switchProfile.setVisibility(View.GONE);

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
        translateButtonChange();
        menuChange();
        languageViews();


        boolean show = tinyDB.getBoolean("isHelpShow");
        boolean switchPatient = tinyDB.getBoolean(IS_FROM_SWITCH_PATIENT);

        if (show && !isQuickReg) {
            checkBioMetricSupport();
        } else {
            if (!isQuickReg) {
                isHelpRequire();
            }
        }

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);

        upcomingApptsRecyclerView.setLayoutManager
                (linearLayoutManager);
        upcomingApptsRecyclerView.setHasFixedSize(true);
        //loadUpcoming();

        if (isQuickReg) {
            Intent bookIntent = new Intent(this, BookAppointmentThreeActivity.class);
            bookIntent.putExtra(DOCTOR_NAME, tinyDB.getString(DOCTOR_NAME));
            bookIntent.putExtra(DOCTOR_SPECIALITY, tinyDB.getString(SPECIALITY_NAME_EN));
            bookIntent.putExtra(DATE, tinyDB.getString(DATE));
            bookIntent.putExtra(SERVICE_DATE, tinyDB.getString(SERVICE_DATE));
            bookIntent.putExtra(COMPANY_NAME_EN, tinyDB.getString(COMPANY_NAME_EN));
            bookIntent.putExtra(SPECIALITY_NAME_EN, tinyDB.getString(SPECIALITY_NAME_EN));
            bookIntent.putExtra(SPECIALITY_NAME_AR, tinyDB.getString(SPECIALITY_NAME_AR));
            bookIntent.putExtra(SPECIALITY_ID, tinyDB.getString(SPECIALITY_ID));
            bookIntent.putExtra(DOCTOR_ID, tinyDB.getString(DOCTOR_ID));
            bookIntent.putExtra(ISTELEMEDICINE_KEY, tinyDB.getBoolean(ISTELEMEDICINE_KEY));
            bookIntent.putExtra(DOCTOR_IMAGE_URL, tinyDB.getString(DOCTOR_IMAGE_URL));
            bookIntent.putExtra(Const.SELECTED_TIME, tinyDB.getString(Const.SELECTED_TIME));
            bookIntent.putExtra(Const.APPT_BOOK_TYPE, tinyDB.getInt(Const.APPT_BOOK_TYPE));
            bookIntent.putExtra(Const.IS_SECONDARY, tinyDB.getBoolean(Const.IS_SECONDARY));
            bookIntent.putExtra(Const.ORDER_RAD, "");
            bookIntent.putExtra(Const.IS_INSURANCE_KEY, tinyDB.getBoolean(Const.IS_INSURANCE_KEY));
            bookIntent.putExtra(Const.FOLLOW_UP_TYPE, tinyDB.getBoolean(Const.FOLLOW_UP_TYPE));
            bookIntent.putExtra(APPT_ID, tinyDB.getLong(APPT_ID, 0));
            startActivityForResult(bookIntent, 112);
        }

        //apptGetSummaryBySType();
    }

    public void loadUpcoming() {
        //  HomeUpcomingAdapter  homeUpcomingAdapter = new HomeUpcomingAdapter(this);
        //upcomingApptsRecyclerView.setAdapter(homeUpcomingAdapter);
    }

    @Override
    protected int layout() {
        return R.layout.dashboard_navigation_view;
    }

    @OnClick(R.id.toolbar_left_iv)
    void callOfServices() {
        updatePatientName(tinyDB);
        // asyncAppointments();
        appGetSummary();
     /*   final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                apptGetSummaryBySType();
            }
        }, 500);*/

        loadCompany();
    }


    @OnClick(R.id.notification_view)
    void onNotification() {
        Intent intent = new Intent(this, NotificationsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.setting_view)
    void onSettingViewClicked() {
        startActivity(SettingsActivity.Companion.getLaunchIntent(this));
    }

    @OnClick(R.id.patient_digital_view)
    void onPatientDigitalView() {
        String patentDigitalUrl = tinyDB.getString(Const.PATIENT_REG_CARD_URL);
        Intent intent = new Intent(this, BrowserActivityPayment.class);
        intent.putExtra("calendar", false);
        intent.putExtra("url", patentDigitalUrl);
        //  intent.putExtra("url", getString(R.string.contact_us_url));
        intent.putExtra(Const.TERMS_CONDITIONS_KEY, termsConditionTxt);
        startActivity(intent);
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        //asyncAppointments();
    }*/

    boolean sliderWorking = false;

    void loadSlidersImages(ArrayList<MarketingClickableUrlsModel> bannersUrls) {

      /*  if (!ValidationHelper.isNullOrEmpty(bannersUrls)) {
            tinyDB.putListObject(Const.BANNERS_KEY, bannersUrls);
        } else {
            bannersUrls = tinyDB.getListObject(Const.BANNERS_KEY, String.class);
        }*/

        if (!ValidationHelper.isNullOrEmpty(bannersUrls)) {

            if (TextUtil.getArabic(this))
                Collections.reverse(bannersUrls);

            if (!sliderWorking) {
                mViewPager.setAdapter(new BannersAdapter(DashBoardActivity.this, bannersUrls));
                indicator.setViewPager(mViewPager);
                sliderWorking = true;

                _numPages = bannersUrls.size();

                if (TextUtil.getArabic(this))
                    mViewPager.setCurrentItem(_numPages);

                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    public void onPageScrollStateChanged(int state) {

                    }

                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    public void onPageSelected(int position) {

                        _currentPage = position;

                    }
                });

                changeSlider();
            }

        }

    }

    public void UpcomingAppointments(View view) {

        Intent intent = new Intent(this, UpcomingAppointmentsActivity.class);
        tinyDB.putBoolean(Const.BACK_TO_HOME, false);
        startActivity(intent);
    }

    public void PastAppointments(View view) {
        Intent intent = new Intent(this, PastAppointmentActivity.class);
        startActivity(intent);

    }

    //temp
    public void CardList(View view) {
     /*   Intent intent = new Intent(this, CustomUIActivity.class);
        startActivity(intent);*/

        Intent intent = new Intent(this, ZoomMeetingActivity.class);
        intent.putExtra("tokenId", "YmJGOXI1di9PWjhYYkcveWpwTHh3QT09");
        intent.putExtra("sessionId", "3691207362");
        intent.putExtra("apptId", "5639641412");
        startActivity(intent);
    }

    public void BookNow(View view) {

        CompanySelectionFragment.newInstance(viewModel.getCompanies())
                .show(getSupportFragmentManager(), "CompanySelectionFragment");

        viewModel.setTeleAppointment(false);
//
//        boolean showDialog = tinyDB.getBoolean("showDialog");
//
//        if (!showDialog)
//            showDialog(false);
//        else
//            nonTeleAppt();
    }

    public void BookTele(View view) {
        /*CompanySelectionFragment.newInstance(viewModel.getCompanies())
                .show(getSupportFragmentManager(),"CompanySelectionFragment");
        viewModel.setTeleAppointment(true);*/
        viewModel.setTeleAppointment(true);
        teleAppt();
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

    void nonTeleAppt() {
        Intent intent = new Intent(this, BookAppointmentOneActivity.class);
        intent.putExtra(ISTELEMEDICINE_KEY, false);
        startActivity(intent);
    }

    void teleAppt() {

        String patientName = tinyDB.getString(Const.PATIENT_NAME);
        long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
        String key = patientName + patientId;
        tinyDB.putString(Const.COMPANY_ID, "");
        boolean accepted = tinyDB.getBoolean(key);

        if (accepted) {
            Intent intent = new Intent(this, BookAppointmentOneActivity.class);
            intent.putExtra(ISTELEMEDICINE_KEY, true);
            startActivity(intent);
            return;
        }

        Intent intent = new Intent(this, ApptTermsConditionsActivity.class);
        intent.putExtra(Const.TERMS_CONDITIONS_KEY, termsConditionTxt);
        startActivity(intent);
    }

    public void showDialog(boolean fromTele) {
        try {
            final Dialog dialog = new Dialog(this, R.style.NewDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.myclinic_dialogue);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            RegularTextView dontRemindAgain = (RegularTextView) dialog.findViewById(R.id.dont_remind);

            RegularTextView dialogButton = dialog.findViewById(R.id.cancel);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    nonTeleAppt();
                }
            });

            dontRemindAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tinyDB.putBoolean("showDialog", true);

                    dialog.dismiss();
                }
            });


            dialog.show();
        } catch (Exception e) {

        }
    }

    ArrayList<PatientsMiniModel> patientsMiniModels = new ArrayList<>();

    void loadDoctors(boolean loadUpcoming, boolean callLogin) {

        if (!ValidationHelper.isNullOrEmpty(patientsMiniModels) && patientsMiniModels.size() == 1) {
            switchDisable();
            //tinyDB.putString(Const.PATIENT_REG_CARD_URL, patientsMiniModels.get(0).getRegCardUrl());
            if(!callLogin){
                logonApi(true, loadUpcoming);
            }
            return;
        } else {
            switchEnable();
            if (loadUpcoming) {
                apptGetSummaryBySType();
            }
        }


        if (!ValidationHelper.isNullOrEmpty(patientsMiniModels)) {

            SwitchDoctorsAdapter adapter = new SwitchDoctorsAdapter(this, patientsMiniModels);
            switchProfileRecycler.setLayoutManager(new LinearLayoutManager(this));
            switchProfileRecycler.setAdapter(adapter);
            adapter.setOnCardClickListner(this);
        } else {
            switchDisable();
        }
    }

    void switchEnable() {
        switch_profile_btn.setTextColor(getResources().getColor(R.color.colorWhite));
        switch_profile_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        switch_profile_btn.setEnabled(true);
    }

    void switchDisable() {
        switch_profile_btn.setTextColor(getResources().getColor(R.color.colorWhite));
        switch_profile_btn.setBackgroundColor(getResources().getColor(R.color.colorGrey));
        switch_profile_btn.setEnabled(false);
    }

    @OnClick(R.id.switch_profile_btn)
    void switchProfile() {

        Animation slideUpAnimation, slideDownAnimation;

        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        if (switchProfile.getVisibility() == View.GONE) {
            loadDoctors(false, false);
            switchProfile.startAnimation(slideUpAnimation);
            switchProfile.setVisibility(View.VISIBLE);
        } else {
            switchProfile.startAnimation(slideDownAnimation);
            switchProfile.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.toolbar_right_iv)
    void righDrawer() {
        if (TextUtil.getEnglish(this))
            mDrawer.openDrawer(Gravity.LEFT, true);
        else if (TextUtil.getArabic(this))
            mDrawer.openDrawer(Gravity.RIGHT, true);
    }

    @OnClick(R.id.switch_container)
    void hide() {
        switchProfile();
    }

    public void Excellency(View view) {
        Intent intent = new Intent(this, CenterOfExcellencyActivity.class);
        startActivity(intent);
    }


    void appGetSummary() {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            String companyId = tinyDB.getString(Const.COMPANY_ID);
            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);

            if (!ValidationHelper.isNullOrEmpty(securityToken) && patientId != 0) {

                ApptGetSummaryDTO apptGetSummaryDTO = new ApptGetSummaryDTO(securityToken, String.valueOf(patientId), companyId);

                RestClient.getInstance().apptGetSummary(apptGetSummaryDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        hideWaitDialog();

                        ApptGetSummaryModel apptGetSummaryModel = (ApptGetSummaryModel) result;

                        if (apptGetSummaryModel != null) {

                            MobileOpResult mobileOpResult = apptGetSummaryModel.getMobileOpResult();

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                    if (!ValidationHelper.isNullOrEmpty(apptGetSummaryModel.getMarketingClickableUrlsModels())) {
                                        getMarketingClickableUrlsModels = apptGetSummaryModel.getMarketingClickableUrlsModels();
                                        loadSlidersImages(getMarketingClickableUrlsModels);
                                    }

                                    int upcomingCount = apptGetSummaryModel.getUpcomingCount();
                                    int pastCount = apptGetSummaryModel.getPastCount();
                                    int reportCount = apptGetSummaryModel.getReportCount();

                                    upcommingAptsCount = upcomingCount;
                                    pastAptsCount = pastCount;

                                    upcomingCountsEdt.setText("" + upcomingCount);
                                    pastCountEdt.setText("" + pastCount);

                                    if (TextUtil.getEnglish(DashBoardActivity.this))
                                        termsConditionTxt = apptGetSummaryModel.getTeleConsentEn();
                                    else if (TextUtil.getArabic(DashBoardActivity.this))
                                        termsConditionTxt = apptGetSummaryModel.getTeleConsentAr();

                                    if (TextUtil.getEnglish(DashBoardActivity.this))
                                        tinyDB.putString(Const.TELE_TERMS_KEY, apptGetSummaryModel.getTeleTermsOfUseEn());
                                    else if (TextUtil.getArabic(DashBoardActivity.this))
                                        tinyDB.putString(Const.TELE_TERMS_KEY, apptGetSummaryModel.getTeleTermsOfUseAr());


                                } else {
                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                        if (TextUtil.getEnglish(DashBoardActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(DashBoardActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";

                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }
                                    ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMesg);
                                }
                            } else {
                                ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMessage);
                            }
                        } else {
                            ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMessage);
                        }
                    }
                });
            }
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    ArrayList<ApptGetFollowUpsModel> followUpsModelArrayList = null;

    void asyncAppointments() {
        if (ConnectionUtil.isInetAvailable(DashBoardActivity.this)) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {

                    String companyId = tinyDB.getString(Const.COMPANY_ID);
                    String securityToken = tinyDB.getString(Const.TOKEN_KEY);
                    long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);

                    if (!ValidationHelper.isNullOrEmpty(securityToken) && patientId != 0) {

                        ApptGetSummaryDTO apptGetSummaryDTO = new ApptGetSummaryDTO(securityToken, String.valueOf(patientId), companyId);
                        RestClient.getInstance().apptGetFollowUps(apptGetSummaryDTO, new OnResultListener() {
                            @Override
                            public void onResult(Object result, boolean status, String errorMessage) {

                                ApptMinisModel apptMinisModel = (ApptMinisModel) result;

                                if (apptMinisModel != null) {

                                    MobileOpResult mobileOpResult = apptMinisModel.getMobileOpResult();

                                    if (mobileOpResult != null) {

                                        if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                            followUpsModelArrayList = apptMinisModel.getApptMinis();

                                            if (!ValidationHelper.isNullOrEmpty(followUpsModelArrayList)) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Animation slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                                                                R.anim.fadein);
                                                        availableFollowUps.setAnimation(slideDownAnimation);
                                                        if (availableFollowUps.getVisibility() == View.GONE)
                                                            availableFollowUps.setVisibility(View.VISIBLE);
                                                        availableFollowUps.setText(getString(R.string.follow_up_without_zero) + " (" + followUpsModelArrayList.size() + ")");
                                                    }
                                                });
                                            } else {
                                                availableFollowUps.setVisibility(View.GONE);
                                            }

                                        } else {
                                            String errorMesg = "";

                                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                                if (TextUtil.getEnglish(DashBoardActivity.this))
                                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                                else if (TextUtil.getArabic(DashBoardActivity.this))
                                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";

                                            }

                                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                            }
                                            ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMesg);
                                        }
                                    } else {
                                        ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMessage);
                                    }
                                } else {
                                    ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMessage);
                                }
                            }
                        });
                    }


                }
            });
        }
    }

    public void Home(View view) {

        /*Intent intent = new Intent(this, BrowserActivityPayment.class);
        intent.putExtra("url", Const.HOME);
        startActivity(intent);*/
        mDrawer.closeDrawers();
    }

    public void AboutUs(View view) {
        Intent intent = new Intent(this, BrowserActivityPayment.class);
        intent.putExtra("calendar", true);
        intent.putExtra("url", getString(R.string.about_us_url));
        intent.putExtra(Const.TERMS_CONDITIONS_KEY, termsConditionTxt);
        startActivity(intent);
    }

    public void DoctorsSpecialist(View view) {
//        Intent intent = new Intent(this, BrowserActivityPayment.class);
//        intent.putExtra("calendar", true);
//        intent.putExtra("url", getString(R.string.dctr_specialist_utl));
//        intent.putExtra(Const.TERMS_CONDITIONS_KEY, termsConditionTxt);
//        startActivity(intent);

        Intent intent = new Intent(this, MedicationDeliveriesActivity.class);
        startActivity(intent);
    }

    public void ContactUs(View view) {
        Intent intent = new Intent(this, BrowserActivityPayment.class);
        intent.putExtra("calendar", true);
        // intent.putExtra("url", "https://myclinic.hellotars.com/conv/Rg2PDe/");
        intent.putExtra("url", getString(R.string.contact_us_url));
        intent.putExtra(Const.TERMS_CONDITIONS_KEY, termsConditionTxt);
        startActivity(intent);
    }

    @OnClick(R.id.add_patient_btn)
    void addPatient() {
        Intent intent = AddProfileActivity.Companion.getLaunchIntent(this);//new Intent(DashBoardActivity.this, CompleteRegistrationActivity.class);
        intent.putExtra(Const.ADD_PATIENT, true);
        intent.putExtra(IS_FROM_DASHBOARD, true);
       // tinyDB.putBoolean(Const.IS_TENT, false);
        startActivityForResult(intent, 313);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 313 && resultCode == RESULT_OK) {

            if (data != null) {

                boolean isAddPatient = data.getBooleanExtra(Const.ADD_PATIENT, false);

                if (isAddPatient) {

                    tinyDB.putString(Const.INSURANCE_ID, String.valueOf(insuranceIdPrev));
                    tinyDB.putString(Const.MRN_NO, mrnNoPrev);
                    tinyDB.putLong(Const.PATIENT_ID, patientIdPrev);
                    tinyDB.putString(Const.PATIENT_NAME, patientNamePrev);
                    tinyDB.putString(Const.PATIENT_GENDER, patientGenderPrev);
                    tinyDB.putString(Const.DATE_BIRTH, dateOfBirthPrev);
                    logonApi(false, false);
                  /*  String insuranceId = tinyDB.getString(Const.INSURANCE_ID);
                    String mrnNo = tinyDB.getString(Const.MRN_NO);
                    long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
                    String patientName = tinyDB.getString(Const.PATIENT_NAME);
                    String patientGender = tinyDB.getString(Const.PATIENT_GENDER);
                    String dateOfBirth = tinyDB.getString(Const.DATE_BIRTH);

                    PatientsMiniModel patientsMiniModel = new PatientsMiniModel();
                    patientsMiniModel.setMRN(mrnNo);
                    patientsMiniModel.setPatientId(patientId);
                    patientsMiniModel.setBirthDate(dateOfBirth);

                    InsuranceCardModel insuranceCardModel = new InsuranceCardModel();
                    if (!ValidationHelper.isNullOrEmpty(insuranceId))
                        insuranceCardModel.setInsuranceId(Long.parseLong(insuranceId));
                    else
                        insuranceCardModel.setInsuranceId(0);

                    ArrayList<InsuranceCardModel> arrayList = new ArrayList<>();
                    arrayList.add(insuranceCardModel);

                    patientsMiniModel.setInsuranceCards(arrayList);

                    if (TextUtil.getEnglish(this))
                        patientsMiniModel.setNameEn(patientName);
                    else if (TextUtil.getArabic(this))
                        patientsMiniModel.setNameAr(patientName);

                    patientsMiniModel.setGender(patientGender);

                    patientsMiniModels.add(patientsMiniModel);

                    tinyDB.putString(Const.INSURANCE_ID, String.valueOf(insuranceIdPrev));
                    tinyDB.putString(Const.MRN_NO, mrnNoPrev);
                    tinyDB.putLong(Const.PATIENT_ID, patientIdPrev);
                    tinyDB.putString(Const.PATIENT_NAME, patientNamePrev);
                    tinyDB.putString(Const.PATIENT_GENDER, patientGenderPrev);
                    tinyDB.putString(Const.DATE_BIRTH, dateOfBirthPrev);

                    loadDoctors();
                    callOfServices();
*/
                }
            }
        }
    }

    void logonApi(boolean isFromDoctor, boolean loadUpcomings) {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            String deviceType = getString(R.string.device_type);
            String versionRelease = Build.VERSION.RELEASE;
            String versionName = BuildConfig.VERSION_NAME;
            String username = tinyDB.getString(Const.USER_NAME_LOGIN);
            String pass = tinyDB.getString(PASSWORD_KEY);
            String locality = tinyDB.getString(Const.LOCALITY);
            String deviceToken = tinyDB.getString(Const.DEVICE_TOKEN);
            int provider = tinyDB.getInt(Const.LOGIN_TYPE);
            String accessToken = tinyDB.getString(Const.ACCESS_TOKEN);

            LogonDTO logonDTO = new LogonDTO(username, pass, deviceType, versionRelease, deviceToken, versionName, locality, provider, accessToken);

            RestClient.getInstance().logOn(logonDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();
                    LogonModel logonModel = (LogonModel) result;

                    if (logonModel != null) {

                        MobileOpResult mobileOpResult = logonModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if ((mobileOpResult.getResult() == Success.SUCCESSCODE.getValue())) {

                                SecurityToken securityToken = logonModel.getSecurityToken();

                                if (securityToken != null) {
                                    String token = securityToken.getToken();
                                    if (!ValidationHelper.isNullOrEmpty(token)) {
                                        tinyDB.putString(TOKEN_KEY, token);
                                    }

                                    String tokenExpiry = securityToken.getExpiryDate();
                                    if (!ValidationHelper.isNullOrEmpty(tokenExpiry)) {
                                        tinyDB.putString(TOKEN_EXPIRY_KEY, tokenExpiry);
                                        startSession();
                                    }
                                }
                                patientsMiniModels.clear();
                                patientsMiniModels.addAll(logonModel.getPatientsMiniModels());
                                if (!ValidationHelper.isNullOrEmpty(logonModel.getPatientsMiniModels()) && logonModel.getPatientsMiniModels().size() == 1) {
                                    if (!ValidationHelper.isNullOrEmpty(patientsMiniModels.get(0).getRegCardUrl())) {
                                        tinyDB.putString(Const.PATIENT_REG_CARD_URL, patientsMiniModels.get(0).getRegCardUrl());
                                    } else {
                                        tinyDB.putString(Const.PATIENT_REG_CARD_URL, "");
                                    }
                                }
                                if (!isFromDoctor) {
                                    loadDoctors(false, true);
                                }

                                if (loadUpcomings) {
                                    apptGetSummaryBySType();
                                }

                            } else {
                                hideWaitDialog();
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMesg);
                            }

                        } else {
                            hideWaitDialog();
                            ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMessage);
                        }

                    } else {
                        hideWaitDialog();
                        ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMessage);
                    }
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(DashBoardActivity.this, getString(R.string.internet_connection));
        }
    }

    void startSession() {
        tinyDB.putBoolean("session", true);
        //tinyDB.putBoolean("session_inter", true);
        // Const.ALLOWED_TOKEN_CALL = true;


    }

    @OnClick(R.id.logout_btn)
    void logout() {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            RestClient.getInstance().logout(securityToken, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    tinyDB.putString(PATIENT_NAME, "");
                    tinyDB.putString(PATIENT_GENDER, "");
                    tinyDB.putString(Const.INSURANCE_ID, "0");
                    tinyDB.putString(Const.MRN_NO, "");
                    tinyDB.putLong(Const.PATIENT_ID, 0);

                    ((App) getApplication()).cancelAlarmIfExists();

                    Intent intent = new Intent(DashBoardActivity.this, LoginActivity.class);
                    intent.putExtra("show", true);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, "Internet is not available");
        }
    }


    @OnClick(R.id.patientRId)
    void patientRId() {
        String patentDigitalUrl = tinyDB.getString(Const.PATIENT_REG_CARD_URL);
        Intent intent = new Intent(this, BrowserActivityPayment.class);
        intent.putExtra("calendar", false);
        intent.putExtra("url", patentDigitalUrl);
        //  intent.putExtra("url", getString(R.string.contact_us_url));
        intent.putExtra(Const.TERMS_CONDITIONS_KEY, termsConditionTxt);
        startActivity(intent);
    }

    @OnClick(R.id.my_acount_btn)
    void myAccount() {

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

                                                if (patientRegistrationUpdate.getInsurance() != null) {
                                                    INSURANCE_IMAGES = patientRegistrationUpdate.getInsurance().getInsuranceCardScanBase64();
                                                    patientRegistrationUpdate.getInsurance().setInsuranceCardScanBase64("");
                                                }

                                                String mrnNo = patientRegistrationUpdate.getMRN();
                                                boolean isTentative;
                                                if (!ValidationHelper.isNullOrEmpty(mrnNo)) {
                                                    isTentative = false;
                                                    tinyDB.putBoolean(IS_TENT, false);
                                                    patientRId.setVisibility(View.VISIBLE);
                                                }else{
                                                    isTentative  = true;
                                                    tinyDB.putBoolean(IS_TENT, true);
                                                    patientRId.setVisibility(View.GONE);
                                                }

                                                Intent intent;
                                                if (isTentative) {

                                                    intent = new Intent(DashBoardActivity.this, AddProfileActivity.class);
                                                    //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    // intent.putExtra(IS_QUICK_REG, isQuickReg);

                                                    // intent.putExtra(EXISTING_PATIENT, patientsMiniModels);
                                                    intent.putExtra(FIRST_NAME, patientRegistrationUpdate.getFirstName());
                                                    intent.putExtra(IS_FROM_DASHBOARD, true);
                                                    intent.putExtra(LAST_NAME, patientRegistrationUpdate.getLastName());
                                                    intent.putExtra(MIDDLE_NAME, patientRegistrationUpdate.getMiddleName());
                                                    intent.putExtra(DATE_OF_BIRTH, patientRegistrationUpdate.getBirthDate());
                                                    intent.putExtra(GENDER, patientRegistrationUpdate.getGender());
                                                    tinyDB.putBoolean(Const.IS_TENT, true);

                                                /*   setResult(RESULT_OK);
                                                finish();*/
                                                } else {
                                                    intent = new Intent(DashBoardActivity.this, UpdateProfileFirstActivity.class);
                                                    intent.putExtra(PATIENT_UPDATE_KEY, patientRegistrationUpdate);
                                                }
                                                startActivity(intent);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    } else {
                                        String errorMesg = "";

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                            if (TextUtil.getEnglish(DashBoardActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                            else if (TextUtil.getArabic(DashBoardActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";

                                        }

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                            errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                        }

                                        if(mobileOpResult.getResult() == 600 || mobileOpResult.getResult() == 700){
                                            ErrorMessage.getInstance().showErrorYellow(DashBoardActivity.this, errorMesg);
                                        }else{
                                            ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMesg);
                                        }
                                      //  ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMesg);
                                    }
                                } else
                                    ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMessage);
                            } else
                                ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMessage);
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

    @Override
    public void OnCardClicked(PatientsMiniModel model, int pos) {

        try {
            // get the data
            long insuranceId = 0;

            if (model != null && !ValidationHelper.isNullOrEmpty(model.getInsuranceCards())) {
                insuranceId = model.getInsuranceCards().get(0).getInsuranceId();
                insuranceIdPrev = insuranceId;
            }

            String dob = model.getBirthDate();
            if (!ValidationHelper.isNullOrEmpty(dob)) {

                dateOfBirthPrev = dob;
            } else {
                dateOfBirthPrev = "";
            }

            String mrnNo = model.getMRN();
            long patientId = model.getPatientId();
            patientIdPrev = patientId;
            TinyDB tinyDB = new TinyDB(this);

            // save the value
            tinyDB.putString(Const.INSURANCE_ID, String.valueOf(insuranceId));

            if (!ValidationHelper.isNullOrEmpty(mrnNo)) {
                tinyDB.putString(Const.MRN_NO, mrnNo);
                tinyDB.putBoolean(Const.IS_TENT, false);
                mrnNoPrev = mrnNo;
                patientRId.setVisibility(View.VISIBLE);
            } else {
                tinyDB.putString(Const.MRN_NO, "0");
                mrnNoPrev = "0";
                tinyDB.putBoolean(Const.IS_TENT, true);
                patientRId.setVisibility(View.GONE);
            }

            tinyDB.putLong(Const.PATIENT_ID, patientId);

            if (TextUtil.getEnglish(this)) {
                tinyDB.putString(PATIENT_NAME, model.getNameEn());
                patientNamePrev = model.getNameEn();
            } else if (TextUtil.getArabic(this)) {
                tinyDB.putString(PATIENT_NAME, model.getNameAr());
                patientNamePrev = model.getNameAr();

            }

            tinyDB.putString(PATIENT_GENDER, model.getGender());
            patientGenderPrev = model.getGender();
            if (!ValidationHelper.isNullOrEmpty(model.getRegCardUrl())) {
                tinyDB.putString(Const.PATIENT_REG_CARD_URL, model.getRegCardUrl());
            } else {
                tinyDB.putString(Const.PATIENT_REG_CARD_URL, "");
            }

            tinyDB.putInt(Const.PATIENT_CATEGORY, model.getPatientCategory());
            hide();

            if (isCenterOfExcellency())
                centerOfExcellency.setVisibility(View.VISIBLE);
            else
                centerOfExcellency.setVisibility(View.GONE);

            callOfServices();

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    apptGetSummaryBySType();
                    getNotifications();
                }
            }, 500);

            checkBioMetricSupport();
            mDrawer.closeDrawers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void updatePatientName(TinyDB tinyDB) {

        String patientName = tinyDB.getString(Const.PATIENT_NAME);

        String gender = tinyDB.getString(Const.PATIENT_GENDER);

        if (!ValidationHelper.isNullOrEmpty(patientName))
            userName.setText(patientName);
        else
            userName.setText("");

        if (!ValidationHelper.isNullOrEmpty(gender)) {

            if (gender.equalsIgnoreCase("1")) {
                userImage.setImageDrawable(getResources().getDrawable(R.drawable.male));

            } else if (gender.equalsIgnoreCase("2")) {
                userImage.setImageDrawable(getResources().getDrawable(R.drawable.female));
            }

        }
    }

    @OnClick(R.id.availableFollowUps)
    void availableFollowUps() {
        if (!ValidationHelper.isNullOrEmpty(followUpsModelArrayList)) {
            Intent intent = new Intent(this, FollowUpsActivity.class);
            intent.putExtra(Const.FOLLOW_UPS, followUpsModelArrayList);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {

        if (isChatBotVisible) {
            openChatBotBg(null);
            return;
        }

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setConfirmText(getString(R.string.yes)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
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
        }).setContentText(getString(R.string.exit)).setTitleText(getString(R.string.my_clininc)).show();
    }

    boolean isCenterOfExcellency() {
        boolean isInclude = false;
        int patientCategory = tinyDB.getInt(Const.PATIENT_CATEGORY);
        if (patientCategory == 1 || patientCategory == 3)
            isInclude = true;

        return isInclude;
    }

    @OnClick(R.id.moreinfo)
    void moreinfo() {

        Intent intent = new Intent(this, MoreInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            if (intent != null) {
                boolean refresh = intent.getBooleanExtra("refresh", false);
                if (refresh) {
                    appGetSummary();
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            apptGetSummaryBySType();
                        }
                    }, 500);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    int checkZero = -1;
    boolean firstTime = false;

    void changeSlider() {

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (TextUtil.getEnglish(DashBoardActivity.this)) {
                    if (_currentPage == _numPages) {
                        _currentPage = 0;
                    }
                    mViewPager.setCurrentItem(_currentPage++, true);
                } else if (TextUtil.getArabic(DashBoardActivity.this)) {

                    if (_currentPage == 0) {
                        if (firstTime)
                            checkZero = _currentPage;
                        _currentPage = _numPages;
                        firstTime = true;
                    } else {
                        checkZero = -1;
                    }

                    if (checkZero == 0)
                        mViewPager.setCurrentItem(checkZero, true);
                    else
                        mViewPager.setCurrentItem(_currentPage--, true);
                }
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 0, 5000);

    }

    void menuChange() {

        if (TextUtil.getEnglish(this)) {

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar_right_iv_back.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            toolbar_right_iv_back.setLayoutParams(params);

            RelativeLayout.LayoutParams param4 = (RelativeLayout.LayoutParams) notification_view.getLayoutParams();
            param4.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            notification_view.setLayoutParams(param4);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) toolbar_left_iv_refresh.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            toolbar_left_iv_refresh.setLayoutParams(params1);

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) patientRId.getLayoutParams();
            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            patientRId.setLayoutParams(params2);


            DrawerLayout.LayoutParams drawerParam = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
            drawerParam.gravity = Gravity.LEFT;

        } else if (TextUtil.getArabic(this)) {

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) toolbar_right_iv_back.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            toolbar_right_iv_back.setLayoutParams(params1);

            RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) notification_view.getLayoutParams();
            params4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            notification_view.setLayoutParams(params4);

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) toolbar_left_iv_refresh.getLayoutParams();
            params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            toolbar_left_iv_refresh.setLayoutParams(params2);

            RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) patientRId.getLayoutParams();
            params3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            patientRId.setLayoutParams(params3);

            DrawerLayout.LayoutParams drawerParam = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
            drawerParam.gravity = Gravity.RIGHT;
        }
    }

    void languageViews() {

        if (TextUtil.getEnglish(this)) {

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) switch_title.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            switch_title.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) eng_ar_btns_container.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            eng_ar_btns_container.setLayoutParams(params1);

            RelativeLayout.LayoutParams params12 = (RelativeLayout.LayoutParams) upcomingCountsEdt.getLayoutParams();
            params12.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            upcomingCountsEdt.setLayoutParams(params12);

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) pastCountEdt.getLayoutParams();
            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            pastCountEdt.setLayoutParams(params2);

            upcoming_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.upcoming_icon, 0, R.drawable.grey_right_arrow, 0);
            past_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.past_icon, 0, R.drawable.grey_right_arrow, 0);

            lab_report_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.recipe, 0, R.drawable.grey_right_arrow, 0);
            rad_report_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.recipe, 0, R.drawable.grey_right_arrow, 0);

            centerOfExcellencyTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.grey_right_arrow, 0);
            book_now_btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arrow, 0);

            switch_profile_btn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

            my_acount_btn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

            add_patient_btn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

            logout_btn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

            book_now_btn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

        } else if (TextUtil.getArabic(this)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) switch_title.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            switch_title.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) eng_ar_btns_container.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            eng_ar_btns_container.setLayoutParams(params1);

            RelativeLayout.LayoutParams params12 = (RelativeLayout.LayoutParams) upcomingCountsEdt.getLayoutParams();
            params12.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            upcomingCountsEdt.setLayoutParams(params12);

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) pastCountEdt.getLayoutParams();
            params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            pastCountEdt.setLayoutParams(params2);

            upcoming_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.grey_left_arrow, 0, R.drawable.upcoming_icon, 0);
            past_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.grey_left_arrow, 0, R.drawable.past_icon, 0);


            lab_report_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.grey_left_arrow, 0, R.drawable.recipe, 0);
            rad_report_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.grey_left_arrow, 0, R.drawable.recipe, 0);

            centerOfExcellencyTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.grey_left_arrow, 0, 0, 0);
            book_now_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            book_now_btn_clinic.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            switch_profile_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            switch_profile_btn.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

            my_acount_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            my_acount_btn.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

            add_patient_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            add_patient_btn.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

            logout_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.grey_left_arrow, 0, 0, 0);
            logout_btn.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

            book_now_btn.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

        }
    }

    void translateButtonChange() {

        if (TextUtil.getEnglish(this)) {
            english.setBackgroundDrawable(getResources().getDrawable(R.drawable.left_round));
            arabic.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_round_right));

            english.setTextColor(getResources().getColor(R.color.colorWhite));
            arabic.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (TextUtil.getArabic(this)) {
            english.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_round_right));
            arabic.setBackgroundDrawable(getResources().getDrawable(R.drawable.left_round));

            english.setTextColor(getResources().getColor(R.color.colorPrimary));
            arabic.setTextColor(getResources().getColor(R.color.colorWhite));
        }

        english.setPadding(20, 20, 20, 20);
        arabic.setPadding(20, 20, 20, 20);
    }

    public void EN(View view) {
        if (!langaugeChanged) {
            langaugeChanged = true;
            tinyDB.putString(Const.LOCALITY_, "1_");
            englsh();
            // refresh();
        }
    }

    public void AR(View view) {
        if (!langaugeChanged) {
            langaugeChanged = true;
            tinyDB.putString(Const.LOCALITY_, "2_");
            // refresh();
            arbc();
        }
    }

    void refresh() {

        mDrawer.closeDrawers();

        String upcoming = upcomingCountsEdt.getText().toString();
        String past = pastCountEdt.getText().toString();

        Intent intent = new Intent(this, DashBoardActivity.class);
        intent.putParcelableArrayListExtra(EXISTING_PATIENT, patientsMiniModels);
        intent.putExtra("upcoming", upcoming);
        intent.putExtra("past", past);
        intent.putParcelableArrayListExtra("banners", getMarketingClickableUrlsModels);
        intent.putExtra("same", true);
        startActivity(intent);
        finish();
    }

    void arbc() {

        english.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_round_left));
        arabic.setBackgroundDrawable(getResources().getDrawable(R.drawable.right_round));

        english.setTextColor(getResources().getColor(R.color.colorPrimary));
        arabic.setTextColor(getResources().getColor(R.color.colorWhite));

        english.setPadding(20, 20, 20, 25);
        arabic.setPadding(20, 20, 20, 20);

        TextUtil.dialgoue(this, getString(R.string.restart_app_etc));
    }

    void englsh() {

        english.setBackgroundDrawable(getResources().getDrawable(R.drawable.right_round));
        arabic.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_round_left));

        english.setTextColor(getResources().getColor(R.color.colorWhite));
        arabic.setTextColor(getResources().getColor(R.color.colorPrimary));

        english.setPadding(20, 20, 20, 20);
        arabic.setPadding(20, 20, 20, 20);

        TextUtil.dialgoue(this, getString(R.string.restart_app_etc));
    }

    public void InstantLiveConsult(View view) {
        Intent intent = new Intent(this, TeleConsultationActivity.class);
        startActivity(intent);
    }

    public void Help(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
        mDrawer.closeDrawers();
    }


    void isHelpRequire() {
        boolean show = tinyDB.getBoolean("isHelpShow");
        if (!show) {
            Help(null);
            //tinyDB.putBoolean("isHelpShow", true);
        }
    }

    @Override
    public void onCompanySelected(@NotNull Company company) {
        if (viewModel.isTeleAppointment()) {
            //teleAppt();
        } else {

            boolean showDialog = tinyDB.getBoolean("showDialog");
            nonTeleAppt();
          /*  if (!showDialog)
                showDialog(false);
            else
                nonTeleAppt();*/
        }
    }

    public void laboratoryReports(View view) {
        reports_trasparent_layout.setVisibility(View.GONE);
        Intent intent = new Intent(this, LaboratoryReportsActivity.class);
        intent.putExtra(IS_LAB, true);
        startActivity(intent);
    }

    public void radReports(View view) {
        reports_trasparent_layout.setVisibility(View.GONE);
        Intent intent = new Intent(this, LaboratoryReportsActivity.class);
        intent.putExtra(IS_LAB, false);
        startActivity(intent);
    }

    private void BiometricDialog() {
        Dialog dialog = new Dialog(this, R.style.ios_dialog_style);
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
                startActivity(SettingsActivity.Companion.getLaunchIntent(DashBoardActivity.this));
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

    public void myProfile(View view) {
        myAccount();
    }

    public void onFindClinic(View view) {
        Intent intent = new Intent(this, FindClinicActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    public void contactUs(View view) {
        Intent intent = new Intent(this, ContactUsPopActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }


    @OnClick(R.id.book_now)
    void bookNow() {
        //bookAppointmentTypeDialog();
        book_now_trasparent_layout.setVisibility(View.VISIBLE);

        if (TextUtil.getArabic(this)) {
            clinic_visit_tv.setText("زيارات العيادة");
            virtual_tv.setText("المحادثات الطبية عن بعد");
        } else {
            clinic_visit_tv.setText("Clinic Visit");
            virtual_tv.setText("Virtual Consultation");
        }

    }

    @OnClick(R.id.book_now_trasparent_layout)
    void bookNowTrasparentLayout() {
        // bookAppointmentTypeDialog();
        book_now_trasparent_layout.setVisibility(View.GONE);

    }

    @OnClick(R.id.reports_trasparent_layout)
    void reportsTrasparentLayout() {
        // bookAppointmentTypeDialog();
        reports_trasparent_layout.setVisibility(View.GONE);
    }

    public void virtualBooking(View view) {
        book_now_trasparent_layout.setVisibility(View.GONE);

        String patientName = tinyDB.getString(Const.PATIENT_NAME);
        long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
        String key = patientName + patientId;
        tinyDB.putString(Const.COMPANY_ID, "");
        boolean accepted = tinyDB.getBoolean(key);

       /* if (accepted) {
            Intent intent = new Intent(this, SearchAppointmentFilterActivity.class);
            intent.putExtra(ISTELEMEDICINE_KEY, true);
            intent.putExtra(LOGGED_IN_MODE, true);
            startActivity(intent);
            return;
        }*/

        Intent intent = new Intent(this, ApptTermsConditionsActivity.class);
        intent.putExtra(Const.TERMS_CONDITIONS_KEY, termsConditionTxt);
        intent.putExtra(LOGGED_IN_MODE, true);
        intent.putExtra(ISTELEMEDICINE_KEY, true);
        startActivity(intent);

     /*   Intent intent = new Intent(DashBoardActivity.this, SearchAppointmentFilterActivity.class);
        intent.putExtra(ISTELEMEDICINE_KEY, true);
        intent.putExtra(LOGGED_IN_MODE, true);
        startActivity(intent);*/
    }

    public void outPatient(View view) {
        book_now_trasparent_layout.setVisibility(View.GONE);
        Intent intent = new Intent(DashBoardActivity.this, SearchAppointmentFilterActivity.class);
        intent.putExtra(ISTELEMEDICINE_KEY, false);
        intent.putExtra(LOGGED_IN_MODE, true);
        startActivity(intent);
    }

    private void bookAppointmentTypeDialog() {
        Dialog dialog = new Dialog(this, R.style.ios_dialog_style);
        dialog.setContentView(R.layout.dialog_appointment_selection);
        dialog.setCancelable(false);
        LightButton smartTouchBtn = dialog.findViewById(R.id.smart_touch_btn);
        LightButton notNowBtn = dialog.findViewById(R.id.not_now_btn);
        LightButton doNotRemindBtn = dialog.findViewById(R.id.do_not_remind_btn);
//        // if button is clicked, close the custom dialog

        smartTouchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(DashBoardActivity.this, SearchAppointmentFilterActivity.class);
                startActivity(intent);
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
                dialog.dismiss();
                Intent intent = new Intent(DashBoardActivity.this, SearchAppointmentFilterActivity.class);
                startActivity(intent);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void loadReports(View view) {

        reports_trasparent_layout.setVisibility(View.VISIBLE);

        if (TextUtil.getArabic(this)) {
            lab_report_tv.setText("تقارير المختبر");
            rad_reports_tv.setText("تقارير الأشعة");
        } else {
            lab_report_tv.setText("Laboratory\nReports");
            rad_reports_tv.setText("Radiology\nReports");
        }

       /* Dialog dialog = new Dialog(this, R.style.ios_dialog_style);
        dialog.setContentView(R.layout.dialog_report_selection);
        dialog.setCancelable(false);
        LightButton smartTouchBtn = dialog.findViewById(R.id.smart_touch_btn);
        LightButton notNowBtn = dialog.findViewById(R.id.not_now_btn);
        LightButton doNotRemindBtn = dialog.findViewById(R.id.do_not_remind_btn);
//        // if button is clicked, close the custom dialog

        smartTouchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(DashBoardActivity.this, LaboratoryReportsActivity.class);
                intent.putExtra(IS_LAB, true);
                startActivity(intent);
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
                dialog.dismiss();
                Intent intent = new Intent(DashBoardActivity.this, LaboratoryReportsActivity.class);
                intent.putExtra(IS_LAB, true);
                startActivity(intent);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();*/
    }

    public void loadAppointments(View view) {
        Intent intent = new Intent(this, AppointmentBottomActivity.class);
        intent.putExtra(UPCOMING_COUNT, upcommingAptsCount);
        intent.putExtra(PAST_COUNT, pastAptsCount);
        intent.putExtra(IS_CENTRE_OF_EXCELLENCE, isCenterOfExcellence);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    void apptGetSummaryBySType() {

        if (ConnectionUtil.isInetAvailable(this)) {
           this.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   showWaitDialog();
               }
           });
            placeholder.setVisibility(View.GONE);
            //progress_bar_upcoming.setVisibility(View.VISIBLE);
            String securityToken = new TinyDB(this).getString(Const.TOKEN_KEY);

            long patientId = new TinyDB(this).getLong(Const.PATIENT_ID, 0);

            String apptType = "2";

            if (!ValidationHelper.isNullOrEmpty(securityToken) && patientId != 0) {

                AppointmentsDTO appointmentsDTO = new AppointmentsDTO(securityToken, String.valueOf(patientId), apptType);

                RestClient.getInstance().apptGetSummaryBySType(appointmentsDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {
                        hideWaitDialog();
                        //progress_bar_upcoming.setVisibility(View.INVISIBLE);

                        ApptsModel apptsModel = (ApptsModel) result;

                        if (apptsModel != null) {

                            MobileOpResult mobileOpResult = apptsModel.getMobileOpResult();

                            if (mobileOpResult != null) {

                                if (adapterDoctors != null) {
                                    if (adapterDoctors.getItemCount() != 0) {
                                        adapterDoctors.clearAdapter();
                                    }
                                }
                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                    ArrayList<ApptsMiniModel> apptsMiniModel = apptsModel.getApptsMiniModel();

                                    if (!ValidationHelper.isNullOrEmpty(apptsMiniModel)) {
                                        placeholder.setVisibility(View.GONE);
                                        chevron_right.setVisibility(View.VISIBLE);
                                        chevron_left.setVisibility(View.VISIBLE);
                                        loadDoctorList(apptsMiniModel);
                                    } else {
                                        placeholder.setVisibility((View.VISIBLE));
                                        chevron_right.setVisibility(View.INVISIBLE);
                                        chevron_left.setVisibility(View.INVISIBLE);
                                        // loadDoctorList(apptsMiniModel);
                                    }

                                } else {
                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(DashBoardActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(DashBoardActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMesg);
                                }
                            } else {
                                ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMessage);
                            }
                        } else {
                            ErrorMessage.getInstance().showError(DashBoardActivity.this, errorMessage);
                        }
                    }
                });
            }
        } else {
            ErrorMessage.getInstance().showError(this, getString(R.string.internet_connection));
        }
    }

    AdapterDashboardUpcomingAppointments adapterDoctors = null;

    void loadDoctorList(ArrayList<ApptsMiniModel> apptsMiniModels) {
        adapterDoctors = new AdapterDashboardUpcomingAppointments(this, apptsMiniModels);
        upcomingApptsRecyclerView.setAdapter(adapterDoctors);
        upcomingApptsRecyclerView.setHasFixedSize(true);
        adapterDoctors.setOnCardClickListner(this);
        SnapHelper snapHelper = new LinearSnapHelper();
        upcomingApptsRecyclerView.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(upcomingApptsRecyclerView);
    }

    @Override
    public void OnCardClicked(ApptsMiniModel model, int pos, boolean cancel, boolean reschedule, boolean checkin, boolean viewServices, boolean isChangeInsurance) {

    }

    @OnClick(R.id.chevron_left)
    void left() {

        if (TextUtil.getEnglish(this)) {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 0) {
                upcomingApptsRecyclerView.smoothScrollToPosition(linearLayoutManager.findFirstVisibleItemPosition() - 1);
            } else {
                upcomingApptsRecyclerView.smoothScrollToPosition(0);
            }
        } else if (TextUtil.getArabic(this)) {
            upcomingApptsRecyclerView.smoothScrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);
        }
    }

    @OnClick(R.id.chevron_right)
    void right() {

        if (TextUtil.getEnglish(this)) {
            upcomingApptsRecyclerView.smoothScrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);
        } else if (TextUtil.getArabic(this)) {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 0) {
                upcomingApptsRecyclerView.smoothScrollToPosition(linearLayoutManager.findFirstVisibleItemPosition() - 1);
            } else {
                upcomingApptsRecyclerView.smoothScrollToPosition(0);
            }
        }
    }

    public void onFindDoctor(View view) {
        Intent intent = new Intent(this, FindDoctorsActivity.class);
        intent.putExtra(LOGGED_IN_MODE, true);
        startActivity(intent);
    }
}
