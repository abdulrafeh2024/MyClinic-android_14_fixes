package com.telemedicine.myclinic.networks.rest.services.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class YakeenResult(
    @SerializedName("DateOfBirth")
    var dateOfBirth: String?,
    @SerializedName("DateOfBirthH")
    var dateOfBirthH: String?,
    @SerializedName("FamilyName")
    var familyName: String?,
    @SerializedName("FatherName")
    var fatherName: String?,
    @SerializedName("FirstNameAr")
    var firstNameAr: String?,
    @SerializedName("FirstNameEn")
    var firstNameEn: String?,
    @SerializedName("Gender")
    var gender: String?,
    @SerializedName("GrandFatherName")
    var grandFatherName: String?,
    @SerializedName("IqamaExpiryDate")
    var iqamaExpiryDate: String?,
    @SerializedName("LastNameAr")
    var lastNameAr: String?,
    @SerializedName("LastNameEn")
    var lastNameEn: String?,
    @SerializedName("LogId")
    var logId: Int?,
    @SerializedName("NationalityDesc")
    var nationalityDesc: String?,
    @SerializedName("OccupationDesc")
    var occupationDesc: String?,
    @SerializedName("PlaceOfBirth")
    var placeOfBirth: String?,
    @SerializedName("PlaceOfBirthDesc")
    var placeOfBirthDesc: String?,
    @SerializedName("SecondNameAr")
    var secondNameAr: String?,
    @SerializedName("SecondNameEn")
    var secondNameEn: String?,
    @SerializedName("SponsorName")
    var sponsorName: String?,
    @SerializedName("ThirdNameAr")
    var thirdNameAr: String?,
    @SerializedName("ThirdNameEn")
    var thirdNameEn: String?,
    @SerializedName("YaqeenError")
    var yaqeenError: String?,
    @SerializedName("YaqeenStatus")
    var yaqeenStatus: Int?
): Parcelable