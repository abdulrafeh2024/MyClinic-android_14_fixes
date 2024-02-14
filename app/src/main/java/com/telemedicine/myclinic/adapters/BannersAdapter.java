package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.telemedicine.myclinic.activities.BrowserActivityPayment;
import com.telemedicine.myclinic.models.dashboardModels.MarketingClickableUrlsModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;


import java.util.List;

public class BannersAdapter extends PagerAdapter {


    private List<MarketingClickableUrlsModel> imageSliderList;
    private LayoutInflater inflater;
    private Activity _activity;

    public BannersAdapter(Activity activity, List<MarketingClickableUrlsModel> imageSliderList) {

        try {

            this._activity = activity;
            this.imageSliderList = imageSliderList;
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
        return imageSliderList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View slidersLayout = inflater.inflate(R.layout.slider_list_item, view, false);

        assert slidersLayout != null;


        final ImageView imageView = slidersLayout
                .findViewById(R.id.imageview);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MarketingClickableUrlsModel MarketingClickableUrlsModel = imageSliderList.get(position);

                if (MarketingClickableUrlsModel != null) {

                    String key = MarketingClickableUrlsModel.getKey();

                    if (TextUtil.getEnglish(_activity)) {
                        key = MarketingClickableUrlsModel.getClickableUrlEn();
                    } else if (TextUtil.getArabic(_activity)) {
                        key = MarketingClickableUrlsModel.getClickableUrlAr();
                    }

                    Intent intent = new Intent(_activity,
                            BrowserActivityPayment.class);
                    intent.putExtra("url", key);
                    _activity.startActivity(intent);
                }

            }
        });

        final ProgressBar progressBar = slidersLayout
                .findViewById(R.id.progressbar);
        MarketingClickableUrlsModel MarketingClickableUrlsModel = (MarketingClickableUrlsModel) imageSliderList.get(position);

        if (MarketingClickableUrlsModel != null) {
            String url = MarketingClickableUrlsModel.getValue();
            if (TextUtil.getEnglish(_activity)) {
                url = MarketingClickableUrlsModel.getBannerUrlEn();
            } else if (TextUtil.getArabic(_activity)) {
                url = MarketingClickableUrlsModel.getBannerUrlAr();
            }

            if (!ValidationHelper.isNullOrEmpty(url)) {

                progressBar.setVisibility(View.VISIBLE);
                Glide.with(_activity)
                        .load(url)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(imageView);
            } else {
                imageView.setImageBitmap(null);
            }

            view.addView(slidersLayout, 0);
        }


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
