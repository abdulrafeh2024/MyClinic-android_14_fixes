package com.telemedicine.myclinic.models.payStage;

import com.google.gson.annotations.SerializedName;

public class TeleInstantPayStageOneDTO {

    @SerializedName("Amount")
    Object Amount;

    @SerializedName("CustomerEmail")
    String CustomerEmail;

    @SerializedName("PatientId")
    String PatientId;

    public TeleInstantPayStageOneDTO(Object amount, String customerEmail, String patientId) {
        Amount = amount;
        CustomerEmail = customerEmail;
        PatientId = patientId;
    }
}
