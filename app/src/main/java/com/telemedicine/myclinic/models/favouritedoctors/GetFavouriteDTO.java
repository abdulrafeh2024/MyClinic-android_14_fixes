package com.telemedicine.myclinic.models.favouritedoctors;

import com.google.gson.annotations.SerializedName;

public class GetFavouriteDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientId")
    String PatientId;

    @SerializedName("DoctorId")
    String DoctorId;

    public GetFavouriteDTO(String securityToken, String patientId, String doctorId) {
        this.SecurityToken = securityToken;
        this.PatientId = patientId;
        this.DoctorId = doctorId;
    }
}
