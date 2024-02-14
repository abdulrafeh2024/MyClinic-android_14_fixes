package com.telemedicine.myclinic.injection


import com.telemedicine.myclinic.activities.profile.AddProfileActivity
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [(ViewModule::class)])
interface ViewComponent {


}
