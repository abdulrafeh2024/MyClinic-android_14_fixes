package com.telemedicine.myclinic.models.bookAppointment;

import com.google.gson.annotations.SerializedName;

public class ApptCreateDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientId")
    String PatientId;

    @SerializedName("SpecialtyId")
    String SpecialtyId;

    @SerializedName("DoctorId")
    String DoctorId;

    @SerializedName("AppointmentBookType")
    String AppointmentBookType;

    @SerializedName("ApptDate")
    String ApptDate;

    @SerializedName("IsTelemedicine")
    boolean IsTelemedicine;

    @SerializedName("IsInsurance")
    boolean IsInsurance;

    @SerializedName("InsuranceId")
    long InsuranceId;

    @SerializedName("CompanyId")
    String companyId;

    public ApptCreateDTO(String securityToken, String patientId, String specialtyId, String doctorId, String appointmentBookType, String apptDate, boolean isTelemedicine, boolean isInsurance, long insuranceId,String companyId) {
        SecurityToken = securityToken;
        PatientId = patientId;
        SpecialtyId = specialtyId;
        DoctorId = doctorId;
        AppointmentBookType = appointmentBookType;
        ApptDate = apptDate;
        IsTelemedicine = isTelemedicine;
        IsInsurance = isInsurance;
        InsuranceId = insuranceId;
        this.companyId = companyId;
    }
}
