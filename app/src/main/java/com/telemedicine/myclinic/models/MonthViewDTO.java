package com.telemedicine.myclinic.models;

import com.google.gson.annotations.SerializedName;

public class MonthViewDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("DoctorId")
    long DoctorId;

    @SerializedName("IsTelemedicine")
    boolean IsTelemedicine;

    @SerializedName("Date")
    String Date;

    @SerializedName("Company")
    String Company;

    public MonthViewDTO(String securityToken, long DoctorId, boolean IsTelemedicine,String Date,String Company) {
        SecurityToken = securityToken;
        this.DoctorId = DoctorId;
        this.IsTelemedicine = IsTelemedicine;
        this.Date = Date;
        this.Company = Company;
    }
}
