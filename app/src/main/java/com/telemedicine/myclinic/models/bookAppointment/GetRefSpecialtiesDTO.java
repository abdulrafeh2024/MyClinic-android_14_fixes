package com.telemedicine.myclinic.models.bookAppointment;

import com.google.gson.annotations.SerializedName;

public class GetRefSpecialtiesDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("IsTelemedicine")
    boolean IsTelemedicine;

    @SerializedName("CompanyId")
    String companyId;

    public GetRefSpecialtiesDTO(String securityToken, boolean isTelemedicine,String companyId) {
        SecurityToken = securityToken;
        IsTelemedicine = isTelemedicine;
        this.companyId = companyId;
    }
}
