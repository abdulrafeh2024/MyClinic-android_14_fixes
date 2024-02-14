package com.telemedicine.myclinic.views;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.telemedicine.myclinic.util.TextUtil;

/**
 * Created by Asad Abbas on 9/20/17.
 * Copyrights © 9/20/17 Ammad Ali. All rights reserved
 */

public class RegularTextView extends AppCompatTextView {

    public RegularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RegularTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        String font = "GothamMedium.otf";
        if (TextUtil.getEnglish(getContext()))
            font = "GothamMedium.otf";
        else if (TextUtil.getArabic(getContext()))
            font = "GE_Thameen_DemiBold.otf";

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), font);
        setTypeface(tf);
        invalidate();

    }

}