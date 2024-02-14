package com.telemedicine.myclinic.models.centerOfExcellency;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

import java.util.ArrayList;

public class CoEDMPLabModel extends BaseEntity {

    @SerializedName("LabResultsList")
    ArrayList<LabResultsList> labResultsList;

    @SerializedName("TargetResult")
    int TargetResult;

    @SerializedName("CurrentResult")
    double CurrentResult;

    @SerializedName("PercentageProgress")
    double PercentageProgress;

    @SerializedName("Weight")
    int Weight;

    @SerializedName("BMI")
    int BMI;

    public ArrayList<LabResultsList> getLabResultsList() {
        return labResultsList;
    }

    public void setLabResultsList(ArrayList<LabResultsList> labResultsList) {
        this.labResultsList = labResultsList;
    }

    public int getTargetResult() {
        return TargetResult;
    }

    public void setTargetResult(int targetResult) {
        TargetResult = targetResult;
    }

    public double getCurrentResult() {
        return CurrentResult;
    }

    public void setCurrentResult(double currentResult) {
        CurrentResult = currentResult;
    }

    public double getPercentageProgress() {
        return PercentageProgress;
    }

    public void setPercentageProgress(double percentageProgress) {
        PercentageProgress = percentageProgress;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public int getBMI() {
        return BMI;
    }

    public void setBMI(int BMI) {
        this.BMI = BMI;
    }
}
