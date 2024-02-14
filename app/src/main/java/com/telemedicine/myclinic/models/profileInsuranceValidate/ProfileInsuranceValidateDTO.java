package com.telemedicine.myclinic.models.profileInsuranceValidate;

import com.google.gson.annotations.SerializedName;

public class ProfileInsuranceValidateDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("InsuranceCarrierType")
    String InsuranceCarrierType;

    @SerializedName("MembershipNo")
    String MembershipNo;

    @SerializedName("PolicyNo")
    String PolicyNo;

    @SerializedName("CarrierId")
    long CarrierId;

    @SerializedName("SpecialtyId")
    long SpecialtyId;

    public ProfileInsuranceValidateDTO() {

    }

    public void setCarrierId(long carrierId) {
        CarrierId = carrierId;
    }

    public ProfileInsuranceValidateDTO(String securityToken, String insuranceCarrierType, String membershipNo, String policyNo) {
        SecurityToken = securityToken;
        InsuranceCarrierType = insuranceCarrierType;
        MembershipNo = membershipNo;
        PolicyNo = policyNo;
    }

    public ProfileInsuranceValidateDTO(String securityToken, String insuranceCarrierType, String membershipNo, String policyNo, long specialtyId) {
        SecurityToken = securityToken;
        InsuranceCarrierType = insuranceCarrierType;
        MembershipNo = membershipNo;
        PolicyNo = policyNo;
        SpecialtyId = specialtyId;
    }
}
