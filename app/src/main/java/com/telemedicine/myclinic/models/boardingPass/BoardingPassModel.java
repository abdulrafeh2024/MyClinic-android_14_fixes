package com.telemedicine.myclinic.models.boardingPass;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

public class BoardingPassModel extends BaseEntity {

    @SerializedName("BoardingPassUrl")
    String BoardingPassUrl;

    @SerializedName("ApptStatus")
    int ApptStatus;

    public String getBoardingPassUrl() {
        return BoardingPassUrl;
    }

    public void setBoardingPassUrl(String boardingPassUrl) {
        BoardingPassUrl = boardingPassUrl;
    }

    public int getApptStatus() {
        return ApptStatus;
    }

    public void setApptStatus(int apptStatus) {
        ApptStatus = apptStatus;
    }
}
