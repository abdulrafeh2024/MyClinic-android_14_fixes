package com.telemedicine.myclinic.views;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.telemedicine.myclinic.util.TextUtil;

/**
 * Created by Asad Abbas on 9/20/17.
 * Copyrights © 9/20/17 Asad Abbas. All rights reserved
 */

public class AutoCompleteTextView extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {

    private final static int MINIMAL_HEIGHT = 50;

    public AutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoCompleteTextView(Context context) {
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

    @Override
    public void showDropDown() {
        Rect displayFrame = new Rect();
        getWindowVisibleDisplayFrame(displayFrame);

        int[] locationOnScreen = new int[2];
        getLocationOnScreen(locationOnScreen);

        int bottom = locationOnScreen[1] + getHeight();
        int availableHeightBelow = displayFrame.bottom - bottom;
        if (availableHeightBelow >= MINIMAL_HEIGHT) {
            setDropDownHeight(availableHeightBelow);
        }

        super.showDropDown();
    }

}