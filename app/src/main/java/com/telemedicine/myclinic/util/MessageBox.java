package com.telemedicine.myclinic.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import com.telemedicine.myclinic.myapplication.R;


public class MessageBox {

    public static boolean isDialogShow = false;

    public static Dialog showSplash(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        dialog.setContentView(R.layout.progress_dialogue);

        return dialog;
    }

}
