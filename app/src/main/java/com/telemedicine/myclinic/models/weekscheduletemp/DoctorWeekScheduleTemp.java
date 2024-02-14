package com.telemedicine.myclinic.models.weekscheduletemp;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.weekschedule.WeekScheduleResponse;

import java.util.List;

public class DoctorWeekScheduleTemp extends BaseEntity {

    @SerializedName("WeekScheduleResponse")
    @Expose
    private List<WeekScheduleResponseTemp> weekScheduleResponse = null;

    public List<WeekScheduleResponseTemp> getWeekScheduleResponse() {
        return weekScheduleResponse;
    }

    public void setWeekScheduleResponse(List<WeekScheduleResponseTemp> weekScheduleResponse) {
        this.weekScheduleResponse = weekScheduleResponse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.weekScheduleResponse);
    }

    public DoctorWeekScheduleTemp() {
    }

    protected DoctorWeekScheduleTemp(Parcel in) {
        super(in);
        this.weekScheduleResponse = in.createTypedArrayList(WeekScheduleResponseTemp.CREATOR);
    }

    public static final Creator<com.telemedicine.myclinic.models.weekscheduletemp.DoctorWeekScheduleTemp> CREATOR = new Creator<com.telemedicine.myclinic.models.weekscheduletemp.DoctorWeekScheduleTemp>() {
        @Override
        public com.telemedicine.myclinic.models.weekscheduletemp.DoctorWeekScheduleTemp createFromParcel(Parcel source) {
            return new com.telemedicine.myclinic.models.weekscheduletemp.DoctorWeekScheduleTemp(source);
        }

        @Override
        public com.telemedicine.myclinic.models.weekscheduletemp.DoctorWeekScheduleTemp[] newArray(int size) {
            return new com.telemedicine.myclinic.models.weekscheduletemp.DoctorWeekScheduleTemp[size];
        }
    };
}
