package com.telemedicine.myclinic.models.homevisit;

import com.google.gson.annotations.SerializedName;

public class GetOrdersByPatientIdDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientId")
    String PatientId;

    public GetOrdersByPatientIdDTO(String securityToken, String patientId) {
        SecurityToken = securityToken;
        PatientId = patientId;
    }
}
