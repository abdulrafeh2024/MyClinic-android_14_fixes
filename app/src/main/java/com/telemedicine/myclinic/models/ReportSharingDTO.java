package com.telemedicine.myclinic.models;

import com.google.gson.annotations.SerializedName;

public class ReportSharingDTO {
    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("EmailId")
    String EmailId;

    @SerializedName("OregId")
    String OregId;

    @SerializedName("OrderId")
    String OrderId;

    public ReportSharingDTO(String securityToken, String  emailId,String oregId, String orderId) {
        SecurityToken = securityToken;
        EmailId = emailId;
        OregId = oregId;
        OrderId = orderId;

    }
}
