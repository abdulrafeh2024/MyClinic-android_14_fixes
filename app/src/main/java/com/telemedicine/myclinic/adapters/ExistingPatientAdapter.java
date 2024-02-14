package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telemedicine.myclinic.models.patientMiniModels.PatientsMiniModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.LightTextView;

import java.util.List;


public class ExistingPatientAdapter extends RecyclerView.Adapter<ExistingPatientAdapter.MyViewHolder> {

    List<PatientsMiniModel> list;
    Activity mC;

    public ExistingPatientAdapter(Activity c, List<PatientsMiniModel> list) {
        this.list = list;
        mC = c;
    }

    @Override
    public ExistingPatientAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.existing_patient_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ExistingPatientAdapter.MyViewHolder holder, final int position) {

        if (!ValidationHelper.isNullOrEmpty(list)) {
            try {
                String nameEng = list.get(position).getNameEn();
                String nameAra = list.get(position).getNameAr();
                String gender = list.get(position).getGender();
                String dob = list.get(position).getBirthDate();

                if (!ValidationHelper.isNullOrEmpty(nameEng))
                    holder.nameEng.setText(nameEng);

                if (!ValidationHelper.isNullOrEmpty(nameAra))
                    holder.nameAra.setText(nameAra);

                if (!ValidationHelper.isNullOrEmpty(gender)) {
                    if (gender.equalsIgnoreCase("1")) {
                        gender = mC.getString(R.string.male);
                        holder.gender.setText(gender);
                    } else if (gender.equalsIgnoreCase("2")) {
                        gender = mC.getString(R.string.female);
                        holder.gender.setText(gender);
                    }

                }


                if (!ValidationHelper.isNullOrEmpty(dob)) {
                    String dateTime = LocaleDateHelper.getUTCDate(dob);

                    if (!ValidationHelper.isNullOrEmpty(dateTime))
                        holder.dob.setText(dateTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LightTextView nameEng, nameAra, gender, dob;

        public MyViewHolder(View view) {
            super(view);
            nameEng = view.findViewById(R.id.name_eng);
            nameAra = view.findViewById(R.id.name_arab);
            gender = view.findViewById(R.id.gender);
            dob = view.findViewById(R.id.dob);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
