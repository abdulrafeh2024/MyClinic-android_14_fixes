package com.telemedicine.myclinic.models.earliestslots;

import com.google.gson.annotations.SerializedName;

public class UpdateUserStatusDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("mobile")
    String mobile;

    public UpdateUserStatusDTO(String securityToken, String mobileNumber) {
        SecurityToken = securityToken;
        this.mobile = mobileNumber;
    }
}
