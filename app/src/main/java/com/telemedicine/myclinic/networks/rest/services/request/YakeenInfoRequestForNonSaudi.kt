package com.telemedicine.myclinic.networks.rest.services.request

import com.google.gson.annotations.SerializedName

data class YakeenInfoRequestForNonSaudi(
    @SerializedName("SecurityToken") var SecurityToken:String,
    @SerializedName("IqamaNumber") var IqamaNumber : String,
    @SerializedName("DateOfBirth") var DateOfBirth:String,
    @SerializedName("NationalIdType") var NationalIdType:String
)