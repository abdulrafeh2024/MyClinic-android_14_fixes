package com.telemedicine.myclinic.models.appointments;

import com.google.gson.annotations.SerializedName;

public class ApptDetailDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("ApptId")
    long ApptId;

    @SerializedName("CompanyId")
    String companyId;
    public ApptDetailDTO(String securityToken, long apptId,String companyId) {
        SecurityToken = securityToken;
        ApptId = apptId;
        this.companyId = companyId;
    }
}
