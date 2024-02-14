package com.telemedicine.myclinic.models.profileInsuranceValidate;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

public class ProfileInsuranceValidateModel extends BaseEntity {

    @SerializedName("Insurance")
    InsuranceModel insuranceModel;

    public InsuranceModel getInsuranceModel() {
        return insuranceModel;
    }

    public void setInsuranceModel(InsuranceModel insuranceModel) {
        this.insuranceModel = insuranceModel;
    }
}
