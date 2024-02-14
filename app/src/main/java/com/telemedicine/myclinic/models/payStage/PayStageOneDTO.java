package com.telemedicine.myclinic.models.payStage;

import com.google.gson.annotations.SerializedName;

public class PayStageOneDTO {

    @SerializedName("Amount")
    Object Amount;

    @SerializedName("CustomerEmail")
    String CustomerEmail;

    @SerializedName("ApptId")
    String ApptId;
    @SerializedName("CompanyId")
    String companyId;
    @SerializedName("ORegId")
    Long ORegId;
    public PayStageOneDTO(Object amount, String customerEmail, String apptId,String companyId) {
        Amount = amount;
        CustomerEmail = customerEmail;
        ApptId = apptId;
        this.companyId = companyId;
    }
}
