package com.telemedicine.myclinic.models;

import com.google.gson.annotations.SerializedName;

public class UpdateInsuranceDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("ApptId")
    String ApptId;

    @SerializedName("PtInsuranceId")
    long PtInsuranceId;

    @SerializedName("CompanyId")
    String CompanyId;

    public UpdateInsuranceDTO(String securityToken, String apptId,long ptInsuranceId,String companyId) {
        SecurityToken = securityToken;
        ApptId = apptId;
        PtInsuranceId = ptInsuranceId;
        CompanyId = companyId;
    }
}