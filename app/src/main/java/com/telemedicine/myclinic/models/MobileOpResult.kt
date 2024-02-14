package com.telemedicine.myclinic.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MobileOpResultCard(
        @SerializedName("BusinessErrorMessageAr") val businessErrorMessageAr: String?,
        @SerializedName("BusinessErrorMessageEn") val businessErrorMessageEn: String?,
        @SerializedName("ErrorException") val errorException: String?,
        @SerializedName("ExpiryDate") val expiryDate: String?,
        @SerializedName("Result") val result: Int?,
        @SerializedName("ResultDesc") val resultDesc: String?,
        @SerializedName("SecurityToken") val securityToken: String?,
        @SerializedName("TechnicalErrorMessage") val technicalErrorMessage: String?
): Parcelable