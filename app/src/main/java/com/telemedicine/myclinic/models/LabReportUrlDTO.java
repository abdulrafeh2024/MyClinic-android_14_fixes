package com.telemedicine.myclinic.models;

import com.google.gson.annotations.SerializedName;

public class LabReportUrlDTO {

    @SerializedName("ReportUrl")
    String ReportUrl;

    public LabReportUrlDTO(String ReportUrl) {
        this.ReportUrl = ReportUrl;
    }
}
