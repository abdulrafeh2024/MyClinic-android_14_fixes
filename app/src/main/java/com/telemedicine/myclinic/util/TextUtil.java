package com.telemedicine.myclinic.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Button;
import android.widget.TextView;

import com.ligl.android.widget.iosdialog.IOSDialog;
import com.telemedicine.myclinic.models.onlineConfig.OpsConfigsModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.views.LightSpinnerEdittext;

import java.util.ArrayList;
import java.util.Locale;

public class TextUtil {

    public static String getValue(TextInputLayout textInputLayout, AppCompatEditText text) {

        return text.getText().toString();
    }

    public static void tILErrorOne(Activity activity, TextInputLayout textInputLayout, LightSpinnerEdittext text) {

        CharSequence str = textInputLayout.getHint();
        if (ValidationHelper.isNullOrEmpty(str))
            str = text.getHint();
        textInputLayout.setBackground(activity.getDrawable(R.drawable.top_border_red));
        textInputLayout.setHintTextAppearance(R.style.redHint);
        text.setHintTextColor(activity.getResources().getColor(R.color.colorred));
        text.setHint(str);

        if (text.hasFocus()) {
            textInputLayout.setHint(str);
            text.setHint("");
        } else {
            textInputLayout.setHint("");
            text.setHint(str);
        }

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputLayout.setBackground(activity.getDrawable(R.drawable.top_border_space_white_bg));
                textInputLayout.setHintTextAppearance(R.style.primaryHint);
            }
        });

        CharSequence finalStr = str;
        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textInputLayout.setHint(finalStr);
                    text.setHint("");
                }
            }
        });

    }


    public static void tILErrorTwo(Activity activity, TextInputLayout textInputLayout, LightSpinnerEdittext text) {

        CharSequence str = textInputLayout.getHint();
        if (ValidationHelper.isNullOrEmpty(str))
            str = text.getHint();
        textInputLayout.setBackground(activity.getDrawable(R.drawable.red_border_ng));
        textInputLayout.setHintTextAppearance(R.style.redHint);
        text.setHintTextColor(activity.getResources().getColor(R.color.colorred));
        text.setHint(str);

        if (text.hasFocus()) {
            textInputLayout.setHint(str);
            text.setHint("");
        } else {
            textInputLayout.setHint("");
            text.setHint(str);
        }

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputLayout.setBackground(activity.getDrawable(R.drawable.grey_border));
                textInputLayout.setHintTextAppearance(R.style.primaryHint);
            }
        });

        CharSequence finalStr = str;
        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textInputLayout.setHint(finalStr);
                    text.setHint("");
                }
            }
        });

    }


    public static void tILErrorOne(Activity activity, TextInputLayout textInputLayout, AppCompatEditText text) {

        CharSequence str = textInputLayout.getHint();
        if (ValidationHelper.isNullOrEmpty(str))
            str = text.getHint();
        textInputLayout.setBackground(activity.getDrawable(R.drawable.top_border_red));
        textInputLayout.setHintTextAppearance(R.style.redHint);
        text.setHintTextColor(activity.getResources().getColor(R.color.colorred));
        text.setHint(str);

        if (text.hasFocus()) {
            textInputLayout.setHint(str);
            text.setHint("");
        } else {
            textInputLayout.setHint("");
            text.setHint(str);
        }

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputLayout.setBackground(activity.getDrawable(R.drawable.top_border_space_white_bg));
                textInputLayout.setHintTextAppearance(R.style.primaryHint);
            }
        });

        CharSequence finalStr = str;
        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textInputLayout.setHint(finalStr);
                    text.setHint("");
                }
            }
        });

    }

    public static void tILError(Activity activity, TextInputLayout textInputLayout, LightSpinnerEdittext text) {

        CharSequence str = textInputLayout.getHint();
        if (ValidationHelper.isNullOrEmpty(str))
            str = text.getHint();
        textInputLayout.setBackground(activity.getDrawable(R.drawable.red_border));
        textInputLayout.setHintTextAppearance(R.style.redHint);
        text.setHintTextColor(activity.getResources().getColor(R.color.colorred));
        text.setHint(str);

        if (text.hasFocus()) {
            textInputLayout.setHint(str);
            text.setHint("");
        } else {
            textInputLayout.setHint("");
            text.setHint(str);
        }

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputLayout.setBackground(activity.getDrawable(R.drawable.grey_border));
                textInputLayout.setHintTextAppearance(R.style.primaryHint);
            }
        });

        CharSequence finalStr = str;
        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textInputLayout.setHint(finalStr);
                    text.setHint("");
                }
            }
        });

    }

    public static void tILError(Activity activity, TextInputLayout textInputLayout) {
        CharSequence str = textInputLayout.getHint();
        if (ValidationHelper.isNullOrEmpty(str))
            str = textInputLayout.getEditText().getHint();
        textInputLayout.setBackground(activity.getDrawable(R.drawable.red_border));
        textInputLayout.setHintTextAppearance(R.style.redHint);
        textInputLayout.getEditText().setHintTextColor(activity.getResources().getColor(R.color.colorred));
        textInputLayout.getEditText().setHint(str);
        if (textInputLayout.getEditText().hasFocus()) {
            textInputLayout.setHint(str);
            textInputLayout.getEditText().setHint("");
        } else {
            textInputLayout.setHint("");
            textInputLayout.getEditText().setHint(str);
        }
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputLayout.setBackground(activity.getDrawable(R.drawable.grey_border));
                textInputLayout.setHintTextAppearance(R.style.primaryHint);

            }
        });

        CharSequence finalStr = str;
        textInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textInputLayout.setHint(finalStr);
                    textInputLayout.getEditText().setHint("");
                }
            }
        });
    }

    public static void tILError(Activity activity, TextInputLayout textInputLayout, AppCompatEditText text) {
        CharSequence str = textInputLayout.getHint();
        if (ValidationHelper.isNullOrEmpty(str))
            str = text.getHint();
        textInputLayout.setBackground(activity.getDrawable(R.drawable.red_border));
        textInputLayout.setHintTextAppearance(R.style.redHint);
        text.setHintTextColor(activity.getResources().getColor(R.color.colorred));
        text.setHint(str);
        if (text.hasFocus()) {
            textInputLayout.setHint(str);
            text.setHint("");
        } else {
            textInputLayout.setHint("");
            text.setHint(str);
        }
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputLayout.setBackground(activity.getDrawable(R.drawable.grey_border));
                textInputLayout.setHintTextAppearance(R.style.primaryHint);

            }
        });

        CharSequence finalStr = str;
        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textInputLayout.setHint(finalStr);
                   text.setHint("");
                }
            }
        });
    }


    public static void tILErrorName(Activity activity, TextInputLayout textInputLayout, AppCompatEditText text) {
        CharSequence str = textInputLayout.getHint();
        if (ValidationHelper.isNullOrEmpty(str))
            str = text.getHint();
        textInputLayout.setBackground(activity.getDrawable(R.drawable.red_border));
        textInputLayout.setHintTextAppearance(R.style.redHint);
        text.setHintTextColor(activity.getResources().getColor(R.color.colorred));
        text.setHint(str);
        if (text.hasFocus()) {
            textInputLayout.setHint(str);
            text.setHint("");
        } else {
            textInputLayout.setHint("");
            text.setHint(str);
        }
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtil.getArabic(activity)) {
                    String pattern = "^[A-Za-z0-9. ]+$";
                    if (s.toString().matches(pattern)) {
                        text.setText("");
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputLayout.setBackground(activity.getDrawable(R.drawable.grey_border));
                textInputLayout.setHintTextAppearance(R.style.primaryHint);

            }
        });

        CharSequence finalStr = str;
        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textInputLayout.setHint(finalStr);
                    text.setHint("");
                }
            }
        });
    }

    public static String urlStart(TinyDB tinyDB) {

        String url = "";

        ArrayList<Object> opsConfig = tinyDB.getListObject(Const.ONLINE_CONFIG_KEY, OpsConfigsModel.class);

        if (opsConfig != null) {

            for (Object object : opsConfig) {

                OpsConfigsModel opsConfigsModel = (OpsConfigsModel) object;

                if (opsConfigsModel != null) {

                    if (opsConfigsModel.getOpsConfigId() == 2 && opsConfigsModel.getKey().equalsIgnoreCase("Hyperpay Prefix Url")) {

                        url = opsConfigsModel.getVal();

                    }
                }
            }
        }

        return url;
    }


    public static String getAfterTime(TinyDB tinyDB) {

        String time = "";

        ArrayList<Object> opsConfig = tinyDB.getListObject(Const.ONLINE_CONFIG_KEY, OpsConfigsModel.class);

        if (opsConfig != null) {

            for (Object object : opsConfig) {

                OpsConfigsModel opsConfigsModel = (OpsConfigsModel) object;

                if (opsConfigsModel != null) {

                    if (opsConfigsModel.getKey().equalsIgnoreCase("Telemedicine CheckedIn Before Time")) {


                        time = opsConfigsModel.getVal();
                        time = time.replace("\"", "");
                    }
                }
            }
        }

        return time;
    }

    public static boolean getEnglish(Context activity) {

        String locality = new TinyDB(activity).getString(Const.LOCALITY);
        boolean flag = false;
        if (locality.equalsIgnoreCase("1"))
            flag = true;
        return flag;
    }

    public static boolean getArabic(Context mC) {
        String locality = new TinyDB(mC).getString(Const.LOCALITY);
        boolean flag = false;
        if (locality.equalsIgnoreCase("2"))
            flag = true;
        return flag;
    }

    public static void setLocality(Locale locality, Context mC) {

        Resources resources = mC.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locality);
            configuration.setLayoutDirection(locality);
        } else {
            configuration.locale = locality;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mC.createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, displayMetrics);
        }
    }

    public static void arabicNavigation(Activity activity) {

        if (getArabic(activity)) {
            LocaleUtils.initialize(activity, LocaleUtils.ARABIC);
            activity.setTheme(R.style.FullScreenAppThemeAr);
        } else if (getEnglish(activity)) {
            LocaleUtils.initialize(activity, LocaleUtils.ENGLISH);
            activity.setTheme(R.style.FullScreenAppTheme);
        }
    }

    public static void arabicNavigation1(Activity activity) {

        if (getArabic(activity)) {
            LocaleUtils.initialize(activity, LocaleUtils.ARABIC);
            activity.setTheme(R.style.FullScreenAppThemeAr1);
        } else if (getEnglish(activity)) {
            LocaleUtils.initialize(activity, LocaleUtils.ENGLISH);
            activity.setTheme(R.style.FullScreenAppTheme1);
        }
    }

    public static void translation(Activity activity) {

        if (getArabic(activity)) {
            LocaleUtils.initialize(activity, LocaleUtils.ARABIC);

        } else if (getEnglish(activity)) {
            LocaleUtils.initialize(activity, LocaleUtils.ENGLISH);

        }
    }

    public static void dialgoue(Activity activity, String msg) {
        IOSDialog dialogView = new IOSDialog.Builder(activity)
                .setTitle(R.string.my_clininc)
                .setMessage(msg).show();
        String font = "GothamMedium.otf";
        String font_bold = "GothamBold.otf";

        Button btnConfirm = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.confirm_btn);
        btnConfirm.setText(R.string.ok);

        TextView tvTitle = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.title);
        TextView tvMessage = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.message);
        Typeface tf = Typeface.createFromAsset(activity.getAssets(), font);
        Typeface tf_title = Typeface.createFromAsset(activity.getAssets(), font_bold);
        btnConfirm.setTypeface(tf);
        btnConfirm.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        tvTitle.setTypeface(tf_title);
        tvMessage.setTypeface(tf);
        tvMessage.setTextColor(activity.getResources().getColor(R.color.black));
    }

    public static IOSDialog dialgouePayment(Activity activity, String msg, DialogInterface.OnClickListener onClickListener,
                                            DialogInterface.OnClickListener onClickNo) {
        IOSDialog dialogView = new IOSDialog.Builder(activity)
                .setTitle(R.string.my_clininc)
                .setPositiveButton(R.string.yes, onClickListener)
                .setNegativeButton(R.string.no,null)
                .setMessage(msg).show();

        String font = "GothamMedium.otf";

        if (TextUtil.getEnglish(activity))
            font = "GothamMedium.otf";
        else if (TextUtil.getArabic(activity))
            font = "GE_Thameen_Book.otf";

        String font_bold = "GothamBold.otf";

        if (TextUtil.getEnglish(activity))
            font_bold = "GothamBold.otf";
        else if (TextUtil.getArabic(activity))
            font_bold = "GE_Thameen_DemiBold.otf";




        Button btnConfirm = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.confirm_btn);
        btnConfirm.setText(R.string._continue);

        Button btnCancel = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.cancel_btn);
        btnCancel.setText(R.string.cancel);

        TextView tvTitle = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.title);
        TextView tvMessage = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.message);

        Typeface tf = Typeface.createFromAsset(activity.getAssets(), font);

        Typeface tf_title = Typeface.createFromAsset(activity.getAssets(), font_bold);

        btnConfirm.setTypeface(tf);
        btnConfirm.setTextColor(activity.getResources().getColor(R.color.colorPrimary));

        btnCancel.setTypeface(tf);
        btnCancel.setTextColor(activity.getResources().getColor(R.color.colorPrimary));

        tvTitle.setTypeface(tf_title);
        tvMessage.setTypeface(tf);

        tvMessage.setTextColor(activity.getResources().getColor(R.color.black));
//        btnConfirm.setOnClickListener(onClickListener);
       // btnCancel.setOnClickListener(null);

        return dialogView;

    }

    public static IOSDialog dialogCardSaving(Activity activity, String msg, DialogInterface.OnClickListener onClickYes,
                                             DialogInterface.OnClickListener onClickNo) {
        IOSDialog dialogView = new IOSDialog.Builder(activity)
                .setTitle(R.string.my_clininc)
                .setPositiveButton(R.string.yes, onClickYes)
                .setNegativeButton(R.string.no,onClickNo)
                .setMessage(msg).show();

        String font = "GothamMedium.otf";

        if (TextUtil.getEnglish(activity))
            font = "GothamMedium.otf";
        else if (TextUtil.getArabic(activity))
            font = "GE_Thameen_Book.otf";

        String font_bold = "GothamBold.otf";

        if (TextUtil.getEnglish(activity))
            font_bold = "GothamBold.otf";
        else if (TextUtil.getArabic(activity))
            font_bold = "GE_Thameen_DemiBold.otf";

        Button btnConfirm = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.confirm_btn);
        btnConfirm.setText(R.string.yes);

        Button btnCancel = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.cancel_btn);
        btnCancel.setText(R.string.no);

        TextView tvTitle = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.title);
        TextView tvMessage = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.message);

        Typeface tf = Typeface.createFromAsset(activity.getAssets(), font);

        Typeface tf_title = Typeface.createFromAsset(activity.getAssets(), font_bold);

        btnConfirm.setTypeface(tf);
        btnConfirm.setTextColor(activity.getResources().getColor(R.color.colorPrimary));

        btnCancel.setTypeface(tf);
        btnCancel.setTextColor(activity.getResources().getColor(R.color.colorPrimary));

        tvTitle.setTypeface(tf_title);
        tvMessage.setTypeface(tf);

        tvMessage.setTextColor(activity.getResources().getColor(R.color.black));
//        btnConfirm.setOnClickListener(onClickListener);
        // btnCancel.setOnClickListener(null);

        return dialogView;

    }

    public static IOSDialog dialgoue(Activity activity, String msg, View.OnClickListener onClickListener) {
        IOSDialog dialogView = new IOSDialog.Builder(activity)
                .setTitle(R.string.my_clininc)
                .setCancelable(false)
                .setMessage(msg).show();
        String font = "GothamMedium.otf";
        String font_bold = "GothamBold.otf";

        Button btnConfirm = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.confirm_btn);
        btnConfirm.setText(R.string.ok);

        TextView tvTitle = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.title);
        TextView tvMessage = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.message);
        Typeface tf = Typeface.createFromAsset(activity.getAssets(), font);
        Typeface tf_title = Typeface.createFromAsset(activity.getAssets(), font_bold);
        btnConfirm.setTypeface(tf);
        btnConfirm.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        tvTitle.setTypeface(tf_title);
        tvMessage.setTypeface(tf);
        tvMessage.setTextColor(activity.getResources().getColor(R.color.black));
        btnConfirm.setOnClickListener(onClickListener);
        return dialogView;

    }

    public static IOSDialog dialgoue(Activity activity, String msg,String btnText, View.OnClickListener onClickListener) {
        IOSDialog dialogView = new IOSDialog.Builder(activity)
                .setTitle(R.string.my_clininc)
                .setCancelable(false)
                .setMessage(msg).show();
        String font = "GothamMedium.otf";
        String font_bold = "GothamBold.otf";

        Button btnConfirm = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.confirm_btn);
        btnConfirm.setText(btnText);

        TextView tvTitle = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.title);
        TextView tvMessage = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.message);
        Typeface tf = Typeface.createFromAsset(activity.getAssets(), font);
        Typeface tf_title = Typeface.createFromAsset(activity.getAssets(), font_bold);
        btnConfirm.setTypeface(tf);
        btnConfirm.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        tvTitle.setTypeface(tf_title);
        tvMessage.setTypeface(tf);
        tvMessage.setTextColor(activity.getResources().getColor(R.color.black));
        btnConfirm.setOnClickListener(onClickListener);
        return dialogView;

    }

    public static IOSDialog dialgoueSuccessBooking(Activity activity, String msg, View.OnClickListener onClickListener) {
        IOSDialog dialogView = new IOSDialog.Builder(activity)
                .setTitle(R.string.appt_booked_successfully_title)
                .setCancelable(false)
                .setMessage(msg).show();
        String font = "GothamMedium.otf";
        String font_bold = "GothamBold.otf";

        Button btnConfirm = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.confirm_btn);
        btnConfirm.setText(R.string.ok);

        TextView tvTitle = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.title);
        TextView tvMessage = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.message);
        Typeface tf = Typeface.createFromAsset(activity.getAssets(), font);
        Typeface tf_title = Typeface.createFromAsset(activity.getAssets(), font_bold);
        btnConfirm.setTypeface(tf);
        btnConfirm.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        tvTitle.setTypeface(tf_title);
        tvMessage.setTypeface(tf);
        tvMessage.setTextColor(activity.getResources().getColor(R.color.black));
        btnConfirm.setOnClickListener(onClickListener);
        return dialogView;

    }

    public static IOSDialog dialgoueReportSuccessBooking(Activity activity, String msg, View.OnClickListener onClickListener) {
        IOSDialog dialogView = new IOSDialog.Builder(activity)
                .setTitle(R.string.my_clininc)
                .setCancelable(false)
                .setMessage(msg).show();
        String font = "GothamMedium.otf";
        String font_bold = "GothamBold.otf";

        Button btnConfirm = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.confirm_btn);
        btnConfirm.setText(R.string.ok);

        TextView tvTitle = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.title);
        TextView tvMessage = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.message);
        Typeface tf = Typeface.createFromAsset(activity.getAssets(), font);
        Typeface tf_title = Typeface.createFromAsset(activity.getAssets(), font_bold);
        btnConfirm.setTypeface(tf);
        btnConfirm.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        tvTitle.setTypeface(tf_title);
        tvMessage.setTypeface(tf);
        tvMessage.setTextColor(activity.getResources().getColor(R.color.black));
        btnConfirm.setOnClickListener(onClickListener);
        return dialogView;

    }

    public static IOSDialog dialgoueNonTeleSuccessBooking(Activity activity, String msg, View.OnClickListener onClickListener) {
        IOSDialog dialogView = new IOSDialog.Builder(activity)
                .setTitle(R.string.my_clininc)
                .setCancelable(false)
                .setMessage(msg).show();
        String font = "GothamMedium.otf";
        String font_bold = "GothamBold.otf";

        Button btnConfirm = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.confirm_btn);
        btnConfirm.setText(R.string.ok);

        TextView tvTitle = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.title);
        TextView tvMessage = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.message);
        Typeface tf = Typeface.createFromAsset(activity.getAssets(), font);
        Typeface tf_title = Typeface.createFromAsset(activity.getAssets(), font_bold);
        btnConfirm.setTypeface(tf);
        btnConfirm.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        tvTitle.setTypeface(tf_title);
        tvMessage.setTypeface(tf);
        tvMessage.setTextColor(activity.getResources().getColor(R.color.black));
        btnConfirm.setOnClickListener(onClickListener);
        return dialogView;

    }

    public static void dialgoueAppt(Activity activity, String msg) {
        IOSDialog dialogView = new IOSDialog.Builder(activity)
                .setTitle(R.string.my_clininc)
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .setNegativeButton("Do not remind me again", null).show();

        String font = "GothamMedium.otf";
        String font_bold = "GothamBold.otf";

        Button btnConfirm = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.confirm_btn);
        btnConfirm.setText(R.string.ok);

        Button cancel = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.cancel_btn);
        cancel.setText("Do not remind me again");

        TextView tvTitle = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.title);
        TextView tvMessage = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.message);
        Typeface tf = Typeface.createFromAsset(activity.getAssets(), font);
        Typeface tf_title = Typeface.createFromAsset(activity.getAssets(), font_bold);
        btnConfirm.setTypeface(tf);
        btnConfirm.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        cancel.setTypeface(tf);
        cancel.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        tvTitle.setTypeface(tf_title);
        tvMessage.setTypeface(tf);
        tvMessage.setTextColor(activity.getResources().getColor(R.color.black));

    }


    public static String getKeyboardLocale(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        InputMethodSubtype ims = imm.getCurrentInputMethodSubtype();
        String localeString = ims.getLocale();
        Locale locale = new Locale(localeString);
        String currentLanguage = locale.getDisplayLanguage();

        return currentLanguage;
    }

    public static String name;
    public static String speciality;
}
