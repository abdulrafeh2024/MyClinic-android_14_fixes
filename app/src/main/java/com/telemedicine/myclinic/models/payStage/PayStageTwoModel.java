package com.telemedicine.myclinic.models.payStage;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

public class PayStageTwoModel extends BaseEntity {

    @SerializedName("Code")
    String Code;

    @SerializedName("Description")
    String Description;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getResultVal() {
        return Code;
    }

    public void setResultVal(String resultVal) {
        Code = resultVal;
    }
}
