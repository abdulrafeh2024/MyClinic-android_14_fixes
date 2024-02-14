package com.telemedicine.myclinic.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.reginald.editspinner.EditSpinner;
import com.telemedicine.myclinic.adapters.SpinnerCustomAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.profileUpdate.PatientRegistrationUpdate;
import com.telemedicine.myclinic.models.profileUpdate.ProfileUpdateDTO;
import com.telemedicine.myclinic.models.profileUpdate.UpdatePatientRegistrationDTO;
import com.telemedicine.myclinic.models.profilecreatoinmodels.Cities;
import com.telemedicine.myclinic.models.profilecreatoinmodels.District;
import com.telemedicine.myclinic.models.profilecreatoinmodels.InsuranceCarriers;
import com.telemedicine.myclinic.models.profilecreatoinmodels.Nationalities;
import com.telemedicine.myclinic.models.profilecreatoinmodels.ProfileCreateModel;
import com.telemedicine.myclinic.models.profilecreatoinmodels.RefPatientProfileModel;
import com.telemedicine.myclinic.models.profilecreatoinmodels.SecurityTokenDTO;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.AppointmentEvent;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextInputLayout;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.LightEdittext;
import com.telemedicine.myclinic.views.LightSpinnerEdittext;
import com.telemedicine.myclinic.webservices.RestClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.telemedicine.myclinic.util.Const.INSURANCE_IMAGES;
import static com.telemedicine.myclinic.util.Const.PATIENT_UPDATE_KEY;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class UpdateProfileFirstActivity extends BaseActivity {

    @BindView(R.id.gender_spinner)
    LightSpinnerEdittext genderSpinner;

    @BindView(R.id.salutation_spinner)
    LightSpinnerEdittext salutatiobSpinner;

    @BindView(R.id.citySpinner)
    LightSpinnerEdittext citySpinner;

    @BindView(R.id.districtSpinner)
    LightSpinnerEdittext districtSpinner;

    @BindView(R.id.districtBoldText)
    BoldTextInputLayout districtBoldText;

    @BindView(R.id.first_name_edt)
    LightEdittext firstNameEdt;

    @BindView(R.id.middle_name_edt)
    LightEdittext middleNameEdt;

    @BindView(R.id.last_name_edt)
    LightEdittext lastNameEdt;

    @BindView(R.id.dateOfBirthEdt)
    LightEdittext dateOfBirthEdt;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @BindView(R.id.next_prfl)
    LightButton next_prfl;

    @BindView(R.id.mrn_edt)
    LightEdittext mrn_edt;

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        callRefPatientProfile();
    }

    private void init() {

        transformView();

        // it converts the item in the list to a string shown in EditText.
        citySpinner.setItemConverter(new EditSpinner.ItemConverter() {
            @Override
            public String convertItemToString(Object selectedItem) {
                String city = (String) selectedItem;
                String cityJeddah = getString(R.string.cityJeddah);
                if (!city.trim().equalsIgnoreCase(cityJeddah.trim())) {
                    districtBoldText.setVisibility(View.GONE);
                } else {
                    districtBoldText.setVisibility(View.VISIBLE);
                }
                return city;
            }
        });

        getBundle();
    }

    @Override
    protected int layout() {
        return R.layout.activity_first_update_profile;
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
                ErrorMessage.getInstance().showSuccessGreen(UpdateProfileFirstActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameAr() + ". "+getString(R.string.please_press_to_continue));
            }else{
                ErrorMessage.getInstance().showSuccessGreen(UpdateProfileFirstActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameEn() + ". "+getString(R.string.please_press_to_continue));
            }} else {
            ErrorMessage.getInstance().showErrorYellow(UpdateProfileFirstActivity.this, appointmentEvent.getErrorMSg());
        }
    }

    public void Continue(View view) {

        String gender = genderSpinner.getText().toString();
        String salutation = salutatiobSpinner.getText().toString();
        String firstName = firstNameEdt.getText().toString();
        String middleName = middleNameEdt.getText().toString();
        String lastName = lastNameEdt.getText().toString();
        String dob = dateOfBirthEdt.getText().toString();
        String city = citySpinner.getText().toString();
        String district = districtSpinner.getText().toString();

        if (ValidationHelper.isNullOrEmpty(gender)) {
            ErrorMessage.getInstance().showValidationMessage(this, genderSpinner, getString(R.string.please_select_gender));
            return;
        } else if (ValidationHelper.isNullOrEmpty(salutation)) {
            ErrorMessage.getInstance().showValidationMessage(this, salutatiobSpinner, getString(R.string.please_select_salutation));
            return;
        } else if (ValidationHelper.isNullOrEmpty(firstName)) {
            ErrorMessage.getInstance().showValidationMessage(this, firstNameEdt, getString(R.string.please_enter_first_name));
            return;
        } else if (ValidationHelper.isNullOrEmpty(middleName)) {
            ErrorMessage.getInstance().showValidationMessage(this, middleNameEdt, getString(R.string.please_enter_middle_name));
            return;
        } else if (ValidationHelper.isNullOrEmpty(lastName)) {
            ErrorMessage.getInstance().showValidationMessage(this, lastNameEdt, getString(R.string.please_enter_last_name));
            return;
        } else if (ValidationHelper.isNullOrEmpty(dob)) {
            ErrorMessage.getInstance().showValidationMessage(this, dateOfBirthEdt, getString(R.string.please_select_date_of_birth));
            return;
        } else if (ValidationHelper.isNullOrEmpty(city)) {
            ErrorMessage.getInstance().showValidationMessage(this, citySpinner, getString(R.string.please_select_city));
            return;
        }
//        else if (districtBoldText.getVisibility() == View.VISIBLE && ValidationHelper.isNullOrEmpty(district)) {
//            ErrorMessage.getInstance().showValidationMessage(this, districtSpinner, getString(R.string.please_select_district));
//            return;
//        }


        String genderVale = null;
        String salutationVale = null;
        gender.trim();

        if (gender.equalsIgnoreCase(getString(R.string.male)))
            genderVale = "1";
        else if (gender.equalsIgnoreCase(getString(R.string.female)))
            genderVale = "2";

        salutation.trim();
        if (salutation.equalsIgnoreCase(getString(R.string.Mr)))
            salutationVale = "1";
        else if (salutation.equalsIgnoreCase(getString(R.string.Mrs)))
            salutationVale = "2";
        else if (salutation.equalsIgnoreCase(getString(R.string.Ms)))
            salutationVale = "3";
        else if (salutation.equalsIgnoreCase(getString(R.string.Miss)))
            salutationVale = "4";
        else if (salutation.equalsIgnoreCase(getString(R.string.Dr)))
            salutationVale = "5";

        String cityId = null;
        String districtId = null;

        if (!ValidationHelper.isNullOrEmpty(citiesList)) {
            String selectedCity = city.trim();
            for (Cities cities : citiesList) {
                if (TextUtil.getEnglish(this)) {
                    if (cities.getNameEn().trim().equalsIgnoreCase(selectedCity)) {
                        cityId = String.valueOf(cities.getId());
                    }
                } else if (TextUtil.getArabic(this)) {
                    if (cities.getNameAr().trim().equalsIgnoreCase(selectedCity)) {
                        cityId = String.valueOf(cities.getId());
                    }
                }
            }
        }

        if (districtBoldText.getVisibility() == View.VISIBLE && !ValidationHelper.isNullOrEmpty(districtsList)) {
            String selectedDistrict = district.trim();
            for (District district1 : districtsList) {
                if (TextUtil.getEnglish(this)) {
                    if (!ValidationHelper.isNullOrEmpty(district1.getNameEn())) {
                        if (district1.getNameEn().trim().equalsIgnoreCase(selectedDistrict)) {
                            districtId = String.valueOf(district1.getId());
                        }
                    } else {
                        ErrorMessage.getInstance().showWarning(this, "District name is null");
                    }

                } else if (TextUtil.getArabic(this)) {
                    if (!ValidationHelper.isNullOrEmpty(district1.getNameAr())) {
                        if (district1.getNameAr().trim().equalsIgnoreCase(selectedDistrict)) {
                            districtId = String.valueOf(district1.getId());
                        }
                    } else {
                        ErrorMessage.getInstance().showWarning(this, "District name is null");
                    }
                }
            }
        }

        dob = LocaleDateHelper.convertDateStringFormat(dob, "MM/dd/yyyy", "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", Locale.ENGLISH);
        UpdatePatientRegistrationDTO updatePatientRegistrationDTO =
                new UpdatePatientRegistrationDTO(patientRegistrationUpdate.getCountry(), patientRegistrationUpdate.getNationalId(),
                        salutationVale, districtId,
                        patientRegistrationUpdate.getNationality(), patientRegistrationUpdate.getNationalIdExpiryDate(),
                        null, firstName, false,
                        middleName, "", genderVale, dob, String.valueOf(patientRegistrationUpdate.getNationalIdType()),
                        patientRegistrationUpdate.getZipCode(),
                        cityId, lastName, patientRegistrationUpdate.getMRN(),String.valueOf(patientRegistrationUpdate.getPatientRecId()),
                        false);

        Intent intent = new Intent(UpdateProfileFirstActivity.this, UpdateProfileSecondActivity.class);
        intent.putExtra("nationalities", nationalitiesList);
        intent.putExtra("insurance", insuranceCarriersList);
        intent.putExtra("patientRegistrationDTO", updatePatientRegistrationDTO);

       // patientRegistrationUpdate.setIdCardScanBase64("");
        intent.putExtra(PATIENT_UPDATE_KEY, patientRegistrationUpdate);
        startActivityForResult(intent, 100);

        //TinyDB tinyDB = new TinyDB(this);
       // updateProfile(updatePatientRegistrationDTO,tinyDB, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }

    void loadCityDistrictValues() {

        ArrayList<String> cityList = new ArrayList<>();
        ArrayList<String> districtList = new ArrayList<>();

        if (!ValidationHelper.isNullOrEmpty(citiesList)) {

            for (Cities cities : citiesList) {
                String city = "";
                if (TextUtil.getEnglish(this))
                    city = cities.getNameEn();
                else if (TextUtil.getArabic(this))
                    city = cities.getNameAr();
                cityList.add(city);
            }

            android.widget.ListAdapter cityAdapter = new SpinnerCustomAdapter(this, R.layout.spinner_item_list, cityList);
            citySpinner.setAdapter(cityAdapter);

        }

        if (!ValidationHelper.isNullOrEmpty(districtsList)) {

            for (District district : districtsList) {
                String dist = "";
                if (TextUtil.getEnglish(this))
                    dist = district.getNameEn();
                else if (TextUtil.getArabic(this))
                    dist = district.getNameAr();
                districtList.add(dist);
            }

            android.widget.ListAdapter districtAdapter = new SpinnerCustomAdapter(this, R.layout.spinner_item_list, districtList);
            districtSpinner.setAdapter(districtAdapter);

        }
    }

    ArrayList<Cities> citiesList = null;
    ArrayList<District> districtsList = null;
    ArrayList<Nationalities> nationalitiesList = null;
    ArrayList<InsuranceCarriers> insuranceCarriersList = null;

    void callRefPatientProfile() {
        showWaitDialog();
        TinyDB tinyDB = new TinyDB(this);
        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        SecurityTokenDTO securityToken1 = new SecurityTokenDTO(securityToken);
        RestClient.getInstance().refPatientProfile(securityToken1, new OnResultListener() {
            @Override
            public void onResult(Object result, boolean status, String errorMessage) {

                hideWaitDialog();

                RefPatientProfileModel refPatientProfileModel = (RefPatientProfileModel) result;

                if (refPatientProfileModel != null) {

                    MobileOpResult mobileOpResult = refPatientProfileModel.getMobileOpResult();
                    if (mobileOpResult != null) {

                        if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                            citiesList = refPatientProfileModel.getRefPatientProfile().getCities();
                            districtsList = refPatientProfileModel.getRefPatientProfile().getDistricts();
                            nationalitiesList = refPatientProfileModel.getRefPatientProfile().getNationalities();
                            insuranceCarriersList = refPatientProfileModel.getRefPatientProfile().getInsuranceCarriers();

                            loadCityDistrictValues();

                            if (patientRegistrationUpdate != null) {
                                fillTheForm(patientRegistrationUpdate);
                            }

                        } else {
                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                if (TextUtil.getEnglish(UpdateProfileFirstActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                else if (TextUtil.getArabic(UpdateProfileFirstActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }
                            ErrorMessage.getInstance().showError(UpdateProfileFirstActivity.this, errorMesg);
                        }
                    } else {
                        ErrorMessage.getInstance().showError(UpdateProfileFirstActivity.this, errorMessage);

                    }
                } else {
                    ErrorMessage.getInstance().showError(UpdateProfileFirstActivity.this, errorMessage);
                }
            }
        });
    }

    PatientRegistrationUpdate patientRegistrationUpdate = null;

    void getBundle() {

        Intent intent = getIntent();

        if (intent != null) {

            patientRegistrationUpdate = intent.getParcelableExtra(PATIENT_UPDATE_KEY);

        }
    }

    void fillTheForm(PatientRegistrationUpdate patientRegistrationUpdate) {

        int genderVal = patientRegistrationUpdate.getGender();
        int salutationVal = patientRegistrationUpdate.getSalutation();
        String firstName = patientRegistrationUpdate.getFirstName();
        String middlename = patientRegistrationUpdate.getMiddleName();
        String lastName = patientRegistrationUpdate.getLastName();
        String dob = patientRegistrationUpdate.getBirthDate();
        String city = patientRegistrationUpdate.getCity();
        String mrn = patientRegistrationUpdate.getMRN();
        long districtId = patientRegistrationUpdate.getDistrictRecId();


        String genderStr = null;
        String salutationStr = null;

        if (genderVal == 1) {
            genderStr = getString(R.string.male);
        } else if (genderVal == 2) {
            genderStr = getString(R.string.female);
        }

        if (salutationVal == 1)
            salutationStr = getString(R.string.Mr);
        else if (salutationVal == 2)
            salutationStr = getString(R.string.Mrs);
        else if (salutationVal == 3)
            salutationStr = getString(R.string.Ms);
        else if (salutationVal == 4)
            salutationStr = getString(R.string.Miss);
        else if (salutationVal == 5)
            salutationStr = getString(R.string.Dr);


        if (!ValidationHelper.isNullOrEmpty(genderStr))
            genderSpinner.setText(genderStr);

        if (!ValidationHelper.isNullOrEmpty(salutationStr))
            salutatiobSpinner.setText(salutationStr);

        if (!ValidationHelper.isNullOrEmpty(firstName))
            firstNameEdt.setText(firstName);

        if (!ValidationHelper.isNullOrEmpty(middlename))
            middleNameEdt.setText(middlename);

        if (!ValidationHelper.isNullOrEmpty(lastName))
            lastNameEdt.setText(lastName);

        String dateOfBirth = LocaleDateHelper.convertDateStringFormat(dob, "yyyy-MM-dd'T'hh:mm:ss", "MM/dd/yyyy");
        if (!ValidationHelper.isNullOrEmpty(dateOfBirth))
            dateOfBirthEdt.setText(dateOfBirth);

        if(!ValidationHelper.isNullOrEmpty(mrn)){
            mrn_edt.setText(mrn);
        }

        String cityName = "";
        if (!ValidationHelper.isNullOrEmpty(citiesList)) {
            String selectedCity = city;
            for (Cities cities : citiesList) {
                if (cities.getId() == Long.parseLong(selectedCity)) {
                    if (TextUtil.getEnglish(this))
                        cityName = cities.getNameEn();
                    else if (TextUtil.getArabic(this))
                        cityName = cities.getNameAr();

                    citySpinner.setText(cityName);
                }
            }
        }

        if (cityName.trim().equalsIgnoreCase(getString(R.string.cityJeddah)))
            districtBoldText.setVisibility(View.VISIBLE);


        if (districtBoldText.getVisibility() == View.VISIBLE && !ValidationHelper.isNullOrEmpty(districtsList)) {
            long selectedDistrict = districtId;
            for (District district1 : districtsList) {
                if (district1.getId() == selectedDistrict) {
                    if (TextUtil.getEnglish(this))
                        districtSpinner.setText(district1.getNameEn());
                    else if (TextUtil.getArabic(this))
                        districtSpinner.setText(district1.getNameAr());

                }
            }
        }
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        INSURANCE_IMAGES = "";
        super.onDestroy();
    }

    void transformView() {
        if (TextUtil.getArabic(this)) {
            toolbar_left_iv.setRotation(180);
            genderSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);
            salutatiobSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);
            citySpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);
            districtSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);
            next_prfl.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            dateOfBirthEdt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0);
        }
    }

    void updateProfile(UpdatePatientRegistrationDTO patientRegistrationDTO, TinyDB tinyDB, boolean showDial) {

        if (showDial)
            showWaitDialog();

        if (tinyDB != null && patientRegistrationDTO != null) {

            String oRegId = tinyDB.getString(Const.OREGID_KEY);
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            ProfileUpdateDTO profileUpdateDTO = new ProfileUpdateDTO(oRegId, securityToken, patientRegistrationDTO);

            RestClient.getInstance().profileUpdate(profileUpdateDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    ProfileCreateModel profileCreateModel = (ProfileCreateModel) result;

                    if (profileCreateModel != null) {

                        MobileOpResult mobileOpResult = profileCreateModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                try {
                                    // get the data
                                    String insuranceId = profileCreateModel.getInsuranceId();
                                    String mrnNo = profileCreateModel.getMRN();
                                    long patientId = profileCreateModel.getPatientId();

                                    // save the value
                                    tinyDB.putString(Const.INSURANCE_ID, insuranceId);

                                    if (!ValidationHelper.isNullOrEmpty(mrnNo))
                                        tinyDB.putString(Const.MRN_NO, mrnNo);
                                    else
                                        tinyDB.putString(Const.MRN_NO, "0");

                                    tinyDB.putLong(Const.PATIENT_ID, patientId);

                                   /* Intent intent = new Intent(UpdateProfileSecondActivity.this, DashBoardActivity.class);
                                    startActivity(intent);*/
                                  //  Intent intent1 = new Intent();
                                    //setResult(RESULT_OK, intent1);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(UpdateProfileFirstActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(UpdateProfileFirstActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(UpdateProfileFirstActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";

                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }
                                ErrorMessage.getInstance().showError(UpdateProfileFirstActivity.this, errorMesg);
                            }

                        } else {
                            ErrorMessage.getInstance().showError(UpdateProfileFirstActivity.this, errorMessage);
                        }
                    } else {
                        ErrorMessage.getInstance().showError(UpdateProfileFirstActivity.this, errorMessage);
                    }
                }
            });
        }
    }
}
