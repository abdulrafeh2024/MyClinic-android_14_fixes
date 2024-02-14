package com.telemedicine.myclinic.models;

import com.google.gson.annotations.SerializedName;

public class ReportsDTO {

    @SerializedName("Id")
    long id;

    public ReportsDTO(long id) {
        this.id = id;
    }
}
