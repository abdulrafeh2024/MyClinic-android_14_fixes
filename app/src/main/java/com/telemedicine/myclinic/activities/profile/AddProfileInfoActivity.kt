package com.telemedicine.myclinic.activities.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.DatePicker
import android.widget.ListAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.OnClick
import com.google.gson.Gson
import com.telemedicine.myclinic.activities.BaseActivity
import com.telemedicine.myclinic.activities.DashBoardActivity
import com.telemedicine.myclinic.adapters.SpinnerCustomAdapter
import com.telemedicine.myclinic.base.BaseViewModel
import com.telemedicine.myclinic.injection.ViewModelFactory
import com.telemedicine.myclinic.models.patientMiniModels.PatientsMiniModel
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.networks.rest.services.response.NonSaudiInfoResult
import com.telemedicine.myclinic.networks.rest.services.response.SaudiInfoResult
import com.telemedicine.myclinic.util.*
import com.telemedicine.myclinic.util.Const.*
import com.telemedicine.myclinic.viewmodels.ProfileRegistrationViewModel
import kotlinx.android.synthetic.main.activity_add_profile_info.*
import kotlinx.android.synthetic.main.layout_basic_profile_info.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddProfileInfoActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ProfileRegistrationViewModel
    var patientsMiniModelsIntent: java.util.ArrayList<PatientsMiniModel>? =  null
    var isFromRegister = false
    var isAddProfile = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileRegistrationViewModel::class.java]
        isAddProfile = intent.getBooleanExtra(Const.ADD_PATIENT, false)
        isFromRegister= intent.getBooleanExtra(Const.IS_FROM_REGISTER, false)

        viewModel.isAddProfile = isAddProfile
        viewModel.isFromRegister = isFromRegister
        populateYakeenData()
        populateTentative()
    }

    private fun populateTentative() {
        if (viewModel.isTentative() && !isFromRegister && !isAddProfile) {
            patientsMiniModelsIntent = intent.getParcelableArrayListExtra(EXISTING_PATIENT)
            val firstNameIntent = intent.getStringExtra(FIRST_NAME) ?: ""
            val lastNameIntent = intent.getStringExtra(LAST_NAME) ?: ""
            val middleNameIntent = intent.getStringExtra(MIDDLE_NAME) ?: ""
            val genderIntent = intent.getIntExtra(GENDER, 0)

            if (genderIntent != 0) {
                if (genderIntent == 1) {
                    gender_spinner.setText(getString(R.string.Male))
                } else {
                    gender_spinner.setText(getString(R.string.Female))
                }
            }

            if (TextUtil.getArabic(this)) {
                first_name_edt_ar.setText(firstNameIntent)
                middle_name_edt_ar.setText(middleNameIntent)
                last_name_edt_ar.setText(lastNameIntent)

                first_name_edt_til_ar.background = ContextCompat.getDrawable(this, R.drawable.grey_non_editable)
                middle_name_edt_til_ar.background = ContextCompat.getDrawable(this, R.drawable.grey_non_editable)
                last_name_edt_til_ar.background = ContextCompat.getDrawable(this, R.drawable.grey_non_editable)
            }else{
                first_name_edt.setText(firstNameIntent)
                middle_name_edt.setText(middleNameIntent)
                last_name_edt.setText(lastNameIntent)

                first_name_edt_til.background = ContextCompat.getDrawable(this, R.drawable.grey_non_editable)
                middle_name_edt_til.background = ContextCompat.getDrawable(this, R.drawable.grey_non_editable)
                last_name_edt_til.background = ContextCompat.getDrawable(this, R.drawable.grey_non_editable)
            }

            gender_til.background = ContextCompat.getDrawable(this, R.drawable.grey_non_editable)
            dateOfBirthEdt_til.background = ContextCompat.getDrawable(this, R.drawable.grey_non_editable)

            dateOfBirthEdt.isFocusable = false
            dateOfBirthEdt.isFocusableInTouchMode = false

            first_name_edt.isFocusable = false
            first_name_edt.isFocusableInTouchMode = false

            first_name_edt_ar.isFocusable = false
            first_name_edt_ar.isFocusableInTouchMode = false

            middle_name_edt.isFocusable = false
            middle_name_edt.isFocusableInTouchMode = false

            middle_name_edt_ar.isFocusable = false
            middle_name_edt_ar.isFocusableInTouchMode = false

            last_name_edt.isFocusable = false
            last_name_edt.isFocusableInTouchMode = false

            last_name_edt_ar.isFocusable = false
            last_name_edt_ar.isFocusableInTouchMode = false

            gender_spinner.isFocusable = false
            gender_spinner.isFocusableInTouchMode = false
            gender_spinner.setEditable(false)


        }
    }

    private fun populateYakeenData() {

        val gson = Gson()
        val isIqama = intent.getBooleanExtra(Const.IS_IQAMA, false)
        val birthdate = intent.getStringExtra(Const.YAKEEN_DATE)
        dateOfBirthEdt.setText(birthdate ?: "")

        if (isIqama) {
            val nonSaudi = gson.fromJson<NonSaudiInfoResult>(intent.getStringExtra(Const.NON_SAUDI), NonSaudiInfoResult::class.java)
            if (nonSaudi != null) {

                val isArabic: Boolean = TextUtil.getArabic(this)

                //gender
                if (nonSaudi?.gender != null) {

                    if (nonSaudi.gender == "M") {
                        gender_spinner.setText(getString(R.string.male))
                    } else {
                        gender_spinner.setText(getString(R.string.female))
                    }

                    /*   val dateOfBirth =  LocaleDateHelper.convertDateStringFormat(nonSaudi.dateOfBirthG,
                               LocaleDateHelper.DATE_PATTERN, LocaleDateHelper.DATE_PATTERN_2, Locale.ENGLISH)*/

                    if (isArabic) {
                        first_name_edt_ar.setText(nonSaudi.firstName ?: "")
                        middle_name_edt_ar.setText(nonSaudi.secondName ?: "")
                        last_name_edt_ar.setText(nonSaudi.lastName ?: "")

                        viewModel.firstName = nonSaudi.englishFirstName
                        viewModel.middleName = nonSaudi.englishSecondName
                        viewModel.lastName = nonSaudi.englishLastName

                    } else {
                        first_name_edt.setText(nonSaudi.englishFirstName ?: "")
                        middle_name_edt.setText(nonSaudi.englishSecondName ?: "")
                        last_name_edt.setText(nonSaudi.englishLastName ?: "")

                        viewModel.firstNameAr = nonSaudi.firstName
                        viewModel.middleNameAr = nonSaudi.secondName
                        viewModel.lastNameAr = nonSaudi.lastName
                    }
                }
            }
        } else {
            val saudiInfo = gson.fromJson<SaudiInfoResult>(intent.getStringExtra(Const.SAUDI), SaudiInfoResult::class.java)
            if (saudiInfo != null) {

                val isArabic: Boolean = TextUtil.getArabic(this)

                //gender
                if (saudiInfo?.gender != null) {

                    if (saudiInfo.gender == "M") {
                        gender_spinner.setText(getString(R.string.male))
                    } else {
                        gender_spinner.setText(getString(R.string.female))
                    }

                    /*  val dateOfBirth =  LocaleDateHelper.convertDateStringFormat(saudiInfo.dateOfBirthH,
                              LocaleDateHelper.DATE_PATTERN, LocaleDateHelper.DATE_PATTERN_2, Locale.ENGLISH)

                      dateOfBirthEdt.setText(dateOfBirth?:"")*/

                    if (isArabic) {
                        first_name_edt_ar.setText(saudiInfo.firstName)
                        middle_name_edt_ar.setText(saudiInfo.fatherName)
                        last_name_edt_ar.setText(saudiInfo.familyName)

                        viewModel.firstName = saudiInfo.englishFirstName
                        viewModel.middleName = saudiInfo.englishSecondName
                        viewModel.lastName = saudiInfo.englishLastName

                    } else {
                        first_name_edt.setText(saudiInfo.englishFirstName ?: "")
                        middle_name_edt.setText(saudiInfo.englishSecondName ?: "")
                        last_name_edt.setText(saudiInfo.englishLastName ?: "")

                        viewModel.firstNameAr = saudiInfo.firstName
                        viewModel.middleNameAr = saudiInfo.fatherName
                        viewModel.lastNameAr = saudiInfo.familyName
                    }
                }
            }
        }

    }

    override fun inject() {
        super.inject()
        app.component.inject(this)
    }

    override fun layout(): Int {
        return R.layout.activity_add_profile_info
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
                    this@AddProfileInfoActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameAr + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            } else {
                ErrorMessage.getInstance().showSuccessGreen(
                    this@AddProfileInfoActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameEn + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            }
        } else {
            ErrorMessage.getInstance()
                .showErrorYellow(this@AddProfileInfoActivity, appointmentEvent.errorMSg)
        }
    }

    override fun setViewModel(): BaseViewModel {
        return viewModel

    }

    override fun onPause() {
        super.onPause()
        viewModel.clearErrorMessage()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        transformView()
        if(!viewModel.isTentative() || isAddProfile || isFromRegister){
            setViewInfo()
        }

        viewModel.resultProfileCreate.observe(this, Observer { success ->
            if (success) {
                if (viewModel.isAddPatient) {
                    setResult(Activity.RESULT_OK, Intent())
                    finish()
                } else {
                    val intent = Intent(this@AddProfileInfoActivity, DashBoardActivity::class.java)
                    intent.putExtra(Const.EXISTING_PATIENT, patientsMiniModelsIntent)
                    viewModel.patientsMiniModel?.let { patientsMiniModel ->
                        val patientsMiniModels = java.util.ArrayList<PatientsMiniModel>()
                        patientsMiniModels.add(patientsMiniModel)
                        intent.putExtra(Const.EXISTING_PATIENT, patientsMiniModels)
                        intent.putExtra("profile_update", true)

                        if (!viewModel.isTentative() && !isAddProfile && !isFromRegister) {
                            intent.putExtra("profile_update_tent", true)
                        }
                    }
                    viewModel.clearSelectedData()
                    viewModel.clearInsuranceData()
                    startActivity(intent)
                    setResult(RESULT_OK)
                    finish()
                }
            }
        })

        viewModel.localizeErrorMessage.observe(this, Observer { mobileOpResult ->
            var errorMesg = ""
            mobileOpResult?.let {
                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.businessErrorMessageEn)) {
                    if (TextUtil.getEnglish(this@AddProfileInfoActivity)) errorMesg = mobileOpResult.businessErrorMessageEn + "\n" else if (TextUtil.getArabic(this@AddProfileInfoActivity)) errorMesg = mobileOpResult.businessErrorMessageAr + "\n"
                }

                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.technicalErrorMessage)) {
                    errorMesg = """
                    ${errorMesg}Technical Info : 
                    ${mobileOpResult.technicalErrorMessage}
                    """.trimIndent()
                }
                if(it.result == 600 ||it.result == 700){
                    ErrorMessage.getInstance().showErrorYellow(this@AddProfileInfoActivity, errorMesg)
                }else{
                    ErrorMessage.getInstance().showError(this@AddProfileInfoActivity, errorMesg)
                }
            }

        })
    }

    fun transformView() {

        if (TextUtil.getArabic(this)) {
            toolbar_left_iv.setRotation(180f)
            gender_spinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0)

            citySpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0)
            districtSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0)
            btnSave.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0)
            dateOfBirthEdt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0)
            first_name_edt_til.visibility = View.GONE
            middle_name_edt_til.visibility = View.GONE
            last_name_edt_til.visibility = View.GONE
            first_name_edt_til_ar.visibility = View.VISIBLE
            middle_name_edt_til_ar.visibility = View.VISIBLE
            last_name_edt_til_ar.visibility = View.VISIBLE

        } else {
            first_name_edt_til.visibility = View.VISIBLE
            middle_name_edt_til.visibility = View.VISIBLE
            last_name_edt_til.visibility = View.VISIBLE
            first_name_edt_til_ar.visibility = View.GONE
            middle_name_edt_til_ar.visibility = View.GONE
            last_name_edt_til_ar.visibility = View.GONE

        }
    }

    @OnClick(R.id.toolbar_left_iv)
    fun backPressed() {
        onBackPressed()
    }

    @OnClick(R.id.dateOfBirthEdt1)
    fun datePicker() {

        if(viewModel.isTentative() && !isAddProfile && !isFromRegister){
            return
        }

        dateOfBirthEdt_til.requestFocus()

        val datePicker = DatePickerDialog(this, this, viewModel.calenderDateOfBirth
                .get(Calendar.YEAR), viewModel.calenderDateOfBirth.get(Calendar.MONTH),
                viewModel.calenderDateOfBirth.get(Calendar.DAY_OF_MONTH))
        datePicker.datePicker.maxDate = System.currentTimeMillis()
        datePicker.setCanceledOnTouchOutside(false)
        datePicker.show()
    }

    @OnClick(R.id.btnSave)
    fun onSaveProfile() {

        val gender: String = gender_spinner.getText().toString()

        var firstName: String = ""
        var middleName: String = ""
        var lastName: String = ""

        if (TextUtil.getArabic(this)) {
            firstName = first_name_edt_ar.getText().toString()
            middleName = middle_name_edt_ar.getText().toString()
            lastName = last_name_edt_ar.getText().toString()

            viewModel.firstNameAr = firstName
            viewModel.lastNameAr = lastName
            viewModel.middleNameAr = middleName

        }else{
             firstName = first_name_edt.getText().toString()
             middleName = middle_name_edt.getText().toString()
             lastName = last_name_edt.getText().toString()

            viewModel.firstName = firstName
            viewModel.lastName = lastName
            viewModel.middleName = middleName

        }

//        viewModel.firstName = firstName
//        viewModel.lastName = lastName
//        viewModel.middleName = middleName


        var dob = dateOfBirthEdt.text.toString()

        if (ValidationHelper.isNullOrEmpty(gender)) {
            TextUtil.tILError(this, gender_til, gender_spinner)
        }

        if (ValidationHelper.isNullOrEmpty(firstName)) {
            if (TextUtil.getArabic(this))
                TextUtil.tILError(this, first_name_edt_til_ar)
            else
                TextUtil.tILError(this, first_name_edt_til)
        }
        if (ValidationHelper.isNullOrEmpty(middleName)) {
            if (TextUtil.getArabic(this)) TextUtil.tILError(this, middle_name_edt_til_ar)
            else
                TextUtil.tILError(this, middle_name_edt_til)
        }
        if (ValidationHelper.isNullOrEmpty(lastName)) {
            if (TextUtil.getArabic(this)) TextUtil.tILError(this, last_name_edt_til_ar)
            else
                TextUtil.tILError(this, last_name_edt_til)
        }

        if (ValidationHelper.isNullOrEmpty(dob)) {
            TextUtil.tILError(this, dateOfBirthEdt_til, dateOfBirthEdt)
        }
        if (ValidationHelper.isNullOrEmpty(gender)) {
            ErrorMessage.getInstance().showValidationMessage(this, gender_spinner, getString(R.string.error_empty_fields))
            return
        } else if (ValidationHelper.isNullOrEmpty(firstName)) {
            ErrorMessage.getInstance().showValidationMessage(this, first_name_edt, getString(R.string.error_empty_fields))
            return
        } else if (ValidationHelper.isNullOrEmpty(middleName)) {
            ErrorMessage.getInstance().showValidationMessage(this, middle_name_edt, getString(R.string.error_empty_fields))
            return
        } else if (ValidationHelper.isNullOrEmpty(lastName)) {
            ErrorMessage.getInstance().showValidationMessage(this, last_name_edt, getString(R.string.error_empty_fields))
            return
        } else if (ValidationHelper.isNullOrEmpty(dob)) {
            ErrorMessage.getInstance().showValidationMessage(this, dateOfBirthEdt, getString(R.string.error_empty_fields))
            return
        }

        dob = LocaleDateHelper.convertDateStringFormat(dob, LocaleDateHelper.DATE_PATTERN_2, "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", Locale.ENGLISH)

        viewModel.birthDate = dob

        if (gender.trim { it <= ' ' }.equals(getString(R.string.male).trim { it <= ' ' }, ignoreCase = true))
            viewModel.gender = "1"
        else if (gender.trim { it <= ' ' }.equals(getString(R.string.female).trim { it <= ' ' }, ignoreCase = true))
            viewModel.gender = "2"

        if (ConnectionUtil.isInetAvailable(this)) {
            viewModel.createPatientProfile()
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection))
        }

    }

    private fun setViewInfo() {
        val genderArr = resources.getStringArray(R.array.gender)
        val genderArrList = ArrayList(listOf(*genderArr))
        val genderAdapter: ListAdapter = SpinnerCustomAdapter(this, R.layout.spinner_item_list, genderArrList)
        gender_spinner.setAdapter(genderAdapter)
    }

    companion object {

        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, AddProfileInfoActivity::class.java)
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {

        viewModel.calenderDateOfBirth.set(Calendar.YEAR, year)
        viewModel.calenderDateOfBirth.set(Calendar.MONTH, monthOfYear)
        viewModel.calenderDateOfBirth.set(Calendar.DAY_OF_MONTH, dayOfMonth)


        val sdf = SimpleDateFormat(LocaleDateHelper.DATE_PATTERN_2, Locale.getDefault())
        dateOfBirthEdt.setText(sdf.format(viewModel.calenderDateOfBirth.time))
    }
}