package com.telemedicine.myclinic.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.reginald.editspinner.EditSpinner;
import com.telemedicine.myclinic.adapters.SpinnerCustomAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.profilecreatoinmodels.Cities;
import com.telemedicine.myclinic.models.profilecreatoinmodels.District;
import com.telemedicine.myclinic.models.profilecreatoinmodels.InsuranceCarriers;
import com.telemedicine.myclinic.models.profilecreatoinmodels.Nationalities;
import com.telemedicine.myclinic.models.profilecreatoinmodels.PatientRegistrationDTO;
import com.telemedicine.myclinic.models.profilecreatoinmodels.RefPatientProfileModel;
import com.telemedicine.myclinic.models.profilecreatoinmodels.SecurityTokenDTO;
import com.telemedicine.myclinic.myapplication.R;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class CompleteRegistrationActivity extends BaseActivity {

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.gender_til)
    BoldTextInputLayout gender_til;

    @BindView(R.id.gender_spinner)
    LightSpinnerEdittext genderSpinner;

    @BindView(R.id.salutation_spinner_til)
    BoldTextInputLayout salutation_spinner_til;

    @BindView(R.id.salutation_spinner)
    LightSpinnerEdittext salutatiobSpinner;

    @BindView(R.id.citySpinner_til)
    BoldTextInputLayout citySpinner_til;

    @BindView(R.id.citySpinner)
    LightSpinnerEdittext citySpinner;

    @BindView(R.id.districtSpinner)
    LightSpinnerEdittext districtSpinner;

    @BindView(R.id.districtBoldText)
    BoldTextInputLayout districtBoldText;

    @BindView(R.id.first_name_edt_til)
    BoldTextInputLayout first_name_edt_til;

    @BindView(R.id.first_name_edt_til_ar)
    BoldTextInputLayout first_name_edt_til_ar;

    @BindView(R.id.first_name_edt)
    LightEdittext firstNameEdt;

    @BindView(R.id.first_name_edt_ar)
    LightEdittext firstNameEdt_ar;

    @BindView(R.id.middle_name_edt_til)
    BoldTextInputLayout middle_name_edt_til;

    @BindView(R.id.middle_name_edt_til_ar)
    BoldTextInputLayout middle_name_edt_til_ar;

    @BindView(R.id.middle_name_edt)
    LightEdittext middleNameEdt;

    @BindView(R.id.middle_name_edt_ar)
    LightEdittext middleNameEdt_ar;

    @BindView(R.id.last_name_edt_til)
    BoldTextInputLayout last_name_edt_til;

    @BindView(R.id.last_name_edt_til_ar)
    BoldTextInputLayout last_name_edt_til_ar;

    @BindView(R.id.last_name_edt)
    LightEdittext lastNameEdt;

    @BindView(R.id.last_name_edt_ar)
    LightEdittext lastNameEdt_ar;

    @BindView(R.id.dateOfBirthEdt_til)
    BoldTextInputLayout dateOfBirthEdt_til;

    @BindView(R.id.profile_title)
    RelativeLayout profile_title;

    @BindView(R.id.dateOfBirthEdt)
    LightEdittext dateOfBirthEdt;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @BindView(R.id.next_prfl)
    LightButton next_prfl;

    boolean isAddPatient = false;

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar.setScaleY(3f);
        init();
        fillSpinnersValues();
        callRefPatientProfile();

    }

    private void init() {
        transformView();

        Intent intent = getIntent();
        if (intent != null) {
            isAddPatient = intent.getBooleanExtra(Const.ADD_PATIENT, false);
            if (isAddPatient)
                profile_title.setVisibility(View.GONE);
        }


        date = new DatePickerDialog.OnDateSetListener() {
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


   /*     firstNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtil.getArabic(CompleteRegistrationActivity.this)) {
                    String pattern = "^[A-Za-z0-9. ]+$";
                    if (s.toString().matches(pattern)) {
                        firstNameEdt.setText("");
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

    /*    middleNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtil.getArabic(CompleteRegistrationActivity.this)) {
                    String pattern = "^[A-Za-z0-9. ]+$";
                    if (s.toString().matches(pattern)) {
                        middleNameEdt.setText("");
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

       /* lastNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtil.getArabic(CompleteRegistrationActivity.this)) {
                    String pattern = "^[A-Za-z0-9. ]+$";
                    if (s.toString().matches(pattern)) {
                        lastNameEdt.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/


    }

    @Override
    protected int layout() {
        return R.layout.activity_complete_registration;
    }


    public void SaveContinue(View view) {

        String gender = genderSpinner.getText().toString();
        String salutation = salutatiobSpinner.getText().toString();
        String firstName = firstNameEdt.getText().toString();
        String middleName = middleNameEdt.getText().toString();
        String lastName = lastNameEdt.getText().toString();

        if (TextUtil.getArabic(this)) {
            firstName = firstNameEdt_ar.getText().toString();
            middleName = middleNameEdt_ar.getText().toString();
            lastName = lastNameEdt_ar.getText().toString();
        }

        String dob = dateOfBirthEdt.getText().toString();
        String city = citySpinner.getText().toString();
        String district = districtSpinner.getText().toString();

        if (ValidationHelper.isNullOrEmpty(gender)) {
            TextUtil.tILError(this, gender_til, genderSpinner);
        }
        if (ValidationHelper.isNullOrEmpty(salutation)) {
            TextUtil.tILError(this, salutation_spinner_til, salutatiobSpinner);
        }
        if (ValidationHelper.isNullOrEmpty(firstName)) {
            if (TextUtil.getArabic(this))
                TextUtil.tILError(this, first_name_edt_til_ar, firstNameEdt_ar);
            else
                TextUtil.tILError(this, first_name_edt_til, firstNameEdt);
        }
        if (ValidationHelper.isNullOrEmpty(middleName)) {
            if (TextUtil.getArabic(this))
                TextUtil.tILError(this, middle_name_edt_til_ar, middleNameEdt_ar);
            else
                TextUtil.tILError(this, middle_name_edt_til, middleNameEdt);
        }
        if (ValidationHelper.isNullOrEmpty(lastName)) {
            if (TextUtil.getArabic(this))
                TextUtil.tILError(this, last_name_edt_til_ar, lastNameEdt_ar);
            else
                TextUtil.tILError(this, last_name_edt_til, lastNameEdt);
        }

        if (ValidationHelper.isNullOrEmpty(dob)) {
            TextUtil.tILError(this, dateOfBirthEdt_til, dateOfBirthEdt);
        }
        if (ValidationHelper.isNullOrEmpty(city)) {
            TextUtil.tILError(this, citySpinner_til, citySpinner);
        }

        if (districtBoldText.getVisibility() == View.VISIBLE && ValidationHelper.isNullOrEmpty(district)) {
            TextUtil.tILError(this, districtBoldText, districtSpinner);
        }

        if (ValidationHelper.isNullOrEmpty(gender)) {
            ErrorMessage.getInstance().showValidationMessage(this, genderSpinner, getString(R.string.error_empty_fields));
            return;
        } else if (ValidationHelper.isNullOrEmpty(salutation)) {
            ErrorMessage.getInstance().showValidationMessage(this, salutatiobSpinner, getString(R.string.error_empty_fields));
            return;
        } else if (ValidationHelper.isNullOrEmpty(firstName)) {
            ErrorMessage.getInstance().showValidationMessage(this, firstNameEdt, getString(R.string.error_empty_fields));
            return;
        } else if (ValidationHelper.isNullOrEmpty(middleName)) {
            ErrorMessage.getInstance().showValidationMessage(this, middleNameEdt, getString(R.string.error_empty_fields));
            return;
        } else if (ValidationHelper.isNullOrEmpty(lastName)) {
            ErrorMessage.getInstance().showValidationMessage(this, lastNameEdt, getString(R.string.error_empty_fields));
            return;
        } else if (ValidationHelper.isNullOrEmpty(dob)) {
            ErrorMessage.getInstance().showValidationMessage(this, dateOfBirthEdt, getString(R.string.error_empty_fields));
            return;
        } else if (ValidationHelper.isNullOrEmpty(city)) {
            ErrorMessage.getInstance().showValidationMessage(this, citySpinner, getString(R.string.error_empty_fields));
            return;
        } else if (districtBoldText.getVisibility() == View.VISIBLE && ValidationHelper.isNullOrEmpty(district)) {
            ErrorMessage.getInstance().showValidationMessage(this, districtSpinner, getString(R.string.error_empty_fields));
            return;
        }


        if (gender.equalsIgnoreCase(getString(R.string.male))) {
            if (salutation.equalsIgnoreCase(getString(R.string.Mr)) || salutation.equalsIgnoreCase(getString(R.string.Dr))) {

            } else {

                ErrorMessage.getInstance().showValidationMessage(this, salutatiobSpinner, getString(R.string.correct_salutation));
                return;
            }

        } else if (gender.equalsIgnoreCase(getString(R.string.female))) {
            if (salutation.equalsIgnoreCase(getString(R.string.Miss)) || salutation.equalsIgnoreCase(getString(R.string.Ms))
                    || salutation.equalsIgnoreCase(getString(R.string.Mrs))) {

            } else {

                ErrorMessage.getInstance().showValidationMessage(this, salutatiobSpinner, getString(R.string.correct_salutation));
                return;
            }
        }

        String genderVale = null;
        String salutationVale = null;

        if (gender.trim().equalsIgnoreCase(getString(R.string.male).trim()))
            genderVale = "1";
        else if (gender.trim().equalsIgnoreCase(getString(R.string.female).trim()))
            genderVale = "2";

        if (salutation.trim().equalsIgnoreCase(getString(R.string.Mr).trim()))
            salutationVale = "1";
        else if (salutation.trim().equalsIgnoreCase(getString(R.string.Mrs).trim()))
            salutationVale = "2";
        else if (salutation.trim().equalsIgnoreCase(getString(R.string.Ms).trim()))
            salutationVale = "3";
        else if (salutation.trim().equalsIgnoreCase(getString(R.string.Miss).trim()))
            salutationVale = "4";
        else if (salutation.trim().equalsIgnoreCase(getString(R.string.Dr).trim()))
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
        PatientRegistrationDTO patientRegistrationDTO =
                new PatientRegistrationDTO("", "", salutationVale, districtId,
                        "", "", null, firstName, false,
                        middleName, "", genderVale, dob, "", "",
                        cityId, lastName, "", "", false, "", "", "");


        Intent intent = new Intent(CompleteRegistrationActivity.this, SecondRegistrationCompleteActivity.class);
        intent.putExtra("nationalities", nationalitiesList);
        intent.putExtra("insurance", insuranceCarriersList);
        intent.putExtra(Const.ADD_PATIENT, isAddPatient);
        intent.putExtra("patientRegistrationDTO", patientRegistrationDTO);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Intent intent1 = new Intent();
                intent1.putExtra(Const.ADD_PATIENT, isAddPatient);
                setResult(RESULT_OK, intent1);
                finish();
            }
        }
    }

    void fillSpinnersValues() {

        String[] genderArr = getResources().getStringArray(R.array.gender);
        ArrayList<String> genderArrList = new ArrayList<String>(Arrays.asList(genderArr)); //new ArrayList is only needed if you absolutely need an ArrayList

        android.widget.ListAdapter genderAdapter = new SpinnerCustomAdapter(this, R.layout.spinner_item_list, genderArrList);
        genderSpinner.setAdapter(genderAdapter);

        String[] salutationArr = getResources().getStringArray(R.array.salutation);
        ArrayList<String> salutationArrList = new ArrayList<String>(Arrays.asList(salutationArr)); //new ArrayList is only needed if you absolutely need an ArrayList

        android.widget.ListAdapter salutationAdapter = new SpinnerCustomAdapter(this, R.layout.spinner_item_list, salutationArrList);
        salutatiobSpinner.setAdapter(salutationAdapter);

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

    @OnClick(R.id.dateOfBirthEdt1)
    void datePicker() {
        dateOfBirthEdt_til.requestFocus();
        DatePickerDialog datePicker = new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePicker.setCanceledOnTouchOutside(false);
        datePicker.show();

    }

    private void updateDOB() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        dateOfBirthEdt.setText(sdf.format(myCalendar.getTime()));
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

                        } else {
                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                if (TextUtil.getEnglish(CompleteRegistrationActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                else if (TextUtil.getArabic(CompleteRegistrationActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";

                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }
                            ErrorMessage.getInstance().showError(CompleteRegistrationActivity.this, errorMesg);
                        }
                    } else {
                        ErrorMessage.getInstance().showError(CompleteRegistrationActivity.this, errorMessage);

                    }
                } else {
                    ErrorMessage.getInstance().showError(CompleteRegistrationActivity.this, errorMessage);
                }
            }
        });
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
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

            first_name_edt_til.setVisibility(View.GONE);
            middle_name_edt_til.setVisibility(View.GONE);
            last_name_edt_til.setVisibility(View.GONE);

            first_name_edt_til_ar.setVisibility(View.VISIBLE);
            middle_name_edt_til_ar.setVisibility(View.VISIBLE);
            last_name_edt_til_ar.setVisibility(View.VISIBLE);

          /*  firstNameEdt.setKeyListener(DigitsKeyListener.getInstance("غظضذخثتشرقصجفعسنملةىكيطئءؤحزوهدبا"));
            middleNameEdt.setKeyListener(DigitsKeyListener.getInstance("غظضذخثتشرقصجفعسنملةىكيطئءؤحزوهدبا"));
            lastNameEdt.setKeyListener(DigitsKeyListener.getInstance("غظضذخثتشرقصجفعسنملةىكيطئءؤحزوهدبا"));
*/

        } else {


            first_name_edt_til.setVisibility(View.VISIBLE);
            middle_name_edt_til.setVisibility(View.VISIBLE);
            last_name_edt_til.setVisibility(View.VISIBLE);

            first_name_edt_til_ar.setVisibility(View.GONE);
            middle_name_edt_til_ar.setVisibility(View.GONE);
            last_name_edt_til_ar.setVisibility(View.GONE);

           /* firstNameEdt.setKeyListener(DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));

            middleNameEdt.setKeyListener(DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
            lastNameEdt.setKeyListener(DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
*/
        }
    }


}
