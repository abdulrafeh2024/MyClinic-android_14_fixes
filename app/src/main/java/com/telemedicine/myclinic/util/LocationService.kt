package com.telemedicine.myclinic.util

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.*
import org.greenrobot.eventbus.EventBus
import java.util.*


class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationRequest: LocationRequest = create().apply {
        interval = 3000
        fastestInterval = 3000
        priority = PRIORITY_BALANCED_POWER_ACCURACY
        maxWaitTime = 5000
    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                val location = locationList.last()
                //  Toast.makeText(this@LocationService, "Latitude: " + location.latitude.toString() + '\n' +
                //        "Longitude: "+ location.longitude, Toast.LENGTH_LONG).show()
                Log.d("Location d", location.latitude.toString())
                Log.i("Location i", location.latitude.toString())

                EventBus.getDefault().post(
                    LocationEvent(
                        location.latitude,
                        location.longitude,
                        null
                    )
                )
            }
        }
    }

    private fun getReverseCoding(latitude: Double, longitude: Double) {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        val addresses: List<Address>
        val geocoder: Geocoder = Geocoder(this@LocationService, Locale.getDefault())

     /*   addresses = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        )
*/
     /*   val address: String =
            addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        val city: String = addresses[0].locality
        val state: String = addresses[0].adminArea
        val country: String = addresses[0].countryName
        val postalCode: String = addresses[0].postalCode
        val knownName: String = addresses[0].featureName*/


    }

    private fun showToast(it: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(this@LocationService, it, Toast.LENGTH_LONG).show()
        }
    }

    /*******************/

    override fun onCreate() {
        super.onCreate()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) createNotificationChanel() else startForeground(
            2,
            Notification()
        )

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {

            Toast.makeText(applicationContext, "Permission required", Toast.LENGTH_LONG).show()
            return
        } else {
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChanel() {
        val notificationChannelId = "Location channel id two"
        val channelName = "Background Service two"
        val chan = NotificationChannel(
            notificationChannelId,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder =
            NotificationCompat.Builder(this, notificationChannelId)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle("Location updates:")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        startForeground(2, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}