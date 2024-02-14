package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telemedicine.myclinic.models.MedicationModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.views.LightTextView;

import java.util.List;


public class AdapterMedications extends RecyclerView.Adapter<AdapterMedications.MyViewHolder> {

    List<MedicationModel> list;
    Activity mC;

    public AdapterMedications(Activity c, List<MedicationModel> list) {
        this.list = list;
        mC = c;
    }

    @Override
    public AdapterMedications.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medication_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AdapterMedications.MyViewHolder holder, final int position) {

        if (list != null && list.size() > 0) {
            String nameMedication = list.get(position).getMedication();
            String nameDosage = list.get(position).getDosage();
            String nquantity = list.get(position).getQty();
            String unit = list.get(position).getUnit();

            holder.name_medication.setText(nameMedication);
            holder.name_dosage.setText(nameDosage);
            holder.quanity.setText(nquantity);
            holder.unit.setText(unit);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LightTextView name_medication;
        LightTextView name_dosage, quanity;
        LightTextView unit;

        public MyViewHolder(View view) {
            super(view);
            name_medication = view.findViewById(R.id.name_medication);
            name_dosage = view.findViewById(R.id.name_dosage);
            quanity = view.findViewById(R.id.quanity);
            unit = view.findViewById(R.id.unit);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
