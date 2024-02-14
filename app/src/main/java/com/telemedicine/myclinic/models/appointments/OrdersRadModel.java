package com.telemedicine.myclinic.models.appointments;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class OrdersRadModel implements Parcelable {

    @SerializedName("Id")
    long Id;

    @SerializedName("Name")
    String Name;

    @SerializedName("OrderDate")
    String OrderDate;

    @SerializedName("OrderNumber")
    String OrderNumber;

    @SerializedName("OrderStatus")
    long OrderStatus;

    @SerializedName("ApptId")
    long ApptId;

    @SerializedName("DeviceId")
    long DeviceId;

    @SerializedName("DeviceName")
    String DeviceName;

    @SerializedName("ItemCode")
    String ItemCode;

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

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public long getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(long orderStatus) {
        OrderStatus = orderStatus;
    }

    public long getApptId() {
        return ApptId;
    }

    public void setApptId(long apptId) {
        ApptId = apptId;
    }

    public long getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(long deviceId) {
        DeviceId = deviceId;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    protected OrdersRadModel(Parcel in) {
        Id = in.readLong();
        Name = in.readString();
        OrderDate = in.readString();
        OrderNumber = in.readString();
        OrderStatus = in.readLong();
        ApptId = in.readLong();
        DeviceId = in.readLong();
        DeviceName = in.readString();
        ItemCode = in.readString();
    }

    public static final Creator<OrdersRadModel> CREATOR = new Creator<OrdersRadModel>() {
        @Override
        public OrdersRadModel createFromParcel(Parcel in) {
            return new OrdersRadModel(in);
        }

        @Override
        public OrdersRadModel[] newArray(int size) {
            return new OrdersRadModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(Id);
        dest.writeString(Name);
        dest.writeString(OrderDate);
        dest.writeString(OrderNumber);
        dest.writeLong(OrderStatus);
        dest.writeLong(ApptId);
        dest.writeLong(DeviceId);
        dest.writeString(DeviceName);
        dest.writeString(ItemCode);
    }

    public OrdersRadModel() {

    }

}
