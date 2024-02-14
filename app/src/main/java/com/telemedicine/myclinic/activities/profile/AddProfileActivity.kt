package com.telemedicine.myclinic.activities.profile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.ListAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionManager
import butterknife.OnClick
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.raywenderlich.android.validatetor.ValidateTor
import com.reginald.editspinner.EditSpinner.ItemConverter
import com.telemedicine.myclinic.activities.BaseActivity
import com.telemedicine.myclinic.activities.DashBoardActivity
import com.telemedicine.myclinic.adapters.SpinnerCustomAdapter
import com.telemedicine.myclinic.base.BaseViewModel
import com.telemedicine.myclinic.extensions.hideKeyboard
import com.telemedicine.myclinic.fragments.CountrySelectionFragment
import com.telemedicine.myclinic.injection.ViewModelFactory
import com.telemedicine.myclinic.models.patientMiniModels.PatientsMiniModel
import com.telemedicine.myclinic.models.profilecreatoinmodels.Nationalities
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.util.*
import com.telemedicine.myclinic.util.Const.*
import com.telemedicine.myclinic.util.LocaleDateHelper.DATE_PATTERN
import com.telemedicine.myclinic.util.LocaleDateHelper.DATE_PATTERN_2
import com.telemedicine.myclinic.viewmodels.ProfileRegistrationViewModel
import kotlinx.android.synthetic.main.activity_add_insurance_profile.*
import kotlinx.android.synthetic.main.activity_add_profile.*
import kotlinx.android.synthetic.main.activity_add_profile.dateOfBirthEdt_til
import kotlinx.android.synthetic.main.activity_add_profile.insuranceCardImg
import kotlinx.android.synthetic.main.activity_add_profile.insuranceExpiryDate
import kotlinx.android.synthetic.main.activity_add_profile.insurance_carrier_spiiner
import kotlinx.android.synthetic.main.activity_add_profile.insurance_id_icon
import kotlinx.android.synthetic.main.activity_add_profile.next_btn
import kotlinx.android.synthetic.main.layout_basic_profile_info.*
import kotlinx.android.synthetic.main.toolbar.*
import net.alhazmy13.hijridatepicker.date.gregorian.GregorianDatePickerDialog
import net.alhazmy13.hijridatepicker.date.hijri.HijriDatePickerDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rebus.permissionutils.PermissionEnum
import rebus.permissionutils.PermissionUtils
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class AddProfileActivity : BaseActivity(), GregorianDatePickerDialog.OnDateSetListener, HijriDatePickerDialog.OnDateSetListener,
        CountrySelectionFragment.OnCountrySelectListener, DatePickerDialog.OnDateSetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ProfileRegistrationViewModel

    private val RC_ADD_PATIENT = 921
    private var isFormLaoded = false
    private var arr: Array<String>? = null
    private var str = ""
    private var errorEnable = false
    var insuranceCardBase64: String? = ""
    var nationalCardBase64: String? = ""

    var firstNameIntent = ""
    var lastNameIntent = ""
    var middleNameIntent = ""
    var genderIntent = 0
    var dateOfBirthIntent = ""
    var isFromDashboard = false
    var loadTentativeProfile = false
    var isFromRegister = false
   var isAddProfile = false
    var patientsMiniModels: ArrayList<PatientsMiniModel>? =  null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutExpiry.visibility = View.GONE
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileRegistrationViewModel::class.java]
        isFromRegister= intent.getBooleanExtra(Const.IS_FROM_REGISTER, false)
        firstNameIntent = intent.getStringExtra(FIRST_NAME) ?: ""
        lastNameIntent = intent.getStringExtra(LAST_NAME) ?: ""
        middleNameIntent = intent.getStringExtra(MIDDLE_NAME) ?: ""
        genderIntent = intent.getIntExtra(GENDER, 0)
        dateOfBirthIntent = intent.getStringExtra(DATE_OF_BIRTH) ?: ""
        isFromDashboard = intent.getBooleanExtra(IS_FROM_DASHBOARD, false)
        loadTentativeProfile = intent.getBooleanExtra(LOAD_TENTATIVE_PROFILE, false)
        patientsMiniModels = intent.getParcelableArrayListExtra(EXISTING_PATIENT)
        isAddProfile = intent.getBooleanExtra(Const.ADD_PATIENT, false)


            handleTentativePatient()


        if (isFromRegister) {
            viewModel.clearSelectedData()
            viewModel.clearInsuranceData()
        }

        viewModel.loadRefPatientProfile()


        if(loadTentativeProfile){
            if (ConnectionUtil.isInetAvailable(this)) {
                viewModel.loadPatientProfileById()
            } else {
                ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection))
            }
        }
    }

    private fun handleTentativePatient() {

        if(!isAddProfile && !isFromRegister){
            if (dateOfBirthIntent.isNotEmpty()) {

                var dobIntent = LocaleDateHelper.convertDateStringFormat(dateOfBirthIntent, "yyyy-MM-dd'T'hh:mm:ss", LocaleDateHelper.DATE_PATTERN_2, Locale.getDefault())
                dateOfBirthEdtYakeen.setText(dobIntent)
                viewModel.yakeenDateOfBirth = (dateOfBirthIntent.substring(0, 10))
            }

            if (viewModel.isTentative()) {
                dateOfBirthEdtYakeen.isFocusable = false
                dateOfBirthEdtYakeen.isFocusableInTouchMode = false
                dateOfBirthEdt_til.background = ContextCompat.getDrawable(this, R.drawable.grey_non_editable)

                if (isFromDashboard) {
                    skip_btn.visibility = View.GONE
                } else {
                    skip_btn.visibility = View.VISIBLE
                }
            }
        }

    }

    override fun inject() {
        super.inject()
        component.inject(this)
    }

    override fun layout(): Int {
        return R.layout.activity_add_profile
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
                    this@AddProfileActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameAr + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            } else {
                ErrorMessage.getInstance().showSuccessGreen(
                    this@AddProfileActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameEn + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            }
        } else {
            ErrorMessage.getInstance()
                .showErrorYellow(this@AddProfileActivity, appointmentEvent.errorMSg)
        }
    }

    override fun setViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onPause() {
        super.onPause()
        viewModel.clearErrorMessage()
    }

    var isFromBasicInfo = false
    override fun onResume() {
        super.onResume()

        if (isFromBasicInfo) {
            isFromBasicInfo = false
            return
        }
        viewModel.localizeErrorMessage.observe(this, Observer { mobileOpResult ->
            var errorMesg = ""
            mobileOpResult?.let {
                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.businessErrorMessageEn)) {
                    if (TextUtil.getEnglish(this@AddProfileActivity)) errorMesg = mobileOpResult.businessErrorMessageEn + "\n" else if (TextUtil.getArabic(this@AddProfileActivity)) errorMesg = mobileOpResult.businessErrorMessageAr + "\n"
                }

                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.technicalErrorMessage)) {
                    errorMesg = """
                    ${errorMesg}Technical Info : 
                    ${mobileOpResult.technicalErrorMessage}
                    """.trimIndent()
                }
                if(it.result == 600 ||it.result == 700){
                    ErrorMessage.getInstance().showErrorYellow(this@AddProfileActivity, errorMesg)
                }else{
                    ErrorMessage.getInstance().showError(this@AddProfileActivity, errorMesg)
                }
            }
        })

        viewModel.yakeenErrorMessage.observe(this, Observer { mobileOpResult ->

           /* mobileOpResult.let {
                if(!it?.technicalErrorMessage.isNullOrEmpty()){
                    ErrorMessage.getInstance().showError(this@AddProfileActivity, errorMsg)
                }
            }*/

        })

        viewModel.resultProfileRef.observe(this, Observer {

            viewModel.nationalitiesList?.let {

                val nationalityList = ArrayList<String>()

                for (cities in it) {
                    var city = ""
                    if (TextUtil.getEnglish(this@AddProfileActivity)) city = cities.nameEn else if (TextUtil.getArabic(this@AddProfileActivity)) city = cities.nameAr
                    nationalityList.add(city)
                }
                //val cityAdapter: ListAdapter = SpinnerCustomAdapter(this, R.layout.spinner_item_list, nationalityList)
                // nationality.setAdapter(cityAdapter)
                if (!isFormLaoded) {
                    init()
                    isFormLaoded = true
                }
            }
        })

        viewModel.resultProfileRefById.observe(this, Observer {
            if (it?.patientRegistrationUpdate != null ) {

                viewModel.setTentative(true)
                firstNameIntent = it.patientRegistrationUpdate.firstName
                lastNameIntent = it.patientRegistrationUpdate.lastName
                middleNameIntent = it.patientRegistrationUpdate.middleName
                genderIntent = it.patientRegistrationUpdate.gender
                dateOfBirthIntent = it.patientRegistrationUpdate.birthDate

                handleTentativePatient()
            }
        })


        viewModel.resultProfileValidate.observe(this, Observer {
            TransitionManager.beginDelayedTransition(rooViewLayout)
            if (viewModel.hasProfileValidate) {
                if (layoutExpiry.visibility != View.VISIBLE) {
                    layoutExpiry.visibility = View.VISIBLE
                    skip_btn.visibility = View.GONE
                } else {
                    onNextButtonClicked()
                }

            } else {
                layoutExpiry.visibility = View.GONE
            }
        })

        viewModel.resultIntelliSearch.observe(this, Observer {
            if (it) {
                if (viewModel.NonSaudiInfoResult != null) {
                    if (viewModel.NonSaudiInfoResult?.iqamaExpiryDateG != null) {
                        if (viewModel.NonSaudiInfoResult?.iqamaExpiryDateG != null) {
                            val expiryDate = LocaleDateHelper.convertDateStringFormat(viewModel.NonSaudiInfoResult?.iqamaExpiryDateG,
                                    DATE_PATTERN, DATE_PATTERN_2, Locale.ENGLISH)
                            expiryDateEdt.setText(expiryDate ?: "")
                        }

                    }
                }
            }
        })

        viewModel.resultIntelliSearchSaudi.observe(this, Observer {
            if (it) {
                if (viewModel.saudiInfoResult != null) {
                }
            }
        })
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

    override fun onDateSet(p0: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {

        viewModel.calenderDateOfBirth.set(Calendar.YEAR, year)
        viewModel.calenderDateOfBirth.set(Calendar.MONTH, monthOfYear)
        viewModel.calenderDateOfBirth.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val yakeenSdf = SimpleDateFormat(LocaleDateHelper.LOCAL_DATE_PATTERN, Locale.getDefault())
        val sdf = SimpleDateFormat(LocaleDateHelper.DATE_PATTERN_2, Locale.getDefault())
        dateOfBirthEdtYakeen.setText(sdf.format(viewModel.calenderDateOfBirth.time))
        //viewModel.yakeenDateOfBirth = yakeenSdf.format(viewModel.calenderDateOfBirth.time)
        viewModel.yakeenDateOfBirth = "${year}-${monthOfYear + 1}-${dayOfMonth}"
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        transformView()
        radioGroupCalender.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                R.id.radioHijri -> {
                    viewModel.isHijriCalender = true
                }
                R.id.radioGeorgian -> {
                    viewModel.isHijriCalender = false
                }
            }

        }
    }

    @OnClick(R.id.toolbar_left_iv)
    fun backPressed() {
        viewModel.clearSelectedData()
        viewModel.clearInsuranceData()
        onBackPressed()
    }

    private fun fillNationalId() {
        arr = resources.getStringArray(R.array.Betaqa)
        val nationaId = ArrayList(Arrays.asList(*arr!!)) //new ArrayList is only needed if you absolutely need an ArrayList
        if (!ValidationHelper.isNullOrEmpty(nationaId)) {
            nationalIdType.setAdapter(SpinnerCustomAdapter(this@AddProfileActivity, R.layout.spinner_item_list, nationaId))
        }
    }

    fun transformView() {
        if (TextUtil.getArabic(this)) {
            toolbar_left_iv.setRotation(180f)
            nationality.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0)
            nationalIdType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0)
            next_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0)
            skip_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0)
            expiryDateEdt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0)
            insuranceExpiryDate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0)
            national_id_attach.setCompoundDrawablesWithIntrinsicBounds(R.drawable.attachment, 0, 0, 0)
            insurance_id_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.attachment, 0, 0, 0)
            insurance_carrier_spiiner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0)
        }
    }

    fun init() {

        val intent = intent
        if (intent != null) {
            viewModel.isAddPatient = intent.getBooleanExtra(Const.ADD_PATIENT, false)

            // if (viewModel.isAddPatient) profile_title.visibility = View.GONE
        }
        fillNationalId()
        //set default values
        // nationality.selectItem(0)
        nationality.setText(getString(R.string.KSA).trim { it <= ' ' }, TextView.BufferType.EDITABLE)

        nationalIdType.selectItem(0)
        nationalID.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        val filters = arrayOfNulls<InputFilter>(2)
        nationalID.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        filters[0] = InputFilter { source, start, end, dest, dstart, dend ->
            if (end > start) {
                val acceptedChars = charArrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0')
                for (index in start until end) {
                    if (!String(acceptedChars).contains(source[index].toString())) {
                        return@InputFilter ""
                    }
                }
            }
            null
        }
        filters[1] = InputFilter.LengthFilter(10)
        nationalID.filters = filters
        nationalIdType.setItemConverter(ItemConverter { selectedItem ->
            str = selectedItem.toString().trim { it <= ' ' } as String
            if (str.equals(resources.getString(R.string.Iqama).trim { it <= ' ' }, ignoreCase = true) ||
                    str.equals(resources.getString(R.string.betaqa).trim { it <= ' ' }, ignoreCase = true)) {

                nationalID.setText("", TextView.BufferType.EDITABLE)
                viewModel.resetNationalIdLayout()
                nationalID.setRawInputType(InputType.TYPE_CLASS_NUMBER)
                val filters2 = arrayOfNulls<InputFilter>(2)
                filters2[0] = InputFilter.LengthFilter(10)
                filters2[1] = InputFilter { source, start, end, dest, dstart, dend ->
                    if (end > start) {
                        val acceptedChars = charArrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0')
                        for (index in start until end) {
                            if (!String(acceptedChars).contains(source[index].toString())) {
                                return@InputFilter ""
                            }
                        }
                    }
                    null
                }
                nationalID.filters = filters2

            } else {
                nationalID.setText("", TextView.BufferType.EDITABLE)
                viewModel.resetNationalIdLayout()
                nationalID.setRawInputType(InputType.TYPE_CLASS_TEXT)
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
                nationalID.filters = filters
            }
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(nationalID.windowToken, 0)
            str
        })
    }

    private fun getNationalityId(countryName: String): Long {
        var nationalityId: Long = 0
        if (!ValidationHelper.isNullOrEmpty(viewModel.nationalitiesList)) {

            for (nationalities in viewModel.nationalitiesList!!) {
                var nationalitiesNameEn = nationalities.nameEn
                if (TextUtil.getEnglish(this)) nationalitiesNameEn = nationalities.nameEn else if (TextUtil.getArabic(this)) nationalitiesNameEn = nationalities.nameAr
                if (nationalitiesNameEn.trim { it <= ' ' }.equals(countryName.trim { it <= ' ' }, ignoreCase = true)) {
                    nationalityId = nationalities.id
                }
            }
        }
        return nationalityId
    }

    @OnClick(R.id.nationality)
    fun onNationalityClicked() {

        viewModel.nationalitiesList?.let {
            CountrySelectionFragment.newInstance(viewModel.nationalitiesList!!).show(supportFragmentManager, "CountrySelectionFragment")
        }

    }

    @OnClick(R.id.skip_btn)
    fun onSkipButtonClicked() {
        val intent = Intent(this, DashBoardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(EXISTING_PATIENT, patientsMiniModels)
        startActivity(intent)
        setResult(RESULT_OK)
        finish()
    }

    var isIqama = false

    var previousNationalId = ""

    @OnClick(R.id.next_btn)
    fun onNextButtonClicked() {

        val validateTor = ValidateTor()
        val nationalIdTypeSpinner = nationalIdType
        val nationalitySpinner = nationality
        val nationality: String = nationalitySpinner.text.toString()
        viewModel.nationality = nationalitySpinner.text.toString()
        // val nationalIdType: String = nationalIdTypeSpinner.getText().toString()
        val nationalIdType = nationalIdTypeSpinner.text.toString()
        // viewModel.nationalIdType = nationalIdTypeSpinner.getText().toString()
        //viewModel.nationalIdType//only for nationa id number

        // val myFormat = "MM/dd/yyyy" //In which you need put here
        when {
            nationalIdType.trim { it <= ' ' }.equals(getString(R.string.betaqa).trim { it <= ' ' }, ignoreCase = true) -> {
                isIqama = false
                viewModel.nationalIdType = "Bataqa"
                viewModel.nationalIdTypeInt = "1"

            }
            nationalIdType.trim { it <= ' ' }.equals(getString(R.string.Iqama).trim { it <= ' ' }, ignoreCase = true) -> {
                isIqama = true
                viewModel.nationalIdType = "Iqama"
                viewModel.nationalIdTypeInt = "2"
            }
            nationalIdType.trim { it <= ' ' }.equals(getString(R.string.Passport).trim { it <= ' ' }, ignoreCase = true) -> {
                isIqama = true
                viewModel.nationalIdType = "Passport"
                viewModel.nationalIdTypeInt = "3"
            }


            // val nationalyIdNumber = nationalID.text.toString()
        }
        nationality?.let {
            viewModel.country = getNationalityId(it).toString()
            viewModel.nationality = getNationalityId(it).toString()
        }

        val nationalTypeId: String = nationalIdTypeSpinner.getText().toString()

        // val nationalyIdNumber = nationalID.text.toString()
        viewModel.nationalId = nationalID.text.toString()

        var expiryDate = if (viewModel.isHijriCalender) {


            // val myFormat = "MM/dd/yyyy" //In which you need put here

            val sdf = SimpleDateFormat(DATE_PATTERN_2, Locale.getDefault())

            sdf.format(viewModel.calenderHijri.time)

        } else {
            expiryDateEdt.text.toString()
        }

        if (viewModel.hasProfileValidate && previousNationalId == nationalID.text.toString()) {
            if (ValidationHelper.isNullOrEmpty(expiryDate)) {
                ErrorMessage.getInstance().showValidationMessage(this, expiryDateEdt, getString(R.string.error_empty_fields))
                return
            }

            expiryDate = LocaleDateHelper.convertDateStringFormat(expiryDate, DATE_PATTERN_2, "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", Locale.ENGLISH)
            viewModel.nationalIdExpiryDate = expiryDate

            if (!viewModel.isTentative()  || isFromRegister || isAddProfile) {
                DialogUtils.showDialog(this,
                        getString(R.string.app_name),
                        getString(R.string.text_insurance_confirmation),
                        getString(R.string.yes),
                        getString(R.string.no),
                        {
                            isFromBasicInfo = true
                            startActivityForResult(AddInsuranceProfileActivity.getLaunchIntent(this), RC_ADD_PATIENT)
                        },
                        {
                            val intent = Intent(this, AddProfileInfoActivity::class.java)
                            intent.putExtra(YAKEEN_DATE, dateOfBirthEdtYakeen.text.toString())
                            intent.putExtra(EXISTING_PATIENT, patientsMiniModels)
                            intent.putExtra(FIRST_NAME, firstNameIntent)
                            intent.putExtra(LAST_NAME, lastNameIntent)
                            intent.putExtra(Const.ADD_PATIENT, isAddProfile);
                            intent.putExtra(Const.IS_FROM_REGISTER, isFromRegister);
                            intent.putExtra(MIDDLE_NAME, middleNameIntent)
                            intent.putExtra(GENDER, genderIntent)

                            val gson = Gson()
                            isFromBasicInfo = true
                            if (isIqama) {
                                if (viewModel.NonSaudiInfoResult != null) {
                                    intent.putExtra(NON_SAUDI, gson.toJson(viewModel.NonSaudiInfoResult))
                                    intent.putExtra(IS_IQAMA, isIqama)
                                }
                            } else {
                                if (viewModel.saudiInfoResult != null) {
                                    intent.putExtra(SAUDI, gson.toJson(viewModel.saudiInfoResult))
                                    intent.putExtra(IS_IQAMA, isIqama)
                                }
                            }

                            startActivityForResult(intent, RC_ADD_PATIENT)
                            //  startActivityForResult(AddProfileInfoActivity.getLaunchIntent(this), RC_ADD_PATIENT)

                        })

           /*     val intent = Intent(this, AddProfileInfoActivity::class.java)
                intent.putExtra(YAKEEN_DATE, dateOfBirthEdtYakeen.text.toString())
                intent.putExtra(EXISTING_PATIENT, patientsMiniModels)
                intent.putExtra(FIRST_NAME, firstNameIntent)
                intent.putExtra(LAST_NAME, lastNameIntent)
                intent.putExtra(Const.ADD_PATIENT, isAddProfile);
                intent.putExtra(Const.IS_FROM_REGISTER, isFromRegister);
                intent.putExtra(MIDDLE_NAME, middleNameIntent)
                intent.putExtra(GENDER, genderIntent)

                val gson = Gson()
                isFromBasicInfo = true
                if (isIqama) {
                    if (viewModel.NonSaudiInfoResult != null) {
                        intent.putExtra(NON_SAUDI, gson.toJson(viewModel.NonSaudiInfoResult))
                        intent.putExtra(IS_IQAMA, isIqama)
                    }
                } else {
                    if (viewModel.saudiInfoResult != null) {
                        intent.putExtra(SAUDI, gson.toJson(viewModel.saudiInfoResult))
                        intent.putExtra(IS_IQAMA, isIqama)
                    }
                }

                startActivityForResult(intent, RC_ADD_PATIENT)*/
                //  startActivityForResult(AddProfileInfoActivity.getLaunchIntent(this), RC_ADD_PATIENT)
            } else {
                val intent = Intent(this, AddProfileInfoActivity::class.java)
                intent.putExtra(YAKEEN_DATE, dateOfBirthEdtYakeen.text.toString())

                intent.putExtra(FIRST_NAME, firstNameIntent)
                intent.putExtra(LAST_NAME, lastNameIntent)
                intent.putExtra(MIDDLE_NAME, middleNameIntent)
                intent.putExtra(GENDER, genderIntent)
                intent.putExtra(Const.ADD_PATIENT, isAddProfile);
                intent.putExtra(Const.IS_FROM_REGISTER, isFromRegister);

                val gson = Gson()
                isFromBasicInfo = true
                if (isIqama) {
                    if (viewModel.NonSaudiInfoResult != null) {
                        intent.putExtra(NON_SAUDI, gson.toJson(viewModel.NonSaudiInfoResult))
                        intent.putExtra(IS_IQAMA, isIqama)
                    }
                } else {
                    if (viewModel.saudiInfoResult != null) {
                        intent.putExtra(SAUDI, gson.toJson(viewModel.saudiInfoResult))
                        intent.putExtra(IS_IQAMA, isIqama)
                    }
                }

                startActivityForResult(intent, RC_ADD_PATIENT)
            }

            hideKeyboard(nationalIdTypeSpinner)
        } else {
            previousNationalId = viewModel.nationalId ?: ""
            if (ValidationHelper.isNullOrEmpty(nationality)) {
                TextUtil.tILError(this, nationality_til, nationalitySpinner)
            }

            if (ValidationHelper.isNullOrEmpty(nationalTypeId)) {
                TextUtil.tILError(this, nationalIdType_til, nationalIdTypeSpinner)
                errorEnable = true
            }

            if (ValidationHelper.isNullOrEmpty(viewModel.nationalId)) {
                TextUtil.tILError(this, nationalIDTIL, nationalID)
            }

            if (ValidationHelper.isNullOrEmpty(nationality)) {
                ErrorMessage.getInstance().showValidationMessage(this, nationalitySpinner, getString(R.string.error_empty_fields))
                return
            } else if (ValidationHelper.isNullOrEmpty(nationalTypeId)) {
                ErrorMessage.getInstance().showValidationMessage(this, nationalIdTypeSpinner, getString(R.string.error_empty_fields))
                return
            } else if (ValidationHelper.isNullOrEmpty(viewModel.nationalId)) {
                ErrorMessage.getInstance().showValidationMessage(this, nationalID, getString(R.string.error_empty_fields))
                return
            } else if (viewModel.nationalIdType?.trim { it <= ' ' }.equals(getString(R.string.betaqa).trim { it <= ' ' }, ignoreCase = true) ||
                    viewModel.nationalIdType?.trim { it <= ' ' }.equals(getString(R.string.Iqama).trim { it <= ' ' }, ignoreCase = true)) {
                if (validateTor.isAtleastLength(viewModel.nationalId, 10)) {
                } else {
                    ErrorMessage.getInstance().showValidationMessage(this, nationalID, getString(R.string.valid_betaqa))
                    return
                }
            }
            if (ValidationHelper.isNullOrEmpty(dateOfBirthEdtYakeen.text.toString())) {
                ErrorMessage.getInstance().showValidationMessage(this, dateOfBirthEdt_til, getString(R.string.error_empty_fields))
                return
            }
            //only for nationa id number
            if (ConnectionUtil.isInetAvailable(this)) {
                //viewModel.nationalId!!
              //  viewModel.validateProfile(viewModel.nationalId!!, yakeenBirthDay = viewModel.yakeenDateOfBirth, isIqama = isIqama)

                if(isIqama){
                    viewModel.getYakeenInfoForNonSaudi(viewModel.nationalId!!,viewModel.nationalIdTypeInt!!,
                        viewModel.yakeenDateOfBirth, isIqama)
                }else{
                    viewModel.getYakeenInfoForSaudi(viewModel.nationalId!!,viewModel.nationalIdTypeInt!!,
                        viewModel.yakeenDateOfBirth, isIqama)
                }

            } else {
                ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection))
            }

            hideKeyboard(nationalIdTypeSpinner)
        }
    }

    @OnClick(R.id.expiryDateEdt1)
    fun expiryDate() {
        expiryDateEdt_til.requestFocus()
        // isInsuranceDate = false

        if (viewModel.isHijriCalender) {
            val dpd: HijriDatePickerDialog = HijriDatePickerDialog.newInstance(
                    this,
                    viewModel.calenderHijri.get(UmmalquraCalendar.YEAR),
                    viewModel.calenderHijri.get(UmmalquraCalendar.MONTH),
                    viewModel.calenderHijri.get(UmmalquraCalendar.DAY_OF_MONTH))
            dpd.minDate = UmmalquraCalendar()
            dpd.show(supportFragmentManager, "HijriDatePickerDialog")

        } else {


            val dpd: GregorianDatePickerDialog = GregorianDatePickerDialog.newInstance(
                    this,
                    viewModel.calender.get(Calendar.YEAR),
                    viewModel.calender.get(Calendar.MONTH),
                    viewModel.calender.get(Calendar.DAY_OF_MONTH))
            dpd.minDate = Calendar.getInstance()
            dpd.show(supportFragmentManager, "GregorianDatePickerDialog")
        }

    }


    var isInsuranceCard = false

    @OnClick(R.id.nationalIdImg)
    fun imgNational() {
        isInsuranceCard = false
        nationalImage()
    }

    private fun nationalImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val writeGranted = PermissionUtils.isGranted(this, PermissionEnum.WRITE_EXTERNAL_STORAGE)
            val readGranted = PermissionUtils.isGranted(this, PermissionEnum.READ_EXTERNAL_STORAGE)
            val cameraGranted = PermissionUtils.isGranted(this, PermissionEnum.CAMERA)
            if (!writeGranted || !readGranted || !cameraGranted) {
                TedPermission.with(this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .check()
            } else {

                //SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_BOTH, this)
                ImagePicker.with(this)
                        .crop() //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start()
                isFromBasicInfo = true
            }
        } else {
            //  SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_BOTH, this)
            ImagePicker.with(this)
                    .crop() //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
            isFromBasicInfo = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_ADD_PATIENT && resultCode == Activity.RESULT_OK) {

            val isAddPatient = viewModel.isAddPatient
            viewModel.clearSelectedData()
            viewModel.clearInsuranceData()
            val intent1 = Intent()
            intent1.putExtra(Const.ADD_PATIENT, isAddPatient)
            setResult(Activity.RESULT_OK, intent1)
            finish()

        } else {
            try {
                //  val path = SimpleImageSelect.onActivityResult(this, requestCode, resultCode, data)
                /*     if (!ValidationHelper.isNullOrEmpty(path)) {
                         if (!isInsuranceCard) {
                             val uri = Uri.parse(path)
                             nationalIdImg.setImageURI(uri)
                             AsyncTask.execute {
                                 try {
                                     val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

                                     //    Bitmap bitmap = encodeImage(path);
                                     if (bitmap != null) {
                                         nationalCardBase64 = ImageUtils.encodeTobase64(bitmap)
                                         viewModel.idCardScanBase64 = nationalCardBase64
                                     }
                                 } catch (e: IOException) {
                                     e.printStackTrace()
                                     val bitmap: Bitmap? = ImageUtils.encodeImage(path)
                                     if (bitmap != null) {
                                         nationalCardBase64 = ImageUtils.encodeTobase64(bitmap)
                                         viewModel.idCardScanBase64 = nationalCardBase64
                                     }
                                 }
                             }
                         } else {
                             val uri = Uri.parse(path)
                             insuranceCardImg.setImageURI(uri)
                             AsyncTask.execute {
                                 try {
                                     val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                                     if (bitmap != null) {
                                         insuranceCardBase64 = ImageUtils.encodeTobase64(bitmap)
                                     }

                                 } catch (e: IOException) {
                                     e.printStackTrace()
                                     val bitmap: Bitmap? = ImageUtils.encodeImage(path)
                                     if (bitmap != null) {
                                         insuranceCardBase64 = ImageUtils.encodeTobase64(bitmap)
                                     }
                                 }
                             }
                         }
                     }*/

                if (resultCode == Activity.RESULT_OK) {
                    isFromBasicInfo = true
                    //Image Uri will not be null for RESULT_OK
                    if (!isInsuranceCard) {
                        val uri: Uri = data?.data!!

                        // Use Uri object instead of File to avoid storage permissions
                        nationalIdImg.setImageURI(uri)


                        // Toast.makeText(this, data.data?.path, Toast.LENGTH_LONG).show()


//var toFile =  File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!, "/" + "MyClinic/" + LocaleDateHelper.get_current_Time()+".jpg")
//                        rename(data.data?.toFile()!!,
//                                toFile)

                        if (!ValidationHelper.isNullOrEmpty(data.data?.path)) {
                            AsyncTask.execute {
                                try {
                                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                                    if (bitmap != null) {
                                        // viewModel.insuranceCardBase64 = ImageUtils.encodeTobase64(bitmap)
                                        nationalCardBase64 = ImageUtils.encodeTobase64(bitmap)
                                        viewModel.idCardScanBase64 = nationalCardBase64
                                    }
                                    //var base = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAFA3PEY8MlBGQUZaVVBfeMiCeG5uePWvuZHI////////////////////////////////////////////////////2wBDAVVaWnhpeOuCguv/////////////////////////////////////////////////////////////////////////wAARCAQ4AeYDASIAAhEBAxEB/8QAGQABAAMBAQAAAAAAAAAAAAAAAAECAwQF/8QAPBABAAIBAQMKBAUEAgICAwEAAAECEQMSITEEEzNBUVJhcZGhFCKB4TJjscHwQmJy0SNTkvE0QyQ1c4L/xAAXAQEBAQEAAAAAAAAAAAAAAAAAAQID/8QAGxEBAQEBAQEBAQAAAAAAAAAAABEBMQIhMkH/2gAMAwEAAhEDEQA/AOcBtgBfT051J44jtBQMStamzWJzE57AVFtj5JtmN3UqANdPR2q7V5xVF9Ouxt6czMRxyDMG0aWnFKza0xMwDEaa2nWmzszMxLMAAAAAAAAAAAAAAAAAAAAAAAAAF9PT26zifm/QFBrzPGdrNY3TjqZY34zHmANebrFYnEz4zMRHurekVjMTMT2T+0goBETPAATMYx4wtsRiJzx4Z8t4KDTm8RbPGP2/9swBMRmUACaxtTEQtbTmNTYjfIKC2zG1EVtE5Jpwms7UTnw4AqJiMytfT2fDdkFAAAAAAG+ls7dYrfOOrtlgRMxOYnEgmc43TM1z9FrdFT6qZnGOozOMZ3AvTotT6fqoZnGM8QHTFtvk/wAs4mI3k22OT/NOZmNzmCKOidO1q0tXH4cYlzpi9ojEWmI8xGmtXYpSvmyTNpnjMz5oAAAAAAAAAAAAAAAAAAAAAAAAAb8lnG1nw3sAHZiKbUxMznM4cYAtW3bjzmMk78Z3R2dioBEZnEdfBrSsRWkxxmLZZcJzBme2QXrWLXpWeEwtpxE1rPzZ2sbp7WUTMTmJnKdq3bPbxBpOztWrmcde/wDnoziJnf1G3fOdq3qjqwDSYiLTEcIszMz2gNKbMUn5oi07uHU0nEa9+EzNeDnJmZnMzOe0VpbOxFpjFs7t2E5zX5szOMzjs/m+WUzM8ZmfMiZicxOJ8BG2lSOemN+IjO/6NL12qWtaMTGceTmi0xOYmYntJvaYxNp9UVACoAAtsT4G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"

                                    /*     val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                                         if (bitmap != null) {
                                             // viewModel.insuranceCardBase64 = ImageUtils.encodeTobase64(bitmap)
                                             nationalCardBase64 = ImageUtils.encodeTobase64(bitmap)
                                             viewModel.idCardScanBase64 = nationalCardBase64
                                         }
     */
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                    val bitmap: Bitmap? = ImageUtils.encodeImage(data.data?.path!!)
                                    if (bitmap != null) {
                                        //viewModel.insuranceCardBase64  = ImageUtils.encodeTobase64(bitmap)
                                        nationalCardBase64 = ImageUtils.encodeTobase64(bitmap)
                                        viewModel.idCardScanBase64 = nationalCardBase64
                                    }
                                }
                            }
                        }

                    } else {
                        val uri: Uri = data?.data!!

                        // Use Uri object instead of File to avoid storage permissions
                        insuranceCardImg.setImageURI(uri)
                        // Toast.makeText(this, data.data?.path, Toast.LENGTH_LONG).show()

                        if (!ValidationHelper.isNullOrEmpty(data.data?.path)) {
                            AsyncTask.execute {
                                try {
                                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                                    if (bitmap != null) {
                                        // viewModel.insuranceCardBase64 = ImageUtils.encodeTobase64(bitmap)
                                        insuranceCardBase64 = ImageUtils.encodeTobase64(bitmap)
                                    }

                                } catch (e: IOException) {
                                    e.printStackTrace()
                                    val bitmap: Bitmap? = ImageUtils.encodeImage(data.data?.path!!)
                                    if (bitmap != null) {
                                        //viewModel.insuranceCardBase64  = ImageUtils.encodeTobase64(bitmap)
                                        insuranceCardBase64 = ImageUtils.encodeTobase64(bitmap)
                                    }
                                }
                            }
                        }
                    }

                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please try again.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }

    }


    private var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            try {
                // SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_BOTH, this@AddProfileActivity)
                ImagePicker.with(this@AddProfileActivity)
                        .crop() //Crop image(Optional), Check Customization for more option
                        .compress(1024) //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080) //Final image resolution will be less than 1080 x 1080(Optional)
                        .start()
                //File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!! ,"/" + "MyClinic/"+ LocaleDateHelper.get_current_Time())
                isFromBasicInfo = true

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            Toast.makeText(this@AddProfileActivity, "Permission Denied\n$deniedPermissions", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, AddProfileActivity::class.java)
        }
    }

    override fun onDateSet(view: GregorianDatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        viewModel.calender.set(Calendar.YEAR, year)
        viewModel.calender.set(Calendar.MONTH, monthOfYear)
        viewModel.calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val sdf = SimpleDateFormat(DATE_PATTERN_2, Locale.getDefault())

        expiryDateEdt.setText(sdf.format(viewModel.calender.time))
    }

    override fun onDateSet(view: HijriDatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        viewModel.calenderHijri.set(Calendar.YEAR, year)
        viewModel.calenderHijri.set(Calendar.MONTH, monthOfYear)
        viewModel.calenderHijri.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        // val myFormat = "MM/dd/yyyy" //In which you need put here
        //cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, new Locale("ar"));
        val sdf = SimpleDateFormat(DATE_PATTERN_2, Locale.getDefault())
        sdf.calendar = viewModel.calenderHijri
        expiryDateEdt.setText(sdf.format(viewModel.calenderHijri.time))
        //expiryDateEdt.setText("${viewModel.calenderHijri.get(Calendar.DAY_OF_MONTH)}/${viewModel.calenderHijri.get(Calendar.DAY_OF_MONTH)}/${viewModel.calenderHijri.get(Calendar.YEAR)}")
    }

    override fun onCountrySelected(nationalities: Nationalities) {

        val country: String = if (TextUtil.getArabic(this)) {
            nationalities.nameAr
        } else {
            nationalities.nameEn
        }
        nationality.setText(country, TextView.BufferType.EDITABLE)
        nationalID.setText("", TextView.BufferType.EDITABLE)
        viewModel.resetNationalIdLayout()
        val countryKSA = getString(R.string.KSA).trim { it <= ' ' }

        if (country.equals(countryKSA, ignoreCase = true)) {
            arr = resources.getStringArray(R.array.Betaqa)
            nationalIdType.setText(R.string.betaqa)
            nationalID.hint = ""
            //nationalIDTIL.hint = resources.getString(R.string.betaqa_no)
            nationalIDTIL.hint = resources.getString(R.string.national_id)
            nationalID.setRawInputType(InputType.TYPE_CLASS_NUMBER)
            val filters = arrayOfNulls<InputFilter>(2)
            filters[0] = InputFilter.LengthFilter(10)
            filters[1] = InputFilter { source, start, end, dest, dstart, dend ->
                if (end > start) {
                    val acceptedChars = charArrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0')
                    for (index in start until end) {
                        if (!String(acceptedChars).contains(source[index].toString())) {
                            return@InputFilter ""
                        }
                    }
                }
                null
            }
            nationalID.filters = filters
        } else {
            arr = resources.getStringArray(R.array.nationalId)
            if (nationalIdType.getText().toString().equals(getString(R.string.betaqa), ignoreCase = true)) {
                // nationalID.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                nationalID.setRawInputType(InputType.TYPE_CLASS_NUMBER)
                val filters = arrayOfNulls<InputFilter>(2)
                filters[0] = InputFilter.LengthFilter(10)
                filters[1] = InputFilter { source, start, end, dest, dstart, dend ->
                    if (end > start) {
                        val acceptedChars = charArrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0')
                        for (index in start until end) {
                            if (!String(acceptedChars).contains(source[index].toString())) {
                                return@InputFilter ""
                            }
                        }
                    }
                    null
                }
                nationalID.filters = filters
            } else if (nationalIdType.getText().toString().equals(getString(R.string.Passport), ignoreCase = true)) {
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
                nationalID.filters = filters

                //  nationalID.setKeyListener(DigitsKeyListener.getInstance("0123456789ABCDEFGHIJKLMNOPQRSTUVYXYZabcdefghijklmnopqrstuvwxyz"));
            }
            nationalIdType.setText("")
            nationalID.hint = ""
            nationalIDTIL.hint = resources.getString(R.string.national_id1)
            if (errorEnable) TextUtil.tILError(this@AddProfileActivity, nationalIDTIL, nationalID)
        }
        val nationaId = ArrayList(Arrays.asList<String?>(*arr!!)) //new ArrayList is only needed if you absolutely need an ArrayList
        if (!ValidationHelper.isNullOrEmpty(nationaId)) {
            val salutationAdapter: ListAdapter = SpinnerCustomAdapter(this@AddProfileActivity, R.layout.spinner_item_list, nationaId)
            nationalIdType.setAdapter(salutationAdapter)
        }


    }
}