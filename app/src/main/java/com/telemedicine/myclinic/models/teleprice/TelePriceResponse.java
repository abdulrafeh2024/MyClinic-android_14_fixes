package com.telemedicine.myclinic.models.teleprice;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

public class TelePriceResponse extends BaseEntity {

    @SerializedName("AmountDue")
    double AmountDue;

    public double getAmountDue() {
        return AmountDue;
    }

    public void setAmountDue(double amountDue) {
        AmountDue = amountDue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeDouble(this.AmountDue);
    }

    public TelePriceResponse() {
    }

    protected TelePriceResponse(Parcel in) {
        super(in);
        this.AmountDue = in.readLong();
    }

    public static final Creator<TelePriceResponse> CREATOR = new Creator<TelePriceResponse>() {
        @Override
        public TelePriceResponse createFromParcel(Parcel source) {
            return new TelePriceResponse(source);
        }

        @Override
        public TelePriceResponse[] newArray(int size) {
            return new TelePriceResponse[size];
        }
    };
}
