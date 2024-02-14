package com.telemedicine.myclinic.injection

import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import com.telemedicine.myclinic.App
import com.telemedicine.myclinic.myapplication.BuildConfig
import com.telemedicine.myclinic.webservices.GsonUTCDateAdapter
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Modifier
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RetrofitModule {


    @Singleton
    @Provides
    fun getRetrofitClient(): Retrofit {

        val cookieJar: ClearableCookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(App.getInstance()))

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
//                .addInterceptor { chain: Interceptor.Chain? ->
//
//                    val request: Request? = chain?.request()
//                            ?.newBuilder()
//                           // ?.addHeader("userToken", token)
//                            ?.build()
//
//                    chain?.proceed(request)
//                }


        if (BuildConfig.DEBUG) {
            client.addInterceptor(logging)
        }
        client.cookieJar(cookieJar)
        client.connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)

        val gson = GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
                .registerTypeAdapter(Date::class.java, GsonUTCDateAdapter())
                .create()

        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client.build())
                .build()
    }
}