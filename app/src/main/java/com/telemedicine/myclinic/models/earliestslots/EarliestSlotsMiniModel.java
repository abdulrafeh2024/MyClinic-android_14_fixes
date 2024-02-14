package com.telemedicine.myclinic.models.earliestslots;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.weekschedule.DayScheduleList;

import java.util.List;

public class EarliestSlotsMiniModel implements Parcelable {

    @SerializedName("WeekDates")
    @Expose
    private List<DayScheduleList> dayScheduleList = null;
    @SerializedName("DoctorName")
    @Expose
    private String doctorName;
    @SerializedName("DoctorNameAr")
    @Expose
    private String DoctorNameAr;
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

    @SerializedName("SpecialtyId")
    @Expose
    private String SpecialtyId;

    @SerializedName("SpecialtyEn")
    @Expose
    private String SpecialtyEn;

    @SerializedName("SpecialtyAr")
    @Expose
    private String SpecialtyAr;

    @SerializedName("DoctorProfileUrl")
    @Expose
    private String DoctorProfileUrl;

    public String getDoctorProfileUrl() {
        return DoctorProfileUrl;
    }

    public void setDoctorProfileUrl(String DoctorProfileUrl) {
        this.DoctorProfileUrl = DoctorProfileUrl;
    }

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

    public String getSpecialtyId() {
        return SpecialtyId;
    }

    public void setSpecialtyId(String SpecialtyId) {
        this.SpecialtyId = SpecialtyId;
    }

    public String getSpecialtyEn() {
        return SpecialtyEn;
    }

    public void setSpecialtyEn(String SpecialtyEn) {
        this.SpecialtyEn = SpecialtyEn;
    }

    public String getSpecialtyAr() {
        return SpecialtyAr;
    }

    public void setSpecialtyAr(String SpecialtyAr) {
        this.SpecialtyAr = SpecialtyAr;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String profilePicUrl) {
        this.slotTime= slotTime;
    }

    public EarliestSlotsMiniModel() {}

    public List<DayScheduleList> getDayScheduleList() {
        return dayScheduleList;
    }

    public void setDayScheduleList(List<DayScheduleList> dayScheduleList) {
        this.dayScheduleList = dayScheduleList;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorNameAr() {
        return DoctorNameAr;
    }

    public void setDoctorNameAr(String doctorNameAr) {
        this.DoctorNameAr = doctorNameAr;
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
        dest.writeValue(this.SpecialtyAr);
        dest.writeValue(this.SpecialtyEn);
        dest.writeValue(this.SpecialtyId);
        dest.writeValue(this.DoctorProfileUrl);
    }

    protected EarliestSlotsMiniModel(Parcel in) {
        this.dayScheduleList = in.createTypedArrayList(DayScheduleList.CREATOR);
        this.doctorName = in.readString();
        this.slotDate = in.readString();
        this.slotTime = in.readString();
        this.SpecialtyAr = in.readString();
        this.SpecialtyEn = in.readString();
        this.DoctorId = (Long) in.readValue(Long.class.getClassLoader());
        this.company = in.readString();
        this.SpecialtyId = in.readString();
        this.DoctorProfileUrl = in.readString();
    }

    public static final Creator<EarliestSlotsMiniModel> CREATOR = new Creator<EarliestSlotsMiniModel>() {
        @Override
        public EarliestSlotsMiniModel createFromParcel(Parcel source) {
            return new EarliestSlotsMiniModel(source);
        }

        @Override
        public EarliestSlotsMiniModel[] newArray(int size) {
            return new EarliestSlotsMiniModel[size];
        }
    };
}