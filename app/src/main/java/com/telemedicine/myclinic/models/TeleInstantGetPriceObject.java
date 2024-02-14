package com.telemedicine.myclinic.models;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class TeleInstantGetPriceObject extends BaseEntity {

    @SerializedName("AmountDue")
    Double amountDue;

    @SerializedName("WaitingTimeMin")
    Double WaitingTimeMin;

    public Double getWaitingTimeMin() {
        return WaitingTimeMin;
    }

    public void setWaitingTimeMin(Double waitingTimeMin) {
        WaitingTimeMin = waitingTimeMin;
    }

    public Double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(Double amountDue) {
        this.amountDue = amountDue;
    }

    public TeleInstantGetPriceObject() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.amountDue);
        dest.writeValue(this.WaitingTimeMin);
    }

    protected TeleInstantGetPriceObject(Parcel in) {
        super(in);
        this.amountDue = (Double) in.readValue(Double.class.getClassLoader());
        this.WaitingTimeMin = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Creator<TeleInstantGetPriceObject> CREATOR = new Creator<TeleInstantGetPriceObject>() {
        @Override
        public TeleInstantGetPriceObject createFromParcel(Parcel source) {
            return new TeleInstantGetPriceObject(source);
        }

        @Override
        public TeleInstantGetPriceObject[] newArray(int size) {
            return new TeleInstantGetPriceObject[size];
        }
    };
}
