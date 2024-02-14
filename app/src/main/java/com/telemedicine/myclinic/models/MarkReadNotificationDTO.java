package com.telemedicine.myclinic.models;

import com.google.gson.annotations.SerializedName;

public class MarkReadNotificationDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientRecId")
    String PatientRecId;

    @SerializedName("MKPushNotificationCampaignId")
    String MKPushNotificationCampaignId;

    @SerializedName("NotificationReadDate")
    String NotificationReadDate;

    @SerializedName("OregId")
    String OregId;

    public MarkReadNotificationDTO(String securityToken, String patientRecId,String mKPushNotificationCampaignId,
                                   String notificationReadDate, String oregId) {
        SecurityToken = securityToken;
        PatientRecId  = patientRecId;
        MKPushNotificationCampaignId  = mKPushNotificationCampaignId;
        NotificationReadDate = notificationReadDate;
        OregId = oregId;
    }
}