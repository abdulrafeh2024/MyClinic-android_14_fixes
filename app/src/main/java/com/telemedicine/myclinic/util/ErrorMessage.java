package com.telemedicine.myclinic.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.irozon.sneaker.Sneaker;
import com.telemedicine.myclinic.activities.UpcomingAppointmentsActivity;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.views.RegularTextView;
import com.tooltip.Tooltip;

public class ErrorMessage {
    static ErrorMessage instance = null;

    public ErrorMessage() {

    }

    public static ErrorMessage getInstance() {

        if (instance == null) {
            instance = new ErrorMessage();
        }
        return instance;
    }

    public void showError(Activity activity, String errorMessage) {
       /* Sneaker.with(activity) // Activity, Fragment or ViewGroup
                .setTitle(activity.getString(R.string.my_clininc), R.color.colorWhite).autoHide(true)
                .setMessage(errorMessage, R.color.colorWhite).setTypeface(Typeface.createFromAsset(activity.getAssets(), "GothamBold.ttf"))
                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Height of the Sneaker layout
                .setIcon(R.drawable.close_circle_outline, true).sneak(R.color.colorred);
*/
        Sneaker sneaker = Sneaker.with(activity);
        sneaker.autoHide(true);
        sneaker.setDuration(10000);
        View view = null;
        if (TextUtil.getArabic(activity))
            view = LayoutInflater.from(activity).inflate(R.layout.error_layout_ar, sneaker.getView(), false);
        else if (TextUtil.getEnglish(activity))
            view = LayoutInflater.from(activity).inflate(R.layout.error_layout, sneaker.getView(), false);

        // Your custom view code
        RegularTextView msg = view.findViewById(R.id.msg);
        msg.setText(errorMessage);
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sneaker.hide();
            }
        });

        ImageView close = view.findViewById(R.id.img);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sneaker.hide();
            }
        });

        RegularTextView contactNo = view.findViewById(R.id.textContact);
        contactNo.setOnClickListener(view1 -> {
            // Use format with "tel:" and phoneNumber created is
            // stored in u.
            Uri u = Uri.parse("tel: 92 00 22 811");

            // Create the intent and set the data for the
            // intent as the phone number.
            Intent i = new Intent(Intent.ACTION_DIAL, u);

            try
            {
                // Launch the Phone app's dialer with a phone
                // number to dial a call.
                activity.startActivity(i);
            }
            catch (SecurityException s)
            {
                Log.e("ErrorMessage","Error "+s.getMessage());
            }
        });
        sneaker.sneakCustom(view);
    }

    public void showErrorYellow(Activity activity, String errorMessage) {
       /* Sneaker.with(activity) // Activity, Fragment or ViewGroup
                .setTitle(activity.getString(R.string.my_clininc), R.color.colorWhite).autoHide(true)
                .setMessage(errorMessage, R.color.colorWhite).setTypeface(Typeface.createFromAsset(activity.getAssets(), "GothamBold.ttf"))
                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Height of the Sneaker layout
                .setIcon(R.drawable.close_circle_outline, true).sneak(R.color.colorred);
*/
        Sneaker sneaker = Sneaker.with(activity);
        sneaker.autoHide(true);
        sneaker.setDuration(10000);
        View view = null;
        if (TextUtil.getArabic(activity))
            view = LayoutInflater.from(activity).inflate(R.layout.error_layout_ar, sneaker.getView(), false);
        else if (TextUtil.getEnglish(activity))
            view = LayoutInflater.from(activity).inflate(R.layout.error_layout, sneaker.getView(), false);

        // Your custom view code
        RegularTextView msg = view.findViewById(R.id.msg);
        msg.setText(errorMessage);
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sneaker.hide();
            }
        });

        ImageView close = view.findViewById(R.id.img);
        ConstraintLayout mainLayout = view.findViewById(R.id.main_layout_error);

        mainLayout.setBackgroundResource(R.drawable.yellow_rounded);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sneaker.hide();
            }
        });

        RegularTextView contactNo = view.findViewById(R.id.textContact);
        contactNo.setOnClickListener(view1 -> {
            // Use format with "tel:" and phoneNumber created is
            // stored in u.
            Uri u = Uri.parse("tel: 92 00 22 811");

            // Create the intent and set the data for the
            // intent as the phone number.
            Intent i = new Intent(Intent.ACTION_DIAL, u);

            try
            {
                // Launch the Phone app's dialer with a phone
                // number to dial a call.
                activity.startActivity(i);
            }
            catch (SecurityException s)
            {
                Log.e("ErrorMessage","Error "+s.getMessage());
            }
        });
        sneaker.sneakCustom(view);
    }

    public void showSuccessGreen(Activity activity, String errorMessage) {
       /* Sneaker.with(activity) // Activity, Fragment or ViewGroup
                .setTitle(activity.getString(R.string.my_clininc), R.color.colorWhite).autoHide(true)
                .setMessage(errorMessage, R.color.colorWhite).setTypeface(Typeface.createFromAsset(activity.getAssets(), "GothamBold.ttf"))
                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Height of the Sneaker layout
                .setIcon(R.drawable.close_circle_outline, true).sneak(R.color.colorred);
*/
        Sneaker sneaker = Sneaker.with(activity);
        sneaker.autoHide(true);
        sneaker.setDuration(10000);
        View view = null;
        if (TextUtil.getArabic(activity))
            view = LayoutInflater.from(activity).inflate(R.layout.error_layout_ar, sneaker.getView(), false);
        else if (TextUtil.getEnglish(activity))
            view = LayoutInflater.from(activity).inflate(R.layout.error_layout, sneaker.getView(), false);

        // Your custom view code
        RegularTextView msg = view.findViewById(R.id.msg);
        msg.setText(errorMessage);
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sneaker.hide();
            }
        });

        ImageView close = view.findViewById(R.id.img);
        ConstraintLayout mainLayout = view.findViewById(R.id.main_layout_error);

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sneaker.hide();
                Intent intent = new Intent(activity, UpcomingAppointmentsActivity.class);
                //.putBoolean(Const.BACK_TO_HOME, false);
                activity.startActivity(intent);
            }
        });

        mainLayout.setBackgroundResource(R.drawable.green_rounded_bg);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sneaker.hide();
            }
        });

        RegularTextView contactNo = view.findViewById(R.id.textContact);
        contactNo.setOnClickListener(view1 -> {
            // Use format with "tel:" and phoneNumber created is
            // stored in u.
            Uri u = Uri.parse("tel: 92 00 22 811");

            // Create the intent and set the data for the
            // intent as the phone number.
            Intent i = new Intent(Intent.ACTION_DIAL, u);

            try
            {
                // Launch the Phone app's dialer with a phone
                // number to dial a call.
                activity.startActivity(i);
            }
            catch (SecurityException s)
            {
                Log.e("ErrorMessage","Error "+s.getMessage());
            }
        });
        sneaker.sneakCustom(view);
    }

    public void showSuccessWithoutNavigationGreen(Activity activity, String errorMessage) {
       /* Sneaker.with(activity) // Activity, Fragment or ViewGroup
                .setTitle(activity.getString(R.string.my_clininc), R.color.colorWhite).autoHide(true)
                .setMessage(errorMessage, R.color.colorWhite).setTypeface(Typeface.createFromAsset(activity.getAssets(), "GothamBold.ttf"))
                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Height of the Sneaker layout
                .setIcon(R.drawable.close_circle_outline, true).sneak(R.color.colorred);
*/
        Sneaker sneaker = Sneaker.with(activity);
        sneaker.autoHide(true);
        sneaker.setDuration(10000);
        View view = null;
        if (TextUtil.getArabic(activity))
            view = LayoutInflater.from(activity).inflate(R.layout.error_layout_ar, sneaker.getView(), false);
        else if (TextUtil.getEnglish(activity))
            view = LayoutInflater.from(activity).inflate(R.layout.error_layout, sneaker.getView(), false);

        // Your custom view code
        RegularTextView msg = view.findViewById(R.id.msg);
        msg.setText(errorMessage);
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sneaker.hide();
            }
        });

        ImageView close = view.findViewById(R.id.img);
        ConstraintLayout mainLayout = view.findViewById(R.id.main_layout_error);


        mainLayout.setBackgroundResource(R.drawable.green_rounded_bg);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sneaker.hide();
            }
        });

        RegularTextView contactNo = view.findViewById(R.id.textContact);
        contactNo.setOnClickListener(view1 -> {
            // Use format with "tel:" and phoneNumber created is
            // stored in u.
            Uri u = Uri.parse("tel: 92 00 22 811");

            // Create the intent and set the data for the
            // intent as the phone number.
            Intent i = new Intent(Intent.ACTION_DIAL, u);

            try
            {
                // Launch the Phone app's dialer with a phone
                // number to dial a call.
                activity.startActivity(i);
            }
            catch (SecurityException s)
            {
                Log.e("ErrorMessage","Error "+s.getMessage());
            }
        });
        sneaker.sneakCustom(view);
    }

    public void showWarning(Activity activity, String errorMessage) {
        showError(activity, errorMessage);
    }

    public void showValidationMessage(Activity activity, View view, String msg) {

        showError(activity, msg);
        hideKeyboard(activity, view);
        /*Tooltip tooltip = new Tooltip.Builder(view, R.style.Tooltip).setTypeface(Typeface.createFromAsset(activity.getAssets(), "GothamBold.ttf"))
                .setText(msg).setCancelable(true).show();*/
    }

    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
