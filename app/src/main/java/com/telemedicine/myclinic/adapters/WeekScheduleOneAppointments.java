package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.telemedicine.myclinic.models.weekschedule.DayScheduleList;
import com.telemedicine.myclinic.models.weekschedule.WeekScheduleResponse;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.views.RegularTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class WeekScheduleOneAppointments extends RecyclerView.Adapter<WeekScheduleOneAppointments.MyViewHolder> {

    List<WeekScheduleResponse> list;
    Activity mC;
    OnCardClickListner onCardClickListner;
    String speciality = "";

    public WeekScheduleOneAppointments(Activity c, List<WeekScheduleResponse> list, String speciality) {
        this.list = list;
        mC = c;
        this.speciality = speciality;
    }

    @Override
    public WeekScheduleOneAppointments.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        //  if (TextUtil.getEnglish(mC)) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_doctors_list_item, parent, false);
        /*   } else if (TextUtil.getArabic(mC)) {
         *//*
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.past_doctors_list_item_ar, parent, false);
                    *//*
        }*/

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WeekScheduleOneAppointments.MyViewHolder holder, final int position) {

        if (!ValidationHelper.isNullOrEmpty(list)) {

            WeekScheduleResponse weekScheduleResponse = list.get(position);

            if (weekScheduleResponse != null) {

                String doctorsProfile = weekScheduleResponse.getProfilePicUrl();
                String doctorsName = weekScheduleResponse.getDoctorNameEn();
                String doctorsProfession = "";

                if (TextUtil.getEnglish(mC)) {
                    doctorsName = weekScheduleResponse.getDoctorNameEn();
                    doctorsProfession = this.speciality;
                } else if (TextUtil.getArabic(mC)) {
                    doctorsName = weekScheduleResponse.getDoctorNameAr();
                    doctorsProfession = this.speciality;
                }

                holder.doctorsName.setText(doctorsName);
                holder.doctorProfession.setText(doctorsProfession);

                if (!ValidationHelper.isNullOrEmpty(doctorsProfile)) {
                    Glide.with(mC).load(doctorsProfile).into((ImageView) holder.doctorImage);
                }
                List<DayScheduleList> dayScheduleList = weekScheduleResponse.getDayScheduleList();
                if (!ValidationHelper.isNullOrEmpty(dayScheduleList))
                    addDays(holder.daysContainer, holder.dateContainer, (ArrayList<DayScheduleList>) dayScheduleList, weekScheduleResponse);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        BoldTextView doctorsName;
        LightTextView doctorProfession, doctorsDateTime;
        CircleImageView doctorImage;
        RelativeLayout containerCard;
        LinearLayout daysContainer;
        LinearLayout dateContainer;

        public MyViewHolder(View view) {
            super(view);
            doctorsName = view.findViewById(R.id.doctor_name);
            doctorProfession = view.findViewById(R.id.doctor_profession);
            doctorsDateTime = view.findViewById(R.id.dateTime);
            doctorImage = view.findViewById(R.id.doctor_image);
            containerCard = view.findViewById(R.id.container_card);
            daysContainer = view.findViewById(R.id.days_container);
            dateContainer = view.findViewById(R.id.date_container);
        }
    }


    public interface OnCardClickListner {
        void OnCardClicked(List<DayScheduleList> dayScheduleLists, DayScheduleList model, WeekScheduleResponse weekScheduleResponse, int finalI);
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    View mView = null;

    void addDays(LinearLayout daysContainer, LinearLayout dateContainer, ArrayList<DayScheduleList> dayScheduleLists, WeekScheduleResponse weekScheduleResponse) {


        sortArray(dayScheduleLists);

        for (int i = 0; i < dayScheduleLists.size(); i++) {

            DayScheduleList scheduleObject = dayScheduleLists.get(i);
            String day = "";

            if (scheduleObject != null) {

                String days = scheduleObject.getDayEn();
                String date = scheduleObject.getDate();

                if (TextUtil.getArabic(mC)) {
                    if (days.equalsIgnoreCase("Sunday"))
                        days = "ح";
                    else if (days.equalsIgnoreCase("Monday"))
                        days = "ن";
                    else if (days.equalsIgnoreCase("Tuesday"))
                        days = "ث";
                    else if (days.equalsIgnoreCase("Wednesday"))
                        days = "ر";
                    else if (days.equalsIgnoreCase("Thursday"))
                        days = "خ";
                    else if (days.equalsIgnoreCase("Friday"))
                        days = "ج";
                    else if (days.equalsIgnoreCase("Saturday"))
                        days = "س";
                }

                if (!ValidationHelper.isNullOrEmpty(date)) {
                    String myFormat = "dd-MMM"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                    Date date1 = LocaleDateHelper.getDateFormat(date, "yyyy-MM-dd'T'hh:mm:ss");
                    day = sdf.format(date1.getTime());
                }

                boolean isAvailable = scheduleObject.getIsAvailable();

                if (!ValidationHelper.isNullOrEmpty(days)) {
                    String word = days;
                    if (TextUtil.getEnglish(mC))
                        word = days.substring(0, 1);

                    if (!isAvailable)
                        word = "X";

                    RegularTextView daysTxt = new RegularTextView(mC);
                    daysTxt.setText(word);
                    daysTxt.setTextSize(14);
                    daysTxt.setWidth(85);
                    daysTxt.setTextColor(mC.getResources().getColor(R.color.black));
                    daysTxt.setPadding(22, 10, 22, 10);
                    daysTxt.setBackground(mC.getDrawable(R.drawable.grey_border));
                    int finalI = i;
                    daysTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            /*if (mView != null)
                                mView.setBackground(mC.getDrawable(R.drawable.grey_border));
                            v.setBackground(mC.getDrawable(R.drawable.selected_days));
                            mView = v;*/
                            if (isAvailable)
                                onCardClickListner.OnCardClicked(dayScheduleLists, scheduleObject, weekScheduleResponse, finalI);
                        }
                    });

                    if (isAvailable)
                        daysTxt.setBackground(mC.getDrawable(R.drawable.selected_days));

                    RegularTextView dateTxt = new RegularTextView(mC);
                    dateTxt.setText(day);
                    dateTxt.setTextSize(7);
                    dateTxt.setTextColor(mC.getResources().getColor(R.color.colorGreyIndicator));
                    // dateTxt.setPadding(12, 0, 0, 0);
                    dateTxt.setSingleLine();
                    dateTxt.setWidth(91);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10, 0, 0, 0);
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp1.setMargins(5, 0, 0, 0);
                    daysTxt.setGravity(Gravity.CENTER);
                    dateTxt.setGravity(Gravity.CENTER);

                    daysTxt.setLayoutParams(lp);
                    dateTxt.setLayoutParams(lp1);
                    daysContainer.addView(daysTxt);
                    dateContainer.addView(dateTxt);
                }
            }
        }
    }

    private void sortArray(ArrayList<DayScheduleList> arraylist) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss"); //your own date format

        Collections.sort(arraylist, new Comparator<DayScheduleList>() {
            @Override
            public int compare(DayScheduleList o1, DayScheduleList o2) {
                try {
                    return simpleDateFormat.parse(o1.getDate()).compareTo(simpleDateFormat.parse(o2.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
    }
}
