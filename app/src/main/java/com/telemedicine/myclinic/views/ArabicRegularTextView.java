package com.telemedicine.myclinic.views;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Asad Abbas on 9/20/17.
 * Copyrights © 9/20/17 Asad Abbas. All rights reserved
 */

public class ArabicRegularTextView extends AppCompatTextView {

    public ArabicRegularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ArabicRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArabicRegularTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        String font = "";
        font = "GE_Thameen_DemiBold.otf";

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), font);
        setTypeface(tf);
        invalidate();

    }

}