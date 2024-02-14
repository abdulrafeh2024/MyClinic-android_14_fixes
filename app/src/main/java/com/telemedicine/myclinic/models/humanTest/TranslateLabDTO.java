package com.telemedicine.myclinic.models.humanTest;

import com.google.gson.annotations.SerializedName;

public class TranslateLabDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("Id")
    long Id;

    @SerializedName("CompanyId")
    String companyId;

    public TranslateLabDTO(String securityToken, long id,String companyId) {
        SecurityToken = securityToken;
        Id = id;
        this.companyId = companyId;
    }
}
