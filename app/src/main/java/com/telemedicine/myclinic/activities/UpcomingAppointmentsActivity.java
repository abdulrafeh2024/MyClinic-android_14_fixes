package com.telemedicine.myclinic.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import com.google.gson.GsonBuilder;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.telemedicine.myclinic.AppointmentDetailActivity;
import com.telemedicine.myclinic.activities.multiservices.ServicesListActivity;
import com.telemedicine.myclinic.adapters.AdapterUpcomingAppointments;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.hyperpay.CustomUIActivity;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.localnotification.NotificationHelper;
import com.telemedicine.myclinic.models.CardDTO;
import com.telemedicine.myclinic.models.CardModel;
import com.telemedicine.myclinic.models.CardsListModel;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.UpdateInsuranceDTO;
import com.telemedicine.myclinic.models.appointments.AppointmentsDTO;
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel;
import com.telemedicine.myclinic.models.appointments.ApptsModel;
import com.telemedicine.myclinic.models.bookAppointment.ApptGetTotalOutstandingDTO;
import com.telemedicine.myclinic.models.bookAppointment.ApptGetTotalOutstandingModel;
import com.telemedicine.myclinic.models.bookAppointment.ApptPaymentDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetInsuranceByIdDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetInsuranceModel;
import com.telemedicine.myclinic.models.cancelAppointment.ApptCancelDTO;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;
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
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.webservices.RestClient;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.telemedicine.myclinic.util.Const.APPT_ID;
import static com.telemedicine.myclinic.util.Const.COMPANY_ID;
import static com.telemedicine.myclinic.util.Const.DATE;
import static com.telemedicine.myclinic.util.Const.DOCTOR_ID;
import static com.telemedicine.myclinic.util.Const.DOCTOR_IMAGE_URL;
import static com.telemedicine.myclinic.util.Const.DOCTOR_NAME;
import static com.telemedicine.myclinic.util.Const.DOCTOR_SPECIALITY;
import static com.telemedicine.myclinic.util.Const.INSURANCE_IMAGES;
import static com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY;
import static com.telemedicine.myclinic.util.Const.IS_TENT;
import static com.telemedicine.myclinic.util.Const.OREGID_KEY;
import static com.telemedicine.myclinic.util.Const.PATIENT_UPDATE_KEY;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_ID;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;

public class UpcomingAppointmentsActivity extends BaseActivity implements AdapterUpcomingAppointments.OnCardClickListner {

    @BindView(R.id.doctor_recycler_view)
    RecyclerView doctorRecyclerView;

    @BindView(R.id.no_apptmnt)
    RegularTextView noApptmnt;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @BindView(R.id.refresh)
    ImageView refresh;

    @BindView(R.id.chat_bot_bg)
    RelativeLayout chatBotBg;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.patientRId)
    BoldTextView patientRId;


    @BindView(R.id.chatBot_rounded)
    View chatBot_rounded;

    @BindView(R.id.chat_bot_web_view)
    WebView chatBotWebView;

    boolean isChatBotVisible = false;

    boolean isRefreshApts = false;

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateListener;

    private static final int RC_RESCHEULED = 529;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRefreshApts = false;
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRefreshApts) {
            isRefreshApts = false;
            boolean backfromReschule = tinyDB.getBoolean(Const.BACK_TO_HOME);

            if (backfromReschule) {
                tinyDB.putBoolean(Const.BACK_TO_HOME, false);
                finish();
            }
            //  apptGetSummaryBySType();
        }


    }

    @Override
    protected int layout() {
        return R.layout.activity_upcoming_appointments;
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

        transformViews();

        boolean isTent = tinyDB.getBoolean(IS_TENT);

        if (!isTent) {
            patientRId.setVisibility(View.VISIBLE);
        } else {
            patientRId.setVisibility(View.GONE);
        }

        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("update_list"));

        doctorRecyclerView.setLayoutManager
                (new LinearLayoutManager(this));

        apptGetSummaryBySType();

        dateListener = new DatePickerDialog.OnDateSetListener() {
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
    }

    @OnClick(R.id.patientRId)
    void patientRId() {
        String patentDigitalUrl = tinyDB.getString(Const.PATIENT_REG_CARD_URL);
        Intent intent = new Intent(this, BrowserActivityPayment.class);
        intent.putExtra("calendar", false);
        intent.putExtra("url", patentDigitalUrl);
        //  intent.putExtra("url", getString(R.string.contact_us_url));
        intent.putExtra(Const.TERMS_CONDITIONS_KEY, "");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if (isChatBotVisible) {
            openChatBotBg(null);
            return;
        }
        super.onBackPressed();
    }

    private void updateDOB() {
        //TODO:BookAppointmentTwoActivity
        String myFormat = "EEEE, dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        String date = sdf.format(myCalendar.getTime());

        if (apptsMiniModel != null && apptsMiniModel.getDoctorProfile() != null) {

            DoctorsProfile doctorsProfile = apptsMiniModel.getDoctorProfile();
            String docName = "";
            String speciality = "";
            Intent intent = new Intent(this, BookAppointmentTwoActivity.class);

            if (TextUtil.getEnglish(this)) {
                speciality = doctorsProfile.getSpecialtyEn();
                docName = doctorsProfile.getNameEn();
            } else if (TextUtil.getArabic(this)) {
                docName = doctorsProfile.getNameAr();
                speciality = doctorsProfile.getSpecialtyAr();
            }


            intent.putExtra(DOCTOR_NAME, docName);
            intent.putExtra(DOCTOR_SPECIALITY, speciality);
            intent.putExtra(DATE, date);
            intent.putExtra(SPECIALITY_ID, doctorsProfile.getSpecialtyId());
            intent.putExtra(DOCTOR_ID, String.valueOf(doctorsProfile.getDoctorId()));
            intent.putExtra(ISTELEMEDICINE_KEY, true);
            intent.putExtra(DOCTOR_IMAGE_URL, doctorsProfile.getProfilePicUrl());
            intent.putExtra(Const.APPT_ID, apptsMiniModel.getApptId());
            startActivityForResult(intent, 111);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            // finish it for going to dashboard.
            finish();

            if (data != null) {

                String updatedTime = data.getStringExtra(Const.UPDATED_TIME);

                long apptId = data.getLongExtra(Const.APPT_ID, 0);

                apptsMiniModel.setApptId(apptId);

                if (!ValidationHelper.isNullOrEmpty(updatedTime)) {

                    String date = LocaleDateHelper.convertDateStringFormat(updatedTime, "EEEE, dd MMMM yyyy hh:mm a", "yyyy-MM-dd'T'HH:mm:ss");

                    if (!ValidationHelper.isNullOrEmpty(date)) {
                        apptsMiniModel.setApptDate(date);
                    }

                    adapterDoctors.updateList(apptsMiniModel, pos);
                }
            }
        } else if (requestCode == 1000 && resultCode == RESULT_OK) {

            if (data != null) {
                boolean isFinish = data.getBooleanExtra("finish", false);
                if (isFinish)
                    finish();
            }

        } else if (requestCode == 500 && resultCode == RESULT_OK) {

            if (data != null) {

                boolean isPaid = data.getBooleanExtra(Const.CONFIRM, true);

                long apptId = data.getLongExtra(Const.APPT_ID, 0);

                if (apptsMiniModel != null)
                    apptsMiniModel.setPaid(isPaid);

                adapterDoctors.updateList(apptsMiniModel, pos);
            }
        } else if (requestCode == 49 && resultCode == RESULT_OK) {
            finish();
        } else if (requestCode == 113 && resultCode == RESULT_OK) {

            if (data != null) {

                boolean isInsurance = data.getBooleanExtra(Const.IS_INSURANCE_KEY, false);
                long insuranceId = data.getLongExtra(Const.INSURANCE_ID, 0);
                double telePrice = data.getDoubleExtra(Const.TELE_PRICE, 0);
                boolean isTele = data.getBooleanExtra(Const.IS_TELE, false);

                updateInsurance(insuranceId);
            }
        }
    }


    void dialgoueCash(double priceval, boolean isInsurance, long insuranceId) {

        String ammount = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US)).format(priceval);

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

                        if (ConnectionUtil.isInetAvailable(UpcomingAppointmentsActivity.this)) {
                            //payStageOne(Double.valueOf(priceval), isInsurance, insuranceId);
                        } else {
                            ErrorMessage.getInstance().showWarning(UpcomingAppointmentsActivity.this, getString(R.string.internet_connection));
                        }
                    }
                }).show();
    }

    void updateInsurance(long insuranceId) {
        try {

            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            String companyId = new TinyDB(this).getString(Const.COMPANY_ID);

            UpdateInsuranceDTO updateInsuranceByIdDTO = new UpdateInsuranceDTO(securityToken,
                    String.valueOf(apptsMiniModel.getApptId()), insuranceId, companyId);

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
                                    if (TextUtil.getEnglish(UpcomingAppointmentsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(UpcomingAppointmentsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMessage);

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
                        apptGetTotalOutstanding(apptsMiniModel.getApptId(), apptsMiniModel.isTelemedicine(), apptsMiniModel.getCompany(), true);
                    }
                }).show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    AdapterUpcomingAppointments adapterDoctors = null;

    void loadDoctorList(ArrayList<ApptsMiniModel> apptsMiniModels) {
        adapterDoctors = new AdapterUpcomingAppointments(this, apptsMiniModels);
        doctorRecyclerView.setAdapter(adapterDoctors);
        doctorRecyclerView.setHasFixedSize(true);
        adapterDoctors.setOnCardClickListner(this);
    }

    int pos = 0;

    ApptsMiniModel apptsMiniModel = null;
    String specialtyId = "";
    long doctorId = 0;
    String apptDate = "";

    String specAR = "";
    String specEn = "";
    boolean isTelemedicineKey = false;

    @Override
    public void OnCardClicked(ApptsMiniModel model, int position, boolean cancel, boolean reschedule, boolean checkin, boolean viewServices,
                              boolean isChangeInsurance) {

        pos = position;
        apptsMiniModel = model;
        isTelemedicineKey = model.isTelemedicine();
        tinyDB.putString(Const.COMPANY_ID, model.getCompany());
        tinyDB.putString(Const.COMPANY_NAME_AR, model.getCompanyNameAr());
        tinyDB.putString(Const.COMPANY_NAME_EN, model.getCompanyNameEn());

        specAR = model.getDoctorProfile().getSpecialtyAr();
        specEn = model.getDoctorProfile().getSpecialtyEn();


        tinyDB.putString(Const.DOCTOR_SPECIALITY, model.getDoctorProfile().getSpecialtyEn());

        if (isChangeInsurance) {
            getInsurance();
            return;
        }

        if (viewServices && model != null) {
            long apptId = model.getApptId();
            Intent intent = new Intent(this, ServicesOrdersActivity.class);
            intent.putExtra(Const.APPT_ID, apptId);
            String jsonModel = new GsonBuilder().create().toJson(model);
            intent.putExtra(Const.APPTMODEL, jsonModel);
            startActivityForResult(intent, 49);
            return;
        }

        if (!model.isTelemedicine()) {
            isRefreshApts = true;
            Intent intent = new Intent(this, AppointmentDetailActivity.class);
            String jsonModel = new GsonBuilder().create().toJson(model);
            intent.putExtra(Const.APPTMODEL, jsonModel);
            startActivityForResult(intent, 49);

        } else {
            if (cancel) {
                showWarningMessage(model, model.getApptId());
            } else if (reschedule) {
                isRefreshApts = true;
                reschedule();
            } else if (checkin) {

                if (model != null) {

                    boolean isPaid = model.isPaid();
                    if (isPaid) {
                       /* Intent intent = new Intent(this, WaitingRoomActivity.class);
                        intent.putExtra("upcoming", true);
                        intent.putExtra(Const.APPTMODEL, model);
                        startActivityForResult(intent, 1000);*/
                        String patientName = tinyDB.getString(Const.PATIENT_NAME);
                        Intent intent = new Intent(this, ZoomMeetingActivity.class);
                        intent.putExtra("tokenId", model.getPassword());
                        intent.putExtra("sessionId", model.getMeetingId());
                        intent.putExtra("apptId", String.valueOf(model.getApptId()));
                        intent.putExtra("PatientName", patientName);

                        startActivity(intent);
                    } else {
                        //  ServiceSelectionDialog(model);
                        if (model.getPatient() != null) {

                            long insuranceId = 0;

                            if (model.getPatient().getInsuranceCards() != null) {
                                insuranceId = model.getPatient().getInsuranceCards().get(0).getInsuranceId();
                            }

                            if (model.getDoctorProfile() != null) {
                                specialtyId = model.getDoctorProfile().getSpecialtyId();
                                doctorId = model.getDoctorProfile().getDoctorId();
                            }

                            apptDate = model.getApptDate();

                            apptGetTotalOutstanding(model.getApptId(), model.isTelemedicine(), model.getCompany(), false);
                        }
                    }
                }
            }
        }
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
                ErrorMessage.getInstance().showSuccessWithoutNavigationGreen(UpcomingAppointmentsActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameAr() + ".");
            }else{
                ErrorMessage.getInstance().showSuccessWithoutNavigationGreen(UpcomingAppointmentsActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameEn() + ".");
            }
        } else {
            ErrorMessage.getInstance().showErrorYellow(UpcomingAppointmentsActivity.this, appointmentEvent.getErrorMSg());
        }
    }

    private void ServiceSelectionDialog(ApptsMiniModel model) {
        Dialog dialog = new Dialog(this, R.style.ios_dialog_style);
        dialog.setContentView(R.layout.dialog_service_selection_layout);
        dialog.setCancelable(false);
        LightButton smartTouchBtn = dialog.findViewById(R.id.smart_touch_btn);
        LightButton notNowBtn = dialog.findViewById(R.id.not_now_btn);
//        // if button is clicked, close the custom dialog

        smartTouchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (model.getPatient() != null) {

                    long insuranceId = 0;

                    if (model.getPatient().getInsuranceCards() != null) {
                        insuranceId = model.getPatient().getInsuranceCards().get(0).getInsuranceId();
                    }

                    if (model.getDoctorProfile() != null) {
                        specialtyId = model.getDoctorProfile().getSpecialtyId();
                        doctorId = model.getDoctorProfile().getDoctorId();
                    }

                    apptDate = model.getApptDate();

                    apptGetTotalOutstanding(model.getApptId(), model.isTelemedicine(), model.getCompany(), false);
                }
            }
        });

        notNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(UpcomingAppointmentsActivity.this, ServicesListActivity.class);
                intent.putExtra(APPT_ID, model.getApptId());
                intent.putExtra(COMPANY_ID, model.getCompany());
                startActivity(intent);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
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

                                    Intent intent = new Intent(UpcomingAppointmentsActivity.this, InsuranceCardCashCardActivity.class);
                                    intent.putExtra("insuranceCard", getInsuranceModelsList);
                                    intent.putExtra("noinsurance", specialtyId);
                                    intent.putExtra("doctorId", apptsMiniModel.getDoctorProfile().getDoctorId().toString());
                                    intent.putExtra(Const.ISTELEMEDICINE_KEY, apptsMiniModel.isTelemedicine());
                                    intent.putExtra(Const.Is_CHANGE_INSURANCE, true);
                                    startActivityForResult(intent, 113);
                                    //  }

                                } else {

                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(UpcomingAppointmentsActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(UpcomingAppointmentsActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMesg);
                                }

                            } else
                                ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMessage);

                        } else {
                            ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMessage);
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
         /*   final Dialog dialog = new Dialog(this, R.style.NewDialog);
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
                        getPatientData();
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

    void getPatientData() {
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


                                                Intent intent = new Intent(UpcomingAppointmentsActivity.this, AddInsuranceActivity.class);
                                                intent.putExtra(PATIENT_UPDATE_KEY, patientRegistrationUpdate);
                                                startActivityForResult(intent, 99);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    } else {
                                        String errorMesg = "";

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                            if (TextUtil.getEnglish(UpcomingAppointmentsActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                            else if (TextUtil.getArabic(UpcomingAppointmentsActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";

                                        }

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                            errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                        }
                                        if (mobileOpResult.getResult() == 600 || mobileOpResult.getResult() == 700) {
                                            ErrorMessage.getInstance().showErrorYellow(UpcomingAppointmentsActivity.this, errorMesg);
                                        } else {
                                            ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMesg);
                                        }

                                        //  ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMesg);
                                    }
                                } else
                                    ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMessage);
                            } else
                                ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMessage);
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

    void reschedule() {
//        DatePickerDialog datePicker = new DatePickerDialog(this, dateListener, myCalendar
//                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                myCalendar.get(Calendar.DAY_OF_MONTH));
//
//        datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
//        datePicker.setCanceledOnTouchOutside(false);
//        datePicker.show();

        //TODO: Open New Reshedule screen from here
        if (apptsMiniModel != null && apptsMiniModel.getDoctorProfile() != null) {
            startActivityForResult(ReScheduleApptActivity.Companion.getLaunchIntent(this, apptsMiniModel), RC_RESCHEULED);
        }
    }

    void apptGetSummaryBySType() {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            String securityToken = new TinyDB(this).getString(Const.TOKEN_KEY);

            long patientId = new TinyDB(this).getLong(Const.PATIENT_ID, 0);

            String apptType = "2";

            if (!ValidationHelper.isNullOrEmpty(securityToken) && patientId != 0) {

                AppointmentsDTO appointmentsDTO = new AppointmentsDTO(securityToken, String.valueOf(patientId), apptType);

                RestClient.getInstance().apptGetSummaryBySType(appointmentsDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        hideWaitDialog();

                        ApptsModel apptsModel = (ApptsModel) result;

                        if (apptsModel != null) {

                            MobileOpResult mobileOpResult = apptsModel.getMobileOpResult();

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                    ArrayList<ApptsMiniModel> apptsMiniModel = apptsModel.getApptsMiniModel();

                                    if (!ValidationHelper.isNullOrEmpty(apptsMiniModel)) {
                                        noApptmnt.setVisibility(View.GONE);
                                        loadDoctorList(apptsMiniModel);
                                    }

                                } else {
                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(UpcomingAppointmentsActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(UpcomingAppointmentsActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMesg);
                                }
                            } else {
                                ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMessage);
                            }
                        } else {
                            ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMessage);
                        }
                    }
                });
            }
        } else {
            ErrorMessage.getInstance().showError(this, getString(R.string.internet_connection));
        }
    }

    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            if (intent != null) {

                ApptsMiniModel apptsMiniModel = intent.getParcelableExtra("updated_list");

                boolean isUpdate = intent.getBooleanExtra(Const.IS_UPDATE, false);


                if (isUpdate) {
                    if (adapterDoctors != null) {
                        adapterDoctors.updateList(apptsMiniModel, pos);
                    }
                } else {
                    if (adapterDoctors != null)
                        adapterDoctors.removeObject(apptsMiniModel, pos);

                    if (adapterDoctors.getItemCount() == 0)
                        noApptmnt.setVisibility(View.VISIBLE);
                }

                sendBroadCast();
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    void showWarningMessage(ApptsMiniModel model, long apptId) {
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
                            if (ConnectionUtil.isInetAvailable(UpcomingAppointmentsActivity.this)) {

                                showWaitDialog();

                                TinyDB tinyDB = new TinyDB(UpcomingAppointmentsActivity.this);

                                String securityToken = tinyDB.getString(Const.TOKEN_KEY);

                                String companyID = tinyDB.getString(Const.COMPANY_ID);

                                ApptCancelDTO apptCancelDTO = new ApptCancelDTO(securityToken, String.valueOf(apptId), false, companyID);

                                RestClient.getInstance().apptCancel(apptCancelDTO, new OnResultListener() {
                                    @Override
                                    public void onResult(Object result, boolean status, String errorMessage) {

                                        hideWaitDialog();

                                        MobileOpResult mobileOpResult = (MobileOpResult) result;

                                        if (mobileOpResult != null) {

                                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                                adapterDoctors.removeObject(model, pos);
                                                if (adapterDoctors.getItemCount() == 0)
                                                    noApptmnt.setVisibility(View.VISIBLE);
                                                sendBroadCast();
                                                NotificationHelper.getInstance().cancelNotification(UpcomingAppointmentsActivity.this, (int) apptId);

                                            } else {

                                                String errorMesg = "";

                                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                                    if (TextUtil.getEnglish(UpcomingAppointmentsActivity.this))
                                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                                    else if (TextUtil.getArabic(UpcomingAppointmentsActivity.this))
                                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                                }

                                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                                }

                                                ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMesg);
                                            }
                                        } else {
                                            ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMessage);
                                        }
                                    }
                                });

                            } else {
                                ErrorMessage.getInstance().showWarning(UpcomingAppointmentsActivity.this, "Internet is not available");
                            }
                        }
                    }).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    void sendBroadCast() {
        Intent intent = new Intent("update_appts");
        // You can also include some extra data.
        intent.putExtra("refresh", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    void transformViews() {
        if (TextUtil.getArabic(this)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar_left_iv.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            toolbar_left_iv.setLayoutParams(params);
            toolbar_left_iv.setRotation(180);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) refresh.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            refresh.setLayoutParams(params1);
        } else {

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) refresh.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            refresh.setLayoutParams(params1);
        }
    }

    void apptGetTotalOutstanding(long apptId, boolean isTelemedicine, String companyId, boolean isChangeInsurance) {

        if (ConnectionUtil.isInetAvailable(this)) {
            TinyDB tinyDB = new TinyDB(this);

            showWaitDialog();
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);

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

                                if (ammountDue > 0) {
                                    dialgoue(apptId, ammountDue, isTelemedicine, false, 0);
                                } else {
                                    if (isChangeInsurance) {
                                        //discuss in meeting with shahbaz and nagwa to comment all the apptpayment from all places except payment stage 2
                                        confirmAppointment(apptId, companyId);
                                    }
                                }
                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(UpcomingAppointmentsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(UpcomingAppointmentsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                if (mobileOpResult.getResult() == 600 || mobileOpResult.getResult() == 700) {
                                    ErrorMessage.getInstance().showErrorYellow(UpcomingAppointmentsActivity.this, errorMesg);
                                } else {
                                    ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMesg);
                                }

                                // ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMessage);

                    } else {
                        ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMessage);
                    }
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, "Internet is not available");
        }
    }

    void payStageOne(long apptId, double ammountDue, boolean isInsurance, long insuranceId) {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            if (ammountDue > 0) {
                String companyId = tinyDB.getString(Const.COMPANY_ID);
                String customerEmail = tinyDB.getString(Const.EMAIL_KEY);
                String oregId = tinyDB.getString(Const.OREGID_KEY);
                String apptIdStr = String.valueOf(apptId);
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

                                    getCards(checkOutId, String.valueOf(ammountDue), apptId);
                                } else {

                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(UpcomingAppointmentsActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(UpcomingAppointmentsActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMesg);
                                }

                            } else {
                                ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMessage);
                            }

                        } else {
                            ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMessage);
                        }
                    }
                });
            }
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }

    }

    void confirmAppointment(long mApptId, String companyId) {

        showWaitDialog();

        String securityToken = new TinyDB(this).getString(Const.TOKEN_KEY);
        //String companyId = new TinyDB(this).getString(Const.COMPANY_ID);
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
                                apptGetSummaryBySType();
                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(UpcomingAppointmentsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(UpcomingAppointmentsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMessage);

                    } else {
                        ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMessage);
                    }
                }
            });
        }
    }

    void getCards(String checkOutId, String mAmount, long apptId) {

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

                                    //  Intent intent = new Intent(UpcomingAppointmentsActivity.this, CustomUIActivity.class);
                                    Intent intent = new Intent(UpcomingAppointmentsActivity.this, CustomUIActivity.class);
                                    intent.putExtra("amount", mAmount);
                                    intent.putExtra(Const.APPT_ID, apptId);
                                    intent.putExtra(Const.IS_FROM_CARD_LISTING, false);
                                    intent.putExtra(Const.CHECK_OUT_ID, checkOutId);
                                    //   intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicineKey);
                                    // intent.putExtra(SPECIALITY_ID, specialtyId);
                                    //intent.putExtra(DATE, apptDate);
                                    //intent.putExtra(DOCTOR_ID,  String.valueOf(doctorId));
                                    //intent.putExtra(Const.APPT_BOOK_TYPE, apptsMiniModel.getApptBookType());
                                    /*            intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcine);*/
                                    //  intent.putExtra(Const.APPT_ID, 0);
                                  /*  intent.putExtra(Const.IS_INSURANCE_KEY, mIsInsurance);
                                    intent.putExtra(Const.INSURANCE_ID, mInsuranceId);*/
                                    startActivityForResult(intent, 500);
                                } else {
                                    //  Intent intent = new Intent(UpcomingAppointmentsActivity.this, CustomUIActivity.class);
                                    Intent intent = new Intent(UpcomingAppointmentsActivity.this, CardListActivity.class);
                                    intent.putExtra("amount", mAmount);
                                    intent.putExtra(Const.APPT_ID, apptId);
                                    //   intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicineKey);
                                    //   intent.putExtra(SPECIALITY_ID, specialtyId);
                                    //   intent.putExtra(DATE, apptDate);
                                    //   intent.putExtra(DOCTOR_ID, String.valueOf(doctorId));
                                    //  intent.putExtra(Const.APPT_BOOK_TYPE, apptsMiniModel.getApptBookType());
                                /*    intent.putExtra(Const.IS_INSURANCE_KEY, mIsInsurance);
                                    intent.putExtra(Const.INSURANCE_ID, mInsuranceId);*/
                                    intent.putExtra(Const.CHECK_OUT_ID, checkOutId);
                                    startActivityForResult(intent, 500);
                                }

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(UpcomingAppointmentsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(UpcomingAppointmentsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMesg);

                            }

                        } else {

                            ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMessage);
                        }

                    } else {

                        ErrorMessage.getInstance().showError(UpcomingAppointmentsActivity.this, errorMessage);
                    }
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }

    }

    IOSDialog iosDialog1 = null;

    private void showCheckinDialog() {
        iosDialog1 = TextUtil.dialgoue(UpcomingAppointmentsActivity.this, getString(R.string.check_in_eligible), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iosDialog1.dismiss();
            }
        });
    }

    void dialgoue(long apptId, double ammountDue, boolean isTelemedcine, boolean isInsurance, long insuranceId) {
        if (!isTelemedcine) {
            if (LocaleDateHelper.expiredAptTime(apptDate)) {
                showCheckinDialog();
                return;
            }
        }

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
                        if (ConnectionUtil.isInetAvailable(UpcomingAppointmentsActivity.this)) {
                            payStageOne(apptId, ammountDue, isInsurance, insuranceId);
                        } else {
                            ErrorMessage.getInstance().showWarning(UpcomingAppointmentsActivity.this, getString(R.string.internet_connection));
                        }
                    }
                }).show();
    }


    @OnClick(R.id.refresh)
    void refresh() {
        apptGetSummaryBySType();
    }


}
