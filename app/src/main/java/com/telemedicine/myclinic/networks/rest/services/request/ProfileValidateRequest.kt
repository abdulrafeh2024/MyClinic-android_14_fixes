package com.telemedicine.myclinic.networks.rest.services.request

import com.google.gson.annotations.SerializedName

data class ProfileValidateRequest(
        @SerializedName("MembershipNo") var MembershipNo:String,
        @SerializedName("SecurityToken") var SecurityToken : String,
        @SerializedName("NationalId") var NationalId:String
)

