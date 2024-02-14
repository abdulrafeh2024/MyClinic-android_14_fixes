package com.telemedicine.myclinic.models.homevisit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverTrack implements Parcelable {

    @SerializedName("DriverId")
    @Expose
    private int DriverId;

    @SerializedName("OrderId")
    @Expose
    private int OrderId;
    @SerializedName("DriverLat")
    @Expose
    private double DriverLat;
    @SerializedName("DriverLong")
    @Expose
    private double DriverLong;
    @SerializedName("DestinationLat")
    @Expose
    private double DestinationLat;
    @SerializedName("DestinationLong")
    @Expose
    private double DestinationLong;
    @SerializedName("CreateById")
    @Expose
    private int CreateById;
    @SerializedName("UpdateById")
    @Expose
    private int UpdateById;
    @SerializedName("CreateStamp")
    @Expose
    private String CreateStamp;
    @SerializedName("UpdateStamp")
    @Expose
    private String UpdateStamp;

    protected DriverTrack(Parcel in) {
        DriverId = in.readInt();
        OrderId = in.readInt();
        DriverLat = in.readDouble();
        DriverLong = in.readDouble();
        DestinationLat = in.readDouble();
        DestinationLong = in.readDouble();
        CreateById = in.readInt();
        UpdateById = in.readInt();
        CreateStamp = in.readString();
        UpdateStamp = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(DriverId);
        dest.writeInt(OrderId);
        dest.writeDouble(DriverLat);
        dest.writeDouble(DriverLong);
        dest.writeDouble(DestinationLat);
        dest.writeDouble(DestinationLong);
        dest.writeInt(CreateById);
        dest.writeInt(UpdateById);
        dest.writeString(CreateStamp);
        dest.writeString(UpdateStamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DriverTrack> CREATOR = new Creator<DriverTrack>() {
        @Override
        public DriverTrack createFromParcel(Parcel in) {
            return new DriverTrack(in);
        }

        @Override
        public DriverTrack[] newArray(int size) {
            return new DriverTrack[size];
        }
    };

    public int getDriverId() {
        return DriverId;
    }

    public void setDriverId(int driverId) {
        DriverId = driverId;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public double getDriverLat() {
        return DriverLat;
    }

    public void setDriverLat(double driverLat) {
        DriverLat = driverLat;
    }

    public double getDriverLong() {
        return DriverLong;
    }

    public void setDriverLong(double driverLong) {
        DriverLong = driverLong;
    }

    public double getDestinationLat() {
        return DestinationLat;
    }

    public void setDestinationLat(double destinationLat) {
        DestinationLat = destinationLat;
    }

    public double getDestinationLong() {
        return DestinationLong;
    }

    public void setDestinationLong(double destinationLong) {
        DestinationLong = destinationLong;
    }

    public int getCreateById() {
        return CreateById;
    }

    public void setCreateById(int createById) {
        CreateById = createById;
    }

    public int getUpdateById() {
        return UpdateById;
    }

    public void setUpdateById(int updateById) {
        UpdateById = updateById;
    }

    public String getCreateStamp() {
        return CreateStamp;
    }

    public void setCreateStamp(String createStamp) {
        CreateStamp = createStamp;
    }

    public String getUpdateStamp() {
        return UpdateStamp;
    }

    public void setUpdateStamp(String updateStamp) {
        UpdateStamp = updateStamp;
    }


}
