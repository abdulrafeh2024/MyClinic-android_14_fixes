package com.telemedicine.myclinic.models.dashboardModels;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

import java.util.ArrayList;

public class ApptMinisModel extends BaseEntity {

    @SerializedName("ApptMinis")
    ArrayList<ApptGetFollowUpsModel> ApptMinis;

    public ArrayList<ApptGetFollowUpsModel> getApptMinis() {
        return ApptMinis;
    }

    public void setApptMinis(ArrayList<ApptGetFollowUpsModel> apptMinis) {
        ApptMinis = apptMinis;
    }
}
