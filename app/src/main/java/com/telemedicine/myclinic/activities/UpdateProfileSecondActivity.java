package com.telemedicine.myclinic.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.transition.TransitionManager;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;
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
import com.telemedicine.myclinic.models.profileInsuranceValidate.InsuranceModel;
import com.telemedicine.myclinic.models.profileInsuranceValidate.ProfileInsuranceValidateDTO;
import com.telemedicine.myclinic.models.profileInsuranceValidate.ProfileInsuranceValidateModel;
import com.telemedicine.myclinic.models.profileUpdate.InsuranceUpdateModel;
import com.telemedicine.myclinic.models.profileUpdate.PatientRegistrationUpdate;
import com.telemedicine.myclinic.models.profileUpdate.ProfileUpdateDTO;
import com.telemedicine.myclinic.models.profileUpdate.UpdatePatientRegistrationDTO;
import com.telemedicine.myclinic.models.profilecreatoinmodels.InsuranceCarriers;
import com.telemedicine.myclinic.models.profilecreatoinmodels.InsuranceDTO;
import com.telemedicine.myclinic.models.profilecreatoinmodels.Nationalities;
import com.telemedicine.myclinic.models.profilecreatoinmodels.ProfileCreateModel;
import com.telemedicine.myclinic.models.profilevalidatemodels.ProfileValidateDTO;
import com.telemedicine.myclinic.models.profilevalidatemodels.ProfileValidateModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.AppointmentEvent;
import com.telemedicine.myclinic.util.ConnectionUtil;
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

import static com.telemedicine.myclinic.util.Const.INSURANCE_IMAGES;
import static com.telemedicine.myclinic.util.Const.PATIENT_UPDATE_KEY;
import static com.telemedicine.myclinic.util.Const.TOKEN_KEY;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class UpdateProfileSecondActivity extends BaseActivity {


    @BindView(R.id.insurance_container_fields)
    LinearLayout insuranceContainer;

    @BindView(R.id.nationality)
    LightSpinnerEdittext nationalitySpinner;

    @BindView(R.id.nationalIdType)
    LightSpinnerEdittext nationalIdTypeSpinner;

    @BindView(R.id.nationalID)
    LightSpinnerEdittext nationalID;

    @BindView(R.id.nationalIDTIL)
    BoldTextInputLayout nationalIDTIL;

    @BindView(R.id.expiryDateEdt)
    LightEdittext expiryDateEdt;

    @BindView(R.id.insurance_carrier_spiiner)
    LightSpinnerEdittext insuranceCarrierSpinner;

    @BindView(R.id.insurance_carrier_spiiner_til)
    BoldTextInputLayout insurance_carrier_spiiner_til;

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

    @BindView(R.id.national_id_container)
    RelativeLayout nationalIdContainer;

    @BindView(R.id.insurance_image)
    ImageView insuranceImage;

    @BindView(R.id.insurance_add_container)
    RelativeLayout insuranceAddContainer;

    @BindView(R.id.insurance_id_container)
    RelativeLayout insuranceIdContainer;

    @BindView(R.id.insurance_card_container)
    RelativeLayout insuranceCardContainer;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @BindView(R.id.national_id_attach)
    TextView national_id_attach;

    @BindView(R.id.insurance_id_icon)
    TextView insurance_id_icon;

    @BindView(R.id.submit_btn)
    LightButton submit_btn;

    @BindView(R.id.insuranceExpiryDate_til)
    BoldTextInputLayout insuranceExpiryDate_til;

    @BindView(R.id.container)
    LinearLayout containerLayout;

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

    UpdatePatientRegistrationDTO patientRegistrationDTO = null;
    PatientRegistrationUpdate patientRegistrationUpdate = null;
    InsuranceDTO insuranceDTO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        getIntentValue();
    }

    private void init() {
        transformView();

        // it converts the item in the list to a string shown in EditText.
        nationalitySpinner.setItemConverter(new EditSpinner.ItemConverter() {
            @Override
            public String convertItemToString(Object selectedItem) {

                String country = (String) selectedItem.toString();

                String countryKSA = getString(R.string.KSA).trim();

                String[] arr;

                if (country.equalsIgnoreCase(countryKSA)) {
                    arr = getResources().getStringArray(R.array.Betaqa);
                    nationalIdTypeSpinner.setText(R.string.betaqa);
                    nationalIDTIL.setHint(getResources().getString(R.string.betaqa_no));
                    // nationalIDTIL.setHint(nationalID.getHint());
                    nationalIdTypeSpinner.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                } else {
                    arr = getResources().getStringArray(R.array.nationalId);
                    if (nationalIdTypeSpinner.getText().toString().equalsIgnoreCase(getString(R.string.betaqa)))
                        nationalIdTypeSpinner.setText("");

                    nationalIDTIL.setHint(getResources().getString(R.string.national_id1));
                }

                ArrayList<String> nationaId = new ArrayList<String>(Arrays.asList(arr)); //new ArrayList is only needed if you absolutely need an ArrayList

                if (!ValidationHelper.isNullOrEmpty(nationaId)) {
                    android.widget.ListAdapter salutationAdapter = new SpinnerCustomAdapter(UpdateProfileSecondActivity.this, R.layout.spinner_item_list, nationaId);
                    nationalIdTypeSpinner.setAdapter(salutationAdapter);
                }

                return country;
            }
        });

        nationalIdTypeSpinner.setItemConverter(new EditSpinner.ItemConverter() {
            @Override
            public String convertItemToString(Object selectedItem) {

                String str = (String) selectedItem.toString().trim();

                if (str.equalsIgnoreCase(getResources().getString(R.string.Iqama).trim()) ||
                        str.equalsIgnoreCase(getResources().getString(R.string.betaqa).trim())) {
                    nationalID.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else {
                    nationalID.setInputType(InputType.TYPE_CLASS_TEXT);
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
                        if (TextUtil.getEnglish(UpdateProfileSecondActivity.this)) {
                            insuranceName = insuranceCarriers1.getNameEn();
                        } else if (TextUtil.getArabic(UpdateProfileSecondActivity.this)) {
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
        validateInsurance();
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
        return R.layout.activity_second_update_profile;
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
                ErrorMessage.getInstance().showSuccessGreen(UpdateProfileSecondActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameAr() + ". "+getString(R.string.please_press_to_continue));
            }else{
                ErrorMessage.getInstance().showSuccessGreen(UpdateProfileSecondActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameEn() + ". "+getString(R.string.please_press_to_continue));
            }
        } else {
            ErrorMessage.getInstance().showErrorYellow(UpdateProfileSecondActivity.this, appointmentEvent.getErrorMSg());
        }
    }

    boolean isInsuranceChecked = false;

    void getRadioValues() {


    }

    public void Update(View view) {

        ValidateTor validateTor = new ValidateTor();

        String nationality = nationalitySpinner.getText().toString();
        String nationalIdType = nationalIdTypeSpinner.getText().toString();
        String nationalTypeId = nationalIdTypeSpinner.getText().toString();
        String nationalyIdNumber = nationalID.getText().toString();
        String expiryDate = expiryDateEdt.getText().toString();
        String insuranceCarrier = insuranceCarrierSpinner.getText().toString();
        String memberShipNum = memberShipNo.getText().toString();
        String insuranceED = insuranceExpiryDate.getText().toString();
        String carrierN = carrierNameEdt.getText().toString();
        String contractN = contractName.getText().toString();
        String policyNo = policyNoEdt.getText().toString();

        if (ValidationHelper.isNullOrEmpty(nationality)) {
            ErrorMessage.getInstance().showValidationMessage(this, nationalitySpinner, getString(R.string.select_country));
            return;
        } else if (ValidationHelper.isNullOrEmpty(nationalTypeId)) {
            ErrorMessage.getInstance().showValidationMessage(this, nationalIdTypeSpinner, getString(R.string.please_select_national_id));
            return;
        } else if (ValidationHelper.isNullOrEmpty(nationalyIdNumber)) {
            ErrorMessage.getInstance().showValidationMessage(this, nationalID, getString(R.string.please_enter_national_id_no));
            return;
        } else if (nationalIdType.equalsIgnoreCase(getString(R.string.betaqa))) {
            if (validateTor.isAtleastLength(nationalyIdNumber, 10)) {

            } else {
                ErrorMessage.getInstance().showValidationMessage(this, nationalID, getString(R.string.valid_betaqa));
                return;
            }
        }

        if (ValidationHelper.isNullOrEmpty(expiryDate)) {
            ErrorMessage.getInstance().showValidationMessage(this, expiryDateEdt, getString(R.string.required));
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
            }
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

        patientRegistrationDTO.setNationality(String.valueOf(getNationalityId(nationality)));
        patientRegistrationDTO.setCountry(String.valueOf(getNationalityId(nationality)));
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

        // these params are used in updation.
        String mrn = patientRegistrationUpdate.getMRN();
        long patientRecId = patientRegistrationUpdate.getPatientRecId();

        if (!ValidationHelper.isNullOrEmpty(mrn))
            patientRegistrationDTO.setMRN(mrn);

        if (patientRecId != 0)
            patientRegistrationDTO.setPatientRecId(String.valueOf(patientRecId));

        boolean isTentativePatient = patientRegistrationUpdate.isTentativePatient();

        patientRegistrationDTO.setTentativePatient(isTentativePatient);

        // if the insurance is added then call the insurance validate service here
        if (!ValidationHelper.isNullOrEmpty(memberShipIdNo) && isInsuranceChecked) {

            String memberSHipNoStr = memberShipNo.getText().toString().trim();

            if (!ValidationHelper.isNullOrEmpty(memberSHipNoStr)) {
                if (memberSHipNoStr.equalsIgnoreCase(memberShipIdNo)) {
                    validateProfile(patientRegistrationDTO,false);
                    return;
                } else {
                    ErrorMessage.getInstance().showWarning(this, "Membership Id Number is not validated");
                    return;
                }
            }
        }

        // if the national id number is same so no need to call the validate service.
        if (nationalyIdNumber.equalsIgnoreCase(patientRegistrationUpdate.getNationalId()) && patientRegistrationUpdate.getInsurance() != null) {
            TinyDB tinyDB = new TinyDB(this);
            updateProfile(patientRegistrationDTO, tinyDB, true);
        } else if (nationalyIdNumber.equalsIgnoreCase(patientRegistrationUpdate.getNationalId()) && !isInsuranceChecked) {
            TinyDB tinyDB = new TinyDB(this);
            updateProfile(patientRegistrationDTO, tinyDB, true);

        } else {
            validateProfile(patientRegistrationDTO,true);
        }

    }

    void validateProfile(UpdatePatientRegistrationDTO patientRegistrationDTO,boolean shouldCheckNationalID) {

        if (ConnectionUtil.isInetAvailable(this)) {

            TinyDB tinyDB = new TinyDB(this);

            if (tinyDB != null) {

                String securityToken = tinyDB.getString(TOKEN_KEY);
                String nationalId =  (shouldCheckNationalID)? nationalID.getText().toString() : "";
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

                                        String nationalyIdNumber = nationalID.getText().toString();
                                        if (nationalyIdNumber.equalsIgnoreCase(patientRegistrationUpdate.getNationalId())) {
                                            // ignore it
                                        } else {
                                            hideWaitDialog();
                                            ErrorMessage.getInstance().showWarning(UpdateProfileSecondActivity.this, getString(R.string.national_id_exist));
                                            return;
                                        }
                                    }


                                    if (profileValidateModel.isMembershipNoExists()) {

                                        if (patientRegistrationUpdate != null && patientRegistrationUpdate.getInsurance() != null) {
                                            // ignore it
                                        } else {
                                            hideWaitDialog();
                                            ErrorMessage.getInstance().showWarning(UpdateProfileSecondActivity.this, getString(R.string.mem_already_exist));
                                            return;
                                        }

                                    }

                                    // then update the profile
                                    updateProfile(patientRegistrationDTO, tinyDB, false);


                                } else {
                                    hideWaitDialog();
                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(UpdateProfileSecondActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(UpdateProfileSecondActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";

                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }
                                    ErrorMessage.getInstance().showError(UpdateProfileSecondActivity.this, errorMesg);
                                }
                            } else {
                                hideWaitDialog();
                                ErrorMessage.getInstance().showError(UpdateProfileSecondActivity.this, errorMessage);
                            }

                        } else {
                            hideWaitDialog();
                            ErrorMessage.getInstance().showError(UpdateProfileSecondActivity.this, errorMessage);
                        }
                    }
                });
            }
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
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
                                    Intent intent1 = new Intent();
                                    setResult(RESULT_OK, intent1);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(UpdateProfileSecondActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(UpdateProfileSecondActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(UpdateProfileSecondActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";

                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }
                                ErrorMessage.getInstance().showError(UpdateProfileSecondActivity.this, errorMesg);
                            }

                        } else {
                            ErrorMessage.getInstance().showError(UpdateProfileSecondActivity.this, errorMessage);
                        }
                    } else {
                        ErrorMessage.getInstance().showError(UpdateProfileSecondActivity.this, errorMessage);
                    }
                }
            });
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
                    if (TextUtil.getEnglish(UpdateProfileSecondActivity.this))
                        city = cities.getNameEn();
                    else if (TextUtil.getArabic(UpdateProfileSecondActivity.this))
                        city = cities.getNameAr();

                    nationalityList.add(city);
                }
                android.widget.ListAdapter cityAdapter = new SpinnerCustomAdapter(this, R.layout.spinner_item_list, nationalityList);
                nationalitySpinner.setAdapter(cityAdapter);
            }

            insuranceCarriers = intent.getParcelableArrayListExtra("insurance");
            ArrayList<String> insuranceList = new ArrayList<>();

            if (!ValidationHelper.isNullOrEmpty(insuranceCarriers)) {

                for (InsuranceCarriers insuranceCarriers1 : insuranceCarriers) {
                    String insuranceName = "";
                    if (TextUtil.getEnglish(this))
                        insuranceName = insuranceCarriers1.getNameEn();
                    else
                        insuranceName = insuranceCarriers1.getNameAr();
                    insuranceList.add(insuranceName);
                }

                android.widget.ListAdapter insuranceAdapter = new SpinnerCustomAdapter(this, R.layout.spinner_item_list, insuranceList);
                insuranceCarrierSpinner.setAdapter(insuranceAdapter);
            }

            patientRegistrationDTO = intent.getParcelableExtra("patientRegistrationDTO");
            patientRegistrationUpdate = intent.getParcelableExtra(PATIENT_UPDATE_KEY);
            if (patientRegistrationUpdate != null) {
                fillTheForm(patientRegistrationUpdate);
            }
        }
    }

    void fillTheForm(PatientRegistrationUpdate patientRegistrationUpdate) {

        String nationality = patientRegistrationUpdate.getNationality();

        String nationalId = patientRegistrationUpdate.getNationalId();

        int nationalIdType = patientRegistrationUpdate.getNationalIdType();

        String expiryDate = patientRegistrationUpdate.getNationalIdExpiryDate();

        if (!ValidationHelper.isNullOrEmpty(nationality)) {

            nationality = getNationality(Long.valueOf(nationality));

            if (!ValidationHelper.isNullOrEmpty(nationality))
                nationalitySpinner.setText(nationality);
        }

        String nationalIdTypeVal = null;

        if (nationalIdType == 1) {
            nationalIdTypeVal = getString(R.string.betaqa);

        } else if (nationalIdType == 2) {
            nationalIdTypeVal = getString(R.string.Iqama);

        } else if (nationalIdType == 3) {
            nationalIdTypeVal = getString(R.string.Passport);

        }

        if (!ValidationHelper.isNullOrEmpty(nationalId)) {
            nationalID.setText(nationalId);
        }

        if (!ValidationHelper.isNullOrEmpty(nationalIdTypeVal)) {
            nationalIdTypeSpinner.setText(nationalIdTypeVal);
        }

        if (!ValidationHelper.isNullOrEmpty(expiryDate)) {
            String expiryDateVal = LocaleDateHelper.convertDateStringFormat(expiryDate, "yyyy-MM-dd'T'hh:mm:ss", "MM/dd/yyyy");
            expiryDateEdt.setText(expiryDateVal);
        }

        String[] arr;
        String countryKSA = getString(R.string.KSA);

        if (nationality.equalsIgnoreCase(countryKSA)) {
            arr = getResources().getStringArray(R.array.Betaqa);
            nationalIdTypeSpinner.setText(R.string.betaqa);
            nationalIDTIL.setHint(getResources().getString(R.string.betaqa_no));
            nationalIdTypeSpinner.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            nationalID.setInputType(InputType.TYPE_CLASS_NUMBER);

        } else {
            arr = getResources().getStringArray(R.array.nationalId);
            nationalIDTIL.setHint(getResources().getString(R.string.national_id1));
            nationalID.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        String str = nationalIdTypeSpinner.getText().toString();

        if (str.equalsIgnoreCase(getResources().getString(R.string.Iqama).trim()) ||
                str.equalsIgnoreCase(getResources().getString(R.string.betaqa).trim())) {
            nationalID.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            nationalID.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nationalID.getWindowToken(), 0);

        ArrayList<String> nationaId = new ArrayList<String>(Arrays.asList(arr)); //new ArrayList is only needed if you absolutely need an ArrayList

        if (!ValidationHelper.isNullOrEmpty(nationaId)) {
            android.widget.ListAdapter salutationAdapter = new SpinnerCustomAdapter(UpdateProfileSecondActivity.this, R.layout.spinner_item_list, nationaId);
            nationalIdTypeSpinner.setAdapter(salutationAdapter);
        }

        String nationalIdPhoto = "";//patientRegistrationUpdate.getIdCardScanBase64();

        if (!ValidationHelper.isNullOrEmpty(nationalIdPhoto)) {
            nationalIdContainer.setVisibility(View.VISIBLE);
            nationalIdImg.setImageBitmap(bitmap(nationalIdPhoto));
            // show the photo here , and click to display bigger image
        } else
            nationalIdContainer.setVisibility(View.GONE);

        // check for insurance added or not
        if (patientRegistrationUpdate.getInsurance() == null) {
            insuranceAddContainer.setVisibility(View.VISIBLE);
            insuranceContainer.setVisibility(View.GONE);
            insuranceCardContainer.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        } else {
            insuranceAddContainer.setVisibility(View.GONE);
            insuranceContainer.setVisibility(View.VISIBLE);
            fillTheInsurance(patientRegistrationUpdate.getInsurance());
        }

       /* if (patientRegistrationUpdate.getInsurance() == null)
            insuranceCarrierSpinner.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    String tawuniya = getResources().getString(R.string.tawnuyia_en);

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

    void fillTheInsurance(InsuranceUpdateModel insuranceUpdateModel) {

        String insuranceCarrier = insuranceUpdateModel.getCarrierName();
        String classPlan = insuranceUpdateModel.getClassPlan();
        String exDate = insuranceUpdateModel.getExpiryDate();
        String memberShipNo = insuranceUpdateModel.getMembershipNo();
        String contractName = insuranceUpdateModel.getContractName();
        String contractNo = insuranceUpdateModel.getContractNo();
        String insuranceCard = INSURANCE_IMAGES;//insuranceUpdateModel.getInsuranceCardScanBase64();

        isInsuranceChecked = true;
        contractId = insuranceUpdateModel.getContractId();
        classPlanId = insuranceUpdateModel.getClassPlanId();
        carrierId = insuranceUpdateModel.getCarrierId();


        if (TextUtil.getArabic(this)) {
            if (!ValidationHelper.isNullOrEmpty(insuranceCarrier)) {

                if (!ValidationHelper.isNullOrEmpty(insuranceCarriers)) {
                    for (InsuranceCarriers insuranceCarriere : insuranceCarriers) {

                        if (insuranceCarriere.getId() == insuranceUpdateModel.getCarrierId()) {
                            insuranceCarrier = insuranceCarriere.getNameAr();
                        }
                    }

                }
            }
        }

        insuranceCarrierSpinner.setText(insuranceCarrier);

        if (!ValidationHelper.isNullOrEmpty(exDate)) {
            exDate = LocaleDateHelper.convertDateStringFormat(exDate, "yyyy-MM-dd'T'hh:mm:ss", "MM/dd/yyyy");
            if (!ValidationHelper.isNullOrEmpty(exDate))
                insuranceExpiryDate.setText(exDate);
        }

        if (!ValidationHelper.isNullOrEmpty(contractNo)) {
            this.policyNoEdt.setText(String.valueOf(contractNo));
        }

        if (!ValidationHelper.isNullOrEmpty(memberShipNo))
            this.memberShipNo.setText(memberShipNo);

        if (!ValidationHelper.isNullOrEmpty(contractName))
            this.contractName.setText(contractName);

        if (!ValidationHelper.isNullOrEmpty(classPlan))
            this.carrierNameEdt.setText(classPlan);

        insuranceCarrierSpinner.setEnabled(false);
        insuranceCarrierSpinner.setAdapter(null);
        insurance_carrier_spiiner_til.setBackground(getResources().getDrawable(R.drawable.grey_non_editable));


        if (insuranceCarrierSpinner.getText().toString().trim().equalsIgnoreCase(String.valueOf(R.string.tawnuyia)))
            insuranceExpiryDate.setEnabled(true);
        else {
            insuranceExpiryDate.setEnabled(false);
            insuranceExpiryDate.setBackground(getResources().getDrawable(R.drawable.grey_non_editable));
        }

        policyNoEdt.setEnabled(false);
        policyNoEdt_til.setBackground(getResources().getDrawable(R.drawable.grey_non_editable));

        this.memberShipNo.setEnabled(false);
        member_ship_no_til.setBackground(getResources().getDrawable(R.drawable.grey_non_editable));

        this.contractName.setEnabled(false);
        this.contractName.setBackground(getResources().getDrawable(R.drawable.grey_non_editable));

        this.carrierNameEdt.setEnabled(false);
        this.carrierNameEdt.setBackground(getResources().getDrawable(R.drawable.grey_non_editable));
        if (!ValidationHelper.isNullOrEmpty(insuranceCard)) {
            insuranceIdContainer.setVisibility(View.VISIBLE);
            insuranceCardImg.setImageBitmap(bitmap(insuranceCard));
            insuranceCardImg.setEnabled(false);
        } else {
            insuranceIdContainer.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.expiryDateEdt)
    void expiryDate() {
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
        datePicker.setCanceledOnTouchOutside(false);
        datePicker.show();
    }

    @OnClick(R.id.insurance_image)
    void insuranceAdd() {
        TransitionManager.beginDelayedTransition(containerLayout);
        if (isInsuranceChecked) {
            isInsuranceChecked = false;
            insuranceImage.setImageDrawable(getResources().getDrawable(R.drawable.plus_box_outline));
            insuranceContainer.setVisibility(View.GONE);
        } else {
            isInsuranceChecked = true;
            insuranceImage.setImageDrawable(getResources().getDrawable(R.drawable.minus_box_outline));
            insuranceContainer.setVisibility(View.VISIBLE);
        }
    }

    boolean isInsuranceCard = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SimpleImageSelect.ClearConfig(this);
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

            try {
               // SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_BOTH, UpdateProfileSecondActivity.this);

                ImagePicker.with(UpdateProfileSecondActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(UpdateProfileSecondActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };
    String path = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /* try {*/
         /*   path = SimpleImageSelect.onActivityResult(this, requestCode, resultCode, data);
            if (!ValidationHelper.isNullOrEmpty(path)) {
                Uri uri = Uri.parse(path);
                insuranceCardImg.setImageURI(uri);
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            if (bitmap != null)
                                insuranceCardBase64 = encodeTobase64(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                            bitmap = encodeImage(path);
                            if (bitmap != null)
                                insuranceCardBase64 = encodeTobase64(bitmap);
                        }

                    }
                });
            }*/

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                Uri uri =data.getData();
                insuranceCardImg.setImageURI(uri);
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            if (bitmap != null)
                                insuranceCardBase64 = encodeTobase64(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                            bitmap = encodeImage(data.getData().getPath());
                            if (bitmap != null)
                                insuranceCardBase64 = encodeTobase64(bitmap);
                        }

                    }
                });

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
        /* catch (IOException e) {
            e.printStackTrace();
            Bitmap bitmap = encodeImage(data.getData().getPath());
            if (bitmap != null)
                insuranceCardBase64 = encodeTobase64(bitmap);
        }*/
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
        immagex.compress(Bitmap.CompressFormat.JPEG, 40, baos);
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
                        if (patientRegistrationUpdate.getInsurance() == null)
                            callProfileInsuranceValidate();
                    }
                }
            }
        });

//        policyNoEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//
//                    if (allFieldsCheck()) {
//                        if (patientRegistrationUpdate.getInsurance() == null)
//                            callProfileInsuranceValidate();
//                    }
//                }
//            }
//        });

//        memberShipNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//
//                if (!hasFocus) {
//
//                    if (allFieldsCheck()) {
//                        if (patientRegistrationUpdate.getInsurance() == null)
//                            callProfileInsuranceValidate();
//                    }
//                }
//            }
//        });
        memberShipNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (allFieldsCheck()) {
                        if (patientRegistrationUpdate.getInsurance() == null)
                            callProfileInsuranceValidate();
                        hideKeyboard(UpdateProfileSecondActivity.this,memberShipNo);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void callProfileInsuranceValidate() {

        TinyDB tinyDB = new TinyDB(UpdateProfileSecondActivity.this);

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
                                    carrierName = insuranceModel.getClassPlan();//insuranceCarrierSpinner.getText().toString();
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
                                    }

                                    if(insuranceExpiryDate1 != null){

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
                                    }else{

                                        insuranceExpiryDate.setEnabled(true);
                                        insuranceExpiryDate_til.setBackground(getResources().getDrawable(R.drawable.grey_border));
                                        insuranceExpiryDate.setText("");
                                    }

                                }
                            } else {
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(UpdateProfileSecondActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(UpdateProfileSecondActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }
                                ErrorMessage.getInstance().showError(UpdateProfileSecondActivity.this, errorMesg);
                            }
                        } else
                            ErrorMessage.getInstance().showError(UpdateProfileSecondActivity.this, errorMessage);
                    } else
                        ErrorMessage.getInstance().showError(UpdateProfileSecondActivity.this, errorMessage);
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

    Bitmap bitmap(String base64) {

        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    String getNationality(long countryId) {

        String nationality = "";

        if (!ValidationHelper.isNullOrEmpty(nationalities)) {

            for (Nationalities nationalities : nationalities) {
                long nationalitiesNameEn = nationalities.getId();
                if (nationalitiesNameEn == countryId) {
                    if (TextUtil.getEnglish(this))
                        nationality = nationalities.getNameEn();
                    else if (TextUtil.getArabic(this))
                        nationality = nationalities.getNameAr();

                }
            }
        }
        return nationality;
    }

    long getNationalityId(String countryName) {

        long nationalityId = 0;

        if (!ValidationHelper.isNullOrEmpty(nationalities)) {

            for (Nationalities nationalities : nationalities) {
                String nationalitiesNameEn = "";
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
             //   SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_BOTH, this);
                ImagePicker.with(this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        } else {
         //   SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_BOTH, this);
            ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        }
    }

    @OnClick(R.id.insuranceCardImg)
    void imgInsurance() {
        isInsuranceCard = true;
        nationalImage();
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

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

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) insuranceImage.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            insuranceImage.setLayoutParams(params);


        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) insuranceImage.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            insuranceImage.setLayoutParams(params);
        }
    }

    public void Home(View view) {
        Intent intent1 = new Intent();
        setResult(RESULT_OK, intent1);
        finish();
    }

}
