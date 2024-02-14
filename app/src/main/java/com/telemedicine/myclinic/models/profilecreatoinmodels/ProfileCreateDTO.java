package com.telemedicine.myclinic.models.profilecreatoinmodels;

import com.google.gson.annotations.SerializedName;

public class ProfileCreateDTO {

    @SerializedName("ORegId")
    String ORegId;

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientRegistration")
    PatientRegistrationDTO PatientRegistration;

    public ProfileCreateDTO(String ORegId, String securityToken, PatientRegistrationDTO patientRegistration) {
        this.ORegId = ORegId;
        SecurityToken = securityToken;
        PatientRegistration = patientRegistration;
    }
}
