package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class DoctorsProfileAdapter extends RecyclerView.Adapter<DoctorsProfileAdapter.MyViewHolder> {

    List<DoctorsProfile> list;
    Activity mC;
    OnCardClickListner onCardClickListner;
    View mView = null;
    boolean click = true;
    boolean fromHome = false;
    public DoctorsProfileAdapter(Activity c, List<DoctorsProfile> list) {
        this.list = list;
        mC = c;
    }

    public DoctorsProfileAdapter(Activity c, List<DoctorsProfile> list, boolean clickable) {
        this.list = list;
        mC = c;
        click = clickable;
    }

    public DoctorsProfileAdapter(Activity c, List<DoctorsProfile> list, boolean clickable, boolean fromHome) {
        this.list = list;
        mC = c;
        click = clickable;
        this.fromHome = fromHome;
    }

    @Override
    public DoctorsProfileAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;

        if (TextUtil.getEnglish(mC)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.doctors_list_item, parent, false);
        } else if (TextUtil.getArabic(mC)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.doctors_list_items_ar, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DoctorsProfileAdapter.MyViewHolder holder, final int position) {

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

                    if (!fromHome) {
                        if (!click) return;

                        if (mView != null)
                            mView.setBackgroundResource(R.color.colorWhite);

                        mView = v;

                        holder.doctorContainer.setBackgroundResource(R.color.colorGrey);
                    }
                    onCardClickListner.OnCardClicked(list.get(position), position);

                }
            });

           /* if (list.size() == 1) {
                holder.doctorContainer.setBackgroundResource(R.color.colorGrey);
                mView = holder.doctorContainer;
            }*/
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

        public MyViewHolder(View view) {
            super(view);
            doctorsName = view.findViewById(R.id.doctor_name);
            doctorProfession = view.findViewById(R.id.doctor_profession);
            doctorsDegree = view.findViewById(R.id.doctor_profession_degree);
            doctorImage = view.findViewById(R.id.doctor_image);
            doctorContainer = view.findViewById(R.id.doctor_container);
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
}
