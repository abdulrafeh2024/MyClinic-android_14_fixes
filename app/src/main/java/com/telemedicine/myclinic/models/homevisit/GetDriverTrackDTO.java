package com.telemedicine.myclinic.models.homevisit;

import com.google.gson.annotations.SerializedName;

public class GetDriverTrackDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("OrderId")
    String OrderId;

    public GetDriverTrackDTO(String securityToken, String orderId) {
        SecurityToken = securityToken;
        OrderId = orderId;
    }
}