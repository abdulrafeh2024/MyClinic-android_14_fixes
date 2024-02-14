package com.telemedicine.myclinic.models

import com.google.gson.annotations.SerializedName

class CardDTO(@field:SerializedName("SecurityToken") var SecurityToken: String,
                      @field:SerializedName("ORegId") var oregID: Long)