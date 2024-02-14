package com.telemedicine.myclinic.activities;

import static com.telemedicine.myclinic.util.Const.BOARDING_URL;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.telemedicine.myclinic.AppointmentDetailActivity;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.hyperpay.CustomUIActivity;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.boardingPass.BoardingPassDTO;
import com.telemedicine.myclinic.models.boardingPass.BoardingPassModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.AppointmentEvent;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.webservices.RestClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class BoardingPassActivity extends BaseActivity {

    @BindView(R.id.barcode_image)
    ImageView barCodeImage;

    @BindView(R.id.apptTime)
    LightTextView dateTime;

    @BindView(R.id.speciality)
    LightTextView speciality;

    @BindView(R.id.doctor_name)
    LightTextView doctor_name;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

   @BindView(R.id.fullScreenBtn)
    LightButton fullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transformView();
        loadBarCode();
    }

    String boardingPassUrl = "";
    private void loadBarCode() {

        Intent intent = getIntent();

        if (intent != null) {

            String apptDate = intent.getStringExtra(Const.DATE);
            if (!ValidationHelper.isNullOrEmpty(apptDate)) {
                String date = LocaleDateHelper.convertDateStringFormat(apptDate, "yyyy-MM-dd'T'HH:mm:ss", "EEEE, dd MMMM yyyy hh:mm a");
                if (!ValidationHelper.isNullOrEmpty(date))
                    dateTime.setText(date);
                else
                    dateTime.setText(apptDate);
            }

            com.telemedicine.myclinic.models.homemodels.DoctorsProfile doctorsProfile =
                    intent.getParcelableExtra(Const.DOC_OBJECT);

            if (doctorsProfile != null) {
                String docName = doctorsProfile.getNameEn();
                if (TextUtil.getArabic(this))
                    docName = doctorsProfile.getNameAr();

                if (!ValidationHelper.isNullOrEmpty(docName))
                    doctor_name.setText(docName);

                String docSpeciality = doctorsProfile.getSpecialtyEn();

                if (TextUtil.getArabic(this))
                    docSpeciality = doctorsProfile.getSpecialtyAr();

                if (!ValidationHelper.isNullOrEmpty(docSpeciality))
                    speciality.setText(docSpeciality);
            } else {

                String docName = intent.getStringExtra(Const.DOCTOR_NAME);
                if (!ValidationHelper.isNullOrEmpty(docName))
                    doctor_name.setText(docName);

                String docSpeciality = intent.getStringExtra(Const.DOCTOR_SPECIALITY);
                if (!ValidationHelper.isNullOrEmpty(docSpeciality))
                    speciality.setText(docSpeciality);
            }

        }

        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog();

            String securityToken = new TinyDB(this).getString(Const.TOKEN_KEY);
            String companyId = new TinyDB(this).getString(Const.COMPANY_ID);
            long apptId = intent.getLongExtra(Const.APPT_ID, 0);

            BoardingPassDTO boardingPassDTO = new BoardingPassDTO(securityToken, apptId,companyId);

            RestClient.getInstance().apptGetBoardingPassUrl(boardingPassDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {
                    hideWaitDialog();
                    BoardingPassModel boardingPassModel = (BoardingPassModel) result;

                    if (boardingPassModel != null) {

                        MobileOpResult mobileOpResult = boardingPassModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                boardingPassUrl = boardingPassModel.getBoardingPassUrl();
                                if (!ValidationHelper.isNullOrEmpty(boardingPassUrl)) {
                                    fullScreen.setVisibility(View.VISIBLE);
                                    Glide.with(BoardingPassActivity.this).load(boardingPassUrl).into(barCodeImage);
                                }

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(BoardingPassActivity.this, errorMesg);

                            }


                        } else {
                            ErrorMessage.getInstance().showError(BoardingPassActivity.this, errorMessage);
                        }

                    } else {
                        ErrorMessage.getInstance().showError(BoardingPassActivity.this, errorMessage);
                    }
                }
            });


        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }

    }

    @Override
    protected int layout() {
        return R.layout.activity_boarding_pass;
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
                ErrorMessage.getInstance().showSuccessGreen(BoardingPassActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameAr() + ". "+getString(R.string.please_press_to_continue));
            }else{
                ErrorMessage.getInstance().showSuccessGreen(BoardingPassActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameEn() + ". "+getString(R.string.please_press_to_continue));
            }
        } else {
            ErrorMessage.getInstance().showErrorYellow(BoardingPassActivity.this, appointmentEvent.getErrorMSg());
        }
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    void transformView() {
        if (TextUtil.getArabic(this)) {
            toolbar_left_iv.setRotation(180);
        }
    }

    public void fullScreen(View view) {
        Intent intent = new Intent(BoardingPassActivity.this, BoardingPassFullScreenActivity.class);
        intent.putExtra(BOARDING_URL, boardingPassUrl);
        startActivity(intent);
    }
}
