package com.telemedicine.myclinic.models.registermodels;

import com.google.gson.annotations.SerializedName;

public class RegisterStageTwoDTO {

    @SerializedName("Email")
    String Email;

    @SerializedName("Mobile")
    String Mobile;

    @SerializedName("VerificationCode")
    String VerificationCode;

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

    @SerializedName("Provider")
    int Provider;

    @SerializedName("OAuthUserId")
    String OAuthUserId;

    public RegisterStageTwoDTO(String email, String mobile, String verificationCode, String password, String deviceType, String OSVersion, String deviceToken, String appVersion,
                               int provider, String oAuthToken) {
        Email = email;
        Mobile = mobile;
        VerificationCode = verificationCode;
        Password = password;
        DeviceType = deviceType;
        this.OSVersion = OSVersion;
        DeviceToken = deviceToken;
        AppVersion = appVersion;
        Provider = provider;
        OAuthUserId = oAuthToken;

    }
}
