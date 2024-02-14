package com.telemedicine.myclinic.models;

import com.google.gson.annotations.SerializedName;

public class LabReportUrlResponseModel extends BaseEntity {

    @SerializedName("ReportUrl")
    String ReportUrl;

    public String getReportUrl() {
        return ReportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.ReportUrl = reportUrl;
    }
}
