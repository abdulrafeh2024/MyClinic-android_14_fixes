package com.telemedicine.myclinic.activities;

import android.content.Intent;
import androidx.annotation.Nullable;

import android.os.Bundle;

import com.reginald.editspinner.EditSpinner;
import com.telemedicine.myclinic.adapters.SpinnerCustomAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.hyperpay.CustomUIActivity;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.CardDTO;
import com.telemedicine.myclinic.models.CardModel;
import com.telemedicine.myclinic.models.CardsListModel;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.TeleInstantEnqueueDTO;
import com.telemedicine.myclinic.models.TeleInstantEnqueueResult;
import com.telemedicine.myclinic.models.TeleInstantGetPriceDTO;
import com.telemedicine.myclinic.models.TeleInstantGetPriceObject;
import com.telemedicine.myclinic.models.bookAppointment.GetRefSpecialtiesDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetRefSpecialtiesModel;
import com.telemedicine.myclinic.models.bookAppointment.SpecialtiesModel;
import com.telemedicine.myclinic.models.payStage.PayStageOneModel;
import com.telemedicine.myclinic.models.payStage.TeleInstantPayStageOneDTO;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightSpinnerEdittext;
import com.telemedicine.myclinic.webservices.RestClient;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.telemedicine.myclinic.util.Const.OREGID_KEY;

public class TeleConsultationActivity extends BaseActivity {

    @BindView(R.id.select_specialities_spinner)
    LightSpinnerEdittext selectSpecialitiesSpinner;

    @BindView(R.id.time)
    BoldTextView waitingTime;

    @BindView(R.id.cost)
    BoldTextView apptCost;

    ArrayList<SpecialtiesModel> specialtiesList;
    String specialId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSpecilities();
        // it converts the item in the list to a string shown in EditText.
        selectSpecialitiesSpinner.setItemConverter(new EditSpinner.ItemConverter() {
            @Override
            public String convertItemToString(Object selectedItem) {

                String selectedItem1 = selectedItem.toString();
                specialId = getSpecialitiesId(selectedItem1);
                if (!ValidationHelper.isNullOrEmpty(specialId))
                    getTeleInstantEnqueueResult(specialId);
                   // getTeleInstantGetPrice(specialId);
                return selectedItem1;
            }
        });
    }

    @Override
    protected int layout() {
        return R.layout.activity_tele_consultation;
    }


    void getTeleInstantEnqueueResult(String specialtyId) {

        showWaitDialog();

        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        Long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);

        TeleInstantEnqueueDTO tele = new TeleInstantEnqueueDTO(securityToken, Long.parseLong(specialtyId), patientId);
        RestClient.getInstance().getTeleInstantEnqueueResult(tele, new OnResultListener() {
            @Override
            public void onResult(Object result, boolean status, String errorMessage) {

                hideWaitDialog();

                TeleInstantEnqueueResult teleInstantEnqueueResult = (TeleInstantEnqueueResult) result;

                if (teleInstantEnqueueResult != null) {

                    MobileOpResult mobileOpResult = teleInstantEnqueueResult.getMobileOpResult();

                    if (mobileOpResult != null) {

                        if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                            Intent intent = new Intent(TeleConsultationActivity.this, ConsultantRoomActivity.class);
                            intent.putExtra("tokenId", teleInstantEnqueueResult.getTokenId());
                            intent.putExtra("sessionId", teleInstantEnqueueResult.getSessionId());
                            intent.putExtra("time", waitingTime.getText().toString());
                            startActivity(intent);
                        } else {

                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                if (TextUtil.getEnglish(TeleConsultationActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                else if (TextUtil.getArabic(TeleConsultationActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }

                            ErrorMessage.getInstance().showError(TeleConsultationActivity.this, errorMesg);

                        }
                    } else {
                        ErrorMessage.getInstance().showError(TeleConsultationActivity.this, errorMessage);
                    }
                } else {
                    ErrorMessage.getInstance().showError(TeleConsultationActivity.this, errorMessage);
                }
            }
        });
    }

    void getSpecilities() {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            String companyId = tinyDB.getString(Const.COMPANY_ID);
            boolean isTelemedcine = false;
            GetRefSpecialtiesDTO getRefSpecialtiesDTO = new GetRefSpecialtiesDTO(securityToken, isTelemedcine,companyId);

            RestClient.getInstance().getRefSpecialties(getRefSpecialtiesDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    GetRefSpecialtiesModel getRefSpecialtiesModel = (GetRefSpecialtiesModel) result;

                    if (getRefSpecialtiesModel != null) {

                        MobileOpResult mobileOpResult = getRefSpecialtiesModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                specialtiesList = getRefSpecialtiesModel.getSpecialtiesList();
                                if (!ValidationHelper.isNullOrEmpty(specialtiesList)) {
                                    setSpecialities(specialtiesList);
                                }

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                    if (TextUtil.getEnglish(TeleConsultationActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    } else if (TextUtil.getArabic(TeleConsultationActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(TeleConsultationActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(TeleConsultationActivity.this, errorMessage);

                    } else
                        ErrorMessage.getInstance().showError(TeleConsultationActivity.this, errorMessage);
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    void setSpecialities(ArrayList<SpecialtiesModel> specialtiesList) {

        ArrayList<String> specialitiesStr = new ArrayList<>();
        String gender = tinyDB.getString(Const.PATIENT_GENDER);

        for (SpecialtiesModel specialtiesModel : specialtiesList) {


            if (TextUtil.getEnglish(this)) {
                if (!ValidationHelper.isNullOrEmpty(specialtiesModel.getNameEn())) {
                    specialitiesStr.add(specialtiesModel.getNameEn());
                }
            } else if (TextUtil.getArabic(this)) {
                if (!ValidationHelper.isNullOrEmpty(specialtiesModel.getNameAr())) {
                    specialitiesStr.add(specialtiesModel.getNameAr());
                }
            }

            if (gender.equalsIgnoreCase("1")) {
                if (String.valueOf(specialtiesModel.getId()).equalsIgnoreCase("5637144584")) {
                    if (TextUtil.getEnglish(this)) {
                        if (!ValidationHelper.isNullOrEmpty(specialtiesModel.getNameEn())) {
                            specialitiesStr.remove(specialtiesModel.getNameEn());
                        }
                    } else if (TextUtil.getArabic(this)) {
                        if (!ValidationHelper.isNullOrEmpty(specialtiesModel.getNameAr())) {
                            specialitiesStr.remove(specialtiesModel.getNameAr());
                        }
                    }
                }
            }
        }

        android.widget.ListAdapter cityAdapter = new SpinnerCustomAdapter(this, R.layout.spinner_item_list, specialitiesStr);
        selectSpecialitiesSpinner.setAdapter(cityAdapter);
    }

    String getSpecialitiesId(String specialitiesStr) {

        String specialitiesId = "";

        for (SpecialtiesModel specialtiesModel : specialtiesList) {

            if (TextUtil.getEnglish(this)) {
                if (!ValidationHelper.isNullOrEmpty(specialtiesModel.getNameEn())) {
                    if (specialitiesStr.trim().equalsIgnoreCase(specialtiesModel.getNameEn().trim())) {
                        specialitiesId = String.valueOf(specialtiesModel.getId());
                    }
                }
            } else if (TextUtil.getArabic(this)) {
                if (!ValidationHelper.isNullOrEmpty(specialtiesModel.getNameAr())) {
                    if (specialitiesStr.trim().equalsIgnoreCase(specialtiesModel.getNameAr().trim())) {
                        specialitiesId = String.valueOf(specialtiesModel.getId());
                    }
                }
            }
        }

        return specialitiesId;
    }

    void getTeleInstantGetPrice(String specialtyId) {

        showWaitDialog();

        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        Long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);

        TeleInstantGetPriceDTO tele = new TeleInstantGetPriceDTO(securityToken, specialtyId, String.valueOf(patientId));
        RestClient.getInstance().getTeleInstantGetPrice(tele, new OnResultListener() {
            @Override
            public void onResult(Object result, boolean status, String errorMessage) {
                hideWaitDialog();
                TeleInstantGetPriceObject teleInstantGetPriceObject = (TeleInstantGetPriceObject) result;
                if (teleInstantGetPriceObject != null) {
                    MobileOpResult mobileOpResult = teleInstantGetPriceObject.getMobileOpResult();
                    if (mobileOpResult != null) {
                        if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                            double cost = teleInstantGetPriceObject.getAmountDue();
                            double time = teleInstantGetPriceObject.getWaitingTimeMin();
                            waitingTime.setText("" + (int) time);
                            apptCost.setText("" + (int) cost);
                        } else {
                            waitingTime.setText("-");
                            apptCost.setText("-");
                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                if (TextUtil.getEnglish(TeleConsultationActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                else if (TextUtil.getArabic(TeleConsultationActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }
                            ErrorMessage.getInstance().showError(TeleConsultationActivity.this, errorMesg);
                        }
                    } else
                        ErrorMessage.getInstance().showError(TeleConsultationActivity.this, errorMessage);
                } else
                    ErrorMessage.getInstance().showError(TeleConsultationActivity.this, errorMessage);
            }
        });
    }

    @OnClick(R.id.pay_btn)
    void payBtn() {
        payStageOne();
    }

    void payStageOne() {

        if (ConnectionUtil.isInetAvailable(this)) {

            String ammountDue = apptCost.getText().toString();
            double ammount = Double.parseDouble(ammountDue);
            long apptId = tinyDB.getLong(Const.PATIENT_ID, 0);

            showWaitDialog();

            if (ammount > 0) {
                String customerEmail = tinyDB.getString(Const.EMAIL_KEY);
                String patientId = String.valueOf(apptId);
                String ammount1 = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US)).format(ammount);

                TeleInstantPayStageOneDTO payStageOneDTO =
                        new TeleInstantPayStageOneDTO(ammount1, customerEmail, patientId);

                RestClient.getInstance().teleInstantPayStage1(payStageOneDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        hideWaitDialog();

                        PayStageOneModel payStageOneModel = (PayStageOneModel) result;

                        if (payStageOneModel != null) {

                            MobileOpResult mobileOpResult = payStageOneModel.getMobileOpResult();

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                    String checkOutId = payStageOneModel.getResultVal();


                                    getCards(checkOutId, String.valueOf(ammount));
                                } else {

                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(TeleConsultationActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(TeleConsultationActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(TeleConsultationActivity.this, errorMesg);

                                }

                            } else {
                                ErrorMessage.getInstance().showError(TeleConsultationActivity.this, errorMessage);
                            }

                        } else {
                            ErrorMessage.getInstance().showError(TeleConsultationActivity.this, errorMessage);
                        }
                    }
                });
            } else {
                if (!ValidationHelper.isNullOrEmpty(specialId))
                    getTeleInstantEnqueueResult(specialId);
            }
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }

    }
    void getCards(String checkOutId, String mFinalPrice) {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();
            TinyDB tinyDB =new  TinyDB(this);

            String oRegId = tinyDB.getString(OREGID_KEY);
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            long OregIdLong = Long.parseLong(oRegId);

            CardDTO cardDTO =new  CardDTO(securityToken, OregIdLong);

            RestClient.getInstance().paymentCardById(cardDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    CardsListModel payStageOneModel = (CardsListModel) result;

                    if (payStageOneModel != null) {

                        MobileOpResult mobileOpResult = payStageOneModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                ArrayList<CardModel> cards = payStageOneModel.getCardsModel();

                                if(cards.isEmpty()){
                                    // Intent intent = new Intent(TeleConsultationActivity.this, CustomUIActivity.class);
                                    Intent intent = new Intent(TeleConsultationActivity.this, CustomUIActivity.class);
                                    intent.putExtra("amount", mFinalPrice);
                                    intent.putExtra(Const.IS_FROM_CARD_LISTING, false);
                                    intent.putExtra(Const.CHECK_OUT_ID, checkOutId);
                                    intent.putExtra(Const.ISTELEINSTANT_KEY, true);
                                    startActivityForResult(intent, 500);
                                }else{
                                    // Intent intent = new Intent(TeleConsultationActivity.this, CustomUIActivity.class);
                                    Intent intent = new Intent(TeleConsultationActivity.this, CardListActivity.class);
                                    intent.putExtra("amount", mFinalPrice);
                                    intent.putExtra(Const.CHECK_OUT_ID, checkOutId);
                                    intent.putExtra(Const.ISTELEINSTANT_KEY, true);
                                    startActivityForResult(intent, 500);
                                }

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(TeleConsultationActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(TeleConsultationActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(TeleConsultationActivity.this, errorMesg);

                            }

                        } else {

                            ErrorMessage.getInstance().showError(TeleConsultationActivity.this, errorMessage);
                        }

                    } else {

                        ErrorMessage.getInstance().showError(TeleConsultationActivity.this, errorMessage);
                    }
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 500 && resultCode == RESULT_OK) {
            if (!ValidationHelper.isNullOrEmpty(specialId))
                getTeleInstantEnqueueResult(specialId);
        }
    }
}
