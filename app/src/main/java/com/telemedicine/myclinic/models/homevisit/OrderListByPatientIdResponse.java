package com.telemedicine.myclinic.models.homevisit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.weekscheduletemp.DoctorWeekScheduleTemp;
import com.telemedicine.myclinic.models.weekscheduletemp.WeekScheduleResponseTemp;

import java.util.List;

public class OrderListByPatientIdResponse extends BaseEntity {

    @SerializedName("OrdersList")
    @Expose
    private List<OrdersListResponse> OrdersList = null;

    public List<OrdersListResponse> getOrdersList() {
        return OrdersList;
    }

    public void setOrdersList(List<OrdersListResponse> OrdersList) {
        this.OrdersList = OrdersList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.OrdersList);
    }

    public OrderListByPatientIdResponse() {
    }

    protected OrderListByPatientIdResponse(Parcel in) {
        super(in);
        this.OrdersList = in.createTypedArrayList(OrdersListResponse.CREATOR);
    }

    public static final Creator<com.telemedicine.myclinic.models.homevisit.OrderListByPatientIdResponse> CREATOR = new Creator<com.telemedicine.myclinic.models.homevisit.OrderListByPatientIdResponse>() {
        @Override
        public com.telemedicine.myclinic.models.homevisit.OrderListByPatientIdResponse createFromParcel(Parcel source) {
            return new com.telemedicine.myclinic.models.homevisit.OrderListByPatientIdResponse(source);
        }

        @Override
        public com.telemedicine.myclinic.models.homevisit.OrderListByPatientIdResponse[] newArray(int size) {
            return new com.telemedicine.myclinic.models.homevisit.OrderListByPatientIdResponse[size];
        }
    };
}
