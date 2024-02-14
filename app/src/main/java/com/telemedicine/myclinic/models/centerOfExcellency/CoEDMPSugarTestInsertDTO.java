package com.telemedicine.myclinic.models.centerOfExcellency;

import com.google.gson.annotations.SerializedName;

public class CoEDMPSugarTestInsertDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientId")
    String PatientId;

    @SerializedName("Result")
    int Result;

    public CoEDMPSugarTestInsertDTO(String securityToken, String patientId, int result) {
        SecurityToken = securityToken;
        PatientId = patientId;
        Result = result;
    }
}
