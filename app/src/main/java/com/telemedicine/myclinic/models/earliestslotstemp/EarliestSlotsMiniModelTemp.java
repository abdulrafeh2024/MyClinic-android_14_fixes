package com.telemedicine.myclinic.models.earliestslotstemp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.weekschedule.DayScheduleList;
import com.telemedicine.myclinic.models.weekscheduletemp.DayScheduleListTemp;

import java.util.List;

public class EarliestSlotsMiniModelTemp implements Parcelable {

    @SerializedName("WeekDates")
    @Expose
    private List<DayScheduleListTemp> dayScheduleList = null;
    @SerializedName("DoctorName")
    @Expose
    private String doctorName;
    @SerializedName("SlotDate")
    @Expose
    private String slotDate;
    @SerializedName("SlotTime")
    @Expose
    private String slotTime;
    @SerializedName("DoctorId")
    @Expose
    private Long DoctorId;
    @SerializedName("Company")
    @Expose
    private String company;

    @SerializedName("DoctorProfileUrl")
    @Expose
    private String DoctorProfileUrl;


    public Long getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(Long doctorId) {
        DoctorId = doctorId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDoctorProfileUrl() {
        return DoctorProfileUrl;
    }

    public void setDoctorProfileUrl(String DoctorProfileUrl) {
        this.DoctorProfileUrl = DoctorProfileUrl;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String profilePicUrl) {
        this.slotTime= slotTime;
    }

    public EarliestSlotsMiniModelTemp() {
    }

    public List<DayScheduleListTemp> getDayScheduleList() {
        return dayScheduleList;
    }

    public void setDayScheduleList(List<DayScheduleListTemp> dayScheduleList) {
        this.dayScheduleList = dayScheduleList;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(String slotDate) {
        this.slotDate = slotDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.dayScheduleList);
        dest.writeString(this.doctorName);
        dest.writeString(this.slotDate);
        dest.writeString(this.slotTime);
        dest.writeValue(this.DoctorId);
        dest.writeValue(this.company);
        dest.writeValue(this.DoctorProfileUrl);
    }

    protected EarliestSlotsMiniModelTemp(Parcel in) {
        this.dayScheduleList = in.createTypedArrayList(DayScheduleListTemp.CREATOR);
        this.doctorName = in.readString();
        this.slotDate = in.readString();
        this.slotTime = in.readString();
        this.DoctorId = (Long) in.readValue(Long.class.getClassLoader());
        this.company = in.readString();
        this.DoctorProfileUrl = in.readString();
    }

    public static final Creator<EarliestSlotsMiniModelTemp> CREATOR = new Creator<EarliestSlotsMiniModelTemp>() {
        @Override
        public EarliestSlotsMiniModelTemp createFromParcel(Parcel source) {
            return new EarliestSlotsMiniModelTemp(source);
        }

        @Override
        public EarliestSlotsMiniModelTemp[] newArray(int size) {
            return new EarliestSlotsMiniModelTemp[size];
        }
    };
}