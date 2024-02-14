package com.telemedicine.myclinic.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.bumptech.glide.Glide;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.HomeModel;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.MonthViewDTO;
import com.telemedicine.myclinic.models.earliestslots.EarliestSlotsMiniModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.MessageBox;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.webservices.RestClient;
import com.viewpagerindicator.CirclePageIndicator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.telemedicine.myclinic.util.Const.DATE;
import static com.telemedicine.myclinic.util.Const.DOCTOR_ID;
import static com.telemedicine.myclinic.util.Const.DOCTOR_IMAGE_URL;
import static com.telemedicine.myclinic.util.Const.DOCTOR_NAME;
import static com.telemedicine.myclinic.util.Const.DOCTOR_SPECIALITY;
import static com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY;
import static com.telemedicine.myclinic.util.Const.LOGGED_IN_MODE;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_ID;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_NAME_AR;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_NAME_EN;

public class MonthViewActivity extends Activity {

   /* @BindView(R.id.close)
    AppCompatTextView close;*/

    CircleImageView doctorImageView;
    BoldTextView doctorNameTv, DateTv;
    LightTextView locationTv;
    LinearLayoutCompat location_layout;

    //@BindView(R.id.calendarView)
    CalendarView calendarView;

    TinyDB tinyDB = null;

    String doctorName = "";
    String doctorDate = "";
    String doctorTime = "";
    String doctorImage = "";
    String company = "";
    long doctorId = 0;
    String startDate = "";
    String specialityId = "";
    boolean isTelemedicine = false;
    String specialityNameEn = "";
    String getSpecialityNameAR = "";

    boolean isLoggedInMode = false;
    String weekDay = "";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextUtil.translation(this);

        super.onCreate(savedInstanceState);
        changeLanguage();
        setContentView(R.layout.activity_month_view);
        calendarView = findViewById(R.id.calendarView);
        doctorImageView = findViewById(R.id.doctor_image);
        doctorNameTv = findViewById(R.id.doctorName);
        locationTv = findViewById(R.id.location);
        DateTv = findViewById(R.id.DateTv);
        progressBar = findViewById(R.id.progress);
        location_layout = findViewById(R.id.location_layout);
        Intent intent = getIntent();
        tinyDB = new TinyDB(this);
        initMonthView();
        if (intent != null) {
            doctorId = intent.getLongExtra("DoctorId", 0);
            company = intent.getStringExtra("Company");
            startDate = intent.getStringExtra("startDate");
            doctorName = intent.getStringExtra("DoctorName");
            doctorDate = intent.getStringExtra("Date");
            isLoggedInMode = intent.getBooleanExtra(LOGGED_IN_MODE, false);
            specialityNameEn = intent.getStringExtra(SPECIALITY_NAME_EN);
            getSpecialityNameAR = intent.getStringExtra(SPECIALITY_NAME_AR);
            isTelemedicine = intent.getBooleanExtra(Const.ISTELEMEDICINE_KEY, false);
            doctorTime = intent.getStringExtra("Time");
            doctorImage = intent.getStringExtra("image");
            weekDay = intent.getStringExtra("week_Day");
            specialityId = intent.getStringExtra("specialityId");
            getMonthView(doctorId, company, startDate);
        }

        populateViews();
        // initMonthView();
    }


    private void changeLanguage() {
        if (TextUtil.getArabic(this)) {
            Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            Configuration configuration = getBaseContext().getResources().getConfiguration();
            configuration.setLocale(locale);
            createConfigurationContext(configuration);
        }
    }

    private void populateViews() {
        Glide.with(this).load(doctorImage).placeholder(R.drawable.doctr).into((ImageView) doctorImageView);
        doctorNameTv.setText(doctorName);

        if (company.toLowerCase().equals("nc01")) {
            locationTv.setText(getString(R.string.prince_sultan));
        } else if (company.toLowerCase().equals("safa")) {
            locationTv.setText(getString(R.string.safa));
        }else if(company.toLowerCase().equals("prc")){
            locationTv.setText(getString(R.string.prc));
        }

        if (isTelemedicine) {
            location_layout.setVisibility(View.GONE);
        } else {
            location_layout.setVisibility(View.VISIBLE);
        }

        if(!doctorDate.isEmpty()){
            String myFormat = "dd-MM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
            Date date1 = LocaleDateHelper.getDateFormat(doctorDate, "yyyy-MM-dd'T'hh:mm:ss");
            // DateTv.setText(sdf.format(date1.getTime()) + ", " + doctorTime);
            DateTv.setText(weekDay + "   " + sdf.format(date1.getTime()));
        }

    }

    private void initMonthView() {
        //  calendarView.setHeaderVisibility(View.GONE);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                if (highlightedDaysList.contains(eventDay.getCalendar())) {

                    String myFormat = "yyyy-MM-dd'T'hh:mm:ss"; //In which you need put here
                    SimpleDateFormat sdf;



                    if (TextUtil.getArabic(MonthViewActivity.this)) {
                        sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                    } else {
                        sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                    }
                    weekDay = getWeekDay(sdf.format(eventDay.getCalendar().getTime()));
                    Intent intent = new Intent(MonthViewActivity.this, TimeSlotActivity.class);
                    intent.putExtra("DoctorId", doctorId);
                    intent.putExtra("Company", company);
                    intent.putExtra("startDate", startDate);
                    intent.putExtra("week_Day", weekDay);
                    intent.putExtra(LOGGED_IN_MODE, isLoggedInMode);
                    intent.putExtra(SPECIALITY_NAME_EN, specialityNameEn);
                    intent.putExtra(SPECIALITY_NAME_AR, getSpecialityNameAR);
                    intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicine);
                    intent.putExtra("DoctorName", doctorName);
                    intent.putExtra("Date", sdf.format(eventDay.getCalendar().getTime()));
                    intent.putExtra("Time", doctorTime);
                    intent.putExtra("image", doctorImage);
                    intent.putExtra("specialityId", specialityId);
                    startActivityForResult(intent, 111);
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                }
            }
        });

        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                calendarView.getCurrentPageDate();
            }
        });

        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                calendarView.getCurrentPageDate();

                if (!maxDate.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                    try {
                        Date dt = sdf.parse(maxDate);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dt);
                        long maximumDate = cal.getTimeInMillis();

                        calendarView.getCurrentPageDate();

                        if(calendarView.getCurrentPageDate().getTimeInMillis() > maximumDate){
                            showNoSlotsDialog(getString(R.string.no_schedule_for_next_month));
                            //calendarView.showCurrentMonthPage();
                        }else{
                            calendarView.getCurrentPageDate();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    showNoSlotsDialog(getString(R.string.no_schedule_for_next_month));
                    //calendarView.showCurrentMonthPage();
                }
            }
        });
    }

    private String getWeekDay(String date) {
        String calenderWeekDay = LocaleDateHelper.getDayOfWeek(date, "yyyy-MM-dd'T'HH:mm:ss");
        if (TextUtil.getArabic(this)) {
            if (calenderWeekDay.equalsIgnoreCase("Sunday")) {
                calenderWeekDay = "الأحد";
            } else if (calenderWeekDay.equalsIgnoreCase("Monday")) {
                calenderWeekDay = "الاثنين";
            } else if (calenderWeekDay.equalsIgnoreCase("Tuesday")) {
                calenderWeekDay = "يوم الثلاثاء";
            } else if (calenderWeekDay.equalsIgnoreCase("Wednesday")) {
                calenderWeekDay = "الأربعاء";
            } else if (calenderWeekDay.equalsIgnoreCase("Thursday")) {
                calenderWeekDay = "يوم الخميس";
            } else if (calenderWeekDay.equalsIgnoreCase("Friday")) {
                calenderWeekDay = "جمعة";
            } else if (calenderWeekDay.equalsIgnoreCase("Saturday")) {
                calenderWeekDay = "السبت";
            }
        }
        return  calenderWeekDay;
    }

    IOSDialog iosDialog2 = null;
    private void showNoSlotsDialog(String msg) {

         iosDialog2 = TextUtil.dialgoue(MonthViewActivity.this, msg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iosDialog2.dismiss();

            }
        });
    }


    ArrayList<Calendar> highlightedDaysList = new ArrayList<Calendar>();

    private List<Calendar> getHighlightedDays(ArrayList<Object> dateList) {

        for (Object object : dateList) {
            highlightedDaysList.add(LocaleDateHelper.getCalDate(object.toString(),
                    "yyyy-MM-dd'T'hh:mm:ss"));
        }

        return highlightedDaysList;
    }

    ArrayList<Calendar> disableDaysList = new ArrayList<Calendar>();
/*
    private List<Calendar> getDisableDates(ArrayList<Object> dateList){

        for (Object object : dateList) {
            LocaleDateHelper.getCalDate(object.toString(),
                    "yyyy-MM-dd'T'hh:mm:ss").getTime();
        }

        return calList;
    }
*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    public void Hide(View view) {
        onBackPressed();
    }

  /*  public void onCloseClick(View view) {
        onBackPressed();
        Intent intent = new Intent(this, TimeSlotActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }*/

    String maxDate = "";

    private void getMonthView(long doctorId, String company, String startDate) {

        String formattedStrDate;
        if (TextUtil.getArabic(MonthViewActivity.this)) {
            formattedStrDate = LocaleDateHelper.convertArabicToEnglish(startDate, "yyyy-MM-dd");
        } else {
            formattedStrDate = startDate;
        }
        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);

            MonthViewDTO getRefSpecialtiesDTO = new MonthViewDTO(securityToken, doctorId, isTelemedicine,
                    formattedStrDate, company);

            RestClient.getInstance().getMonthView(getRefSpecialtiesDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    HomeModel homeModel = (HomeModel) result;

                    if (homeModel != null) {

                        MobileOpResult mobileOpResult = homeModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                // Toast.makeText(MonthViewActivity.this, homeModel.toString(), Toast.LENGTH_LONG).show();
                                if (homeModel.getAvailableDates() != null) {

                                    calendarView.setHighlightedDays(getHighlightedDays(homeModel.getAvailableDates()));
                                    // calendarView.setDisabledDays(getHighlightedDays(homeModel.getAvailableDates()));
                                    try {
                                        calendarView.setDate(LocaleDateHelper.getCalDate(homeModel.getAvailableDates().get(0).toString(),
                                                "yyyy-MM-dd'T'hh:mm:ss"));
                                    } catch (OutOfDateRangeException e) {
                                        e.printStackTrace();
                                    }
                                    calendarView.setMinimumDate(LocaleDateHelper.getCalDate(homeModel.getAvailableDates().get(0).toString(),
                                            "yyyy-MM-dd'T'hh:mm:ss"));
                                   /*calendarView.setMaximumDate(LocaleDateHelper.getCalDate(homeModel.getAvailableDates().get(homeModel.getAvailableDates().size()-1).toString(),
                                           "yyyy-MM-dd'T'hh:mm:ss"));*/

                                    maxDate = homeModel.getAvailableDates().get(homeModel.getAvailableDates().size() - 1).toString();


                                }else{
                                    showNoSlotsDialog(getString(R.string.no_schedule_for_this_month));
                                    //calendarView.showCurrentMonthPage();
                                }

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                    if (TextUtil.getEnglish(MonthViewActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    } else if (TextUtil.getArabic(MonthViewActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(MonthViewActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(MonthViewActivity.this, errorMessage);

                    } else
                        ErrorMessage.getInstance().showError(MonthViewActivity.this, errorMessage);
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    private Dialog dialog;

    public void showWaitDialog() {
        progressBar.setVisibility(View.VISIBLE);
       /* if (dialog != null && dialog.isShowing()) {

        } else {
            dialog = MessageBox.showSplash(this);
        }
        dialog.setCancelable(false);

        return dialog;*/
    }

    public void hideWaitDialog() {
        progressBar.setVisibility(View.GONE);
       /* progressBar.setVisibility(View.GONE);
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {

            }
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}