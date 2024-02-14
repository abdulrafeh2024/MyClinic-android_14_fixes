package com.telemedicine.myclinic.models.bookAppointment;

import com.google.gson.annotations.SerializedName;

public class ApptGetTotalOutstandingDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("ApptId")
    long ApptId;

    @SerializedName("IsTelemedicine")
    boolean IsTelemedicine;

    @SerializedName("CompanyId")
    String companyId;
    public ApptGetTotalOutstandingDTO(String securityToken, long apptId, boolean isTelemedicine,String companyId) {
        SecurityToken = securityToken;
        ApptId = apptId;
        IsTelemedicine = isTelemedicine;
        this.companyId = companyId;
    }
}
