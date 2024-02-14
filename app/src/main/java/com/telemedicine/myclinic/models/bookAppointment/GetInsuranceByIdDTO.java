package com.telemedicine.myclinic.models.bookAppointment;

import com.google.gson.annotations.SerializedName;

public class GetInsuranceByIdDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientId")
    String PatientId;

    public GetInsuranceByIdDTO(String securityToken, String patientId) {
        SecurityToken = securityToken;
        PatientId = patientId;
    }
}
