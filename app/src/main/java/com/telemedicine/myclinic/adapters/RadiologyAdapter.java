package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.telemedicine.myclinic.models.appointments.OrdersRadModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.EnglishRegularTextView;
import com.telemedicine.myclinic.views.RegularTextView;

import java.util.ArrayList;


public class RadiologyAdapter extends RecyclerView.Adapter<RadiologyAdapter.MyViewHolder> {

    ArrayList<OrdersRadModel> list;
    Activity mC;
    OnCardClickListner onCardClickListner;

    public RadiologyAdapter(Activity c, ArrayList<OrdersRadModel> list) {
        this.list = list;
        mC = c;
    }

    @Override
    public RadiologyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.radiology_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RadiologyAdapter.MyViewHolder holder, final int position) {

        if (!ValidationHelper.isNullOrEmpty(list)) {
            String service = list.get(position).getName();
            String date = list.get(position).getOrderDate();
            long orderStatus = list.get(position).getOrderStatus();

            if (!ValidationHelper.isNullOrEmpty(date))
                date = LocaleDateHelper.convertDateStringFormat(date, "yyyy-MM-dd'T'hh:mm:ss", "dd-MM-yyyy");

            if (!ValidationHelper.isNullOrEmpty(service))
                holder.serviceTv.setText(service);

            /*if (!ValidationHelper.isNullOrEmpty(date))
                holder.dateTv.setText(date);*/

            if (!ValidationHelper.isNullOrEmpty(String.valueOf(orderStatus))) {

                String statusTxt = "";

                switch (String.valueOf(orderStatus)) {
                    case "1":
                        statusTxt = mC.getString(R.string.ordered);
                        break;
                    case "2":
                        statusTxt = "";//mC.getString(R.string.cancelled);
                        break;
                    case "3":
                        statusTxt = mC.getString(R.string.paid);
                        break;
                    case "4":
                        statusTxt = mC.getString(R.string.dispensed);
                        break;
                    case "5":
                        statusTxt = mC.getString(R.string.prepared);
                        break;
                    case "6":
                        statusTxt = mC.getString(R.string.declined);
                        break;
                    case "7":
                        statusTxt = mC.getString(R.string.awaiting_services);
                        break;
                    case "8":
                        statusTxt = mC.getString(R.string.service_being_rendered);
                        break;
                    case "9":
                        statusTxt = mC.getString(R.string.service_signed_off);
                        break;
                    case "10":
                        statusTxt = mC.getString(R.string.discontinued);
                        break;
                    case "11":
                        statusTxt = mC.getString(R.string.started);
                        break;
                    case "12":
                        statusTxt = mC.getString(R.string.completed);
                        break;
                    case "13":
                        statusTxt = mC.getString(R.string.verified_and_reported);
                        holder.download.setVisibility(View.VISIBLE);
                        break;
                    case "14":
                        statusTxt = mC.getString(R.string.sample_collected);
                        break;
                    case "15":
                        statusTxt = mC.getString(R.string.sample_recieved);
                        break;
                    case "16":
                        statusTxt = mC.getString(R.string.sample_dispatched);
                        break;
                    case "17":
                        statusTxt = mC.getString(R.string.sample_sent_out);
                        break;
                    case "18":
                        statusTxt = mC.getString(R.string.sample_in_process);
                        break;
                    case "19":
                        statusTxt = mC.getString(R.string.need_second_sample);
                        break;
                    case "20":
                        statusTxt = mC.getString(R.string.quantity_not_sufficient);
                        break;
                    case "21":
                        statusTxt = mC.getString(R.string.sample_rejected);
                        break;
                    case "22":
                        statusTxt = mC.getString(R.string.sample_refused);
                        break;
                    case "23":
                        statusTxt = mC.getString(R.string.pending);
                        break;
                    case "275380000":
                        statusTxt = mC.getString(R.string.created);
                        break;
                    case "275380001":
                        statusTxt = mC.getString(R.string.partialy_paid);
                        break;

                }

                holder.dateTv.setText(statusTxt);
            }

            switch (String.valueOf(orderStatus)) {
                case "12":
                    holder.button.setVisibility(View.VISIBLE);
                    holder.button.setText("");
                    holder.button.setVisibility(View.GONE);
                    holder.download.setVisibility(View.VISIBLE);
                    break;
                case "1":
                    holder.button.setVisibility(View.VISIBLE);
                    holder.button.setText(R.string.book);
                    holder.button.setVisibility(View.VISIBLE);
                    holder.download.setVisibility(View.GONE);
                    break;
                case "2":
                    holder.button.setVisibility(View.VISIBLE);
                    holder.button.setText(R.string.book);
                    holder.button.setVisibility(View.VISIBLE);
                    holder.download.setVisibility(View.GONE);
                    break;
                case "1000":
                    holder.button.setVisibility(View.VISIBLE);
                    holder.button.setText(R.string.view);
                    holder.button.setVisibility(View.VISIBLE);
                    holder.download.setVisibility(View.GONE);
                    break;

                default:
                    holder.download.setVisibility(View.GONE);
                    break;

            }

            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = holder.button.getText().toString();

                    if (str.equalsIgnoreCase(mC.getResources().getString(R.string.view))) {
                        onCardClickListner.OnCardClicked(list.get(position), position, false, true, false);
                    } else if (str.equalsIgnoreCase(mC.getResources().getString(R.string.book))) {
                        onCardClickListner.OnCardClicked(list.get(position), position, true, false, false);
                    } else if (ValidationHelper.isNullOrEmpty(str)) {
                        onCardClickListner.OnCardClicked(list.get(position), position, false, false, true);
                    }
                }
            });

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String str = holder.button.getText().toString();

                    if (str.equalsIgnoreCase(mC.getResources().getString(R.string.view))) {
                        onCardClickListner.OnCardClicked(list.get(position), position, false, true, false);
                    } else if (str.equalsIgnoreCase(mC.getResources().getString(R.string.book))) {
                        onCardClickListner.OnCardClicked(list.get(position), position, true, false, false);
                    } else if (ValidationHelper.isNullOrEmpty(str)) {
                        onCardClickListner.OnCardClicked(list.get(position), position, false, false, true);
                    }

                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        EnglishRegularTextView serviceTv;
        EnglishRegularTextView dateTv;
        RegularTextView button;
        ImageView download;

        public MyViewHolder(View view) {
            super(view);
            serviceTv = view.findViewById(R.id.service_tv);
            dateTv = view.findViewById(R.id.date_tv);
            button = view.findViewById(R.id.button);
            download = view.findViewById(R.id.download);
        }
    }


    public interface OnCardClickListner {
        void OnCardClicked(OrdersRadModel model, int pos, boolean isBook, boolean isView, boolean isDownload);
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
