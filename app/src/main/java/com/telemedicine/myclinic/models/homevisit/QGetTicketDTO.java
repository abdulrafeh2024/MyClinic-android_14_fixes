package com.telemedicine.myclinic.models.homevisit;

import com.google.gson.annotations.SerializedName;

public class QGetTicketDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("QSiteType")
    String QSiteType;

    @SerializedName("ApptId")
    String ApptId;

    @SerializedName("CompanyId")
    String companyId;



    public QGetTicketDTO(String securityToken, String QSiteType, String apptId,String companyId) {
        SecurityToken = securityToken;
        this.QSiteType = QSiteType;
        ApptId = apptId;
        this.companyId = companyId;
    }
}
