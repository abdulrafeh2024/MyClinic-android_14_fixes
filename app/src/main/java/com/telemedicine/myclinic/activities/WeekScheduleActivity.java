package com.telemedicine.myclinic.activities;

import android.content.Intent;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.telemedicine.myclinic.adapters.WeekScheduleOneAppointments;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.DoctorWeekScheduleTeleDTO;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.weekschedule.DayScheduleList;
import com.telemedicine.myclinic.models.weekschedule.DoctorWeekSchedule;
import com.telemedicine.myclinic.models.weekschedule.DoctorWeekScheduleDTO;
import com.telemedicine.myclinic.models.weekschedule.WeekScheduleResponse;
import com.telemedicine.myclinic.models.weekscheduletemp.DayScheduleListTemp;
import com.telemedicine.myclinic.models.weekscheduletemp.DoctorWeekScheduleTemp;
import com.telemedicine.myclinic.models.weekscheduletemp.WeekScheduleResponseTemp;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.webservices.RestClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.telemedicine.myclinic.util.Const.DATE;
import static com.telemedicine.myclinic.util.Const.DOCTOR_ID;
import static com.telemedicine.myclinic.util.Const.DOCTOR_IMAGE_URL;
import static com.telemedicine.myclinic.util.Const.DOCTOR_NAME;
import static com.telemedicine.myclinic.util.Const.DOCTOR_SPECIALITY;
import static com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_ID;

public class WeekScheduleActivity extends BaseActivity implements WeekScheduleOneAppointments.OnCardClickListner {

    @BindView(R.id.schedule_week_time_rv)
    RecyclerView scheduleWeekTimeRv;

    @BindView(R.id.doctor_profession)
    LightTextView doctor_profession;

    @BindView(R.id.placeholder)
    RegularTextView placeholder;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @BindView(R.id.company_name)
    TextView companyName;

    String doctorName = "";
    String doctorImageUrl = "";
    String speciality = "";
    String date = "";
    String specialityId = "";
    String doctorId = "";
    boolean isTelemedcine = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if (!isTelemedcine) {
            getWeekSchedule();
        } else {
            getTeleWeekScheduleSecond();
        }
    }


    private void getWeekSchedule() {
        showWaitDialog();
        String doctorId = this.doctorId;
        if (ValidationHelper.isNullOrEmpty(doctorId))
            doctorId = "0";
        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        String companyId = tinyDB.getString(Const.COMPANY_ID);
        date = LocaleDateHelper.convertDateStringFormat(date, "EEEE, dd MMMM yyyy", "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

        if (ConnectionUtil.isInetAvailable(this)) {

            if (isTelemedcine) {
                companyId = "";
            }
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
                                    placeholder.setVisibility(View.GONE);
                                    WeekScheduleOneAppointments scheduleAppointments = new WeekScheduleOneAppointments(WeekScheduleActivity.this, responseList, speciality);
                                    scheduleWeekTimeRv.setAdapter(scheduleAppointments);
                                    scheduleAppointments.setOnCardClickListner(WeekScheduleActivity.this);
                                }
                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(WeekScheduleActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    } else if (TextUtil.getArabic(WeekScheduleActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(WeekScheduleActivity.this, errorMesg);
                            }
                        } else
                            ErrorMessage.getInstance().showError(WeekScheduleActivity.this, errorMessage);
                    } else
                        ErrorMessage.getInstance().showError(WeekScheduleActivity.this, errorMessage);
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

                                    List<DayScheduleListTemp> responsesListTemp =  doctorWeekScheduleTemp.getWeekScheduleResponse().get(i).getDayScheduleList();
                                    for (int j = 0; j < 7; j++) {
                                        String IncrementedDate = LocaleDateHelper.convertDateStringFormatWeekAddDay(date, "EEEE, dd MMMM yyyy",
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

                                        if(!added){
                                            listTemp.get(i).getDayScheduleList().add(j, new DayScheduleList(IncrementedDate,
                                                    "test", "test", "test", "test", 0, false));
                                        }
                                    }


                                  /*  List<DayScheduleListTemp> responsesListTemp =  doctorWeekScheduleTemp.getWeekScheduleResponse().get(i).getDayScheduleList();
                                    if (responsesListTemp.size() != 0) {
                                        for (int n = 0; n < responsesListTemp.size(); n++) {
                                            for (int n1 = 0; n1 < 7; n1++) {
                                                if (responsesListTemp.get(n).getDate().equals(listTemp.get(i).getDayScheduleList().get(n1).getDate())) {
                                                    listTemp.get(i).getDayScheduleList().set(n1, new DayScheduleList(responsesListTemp.get(n).getDate(),
                                                            responsesListTemp.get(n).getDoctorNameAr(), responsesListTemp.get(n).getDoctorNameEn(),
                                                            responsesListTemp.get(n).getDayAr(), responsesListTemp.get(n).getDayEn(), responsesListTemp.get(n).getDoctorId(),
                                                            true));
                                                    break;
                                                }
                                            }
                                        }
                                    }*/
                                }


                                    placeholder.setVisibility(View.GONE);
                                    WeekScheduleOneAppointments scheduleAppointments = new WeekScheduleOneAppointments(WeekScheduleActivity.this, listTemp, speciality);
                                    scheduleWeekTimeRv.setAdapter(scheduleAppointments);
                                    scheduleAppointments.setOnCardClickListner(WeekScheduleActivity.this);
                                }
                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(WeekScheduleActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    } else if (TextUtil.getArabic(WeekScheduleActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(WeekScheduleActivity.this, errorMesg);
                            }
                        } else
                            ErrorMessage.getInstance().showError(WeekScheduleActivity.this, errorMessage);
                    } else
                        ErrorMessage.getInstance().showError(WeekScheduleActivity.this, errorMessage);
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
                                    if (TextUtil.getEnglish(WeekScheduleActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    } else if (TextUtil.getArabic(WeekScheduleActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(WeekScheduleActivity.this, errorMesg);
                            }
                        } else {
                            hideWaitDialog();
                            ErrorMessage.getInstance().showError(WeekScheduleActivity.this, errorMessage);
                        }

                    } else {
                        hideWaitDialog();
                        ErrorMessage.getInstance().showError(WeekScheduleActivity.this, errorMessage);
                    }
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    private void init() {

        scheduleWeekTimeRv.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();

        if (intent != null) {
            date = intent.getStringExtra(DATE);
            doctorName = intent.getStringExtra(DOCTOR_NAME);
            speciality = intent.getStringExtra(DOCTOR_SPECIALITY);
            date = intent.getStringExtra(DATE);
            specialityId = intent.getStringExtra(SPECIALITY_ID);
            doctorId = intent.getStringExtra(DOCTOR_ID);
            isTelemedcine = intent.getBooleanExtra(Const.ISTELEMEDICINE_KEY, false);
            doctorImageUrl = intent.getStringExtra(DOCTOR_IMAGE_URL);
            if (!ValidationHelper.isNullOrEmpty(speciality))
                doctor_profession.setText(speciality);
        }
        if (TextUtil.getArabic(this))
            toolbar_left_iv.setRotation(180);

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

    @Override
    protected int layout() {
        return R.layout.activity_week_schedule;
    }



    @Override
    public void OnCardClicked(List<DayScheduleList> dayScheduleLists, DayScheduleList model, WeekScheduleResponse weekScheduleResponse, int finalI) {

        if (model != null && weekScheduleResponse != null) {
            String myFormat = "EEEE, dd MMMM yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
            Date date1 = LocaleDateHelper.getDateFormat(model.getDate(), "yyyy-MM-dd'T'hh:mm:ss");
            String date = sdf.format(date1.getTime());
            Intent intent = new Intent(this, BookAppointmentTwoActivity.class);
            intent.putExtra(DOCTOR_NAME, weekScheduleResponse.getDoctorNameEn());
            intent.putExtra(DOCTOR_SPECIALITY, speciality);
            intent.putExtra(DATE, date);
            intent.putExtra(SPECIALITY_ID, String.valueOf(specialityId));
            intent.putExtra(DOCTOR_ID, String.valueOf(weekScheduleResponse.getDoctorId()));
            intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcine);
            intent.putExtra("isWeekly", true);
            intent.putExtra(DOCTOR_IMAGE_URL, weekScheduleResponse.getProfilePicUrl());
            intent.putParcelableArrayListExtra(Const.WEEKLY_ARRY, (ArrayList<? extends Parcelable>) dayScheduleLists);
            intent.putExtra(Const.POSITION, model.getDate());

            startActivityForResult(intent, 111);
        }
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        finish();
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
