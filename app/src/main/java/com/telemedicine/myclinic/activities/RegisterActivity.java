package com.telemedicine.myclinic.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.raywenderlich.android.validatetor.ValidateTor;
import com.telemedicine.myclinic.activities.profile.AddProfileActivity;
import com.telemedicine.myclinic.adapters.ExistingPatientAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.logonmodels.LogonDTO;
import com.telemedicine.myclinic.models.logonmodels.LogonModel;
import com.telemedicine.myclinic.models.logonmodels.SecurityToken;
import com.telemedicine.myclinic.models.onlineConfig.OnlineConfigDTO;
import com.telemedicine.myclinic.models.onlineConfig.OnlineConfigModel;
import com.telemedicine.myclinic.models.patientMiniModels.PatientsMiniModel;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.registermodels.RegisterOneModel;
import com.telemedicine.myclinic.models.registermodels.RegisterOneDTO;
import com.telemedicine.myclinic.models.registermodels.RegisterStageTwoDTO;
import com.telemedicine.myclinic.models.registermodels.RegisterTwoModel;
import com.telemedicine.myclinic.myapplication.BuildConfig;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.BoldTextInputLayout;
import com.telemedicine.myclinic.views.LightEdittext;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.views.RegularEdittext;
import com.telemedicine.myclinic.webservices.RestClient;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static com.telemedicine.myclinic.util.Const.EMAIL_KEY;
import static com.telemedicine.myclinic.util.Const.EXISTING_PATIENT;
import static com.telemedicine.myclinic.util.Const.OREGID_KEY;
import static com.telemedicine.myclinic.util.Const.PASSWORD_KEY;
import static com.telemedicine.myclinic.util.Const.PATIENT_GENDER;
import static com.telemedicine.myclinic.util.Const.PATIENT_NAME;
import static com.telemedicine.myclinic.util.Const.TMPASSWORD_KEY;
import static com.telemedicine.myclinic.util.Const.TMUSERNAME_KEY;
import static com.telemedicine.myclinic.util.Const.TOKEN_EXPIRY_KEY;
import static com.telemedicine.myclinic.util.Const.TOKEN_KEY;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.people_recycler)
    RecyclerView peopleRecycler;

    @BindView(R.id.email_edt_til)
    BoldTextInputLayout emailEdtTil;

    @BindView(R.id.email_edt)
    RegularEdittext emailEdt;

    @BindView(R.id.mobile_edt_til)
    BoldTextInputLayout mobileEdtTil;

    @BindView(R.id.mobile_edt)
    LightEdittext mobileEdt;

    @BindView(R.id.verify_acount_form)
    ScrollView verifyAccountForm;

    @BindView(R.id.basic_detail_form)
    ScrollView basicDetailForm;

    @BindView(R.id.confirm_container)
    NestedScrollView confirmContainer;

    @BindView(R.id.one)
    LightTextView one;

    @BindView(R.id.two)
    LightTextView two;

    @BindView(R.id.three)
    LightTextView three;

    @BindView(R.id.verify_edt)
    LightEdittext verifyEdt;

    @BindView(R.id.verify_edt_til)
    BoldTextInputLayout verifyEdtTil;

    @BindView(R.id.countdown_txt)
    LightTextView countDownTxt;

    @BindView(R.id.resend_btn)
    LightButton resendBtn;

    @BindView(R.id.set_edt_til)
    BoldTextInputLayout setEdtTil;

    @BindView(R.id.reenter_edt_til)
    BoldTextInputLayout reenterEdtTil;

    @BindView(R.id.set_password_edt)
    LightEdittext setPasswordEdt;

    @BindView(R.id.reenter_password_edt)
    LightEdittext reEnterPasswordEdt;

    @BindView(R.id.start_up_number)
    LightTextView startUpNumber;

    @BindView(R.id.confirm_message)
    LightTextView confirmMessage;

    @BindView(R.id.list_container)
    RelativeLayout listContainer;

    @BindView(R.id.password_show_hide)
    ImageView passwordShowHide;

    @BindView(R.id.reenter_password_show_hide)
    ImageView reEnterpasswordShowHide;

    @BindView(R.id.container)
    RelativeLayout container;

    @BindView(R.id.container2)
    RelativeLayout container2;

    @BindView(R.id.toolbar_left_iv)
    ImageView backButton;

    @BindView(R.id.lower_title_tv)
    LightTextView lower_title_tv;

    @BindView(R.id.next_reg)
    LightButton nextReg;

    @BindView(R.id.gmailBtn)
    LinearLayoutCompat googleSignInBtn;

    @BindView(R.id.fb_Btn)
    LightButton fbBtn;

    @BindView(R.id.facebookBtn)
    LinearLayoutCompat facebookBtn;

    @BindView(R.id.fbLoginBtn)
    LoginButton fbLoginButton;

    @BindView(R.id.verifyBtn)
    LightButton verifyBtn;

    @BindView(R.id.logon_btn)
    LightButton logon_btn;

    String oTPCode = "";

    private String deviceToken = "";
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected int layout() {
        return R.layout.activity_register;
    }


    void init() {

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
      /*  FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(this, new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                if (!task.isSuccessful()) {
                    Log.w("LoginActivity", "getInstanceId failed", task.getException());
                    return;
                }

                // Get new Instance ID token
                deviceToken = task.getResult().getToken();
            }
        });*/
        viewTransform();
        initGoogleAuth();

        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(RegisterActivity.this, (OnCompleteListener<Void>) task -> {
                            googleSignIn(v);
                        });
            }
        });

        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }
                fbLoginButton.performClick();
            }
        });

        container.setVisibility(View.GONE);
        container2.setVisibility(View.GONE);
        peopleRecycler.setLayoutManager(new LinearLayoutManager(this));
        verifyEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
/*
                if (!ValidationHelper.isNullOrEmpty(s) && oTPCode.equalsIgnoreCase(s.toString())) {
                    container.setVisibility(View.VISIBLE);
                    container2.setVisibility(View.VISIBLE);
                    verifyEdt.setEnabled(false);
                    verifyEdtTil.setBackgroundResource(R.drawable.grey_non_editable);
                    countDownTxt.setVisibility(View.GONE);
                    if (countDownTimer != null)
                        countDownTimer.cancel();

                    resendBtn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                    resendBtn.setEnabled(false);

                } else {
                   // container.setVisibility(View.GONE);
                    //container2.setVisibility(View.GONE);

                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setPasswordEdt.setTransformationMethod(new PasswordTransformationMethod());
        reEnterPasswordEdt.setTransformationMethod(new PasswordTransformationMethod());

       /* mobileEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                emailEdt.setBackgroundResource(R.drawable.border_space);
            }
        });*/
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
                                        Log.d("FACEBOOK_DATA", jsonObject.getString("id"));
                                        Log.d("FACEBOOK_DATA", jsonObject.getString("name"));
                                        Log.d("FACEBOOK_DATA", jsonObject.getString("email"));
                                    }
                                } catch (Exception exception) {
                                }

                            }
                        });
                Bundle param = new Bundle();
                param.putString("fields", "id,name,email");
                data_request.setParameters(param);
                data_request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    public void Next(View view) {

        String emailStr = TextUtil.getValue(emailEdtTil, emailEdt);
        String mobileStr = TextUtil.getValue(mobileEdtTil, mobileEdt);

        if (ValidationHelper.isNullOrEmpty(emailStr)) {
            TextUtil.tILError(this, emailEdtTil, emailEdt);
        }
        if (!ValidationHelper.isEmailValid(emailStr)) {
            TextUtil.tILError(this, emailEdtTil, emailEdt);
        }
        if (ValidationHelper.isNullOrEmpty(mobileStr)) {
            TextUtil.tILError(this, mobileEdtTil, mobileEdt);
        }

        if (ValidationHelper.isNullOrEmpty(emailStr)) {
            ErrorMessage.getInstance().showValidationMessage(this, emailEdt, getString(R.string.error_empty_fields));
            return;
        } else if (!ValidationHelper.isEmailValid(emailStr)) {
            ErrorMessage.getInstance().showValidationMessage(this, emailEdt, getString(R.string.please_enter_valid_email));
            return;
        } else if (ValidationHelper.isNullOrEmpty(mobileStr)) {
            ErrorMessage.getInstance().showValidationMessage(this, mobileEdt, getString(R.string.error_empty_fields));
            return;
        }

        if (!ValidationHelper.isNullOrEmpty(mobileStr)) {
            mobileStr = startUpNumber.getText().toString().trim() + TextUtil.getValue(mobileEdtTil, mobileEdt);
            if (mobileStr.length() != 10) {
                ErrorMessage.getInstance().showValidationMessage(this, mobileEdt, getString(R.string.please_enter_valid_number));
                return;
            }
        }

        if (ConnectionUtil.isInetAvailable(this)) {

            mobileStr = startUpNumber.getText().toString().trim() + TextUtil.getValue(mobileEdtTil, mobileEdt);

            showWaitDialog();

            try {

                String locale = new TinyDB(this).getString(Const.LOCALITY);

                RegisterOneDTO registerOneDTO = new RegisterOneDTO(emailStr, mobileStr, locale);

                RestClient.getInstance().registerStageOne(registerOneDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        hideWaitDialog();

                        RegisterOneModel registerOneModel = (RegisterOneModel) result;

                        if (registerOneModel != null) {

                            MobileOpResult mobileOpResult = registerOneModel.getMobileOpResult();

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                    if (basicDetailForm.getVisibility() == View.VISIBLE) {
                                        verifyAccountForm.setVisibility(View.VISIBLE);
                                        basicDetailForm.setVisibility(View.GONE);
                                        two.setBackgroundResource(R.drawable.color_round_circle);
                                        two.setTextColor(getResources().getColor(R.color.colorWhite));

                                        oTPCode = registerOneModel.getVerificationCode();
                                        //verifyEdt.setText(oTPCode);756777
                                    //    Toast.makeText(RegisterActivity.this, oTPCode, Toast.LENGTH_LONG).show();
                                    }
                                    countDown();

                                } else {
                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(RegisterActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(RegisterActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(RegisterActivity.this, errorMesg);
                                }
                            } else {
                                ErrorMessage.getInstance().showError(RegisterActivity.this, errorMessage);
                            }
                        } else {
                            ErrorMessage.getInstance().showError(RegisterActivity.this, errorMessage);
                        }
                    }
                });
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }

        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }

    }

    public void Verify(View view) {

        ValidateTor validateTor = new ValidateTor();
        String otp = TextUtil.getValue(verifyEdtTil, verifyEdt);
        String set = TextUtil.getValue(setEdtTil, setPasswordEdt);
        String reenter = TextUtil.getValue(reenterEdtTil, reEnterPasswordEdt);
        String email = TextUtil.getValue(emailEdtTil, emailEdt);
        String mobile = TextUtil.getValue(mobileEdtTil, mobileEdt);
        mobile = startUpNumber.getText().toString() + mobile;

        //modi
        if (ValidationHelper.isNullOrEmpty(otp)) {
            TextUtil.tILError(this, verifyEdtTil, verifyEdt);
            ErrorMessage.getInstance().showValidationMessage(this, verifyEdt, getString(R.string.error_empty_fields));
            return;
        }
        //modi end

        showWaitDialog();
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideWaitDialog();
                if (!ValidationHelper.isNullOrEmpty(otp) && oTPCode.equalsIgnoreCase(otp.toString())) {

                    //modi
                    if (verifyAccountForm.getVisibility() == View.VISIBLE) {
                        container.setVisibility(View.VISIBLE);
                        container2.setVisibility(View.VISIBLE);
                        verifyAccountForm.setVisibility(View.GONE);
                        confirmContainer.setVisibility(View.VISIBLE);
                        three.setBackgroundResource(R.drawable.color_round_circle);
                        three.setTextColor(getResources().getColor(R.color.colorWhite));
                        backButton.setVisibility(View.GONE);
                    }

                    countDownTxt.setVisibility(View.GONE);
                    if (countDownTimer != null)
                        countDownTimer.cancel();

                    resendBtn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                    resendBtn.setEnabled(false);

                    confirmMessage.setText(R.string.profile_created_successfully);
                    listContainer.setVisibility(View.INVISIBLE);
                    lower_title_tv.setVisibility(View.GONE);

                    return;
                    //modi end

                } else {
                    ErrorMessage.getInstance().showValidationMessage(RegisterActivity.this, verifyEdt, getString(R.string.valid_verification_code));
                    TextUtil.tILError(RegisterActivity.this, verifyEdtTil, verifyEdt);
                    return;
                }
            }
        }, 500);

    }

    public void LogOn(View view) {
     /*   Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();*/

        registerStageTwo(Const.RG_CUSTOM, "");
    }


    void tempDAta() {
          /*if (ValidationHelper.isNullOrEmpty(otp)) {
            TextUtil.tILError(this, verifyEdtTil, verifyEdt);
        }*/

          /* if (ValidationHelper.isNullOrEmpty(otp)) {
            ErrorMessage.getInstance().showValidationMessage(this, verifyEdt, getString(R.string.error_empty_fields));
            return;
        } */

        // && validateTor.hasAtleastOneUppercaseCharacter(set)
        // && validateTor.hasAtleastOneSpecialCharacter(set)

        //&& validateTor.hasAtleastOneUppercaseCharacter(reenter)
        // && validateTor.hasAtleastOneSpecialCharacter(reenter)

        // ErrorMessage.getInstance().showValidationMessage(this, reEnterPasswordEdt, getString(R.string.password_should_same));
    }

    private void registerStageTwo(int provider, String accessToken) {

        ValidateTor validateTor = new ValidateTor();
        String otp = TextUtil.getValue(verifyEdtTil, verifyEdt);
        String set = "";
        String reenter = "";
        String email = TextUtil.getValue(emailEdtTil, emailEdt);
        String mobile = TextUtil.getValue(mobileEdtTil, mobileEdt);
        mobile = startUpNumber.getText().toString() + mobile;

        if (provider == Const.RG_CUSTOM) {

            set = TextUtil.getValue(setEdtTil, setPasswordEdt);
            reenter = TextUtil.getValue(reenterEdtTil, reEnterPasswordEdt);

            if (ValidationHelper.isNullOrEmpty(set)) {
                TextUtil.tILError(this, setEdtTil, setPasswordEdt);
            }

            if (ValidationHelper.isNullOrEmpty(reenter)) {
                TextUtil.tILError(this, reenterEdtTil, reEnterPasswordEdt);
            }

            if (ValidationHelper.isNullOrEmpty(set)) {
                ErrorMessage.getInstance().showValidationMessage(this, setPasswordEdt, getString(R.string.error_empty_fields));
                return;
            } else if (ValidationHelper.isNullOrEmpty(reenter)) {
                ErrorMessage.getInstance().showValidationMessage(this, reEnterPasswordEdt, getString(R.string.error_empty_fields));
                return;
            }

            if (!validateTor.isAtleastLength(set, 6)) { // 6 . and number
                ErrorMessage.getInstance().showValidationMessage(this, setPasswordEdt, getString(R.string.limit_on_password_6_chars));
                TextUtil.tILError(this, setEdtTil, setPasswordEdt);
                return;
            }
            if (!validateTor.isAtleastLength(reenter, 6)) {
                ErrorMessage.getInstance().showValidationMessage(this, setPasswordEdt, getString(R.string.limit_on_password_6_chars));
                TextUtil.tILError(this, reenterEdtTil, reEnterPasswordEdt);
                return;
            }

            if (validateTor.hasAtleastOneDigit(set)
                //already comment
                //already comment end
            ) {
            } else {
                ErrorMessage.getInstance().showValidationMessage(this, setPasswordEdt, getString(R.string.limit_on_password));
                TextUtil.tILError(this, setEdtTil, setPasswordEdt);
                return;
            }
            if (validateTor.hasAtleastOneDigit(reenter)
                //already comment

                //already comment end
            ) {
            } else {
                ErrorMessage.getInstance().showValidationMessage(this, setPasswordEdt, getString(R.string.limit_on_password));
                TextUtil.tILError(this, reenterEdtTil, reEnterPasswordEdt);
                return;
            }

            if (!set.trim().equalsIgnoreCase(reenter.trim())) {
                ErrorMessage.getInstance().showValidationMessage(this, setPasswordEdt, getString(R.string.password_should_same));
                TextUtil.tILError(this, setEdtTil, setPasswordEdt);
                //already comment

                //already comment end
                return;
            }
        }

        String deviceType = getString(R.string.device_type);
        String versionRelease = Build.VERSION.RELEASE;
        String versionName = BuildConfig.VERSION_NAME;

        try {
            versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        RegisterStageTwoDTO registerStageTwoDTO = new RegisterStageTwoDTO(
                email, mobile, otp, reenter, deviceType, versionRelease, deviceToken, versionName, provider, accessToken
        );

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            try {
                RestClient.getInstance().registerStageTwo(registerStageTwoDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        RegisterTwoModel registerTwoModel = (RegisterTwoModel) result;

                        if (registerTwoModel != null) {

                            MobileOpResult mobileOpResult = registerTwoModel.getMobileOpResult();

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.VERIFICTIONMATCHED.getValue()) {
                                    tinyDB.putInt(Const.LOGIN_TYPE, provider);
                                    logon(provider, accessToken);
                                  /*  Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    setResult(RESULT_OK);
                                    finish();*/

                                   /* if (verifyAccountForm.getVisibility() == View.VISIBLE) {
                                        verifyAccountForm.setVisibility(View.GONE);
                                        confirmContainer.setVisibility(View.VISIBLE);
                                        three.setBackgroundResource(R.drawable.color_round_circle);
                                        three.setTextColor(getResources().getColor(R.color.colorWhite));
                                        backButton.setVisibility(View.GONE);
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(registerTwoModel.getPatientsMiniModels())) {
                                        loadExistingPatients(registerTwoModel.getPatientsMiniModels());
                                        confirmMessage.setText(R.string.we_have_found_the_following_people_registered_under_this_mobile_number);
                                        confirmContainer.setVisibility(View.VISIBLE);
                                        listContainer.setVisibility(View.VISIBLE);
                                        lower_title_tv.setVisibility(View.GONE);
                                    } else {
                                        confirmMessage.setText(R.string.profile_created_successfully);
                                        listContainer.setVisibility(View.INVISIBLE);
                                        lower_title_tv.setVisibility(View.GONE);
                                    }*/

                                } else {
                                    hideWaitDialog();
                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(RegisterActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(RegisterActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }
                                    ErrorMessage.getInstance().showError(RegisterActivity.this, errorMesg);
                                }
                            } else {
                                hideWaitDialog();
                                ErrorMessage.getInstance().showError(RegisterActivity.this, errorMessage);
                            }
                        } else {
                            hideWaitDialog();
                            ErrorMessage.getInstance().showError(RegisterActivity.this, errorMessage);
                        }
                    }
                });
            } catch (Exception e) {
                hideWaitDialog();
                e.printStackTrace();
            }

        } else {
            ErrorMessage.getInstance().showWarning(this, "Internet is not available");
        }
    }

    void logon(int provider, String accessToken) {
        if (ConnectionUtil.isInetAvailable(this)) {
            //showWaitDialog();

            String deviceType = getString(R.string.device_type);
            String versionRelease = Build.VERSION.RELEASE;
            String versionName = BuildConfig.VERSION_NAME;
            String username = "";
            String pass = "";
            String locality = tinyDB.getString(Const.LOCALITY);

            if (provider == Const.RG_CUSTOM) {
                username = TextUtil.getValue(emailEdtTil, emailEdt);
                pass = TextUtil.getValue(setEdtTil, setPasswordEdt);
            } else {
                // username = email;
            }

            username = TextUtil.getValue(emailEdtTil, emailEdt);

             /*FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(this, new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {

                }
            });*/

            LogonDTO logonDTO = new LogonDTO(username, pass, deviceType, versionRelease, deviceToken, versionName, locality, provider, accessToken);

            String finalUsername = username;
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
                                tinyDB.putBoolean(Const.IS_FROM_LOGIN, true);
                                tinyDB.putInt(Const.LOGIN_TYPE, provider);
                                tinyDB.putString(EMAIL_KEY, finalUsername);
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

                                ErrorMessage.getInstance().showError(RegisterActivity.this, errorMesg);
                            }

                        } else {
                            hideWaitDialog();
                            ErrorMessage.getInstance().showError(RegisterActivity.this, errorMessage);
                        }

                    } else {
                        hideWaitDialog();
                        ErrorMessage.getInstance().showError(RegisterActivity.this, errorMessage);
                    }
                }
            });
            tinyDB.putString(Const.ACCESS_TOKEN, accessToken);
            tinyDB.putString(Const.DEVICE_TOKEN, deviceToken);
            tinyDB.putString(Const.USER_NAME_LOGIN, username);


        } else {
            hideWaitDialog();
            ErrorMessage.getInstance().showWarning(RegisterActivity.this, getString(R.string.internet_connection));
        }
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

                            tinyDB.putBoolean(Const.BIOMETRIC_ENABLED, false);
                            tinyDB.putBoolean(Const.BIOMETRIC_REMINDER, false);
                            tinyDB.putListObject(Const.ONLINE_CONFIG_KEY, onlineConfigModel.getOpsConfigs());

                            // list of existing patients if lsit is empty create the profile , only one patient then move to dashboard
                            // and if list is greater than then show the existing patient list.
                            if (ValidationHelper.isNullOrEmpty(patientsMiniModels)) {

                                // Intent intent = new Intent(LoginActivity.this, CompleteRegistrationActivity.class);
                                // startActivity(AddProfileActivity.Companion.getLaunchIntent(RegisterActivity.this));
                                Intent intent = AddProfileActivity.Companion.getLaunchIntent(RegisterActivity.this);
                                intent.putExtra(Const.IS_FROM_REGISTER, true);
                                tinyDB.putBoolean(Const.IS_TENT, false);
                                startActivity(intent);
                                setResult(RESULT_OK);
                                finish();
                            } else if (!ValidationHelper.isNullOrEmpty(patientsMiniModels) && patientsMiniModels.size() == 1) {

                                if (!ValidationHelper.isNullOrEmpty(patientsMiniModels.get(0).getNameEn())) {
                                    if (TextUtil.getEnglish(RegisterActivity.this))
                                        tinyDB.putString(PATIENT_NAME, patientsMiniModels.get(0).getNameEn());
                                    else if (TextUtil.getEnglish(RegisterActivity.this)) {
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

                                if (!ValidationHelper.isNullOrEmpty(mrnNo))
                                    tinyDB.putString(Const.MRN_NO, mrnNo);
                                else
                                    tinyDB.putString(Const.MRN_NO, "0");

                                tinyDB.putLong(Const.PATIENT_ID, patientId);

                                if (!ValidationHelper.isNullOrEmpty(patientsMiniModels.get(0).getRegCardUrl())) {
                                    tinyDB.putString(Const.PATIENT_REG_CARD_URL, patientsMiniModels.get(0).getRegCardUrl());
                                } else {
                                    tinyDB.putString(Const.PATIENT_REG_CARD_URL, "");
                                }


                                Intent intent = new Intent(RegisterActivity.this, DashBoardActivity.class);
                                intent.putExtra(EXISTING_PATIENT, patientsMiniModels);
                                startActivity(intent);
                                setResult(RESULT_OK);
                                finish();
                            } else if (!ValidationHelper.isNullOrEmpty(patientsMiniModels) && patientsMiniModels.size() > 1) {
                                Intent intent = new Intent(RegisterActivity.this, SwitchPatientActivity.class);
                                intent.putExtra(EXISTING_PATIENT, patientsMiniModels);
                                startActivityForResult(intent, 100);
                            }

                        } else {

                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                if (TextUtil.getEnglish(RegisterActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                else if (TextUtil.getArabic(RegisterActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + getString(R.string.technical_info) + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }

                            ErrorMessage.getInstance().showError(RegisterActivity.this, errorMesg);
                        }

                    } else {
                        ErrorMessage.getInstance().showError(RegisterActivity.this, errorMessage);
                    }

                } else ErrorMessage.getInstance().showError(RegisterActivity.this, errorMessage);
            }
        });
    }

    void startSession() {
        tinyDB.putBoolean("session", true);
        //tinyDB.putBoolean("session_inter", true);
        // Const.ALLOWED_TOKEN_CALL = true;
    }

    CountDownTimer countDownTimer = null;

    void countDown() {

        countDownTimer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDownTxt.setText(getString(R.string.sms_count_msg) + " " + millisUntilFinished / 1000 + " " + getString(R.string.seconds));
            }

            public void onFinish() {
                resendBtn.setBackgroundResource(R.color.colorPrimary);
                resendBtn.setEnabled(true);
                countDownTxt.setText("");
            }

        };
        countDownTimer.start();
    }

    @OnClick(R.id.resend_btn)
    void resend() {
        Next(null);
        resendBtn.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
        resendBtn.setEnabled(false);
        //countDown();
    }

    void loadExistingPatients(ArrayList<PatientsMiniModel> patientsMiniModels) {
        ExistingPatientAdapter existingPatientAdapter = new ExistingPatientAdapter(this, patientsMiniModels);
        peopleRecycler.setAdapter(existingPatientAdapter);
    }

    @Override
    public void onBackPressed() {

        if (basicDetailForm.getVisibility() == View.GONE && confirmContainer.getVisibility() != View.VISIBLE) {
            basicDetailForm.setVisibility(View.VISIBLE);
            verifyAccountForm.setVisibility(View.GONE);
            two.setBackgroundResource(R.drawable.round_circle);
            lower_title_tv.setVisibility(View.VISIBLE);
            two.setTextColor(getResources().getColor(R.color.black));
            if (countDownTimer != null)
                countDownTimer.cancel();
        } else {
            super.onBackPressed();
        }
    }

    boolean flag = false;
    boolean flagReneter = false;

    //this function is not enabled
    @OnClick(R.id.password_show_hide)
    void showHide() {

        if (flag) {
            setPasswordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordShowHide.setImageDrawable(getResources().getDrawable(R.drawable.hide));
            flag = false;
        } else {
            flag = true;
            passwordShowHide.setImageDrawable(getResources().getDrawable(R.drawable.show));
            setPasswordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    //this function is not enabled
    @OnClick(R.id.reenter_password_show_hide)
    void showHideReenter() {

        if (flagReneter) {
            reEnterPasswordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            reEnterpasswordShowHide.setImageDrawable(getResources().getDrawable(R.drawable.hide));
            flagReneter = false;
        } else {
            flagReneter = true;
            reEnterpasswordShowHide.setImageDrawable(getResources().getDrawable(R.drawable.show));
            reEnterPasswordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    void viewTransform() {
        if (TextUtil.getArabic(this)) {
            backButton.setRotation(180);
            nextReg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            resendBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.refresh, 0, 0, 0);
            verifyBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            logon_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);

        }
    }

    //on google sign in button click
    public void googleSignIn(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Const.RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task != null) {
                handleSignInResult(task);
            }
        } else {
            Timber.d("on Activity Result");
            AccessToken accessToken = AccessToken.getCurrentAccessToken();

            if (accessToken != null && !accessToken.isExpired()) {
                //   firebaseAuthWithGFacebook(accessToken.getToken());
                // registerStageTwo(Const.RG_FACEBOOK, accessToken.getToken());
                //   Toast.makeText(this, "facebook", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // registerStageTwo(Const.RG_GOOGLE, account.getIdToken());
            firebaseAuthWithGoogle(account.getIdToken());
            //  Toast.makeText(this, "Google", Toast.LENGTH_LONG).show();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, e.getStatusCode(), Toast.LENGTH_LONG).show();
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

                            registerStageTwo(Const.RG_GOOGLE, mAuth.getUid());

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
                            registerStageTwo(Const.RG_FACEBOOK, mAuth.getUid());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Register", "signInWithCredential:failure", task.getException());
                            //  updateUI(null);
                        }
                    }

                });
    }
}
