package com.telemedicine.myclinic.models;

import com.google.gson.annotations.SerializedName;

public class AfgaReportDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("OrderId")
    String OrderId;

    @SerializedName("CompanyId")
    String companyId;

    public AfgaReportDTO(String securityToken, String id,String companyId) {
        SecurityToken = securityToken;
        OrderId = id;
        this.companyId = companyId;
    }
}
