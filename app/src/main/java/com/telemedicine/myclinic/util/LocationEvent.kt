package com.telemedicine.myclinic.util

import android.location.Address

data class LocationEvent(val latitude: Double,val longitude: Double, val address: List<Address>?) {

}

data class AppointmentEvent(val doctorNameEn: String, val doctorNameAr: String, val errorMSg: String) {

}
