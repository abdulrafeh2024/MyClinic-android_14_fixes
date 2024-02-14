package com.telemedicine.myclinic.activities.multiservices;

import com.google.gson.annotations.SerializedName;

public class ApptGetServicesByIdDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("ApptId")
    String ApptId;

    @SerializedName("CompanyId")
    String CompanyId;

    public ApptGetServicesByIdDTO(String securityToken, String apptId, String companyId) {
        SecurityToken = securityToken;
        ApptId = apptId;
        CompanyId = companyId;
    }
}