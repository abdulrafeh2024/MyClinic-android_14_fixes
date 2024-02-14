package com.telemedicine.myclinic.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ApptValidationForCheckinDTO {


    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("ApptId")
    long ApptId;

    @SerializedName("CompanyId")
    String CompanyId;

    public ApptValidationForCheckinDTO(String securityToken, long apptId, String companyId) {
        SecurityToken = securityToken;
        ApptId = apptId;
        CompanyId = companyId;
    }
}
