package com.telemedicine.myclinic.adapters;

import android.app.Activity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.views.RegularTextView;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterUpcomingAppointments extends RecyclerView.Adapter<AdapterUpcomingAppointments.MyViewHolder> {

    ArrayList<ApptsMiniModel> list;
    Activity mC;
    OnCardClickListner onCardClickListner;
    TimeAgoMessages messages = null;

    public AdapterUpcomingAppointments(Activity c, ArrayList<ApptsMiniModel> list) {
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
    public AdapterUpcomingAppointments.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;

        if (TextUtil.getEnglish(mC)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.upcoming_doctors_list_item, parent, false);
        } else if (TextUtil.getArabic(mC)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.upcoming_doctors_list_item_ar, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AdapterUpcomingAppointments.MyViewHolder holder, final int position) {

        if (list != null && list.size() > 0) {
            DoctorsProfile doctorsProfile = list.get(position).getDoctorProfile();
            String companyName =  (TextUtil.getEnglish(mC)) ? list.get(position).getCompanyNameEn() :list.get(position).getCompanyNameAr() ;
            if (doctorsProfile != null) {

                String doctorsName = "";
                String doctorsProfession = "";

                if (TextUtil.getEnglish(mC)) {
                    doctorsProfession = doctorsProfile.getTitleEn();
                    doctorsName = doctorsProfile.getNameEn();
                } else if (TextUtil.getArabic(mC)) {
                    doctorsName = doctorsProfile.getNameAr();
                    doctorsProfession = doctorsProfile.getTitleAr();
                }

                String doctorImage = doctorsProfile.getProfilePicUrl();
                String dateTime = list.get(position).getApptDate();

                holder.doctorsName.setText(doctorsName);
                holder.doctorProfession.setText(doctorsProfession);
                if(!list.get(position).isTelemedicine()){
                    holder.companyTextView.setText(companyName);
                }else{
                    holder.companyTextView.setVisibility(View.INVISIBLE);
                }
                if (!ValidationHelper.isNullOrEmpty(doctorImage)) {
                    Glide.with(mC).load(doctorImage).placeholder(R.drawable.doctr).into((ImageView) holder.doctorImage);
                }

             /*   if (list.get(position).getApptBookType() == 10) {
                    if (!ValidationHelper.isNullOrEmpty(doctorImage)) {
                        Glide.with(mC).load(doctorImage).into((ImageView) holder.doctorImage);
                    }
                } else if (list.get(position).getApptBookType() == 30) {
                    Glide.with(mC).load(R.drawable.xray).into((ImageView) holder.doctorImage);
                } else if (list.get(position).getApptBookType() == 20) {
                    if (!ValidationHelper.isNullOrEmpty(doctorImage)) {
                        Glide.with(mC).load(doctorImage).into((ImageView) holder.doctorImage);
                    }
                }*/

                if (!ValidationHelper.isNullOrEmpty(dateTime)) {

                    String date = LocaleDateHelper.convertDateStringFormat(dateTime, "yyyy-MM-dd'T'HH:mm:ss", "EEEE, dd MMMM yyyy hh:mm a");
                    holder.doctorsDateTime.setText(date);

                    long dateLong = LocaleDateHelper.convertDateFormat(dateTime, "yyyy-MM-dd'T'HH:mm:ss");
                    String dateAgo = TimeAgo.using(dateLong, messages);
                    //if (dateAgo.contains("within")) {
                    String dateIn = dateAgo.replaceAll("within", "in");
                    // }


                    if (!ValidationHelper.isNullOrEmpty(dateIn)) {

                        String days = LocaleDateHelper.getCountOfDays("", dateTime);

                        if (days.equalsIgnoreCase("0")) {
                            holder.days.setText(R.string.today);
                        } else if (days.equalsIgnoreCase("1")) {
                            holder.days.setText(R.string.tomorrow);
                        }/* else if (days.equalsIgnoreCase("2")) {
                            holder.days.setText(R.string.in_2_days);
                        }*/ else {
                            int d = 0;
                            if (!ValidationHelper.isNullOrEmpty(days))
                                d = Integer.valueOf(days);
                            if (d > 1) {
                                d = d - 1;

                                String gy = mC.getString(R.string.in) + " " + d + " " + mC.getString(R.string.days);
                                holder.days.setText(gy);
                            } else
                                holder.days.setText(dateIn);
                        }

                    }


               /*     long apptTimeMilli = LocaleDateHelper.getApptTimeInMilli(dateTime);
                    long currentTimeMill = System.currentTimeMillis();

                    long diff = apptTimeMilli - currentTimeMill;
                    int diffmin = (int) (diff / (60 * 1000));

                    if (diffmin < 30 && diffmin > -30) {
                        holder.checkin.setVisibility(View.VISIBLE);
                    } else {
                        holder.checkin.setVisibility(View.GONE);

                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                45.0f
                        );
                        param.leftMargin = 10;
                        holder.reschedule.setLayoutParams(param);
                        holder.cancel.setLayoutParams(param);

                    }*/

                 /*   long apptTimeMilliPlus9 = LocaleDateHelper.getApptTimeInMilli(dateTime) + 540000 - currentTimeMill;
                    if (apptTimeMilliPlus9 <= 8 && diffmin >= 1) {
                        holder.checkin.setEnabled(true);
                    } else {
                        holder.checkin.setEnabled(false);
                        holder.checkin.setTextColor(mC.getResources().getColor(R.color.colorGrey2));
                        holder.checkin.setBackgroundColor(mC.getResources().getColor(R.color.colorPrimaryDisbale));
                    }*/

                }

                holder.containerOutside.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!list.get(position).isTelemedicine())
                            onCardClickListner.OnCardClicked(list.get(position), position, false, false, false, false, false);
                    }
                });
                holder.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.get(position).isTelemedicine())
                            onCardClickListner.OnCardClicked(list.get(position), position, true, false, false, false, false);
                    }
                });
                holder.reschedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.get(position).isTelemedicine())
                            onCardClickListner.OnCardClicked(list.get(position), position, false, true, false, false, false);
                    }
                });
                holder.checkin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.get(position).isTelemedicine())
                            onCardClickListner.OnCardClicked(list.get(position), position, false, false, true, false, false);
                    }
                });
            }
           // boolean isEnable = LocaleDateHelper.isEnableCall(list.get(position).getApptDate(), "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);



            if(!list.get(position).isPaid()){
                holder.checkin.setText(mC.getString(R.string.pay));
                holder.checkin.setVisibility(View.VISIBLE);
            }else {
                if(list.get(position).getMeetingId() != null){
                    holder.checkin.setText(mC.getString(R.string.join));
                    holder.checkin.setVisibility(View.VISIBLE);
                }else {
                    holder.checkin.setVisibility(View.GONE);
                }
            }

            boolean isTelemedicine = list.get(position).isTelemedicine();
            boolean IsTelemedicineCompleted = list.get(position).isTelemedicineCompleted();
            holder.changeInsurance.setVisibility(View.VISIBLE);
            if (isTelemedicine) {

                //holder.teleContainer.setVisibility(View.VISIBLE);

                if(list.get(position).isPaid()){
                    holder.changeInsurance.setVisibility(View.GONE);
                }else{
                    holder.changeInsurance.setVisibility(View.VISIBLE);
                }

                holder.changeInsurance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            onCardClickListner.OnCardClicked(list.get(position), position, false, false, false, true, true);
                    }
                });
                if (IsTelemedicineCompleted) {
                    holder.viewServicesBtn.setVisibility(View.VISIBLE);
                    holder.teleContainer.setVisibility(View.GONE);

                    holder.viewServicesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (list.get(position).isTelemedicine())
                                onCardClickListner.OnCardClicked(list.get(position), position, false, false, false, true, false);
                        }
                    });
                } else {
                    holder.viewServicesBtn.setVisibility(View.GONE);
                    if (list.get(position).getApptBookType() != 30){
                        holder.teleContainer.setVisibility(View.VISIBLE);
                        holder.headSet.setVisibility(View.VISIBLE);
                    }

                }
            } else {
                holder.changeInsurance.setVisibility(View.GONE);
                holder.viewServicesBtn.setVisibility(View.GONE);
                holder.headSet.setVisibility(View.INVISIBLE);
                holder.teleContainer.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        BoldTextView doctorsName;
        BoldTextView companyTextView;
        LightTextView doctorProfession, doctorsDateTime;
        CircleImageView doctorImage;
        RelativeLayout containerOutside;
        RegularTextView days;
        LinearLayout teleContainer;
        LightButton cancel, reschedule, checkin;
        ImageView headSet;
        LightButton viewServicesBtn;
        LightButton changeInsurance;

        public MyViewHolder(View view) {
            super(view);
            doctorsName = view.findViewById(R.id.doctor_name);
            doctorProfession = view.findViewById(R.id.doctor_profession);
            doctorsDateTime = view.findViewById(R.id.dateTime);
            doctorImage = view.findViewById(R.id.doctor_image);
            teleContainer = view.findViewById(R.id.tele_container);
            containerOutside = view.findViewById(R.id.container_outside);
            days = view.findViewById(R.id.days);
            cancel = view.findViewById(R.id.cancel);
            reschedule = view.findViewById(R.id.reschedule);
            checkin = view.findViewById(R.id.check_in);
            headSet = view.findViewById(R.id.headset);
            viewServicesBtn = view.findViewById(R.id.view_services_btn);
            changeInsurance = view.findViewById(R.id.change_Insurance);
            companyTextView = view.findViewById(R.id.companyTextView);
        }
    }

    public interface OnCardClickListner {
        void OnCardClicked(ApptsMiniModel model, int pos, boolean cancel, boolean reschedule, boolean checkin,
                           boolean viewServices, boolean isChangeInsurance);
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }

    public void updateList(ApptsMiniModel model, int position) {
        list.set(position, model);
        notifyItemChanged(position);
    }

    public void removeObject(ApptsMiniModel model, int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
