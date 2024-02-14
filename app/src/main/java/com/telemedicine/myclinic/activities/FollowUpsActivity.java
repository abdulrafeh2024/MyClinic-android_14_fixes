package com.telemedicine.myclinic.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;

import com.telemedicine.myclinic.adapters.FollowUpsAdapter;
import com.telemedicine.myclinic.models.dashboardModels.ApptGetFollowUpsModel;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.RegularTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

public class FollowUpsActivity extends BaseActivity implements FollowUpsAdapter.OnCardClickListner {

    @BindView(R.id.list)
    RecyclerView list;

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateListener;

    @BindView(R.id.no_apptmnt)
    RegularTextView no_apptmnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {

        loadFollowUps();
        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDOB();
            }
        };
    }

    private void updateDOB() {
        String myFormat = "EEEE, dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        String date = sdf.format(myCalendar.getTime());

        if (model != null && model.getDoctorProfile() != null) {

            DoctorsProfile doctorsProfile = model.getDoctorProfile();

            Intent intent = new Intent(this, BookAppointmentTwoActivity.class);
            String docName = "";
            String speciality = "";

            if (TextUtil.getEnglish(this)) {
                docName = doctorsProfile.getNameEn();
                speciality = doctorsProfile.getSpecialtyEn();
            } else if (TextUtil.getArabic(this)) {
                docName = doctorsProfile.getNameAr();
                speciality = doctorsProfile.getSpecialtyAr();
            }

            intent.putExtra(DOCTOR_NAME, docName);
            intent.putExtra(DOCTOR_SPECIALITY, speciality);
            intent.putExtra(DATE, date);
            intent.putExtra(SPECIALITY_ID, doctorsProfile.getSpecialtyId());
            intent.putExtra(DOCTOR_ID, String.valueOf(doctorsProfile.getDoctorId()));
            intent.putExtra(ISTELEMEDICINE_KEY, false);
            intent.putExtra(DOCTOR_IMAGE_URL, doctorsProfile.getProfilePicUrl());
            intent.putExtra(Const.APPT_ID, model.getApptId());
            intent.putExtra(Const.FOLLOW_UP_TYPE, true);
            if (model != null)
                intent.putExtra(Const.FOLLOW_UP_DATE, model.getApptDate());
            startActivityForResult(intent, 111);
        }

    }

    @Override
    protected int layout() {
        return R.layout.activity_follow_ups;
    }


    void loadFollowUps() {

        Intent intent = getIntent();

        if (intent != null) {

            ArrayList<ApptGetFollowUpsModel> patientsMiniModels = intent.getParcelableArrayListExtra(Const.FOLLOW_UPS);

            if (!ValidationHelper.isNullOrEmpty(patientsMiniModels)) {
                FollowUpsAdapter adapter = new FollowUpsAdapter(this, patientsMiniModels);
                list.setLayoutManager(new LinearLayoutManager(this));
                list.setAdapter(adapter);
                adapter.setOnCardClickListner(this);
                no_apptmnt.setVisibility(View.GONE);
            } else {
                no_apptmnt.setVisibility(View.VISIBLE);
            }
        }
    }

    ApptGetFollowUpsModel model = null;

    @Override
    public void OnCardClicked(ApptGetFollowUpsModel model, int pos) {
        this.model = model;

        String apptDate = model.getApptDate();

        Date appDateStart = LocaleDateHelper.getDateFormat(apptDate, "yyyy-MM-dd'T'HH:mm:ss");

        DatePickerDialog datePicker = new DatePickerDialog(this, dateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        myCalendar.setTime(appDateStart);

        //Add 14 days with current date
        myCalendar.add(Calendar.DAY_OF_MONTH, 14);

        datePicker.getDatePicker().setMinDate(System.currentTimeMillis());

        //   datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        //Set the maximum date to select from DatePickerDialog
        datePicker.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
        datePicker.setCanceledOnTouchOutside(false);
        datePicker.show();

    }


    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }
}
