package com.telemedicine.myclinic.models;

import com.google.gson.annotations.SerializedName;

public class TeleInstantGetPriceDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("SpecialtyId")
    String SpecialtyId;

    @SerializedName("PatientId")
    String PatientId;

    public TeleInstantGetPriceDTO(String securityToken, String specialtyId, String patientId) {
        SecurityToken = securityToken;
        SpecialtyId = specialtyId;
        PatientId = patientId;
    }
}
