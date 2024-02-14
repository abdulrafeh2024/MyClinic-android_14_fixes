package com.telemedicine.myclinic.networks.rest.services

import com.telemedicine.myclinic.models.bookAppointment.GetRefSpecialtiesDTO
import com.telemedicine.myclinic.models.bookAppointment.GetRefSpecialtiesModel
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface ApptService {

    @POST("GetRefSpecialties")
    fun getRefSpecialties(@Body getRefSpecialtiesDTO: GetRefSpecialtiesDTO?): Single<GetRefSpecialtiesModel>
}