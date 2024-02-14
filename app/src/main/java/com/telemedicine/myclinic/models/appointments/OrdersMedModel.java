package com.telemedicine.myclinic.models.appointments;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class OrdersMedModel implements Parcelable {

    @SerializedName("Id")
    long Id;

    @SerializedName("Name")
    String Name;

    @SerializedName("Duration")
    long Duration;

    @SerializedName("Dose")
    double Dose;

    @SerializedName("Route")
    String Route;

    @SerializedName("Frequency")
    String Frequency;

    @SerializedName("Unit")
    String Unit;

    @SerializedName("DosageDesc")
    String DosageDesc;

    @SerializedName("OrderStatus")
    String OrderStatus;

    @SerializedName("OrderStatusDesc")
    String OrderStatusDesc;

    @SerializedName("OrderedDate")
    String OrderedDate;

    protected OrdersMedModel(Parcel in) {
        Id = in.readLong();
        Name = in.readString();
        Duration = in.readLong();
        Dose = in.readLong();
        Route = in.readString();
        Frequency = in.readString();
        Unit = in.readString();
        DosageDesc = in.readString();
        OrderStatus = in.readString();
        OrderStatusDesc = in.readString();
        OrderedDate = in.readString();
    }

    public static final Creator<OrdersMedModel> CREATOR = new Creator<OrdersMedModel>() {
        @Override
        public OrdersMedModel createFromParcel(Parcel in) {
            return new OrdersMedModel(in);
        }

        @Override
        public OrdersMedModel[] newArray(int size) {
            return new OrdersMedModel[size];
        }
    };

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getDuration() {
        return Duration;
    }

    public void setDuration(long duration) {
        Duration = duration;
    }

    public double getDose() {
        return Dose;
    }

    public void setDose(long dose) {
        Dose = dose;
    }

    public String getRoute() {
        return Route;
    }

    public void setRoute(String route) {
        Route = route;
    }

    public String getFrequency() {
        return Frequency;
    }

    public void setFrequency(String frequency) {
        Frequency = frequency;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getDosageDesc() {
        return DosageDesc;
    }

    public void setDosageDesc(String dosageDesc) {
        DosageDesc = dosageDesc;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getOrderStatusDesc() {
        return OrderStatusDesc;
    }

    public void setOrderStatusDesc(String orderStatusDesc) {
        OrderStatusDesc = orderStatusDesc;
    }

    public String getOrderedDate() {
        return OrderedDate;
    }

    public void setOrderedDate(String orderedDate) {
        OrderedDate = orderedDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(Id);
        dest.writeString(Name);
        dest.writeLong(Duration);
        dest.writeDouble(Dose);
        dest.writeString(Route);
        dest.writeString(Frequency);
        dest.writeString(Unit);
        dest.writeString(DosageDesc);
        dest.writeString(OrderStatus);
        dest.writeString(OrderStatusDesc);
        dest.writeString(OrderedDate);
    }
}
