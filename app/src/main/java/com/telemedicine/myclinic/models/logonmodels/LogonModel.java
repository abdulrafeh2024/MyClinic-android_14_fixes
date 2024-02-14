package com.telemedicine.myclinic.models.logonmodels;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.patientMiniModels.PatientsMiniModel;

import java.util.ArrayList;

public class LogonModel extends BaseEntity {


    @SerializedName("SecurityToken")
    SecurityToken securityToken;

    @SerializedName("TMUsername")
    String TMUsername;

    @SerializedName("TMPassword")
    String TMPassword;

    @SerializedName("ORegId")
    String ORegId;

    @SerializedName("email")
    String email;

    @SerializedName("ExistingPatients")
    ArrayList<PatientsMiniModel> patientsMiniModels;

    public SecurityToken getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(SecurityToken securityToken) {
        this.securityToken = securityToken;
    }

    public String getTMUsername() {
        return TMUsername;
    }

    public void setTMUsername(String TMUsername) {
        this.TMUsername = TMUsername;
    }

    public String getTMPassword() {
        return TMPassword;
    }

    public void setTMPassword(String TMPassword) {
        this.TMPassword = TMPassword;
    }

    public String getORegId() {
        return ORegId;
    }

    public void setORegId(String ORegId) {
        this.ORegId = ORegId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<PatientsMiniModel> getPatientsMiniModels() {
        return patientsMiniModels;
    }

    public void setPatientsMiniModels(ArrayList<PatientsMiniModel> patientsMiniModels) {
        this.patientsMiniModels = patientsMiniModels;
    }
}
