package com.telemedicine.myclinic.models.onlineConfig;

import com.google.gson.annotations.SerializedName;

public class OnlineConfigDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    public OnlineConfigDTO(String securityToken) {
        SecurityToken = securityToken;
    }
}
