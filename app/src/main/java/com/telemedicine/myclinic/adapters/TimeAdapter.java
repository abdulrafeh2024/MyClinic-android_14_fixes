package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.views.LightTextView;

import java.util.List;


public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.MyViewHolder> {

    List<String> list;
    Activity mC;
    OnCardClickListner onCardClickListner;
    View mv = null;
    View mvTime = null;

    public TimeAdapter(Activity c, List<String> list) {
        this.list = list;
        mC = c;
    }

    @Override
    public TimeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TimeAdapter.MyViewHolder holder, final int position) {

        if (list != null && list.size() > 0) {
            String time = list.get(position).trim();
            if (TextUtil.getArabic(mC)) {
                time = time.replaceAll(" AM", " ص");
                time = time.replaceAll(" PM", " م");
            }

            holder.time.setText(time);
            String finalTime = time;
            holder.timeContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardClickListner.OnCardClicked(list.get(position), position, v, holder.time);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LightTextView time;
        LinearLayout timeContainer;

        public MyViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.time);
            timeContainer = view.findViewById(R.id.time_container);
        }
    }


    public interface OnCardClickListner {
        void OnCardClicked(String model, int pos, View view, LightTextView textView);
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
