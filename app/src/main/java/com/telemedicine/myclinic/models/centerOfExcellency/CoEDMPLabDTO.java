package com.telemedicine.myclinic.models.centerOfExcellency;

import com.google.gson.annotations.SerializedName;

public class CoEDMPLabDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientId")
    String PatientId;

    public CoEDMPLabDTO(String securityToken, String patientId) {
        SecurityToken = securityToken;
        PatientId = patientId;
    }
}
