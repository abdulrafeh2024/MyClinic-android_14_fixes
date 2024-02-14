package com.telemedicine.myclinic.activities;

import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.GsonBuilder;
import com.telemedicine.myclinic.AppointmentDetailActivity;
import com.telemedicine.myclinic.adapters.AdapterPastAppointments;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.appointments.AppointmentsDTO;
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel;
import com.telemedicine.myclinic.models.appointments.ApptsModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.AppointmentEvent;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.webservices.RestClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.telemedicine.myclinic.util.Const.SECONDARY_APPT_REFRESH;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PastAppointmentActivity extends BaseActivity implements AdapterPastAppointments.OnCardClickListner {

    @BindView(R.id.doctor_recycler_view)
    RecyclerView doctorRecyclerView;

    @BindView(R.id.no_apptmnt)
    RegularTextView noApptmnt;

    @BindView(R.id.toolbar_left_iv)
    ImageView   toolbar_left_iv;

    @BindView(R.id.chat_bot_bg)
    RelativeLayout chatBotBg;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.chatBot_rounded)
    View chatBot_rounded;

    @BindView(R.id.chat_bot_web_view)
    WebView chatBotWebView;

    boolean isChatBotVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transformViews();
        init();
        apptGetSummaryBySType();
    }

    public void openChatBot(View view){

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

    public void openChatBotBg(View view){
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

        if(isChatBotVisible){
            openChatBotBg(null);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected int layout() {
        return R.layout.activity_past_appointment;
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
                ErrorMessage.getInstance().showSuccessGreen(PastAppointmentActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameAr() + ". "+getString(R.string.please_press_to_continue));
            }else{
                ErrorMessage.getInstance().showSuccessGreen(PastAppointmentActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameEn() + ". "+getString(R.string.please_press_to_continue));
            }
        } else {
            ErrorMessage.getInstance().showErrorYellow(PastAppointmentActivity.this, appointmentEvent.getErrorMSg());
        }
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

        doctorRecyclerView.setLayoutManager
                (new LinearLayoutManager(this));
    }

    void loadDoctorList(ArrayList<ApptsMiniModel> apptsMiniModels) {

        ArrayList<ApptsMiniModel> apptsMiniModelsFilter = new ArrayList<>();

        for (ApptsMiniModel apptsMiniModel : apptsMiniModels) {
            int apptType = apptsMiniModel.getApptBookType();
            if (apptType != 30 ) {
                apptsMiniModelsFilter.add(apptsMiniModel);
            }
        }

        AdapterPastAppointments adapterDoctors = new AdapterPastAppointments(this, apptsMiniModelsFilter);
        doctorRecyclerView.setAdapter(adapterDoctors);
        adapterDoctors.setOnCardClickListner(this);
    }

    @Override
    public void OnCardClicked(ApptsMiniModel model, boolean cardClick) {

        SECONDARY_APPT_REFRESH = false;
        //setting company id

        if(model.getCompany() != null){
            tinyDB.putString(Const.COMPANY_ID,model.getCompany());
        }

        if(model.getCompanyNameAr() != null){
            tinyDB.putString(Const.COMPANY_NAME_AR,model.getCompanyNameAr());
        }

        if(model.getCompanyNameEn() != null){
            tinyDB.putString(Const.COMPANY_NAME_EN,model.getCompanyNameEn());
        }

        if (cardClick) {
            Intent intent = new Intent(this, AppointmentDetailActivity.class);
            intent.putExtra("pastAppointment", true);
            startActivity(intent);
        } else {
            long apptId = model.getApptId();
            Intent intent = new Intent(this, ServicesOrdersActivity.class);
            intent.putExtra(Const.APPT_ID, apptId);
            String jsonModel  = new GsonBuilder().create().toJson(model);
            intent.putExtra(Const.APPTMODEL, jsonModel);
          //  intent.putExtra(Const.APPTMODEL, model);
            startActivityForResult(intent, 7200);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7200 && resultCode == RESULT_OK) {
            finish();
        }
    }

    void apptGetSummaryBySType() {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            String securityToken = new TinyDB(this).getString(Const.TOKEN_KEY);

            long patientId = new TinyDB(this).getLong(Const.PATIENT_ID, 0);

            String apptType = "1";

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
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(PastAppointmentActivity.this, errorMesg);
                                }
                            } else {
                                ErrorMessage.getInstance().showError(PastAppointmentActivity.this, errorMessage);
                            }
                        } else {
                            ErrorMessage.getInstance().showError(PastAppointmentActivity.this, errorMessage);
                        }
                    }
                });
            }
        } else {
            ErrorMessage.getInstance().showError(this, getString(R.string.internet_connection));
        }
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    void transformViews() {
        if (TextUtil.getArabic(this)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar_left_iv.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            toolbar_left_iv.setLayoutParams(params);
            toolbar_left_iv.setRotation(180);
        }
    }

}
