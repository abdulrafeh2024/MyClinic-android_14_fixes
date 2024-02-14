package com.telemedicine.myclinic.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ligl.android.widget.iosdialog.IOSDialog;
import com.telemedicine.myclinic.adapters.LabReportAdapter;
import com.telemedicine.myclinic.adapters.LabTestAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.AfgaReportDTO;
import com.telemedicine.myclinic.models.LabReportMainModel;
import com.telemedicine.myclinic.models.LabReportUrlDTO;
import com.telemedicine.myclinic.models.LabReportUrlResponseModel;
import com.telemedicine.myclinic.models.LabReportsResponseModel;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.ReportSharingDTO;
import com.telemedicine.myclinic.models.ReportsDTO;
import com.telemedicine.myclinic.models.SendRadiologyResponseModel;
import com.telemedicine.myclinic.models.appointments.ApptDetailDTO;
import com.telemedicine.myclinic.models.appointments.ApptDetailModel;
import com.telemedicine.myclinic.models.appointments.OrdersLabModel;
import com.telemedicine.myclinic.models.humanTest.TranslateLabDTO;
import com.telemedicine.myclinic.models.humanTest.TranslateLabModel;
import com.telemedicine.myclinic.models.securityChallenge.SecurityChallengeDTO;
import com.telemedicine.myclinic.models.securityChallenge.SecurityChallengeModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.AppointmentEvent;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.LightEdittext;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.webservices.RestClient;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.telemedicine.myclinic.util.Const.IS_LAB;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LaboratoryReportsActivity extends BaseActivity implements LabReportAdapter.OnCardClickListner {

    @BindView(R.id.lab_test_recycler)
    RecyclerView labTestRecycler;

    @BindView(R.id.title_services)
    RegularTextView title_services;

    @BindView(R.id.lab_place_holder)
    RegularTextView lab_place_holder;

    @BindView(R.id.test_lab_title)
    LinearLayout test_lab_title;


    @BindView(R.id.chat_bot_bg)
    RelativeLayout chatBotBg;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.chatBot_rounded)
    View chatBot_rounded;

    @BindView(R.id.chat_bot_web_view)
    WebView chatBotWebView;

    boolean isChatBotVisible = false;

    private Boolean isLab = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
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

        labTestRecycler.setLayoutManager(new LinearLayoutManager(this));

        isLab = getIntent().getBooleanExtra(IS_LAB, false);

        if (isLab) {
            title_services.setText(getString(R.string.your_laboratory_reports));
            loadLabReports();
        } else {
            title_services.setText(getString(R.string.your_radiology_reports));
            loadRadReports();
        }
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


    @OnClick(R.id.toolbar_left_iv)
    void backPressed() {
        onBackPressed();
    }

    @Override
    protected int layout() {
        return R.layout.activity_laboratory_reports;
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
                ErrorMessage.getInstance().showSuccessGreen(LaboratoryReportsActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameAr() + ". "+getString(R.string.please_press_to_continue));
            }else{
                ErrorMessage.getInstance().showSuccessGreen(LaboratoryReportsActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameEn() + ". "+getString(R.string.please_press_to_continue));
            }
        } else {
            ErrorMessage.getInstance().showErrorYellow(LaboratoryReportsActivity.this, appointmentEvent.getErrorMSg());
        }
    }

    void loadLabReports() {

        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog();

            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);

            ReportsDTO reportsDTO = new ReportsDTO(patientId);

            RestClient.getInstance().GetAllLabReportByPatientId(reportsDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    LabReportMainModel labReportMainModel = (LabReportMainModel) result;

                    if (labReportMainModel != null) {

                        MobileOpResult mobileOpResult = labReportMainModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                ordersLab(labReportMainModel.getPatientReports());

                            } else {
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(LaboratoryReportsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(LaboratoryReportsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMesg);
                            }
                        } else {
                            ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMessage);
                        }
                    } else {
                        ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMessage);
                    }
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    void loadRadReports() {

        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog();

            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);

            ReportsDTO reportsDTO = new ReportsDTO(patientId);

            RestClient.getInstance().GetAllRadReportByPatientId(reportsDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    LabReportMainModel labReportMainModel = (LabReportMainModel) result;

                    if (labReportMainModel != null) {

                        MobileOpResult mobileOpResult = labReportMainModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                ordersLab(labReportMainModel.getPatientReports());

                            } else {
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(LaboratoryReportsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(LaboratoryReportsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMesg);
                            }
                        } else {
                            ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMessage);
                        }
                    } else {
                        ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMessage);
                    }
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    public void showPlaceholder(String msg) {
        lab_place_holder.setVisibility(View.VISIBLE);
        lab_place_holder.setText(msg);
    }

    public void hidePlaceholder() {
        lab_place_holder.setVisibility(View.GONE);
    }

    private void getSecurityChallenge(long id, boolean isLab, boolean isRad, String name, String reportUrl, LabReportsResponseModel labModel,
                                      boolean isShare) {

        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog();

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
            SecurityChallengeDTO securityChallengeDTO = new SecurityChallengeDTO(securityToken, patientId);

            RestClient.getInstance().getSecurityChallenge(securityChallengeDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {
                    hideWaitDialog();

                    SecurityChallengeModel securityChallengeModel = (SecurityChallengeModel) result;

                    if (securityChallengeModel != null) {

                        MobileOpResult mobileOpResult = securityChallengeModel.getMobileOpResult();

                        if (mobileOpResult != null) {
                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                String question = "";

                                if (TextUtil.getArabic(LaboratoryReportsActivity.this))
                                    question = securityChallengeModel.getQuestionAr();
                                else if (TextUtil.getEnglish(LaboratoryReportsActivity.this))
                                    question = securityChallengeModel.getQuestionEn();

                                String answer = securityChallengeModel.getAnswer();
                                showDialog(question, answer, 0, isLab, isRad, name, reportUrl, labModel, isShare);

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(LaboratoryReportsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(LaboratoryReportsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMesg);
                            }

                        } else {
                            ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMessage);
                        }


                    } else
                        ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMessage);

                }
            });

        } else
            ErrorMessage.getInstance().showWarning(this, "Internet is not available");
    }

    public void showDialog(String question, String answer, long id, boolean isLab, boolean isRad, String name,
                           String reportUrl, LabReportsResponseModel labModel, boolean isShare) {

        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.question_dialogue);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            LightEdittext input = (LightEdittext) dialog.findViewById(R.id.add_sugar);
            RegularTextView questionTxt = (RegularTextView) dialog.findViewById(R.id.question_txt);
            questionTxt.setText(question);

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

                        if (value.equalsIgnoreCase(answer)) {
                            patientUsedSecurityQuestion();
                            if(isShare){
                                dialog.dismiss();
                                showShareDialog(labModel);
                                return;
                            }

                            if (isRad)
                                showReportSelectionDialog(labModel);
                            else if (isLab)
                                labReport(labModel);


                        } else {
                            ErrorMessage.getInstance().showValidationMessage(LaboratoryReportsActivity.this, input, getString(R.string.sorry_not_match));
                        }

                    } else
                        ErrorMessage.getInstance().showValidationMessage(LaboratoryReportsActivity.this, input, "Please enter value ");

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

    void patientUsedSecurityQuestion() {
        String patientName = tinyDB.getString(Const.PATIENT_NAME);
        long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
        String patientKey = patientId + "_key";
        tinyDB.putString(patientKey, String.valueOf(patientId));
    }

    void ordersLab(ArrayList<LabReportsResponseModel> OrdersLab) {

        if (!ValidationHelper.isNullOrEmpty(OrdersLab)) {

            test_lab_title.setVisibility(View.VISIBLE);
            //hide placeholder
            hidePlaceholder();
            LabReportAdapter confirmationAdapter = new LabReportAdapter(this, OrdersLab);
            labTestRecycler.setAdapter(confirmationAdapter);
            confirmationAdapter.setOnCardClickListner(this);

        } else {

            test_lab_title.setVisibility(View.INVISIBLE);
            if (isLab) {
                showPlaceholder(getString(R.string.you_have_not_any_lab_test));
            } else {
                showPlaceholder(getString(R.string.you_have_not_any_radiology_test));
            }

            //show empty placeholder
        }
    }

    @Override
    public void OnCardClicked(LabReportsResponseModel model, int pos, boolean isShare) {

        String patientName = tinyDB.getString(Const.PATIENT_NAME);
        long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
        String patientKey = patientId + "_key";

        String existPatient = tinyDB.getString(patientKey);
        if (ValidationHelper.isNullOrEmpty(existPatient)) {

            if (isLab) {
                getSecurityChallenge(0, true, false, model.getName(), model.getReportUrl(), model, false);
            } else {
                getSecurityChallenge(0, false, true, model.getName(), model.getReportUrl(), model, isShare);
            }

        } else {
            if(isShare){
                showShareDialog(model);
                return;
            }
            if (isLab) {
                // labReport(0, model.getName(), model.getReportUrl());
                labReport(model);
            } else {
                showReportSelectionDialog(model);
            }
        }
    }

    private void showShareDialog(LabReportsResponseModel model) {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.share_report_dialog);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            LightEdittext input = (LightEdittext) dialog.findViewById(R.id.add_sugar);

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
                        dialog.dismiss();
                        shareRadReportApi(model, value);
                    } else {
                        ErrorMessage.getInstance().showValidationMessage(LaboratoryReportsActivity.this, input, getString(R.string.please_enter_email));
                    }
                }
            });

            dialog.show();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void shareRadReportApi(LabReportsResponseModel model, String email) {
        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog();

            String oregId = tinyDB.getString(Const.OREGID_KEY);
            String securityCode = tinyDB.getString(Const.TOKEN_KEY);

            ReportSharingDTO reportSharingDTO = new ReportSharingDTO(securityCode, email, oregId, model.getOrderNumber());

            RestClient.getInstance().sendAgfaRadiologyImageUrlEmail(reportSharingDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    SendRadiologyResponseModel sendRadiologyResponseModel = (SendRadiologyResponseModel) result;
                    if (sendRadiologyResponseModel != null) {

                        MobileOpResult mobileOpResult = sendRadiologyResponseModel.getMobileOpResult();
                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                showSuccessDialog();

                            } else {
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(LaboratoryReportsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(LaboratoryReportsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMesg);
                            }
                        } else {
                            ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMessage);
                        }
                    }
                    else {
                        ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMessage);
                    }
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    IOSDialog iosDialog3 = null;
    private void showSuccessDialog() {

         iosDialog3 =  TextUtil.dialgoueReportSuccessBooking(LaboratoryReportsActivity.this, getString(R.string.report_success_msg), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iosDialog3.dismiss();
            }
        });

         iosDialog3.show();
    }

    private void showReportSelectionDialog(LabReportsResponseModel model) {

        Dialog dialog = new Dialog(this, R.style.ios_dialog_style);
        dialog.setContentView(R.layout.dialog_report_selection_layout);
        dialog.setCancelable(true);
        LightButton viewReportBtn = dialog.findViewById(R.id.view_report_btn);
        LightButton viewImageBtn = dialog.findViewById(R.id.view_image_btn);
        LightButton doNotRemindBtn = dialog.findViewById(R.id.do_not_remind_btn);
//        // if button is clicked, close the custom dialog

        viewReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getRadReportUrl(model.getOrderNumber());
            }
        });

        viewImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getRadReportImageUrl(model.getOrderNumber());
            }
        });

        doNotRemindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getRadReportImageUrl("order3209885");
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    //LabReportsResponseModel
    void getRadReportImageUrl(String radModel) {

        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        // String companyID = radModel.getCompany();
        AfgaReportDTO translateLabDTO = new AfgaReportDTO(securityToken, radModel, "");

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            RestClient.getInstance().GetAgfaRadiologyImageUrl(translateLabDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    TranslateLabModel translateLabModel = (TranslateLabModel) result;

                    if (translateLabModel != null) {

                        MobileOpResult mobileOpResult = translateLabModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                String url = translateLabModel.getResultVal();
                             /*   Intent intent = new Intent(LaboratoryReportsActivity.this, BrowserActivityPayment.class);
                                intent.putExtra("calendar", true);
                                intent.putExtra("url", url);
                                intent.putExtra(Const.TERMS_CONDITIONS_KEY, "");
                                startActivity(intent);*/
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("url", url);
                                clipboard.setPrimaryClip(clip);

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                browserIntent.setPackage("com.android.chrome");
                                browserIntent.putExtra("com.android.chrome.EXTRA_OPEN_NEW_INCOGNITO_TAB", true);
                                startActivity(browserIntent);

                              /*  String doc = "http://docs.google.com/gview?embedded=true&url=" + url;
                                String date = LocaleDateHelper.convertDateStringFormat(radModel.getOrderDate(), "yyyy-MM-dd'T'hh:mm:ss", "dd_MM_yyyy");
                                Intent intent = new Intent(LaboratoryReportsActivity.this, PdfViewBrowser.class);
                                intent.putExtra("url", url);
                                intent.putExtra("date_report", date);
                                intent.putExtra("name", radModel.getName());
                                startActivity(intent);*/

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(LaboratoryReportsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(LaboratoryReportsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMessage);


                    } else
                        ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMessage);
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }


    void getRadReportUrl(String radModel) {

        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        //    String companyID = radModel.getCompany();
        AfgaReportDTO translateLabDTO = new AfgaReportDTO(securityToken, radModel, "");

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            RestClient.getInstance().GetAgfaRadiologyReportUrl(translateLabDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    TranslateLabModel translateLabModel = (TranslateLabModel) result;

                    if (translateLabModel != null) {

                        MobileOpResult mobileOpResult = translateLabModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                String url = translateLabModel.getResultVal();
                             /*   Intent intent = new Intent(LaboratoryReportsActivity.this, BrowserActivityPayment.class);
                                intent.putExtra("calendar", true);
                                intent.putExtra("url", url);
                                intent.putExtra(Const.TERMS_CONDITIONS_KEY, "");
                                startActivity(intent);*/
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("url", url);
                                clipboard.setPrimaryClip(clip);

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                browserIntent.setPackage("com.android.chrome");
                                browserIntent.putExtra("com.android.chrome.EXTRA_OPEN_NEW_INCOGNITO_TAB", true);
                                startActivity(browserIntent);
                                
                              /*  String doc = "http://docs.google.com/gview?embedded=true&url=" + url;
                                String date = LocaleDateHelper.convertDateStringFormat(radModel.getOrderDate(), "yyyy-MM-dd'T'hh:mm:ss", "dd_MM_yyyy");
                                Intent intent = new Intent(LaboratoryReportsActivity.this, PdfViewBrowser.class);
                                intent.putExtra("url", url);
                                intent.putExtra("date_report", date);
                                intent.putExtra("name", radModel.getName());
                                startActivity(intent);*/

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(LaboratoryReportsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(LaboratoryReportsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMessage);


                    } else
                        ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMessage);
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    void labReport(LabReportsResponseModel labModel) {
        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        String companyID = labModel.getCompany();
        TranslateLabDTO translateLabDTO = new TranslateLabDTO(securityToken, labModel.getOrderId(), companyID);

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();
            RestClient.getInstance().translateLab(translateLabDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    TranslateLabModel translateLabModel = (TranslateLabModel) result;

                    if (translateLabModel != null) {

                        MobileOpResult mobileOpResult = translateLabModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                String url = translateLabModel.getResultVal();

                                String doc = "http://docs.google.com/viewer?embedded=true&url=" + url;
                                String date = LocaleDateHelper.convertDateStringFormat(labModel.getOrderDate(), "yyyy-MM-dd'T'hh:mm:ss", "dd_MM_yyyy");
                                Intent intent = new Intent(LaboratoryReportsActivity.this, PdfViewBrowser.class);
                                intent.putExtra("url", doc);
                                intent.putExtra("date_report", date);
                                intent.putExtra("name", labModel.getName());
                                startActivity(intent);

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(LaboratoryReportsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(LaboratoryReportsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMessage);


                    } else
                        ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMessage);


                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    private void labReport(int i, String name, String reportUrl) {
        LabReportUrlDTO labReportUrlDTO = new LabReportUrlDTO(reportUrl);

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            RestClient.getInstance().getLabRadReportURL(labReportUrlDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    LabReportUrlResponseModel labReportUrlResponseModel = (LabReportUrlResponseModel) result;

                    if (labReportUrlResponseModel != null) {

                        MobileOpResult mobileOpResult = labReportUrlResponseModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                String url = labReportUrlResponseModel.getReportUrl();

                                String doc = "http://docs.google.com/viewer?embedded=true&url=" + url;
                                //String date = LocaleDateHelper.convertDateStringFormat(mo.getOrderDate(), "yyyy-MM-dd'T'hh:mm:ss", "dd-MM-yyyy");
                                Intent intent = new Intent(LaboratoryReportsActivity.this, PdfViewBrowser.class);
                                intent.putExtra("url", doc);
                                intent.putExtra("name", name);
                                startActivity(intent);

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(LaboratoryReportsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(LaboratoryReportsActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMessage);


                    } else
                        ErrorMessage.getInstance().showError(LaboratoryReportsActivity.this, errorMessage);
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }
}