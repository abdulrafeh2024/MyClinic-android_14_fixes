package com.telemedicine.myclinic.models.profileUpdate;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.profilecreatoinmodels.PatientRegistrationDTO;

public class ProfileUpdateDTO {

    @SerializedName("ORegId")
    String ORegId;

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientRegistration")
    UpdatePatientRegistrationDTO PatientRegistration;

    public ProfileUpdateDTO(String ORegId, String securityToken, UpdatePatientRegistrationDTO patientRegistration) {
        this.ORegId = ORegId;
        SecurityToken = securityToken;
        PatientRegistration = patientRegistration;
    }
}
