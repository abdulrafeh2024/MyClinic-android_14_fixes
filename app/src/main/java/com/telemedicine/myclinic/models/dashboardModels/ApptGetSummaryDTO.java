package com.telemedicine.myclinic.models.dashboardModels;

import com.google.gson.annotations.SerializedName;

public class ApptGetSummaryDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientId")
    String PatientId;

    @SerializedName("CompanyId")
    String companyId;


    public ApptGetSummaryDTO(String securityToken, String patientId,String companyId) {
        SecurityToken = securityToken;
        PatientId = patientId;
        this.companyId = companyId;
    }
}
