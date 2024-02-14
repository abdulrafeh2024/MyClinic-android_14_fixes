package com.telemedicine.myclinic.views;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Asad Abbas on 9/20/17.
 * Copyrights © 9/20/17 Asad Abbas. All rights reserved
 */

public class EnglishRegularTextView extends AppCompatTextView {

    public EnglishRegularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EnglishRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EnglishRegularTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        String font = "GothamMedium.otf";

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), font);
        setTypeface(tf);
        invalidate();

    }

}