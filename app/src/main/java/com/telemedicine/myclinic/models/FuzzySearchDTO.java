package com.telemedicine.myclinic.models;

import com.google.gson.annotations.SerializedName;

public class FuzzySearchDTO {
    @SerializedName("Keyword")
    String Keyword;

    @SerializedName("SecurityToken")
    String SecurityToken;

    public FuzzySearchDTO(String securityToken, String Keyword) {
        this.Keyword = Keyword;
        SecurityToken = securityToken;
    }
}
