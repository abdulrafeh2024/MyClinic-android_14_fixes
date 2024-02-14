package com.telemedicine.myclinic.models.bookAppointment;

import com.google.gson.annotations.SerializedName;

public class GetRefBySpecialtyIdDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("SpecialtyId")
    String SpecialtyId;

    @SerializedName("IsTelemedicine")
    boolean IsTelemedicine;

    @SerializedName("CompanyId")
    String companyId;
    public GetRefBySpecialtyIdDTO(String securityToken, String specialtyId, boolean isTelemedicine, String companyId) {
        SecurityToken = securityToken;
        SpecialtyId = specialtyId;
        IsTelemedicine = isTelemedicine;
        this.companyId = companyId;
    }
}
