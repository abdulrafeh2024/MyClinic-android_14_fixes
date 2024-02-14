package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telemedicine.myclinic.models.homemodels.PatientComments;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightTextView;

import java.util.List;

public class TextPatientViewAdapter extends PagerAdapter {


    private List<PatientComments> patientModelList;
    private LayoutInflater inflater;
    private Activity _activity;

    public TextPatientViewAdapter(Activity activity, List<PatientComments> patientModelList) {

        try {

            this._activity = activity;
            this.patientModelList = patientModelList;
            inflater = LayoutInflater.from(activity);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return patientModelList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View slidersLayout = inflater.inflate(R.layout.patient_view_item_list, view, false);

        assert slidersLayout != null;

        final BoldTextView title = slidersLayout
                .findViewById(R.id.title);

        final LightTextView value = slidersLayout
                .findViewById(R.id.value);

        String valueStr = "";

        if (TextUtil.getEnglish(_activity)){
            valueStr = patientModelList.get(position).getCommentEn();
            title.setText("Our patient say");
        }
        else if (TextUtil.getArabic(_activity)){
            valueStr = patientModelList.get(position).getCommentAr();
            title.setText("آراء مراجعينا");
        }

        if (!ValidationHelper.isNullOrEmpty(valueStr))
            value.setText(valueStr);

        view.addView(slidersLayout, 0);

        return slidersLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
