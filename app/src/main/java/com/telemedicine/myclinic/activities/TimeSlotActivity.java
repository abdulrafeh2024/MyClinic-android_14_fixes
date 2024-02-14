package com.telemedicine.myclinic.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.telemedicine.myclinic.adapters.TimeAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.bookAppointment.ApptGetSlotsDTO;
import com.telemedicine.myclinic.models.bookAppointment.ApptGetSlotsModel;
import com.telemedicine.myclinic.models.secondaryAppt.SecondaryTimeSlotApptDTO;
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

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.telemedicine.myclinic.util.Const.APPT_ID;
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

public class TimeSlotActivity extends Activity implements TimeAdapter.OnCardClickListner {

    CircleImageView doctorImageView;
    BoldTextView doctorNameTv, DateTv;
    LightTextView locationTv;

    ExpandableLayout morningExpandableLayout;
    AppCompatImageView morningArrowUp, morningArrowDown, afternoonArrowUp,
            afternoonArrowDown, eveningArrowUp, eveningArrowDown;

    RelativeLayout morningLayout, afternoonLayout, eveningLayout;
    ExpandableLayout afternoonExpandableLayout;

    ExpandableLayout eveningExpandableLayout;
    AppCompatImageView locationIcon;

    RecyclerView recyclerTimeAM;

    RecyclerView recyclerTimePM;
    RecyclerView recyclerTimeEvening;
    ProgressBar progressBar;

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
    private String companyName = "";
    String weekDay = "";
    long apptId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextUtil.translation(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot);
        recyclerTimeAM = findViewById(R.id.recycler_time_am);
        recyclerTimePM = findViewById(R.id.recycler_time_pm);
        recyclerTimeEvening = findViewById(R.id.recycler_time_evening);
        doctorImageView = findViewById(R.id.doctor_image);
        doctorNameTv = findViewById(R.id.doctorName);
        locationTv = findViewById(R.id.location);
        progressBar = findViewById(R.id.progress);
        DateTv = findViewById(R.id.DateTv);
        locationIcon = findViewById(R.id.loc);
        morningExpandableLayout = findViewById(R.id.exl_type_morning);
        morningArrowUp = findViewById(R.id.morning_arrow_up);
        morningArrowDown = findViewById(R.id.morning_arrow_down);
        afternoonArrowUp = findViewById(R.id.afternoon_arrow_up);
        afternoonArrowDown = findViewById(R.id.afternoon_arrow_down);
        eveningArrowUp = findViewById(R.id.evening_arrow_up);
        eveningArrowDown = findViewById(R.id.evening_arrow_down);

        morningLayout = findViewById(R.id.morningLayout);
        afternoonLayout = findViewById(R.id.afternoonLayout);
        afternoonExpandableLayout = findViewById(R.id.exl_type_afternoon);

        eveningLayout = findViewById(R.id.eveningLayout);
        eveningExpandableLayout = findViewById(R.id.exl_type_evening);

        Intent intent = getIntent();
        tinyDB = new TinyDB(this);
        if (intent != null) {
            doctorId = intent.getLongExtra("DoctorId", 0);
            company = intent.getStringExtra("Company");
            startDate = intent.getStringExtra("startDate");
            doctorName = intent.getStringExtra("DoctorName");
            doctorDate = intent.getStringExtra("Date");
            weekDay = intent.getStringExtra("week_Day");
            isLoggedInMode = intent.getBooleanExtra(LOGGED_IN_MODE, false);
            specialityNameEn = intent.getStringExtra(SPECIALITY_NAME_EN);
            getSpecialityNameAR = intent.getStringExtra(SPECIALITY_NAME_AR);
            isTelemedicine = intent.getBooleanExtra(ISTELEMEDICINE_KEY, false);
            doctorTime = intent.getStringExtra("Time");
            doctorImage = intent.getStringExtra("image");
            specialityId = intent.getStringExtra("specialityId");
            apptId = intent.getLongExtra(Const.APPT_ID, 0);
        }

        company = tinyDB.getString(Const.COMPANY_ID);

        if (company.equals("nc01")) {
            companyName = getString(R.string.prince_sultan);
        } else if (company.equals("safa")) {
            companyName = getString(R.string.safa);
        } else if (company.equals("prc")) {
            companyName = getString(R.string.prc);
        }
        populateViews();
        initRecyclerViews();
    }

    private void initRecyclerViews() {
        recyclerTimeAM.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerTimeAM.setHasFixedSize(true);

        recyclerTimePM.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerTimePM.setHasFixedSize(true);

        recyclerTimeEvening.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerTimeEvening.setHasFixedSize(true);
        getSlots();

    }

    private void populateViews() {
        Glide.with(this).load(doctorImage).placeholder(R.drawable.doctr).into((ImageView) doctorImageView);
        doctorNameTv.setText(doctorName);

        if (company.equals("nc01")) {
            locationTv.setText(getString(R.string.prince_sultan));
        } else if (company.equals("safa")) {
            locationTv.setText(getString(R.string.safa));
        } else if (company.equals("prc")) {
            locationTv.setText(getString(R.string.prc));
        }

        if (isTelemedicine) {
            locationIcon.setVisibility(View.GONE);
            locationTv.setVisibility(View.GONE);
        } else {
            locationIcon.setVisibility(View.VISIBLE);
            locationTv.setVisibility(View.VISIBLE);
        }

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        Date date1 = LocaleDateHelper.getDateFormat(doctorDate, "yyyy-MM-dd'T'hh:mm:ss");
        //  DateTv.setText(sdf.format(date1.getTime()) + ", " + doctorTime);
        DateTv.setText(weekDay + "   " + sdf.format(date1.getTime()));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    public void Hide(View view) {
        onBackPressed();
    }

   /* public void onTimeClick(View view) {
        onBackPressed();
        Intent intent = new Intent(this, AppointmentBookingRegisterActivity.class);
        startActivity(intent);
    }*/

    void getSlots() {

        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        String patientId = "null";
        boolean isAnonymousMode = true;
        if (isLoggedInMode) {
            isAnonymousMode = false;
            long patId = tinyDB.getLong(Const.PATIENT_ID, 0);
            if (patId != 0) {
                patientId = String.valueOf(patId);
            }
        }

        tinyDB.putString(Const.T_DATE, doctorDate);
        tinyDB.putString("com", company);
        ApptGetSlotsDTO apptGetSlotsDTO = new ApptGetSlotsDTO(securityToken, patientId, specialityId, String.valueOf(doctorId), doctorDate, isTelemedicine,
                company, isAnonymousMode);

        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog();

            if (!isTelemedicine) {
                RestClient.getInstance().getApptGetSlots(apptGetSlotsDTO, new OnResultListener() {
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
                                    setTimeSlots(apptGetSlotsModel);

                                } else {

                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(TimeSlotActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(TimeSlotActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(TimeSlotActivity.this, errorMesg);
                                }

                            } else
                                ErrorMessage.getInstance().showError(TimeSlotActivity.this, errorMessage);
                        } else
                            ErrorMessage.getInstance().showError(TimeSlotActivity.this, errorMessage);
                    }
                });
            } else {
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
                                    setTimeSlots(apptGetSlotsModel);

                                } else {

                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(TimeSlotActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(TimeSlotActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(TimeSlotActivity.this, errorMesg);
                                }

                            } else
                                ErrorMessage.getInstance().showError(TimeSlotActivity.this, errorMessage);
                        } else
                            ErrorMessage.getInstance().showError(TimeSlotActivity.this, errorMessage);
                    }
                });
            }

        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));

        }
    }

    void setTimeSlots(ApptGetSlotsModel apptGetSlotsModel) {

        if (!ValidationHelper.isNullOrEmpty(apptGetSlotsModel.getAMSlots())) {
            amSlot(apptGetSlotsModel.getAMSlots());
            morningLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (morningExpandableLayout.isExpanded()) {
                        morningExpandableLayout.collapse(true);
                        morningArrowDown.setVisibility(View.VISIBLE);
                        morningArrowUp.setVisibility(View.INVISIBLE);
                    } else {
                        morningArrowDown.setVisibility(View.INVISIBLE);
                        morningArrowUp.setVisibility(View.VISIBLE);
                        morningExpandableLayout.setExpanded(true, true);
                    }
                }
            });
        } else {
            morningArrowDown.setVisibility(View.INVISIBLE);
            amSlot(apptGetSlotsModel.getAMSlots());
        }

        if (!ValidationHelper.isNullOrEmpty(apptGetSlotsModel.getPMSlots())) {
            for (String timeStr : apptGetSlotsModel.getPMSlots()) {
                int time = Integer.parseInt(timeStr.substring(0, 2));
                if (time < 5 || time == 12) {
                    afterNoonList.add(timeStr);
                } else {
                    eveningList.add(timeStr);
                }
            }

            pmSlot(afterNoonList);
            eveningSlots(eveningList);
            if (afterNoonList.isEmpty()) {
                afternoonArrowDown.setVisibility(View.INVISIBLE);
            }
            afternoonLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (afternoonExpandableLayout.isExpanded()) {
                        if (!afterNoonList.isEmpty()) {
                            afternoonArrowDown.setVisibility(View.VISIBLE);
                            afternoonArrowUp.setVisibility(View.INVISIBLE);
                        }
                        afternoonExpandableLayout.collapse(true);
                    } else {
                        if (!afterNoonList.isEmpty()) {
                            afternoonArrowDown.setVisibility(View.INVISIBLE);
                            afternoonArrowUp.setVisibility(View.VISIBLE);
                        }
                        afternoonExpandableLayout.setExpanded(true, true);
                    }
                }
            });

            if (eveningList.isEmpty()) {
                eveningArrowDown.setVisibility(View.INVISIBLE);
            }
            eveningLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (eveningExpandableLayout.isExpanded()) {
                        if (!eveningList.isEmpty()) {
                            eveningArrowDown.setVisibility(View.VISIBLE);
                            eveningArrowUp.setVisibility(View.INVISIBLE);
                        }
                        eveningExpandableLayout.collapse(true);
                    } else {
                        if (!eveningList.isEmpty()) {
                            eveningArrowDown.setVisibility(View.INVISIBLE);
                            eveningArrowUp.setVisibility(View.VISIBLE);
                        }
                        eveningExpandableLayout.setExpanded(true, true);
                    }
                }
            });
        } else {
            afternoonArrowDown.setVisibility(View.INVISIBLE);
            eveningArrowDown.setVisibility(View.INVISIBLE);
            pmSlot(apptGetSlotsModel.getPMSlots());
            eveningSlots(apptGetSlotsModel.getPMSlots());
        }

      /*  if (!ValidationHelper.isNullOrEmpty(apptGetSlotsModel.getPMSlots())) {
            pmSlot(apptGetSlotsModel.getPMSlots());
        } else {
            pmSlot(apptGetSlotsModel.getPMSlots());
        }*/

        if (ValidationHelper.isNullOrEmpty(apptGetSlotsModel.getAMSlots())
                && ValidationHelper.isNullOrEmpty(apptGetSlotsModel.getPMSlots()))
            ErrorMessage.getInstance().showError(this, getString(R.string.no_appt_available));
    }

    private void amSlot(ArrayList<String> amSlots) {
        TimeAdapter amAdapter = new TimeAdapter(this, amSlots);
        recyclerTimeAM.setAdapter(amAdapter);
        amAdapter.setOnCardClickListner(this);
    }

    ArrayList<String> afterNoonList = new ArrayList<String>();

    void pmSlot(ArrayList<String> amSlots) {
        TimeAdapter pmAdapter = new TimeAdapter(this, amSlots);
        recyclerTimePM.setAdapter(pmAdapter);
        pmAdapter.setOnCardClickListner(this);
    }

    ArrayList<String> eveningList = new ArrayList<String>();

    void eveningSlots(ArrayList<String> amSlots) {
        TimeAdapter eveningAdapter = new TimeAdapter(this, amSlots);
        recyclerTimeEvening.setAdapter(eveningAdapter);
        eveningAdapter.setOnCardClickListner(this);
    }

    private Dialog dialog;

    public void showWaitDialog() {
        progressBar.setVisibility(View.VISIBLE);
       /* if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        } else {
            dialog = MessageBox.showSplash(this);
        }
        dialog.setCancelable(false);

        return dialog;*/
    }

    public void hideWaitDialog() {
        progressBar.setVisibility(View.GONE);
      /*  if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {

            }
        }*/
    }

    @Override
    public void OnCardClicked(String model, int pos, View view, LightTextView textView) {
        if (isLoggedInMode) {

            String myFormat = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
            Date date1 = LocaleDateHelper.getDateFormat(doctorDate, "yyyy-MM-dd'T'hh:mm:ss");

            Intent intent = new Intent(this, BookAppointmentThreeActivity.class);
            String currentTimeFormatSt = "hh:mm aa";
            String myFormatTimeSt = "HH:mm:ss";
            String finalFormatTime = "";

            SimpleDateFormat currentTFormat = new SimpleDateFormat(currentTimeFormatSt, Locale.US);
            SimpleDateFormat myTFormat = new SimpleDateFormat(myFormatTimeSt, Locale.US);
            try {
                //finalFormatTime = myTFormat.format(currentTFormat.parse(doctorTime));
                finalFormatTime = myTFormat.format(currentTFormat.parse(model));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String formatedTime = sdf.format(date1.getTime()) + "T" + finalFormatTime;
            long patientId = new TinyDB(this).getLong(Const.PATIENT_ID, 0);
            intent.putExtra(Const.APPT_ID, apptId);

            if (!isTelemedicine) {

                if (company.toLowerCase().equals("nc01")) {
                    tinyDB.putString(Const.COMPANY_NAME_AR, getString(R.string.prince_sultan));
                    tinyDB.putString(Const.COMPANY_NAME_EN, getString(R.string.prince_sultan));
                } else if (company.toLowerCase().equals("safa")) {
                    tinyDB.putString(Const.COMPANY_NAME_EN, getString(R.string.safa));
                    tinyDB.putString(Const.COMPANY_NAME_AR, getString(R.string.safa));
                } else {
                    tinyDB.putString(Const.COMPANY_NAME_EN, getString(R.string.prc));
                    tinyDB.putString(Const.COMPANY_NAME_AR, getString(R.string.prc));
                }
            }

            intent.putExtra(DOCTOR_NAME, doctorName);
            intent.putExtra(DOCTOR_SPECIALITY, "");
            intent.putExtra(DATE, formatedTime);
            intent.putExtra(SERVICE_DATE, formatedTime);
            intent.putExtra(SPECIALITY_ID, specialityId);
            intent.putExtra(PATIENT_ID, patientId);


            intent.putExtra(DOCTOR_ID, String.valueOf(doctorId));
            intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicine);
            intent.putExtra(DOCTOR_SPECIALITY, specialityNameEn);
            intent.putExtra(DOCTOR_SPECIALITY, specialityNameEn);

            intent.putExtra(SPECIALITY_NAME_EN, specialityNameEn);
            intent.putExtra(SPECIALITY_NAME_AR, getSpecialityNameAR);

            intent.putExtra(COMPANY_NAME_EN, companyName);
            intent.putExtra(IS_QUICK_REG, true);
            intent.putExtra(DOCTOR_IMAGE_URL, doctorImage);
            intent.putExtra(Const.SELECTED_TIME, model);
            intent.putExtra(Const.APPT_BOOK_TYPE, 10);
            intent.putExtra(Const.IS_SECONDARY, false);
            intent.putExtra("Date_local", doctorDate);
            intent.putExtra(Const.ORDER_RAD, "");
            intent.putExtra(Const.IS_INSURANCE_KEY, false);
            intent.putExtra(Const.FOLLOW_UP_TYPE, false);
            startActivityForResult(intent, 111);
        } else {
            String myFormat = "yyyy-MM-dd"; //In which you need put here
            //  SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
            Date date1 = LocaleDateHelper.getDateFormat(doctorDate, "yyyy-MM-dd'T'hh:mm:ss");
            Intent intent = new Intent(this, AppointmentBookingRegisterActivity.class);
            intent.putExtra("DoctorId", doctorId);
            intent.putExtra("Company", company);
            intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicine);
            intent.putExtra(SPECIALITY_NAME_EN, specialityNameEn);
            intent.putExtra("week_Day", weekDay);
            intent.putExtra(SPECIALITY_NAME_AR, getSpecialityNameAR);
            intent.putExtra("Date", sdf.format(date1.getTime()));
            intent.putExtra("specialityId", specialityId);
            intent.putExtra("DoctorName", doctorName);
            intent.putExtra("Time", model);
            intent.putExtra("image", doctorImage);
            startActivityForResult(intent, 111);
        }
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