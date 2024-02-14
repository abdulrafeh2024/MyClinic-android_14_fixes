package com.telemedicine.myclinic.views;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.reginald.editspinner.EditSpinner;
import com.telemedicine.myclinic.util.TextUtil;

/**
 * Created by Asad Abbas on 9/20/17.
 * Copyrights © 9/20/17 Asad Abbas. All rights reserved
 */

public class LightSpinnerEdittext extends EditSpinner {

    public LightSpinnerEdittext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LightSpinnerEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LightSpinnerEdittext(Context context) {
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

    @Override
    public void selectItem(int index) {
        super.selectItem(index);
    }
}