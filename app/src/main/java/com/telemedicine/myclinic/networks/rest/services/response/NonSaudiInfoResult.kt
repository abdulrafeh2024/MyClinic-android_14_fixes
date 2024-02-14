package com.telemedicine.myclinic.networks.rest.services.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NonSaudiInfoResult(
        @SerializedName("dateOfBirthG")
        var dateOfBirthG: String,
        @SerializedName("englishFirstName")
        var englishFirstName: String,
        @SerializedName("englishLastName")
        var englishLastName: String,
        @SerializedName("englishSecondName")
        var englishSecondName: String,
        @SerializedName("englishThirdName")
        var englishThirdName: String,
        @SerializedName("firstName")
        var firstName: String,
        @SerializedName("gender")
        var gender: String,
        @SerializedName("iqamaExpiryDateG")
        var iqamaExpiryDateG: String,
        @SerializedName("lastName")
        var lastName: String,
        @SerializedName("logId")
        var logId: Int,
        @SerializedName("nationalityDesc")
        var nationalityDesc: String,
        @SerializedName("occupationDesc")
        var occupationDesc: String,
        @SerializedName("placeOfBirthDesc")
        var placeOfBirthDesc: String,
        @SerializedName("secondName")
        var secondName: String,
        @SerializedName("sponsorName")
        var sponsorName: String,
        @SerializedName("thirdName")
        var thirdName: String
): Parcelable