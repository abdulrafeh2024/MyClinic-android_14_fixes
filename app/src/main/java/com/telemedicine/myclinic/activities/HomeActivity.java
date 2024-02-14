package com.telemedicine.myclinic.activities;

import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.telemedicine.myclinic.adapters.DoctorsProfileAdapter;
import com.telemedicine.myclinic.adapters.BannersAdapter;
import com.telemedicine.myclinic.adapters.TextPatientViewAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.fragments.CompanySelectionFragment;
import com.telemedicine.myclinic.injection.ViewModelFactory;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.HomeModel;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.company.Company;
import com.telemedicine.myclinic.models.dashboardModels.MarketingClickableUrlsModel;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;
import com.telemedicine.myclinic.models.homemodels.PatientComments;
import com.telemedicine.myclinic.models.logonmodels.LogonDTO;
import com.telemedicine.myclinic.models.logonmodels.LogonModel;
import com.telemedicine.myclinic.models.logonmodels.SecurityToken;
import com.telemedicine.myclinic.models.profilecreatoinmodels.SecurityTokenDTO;
import com.telemedicine.myclinic.myapplication.ApptTermsConditionsActivity;
import com.telemedicine.myclinic.myapplication.BuildConfig;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.viewmodels.DashboardViewModel;
import com.telemedicine.myclinic.views.ArabicRegularTextView;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.webservices.RestClient;
import com.viewpagerindicator.CirclePageIndicator;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.telemedicine.myclinic.util.Const.EMAIL_KEY;
import static com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY;
import static com.telemedicine.myclinic.util.Const.LOGGED_IN_MODE;
import static com.telemedicine.myclinic.util.Const.OREGID_KEY;
import static com.telemedicine.myclinic.util.Const.PASSWORD_KEY;
import static com.telemedicine.myclinic.util.Const.RG_BIO;
import static com.telemedicine.myclinic.util.Const.TMPASSWORD_KEY;
import static com.telemedicine.myclinic.util.Const.TMUSERNAME_KEY;
import static com.telemedicine.myclinic.util.Const.TOKEN_EXPIRY_KEY;
import static com.telemedicine.myclinic.util.Const.TOKEN_KEY;

public class HomeActivity extends BaseActivity implements CompanySelectionFragment.OnCompanySelectListener, DoctorsProfileAdapter.OnCardClickListner{

    @BindView(R.id.pager)
    ViewPager mViewPager;

    @BindView(R.id.book_now_trasparent_layout)
    ConstraintLayout book_now_trasparent_layout;

    @BindView(R.id.indicator)
    CirclePageIndicator indicator;

    @BindView(R.id.chat_bot_bg)
    RelativeLayout chatBotBg;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.chatBot_rounded)
    View chatBot_rounded;

    @BindView(R.id.chat_bot_web_view)
    WebView chatBotWebView;


    @BindView(R.id.doctor_recycler_view)
    RecyclerView doctorRecyclerView;

    @BindView(R.id.pager_patient)
    ViewPager mViewPagerPatient;

    @BindView(R.id.indicator_patient)
    CirclePageIndicator indicatorPatient;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbarLeftImage;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.home_menu)
    RegularTextView home_menu;

    @BindView(R.id.about_us_menu)
    RegularTextView about_us_menu;

    @BindView(R.id.spec_menu)
    RegularTextView spec_menu;

    @BindView(R.id.contact_us_menu)
    RegularTextView contact_us_menu;


    @BindView(R.id.nav_view)
    NavigationView navigationView;

/*    @BindView(R.id.register_btn)
    LightButton register_btn;*/

 /*   @BindView(R.id.logon_btn)
    LightButton logon_btn;*/

    /*@BindView(R.id.buttons_container)
    RelativeLayout buttons_container;*/

   /* @BindView(R.id.our_doctors_title)
    LightTextView our_doctors_title;*/

  /*  @BindView(R.id.view_all_container)
    LinearLayout view_all_container;*/

   /* @BindView(R.id.line)
    View line;*/

    @BindView(R.id.toolbar_right_iv)
    ImageView toolbar_right_iv;

    @BindView(R.id.english)
    RegularTextView english;

    @BindView(R.id.clinic_visit_tv)
    BoldTextView clinic_visit_tv;

    @BindView(R.id.virtual_tv)
    BoldTextView virtual_tv;

    @BindView(R.id.arabic)
    ArabicRegularTextView arabic;

    boolean isChatBotVisible = false;

    private int _currentPage = 0;

    private int _numPages = 0;

    private int _currentPageComment = 0;

    private int _numPagesComment = 0, _numPagesCommentSize = 0;

    @BindView(R.id.chevron_left)
    ImageView chevronLeft;

    @BindView(R.id.chevron_right)
    ImageView chevronRight;

    @BindView(R.id.eng_ar_btns_container)
    LinearLayout eng_ar_btns_container;

    @BindView(R.id.switch_title)
    RegularTextView switch_title;

    LinearLayoutManager linearLayoutManager = null;

    @Inject
    ViewModelFactory viewModelFactory;

    DashboardViewModel viewModel;
    private String deviceToken = "";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // changeLanguage();

        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(DashboardViewModel.class);
        changeLocaleOfViews();
        setDefaultDate();
        init();
        loadCompany();
        checkBiometricSupport();
        translateButtonChange();

        if (ConnectionUtil.isInetAvailable(this))
            callLoadHomeService();
        else
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
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

    private Boolean checkBiometricSupport() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (!keyguardManager.isKeyguardSecure()) {
            tinyDB.putBoolean(Const.BIOMETRIC_SUPPORT, false);
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC)
                != PackageManager.PERMISSION_GRANTED) {
            tinyDB.putBoolean(Const.BIOMETRIC_SUPPORT, false);
            return false;
        }

        /*if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            tinyDB.putBoolean(Const.BIOMETRIC_SUPPORT, true);
        }*/

        tinyDB.putBoolean(Const.BIOMETRIC_SUPPORT, true);
        return true;
    }

    private void init() {


    //    FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this);

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

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);

        if (TextUtil.getEnglish(this))
            linearLayoutManager.setReverseLayout(false);
        else if (TextUtil.getArabic(this))
            linearLayoutManager.setReverseLayout(true);

        // hide it
        toolbarLeftImage.setVisibility(View.GONE);
        doctorRecyclerView.setLayoutManager
                (linearLayoutManager);
        doctorRecyclerView.setHasFixedSize(true);
        loadsBanners(null);

        mAuth = FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("LoginActivity", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        deviceToken = task.getResult();
                    }
                });
    }

    @Override
    protected int layout() {
        return R.layout.home_activity_drawer;
    }

    void loadsBanners(ArrayList<MarketingClickableUrlsModel> bannersUrls) {

       /* TinyDB tinyDB = new TinyDB(this);
        if (!ValidationHelper.isNullOrEmpty(bannersUrls)) {
            tinyDB.putListObject(Const.BANNERS_KEY, bannersUrls);
        } else {
            bannersUrls = tinyDB.getListObject(Const.BANNERS_KEY, String.class);
        }*/

        if (!ValidationHelper.isNullOrEmpty(bannersUrls)) {

            if (TextUtil.getArabic(this))
                Collections.reverse(bannersUrls);

            mViewPager.setAdapter(new BannersAdapter(HomeActivity.this, bannersUrls));
            indicator.setViewPager(mViewPager);

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

    public void Register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.book_now)
    void bookNow() {
       // bookAppointmentTypeDialog();

        book_now_trasparent_layout.setVisibility(View.VISIBLE);
        // android:text="@string/virtual_consultation"
        // android:text="@string/clinic_visit"

        if(TextUtil.getArabic(this)){
            clinic_visit_tv.setText("زيارات العيادة");
            virtual_tv.setText("المحادثات الطبية عن بعد");
        }else{
            clinic_visit_tv.setText("Clinic Visit");
            virtual_tv.setText("Virtual Consultation");
        }
    }

    @OnClick(R.id.book_now_trasparent_layout)
    void bookNowTrasparentLayout() {
        // bookAppointmentTypeDialog();
        book_now_trasparent_layout.setVisibility(View.GONE);
    }

    void loadDoctorList(ArrayList<DoctorsProfile> doctorsProfiles) {
        DoctorsProfileAdapter doctorsProfileAdapter = new DoctorsProfileAdapter(this, doctorsProfiles, false, true);
        doctorRecyclerView.setAdapter(doctorsProfileAdapter);
        doctorsProfileAdapter.setOnCardClickListner(this);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(doctorRecyclerView);

        if (doctorsProfiles.size() == 1)
            navigationInVisible();
        else
            navigationVisible();


        //autoRVScroll(doctorsProfileAdapter);
    }

    void loadPatientComments(ArrayList<PatientComments> patientComments) {


        if (TextUtil.getArabic(this))
            Collections.reverse(patientComments);


        mViewPagerPatient.setAdapter(new TextPatientViewAdapter(HomeActivity.this, patientComments));
        indicatorPatient.setViewPager(mViewPagerPatient);

        _numPagesComment = patientComments.size() + 1;
        _numPagesCommentSize = patientComments.size();

        if (TextUtil.getArabic(this))
            mViewPagerPatient.setCurrentItem(_numPagesComment);

        mViewPagerPatient.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {

            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {

                _currentPageComment = position;

            }
        });

        changeComment();
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
                Intent intent = new Intent(HomeActivity.this, SearchAppointmentFilterActivity.class);
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
                Intent intent = new Intent(HomeActivity.this, SearchAppointmentFilterActivity.class);
                startActivity(intent);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @OnClick(R.id.toolbar_right_iv)
    void righDrawer() {
        if (TextUtil.getEnglish(this)){
            mDrawer.openDrawer(Gravity.LEFT, true);
            home_menu.setText(getString(R.string.home));
            about_us_menu.setText(getString(R.string.about_us));
            contact_us_menu.setText(getString(R.string.contact_us));
            spec_menu.setText(getString(R.string.doctors_amp_specialities));
            switch_title.setText(getString(R.string.switch_language));
        }

        else if(TextUtil.getArabic(this)){
            mDrawer.openDrawer(Gravity.RIGHT, true);
            home_menu.setText("الصفحة الرئيسية");
            about_us_menu.setText("نبذة عنا");
            contact_us_menu.setText("تواصل معنا");
            spec_menu.setText("أطباؤنا وتخصصاتنا");
            switch_title.setText("تغيير اللغة");
        }

    }

    public void LogOn(View view) {
      /*  TinyDB tinyDB = new TinyDB(this);
        if (tinyDB != null) {
            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
            if (patientId != 0) {
                Intent intent = new Intent(this, DashBoardActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        }*/

        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        if (isChatBotVisible) {
            openChatBotBg(null);
            return;
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


    void callLoadHomeService() {

        showWaitDialog();

        RestClient.getInstance().loadPublicHome(new OnResultListener() {

            @Override
            public void onResult(Object result, boolean status, String errorMessage) {

                hideWaitDialog();

                HomeModel homeModel = (HomeModel) result;

                if (homeModel != null) {

                    MobileOpResult mobileOpResult = homeModel.getMobileOpResult();

                    if (mobileOpResult != null) {

                        int resultCode = mobileOpResult.getResult();

                        if (resultCode == Success.SUCCESSCODE.getValue()) {

                            ArrayList<MarketingClickableUrlsModel> bannersUrls = homeModel.getMarketingClickableUrlsModels();

                            if (!ValidationHelper.isNullOrEmpty(bannersUrls)) {

                                loadsBanners(bannersUrls);
                               SecurityToken securityToken =  homeModel.getSecurityToken();

                                if (securityToken != null) {
                                    String token = securityToken.getToken();
                                    if (!ValidationHelper.isNullOrEmpty(token)) {
                                        tinyDB.putString(TOKEN_KEY, token);
                                    }

                                    String tokenExpiry = securityToken.getExpiryDate();
                                    if (!ValidationHelper.isNullOrEmpty(tokenExpiry)) {
                                        tinyDB.putString(TOKEN_EXPIRY_KEY, tokenExpiry);
                                    }
                                }
                            }

                            ArrayList<DoctorsProfile> doctorsProfiles = homeModel.getDoctorsProfiles();

                            if (!ValidationHelper.isNullOrEmpty(doctorsProfiles)) {

                                loadDoctorList(doctorsProfiles);
                            }

                            ArrayList<PatientComments> patientComments = homeModel.getPatientComments();

                            if (!ValidationHelper.isNullOrEmpty(patientComments)) {
                                loadPatientComments(patientComments);
                            }

                           // loginService();

                        } else {
                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }
                            ErrorMessage.getInstance().showError(HomeActivity.this, errorMesg);
                        }
                    } else {
                        ErrorMessage.getInstance().showError(HomeActivity.this, errorMessage);
                    }
                } else {
                    ErrorMessage.getInstance().showError(HomeActivity.this, errorMessage);
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        loginService();
       // Toast.makeText(this, "On Resume Called", Toast.LENGTH_LONG).show();
    }


    void loginService() {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            String deviceType = getString(R.string.device_type);
            String versionRelease = Build.VERSION.RELEASE;
            String versionName = BuildConfig.VERSION_NAME;
            String username = "MobileDevUser@myclinic.com.sa";
            String pass = "Mob#Dev$1@User";
            String locality = tinyDB.getString(Const.LOCALITY);

            /*FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(this, new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {

                }
            });*/

            LogonDTO logonDTO = new LogonDTO(username, pass, deviceType, versionRelease, deviceToken, versionName, locality, Const.RG_CUSTOM, "");

            String finalUsername = username;
            tinyDB.putString(Const.ACCESS_TOKEN, "");
            tinyDB.putString(Const.DEVICE_TOKEN, deviceToken);
            tinyDB.putString(Const.USER_NAME_LOGIN, username);

            String finalPass = pass;
            RestClient.getInstance().logOn(logonDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    LogonModel logonModel = (LogonModel) result;

                    if (logonModel != null) {

                        MobileOpResult mobileOpResult = logonModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if ((mobileOpResult.getResult() == Success.SUCCESSCODE.getValue())) {
hideWaitDialog();
                                SecurityToken securityToken = logonModel.getSecurityToken();

                                if (securityToken != null) {
                                    String token = securityToken.getToken();
                                    if (!ValidationHelper.isNullOrEmpty(token)) {
                                        tinyDB.putString(TOKEN_KEY, token);
                                        if(viewModel.getCompanies().isEmpty()){
                                            loadCompany();
                                        }
                                    }
                                }
                            } else {
                                hideWaitDialog();
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())
                                && mobileOpResult.getTechnicalErrorMessage().equals("Application version on device not compatible.")) {
                                   if(TextUtil.getArabic(HomeActivity.this)){
                                       showVersionCheckDialog(mobileOpResult.getBusinessErrorMessageAr());
                                   }else{
                                       showVersionCheckDialog(mobileOpResult.getBusinessErrorMessageEn());
                                   }
                                }else{

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(HomeActivity.this, errorMesg);
                                }
                            }
                        } else {
                            hideWaitDialog();
                            ErrorMessage.getInstance().showError(HomeActivity.this, errorMessage);
                        }

                    } else {
                        hideWaitDialog();
                        ErrorMessage.getInstance().showError(HomeActivity.this, errorMessage);
                    }
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(HomeActivity.this, getString(R.string.internet_connection));
        }
    }

    IOSDialog iosDialog2 = null;
    private void showVersionCheckDialog(String businessErrorMessage) {

        iosDialog2 = TextUtil.dialgoue(HomeActivity.this, businessErrorMessage,getString(R.string.update), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent appStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.myclinic.ksa"));
                    appStoreIntent.setPackage("com.android.vending");

                    startActivity(appStoreIntent);
                } catch (android.content.ActivityNotFoundException exception) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.myclinic.ksa")));
                }

            }
        });
    }

    private void changeLanguage(){
        if(TextUtil.getArabic(this)){
            Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            Configuration configuration = getBaseContext().getResources().getConfiguration();
            configuration.setLocale(locale);
            createConfigurationContext(configuration);
        }
    }
    public void Home(View view) {
      /*  Intent intent = new Intent(this, BrowserActivityPayment.class);
        intent.putExtra("url", Const.HOME);
        startActivity(intent);*/

        mDrawer.closeDrawers();

    }

    public void ViewALL(View view) {
        Intent intent = new Intent(this, BrowserActivityPayment.class);
        intent.putExtra("url", getString(R.string.viewall_url));
        startActivity(intent);

    }

    public void AboutUs(View view) {
        Intent intent = new Intent(this, BrowserActivityPayment.class);
        intent.putExtra("url", getString(R.string.about_us_url));
        startActivity(intent);
    }

    public void DoctorsSpecialist(View view) {
//        Intent intent = new Intent(this, BrowserActivityPayment.class);
//        intent.putExtra("url", getString(R.string.dctr_specialist_utl));
//        startActivity(intent);


//        Intent intent = new Intent(this, MedicationDeliveriesActivity.class);
//    //    intent.putParcelableArrayListExtra("orderLab", OrdersLab);
//        startActivity(intent);

        Intent intent = new Intent(this, MedicationDeliveriesActivity.class);
       // intent.putExtra("url", getString(R.string.dctr_specialist_utl));
        startActivity(intent);
    }

    public void ContactUs(View view) {
        Intent intent = new Intent(this, BrowserActivityPayment.class);
        intent.putExtra("url", getString(R.string.contact_us_url));
        startActivity(intent);
    }

   /* @OnClick(R.id.moreinfo)
    void moreinfo() {

        Intent intent = new Intent(this, MoreInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }*/

    void changeLocaleOfViews() {

        languageViews();
        menuChange();
        if (TextUtil.getEnglish(this))
            englishView();
        else if (TextUtil.getArabic(this))
            arabicView();
    }

    void arabicView() {

        //region buttons logon and register
       // RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) logon_btn.getLayoutParams();
        //params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //logon_btn.setLayoutParams(params1); //causes layout update
        //logon_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);

      //  RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) register_btn.getLayoutParams();
       // params.addRule(RelativeLayout.RIGHT_OF, R.id.logon_btn);
        //params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //params.setMargins(40, 0, 0, 0);  // left, top, right, bottom
        //register_btn.setLayoutParams(params); //causes layout update
        //register_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);

        //buttons_container.setGravity(Gravity.CENTER_HORIZONTAL);
        //endregion

        //region View all and doctor title
        //RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) our_doctors_title.getLayoutParams();
        //params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //params2.setMargins(0, 0, 40, 0);  // left, top, right, bottom
        //our_doctors_title.setLayoutParams(params2); //causes layout update

       // RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) view_all_container.getLayoutParams();
        //params3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        //params3.setMargins(40, 0, 0, 0);  // left, top, right, bottom
        //view_all_container.setLayoutParams(params3); //causes layout update

       // RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) line.getLayoutParams();
        // params4.addRule(RelativeLayout.RIGHT_OF, R.id.view_all_container);
     //   params4.addRule(RelativeLayout.BELOW, R.id.our_doctors_title);
        //params4.setMargins(40, 32, 40, 0);  // left, top, right, bottom

        //line.setLayoutParams(params4);

        //endregion

    }

    void menuChange() {

        if (TextUtil.getEnglish(this)) {

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar_right_iv.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            toolbar_right_iv.setLayoutParams(params);

            DrawerLayout.LayoutParams drawerParam = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
            drawerParam.gravity = Gravity.LEFT;

        } else if (TextUtil.getArabic(this)) {

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) toolbar_right_iv.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            toolbar_right_iv.setLayoutParams(params1);

            DrawerLayout.LayoutParams drawerParam = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
            drawerParam.gravity = Gravity.RIGHT;
        }
    }

    void englishView() {

    //    RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) register_btn.getLayoutParams();
      //  params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //register_btn.setLayoutParams(params1);//causes layout update
        //register_btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);

     //   RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) logon_btn.getLayoutParams();
       // params.addRule(RelativeLayout.RIGHT_OF, R.id.register_btn);
       // params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //params.setMargins(40, 0, 0, 0);  // left, top, right, bottom
        //logon_btn.setLayoutParams(params); //causes layout update
        //logon_btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);

        //buttons_container.setGravity(Gravity.CENTER_HORIZONTAL);


        //region View all and doctor title

       // RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) our_doctors_title.getLayoutParams();
        //params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        //our_doctors_title.setLayoutParams(params2); //causes layout update

//        RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) view_all_container.getLayoutParams();
  //      params3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    //    view_all_container.setLayoutParams(params3); //causes layout update

       // RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) line.getLayoutParams();
       // params4.addRule(RelativeLayout.LEFT_OF, R.id.view_all_container);
       // params4.addRule(RelativeLayout.BELOW, R.id.our_doctors_title);
       // params4.setMargins(50, 32, 0, 0);  // left, top, right, bottom

        //line.setLayoutParams(params4);

        //endregion

    }

    void languageViews() {

        if (TextUtil.getEnglish(this)) {

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) switch_title.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            switch_title.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) eng_ar_btns_container.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            eng_ar_btns_container.setLayoutParams(params1);

        } else if (TextUtil.getArabic(this)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) switch_title.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            switch_title.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) eng_ar_btns_container.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            eng_ar_btns_container.setLayoutParams(params1);
        }
    }

    int checkZero = -1;
    boolean firstTime = false;

    void changeSlider() {

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {

                if (TextUtil.getEnglish(HomeActivity.this)) {
                    if (_currentPage == _numPages) {
                        _currentPage = 0;
                    }
                    mViewPager.setCurrentItem(_currentPage++, true);
                } else if (TextUtil.getArabic(HomeActivity.this)) {

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

    int checkZeroComment = -1;
    boolean firstTimeComment = false;

    void changeComment() {

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update1 = new Runnable() {
            public void run() {

                if (TextUtil.getEnglish(HomeActivity.this)) {
                    if (_currentPageComment == _numPagesComment) {
                        _currentPageComment = 0;
                    }

                    mViewPagerPatient.setCurrentItem(_currentPageComment++, true);
                } else if (TextUtil.getArabic(HomeActivity.this)) {

                    if (_currentPageComment == 0) {
                        if (firstTimeComment)
                            checkZeroComment = _currentPageComment;
                        _currentPageComment = _numPagesComment;
                        firstTimeComment = true;
                    } else {
                        checkZeroComment = -1;
                    }

                    if (checkZeroComment == 0)
                        mViewPagerPatient.setCurrentItem(checkZeroComment, true);
                    else
                        mViewPagerPatient.setCurrentItem(_currentPageComment--, true);

                }
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update1);
            }
        }, 0, 5000);

    }

    void navigationVisible() {
        chevronLeft.setVisibility(View.VISIBLE);
        chevronRight.setVisibility(View.VISIBLE);
    }

    void navigationInVisible() {
        chevronLeft.setVisibility(View.INVISIBLE);
        chevronRight.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.chevron_left)
    void left() {

        if (TextUtil.getEnglish(this)) {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 0) {
                doctorRecyclerView.smoothScrollToPosition(linearLayoutManager.findFirstVisibleItemPosition() - 1);
            } else {
                doctorRecyclerView.smoothScrollToPosition(0);
            }
        } else if (TextUtil.getArabic(this)) {
            doctorRecyclerView.smoothScrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);
        }
    }

    @OnClick(R.id.chevron_right)
    void right() {

        if (TextUtil.getEnglish(this)) {
            doctorRecyclerView.smoothScrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);
        } else if (TextUtil.getArabic(this)) {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 0) {
                doctorRecyclerView.smoothScrollToPosition(linearLayoutManager.findFirstVisibleItemPosition() - 1);
            } else {
                doctorRecyclerView.smoothScrollToPosition(0);
            }
        }
    }

    boolean langaugeChanged = false;

    @OnClick(R.id.arabic)
    public void Ara(View view) {
      /*  tinyDB.putString(Const.LOCALITY, "2");
        tinyDB.putString(Const.LOCALITY_, "2_");
        TextUtil.dialgoue(this, getString(R.string.restart_app_etc));
        refresh();*/

        if (!langaugeChanged) {
            langaugeChanged = true;
            tinyDB.putString(Const.LOCALITY_, "2_");
            // refresh();
            arbc();
        }
    }

    @OnClick(R.id.english)
    public void Eng(View view) {
       /* tinyDB.putString(Const.LOCALITY, "1");
        tinyDB.putString(Const.LOCALITY_, "1_");
        TextUtil.dialgoue(this, getString(R.string.restart_app_etc));
        refresh();*/
        if (!langaugeChanged) {
            langaugeChanged = true;
            tinyDB.putString(Const.LOCALITY_, "1_");
            englsh();
            // refresh();
        }
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

    void refresh() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    void autoRVScroll(DoctorsProfileAdapter adapter) {

        final int speedScroll = 6000;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            boolean flag = true;

            @Override
            public void run() {
                if (count < adapter.getItemCount()) {
                    if (count == adapter.getItemCount() - 1) {
                        flag = false;
                    } else if (count == 0) {
                        flag = true;
                    }
                    if (flag) count++;
                    else count--;

                    doctorRecyclerView.smoothScrollToPosition(count);
                    handler.postDelayed(this, speedScroll);
                }
            }
        };

        handler.postDelayed(runnable, speedScroll);
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

    public void onContactUs(View view) {
        Intent intent = new Intent(this, ContactUsPopActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }


    public void onFindClinic(View view) {
        Intent intent = new Intent(this, FindClinicActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }
    public void virtualBooking(View view) {
        book_now_trasparent_layout.setVisibility(View.GONE);

  /*      String patientName = tinyDB.getString(Const.PATIENT_NAME);
        long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
        String key = patientName + patientId;
        tinyDB.putString(Const.COMPANY_ID, "");
        boolean accepted = tinyDB.getBoolean(key);

        if (accepted) {
            Intent intent = new Intent(this, SearchAppointmentFilterActivity.class);
            intent.putExtra(ISTELEMEDICINE_KEY, true);
            intent.putExtra(LOGGED_IN_MODE, true);
            startActivity(intent);
            return;
        }

        Intent intent = new Intent(this, ApptTermsConditionsActivity.class);
        intent.putExtra(Const.TERMS_CONDITIONS_KEY, termsConditionTxt);
        intent.putExtra(LOGGED_IN_MODE, true);
        intent.putExtra(ISTELEMEDICINE_KEY, true);
        startActivity(intent);*/

        Intent intent = new Intent(HomeActivity.this, SearchAppointmentFilterActivity.class);
        intent.putExtra(ISTELEMEDICINE_KEY, true);
        startActivity(intent);
    }

    public void outPatient(View view) {
        book_now_trasparent_layout.setVisibility(View.GONE);
        Intent intent = new Intent(this, SearchAppointmentFilterActivity.class);
        intent.putExtra(ISTELEMEDICINE_KEY, false);
        startActivity(intent);

      /*  Intent intent = new Intent(HomeActivity.this, SearchAppointmentFilterActivity.class);
        startActivity(intent);*/

    /*    CompanySelectionFragment.newInstance(viewModel.getCompanies())
                .show(getSupportFragmentManager(), "CompanySelectionFragment");

        viewModel.setTeleAppointment(false);*/
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

    void nonTeleAppt() {
        Intent intent = new Intent(this, SearchAppointmentFilterActivity.class);
        intent.putExtra(ISTELEMEDICINE_KEY, false);
        startActivity(intent);
    }

    public void onFindDoctor(View view) {


        Intent intent = new Intent(this, FindDoctorsActivity.class);
        intent.putExtra(LOGGED_IN_MODE, false);
        startActivity(intent);
    }

    @Override
    public void OnCardClicked(DoctorsProfile model, int pos) {
        Intent intent = new Intent(HomeActivity.this, SearchAppointmentResultActivity.class);
        intent.putExtra("startDate", strDate);
        intent.putExtra("SpecialityId", "");
        intent.putExtra("DoctorId", model.getDoctorId().toString());
        intent.putExtra("Date", "");
        intent.putExtra("CompanyId", "");
        intent.putExtra(LOGGED_IN_MODE, false);
        intent.putExtra(ISTELEMEDICINE_KEY, false);
        startActivity(intent);
    }

    String strDate = "";
    final Calendar myCalendar = Calendar.getInstance();
    void setDefaultDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        strDate = sdf.format(myCalendar.getTime());
    }
}


