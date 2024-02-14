package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.telemedicine.myclinic.models.appointments.OrdersLabModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.EnglishRegularTextView;
import com.telemedicine.myclinic.views.LightTextView;

import java.util.List;


public class LabTestAdapter extends RecyclerView.Adapter<LabTestAdapter.MyViewHolder> {

    List<OrdersLabModel> list;
    Activity mC;
    OnCardClickListner onCardClickListner;

    public LabTestAdapter(Activity c, List<OrdersLabModel> list) {
        this.list = list;
        mC = c;
    }

    @Override
    public LabTestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lab_test_list_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LabTestAdapter.MyViewHolder holder, final int position) {

        if (list != null && list.size() > 0) {
            String name = list.get(position).getName();
            String orderDate = list.get(position).getOrderDate();
            String status = list.get(position).getOrderStatus();

            if (!ValidationHelper.isNullOrEmpty(name))
                holder.nameTv.setText(name);

            if (!ValidationHelper.isNullOrEmpty(orderDate))
                orderDate = LocaleDateHelper.convertDateStringFormat(orderDate, "yyyy-MM-dd'T'hh:mm:ss", "dd-MM-yyyy");

            if (!ValidationHelper.isNullOrEmpty(orderDate))
                holder.orderDateTv.setText(orderDate);

            holder.download.setVisibility(View.GONE);

            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardClickListner.OnCardClicked(list.get(position), position);
                }
            });

            if (!ValidationHelper.isNullOrEmpty(status)) {

                String statusTxt = "";

                switch (status) {
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

                holder.statusTv.setText(statusTxt);
            }

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LightTextView orderDateTv, statusTv;
        EnglishRegularTextView nameTv;
        ImageView download;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.name);
            orderDateTv = view.findViewById(R.id.order_date);
            statusTv = view.findViewById(R.id.status);
            download = view.findViewById(R.id.download);
        }
    }


    public interface OnCardClickListner {
        void OnCardClicked(OrdersLabModel model, int pos);
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
