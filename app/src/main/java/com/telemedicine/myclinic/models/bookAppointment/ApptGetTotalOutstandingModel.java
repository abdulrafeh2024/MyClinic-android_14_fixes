package com.telemedicine.myclinic.models.bookAppointment;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

public class ApptGetTotalOutstandingModel extends BaseEntity {


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

    public ApptGetTotalOutstandingModel() {
    }

    protected ApptGetTotalOutstandingModel(Parcel in) {
        super(in);
        this.AmountDue = in.readDouble();
    }

    public static final Creator<ApptGetTotalOutstandingModel> CREATOR = new Creator<ApptGetTotalOutstandingModel>() {
        @Override
        public ApptGetTotalOutstandingModel createFromParcel(Parcel source) {
            return new ApptGetTotalOutstandingModel(source);
        }

        @Override
        public ApptGetTotalOutstandingModel[] newArray(int size) {
            return new ApptGetTotalOutstandingModel[size];
        }
    };
}
