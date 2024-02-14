package com.telemedicine.myclinic.models;

import com.google.gson.annotations.SerializedName;

public class NotificationDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientRecId")
    String PatientRecId;

    public NotificationDTO(String securityToken, String patientRecId) {
        SecurityToken = securityToken;
        PatientRecId  = patientRecId;
    }
}

