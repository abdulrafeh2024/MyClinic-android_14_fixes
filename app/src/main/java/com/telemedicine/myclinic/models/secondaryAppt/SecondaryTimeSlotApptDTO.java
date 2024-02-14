package com.telemedicine.myclinic.models.secondaryAppt;

import com.google.gson.annotations.SerializedName;

public class SecondaryTimeSlotApptDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientId")
    String PatientId;

    @SerializedName("ResourceId")
    String ResourceId;

    @SerializedName("TargetDate")
    String TargetDate;

    @SerializedName("ItemCode")
    String ItemCode;

    @SerializedName("CompanyId")
    String companyId;

    public SecondaryTimeSlotApptDTO(String securityToken, String patientId, String resourceId, String targetDate, String itemCode,String companyId) {
        SecurityToken = securityToken;
        PatientId = patientId;
        ResourceId = resourceId;
        TargetDate = targetDate;
        ItemCode = itemCode;
        this.companyId = companyId;
    }
}
