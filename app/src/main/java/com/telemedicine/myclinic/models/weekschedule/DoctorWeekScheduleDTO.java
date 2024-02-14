package com.telemedicine.myclinic.models.weekschedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorWeekScheduleDTO {

    @SerializedName("DoctorId")
    @Expose
    public String doctorId;
    @SerializedName("SecurityToken")
    @Expose
    public String securityToken;
    @SerializedName("Date")
    @Expose
    public String date;
    @SerializedName("SpecialtyId")
    @Expose
    public String specialtyId;
    @SerializedName("IsTelemedicine")
    @Expose
    public boolean IsTelemedicine;

    @SerializedName("CompanyId")
    public String companyId;

    /**
     * No args constructor for use in serialization
     */
    public DoctorWeekScheduleDTO() {
    }

    /**
     * @param securityToken
     * @param date
     * @param doctorId
     * @param specialtyId
     */
    public DoctorWeekScheduleDTO(String doctorId, String securityToken, String date, String specialtyId, boolean IsTelemedicine,String companyId) {
        super();
        this.doctorId = doctorId;
        this.securityToken = securityToken;
        this.date = date;
        this.specialtyId = specialtyId;
        this.IsTelemedicine = IsTelemedicine;
        this.companyId = companyId;
    }

}