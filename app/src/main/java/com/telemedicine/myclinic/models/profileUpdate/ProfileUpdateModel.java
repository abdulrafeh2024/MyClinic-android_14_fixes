package com.telemedicine.myclinic.models.profileUpdate;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

public class ProfileUpdateModel extends BaseEntity {

    @SerializedName("PatientRegistration")
    PatientRegistrationUpdate patientRegistrationUpdate;

    public PatientRegistrationUpdate getPatientRegistrationUpdate() {
        return patientRegistrationUpdate;
    }

    public void setPatientRegistrationUpdate(PatientRegistrationUpdate patientRegistrationUpdate) {
        this.patientRegistrationUpdate = patientRegistrationUpdate;
    }
}
