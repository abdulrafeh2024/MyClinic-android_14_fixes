package com.telemedicine.myclinic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.telemedicine.myclinic.adapters.EarliestSlotsAdapter;
import com.telemedicine.myclinic.adapters.LocationFilterAdapter;
import com.telemedicine.myclinic.adapters.WeekScheduleOneAppointments;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.DoctorProfileDTO;
import com.telemedicine.myclinic.models.HomeModel;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.MonthViewDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetRefSpecialtiesDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetRefSpecialtiesModel;
import com.telemedicine.myclinic.models.company.Company;
import com.telemedicine.myclinic.models.earliestslots.EarliestSlotsDTO;
import com.telemedicine.myclinic.models.earliestslots.EarliestSlotsMiniModel;
import com.telemedicine.myclinic.models.earliestslots.EarliestSlotsResponseModel;
import com.telemedicine.myclinic.models.earliestslotstemp.EarliestSlotsMiniModelTemp;
import com.telemedicine.myclinic.models.earliestslotstemp.EarliestSlotsResponseModelTemp;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;
import com.telemedicine.myclinic.models.weekschedule.DayScheduleList;
import com.telemedicine.myclinic.models.weekschedule.WeekScheduleResponse;
import com.telemedicine.myclinic.models.weekscheduletemp.DayScheduleListTemp;
import com.telemedicine.myclinic.models.weekscheduletemp.DoctorWeekScheduleTemp;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.webservices.RestClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.telemedicine.myclinic.util.Const.COMPANY_NAME_EN;
import static com.telemedicine.myclinic.util.Const.DATE;
import static com.telemedicine.myclinic.util.Const.DOCTOR_ID;
import static com.telemedicine.myclinic.util.Const.DOCTOR_IMAGE_URL;
import static com.telemedicine.myclinic.util.Const.DOCTOR_NAME;
import static com.telemedicine.myclinic.util.Const.DOCTOR_SPECIALITY;
import static com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY;
import static com.telemedicine.myclinic.util.Const.IS_QUICK_REG;
import static com.telemedicine.myclinic.util.Const.LOGGED_IN_MODE;
import static com.telemedicine.myclinic.util.Const.PATIENT_ID;
import static com.telemedicine.myclinic.util.Const.SERVICE_DATE;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_ID;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_NAME_AR;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_NAME_EN;

public class SearchAppointmentResultActivity extends BaseActivity implements EarliestSlotsAdapter.OnCardClickListner, LocationFilterAdapter.OnCardClickListner {

    @BindView(R.id.searchResultRv)
    RecyclerView searchResultRv;

    @BindView(R.id.progress)
    ProgressBar progress;

    @BindView(R.id.placeholder)
    BoldTextView placeholder;

    @BindView(R.id.placeholder_month_view)
    BoldTextView placeholder_month_view;

    @BindView(R.id.monthViewBtn)
    LightButton monthViewBtn;

    @BindView(R.id.speciality)
    LightTextView specialityTv;

    @BindView(R.id.location_filter_rv)
    RecyclerView locationFilterRv;

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
    protected int layout() {
        return R.layout.activity_search_appointment_result;
    }


    int nextRecordCount = 0;
    int currentCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  initSearchResultAdapter();

        init();
        getEarliestSlotsTemp(nextRecordCount);
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    EarliestSlotsAdapter earliestSlotsAdapter;
    LocationFilterAdapter locationFilterAdapter;

    ArrayList<Company> listCompanies = new ArrayList<Company>();

    String specialityId = "";
    String doctorId = "";
    String date = "";
    String companyid = "";
    String startDate = "";
    String companyName = "";
    String speciality = "";
    boolean isTelemedicine = false;
    int locationPositon = 0;
    boolean isLoggedInMode = false;

    boolean nextPageCallSend = false;

    private void init() {

        Intent intent = getIntent();
        if (intent != null) {
            specialityId = intent.getStringExtra("SpecialityId");
            doctorId = intent.getStringExtra("DoctorId");
            date = intent.getStringExtra("Date");
            companyid = intent.getStringExtra("CompanyId");
            startDate = intent.getStringExtra("startDate");
            companyName = intent.getStringExtra("CompanyName");
            speciality = intent.getStringExtra("Speciality");
            isLoggedInMode = intent.getBooleanExtra(LOGGED_IN_MODE, false);
            isTelemedicine = intent.getBooleanExtra(ISTELEMEDICINE_KEY, false);
            if (specialityId.isEmpty()) {
                specialityId = null;
            }

            if (doctorId.isEmpty()) {
                doctorId = null;
            }
            if (date.isEmpty()) {
                date = startDate;
            }
        }

        monthViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDoctorProfile(Long.parseLong(doctorId), -2);
            }
        });

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

        if (isTelemedicine) {
            locationFilterRv.setVisibility(View.GONE);
        } else {
            locationFilterRv.setVisibility(View.VISIBLE);
        }

        if (speciality != null) {
            if (!speciality.isEmpty()) {
                specialityTv.setVisibility(View.VISIBLE);
                specialityTv.setText(speciality);
            }
        }

        if (companyName != null) {
            if (!companyName.isEmpty()) {
                if (companyid.toLowerCase().equals("nc01")) {
                    locationPositon = 0;
                } else if(companyid.toLowerCase().equals("safa")) {
                    locationPositon = 1;
                }else{
                    locationPositon = 2;
                }
                // locationIcon.setVisibility(View.VISIBLE);
                //   locationTv.setVisibility(View.VISIBLE);
                //  locationLayout.setVisibility(View.VISIBLE);
                // locationTv.setText(companyName);
            }
        }

        searchResultRv.setLayoutManager(new LinearLayoutManager(this));
        initSearchAdapter();

        //location adapter

      //  listCompanies.add(null);
        listCompanies.addAll(tinyDB.getListCompany(Const.COMPANY_LIST));
        locationFilterRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        locationFilterAdapter = new LocationFilterAdapter(SearchAppointmentResultActivity.this, listCompanies);
        locationFilterAdapter.setSelectedPosition(locationPositon);
        locationFilterRv.setAdapter(locationFilterAdapter);
        locationFilterAdapter.setOnCardClickListner(SearchAppointmentResultActivity.this);


        searchResultRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (currentCount >= 5 && !nextPageCallSend) {
                       // progress.setVisibility(View.VISIBLE); temp
                        //showWaitDialog();
                        nextRecordCount = nextRecordCount + 5;
                        getEarliestSlotsTemp(nextRecordCount);
                        nextPageCallSend = true;
                    }
                }
            }
        });
    }

    public void initSearchAdapter(){
        earliestSlotsAdapter = new EarliestSlotsAdapter(SearchAppointmentResultActivity.this, listTemp, isTelemedicine, tinyDB);
        searchResultRv.setAdapter(earliestSlotsAdapter);
        earliestSlotsAdapter.setOnCardClickListner(SearchAppointmentResultActivity.this);
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
        } else {
            super.onBackPressed();
        }
    }

    // EarliestSlotsResponseModelTemp earliestSlotsResponseModelTemp;
    ArrayList<EarliestSlotsMiniModelTemp> earliestSlotsResponseModelTemp = new ArrayList<EarliestSlotsMiniModelTemp>();


    private void getEarliestSlotsTemp(int mNextRecordCount) {
        tinyDB.putString(Const.COMPANY_ID, companyid);
        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog();
            if (mNextRecordCount == 0) {
                //showWaitDialog(); temp
                //earliestSlotsResponseModelTemp.clear();
               // listTemp.clear();
            }

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            //  String companyId = tinyDB.getString(Const.COMPANY_ID);
//5637144584

            EarliestSlotsDTO earliestSlotsDTO = new EarliestSlotsDTO(securityToken, specialityId, doctorId, date, companyid, mNextRecordCount, isTelemedicine);

            RestClient.getInstance().getApptGetEarliestSlotsTemp(earliestSlotsDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    EarliestSlotsResponseModelTemp earliestSlotsResponseModelTemp1 = (EarliestSlotsResponseModelTemp) result;

                    if (earliestSlotsResponseModelTemp1 != null) {

                        MobileOpResult mobileOpResult = earliestSlotsResponseModelTemp1.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                if (earliestSlotsResponseModelTemp1.getEarliestSlotsMiniModel() != null) {
                                    currentCount = earliestSlotsResponseModelTemp1.getEarliestSlotsMiniModel().size();
                                    //earliestSlotsResponseModelTemp = earliestSlotsResponseModelTemp1;
                                    // earliestSlotsResponseModelTemp.addAll((ArrayList<EarliestSlotsMiniModel>) earliestSlotsResponseModelTemp1.getEarliestSlotsMiniModel());
                                    earliestSlotsResponseModelTemp.addAll((ArrayList<EarliestSlotsMiniModelTemp>) earliestSlotsResponseModelTemp1.getEarliestSlotsMiniModel());
                                    getEarliestSlots(mNextRecordCount);

                                } else {
                                    nextPageCallSend = false;
                                    hideWaitDialog();
                                    currentCount = 0;
                                    if (mNextRecordCount == 0) {
                                        placeholder.setVisibility(View.VISIBLE);

                                        if(companyid.isEmpty()){
                                            placeholder.setText(getString(R.string.no_earliest_slot_available));
                                        }else{
                                            placeholder.setText(getString(R.string.no_earliest_slot_available_check_other_branch));
                                        }
                                      /*  if (doctorId != null) {
                                            monthViewBtn.setVisibility(View.VISIBLE);
                                            placeholder_month_view.setVisibility(View.VISIBLE);
                                            placeholder.setVisibility(View.GONE);
                                        }*/
                                    }

                                }
                            } else {
                                nextPageCallSend = false;
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    hideWaitDialog();
                                    if (TextUtil.getEnglish(SearchAppointmentResultActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    } else if (TextUtil.getArabic(SearchAppointmentResultActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }
                                hideWaitDialog();
                                nextPageCallSend = false;
                                ErrorMessage.getInstance().showError(SearchAppointmentResultActivity.this, errorMesg);
                            }

                        } else {
                            nextPageCallSend = false;
                            hideWaitDialog();
                            ErrorMessage.getInstance().showError(SearchAppointmentResultActivity.this, errorMessage);
                        }


                    } else {
                        nextPageCallSend = false;
                        hideWaitDialog();
                        ErrorMessage.getInstance().showError(SearchAppointmentResultActivity.this, errorMessage);
                    }
                }
            });
        } else {
            hideWaitDialog();
            nextPageCallSend = false;
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    ArrayList<EarliestSlotsMiniModel> listTemp = new ArrayList<EarliestSlotsMiniModel>();

    //  ArrayList<EarliestSlotsMiniModel> listTemp1 = new ArrayList<EarliestSlotsMiniModel>();

    private void getEarliestSlots(int msNextRecordCount) {
        if (ConnectionUtil.isInetAvailable(this)) {

            if (msNextRecordCount == 0) {
                // showWaitDialog();
              //  listTemp.clear();
            }

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            // String companyId = tinyDB.getString(Const.COMPANY_ID);

            EarliestSlotsDTO earliestSlotsDTO = new EarliestSlotsDTO(securityToken, specialityId, doctorId, date, companyid, msNextRecordCount, isTelemedicine);

            RestClient.getInstance().getApptGetEarliestSlots(earliestSlotsDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();
                    progress.setVisibility(View.GONE);
                    nextPageCallSend = false;
                    EarliestSlotsResponseModel earliestSlotsResponseModel = (EarliestSlotsResponseModel) result;

                    if (earliestSlotsResponseModel != null) {

                        MobileOpResult mobileOpResult = earliestSlotsResponseModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                try {

                                    List<EarliestSlotsMiniModel> responseList = earliestSlotsResponseModel.getEarliestSlotsMiniModel();

                                    //ArrayList<EarliestSlotsMiniModel> listTemp = new ArrayList<EarliestSlotsMiniModel>();
                                    if (!ValidationHelper.isNullOrEmpty(responseList)) {
                                        listTemp.addAll((ArrayList<EarliestSlotsMiniModel>) responseList);

                                        for (int i = msNextRecordCount; i < listTemp.size(); i++) {
                                            List<DayScheduleList> tempDays = listTemp.get(i).getDayScheduleList();
                                            listTemp.get(i).getDayScheduleList().clear();

                                            List<DayScheduleListTemp> responsesListTemp = earliestSlotsResponseModelTemp.get(i).getDayScheduleList();
                                            for (int j = 0; j < 7; j++) {
                                                String IncrementedDate;
                                                if (TextUtil.getArabic(SearchAppointmentResultActivity.this)) {
                                                    IncrementedDate = LocaleDateHelper.convertDateStringFormatWeekAddDayEarliest(startDate + "T00:00:00",
                                                            "yyyy-MM-dd'T'HH:mm:ss", Locale.FRENCH, j);
                                                } else {
                                                    IncrementedDate = LocaleDateHelper.convertDateStringFormatWeekAddDayEarliest(startDate + "T00:00:00",
                                                            "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH, j);
                                                }

                                                boolean added = false;

                                                for (int n = 0; n < responsesListTemp.size(); n++) {
                                                    if (responsesListTemp.get(n).getDate().equals(IncrementedDate)) {
                                                        added = true;
                                                        listTemp.get(i).getDayScheduleList().add(j, new DayScheduleList(responsesListTemp.get(n).getDate(),
                                                                responsesListTemp.get(n).getDoctorNameAr(), responsesListTemp.get(n).getDoctorNameEn(),
                                                                responsesListTemp.get(n).getDayAr(), LocaleDateHelper.getDayOfWeek(responsesListTemp.get(n).getDate(), "yyyy-MM-dd'T'HH:mm:ss"),
                                                                responsesListTemp.get(n).getDoctorId(),
                                                                true));

                                                        break;
                                                    }
                                                }

                                                if (!added) {
                                                    listTemp.get(i).getDayScheduleList().add(j, new DayScheduleList(IncrementedDate,
                                                            "test", "test", "test", "test", 0, false));

                                                }
                                            }
                                        }
                                        earliestSlotsAdapter.notifyDataSetChanged();

                                   /* EarliestSlotsAdapter earliestSlotsAdapter = new EarliestSlotsAdapter(SearchAppointmentResultActivity.this, listTemp);
                                    searchResultRv.setAdapter(earliestSlotsAdapter);
                                    earliestSlotsAdapter.setOnCardClickListner(SearchAppointmentResultActivity.this);*/
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(SearchAppointmentResultActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }

                            } else {

                                String errorMesg = "";
                                nextPageCallSend = false;
                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                    if (TextUtil.getEnglish(SearchAppointmentResultActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    } else if (TextUtil.getArabic(SearchAppointmentResultActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                nextPageCallSend = false;
                                ErrorMessage.getInstance().showError(SearchAppointmentResultActivity.this, errorMesg);
                            }

                        } else {
                            nextPageCallSend = false;
                            ErrorMessage.getInstance().showError(SearchAppointmentResultActivity.this, errorMessage);
                        }


                    } else {
                        nextPageCallSend = false;
                        ErrorMessage.getInstance().showError(SearchAppointmentResultActivity.this, errorMessage);
                    }

                }
            });
        } else {
            nextPageCallSend = false;
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    @Override
    public void OnCardClicked(List<DayScheduleList> dayScheduleLists, DayScheduleList model, EarliestSlotsMiniModel weekScheduleResponse, int finalI) {
    }

    @Override
    public void viewDoctorProfile(long mDoctorId, int itemPosition) {
        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);

            DoctorProfileDTO getRefSpecialtiesDTO = new DoctorProfileDTO(securityToken, mDoctorId);

            RestClient.getInstance().getDoctorProfileById(getRefSpecialtiesDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    HomeModel homeModel = (HomeModel) result;

                    if (homeModel != null) {

                        MobileOpResult mobileOpResult = homeModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                // Toast.makeText(SearchAppointmentResultActivity.this, homeModel.toString(), Toast.LENGTH_LONG).show();
                                if (itemPosition == -2) {
                                    navigateMonthView(homeModel.getDoctorsProfiles().get(0));
                                    return;
                                }
                                earliestSlotsAdapter.setDoctorProfile(homeModel.getDoctorsProfiles());
                                earliestSlotsAdapter.flipTheView(itemPosition);
                                earliestSlotsAdapter.notifyItemChanged(itemPosition);
                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                    if (TextUtil.getEnglish(SearchAppointmentResultActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    } else if (TextUtil.getArabic(SearchAppointmentResultActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(SearchAppointmentResultActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(SearchAppointmentResultActivity.this, errorMessage);

                    } else
                        ErrorMessage.getInstance().showError(SearchAppointmentResultActivity.this, errorMessage);
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    private void navigateMonthView(DoctorsProfile doctorsProfile) {
        // Toast.makeText(this, doctorId.toString(), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, MonthViewActivity.class);
        String weekDay = "";
        String monthDate = startDate + "T00:00:00";

        weekDay = LocaleDateHelper.getDayOfWeek(monthDate, "yyyy-MM-dd'T'HH:mm:ss");
        if (TextUtil.getArabic(this)) {
            if (weekDay.equalsIgnoreCase("Sunday")) {
                weekDay = "الأحد";
            } else if (weekDay.equalsIgnoreCase("Monday")) {
                weekDay = "الاثنين";
            } else if (weekDay.equalsIgnoreCase("Tuesday")) {
                weekDay = "يوم الثلاثاء";
            } else if (weekDay.equalsIgnoreCase("Wednesday")) {
                weekDay = "الأربعاء";
            } else if (weekDay.equalsIgnoreCase("Thursday")) {
                weekDay = "يوم الخميس";
            } else if (weekDay.equalsIgnoreCase("Friday")) {
                weekDay = "جمعة";
            } else if (weekDay.equalsIgnoreCase("Saturday")) {
                weekDay = "السبت";
            }
        }
        intent.putExtra("DoctorId", Long.parseLong(doctorId));
        intent.putExtra("Company", companyid);
        intent.putExtra("startDate", startDate);
        intent.putExtra("week_Day", weekDay);
        intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicine);
        intent.putExtra(LOGGED_IN_MODE, isLoggedInMode);
        intent.putExtra(SPECIALITY_NAME_EN, speciality);
        intent.putExtra(SPECIALITY_NAME_AR, speciality);
        if (TextUtil.getArabic(this)) {
            intent.putExtra("DoctorName", doctorsProfile.getNameAr());
        } else {
            intent.putExtra("DoctorName", doctorsProfile.getNameEn());
        }
        intent.putExtra("Date", monthDate);
        intent.putExtra("Time", "");
        intent.putExtra("image", doctorsProfile.getProfilePicUrl());
        if (isTelemedicine) {
            intent.putExtra("specialityId", specialityId);
        } else {
            intent.putExtra("specialityId", specialityId);
        }

        startActivityForResult(intent, 111);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);


    }

    @Override
    public void onMonthView(long doctorId, String company, EarliestSlotsMiniModel earliestSlotsMiniModel, boolean isBookNow, boolean isWeekView, String dateWeek, String weekDay) {
        //   getMonthView(doctorId, company);

        tinyDB.putString(Const.COMPANY_ID, company);
        if (isBookNow) {
            if (isWeekView) {
                //
                //change for purpose of tele invalid speciality

                Intent intent = new Intent(this, TimeSlotActivity.class);
                intent.putExtra("DoctorId", doctorId);
                intent.putExtra("Company", company);
                intent.putExtra("startDate", startDate);
                intent.putExtra("week_Day", weekDay);
                intent.putExtra(LOGGED_IN_MODE, isLoggedInMode);
                intent.putExtra(SPECIALITY_NAME_EN, earliestSlotsMiniModel.getSpecialtyEn());
                intent.putExtra(SPECIALITY_NAME_AR, earliestSlotsMiniModel.getSpecialtyAr());
                intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicine);
                if (TextUtil.getArabic(this)) {
                    intent.putExtra("DoctorName", earliestSlotsMiniModel.getDoctorNameAr());
                } else {
                    intent.putExtra("DoctorName", earliestSlotsMiniModel.getDoctorName());
                }

                if (isWeekView) {
                    intent.putExtra("Date", dateWeek);
                } else {
                    intent.putExtra("Date", earliestSlotsMiniModel.getSlotDate());
                }
                intent.putExtra("Time", earliestSlotsMiniModel.getSlotTime());
                intent.putExtra("image", earliestSlotsMiniModel.getDoctorProfileUrl());
                if (isTelemedicine) {
                    intent.putExtra("specialityId", specialityId);
                } else {
                    intent.putExtra("specialityId", earliestSlotsMiniModel.getSpecialtyId());
                }

                startActivityForResult(intent, 111);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            } else {
                if (isLoggedInMode) {
                    String myFormat = "yyyy-MM-dd"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                    String doctorDateFinal = "";
                    if (isWeekView) {
                        doctorDateFinal = dateWeek;
                    } else {
                        doctorDateFinal = earliestSlotsMiniModel.getSlotDate();
                    }
                    Date date1 = LocaleDateHelper.getDateFormat(doctorDateFinal, "yyyy-MM-dd'T'hh:mm:ss");

                    Intent intent = new Intent(this, BookAppointmentThreeActivity.class);
                    String currentTimeFormatSt = "hh:mm aa";
                    String myFormatTimeSt = "HH:mm:ss";
                    String finalFormatTime = "";

                    SimpleDateFormat currentTFormat = new SimpleDateFormat(currentTimeFormatSt, Locale.US);
                    SimpleDateFormat myTFormat = new SimpleDateFormat(myFormatTimeSt, Locale.US);
                    try {
                        //finalFormatTime = myTFormat.format(currentTFormat.parse(doctorTime));
                        finalFormatTime = myTFormat.format(currentTFormat.parse(earliestSlotsMiniModel.getSlotTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String formatedTime = sdf.format(date1.getTime()) + "T" + finalFormatTime;
                    long patientId = new TinyDB(this).getLong(Const.PATIENT_ID, 0);
                    intent.putExtra(Const.APPT_ID, 0);

                    if (!isTelemedicine) {

                        if (company.toLowerCase().equals("nc01")) {
                            tinyDB.putString(Const.COMPANY_NAME_AR, getString(R.string.prince_sultan));
                            tinyDB.putString(Const.COMPANY_NAME_EN, getString(R.string.prince_sultan));
                        } else {
                            tinyDB.putString(Const.COMPANY_NAME_EN, getString(R.string.safa));
                            tinyDB.putString(Const.COMPANY_NAME_AR, getString(R.string.safa));
                        }
                    }

                    if (TextUtil.getArabic(this)) {
                        intent.putExtra(DOCTOR_NAME, earliestSlotsMiniModel.getDoctorNameAr());
                    } else {
                        intent.putExtra(DOCTOR_NAME, earliestSlotsMiniModel.getDoctorName());
                    }

                    intent.putExtra(DOCTOR_SPECIALITY, "");
                    intent.putExtra(DATE, formatedTime);
                    intent.putExtra(SERVICE_DATE, formatedTime);
                    // intent.putExtra(SPECIALITY_ID, specialityId);
                    if (isTelemedicine) {
                        intent.putExtra("specialityId", specialityId);
                    } else {
                        intent.putExtra("specialityId", earliestSlotsMiniModel.getSpecialtyId());
                    }
                    intent.putExtra(PATIENT_ID, patientId);
                    intent.putExtra(DOCTOR_ID, String.valueOf(doctorId));
                    intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicine);

                    if (TextUtil.getArabic(this)) {
                        intent.putExtra(DOCTOR_SPECIALITY, earliestSlotsMiniModel.getSpecialtyAr());
                    } else {
                        intent.putExtra(DOCTOR_SPECIALITY, earliestSlotsMiniModel.getSpecialtyEn());
                    }

                    intent.putExtra(SPECIALITY_NAME_EN, earliestSlotsMiniModel.getSpecialtyEn());
                    intent.putExtra(SPECIALITY_NAME_AR, earliestSlotsMiniModel.getSpecialtyAr());

                    intent.putExtra(COMPANY_NAME_EN, companyName);
                    intent.putExtra(IS_QUICK_REG, true);
                    intent.putExtra(DOCTOR_IMAGE_URL, earliestSlotsMiniModel.getDoctorProfileUrl());
                    intent.putExtra(Const.SELECTED_TIME, earliestSlotsMiniModel.getSlotTime());
                    intent.putExtra(Const.APPT_BOOK_TYPE, 10);
                    intent.putExtra(Const.IS_SECONDARY, false);
                    intent.putExtra("Date_local", doctorDateFinal);
                    intent.putExtra(Const.ORDER_RAD, "");
                    intent.putExtra(Const.IS_INSURANCE_KEY, false);
                    intent.putExtra(Const.FOLLOW_UP_TYPE, false);
                    startActivityForResult(intent, 111);
                } else {
                    String myFormat = "yyyy-MM-dd"; //In which you need put here
                    //  SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                    String doctorDateFinal = "";
                    if (isWeekView) {
                        doctorDateFinal = dateWeek;
                    } else {
                        doctorDateFinal = earliestSlotsMiniModel.getSlotDate();
                    }
                    Date date1 = LocaleDateHelper.getDateFormat(doctorDateFinal, "yyyy-MM-dd'T'hh:mm:ss");
                    Intent intent = new Intent(this, AppointmentBookingRegisterActivity.class);
                    intent.putExtra("DoctorId", doctorId);
                    intent.putExtra("Company", company);
                    intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicine);
                    intent.putExtra(SPECIALITY_NAME_EN, earliestSlotsMiniModel.getSpecialtyEn());
                    intent.putExtra("week_Day", weekDay);
                    intent.putExtra(SPECIALITY_NAME_AR, earliestSlotsMiniModel.getSpecialtyAr());
                    intent.putExtra("Date", sdf.format(date1.getTime()));
                    if (isTelemedicine) {
                        intent.putExtra("specialityId", specialityId);
                    } else {
                        intent.putExtra("specialityId", earliestSlotsMiniModel.getSpecialtyId());
                    }
                    if (TextUtil.getArabic(this)) {
                        intent.putExtra("DoctorName", earliestSlotsMiniModel.getDoctorNameAr());
                    } else {
                        intent.putExtra("DoctorName", earliestSlotsMiniModel.getDoctorName());
                    }
                    intent.putExtra("Time", earliestSlotsMiniModel.getSlotTime());
                    intent.putExtra("image", earliestSlotsMiniModel.getDoctorProfileUrl());
                    startActivityForResult(intent, 111);
                }
            }
        } else {
            Intent intent = new Intent(this, MonthViewActivity.class);
            intent.putExtra("DoctorId", doctorId);
            intent.putExtra("Company", company);
            intent.putExtra("startDate", startDate);
            intent.putExtra("week_Day", weekDay);
            intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicine);
            intent.putExtra(LOGGED_IN_MODE, isLoggedInMode);
            intent.putExtra(SPECIALITY_NAME_EN, earliestSlotsMiniModel.getSpecialtyEn());
            intent.putExtra(SPECIALITY_NAME_AR, earliestSlotsMiniModel.getSpecialtyAr());
            if (TextUtil.getArabic(this)) {
                intent.putExtra("DoctorName", earliestSlotsMiniModel.getDoctorNameAr());
            } else {
                intent.putExtra("DoctorName", earliestSlotsMiniModel.getDoctorName());
            }
            if (earliestSlotsMiniModel.getSlotDate() != null) {
                intent.putExtra("Date", earliestSlotsMiniModel.getSlotDate());
                intent.putExtra("Time", earliestSlotsMiniModel.getSlotTime());
            } else {
                intent.putExtra("Date", "");
                intent.putExtra("Time", "");
            }
            intent.putExtra("image", earliestSlotsMiniModel.getDoctorProfileUrl());
            if (isTelemedicine) {
                intent.putExtra("specialityId", specialityId);
            } else {
                intent.putExtra("specialityId", earliestSlotsMiniModel.getSpecialtyId());
            }

            startActivityForResult(intent, 111);
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        }
    }

    @Override
    public void onCompanyClick(String id, String name, boolean isAll) {
        nextRecordCount = 0;
        earliestSlotsResponseModelTemp.clear();
        listTemp.clear();
        initSearchAdapter();
        companyid = id;
        companyName = name;

        placeholder.setVisibility(View.GONE);
        monthViewBtn.setVisibility(View.GONE);
        getEarliestSlotsTemp(nextRecordCount);
    }

  /*  public void openMonthView(View view) {
        Intent intent = new Intent(this, MonthViewActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }*/

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
