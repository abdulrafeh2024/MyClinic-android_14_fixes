package com.telemedicine.myclinic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

import com.telemedicine.myclinic.activities.BaseActivity;
import com.telemedicine.myclinic.myapplication.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Asad Abbas on 4/24/18.
 * Copyrights © 4/24/18 Asad Abbas. All rights reserved
 */

public class UtilProgressBar extends BaseActivity {
    static SweetAlertDialog pDialog = null;

    public static void dialogueShow(Activity activity, String message) {
        pDialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#21ABE6"));
        pDialog.setTitleText(message);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public static void disMissDialogue() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }


    public static void showErrorMessage(Activity activity, String message) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(message)
                        .show();
            }
        });

    }

    public static void successMessage(Activity activity, String message) {
        new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(message).setConfirmText(activity.getString(R.string.ok))
                .show();
    }


    public static void yesNoDialogue(final Activity activity, String message, SweetAlertDialog.OnSweetClickListener listener) {
        new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE).setConfirmText(activity.getString(R.string.yes)).setConfirmClickListener(listener)
                .setCancelText(activity.getString(R.string.no)).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        }).setTitleText(activity.getResources().getString(R.string.my_clininc)).setContentText(message).show();
    }

    public static void closeDialogue(final Activity activity, String message, SweetAlertDialog.OnSweetClickListener listener) {
        new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE).setConfirmText(activity.getResources().getString(R.string.ok)).setConfirmClickListener(listener)
                .setTitleText(activity.getResources().getString(R.string.my_clininc)).setContentText(message).show();
    }


    public static void continueCancelDialogue(final Activity activity, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setTitle("My Clinic");

        builder1.setPositiveButton("Continue", listener);

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

        TextView textView = alert11.findViewById(android.R.id.message);

        Typeface face = Typeface.createFromAsset(activity.getAssets(), "GothamMedium.ttf");
        textView.setTypeface(face);

        Button button1 = (Button) alert11.getWindow().findViewById(android.R.id.button1);
        Button button2 = (Button) alert11.getWindow().findViewById(android.R.id.button2);
        button1.setTypeface(face);
        button2.setTypeface(face);


    }


    @Override
    protected int layout() {
        return 0;
    }

}
