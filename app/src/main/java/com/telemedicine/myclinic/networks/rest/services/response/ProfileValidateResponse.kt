package com.telemedicine.myclinic.networks.rest.services.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.telemedicine.myclinic.models.BaseEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileValidateResponse(
        @SerializedName("IsMembershipNoExists") var isMembershipNoExists:Boolean,
        @SerializedName("IsNationalIdExists") var isNationalIdExists:Boolean
):BaseEntity(), Parcelable