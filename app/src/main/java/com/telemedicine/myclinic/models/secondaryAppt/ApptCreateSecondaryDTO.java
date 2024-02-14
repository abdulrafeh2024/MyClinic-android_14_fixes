package com.telemedicine.myclinic.models.secondaryAppt;

import com.google.gson.annotations.SerializedName;

public class ApptCreateSecondaryDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientId")
    String PatientId;

    @SerializedName("OrderId")
    String OrderId;

    @SerializedName("ApptDate")
    String ApptDate;

    @SerializedName("InsuranceId")
    long InsuranceId;

    @SerializedName("IsInsurance")
    boolean IsInsurance;

    @SerializedName("CompanyId")
    String companyId;

    public ApptCreateSecondaryDTO(String securityToken, String patientId, String orderId, String apptDate, long insuranceId, boolean isInsurance,String companyId) {
        SecurityToken = securityToken;
        PatientId = patientId;
        OrderId = orderId;
        ApptDate = apptDate;
        InsuranceId = insuranceId;
        IsInsurance = isInsurance;
        this.companyId = companyId;
    }
}
