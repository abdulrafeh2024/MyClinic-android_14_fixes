package com.telemedicine.myclinic.activities.profile

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.InputFilter
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.OnClick
import com.telemedicine.myclinic.activities.BaseActivity
import com.telemedicine.myclinic.base.BaseViewModel
import com.telemedicine.myclinic.extensions.hideKeyboard
import com.telemedicine.myclinic.injection.ViewModelFactory
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.util.*
import com.telemedicine.myclinic.viewmodels.ChangePasswordViewModel
import kotlinx.android.synthetic.main.activity_change_password.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Locale
import javax.inject.Inject

class ChangePasswordActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var  viewModel:ChangePasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,viewModelFactory)[ChangePasswordViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        configPasswordFields();

        viewModel.localizeErrorMessage.observe(this, Observer { mobileOpResult ->
            var errorMesg = ""
            mobileOpResult?.let {
                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.businessErrorMessageEn)) {
                    if (TextUtil.getEnglish(this@ChangePasswordActivity)) errorMesg = mobileOpResult.businessErrorMessageEn + "\n" else if (TextUtil.getArabic(this@ChangePasswordActivity)) errorMesg = mobileOpResult.businessErrorMessageAr + "\n"
                }

                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.technicalErrorMessage)) {
                    errorMesg = """
                    ${errorMesg}Technical Info : 
                    ${mobileOpResult.technicalErrorMessage}
                    """.trimIndent()
                }
                ErrorMessage.getInstance().showError(this@ChangePasswordActivity, errorMesg)
            }

        })

        viewModel.changePasswordResponse.observe(this, Observer {
            if(it){
                DialogUtils.showDialog(this,getString(R.string.app_name),getString(R.string.text_password_changed),positiveText = getString(R.string.ok),positiveButton = {
                   finish()
                })
            }
        })
        viewModel.validation.observe(this, Observer {
            when(it.name){

                ChangePasswordViewModel.Validation.OLD_PASSWORD.name -> {
                    TextUtil.tILError(this, old_password_edt_til)
                    ErrorMessage.getInstance().showValidationMessage(this, old_password_edt, getString(it.msgId))
                }
                ChangePasswordViewModel.Validation.NEW_PASSWORD.name -> {
                    TextUtil.tILError(this, new_password_edt_til)
                    ErrorMessage.getInstance().showValidationMessage(this, new_password_edt, getString(it.msgId))
                }
                ChangePasswordViewModel.Validation.NEW_PASSWORD_INVALID.name -> {
                    TextUtil.tILError(this, new_password_edt_til)
                    ErrorMessage.getInstance().showValidationMessage(this, new_password_edt, getString(it.msgId))
                }
                ChangePasswordViewModel.Validation.REPEAT_PASSWORD.name -> {
                    TextUtil.tILError(this, repeat_password_edt_til)
                    ErrorMessage.getInstance().showValidationMessage(this, repeat_password_edt, getString(it.msgId))
                }
            }
        })
    }
    override fun inject() {
        super.inject()
        component.inject(this)
    }

    override fun setViewModel(): BaseViewModel {
        return viewModel
    }
    override fun layout(): Int {
        return R.layout.activity_change_password
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
                    this@ChangePasswordActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameAr + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            } else {
                ErrorMessage.getInstance().showSuccessGreen(
                    this@ChangePasswordActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameEn + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            }
        } else {
            ErrorMessage.getInstance()
                .showErrorYellow(this@ChangePasswordActivity, appointmentEvent.errorMSg)
        }
    }


    @OnClick(R.id.toolbar_left_iv)
    fun backPressed() {
        onBackPressed()
    }
    @OnClick(R.id.btnSave)
    fun  onSaveButtonClicked(){
        if (ConnectionUtil.isInetAvailable(this)) {
            hideKeyboard(repeat_password_edt)
            viewModel.changePassword(old_password_edt.text.toString(),new_password_edt.text.toString(),repeat_password_edt.text.toString())
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection))
        }
    }

    private fun configPasswordFields(){



            val filters = arrayOfNulls<InputFilter>(1)
            filters[0] = InputFilter { source, start, end, dest, dstart, dend ->
                if (end > start) {
                    val acceptedChars = charArrayOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '@', '.', '_', '#', '$', '%', '&', '*', '-', '+', '(', ')', '!', '"', '\'', ':',
                            ';', '/', '?', ',', '~', '`', '|', '\\', '^', '<', '>', '{', '}', '[', ']', '=', '£', '¥', '€', '.', '¢', '•', '©')
                    for (index in start until end) {
                        if (!String(acceptedChars).contains(source[index].toString())) {
                            return@InputFilter ""
                        }
                    }
                }
                null
            }
            old_password_edt.filters = filters
            new_password_edt.filters = filters
            repeat_password_edt.filters = filters



    }

    fun setLocaleLanguage(language: String?) {
        val locale = Locale(language!!)
        Locale.setDefault(locale)
        val configuration: Configuration = baseContext.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        baseContext.resources
                .updateConfiguration(configuration, baseContext.resources.displayMetrics)
    }

    companion object{

        fun getLaunchIntent(context: Context):Intent{
                return Intent(context,ChangePasswordActivity::class.java)
        }
    }
}