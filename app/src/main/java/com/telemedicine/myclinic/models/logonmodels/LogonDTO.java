package com.telemedicine.myclinic.models.logonmodels;

import com.google.gson.annotations.SerializedName;

public class LogonDTO {

    @SerializedName("Email")
    String Email;

    @SerializedName("Password")
    String Password;

    @SerializedName("DeviceType")
    String DeviceType;

    @SerializedName("OSVersion")
    String OSVersion;

    @SerializedName("DeviceToken")
    String DeviceToken;

    @SerializedName("AppVersion")
    String AppVersion;

    @SerializedName("PreferredLanguage")
    String PreferredLanguage;

    @SerializedName("Provider")
    int Provider;

    @SerializedName("OAuthUserId")
    String OAuthUserId;


    public LogonDTO(String email, String password, String deviceType, String OSVersion, String deviceToken, String appVersion, String preferredLanguage,  int provider, String oAuthToken) {
        Email = email;
        Password = password;
        DeviceType = deviceType;
        this.OSVersion = OSVersion;
        DeviceToken = deviceToken;
        AppVersion = appVersion;
        PreferredLanguage = preferredLanguage;
        Provider = provider;
        OAuthUserId = oAuthToken;
    }
}
