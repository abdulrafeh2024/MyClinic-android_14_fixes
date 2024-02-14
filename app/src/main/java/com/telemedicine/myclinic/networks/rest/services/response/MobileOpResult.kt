package com.telemedicine.myclinic.networks.rest.services.response


import com.google.gson.annotations.SerializedName

data class MobileOpResult(
    @SerializedName("BusinessErrorMessageAr")
    var businessErrorMessageAr: String?,
    @SerializedName("BusinessErrorMessageEn")
    var businessErrorMessageEn: String?,
    @SerializedName("ErrorException")
    var errorException: Any?,
    @SerializedName("ExpiryDate")
    var expiryDate: String?,
    @SerializedName("recordID")
    var recordID: Int?,
    @SerializedName("Result")
    var result: Int?,
    @SerializedName("ResultDesc")
    var resultDesc: String?,
    @SerializedName("SecurityToken")
    var securityToken: String?,
    @SerializedName("TechnicalErrorMessage")
    var technicalErrorMessage: String?
)