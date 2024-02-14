package com.telemedicine.myclinic.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.raywenderlich.android.validatetor.ValidateTor;
import com.reginald.editspinner.EditSpinner;
import com.telemedicine.myclinic.adapters.SpinnerCustomAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.LabReportsResponseModel;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.earliestslots.UpdateUserStatusDTO;
import com.telemedicine.myclinic.models.logonmodels.LogonModel;
import com.telemedicine.myclinic.models.logonmodels.SecurityToken;
import com.telemedicine.myclinic.models.quickregistration.QuickRegistrationDTO;
import com.telemedicine.myclinic.models.quickregistration.QuickRegistrationResponseModel;
import com.telemedicine.myclinic.models.registermodels.RegisterStageTwoDTO;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldCheckBox;
import com.telemedicine.myclinic.views.BoldTextInputLayout;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.LightEdittext;
import com.telemedicine.myclinic.views.LightSpinnerEdittext;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.webservices.RestClient;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

import static com.telemedicine.myclinic.util.Const.APPT_ID;
import static com.telemedicine.myclinic.util.Const.COMPANY_NAME_EN;
import static com.telemedicine.myclinic.util.Const.DATE;
import static com.telemedicine.myclinic.util.Const.DOCTOR_ID;
import static com.telemedicine.myclinic.util.Const.DOCTOR_IMAGE_URL;
import static com.telemedicine.myclinic.util.Const.DOCTOR_NAME;
import static com.telemedicine.myclinic.util.Const.DOCTOR_SPECIALITY;
import static com.telemedicine.myclinic.util.Const.EMAIL_KEY;
import static com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY;
import static com.telemedicine.myclinic.util.Const.IS_QUICK_REG;
import static com.telemedicine.myclinic.util.Const.OREGID_KEY;
import static com.telemedicine.myclinic.util.Const.PASSWORD_KEY;
import static com.telemedicine.myclinic.util.Const.PATIENT_ID;
import static com.telemedicine.myclinic.util.Const.REMEMBERME;
import static com.telemedicine.myclinic.util.Const.SERVICE_DATE;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_ID;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_NAME_AR;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_NAME_EN;
import static com.telemedicine.myclinic.util.Const.TMPASSWORD_KEY;
import static com.telemedicine.myclinic.util.Const.TMUSERNAME_KEY;
import static com.telemedicine.myclinic.util.Const.TOKEN_EXPIRY_KEY;
import static com.telemedicine.myclinic.util.Const.TOKEN_KEY;

public class AppointmentBookingRegisterActivity extends BaseActivity {

    @BindView(R.id.confirmBooking_btn)
    LightButton confirmBooking_btn;

    @BindView(R.id.dateOfBirthEdt_til)
    BoldTextInputLayout dateOfBirthEdt_til;

    @BindView(R.id.dateOfBirthEdt)
    LightEdittext dateOfBirthEdt;

    @BindView(R.id.doctor_image)
    CircleImageView doctorImageView;

    @BindView(R.id.doctor_name)
    BoldTextView doctorNameTv;

    @BindView(R.id.datetime_value)
    LightTextView datetimeValueTv;

    @BindView(R.id.doctor_profession)
    LightTextView doctor_profession;

    @BindView(R.id.location_name)
    BoldTextView location_name;

    @BindView(R.id.first_name_edt_til)
    BoldTextInputLayout first_name_edt_til;

    @BindView(R.id.first_name_edt)
    LightEdittext first_name_edt;

    @BindView(R.id.middle_name_edt_til)
    BoldTextInputLayout middle_name_edt_til;

    @BindView(R.id.middle_name_edt)
    LightEdittext middle_name_edt;

    @BindView(R.id.last_name_edt_til)
    BoldTextInputLayout last_name_edt_til;

    @BindView(R.id.last_name_edt)
    LightEdittext last_name_edt;

    @BindView(R.id.gender_til)
    BoldTextInputLayout gender_til;

    @BindView(R.id.gender_spinner)
    LightSpinnerEdittext gender_spinner;

    @BindView(R.id.phoneNumberEdt_til)
    BoldTextInputLayout phoneNumberEdt_til;

    @BindView(R.id.mobileNumberEt)
    LightEdittext mobileNumberEt;

    @BindView(R.id.emailEdt_til)
    BoldTextInputLayout emailEdt_til;

    @BindView(R.id.emailEt)
    LightEdittext emailEt;

    @BindView(R.id.password_till)
    BoldTextInputLayout password_till;

    @BindView(R.id.password_et)
    LightEdittext password_et;

    @BindView(R.id.confirmPassword_till)
    BoldTextInputLayout confirmPassword_till;

    @BindView(R.id.confirmPassword_et)
    LightEdittext confirmPassword_et;

    @BindView(R.id.gmailBtn)
    LinearLayoutCompat googleSignInBtn;


    @BindView(R.id.facebookBtn)
    LinearLayoutCompat facebookBtn;

    @BindView(R.id.fbLoginBtn)
    LoginButton fbLoginButton;

    @BindView(R.id.start_up_number)
    LightTextView startUpNumber;


    @BindView(R.id.chat_bot_bg)
    RelativeLayout chatBotBg;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.chatBot_rounded)
    View chatBot_rounded;

    @BindView(R.id.chat_bot_web_view)
    WebView chatBotWebView;

    @BindView(R.id.account_detail_hint)
    LightTextView account_detail_hint;

    @BindView(R.id.imSaudi)
    BoldCheckBox imSaudi;


    boolean isSaudi = false;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;

    String doctorName = "";
    String doctorDate = "";
    String doctorTime = "";
    String doctorImage = "";
    String company = "";

    long doctorId = 0;
    String specialityId = "";
    int gender = 0;
    boolean isTelemedicine = false;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;

    private FirebaseAuth mAuth;

    String specialityNameEn = "";
    String specialityNameAR = "";
    String companyName = "";
    boolean isChatBotVisible = false;
    String weekDay = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected int layout() {
        return R.layout.activity_appointment_booking_register;
    }


    private void init() {

        transformView();
        Intent intent = getIntent();
        tinyDB = new TinyDB(this);
        if (intent != null) {
            doctorId = intent.getLongExtra("DoctorId", 0);
            company = intent.getStringExtra("Company");
            doctorName = intent.getStringExtra("DoctorName");
            doctorDate = intent.getStringExtra("Date");
            doctorTime = intent.getStringExtra("Time");
            weekDay = intent.getStringExtra("week_Day");
            specialityNameEn = intent.getStringExtra(SPECIALITY_NAME_EN);
            specialityNameAR = intent.getStringExtra(SPECIALITY_NAME_AR);
            isTelemedicine = intent.getBooleanExtra(ISTELEMEDICINE_KEY, false);
            doctorImage = intent.getStringExtra("image");
            specialityId = intent.getStringExtra("specialityId");
        }


        if(TextUtil.getArabic(this)){
            first_name_edt.setKeyListener(DigitsKeyListener.getInstance("غظضذخثتشرقصجفعسنملةىكيطئءؤحزوهدبا "));
            first_name_edt.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

            middle_name_edt.setKeyListener(DigitsKeyListener.getInstance("غظضذخثتشرقصجفعسنملةىكيطئءؤحزوهدبا "));
            middle_name_edt.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

            last_name_edt.setKeyListener(DigitsKeyListener.getInstance("غظضذخثتشرقصجفعسنملةىكيطئءؤحزوهدبا "));
            last_name_edt.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        }else{
            first_name_edt.setKeyListener(DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ "));
            first_name_edt.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

            middle_name_edt.setKeyListener(DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ "));
            middle_name_edt.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

            last_name_edt.setKeyListener(DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ "));
            last_name_edt.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        }


        imSaudi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isSaudi = true;
                } else {
                    isSaudi = false;
                }
            }
        });

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

        company = tinyDB.getString(Const.COMPANY_ID);

        if (company.equals("nc01")) {
            companyName = getString(R.string.prince_sultan);
        } else if (company.equals("safa")) {
            companyName = getString(R.string.safa);
        }else if(company.equals("prc")){
            companyName = getString(R.string.prc);
        }

        populateViews();
        String[] genderArr = getResources().getStringArray(R.array.gender);
        ArrayList<String> genderArrList = new ArrayList<String>(Arrays.asList(genderArr)); //new ArrayList is only needed if you absolutely need an ArrayList

        android.widget.ListAdapter genderAdapter = new SpinnerCustomAdapter(this, R.layout.spinner_item_list, genderArrList);
        gender_spinner.setAdapter(genderAdapter);

        gender_spinner.setItemConverter(new EditSpinner.ItemConverter() {
            @Override
            public String convertItemToString(Object selectedItem) {
                String selectedItem1 = (String) selectedItem.toString();
                if (selectedItem1.equals(getString(R.string.male))) {
                    gender = 1;
                } else {
                    gender = 2;
                }
                return selectedItem1;
            }
        });
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDOB();
            }
        };

        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(AppointmentBookingRegisterActivity.this, (OnCompleteListener<Void>) task -> {
                            googleSignIn(v);
                        });
            }
        });

        mAuth = FirebaseAuth.getInstance();

        initGoogleAuth();
    }

    private void transformView() {
        account_detail_hint.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        dateOfBirthEdt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0);
        dateOfBirthEdt.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        gender_spinner.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        gender_spinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);
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
    //on google sign in button click
    public void googleSignIn(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Const.RC_SIGN_IN);
    }

    private void initGoogleAuth() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_Id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();

        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest data_request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject jsonObject,
                                    GraphResponse response) {
                                try {
                                    firebaseAuthWithGFacebook(loginResult.getAccessToken().getToken());
                                    if (jsonObject.has("id")) {
                                        //  logIn(Const.RG_FACEBOOK, loginResult.getAccessToken().getToken(),jsonObject.getString("email") );
                                        Log.d("FACEBOOK_DATA", jsonObject.getString("id"));
                                        Log.d("FACEBOOK_DATA", jsonObject.getString("name"));
                                        Log.d("FACEBOOK_DATA", jsonObject.getString("email"));

                                    }
                                } catch (Exception exception) {
                                }

                            }
                        });
                Bundle param = new Bundle();
                param.putString("fields", "name,email,id");
                data_request.setParameters(param);
                data_request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(AppointmentBookingRegisterActivity.this, "onCancel", Toast.LENGTH_LONG).show();
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(AppointmentBookingRegisterActivity.this, "onCancel", Toast.LENGTH_LONG).show();
            }
        });

        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }

               /* LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email", "name",
                        "id", "picture.type(large)"));*/
                fbLoginButton.performClick();
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
        if(TextUtil.getArabic(AppointmentBookingRegisterActivity.this)){
            doctor_profession.setText(specialityNameAR);
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

    String dobSt = "";
    String dobStEn = "";

    private void updateDOB() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        SimpleDateFormat sdfEn = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        dateOfBirthEdt.setText(sdf.format(myCalendar.getTime()));
        dobSt = sdf.format(myCalendar.getTime());
        dobStEn = sdfEn.format(myCalendar.getTime());
    }

    @OnClick(R.id.dateOfBirthEdt1)
    void datePicker() {
        dateOfBirthEdt_til.requestFocus();
        DatePickerDialog datePicker = new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePicker.setCanceledOnTouchOutside(false);
        datePicker.show();
    }

    int authProvider = 0;
    String authToken = "";
String patientId = "";
    public void confirmClicked(View view) {
        String fullName = TextUtil.getValue(first_name_edt_til, first_name_edt);
        String middleName = TextUtil.getValue(middle_name_edt_til, middle_name_edt);
        String lastName = TextUtil.getValue(last_name_edt_til, last_name_edt);

        if (ValidationHelper.isNullOrEmpty(fullName)) {
            TextUtil.tILError(this, first_name_edt_til, first_name_edt);
            ErrorMessage.getInstance().showValidationMessage(this, first_name_edt, getString(R.string.error_empty_fields));
            return;
        }

        if (ValidationHelper.isNullOrEmpty(middleName)) {
            TextUtil.tILError(this, middle_name_edt_til, middle_name_edt);
            ErrorMessage.getInstance().showValidationMessage(this, middle_name_edt, getString(R.string.error_empty_fields));
            return;
        }

        if (ValidationHelper.isNullOrEmpty(lastName)) {
            TextUtil.tILError(this, last_name_edt_til, last_name_edt);
            ErrorMessage.getInstance().showValidationMessage(this, last_name_edt, getString(R.string.error_empty_fields));
            return;
        }

       /* if (ValidationHelper.isNullOrEmpty(fullName)) {
            TextUtil.tILError(this, first_name_edt_til, first_name_edt);
            ErrorMessage.getInstance().showValidationMessage(this, first_name_edt, getString(R.string.error_empty_fields));
            return;
        }*/

        if (gender == 0) {
            TextUtil.tILError(this, gender_til, gender_spinner);
            ErrorMessage.getInstance().showValidationMessage(this, gender_spinner, getString(R.string.error_empty_fields));
            return;
        }

        String dateOfBirth = TextUtil.getValue(dateOfBirthEdt_til, dateOfBirthEdt);

        if (ValidationHelper.isNullOrEmpty(dateOfBirth)) {
            TextUtil.tILError(this, dateOfBirthEdt_til, dateOfBirthEdt);
            ErrorMessage.getInstance().showValidationMessage(this, dateOfBirthEdt, getString(R.string.error_empty_fields));
            return;
        }

        String phoneNumber = TextUtil.getValue(phoneNumberEdt_til, mobileNumberEt);

        if (ValidationHelper.isNullOrEmpty(phoneNumber)) {
            TextUtil.tILError(this, phoneNumberEdt_til, mobileNumberEt);
            ErrorMessage.getInstance().showValidationMessage(this, mobileNumberEt, getString(R.string.error_empty_fields));
            return;
        }

        if (!ValidationHelper.isNullOrEmpty(phoneNumber)) {
            phoneNumber = startUpNumber.getText().toString().trim() + TextUtil.getValue(phoneNumberEdt_til, mobileNumberEt);
            if (phoneNumber.length() != 10) {
                ErrorMessage.getInstance().showValidationMessage(this, mobileNumberEt, getString(R.string.please_enter_valid_number));
                return;
            }
        }

        phoneNumber = startUpNumber.getText().toString().trim() + TextUtil.getValue(phoneNumberEdt_til, mobileNumberEt);

        String email = TextUtil.getValue(emailEdt_til, emailEt);

        if (ValidationHelper.isNullOrEmpty(email)) {
            if (authProvider == 0) {
                authProvider = 7;
            }
           /* TextUtil.tILError(this, emailEdt_til, emailEt);
            return;*/
        } else {
            if (!ValidationHelper.isEmailValid(email)) {
                TextUtil.tILError(this, emailEdt_til, emailEt);
                ErrorMessage.getInstance().showValidationMessage(this, emailEt, getString(R.string.please_enter_valid_email));
                return;
            }
        }

        ValidateTor validateTor = new ValidateTor();
        String password = TextUtil.getValue(password_till, password_et);
        String confirmPassword = TextUtil.getValue(confirmPassword_till, confirmPassword_et);

        if (!ValidationHelper.isNullOrEmpty(password)) {
            if (!validateTor.isAtleastLength(password, 6)) { // 6 . and number
                ErrorMessage.getInstance().showValidationMessage(this, password_et, getString(R.string.limit_on_password_6_chars));
                TextUtil.tILError(this, password_till, password_et);
                return;
            }

            if (validateTor.hasAtleastOneDigit(password)
                //already comment
                //already comment end
            ) {
            } else {
                ErrorMessage.getInstance().showValidationMessage(this, password_et, getString(R.string.limit_on_password));
                TextUtil.tILError(this, password_till, password_et);
                return;
            }

            //TextUtil.tILError(this, password_till, password_et);
            if (ValidationHelper.isNullOrEmpty(confirmPassword)) {
                TextUtil.tILError(this, confirmPassword_till, confirmPassword_et);
                ErrorMessage.getInstance().showValidationMessage(this, confirmPassword_et, getString(R.string.error_empty_fields));
                return;
            }

            if (!validateTor.isAtleastLength(confirmPassword, 6)) { // 6 . and number
                ErrorMessage.getInstance().showValidationMessage(this, confirmPassword_et, getString(R.string.limit_on_password_6_chars));
                TextUtil.tILError(this, confirmPassword_till, confirmPassword_et);
                return;
            }


            if (validateTor.hasAtleastOneDigit(confirmPassword)
                //already comment
                //already comment end
            ) {
            } else {
                ErrorMessage.getInstance().showValidationMessage(this, confirmPassword_et, getString(R.string.limit_on_password));
                TextUtil.tILError(this, confirmPassword_till, confirmPassword_et);
                return;
            }

            if (!password.trim().equalsIgnoreCase(confirmPassword.trim())) {
                ErrorMessage.getInstance().showValidationMessage(this, password_et, getString(R.string.password_should_same));
                TextUtil.tILError(this, password_till, password_et);
                //already comment

                //already comment end
                return;
            }
        }

        String aptDate = doctorDate + ", " + doctorTime;
      /*  QuickRegistrationDTO quickRegistrationDTO = new QuickRegistrationDTO(email, phoneNumber, 10, password, aptDate, dobSt,
                String.valueOf(doctorId), fullName, gender, authToken, isTelemedicine, false, authProvider, 1,
                specialityId, company);*/

        String securityToken = tinyDB.getString(Const.TOKEN_KEY);

        int preferredLanguage;
        if(!TextUtil.getArabic(this)){
            preferredLanguage = 1;
        }else{
            preferredLanguage = 2;
        }
        QuickRegistrationDTO quickRegistrationDTO = new QuickRegistrationDTO(securityToken, email, phoneNumber,  password, dobStEn,
                fullName, middleName, lastName, gender, authToken, false, authProvider, preferredLanguage, isSaudi,
                specialityId, String.valueOf(doctorId), isTelemedicine, company, aptDate);

        showWaitDialog();
        if (ConnectionUtil.isInetAvailable(this)) {
            String finalPhoneNumber = phoneNumber;
            RestClient.getInstance().quickRegistration(quickRegistrationDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();
                    QuickRegistrationResponseModel quickRegistrationResponseModel = (QuickRegistrationResponseModel) result;

                    if (quickRegistrationResponseModel != null) {

                        MobileOpResult mobileOpResult = quickRegistrationResponseModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if ((mobileOpResult.getResult() == Success.SUCCESSCODE.getValue())) {
                                // Toast.makeText(AppointmentBookingRegisterActivity.this, quickRegistrationResponseModel.getVerificationCode().toString(), Toast.LENGTH_LONG).show();

                                if (!quickRegistrationResponseModel.getVerificationCode().isEmpty()) {
                                    patientId = quickRegistrationResponseModel.getPatientId();
                                    showDialog(finalPhoneNumber, quickRegistrationResponseModel.getVerificationCode());
                                } else {
                                    ErrorMessage.getInstance().showError(AppointmentBookingRegisterActivity.this, getString(R.string.verification_code_not_received));
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

                                ErrorMessage.getInstance().showError(AppointmentBookingRegisterActivity.this, errorMesg);
                            }

                        } else {
                            hideWaitDialog();
                            ErrorMessage.getInstance().showError(AppointmentBookingRegisterActivity.this, errorMessage);
                        }

                    } else {
                        hideWaitDialog();
                        ErrorMessage.getInstance().showError(AppointmentBookingRegisterActivity.this, errorMessage);
                    }
                }
            });
        } else {
            hideWaitDialog();
            ErrorMessage.getInstance().showWarning(AppointmentBookingRegisterActivity.this, getString(R.string.internet_connection));
        }
    }

    private void updateUserStatus(String mobileNumber) {
        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        UpdateUserStatusDTO updateUserStatusDTO = new UpdateUserStatusDTO(securityToken, mobileNumber);
        showWaitDialog();
        if (ConnectionUtil.isInetAvailable(this)) {
            RestClient.getInstance().updateUserStatus(updateUserStatusDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();
                    MobileOpResult mobileOpResult = (MobileOpResult) result;
                    if (mobileOpResult != null) {

                        if ((mobileOpResult.getResult() == Success.SUCCESSCODE.getValue())) {
                            // Toast.makeText(AppointmentBookingRegisterActivity.this, quickRegistrationResponseModel.toString(), Toast.LENGTH_LONG).show();
                            //launchAppointmentConfirm();
                            BookAppointment(true);

                        } else {
                            hideWaitDialog();
                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }

                            ErrorMessage.getInstance().showError(AppointmentBookingRegisterActivity.this, errorMesg);
                        }

                    } else {
                        hideWaitDialog();
                        ErrorMessage.getInstance().showError(AppointmentBookingRegisterActivity.this, errorMessage);
                    }
                }
            });
        } else {
            hideWaitDialog();
            ErrorMessage.getInstance().showWarning(AppointmentBookingRegisterActivity.this, getString(R.string.internet_connection));
        }
    }


    public void showDialog(String mobileNumber, String verificationCode) {

        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.question_dialogue);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            LightEdittext input = (LightEdittext) dialog.findViewById(R.id.add_sugar);
            RegularTextView questionTxt = (RegularTextView) dialog.findViewById(R.id.question_txt);
            questionTxt.setText(getString(R.string.please_enter_the_verification_code));

            RegularTextView dialogButton = dialog.findViewById(R.id.cancel);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            RegularTextView dialogButton1 = dialog.findViewById(R.id.add);
            dialogButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String value = input.getText().toString();

                    if (!ValidationHelper.isNullOrEmpty(value)) {

                        if (value.equalsIgnoreCase(verificationCode)) {
                            //update user status api
                            dialog.dismiss();
                            updateUserStatus(mobileNumber);
                            //  launchAppointmentConfirm();
                        } else {
                            ErrorMessage.getInstance().showValidationMessage(AppointmentBookingRegisterActivity.this, input, getString(R.string.sorry_not_match));
                        }

                    } else
                        ErrorMessage.getInstance().showValidationMessage(AppointmentBookingRegisterActivity.this, input, "Please enter value ");

                    dialog.dismiss();

                }
            });

            dialog.show();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task != null) {
                handleSignInResult(task);
            }
        } else {
            Timber.d("on Activity Result");
            AccessToken accessToken = AccessToken.getCurrentAccessToken();

            if (accessToken != null && !accessToken.isExpired()) {

                // firebaseAuthWithGFacebook(accessToken.getToken());

                // testing(accessToken);
                //  Toast.makeText(this, "facebook", Toast.LENGTH_LONG).show();
            } else {
                // Toast.makeText(this, "Invalid credentials", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == 111 && resultCode == RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            //  logIn(Const.RG_GOOGLE, account.getIdToken(), account.getEmail());
            firebaseAuthWithGoogle(account.getIdToken());

            // Toast.makeText(this, "Google", Toast.LENGTH_LONG).show();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            authProvider = 1;
                            authToken = mAuth.getUid();
                            confirmClicked(null);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Register", "signInWithCredential:failure", task.getException());
                            //  updateUI(null);
                        }
                    }
                });
    }


    private void firebaseAuthWithGFacebook(String accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            authProvider = 2;
                            authToken = mAuth.getUid();
                            confirmClicked(null);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Register", "signInWithCredential:failure", task.getException());
                            //  updateUI(null);
                        }
                    }

                });
    }

    public void onModify(View view) {
        finish();
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    public void onSignIn(View view) {
        BookAppointment(false);
    }

    private void BookAppointment(boolean isNew){

        Intent intent;

        if(isNew){
            intent = new Intent(this, ConfirmBookingQuickRegActivity.class);
        }else{
            intent = new Intent(this, LoginActivity.class);
            tinyDB.putBoolean(Const.BIOMETRIC_ENABLED, false);
            tinyDB.putBoolean(Const.BIOMETRIC_REMINDER, false);
        }
        String currentTimeFormatSt = "hh:mm a";
        String myFormatTimeSt = "HH:mm:ss";
        String finalFormatTime = "";
        SimpleDateFormat currentTFormat;
        if(TextUtil.getArabic(AppointmentBookingRegisterActivity.this)){
            currentTFormat =   new SimpleDateFormat(currentTimeFormatSt, Locale.ENGLISH);
        }else{
            currentTFormat =   new SimpleDateFormat(currentTimeFormatSt, Locale.getDefault());
        }

        SimpleDateFormat myTFormat;

        if(TextUtil.getArabic(AppointmentBookingRegisterActivity.this)){
            myTFormat =  new SimpleDateFormat(myFormatTimeSt, Locale.ENGLISH);
        }else{
            myTFormat =  new SimpleDateFormat(myFormatTimeSt, Locale.getDefault());
        }

        try {
            finalFormatTime = myTFormat.format(currentTFormat.parse(doctorTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String formatedTime = LocaleDateHelper.convertArabicToEnglish(doctorDate, "yyyy-MM-dd") + "T" + finalFormatTime;

        intent.putExtra(DOCTOR_NAME, doctorName);
        intent.putExtra(DOCTOR_SPECIALITY, "");
        intent.putExtra(DATE, formatedTime);
        intent.putExtra(SERVICE_DATE, formatedTime);
        intent.putExtra(SPECIALITY_ID, specialityId);
        intent.putExtra(PATIENT_ID, patientId);
        intent.putExtra(DOCTOR_ID, String.valueOf(doctorId));
        intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicine);
        intent.putExtra(SPECIALITY_NAME_EN, specialityNameEn);
        intent.putExtra(SPECIALITY_NAME_AR, specialityNameAR);
        intent.putExtra(COMPANY_NAME_EN, companyName);
        intent.putExtra(IS_QUICK_REG, true);
        intent.putExtra("week_Day", weekDay);
        intent.putExtra(DOCTOR_IMAGE_URL, doctorImage);
        intent.putExtra(Const.SELECTED_TIME, doctorTime);
        intent.putExtra(Const.APPT_BOOK_TYPE, 10);
        intent.putExtra(Const.AUTH_PROVIDER, authProvider);
        intent.putExtra(Const.IS_SECONDARY, false);
        intent.putExtra("Date_local", doctorDate);
        intent.putExtra(Const.ORDER_RAD, "");
        intent.putExtra(Const.IS_INSURANCE_KEY, false);
        intent.putExtra(Const.FOLLOW_UP_TYPE, false);
        intent.putExtra(APPT_ID, 0);
        startActivityForResult(intent, 111);
    }

    private void launchAppointmentConfirm() {
      /*  Intent intent = new Intent(this, AppointmentConfirmedAnonymousActivity.class);
        intent.putExtra("Date_local", doctorDate);
        intent.putExtra("DoctorName", doctorName);
        intent.putExtra("Time", doctorTime);
        intent.putExtra("image", doctorImage);
        intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicine);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivityForResult(intent, 111);*/
    }

}