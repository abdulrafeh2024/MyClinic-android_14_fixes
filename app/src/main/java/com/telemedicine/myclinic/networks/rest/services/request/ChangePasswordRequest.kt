package com.telemedicine.myclinic.networks.rest.services.request

import com.google.gson.annotations.SerializedName


data class ChangePasswordRequest(
        @SerializedName("SecurityToken") var securityToken: String,
        @SerializedName("OldPassword") var oldPassword: String,
        @SerializedName("NewPassword") var newPassword: String,
        @SerializedName("ORegId") var oRegId: String
)