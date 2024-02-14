package com.telemedicine.myclinic.models.cancelAppointment;

import com.google.gson.annotations.SerializedName;

public class ApptCancelDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("ApptId")
    String ApptId;

    @SerializedName("IsSecondary")
    boolean IsSecondary;

    @SerializedName("CompanyId")
    String companyId;

    public ApptCancelDTO(String securityToken, String apptId, boolean isSecondary,String companyId) {
        SecurityToken = securityToken;
        ApptId = apptId;
        IsSecondary = isSecondary;
        this.companyId = companyId;
    }
}
