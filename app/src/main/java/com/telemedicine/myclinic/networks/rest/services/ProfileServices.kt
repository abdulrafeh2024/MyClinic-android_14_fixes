package com.telemedicine.myclinic.networks.rest.services

import com.telemedicine.myclinic.models.MobileOpResult
import com.telemedicine.myclinic.models.profileInsuranceValidate.ProfileInsuranceValidateDTO
import com.telemedicine.myclinic.models.profileInsuranceValidate.ProfileInsuranceValidateModel
import com.telemedicine.myclinic.models.profileUpdate.GetProfileDTO
import com.telemedicine.myclinic.models.profileUpdate.ProfileUpdateDTO
import com.telemedicine.myclinic.models.profileUpdate.ProfileUpdateModel
import com.telemedicine.myclinic.models.profilecreatoinmodels.ProfileCreateDTO
import com.telemedicine.myclinic.models.profilecreatoinmodels.ProfileCreateModel
import com.telemedicine.myclinic.models.profilecreatoinmodels.RefPatientProfileModel
import com.telemedicine.myclinic.models.profilecreatoinmodels.SecurityTokenDTO
import com.telemedicine.myclinic.networks.rest.services.request.*
import com.telemedicine.myclinic.networks.rest.services.response.GetIntelliSearchInfoForNonSaudiResponse
import com.telemedicine.myclinic.networks.rest.services.response.GetIntelliSearchInfoForSaudiResponse
import com.telemedicine.myclinic.networks.rest.services.response.ProfileValidateResponse
import com.telemedicine.myclinic.networks.rest.services.response.YakeenInfoResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface ProfileServices {

    @POST("GetRefPatientProfile")
    fun refPatientProfile(@Body securityTokenDTO : SecurityTokenDTO):Single<RefPatientProfileModel>

    @POST("ProfileValidate")
    fun profileValidate(@Body profileValidateRequest: ProfileValidateRequest):Single<ProfileValidateResponse>

    @POST("GetIntelliSearchInfoForNonSaudi")
    fun getIntelliSearchInfoForNonSaudi(@Body getIntelliSearchForNonSaudi: GetIntelliSearchForNonSaudi):Single<GetIntelliSearchInfoForNonSaudiResponse>

    @POST("GetIntelliSearchInfoForSaudi")
    fun getIntelliSearchInfoForSaudi(@Body getIntelliSearchForSaudi: GetIntelliSearchForSaudi):Single<GetIntelliSearchInfoForSaudiResponse>

    @POST("ProfileCreate")
    fun profileCreate(@Body profileCreateDTO: ProfileCreateDTO): Single<ProfileCreateModel>

    @POST("ProfileUpdate")
    fun profileUpdate(@Body profileUpdateDTO: ProfileCreateDTO): Single<ProfileCreateModel>

    @POST("ProfileInsuranceValidate")
    fun profileInsuranceValidate(@Body profileCreateDTO: ProfileInsuranceValidateDTO?): Single<ProfileInsuranceValidateModel>

    @POST("ChangePassword")
    fun changePassword(@Body changePasswordRequest: ChangePasswordRequest):Single<MobileOpResult>

    @POST("ProfileGetById")
    fun profileGetById(@Body getProfileDTO : GetProfileDTO):Single<ProfileUpdateModel>

    @POST("GetYakeenInfo")
    fun GetYakeenInfoNonSaudi(@Body yakeenInfoRequestForNonSaudi: YakeenInfoRequestForNonSaudi):
            Single<YakeenInfoResponse>

    @POST("GetYakeenInfo")
    fun GetYakeenInfoSaudi(@Body yakeenInfoRequestForSaudi: YakeenInfoRequestForSaudi):
            Single<YakeenInfoResponse>

}