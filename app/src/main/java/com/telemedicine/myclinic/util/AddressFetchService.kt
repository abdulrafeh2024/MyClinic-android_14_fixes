package com.telemedicine.myclinic.util

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.core.app.JobIntentService
import com.telemedicine.myclinic.myapplication.R
import org.greenrobot.eventbus.EventBus
import java.io.IOException
import java.util.*

class AddressFetchService:   JobIntentService()
{
    private val TAG by lazy { this::class.java.simpleName }


    override fun onHandleWork(intent: Intent) {
        var errorMessage = ""

        //Get the location passed to this service through an extra.
        var latitude = intent.getStringExtra("lat")
        var longitude = intent.getStringExtra("lng")

        //Make sure the location is really sent
        if (latitude != null) {
            val geocoder = Geocoder(this, Locale.ROOT)
            var addresses: List<Address>? = null
            try {
                addresses = geocoder.getFromLocation(latitude.toDouble(), longitude!!.toDouble(), 1)
            } catch (ioException: IOException) { //Catches network or i/o problems

            } catch (illegalArgumentException: IllegalArgumentException) { //Error in latitude or longitude data

            }
            //Handles cases where no addresses where found
            if (addresses == null || addresses.isEmpty()) {

            } else {
                val address = addresses[0]
                EventBus.getDefault().post(
                    LocationEvent(
                        latitude.toDouble(),
                        longitude!!.toDouble(),
                        addresses
                    )
                )
            }
        }
    }

    companion object{
        fun enqueueWork(context: Context, intent: Intent){
            enqueueWork(context,AddressFetchService::class.java, 1, intent)
        }
    }

}
