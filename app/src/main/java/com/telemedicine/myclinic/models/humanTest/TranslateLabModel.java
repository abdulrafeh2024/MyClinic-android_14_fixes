package com.telemedicine.myclinic.models.humanTest;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

public class TranslateLabModel extends BaseEntity {

    @SerializedName("ResultVal")
    String ResultVal;

    public String getResultVal() {
        return ResultVal;
    }

    public void setResultVal(String resultVal) {
        ResultVal = resultVal;
    }
}
