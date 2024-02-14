package com.telemedicine.myclinic.models.homevisit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.weekscheduletemp.DayScheduleListTemp;

import java.util.List;

public class OrdersListResponse  implements Parcelable {
    @SerializedName("Medicine")
    @Expose
    private List<MedicineList> Medicine = null;
    @SerializedName("ApptDate")
    @Expose
    private String apptDate;
    @SerializedName("ApptId")
    @Expose
    private String ApptId;
    @SerializedName("DortorId")
    @Expose
    private int DortorId;
    @SerializedName("DortorName")
    @Expose
    private String DortorName;
    @SerializedName("Lable")
    @Expose
    private String Lable;
    @SerializedName("OrderId")
    @Expose
    private int OrderId;
    @SerializedName("OrderStatus")
    @Expose
    private int OrderStatus;

    protected OrdersListResponse(Parcel in) {
        Medicine = in.createTypedArrayList(MedicineList.CREATOR);
        apptDate = in.readString();
        ApptId = in.readString();
        DortorId = in.readInt();
        DortorName = in.readString();
        Lable = in.readString();
        OrderId = in.readInt();
        OrderStatus = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Medicine);
        dest.writeString(apptDate);
        dest.writeString(ApptId);
        dest.writeInt(DortorId);
        dest.writeString(DortorName);
        dest.writeString(Lable);
        dest.writeInt(OrderId);
        dest.writeInt(OrderStatus);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrdersListResponse> CREATOR = new Creator<OrdersListResponse>() {
        @Override
        public OrdersListResponse createFromParcel(Parcel in) {
            return new OrdersListResponse(in);
        }

        @Override
        public OrdersListResponse[] newArray(int size) {
            return new OrdersListResponse[size];
        }
    };

    public List<MedicineList> getMedicine() {
        return Medicine;
    }

    public void setMedicine(List<MedicineList> medicine) {
        Medicine = medicine;
    }

    public String getApptDate() {
        return apptDate;
    }

    public void setApptDate(String apptDate) {
        this.apptDate = apptDate;
    }

    public String getApptId() {
        return ApptId;
    }

    public void setApptId(String apptId) {
        ApptId = apptId;
    }

    public int getDortorId() {
        return DortorId;
    }

    public void setDortorId(int dortorId) {
        DortorId = dortorId;
    }

    public String getDortorName() {
        return DortorName;
    }

    public void setDortorName(String dortorName) {
        DortorName = dortorName;
    }

    public String getLable() {
        return Lable;
    }

    public void setLable(String lable) {
        Lable = lable;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public int getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        OrderStatus = orderStatus;
    }


}
