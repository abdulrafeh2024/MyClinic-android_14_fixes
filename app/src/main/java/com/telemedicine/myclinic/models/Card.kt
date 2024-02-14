package com.telemedicine.myclinic.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Card(
        @SerializedName("CardBrand") val cardBrand: String?,
        @SerializedName("CardHolder") val cardHolder: String?,
        @SerializedName("CardHolderRegID") val cardHolderRegID: String?,
        @SerializedName("ExpiryMonth") val expiryMonth: String?,
        @SerializedName("ExpiryYear") val expiryYear: String?,
        @SerializedName("Last4Digit") val last4Digit: String?,
        @SerializedName("PaymentCardId") val paymentCardId: Int?
): Parcelable
