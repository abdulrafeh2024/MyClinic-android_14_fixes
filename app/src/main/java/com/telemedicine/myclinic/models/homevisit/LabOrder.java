package com.telemedicine.myclinic.models.homevisit;

import com.google.gson.annotations.SerializedName;

public class LabOrder {

    @SerializedName("Id")
    long Id;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
}
