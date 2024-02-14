package com.telemedicine.myclinic.models.reschedule;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

public class ApptRescheduleModel extends BaseEntity {

    @SerializedName("ApptNo")
    String ApptNo;

    @SerializedName("ApptId")
    long ApptId;

    public String getApptNo() {
        return ApptNo;
    }

    public void setApptNo(String apptNo) {
        ApptNo = apptNo;
    }

    public long getApptId() {
        return ApptId;
    }

    public void setApptId(long apptId) {
        ApptId = apptId;
    }
}
