package com.telemedicine.myclinic.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.telemedicine.myclinic.models.dashboardModels.DoctorProfile;
import com.telemedicine.myclinic.models.earliestslots.EarliestSlotsMiniModel;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;
import com.telemedicine.myclinic.models.weekschedule.DayScheduleList;
import com.telemedicine.myclinic.models.weekschedule.WeekScheduleResponse;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightButton;
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
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EarliestSlotsAdapter extends RecyclerView.Adapter<EarliestSlotsAdapter.MyViewHolder> {

    List<EarliestSlotsMiniModel> list;
    Activity mC;
    OnCardClickListner onCardClickListner;
    String speciality = "";
    boolean changed = false;
    int flipPosition = -1;
    boolean isTelemedicine;
    TinyDB tinyDB;

    ArrayList<DoctorsProfile> doctorsProfiles;

    public EarliestSlotsAdapter(Activity c, List<EarliestSlotsMiniModel> list, boolean isTelemedicine, TinyDB tinyDB) {
        this.list = list;
        mC = c;
        this.tinyDB = tinyDB;
        this.isTelemedicine = isTelemedicine;
    }

    @Override
    public EarliestSlotsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        //  if (TextUtil.getEnglish(mC)) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_appointment_doctor_layout, parent, false);
        /*   } else if (TextUtil.getArabic(mC)) {
         *//*
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.past_doctors_list_item_ar, parent, false);
                    *//*
        }*/

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final EarliestSlotsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        if (!ValidationHelper.isNullOrEmpty(list)) {

            EarliestSlotsMiniModel earliestSlotsMiniModel = list.get(position);

            if (earliestSlotsMiniModel != null) {

                if (flipPosition == position && changed) {
                    flipPosition = -1;
                    changed = false;
                    holder.myEasyFlipView.flipTheView();
                    if (TextUtil.getArabic(mC)) {
                        holder.dr_name_tv_back.setText(doctorsProfiles.get(0).getNameAr());
                        holder.doctor_profession.setText(doctorsProfiles.get(0).getTitleAr());
                        holder.doctor_profession_degree.setText(doctorsProfiles.get(0).getQualificationAr());
                    } else {
                        holder.dr_name_tv_back.setText(doctorsProfiles.get(0).getNameEn());
                        holder.doctor_profession.setText(doctorsProfiles.get(0).getTitleEn());
                        holder.doctor_profession_degree.setText(doctorsProfiles.get(0).getQualificationEn());
                    }

                    Glide.with(mC).load(doctorsProfiles.get(0).getProfilePicUrl()).placeholder(R.drawable.doctr).into((ImageView) holder.doctor_image_back);

                }

                String doctorsName = earliestSlotsMiniModel.getDoctorName();

                if (TextUtil.getEnglish(mC)) {
                    doctorsName = earliestSlotsMiniModel.getDoctorName();
                    holder.doctor_profession_front.setText(earliestSlotsMiniModel.getSpecialtyEn());
                } else if (TextUtil.getArabic(mC)) {
                    doctorsName = earliestSlotsMiniModel.getDoctorNameAr();
                    holder.doctor_profession_front.setText(earliestSlotsMiniModel.getSpecialtyAr());
                }

                if (position == 0) {
                    if(earliestSlotsMiniModel.getSlotDate()!= null){
                        holder.earliest_available_label1.setVisibility(View.VISIBLE);
                        holder.earliest_available_label_back.setVisibility(View.VISIBLE);
                    }else{
                        holder.earliest_available_label1.setVisibility(View.GONE);
                        holder.earliest_available_label_back.setVisibility(View.GONE);
                    }

                }
                holder.doctorName.setText(doctorsName);

                Glide.with(mC).load(earliestSlotsMiniModel.getDoctorProfileUrl()).placeholder(R.drawable.doctr).into((ImageView) holder.doctor_image1);


                if (earliestSlotsMiniModel.getCompany().toLowerCase().equals("nc01")) {
                    holder.location1.setText(mC.getString(R.string.prince_sultan));
                    holder.location_back.setText(mC.getString(R.string.prince_sultan));
                } else if (earliestSlotsMiniModel.getCompany().toLowerCase().equals("safa")) {
                    holder.location_back.setText(mC.getString(R.string.safa));
                    holder.location1.setText(mC.getString(R.string.safa));
                } else if(earliestSlotsMiniModel.getCompany().toLowerCase().equals("prc")){
                    holder.location_back.setText(mC.getString(R.string.prc));
                    holder.location1.setText(mC.getString(R.string.prc));
                }

                if (isTelemedicine) {
                    holder.location1.setVisibility(View.GONE);
                    holder.location_back.setVisibility(View.GONE);
                    holder.locationIcon_front.setVisibility(View.GONE);
                    holder.locationIcon_back.setVisibility(View.GONE);
                } else {
                    holder.location1.setVisibility(View.VISIBLE);
                    holder.location_back.setVisibility(View.VISIBLE);
                    holder.locationIcon_front.setVisibility(View.VISIBLE);
                    holder.locationIcon_back.setVisibility(View.VISIBLE);
                }
                String weekDay = "";
                holder.slotDate.setText("");
                if (!ValidationHelper.isNullOrEmpty(earliestSlotsMiniModel.getSlotDate())) {
                    holder.confirmBooking_btn1.setVisibility(View.VISIBLE);

                    holder.date_container.setVisibility(View.VISIBLE);
                    holder.daysContainer.setVisibility(View.VISIBLE);
                    holder.press_month_view_label.setVisibility(View.GONE);

                    weekDay = LocaleDateHelper.getDayOfWeek(earliestSlotsMiniModel.getSlotDate(), "yyyy-MM-dd'T'HH:mm:ss");
                    if (TextUtil.getArabic(mC)) {
                        if (weekDay.equalsIgnoreCase("Sunday")) {
                            weekDay = "الأحد";
                        } else if (weekDay.equalsIgnoreCase("Monday")) {
                            weekDay = "الاثنين";
                        } else if (weekDay.equalsIgnoreCase("Tuesday")) {
                            weekDay = "يوم الثلاثاء";
                        } else if (weekDay.equalsIgnoreCase("Wednesday")) {
                            weekDay = "الأربعاء";
                        } else if (weekDay.equalsIgnoreCase("Thursday")) {
                            weekDay = "يوم الخميس";
                        } else if (weekDay.equalsIgnoreCase("Friday")) {
                            weekDay = "جمعة";
                        } else if (weekDay.equalsIgnoreCase("Saturday")) {
                            weekDay = "السبت";
                        }
                    }

                    String myFormat = "dd-MM-yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                    //  Date date1 = LocaleDateHelper.getDateFormat(earliestSlotsMiniModel.getSlotDate(), "yyyy-MM-dd'T'hh:mm:ss");

                    SimpleDateFormat date1Format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault());
                    Date date1 = null;
                    Date time1 = null;
                    try {
                        date1 = date1Format.parse(earliestSlotsMiniModel.getSlotDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String time = earliestSlotsMiniModel.getSlotTime();
                    if (TextUtil.getArabic(mC)) {
                        time = time.replaceAll(" AM", " ص");
                        time = time.replaceAll(" PM", " م");
                    }

                    holder.slotTime.setText(time);
                    holder.slotDate.setText(weekDay + "   " + sdf.format(date1.getTime()));
                    holder.dr_slot_tv_back.setText(sdf.format(date1.getTime()) + "   " + time);
                }else{
                    holder.date_container.setVisibility(View.GONE);
                    holder.daysContainer.setVisibility(View.GONE);
                    holder.confirmBooking_btn1.setVisibility(View.GONE);
                    holder.press_month_view_label.setVisibility(View.VISIBLE);
                    holder.slotDate.setText(mC.getString(R.string.no_earliest_slot_available_this_week));
                }

                holder.viewProfile1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCardClickListner.viewDoctorProfile(earliestSlotsMiniModel.getDoctorId(), position);
                    }
                });

                holder.confirmBooking_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.myEasyFlipView.flipTheView();
                    }
                });

                String finalWeekDay = weekDay;
                holder.confirmBooking_btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCardClickListner.onMonthView(earliestSlotsMiniModel.getDoctorId(), earliestSlotsMiniModel.getCompany(), earliestSlotsMiniModel, true, false, "", finalWeekDay);
                    }
                });

                holder.monthViewBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCardClickListner.onMonthView(earliestSlotsMiniModel.getDoctorId(), earliestSlotsMiniModel.getCompany(), earliestSlotsMiniModel, false, false, "", finalWeekDay);
                    }
                });

                List<DayScheduleList> dayScheduleList = earliestSlotsMiniModel.getDayScheduleList();
                if (!ValidationHelper.isNullOrEmpty(dayScheduleList))
                    addDays(holder.daysContainer, holder.date_container, (ArrayList<DayScheduleList>) dayScheduleList, earliestSlotsMiniModel,
                            holder.earliest_available_label1, holder.earliest_available_label_back);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        BoldTextView slotDate, dr_slot_tv_back, slotTime, doctorName, earliest_available_label1, dr_name_tv_back, earliest_available_label_back;
        CircleImageView doctor_image1, doctor_image_back;
        LinearLayout daysContainer, date_container;
        LightTextView location1, viewProfile1, location_back, doctor_profession, doctor_profession_front, doctor_profession_degree,
                press_month_view_label;
        EasyFlipView myEasyFlipView;
        LightButton confirmBooking_btn, confirmBooking_btn1;
        AppCompatImageView locationIcon_front, locationIcon_back;
        CardView monthViewBtn;

        public MyViewHolder(View view) {
            super(view);
            slotDate = view.findViewById(R.id.slot_date_tv);
            dr_slot_tv_back = view.findViewById(R.id.dr_slot_tv_back);
            slotTime = view.findViewById(R.id.slot_time_tv);
            press_month_view_label = view.findViewById(R.id.press_month_view_label);
            doctorName = view.findViewById(R.id.doctor_name_Tv);
            daysContainer = view.findViewById(R.id.days_container);
            date_container = view.findViewById(R.id.date_container);
            earliest_available_label1 = view.findViewById(R.id.earliest_available_label1);
            earliest_available_label_back = view.findViewById(R.id.earliest_available_label_back);
            location1 = view.findViewById(R.id.location1);
            location_back = view.findViewById(R.id.location_back);
            locationIcon_front = view.findViewById(R.id.locationIcon_front);
            locationIcon_back = view.findViewById(R.id.locationIcon_back);
            viewProfile1 = view.findViewById(R.id.viewProfile1);
            myEasyFlipView = view.findViewById(R.id.myEasyFlipView);
            dr_name_tv_back = view.findViewById(R.id.dr_name_tv_back);
            doctor_image_back = view.findViewById(R.id.doctor_image_back);
            doctor_image1 = view.findViewById(R.id.doctor_image1);
            doctor_profession = view.findViewById(R.id.doctor_profession);
            doctor_profession_degree = view.findViewById(R.id.doctor_profession_degree);
            confirmBooking_btn = view.findViewById(R.id.confirmBooking_btn);
            confirmBooking_btn1 = view.findViewById(R.id.confirmBooking_btn1);
            monthViewBtn = view.findViewById(R.id.monthViewBtn);
            doctor_profession_front = view.findViewById(R.id.doctor_profession_front);
        }
    }


    public interface OnCardClickListner {
        void OnCardClicked(List<DayScheduleList> dayScheduleLists, DayScheduleList model, EarliestSlotsMiniModel weekScheduleResponse, int finalI);

        void viewDoctorProfile(long doctorId, int itemPosition);

        void onMonthView(long doctorId, String company, EarliestSlotsMiniModel earliestSlotsMiniModel, boolean isBookNow, boolean isWeekView, String date, String weekDay);

    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }

    public void flipTheView(int flipPosition) {
        this.flipPosition = flipPosition;
        changed = true;
    }

    public void setDoctorProfile(ArrayList<DoctorsProfile> doctorsProfiles) {
        this.doctorsProfiles = doctorsProfiles;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    View mView = null;

    void addDays(LinearLayout daysContainer, LinearLayout dateContainer, ArrayList<DayScheduleList> dayScheduleLists, EarliestSlotsMiniModel weekScheduleResponse,
                 BoldTextView earliestSLot, BoldTextView earliestSLotback) {


        sortArray(dayScheduleLists);
        daysContainer.removeAllViewsInLayout();
        dateContainer.removeAllViewsInLayout();

        for (int i = 0; i < dayScheduleLists.size(); i++) {

            DayScheduleList scheduleObject = dayScheduleLists.get(i);
            String day = "";
            String weekDay = "";

            if (scheduleObject != null) {

                String days = scheduleObject.getDayEn();
                String date = scheduleObject.getDate();

                weekDay = scheduleObject.getDayEn();
                if (TextUtil.getArabic(mC)) {
                    if (days.equalsIgnoreCase("Sunday")) {
                        days = "ح";
                        weekDay = "الأحد";
                    } else if (days.equalsIgnoreCase("Monday")) {
                        days = "ن";
                        weekDay = "الاثنين";
                    } else if (days.equalsIgnoreCase("Tuesday")) {
                        days = "ث";
                        weekDay = "يوم الثلاثاء";
                    } else if (days.equalsIgnoreCase("Wednesday")) {
                        days = "ر";
                        weekDay = "الأربعاء";
                    } else if (days.equalsIgnoreCase("Thursday")) {
                        days = "خ";
                        weekDay = "يوم الخميس";
                    } else if (days.equalsIgnoreCase("Friday")) {
                        days = "ج";
                        weekDay = "جمعة";
                    } else if (days.equalsIgnoreCase("Saturday")) {
                        days = "س";
                        weekDay = "السبت";
                    }
                }

                if (!ValidationHelper.isNullOrEmpty(date)) {
                    String myFormat = "dd-MMM"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                    Date date1 = LocaleDateHelper.getDateFormat(date, "yyyy-MM-dd'T'hh:mm:ss");
                    day = sdf.format(date1.getTime());
                }

                boolean isAvailable = false;
                if (scheduleObject.getIsAvailable() != null) {
                    isAvailable = scheduleObject.getIsAvailable();
                }

                if (!ValidationHelper.isNullOrEmpty(days)) {
                    String word = days;
                    if (TextUtil.getEnglish(mC)) {
                        //  earliestSLot.setText("Earliest Availability - " + days);
                        //   earliestSLotback.setText("Earliest Availability - " + days);
                        word = days.substring(0, 1);
                    }


                    if (!isAvailable)
                        word = "X";

                    RegularTextView daysTxt = new RegularTextView(mC);
                    daysTxt.setText(word);
                    daysTxt.setTextSize(14);
                    daysTxt.setWidth(115);
                    // daysTxt.setWidth(140);
                    daysTxt.setHeight(95);
                    //  daysTxt.setHeight(110);
                    daysTxt.setTextColor(mC.getResources().getColor(R.color.black));
                    daysTxt.setPadding(22, 10, 22, 10);
                    daysTxt.setBackground(mC.getDrawable(R.drawable.grey_border));
                    int finalI = i;
                    String finalWeekDays = weekDay;
                    boolean finalIsAvailable = isAvailable;
                    daysTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            /*if (mView != null)
                                mView.setBackground(mC.getDrawable(R.drawable.grey_border));
                            v.setBackground(mC.getDrawable(R.drawable.selected_days));
                            mView = v;*/
                            if (finalIsAvailable) {
                                onCardClickListner.onMonthView(weekScheduleResponse.getDoctorId(), weekScheduleResponse.getCompany(),
                                        weekScheduleResponse, true, true, scheduleObject.getDate(), finalWeekDays);
                            }
                            //  onCardClickListner.OnCardClicked(dayScheduleLists, scheduleObject, weekScheduleResponse, finalI);
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
                    dateTxt.setWidth(115);
                    //  dateTxt.setWidth(140);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10, 0, 0, 0);
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp1.setMargins(5, 0, 0, 0);
                    dateTxt.setGravity(Gravity.CENTER);
                    dateTxt.setLayoutParams(lp1);

                    daysContainer.addView(daysTxt);
                    daysTxt.setGravity(Gravity.CENTER);
                    daysTxt.setLayoutParams(lp);


                    dateContainer.addView(dateTxt);
                    dateContainer.setWeightSum(7);

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
