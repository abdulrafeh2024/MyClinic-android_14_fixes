package com.telemedicine.myclinic.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.telemedicine.myclinic.viewmodels.ChangePasswordViewModel
import com.telemedicine.myclinic.viewmodels.DashboardViewModel
import com.telemedicine.myclinic.viewmodels.ProfileRegistrationViewModel
import com.telemedicine.myclinic.viewmodels.ReScheduleApptViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(ProfileRegistrationViewModel::class)
    internal abstract fun profileRegistrationViewModel(viewModel: ProfileRegistrationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordViewModel::class)
    internal abstract fun changePasswordViewModel(viewModel: ChangePasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    internal abstract fun dashboardViewModel(viewModel: DashboardViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(ReScheduleApptViewModel::class)
    internal abstract fun reScheduleApptViewModel(viewModel: ReScheduleApptViewModel): ViewModel



}