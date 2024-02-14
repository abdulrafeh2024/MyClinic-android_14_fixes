package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.TextUtil;

public class HelpAdapter extends PagerAdapter {

    public static int[] eng_help = {R.drawable.one_en, R.drawable.two_en,
            R.drawable.three_en,
            R.drawable.four_en, R.drawable.five_en,
            R.drawable.six_en};

    public static int[] ar_help = {R.drawable.six_ar, R.drawable.five_ar,
            R.drawable.four_ar,
            R.drawable.three_ar, R.drawable.two_ar,
            R.drawable.one_ar};

    private LayoutInflater inflater;
    private Activity _activity;

    public HelpAdapter(Activity activity) {

        try {
            this._activity = activity;
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
        return eng_help.length;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {

        View slidersLayout = inflater.inflate(R.layout.help_slider_list_item, view, false);

        assert slidersLayout != null;

        final ImageView imageView = slidersLayout
                .findViewById(R.id.imageview);

        if (TextUtil.getEnglish(_activity))
            Glide.with(_activity).load(eng_help[position]).into((ImageView) imageView);
        else{
            Glide.with(_activity).load(ar_help[position]).into((ImageView) imageView);
        }


        view.addView(slidersLayout);

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
