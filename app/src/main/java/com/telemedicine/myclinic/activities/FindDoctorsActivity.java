package com.telemedicine.myclinic.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;

import com.telemedicine.myclinic.adapters.DoctorsProfileAdapter;
import com.telemedicine.myclinic.adapters.FindDoctorAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.bookAppointment.GetRefBySpecialtyIdDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetRefDoctorsBySpecialtyModel;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;
import com.telemedicine.myclinic.models.profilecreatoinmodels.SecurityTokenDTO;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextInputLayout;
import com.telemedicine.myclinic.views.LightEdittext;
import com.telemedicine.myclinic.webservices.RestClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY;
import static com.telemedicine.myclinic.util.Const.LOGGED_IN_MODE;

public class FindDoctorsActivity extends BaseActivity implements  FindDoctorAdapter.OnCardClickListner{

    @BindView(R.id.doctor_recycler_view)
    RecyclerView doctorRecyclerView;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @BindView(R.id.search_doctor_till)
    BoldTextInputLayout search_doctor_till;

    @BindView(R.id.search_doctor_et)
    LightEdittext search_doctor_et;


    LinearLayoutManager linearLayoutManager = null;
    boolean isLoggedinMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultDate();

        Intent intent = getIntent();
        if (intent != null) {
            isLoggedinMode = intent.getBooleanExtra(LOGGED_IN_MODE, false);
        }
        initViews();
    }

    private void initViews() {
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        doctorRecyclerView.setLayoutManager
                (linearLayoutManager);

        getDoctorsList();

        search_doctor_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                findDoctorAdapter.getFilter().filter(s.toString());
            }
        });
    }

    @Override
    protected int layout() {
        return R.layout.activity_find_doctors;
    }




    ArrayList<DoctorsProfile> doctorsProfiles = null;
    void getDoctorsList() {

        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog();

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);

            SecurityTokenDTO securityTokenDTO = new SecurityTokenDTO(securityToken);
            RestClient.getInstance().getRefDoctors(securityTokenDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    GetRefDoctorsBySpecialtyModel getRefDoctorsBySpecialtyModel = (GetRefDoctorsBySpecialtyModel) result;

                    if (getRefDoctorsBySpecialtyModel != null) {

                        MobileOpResult mobileOpResult = getRefDoctorsBySpecialtyModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                doctorsProfiles = getRefDoctorsBySpecialtyModel.getDoctorsProfileArrayList();

                                if (!ValidationHelper.isNullOrEmpty(doctorsProfiles)) {
                                    loadDoctorList(doctorsProfiles);
                                }

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(FindDoctorsActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    } else if (TextUtil.getArabic(FindDoctorsActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(FindDoctorsActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(FindDoctorsActivity.this, errorMessage);

                    } else
                        ErrorMessage.getInstance().showError(FindDoctorsActivity.this, errorMessage);
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, "Internet is not available");
        }
    }
    FindDoctorAdapter findDoctorAdapter;
    void loadDoctorList(ArrayList<DoctorsProfile> doctorsProfiles) {
        findDoctorAdapter = new FindDoctorAdapter(this, doctorsProfiles, false);
        doctorRecyclerView.setAdapter(findDoctorAdapter);
        findDoctorAdapter.setOnCardClickListner(this);
    }

    @Override
    public void OnCardClicked(DoctorsProfile model,
                              int pos) {

        Intent intent = new Intent(FindDoctorsActivity.this, SearchAppointmentResultActivity.class);
        intent.putExtra("startDate", strDate);
        intent.putExtra("SpecialityId", "");
        intent.putExtra("DoctorId", model.getDoctorId().toString());
        intent.putExtra("Date", "");
        intent.putExtra("CompanyId", "nc01");
        intent.putExtra(LOGGED_IN_MODE, isLoggedinMode);
        intent.putExtra(ISTELEMEDICINE_KEY, false);
        hideKeyboard(FindDoctorsActivity.this, search_doctor_et);
        startActivityForResult(intent, 111);
    }

    String strDate = "";
    final Calendar myCalendar = Calendar.getInstance();
    void setDefaultDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        strDate = sdf.format(myCalendar.getTime());
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            finish();
        }
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }
}