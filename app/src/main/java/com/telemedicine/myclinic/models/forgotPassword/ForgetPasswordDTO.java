package com.telemedicine.myclinic.models.forgotPassword;

import com.google.gson.annotations.SerializedName;

public class ForgetPasswordDTO {

    @SerializedName("Email")
    String String;

    @SerializedName("PreferredLanguage")
    int PreferredLanguage;

    public ForgetPasswordDTO(java.lang.String string, int preferredLanguage) {
        String = string;
        PreferredLanguage = preferredLanguage;
    }
}
