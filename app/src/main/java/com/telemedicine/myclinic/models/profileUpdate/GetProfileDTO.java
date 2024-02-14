package com.telemedicine.myclinic.models.profileUpdate;

import com.google.gson.annotations.SerializedName;

public class GetProfileDTO {

    @SerializedName("PatientId")
    String PatientId;

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("ORegId")
    String ORegId;

    public GetProfileDTO(String patientId, String securityToken, String ORegId) {
        PatientId = patientId;
        SecurityToken = securityToken;
        this.ORegId = ORegId;
    }
}
