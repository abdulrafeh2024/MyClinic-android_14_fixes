package com.telemedicine.myclinic.models.bookAppointment;

import com.google.gson.annotations.SerializedName;

public class ApptGetSlotsDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientId")
    String PatientId;

    @SerializedName("SpecialtyId")
    String SpecialtyId;

    @SerializedName("DoctorId")
    String DoctorId;

    @SerializedName("TargetDate")
    String TargetDate;

    @SerializedName("IsTelemedicine")
    boolean IsTelemedicine;

    @SerializedName("IsAnonymous")
    boolean IsAnonymous;

    @SerializedName("CompanyId")
    String companyId;

    public ApptGetSlotsDTO(String securityToken, String patientId, String specialtyId, String doctorId, String targetDate, boolean isTelemedicine,String companyId,
                           boolean IsAnonymous) {
        SecurityToken = securityToken;
        PatientId = patientId;
        SpecialtyId = specialtyId;
        DoctorId = doctorId;
        TargetDate = targetDate;
        IsTelemedicine = isTelemedicine;
        this.companyId = companyId;
        this.IsAnonymous = IsAnonymous;
    }
}
