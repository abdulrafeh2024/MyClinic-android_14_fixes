package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.telemedicine.myclinic.models.LabReportsResponseModel;
import com.telemedicine.myclinic.models.appointments.OrdersLabModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.EnglishRegularTextView;
import com.telemedicine.myclinic.views.LightTextView;

import java.util.List;


public class LabReportAdapter extends RecyclerView.Adapter<LabReportAdapter.MyViewHolder> {

    List<LabReportsResponseModel> list;
    Activity mC;
    OnCardClickListner onCardClickListner;

    public LabReportAdapter(Activity c, List<LabReportsResponseModel> list) {
        this.list = list;
        mC = c;
    }

    @Override
    public LabReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lab_report_item_view, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LabReportAdapter.MyViewHolder holder, final int position) {

        if (list != null && list.size() > 0) {
            String name = list.get(position).getName();
            String orderDate = list.get(position).getOrderDate();
            int status = list.get(position).getOrderType();

            if(status == 3){
                holder.share.setVisibility(View.VISIBLE);
            }else{
                holder.share.setVisibility(View.GONE);
            }
            if (!ValidationHelper.isNullOrEmpty(name))
                holder.nameTv.setText(name);

            if (!ValidationHelper.isNullOrEmpty(orderDate))
                orderDate = LocaleDateHelper.convertDateStringFormat(orderDate, "yyyy-MM-dd'T'hh:mm:ss", "dd-MM-yyyy");

            if (!ValidationHelper.isNullOrEmpty(orderDate))
                holder.orderDateTv.setText(orderDate);

            if(!TextUtil.getArabic(mC)){
                holder.specialityLab.setText(list.get(position).getSpecialtyEn());
                holder.doctorLabTv.setText(list.get(position).getDoctorNameEn());
            }else{
                holder.specialityLab.setText(list.get(position).getSpecialtyAr());
                holder.doctorLabTv.setText(list.get(position).getDoctorNameAr());
            }

            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardClickListner.OnCardClicked(list.get(position), position, false);
                }
            });

            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardClickListner.OnCardClicked(list.get(position), position, true);
                }
            });


         /*   holder.orderDateTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardClickListner.OnCardClicked(list.get(position), position, true);
                }
            });*/

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LightTextView orderDateTv, nameTv, doctorLabTv, specialityLab;
        ImageView download, share;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameLab);
            orderDateTv = view.findViewById(R.id.dateLab);
            specialityLab = view.findViewById(R.id.specialityLab);
            doctorLabTv = view.findViewById(R.id.doctorLab);
            download = view.findViewById(R.id.download);
            share = view.findViewById(R.id.share);
        }
    }


    public interface OnCardClickListner {
        void OnCardClicked(LabReportsResponseModel model, int pos, boolean isShare);
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

