package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.telemedicine.myclinic.models.patientMiniModels.PatientsMiniModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class SwitchDoctorsAdapter extends RecyclerView.Adapter<SwitchDoctorsAdapter.MyViewHolder> {

    List<PatientsMiniModel> list;
    Activity mC;
    OnCardClickListner onCardClickListner;
    long patientId = 0;


    public SwitchDoctorsAdapter(Activity c, List<PatientsMiniModel> list) {
        this.list = list;
        mC = c;
        patientId = new TinyDB(c).getLong(Const.PATIENT_ID, 0);
    }

    @Override
    public SwitchDoctorsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        if (TextUtil.getEnglish(mC)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.switch_profile_list_items, parent, false);
        } else if (TextUtil.getArabic(mC)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.switch_profile_list_items_ar, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SwitchDoctorsAdapter.MyViewHolder holder, final int position) {

        if (list != null && list.size() > 0) {

            String doctorsName = "";

            if (TextUtil.getEnglish(mC))
                doctorsName = list.get(position).getNameEn();
            else if (TextUtil.getArabic(mC))
                doctorsName = list.get(position).getNameAr();

            String dob = list.get(position).getBirthDate();

            String gender = list.get(position).getGender();

            if (!ValidationHelper.isNullOrEmpty(gender)) {

                if (gender.equalsIgnoreCase("1")) {
                    holder.doctorImage.setImageDrawable(mC.getResources().getDrawable(R.drawable.male));

                } else if (gender.equalsIgnoreCase("2")) {
                    holder.doctorImage.setImageDrawable(mC.getResources().getDrawable(R.drawable.female));
                }
            }

            if (!ValidationHelper.isNullOrEmpty(dob)) {
                dob = LocaleDateHelper.convertDateStringFormat(dob, "yyyy-MM-dd'T'hh:mm:ss", "MM-dd-yyyy");
                holder.doctorAge.setText(dob);
            }

            String doctorImage = "";

            holder.doctorsName.setText(doctorsName);

            if (!ValidationHelper.isNullOrEmpty(doctorImage)) {
                Glide.with(mC).load(doctorImage).into((ImageView) holder.doctorImage);
            }

            holder.select_patient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onCardClickListner.OnCardClicked(list.get(position), position);

                }
            });

            if (patientId == list.get(position).getPatientId()) {
                holder.select_patient.setBackgroundColor(mC.getResources().getColor(R.color.colorPrimaryDisbale));
                holder.select_patient.setEnabled(false);
                holder.select_patient.setTextColor(mC.getResources().getColor(R.color.colorGrey));
            }

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        BoldTextView doctorsName;
        LightTextView doctorAge, select_patient;
        CircleImageView doctorImage;

        public MyViewHolder(View view) {
            super(view);
            doctorsName = view.findViewById(R.id.doctor_name);
            doctorAge = view.findViewById(R.id.doctor_age);
            select_patient = view.findViewById(R.id.select_patient);
            doctorImage = view.findViewById(R.id.doctor_image);
        }
    }

    public interface OnCardClickListner {
        void OnCardClicked(PatientsMiniModel model, int pos);
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
