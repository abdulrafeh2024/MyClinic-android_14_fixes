package com.telemedicine.myclinic.activities.multiservices;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.telemedicine.myclinic.models.NotificationMiniModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.views.RegularTextView;

import java.util.ArrayList;
import java.util.Locale;

public class ServicesListAdapter extends RecyclerView.Adapter<ServicesListAdapter.MyViewHolder>  {

    ArrayList<ServiceItems> list;
    Activity mC;
    OnCardClickListner onCardClickListner;
    TimeAgoMessages messages = null;

    public ServicesListAdapter(Activity c, ArrayList<ServiceItems> list) {
        this.list = list;
        mC = c;
        if (TextUtil.getEnglish(c)) {
            Locale LocaleBylanguageTag = Locale.forLanguageTag("en");
            messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
        } else if (TextUtil.getArabic(c)) {
            Locale LocaleBylanguageTag = Locale.forLanguageTag("ar");
            messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
        }
    }

    @Override
    public void onBindViewHolder(final ServicesListAdapter.MyViewHolder holder, final int position) {
        if (list != null && list.size() > 0) {
            if (!ValidationHelper.isNullOrEmpty(list.get(position).getName())) {
                if (TextUtil.getEnglish(mC)) {
                    holder.serviceName.setText(list.get(position).getName());
                }else{
                    holder.serviceName.setText(list.get(position).getName());
                }
            }

            if(!list.get(position).isPayAllowed()){
                holder.checkbox.setVisibility(View.INVISIBLE);
            }else{
                holder.checkbox.setVisibility(View.VISIBLE);
            }
            holder.amountTv.setText("Amount: "+list.get(position).getAmount());
            holder.paymentStatus.setText(list.get(position).BillStatus);
            holder.serviceStatus.setText(list.get(position).getServiceStatus());
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 /*   if(holder.checkbox.isChecked()){
                        list.get(position).setSelected(true);
                        notifyItemChanged(position);
                    }else{
                        list.get(position).setSelected(false);
                        notifyItemChanged(position);
                    }*/

                    onCardClickListner.OnCardClicked(list.get(position), position,holder.checkbox.isChecked());
                }
            });
        }
    }

    @Override
    public ServicesListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;

        if (TextUtil.getEnglish(mC)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_services_list_layout, parent, false);
        } else if (TextUtil.getArabic(mC)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_services_list_layout, parent, false);
        }

        return new ServicesListAdapter.MyViewHolder(itemView);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        BoldTextView serviceName;
        LightTextView amountTv, paymentStatus;
        LightButton serviceStatus;
        AppCompatCheckBox checkbox;

        public MyViewHolder(View view) {
            super(view);
            serviceName = view.findViewById(R.id.serviceName);
            paymentStatus = view.findViewById(R.id.paymentStatus);
            amountTv = view.findViewById(R.id.amountTv);
            serviceStatus = view.findViewById(R.id.serviceStatus);
            checkbox = view.findViewById(R.id.checkbox);
        }
    }

    public interface OnCardClickListner {
        void OnCardClicked(ServiceItems model, int pos, boolean isChecked);
    }

    public void setOnCardClickListner(ServicesListAdapter.OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
