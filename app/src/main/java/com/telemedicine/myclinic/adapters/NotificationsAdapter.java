package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.telemedicine.myclinic.models.NotificationMiniModel;
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.views.RegularTextView;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder>  {

    ArrayList<NotificationMiniModel> list;
    Activity mC;
    NotificationsAdapter.OnCardClickListner onCardClickListner;
    TimeAgoMessages messages = null;

    public NotificationsAdapter(Activity c, ArrayList<NotificationMiniModel> list) {
        this.list = list;
        mC = c;
        if (TextUtil.getEnglish(c)) {
            Locale LocaleBylanguageTag = Locale.forLanguageTag("en");
            messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
        } else if (TextUtil.getArabic(c)) {
            Locale LocaleBylanguageTag = Locale.forLanguageTag("ar");
            messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
        }
    }

    @Override
    public void onBindViewHolder(final NotificationsAdapter.MyViewHolder holder, final int position) {
        if (list != null && list.size() > 0) {

            if (!ValidationHelper.isNullOrEmpty(list.get(position).getTitle())) {
                if (TextUtil.getEnglish(mC)) {
                    holder.title.setText(list.get(position).getTitle());
                }else{
                    holder.title.setText(list.get(position).getTitleAr());
                }
            }

            if (!ValidationHelper.isNullOrEmpty(list.get(position).getBody())) {
                if (TextUtil.getEnglish(mC)) {
                    holder.body.setText(list.get(position).getBody());
                }else{
                    holder.body.setText(list.get(position).getBodyAr());
                }
            }

            if (!ValidationHelper.isNullOrEmpty(list.get(position).getDateTime())) {
                long dateLong = LocaleDateHelper.convertDateFormat(list.get(position).getDateTime(), "yyyy-MM-dd'T'HH:mm:ss");
                String dateAgo = TimeAgo.using(dateLong);
                holder.time.setText(dateAgo);
            }

            if(!list.get(position).getIsRead()){
                holder.notification_bg.setBackground(ContextCompat.getDrawable(mC, R.color.colorGreyAlpha));
            }else {
                holder.notification_bg.setBackground(ContextCompat.getDrawable(mC, R.color.transparent));
            }

            holder.notificaitonView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onCardClickListner.OnCardClicked(list.get(position), position);
                }
            });
        }
    }

    @Override
    public NotificationsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;

        if (TextUtil.getEnglish(mC)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_announcement_notification_layout, parent, false);
        } else if (TextUtil.getArabic(mC)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_announcement_notification_layout, parent, false);
        }

        return new MyViewHolder(itemView);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        BoldTextView title;
        RegularTextView body;
        RegularTextView time;
        ConstraintLayout notificaitonView,notification_bg;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            body = view.findViewById(R.id.body);
            time = view.findViewById(R.id.time);
            notificaitonView = view.findViewById(R.id.notificationView);
            notification_bg = view.findViewById(R.id.notification_bg);
        }
    }

    public interface OnCardClickListner {
        void OnCardClicked(NotificationMiniModel model, int pos);
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
