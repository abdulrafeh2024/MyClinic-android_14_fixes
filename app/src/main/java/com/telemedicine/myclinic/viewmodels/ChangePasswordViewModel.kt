package com.telemedicine.myclinic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.telemedicine.myclinic.base.BaseViewModel
import com.telemedicine.myclinic.eenum.Success
import com.telemedicine.myclinic.models.MobileOpResult
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.networks.rest.services.ProfileServices
import com.telemedicine.myclinic.networks.rest.services.request.ChangePasswordRequest
import com.telemedicine.myclinic.util.Const
import com.telemedicine.myclinic.util.TinyDB
import java.util.regex.Pattern
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(private val tinyDB: TinyDB, private val profileServices: ProfileServices) : BaseViewModel() {

    val validation by lazy {
        MutableLiveData<Validation>()
    }

    private val _localizeErrorMessage: MutableLiveData<MobileOpResult?> by lazy {
        MutableLiveData<MobileOpResult?>()
    }

    val localizeErrorMessage: LiveData<MobileOpResult?> = _localizeErrorMessage

    private val _changePasswordResponse: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val changePasswordResponse: LiveData<Boolean> = _changePasswordResponse

    fun changePassword(oldPassword: String, newPassword: String, repeatPassword: String) {

        val oRegId = tinyDB.getString(Const.OREGID_KEY)
        val securityToken = tinyDB.getString(Const.TOKEN_KEY)

        if (isPasswordNotEmpty(oldPassword) && isNewPasswordNotEmpty(newPassword) && isRepeatPasswordNotEmpty(newPassword, repeatPassword)) {

            _loading.postValue(true)
            compositeDisposable.add(
                    profileServices.changePassword(ChangePasswordRequest(securityToken, oldPassword, newPassword, oRegId))
                            .compose(applySchedulers())
                            .subscribe({mobileOpResult->
                                _loading.postValue(false)
                               if(mobileOpResult != null &&   mobileOpResult.result == Success.SUCCESSCODE.value){
                                   _changePasswordResponse.postValue(true)
                                }else{
                                   _changePasswordResponse.postValue(false)
                                    _localizeErrorMessage.postValue(mobileOpResult)
                                }
                            }, {
                                _changePasswordResponse.postValue(false)
                                _loading.postValue(false)
                                onHandleError(it)
                            })
            )
        }

    }

    private fun isPasswordNotEmpty(password: String): Boolean {

        return if (password.isNotEmpty() || password.isNotBlank()) {
            validation.postValue(Validation.RESET)
            true
        } else {
            validation.postValue(Validation.OLD_PASSWORD)
            false
        }
    }

    private fun isNewPasswordNotEmpty(newPassword: String): Boolean {

        val regex = "(.)*(\\d)(.)*";
        val pattern = Pattern.compile(regex);

        val matcher = pattern.matcher(newPassword)

        val isMatched = matcher.matches()



        return if (newPassword.isNotEmpty() || newPassword.isNotBlank()) {

            if(newPassword.length < 6 || !isMatched){
                validation.postValue(Validation.NEW_PASSWORD_INVALID)
                false
            }else{
                validation.postValue(Validation.RESET)
                true
            }

        } else {
            validation.postValue(Validation.NEW_PASSWORD)
            false
        }
    }


    private fun isRepeatPasswordNotEmpty(newPassword: String, repeatPassword: String): Boolean {
        return if (repeatPassword == newPassword) {
            validation.postValue(Validation.RESET)
            true
        } else {
            validation.postValue(Validation.REPEAT_PASSWORD)
            false
        }
    }


    enum class Validation(val msgId: Int) {
        OLD_PASSWORD(R.string.error_enter_password),
        NEW_PASSWORD(R.string.error_new_password),
        NEW_PASSWORD_INVALID(R.string.error_new_password_invalid),
        REPEAT_PASSWORD(R.string.error_repeat_password),
        RESET(0)
    }
}