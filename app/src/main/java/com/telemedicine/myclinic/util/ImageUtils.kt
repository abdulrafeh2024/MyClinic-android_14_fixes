package com.telemedicine.myclinic.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File

object ImageUtils {

     fun encodeImage(path: String): Bitmap? {
        val file = File(path)
        var myBitmap: Bitmap? = null
        if (file.exists()) {
            myBitmap = BitmapFactory.decodeFile(file.absolutePath)
        }
        return myBitmap
    }

    fun encodeTobase64(image: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 10, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.NO_WRAP or Base64.DEFAULT)
    }
}