package com.telemedicine.myclinic.models;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class PaymentResponsibilty extends BaseEntity {


    @SerializedName("PayResAnswer")
    PayResAnswer payResAnswer;

    public PayResAnswer getPayResAnswer() {
        return payResAnswer;
    }

    public void setPayResAnswer(PayResAnswer payResAnswer) {
        this.payResAnswer = payResAnswer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.payResAnswer, flags);
    }

    public PaymentResponsibilty() {
    }

    protected PaymentResponsibilty(Parcel in) {
        super(in);
        this.payResAnswer = in.readParcelable(PayResAnswer.class.getClassLoader());
    }

    public static final Creator<PaymentResponsibilty> CREATOR = new Creator<PaymentResponsibilty>() {
        @Override
        public PaymentResponsibilty createFromParcel(Parcel source) {
            return new PaymentResponsibilty(source);
        }

        @Override
        public PaymentResponsibilty[] newArray(int size) {
            return new PaymentResponsibilty[size];
        }
    };
}
