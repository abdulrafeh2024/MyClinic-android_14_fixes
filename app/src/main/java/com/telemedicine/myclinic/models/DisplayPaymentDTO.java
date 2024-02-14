package com.telemedicine.myclinic.models;

import com.google.gson.annotations.SerializedName;

public class DisplayPaymentDTO {


    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientId")
    String PatientId;

    @SerializedName("CompanyId")
    String companyId;

    public DisplayPaymentDTO(String securityToken, String patientId,String companyId) {
        SecurityToken = securityToken;
        PatientId = patientId;
        this.companyId = companyId;
    }
}
