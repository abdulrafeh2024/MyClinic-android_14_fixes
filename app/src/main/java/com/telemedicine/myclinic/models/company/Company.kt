package com.telemedicine.myclinic.models.company

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(

        @SerializedName("CompanyId") var companyId :String,
        @SerializedName("CompanyShortEn") var companyShortEn :String,
        @SerializedName("CompanyFullAr") var companyShortAr :String,
        @SerializedName("CompanyFullEn") var companyFullEn :String,
        @SerializedName("CompanyShortAr") var companyFullAr :String,
        @SerializedName("CityEn") var cityEn :String,
        @SerializedName("CityAr") var cityAr :String

) : Parcelable


/*

@SerializedName("CompanyId") var companyId :String,
@SerializedName("CompanyShortEn") var companyShortEn :String,
@SerializedName("CompanyShortAr") var companyShortAr :String,
@SerializedName("CompanyFullEn") var companyFullEn :String,
@SerializedName("CompanyFullAr") var companyFullAr :String,
@SerializedName("CityEn") var cityEn :String,
@SerializedName("CityAr") var cityAr :String*/
