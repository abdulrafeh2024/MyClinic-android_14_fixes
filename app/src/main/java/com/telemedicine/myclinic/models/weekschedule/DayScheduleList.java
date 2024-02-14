
package com.telemedicine.myclinic.models.weekschedule;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DayScheduleList implements Parcelable
{

    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("DayEn")
    @Expose
    private String dayEn;
    @SerializedName("DayAr")
    @Expose
    private String dayAr;
    @SerializedName("DoctorNameEn")
    @Expose
    private String doctorNameEn;
    @SerializedName("DoctorNameAr")
    @Expose
    private String doctorNameAr;
    @SerializedName("DoctorId")
    @Expose
    private Long doctorId;
    @SerializedName("IsAvailable")
    @Expose
    private Boolean isAvailable ;
    public final static Creator<DayScheduleList> CREATOR = new Creator<DayScheduleList>() {
      //  private Integer doctorId;

        @SuppressWarnings({
            "unchecked"
        })
        public DayScheduleList createFromParcel(Parcel in) {
            return new DayScheduleList(in);
        }

        public DayScheduleList[] newArray(int size) {
            return (new DayScheduleList[size]);
        }

    }
    ;

    protected DayScheduleList(Parcel in) {
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.dayEn = ((String) in.readValue((String.class.getClassLoader())));
        this.dayAr = ((String) in.readValue((String.class.getClassLoader())));
        this.doctorNameEn = ((String) in.readValue((String.class.getClassLoader())));
        this.doctorNameAr = ((String) in.readValue((String.class.getClassLoader())));
        this.doctorId = (Long) in.readValue((Integer.class.getClassLoader()));
        this.isAvailable = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public DayScheduleList(String date, String drAr, String drEn, String dayAr, String dayEn, long drId, Boolean IsAvailable) {
        this.date = date;
        this.dayAr = dayAr;
        this.dayEn = dayEn;
        this.doctorNameAr = drAr;
        this.doctorNameEn = drEn;
        this.doctorId = drId;
        this.isAvailable =IsAvailable;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayEn() {
        return dayEn;
    }

    public void setDayEn(String dayEn) {
        this.dayEn = dayEn;
    }

    public String getDayAr() {
        return dayAr;
    }

    public void setDayAr(String dayAr) {
        this.dayAr = dayAr;
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

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(date);
        dest.writeValue(dayEn);
        dest.writeValue(dayAr);
        dest.writeValue(doctorNameEn);
        dest.writeValue(doctorNameAr);
        dest.writeValue(doctorId);
        dest.writeValue(isAvailable);
    }

    public int describeContents() {
        return  0;
    }

}
