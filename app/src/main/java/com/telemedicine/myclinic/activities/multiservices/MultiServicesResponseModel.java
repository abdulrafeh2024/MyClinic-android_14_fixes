package com.telemedicine.myclinic.activities.multiservices;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.appointments.ApptModel;


public class MultiServicesResponseModel extends BaseEntity implements Parcelable {

    @SerializedName("PaymentOrder")
    PaymentOrder paymentOrder;

    protected MultiServicesResponseModel(Parcel in) {
        super(in);
        paymentOrder = in.readParcelable(PaymentOrder.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(paymentOrder, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MultiServicesResponseModel> CREATOR = new Creator<MultiServicesResponseModel>() {
        @Override
        public MultiServicesResponseModel createFromParcel(Parcel in) {
            return new MultiServicesResponseModel(in);
        }

        @Override
        public MultiServicesResponseModel[] newArray(int size) {
            return new MultiServicesResponseModel[size];
        }
    };

    public PaymentOrder getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(PaymentOrder paymentOrder) {
        this.paymentOrder = paymentOrder;
    }
}