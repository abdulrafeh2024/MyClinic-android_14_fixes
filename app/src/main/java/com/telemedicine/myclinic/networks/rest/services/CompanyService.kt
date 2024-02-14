package com.telemedicine.myclinic.networks.rest.services

import com.telemedicine.myclinic.models.profilecreatoinmodels.RefPatientProfileModel
import com.telemedicine.myclinic.models.profilecreatoinmodels.SecurityTokenDTO
import com.telemedicine.myclinic.networks.rest.services.response.CompanyListResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface CompanyService {

    @POST("GetRefCompanies")
    fun getRefCompanies(@Body securityTokenDTO : SecurityTokenDTO): Single<CompanyListResponse>
}