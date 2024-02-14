package com.telemedicine.myclinic.driver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.telemedicine.myclinic.activities.BaseActivity
import com.telemedicine.myclinic.myapplication.R

class TwoStepVerificationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun layout(): Int {
        return  R.layout.activity_two_step_verification
    }


}