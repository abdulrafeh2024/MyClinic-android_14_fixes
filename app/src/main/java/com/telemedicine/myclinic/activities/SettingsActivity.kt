package com.telemedicine.myclinic.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import butterknife.OnClick
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.activities.profile.ChangePasswordActivity
import com.telemedicine.myclinic.util.AppointmentEvent
import com.telemedicine.myclinic.util.Const
import com.telemedicine.myclinic.util.ErrorMessage
import com.telemedicine.myclinic.util.TextUtil
import kotlinx.android.synthetic.main.activity_settings.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class SettingsActivity : BaseActivity() {

    var fromCancel = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fromCancel = false
        biomatricSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                if(!fromCancel){
                    showBiometricSwitchDialog()
                }
            }else{
                fromCancel = false
                hideBiometricSwitchDialog()
            }
        }
        transformView()
        val biometricSwitchSupport = tinyDB.getBoolean(Const.BIOMETRIC_SUPPORT)

        if(biometricSwitchSupport){
            showBiometricSwitch()
        }else{
            hideBiometricSwitch()
        }

        val isEnable = tinyDB.getBoolean(Const.BIOMETRIC_ENABLED)
        if(isEnable){
            fromCancel = true
            biomatricSwitch.isChecked = true
            showDisableLabel()
        }else{
            showEnableLabel()
        }
    }

    private fun showEnableLabel(){
        biomatricLabel.text = getString(R.string.enable_smart_touch)
    }

    private fun showDisableLabel(){
        biomatricLabel.text = getString(R.string.disable_smart_touch)
    }

    private fun showBiometricSwitchDialog() {
        TextUtil.dialgoue(this@SettingsActivity, getString(R.string.successfully_enabled_touch))
        fromCancel = false
        tinyDB.putBoolean(Const.BIOMETRIC_ENABLED, true)
        showDisableLabel()
    }

    private fun hideBiometricSwitchDialog() {
        TextUtil.dialogCardSaving(this@SettingsActivity, getString(R.string.disabled_smart_touch), { dialog, which ->

            fromCancel = false
            tinyDB.putBoolean(Const.BIOMETRIC_ENABLED, false)
            showEnableLabel()
            dialog.dismiss()
        })
        { dialog, which ->
            fromCancel = true
            biomatricSwitch.isChecked  = true
            dialog.dismiss()
        }
    }


    private fun hideBiometricSwitch() {
        biometricView.visibility = View.INVISIBLE
        viewLine.visibility = View.INVISIBLE
    }

    private fun showBiometricSwitch() {
        biometricView.visibility = View.VISIBLE
        viewLine.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        transformViews()
    }

    fun transformView(){
        if (TextUtil.getArabic(this)) {
   /*         val paramsBio = biomatricLabel.layoutParams as RelativeLayout.LayoutParams
            paramsBio.addRule(RelativeLayout.ALIGN_PARENT_END)
            biomatricLabel.layoutParams = paramsBio*/

           /* val paramsSwitch = biomatricSwitch.layoutParams as RelativeLayout.LayoutParams
            paramsSwitch.addRule(RelativeLayout.ALIGN_PARENT_END)
            biomatricSwitch.layoutParams = paramsSwitch*/
            biometricView.rotation = 180f
            biomatricLabel.rotation = 180f

        }else{
            /*val paramsBio = biomatricLabel.layoutParams as RelativeLayout.LayoutParams
            paramsBio.addRule(RelativeLayout.ALIGN_PARENT_START)
            biomatricLabel.layoutParams = paramsBio

            val paramsSwitch = biomatricSwitch.layoutParams as RelativeLayout.LayoutParams
            paramsSwitch.addRule(RelativeLayout.ALIGN_PARENT_END)
            biomatricSwitch.layoutParams = paramsSwitch*/
            biometricView.rotation = 0f
            biomatricLabel.rotation = 0f
        }

    }
    override fun layout(): Int {
        return R.layout.activity_settings
    }



    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(appointmentEvent: AppointmentEvent) {
        if (appointmentEvent.doctorNameEn != "") {
            if (TextUtil.getArabic(this)) {
                ErrorMessage.getInstance().showSuccessGreen(
                    this@SettingsActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameAr + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            } else {
                ErrorMessage.getInstance().showSuccessGreen(
                    this@SettingsActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameEn + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            }
        } else {
            ErrorMessage.getInstance()
                .showErrorYellow(this@SettingsActivity, appointmentEvent.errorMSg)
        }
    }

    @OnClick(R.id.textChangePassword)
    fun onChangePasswordClicked(){
        startActivity(ChangePasswordActivity.getLaunchIntent(this))
    }

    @OnClick(R.id.toolbar_left_iv)
    fun backPressed() {
        onBackPressed()
    }

    private fun transformViews(){
        if (TextUtil.getArabic(this)) {
            textChangePassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.grey_left_arrow, 0, 0, 0)
        }else{
            textChangePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.grey_right_arrow, 0)
        }
    }

    companion object{
        fun getLaunchIntent(context: Context):Intent{
            return Intent(context, SettingsActivity::class.java)
        }
    }
}