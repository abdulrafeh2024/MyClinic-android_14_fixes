package com.telemedicine.myclinic.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorWeekScheduleTeleDTO {

    @SerializedName("DoctorId")
    @Expose
    public String doctorId;
    @SerializedName("SecurityToken")
    @Expose
    public String securityToken;
    @SerializedName("StartDate")
    @Expose
    public String startDate;

    @SerializedName("EndDate")
    @Expose
    public String endDate;

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
    public DoctorWeekScheduleTeleDTO() {
    }

    /**
     * @param securityToken
     * @param startDate
     * @param doctorId
     * @param specialtyId
     */
    public DoctorWeekScheduleTeleDTO(String doctorId, String securityToken, String startDate, String endDate,
                                     String specialtyId, boolean IsTelemedicine,String companyId) {
        super();
        this.doctorId = doctorId;
        this.securityToken = securityToken;
        this.startDate = startDate;
        this.endDate = endDate;
        this.specialtyId = specialtyId;
        this.IsTelemedicine = IsTelemedicine;
        this.companyId = companyId;
    }

}