package com.telemedicine.myclinic.models.securityChallenge;

import com.google.gson.annotations.SerializedName;

public class SecurityChallengeDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientId")
    long PatientId;

    public SecurityChallengeDTO(String securityToken, long patientId) {
        SecurityToken = securityToken;
        PatientId = patientId;
    }
}
