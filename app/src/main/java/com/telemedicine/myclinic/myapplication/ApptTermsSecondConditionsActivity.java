package com.telemedicine.myclinic.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.telemedicine.myclinic.activities.BookAppointmentOneActivity;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.views.RegularTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY;

public class ApptTermsSecondConditionsActivity extends Activity {

    @BindView(R.id.termscondition_txt)
    RegularTextView termsConditionTxt;

    boolean isTele = false;
    TinyDB tinyDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appt_terms_conditions_second);
        ButterKnife.bind(this);
        tinyDB = new TinyDB(this);

        Intent intent = getIntent();

        if (intent != null) {

            String patientName = tinyDB.getString(Const.PATIENT_NAME);
            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
            String key = patientName + patientId + "1";

            String termsCondition = intent.getStringExtra(Const.TERMS_CONDITIONS_KEY);

            isTele = intent.getBooleanExtra(Const.ISTELEMEDICINE_KEY, false);

           /* if (tinyDB.getBoolean(key)) {
                Intent i1 = new Intent();
                i1.putExtra(Const.ACCEPTED_TELE, true);
                setResult(RESULT_OK, i1);
                finish();
            }*/

            termsConditionTxt.setText(Html.fromHtml(termsCondition));
        }
    }

    public void Cancel(View view) {

        if (isTele) {
            Intent intent = new Intent();
            intent.putExtra(Const.ACCEPTED_TELE, false);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    public void Accept(View view) {

        String patientName = tinyDB.getString(Const.PATIENT_NAME);
        long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
        String key = patientName + patientId + "1";

        if (isTele) {
            tinyDB.putBoolean(key, true);
            Intent intent = new Intent();
            intent.putExtra(Const.ACCEPTED_TELE, true);
            setResult(RESULT_OK, intent);
            finish();

            return;
        }

        Intent intent = new Intent(this, BookAppointmentOneActivity.class);
        intent.putExtra(ISTELEMEDICINE_KEY, true);
        startActivity(intent);
        tinyDB.putBoolean(key, true);
        finish();
    }
}
