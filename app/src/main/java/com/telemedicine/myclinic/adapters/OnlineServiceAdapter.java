package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telemedicine.myclinic.models.OnlineServicesModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.views.LightTextView;

import java.util.List;


public class OnlineServiceAdapter extends RecyclerView.Adapter<OnlineServiceAdapter.MyViewHolder> {

    List<OnlineServicesModel> list;
    Activity mC;

    public OnlineServiceAdapter(Activity c, List<OnlineServicesModel> list) {
        this.list = list;
        mC = c;
    }

    @Override
    public OnlineServiceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_services_item_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OnlineServiceAdapter.MyViewHolder holder, final int position) {

        if (list != null && list.size() > 0) {

            String number = list.get(position).getServiceNumber();
            String type = list.get(position).getServiceType();
            String money = list.get(position).getServiceMoney();

            holder.serviceNumber.setText(number);
            holder.serviceType.setText(type);
            holder.serviceMoney.setText(money);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LightTextView serviceNumber, serviceType, serviceMoney;

        public MyViewHolder(View view) {
            super(view);
            serviceNumber = view.findViewById(R.id.service_number);
            serviceType = view.findViewById(R.id.service_type);
            serviceMoney = view.findViewById(R.id.money);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
