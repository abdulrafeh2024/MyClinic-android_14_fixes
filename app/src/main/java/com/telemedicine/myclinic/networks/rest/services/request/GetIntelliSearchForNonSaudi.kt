package com.telemedicine.myclinic.networks.rest.services.request

import com.google.gson.annotations.SerializedName

data class GetIntelliSearchForNonSaudi(
        @SerializedName("IqamaNumber") var IqamaNumber:String,
        @SerializedName("DateOfBirth") var DateOfBirth : String
)
