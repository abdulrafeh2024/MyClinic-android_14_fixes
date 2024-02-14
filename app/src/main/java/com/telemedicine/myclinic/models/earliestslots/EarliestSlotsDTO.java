package com.telemedicine.myclinic.models.earliestslots;

import com.google.gson.annotations.SerializedName;

public class EarliestSlotsDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("SpecialityId")
    String SpecialityId;

    @SerializedName("DoctorId")
    String DoctorId;

    @SerializedName("Date")
    String Date;

    @SerializedName("CompanyId")
    String CompanyId;

    @SerializedName("isTelemedicine")
    boolean isTelemedicine;

    @SerializedName("NextRecordsFrom")
    int NextRecordsFrom;

    public EarliestSlotsDTO(String securityToken,String specialityId, String doctorId, String date, String companyId, int NextRecordsFrom, boolean isTelemedicine) {
        SecurityToken = securityToken;
        this.SpecialityId = specialityId;
        this.NextRecordsFrom = NextRecordsFrom;
        this.DoctorId = doctorId;
        this.Date = date;
        this.CompanyId = companyId;
        this.isTelemedicine =isTelemedicine;
    }
}
