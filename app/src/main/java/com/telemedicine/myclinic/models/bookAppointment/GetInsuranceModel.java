package com.telemedicine.myclinic.models.bookAppointment;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.patientMiniModels.InsuranceCardModel;

import java.util.ArrayList;

public class GetInsuranceModel extends BaseEntity {

    @SerializedName("InsuranceCard")
    ArrayList<InsuranceCardModel> insuranceCardModels;

    public ArrayList<InsuranceCardModel> getInsuranceCardModels() {
        return insuranceCardModels;
    }

    public void setInsuranceCardModels(ArrayList<InsuranceCardModel> insuranceCardModels) {
        this.insuranceCardModels = insuranceCardModels;
    }
}
