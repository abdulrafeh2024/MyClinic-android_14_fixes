package com.telemedicine.myclinic.models.appointments;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class OrdersLabModel implements Parcelable {

    @SerializedName("Id")
    long Id;

    @SerializedName("Name")
    String Name;

    @SerializedName("OrderDate")
    String OrderDate;

    @SerializedName("OrderStatus")
    String OrderStatus;


    protected OrdersLabModel(Parcel in) {
        Id = in.readLong();
        Name = in.readString();
        OrderDate = in.readString();
        OrderStatus = in.readString();
    }

    public static final Creator<OrdersLabModel> CREATOR = new Creator<OrdersLabModel>() {
        @Override
        public OrdersLabModel createFromParcel(Parcel in) {
            return new OrdersLabModel(in);
        }

        @Override
        public OrdersLabModel[] newArray(int size) {
            return new OrdersLabModel[size];
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

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(Id);
        dest.writeString(Name);
        dest.writeString(OrderDate);
        dest.writeString(OrderStatus);
    }
}
