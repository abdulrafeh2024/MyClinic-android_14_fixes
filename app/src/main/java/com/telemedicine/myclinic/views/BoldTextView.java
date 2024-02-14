package com.telemedicine.myclinic.views;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.telemedicine.myclinic.util.TextUtil;

/**
 * Created by Asad Abbas on 9/20/17.
 * Copyrights © 9/20/17 Asad Abbas. All rights reserved
 */

public class BoldTextView extends AppCompatTextView {

    public BoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoldTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        String font = "GothamBold.otf";
        if (TextUtil.getEnglish(getContext()))
            font = "GothamBold.otf";
        else if (TextUtil.getArabic(getContext()))
            font = "GE_Thameen_DemiBold.otf";


        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), font);
        setTypeface(tf);
        invalidate();

    }

    public void setBackground(int circle) {
    }
}