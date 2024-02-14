package com.telemedicine.myclinic.models;

import com.google.gson.annotations.SerializedName;

public class TeleInstantEnqueueDTO {

    @SerializedName("SecurityToken")
    String securityToken;

    @SerializedName("SpecialtyId")
    long specialtyId;

    @SerializedName("PatientId")
    long patientId;

    public TeleInstantEnqueueDTO(String securityToken, long specialtyId, long patientId) {
        this.securityToken = securityToken;
        this.specialtyId = specialtyId;
        this.patientId = patientId;
    }
}
