package com.telemedicine.myclinic.models.appointments;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

public class ApptDetailModel extends BaseEntity {

    @SerializedName("Appt")
    ApptModel Appt;

    public ApptModel getAppt() {
        return Appt;
    }

    public void setAppt(ApptModel appt) {
        Appt = appt;
    }
}
