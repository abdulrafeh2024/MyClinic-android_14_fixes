package com.telemedicine.myclinic.networks.rest.services.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SaudiInfoResult(
        @SerializedName("dateOfBirthH")
        var dateOfBirthH: String,
        @SerializedName("englishFirstName")
        var englishFirstName: String,
        @SerializedName("englishLastName")
        var englishLastName: String,
        @SerializedName("englishSecondName")
        var englishSecondName: String,
        @SerializedName("englishThirdName")
        var englishThirdName: String,
        @SerializedName("familyName")
        var familyName: String,
        @SerializedName("fatherName")
        var fatherName: String,
        @SerializedName("firstName")
        var firstName: String,
        @SerializedName("gender")
        var gender: String,
        @SerializedName("grandFatherName")
        var grandFatherName: String,
        @SerializedName("logId")
        var logId: Int,
        @SerializedName("placeOfBirth")
        var placeOfBirth: String
) : Parcelable