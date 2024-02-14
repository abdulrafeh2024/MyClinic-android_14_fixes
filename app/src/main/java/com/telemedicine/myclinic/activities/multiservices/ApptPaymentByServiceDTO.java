package com.telemedicine.myclinic.activities.multiservices;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.ApptPayServicesDTO;

import java.util.ArrayList;

public class ApptPaymentByServiceDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("ApptId")
    long ApptId;

    @SerializedName("CompanyId")
    String companyId;

    @SerializedName("ApptPayServices")
    ArrayList<ApptPayServicesDTO> ApptPayServices;

    public ApptPaymentByServiceDTO(String securityToken, long apptId,String companyId,
                                   ArrayList<ApptPayServicesDTO> ApptPayServices) {
        SecurityToken = securityToken;
        ApptId = apptId;
        this.companyId = companyId;
        this.ApptPayServices = ApptPayServices;
    }
}
