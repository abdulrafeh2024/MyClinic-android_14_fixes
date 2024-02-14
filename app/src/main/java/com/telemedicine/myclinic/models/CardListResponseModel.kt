package com.telemedicine.myclinic.models


import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardListResponseModel(
    @SerializedName("Cards")
    val cards: ArrayList<Card?>?,
    @SerializedName("MobileOpResult")
    val mobileOpResult: MobileOpResultCard?
):BaseEntity(), Parcelable