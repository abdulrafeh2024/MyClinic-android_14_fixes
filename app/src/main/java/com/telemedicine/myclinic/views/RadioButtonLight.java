package com.telemedicine.myclinic.views;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.telemedicine.myclinic.util.TextUtil;

/**
 * Created by Asad Abbas on 9/20/17.
 * Copyrights © 9/20/17 Ammad Ali. All rights reserved
 */

public class RadioButtonLight extends AppCompatRadioButton {

    public RadioButtonLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RadioButtonLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadioButtonLight(Context context) {
        super(context);
        init();
    }

    public void init() {
        String font = "GothamLight.otf";
        if (TextUtil.getEnglish(getContext()))
            font = "GothamLight.otf";
        else if (TextUtil.getArabic(getContext()))
            font = "GE_Thameen_Book.otf";

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), font);
        setTypeface(tf);
        invalidate();
    }

}