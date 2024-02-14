package com.telemedicine.myclinic.models.homemodels;

import com.google.gson.annotations.SerializedName;

public class QCancelTicketDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("QLineId")
    String QLineId;

    @SerializedName("CompanyId")
    String companyId;

    public QCancelTicketDTO(String securityToken, String QLineId,String companyId) {
        SecurityToken = securityToken;
        this.QLineId = QLineId;
        this.companyId = companyId;
    }
}
