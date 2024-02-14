package com.telemedicine.myclinic.models.weekschedule;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

import java.util.List;

public class DoctorWeekSchedule extends BaseEntity {

    @SerializedName("WeekScheduleResponse")
    @Expose
    private List<WeekScheduleResponse> weekScheduleResponse = null;

    public List<WeekScheduleResponse> getWeekScheduleResponse() {
        return weekScheduleResponse;
    }

    public void setWeekScheduleResponse(List<WeekScheduleResponse> weekScheduleResponse) {
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

    public DoctorWeekSchedule() {
    }

    protected DoctorWeekSchedule(Parcel in) {
        super(in);
        this.weekScheduleResponse = in.createTypedArrayList(WeekScheduleResponse.CREATOR);
    }

    public static final Creator<DoctorWeekSchedule> CREATOR = new Creator<DoctorWeekSchedule>() {
        @Override
        public DoctorWeekSchedule createFromParcel(Parcel source) {
            return new DoctorWeekSchedule(source);
        }

        @Override
        public DoctorWeekSchedule[] newArray(int size) {
            return new DoctorWeekSchedule[size];
        }
    };
}
