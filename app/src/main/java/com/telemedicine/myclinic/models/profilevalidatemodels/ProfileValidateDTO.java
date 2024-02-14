package com.telemedicine.myclinic.models.profilevalidatemodels;

import com.google.gson.annotations.SerializedName;

public class ProfileValidateDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("MembershipNo")
    String MembershipNo;

    @SerializedName("NationalId")
    String NationalId;

    public ProfileValidateDTO(String securityToken, String membershipNo, String nationalId) {
        SecurityToken = securityToken;
        MembershipNo = membershipNo;
        NationalId = nationalId;
    }
}
