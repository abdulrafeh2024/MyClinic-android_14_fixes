package com.telemedicine.myclinic.networks.rest.services.request

import com.google.gson.annotations.SerializedName

data class YakeenInfoRequestForSaudi(
    @SerializedName("SecurityToken") var SecurityToken:String,
    @SerializedName("NationalId") var NationalId : String,
    @SerializedName("DateOfBirth") var DateOfBirth:String,
    @SerializedName("NationalIdType") var NationalIdType:String
)