package com.telemedicine.myclinic.models.appointments;

import com.google.gson.annotations.SerializedName;

public class AppointmentsDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientId")
    String PatientId;

    @SerializedName("ApptSummaryType")
    String ApptSummaryType;

    public AppointmentsDTO(String securityToken, String patientId, String apptSummaryType) {
        SecurityToken = securityToken;
        PatientId = patientId;
        ApptSummaryType = apptSummaryType;
    }
}
