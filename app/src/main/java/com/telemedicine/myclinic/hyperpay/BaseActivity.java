package com.telemedicine.myclinic.hyperpay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.MessageBox;


@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    protected void showProgressDialog(int messageId) {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
        }

        progressDialog.setMessage(getString(messageId));
        progressDialog.show();
    }

    protected void hideProgressDialog() {
        if (progressDialog == null) {
            return;
        }

        progressDialog.dismiss();
    }

    protected void showAlertDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(R.string.button_ok, null)
                .setCancelable(false)
                .show();
    }

    protected void showAlertDialog(int messageId) {
        showAlertDialog(getString(messageId));
    }

    private Dialog dialog;

    public void showWaitDialog() {
        if (dialog != null && dialog.isShowing()) {

        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog = MessageBox.showSplash(BaseActivity.this);
                }
            });
        }
    }

    public void hideWaitDialog() {
        if (dialog != null && dialog.isShowing()) {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });

            } catch (Exception e) {

            }
        }
    }
}
