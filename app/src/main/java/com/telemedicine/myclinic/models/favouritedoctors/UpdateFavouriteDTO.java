package com.telemedicine.myclinic.models.favouritedoctors;

import com.google.gson.annotations.SerializedName;

public class UpdateFavouriteDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientId")
    String PatientId;

    @SerializedName("DoctorId")
    String DoctorId;

    @SerializedName("IsFavorite")
    Boolean IsFavorite;

    public UpdateFavouriteDTO(String securityToken, String patientId, String doctorId, Boolean isFavourite) {
        this.SecurityToken = securityToken;
        this.PatientId = patientId;
        this.DoctorId = doctorId;
        this.IsFavorite  = isFavourite;
    }


}
