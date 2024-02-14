package com.telemedicine.myclinic.models;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.patientMiniModels.PatientsMiniModel;

import java.util.ArrayList;

public class LabReportMainModel extends BaseEntity{

    @SerializedName("PatientReports")
    ArrayList<LabReportsResponseModel> patientReports;

    public ArrayList<LabReportsResponseModel> getPatientReports() {
        return patientReports;
    }

    public void setPatientReports(ArrayList<LabReportsResponseModel> patientReports) {
        this.patientReports = patientReports;
    }
}
