package com.telemedicine.myclinic.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.telemedicine.myclinic.adapters.InsuranceCashCardAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.bookAppointment.GetInsuranceByIdDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetInsuranceModel;
import com.telemedicine.myclinic.models.patientMiniModels.InsuranceCardModel;
import com.telemedicine.myclinic.models.profileInsuranceValidate.ProfileInsuranceValidateDTO;
import com.telemedicine.myclinic.models.profileInsuranceValidate.ProfileInsuranceValidateModel;
import com.telemedicine.myclinic.models.profileUpdate.GetProfileDTO;
import com.telemedicine.myclinic.models.profileUpdate.PatientRegistrationUpdate;
import com.telemedicine.myclinic.models.profileUpdate.ProfileUpdateModel;
import com.telemedicine.myclinic.models.teleprice.TelePriceDto;
import com.telemedicine.myclinic.models.teleprice.TelePriceResponse;
import com.telemedicine.myclinic.models.topupfee.TopUpFeeDTO;
import com.telemedicine.myclinic.models.topupfee.TopUpFeeModel;
import com.telemedicine.myclinic.models.topupfee.TopUpFeeResponseModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.AppointmentEvent;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.LightEdittext;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.webservices.RestClient;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;

import static com.telemedicine.myclinic.util.Const.INSURANCE_IMAGES;
import static com.telemedicine.myclinic.util.Const.PATIENT_UPDATE_KEY;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class InsuranceCardCashCardActivity extends BaseActivity implements InsuranceCashCardAdapter.OnCardClickListner {


    @BindView(R.id.insurance_cash_payment_listview)
    RecyclerView insuranceCashPaymentListview;

    @BindView(R.id.close)
    ImageView close;

    @BindView(R.id.payment_by_cash_container)
    RelativeLayout payment_by_cash_container;

    @BindView(R.id.yes_pay_cash)
    LightButton YesPayCash;

    @BindView(R.id.insurance_image)
    ImageView insurance_image;

    boolean isTelemedcine = false;

    String noInsurance = "";
    String doctorId = "";
    boolean isChangeInsurance = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected int layout() {
        return R.layout.activity_insurance_card_cash_card;
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
                ErrorMessage.getInstance().showSuccessGreen(InsuranceCardCashCardActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameAr() + ". "+getString(R.string.please_press_to_continue));
            }else{
                ErrorMessage.getInstance().showSuccessGreen(InsuranceCardCashCardActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameEn() + ". "+getString(R.string.please_press_to_continue));
            }
        } else {
            ErrorMessage.getInstance().showErrorYellow(InsuranceCardCashCardActivity.this, appointmentEvent.getErrorMSg());
        }
    }

    InsuranceCashCardAdapter insuranceCashCardAdapter;
    private void init() {

        if (TextUtil.getArabic(this)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) close.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            close.setLayoutParams(params);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) close.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            close.setLayoutParams(params);

        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        if (TextUtil.getArabic(this))
            layoutManager.setReverseLayout(true);

        insuranceCashPaymentListview.setLayoutManager(layoutManager);
        Intent intent = getIntent();
        if (intent != null) {

            noInsurance = intent.getStringExtra("noinsurance");
            doctorId = intent.getStringExtra("doctorId");
            isChangeInsurance = intent.getBooleanExtra(Const.Is_CHANGE_INSURANCE, false);
            ArrayList<InsuranceCardModel> insuranceCardModels = intent.getParcelableArrayListExtra("insuranceCard");
            isTelemedcine = intent.getBooleanExtra(Const.ISTELEMEDICINE_KEY, false);

            if (!ValidationHelper.isNullOrEmpty(noInsurance) || ValidationHelper.isNullOrEmpty(insuranceCardModels)) {

                if (noInsurance.equalsIgnoreCase("5637149080") || noInsurance.equalsIgnoreCase("5637152079") || ValidationHelper.isNullOrEmpty(insuranceCardModels)) {
                    payment_by_cash_container.setVisibility(View.VISIBLE);

                    if (TextUtil.getArabic(this))
                        YesPayCash.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);

                    return;
                }
            }


           updateInsuranceList(insuranceCardModels);
        }
    }

    @OnClick(R.id.close)
    void close() {
        finish();
    }

    Intent intent = new Intent();

    @Override
    public void OnCardClicked(InsuranceCardModel model, int pos) {

        if (model != null) {
            if(!isTelemedcine){
                GetTopUpFeesByDoctor(model);
            }else{
                insuranceValidate(model);
            }

        } else {
            if (isTelemedcine && !isChangeInsurance) {
                getPrice(false, 0, true);
            } else {
                intent.putExtra(Const.IS_INSURANCE_KEY, false);
                intent.putExtra(Const.INSURANCE_ID, 0);
                setResult(RESULT_OK, intent);
                finish();
            }


        }
    }

    public void updateInsuranceList(ArrayList<InsuranceCardModel> mGetInsuranceModelsList){
        insuranceCashCardAdapter = new InsuranceCashCardAdapter(this, mGetInsuranceModelsList, noInsurance);
        if(!isChangeInsurance){
            mGetInsuranceModelsList.add(null);
        }

        if (!ValidationHelper.isNullOrEmpty(mGetInsuranceModelsList))
            Collections.reverse(mGetInsuranceModelsList);
        insuranceCashPaymentListview.setAdapter(insuranceCashCardAdapter);
        insuranceCashCardAdapter.setOnCardClickListner(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == RESULT_OK) {
            getInsurance();
        }
    }

    void getInsurance() {
        try {
            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);

            GetInsuranceByIdDTO getInsuranceByIdDTO = new GetInsuranceByIdDTO(securityToken, String.valueOf(patientId));

            if (ConnectionUtil.isInetAvailable(this)) {

                showWaitDialog();

                RestClient.getInstance().getInsuranceByProfileId(getInsuranceByIdDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        hideWaitDialog();

                        GetInsuranceModel getInsuranceModel = (GetInsuranceModel) result;

                        if (getInsuranceModel != null) {

                            MobileOpResult mobileOpResult = getInsuranceModel.getMobileOpResult();

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                    ArrayList<InsuranceCardModel> getInsuranceModelsList = getInsuranceModel.getInsuranceCardModels();
                                    updateInsuranceList(getInsuranceModelsList);

                                } else {


                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(InsuranceCardCashCardActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(InsuranceCardCashCardActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(InsuranceCardCashCardActivity.this, errorMesg);
                                }

                            } else
                                ErrorMessage.getInstance().showError(InsuranceCardCashCardActivity.this, errorMessage);

                        } else {
                            ErrorMessage.getInstance().showError(InsuranceCardCashCardActivity.this, errorMessage);
                        }
                    }
                });

            } else {
                ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void GetTopUpFeesByDoctor(InsuranceCardModel model) {

        TinyDB tinyDB = new TinyDB(this);

        if (tinyDB != null && ConnectionUtil.isInetAvailable(this)) {

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
//
            TopUpFeeDTO topUpFeeDTO = new TopUpFeeDTO(securityToken, doctorId);

            showWaitDialog();

            RestClient.getInstance().GetTopUpFeesByDoctor(topUpFeeDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {
                    hideWaitDialog();

                    TopUpFeeResponseModel topUpFeeResponseModel = (TopUpFeeResponseModel) result;

                    if (topUpFeeResponseModel != null) {

                        MobileOpResult mobileOpResult = topUpFeeResponseModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                if(!ValidationHelper.isNullOrEmpty(topUpFeeResponseModel.getTopUpFeeModel())){
                                    showTopUpFeePopDialog(model, topUpFeeResponseModel.getTopUpFeeModel());
                                }else{
                                    insuranceValidate(model);
                                }
                            } else {
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }
                                ErrorMessage.getInstance().showError(InsuranceCardCashCardActivity.this, errorMesg);
                            }
                        } else
                            ErrorMessage.getInstance().showError(InsuranceCardCashCardActivity.this, errorMessage);
                    } else
                        ErrorMessage.getInstance().showError(InsuranceCardCashCardActivity.this, errorMessage);
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    private void showTopUpFeePopDialog(InsuranceCardModel model, ArrayList<TopUpFeeModel> topUpFeeModel) {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_top_up_fee_layout);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            RegularTextView questionTxt = (RegularTextView) dialog.findViewById(R.id.question_txt);
            questionTxt.setText(getString(R.string.TopUp_Fee_Alert_1)+" "+
                    String.format("%.2f", topUpFeeModel.get(0).getPrice())+" "+
                    getString(R.string.sar)+". "+getString(R.string.TopUp_Fee_Alert_2));

            RegularTextView dialogButton = dialog.findViewById(R.id.yes);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    insuranceValidate(model);
                }
            });

            RegularTextView dialogButton1 = dialog.findViewById(R.id.no);
            dialogButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    private void insuranceValidate(InsuranceCardModel model) {

        TinyDB tinyDB = new TinyDB(this);

        if (tinyDB != null && ConnectionUtil.isInetAvailable(this)) {

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            String insuranceCarrierTypeVal = String.valueOf(model.getCarrierId());
            String policyNo = model.getContractNo();
            String memberIdNumber = model.getMembershipNo();
            long specialityId = 0;
            if (!ValidationHelper.isNullOrEmpty(noInsurance))
                specialityId = Long.parseLong(noInsurance);

            ProfileInsuranceValidateDTO profileInsuranceValidateDTO = new ProfileInsuranceValidateDTO(securityToken,
                    insuranceCarrierTypeVal, memberIdNumber, policyNo, specialityId);

            profileInsuranceValidateDTO.setCarrierId(model.getCarrierId());
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

                                if (isTelemedcine && !isChangeInsurance) {
                                    getPrice(true, model.getInsuranceId(), false);
                                } else {
                                    intent.putExtra(Const.IS_INSURANCE_KEY, true);
                                    intent.putExtra(Const.INSURANCE_ID, model.getInsuranceId());
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }


                            } else {
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }
                                ErrorMessage.getInstance().showError(InsuranceCardCashCardActivity.this, errorMesg);
                            }
                        } else
                            ErrorMessage.getInstance().showError(InsuranceCardCashCardActivity.this, errorMessage);
                    } else
                        ErrorMessage.getInstance().showError(InsuranceCardCashCardActivity.this, errorMessage);
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    @OnClick(R.id.yes_pay_cash)
    void payCash() {
        Intent intent = new Intent();

        if (isTelemedcine && !isChangeInsurance) {
            double amount = getPrice(false, 0, true);
            intent.putExtra(Const.TELE_PRICE, amount);
        } else {
            intent.putExtra(Const.IS_INSURANCE_KEY, false);
            intent.putExtra(Const.INSURANCE_ID, 0);
        }
        intent.putExtra(Const.IS_TELE, isTelemedcine);
        setResult(RESULT_OK, intent);
        finish();

    }

    double amount = 0;

    double getPrice(boolean isInsurance, long insuranceId, boolean isCash) {

        showWaitDialog();
        String companyId = tinyDB.getString(Const.COMPANY_ID);
        RestClient.getInstance().telePrice(new TelePriceDto(tinyDB.getString(Const.TOKEN_KEY), isCash, insuranceId, Long.parseLong(doctorId),companyId), new OnResultListener() {
            @Override
            public void onResult(Object result, boolean status, String errorMessage) {


                hideWaitDialog();

                TelePriceResponse telePriceResponse = (TelePriceResponse) result;

                if (telePriceResponse != null) {

                    MobileOpResult mobileOpResult = telePriceResponse.getMobileOpResult();

                    if (mobileOpResult != null) {

                        if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                            amount = telePriceResponse.getAmountDue();
                            intent.putExtra(Const.TELE_PRICE, amount);
                            intent.putExtra(Const.IS_TELE, isTelemedcine);
                            intent.putExtra(Const.IS_INSURANCE_KEY, isInsurance);
                            intent.putExtra(Const.INSURANCE_ID, insuranceId);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            ErrorMessage.getInstance().showError(InsuranceCardCashCardActivity.this, mobileOpResult.getBusinessErrorMessageEn());
                        }
                    }
                } else {
                    ErrorMessage.getInstance().showError(InsuranceCardCashCardActivity.this, errorMessage);
                }


            }
        });

        return amount;
    }


    @OnClick(R.id.insurance_image)
    void getPatientData(){
        try {
            if (ConnectionUtil.isInetAvailable(this)) {

                showWaitDialog();

                if (tinyDB != null) {

                    long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
                    String securityToken = tinyDB.getString(Const.TOKEN_KEY);
                    String oRegId = tinyDB.getString(Const.OREGID_KEY);

                    GetProfileDTO getProfileDTO = new GetProfileDTO(String.valueOf(patientId), securityToken, oRegId);
                    RestClient.getInstance().profileGetById(getProfileDTO, new OnResultListener() {
                        @Override
                        public void onResult(Object result, boolean status, String errorMessage) {

                            hideWaitDialog();

                            ProfileUpdateModel profileUpdateModel = (ProfileUpdateModel) result;

                            if (profileUpdateModel != null) {
                                INSURANCE_IMAGES = "";

                                MobileOpResult mobileOpResult = profileUpdateModel.getMobileOpResult();

                                if (mobileOpResult != null) {

                                    if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                        PatientRegistrationUpdate patientRegistrationUpdate = profileUpdateModel.getPatientRegistrationUpdate();

                                        if (patientRegistrationUpdate != null) {
                                            try {

                                                if (patientRegistrationUpdate.getInsurance() != null) {
                                                    INSURANCE_IMAGES = patientRegistrationUpdate.getInsurance().getInsuranceCardScanBase64();
                                                    patientRegistrationUpdate.getInsurance().setInsuranceCardScanBase64("");
                                                }


                                                Intent intent = new Intent(InsuranceCardCashCardActivity.this, AddInsuranceActivity.class);
                                                intent.putExtra(PATIENT_UPDATE_KEY, patientRegistrationUpdate);
                                                startActivityForResult(intent, 99);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    } else {
                                        String errorMesg = "";

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                            if (TextUtil.getEnglish(InsuranceCardCashCardActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                            else if (TextUtil.getArabic(InsuranceCardCashCardActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";

                                        }

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                            errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                        }
                                        if(mobileOpResult.getResult() == 600 || mobileOpResult.getResult() == 700){
                                            ErrorMessage.getInstance().showErrorYellow(InsuranceCardCashCardActivity.this, errorMesg);
                                        }else{
                                            ErrorMessage.getInstance().showError(InsuranceCardCashCardActivity.this, errorMesg);
                                        }
                                       // ErrorMessage.getInstance().showError(InsuranceCardCashCardActivity.this, errorMesg);
                                    }
                                } else
                                    ErrorMessage.getInstance().showError(InsuranceCardCashCardActivity.this, errorMessage);
                            } else
                                ErrorMessage.getInstance().showError(InsuranceCardCashCardActivity.this, errorMessage);
                        }
                    });
                }
            } else {
                ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
