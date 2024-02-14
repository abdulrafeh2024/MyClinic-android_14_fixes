package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telemedicine.myclinic.models.appointments.OrdersMedModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.EnglishRegularTextView;

import java.util.ArrayList;


public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MyViewHolder> {

    ArrayList<OrdersMedModel> list;
    Activity mC;

    public MedicationAdapter(Activity c, ArrayList<OrdersMedModel> list) {
        this.list = list;
        mC = c;
    }

    @Override
    public MedicationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.med_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MedicationAdapter.MyViewHolder holder, final int position) {

        if (list != null && list.size() > 0) {

            String orderStatus = list.get(position).getOrderStatus();

            if (!orderStatus.equalsIgnoreCase("2")) {

                String service = list.get(position).getName();
                String date = list.get(position).getDosageDesc();

                if (!ValidationHelper.isNullOrEmpty(service))
                    holder.serviceTv.setText(service);

                if (!ValidationHelper.isNullOrEmpty(date)) {
                    holder.dateTv.setText(date);
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        EnglishRegularTextView serviceTv;
        EnglishRegularTextView dateTv;

        public MyViewHolder(View view) {
            super(view);
            serviceTv = view.findViewById(R.id.service_tv);
            dateTv = view.findViewById(R.id.date_tv);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
