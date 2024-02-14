package com.telemedicine.myclinic.networks.rest.services.request

import com.google.gson.annotations.SerializedName


data class GetIntelliSearchForSaudi(
        @SerializedName("NationalId") var NationalId:String,
        @SerializedName("DateOfBirth") var DateOfBirth : String
)