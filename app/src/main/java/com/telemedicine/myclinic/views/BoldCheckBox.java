package com.telemedicine.myclinic.views;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.util.AttributeSet;

import com.telemedicine.myclinic.util.TextUtil;

/**
 * Created by Asad Abbas on 9/20/17.
 * Copyrights © 9/20/17 Asad Abbas. All rights reserved
 */

public class BoldCheckBox extends AppCompatCheckBox {

    public BoldCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BoldCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoldCheckBox(Context context) {
        super(context);
        init();
    }

    public void init() {
        String font = "GothamMedium.otf";
        if (TextUtil.getEnglish(getContext()))
            font = "GothamMedium.otf";
        else if (TextUtil.getArabic(getContext()))
            font = "GE_Thameen_DemiBold.otf";

        setTypeface(Typeface.createFromAsset(getContext().getAssets(), font));
        invalidate();
    }

}