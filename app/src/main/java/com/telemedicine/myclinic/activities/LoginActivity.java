package com.telemedicine.myclinic.activities;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.CancellationSignal;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoginStatusCallback;
import com.facebook.login.Login;
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
import com.telemedicine.myclinic.RefreshTokenWork;
import com.telemedicine.myclinic.activities.profile.AddProfileActivity;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.appointments.OrdersRadModel;
import com.telemedicine.myclinic.models.onlineConfig.OnlineConfigDTO;
import com.telemedicine.myclinic.models.onlineConfig.OnlineConfigModel;
import com.telemedicine.myclinic.models.patientMiniModels.PatientsMiniModel;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.logonmodels.LogonDTO;
import com.telemedicine.myclinic.models.logonmodels.LogonModel;
import com.telemedicine.myclinic.models.logonmodels.SecurityToken;
import com.telemedicine.myclinic.models.profileUpdate.GetProfileDTO;
import com.telemedicine.myclinic.models.profileUpdate.PatientRegistrationUpdate;
import com.telemedicine.myclinic.models.profileUpdate.ProfileUpdateModel;
import com.telemedicine.myclinic.myapplication.BuildConfig;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldCheckBox;
import com.telemedicine.myclinic.views.BoldTextInputLayout;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.views.RegularEdittext;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.webservices.RestClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

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
import static com.telemedicine.myclinic.util.Const.IS_QUICK_REG;
import static com.telemedicine.myclinic.util.Const.LAST_NAME;
import static com.telemedicine.myclinic.util.Const.MIDDLE_NAME;
import static com.telemedicine.myclinic.util.Const.OREGID_KEY;
import static com.telemedicine.myclinic.util.Const.PASSWORD_KEY;
import static com.telemedicine.myclinic.util.Const.PATIENT_GENDER;
import static com.telemedicine.myclinic.util.Const.PATIENT_NAME;
import static com.telemedicine.myclinic.util.Const.PATIENT_UPDATE_KEY;
import static com.telemedicine.myclinic.util.Const.REMEMBERME;
import static com.telemedicine.myclinic.util.Const.RG_BIO;
import static com.telemedicine.myclinic.util.Const.SERVICE_DATE;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_ID;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_NAME_AR;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_NAME_EN;
import static com.telemedicine.myclinic.util.Const.TMPASSWORD_KEY;
import static com.telemedicine.myclinic.util.Const.TMUSERNAME_KEY;
import static com.telemedicine.myclinic.util.Const.TOKEN_EXPIRY_KEY;
import static com.telemedicine.myclinic.util.Const.TOKEN_KEY;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.password_show_hide)
    ImageView passwordShowHide;

    @BindView(R.id.password)
    RegularEdittext password;

    @BindView(R.id.userName)
    RegularEdittext userName;

    @BindView(R.id.userName_til)
    BoldTextInputLayout userName_til;

    @BindView(R.id.password_til)
    BoldTextInputLayout password_til;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @BindView(R.id.toolbar_left_iv_back)
    ImageView toolbar_left_iv_back;

    @BindView(R.id.forgot)
    RegularTextView forgot;

    @BindView(R.id.sign_in)
    LightButton sign_in;

    @BindView(R.id.fingerPrintBtn)
    LightButton fingerPrintBtn;

    @BindView(R.id.version)
    LightTextView version;

    @BindView(R.id.urlTV)
    LightTextView urlTV;

    @BindView(R.id.gmailBtn)
    LinearLayoutCompat googleSignInBtn;

    @BindView(R.id.facebookBtn)
    LinearLayoutCompat facebookBtn;

    @BindView(R.id.fbLoginBtn)
    LoginButton fbLoginButton;

    boolean flag = false;

    TinyDB tinyDB;

    @BindView(R.id.remember_me)
    BoldCheckBox rememberMe;

    private String deviceToken = "";
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;

    private FirebaseAuth mAuth;
    private String doctorName;
    private String speciality;
    private String date;
    private String dateForService;
    private String specialityId;
    private String doctorId;
    private boolean isQuickReg;
    private boolean isTelemedcine;
    private String doctorImageUrl;
    private String selectedTime;
    private int apptBookType;
    private boolean isSecondary;
    private OrdersRadModel ordersRadModel;
    private boolean isInsurance;
    private boolean followUpAppt;
    private long apptId;

    String specialityNameEn = "";
    String specialityNameAR = "";
    String companyName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        tinyDB = new TinyDB(this);
        Intent intent = getIntent();
        if (intent != null) {
            boolean timeOut = intent.getBooleanExtra("timeout", false);
            if (timeOut) {
                //moveTaskToBack(true);
                tinyDB.putString(PATIENT_NAME, "");
                tinyDB.putString(PATIENT_GENDER, "");
                tinyDB.putString(Const.INSURANCE_ID, "0");
                tinyDB.putString(Const.MRN_NO, "");
                tinyDB.putLong(Const.PATIENT_ID, 0);
            }

            doctorName = intent.getStringExtra(DOCTOR_NAME);
            speciality = intent.getStringExtra(DOCTOR_SPECIALITY);
            date = intent.getStringExtra(DATE);
            dateForService = intent.getStringExtra(SERVICE_DATE);
            specialityId = intent.getStringExtra(SPECIALITY_ID);
            doctorId = intent.getStringExtra(DOCTOR_ID);
            isQuickReg = intent.getBooleanExtra(IS_QUICK_REG, false);
            isTelemedcine = intent.getBooleanExtra(Const.ISTELEMEDICINE_KEY, false);
            doctorImageUrl = intent.getStringExtra(DOCTOR_IMAGE_URL);
            selectedTime = intent.getStringExtra(Const.SELECTED_TIME);
            specialityNameEn = intent.getStringExtra(SPECIALITY_NAME_EN);
            specialityNameAR = intent.getStringExtra(SPECIALITY_NAME_AR);
            companyName = intent.getStringExtra(COMPANY_NAME_EN);
            apptBookType = intent.getIntExtra(Const.APPT_BOOK_TYPE, 0);
            isSecondary = intent.getBooleanExtra(Const.IS_SECONDARY, false);
            ordersRadModel = intent.getParcelableExtra(Const.ORDER_RAD);
            isInsurance = intent.getBooleanExtra(Const.IS_INSURANCE_KEY, false);
            followUpAppt = intent.getBooleanExtra(Const.FOLLOW_UP_TYPE, false);
            apptId = intent.getLongExtra(Const.APPT_ID, 0);

            if(isQuickReg){
                tinyDB.putString(DOCTOR_NAME,doctorName);
                tinyDB.putString(DOCTOR_SPECIALITY, specialityNameEn);
                tinyDB.putString(DATE, date);
                tinyDB.putString(SERVICE_DATE, dateForService);
                tinyDB.putString(SPECIALITY_ID, specialityId);
                tinyDB.putString(DOCTOR_ID, doctorId);
                tinyDB.putBoolean(Const.ISTELEMEDICINE_KEY, isTelemedcine);
                tinyDB.putString(DOCTOR_IMAGE_URL,doctorImageUrl);
                tinyDB.putString(Const.SELECTED_TIME, selectedTime);
                tinyDB.putInt(Const.APPT_BOOK_TYPE, apptBookType);
                tinyDB.putBoolean(Const.IS_SECONDARY, isSecondary);
                tinyDB.putBoolean(Const.IS_INSURANCE_KEY,isInsurance);
                tinyDB.putBoolean(Const.FOLLOW_UP_TYPE,followUpAppt);
                tinyDB.putLong(Const.APPT_ID, apptId);

                tinyDB.putString(COMPANY_NAME_EN, companyName);
                tinyDB.putString(SPECIALITY_NAME_EN, specialityNameEn);
                tinyDB.putString(SPECIALITY_NAME_AR, specialityNameAR);
            }
        }
        super.onCreate(savedInstanceState);

        showOrHideFingerPrintButton();

        init();
       // throw new RuntimeException("Test Crash");
    }

    private void showOrHideFingerPrintButton() {

        boolean isBiometricSupport = tinyDB.getBoolean(Const.BIOMETRIC_SUPPORT);
        int loggedInType = tinyDB.getInt(Const.LOGIN_TYPE);
        boolean isEnabled = tinyDB.getBoolean(Const.BIOMETRIC_ENABLED);

        if (isBiometricSupport) {
            if (loggedInType == Const.RG_FACEBOOK) {
                fingerPrintBtn.setVisibility(View.GONE);
            } else if (loggedInType == Const.RG_GOOGLE) {
                fingerPrintBtn.setVisibility(View.GONE);
            } else {
                if (isEnabled){
                    fingerPrintBtn.setVisibility(View.VISIBLE);
                }else{
                    fingerPrintBtn.setVisibility(View.GONE);
                }
            }
        } else {
            fingerPrintBtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void init() {
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

  /*      FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(this, new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                if (!task.isSuccessful()) {
                    Log.w("LoginActivity", "getInstanceId failed", task.getException());
                    return;
                }

                // Get new Instance ID token
                deviceToken  = task.getResult().getToken();
            }
        });*/


        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn(Const.RG_CUSTOM, "", "");
            }
        });
        initGoogleAuth();

        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(LoginActivity.this, (OnCompleteListener<Void>) task -> {
                            googleSignIn(v);
                        });
            }
        });


       // https:
//bamc.myclinic.com.sa/OS.Test.WebAPI.External/api/OSExternal/

        password.setTransformationMethod(new PasswordTransformationMethod());
        endSession();
        Intent intent = getIntent();
        if (intent != null) {
            boolean showHome = intent.getBooleanExtra("show", false);
            boolean timeOut = intent.getBooleanExtra("timeout", false);

            if (timeOut) {
                logout();
            }
            if (showHome) {
                toolbar_left_iv_back.setVisibility(View.GONE);
                toolbar_left_iv.setVisibility(View.VISIBLE);
            } else {
                toolbar_left_iv_back.setVisibility(View.VISIBLE);
                toolbar_left_iv.setVisibility(View.GONE);
            }


        }

        setRememberMe();

        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tinyDB.putBoolean(REMEMBERME, true);
                } else {
                    tinyDB.putBoolean(REMEMBERME, false);
                }
            }
        });

        transformView();
        String versionName = BuildConfig.VERSION_NAME;
        if (!ValidationHelper.isNullOrEmpty(versionName))
            version.setText("v" + versionName);

        //  version.setText("v" + versionName);
       // urlTV.setText(BuildConfig.BASE_URL);
        executor = ContextCompat.getMainExecutor(this);
        initBiomatric();
    }

    Executor executor;
    BiometricPrompt.AuthenticationCallback authenticationCallback;
    private CancellationSignal cancellationSignal;

    private CancellationSignal getCancellationSignal() {
        cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                //  notifyUser("Authentication was cancelled by the user");
            }
        });
        return cancellationSignal;
    }

    public void authenticateFingerPrint(View view) {
        BiometricPrompt biometricPrompt = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            biometricPrompt = new BiometricPrompt.Builder(this)
                    .setTitle(getString(R.string.smart_touch))
                    .setSubtitle(getString(R.string.login_using_your_bio_metric))
                    .setDescription("")
                    .setNegativeButton("Cancel", executor, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).build();
            biometricPrompt.authenticate(getCancellationSignal(), executor, authenticationCallback);
        }


    }

    String bioEmail = "";
    String bioPass = "";

    private void initBiomatric() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    notifyUser(errString.toString());
                }

                @Override
                public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                    super.onAuthenticationHelp(helpCode, helpString);
                }

                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    bioEmail = tinyDB.getString(EMAIL_KEY);
                    bioPass = tinyDB.getString(PASSWORD_KEY);
                    logonActivity(Const.RG_CUSTOM, "", "", RG_BIO);

                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                }
            };
        }

      /*  BiometricManager biometricManager =  BiometricManager.fr();
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, REQUEST_CODE);
                break;
        }*/
    }

    private void notifyUser(String message) {
        showToast(message);
    }

    @Override
    protected int layout() {
        return R.layout.activity_login;
    }

    private void initGoogleAuth() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_Id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();

        // fbLoginButton.setPermissions(Arrays.asList("public_profile,name,email,user_birthday"));
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
                Toast.makeText(LoginActivity.this, "onCancel",Toast.LENGTH_LONG).show();
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(LoginActivity.this, "onCancel",Toast.LENGTH_LONG).show();
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

    //on google sign in button click
    public void googleSignIn(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Const.RC_SIGN_IN);
    }


    public void logIn(int provider, String accessToken, String email) {

        String username = userName.getText().toString();
        String pass = "";

        if (provider == Const.RG_CUSTOM) {
            pass = password.getText().toString();

            if (ValidationHelper.isNullOrEmpty(username)) {
                TextUtil.tILError(this, userName_til, userName);
            }

            if (!ValidationHelper.isEmailValid(username)) {
                TextUtil.tILError(this, userName_til, userName);
            }

            if (ValidationHelper.isNullOrEmpty(pass)) {
                TextUtil.tILError(this, password_til, password);
            }

            if (ValidationHelper.isNullOrEmpty(username)) {
                ErrorMessage.getInstance().showValidationMessage(this, userName, getString(R.string.error_empty_fields));
                return;
            } else if (!ValidationHelper.isEmailValid(username)) {
                ErrorMessage.getInstance().showValidationMessage(this, userName, getString(R.string.please_enter_valid_email));
                return;
            } else if (ValidationHelper.isNullOrEmpty(pass)) {
                ErrorMessage.getInstance().showValidationMessage(this, password, getString(R.string.error_empty_fields));
                return;
            }
        }

        logonActivity(provider, accessToken, email, 0);
    }

    private void getOnlineConfig(ArrayList<PatientsMiniModel> patientsMiniModels) {

        String securityToken = tinyDB.getString(Const.TOKEN_KEY);

        OnlineConfigDTO onlineConfigDTO = new OnlineConfigDTO(securityToken);

        RestClient.getInstance().onlineConfig(onlineConfigDTO, new OnResultListener() {
            @Override
            public void onResult(Object result, boolean status, String errorMessage) {
                hideWaitDialog();

                OnlineConfigModel onlineConfigModel = (OnlineConfigModel) result;

                if (onlineConfigModel != null) {

                    MobileOpResult mobileOpResult = onlineConfigModel.getMobileOpResult();

                    if (mobileOpResult != null) {

                        if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                            tinyDB.putListObject(Const.ONLINE_CONFIG_KEY, onlineConfigModel.getOpsConfigs());

                            // list of existing patients if lsit is empty create the profile , only one patient then move to dashboard
                            // and if list is greater than then show the existing patient list.
                            if (ValidationHelper.isNullOrEmpty(patientsMiniModels)) {

                                // Intent intent = new Intent(LoginActivity.this, CompleteRegistrationActivity.class);
                                tinyDB.putBoolean(Const.IS_TENT, false);
                                startActivity(AddProfileActivity.Companion.getLaunchIntent(LoginActivity.this));
                                setResult(RESULT_OK);
                                finish();
                            } else if (!ValidationHelper.isNullOrEmpty(patientsMiniModels) && patientsMiniModels.size() == 1) {

                                if (!ValidationHelper.isNullOrEmpty(patientsMiniModels.get(0).getNameEn())) {
                                    if (TextUtil.getEnglish(LoginActivity.this))
                                        tinyDB.putString(PATIENT_NAME, patientsMiniModels.get(0).getNameEn());
                                    else if (TextUtil.getArabic(LoginActivity.this)) {
                                        tinyDB.putString(PATIENT_NAME, patientsMiniModels.get(0).getNameAr());
                                    }
                                }

                                if (!ValidationHelper.isNullOrEmpty(patientsMiniModels.get(0).getGender()))
                                    tinyDB.putString(PATIENT_GENDER, patientsMiniModels.get(0).getGender());

                                tinyDB.putInt(Const.PATIENT_CATEGORY, patientsMiniModels.get(0).getPatientCategory());

                                // get the data
                                long insuranceId = 0;

                                if (patientsMiniModels != null && !ValidationHelper.isNullOrEmpty(patientsMiniModels.get(0).getInsuranceCards())) {
                                    insuranceId = patientsMiniModels.get(0).getInsuranceCards().get(0).getInsuranceId();
                                }

                                String mrnNo = patientsMiniModels.get(0).getMRN();
                                long patientId = patientsMiniModels.get(0).getPatientId();

                                // save the value
                                tinyDB.putString(Const.INSURANCE_ID, String.valueOf(insuranceId));


                                tinyDB.putLong(Const.PATIENT_ID, patientId);

                                if(!ValidationHelper.isNullOrEmpty(patientsMiniModels.get(0).getRegCardUrl())){
                                    tinyDB.putString(Const.PATIENT_REG_CARD_URL,patientsMiniModels.get(0).getRegCardUrl());
                                }else{
                                    tinyDB.putString(Const.PATIENT_REG_CARD_URL,"");
                                }

                                if (!ValidationHelper.isNullOrEmpty(mrnNo)){
                                    tinyDB.putString(Const.MRN_NO, mrnNo);

                                    Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra(IS_QUICK_REG, isQuickReg);
                                    intent.putExtra(EXISTING_PATIENT, patientsMiniModels);
                                    tinyDB.putBoolean(Const.IS_TENT, false);
                                    startActivity(intent);
                                    setResult(RESULT_OK);
                                    finish();

                                }else{
                                 /*   Intent intent = new Intent(LoginActivity.this, AddProfileActivity.class);
                                  //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                   // intent.putExtra(IS_QUICK_REG, isQuickReg);
                                    intent.putExtra(EXISTING_PATIENT, patientsMiniModels);
                                    tinyDB.putBoolean(Const.IS_TENT, true);
                                    startActivity(intent);
                                    setResult(RESULT_OK);
                                    finish();*/
                                    myAccount(patientsMiniModels);

                                }

                            } else if (!ValidationHelper.isNullOrEmpty(patientsMiniModels) && patientsMiniModels.size() > 1) {
                                Intent intent = new Intent(LoginActivity.this, SwitchPatientActivity.class);
                                intent.putExtra(IS_QUICK_REG, isQuickReg);
                                intent.putExtra(EXISTING_PATIENT, patientsMiniModels);
                                startActivityForResult(intent, 100);
                            }

                        } else {

                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                if (TextUtil.getEnglish(LoginActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                else if (TextUtil.getArabic(LoginActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + getString(R.string.technical_info) + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }

                            ErrorMessage.getInstance().showError(LoginActivity.this, errorMesg);
                        }

                    } else {
                        ErrorMessage.getInstance().showError(LoginActivity.this, errorMessage);
                    }

                } else ErrorMessage.getInstance().showError(LoginActivity.this, errorMessage);
            }
        });
    }

    public void forget(View view) {
        Intent intent = new Intent(this, ForgotActivity.class);
        startActivity(intent);
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.password_show_hide)
    void showHide() {
        if (flag) {
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordShowHide.setImageDrawable(getResources().getDrawable(R.drawable.hide));
            flag = false;
        } else {
            flag = true;
            passwordShowHide.setImageDrawable(getResources().getDrawable(R.drawable.show));
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    void logonActivity(int provider, String accessToken, String email, int bio) {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            String deviceType = getString(R.string.device_type);
            String versionRelease = Build.VERSION.RELEASE;
            String versionName = BuildConfig.VERSION_NAME;
            String username = "";
            String pass = "";
            String locality = tinyDB.getString(Const.LOCALITY);

            if (provider == Const.RG_CUSTOM) {
                if (RG_BIO == bio) {
                    username = bioEmail;
                    pass = bioPass;
                } else {
                    username = userName.getText().toString();
                    pass = password.getText().toString();
                }
            } else {
                username = email;
            }

            /*FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(this, new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {

                }
            });*/

            LogonDTO logonDTO = new LogonDTO(username, pass, deviceType, versionRelease, deviceToken, versionName, locality, provider, accessToken);

            String finalUsername = username;
            tinyDB.putString(Const.ACCESS_TOKEN, accessToken);
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

                                // save the email and password
                                tinyDB.putInt(Const.LOGIN_TYPE, provider);
                                tinyDB.putBoolean(Const.IS_FROM_LOGIN, true);
                                tinyDB.putString(EMAIL_KEY, logonModel.getEmail());
                                tinyDB.putString(PASSWORD_KEY, finalPass);

                                // save the security token object in local db.
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

                                // Save the Vsee Credentials
                                String tmUsername = logonModel.getTMUsername();
                                if (!ValidationHelper.isNullOrEmpty(tmUsername)) {
                                    tinyDB.putString(TMUSERNAME_KEY, tmUsername);
                                }

                                String tmPassword = logonModel.getTMPassword();
                                if (!ValidationHelper.isNullOrEmpty(tmPassword)) {
                                    tinyDB.putString(TMPASSWORD_KEY, tmPassword);
                                }

                                String oRegId = logonModel.getORegId();
                                if (!ValidationHelper.isNullOrEmpty(oRegId)) {
                                    tinyDB.putString(OREGID_KEY, oRegId);
                                }

                                getOnlineConfig(logonModel.getPatientsMiniModels());

                            } else {
                                hideWaitDialog();
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(LoginActivity.this, errorMesg);
                            }

                        } else {
                            hideWaitDialog();
                            ErrorMessage.getInstance().showError(LoginActivity.this, errorMessage);
                        }

                    } else {
                        hideWaitDialog();
                        ErrorMessage.getInstance().showError(LoginActivity.this, errorMessage);
                    }
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(LoginActivity.this, getString(R.string.internet_connection));
        }
    }

/*    private void createWorkRequest() {
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder
                (RefreshTokenWork.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork("sendLocation", ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest);
    }

    // Then i'm learning the state of Work
    private WorkInfo.State getStateOfWork() {
        try {
            if (WorkManager.getInstance(this).getWorkInfosForUniqueWork("sendLocation").get().size() > 0) {
                return WorkManager.getInstance(this).getWorkInfosForUniqueWork("sendLocation")
                        .get().get(0).getState();
                // this can return WorkInfo.State.ENQUEUED or WorkInfo.State.RUNNING
                // you can check all of them in WorkInfo class.
            } else {
                return WorkInfo.State.CANCELLED;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            return WorkInfo.State.CANCELLED;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return WorkInfo.State.CANCELLED;
        }
    }

    // If work not ( ENQUEUED and RUNNING ) i'm running the work.
// You can check with other ways. It's up to you.
    private void startServerWork() {
        if (getStateOfWork() != WorkInfo.State.ENQUEUED && getStateOfWork() != WorkInfo.State.RUNNING) {
            createWorkRequest();
            Log.wtf("startLocationUpdates", ": server started");
        } else {
            Log.wtf("startLocationUpdates", ": server already working");
        }
    }*/

    void setRememberMe() {

        if (tinyDB != null && tinyDB.getBoolean(REMEMBERME)) {

            String email = tinyDB.getString(EMAIL_KEY);

            if (!ValidationHelper.isNullOrEmpty(email)) {
                userName.setText(email);
            }

            rememberMe.setChecked(true);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }

        if (requestCode == Const.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(task != null){
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
    }

    public void testing(AccessToken accessToken){
       // Toast.makeText(this, accessToken.toString(), Toast.LENGTH_LONG).show();
    }

   /* private void handleFacebookResult(AccessToken accessToken) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject jsonObject,
                            GraphResponse response) {
                        try {
                            logonActivity(Const.RG_FACEBOOK, accessToken.getToken(),"");
                            if (jsonObject.has("id")) {

                                Log.d("FACEBOOK_DATA", jsonObject.getString("id"));
                                Log.d("FACEBOOK_DATA", jsonObject.getString("name"));
                                Log.d("FACEBOOK_DATA", jsonObject.getString("email"));
                            }
                        } catch (Exception exception) {
                        }

                    }
                });
        Bundle param = new Bundle();
        param.putString("fields", "name,email,id,picture.type(large),birthday,friends");
        data_request.setParameters(param);
        data_request.executeAsync();
    }*/

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
                            logonActivity(Const.RG_GOOGLE, mAuth.getUid(), "", 0);

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
                            logonActivity(Const.RG_FACEBOOK, mAuth.getUid(), "", 0);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Register", "signInWithCredential:failure", task.getException());
                            //  updateUI(null);
                        }
                    }

                });
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.toolbar_left_iv_back)
    void onBack() {
        onBackPressed();
    }

    void transformView() {

        if (TextUtil.getEnglish(this)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) forgot.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            forgot.setLayoutParams(params);
            userName.setGravity(Gravity.LEFT);
            password.setGravity(Gravity.LEFT);
            rememberMe.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

            // sign in button
            /*RelativeLayout.LayoutParams forgorParams = (RelativeLayout.LayoutParams) sign_in.getLayoutParams();
           // forgorParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            sign_in.setLayoutParams(forgorParams);*/

        } else if (TextUtil.getArabic(this)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) forgot.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            forgot.setLayoutParams(params);
            userName.setGravity(Gravity.RIGHT);
            password.setGravity(Gravity.RIGHT);
            rememberMe.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            sign_in.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);


            //Home Button

            // back button
            LinearLayout.LayoutParams homeParams = (LinearLayout.LayoutParams) toolbar_left_iv.getLayoutParams();
            homeParams.gravity = Gravity.LEFT;
            toolbar_left_iv.setLayoutParams(homeParams);

            // back button
            LinearLayout.LayoutParams backLayoutParams = (LinearLayout.LayoutParams) toolbar_left_iv_back.getLayoutParams();
            backLayoutParams.gravity = Gravity.RIGHT;
            toolbar_left_iv_back.setLayoutParams(backLayoutParams);
            toolbar_left_iv_back.setRotationY(180);

            // sign in button
            /*LinearLayout.LayoutParams forgorParams = (LinearLayout.LayoutParams) sign_in.getLayoutParams();
            forgorParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
            sign_in.setLayoutParams(forgorParams);*/
        }
    }

    void endSession() {
        tinyDB.putBoolean("session", false);
        //tinyDB.putBoolean("session_inter", false);
        //Const.ALLOWED_TOKEN_CALL = true;
    }

    void startSession() {
        tinyDB.putBoolean("session", true);
        //tinyDB.putBoolean("session_inter", true);
        // Const.ALLOWED_TOKEN_CALL = true;


    }

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
                    TextUtil.dialgoue(LoginActivity.this, getString(R.string.relogin));
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, "Internet is not available");
        }
    }

    void myAccount(ArrayList<PatientsMiniModel> patientsMiniModels) {

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

                                                Intent intent = new Intent(LoginActivity.this, AddProfileActivity.class);
                                                //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                // intent.putExtra(IS_QUICK_REG, isQuickReg);

                                                intent.putExtra(EXISTING_PATIENT, patientsMiniModels);
                                                intent.putExtra(FIRST_NAME, patientRegistrationUpdate.getFirstName());
                                                intent.putExtra(LAST_NAME, patientRegistrationUpdate.getLastName());
                                                intent.putExtra(MIDDLE_NAME, patientRegistrationUpdate.getMiddleName());
                                                intent.putExtra(DATE_OF_BIRTH, patientRegistrationUpdate.getBirthDate());
                                                intent.putExtra(GENDER, patientRegistrationUpdate.getGender());

                                                tinyDB.putBoolean(Const.IS_TENT, true);
                                                startActivity(intent);
                                                setResult(RESULT_OK);
                                                finish();

                                             /*
                                                Intent intent = new Intent(LoginActivity.this, AddProfileActivity.class);
                                                intent.putExtra(PATIENT_UPDATE_KEY, patientRegistrationUpdate);
                                                startActivity(intent);*/
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    } else {
                                        String errorMesg = "";

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                            if (TextUtil.getEnglish(LoginActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                            else if (TextUtil.getArabic(LoginActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";

                                        }

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                            errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                        }

                                        if(mobileOpResult.getResult() == 600 || mobileOpResult.getResult() == 700){
                                            ErrorMessage.getInstance().showErrorYellow(LoginActivity.this, errorMesg);
                                        }else{
                                            ErrorMessage.getInstance().showError(LoginActivity.this, errorMesg);
                                        }
                                       // ErrorMessage.getInstance().showError(LoginActivity.this, errorMesg);
                                    }
                                } else
                                    ErrorMessage.getInstance().showError(LoginActivity.this, errorMessage);
                            } else
                                ErrorMessage.getInstance().showError(LoginActivity.this, errorMessage);
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
}
