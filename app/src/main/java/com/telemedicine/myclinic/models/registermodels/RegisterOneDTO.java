package com.telemedicine.myclinic.models.registermodels;

import com.google.gson.annotations.SerializedName;

public class RegisterOneDTO {

    @SerializedName("Email")
    String Email;

    @SerializedName("Mobile")
    String Mobile;

    @SerializedName("PreferredLanguage")
    String PreferredLanguage;

    public RegisterOneDTO(String email, String mobile, String preferredLanguage) {
        Email = email;
        Mobile = mobile;
        PreferredLanguage = preferredLanguage;
    }
}
