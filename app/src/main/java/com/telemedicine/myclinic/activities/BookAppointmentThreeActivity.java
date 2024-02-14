package com.telemedicine.myclinic.activities;

import android.content.Intent;

import androidx.annotation.Nullable;

import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.telemedicine.myclinic.AppointmentDetailActivity;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.hyperpay.CustomUIActivity;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.localnotification.NotificationHelper;
import com.telemedicine.myclinic.models.CardDTO;
import com.telemedicine.myclinic.models.CardModel;
import com.telemedicine.myclinic.models.CardsListModel;
import com.telemedicine.myclinic.models.DisplayPaymentDTO;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.PayResAnswer;
import com.telemedicine.myclinic.models.PaymentResponsibilty;
import com.telemedicine.myclinic.models.appointments.OrdersRadModel;
import com.telemedicine.myclinic.models.bookAppointment.ApptCreateDTO;
import com.telemedicine.myclinic.models.bookAppointment.ApptCreateModel;
import com.telemedicine.myclinic.models.bookAppointment.ApptGetSlotsDTO;
import com.telemedicine.myclinic.models.bookAppointment.ApptGetSlotsModel;
import com.telemedicine.myclinic.models.bookAppointment.GetInsuranceByIdDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetInsuranceModel;
import com.telemedicine.myclinic.models.patientMiniModels.InsuranceCardModel;
import com.telemedicine.myclinic.models.payStage.PayStageOneDTO;
import com.telemedicine.myclinic.models.payStage.PayStageOneModel;
import com.telemedicine.myclinic.models.reschedule.ApptRescheduleDTO;
import com.telemedicine.myclinic.models.reschedule.ApptRescheduleModel;
import com.telemedicine.myclinic.models.secondaryAppt.ApptCreateSecondaryDTO;
import com.telemedicine.myclinic.models.teleprice.TelePriceDto;
import com.telemedicine.myclinic.models.teleprice.TelePriceResponse;
import com.telemedicine.myclinic.myapplication.ApptTermsSecondConditionsActivity;
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
import com.telemedicine.myclinic.webservices.RestClient;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.telemedicine.myclinic.util.Const.APPT_ID;
import static com.telemedicine.myclinic.util.Const.COMPANY_NAME_EN;
import static com.telemedicine.myclinic.util.Const.DATE;
import static com.telemedicine.myclinic.util.Const.DOCTOR_ID;
import static com.telemedicine.myclinic.util.Const.DOCTOR_IMAGE_URL;
import static com.telemedicine.myclinic.util.Const.DOCTOR_NAME;
import static com.telemedicine.myclinic.util.Const.DOCTOR_SPECIALITY;
import static com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY;
import static com.telemedicine.myclinic.util.Const.OREGID_KEY;
import static com.telemedicine.myclinic.util.Const.SECONDARY_APPT_REFRESH;
import static com.telemedicine.myclinic.util.Const.SERVICE_DATE;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_ID;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_NAME_AR;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_NAME_EN;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BookAppointmentThreeActivity extends BaseActivity {

    @BindView(R.id.doctor_name)
    BoldTextView doctorNameTv;

    @BindView(R.id.doctor_profession)
    LightTextView doctorProfession;

    @BindView(R.id.datetime_value)
    LightTextView mDateTime;

    @BindView(R.id.doctor_image)
    CircleImageView doctorImage;

    @BindView(R.id.confirm)
    LightButton confirm;

    @BindView(R.id.modify)
    LightButton modify;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @BindView(R.id.company_name)
    TextView companyNameTv;

    @BindView(R.id.chat_bot_bg)
    RelativeLayout chatBotBg;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.chatBot_rounded)
    View chatBot_rounded;

    @BindView(R.id.chat_bot_web_view)
    WebView chatBotWebView;

    boolean isChatBotVisible = false;

    String doctorName = "";
    String doctorImageUrl = "";
    String speciality = "";
    String date = "";
    String specialityId = "";
    String doctorId = "";
    boolean isTelemedcine = false;
    String selectedTime = "";
    long apptBookType;
    // long apptId;
    TinyDB tinyDB = null;
    String apptNo = "";
    boolean isSecondary = false;
    boolean isInsurance = false;
    boolean followUpAppt = false;
    OrdersRadModel ordersRadModel = null;
    long apptId = 0;

    String specialityNameEn= "";
    String specialityNameAr = "";
    String companyName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected int layout() {
        return R.layout.activity_bookappointment_three;
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
                ErrorMessage.getInstance().showSuccessGreen(BookAppointmentThreeActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameAr() + ". "+getString(R.string.please_press_to_continue));
            }else{
                ErrorMessage.getInstance().showSuccessGreen(BookAppointmentThreeActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameEn() + ". "+getString(R.string.please_press_to_continue));
            }
        } else {
            ErrorMessage.getInstance().showErrorYellow(BookAppointmentThreeActivity.this, appointmentEvent.getErrorMSg());
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

        Intent intent = getIntent();

        if (intent != null) {

            doctorName = intent.getStringExtra(DOCTOR_NAME);
            speciality = intent.getStringExtra(DOCTOR_SPECIALITY);
            date = intent.getStringExtra(DATE);
            dateForService = intent.getStringExtra(SERVICE_DATE);
            specialityId = intent.getStringExtra(SPECIALITY_ID);
            doctorId = intent.getStringExtra(DOCTOR_ID);
            isTelemedcine = intent.getBooleanExtra(Const.ISTELEMEDICINE_KEY, false);
            doctorImageUrl = intent.getStringExtra(DOCTOR_IMAGE_URL);
            selectedTime = intent.getStringExtra(Const.SELECTED_TIME);
            apptBookType = intent.getIntExtra(Const.APPT_BOOK_TYPE, 0);
            apptBookType = 10;
            specialityNameEn = intent.getStringExtra(SPECIALITY_NAME_EN);
            specialityNameAr = intent.getStringExtra(SPECIALITY_NAME_AR);
            companyName = intent.getStringExtra(COMPANY_NAME_EN);
            isSecondary = intent.getBooleanExtra(Const.IS_SECONDARY, false);
            ordersRadModel = intent.getParcelableExtra(Const.ORDER_RAD);
            isInsurance = intent.getBooleanExtra(Const.IS_INSURANCE_KEY, false);
            followUpAppt = intent.getBooleanExtra(Const.FOLLOW_UP_TYPE, false);
            apptId = intent.getLongExtra(Const.APPT_ID, 0);

            if (apptId != 0 && apptId > 0) {
                confirm.setText(R.string.confirm_reschedule);
            }

            setDoctorDetail();
            String companyNameId = tinyDB.getString(Const.COMPANY_ID);

            if (companyNameId.equals("nc01")) {
                companyName = getString(R.string.prince_sultan);
            } else if (companyNameId.equals("safa")) {
                companyName = getString(R.string.safa);
            }else if(companyNameId.equals("prc")){
                companyName = getString(R.string.prc);
            }

            if (!isTelemedcine) {
                companyNameTv.setText(companyName);
            }

        }

        if(isTelemedcine){
            getSlots();
        }

        showVersionCheckDialog(getString(R.string.follow_up_note));
    }


    void getSlots() {

        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        String dateee = tinyDB.getString(Const.T_DATE);
        String com = tinyDB.getString("com");
        String patientId = "null";
        long patId =  tinyDB.getLong(Const.PATIENT_ID, 0);
        boolean isAnonymousMode = true;


        ApptGetSlotsDTO apptGetSlotsDTO = new ApptGetSlotsDTO(securityToken,  String.valueOf(patId),  specialityId, String.valueOf(doctorId), dateee, isTelemedcine,
                com, false);

        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog();

            if (!isTelemedcine) {

            }else{
                RestClient.getInstance().getApptGetSlotsMedicine(apptGetSlotsDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {
                        hideWaitDialog();

                        ApptGetSlotsModel apptGetSlotsModel = (ApptGetSlotsModel) result;

                        if (apptGetSlotsModel != null) {

                            MobileOpResult mobileOpResult = apptGetSlotsModel.getMobileOpResult();

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                    //  apptBookType = apptGetSlotsModel.getAppointmentBookType();
                                 /*   if (apptGetSlotsModel != null && ValidationHelper.isNullOrEmpty(apptGetSlotsModel.getAMSlots())
                                            && ValidationHelper.isNullOrEmpty(apptGetSlotsModel.getPMSlots())
                                            && isWeekly) {

                                    } else*/
                                  //  setTimeSlots(apptGetSlotsModel);

                                } else {

                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(BookAppointmentThreeActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(BookAppointmentThreeActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMesg);
                                }

                            } else
                                ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMessage);
                        } else
                            ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMessage);
                    }
                });
            }

        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));

        }
    }

    public void Confirmed(View view) {

        // if apptId exist then its reschedule appt.
        if (apptId != 0 && apptId > 0) {

            rescheduleAppt();
            return;
        }


        if (followUpAppt) {
            bookAppt(false, 0);
            return;
        }

        if (isTelemedcine) {
            Intent intent = new Intent(this, ApptTermsSecondConditionsActivity.class);
            intent.putExtra(Const.TERMS_CONDITIONS_KEY, tinyDB.getString(Const.TELE_TERMS_KEY));
            intent.putExtra(Const.ISTELEMEDICINE_KEY, true);
            startActivityForResult(intent, 500);

        } else if (isSecondary) {

            String insuranceId = tinyDB.getString(Const.INSURANCE_ID);

            if (isInsurance) {
                // if insurance will be true
                getInsurance();

            } else {
                if (ConnectionUtil.isInetAvailable(this)) {
                    // book secondary appt
                    bookApptSecondary(false, 0);
                } else {
                    ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
                }
            }

        } else {
            showIsuranceOrBookappt();
        }
    }

    private void payDisplayPayment() {

        long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        String companyId = tinyDB.getString(Const.COMPANY_ID);

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            DisplayPaymentDTO displayPaymentDTO = new DisplayPaymentDTO(securityToken, String.valueOf(patientId), companyId);

            RestClient.getInstance().displayPaymentRes(displayPaymentDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    if (result != null) {

                        PaymentResponsibilty paymentResponsibilty = (PaymentResponsibilty) result;

                        if (paymentResponsibilty != null) {

                            MobileOpResult mobileOpResult = paymentResponsibilty.getMobileOpResult();

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                    PayResAnswer payResAnswer = paymentResponsibilty.getPayResAnswer();

                                    if (payResAnswer != null) {

                                        String msg = "";

                                        if (TextUtil.getEnglish(BookAppointmentThreeActivity.this))
                                            msg = payResAnswer.getReasonEN();
                                        else
                                            msg = payResAnswer.getReasonAr();

                                        if (payResAnswer.isShowPaymentResponsibilty()) {
                                            getInsurance();
                                        } else {
                                            if(!isTelemedcine){
                                                getInsurance();
                                            }else{
                                                dialogue(msg);
                                            }
                                        }
                                    }
                                } else {
                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(BookAppointmentThreeActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(BookAppointmentThreeActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMesg);
                                }
                            } else
                                ErrorMessage.getInstance().showWarning(BookAppointmentThreeActivity.this, errorMessage);
                        } else
                            ErrorMessage.getInstance().showWarning(BookAppointmentThreeActivity.this, errorMessage);
                    } else
                        ErrorMessage.getInstance().showWarning(BookAppointmentThreeActivity.this, errorMessage);
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(BookAppointmentThreeActivity.this, getString(R.string.internet_connection));
        }

    }

    void showIsuranceOrBookappt() {
//5637467719
        String insuranceId = tinyDB.getString(Const.INSURANCE_ID);
        if (insuranceId.equalsIgnoreCase("0")) {
            if (isTelemedcine)
                getPrice(false, 0, true);
            else
                bookAppt(false, 0);
        } else {
            payDisplayPayment();
        }
    }

    private void bookApptSecondary(boolean isInsurance, long insuranceID) {

        showWaitDialog();

        //date = mDateTime.getText().toString();

        //date = LocaleDateHelper.convertDateStringFormat(date, "EEEE, dd MMMM yyyy hh:mm a", "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
        String companyId = tinyDB.getString(Const.COMPANY_ID);
        long orderId = 0;
        if (ordersRadModel != null)
            orderId = ordersRadModel.getId();

        ApptCreateSecondaryDTO secondaryDTO = new ApptCreateSecondaryDTO(securityToken, String.valueOf(patientId),
                String.valueOf(orderId), dateForService, insuranceID, isInsurance, companyId);

        RestClient.getInstance().apptCreateSecondary(secondaryDTO, new OnResultListener() {
            @Override
            public void onResult(Object result, boolean status, String errorMessage) {

                hideWaitDialog();

                ApptCreateModel apptCreateModel = (ApptCreateModel) result;

                if (apptCreateModel != null) {

                    MobileOpResult mobileOpResult = apptCreateModel.getMobileOpResult();

                    if (mobileOpResult != null) {

                        if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                            apptNo = apptCreateModel.getApptNo();
                            long apptId = apptCreateModel.getApptId();
                            SECONDARY_APPT_REFRESH = true;
                            showSuceessFulMessage(apptNo, apptId);

                        } else {

                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                if (TextUtil.getEnglish(BookAppointmentThreeActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                else if (TextUtil.getArabic(BookAppointmentThreeActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }

                            ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMesg);
                        }

                    } else
                        ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMessage);

                } else {
                    ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMessage);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 113 && resultCode == RESULT_OK) {

            if (data != null) {

                boolean isInsurance = data.getBooleanExtra(Const.IS_INSURANCE_KEY, false);
                long insuranceId = data.getLongExtra(Const.INSURANCE_ID, 0);
                double telePrice = data.getDoubleExtra(Const.TELE_PRICE, 0);

                if (isSecondary) {
                    bookApptSecondary(isInsurance, insuranceId);
                } else if (isTelemedcine) {
                    telePriceAppt(String.valueOf(telePrice), isInsurance, insuranceId);
                } else {
                    bookAppt(isInsurance, insuranceId);
                }

            }
        } else if (requestCode == 500 && resultCode == RESULT_OK) {

            boolean accepted = data.getBooleanExtra(Const.ACCEPTED_TELE, false);

            if (accepted) {

                // call insurance here
                showIsuranceOrBookappt();

/*                double telePrice = tinyDB.getDouble(Const.PRICE, 0);

                ArrayList<Object> opsConfig = tinyDB.getListObject(Const.ONLINE_CONFIG_KEY, OpsConfigsModel.class);

                if (opsConfig != null) {

                    for (Object object : opsConfig) {

                        OpsConfigsModel opsConfigsModel = (OpsConfigsModel) object;

                        if (opsConfigsModel != null) {

                            if (opsConfigsModel.getOpsConfigId() == 1 && opsConfigsModel.getKey().equalsIgnoreCase(getString(R.string.telemedicine_service_price))) {

                                String price = opsConfigsModel.getVal();

                                if (!ValidationHelper.isNullOrEmpty(price)) {

                                    double priceVal = Double.valueOf(price);

                                    if (ConnectionUtil.isInetAvailable(this)) {

                                        if (priceVal < 1) {
                                            bookAppt(false, 0);
                                        } else {
                                            payStageOne(priceVal);
                                        }
                                    } else {
                                        ErrorMessage.getInstance().showWarning(this, "Internet is not available");

                                    }
                                }
                            }
                        }
                    }
                }*/
            }
        } else if (requestCode == 600 && resultCode == RESULT_OK) {

            if (data != null) {
                long apptId = data.getLongExtra(Const.APPT_ID, 0);
                showSuceessFulMessage("", apptId);
            }
        }
    }

    void telePriceAppt(String priceval, boolean isInsurance, long insuranceId) {

        String price = priceval;

        if (!ValidationHelper.isNullOrEmpty(price)) {

            double priceValue = Double.valueOf(price);

            if (ConnectionUtil.isInetAvailable(this)) {

                if (priceValue < 1) {
                    bookAppt(isInsurance, insuranceId);
                } else {
                    dialgoueCash(priceValue, isInsurance, insuranceId);
                }
            } else {
                ErrorMessage.getInstance().showWarning(this, "Internet is not available");
            }
        }

    }

    String dateForService = "";
    void setDoctorDetail() {
        doctorNameTv.setText(doctorName);
        doctorProfession.setText(speciality);

       // String dateSTr = LocaleDateHelper.convertDateStringFormat(date, "yyyy-MM-dd'T'hh:mm:ss", "EEEE, dd MMMM yyyy hh:mm a", Locale.getDefault());
        String dateSTr = LocaleDateHelper.convertDateStringFormat(date, "yyyy-MM-dd'T'hh:mm:ss", "EEEE, dd MMMM yyyy", Locale.getDefault());

        String time = selectedTime;
        if (TextUtil.getArabic(this)) {
            time = time.replaceAll(" AM", " ص");
            time = time.replaceAll(" PM", " م");
        }
        dateSTr = dateSTr + " " + time;

        mDateTime.setText(dateSTr);

        notificationDate = mDateTime.getText().toString();

        if (!ValidationHelper.isNullOrEmpty(doctorImageUrl)) {
            Glide.with(this).load(doctorImageUrl).placeholder(R.drawable.doctr).into((ImageView) doctorImage);
        } else {
            if (isSecondary)
                Glide.with(this).load(R.drawable.xray).placeholder(R.drawable.doctr).into((ImageView) doctorImage);
        }
    }

    public void Modify(View view) {
        finish();
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

                                    //  if (!ValidationHelper.isNullOrEmpty(getInsuranceModelsList)) {

                                    Intent intent = new Intent(BookAppointmentThreeActivity.this, InsuranceCardCashCardActivity.class);
                                    intent.putExtra("insuranceCard", getInsuranceModelsList);
                                    intent.putExtra("noinsurance", specialityId);
                                    intent.putExtra("doctorId", doctorId);
                                    intent.putExtra(Const.ISTELEMEDICINE_KEY, isTelemedcine);
                                    startActivityForResult(intent, 113);
                                    //  }

                                } else {

                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(BookAppointmentThreeActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(BookAppointmentThreeActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMesg);
                                }

                            } else
                                ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMessage);

                        } else {
                            ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMessage);
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

    String notificationDate = "";

    void bookAppt(boolean isInsurance, long insuranceId) {
        date = mDateTime.getText().toString();
        date = LocaleDateHelper.convertDateStringFormat(date, "EEEE, dd MMMM yyyy hh:mm a", "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

        TinyDB tinyDB = new TinyDB(this);
        long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
        String securityToken = tinyDB.getString(Const.TOKEN_KEY);

        String companyId = tinyDB.getString(Const.COMPANY_ID);

        if (followUpAppt)
            apptBookType = 20;

        ApptCreateDTO apptCreateDTO = new ApptCreateDTO(securityToken, String.valueOf(patientId), specialityId, doctorId,
                String.valueOf(apptBookType), dateForService, isTelemedcine, isInsurance, insuranceId, companyId);

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

                                apptNo = apptCreateModel.getApptNo();
                                long apptId = apptCreateModel.getApptId();

                                showSuceessFulMessage(apptNo, apptId);

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(BookAppointmentThreeActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(BookAppointmentThreeActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                if(mobileOpResult.getResult() == 600 || mobileOpResult.getResult() == 700){
                                    ErrorMessage.getInstance().showErrorYellow(BookAppointmentThreeActivity.this, errorMesg);
                                }else{
                                    ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMesg);
                                }
                               // ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMessage);

                    } else {
                        ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMessage);
                    }
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, "Internet is not available");
        }
    }

    void rescheduleSuceessFulMessage(String apptdate, long apptId) {

        try {

            SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
            alertDialog.setCancelable(false);
            alertDialog.setTitleText(getResources().getString(R.string.my_clininc))
                    .setContentText(getString(R.string.appt_reschedule_successfully))
                    .setConfirmText(getString(R.string.ok)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    // added after
                    Intent intent = new Intent(BookAppointmentThreeActivity.this, BookAppointmentFourActivity.class);


                    intent.putExtra(DOCTOR_NAME, doctorName);
                    String jsonModel  = new GsonBuilder().create().toJson(mApptsMiniModel);
                    intent.putExtra(Const.APPTMODEL, jsonModel);

                    intent.putExtra(DOCTOR_SPECIALITY, speciality);
                    String date = mDateTime.getText().toString();
                    intent.putExtra(DATE, date);
                    intent.putExtra(SPECIALITY_ID, specialityId);
                    intent.putExtra(DOCTOR_ID, doctorId);
                    intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcine);
                    intent.putExtra(DOCTOR_IMAGE_URL, doctorImageUrl);
                    intent.putExtra(Const.APPT_BOOK_TYPE, apptBookType);
                    intent.putExtra(Const.APPT_ID, apptId);
                    intent.putExtra(Const.APP_NO, apptNo);
                    intent.putExtra(Const.IS_SECONDARY, isSecondary);
                    intent.putExtra(Const.FOLLOW_UP_TYPE, followUpAppt);
                    startActivity(intent);
                    SECONDARY_APPT_REFRESH = true;
                    sendBroadCast();
                    Intent intent1 = new Intent();
                    setResult(RESULT_OK, intent1);
                    finish();

                    // only this code was there ,
                  /*  Intent intent = new Intent();
                    intent.putExtra(Const.UPDATED_TIME, apptdate);
                    intent.putExtra(Const.APPT_ID, apptId);
                    setResult(RESULT_OK, intent);
                    finish();*/

                }
            }).show();
            Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
            btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    IOSDialog iosDialog3 = null;

    void showSuceessFulMessage(String apptNo, long apptId) {

        try {
            String apptMsg = getString(R.string.appt_booked_successfully);

            // set the local notification timer

            if (isTelemedcine) {
                apptMsg = getString(R.string.teleAPptBookedmsg);

                try {

                    /*String title = getString(R.string.reminder) + "\n " + getString(R.string.appointment_with) + " " + doctorName + " " + getString(R.string.at_my_clinic);
                    String msg = " " + tinyDB.getString(Const.PATIENT_NAME) + " " + getString(R.string.has_an_appointment_with) + " " + doctorName + " (" + speciality + ") " + getString(R.string.at) + " " + notificationDate +
                            ".";// + getString(R.string.apptment_expired_msg);*/

                    String title = getString(R.string.reminder);
                    String msg = getString(R.string.this_is_a_kind_reminder) + " " + doctorName + " " + getString(R.string.on) + " " + notificationDate +
                            getString(R.string.please_logon_at_that);// + getString(R.string.apptment_expired_msg);

                    long getTimeMillis = LocaleDateHelper.getApptTimeInMilliOther(notificationDate);

                    NotificationHelper.getInstance().scheduleNotification(BookAppointmentThreeActivity.this, getTimeMillis, (int) apptId, title, msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                iosDialog3 = TextUtil.dialgoueSuccessBooking(BookAppointmentThreeActivity.this, apptMsg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iosDialog3.dismiss();
                        showApptCancelAlert(apptNo, apptId);
                    }
                });
            }else{
                  iosDialog3 = TextUtil.dialgoueNonTeleSuccessBooking(BookAppointmentThreeActivity.this, apptMsg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iosDialog3.dismiss();
                        Intent intent = new Intent(BookAppointmentThreeActivity.this, BookAppointmentFourActivity.class);
                        intent.putExtra(DOCTOR_NAME, doctorName);
                        String jsonModel  = new GsonBuilder().create().toJson(mApptsMiniModel);
                        intent.putExtra(Const.APPTMODEL, jsonModel);
                        intent.putExtra(DOCTOR_SPECIALITY, speciality);
                        String date = mDateTime.getText().toString();
                        intent.putExtra(DATE, date);
                        intent.putExtra(SPECIALITY_ID, specialityId);
                        intent.putExtra(DOCTOR_ID, doctorId);
                        intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcine);
                        intent.putExtra(DOCTOR_IMAGE_URL, doctorImageUrl);
                        intent.putExtra(Const.APPT_BOOK_TYPE, apptBookType);
                        intent.putExtra(Const.APPT_ID, apptId);
                        intent.putExtra(Const.APP_NO, apptNo);
                        intent.putExtra(Const.IS_SECONDARY, isSecondary);
                        intent.putExtra(Const.FOLLOW_UP_TYPE, followUpAppt);
                        intent.putExtra("teleprice", finalPrice);
                        startActivity(intent);
                        sendBroadCast();
                        Intent intent1 = new Intent();
                        setResult(RESULT_OK, intent1);
                        finish();
                    }
                });
            }

            /*SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
            alertDialog.setCancelable(false);
            alertDialog.setTitleText(getString(R.string.appt_booked_successfully_title))
                    .setContentText(apptMsg)
                    .setConfirmText(getString(R.string.ok)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    alertDialog.dismiss();
                    showApptCancelAlert(apptNo, apptId);

                }
            }).show();
            Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
            btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    IOSDialog iosDialog2 = null;

    private void showApptCancelAlert(String apptNo, long apptId) {

        iosDialog2 = TextUtil.dialgoue(BookAppointmentThreeActivity.this, getString(R.string.teleAPptBookedmsgCancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iosDialog2.dismiss();
                Intent intent = new Intent(BookAppointmentThreeActivity.this, BookAppointmentFourActivity.class);
                intent.putExtra(DOCTOR_NAME, doctorName);
                intent.putExtra(DOCTOR_SPECIALITY, speciality);
                String date = mDateTime.getText().toString();
                String jsonModel  = new GsonBuilder().create().toJson(mApptsMiniModel);
                intent.putExtra(Const.APPTMODEL, jsonModel);
                intent.putExtra(DATE, date);
                intent.putExtra(SPECIALITY_ID, specialityId);
                intent.putExtra(DOCTOR_ID, doctorId);
                intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcine);
                intent.putExtra(DOCTOR_IMAGE_URL, doctorImageUrl);
                intent.putExtra(Const.APPT_BOOK_TYPE, apptBookType);
                intent.putExtra(Const.APPT_ID, apptId);
                intent.putExtra(Const.APP_NO, apptNo);
                intent.putExtra(Const.IS_SECONDARY, isSecondary);
                intent.putExtra(Const.FOLLOW_UP_TYPE, followUpAppt);
                intent.putExtra("teleprice", finalPrice);
                startActivity(intent);
                sendBroadCast();
                Intent intent1 = new Intent();
                setResult(RESULT_OK, intent1);
                finish();
            }
        });
    }

    double finalPrice = 0;

    void payStageOne(double price, boolean isInsurance, long insuranceId) {

        showWaitDialog();
        String companyId = tinyDB.getString(Const.COMPANY_ID);
        String customerEmail = tinyDB.getString(Const.EMAIL_KEY);
        String ORegId = tinyDB.getString(Const.OREGID_KEY);
        String doctId = doctorId;
        String dateTime = LocaleDateHelper.convertDateStringFormat(this.mDateTime.getText().toString(), "EEEE, dd MMMM yyyy hh:mm a", "dd/MM/yyyy HH:mm", Locale.ENGLISH);
        String apptIdStr = ORegId + "-" + doctId + "-" + dateTime;

        price = Math.round(price);

        String ammount = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US)).format(price);

        PayStageOneDTO payStageOneDTO =
                new PayStageOneDTO(ammount, customerEmail, apptIdStr, companyId);

        finalPrice = price;
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

                            getCards(checkOutId, String.valueOf(finalPrice), isInsurance, insuranceId);

                        } else {
                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                if (TextUtil.getEnglish(BookAppointmentThreeActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                else if (TextUtil.getArabic(BookAppointmentThreeActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }

                            ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMesg);

                        }

                    } else {
                        hideWaitDialog();
                        ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMessage);
                    }

                } else {
                    hideWaitDialog();
                    ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMessage);
                }
            }
        });
    }

    void getCards(String checkOutId, String mFinalPrice, boolean mIsInsurance, long mInsuranceId) {

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
                                    // Intent intent = new Intent(BookAppointmentThreeActivity.this, CustomUIActivity.class);
                                    Intent intent = new Intent(BookAppointmentThreeActivity.this, CustomUIActivity.class);
                                    intent.putExtra("amount", mFinalPrice);
                                    intent.putExtra(Const.IS_FROM_CARD_LISTING, false);
                                    intent.putExtra(Const.CHECK_OUT_ID, checkOutId);
                                    intent.putExtra(DOCTOR_NAME, doctorName);
                                    intent.putExtra(DOCTOR_SPECIALITY, speciality);
                                    String date = mDateTime.getText().toString();
                                    intent.putExtra(DATE, date);
                                    intent.putExtra(SPECIALITY_ID, specialityId);
                                    intent.putExtra(DOCTOR_ID, doctorId);
                                    intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcine);
                                    intent.putExtra(DOCTOR_IMAGE_URL, doctorImageUrl);
                                    intent.putExtra(Const.APPT_BOOK_TYPE, apptBookType);
                                    intent.putExtra(Const.APPT_ID, apptId);
                                    intent.putExtra(Const.IS_INSURANCE_KEY, mIsInsurance);
                                    intent.putExtra(Const.INSURANCE_ID, mInsuranceId);
                                    startActivityForResult(intent, 600);

                                } else {
                                    // Intent intent = new Intent(BookAppointmentThreeActivity.this, CustomUIActivity.class);
                                    Intent intent = new Intent(BookAppointmentThreeActivity.this, CardListActivity.class);
                                    intent.putExtra("amount", mFinalPrice);
                                    intent.putExtra(Const.CHECK_OUT_ID, checkOutId);
                                    intent.putExtra(DOCTOR_NAME, doctorName);
                                    intent.putExtra(DOCTOR_SPECIALITY, speciality);
                                    String date = mDateTime.getText().toString();
                                    intent.putExtra(DATE, date);
                                    intent.putExtra(SPECIALITY_ID, specialityId);
                                    intent.putExtra(DOCTOR_ID, doctorId);
                                    intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcine);
                                    intent.putExtra(DOCTOR_IMAGE_URL, doctorImageUrl);
                                    intent.putExtra(Const.APPT_BOOK_TYPE, apptBookType);
                                    intent.putExtra(Const.APPT_ID, apptId);
                                    intent.putExtra(Const.IS_INSURANCE_KEY, mIsInsurance);
                                    intent.putExtra(Const.INSURANCE_ID, mInsuranceId);
                                    startActivityForResult(intent, 600);
                                }


                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(BookAppointmentThreeActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(BookAppointmentThreeActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMesg);

                            }

                        } else {

                            ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMessage);
                        }

                    } else {

                        ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMessage);
                    }
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
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

    String apptDate = "";

    void rescheduleAppt() {

        if (ConnectionUtil.isInetAvailable(BookAppointmentThreeActivity.this)) {

            showWaitDialog();

            apptDate = mDateTime.getText().toString();



            String formattedDateTime = LocaleDateHelper.convertDateStringFormat(apptDate, "EEEE, dd MMMM yyyy hh:mm a", "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);

            String companyId = tinyDB.getString(Const.COMPANY_ID);

            ApptRescheduleDTO apptRescheduleDTO =
                    new ApptRescheduleDTO(securityToken, apptId, Long.valueOf(doctorId), formattedDateTime, isSecondary, companyId);

            RestClient.getInstance().apptReschedule(apptRescheduleDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {
                    hideWaitDialog();

                    ApptRescheduleModel apptRescheduleModel = (ApptRescheduleModel) result;

                    if (apptRescheduleModel != null) {

                        MobileOpResult mobileOpResult = apptRescheduleModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                long apptId = apptRescheduleModel.getApptId();
                                apptNo = apptRescheduleModel.getApptNo();
                                rescheduleSuceessFulMessage(apptDate, apptId);

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(BookAppointmentThreeActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(BookAppointmentThreeActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                if(mobileOpResult.getResult() == 600 || mobileOpResult.getResult() == 700){
                                    ErrorMessage.getInstance().showErrorYellow(BookAppointmentThreeActivity.this, errorMesg);
                                }else{
                                    ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMesg);
                                }

                               // ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMesg);
                            }

                        } else {
                            ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMessage);
                        }

                    } else {
                        ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMessage);
                    }
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(BookAppointmentThreeActivity.this, getString(R.string.internet_connection));
        }
    }


    IOSDialog dialogFollowUp = null;
    private void showVersionCheckDialog(String businessErrorMessage) {

        dialogFollowUp = TextUtil.dialgoue(BookAppointmentThreeActivity.this, businessErrorMessage,getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialogFollowUp.dismiss();
            }
        });
    }

    void transformView() {
        if (TextUtil.getArabic(this)) {
            toolbar_left_iv.setRotation(180);
            modify.setCompoundDrawablesWithIntrinsicBounds(R.drawable.grey_left_arrow, 0, 0, 0);
            confirm.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
        }
    }

    void getPrice(boolean isInsurance, long insuranceId, boolean isCash) {

        String companyId = tinyDB.getString(Const.COMPANY_ID);
        showWaitDialog();

        RestClient.getInstance().telePrice(new TelePriceDto(tinyDB.getString(Const.TOKEN_KEY), isCash, insuranceId, Long.parseLong(doctorId), companyId), new OnResultListener() {
            @Override
            public void onResult(Object result, boolean status, String errorMessage) {

                hideWaitDialog();

                TelePriceResponse telePriceResponse = (TelePriceResponse) result;

                if (telePriceResponse != null) {

                    MobileOpResult mobileOpResult = telePriceResponse.getMobileOpResult();

                    if (mobileOpResult != null) {

                        if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                            double amount = telePriceResponse.getAmountDue();
                            if (!isTelemedcine) {
                                if (LocaleDateHelper.expiredAptTime(dateForService)) {
                                    showCheckinDialog();
                                    return;
                                }
                            }
                            telePriceAppt(String.valueOf(amount), isInsurance, insuranceId);
                        } else {

                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                if (TextUtil.getEnglish(BookAppointmentThreeActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                else if (TextUtil.getArabic(BookAppointmentThreeActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }

                            ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMesg);
                        }
                    }
                } else {
                    ErrorMessage.getInstance().showError(BookAppointmentThreeActivity.this, errorMessage);
                }

            }
        });
    }

    IOSDialog iosDialog1 = null;

    private void showCheckinDialog() {
        iosDialog1 = TextUtil.dialgoue(BookAppointmentThreeActivity.this, getString(R.string.check_in_eligible), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iosDialog1.dismiss();
            }
        });
    }

    public void dialogue(String msg) {

        SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setCancelable(false);
        alertDialog.setTitleText(getString(R.string.my_clininc)).setCancelText(getString(R.string.cancel))
                .setContentText(msg).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                alertDialog.dismiss();
            }
        }).setConfirmText(getString(R.string._continue)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                alertDialog.dismiss();
                if (ConnectionUtil.isInetAvailable(BookAppointmentThreeActivity.this)) {
                    if (isTelemedcine)
                        getPrice(false, 0, true);

                } else {
                    ErrorMessage.getInstance().showWarning(BookAppointmentThreeActivity.this, getString(R.string.internet_connection));
                }
            }
        }).show();
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

                if (ConnectionUtil.isInetAvailable(BookAppointmentThreeActivity.this)) {
                    payStageOne(Double.valueOf(priceval), isInsurance, insuranceId);
                } else {
                    ErrorMessage.getInstance().showWarning(BookAppointmentThreeActivity.this, getString(R.string.internet_connection));
                }
            }
        }).show();
    }


}
