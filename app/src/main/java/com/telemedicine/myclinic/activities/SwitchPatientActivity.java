package com.telemedicine.myclinic.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.telemedicine.myclinic.App;
import com.telemedicine.myclinic.activities.profile.AddProfileActivity;
import com.telemedicine.myclinic.adapters.SwitchDoctorsAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.patientMiniModels.PatientsMiniModel;
import com.telemedicine.myclinic.models.profileUpdate.GetProfileDTO;
import com.telemedicine.myclinic.models.profileUpdate.PatientRegistrationUpdate;
import com.telemedicine.myclinic.models.profileUpdate.ProfileUpdateModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.webservices.RestClient;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.telemedicine.myclinic.util.Const.DATE_OF_BIRTH;
import static com.telemedicine.myclinic.util.Const.EXISTING_PATIENT;
import static com.telemedicine.myclinic.util.Const.FIRST_NAME;
import static com.telemedicine.myclinic.util.Const.GENDER;
import static com.telemedicine.myclinic.util.Const.INSURANCE_IMAGES;
import static com.telemedicine.myclinic.util.Const.IS_FROM_SWITCH_PATIENT;
import static com.telemedicine.myclinic.util.Const.IS_QUICK_REG;
import static com.telemedicine.myclinic.util.Const.LAST_NAME;
import static com.telemedicine.myclinic.util.Const.LOAD_TENTATIVE_PROFILE;
import static com.telemedicine.myclinic.util.Const.MIDDLE_NAME;
import static com.telemedicine.myclinic.util.Const.PATIENT_GENDER;
import static com.telemedicine.myclinic.util.Const.PATIENT_NAME;

public class SwitchPatientActivity extends Activity implements SwitchDoctorsAdapter.OnCardClickListner {

    @BindView(R.id.switch_profile_recycler)
    RecyclerView switchProfileRecycler;

    @BindView(R.id.imageView_close)
    ImageView imageView_close;

    boolean isQuickReg = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextUtil.translation(this);
        super.onCreate(savedInstanceState);
        changeLanguage();
        setContentView(R.layout.activity_switch_patient);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.setFinishOnTouchOutside(false);
        ButterKnife.bind(this);
        getValue();
        trasformView();
    }

    ArrayList<PatientsMiniModel> patientsMiniModels = null;

    private void changeLanguage(){
        if(TextUtil.getArabic(this)){
            Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            Configuration configuration = getBaseContext().getResources().getConfiguration();
            configuration.setLocale(locale);
            createConfigurationContext(configuration);
        }
    }

    private void getValue() {

        try {

            Intent intent = getIntent();

            if (intent != null) {

                patientsMiniModels = intent.getParcelableArrayListExtra(EXISTING_PATIENT);
                isQuickReg = intent.getBooleanExtra(IS_QUICK_REG, false);
                if (!ValidationHelper.isNullOrEmpty(patientsMiniModels)) {
                    SwitchDoctorsAdapter adapter = new SwitchDoctorsAdapter(this, patientsMiniModels);
                    switchProfileRecycler.setLayoutManager(new LinearLayoutManager(this));
                    switchProfileRecycler.setAdapter(adapter);
                    adapter.setOnCardClickListner(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnCardClicked(PatientsMiniModel model, int pos) {

        try{
            // get the data
            long insuranceId = 0;

            if (model != null && !ValidationHelper.isNullOrEmpty(model.getInsuranceCards())) {
                insuranceId = model.getInsuranceCards().get(0).getInsuranceId();
            }

            String mrnNo = model.getMRN();
            long patientId = model.getPatientId();

            TinyDB tinyDB = new TinyDB(this);

            // save the value
            tinyDB.putString(Const.INSURANCE_ID, String.valueOf(insuranceId));

            tinyDB.putLong(Const.PATIENT_ID, patientId);

            if (TextUtil.getEnglish(this)) {
                if (!ValidationHelper.isNullOrEmpty(model.getNameEn()))
                    tinyDB.putString(PATIENT_NAME, model.getNameEn());
            } else if (TextUtil.getArabic(this)) {
                if (!ValidationHelper.isNullOrEmpty(model.getNameAr()))
                    tinyDB.putString(PATIENT_NAME, model.getNameAr());
            }


            if (!ValidationHelper.isNullOrEmpty(model.getGender()))
                tinyDB.putString(PATIENT_GENDER, model.getGender());

            tinyDB.putInt(Const.PATIENT_CATEGORY, model.getPatientCategory());

            if(!ValidationHelper.isNullOrEmpty(model.getRegCardUrl())){
                tinyDB.putString(Const.PATIENT_REG_CARD_URL, model.getRegCardUrl());
            }else{
                tinyDB.putString(Const.PATIENT_REG_CARD_URL,"");
            }

            if (!ValidationHelper.isNullOrEmpty(mrnNo)){
                tinyDB.putString(Const.MRN_NO, mrnNo);
                Intent intent1 = new Intent(this, DashBoardActivity.class);
                intent1.putExtra(IS_QUICK_REG, isQuickReg);
                intent1.putExtra(Const.EXISTING_PATIENT, patientsMiniModels);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                tinyDB.putBoolean(Const.IS_TENT, false);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }else{

                Intent intent = new Intent(SwitchPatientActivity.this, AddProfileActivity.class);
                //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // intent.putExtra(IS_QUICK_REG, isQuickReg);
                tinyDB.putString(Const.MRN_NO, "0");
                intent.putExtra(EXISTING_PATIENT, patientsMiniModels);
                intent.putExtra(LOAD_TENTATIVE_PROFILE, true);
                startActivity(intent);
                setResult(RESULT_OK);
                finish();

            }


        }catch (Exception e){
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {

    }

    public void Close(View view) {

        ((App) getApplication()).cancelAlarmIfExists();

        finish();
    }

    void trasformView() {
        if (TextUtil.getArabic(this)) {
            RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) imageView_close.getLayoutParams();
            params4.width = 78;
            params4.height = 78;
            params4.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params4.setMargins(0, 85, 0, 0);  // left, top, right, bottom
            imageView_close.setLayoutParams(params4);

        } else if (TextUtil.getEnglish(this)) {
            RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) imageView_close.getLayoutParams();
            params4.width = 78;
            params4.height = 78;
            params4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params4.setMargins(0, 85, 0, 0);  // left, top, right, bottom
            imageView_close.setLayoutParams(params4);

        }
    }
}
