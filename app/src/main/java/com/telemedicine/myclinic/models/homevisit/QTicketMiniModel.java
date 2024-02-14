package com.telemedicine.myclinic.models.homevisit;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

public class QTicketMiniModel extends BaseEntity {

    @SerializedName("QTicketMini")
    QTicketMini qTicketMini;

    public QTicketMini getqTicketMini() {
        return qTicketMini;
    }

    public void setqTicketMini(QTicketMini qTicketMini) {
        this.qTicketMini = qTicketMini;
    }
}
