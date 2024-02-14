package com.telemedicine.myclinic.models.weekscheduletemp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.weekschedule.DayScheduleList;

import java.util.List;

public class WeekScheduleResponseTemp implements Parcelable {

    @SerializedName("DayScheduleList")
    @Expose
    private List<DayScheduleListTemp> dayScheduleList = null;
    @SerializedName("DoctorNameEn")
    @Expose
    private String doctorNameEn;
    @SerializedName("DoctorNameAr")
    @Expose
    private String doctorNameAr;
    @SerializedName("ProfilePicUrl")
    @Expose
    private String profilePicUrl;
    @SerializedName("DoctorId")
    @Expose
    private Long DoctorId;
    @SerializedName("SpecialtyId")
    @Expose
    private Long SpecialtyId;

    public Long getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(Long doctorId) {
        DoctorId = doctorId;
    }

    public Long getSpecialtyId() {
        return SpecialtyId;
    }

    public void setSpecialtyId(Long specialtyId) {
        SpecialtyId = specialtyId;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public WeekScheduleResponseTemp() {
    }

    public List<DayScheduleListTemp> getDayScheduleList() {
        return dayScheduleList;
    }

    public void setDayScheduleList(List<DayScheduleListTemp> dayScheduleList) {
        this.dayScheduleList = dayScheduleList;
    }

    public String getDoctorNameEn() {
        return doctorNameEn;
    }

    public void setDoctorNameEn(String doctorNameEn) {
        this.doctorNameEn = doctorNameEn;
    }

    public String getDoctorNameAr() {
        return doctorNameAr;
    }

    public void setDoctorNameAr(String doctorNameAr) {
        this.doctorNameAr = doctorNameAr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.dayScheduleList);
        dest.writeString(this.doctorNameEn);
        dest.writeString(this.doctorNameAr);
        dest.writeString(this.profilePicUrl);
        dest.writeValue(this.DoctorId);
        dest.writeValue(this.SpecialtyId);
    }

    protected WeekScheduleResponseTemp(Parcel in) {
        this.dayScheduleList = in.createTypedArrayList(DayScheduleListTemp.CREATOR);
        this.doctorNameEn = in.readString();
        this.doctorNameAr = in.readString();
        this.profilePicUrl = in.readString();
        this.DoctorId = (Long) in.readValue(Long.class.getClassLoader());
        this.SpecialtyId = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<com.telemedicine.myclinic.models.weekscheduletemp.WeekScheduleResponseTemp> CREATOR = new Creator<com.telemedicine.myclinic.models.weekscheduletemp.WeekScheduleResponseTemp>() {
        @Override
        public com.telemedicine.myclinic.models.weekscheduletemp.WeekScheduleResponseTemp createFromParcel(Parcel source) {
            return new com.telemedicine.myclinic.models.weekscheduletemp.WeekScheduleResponseTemp(source);
        }

        @Override
        public com.telemedicine.myclinic.models.weekscheduletemp.WeekScheduleResponseTemp[] newArray(int size) {
            return new com.telemedicine.myclinic.models.weekscheduletemp.WeekScheduleResponseTemp[size];
        }
    };
}