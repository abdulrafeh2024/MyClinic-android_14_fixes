package com.telemedicine.myclinic.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.raywenderlich.android.validatetor.ValidateTor;
import com.reginald.editspinner.EditSpinner;
import com.telemedicine.myclinic.adapters.SpinnerCustomAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.profileInsuranceValidate.InsuranceModel;
import com.telemedicine.myclinic.models.profileInsuranceValidate.ProfileInsuranceValidateDTO;
import com.telemedicine.myclinic.models.profileInsuranceValidate.ProfileInsuranceValidateModel;
import com.telemedicine.myclinic.models.profileUpdate.PatientRegistrationUpdate;
import com.telemedicine.myclinic.models.profileUpdate.ProfileUpdateDTO;
import com.telemedicine.myclinic.models.profileUpdate.UpdatePatientRegistrationDTO;
import com.telemedicine.myclinic.models.profilecreatoinmodels.Cities;
import com.telemedicine.myclinic.models.profilecreatoinmodels.District;
import com.telemedicine.myclinic.models.profilecreatoinmodels.InsuranceCarriers;
import com.telemedicine.myclinic.models.profilecreatoinmodels.InsuranceDTO;
import com.telemedicine.myclinic.models.profilecreatoinmodels.Nationalities;
import com.telemedicine.myclinic.models.profilecreatoinmodels.ProfileCreateModel;
import com.telemedicine.myclinic.models.profilecreatoinmodels.RefPatientProfileModel;
import com.telemedicine.myclinic.models.profilecreatoinmodels.SecurityTokenDTO;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionUtils;

import static com.telemedicine.myclinic.util.Const.PATIENT_UPDATE_KEY;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AddInsuranceActivity extends BaseActivity {

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
        transformView();
        callRefPatientProfile();
        validateInsurance();
        getIntentValue();
        initInsurance();
    }

    private void initInsurance() {
        insuranceCarrierSpinner.setItemConverter(new EditSpinner.ItemConverter() {
            @Override
            public String convertItemToString(Object selectedItem) {

                String insuranceCarrier = (String) selectedItem.toString().trim();

                if (!ValidationHelper.isNullOrEmpty(insuranceCarriersList)) {

                    for (InsuranceCarriers insuranceCarriers1 : insuranceCarriersList) {

                        String insuranceName = "";
                        if (TextUtil.getEnglish(AddInsuranceActivity.this)) {
                            insuranceName = insuranceCarriers1.getNameEn();
                        } else if (TextUtil.getArabic(AddInsuranceActivity.this)) {
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


    }

    @Override
    protected int layout() {
        return R.layout.activity_add_insurance;
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
                ErrorMessage.getInstance().showSuccessGreen(AddInsuranceActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameAr() + ". "+getString(R.string.please_press_to_continue));
            }else{
                ErrorMessage.getInstance().showSuccessGreen(AddInsuranceActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameEn() + ". "+getString(R.string.please_press_to_continue));
            }
        } else {
            ErrorMessage.getInstance().showErrorYellow(AddInsuranceActivity.this, appointmentEvent.getErrorMSg());
        }
    }

    public void Update(View view) {

        ValidateTor validateTor = new ValidateTor();

        String nationality = patientRegistrationUpdate.getNationality();
        String nationalIdType = String.valueOf(patientRegistrationUpdate.getNationalIdType());
        String nationalTypeId = String.valueOf(patientRegistrationUpdate.getNationalIdType());
        String nationalyIdNumber = patientRegistrationUpdate.getNationalId();
        String expiryDate = patientRegistrationUpdate.getNationalIdExpiryDate();
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

        String nationalIdTypeVal = patientRegistrationUpdate.getNationalId();


        UpdatePatientRegistrationDTO updatePatientRegistrationDTO =
                new UpdatePatientRegistrationDTO(patientRegistrationUpdate.getCountry(),
                        patientRegistrationUpdate.getNationalId(),
                        String.valueOf(patientRegistrationUpdate.getSalutation()),
                        String.valueOf(patientRegistrationUpdate.getDistrictRecId()),
                        patientRegistrationUpdate.getNationality(),
                        patientRegistrationUpdate.getNationalIdExpiryDate(),
                        null,
                        patientRegistrationUpdate.getFirstName(),
                        true,
                        patientRegistrationUpdate.getMiddleName(),
                        "",
                        String.valueOf(patientRegistrationUpdate.getGender()),
                        patientRegistrationUpdate.getBirthDate(),
                        String.valueOf(patientRegistrationUpdate.getNationalIdType()),
                        "",
                        patientRegistrationUpdate.getCity(),
                        patientRegistrationUpdate.getLastName(),
                        patientRegistrationUpdate.getMRN(),
                        String.valueOf(patientRegistrationUpdate.getPatientRecId()),
                        patientRegistrationUpdate.isTentativePatient());


     /*   patientRegistrationDTO.setNationality(patientRegistrationUpdate.getNationality());
        patientRegistrationDTO.setCountry(patientRegistrationUpdate.getCountry());
        patientRegistrationDTO.setNationalIdType(nationalIdTypeVal);
        patientRegistrationDTO.setNationalId(nationalyIdNumber);
        patientRegistrationDTO.setNationalIdExpiryDate(expiryDate);
        patientRegistrationDTO.setIdCardScanBase64(nationalCardBase64);
        patientRegistrationDTO.setInsuranceHolder(isInsuranceChecked);*/

        if (!isInsuranceChecked)
            updatePatientRegistrationDTO.setInsurance(null);
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


         /*   insuranceDTO = new InsuranceDTO(contractIdStr, memberShipIdNo, insuranceCardBase64, insuranceED,
                    classPlanIdStr, carrierName, carrierIdStr);*/

            insuranceDTO = new InsuranceDTO(String.valueOf(contractId), memberShipIdNo, insuranceCardBase64, insuranceED,
                    String.valueOf(classPlanId), carrierName, String.valueOf(carrierId));

            updatePatientRegistrationDTO.setInsurance(insuranceDTO);
        }

        // these params are used in updation.
  /*      String mrn = patientRegistrationUpdate.getMRN();
        long patientRecId = patientRegistrationUpdate.getPatientRecId();

        if (!ValidationHelper.isNullOrEmpty(mrn))
            patientRegistrationDTO.setMRN(mrn);

        if (patientRecId != 0)
            patientRegistrationDTO.setPatientRecId(String.valueOf(patientRecId));

        boolean isTentativePatient = patientRegistrationUpdate.isTentativePatient();
*/
      //  patientRegistrationDTO.setTentativePatient(isTentativePatient);

        // if the insurance is added then call the insurance validate service here
        if (!ValidationHelper.isNullOrEmpty(memberShipIdNo) && isInsuranceChecked) {

            String memberSHipNoStr = memberShipNo.getText().toString().trim();

            if (!ValidationHelper.isNullOrEmpty(memberSHipNoStr)) {
                if (!memberSHipNoStr.equalsIgnoreCase(memberShipIdNo)) {
                    ErrorMessage.getInstance().showWarning(this, "Membership Id Number is not validated");
                    return;
                }
            }
        }
        updateProfile(updatePatientRegistrationDTO, tinyDB, true);
        // if the national id number is same so no need to call the validate service.
        if (nationalyIdNumber.equalsIgnoreCase(patientRegistrationUpdate.getNationalId()) && patientRegistrationUpdate.getInsurance() != null) {
            TinyDB tinyDB = new TinyDB(this);
           // updateProfile(updatePatientRegistrationDTO, tinyDB, true);
        } else if (nationalyIdNumber.equalsIgnoreCase(patientRegistrationUpdate.getNationalId()) && !isInsuranceChecked) {
            TinyDB tinyDB = new TinyDB(this);
           // updateProfile(updatePatientRegistrationDTO, tinyDB, true);

        } else {
           // validateProfile(patientRegistrationDTO,true);
        }

    }

    void updateProfile(UpdatePatientRegistrationDTO patientRegistrationDTO, TinyDB tinyDB, boolean showDial) {

        if (showDial)
            showWaitDialog();

        if (tinyDB != null && patientRegistrationDTO != null) {

            String oRegId = tinyDB.getString(Const.OREGID_KEY);
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            ProfileUpdateDTO profileUpdateDTO = new ProfileUpdateDTO(oRegId, securityToken, patientRegistrationDTO);

            RestClient.getInstance().profileInsuranceAdd(profileUpdateDTO, new OnResultListener() {
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

                                    //uncoment this after testing
                                   if(insuranceId != "0"){
                                       Intent intent1 = new Intent();
                                       setResult(RESULT_OK, intent1);
                                       finish();
                                   }else{
                                       ErrorMessage.getInstance().showError(AddInsuranceActivity.this, "Could not add, Please Check your insurance");
                                   }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(AddInsuranceActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(AddInsuranceActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(AddInsuranceActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";

                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }
                                ErrorMessage.getInstance().showError(AddInsuranceActivity.this, errorMesg);
                            }

                        } else {
                            ErrorMessage.getInstance().showError(AddInsuranceActivity.this, errorMessage);
                        }
                    } else {
                        ErrorMessage.getInstance().showError(AddInsuranceActivity.this, errorMessage);
                    }
                }
            });
        }
    }

    void getIntentValue() {

        Intent intent = getIntent();
        if (intent != null) {
            patientRegistrationUpdate = intent.getParcelableExtra(PATIENT_UPDATE_KEY);
        }
    }

    // register insurance validate listener
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

        memberShipNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (allFieldsCheck()) {
                            callProfileInsuranceValidate();
                        hideKeyboard(AddInsuranceActivity.this,memberShipNo);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });


    }

    boolean isInsuranceChecked = true;

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

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

            try {
                // SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_BOTH, UpdateProfileSecondActivity.this);
                ImagePicker.with(AddInsuranceActivity.this)
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
            Toast.makeText(AddInsuranceActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

    Bitmap bitmap(String base64) {

        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    boolean isInsuranceCard = false;
    @OnClick(R.id.insuranceCardImg)
    void imgInsurance() {
        isInsuranceCard = true;
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

    private void callProfileInsuranceValidate() {

        TinyDB tinyDB = new TinyDB(AddInsuranceActivity.this);

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
                                    if (TextUtil.getEnglish(AddInsuranceActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(AddInsuranceActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }
                                ErrorMessage.getInstance().showError(AddInsuranceActivity.this, errorMesg);
                            }
                        } else
                            ErrorMessage.getInstance().showError(AddInsuranceActivity.this, errorMessage);
                    } else
                        ErrorMessage.getInstance().showError(AddInsuranceActivity.this, errorMessage);
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, "Internet is not available");
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

                            updateInsuranceCarrierLsit();

                        } else {
                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                if (TextUtil.getEnglish(AddInsuranceActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                else if (TextUtil.getArabic(AddInsuranceActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }
                            ErrorMessage.getInstance().showError(AddInsuranceActivity.this, errorMesg);
                        }
                    } else {
                        ErrorMessage.getInstance().showError(AddInsuranceActivity.this, errorMessage);

                    }
                } else {
                    ErrorMessage.getInstance().showError(AddInsuranceActivity.this, errorMessage);
                }
            }
        });
    }

    private void updateInsuranceCarrierLsit() {
        ArrayList<String> insuranceList = new ArrayList<>();
        if (!ValidationHelper.isNullOrEmpty(insuranceCarriersList)) {

            for (InsuranceCarriers insuranceCarriers1 : insuranceCarriersList) {
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
}