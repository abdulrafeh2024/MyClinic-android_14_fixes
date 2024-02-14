package com.telemedicine.myclinic.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PayResAnswer implements Parcelable {


    @SerializedName("ShowPaymentResponsibility")
    boolean showPaymentResponsibilty;

    @SerializedName("ReasonEn")
    String reasonEN;

    @SerializedName("ReasonAr")
    String reasonAr;

    public boolean isShowPaymentResponsibilty() {
        return showPaymentResponsibilty;
    }

    public void setShowPaymentResponsibilty(boolean showPaymentResponsibilty) {
        this.showPaymentResponsibilty = showPaymentResponsibilty;
    }

    public String getReasonEN() {
        return reasonEN;
    }

    public void setReasonEN(String reasonEN) {
        this.reasonEN = reasonEN;
    }

    public String getReasonAr() {
        return reasonAr;
    }

    public void setReasonAr(String reasonAr) {
        this.reasonAr = reasonAr;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.showPaymentResponsibilty ? (byte) 1 : (byte) 0);
        dest.writeString(this.reasonEN);
        dest.writeString(this.reasonAr);
    }

    public PayResAnswer() {
    }

    protected PayResAnswer(Parcel in) {
        this.showPaymentResponsibilty = in.readByte() != 0;
        this.reasonEN = in.readString();
        this.reasonAr = in.readString();
    }

    public static final Parcelable.Creator<PayResAnswer> CREATOR = new Parcelable.Creator<PayResAnswer>() {
        @Override
        public PayResAnswer createFromParcel(Parcel source) {
            return new PayResAnswer(source);
        }

        @Override
        public PayResAnswer[] newArray(int size) {
            return new PayResAnswer[size];
        }
    };
}
