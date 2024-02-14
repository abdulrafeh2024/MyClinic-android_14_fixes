package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.telemedicine.myclinic.models.weekschedule.DayScheduleList;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;


public class WeekScheduleTwoAdapterAppointments extends RecyclerView.Adapter<WeekScheduleTwoAdapterAppointments.MyViewHolder> {

    ArrayList<DayScheduleList> list;
    Activity mC;
    OnItemClick onItemClick;
    String selectedDate;
    boolean selectFirstItem = false;

    public WeekScheduleTwoAdapterAppointments(Activity c, ArrayList<DayScheduleList> list, String itemDate) {
        this.list = list;
        mC = c;
        selectedDate = itemDate;
        sortArray(this.list);
    }

    @Override
    public WeekScheduleTwoAdapterAppointments.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.week_schedule_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WeekScheduleTwoAdapterAppointments.MyViewHolder holder, final int position) {

        if (!ValidationHelper.isNullOrEmpty(list)) {

            String day = list.get(position).getDayEn();
            String date = list.get(position).getDate();

            if (TextUtil.getArabic(mC)) {
                day = list.get(position).getDayAr();
            }

            if (!ValidationHelper.isNullOrEmpty(day))
                holder.day.setText(day);
            if (!ValidationHelper.isNullOrEmpty(date)) {

                String myFormat = " dd-MMM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                Date date1 = LocaleDateHelper.getDateFormat(date, "yyyy-MM-dd'T'hh:mm:ss");
                String d = sdf.format(date1.getTime());
                holder.date.setText(d);
            }

            holder.selectedContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedDate = date;
                    onItemClick.OnCardClicked(list.get(position));
                    notifyDataSetChanged();
                }
            });

            if (selectFirstItem) {
                selectedDate = date;
                selectFirstItem = false;
                onItemClick.OnCardClicked(list.get(0));
            }

            if (selectedDate(date)) {
                holder.selectedContainer.setBackground(mC.getDrawable(R.drawable.grey_bg));
                holder.selectedBar.setVisibility(View.VISIBLE);
            } else {
                holder.selectedContainer.setBackground(null);
                holder.selectedBar.setVisibility(View.GONE);
            }

        }
    }

    private boolean selectedDate(String date) {
        boolean isSelected = false;

        String selected = LocaleDateHelper.convertDateStringFormat(this.selectedDate, "yyyy-MM-dd'T'HH:mm:ss", "EEEE, dd MMMM yyyy", Locale.ENGLISH);
        String date1 = LocaleDateHelper.convertDateStringFormat(date, "yyyy-MM-dd'T'HH:mm:ss", "EEEE, dd MMMM yyyy", Locale.ENGLISH);
        if (selected.equalsIgnoreCase(date1)) {
            isSelected = true;
        }

        return isSelected;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        BoldTextView day;
        LightTextView date;
        LinearLayout selectedContainer;
        View selectedBar;

        public MyViewHolder(View view) {
            super(view);
            day = view.findViewById(R.id.day);
            date = view.findViewById(R.id.date);
            selectedContainer = view.findViewById(R.id.container);
            selectedBar = view.findViewById(R.id.selectedBar);
        }
    }

    public interface OnItemClick {
        void OnCardClicked(DayScheduleList model);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void selectFirstDate(boolean select) {
        selectFirstItem = select;
        notifyDataSetChanged();
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
