package com.telemedicine.myclinic.models.profilecreatoinmodels;

import com.google.gson.annotations.SerializedName;

public class SecurityTokenDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    public SecurityTokenDTO(String securityToken) {
        SecurityToken = securityToken;
    }
}
