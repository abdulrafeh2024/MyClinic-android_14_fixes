package com.telemedicine.myclinic.models.boardingPass;

import com.google.gson.annotations.SerializedName;

public class BoardingPassDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("ApptId")
    long ApptId;

    @SerializedName("CompanyId")
    String companyId;

    public BoardingPassDTO(String securityToken, long apptId,String companyId) {
        SecurityToken = securityToken;
        ApptId = apptId;
        this.companyId = companyId;
    }
}
