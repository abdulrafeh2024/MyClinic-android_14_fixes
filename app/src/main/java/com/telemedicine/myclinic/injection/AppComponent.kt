package com.telemedicine.myclinic.injection

import com.telemedicine.myclinic.App
import com.telemedicine.myclinic.activities.DashBoardActivity
import com.telemedicine.myclinic.activities.HomeActivity
import com.telemedicine.myclinic.activities.ReScheduleApptActivity
import com.telemedicine.myclinic.activities.SearchAppointmentFilterActivity
import com.telemedicine.myclinic.activities.profile.AddInsuranceProfileActivity
import com.telemedicine.myclinic.activities.profile.AddProfileActivity
import com.telemedicine.myclinic.activities.profile.AddProfileInfoActivity
import com.telemedicine.myclinic.activities.profile.ChangePasswordActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class, RetrofitModule::class, DataModule::class])
interface AppComponent {

    fun inject(app: App)

    fun plus(viewModule: ViewModule): ViewComponent

    fun inject(activity: AddProfileActivity)

    fun inject(activity: AddProfileInfoActivity)

    fun inject(activity: AddInsuranceProfileActivity)

    fun inject(activity: ChangePasswordActivity)

    fun inject(activity: DashBoardActivity)

    fun inject(activity: SearchAppointmentFilterActivity)

    fun inject(activity: ReScheduleApptActivity)

    fun inject(activity: HomeActivity)
}