package com.telemedicine.myclinic.activities.multiservices;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

import java.util.ArrayList;

public class PaymentOrder extends BaseEntity implements Parcelable {

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public double getDeductibleAmount() {
        return DeductibleAmount;
    }

    public void setDeductibleAmount(double deductibleAmount) {
        DeductibleAmount = deductibleAmount;
    }

    public double getDeductiblePercent() {
        return DeductiblePercent;
    }

    public void setDeductiblePercent(double deductiblePercent) {
        DeductiblePercent = deductiblePercent;
    }

    public double getAmountDue() {
        return AmountDue;
    }

    public void setAmountDue(double amountDue) {
        AmountDue = amountDue;
    }

    public long getApptId() {
        return ApptId;
    }

    public void setApptId(long apptId) {
        ApptId = apptId;
    }

    public ArrayList<ServiceItems> getServiceItems() {
        return serviceItems;
    }

    public void setServiceItems(ArrayList<ServiceItems> serviceItems) {
        this.serviceItems = serviceItems;
    }

    @SerializedName("Id")
    long Id;

    @SerializedName("DeductibleAmount")
    double DeductibleAmount;

    @SerializedName("DeductiblePercent")
    double DeductiblePercent;

    @SerializedName("AmountDue")
    double AmountDue;

    @SerializedName("ApptId")
    long ApptId;

    @SerializedName("ServiceItems")
    ArrayList<ServiceItems> serviceItems;

    protected PaymentOrder(Parcel in) {
        super(in);
        Id = in.readLong();
        DeductibleAmount = in.readDouble();
        DeductiblePercent = in.readDouble();
        AmountDue = in.readDouble();
        ApptId = in.readLong();
        serviceItems = in.createTypedArrayList(ServiceItems.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(Id);
        dest.writeDouble(DeductibleAmount);
        dest.writeDouble(DeductiblePercent);
        dest.writeDouble(AmountDue);
        dest.writeLong(ApptId);
        dest.writeTypedList(serviceItems);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaymentOrder> CREATOR = new Creator<PaymentOrder>() {
        @Override
        public PaymentOrder createFromParcel(Parcel in) {
            return new PaymentOrder(in);
        }

        @Override
        public PaymentOrder[] newArray(int size) {
            return new PaymentOrder[size];
        }
    };
}
