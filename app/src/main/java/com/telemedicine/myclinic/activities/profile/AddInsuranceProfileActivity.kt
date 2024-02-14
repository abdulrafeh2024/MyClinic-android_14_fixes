package com.telemedicine.myclinic.activities.profile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.DatePicker
import android.widget.ListAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionManager
import butterknife.OnClick
import com.github.dhaval2404.imagepicker.ImagePicker
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.reginald.editspinner.EditSpinner.ItemConverter
import com.telemedicine.myclinic.activities.BaseActivity
import com.telemedicine.myclinic.activities.DashBoardActivity
import com.telemedicine.myclinic.adapters.SpinnerCustomAdapter
import com.telemedicine.myclinic.base.BaseViewModel
import com.telemedicine.myclinic.extensions.hideKeyboard
import com.telemedicine.myclinic.injection.ViewModelFactory
import com.telemedicine.myclinic.models.patientMiniModels.PatientsMiniModel
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.util.*
import com.telemedicine.myclinic.util.LocaleDateHelper.DATE_PATTERN_2
import com.telemedicine.myclinic.viewmodels.ProfileRegistrationViewModel
import kotlinx.android.synthetic.main.activity_add_insurance_profile.*
import kotlinx.android.synthetic.main.layout_basic_profile_info.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rebus.permissionutils.PermissionEnum
import rebus.permissionutils.PermissionUtils
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AddInsuranceProfileActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel:ProfileRegistrationViewModel
    //region Activity overrides methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileRegistrationViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        transformView()
        init()
        viewModel.resultProfileInsuranceValidate.observe(this, androidx.lifecycle.Observer {

            TransitionManager.beginDelayedTransition(contentView)
            if (it) {
                insurance_detail_layout.visibility = View.VISIBLE
                next_btn.visibility = View.GONE
                setInsuranceInfo()
            } else {
                insurance_detail_layout.visibility = View.GONE
                next_btn.visibility = View.VISIBLE
            }
        })

        viewModel.resultProfileCreate.observe(this, Observer { success ->
            if (success) {
                if (viewModel.isAddPatient) {
                    setResult(Activity.RESULT_OK, Intent())
                    finish()
                } else {
                    val intent = Intent(this@AddInsuranceProfileActivity, DashBoardActivity::class.java)
                    viewModel.patientsMiniModel?.let { patientsMiniModel ->
                        val patientsMiniModels = ArrayList<PatientsMiniModel>()
                        patientsMiniModels.add(patientsMiniModel)
                        intent.putExtra(Const.EXISTING_PATIENT, patientsMiniModels)
                    }
                    viewModel.clearSelectedData()
                    viewModel.clearInsuranceData()
                    startActivity(intent)
                }
            }
        })

        viewModel.localizeErrorMessage.observe(this, androidx.lifecycle.Observer { mobileOpResult ->
            var errorMesg = ""
            mobileOpResult?.let {
                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.businessErrorMessageEn)) {
                    if (TextUtil.getEnglish(this@AddInsuranceProfileActivity)) errorMesg = mobileOpResult.businessErrorMessageEn + "\n" else if (TextUtil.getArabic(this@AddInsuranceProfileActivity)) errorMesg = mobileOpResult.businessErrorMessageAr + "\n"
                }

                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.technicalErrorMessage)) {
                    errorMesg = """
                    ${errorMesg}Technical Info : 
                    ${mobileOpResult.technicalErrorMessage}
                    """.trimIndent()
                }
                ErrorMessage.getInstance().showError(this@AddInsuranceProfileActivity, errorMesg)
            }

        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.clearErrorMessage()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            try {
             /*   val path = SimpleImageSelect.onActivityResult(this, requestCode, resultCode, data)
                if (!ValidationHelper.isNullOrEmpty(path)) {

                        val uri = Uri.parse(path)
                        insuranceCardImg.setImageURI(uri)
                        AsyncTask.execute {
                            try {
                                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                                if (bitmap != null){
                                    viewModel.insuranceCardBase64 = ImageUtils.encodeTobase64(bitmap)
                                }

                            } catch (e: IOException) {
                                e.printStackTrace()
                                val bitmap: Bitmap? = ImageUtils.encodeImage(path)
                                if (bitmap != null){
                                    viewModel.insuranceCardBase64  = ImageUtils.encodeTobase64(bitmap)
                                }
                            }
                        }
                }*/
                if (resultCode == Activity.RESULT_OK) {
                    //Image Uri will not be null for RESULT_OK
                    val uri: Uri = data?.data!!

                    // Use Uri object instead of File to avoid storage permissions
                    insuranceCardImg.setImageURI(uri)
                   // Toast.makeText(this, data.data?.path, Toast.LENGTH_LONG).show()

                    if(!ValidationHelper.isNullOrEmpty(data.data?.path)){
                        AsyncTask.execute {
                            try {
                                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                                if (bitmap != null){
                                    viewModel.insuranceCardBase64 = ImageUtils.encodeTobase64(bitmap)
                                }

                            } catch (e: IOException) {
                                e.printStackTrace()
                                val bitmap: Bitmap? = ImageUtils.encodeImage(data.data?.path!!)
                                if (bitmap != null){
                                    viewModel.insuranceCardBase64  = ImageUtils.encodeTobase64(bitmap)
                                }
                            }
                        }
                    }

                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
    }

    // endregion

    //region Base class and interface override methods
    override fun inject() {
        super.inject()
        component.inject(this)
    }

    override fun setViewModel(): BaseViewModel {
        return viewModel
    }

    override fun layout(): Int {
       return R.layout.activity_add_insurance_profile
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
                    this@AddInsuranceProfileActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameAr + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            } else {
                ErrorMessage.getInstance().showSuccessGreen(
                    this@AddInsuranceProfileActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameEn + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            }
        } else {
            ErrorMessage.getInstance()
                .showErrorYellow(this@AddInsuranceProfileActivity, appointmentEvent.errorMSg)
        }
    }

    @OnClick(R.id.toolbar_left_iv)
    fun backPressed(){
        onBackPressed()
    }

    override fun onBackPressed() {
        viewModel.clearInsuranceData()
        super.onBackPressed()
    }
    @OnClick(R.id.dateOfBirthEdt1)
    fun datePicker() {

        dateOfBirthEdt_til.requestFocus()

        val datePicker = DatePickerDialog(this, this, viewModel.calenderDateOfBirth
                .get(Calendar.YEAR), viewModel.calenderDateOfBirth.get(Calendar.MONTH),
                viewModel.calenderDateOfBirth.get(Calendar.DAY_OF_MONTH))
        datePicker.datePicker.maxDate = System.currentTimeMillis()
        datePicker.setCanceledOnTouchOutside(false)
        datePicker.show()
    }

    @OnClick(R.id.insuranceExpiryDate)
    fun onExpiryDatePicker(){
        insuranceExpiryDate_til.requestFocus()
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { p0: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->

            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)


            val sdf = SimpleDateFormat(DATE_PATTERN_2, Locale.getDefault())
            insuranceExpiryDate.setText(sdf.format(calendar.time))

        }, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))

        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.setCanceledOnTouchOutside(false)
        datePicker.show()
    }

    @OnClick(R.id.insuranceCardImg)
    fun imgInsurance() {

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
                   /* SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_BOTH, this)*/
                    ImagePicker.with(this)
                            .crop() //Crop image(Optional), Check Customization for more option
                            .compress(1024)			//Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start()
                }
            } else {
              //  SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_BOTH, this)
                ImagePicker.with(this)
                        .crop()
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start()
            }

    }

    private var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            try {
               // SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_BOTH, this@AddInsuranceProfileActivity)
                ImagePicker.with(this@AddInsuranceProfileActivity)
                        .crop()
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            Toast.makeText(this@AddInsuranceProfileActivity, "Permission Denied\n$deniedPermissions", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDateSet(p0: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {

        viewModel.calenderDateOfBirth.set(Calendar.YEAR, year)
        viewModel.calenderDateOfBirth.set(Calendar.MONTH, monthOfYear)
        viewModel.calenderDateOfBirth.set(Calendar.DAY_OF_MONTH, dayOfMonth)


        val sdf = SimpleDateFormat(DATE_PATTERN_2, Locale.getDefault())
        dateOfBirthEdt.setText(sdf.format(viewModel.calenderDateOfBirth.time))
    }
    // endregion

    private fun setInsuranceInfo(){

        viewModel.insuranceModel?.let {

            var locale = Locale("ar", "SA")
            if(TextUtil.getArabic(this)){
                first_name_edt_ar.setText(it.getFirstName(), TextView.BufferType.EDITABLE)
                middle_name_edt_ar.setText(it.getMiddleName(), TextView.BufferType.EDITABLE)
                last_name_edt_ar.setText(it.getLastName(), TextView.BufferType.EDITABLE)
                 locale = Locale("ar", "SA")
            }else{
                first_name_edt.setText(it.getFirstName(), TextView.BufferType.EDITABLE)
                middle_name_edt.setText(it.getMiddleName(), TextView.BufferType.EDITABLE)
                last_name_edt.setText(it.getLastName(), TextView.BufferType.EDITABLE)
                locale = Locale.ENGLISH
            }


            val dob = LocaleDateHelper.convertDateStringFormat(it.dateOfBirth, "yyyy-MM-dd'T'hh:mm:ss", DATE_PATTERN_2, locale)
                //yyyy-MM-dd'T'hh:mm:ss.SSS'Z //to MM/dd/yyyy
            dateOfBirthEdt.setText(dob, TextView.BufferType.EDITABLE)

            val expiryDate = LocaleDateHelper.convertDateStringFormat(it.expiryDate, "yyyy-MM-dd'T'hh:mm:ss", DATE_PATTERN_2, locale)

            if(!expiryDate.isNullOrEmpty()){
                insuranceExpiryDate.setText(expiryDate, TextView.BufferType.EDITABLE)
                insuranceExpiryDate.isEnabled = false
                insuranceExpiryDate_til.setBackgroundResource(R.drawable.grey_non_editable)
            }else{
                insuranceExpiryDate.isEnabled = true
                //grey_non_editable
                insuranceExpiryDate_til.setBackgroundColor(Color.WHITE)
                insuranceExpiryDate_til.setBackgroundResource(R.drawable.grey_border)


            }


            gender_spinner.setText(it.gender, TextView.BufferType.EDITABLE)
        }
    }
    fun transformView() {
        if (TextUtil.getArabic(this)) {
            toolbar_left_iv.setRotation(180f)
            gender_spinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0)

            citySpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0)
            districtSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0)
        //    btnSave.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0)
            next_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0)
            dateOfBirthEdt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0)
            insuranceExpiryDate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0)
            insurance_id_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.attachment, 0, 0, 0)
            insurance_carrier_spiiner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0)
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
    private fun init(){

        val insuranceList = ArrayList<String>()

        viewModel.insuranceCarriersList?.let {
            //التعاونية للتأمين
            for (insuranceCarriers1 in  it) {
                var insuranceName = ""
                if (TextUtil.getEnglish(this@AddInsuranceProfileActivity))
                    insuranceName = insuranceCarriers1.nameEn
                else if (TextUtil.getArabic(this@AddInsuranceProfileActivity))
                    insuranceName = insuranceCarriers1.nameAr
                //5637169326
                insuranceList.add(insuranceName)
            }

            insurance_carrier_spiiner.setItemConverter(ItemConverter { selectedItem ->
                val insuranceCarrier = selectedItem.toString().trim { it <= ' ' } as String
                if (!ValidationHelper.isNullOrEmpty(it)) {
                    for (insuranceCarriers1 in it) {
                        var insuranceName = ""
                        if (TextUtil.getEnglish(this@AddInsuranceProfileActivity)) {
                            insuranceName = insuranceCarriers1.nameEn
                        } else if (TextUtil.getArabic(this@AddInsuranceProfileActivity)) {
                            insuranceName = insuranceCarriers1.nameAr
                        }
                        if (insuranceCarrier.equals(insuranceName.trim { it <= ' ' }, ignoreCase = true)) {
                            viewModel.insuranceCarrierType = insuranceCarriers1.id
                            viewModel.insuranceCarrier = insuranceCarriers1.nameEn
                        }
                    }
                }
                insuranceCarrier
            })
        }

        val insuranceAdapter: ListAdapter = SpinnerCustomAdapter(this, R.layout.spinner_item_list, insuranceList)
        insurance_carrier_spiiner.setAdapter(insuranceAdapter)


        val genderArr = resources.getStringArray(R.array.gender)
        val genderArrList = ArrayList(Arrays.asList(*genderArr)) //new ArrayList is only needed if you absolutely need an ArrayList


        val genderAdapter: ListAdapter = SpinnerCustomAdapter(this, R.layout.spinner_item_list, genderArrList)
        gender_spinner.setAdapter(genderAdapter)
    }

    @OnClick(R.id.btnSave)
    fun onSaveClicked(){

        val policyNo = policyNoEdt.text.toString()
        val memberShipNum: String = member_ship_no.text.toString()
        val insuranceCarrier: String = insurance_carrier_spiiner.getText().toString()


        viewModel.memberShipNo = memberShipNum
        viewModel.policyNo = policyNo


        if (ValidationHelper.isNullOrEmpty(insuranceCarrier)) {
            TextUtil.tILError(this, insurance_carrier_spiiner_til, insurance_carrier_spiiner)
        }

        if (ValidationHelper.isNullOrEmpty(policyNo)) {
            TextUtil.tILError(this, policyNoEdt_til, policyNoEdt)
        }

        if (ValidationHelper.isNullOrEmpty(memberShipNum)) {
            TextUtil.tILError(this, member_ship_no_til, member_ship_no)
        }

        if (ValidationHelper.isNullOrEmpty(insuranceCarrier)) {
            ErrorMessage.getInstance().showValidationMessage(this, insurance_carrier_spiiner, getString(R.string.error_empty_fields))
            return
        }
        if (ValidationHelper.isNullOrEmpty(policyNo)) {
            ErrorMessage.getInstance().showValidationMessage(this, policyNoEdt, getString(R.string.error_empty_fields))
            return
        }

        if (ValidationHelper.isNullOrEmpty(memberShipNum)) {
            ErrorMessage.getInstance().showValidationMessage(this, member_ship_no, getString(R.string.error_empty_fields))
            return
        }


        createInsuranceProfile()
    }

    @OnClick(R.id.next_btn)
    fun onNextClicked(){
        validateInsuranceData()
    }

    private fun validateInsuranceData() {


        val policyNo = policyNoEdt.text.toString()
        val memberShipNum: String = member_ship_no.text.toString()
        val insuranceCarrier: String = insurance_carrier_spiiner.getText().toString()


        viewModel.memberShipNo = memberShipNum
        viewModel.policyNo = policyNo


        if (ValidationHelper.isNullOrEmpty(insuranceCarrier)) {
            TextUtil.tILError(this, insurance_carrier_spiiner_til, insurance_carrier_spiiner)
        }

        if (ValidationHelper.isNullOrEmpty(policyNo)) {
            TextUtil.tILError(this, policyNoEdt_til, policyNoEdt)
        }

        if (ValidationHelper.isNullOrEmpty(memberShipNum)) {
            TextUtil.tILError(this, member_ship_no_til, member_ship_no)
        }

        if (ValidationHelper.isNullOrEmpty(insuranceCarrier)) {
            ErrorMessage.getInstance().showValidationMessage(this, insurance_carrier_spiiner, getString(R.string.error_empty_fields))
            return
        }
        if (ValidationHelper.isNullOrEmpty(policyNo)) {
            ErrorMessage.getInstance().showValidationMessage(this, policyNoEdt, getString(R.string.error_empty_fields))
            return
        }

        if (ValidationHelper.isNullOrEmpty(memberShipNum)) {
            ErrorMessage.getInstance().showValidationMessage(this, member_ship_no, getString(R.string.error_empty_fields))
            return
        }
        if (ConnectionUtil.isInetAvailable(this)) {
            viewModel.validateMemberShip()
            hideKeyboard(member_ship_no)
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection))
        }
    }

    private  fun createInsuranceProfile(){

        val gender: String = gender_spinner.getText().toString()

        var firstName: String = first_name_edt.getText().toString()
        var middleName: String = middle_name_edt.getText().toString()
        var lastName: String = last_name_edt.getText().toString()


        if (TextUtil.getArabic(this)) {
            firstName = first_name_edt_ar.getText().toString()
            middleName = middle_name_edt_ar.getText().toString()
            lastName = last_name_edt_ar.getText().toString()
        }

        viewModel.firstName = firstName
        viewModel.lastName = lastName
        viewModel.middleName = middleName

        var dob = dateOfBirthEdt.text.toString()

       // viewModel.insuranceExpiryDate = LocaleDateHelper.convertDateStringFormat(insuranceExpiryDate.text.toString(), LocaleDateHelper.DATE_PATTERN_2, "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", Locale.ENGLISH)
        var expiryDate = insuranceExpiryDate.text.toString()

        //insuranceExpiryDate


        if (ValidationHelper.isNullOrEmpty(expiryDate)) {
            TextUtil.tILError(this, insuranceExpiryDate_til, insuranceExpiryDate)
        }


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
        }else if(ValidationHelper.isNullOrEmpty(expiryDate)){
            ErrorMessage.getInstance().showValidationMessage(this, insuranceExpiryDate, getString(R.string.error_empty_fields))
            return
        }


        val locale = if(TextUtil.getArabic(this)){
            Locale("ar", "SA")
        }else{
            Locale.ENGLISH
        }

        expiryDate =  LocaleDateHelper.convertDateStringFormat(expiryDate, DATE_PATTERN_2, "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", locale)
        dob = LocaleDateHelper.convertDateStringFormat(dob, DATE_PATTERN_2, "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", locale)

        viewModel.insuranceExpiryDate = LocaleDateHelper.convertDateStringFormat(expiryDate, "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", Locale.ENGLISH)

        viewModel.birthDate = LocaleDateHelper.convertDateStringFormat(dob, "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", Locale.ENGLISH)//dob

        if (gender.trim { it <= ' ' }.equals(getString(R.string.male).trim { it <= ' ' }, ignoreCase = true))
            viewModel.gender = "1"
        else if (gender.trim { it <= ' ' }.equals(getString(R.string.female).trim { it <= ' ' }, ignoreCase = true))
            viewModel.gender = "2"
        else if(gender.trim().equals("Female", ignoreCase = true)){
            viewModel.gender = "2"
        }else{
            viewModel.gender = "1"
        }


        if (ConnectionUtil.isInetAvailable(this)) {
            viewModel.createPatientProfile()
            hideKeyboard(gender_spinner)
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection))
        }

    }
    //companion class object
    companion object{
        fun getLaunchIntent(context: Context):Intent{
            return Intent(context, AddInsuranceProfileActivity::class.java)
        }
    }

    // endregion
}