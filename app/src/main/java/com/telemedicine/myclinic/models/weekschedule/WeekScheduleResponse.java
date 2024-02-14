
package com.telemedicine.myclinic.models.weekschedule;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeekScheduleResponse implements Parcelable {

    @SerializedName("DayScheduleList")
    @Expose
    private List<DayScheduleList> dayScheduleList = null;
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

    public WeekScheduleResponse() {
    }

    public List<DayScheduleList> getDayScheduleList() {
        return dayScheduleList;
    }

    public void setDayScheduleList(List<DayScheduleList> dayScheduleList) {
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

    protected WeekScheduleResponse(Parcel in) {
        this.dayScheduleList = in.createTypedArrayList(DayScheduleList.CREATOR);
        this.doctorNameEn = in.readString();
        this.doctorNameAr = in.readString();
        this.profilePicUrl = in.readString();
        this.DoctorId = (Long) in.readValue(Long.class.getClassLoader());
        this.SpecialtyId = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<WeekScheduleResponse> CREATOR = new Creator<WeekScheduleResponse>() {
        @Override
        public WeekScheduleResponse createFromParcel(Parcel source) {
            return new WeekScheduleResponse(source);
        }

        @Override
        public WeekScheduleResponse[] newArray(int size) {
            return new WeekScheduleResponse[size];
        }
    };
}
