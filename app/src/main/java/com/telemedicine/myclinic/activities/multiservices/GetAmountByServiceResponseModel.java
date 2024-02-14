package com.telemedicine.myclinic.activities.multiservices;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

public class GetAmountByServiceResponseModel extends BaseEntity {

    @SerializedName("AmountDue")
    Double AmountDue;

    public Double getAmountDue() {
        return AmountDue;
    }

    public void setAmountDue(Double amountDue) {
        AmountDue = amountDue;
    }
}
