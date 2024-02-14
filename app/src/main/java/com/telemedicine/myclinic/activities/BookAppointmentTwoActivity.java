package com.telemedicine.myclinic.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.telemedicine.myclinic.adapters.TimeAdapter;
import com.telemedicine.myclinic.adapters.WeekScheduleOneAppointments;
import com.telemedicine.myclinic.adapters.WeekScheduleTwoAdapterAppointments;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.DoctorWeekScheduleTeleDTO;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.appointments.OrdersRadModel;
import com.telemedicine.myclinic.models.bookAppointment.ApptGetSlotsDTO;
import com.telemedicine.myclinic.models.bookAppointment.ApptGetSlotsModel;
import com.telemedicine.myclinic.models.reschedule.ApptRescheduleDTO;
import com.telemedicine.myclinic.models.reschedule.ApptRescheduleModel;
import com.telemedicine.myclinic.models.secondaryAppt.SecondaryTimeSlotApptDTO;
import com.telemedicine.myclinic.models.weekschedule.DayScheduleList;
import com.telemedicine.myclinic.models.weekschedule.DoctorWeekSchedule;
import com.telemedicine.myclinic.models.weekschedule.DoctorWeekScheduleDTO;
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
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.webservices.RestClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.telemedicine.myclinic.util.Const.APPT_ID;
import static com.telemedicine.myclinic.util.Const.DATE;
import static com.telemedicine.myclinic.util.Const.DOCTOR_ID;
import static com.telemedicine.myclinic.util.Const.DOCTOR_IMAGE_URL;
import static com.telemedicine.myclinic.util.Const.DOCTOR_NAME;
import static com.telemedicine.myclinic.util.Const.DOCTOR_SPECIALITY;
import static com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_ID;
import static com.telemedicine.myclinic.util.Const.UPDATED_TIME;

public class BookAppointmentTwoActivity extends BaseActivity implements TimeAdapter.OnCardClickListner, WeekScheduleTwoAdapterAppointments.OnItemClick {

    @BindView(R.id.weekly_schedule_rv)
    RecyclerView weeklyScheduleRv;

    @BindView(R.id.recycler_time_am)
    RecyclerView recyclerTimeAM;

    @BindView(R.id.recycler_time_pm)
    RecyclerView recyclerTimePM;

    @BindView(R.id.doctor_name)
    BoldTextView doctorNameTv;

    @BindView(R.id.doctor_profession)
    LightTextView doctorProfession;

    @BindView(R.id.dateTime)
    RegularTextView dateTime;

    @BindView(R.id.doctor_image)
    CircleImageView doctorImage;

    @BindView(R.id.nextBtn)
    LightButton nextBtn;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @BindView(R.id.company_name)
    TextView companyName;
    List<DayScheduleList> dayScheduleLists;

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateListener;
    OrdersRadModel ordersRadModel = null;
    String doctorName = "";
    String doctorImageUrl = "";
    String speciality = "";
    String date = "";
    String eDate = "";
    String specialityId = "";
    String doctorId = "";
    boolean isTelemedcine = false;
    TinyDB tinyDB = null;
    long apptBookType;
    long apptId = 0;
    boolean isSecondary = false;
    private boolean isInsurance = false;
    boolean followUpAppt = false;
    String followUpDate = null;
    String itemDate = null;
    boolean isWeekly = false;
    String previousDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {

        transformView();

        weeklyScheduleRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        weeklyScheduleRv.setHasFixedSize(true);

        recyclerTimeAM.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerTimeAM.setHasFixedSize(true);

        recyclerTimePM.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerTimePM.setHasFixedSize(true);

        tinyDB = new TinyDB(this);


        Intent intent = getIntent();
        if (intent != null) {

            doctorName = intent.getStringExtra(DOCTOR_NAME);
            speciality = intent.getStringExtra(DOCTOR_SPECIALITY);
            date = intent.getStringExtra(DATE);
            eDate = intent.getStringExtra(DATE);
            specialityId = intent.getStringExtra(SPECIALITY_ID);
            doctorId = intent.getStringExtra(DOCTOR_ID);
            isTelemedcine = intent.getBooleanExtra(Const.ISTELEMEDICINE_KEY, false);
            doctorImageUrl = intent.getStringExtra(DOCTOR_IMAGE_URL);
            apptId = intent.getLongExtra(APPT_ID, 0);
            isSecondary = intent.getBooleanExtra(Const.IS_SECONDARY, false);
            ordersRadModel = intent.getParcelableExtra(Const.ORDER_RAD);
            isInsurance = intent.getBooleanExtra(Const.IS_INSURANCE_KEY, false);
            followUpAppt = intent.getBooleanExtra(Const.FOLLOW_UP_TYPE, false);
            followUpDate = intent.getStringExtra(Const.FOLLOW_UP_DATE);
            isWeekly = intent.getBooleanExtra("isWeekly", false);
           /* if (apptId != 0 && apptId > 0 && !followUpAppt) {
                nextBtn.setText(R.string.reschedule);
            }*/

            dayScheduleLists = intent.getParcelableArrayListExtra(Const.WEEKLY_ARRY);
            itemDate = intent.getStringExtra(Const.POSITION);
            if (!ValidationHelper.isNullOrEmpty(dayScheduleLists)) {
                setWeekSchedule(dayScheduleLists, itemDate, false);
            }
        }

        setDoctorDetail();
        getSlots();

        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                previousDate = dateTime.getText().toString();
                updateChange();
            }
        };
    }

    IOSDialog iosDialog = null;
    WeekScheduleTwoAdapterAppointments weekScheduleTwoAdapterAppointments = null;

    void setWeekSchedule(List<DayScheduleList> dayScheduleLists, String itemDate, boolean isServiceCall) {

        ArrayList<DayScheduleList> filterDaySchedule = new ArrayList<>();
        boolean isNotAvailable = false;

        for (DayScheduleList daySchedule : dayScheduleLists) {
            if (daySchedule.getIsAvailable()) {
                filterDaySchedule.add(daySchedule);
            } else {
                String date = LocaleDateHelper.convertDateStringFormat(daySchedule.getDate(), "yyyy-MM-dd'T'HH:mm:ss", "EEEE, dd MMMM yyyy", Locale.ENGLISH);
                String date1 = LocaleDateHelper.convertDateStringFormat(dateTime.getText().toString(), "EEEE, dd MMMM yyyy", "EEEE, dd MMMM yyyy", Locale.ENGLISH);

                if (date.equalsIgnoreCase(date1)) {
                    isNotAvailable = true;
                    iosDialog = TextUtil.dialgoue(this, getString(R.string.sorry_request_data), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iosDialog.dismiss();
                            weekScheduleTwoAdapterAppointments.selectFirstDate(true);
                        }
                    });
                }
            }
        }
        weekScheduleTwoAdapterAppointments = new WeekScheduleTwoAdapterAppointments(this, filterDaySchedule, itemDate);
        weeklyScheduleRv.setAdapter(weekScheduleTwoAdapterAppointments);
        weekScheduleTwoAdapterAppointments.setOnItemClick(this);
        if (!isNotAvailable && isServiceCall)
            getSlots();
    }

    @Override
    protected int layout() {
        return R.layout.activity_book_appointment_two;
    }


    public void AppointmentDetail(View view) {

        if (selectApptTime == null || selectApptTime.isEmpty()) {
            ErrorMessage.getInstance().showValidationMessage(this, recyclerTimeAM, getString(R.string.please_select_the_time));
            return;
        }

        if (apptId != 0 && apptId > 0 && !followUpAppt) {

            Intent intent = new Intent(this, BookAppointmentThreeActivity.class);
            intent.putExtra(DOCTOR_NAME, doctorName);
            intent.putExtra(DOCTOR_SPECIALITY, speciality);
            intent.putExtra(DATE, date);
            intent.putExtra(SPECIALITY_ID, specialityId);
            intent.putExtra(DOCTOR_ID, doctorId);
            intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcine);
            intent.putExtra(DOCTOR_IMAGE_URL, doctorImageUrl);
            intent.putExtra(Const.SELECTED_TIME, selectApptTime);
            intent.putExtra(Const.APPT_BOOK_TYPE, apptBookType);
            intent.putExtra(Const.IS_SECONDARY, isSecondary);
            intent.putExtra(Const.ORDER_RAD, ordersRadModel);
            intent.putExtra(Const.IS_INSURANCE_KEY, isInsurance);
            intent.putExtra(Const.FOLLOW_UP_TYPE, followUpAppt);
            intent.putExtra(APPT_ID, apptId);
            startActivityForResult(intent, 113);

            //showWarningMessage(null, apptId);

            return;
        }
        Intent intent = new Intent(this, BookAppointmentThreeActivity.class);
        intent.putExtra(DOCTOR_NAME, doctorName);
        intent.putExtra(DOCTOR_SPECIALITY, speciality);
        intent.putExtra(DATE, date);
        intent.putExtra(SPECIALITY_ID, specialityId);
        intent.putExtra(DOCTOR_ID, doctorId);
        intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcine);
        intent.putExtra(DOCTOR_IMAGE_URL, doctorImageUrl);
        intent.putExtra(Const.SELECTED_TIME, selectApptTime);
        intent.putExtra(Const.APPT_BOOK_TYPE, apptBookType);
        intent.putExtra(Const.IS_SECONDARY, isSecondary);
        intent.putExtra(Const.ORDER_RAD, ordersRadModel);
        intent.putExtra(Const.IS_INSURANCE_KEY, isInsurance);
        intent.putExtra(Const.FOLLOW_UP_TYPE, followUpAppt);
        intent.putExtra(APPT_ID, apptId);
        startActivityForResult(intent, 112);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 112 && resultCode == RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else if (requestCode == 113 && resultCode == RESULT_OK) {
            Intent intent = new Intent();
            intent.putExtra(UPDATED_TIME, data.getStringExtra(UPDATED_TIME));
            intent.putExtra(APPT_ID, data.getLongExtra(APPT_ID, 0));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    void setTimeSlots(ApptGetSlotsModel apptGetSlotsModel) {

        if (!ValidationHelper.isNullOrEmpty(apptGetSlotsModel.getAMSlots())) {
            amSlot(apptGetSlotsModel.getAMSlots());
        } else {
            amSlot(apptGetSlotsModel.getAMSlots());
        }

        if (!ValidationHelper.isNullOrEmpty(apptGetSlotsModel.getPMSlots())) {
            pmSlot(apptGetSlotsModel.getPMSlots());
        } else {
            pmSlot(apptGetSlotsModel.getPMSlots());
        }

        if (ValidationHelper.isNullOrEmpty(apptGetSlotsModel.getAMSlots())
                && ValidationHelper.isNullOrEmpty(apptGetSlotsModel.getPMSlots()))
            ErrorMessage.getInstance().showError(this, getString(R.string.no_appt_available));
    }

    private void amSlot(ArrayList<String> amSlots) {
        TimeAdapter amAdapter = new TimeAdapter(this, amSlots);
        recyclerTimeAM.setAdapter(amAdapter);
        amAdapter.setOnCardClickListner(this);
    }

    void pmSlot(ArrayList<String> amSlots) {
        TimeAdapter pmAdapter = new TimeAdapter(this, amSlots);
        recyclerTimePM.setAdapter(pmAdapter);
        pmAdapter.setOnCardClickListner(this);
    }

    void setDoctorDetail() {

        if (ordersRadModel != null) {
            doctorName = ordersRadModel.getDeviceName();
            speciality = ordersRadModel.getName();
        }

        doctorNameTv.setText(doctorName);
        doctorProfession.setText(speciality);

        dateTime.setText(date);

        if (!ValidationHelper.isNullOrEmpty(doctorImageUrl)) {
            Glide.with(this).load(doctorImageUrl).placeholder(R.drawable.doctr).into((ImageView) doctorImage);
        } else {
            if (isSecondary)
                Glide.with(this).load(R.drawable.xray).placeholder(R.drawable.doctr).into((ImageView) doctorImage);
        }

        //company name
        if (TextUtil.getArabic(this)) {
            if (!isTelemedcine) {
                companyName.setText(tinyDB.getString(Const.COMPANY_NAME_AR));
            }
        } else {
            if (!isTelemedcine) {
                companyName.setText(tinyDB.getString(Const.COMPANY_NAME_EN));
            }
        }
    }

    @OnClick(R.id.changeDate)
    void changeDate() {
        DatePickerDialog datePicker = new DatePickerDialog(this, dateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));


        if (followUpDate != null && followUpAppt) {

            String apptDate = followUpDate;

            Date appDateStart = LocaleDateHelper.getDateFormat(apptDate, "yyyy-MM-dd'T'hh:mm:ss");

            myCalendar.setTime(appDateStart);

            //Add 14 days with current date
            myCalendar.add(Calendar.DAY_OF_MONTH, 14);

            datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            //Set the maximum date to select from DatePickerDialog
            datePicker.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
        } else {
            datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        }

        Date appDateStart = LocaleDateHelper.getDateFormat(date, "yyyy-MM-dd'T'hh:mm:ss");
        Calendar calendrier = Calendar.getInstance();
        calendrier.setTime(appDateStart);

        datePicker.getDatePicker().updateDate(calendrier.get(Calendar.YEAR), calendrier.get(Calendar.MONTH), calendrier.get(Calendar.DAY_OF_MONTH));
        datePicker.setCanceledOnTouchOutside(false);
        datePicker.show();
    }

    private void updateChange() {
        String myFormat = "EEEE, dd MMMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        date = sdf.format(myCalendar.getTime());
        dateTime.setText(sdf.format(myCalendar.getTime()));

        if (isWeekly) {
            if (!isTelemedcine) {
                getWeekSchedule();
            } else {
                getTeleWeekScheduleSecond();
            }
        } else {
            getSlots();
        }

    }

    void getSlots() {

        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
        String companyId = tinyDB.getString(Const.COMPANY_ID);
        String d = dateTime.getText().toString();
        //EEEE, dd MMMM yyyy hh:mm a //

        if (isTelemedcine) {
            companyId = "";
        }
        date = LocaleDateHelper.convertDateStringFormat(d, "EEEE, dd MMMM yyyy", "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

        if (date == null) {
            date = LocaleDateHelper.convertDateStringFormat(d, "EEEE, dd MMMM yyyy hh:mm a", "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        }
        ApptGetSlotsDTO apptGetSlotsDTO = new ApptGetSlotsDTO(securityToken, String.valueOf(patientId), specialityId, doctorId, date, isTelemedcine, companyId, false);

        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog();

            if (!isSecondary) {

                if (!isTelemedcine) {
                    RestClient.getInstance().getApptGetSlots(apptGetSlotsDTO, new OnResultListener() {
                        @Override
                        public void onResult(Object result, boolean status, String errorMessage) {
                            hideWaitDialog();

                            ApptGetSlotsModel apptGetSlotsModel = (ApptGetSlotsModel) result;

                            if (apptGetSlotsModel != null) {

                                MobileOpResult mobileOpResult = apptGetSlotsModel.getMobileOpResult();

                                if (mobileOpResult != null) {

                                    if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                        apptBookType = apptGetSlotsModel.getAppointmentBookType();
                                 /*   if (apptGetSlotsModel != null && ValidationHelper.isNullOrEmpty(apptGetSlotsModel.getAMSlots())
                                            && ValidationHelper.isNullOrEmpty(apptGetSlotsModel.getPMSlots())
                                            && isWeekly) {

                                    } else*/
                                        setTimeSlots(apptGetSlotsModel);

                                    } else {

                                        String errorMesg = "";

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                            if (TextUtil.getEnglish(BookAppointmentTwoActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                            else if (TextUtil.getArabic(BookAppointmentTwoActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                        }

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                            errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                        }

                                        ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMesg);
                                    }

                                } else
                                    ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMessage);
                            } else
                                ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMessage);
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
                                        apptBookType = apptGetSlotsModel.getAppointmentBookType();
                                 /*   if (apptGetSlotsModel != null && ValidationHelper.isNullOrEmpty(apptGetSlotsModel.getAMSlots())
                                            && ValidationHelper.isNullOrEmpty(apptGetSlotsModel.getPMSlots())
                                            && isWeekly) {

                                    } else*/
                                        setTimeSlots(apptGetSlotsModel);

                                    } else {

                                        String errorMesg = "";

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                            if (TextUtil.getEnglish(BookAppointmentTwoActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                            else if (TextUtil.getArabic(BookAppointmentTwoActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                        }

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                            errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                        }

                                        ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMesg);
                                    }

                                } else
                                    ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMessage);
                            } else
                                ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMessage);
                        }
                    });
                }
            } else if (isSecondary) {

                String resourceId = String.valueOf(ordersRadModel.getDeviceId());
                String itemCode = ordersRadModel.getItemCode();

                SecondaryTimeSlotApptDTO secondaryTimeSlotApptDTO = new SecondaryTimeSlotApptDTO(securityToken, String.valueOf(patientId), resourceId, date, itemCode, companyId);

                RestClient.getInstance().apptGetSlotsSecondary(secondaryTimeSlotApptDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        hideWaitDialog();

                        ApptGetSlotsModel apptGetSlotsModel = (ApptGetSlotsModel) result;

                        if (apptGetSlotsModel != null) {

                            MobileOpResult mobileOpResult = apptGetSlotsModel.getMobileOpResult();

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                    apptBookType = apptGetSlotsModel.getAppointmentBookType();
                                    setTimeSlots(apptGetSlotsModel);
                                } else {

                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(BookAppointmentTwoActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(BookAppointmentTwoActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMesg);
                                }

                            } else
                                ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMessage);
                        } else
                            ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMessage);
                    }
                });
            }
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));

        }
    }

    View mv = null;
    LightTextView Txt;
    String selectApptTime = null;

    @Override
    public void OnCardClicked(String model, int pos, View view, LightTextView txt) {

        selectApptTime = model;

        if (mv != null) {
            mv.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            Txt.setTextColor(getResources().getColor(R.color.black));
        }

        mv = view;
        Txt = txt;

        mv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Txt.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    void showWarningMessage(String apptNo, long apptId) {
        try {
            SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            alertDialog.setCancelable(false);
            alertDialog.setTitleText(getResources().getString(R.string.my_clininc)).setCancelText(getResources().getString(R.string.cancel))
                    .setContentText(getString(R.string.sure_do_want_schedule)).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    alertDialog.dismiss();
                }
            }).setConfirmText(getResources().getString(R.string.yes)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    alertDialog.dismiss();

                    if (ConnectionUtil.isInetAvailable(BookAppointmentTwoActivity.this)) {

                        showWaitDialog();

                        String date = dateTime.getText().toString();

                        String apptDate = date + " " + selectApptTime;

                        String formattedDateTime = LocaleDateHelper.convertDateStringFormat(apptDate, "EEEE, dd MMMM yyyy hh:mm a", "yyyy-MM-dd'T'HH:mm:ss");

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
                                            showSuceessFulMessage(apptDate, apptId);

                                        } else {

                                            String errorMesg = "";

                                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                                if (TextUtil.getEnglish(BookAppointmentTwoActivity.this))
                                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                                else if (TextUtil.getArabic(BookAppointmentTwoActivity.this)) {
                                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                                }
                                            }

                                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                            }

                                            ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMesg);
                                        }

                                    } else {
                                        ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMessage);
                                    }

                                } else {
                                    ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMessage);
                                }
                            }
                        });

                    } else {
                        ErrorMessage.getInstance().showWarning(BookAppointmentTwoActivity.this, "Internet is not available");
                    }

                }
            }).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showSuceessFulMessage(String apptdate, long apptId) {

        try {

            SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
            alertDialog.setCancelable(false);


            alertDialog.setTitleText(getResources().getString(R.string.my_clininc))
                    .setContentText(getString(R.string.appt_reschedule_successfully))
                    .setConfirmText(getString(R.string.ok)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    Intent intent = new Intent();
                    intent.putExtra(UPDATED_TIME, apptdate);
                    intent.putExtra(APPT_ID, apptId);
                    setResult(RESULT_OK, intent);
                    finish();

                }
            }).show();

            Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
            btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    @Override
    public void OnCardClicked(DayScheduleList model) {

        if (model != null) {
            String getDate = model.getDate();
            date = LocaleDateHelper.convertDateStringFormat(getDate, "yyyy-MM-dd'T'HH:mm:ss", "EEEE, dd MMMM yyyy", Locale.getDefault());
            dateTime.setText(date);
            getSlots();
        }
    }

    void transformView() {
        if (TextUtil.getArabic(this)) {
            toolbar_left_iv.setRotation(180);
            nextBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
        }
    }

    IOSDialog iosDialog1 = null;

    private void getWeekSchedule() {
        showWaitDialog();
        String doctorId = this.doctorId;
        if (ValidationHelper.isNullOrEmpty(doctorId))
            doctorId = "0";
        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        String companyId = tinyDB.getString(Const.COMPANY_ID);
        date = LocaleDateHelper.convertDateStringFormat(date, "EEEE, dd MMMM yyyy", "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

        if (isTelemedcine) {
            companyId = "";
        }
        if (ConnectionUtil.isInetAvailable(this)) {

            DoctorWeekScheduleDTO doctorWeekScheduleDTO =
                    new DoctorWeekScheduleDTO(doctorId, securityToken, date, specialityId, isTelemedcine, companyId);

            RestClient.getInstance().getDoctorSchedule(doctorWeekScheduleDTO, new OnResultListener() {

                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    DoctorWeekSchedule doctorWeekSchedule = (DoctorWeekSchedule) result;

                    if (doctorWeekSchedule != null) {

                        MobileOpResult mobileOpResult = doctorWeekSchedule.getMobileOpResult();

                        if (mobileOpResult != null) {
                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                List<WeekScheduleResponse> responseList = doctorWeekSchedule.getWeekScheduleResponse();

                                if (!ValidationHelper.isNullOrEmpty(responseList)) {
                                    List<DayScheduleList> dayScheduleLists = responseList.get(0).getDayScheduleList();
                                    if (TextUtil.getArabic(BookAppointmentTwoActivity.this))
                                        Collections.reverse(dayScheduleLists);

                                    if (!ValidationHelper.isNullOrEmpty(dayScheduleLists)) {
                                        String date = LocaleDateHelper.convertDateStringFormat(dateTime.getText().toString(), "EEEE, dd MMMM yyyy", "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                                        setWeekSchedule(dayScheduleLists, date, true);
                                    }
                                } else {
                                    iosDialog1 = TextUtil.dialgoue(BookAppointmentTwoActivity.this, getString(R.string.there_is_no_doctor_available), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (!ValidationHelper.isNullOrEmpty(previousDate))
                                                dateTime.setText(previousDate);
                                            iosDialog1.dismiss();
                                        }
                                    });
                                }
                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(BookAppointmentTwoActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    } else if (TextUtil.getArabic(BookAppointmentTwoActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMesg);
                            }
                        } else
                            ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMessage);
                    } else
                        ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMessage);
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    DoctorWeekScheduleTemp doctorWeekScheduleTemp;

    private void getTeleWeekScheduleSecond() {
        showWaitDialog();
        String doctorId = this.doctorId;
        if (ValidationHelper.isNullOrEmpty(doctorId))
            doctorId = "0";
        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        String companyId = tinyDB.getString(Const.COMPANY_ID);
        String endDate = LocaleDateHelper.convertDateStringFormatWeek(date, "EEEE, dd MMMM yyyy", "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        String startDateTemp = date;
        startDateTemp = LocaleDateHelper.convertDateStringFormat(date, "EEEE, dd MMMM yyyy", "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        String eDate = date;

        if (ConnectionUtil.isInetAvailable(this)) {

            if (isTelemedcine) {
                companyId = "";
            }

            if (doctorId.equals("0")) {
                doctorId = "";
            }

            DoctorWeekScheduleTeleDTO doctorWeekScheduleDTO =
                    new DoctorWeekScheduleTeleDTO(doctorId, securityToken, startDateTemp, endDate, specialityId, isTelemedcine, companyId);

            RestClient.getInstance().getDoctorScheduleTeleSecond(doctorWeekScheduleDTO, new OnResultListener() {

                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    doctorWeekScheduleTemp = (DoctorWeekScheduleTemp) result;

                    if (doctorWeekScheduleTemp != null) {

                        MobileOpResult mobileOpResult = doctorWeekScheduleTemp.getMobileOpResult();

                        if (mobileOpResult != null) {
                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                getTeleWeekSchedule();
                            } else {
                                hideWaitDialog();
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(BookAppointmentTwoActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    } else if (TextUtil.getArabic(BookAppointmentTwoActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMesg);
                            }
                        } else {
                            hideWaitDialog();
                            ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMessage);
                        }

                    } else {
                        hideWaitDialog();
                        ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMessage);
                    }
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    private void getTeleWeekSchedule() {
        showWaitDialog();
        String doctorId = this.doctorId;
        if (ValidationHelper.isNullOrEmpty(doctorId))
            doctorId = "0";
        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        String companyId = tinyDB.getString(Const.COMPANY_ID);
        String endDate = LocaleDateHelper.convertDateStringFormatWeek(date, "EEEE, dd MMMM yyyy", "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        String startDateTemp = date;
        startDateTemp = LocaleDateHelper.convertDateStringFormat(date, "EEEE, dd MMMM yyyy", "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        date =  LocaleDateHelper.convertDateStringFormat(date, "EEEE, dd MMMM yyyy", "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        String fDate = LocaleDateHelper.convertDateStringFormat(date, "yyyy-MM-dd'T'HH:mm:ss", "EEEE, dd MMMM yyyy", Locale.ENGLISH);
        if (ConnectionUtil.isInetAvailable(this)) {

            if (isTelemedcine) {
                companyId = "";
            }

            if (doctorId.equals("0")) {
                doctorId = "";
            }

            DoctorWeekScheduleTeleDTO doctorWeekScheduleDTO =
                    new DoctorWeekScheduleTeleDTO(doctorId, securityToken, startDateTemp, endDate, specialityId, isTelemedcine, companyId);

            RestClient.getInstance().getDoctorScheduleTele(doctorWeekScheduleDTO, new OnResultListener() {

                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    DoctorWeekSchedule doctorWeekSchedule = (DoctorWeekSchedule) result;
                    DoctorWeekSchedule doctorWeekSchedule11 = (DoctorWeekSchedule) result;

                    if (doctorWeekSchedule != null) {

                        MobileOpResult mobileOpResult = doctorWeekSchedule.getMobileOpResult();

                        if (mobileOpResult != null) {
                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                List<WeekScheduleResponse> responseList = doctorWeekSchedule.getWeekScheduleResponse();

                                ArrayList<WeekScheduleResponse> listTemp = new ArrayList<WeekScheduleResponse>();
                                if (!ValidationHelper.isNullOrEmpty(responseList)) {
                                listTemp = (ArrayList<WeekScheduleResponse>) responseList;

                                for (int i = 0; i < listTemp.size(); i++) {
                                    List<DayScheduleList> tempDays = listTemp.get(i).getDayScheduleList();
                                    listTemp.get(i).getDayScheduleList().clear();

                                    List<DayScheduleListTemp> responsesListTemp = doctorWeekScheduleTemp.getWeekScheduleResponse().get(i).getDayScheduleList();
                                    for (int j = 0; j < 7; j++) {
                                        String IncrementedDate = LocaleDateHelper.convertDateStringFormatWeekAddDay(fDate, "EEEE, dd MMMM yyyy",
                                                "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH, j);
                                        boolean added = false;

                                        for (int n = 0; n < responsesListTemp.size(); n++) {
                                            if (responsesListTemp.get(n).getDate().equals(IncrementedDate)) {
                                                added = true;
                                                listTemp.get(i).getDayScheduleList().add(j, new DayScheduleList(responsesListTemp.get(n).getDate(),
                                                        responsesListTemp.get(n).getDoctorNameAr(), responsesListTemp.get(n).getDoctorNameEn(),
                                                        responsesListTemp.get(n).getDayAr(), responsesListTemp.get(n).getDayEn(), responsesListTemp.get(n).getDoctorId(),
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


                                    List<DayScheduleList> dayScheduleLists = listTemp.get(0).getDayScheduleList();
                                    if (TextUtil.getArabic(BookAppointmentTwoActivity.this))
                                        Collections.reverse(dayScheduleLists);

                                    if (!ValidationHelper.isNullOrEmpty(dayScheduleLists)) {
                                        String date = LocaleDateHelper.convertDateStringFormat(dateTime.getText().toString(), "EEEE, dd MMMM yyyy", "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                                        setWeekSchedule(dayScheduleLists, date, true);
                                    }
                                } else {
                                    iosDialog1 = TextUtil.dialgoue(BookAppointmentTwoActivity.this, getString(R.string.there_is_no_doctor_available), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (!ValidationHelper.isNullOrEmpty(previousDate))
                                                dateTime.setText(previousDate);
                                            iosDialog1.dismiss();
                                        }
                                    });
                                }
                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(BookAppointmentTwoActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    } else if (TextUtil.getArabic(BookAppointmentTwoActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMesg);
                            }
                        } else
                            ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMessage);
                    } else
                        ErrorMessage.getInstance().showError(BookAppointmentTwoActivity.this, errorMessage);
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }
}
