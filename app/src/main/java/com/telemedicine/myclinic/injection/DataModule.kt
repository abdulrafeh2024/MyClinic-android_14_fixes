package com.telemedicine.myclinic.injection


import android.content.Context
import com.telemedicine.myclinic.networks.rest.services.ApptService
import com.telemedicine.myclinic.networks.rest.services.CompanyService
import com.telemedicine.myclinic.networks.rest.services.ProfileServices
import com.telemedicine.myclinic.util.TinyDB
import com.telemedicine.myclinic.webservices.APIService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun getUserStorage(@AppContext context: Context): TinyDB {
        return TinyDB(context);
    }


    @Provides
    @Singleton
    fun getProfileServices(retrofit: Retrofit): ProfileServices {
        return retrofit.create(ProfileServices::class.java)
    }


    @Provides
    @Singleton
    fun getCompanyService(retrofit: Retrofit): CompanyService {
        return retrofit.create(CompanyService::class.java)
    }

    @Provides
    @Singleton
    fun getApiService(retrofit: Retrofit):ApptService{
        return retrofit.create(ApptService::class.java)
    }

}