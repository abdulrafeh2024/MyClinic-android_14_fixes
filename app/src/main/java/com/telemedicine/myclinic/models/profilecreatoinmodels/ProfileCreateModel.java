package com.telemedicine.myclinic.models.profilecreatoinmodels;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

public class ProfileCreateModel extends BaseEntity {

    @SerializedName("InsuranceId")
    String InsuranceId;

    @SerializedName("MRN")
    String MRN;

    @SerializedName("PatientId")
    long PatientId;

    public String getInsuranceId() {
        return InsuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        InsuranceId = insuranceId;
    }

    public String getMRN() {
        return MRN;
    }

    public void setMRN(String MRN) {
        this.MRN = MRN;
    }

    public long getPatientId() {
        return PatientId;
    }

    public void setPatientId(long patientId) {
        PatientId = patientId;
    }
}
