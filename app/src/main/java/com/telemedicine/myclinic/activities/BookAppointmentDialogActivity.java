package com.telemedicine.myclinic.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.telemedicine.myclinic.myapplication.ApptTermsConditionsActivity;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.views.RegularTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY;

public class BookAppointmentDialogActivity extends Activity {

    String termsCondition = "";

    @BindView(R.id.visit_clinic)
    LightTextView visitClinic;

    @BindView(R.id.tele_appt_btn)
    LightTextView teleApptBtn;

    @BindView(R.id.imageView_close)
    ImageView imageView_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextUtil.translation(this);

        setContentView(R.layout.activity_book_appointment_dialog);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ButterKnife.bind(this);
        viewTransform();

        Intent intent = getIntent();

        if (intent != null) {
            termsCondition = intent.getStringExtra(Const.TERMS_CONDITIONS_KEY);
        }

        this.setFinishOnTouchOutside(false);
        tinyDB = new TinyDB(this);

    }

    public void Close(View view) {
        finish();
    }

    TinyDB tinyDB;

    public void TeleAppt(View view) {

        /*boolean showDialog = tinyDB.getBoolean("showDialog");
        if (!showDialog)
            showDialog(true);
        else*/
        teleAppt();

    }

    void teleAppt() {

        String patientName = tinyDB.getString(Const.PATIENT_NAME);
        long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
        String key = patientName + patientId;

        boolean accepted = tinyDB.getBoolean(key);

        if (accepted) {
            Intent intent = new Intent(this, BookAppointmentOneActivity.class);
            intent.putExtra(ISTELEMEDICINE_KEY, true);
            startActivity(intent);
            finish();
            return;
        }

        Intent intent = new Intent(this, ApptTermsConditionsActivity.class);
        intent.putExtra(Const.TERMS_CONDITIONS_KEY, termsCondition);
        startActivity(intent);
        finish();
    }

    public void ClinicAppt(View view) {

        nonTeleAppt();
   /*     boolean showDialog = tinyDB.getBoolean("showDialog");

        if (!showDialog)
            showDialog(false);
        else
            nonTeleAppt();*/

    }

    void nonTeleAppt() {
        Intent intent = new Intent(this, BookAppointmentOneActivity.class);
        intent.putExtra(ISTELEMEDICINE_KEY, false);
        startActivity(intent);
        finish();
    }


    public void showDialog(boolean fromTele) {
        try {
            final Dialog dialog = new Dialog(this, R.style.NewDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.myclinic_dialogue);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            RegularTextView dontRemindAgain = (RegularTextView) dialog.findViewById(R.id.dont_remind);

            RegularTextView dialogButton = dialog.findViewById(R.id.cancel);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                    if (fromTele)
                        teleAppt();
                    else
                        nonTeleAppt();
                }
            });

            dontRemindAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tinyDB.putBoolean("showDialog", true);

                    dialog.dismiss();
                }
            });


            dialog.show();
        } catch (Exception e) {

        }
    }


    void viewTransform() {

        if (TextUtil.getArabic(this)) {
            visitClinic.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            visitClinic.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            teleApptBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            teleApptBtn.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

            FrameLayout.LayoutParams params4 = (FrameLayout.LayoutParams) imageView_close.getLayoutParams();
            params4.width = 78;
            params4.height = 78;
            params4.gravity = Gravity.TOP | Gravity.LEFT;
            // params4.setMargins(0, 40, 0, 0);  // left, top, right, bottom
            imageView_close.setLayoutParams(params4);

        } else if (TextUtil.getEnglish(this)) {
            visitClinic.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arrow, 0);
            visitClinic.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            teleApptBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arrow, 0);
            teleApptBtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

            FrameLayout.LayoutParams params4 = (FrameLayout.LayoutParams) imageView_close.getLayoutParams();
            params4.width = 78;
            params4.height = 78;
            params4.gravity = Gravity.TOP | Gravity.RIGHT;
            //params4.setMargins(0, 40, 0, 0);  // left, top, right, bottom
            imageView_close.setLayoutParams(params4);
        }

    }

}
