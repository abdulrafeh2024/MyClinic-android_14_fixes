package com.telemedicine.myclinic.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.centerOfExcellency.CoEDMPLabDTO;
import com.telemedicine.myclinic.models.centerOfExcellency.CoEDMPLabModel;
import com.telemedicine.myclinic.models.centerOfExcellency.CoEDMPSugarTestInsertDTO;
import com.telemedicine.myclinic.models.centerOfExcellency.CoEDMPSugarTestModel;
import com.telemedicine.myclinic.models.centerOfExcellency.LabResultsList;
import com.telemedicine.myclinic.models.centerOfExcellency.SugarTests;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.AppointmentEvent;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.LocaleUtils;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightEdittext;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.webservices.RestClient;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnClick;

public class CenterOfExcellencyActivity extends BaseActivity {

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.weightVal)
    BoldTextView weightVal;

    @BindView(R.id.bmiVal)
    BoldTextView bmiVal;

    @BindView(R.id.progress_txt)
    RegularTextView progress_txt;

    @BindView(R.id.fighter_title)
    LightTextView fighter_title;

    @BindView(R.id.stars)
    ImageView stars;

    @BindView(R.id.img)
    ImageView img;

    @BindView(R.id.empty_star)
    ImageView empty_star;

    TinyDB tinyDB = null;

    @BindView(R.id.bs_value)
    RegularTextView bs_value;

    @BindView(R.id.add_sugar)
    LightButton add_sugar;

    @BindView(R.id.sugar_webview)
    WebView sugarWebView;

    @BindView(R.id.hc1_webview)
    WebView hc1WebView;

    @BindView(R.id.today)
    RegularTextView today;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    String hc1Html = "", sugarHtml = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextUtil.translation(this);

        progressBar.setScaleY(7f);
        init();
        transformViews();
    }

    private void init() {

        tinyDB = new TinyDB(this);

        SugarTests sugarTests = new SugarTests();
        sugarTests.setCreateStamp(getString(R.string.no_date_added));
        sugarTests.setResult(0);
        this.sugarTests.add(sugarTests);

        LabResultsList labResultsList = new LabResultsList();
        labResultsList.setReviewDate(getString(R.string.no_date_added));
        labResultsList.setResult(0);

        ArrayList<LabResultsList> labResultsLists = new ArrayList<>();

        labResultsLists.add(labResultsList);

        loadHC1Chart(labResultsLists);

        try {
            loadSugarChart(this.sugarTests);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sugarWebView.getSettings();
        sugarWebView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        hc1WebView.getSettings();
        hc1WebView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        callExcellency();

    }

    @Override
    protected int layout() {
        return R.layout.activity_center_of_excellency;
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
                ErrorMessage.getInstance().showSuccessGreen(CenterOfExcellencyActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameAr() + ". "+getString(R.string.please_press_to_continue));
            }else{
                ErrorMessage.getInstance().showSuccessGreen(CenterOfExcellencyActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameEn() + ". "+getString(R.string.please_press_to_continue));
            }
        } else {
            ErrorMessage.getInstance().showErrorYellow(CenterOfExcellencyActivity.this, appointmentEvent.getErrorMSg());
        }
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    void callExcellency() {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            String secuirityToken = tinyDB.getString(Const.TOKEN_KEY);
            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
            CoEDMPLabDTO coEDMPLabDTO = new CoEDMPLabDTO(secuirityToken, String.valueOf(patientId));

            RestClient.getInstance().coEDMPLab(coEDMPLabDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {


                    CoEDMPLabModel coEDMPLabModel = (CoEDMPLabModel) result;

                    if (coEDMPLabModel != null) {

                        MobileOpResult mobileOpResult = coEDMPLabModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                progress_txt.setVisibility(View.VISIBLE);

                                int weight = coEDMPLabModel.getWeight();

                                int bmi = coEDMPLabModel.getBMI();

                                double progressVal = coEDMPLabModel.getPercentageProgress();

                                excellency(progressVal);

                                weightVal.setText("" + weight);

                                bmiVal.setText("" + bmi);

                                progressBar.setProgress((int) progressVal);
                                if (progressVal == 0.0) {
                                    progress_txt.setText((int) progressVal + "% " + getString(R.string.goal_achieved));
                                } else
                                    progress_txt.setText(progressVal + "% " + getString(R.string.goal_achieved));

                                RestClient.getInstance().coEDMPSugarTest(coEDMPLabDTO, new OnResultListener() {
                                    @Override
                                    public void onResult(Object result, boolean status, String errorMessage) {

                                        hideWaitDialog();

                                        CoEDMPSugarTestModel coEDMPSugarTestModel = (CoEDMPSugarTestModel) result;

                                        if (coEDMPSugarTestModel != null) {

                                            MobileOpResult mobileOpResult = coEDMPLabModel.getMobileOpResult();

                                            if (mobileOpResult != null) {

                                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                                    int sugarTestIndicator = coEDMPSugarTestModel.getSugarTestIndicator();
                                                    sugarIndicator(sugarTestIndicator);

                                                    if (!ValidationHelper.isNullOrEmpty(coEDMPSugarTestModel.getSugarTests())) {
                                                        try {
                                                            loadSugarChart(coEDMPSugarTestModel.getSugarTests());
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                } else {

                                                    String errorMesg = "";

                                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                                        if (TextUtil.getEnglish(CenterOfExcellencyActivity.this))
                                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                                        else if (TextUtil.getArabic(CenterOfExcellencyActivity.this))
                                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                                    }

                                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                                    }

                                                    ErrorMessage.getInstance().showError(CenterOfExcellencyActivity.this, errorMesg);
                                                }
                                            } else {
                                                ErrorMessage.getInstance().showError(CenterOfExcellencyActivity.this, errorMessage);
                                            }
                                        } else {
                                            ErrorMessage.getInstance().showError(CenterOfExcellencyActivity.this, errorMessage);
                                        }
                                    }
                                });


                            } else {
                                hideWaitDialog();
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(CenterOfExcellencyActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(CenterOfExcellencyActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(CenterOfExcellencyActivity.this, errorMesg);

                            }

                        } else {
                            hideWaitDialog();
                            ErrorMessage.getInstance().showError(CenterOfExcellencyActivity.this, errorMessage);
                        }

                    } else {
                        hideWaitDialog();
                        ErrorMessage.getInstance().showError(CenterOfExcellencyActivity.this, errorMessage);
                    }
                }
            });


        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }

    }

    void sugarIndicator(int sugarTestIndicator) {

        if (sugarTestIndicator == 0) {

            bs_value.setText(getString(R.string.your_bs_is_perfect));
           // bs_value.setBackgroundColor(getResources().getColor(R.color.vsee_kit_green));
            bs_value.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        } else if (sugarTestIndicator == 1) {

            bs_value.setText(R.string.custion_bs_low);
            bs_value.setBackgroundColor(getResources().getColor(R.color.colorred));

        } else if (sugarTestIndicator == 2) {
            bs_value.setBackgroundColor(getResources().getColor(R.color.colorred));
            bs_value.setText(R.string.bs_high);
        }
    }

    void excellency(double progress) {

        if (progress >= 33 && progress <= 66) {
            empty_star.setVisibility(View.GONE);
            fighter_title.setText(R.string.fighter);
            stars.setImageDrawable(getResources().getDrawable(R.drawable.single_star));
            img.setImageDrawable(getResources().getDrawable(R.drawable.weightlifter));
        } else if (progress >= 66 && progress <= 99) {
            empty_star.setVisibility(View.GONE);
            // two star
            stars.setImageDrawable(getResources().getDrawable(R.drawable.double_stars));
            img.setImageDrawable(getResources().getDrawable(R.drawable.ninja));
            fighter_title.setText(R.string.warrior);

        } else if (progress > 99) {
            empty_star.setVisibility(View.GONE);
            // three star
            stars.setImageDrawable(getResources().getDrawable(R.drawable.three_stars));
            img.setImageDrawable(getResources().getDrawable(R.drawable.champion));
            fighter_title.setText(R.string.champion);
        }

    }

    @OnClick(R.id.add_sugar)
    void addSugar() {

        showDialog();

 /*       AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(getString(R.string.my_clininc));
        alert.setMessage(" ");
        alert.setCancelable(false);

        final EditText input = new EditText(this);
        alert.setView(input);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint(R.string.add_sugar);

        input.setPadding(30, 0, 0, 30);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        alert.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String value = input.getText().toString();

                if (!ValidationHelper.isNullOrEmpty(value)) {

                    if (input.getText().length() > 1) {
                        int num = Integer.parseInt(input.getText().toString());
                        if (num >= 20 && num <= 2600) {
                            //save the number
                            num1 = num;

                            if (ConnectionUtil.isInetAvailable(CenterOfExcellencyActivity.this)) {
                                if (!ValidationHelper.isNullOrEmpty(value))
                                    addSugar(Integer.valueOf(value));
                            } else
                                ErrorMessage.getInstance().showWarning(CenterOfExcellencyActivity.this, "internet is not available");

                        } else {
                            Toast.makeText(CenterOfExcellencyActivity.this, R.string.please_enter_20_2600, Toast.LENGTH_LONG).show();
                            input.setText("");
                            num1 = -1;
                        }
                    }

                } else
                    ErrorMessage.getInstance().showValidationMessage(CenterOfExcellencyActivity.this, input, "Required *");
            }
        });

        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();*/
    }

    void addSugar(int sugar) {
        showWaitDialog();
        String secuirityToken = tinyDB.getString(Const.TOKEN_KEY);
        long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
        CoEDMPSugarTestInsertDTO coEDMPLabDTO = new CoEDMPSugarTestInsertDTO(secuirityToken, String.valueOf(patientId), sugar);


        RestClient.getInstance().coEDMPSugarTestInsert(coEDMPLabDTO, new OnResultListener() {
            @Override
            public void onResult(Object result, boolean status, String errorMessage) {

                hideWaitDialog();

                MobileOpResult mobileOpResult = (MobileOpResult) result;

                if (mobileOpResult != null) {

                    if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                        SugarTests sugarTestsOb = new SugarTests();
                        sugarTestsOb.setResult(sugar);
                        sugarTestsOb.setCreateStamp(date());

                        sugarTests.add(sugarTestsOb);

                        try {
                            loadSugarChart(sugarTests);

                            callExcellency();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        // add in the chart
                    } else {
                        String errorMesg = "";

                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                            if (TextUtil.getEnglish(CenterOfExcellencyActivity.this))
                                errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                            else if (TextUtil.getArabic(CenterOfExcellencyActivity.this))
                                errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";

                        }

                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                            errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                        }

                        ErrorMessage.getInstance().showError(CenterOfExcellencyActivity.this, errorMesg);
                    }
                } else {
                    ErrorMessage.getInstance().showError(CenterOfExcellencyActivity.this, errorMessage);
                }

            }
        });
    }

    ArrayList<SugarTests> sugarTests = new ArrayList<>();

    void loadSugarChart(ArrayList<SugarTests> sugarTests) throws ParseException {

        String array = "";

        this.sugarTests = sugarTests;

        if (!ValidationHelper.isNullOrEmpty(sugarTests)) {

            if (sugarTests.get(0).getCreateStamp().equalsIgnoreCase(getString(R.string.no_date_added))) {
                array = "['" + sugarTests.get(0).getCreateStamp() + "', " + sugarTests.get(0).getResult() + "]," + array;
                this.sugarTests.clear();
            } else {
                for (SugarTests sugarTests1 : sugarTests) {

                    String apptDate = sugarTests1.getCreateStamp();
                    int result = sugarTests1.getResult();
                    if (!ValidationHelper.isNullOrEmpty(apptDate)) {

                        apptDate = LocaleDateHelper.convertDateStringFormatWithZone(apptDate, "yyyy-MM-dd'T'HH:mm:ss", "dd/MM/yy hh:mm a");

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");

                        Date date = simpleDateFormat.parse(apptDate);

                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
                        String today = df.format(c);
                        String DateStr = df.format(date);

                        if (today.equalsIgnoreCase(DateStr)) {
                            this.today.setVisibility(View.VISIBLE);
                            String onlyTime = LocaleDateHelper.convertDateStringFormatWithZone(sugarTests1.getCreateStamp(), "yyyy-MM-dd'T'HH:mm:ss", "hh:mm:ss aa");
                            this.today.setText(getString(R.string.your_hbs_today) + sugarTests1.getResult());
                            apptDate = onlyTime;
                        } else {
                            this.today.setVisibility(View.GONE);
                        }

                        array = "['" + apptDate + "', " + result + "]," + array;

                    }

                }
            }
        }

        // remove last literal
        array.replaceAll(",\"", "\"");

        sugarWebView.getSettings().setJavaScriptEnabled(true);

        String chartHtml = html(array, "BS");

        sugarHtml = chartHtml;
        sugarWebView.loadDataWithBaseURL("file:///android_asset/", chartHtml,
                "text/html", "utf-8", null);

        sugarWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                // Do something
                Toast.makeText(CenterOfExcellencyActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void loadHC1Chart(ArrayList<LabResultsList> labResult) {

        String array = "";

        if (!ValidationHelper.isNullOrEmpty(labResult)) {

            if (labResult.get(0).getReviewDate().equalsIgnoreCase(getString(R.string.no_date_added))) {
                array = "['" + labResult.get(0).getReviewDate() + "', " + labResult.get(0).getResult() + "]," + array;

            } else {
                for (LabResultsList resultsList : labResult) {

                    String reviewDate = resultsList.getReviewDate();
                    double result = resultsList.getResult();
                    if (!ValidationHelper.isNullOrEmpty(reviewDate))
                        reviewDate = LocaleDateHelper.convertDateStringFormatWithZone(reviewDate, "yyyy-MM-dd'T'HH:mm:ss", "dd/MM/yy hh:mm a");
                    array = "['" + reviewDate + "', " + result + "]," + array;
                }
            }
        }

        // remove last literal
        array.replaceAll(",\"", "\"");

        hc1WebView.getSettings().setJavaScriptEnabled(true);

        String chartHtml = html(array, getString(R.string.ha1c));

        hc1Html = chartHtml;

        hc1WebView.loadDataWithBaseURL("file:///android_asset/", chartHtml,
                "text/html", "utf-8", null);

        hc1WebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                // Do something
                Toast.makeText(CenterOfExcellencyActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    int num1;

    String html(String array, String title) {

        String name = "ar";
        if (TextUtil.getArabic(this))
            name = "ar";

        String html = "<html>" +
                "<head>" +
                "    <script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>" +
                "    <script type=\"text/javascript\">" +
                "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"], 'language': '" + name + "'});" +
                "      google.setOnLoadCallback(drawChart);" +
                "      function drawChart() {" +
                "        var data = google.visualization.arrayToDataTable([" +
                "          ['Items', '" + title + "']," + array +
                "        ]);" +
                "" +
                "        var options = {" +
                "        title: '" + "" + "  " + "" + " ',colors: ['#FFF'], hAxis: { textStyle:{color: '#FFF' , fontSize: 7} ," +
                "        direction:-1," +
                "        slantedText:true," +
                "        slantedTextAngle:90" +
                "    },vAxis: { baselineColor: '#fff',gridlineColor: '#fff' ,textStyle:{color: '#FFF' , fontSize: 8}  },legend: 'none',colors:['white'],backgroundColor: '#003868' " +
                "        };" +
                "" +
                "        var chart = new google.visualization.LineChart(document.getElementById('chart_div'));" +
                "        chart.draw(data, options);" +
                "      }" +
                "" +
                "    </script>" +
                "</head>" +
                "<body>" +
                "<div id=\"chart_div\" style=\"width: 100%; height: 100%;\"></div>" +
                "<p>\n\n</p>" +
                "</body>" +
                "</html>";

        return html;
    }


    String date() {

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("GMT+03"));
        String formattedDate = df.format(c);

        return formattedDate;
    }

    @OnClick(R.id.sug_full)
    void sugar() {
        Intent intent = new Intent(this, FullScreenReport.class);
        intent.putExtra("url", sugarHtml);
        startActivity(intent);

    }

    @OnClick(R.id.hc1_full)
    void hc1() {
        Intent intent = new Intent(this, FullScreenReport.class);
        intent.putExtra("url", hc1Html);
        startActivity(intent);

    }

    public void showDialog() {
        try {
            final Dialog dialog = new Dialog(this, R.style.NewDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.sugar_dialogue);
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

                        if (input.getText().length() > 1) {
                            int num = Integer.parseInt(input.getText().toString());
                            if (num >= 20 && num <= 2600) {
                                //save the number
                                num1 = num;

                                if (ConnectionUtil.isInetAvailable(CenterOfExcellencyActivity.this)) {
                                    if (!ValidationHelper.isNullOrEmpty(value)) {
                                        dialog.dismiss();
                                        addSugar(Integer.valueOf(value));
                                    }

                                } else
                                    ErrorMessage.getInstance().showWarning(CenterOfExcellencyActivity.this, getString(R.string.internet_connection));

                            } else {
                                ErrorMessage.getInstance().showValidationMessage(CenterOfExcellencyActivity.this, input, getResources().getString(R.string.please_enter_20_2600));
                                input.setText("");
                                num1 = -1;
                            }
                        } else
                            ErrorMessage.getInstance().showValidationMessage(CenterOfExcellencyActivity.this, input, getResources().getString(R.string.please_enter_20_2600));

                    } else
                        ErrorMessage.getInstance().showValidationMessage(CenterOfExcellencyActivity.this, input, getResources().getString(R.string.please_enter_20_2600));
                }
            });

            dialog.show();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    void transformViews() {
        if (TextUtil.getArabic(this))
            toolbar_left_iv.setRotation(180);

    }
}
