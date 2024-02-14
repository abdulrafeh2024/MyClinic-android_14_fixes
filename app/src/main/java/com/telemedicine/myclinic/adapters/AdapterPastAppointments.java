package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterPastAppointments extends RecyclerView.Adapter<AdapterPastAppointments.MyViewHolder> {

    List<ApptsMiniModel> list;
    Activity mC;
    OnCardClickListner onCardClickListner;

    public AdapterPastAppointments(Activity c, List<ApptsMiniModel> list) {
        this.list = list;
        mC = c;
    }

    @Override
    public AdapterPastAppointments.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (TextUtil.getEnglish(mC)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.past_doctors_list_item, parent, false);
        } else if (TextUtil.getArabic(mC)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.past_doctors_list_item_ar, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AdapterPastAppointments.MyViewHolder holder, final int position) {

        if (!ValidationHelper.isNullOrEmpty(list)) {
            DoctorsProfile doctorsProfile = list.get(position).getDoctorProfile();
            String companyName =  (TextUtil.getEnglish(mC)) ? list.get(position).getCompanyNameEn() :list.get(position).getCompanyNameAr() ;
            if (doctorsProfile != null) {

                String doctorsName = doctorsProfile.getNameEn();
                String doctorsProfession = doctorsProfile.getSpecialtyEn();


                if (TextUtil.getEnglish(mC)) {
                    doctorsName = doctorsProfile.getNameEn();
                    doctorsProfession = doctorsProfile.getTitleEn();

                } else if (TextUtil.getArabic(mC)) {
                    doctorsName = doctorsProfile.getNameAr();
                    doctorsProfession = doctorsProfile.getTitleAr();
                    holder.serviceOrders.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                }

                String dateTime = list.get(position).getApptDate();
                String doctorImage = doctorsProfile.getProfilePicUrl();

                holder.doctorsName.setText(doctorsName);
                holder.doctorProfession.setText(doctorsProfession);

                if(!list.get(position).isTelemedicine()){
                    holder.companyTextView.setText(companyName);
                }else{
                    holder.companyTextView.setVisibility(View.INVISIBLE);
                }

                if (!ValidationHelper.isNullOrEmpty(dateTime)) {
                    String date = LocaleDateHelper.convertDateStringFormat(dateTime, "yyyy-MM-dd'T'HH:mm:ss", "EEEE, dd MMMM yyyy hh:mm a");
                    holder.doctorsDateTime.setText(date);
                }

                if (!ValidationHelper.isNullOrEmpty(doctorImage)) {
                    Glide.with(mC).load(doctorImage).placeholder(R.drawable.doctr).into((ImageView) holder.doctorImage);
                }

              /*  if (list.get(position).getApptBookType() == 10) {
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
            }

            holder.serviceOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardClickListner.OnCardClicked(list.get(position), false);
                }
            });

            holder.containerCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // onCardClickListner.OnCardClicked(list.get(position), true);
                }
            });
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
        LightButton serviceOrders;
        RelativeLayout containerCard;
        BoldTextView companyTextView;
        public MyViewHolder(View view) {
            super(view);
            doctorsName = view.findViewById(R.id.doctor_name);
            doctorProfession = view.findViewById(R.id.doctor_profession);
            doctorsDateTime = view.findViewById(R.id.dateTime);
            doctorImage = view.findViewById(R.id.doctor_image);
            containerCard = view.findViewById(R.id.container_card);
            companyTextView = view.findViewById(R.id.companyTextView);
            serviceOrders = view.findViewById(R.id.view_services_btn);
        }
    }


    public interface OnCardClickListner {
        void OnCardClicked(ApptsMiniModel model, boolean cardClick);
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
