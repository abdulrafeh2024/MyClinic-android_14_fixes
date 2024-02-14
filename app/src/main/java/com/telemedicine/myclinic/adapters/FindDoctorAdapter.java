package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.telemedicine.myclinic.activities.FindDoctorsActivity;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightTextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindDoctorAdapter extends RecyclerView.Adapter<FindDoctorAdapter.MyViewHolder> implements Filterable {

    List<DoctorsProfile> list;
    ArrayList<DoctorsProfile> fullList;
    Activity mC;
    OnCardClickListner onCardClickListner;
    View mView = null;
    boolean click = true;

    public FindDoctorAdapter(Activity c, List<DoctorsProfile> list) {
        this.list = list;

        mC = c;
    }

    public FindDoctorAdapter(Activity c, List<DoctorsProfile> list, boolean clickable) {
        this.list = list;
        fullList = new ArrayList<>(list);
        mC = c;
        click = clickable;
    }

    @Override
    public FindDoctorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;

        if (TextUtil.getEnglish(mC)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_find_doctors_layout, parent, false);
        } else if (TextUtil.getArabic(mC)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_find_doctors_layout, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FindDoctorAdapter.MyViewHolder holder, final int position) {

        if (list != null && list.size() > 0) {
            String doctorsName = "";
            String doctorsProfession = "";
            String doctorDegree = "";
            if (TextUtil.getEnglish(mC)) {
                doctorsName = list.get(position).getNameEn();
                doctorsProfession = list.get(position).getTitleEn();
                doctorDegree = list.get(position).getQualificationEn();
            } else if (TextUtil.getArabic(mC)) {
                doctorsName = list.get(position).getNameAr();
                doctorsProfession = list.get(position).getTitleAr();
                doctorDegree = list.get(position).getQualificationAr();
            }

            String doctorImage = list.get(position).getProfilePicUrl();
            holder.doctorsName.setText(doctorsName);
            holder.doctorProfession.setText(doctorsProfession);
            holder.doctorsDegree.setText(doctorDegree);

            if (!ValidationHelper.isNullOrEmpty(doctorImage)) {
                Glide.with(mC).load(doctorImage).into((ImageView) holder.doctorImage);
            }

            holder.doctorContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*if (!click) return;

                    if (mView != null)
                        mView.setBackgroundResource(R.color.colorWhite);

                    mView = v;

                    holder.doctorContainer.setBackgroundResource(R.color.colorGrey);*/
                    onCardClickListner.OnCardClicked(list.get(position), position);
                }
            });

            if (list.size() == 1) {
                holder.doctorContainer.setBackgroundResource(R.color.colorGrey);
                mView = holder.doctorContainer;
            }

            if(TextUtil.getArabic(mC)){
                holder.arrowImg.setImageResource(R.drawable.arrow_left);
            }else{
                holder.arrowImg.setImageResource(R.drawable.arrow_right);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        BoldTextView doctorsName;
        LightTextView doctorProfession, doctorsDegree;
        CircleImageView doctorImage;
        RelativeLayout doctorContainer;
        AppCompatImageView arrowImg;

        public MyViewHolder(View view) {
            super(view);
            doctorsName = view.findViewById(R.id.doctor_name);
            doctorProfession = view.findViewById(R.id.doctor_profession);
            doctorsDegree = view.findViewById(R.id.doctor_profession_degree);
            doctorImage = view.findViewById(R.id.doctor_image);
            doctorContainer = view.findViewById(R.id.doctor_container);
            arrowImg = view.findViewById(R.id.arrow_img);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public interface OnCardClickListner {
        void OnCardClicked(DoctorsProfile model, int pos);
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }

    public void unSelect() {
        if (mView != null)
            mView.setBackgroundResource(android.R.color.transparent);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return Searched_Filter;
    }

    private Filter Searched_Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<DoctorsProfile> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (DoctorsProfile item : fullList) {
                    String[] splitStr;
                    if(TextUtil.getArabic(mC)){
                        splitStr  = item.getNameAr().split("\\s+");
                    }else{
                        splitStr  = item.getNameEn().split("\\s+");
                    }
                    boolean added = false;
                    for(String name: splitStr){
                        if(name.toLowerCase().startsWith(filterPattern)){
                            filteredList.add(item);
                            added = true;
                            break;
                        }
                    }

                    if(!added){
                        if(TextUtil.getArabic(mC)){
                            if (item.getSpecialtyAr().toLowerCase().startsWith(filterPattern)) {
                                filteredList.add(item);
                            }
                        }else{
                            if (item.getSpecialtyEn().toLowerCase().startsWith(filterPattern)) {
                                filteredList.add(item);
                            }
                        }

                    }
                /*    if(item.getNameEn().startsWith("Dr")){
                        String subString = item.getNameEn().substring(4);
                        if (subString.toLowerCase().contains(filterPattern) || item.getSpecialtyEn().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }else{
                        if (item.getNameEn().toLowerCase().contains(filterPattern) ||
                                item.getSpecialtyEn().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }*/
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}
