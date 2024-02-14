package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telemedicine.myclinic.models.appointments.OrdersLabModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.LightTextView;

import java.util.List;


public class SimpleLabTestAdapter extends RecyclerView.Adapter<SimpleLabTestAdapter.MyViewHolder> {

    List<OrdersLabModel> list;
    Activity mC;

    public SimpleLabTestAdapter(Activity c, List<OrdersLabModel> list) {
        this.list = list;
        mC = c;
    }

    @Override
    public SimpleLabTestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_lab_test_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SimpleLabTestAdapter.MyViewHolder holder, final int position) {

        if (list != null && list.size() > 0) {
            String name = list.get(position).getName();
            if (!ValidationHelper.isNullOrEmpty(name))
                holder.nameTv.setText(name);

            if (TextUtil.getArabic(mC))
                holder.nameTv.setGravity(Gravity.RIGHT);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LightTextView nameTv;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.name);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
