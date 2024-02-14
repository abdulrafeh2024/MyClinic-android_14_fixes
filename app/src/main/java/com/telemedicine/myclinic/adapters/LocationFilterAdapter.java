package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.telemedicine.myclinic.models.company.Company;
import com.telemedicine.myclinic.models.earliestslots.EarliestSlotsMiniModel;
import com.telemedicine.myclinic.models.weekschedule.DayScheduleList;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.LightTextView;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LocationFilterAdapter extends RecyclerView.Adapter<LocationFilterAdapter.MyViewHolder> {

    List<Company> list;
    Activity mC;
    OnCardClickListner onCardClickListner;

    public int selectedPosition = 0;

    public LocationFilterAdapter(Activity c, List<Company> list) {
        this.list = list;
        mC = c;
    }

    @NonNull
    @Override
    public LocationFilterAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        //  if (TextUtil.getEnglish(mC)) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location_filter_layout, parent, false);
        /*   } else if (TextUtil.getArabic(mC)) {
         *//*
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.past_doctors_list_item_ar, parent, false);
                    *//*
        }*/

        return new LocationFilterAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationFilterAdapter.MyViewHolder holder, int position) {
        if (!ValidationHelper.isNullOrEmpty(list)) {

            holder.itemView.setSelected(selectedPosition == position);
            Company company = list.get(position);
          /*  if(position == 0){
                holder.locationName.setText(R.string.all);
            }*/
             if(company != null){
                 if (TextUtil.getEnglish(mC)) {
                     holder.locationName.setText(company.getCompanyFullEn());
                 } else if (TextUtil.getArabic(mC)) {
                     holder.locationName.setText(company.getCompanyFullAr());
                 }
             }

            holder.locationLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selectedPosition);
                    selectedPosition = holder.getLayoutPosition();
                    notifyItemChanged(selectedPosition);

                    onCardClickListner.onCompanyClick(company.getCompanyId(), company.getCompanyFullEn(), false);
                    /*if(position != 0){
                        onCardClickListner.onCompanyClick(company.getCompanyId(), company.getCompanyFullEn(), false);
                    }else{
                        onCardClickListner.onCompanyClick("", "", true);
                    }*/
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LightTextView locationName;
        LinearLayoutCompat locationLayout;

        public MyViewHolder(View view) {
            super(view);
            locationName = view.findViewById(R.id.location);
            locationLayout = view.findViewById(R.id.locationLayout);
            //itemView.setOnClickListener(this);
        }

       /* @Override
        public void onClick(View v) {
            notifyItemChanged(selectedPosition);
            selectedPosition = getLayoutPosition();
            notifyItemChanged(selectedPosition);
        }*/
    }

    public void setSelectedPosition(int position){
        selectedPosition = position;
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }

    public interface OnCardClickListner {
        void onCompanyClick(String companyId, String Companyname, boolean isAll);
    }
}
