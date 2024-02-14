package com.telemedicine.myclinic.models.teleCheckin;

import com.google.gson.annotations.SerializedName;

public class TeleCheckInOutDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("ApptId")
    long ApptId;

    @SerializedName("CheckIn")
    boolean CheckIn;

    @SerializedName("CompanyId")
    String companyId;


    public TeleCheckInOutDTO(String securityToken, long apptId, boolean checkIn,String companyId) {
        SecurityToken = securityToken;
        ApptId = apptId;
        CheckIn = checkIn;
        this.companyId = companyId;
    }
}
