package com.telemedicine.myclinic.models.topupfee;

import com.google.gson.annotations.SerializedName;

public class TopUpFeeDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("Id")
    String Id;

    public TopUpFeeDTO(String securityToken, String doctorId) {
        SecurityToken = securityToken;
        Id = doctorId;
    }

}
