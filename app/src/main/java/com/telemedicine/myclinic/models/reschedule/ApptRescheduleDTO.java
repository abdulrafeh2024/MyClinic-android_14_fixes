package com.telemedicine.myclinic.models.reschedule;

import com.google.gson.annotations.SerializedName;

public class ApptRescheduleDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("ApptId")
    long ApptId;

    @SerializedName("DoctorId")
    long DoctorId;

    @SerializedName("ApptDate")
    String ApptDate;

    @SerializedName("IsSecondary")
    boolean IsSecondary;

    @SerializedName("CompanyId")
    String companyId;


    public ApptRescheduleDTO(String securityToken, long apptId, long doctorId, String apptDate, boolean isSecondary,String companyId) {
        SecurityToken = securityToken;
        ApptId = apptId;
        DoctorId = doctorId;
        ApptDate = apptDate;
        IsSecondary = isSecondary;
        this.companyId = companyId;
    }
}
