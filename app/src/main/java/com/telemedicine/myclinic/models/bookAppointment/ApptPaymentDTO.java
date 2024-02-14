package com.telemedicine.myclinic.models.bookAppointment;

import com.google.gson.annotations.SerializedName;

public class ApptPaymentDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("ApptId")
    long ApptId;

    @SerializedName("CompanyId")
    String companyId;

    public ApptPaymentDTO(String securityToken, long apptId,String companyId) {
        SecurityToken = securityToken;
        ApptId = apptId;
        this.companyId = companyId;
    }
}
