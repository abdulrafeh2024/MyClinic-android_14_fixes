package com.telemedicine.myclinic.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.widget.CompoundButtonCompat;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.raywenderlich.android.validatetor.ValidateTor;
import com.reginald.editspinner.EditSpinner;
import com.samkazmi.simpleimageselect.Config;
import com.samkazmi.simpleimageselect.SimpleImageSelect;
import com.telemedicine.myclinic.adapters.SpinnerCustomAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.patientMiniModels.PatientsMiniModel;
import com.telemedicine.myclinic.models.profileInsuranceValidate.InsuranceModel;
import com.telemedicine.myclinic.models.profileInsuranceValidate.ProfileInsuranceValidateDTO;
import com.telemedicine.myclinic.models.profileInsuranceValidate.ProfileInsuranceValidateModel;
import com.telemedicine.myclinic.models.profileUpdate.ProfileUpdateDTO;
import com.telemedicine.myclinic.models.profileUpdate.UpdatePatientRegistrationDTO;
import com.telemedicine.myclinic.models.profilecreatoinmodels.InsuranceCarriers;
import com.telemedicine.myclinic.models.profilecreatoinmodels.InsuranceDTO;
import com.telemedicine.myclinic.models.profilecreatoinmodels.Nationalities;
import com.telemedicine.myclinic.models.profilecreatoinmodels.PatientRegistrationDTO;
import com.telemedicine.myclinic.models.profilecreatoinmodels.ProfileCreateDTO;
import com.telemedicine.myclinic.models.profilecreatoinmodels.ProfileCreateModel;
import com.telemedicine.myclinic.models.profilevalidatemodels.ProfileValidateDTO;
import com.telemedicine.myclinic.models.profilevalidatemodels.ProfileValidateModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextInputLayout;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.LightCheckBox;
import com.telemedicine.myclinic.views.LightEdittext;
import com.telemedicine.myclinic.views.LightSpinnerEdittext;
import com.telemedicine.myclinic.webservices.RestClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionUtils;

import static com.telemedicine.myclinic.util.Const.EXISTING_PATIENT;
import static com.telemedicine.myclinic.util.Const.PATIENT_GENDER;
import static com.telemedicine.myclinic.util.Const.PATIENT_NAME;
import static com.telemedicine.myclinic.util.Const.TOKEN_KEY;

public class SecondRegistrationCompleteActivity extends BaseActivity {


    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.insurance_container_fields)
    LinearLayout insuranceContainer;

    @BindView(R.id.nationality_til)
    BoldTextInputLayout nationality_til;

    @BindView(R.id.nationality)
    LightSpinnerEdittext nationalitySpinner;

    @BindView(R.id.nationalIdType_til)
    BoldTextInputLayout nationalIdType_til;

    @BindView(R.id.nationalIdType)
    LightSpinnerEdittext nationalIdTypeSpinner;

    @BindView(R.id.nationalID)
    LightSpinnerEdittext nationalID;

    @BindView(R.id.nationalIDTIL)
    BoldTextInputLayout nationalIDTIL;

    @BindView(R.id.expiryDateEdt_til)
    BoldTextInputLayout expiryDateEdt_til;

    @BindView(R.id.insuranceExpiryDate_til)
    BoldTextInputLayout insuranceExpiryDate_til;

    @BindView(R.id.expiryDateEdt)
    LightEdittext expiryDateEdt;

    @BindView(R.id.insurance_carrier_spiiner_til)
    BoldTextInputLayout insurance_carrier_spiiner_til;

    @BindView(R.id.insurance_carrier_spiiner)
    LightSpinnerEdittext insuranceCarrierSpinner;

    @BindView(R.id.insuranceExpiryDate)
    LightEdittext insuranceExpiryDate;

    @BindView(R.id.policyNoEdt_til)
    BoldTextInputLayout policyNoEdt_til;

    @BindView(R.id.policyNoEdt)
    LightEdittext policyNoEdt;

    @BindView(R.id.member_ship_no_til)
    BoldTextInputLayout member_ship_no_til;

    @BindView(R.id.member_ship_no)
    LightEdittext memberShipNo;

    @BindView(R.id.contractName)
    LightEdittext contractName;

    @BindView(R.id.carrierNameEdt)
    LightEdittext carrierNameEdt;

    @BindView(R.id.nationalIdImg)
    ImageView nationalIdImg;

    @BindView(R.id.insuranceCardImg)
    ImageView insuranceCardImg;

    @BindView(R.id.termsConditions)
    LightCheckBox termsConditions;

    @BindView(R.id.profile_title)
    RelativeLayout profile_title;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @BindView(R.id.national_id_attach)
    TextView national_id_attach;

    @BindView(R.id.insurance_id_icon)
    TextView insurance_id_icon;

    @BindView(R.id.submit_btn)
    LightButton submit_btn;

    boolean isInsuranceDate = false;
    String insuranceCardBase64 = "";
    String nationalCardBase64 = "";
    long insuranceCarrierType = 0;

    // insurance data
    long contractId = 0;
    String memberShipIdNo = "";
    long classPlanId = 0;
    String carrierName = "";
    long carrierId = 0;


    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;

    PatientRegistrationDTO patientRegistrationDTO = null;
    InsuranceDTO insuranceDTO = null;

    boolean isAddPatient = false;
    boolean errorEnable = false;
    String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar.setScaleY(3f);
        transformView();
        init();
        getRadioValues();
        getIntentValue();
    }

    String[] arr;

    void fillNationalId() {
        arr = getResources().getStringArray(R.array.Betaqa);
        ArrayList<String> nationaId = new ArrayList<String>(Arrays.asList(arr)); //new ArrayList is only needed if you absolutely need an ArrayList

        if (!ValidationHelper.isNullOrEmpty(nationaId)) {
            android.widget.ListAdapter salutationAdapter = new SpinnerCustomAdapter(SecondRegistrationCompleteActivity.this, R.layout.spinner_item_list, nationaId);
            nationalIdTypeSpinner.setAdapter(salutationAdapter);
        }
    }

    private void init() {
        nationalID.setHint(getResources().getString(R.string.national_id));
        nationalID.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        // nationalID.setFilters(new InputFilter[]{filter});
        //nationalID.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        Intent intent = getIntent();
        if (intent != null) {
            isAddPatient = intent.getBooleanExtra(Const.ADD_PATIENT, false);
            if (isAddPatient)
                profile_title.setVisibility(View.GONE);
        }

        fillNationalId();

        // it converts the item in the list to a string shown in EditText.
        nationalitySpinner.setItemConverter(new EditSpinner.ItemConverter() {
            @Override
            public String convertItemToString(Object selectedItem) {

                String country = (String) selectedItem.toString();

                String countryKSA = getString(R.string.KSA).trim();

                if (country.equalsIgnoreCase(countryKSA)) {
                    arr = getResources().getStringArray(R.array.Betaqa);
                    nationalIdTypeSpinner.setText(R.string.betaqa);
                    nationalID.setHint("");
                    nationalIDTIL.setHint(getResources().getString(R.string.betaqa_no));

                    nationalID.setRawInputType(InputType.TYPE_CLASS_NUMBER);

                    // nationalIDTIL.setHint(nationalID.getHint());
                    /*nationalID.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                    nationalID.setFilters(new InputFilter[]{filter});*/

                } else {
                    arr = getResources().getStringArray(R.array.nationalId);
                    if (nationalIdTypeSpinner.getText().toString().equalsIgnoreCase(getString(R.string.betaqa))) {
                        // nationalID.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                        nationalID.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                    } else if (nationalIdTypeSpinner.getText().toString().equalsIgnoreCase(getString(R.string.Passport))) {

                        InputFilter[] filters = new InputFilter[1];
                        filters[0] = new InputFilter() {
                            @Override
                            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                                if (end > start) {

                                    char[] acceptedChars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                                            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                                            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '@', '.', '_', '#', '$', '%', '&', '*', '-', '+', '(', ')', '!', '"', '\'', ':',
                                            ';', '/', '?', ',', '~', '`', '|', '\\', '^', '<', '>', '{', '}', '[', ']', '=', '£', '¥', '€', '.', '¢', '•', '©'};

                                    for (int index = start; index < end; index++) {
                                        if (!new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) {
                                            return "";
                                        }
                                    }
                                }
                                return null;
                            }

                        };

                        nationalID.setFilters(filters);

                        //  nationalID.setKeyListener(DigitsKeyListener.getInstance("0123456789ABCDEFGHIJKLMNOPQRSTUVYXYZabcdefghijklmnopqrstuvwxyz"));
                    }
                    nationalIdTypeSpinner.setText("");
                    nationalID.setHint("");
                    nationalIDTIL.setHint(getResources().getString(R.string.national_id1));
                    if (errorEnable)
                        TextUtil.tILError(SecondRegistrationCompleteActivity.this, nationalIDTIL, nationalID);
                }

                ArrayList<String> nationaId = new ArrayList<String>(Arrays.asList(arr)); //new ArrayList is only needed if you absolutely need an ArrayList

                if (!ValidationHelper.isNullOrEmpty(nationaId)) {
                    android.widget.ListAdapter salutationAdapter = new SpinnerCustomAdapter(SecondRegistrationCompleteActivity.this, R.layout.spinner_item_list, nationaId);
                    nationalIdTypeSpinner.setAdapter(salutationAdapter);
                }

                return country;
            }
        });


        nationalIdTypeSpinner.setItemConverter(new EditSpinner.ItemConverter() {
            @Override
            public String convertItemToString(Object selectedItem) {

                str = (String) selectedItem.toString().trim();

                if (str.equalsIgnoreCase(getResources().getString(R.string.Iqama).trim()) ||
                        str.equalsIgnoreCase(getResources().getString(R.string.betaqa).trim())) {
                   /* nationalID.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                    nationalID.setFilters(new InputFilter[]{filter});
*/
                    /*nationalID.setInputType(InputType.TYPE_CLASS_NUMBER);*/

                    nationalID.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                } else {
                   /* nationalID.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                    nationalID.setFilters(new InputFilter[]{filter1});*/

                    nationalID.setRawInputType(InputType.TYPE_CLASS_TEXT);

                    InputFilter[] filters = new InputFilter[1];
                    filters[0] = new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                            if (end > start) {

                                char[] acceptedChars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                                        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                                        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '@', '.', '_', '#', '$', '%', '&', '*', '-', '+', '(', ')', '!', '"', '\'', ':',
                                        ';', '/', '?', ',', '~', '`', '|', '\\', '^', '<', '>', '{', '}', '[', ']', '=', '£', '¥', '€', '.', '¢', '•', '©'};

                                for (int index = start; index < end; index++) {
                                    if (!new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) {
                                        return "";
                                    }
                                }
                            }
                            return null;
                        }

                    };

                    nationalID.setFilters(filters);

                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(nationalID.getWindowToken(), 0);

                return str;
            }
        });


        insuranceCarrierSpinner.setItemConverter(new EditSpinner.ItemConverter() {
            @Override
            public String convertItemToString(Object selectedItem) {

                String insuranceCarrier = (String) selectedItem.toString().trim();

                if (!ValidationHelper.isNullOrEmpty(insuranceCarriers)) {

                    for (InsuranceCarriers insuranceCarriers1 : insuranceCarriers) {
                        String insuranceName = "";
                        if (TextUtil.getEnglish(SecondRegistrationCompleteActivity.this)) {
                            insuranceName = insuranceCarriers1.getNameEn();
                        } else if (TextUtil.getArabic(SecondRegistrationCompleteActivity.this)) {
                            insuranceName = insuranceCarriers1.getNameAr();
                        }

                        if (insuranceCarrier.equalsIgnoreCase(insuranceName.trim())) {
                            insuranceCarrierType = insuranceCarriers1.getId();

                        }
                    }
                }

                return insuranceCarrier;
            }
        });

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDOB(isInsuranceDate);
            }
        };

        SimpleImageSelect.Config(this, getString(R.string.choose_image_media), "MyClinic");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        validateInsurance();

        expiryDateEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                }
            }
        });

    }

    private void updateDOB(boolean insuranceDate) {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        if (insuranceDate)
            insuranceExpiryDate.setText(sdf.format(myCalendar.getTime()));
        else
            expiryDateEdt.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected int layout() {
        return R.layout.activity_second_registration_complete;
    }

    boolean isInsuranceChecked = false;

    void getRadioValues() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radioYes:
                        isInsuranceChecked = true;
                        insuranceContainer.setVisibility(View.VISIBLE);
                        break;

                    case R.id.radioNo:
                        isInsuranceChecked = false;
                        insuranceContainer.setVisibility(View.GONE);
                        break;
                }


            }
        });
    }

    public void Submit(View view) {
        ValidateTor validateTor = new ValidateTor();

        String nationality = nationalitySpinner.getText().toString();
        String nationalIdType = nationalIdTypeSpinner.getText().toString();
        String nationalTypeId = nationalIdTypeSpinner.getText().toString();
        String nationalyIdNumber = nationalID.getText().toString();
        String expiryDate = expiryDateEdt.getText().toString();
        String insuranceCarrier = insuranceCarrierSpinner.getText().toString();
        String policyNo = policyNoEdt.getText().toString();
        String memberShipNum = memberShipNo.getText().toString();
        String insuranceED = insuranceExpiryDate.getText().toString();
        String carrierN = carrierNameEdt.getText().toString();
        String contractN = contractName.getText().toString();

        if (ValidationHelper.isNullOrEmpty(nationality)) {
            TextUtil.tILError(this, nationality_til, nationalitySpinner);
        }

        if (ValidationHelper.isNullOrEmpty(nationalTypeId)) {
            TextUtil.tILError(this, nationalIdType_til, nationalIdTypeSpinner);
            errorEnable = true;
        }

        if (ValidationHelper.isNullOrEmpty(nationalyIdNumber)) {
            TextUtil.tILError(this, nationalIDTIL, nationalID);
        }

        if (ValidationHelper.isNullOrEmpty(expiryDate)) {
            TextUtil.tILError(this, expiryDateEdt_til, expiryDateEdt);
        }

        if (!termsConditions.isChecked()) {
            if (Build.VERSION.SDK_INT < 21) {
                CompoundButtonCompat.setButtonTintList(termsConditions, ColorStateList.valueOf(getResources().getColor(R.color.colorred)));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
            } else {
                termsConditions.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorred)));//setButtonTintList is accessible directly on API>19
            }
            termsConditions.setTextColor(getResources().getColor(R.color.colorred));
        }

        termsConditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (Build.VERSION.SDK_INT < 21) {
                        CompoundButtonCompat.setButtonTintList(termsConditions, ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
                    } else {
                        termsConditions.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));//setButtonTintList is accessible directly on API>19
                    }

                    termsConditions.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });

        if (ValidationHelper.isNullOrEmpty(nationality)) {
            ErrorMessage.getInstance().showValidationMessage(this, nationalitySpinner, getString(R.string.error_empty_fields));
            return;
        } else if (ValidationHelper.isNullOrEmpty(nationalTypeId)) {
            ErrorMessage.getInstance().showValidationMessage(this, nationalIdTypeSpinner, getString(R.string.error_empty_fields));
            return;
        } else if (ValidationHelper.isNullOrEmpty(nationalyIdNumber)) {
            ErrorMessage.getInstance().showValidationMessage(this, nationalID, getString(R.string.error_empty_fields));
            return;
        } else if (nationalIdType.trim().equalsIgnoreCase(getString(R.string.betaqa).trim()) || nationalIdType.trim().equalsIgnoreCase(getString(R.string.Iqama).trim())) {
            if (validateTor.isAtleastLength(nationalyIdNumber, 10)) {

            } else {
                ErrorMessage.getInstance().showValidationMessage(this, nationalID, getString(R.string.valid_betaqa));
                return;
            }
        }

        if (ValidationHelper.isNullOrEmpty(expiryDate)) {
            ErrorMessage.getInstance().showValidationMessage(this, expiryDateEdt, getString(R.string.error_empty_fields));
            return;
        }

        expiryDate = LocaleDateHelper.convertDateStringFormat(expiryDate, "MM/dd/yyyy", "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", Locale.ENGLISH);


        if (isInsuranceChecked) {

            if (ValidationHelper.isNullOrEmpty(insuranceCarrier)) {
                TextUtil.tILError(this, insurance_carrier_spiiner_til, insuranceCarrierSpinner);
            }

            if (ValidationHelper.isNullOrEmpty(policyNo)) {
                tILError(this, policyNoEdt_til, policyNoEdt);
            }

            if (ValidationHelper.isNullOrEmpty(memberShipNum)) {
                tILError(this, member_ship_no_til, memberShipNo);
            }

            if (!termsConditions.isChecked()) {
                if (Build.VERSION.SDK_INT < 21) {
                    CompoundButtonCompat.setButtonTintList(termsConditions, ColorStateList.valueOf(getResources().getColor(R.color.colorred)));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
                } else {
                    termsConditions.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorred)));//setButtonTintList is accessible directly on API>19
                }
            }

            if (ValidationHelper.isNullOrEmpty(insuranceCarrier)) {
                ErrorMessage.getInstance().showValidationMessage(this, insuranceCarrierSpinner, getString(R.string.error_empty_fields));
                return;
            } else if (ValidationHelper.isNullOrEmpty(insuranceED)) {
                ErrorMessage.getInstance().showValidationMessage(this, insuranceExpiryDate, getString(R.string.error_empty_fields));
                return;
            } else if (ValidationHelper.isNullOrEmpty(memberShipNum)) {
                ErrorMessage.getInstance().showValidationMessage(this, memberShipNo, getString(R.string.error_empty_fields));
                return;
            } else if (ValidationHelper.isNullOrEmpty(carrierN)) {
                ErrorMessage.getInstance().showValidationMessage(this, carrierNameEdt, getString(R.string.error_empty_fields));
                return;
            } else if (ValidationHelper.isNullOrEmpty(contractN)) {
                ErrorMessage.getInstance().showValidationMessage(this, contractName, getString(R.string.error_empty_fields));
                return;
            } else if (!termsConditions.isChecked()) {
                ErrorMessage.getInstance().showValidationMessage(this, termsConditions, getString(R.string.please_accept_terms_conditions));
                return;
            }
        }

        if (!termsConditions.isChecked()) {
            ErrorMessage.getInstance().showValidationMessage(this, termsConditions, getString(R.string.please_accept_terms_conditions));
            return;
        }

        String nationalIdTypeVal = null;

        if (!ValidationHelper.isNullOrEmpty(nationalIdType)) {
            if (nationalIdType.trim().equalsIgnoreCase(getString(R.string.betaqa).trim())) {
                nationalIdTypeVal = "1";
            } else if (nationalIdType.trim().equalsIgnoreCase(getString(R.string.Iqama).trim())) {
                nationalIdTypeVal = "2";
            } else if (nationalIdType.trim().equalsIgnoreCase(getString(R.string.Passport).trim())) {
                nationalIdTypeVal = "3";
            }
        }

        String nationalityId = String.valueOf(getNationalityId(nationality));
        patientRegistrationDTO.setNationality(nationalityId);
        patientRegistrationDTO.setCountry(nationalityId);
        patientRegistrationDTO.setNationalIdType(nationalIdTypeVal);
        patientRegistrationDTO.setNationalId(nationalyIdNumber);
        patientRegistrationDTO.setNationalIdExpiryDate(expiryDate);
        patientRegistrationDTO.setIdCardScanBase64(nationalCardBase64);
        patientRegistrationDTO.setInsuranceHolder(isInsuranceChecked);

        if (!isInsuranceChecked)
            patientRegistrationDTO.setInsurance(null);
        else {

            String contractIdStr = "";
            String classPlanIdStr = "";
            String carrierIdStr = "";

            if (contractId != 0) {
                contractIdStr = String.valueOf(contractId);
            }

            if (classPlanId != 0) {
                classPlanIdStr = String.valueOf(classPlanId);
            }

            if (carrierId != 0) {
                carrierIdStr = String.valueOf(carrierId);
            }

            insuranceED = LocaleDateHelper.convertDateStringFormat(insuranceED, "MM/dd/yyyy", "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", Locale.ENGLISH);

            insuranceDTO = new InsuranceDTO(contractIdStr, memberShipIdNo, insuranceCardBase64, insuranceED,
                    classPlanIdStr, carrierName, carrierIdStr);

            patientRegistrationDTO.setInsurance(insuranceDTO);
        }

        if (!ValidationHelper.isNullOrEmpty(memberShipIdNo) && isInsuranceChecked) {

            String memberSHipNoStr = memberShipNo.getText().toString().trim();

            if (!ValidationHelper.isNullOrEmpty(memberSHipNoStr)) {
                if (memberSHipNoStr.equalsIgnoreCase(memberShipIdNo)) {
                    validateProfile(patientRegistrationDTO);
                    return;
                } else {
                    ErrorMessage.getInstance().showWarning(this, "Membership Id Number is not validated");
                    return;
                }

            }
        }


        if (str.equalsIgnoreCase(getResources().getString(R.string.Iqama).trim()) ||
                str.equalsIgnoreCase(getResources().getString(R.string.betaqa).trim())) {

            String set = nationalID.getText().toString();

            if (validateTor.isNumeric(set)) {

            } else {
                ErrorMessage.getInstance().showValidationMessage(this, nationalID, getString(R.string.valid_betaqa));
                return;
            }
        }

        validateProfile(patientRegistrationDTO);
    }

    void validateProfile(PatientRegistrationDTO patientRegistrationDTO) {

        if (ConnectionUtil.isInetAvailable(this)) {

            TinyDB tinyDB = new TinyDB(this);

            if (tinyDB != null) {

                String securityToken = tinyDB.getString(TOKEN_KEY);
                String nationalId = nationalID.getText().toString();
                String memberShioNo = null;
                if (isInsuranceChecked)
                    memberShioNo = memberShipNo.getText().toString().trim();

                ProfileValidateDTO profileValidateDTO = new ProfileValidateDTO(securityToken, memberShioNo, nationalId);

                showWaitDialog();

                RestClient.getInstance().profileValidate(profileValidateDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        ProfileValidateModel profileValidateModel = (ProfileValidateModel) result;

                        if (profileValidateModel != null) {

                            MobileOpResult mobileOpResult = profileValidateModel.getMobileOpResult();

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                    if (profileValidateModel.isNationalIdExists()) {
                                        hideWaitDialog();
                                        ErrorMessage.getInstance().showWarning(SecondRegistrationCompleteActivity.this, getString(R.string.national_id_exist));

                                    } else if (profileValidateModel.isMembershipNoExists()) {
                                        hideWaitDialog();
                                        ErrorMessage.getInstance().showWarning(SecondRegistrationCompleteActivity.this, getString(R.string.mem_already_exist));

                                    } else {
                                        createProfile(patientRegistrationDTO, tinyDB);
                                    }

                                } else {
                                    hideWaitDialog();
                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(SecondRegistrationCompleteActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(SecondRegistrationCompleteActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }
                                    ErrorMessage.getInstance().showError(SecondRegistrationCompleteActivity.this, errorMesg);
                                }
                            } else {
                                hideWaitDialog();
                                ErrorMessage.getInstance().showError(SecondRegistrationCompleteActivity.this, errorMessage);
                            }

                        } else {
                            hideWaitDialog();
                            ErrorMessage.getInstance().showError(SecondRegistrationCompleteActivity.this, errorMessage);
                        }
                    }
                });
            }
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    void createProfile(PatientRegistrationDTO patientRegistrationDTO, TinyDB tinyDB) {

        if (tinyDB != null && patientRegistrationDTO != null) {

            String oRegId = tinyDB.getString(Const.OREGID_KEY);
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);

            boolean isTentative = tinyDB.getBoolean(Const.IS_TENT);

            if(!isTentative){
                    ProfileCreateDTO profileCreateDTO = new ProfileCreateDTO(oRegId, securityToken, patientRegistrationDTO);

            RestClient.getInstance().profileCreate(profileCreateDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    ProfileCreateModel profileCreateModel = (ProfileCreateModel) result;

                    if (profileCreateModel != null) {

                        MobileOpResult mobileOpResult = profileCreateModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                // get the data
                                String insuranceId = profileCreateModel.getInsuranceId();
                                String mrnNo = profileCreateModel.getMRN();
                                long patientId = profileCreateModel.getPatientId();
                                String fullName = patientRegistrationDTO.getFirstName() + " " + patientRegistrationDTO.getMiddleName() + " " + patientRegistrationDTO.getLastName();
                                String gender = patientRegistrationDTO.getGender();

                                // save the value
                                tinyDB.putString(Const.INSURANCE_ID, insuranceId);
                                if (!ValidationHelper.isNullOrEmpty(mrnNo))
                                    tinyDB.putString(Const.MRN_NO, mrnNo);
                                else
                                    tinyDB.putString(Const.MRN_NO, "0");

                                tinyDB.putLong(Const.PATIENT_ID, patientId);

                                if (!ValidationHelper.isNullOrEmpty(fullName))
                                    tinyDB.putString(PATIENT_NAME, fullName);

                                if (!ValidationHelper.isNullOrEmpty(patientRegistrationDTO.getGender()))
                                    tinyDB.putString(PATIENT_GENDER, patientRegistrationDTO.getGender());

                                String dob = patientRegistrationDTO.getBirthDate();

                                if (!isAddPatient) {

                                    PatientsMiniModel patientsMiniModel = new PatientsMiniModel();
                                    patientsMiniModel.setNameEn(fullName);
                                    patientsMiniModel.setMRN(mrnNo);
                                    patientsMiniModel.setPatientId(patientId);
                                    patientsMiniModel.setBirthDate(dob);
                                    patientsMiniModel.setGender(gender);

                                    ArrayList<PatientsMiniModel> patientsMiniModels = new ArrayList<>();
                                    patientsMiniModels.add(patientsMiniModel);
                                    Intent intent = new Intent(SecondRegistrationCompleteActivity.this, DashBoardActivity.class);
                                    intent.putExtra(EXISTING_PATIENT, patientsMiniModels);
                                    startActivity(intent);
                                }

                                if (!ValidationHelper.isNullOrEmpty(dob)) {
                                    tinyDB.putString(Const.DATE_BIRTH, dob);
                                }

                                Intent intent1 = new Intent();
                                setResult(RESULT_OK, intent1);
                                finish();
                            } else {
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(SecondRegistrationCompleteActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(SecondRegistrationCompleteActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }
                                ErrorMessage.getInstance().showError(SecondRegistrationCompleteActivity.this, errorMesg);
                            }

                        } else {
                            ErrorMessage.getInstance().showError(SecondRegistrationCompleteActivity.this, errorMessage);
                        }
                    } else {
                        ErrorMessage.getInstance().showError(SecondRegistrationCompleteActivity.this, errorMessage);
                    }
                }
            });
            }else{
                long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);

                //patientRegistrationDTO
                UpdatePatientRegistrationDTO updatePatientRegistrationDTO = new  UpdatePatientRegistrationDTO(patientRegistrationDTO.getCountry(),
                        patientRegistrationDTO.getNationalId(),
                        patientRegistrationDTO.getSalutation(),
                        patientRegistrationDTO.getDistrictRecId(),
                        patientRegistrationDTO.getNationality(),
                        patientRegistrationDTO.getNationalIdExpiryDate(),
                        patientRegistrationDTO.getInsurance(),
                        patientRegistrationDTO.getFirstName(),
                        patientRegistrationDTO.isInsuranceHolder(),
                        patientRegistrationDTO.getMiddleName(),
                        patientRegistrationDTO.getIdCardScanBase64(),
                        patientRegistrationDTO.getGender(),
                        patientRegistrationDTO.getBirthDate(),
                        patientRegistrationDTO.getNationalIdType(),
                        patientRegistrationDTO.getZipCode(),
                        patientRegistrationDTO.getCity(),
                        patientRegistrationDTO.getLastName(),
                        "",
                        String.valueOf(patientId),
                        false);

                ProfileUpdateDTO profileUpdateDTO = new ProfileUpdateDTO(oRegId, securityToken, updatePatientRegistrationDTO);

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
                                        Intent intent1 = new Intent();
                                        setResult(RESULT_OK, intent1);
                                        finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(SecondRegistrationCompleteActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(SecondRegistrationCompleteActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(SecondRegistrationCompleteActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";

                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }
                                    ErrorMessage.getInstance().showError(SecondRegistrationCompleteActivity.this, errorMesg);
                                }

                            } else {
                                ErrorMessage.getInstance().showError(SecondRegistrationCompleteActivity.this, errorMessage);
                            }
                        } else {
                            ErrorMessage.getInstance().showError(SecondRegistrationCompleteActivity.this, errorMessage);
                        }
                    }
                });
            }
        }
    }

    ArrayList<InsuranceCarriers> insuranceCarriers;

    ArrayList<Nationalities> nationalities = null;

    void getIntentValue() {

        Intent intent = getIntent();
        if (intent != null) {

            nationalities = intent.getParcelableArrayListExtra("nationalities");
            ArrayList<String> nationalityList = new ArrayList<>();

            if (!ValidationHelper.isNullOrEmpty(nationalities)) {

                for (Nationalities cities : nationalities) {
                    String city = "";
                    if (TextUtil.getEnglish(SecondRegistrationCompleteActivity.this))
                        city = cities.getNameEn();
                    else if (TextUtil.getArabic(SecondRegistrationCompleteActivity.this))
                        city = cities.getNameAr();

                    nationalityList.add(city);
                }
                android.widget.ListAdapter cityAdapter = new SpinnerCustomAdapter(this, R.layout.spinner_item_list, nationalityList);
                nationalitySpinner.setAdapter(cityAdapter);
            }

            insuranceCarriers = intent.getParcelableArrayListExtra("insurance");
            ArrayList<String> insuranceList = new ArrayList<>();

            if (!ValidationHelper.isNullOrEmpty(insuranceCarriers)) {

                //التعاونية للتأمين

                for (InsuranceCarriers insuranceCarriers1 : insuranceCarriers) {
                    String insuranceName = "";
                    if (TextUtil.getEnglish(SecondRegistrationCompleteActivity.this))
                        insuranceName = insuranceCarriers1.getNameEn();
                    else if (TextUtil.getArabic(SecondRegistrationCompleteActivity.this))
                        insuranceName = insuranceCarriers1.getNameAr();
//5637169326
                    insuranceList.add(insuranceName);
                }

                android.widget.ListAdapter insuranceAdapter = new SpinnerCustomAdapter(this, R.layout.spinner_item_list, insuranceList);
                insuranceCarrierSpinner.setAdapter(insuranceAdapter);
            }

            patientRegistrationDTO = intent.getParcelableExtra("patientRegistrationDTO");
            if (patientRegistrationDTO != null) {

            }
        }
    }

    @OnClick(R.id.expiryDateEdt1)
    void expiryDate() {
        expiryDateEdt_til.requestFocus();
        isInsuranceDate = false;
        DatePickerDialog datePicker = new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        datePicker.setCanceledOnTouchOutside(false);
        datePicker.show();
    }

    @OnClick(R.id.insuranceExpiryDate)
    void insuranceExpiryDate() {
        isInsuranceDate = true;
        DatePickerDialog datePicker = new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        datePicker.show();
    }

    boolean isInsuranceCard = false;

    @OnClick(R.id.nationalIdImg)
    void imgNational() {
        isInsuranceCard = false;
        nationalImage();
    }

    void nationalImage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            boolean writeGranted = PermissionUtils.isGranted(this, PermissionEnum.WRITE_EXTERNAL_STORAGE);
            boolean readGranted = PermissionUtils.isGranted(this, PermissionEnum.READ_EXTERNAL_STORAGE);
            boolean cameraGranted = PermissionUtils.isGranted(this, PermissionEnum.CAMERA);
            if (!writeGranted || !readGranted || !cameraGranted) {
                TedPermission.with(this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.CAMERA)
                        .check();
            } else {
                SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_BOTH, this);
            }
        } else {
            SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_BOTH, this);
        }
    }

    @OnClick(R.id.insuranceCardImg)
    void imgInsurance() {
        isInsuranceCard = true;
        nationalImage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SimpleImageSelect.ClearConfig(this);
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

            try {
                SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_BOTH, SecondRegistrationCompleteActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(SecondRegistrationCompleteActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            String path = SimpleImageSelect.onActivityResult(this, requestCode, resultCode, data);
            if (!ValidationHelper.isNullOrEmpty(path)) {

                if (!isInsuranceCard) {
                    Uri uri = Uri.parse(path);
                    nationalIdImg.setImageURI(uri);

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                                //    Bitmap bitmap = encodeImage(path);
                                if (bitmap != null)
                                    nationalCardBase64 = encodeTobase64(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                                Bitmap bitmap = encodeImage(path);
                                if (bitmap != null)
                                    nationalCardBase64 = encodeTobase64(bitmap);

                            }
                        }
                    });

                } else {
                    Uri uri = Uri.parse(path);
                    insuranceCardImg.setImageURI(uri);

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                if (bitmap != null)
                                    insuranceCardBase64 = encodeTobase64(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                                Bitmap bitmap = encodeImage(path);
                                if (bitmap != null)
                                    insuranceCardBase64 = encodeTobase64(bitmap);
                            }
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap encodeImage(String path) {
        File file = new File(path);
        Bitmap myBitmap = null;
        if (file.exists()) {
            myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        return myBitmap;
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.NO_WRAP | Base64.DEFAULT);
        return imageEncoded;
    }

    void validateInsurance() {

        insuranceCarrierSpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (allFieldsCheck()) {
                        callProfileInsuranceValidate();
                    }
                }
            }
        });

        policyNoEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (allFieldsCheck()) {
                        callProfileInsuranceValidate();
                    }
                }
            }
        });

        memberShipNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (allFieldsCheck()) {
                        callProfileInsuranceValidate();
                    }
                }
            }
        });

       /* insuranceCarrierSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String tawuniya = getResources().getString(R.string.tawnuyia);

                if (s.toString().equalsIgnoreCase(tawuniya.trim())) {
                    insuranceExpiryDate.setEnabled(true);
                    insuranceExpiryDate_til.setBackground(getResources().getDrawable(R.drawable.grey_border));
                } else {
                    insuranceExpiryDate.setEnabled(false);
                    insuranceExpiryDate_til.setBackground(getResources().getDrawable(R.drawable.grey_non_editable));
                }

            }
        });*/

    }

    private void callProfileInsuranceValidate() {

        TinyDB tinyDB = new TinyDB(SecondRegistrationCompleteActivity.this);

        if (tinyDB != null && ConnectionUtil.isInetAvailable(this)) {

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            String insuranceCarrierTypeVal = String.valueOf(insuranceCarrierType);
            String policyNo = policyNoEdt.getText().toString();
            String memberIdNumber = memberShipNo.getText().toString();

            ProfileInsuranceValidateDTO profileInsuranceValidateDTO = new ProfileInsuranceValidateDTO(securityToken,
                    insuranceCarrierTypeVal, memberIdNumber, policyNo);
            showWaitDialog();

            RestClient.getInstance().profileInsuranceValidate(profileInsuranceValidateDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {
                    hideWaitDialog();

                    ProfileInsuranceValidateModel profileInsuranceValidateModel = (ProfileInsuranceValidateModel) result;

                    if (profileInsuranceValidateModel != null) {

                        MobileOpResult mobileOpResult = profileInsuranceValidateModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                InsuranceModel insuranceModel = profileInsuranceValidateModel.getInsuranceModel();

                                if (insuranceModel != null) {

                                    contractId = insuranceModel.getContractId();
                                    memberShipIdNo = insuranceModel.getMembershipNo();
                                    classPlanId = insuranceModel.getClassPlanId();
                                    carrierName = insuranceModel.getClassPlan();     //insuranceCarrierSpinner.getText().toString();
                                    carrierId = insuranceModel.getCarrierId();
                                    String contractNameVal = insuranceModel.getContractName();

                                    if (!ValidationHelper.isNullOrEmpty(carrierName)) {
                                        carrierNameEdt.setText(carrierName);
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(contractNameVal)) {
                                        contractName.setText(contractNameVal);
                                    }

                                    String insuranceExpiryDate1 = insuranceModel.getExpiryDate();

                                    if (!ValidationHelper.isNullOrEmpty(insuranceExpiryDate1)) {

                                        isInsuranceDate = true;
                                        insuranceExpiryDate1 = LocaleDateHelper.convertDateStringFormat(insuranceExpiryDate1, "yyyy-MM-dd'T'hh:mm:ss", "MM/dd/yyyy");
                                        insuranceExpiryDate.setText(insuranceExpiryDate1);

                                        boolean isExpired = LocaleDateHelper.expiredDate(insuranceExpiryDate1);

                                        if (isExpired) {

                                            String tawuniya = getResources().getString(R.string.tawnuyia_en);

                                            if (carrierName.toString().equalsIgnoreCase(tawuniya.trim())) {
                                                insuranceExpiryDate.setEnabled(true);
                                                insuranceExpiryDate_til.setBackground(getResources().getDrawable(R.drawable.grey_border));
                                                insuranceExpiryDate.setText("");
                                            } else {
                                                insuranceExpiryDate.setEnabled(false);
                                                insuranceExpiryDate_til.setBackground(getResources().getDrawable(R.drawable.grey_non_editable));
                                            }
                                        } else {
                                            insuranceExpiryDate.setEnabled(false);
                                            insuranceExpiryDate_til.setBackground(getResources().getDrawable(R.drawable.grey_non_editable));
                                        }
                                    }
                                }
                            } else {
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(SecondRegistrationCompleteActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(SecondRegistrationCompleteActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(SecondRegistrationCompleteActivity.this, errorMesg);
                            }
                        } else
                            ErrorMessage.getInstance().showError(SecondRegistrationCompleteActivity.this, errorMessage);
                    } else
                        ErrorMessage.getInstance().showError(SecondRegistrationCompleteActivity.this, errorMessage);
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, "Internet is not available");
        }
    }

    boolean allFieldsCheck() {
        boolean flag = false;

        String insuranceCarrier = insuranceCarrierSpinner.getText().toString();
        String policyNo = policyNoEdt.getText().toString();
        String memberIdNumber = memberShipNo.getText().toString();

        if (!ValidationHelper.isNullOrEmpty(insuranceCarrier) && !ValidationHelper.isNullOrEmpty(policyNo) &&
                !ValidationHelper.isNullOrEmpty(memberIdNumber)) {
            flag = true;
        } else {
            flag = false;
        }

        return flag;
    }

    long getNationalityId(String countryName) {

        long nationalityId = 0;

        if (!ValidationHelper.isNullOrEmpty(nationalities)) {

            for (Nationalities nationalities : nationalities) {
                String nationalitiesNameEn = nationalities.getNameEn();
                if (TextUtil.getEnglish(this))
                    nationalitiesNameEn = nationalities.getNameEn();
                else if (TextUtil.getArabic(this))
                    nationalitiesNameEn = nationalities.getNameAr();

                if (nationalitiesNameEn.trim().equalsIgnoreCase(countryName.trim())) {
                    nationalityId = nationalities.getId();
                }
            }
        }
        return nationalityId;
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.nationalIdType)
    void nationalIdType() {
        if (ValidationHelper.isNullOrEmpty(nationalID.getText().toString()))
            ErrorMessage.getInstance().showValidationMessage(this, nationalID, getString(R.string.please_select_national_id));
    }

    private String blockCharacterSet = "0123456789";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                if (nationalID.getText().toString().length() == 10)
                    return "";
                return null;
            } else {
                return "";
            }
        }
    };

    private String blockCharacterSet1 = "0123456789ABCDEFGHIJKLMNOPQRSTUVYXYZabcdefghijklmnopqrstuvwxyz";

    private InputFilter filter1 = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet1.contains(("" + source))) {
                if (nationalID.getText().toString().length() == 10)
                    return "";
                return null;
            } else {
                return "";
            }
        }
    };

    public void tILError(Activity activity, TextInputLayout textInputLayout, AppCompatEditText text) {
        CharSequence str = textInputLayout.getHint();
        if (ValidationHelper.isNullOrEmpty(str))
            str = text.getHint();
        textInputLayout.setBackground(activity.getDrawable(R.drawable.red_border));
        textInputLayout.setHintTextAppearance(R.style.redHint);
        text.setHintTextColor(activity.getResources().getColor(R.color.colorred));
        text.setHint(str);
        if (text.hasFocus()) {
            textInputLayout.setHint(str);
            text.setHint("");
        } else {
            textInputLayout.setHint("");
            text.setHint(str);
        }

        CharSequence finalStr = str;
        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textInputLayout.setHint(finalStr);
                    text.setHint("");
                }
            }
        });

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputLayout.setBackground(activity.getDrawable(R.drawable.grey_border));
                textInputLayout.setHintTextAppearance(R.style.primaryHint);

                if (memberShipNo.hasFocus()) {
                    text.setOnFocusChangeListener(null);
                    validateInsurance();
                }
            }
        });
    }

    void transformView() {
        if (TextUtil.getArabic(this)) {
            toolbar_left_iv.setRotation(180);
            nationalitySpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);
            nationalIdTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);
            submit_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            expiryDateEdt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0);
            insuranceExpiryDate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0);

            national_id_attach.setCompoundDrawablesWithIntrinsicBounds(R.drawable.attachment, 0, 0, 0);
            insurance_id_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.attachment, 0, 0, 0);
            insuranceCarrierSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);

        }
    }


}
