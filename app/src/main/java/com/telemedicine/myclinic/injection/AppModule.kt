package com.telemedicine.myclinic.injection
import android.content.Context
import com.telemedicine.myclinic.App


import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: App) {

    @Provides
    @Singleton
    fun provideApp() = app


    @Provides
    @AppContext
    internal fun provideContext(): Context {
        return app.applicationContext
    }

}