package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.telemedicine.myclinic.models.dashboardModels.ApptGetFollowUpsModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.views.RegularTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class FollowUpsAdapter extends RecyclerView.Adapter<FollowUpsAdapter.MyViewHolder> {

    List<ApptGetFollowUpsModel> list;
    Activity mC;
    OnCardClickListner onCardClickListner;

    public FollowUpsAdapter(Activity c, List<ApptGetFollowUpsModel> list) {
        this.list = list;
        mC = c;
    }

    @Override
    public FollowUpsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follow_up_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FollowUpsAdapter.MyViewHolder holder, final int position) {

        if (!ValidationHelper.isNullOrEmpty(list)) {
            if (list.get(position).getDoctorProfile() != null) {

                String doctorsName = "";
                String doctorsProfession = "";

                if (TextUtil.getEnglish(mC)) {
                    doctorsName = list.get(position).getDoctorProfile().getNameEn();
                    doctorsProfession = list.get(position).getDoctorProfile().getSpecialtyEn();
                } else if (TextUtil.getArabic(mC)) {
                    doctorsName = list.get(position).getDoctorProfile().getNameAr();
                    doctorsProfession = list.get(position).getDoctorProfile().getSpecialtyAr();
                }

                String apptDate = list.get(position).getApptDate();
                String doctorImage = list.get(position).getDoctorProfile().getProfilePicUrl();

                holder.doctorsName.setText(doctorsName);
                holder.doctorProfession.setText(doctorsProfession);

                if (!ValidationHelper.isNullOrEmpty(apptDate)) {
                    String date = LocaleDateHelper.convertDateStringFormat(apptDate, "yyyy-MM-dd'T'HH:mm:ss", "EEEE, dd MMMM yyyy hh:mm a");
                    date = mC.getString(R.string.previous_appt_was_on) + " "+ date;
                    holder.dateTime.setText(date);
                }

                if (!ValidationHelper.isNullOrEmpty(doctorImage)) {
                    Glide.with(mC).load(doctorImage).into((ImageView) holder.doctorImage);
                }

                holder.bookNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCardClickListner.OnCardClicked(list.get(position), position);
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        BoldTextView doctorsName;
        RegularTextView bookNow;
        LightTextView doctorProfession, dateTime;
        CircleImageView doctorImage;


        public MyViewHolder(View view) {
            super(view);
            doctorsName = view.findViewById(R.id.doctor_name);
            doctorProfession = view.findViewById(R.id.doctor_profession);
            dateTime = view.findViewById(R.id.dateTime);
            doctorImage = view.findViewById(R.id.doctor_image);
            bookNow = view.findViewById(R.id.book_now);
        }
    }


    public interface OnCardClickListner {
        void OnCardClicked(ApptGetFollowUpsModel model, int pos);
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
