package com.telemedicine.myclinic.activities.multiservices;

import com.airbnb.lottie.L;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAmountByServiceIdDTO {


    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("ApptId")
    long ApptId;

    @SerializedName("CaseTransRecIds")
    ArrayList<Long> CaseTransRecId;

    @SerializedName("UnMarkedCaseTransRecIds")
    ArrayList<Long> UnMarkedCaseTransRecId;

    @SerializedName("Company")
    String CompanyId;

    public GetAmountByServiceIdDTO(String securityToken, long apptId, ArrayList<Long>  caseTransRecId, ArrayList<Long>  unMarkedCaseTransRecId,
                                   String companyId) {
        SecurityToken = securityToken;
        ApptId = apptId;
        CaseTransRecId = caseTransRecId;
        UnMarkedCaseTransRecId = unMarkedCaseTransRecId;
        CompanyId = companyId;
    }
}