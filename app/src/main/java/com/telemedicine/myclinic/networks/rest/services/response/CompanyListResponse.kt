package com.telemedicine.myclinic.networks.rest.services.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.telemedicine.myclinic.models.BaseEntity
import com.telemedicine.myclinic.models.company.Company
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

@Parcelize
data class CompanyListResponse(
        @SerializedName("Company") var companyList : ArrayList<Company>
):BaseEntity(), Parcelable