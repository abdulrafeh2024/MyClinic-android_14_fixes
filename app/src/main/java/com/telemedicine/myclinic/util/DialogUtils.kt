package com.telemedicine.myclinic.util

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog

object DialogUtils {

    fun showDialog(context: Context, title:String, message:String,positiveText:String="Yes",negativeText:String = "No", positiveButton: ()-> Unit, negativeButton:()-> Unit){
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(title)
        dialog.setMessage(message)

        dialog.setNegativeButton(negativeText, DialogInterface.OnClickListener { dialogInterface, i ->
            negativeButton()
            dialogInterface.dismiss()
        })


        dialog.setPositiveButton(positiveText, DialogInterface.OnClickListener { dialogInterface, i ->
            positiveButton()
            dialogInterface.dismiss()
        })

        val alertDialog: AlertDialog = dialog.create()
        if(TextUtil.getArabic(context)){
            alertDialog.window?.decorView?.layoutDirection = View.LAYOUT_DIRECTION_RTL
        }else{
            alertDialog.window?.decorView?.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }

        alertDialog.show()
    }

    fun showDialog(context: Context, title:String, message:String,positiveText:String="Yes", positiveButton: ()-> Unit){
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(title)
        dialog.setMessage(message)

        dialog.setPositiveButton(positiveText, DialogInterface.OnClickListener { dialogInterface, i ->
            positiveButton()
            dialogInterface.dismiss()
        })

        val alertDialog: AlertDialog = dialog.create()
        if(TextUtil.getArabic(context)){
            alertDialog.window?.decorView?.layoutDirection = View.LAYOUT_DIRECTION_RTL
        }else{
            alertDialog.window?.decorView?.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }

        alertDialog.show()
    }
}