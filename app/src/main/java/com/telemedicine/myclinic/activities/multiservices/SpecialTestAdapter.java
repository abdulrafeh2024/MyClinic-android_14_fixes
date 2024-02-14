package com.telemedicine.myclinic.activities.multiservices;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.EnglishRegularTextView;

import java.util.ArrayList;



public class SpecialTestAdapter extends RecyclerView.Adapter<SpecialTestAdapter.MyViewHolder> {

    ArrayList<ServiceItems> list;
    Activity mC;

    public SpecialTestAdapter(Activity c, ArrayList<ServiceItems> list) {
        this.list = list;
        mC = c;
    }

    @Override
    public SpecialTestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.med_list_item, parent, false);

        return new SpecialTestAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SpecialTestAdapter.MyViewHolder holder, final int position) {

        if (list != null && list.size() > 0) {

            String orderStatus = list.get(position).getServiceStatus();

            String service = list.get(position).getName();
            String status = list.get(position).getServiceStatus();

            if (!ValidationHelper.isNullOrEmpty(service))
                holder.serviceTv.setText(service);

            if (!ValidationHelper.isNullOrEmpty(status)) {
                holder.dateTv.setText(status);
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
