package com.telemedicine.myclinic.vseeActivities;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PowerManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.telemedicine.myclinic.activities.BaseActivity;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.localnotification.NotificationHelper;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;
import com.telemedicine.myclinic.models.teleCheckin.TeleCheckInModel;
import com.telemedicine.myclinic.models.teleCheckin.TeleCheckInOutDTO;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.webservices.RestClient;
//import com.vsee.helpers.LogHelper;
//import com.vsee.kit.VSeeActivityHooks;
//import com.vsee.kit.VSeeServerConnection;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class WaitingRoomActivity extends BaseActivity {

    @BindView(R.id.doctor_name)
    RegularTextView doctorName;

    @BindView(R.id.doctor_image)
    CircleImageView doctorImage;

    @BindView(R.id.doctor_profession)
    RegularTextView doctorProfession;

    @BindView(R.id.doctor_name1)
    RegularTextView doctorName1;

    @BindView(R.id.doctor_image1)
    CircleImageView doctorImage1;

    @BindView(R.id.doctor_profession1)
    RegularTextView doctorProfession1;

    @BindView(R.id.dateTime)
    RegularTextView dateTime;

    @BindView(R.id.apptNo)
    RegularTextView apptNo;

    @BindView(R.id.status)
    ImageView mStatus;

    @BindView(R.id.waiting_doctor_ar)
    View waiting_doctor_ar;

    @BindView(R.id.waiting_doctor_en)
    View waiting_doctor_en;

    @BindView(R.id.toolbar_left_iv)
    ImageView imageView;
    boolean fromUpcoming = false;

    protected PowerManager.WakeLock mWakeLock;

//    VSeeServerConnection.VSeeServerConnectionReceiver receiver = new VSeeServerConnection.SimpleVSeeServerConnectionReceiver() {
//        @Override
//        public void onNewStatusMessage(String message) {
//            ErrorMessage.getInstance().showError(WaitingRoomActivity.this, message);
//            if (message.toLowerCase().contains("password")) {
//                String error = "Error logging in:" + message;
//
//            }
//        }
//
//        @Override
//        public void onLoginStateChange(VSeeServerConnection.LoginState newState) {
//            updateLoginState(newState);
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* This code together with the one in onDestroy()
         * will make the screen be always on until this Activity gets destroyed. */
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "myclinic:MyTag");
        this.mWakeLock.acquire();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = 1.0f;
        getWindow().setAttributes(params);

        imageView.setVisibility(View.GONE);

        transformViews();


   /*     try {
    // setupLoginStateReceiver();
       // validateAPIKeys(false);
            VSeeServerConnection.instance().getRememberedUser(new VSeeServerConnection.VSeeGetRememberedUserCallBack() {
                @Override
                public void onSuccess(String rememberedUsername) {
                    // loginEditText.setText(rememberedUsername);
                    VSeeServerConnection.instance().loginRememberedUser();
                }

                @Override
                public void onError() {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        getValue();

        teleCheckIn(true);

     /*   LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("waiting_room"));*/
    }

    ApptsMiniModel apptsMiniModel = null;
    DoctorsProfile doctorsProfile = null;
    long apptId = 0;
    boolean isTestConnection = false;

    void getValue() {

        Intent intent = getIntent();

        if (intent != null) {

            apptsMiniModel = intent.getParcelableExtra(Const.APPTMODEL);
            fromUpcoming = intent.getBooleanExtra("upcoming", false);

            if (apptsMiniModel != null)
                doctorsProfile = apptsMiniModel.getDoctorProfile();

            String doctrName = null, doctrProfession = null, apptDate, doctrUrl, apptNo = null;

            if (doctorsProfile != null) {

                if (TextUtil.getEnglish(this))
                    doctrName = doctorsProfile.getNameEn();
                else if (TextUtil.getArabic(this))
                    doctrName = doctorsProfile.getNameAr();

                if (TextUtil.getEnglish(this))
                    doctrProfession = doctorsProfile.getSpecialtyEn();
                else if (TextUtil.getArabic(this))
                    doctrProfession = doctorsProfile.getSpecialtyAr();

                apptDate = apptsMiniModel.getApptDate();
                doctrUrl = doctorsProfile.getProfilePicUrl();
                apptNo = apptsMiniModel.getApptNo();

            } else {
                doctrName = intent.getStringExtra(Const.DOCTOR_NAME);
                doctrProfession = intent.getStringExtra(Const.DOCTOR_SPECIALITY);
                apptDate = intent.getStringExtra(Const.DATE);
                doctrUrl = intent.getStringExtra(Const.DOCTOR_IMAGE_URL);
                apptNo = intent.getStringExtra(Const.APP_NO);
                apptId = intent.getLongExtra(Const.APPT_ID, 0);
            }

            if (!ValidationHelper.isNullOrEmpty(doctrName))
                doctorName.setText(doctrName);

            if (!ValidationHelper.isNullOrEmpty(doctrProfession))
                doctorProfession.setText(doctrProfession);

            if (!ValidationHelper.isNullOrEmpty(doctrUrl)) {
                Glide.with(this).load(doctrUrl).into(doctorImage);
            }

            if (!ValidationHelper.isNullOrEmpty(doctrName))
                doctorName1.setText(doctrName);

            if (!ValidationHelper.isNullOrEmpty(doctrProfession))
                doctorProfession1.setText(doctrProfession);

            if (!ValidationHelper.isNullOrEmpty(doctrUrl)) {
                Glide.with(this).load(doctrUrl).into(doctorImage1);
            }

            if (!ValidationHelper.isNullOrEmpty(apptDate)) {
                String date = LocaleDateHelper.convertDateStringFormat(apptDate, "yyyy-MM-dd'T'HH:mm:ss", "EEEE, dd MMMM yyyy HH:mm");
                if (!ValidationHelper.isNullOrEmpty(date))
                    dateTime.setText(date);
                else {
                    String date1 = LocaleDateHelper.convertDateStringFormat(apptDate, "EEEE, dd MMMM yyyy hh:mm a", "EEEE, dd MMMM yyyy HH:mm");
                    dateTime.setText(date1);
                }

            }

            if (!ValidationHelper.isNullOrEmpty(apptNo))
                this.apptNo.setText(getString(R.string.appointment_no) + apptNo);
        }

    }

    @Override
    protected int layout() {
        return R.layout.activity_waiting_room;
    }


    @OnClick(R.id.btn_home)
    void done() {

        dialgoueAppt(this, getResources().getString(R.string.leavemsg_waiting_room));
        // teleCheckIn(false);
    }

 /*   @OnClick(R.id.test_connection)
    void testConnection() {
        VSeeServerConnection.instance().logout();
        validateAPIKeys(true);
    }*/

    // region Vsee SDK Setup

   /* @Override
    protected void onPause() {
        if (!Const.CALL_ACTIVE) {
            done();
        }
        super.onPause();
    }*/

    // Update the text view with login status. If user login successfully, the state
    // changes to "LOGGED_IN" and we launch a new activity for authenticated user.
//    private void updateLoginState(VSeeServerConnection.LoginState loginState) {
//        String text = "Login State: " + loginState.toString();
//        Log.e("state last", text);
//        //  statusTextView.setText(text);
//        //  UtilProgressBar.dialogueShow(this, "Please wait...");
//
//
//        if (loginState == VSeeServerConnection.LoginState.LOGGED_IN) {
//
//            // Show example activity
//         /*   Intent intent = new Intent(this, ExampleActivity.class);
//            startActivity(intent);*/
//            mStatus.setImageDrawable(getResources().getDrawable(R.drawable.online));
//            if (!isTestConnection)
//                teleCheckIn(true);
//            else {
//                try {
//                    hideWaitDialog();
//                    VSeeServerConnection.instance().setAutoAccept(true);
//                    VSeeServerConnection.instance().setAutoAcceptNames(autoacceptList);
//                    runOnUiThread(() -> TextUtil.dialgoue(WaitingRoomActivity.this, getString(R.string.successTest)));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            // Finish current login activity since it is no longer useful
//            //removeLoginStateReceiver();
//            //   finish();
//        } else if (loginState.equals(VSeeServerConnection.LoginState.CANNOT_TOO_OLD)) {
//            //hideWaitDialog();
//            // VSeeKit is too old and update is needed.
//            //  Toast.makeText(LoginActivity.this, R.string.prompt_vseekit_too_old, Toast.LENGTH_LONG).show();
//        } else if (loginState.equals(VSeeServerConnection.LoginState.IDLE)) {
//            //hideWaitDialog();
//            //loginButton.setEnabled(true);
//        }
//    }
//
//    // Listen to state of login. The callbacks is always run on main thread.
//    private void setupLoginStateReceiver() {
//        VSeeServerConnection.instance().addReceiver(receiver);
//    }
//
//    private void removeLoginStateReceiver() {
//        VSeeServerConnection.instance().removeReceiver(receiver);
//    }
//
//    private void validateAPIKeys(boolean isTestConnection) {
//
//        this.isTestConnection = isTestConnection;
//        String apiKey = getString(R.string.vsee_api_key);
//        String secretHash = getString(R.string.vsee_secret_hash_key);
//        Dialog dialog = showWaitDialog();
//
//        RegularTextView txt = dialog.findViewById(R.id.progress_txt);
//        txt.setText(R.string.please_wait_vsee);
//        if (isTestConnection)
//            txt.setText(R.string.testingConnectionMsg);
//
//        VSeeServerConnection.instance().validate(apiKey, secretHash, new VSeeServerConnection.VSeeCallback() {
//            @Override
//            public void onSuccess() {
//                LogHelper.d("VSeeKitExample", "API Keys was validated successfully");
//                TinyDB tinyDB = new TinyDB(WaitingRoomActivity.this);
//                String username = tinyDB.getString(Const.TMUSERNAME_KEY); // "myclinic+187mervatqutub";//
//                String password = tinyDB.getString(Const.TMPASSWORD_KEY); // "123456";//
//                login(username, password);
//
//            /*mName.setText(result.DoctorName);
//            expertize.setText(result.Specialty);
//            date.setText(result.ActualDate);*/
//            }
//
//            @Override
//            public void onError(int code, String error) {
//                hideWaitDialog();
//                ErrorMessage.getInstance().showError(WaitingRoomActivity.this, error);
//            }
//        });
//    }

    // logged in
//    private void login(String userName, String password) {
//
//        /*if (mLoginState == VSeeServerConnection.LoginState.LOGGED_IN) {
//            hideWaitDialog();
//            return;
//        }*/
//
//
//        // String finalUsername = "myclinic+" + userName;
//        // Basic validation
//       /* if (TextUtils.isEmpty(loginEditText.getText()) || TextUtils.isEmpty(passwordEditText.getText())) {
//            return;
//        }*/
//
//        // Initialize VSeeKit, connect to server, perform login...
//        VSeeServerConnection.instance().loginUser(userName, password);
//    }

    @Override
    public void onBackPressed() {
       /* VSeeServerConnection.instance().logout();
        teleCheckIn(false);*/
        //super.onBackPressed();
    }

    //
    // Let VSeeKit know that there has been some user interaction.
    //
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        //VSeeActivityHooks.onUserInteraction();
    }
    //endregion

    ArrayList<String> autoacceptList = null;

    void teleCheckIn(boolean checkIn) {

        if (ConnectionUtil.isInetAvailable(this)) {

            if (!checkIn)
                showWaitDialog();

            String securityToken = new TinyDB(this).getString(Const.TOKEN_KEY);
            String companyId = new TinyDB(this).getString(Const.COMPANY_ID);
            long apptId = 0;
            if (apptsMiniModel != null) {
                apptId = apptsMiniModel.getApptId();
            } else {
                apptId = this.apptId;
            }

            TeleCheckInOutDTO teleCheckInOutDTO = new TeleCheckInOutDTO(securityToken, apptId, checkIn,companyId);

            RestClient.getInstance().teleCheckInOut(teleCheckInOutDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    TeleCheckInModel teleCheckInModel = (TeleCheckInModel) result;

                    if (teleCheckInModel != null) {
                        MobileOpResult mobileOpResult = teleCheckInModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                mStatus.setImageDrawable(getResources().getDrawable(R.drawable.online));

                                if (!checkIn) {
                                    if (fromUpcoming) {
                                        // VSeeServerConnection.instance().logout();
                                        finish();
                                        return;
                                    }
                                    // VSeeServerConnection.instance().logout();
                                    Intent intent = new Intent();
                                    intent.putExtra("finish", true);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } else {
                                    autoacceptList = new ArrayList<>();
                                    autoacceptList.add(teleCheckInModel.getVSeeUsername());
                                    // autoacceptList.add("asim.hafeez@myclinic.com.sa");
                                   /* VSeeServerConnection.instance().setAutoAccept(true);
                                    VSeeServerConnection.instance().setAutoAcceptNames(autoacceptList);*/
                                }

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                    if (TextUtil.getEnglish(WaitingRoomActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(WaitingRoomActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(WaitingRoomActivity.this, errorMesg);

                            }

                        } else {
                            ErrorMessage.getInstance().showError(WaitingRoomActivity.this, errorMessage);
                        }
                    } else
                        ErrorMessage.getInstance().showError(WaitingRoomActivity.this, errorMessage);
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    @Override
    public void onDestroy() {
        this.mWakeLock.release();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    void transformViews() {
        if (TextUtil.getArabic(this)) {

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mStatus.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            mStatus.setLayoutParams(params);
            waiting_doctor_ar.setVisibility(View.VISIBLE);

            waiting_doctor_en.setVisibility(View.INVISIBLE);

        } else if (TextUtil.getEnglish(this)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mStatus.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mStatus.setLayoutParams(params);
            waiting_doctor_ar.setVisibility(View.INVISIBLE);
            waiting_doctor_en.setVisibility(View.VISIBLE);
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                boolean cancelAlarm = intent.getBooleanExtra("cancel_alarm", false);
                if (cancelAlarm)
                    NotificationHelper.getInstance().cancelNotification(context, (int) apptId);
            }
        }
    };

    public void dialgoueAppt(Activity activity, String msg) {

        String cancel1 = getString(R.string.cancel);
        String yes = getString(R.string.yes);

        IOSDialog dialogView = new IOSDialog.Builder(activity)
                .setTitle(R.string.are_you_sure)
                .setMessage(msg)
                .setPositiveButton(yes, (dialog, which) -> {

                    if (fromUpcoming) {
                        //VSeeServerConnection.instance().logout();
                        activity.finish();
                        return;
                    }

                   // VSeeServerConnection.instance().logout();
                    Intent intent = new Intent();
                    intent.putExtra("finish", true);
                    activity.setResult(RESULT_OK, intent);
                    activity.finish();
                })
                .setNegativeButton(cancel1, null).show();

        String font = "GothamMedium.otf";
        String font_bold = "GothamBold.otf";

        Button btnConfirm = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.confirm_btn);
        btnConfirm.setText(yes);

        Button cancel = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.cancel_btn);
        cancel.setText(cancel1);

        TextView tvTitle = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.title);
        TextView tvMessage = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.message);
        Typeface tf = Typeface.createFromAsset(activity.getAssets(), font);
        Typeface tf_title = Typeface.createFromAsset(activity.getAssets(), font_bold);
        btnConfirm.setTypeface(tf);
        btnConfirm.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        cancel.setTypeface(tf);
        cancel.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        tvTitle.setTypeface(tf_title);
        tvMessage.setTypeface(tf);
        tvMessage.setTextColor(activity.getResources().getColor(R.color.black));
    }

}

