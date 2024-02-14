package com.telemedicine.myclinic.injection

import androidx.fragment.app.FragmentActivity
import dagger.Module

@Module class ViewModule(val activity: FragmentActivity?){
    //put view level dependency here
}