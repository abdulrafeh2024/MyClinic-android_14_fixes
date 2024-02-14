package com.telemedicine.myclinic.models.profilecreatoinmodels;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

import java.util.ArrayList;

public class RefPatientProfileModel extends BaseEntity {

    @SerializedName("RefPatientProfile")
    RefPatientProfile refPatientProfile;

    public RefPatientProfile getRefPatientProfile() {
        return refPatientProfile;
    }

    public void setRefPatientProfile(RefPatientProfile refPatientProfile) {
        this.refPatientProfile = refPatientProfile;
    }
}
