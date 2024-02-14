package com.telemedicine.myclinic.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.telemedicine.myclinic.models.patientMiniModels.InsuranceCardModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.LightTextView;

import java.util.List;


public class InsuranceCashCardAdapter extends RecyclerView.Adapter<InsuranceCashCardAdapter.MyViewHolder> {

    List<InsuranceCardModel> list;
    Activity mC;
    OnCardClickListner onCardClickListner;
    String noInsurance = "";

    public InsuranceCashCardAdapter(Activity c, List<InsuranceCardModel> list, String noInsurance) {
        this.list = list;
        mC = c;
        this.noInsurance = noInsurance;
    }

    @Override
    public InsuranceCashCardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cash_payment_ui, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final InsuranceCashCardAdapter.MyViewHolder holder, final int position) {

        if (list != null && list.size() > 0) {

            if (list.get(position) != null) {
                holder.container_cash_card.setVisibility(View.GONE);
                holder.container_insurance_card.setVisibility(View.VISIBLE);

                String memeberShipNo = list.get(position).getMembershipNo();
                String expiryDate = list.get(position).getExpiryDate();
                String insuranceCarrier = list.get(position).getInsuranceCarrier();
                String policyNo = list.get(position).getContractNo();

                if (!ValidationHelper.isNullOrEmpty(memeberShipNo))
                    holder.member_ship_no.setText(memeberShipNo);

                if (!ValidationHelper.isNullOrEmpty(expiryDate)) {
                    expiryDate = LocaleDateHelper.convertDateStringFormat(expiryDate, "yyyy-MM-dd'T'hh:mm:ss", "dd-MM-yyy");
                    holder.expiry_date.setText(expiryDate);
                }

                if (!ValidationHelper.isNullOrEmpty(insuranceCarrier))
                    holder.insurance_provider.setText(insuranceCarrier);

                if (!ValidationHelper.isNullOrEmpty(policyNo))
                    holder.policy_no.setText(policyNo);

            } else {
                holder.container_cash_card.setVisibility(View.VISIBLE);
                holder.container_insurance_card.setVisibility(View.GONE);
                holder.swipe.setVisibility(View.GONE);
            }

            holder.yesPayCash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardClickListner.OnCardClicked(list.get(position), position);
                }
            });

            holder.yesPayByInsurance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardClickListner.OnCardClicked(list.get(position), position);
                }
            });

            if (TextUtil.getArabic(mC)) {
                holder.yesPayCash.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                holder.yesPayByInsurance.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                holder.swipe.setImageDrawable(mC.getResources().getDrawable(R.drawable.arabic_swipe));

            } else
                holder.swipe.setImageResource(R.drawable.swipe);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LightTextView member_ship_no, expiry_date, insurance_provider, policy_no;

        RelativeLayout container_cash_card, container_insurance_card;

        LightButton yesPayCash, yesPayByInsurance;

        ImageView swipe;

        public MyViewHolder(View view) {
            super(view);
            container_cash_card = view.findViewById(R.id.payment_by_cash_container);
            container_insurance_card = view.findViewById(R.id.payment_by_insurance_card);
            member_ship_no = view.findViewById(R.id.member_ship_no);
            expiry_date = view.findViewById(R.id.expiry_date);
            insurance_provider = view.findViewById(R.id.insurance_provider);
            policy_no = view.findViewById(R.id.policy_no);
            yesPayCash = view.findViewById(R.id.yes_pay_cash);
            yesPayByInsurance = view.findViewById(R.id.yes_pay_by_insurance);
            swipe = view.findViewById(R.id.swipe);
        }
    }


    public interface OnCardClickListner {
        void OnCardClicked(InsuranceCardModel model, int pos);
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
