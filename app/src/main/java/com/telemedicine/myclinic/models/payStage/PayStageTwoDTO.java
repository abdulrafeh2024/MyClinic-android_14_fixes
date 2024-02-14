package com.telemedicine.myclinic.models.payStage;

import com.google.gson.annotations.SerializedName;

public class PayStageTwoDTO {

    @SerializedName("resourcePath")
    String resourcePath;
    @SerializedName("CompanyId")
    String companyId;
    public PayStageTwoDTO(String resourcePath,String companyId) {
        this.resourcePath = resourcePath;
        this.companyId = companyId;
    }
}
