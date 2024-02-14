package com.telemedicine.myclinic.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.telemedicine.myclinic.activities.HomeActivity
import com.telemedicine.myclinic.activities.LoginActivity
import com.telemedicine.myclinic.util.Const
import com.telemedicine.myclinic.util.Const.*
import com.telemedicine.myclinic.util.LocaleUtils
import com.telemedicine.myclinic.util.TextUtil
import com.telemedicine.myclinic.util.TinyDB
import java.util.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isTaskRoot) {
            val intent = intent
            val action = intent.action
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action != null && action == Intent.ACTION_MAIN) {
                finish()//Launcher Activity is not the root. So,finish it instead of launching
                return
            }
        }

        setContentView(R.layout.activity_splash)
        selected()
    }

    fun English(view: View) {
        var db = TinyDB(this@SplashActivity);
        db.putString(Const.LOCALITY, "1");
        db.putString(Const.LOCALITY_, "1_")
        LocaleUtils.initialize(this, LocaleUtils.ENGLISH)
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun Arabic(view: View) {
        var db = TinyDB(this@SplashActivity);
        db.putString(Const.LOCALITY, "2");
        db.putString(Const.LOCALITY_, "2_")
        LocaleUtils.initialize(this, LocaleUtils.ARABIC)
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun selected() {
        var db = TinyDB(this@SplashActivity);

        var check1 = db.getString(Const.LOCALITY_);
        if (check1 == "1_") {

            db.putString(Const.LOCALITY, "1")
        }

        if (check1 == "2_") {

            db.putString(Const.LOCALITY, "2")
        }

        var check = db.getString(Const.LOCALITY);
        endSession(db)

        if (check == "1" || check == "2") {

            db.putString(PATIENT_NAME, "")
            db.putString(PATIENT_GENDER, "")
            db.putString(Const.INSURANCE_ID, "0")
            db.putString(Const.MRN_NO, "")
            db.putLong(Const.PATIENT_ID, 0)

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    internal fun endSession(db: TinyDB) {
        db.putBoolean("session", false)
    }

}
