package com.telemedicine.myclinic.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.telemedicine.myclinic.adapters.AdapterUpcomingAppointments;
import com.telemedicine.myclinic.adapters.NotificationsAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MarkReadNotificationDTO;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.NotificationDTO;
import com.telemedicine.myclinic.models.NotificationMiniModel;
import com.telemedicine.myclinic.models.NotificationModel;
import com.telemedicine.myclinic.models.appointments.AppointmentsDTO;
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel;
import com.telemedicine.myclinic.models.appointments.ApptsModel;
import com.telemedicine.myclinic.models.profilecreatoinmodels.SecurityTokenDTO;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.AppointmentEvent;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.webservices.RestClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.telemedicine.myclinic.util.Const.NOTIFICATION_BODY;
import static com.telemedicine.myclinic.util.Const.NOTIFICATION_TITLE;
import static com.telemedicine.myclinic.util.Const.NOTIFICATION_URL;
import static com.telemedicine.myclinic.util.Const.TOKEN_KEY;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class NotificationsActivity extends BaseActivity implements NotificationsAdapter.OnCardClickListner {

    @BindView(R.id.notification_recycler_view)
    RecyclerView notificationRecyclerView;

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

    @BindView(R.id.chatBot_rounded)
    View chatBot_rounded;

    @BindView(R.id.chat_bot_web_view)
    WebView chatBotWebView;

    boolean isChatBotVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refresh.setVisibility(View.GONE);
        init();
    }

    @Override
    protected int layout() {
        return R.layout.activity_notifications;
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
       // linearLayoutManager.setReverseLayout(true);
       // linearLayoutManager.setStackFromEnd(true);
        notificationRecyclerView.setLayoutManager(linearLayoutManager);
        transformViews();
        getNotifications();
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
                                        noApptmnt.setVisibility(View.GONE);
                                        loadNotificationList(notificationMiniModel);
                                    }

                                } else {
                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(NotificationsActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(NotificationsActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(NotificationsActivity.this, errorMesg);
                                }
                            } else {
                                ErrorMessage.getInstance().showError(NotificationsActivity.this, errorMessage);
                            }
                        } else {
                            ErrorMessage.getInstance().showError(NotificationsActivity.this, errorMessage);
                        }
                    }
                });
            }
        } else {
            ErrorMessage.getInstance().showError(this, getString(R.string.internet_connection));
        }
    }

    NotificationsAdapter adapterNotification = null;
    ArrayList<NotificationMiniModel> notificationMiniModel = new ArrayList<NotificationMiniModel>();
    void loadNotificationList(ArrayList<NotificationMiniModel> notificationMiniModel) {
        this.notificationMiniModel.clear();
        this.notificationMiniModel= notificationMiniModel;
        adapterNotification = new NotificationsAdapter(this, this.notificationMiniModel);
        notificationRecyclerView.setAdapter(adapterNotification);
        notificationRecyclerView.setHasFixedSize(true);
        adapterNotification.setOnCardClickListner(this);
    }


    @Override
    public void onBackPressed() {
        if(isChatBotVisible){
            openChatBotBg(null);
            return;
        }
        super.onBackPressed();
    }

    boolean isReadCalled= false;
    @Override
    public void OnCardClicked(NotificationMiniModel model, int pos) {

        if(!model.getIsRead()){
            isReadCalled = true;
            notificationMiniModel.get(pos).setIsRead(true);
            adapterNotification.notifyItemChanged(pos);
            callIsReadApi(model);
        }else{
            navigateToDetailPage(model);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isReadCalled){
           // isReadCalled = false;
            //getNotifications();
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
                ErrorMessage.getInstance().showSuccessGreen(NotificationsActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameAr() + ". "+getString(R.string.please_press_to_continue));
            }else{
                ErrorMessage.getInstance().showSuccessGreen(NotificationsActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameEn() + ". "+getString(R.string.please_press_to_continue));
            }
        } else {
            ErrorMessage.getInstance().showErrorYellow(NotificationsActivity.this, appointmentEvent.getErrorMSg());
        }
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    private void callIsReadApi(NotificationMiniModel model) {
        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            String securityToken = new TinyDB(this).getString(Const.TOKEN_KEY);
            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
            String oRegId = tinyDB.getString(Const.OREGID_KEY);

            if (!ValidationHelper.isNullOrEmpty(securityToken)) {

                MarkReadNotificationDTO markReadNotificationDTO = new MarkReadNotificationDTO(securityToken,
                        String.valueOf(patientId), String.valueOf(model.getId()), "2022-05-24", oRegId);

                RestClient.getInstance().markNotificationRead(markReadNotificationDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        hideWaitDialog();

                        MobileOpResult mobileOpResult = (MobileOpResult) result;

                        if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                    navigateToDetailPage(model);

                                } else {
                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(NotificationsActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(NotificationsActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(NotificationsActivity.this, errorMesg);
                                }
                        } else {
                            ErrorMessage.getInstance().showError(NotificationsActivity.this, errorMessage);
                        }
                    }
                });
            }
        } else {
            ErrorMessage.getInstance().showError(this, getString(R.string.internet_connection));
        }
    }

    private void navigateToDetailPage(NotificationMiniModel model) {
        Intent intent = new Intent(this, NotificationsDetailActivity.class);
        if(TextUtil.getEnglish(this)){
            intent.putExtra(NOTIFICATION_TITLE, model.getTitle());
            intent.putExtra(NOTIFICATION_BODY, model.getBody());
        }else{
            intent.putExtra(NOTIFICATION_TITLE, model.getTitleAr());
            intent.putExtra(NOTIFICATION_BODY, model.getBodyAr());
        }

        intent.putExtra(NOTIFICATION_URL, model.getUrl());
        startActivity(intent);
    }

    void transformViews() {
        if (TextUtil.getArabic(this)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar_left_iv.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            toolbar_left_iv.setLayoutParams(params);
            toolbar_left_iv.setRotation(180);
        } else {

        }
    }
}