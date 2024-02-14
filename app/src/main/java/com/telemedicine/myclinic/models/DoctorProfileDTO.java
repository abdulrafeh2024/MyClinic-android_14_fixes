package com.telemedicine.myclinic.models;
import com.google.gson.annotations.SerializedName;

public class DoctorProfileDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("DoctorId")
    long DoctorId;

    public DoctorProfileDTO(String securityToken, long DoctorId) {
        SecurityToken = securityToken;
        this.DoctorId = DoctorId;
    }
}
