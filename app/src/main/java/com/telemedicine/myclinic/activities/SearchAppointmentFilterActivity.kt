package com.telemedicine.myclinic.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.Base64
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ListAdapter
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.OnClick
import com.jakewharton.rxbinding.widget.RxTextView
import com.reginald.editspinner.EditSpinner.ItemConverter
import com.telemedicine.myclinic.App
import com.telemedicine.myclinic.adapters.DoctorsProfileAdapter
import com.telemedicine.myclinic.adapters.FavouriteDoctorsRvAdapter
import com.telemedicine.myclinic.adapters.FuzzySearchRvAdapter
import com.telemedicine.myclinic.adapters.SpinnerCustomAdapter
import com.telemedicine.myclinic.base.BaseViewModel
import com.telemedicine.myclinic.eenum.Success
import com.telemedicine.myclinic.extensions.hideKeyboard
import com.telemedicine.myclinic.injection.ViewModelFactory
import com.telemedicine.myclinic.models.FuzzySearchDTO
import com.telemedicine.myclinic.models.FuzzySearchMinModel
import com.telemedicine.myclinic.models.FuzzySearchResponseModel
import com.telemedicine.myclinic.models.MobileOpResult
import com.telemedicine.myclinic.models.bookAppointment.*
import com.telemedicine.myclinic.models.company.Company
import com.telemedicine.myclinic.models.favouritedoctors.FavouriteDoctorsMiniModel
import com.telemedicine.myclinic.models.favouritedoctors.FavouriteDoctorsResponseModel
import com.telemedicine.myclinic.models.favouritedoctors.GetFavouriteDTO
import com.telemedicine.myclinic.models.favouritedoctors.UpdateFavouriteDTO
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile
import com.telemedicine.myclinic.models.profilecreatoinmodels.SecurityTokenDTO
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.util.*
import com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY
import com.telemedicine.myclinic.util.Const.LOGGED_IN_MODE
import com.telemedicine.myclinic.viewmodels.DashboardViewModel
import com.telemedicine.myclinic.views.LightSpinnerEdittext
import com.telemedicine.myclinic.webservices.RestClient
import kotlinx.android.synthetic.main.activity_re_schedule_appt.*
import kotlinx.android.synthetic.main.activity_search_appointment_filter.*
import kotlinx.android.synthetic.main.activity_time_slot.view.*
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.xml.datatype.DatatypeConstants.MONTHS


public class SearchAppointmentFilterActivity : BaseActivity() {

    var chatBotBg: RelativeLayout? = null

    public var progressBar: ProgressBar? = null

    public var chatBot_rounded: View? = null

    public var chatBotWebView: WebView? = null

    private var fuzzySearchList: ArrayList<FuzzySearchMinModel?>? = ArrayList()
    private var fuzzySearchListTemp: ArrayList<FuzzySearchMinModel?>? = ArrayList()
    private lateinit var fuzzySearchRvAdapter: FuzzySearchRvAdapter

    private var favouriteDoctorsList: ArrayList<FavouriteDoctorsMiniModel?>? = ArrayList()
    private var favouriteDoctorsListTemp: ArrayList<FavouriteDoctorsMiniModel?>? = ArrayList()
    private lateinit var favouriteDoctorsRvAdapter: FavouriteDoctorsRvAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: DashboardViewModel

    override fun layout(): Int {
        return R.layout.activity_search_appointment_filter
    }



    var isLoggedInMode = false
    var startDate = ""
    var selectedDate = ""
    var specialityId = ""
    var doctorid = ""
    var companyId = ""
    var companyName = ""
    var isTelemedicine = false;
    var isSpecialitySelected = false
    var isLocationSelected = false
    var isDoctorSelected = false

    //chatBot
    var isChatBotVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        changeLanguage()
        super.onCreate(savedInstanceState)

        //viewModel =  ViewModelProvider(this, viewModelFactory!!).get(DashboardViewModel::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory)[DashboardViewModel::class.java]
        chatBotBg = findViewById(R.id.chat_bot_bg)
        progressBar = findViewById(R.id.progressBar)
        chatBot_rounded = findViewById(R.id.chatBot_rounded)
        chatBotWebView = findViewById(R.id.chat_bot_web_view)
        getCurrentDate()
        init()
        loadCompany()
        getSpecilities()
        transformView()

        if (TextUtil.getArabic(this)) {
            member_ship_no.keyListener =
                DigitsKeyListener.getInstance("غظضذخثتشرقصجفعسنملةىكيطئءؤحزوهدبا ")
            member_ship_no.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)

            select_specialities_spinner.keyListener =
                DigitsKeyListener.getInstance("غظضذخثتشرقصجفعسنملةىكيطئءؤحزوهدبا ")
            select_specialities_spinner.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)

            select_doctor_spinner.keyListener =
                DigitsKeyListener.getInstance("غظضذخثتشرقصجفعسنملةىكيطئءؤحزوهدبا ")
            select_doctor_spinner.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)

            location_spiiner.keyListener =
                DigitsKeyListener.getInstance("غظضذخثتشرقصجفعسنملةىكيطئءؤحزوهدبا ")
            location_spiiner.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)

        } else {
            member_ship_no.keyListener =
                DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ ")
            member_ship_no.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)

            select_specialities_spinner.keyListener =
                DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ ")
            select_specialities_spinner.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)

            select_doctor_spinner.keyListener =
                DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ ")
            select_doctor_spinner.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)

            location_spiiner.keyListener =
                DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ ")
            location_spiiner.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)

        }

    }

    override fun onStart() {
        super.onStart()

        viewModel.companyResponse.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                if (it.companyList.isNotEmpty()) {
                    setCompanies(it.companyList)
                }
            }
        })
    }

    override fun inject() {
        super.inject()
        component.inject(this)
    }

    @OnClick(R.id.toolbar_left_iv)
    fun back() {
        onBackPressed()
    }

    override fun onBackPressed() {
        if (isChatBotVisible) {
            openChatBotBg(null)
            return
        } else {
            super.onBackPressed()
        }
    }

    override fun setViewModel(): BaseViewModel {
        return viewModel
    }

    private fun loadCompany() {
        val securityToken = tinyDB.getString(Const.TOKEN_KEY)
        viewModel.loadCompanies(SecurityTokenDTO(securityToken))
    }

    val myCalendar = Calendar.getInstance()
    var date: DatePickerDialog.OnDateSetListener? = null
    private var handler: Handler? = null
    var isOnTextChanged = false

    @SuppressLint("ClickableViewAccessibility")
    fun init() {

        val intent = intent
        if (intent != null) {
            isTelemedicine = intent.getBooleanExtra(Const.ISTELEMEDICINE_KEY, false)
            isLoggedInMode = intent.getBooleanExtra(Const.LOGGED_IN_MODE, false)
        }


        //    FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this);
        chatBotWebView!!.settings.javaScriptEnabled = true
        chatBotWebView!!.isHorizontalScrollBarEnabled = false

        chatBotWebView!!.settings.loadWithOverviewMode = true
        chatBotWebView!!.settings.useWideViewPort = true

        chatBotWebView!!.settings.setSupportZoom(true)
        chatBotWebView!!.settings.builtInZoomControls = true
        chatBotWebView!!.settings.displayZoomControls = false

        chatBotWebView!!.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        chatBotWebView!!.isScrollbarFadingEnabled = false
        loadChatBot()

        if (isTelemedicine) {
            search_location_layout.visibility = View.GONE
        } else {
            search_location_layout.visibility = View.VISIBLE
        }

        if (isLoggedInMode) {
            past_doctor_label.visibility = View.VISIBLE
            past_doctors_view_line.visibility = View.VISIBLE
        } else {
            past_doctor_label.visibility = View.GONE
            past_doctors_view_line.visibility = View.GONE
            (application as App).cancelAlarmIfExists()
            tinyDB.putBoolean("session", false)
        }

        location_spiiner.disableCopyPaste()
        select_specialities_spinner.disableCopyPaste()
        select_doctor_spinner.disableCopyPaste()

        speciality_arrow_down.setOnClickListener {
            select_specialities_spinner.showDropDown()
        }

        select_specialities_spinner.setOnTouchListener { v, event ->
            select_specialities_spinner.showDropDown()
            //scrollView.requestFocusFromTouch()
            false
        }

        select_doctor_spinner.setOnTouchListener { v, event ->
            select_doctor_spinner.showDropDown()
            //scrollView.requestFocusFromTouch()
            false
        }

        location_spiiner.setOnTouchListener { v, event ->
            location_spiiner.showDropDown()
            //scrollView.requestFocusFromTouch()
            false
        }

        doctor_arrow_down.setOnClickListener {
            select_doctor_spinner.showDropDown()
        }

        location_arrow_down.setOnClickListener {
            location_spiiner.showDropDown()
        }

        RxTextView.textChanges(member_ship_no)
            .debounce(2, TimeUnit.SECONDS)
            .subscribe { text ->
                this@SearchAppointmentFilterActivity.runOnUiThread(Runnable {
                    if (text.isNotEmpty()) {
                        getFuzzySearchResult(text.toString())
                    } else {
                        exl_type_afternoon.collapse(true)
                        recycler_view_layout.visibility = View.GONE
                        //  recycler_view_layout.visibility = View.GONE
                    }
                })
            }

        RxTextView.textChanges(location_spiiner)
            .debounce(1, TimeUnit.SECONDS)
            .subscribe { text ->
                this@SearchAppointmentFilterActivity.runOnUiThread(Runnable {
                    if (text.isNotEmpty()) {

                        if (!isLocationSelected) {
                            var filterResult: ArrayList<String>? = ArrayList()
                            filterResult = getCompaniesFilter(text.toString())
                            val cityAdapter: ListAdapter =
                                SpinnerCustomAdapter(this, R.layout.spinner_item_list, filterResult)
                            location_spiiner.setAdapter(cityAdapter)
                            location_spiiner.showDropDown()
                        } else {
                            isLocationSelected = false
                        }

                    } else {
                        val cityAdapter: ListAdapter = SpinnerCustomAdapter(
                            this,
                            R.layout.spinner_item_list,
                            companiesListTemp
                        )
                        location_spiiner.setAdapter(cityAdapter)
                        //  recycler_view_layout.visibility = View.GONE
                    }
                })
            }

        RxTextView.textChanges(select_specialities_spinner)
            .debounce(1, TimeUnit.SECONDS)
            .subscribe { text ->
                this@SearchAppointmentFilterActivity.runOnUiThread(Runnable {
                    if (text.isNotEmpty()) {
                        if (!isSpecialitySelected) {
                            var filterResult: ArrayList<String>? = ArrayList()
                            filterResult = getSpecialitiesFilter(text.toString())
                            val cityAdapter: ListAdapter =
                                SpinnerCustomAdapter(this, R.layout.spinner_item_list, filterResult)
                            select_specialities_spinner.setAdapter(cityAdapter)
                            select_specialities_spinner.showDropDown()
                        } else {
                            isSpecialitySelected = false
                        }

                    } else {
                        val cityAdapter: ListAdapter =
                            SpinnerCustomAdapter(this, R.layout.spinner_item_list, specialitiesTemp)
                        select_specialities_spinner.setAdapter(cityAdapter)
                    }
                })
            }

        RxTextView.textChanges(select_doctor_spinner)
            .debounce(1, TimeUnit.SECONDS)
            .subscribe { text ->
                this@SearchAppointmentFilterActivity.runOnUiThread(Runnable {
                    if (text.isNotEmpty()) {

                        if (!isDoctorSelected) {
                            var filterResult: ArrayList<String>? = ArrayList()
                            filterResult = getDoctorsFilter(text.toString())
                            val cityAdapter: ListAdapter =
                                SpinnerCustomAdapter(this, R.layout.spinner_item_list, filterResult)
                            select_doctor_spinner.setAdapter(cityAdapter)
                            select_doctor_spinner.showDropDown()

                        } else {
                            isDoctorSelected = false
                        }

                    } else {
                        val cityAdapter: ListAdapter =
                            SpinnerCustomAdapter(this, R.layout.spinner_item_list, doctorsListTemp)
                        select_doctor_spinner.setAdapter(cityAdapter)
                    }
                })
            }

        /*     location_spiiner.addTextChangedListener(object : TextWatcher{
             override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

             }

             override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

             }

             override fun afterTextChanged(text: Editable?) {
                 if (text!!.isNotEmpty()) {
                     this@SearchAppointmentFilterActivity.runOnUiThread(Runnable {
                         var filterResult: ArrayList<String>? = ArrayList()
                         filterResult =  getFilter(text.toString())
                         val cityAdapter: ListAdapter = SpinnerCustomAdapter(this@SearchAppointmentFilterActivity, R.layout.spinner_item_list, filterResult)
                         location_spiiner.setAdapter(cityAdapter)
                         location_spiiner.showDropDown()
                     })
                 }else{
                     //  recycler_view_layout.visibility = View.GONE
                 }
             }

         })*/

        /*    member_ship_no.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    isOnTextChanged = true
                }

                override fun afterTextChanged(s: Editable?) {
                   Toast.makeText(this@SearchAppointmentFilterActivity, s.toString(), Toast.LENGTH_SHORT).show()

                    if (s.toString().isEmpty()) {
                        recycler_view_layout.visibility = View.GONE
                    }
                    *//* if (isOnTextChanged) {
                    isOnTextChanged = false;
                    if (s.toString().isNotEmpty()) {
                        getFuzzySearchResult(s.toString())
                    } else {
                        fuzzySearchList?.clear()
                        fuzzySearchRvAdapter.notifyDataSetChanged()
                        recycler_view_layout.visibility = View.GONE
                    }
                }

                 if (s.toString().isEmpty()) {
                     fuzzySearchList?.clear()
                     fuzzySearchRvAdapter.notifyDataSetChanged()
                     recycler_view_layout.visibility = View.GONE
                 }*//*
            }
        })*/

        search_bar_btn.setOnClickListener {
            getFuzzySearchResult(member_ship_no.text.toString())
        }
        confirmBooking_btn.setOnClickListener {

            val specialityTv: String = select_specialities_spinner.text.toString()

            if (ValidationHelper.isNullOrEmpty(specialityTv)) {
                TextUtil.tILErrorTwo(
                    this,
                    insurance_carrier_spiiner_til,
                    select_specialities_spinner
                )
                ErrorMessage.getInstance().showValidationMessage(
                    this,
                    select_specialities_spinner,
                    getString(R.string.please_select_the_speciality)
                )
                return@setOnClickListener
            }

            if (selectedSpeciality != select_specialities_spinner.text.toString()) {
                TextUtil.tILErrorTwo(
                    this,
                    insurance_carrier_spiiner_til,
                    select_specialities_spinner
                )
                ErrorMessage.getInstance().showValidationMessage(
                    this,
                    select_specialities_spinner,
                    getString(R.string.please_select_valid_speciality)
                )
                return@setOnClickListener
            }
            val enteredDoctor: String = select_doctor_spinner.text.toString() ?: ""

            if (!ValidationHelper.isNullOrEmpty(enteredDoctor)) {
                if (selectedDoctor != enteredDoctor) {
                    TextUtil.tILErrorTwo(this, doctor_spiiner_til, select_doctor_spinner)
                    ErrorMessage.getInstance().showValidationMessage(
                        this,
                        select_doctor_spinner,
                        getString(R.string.please_select_valid_doctor)
                    )
                    return@setOnClickListener
                }
            }

            if(!isTelemedicine){
                val enteredLocation: String = location_spiiner.text.toString() ?: ""

                if (ValidationHelper.isNullOrEmpty(enteredLocation)) {
                    TextUtil.tILErrorTwo(
                        this,
                        location_spiiner_til,
                        location_spiiner
                    )
                    ErrorMessage.getInstance().showValidationMessage(
                        this,
                        location_spiiner,
                        getString(R.string.please_select_location)
                    )
                    return@setOnClickListener
                }
            }

         /*   if (!ValidationHelper.isNullOrEmpty(enteredLocation)) {
                if (selectedLocationSt != enteredLocation) {
                    TextUtil.tILErrorTwo(this, location_spiiner_til, location_spiiner)
                    ErrorMessage.getInstance().showValidationMessage(
                        this,
                        location_spiiner,
                        getString(R.string.please_select_valid_location)
                    )
                    return@setOnClickListener
                }
            }*/


            val intent = Intent(this, SearchAppointmentResultActivity::class.java)

            if (enteredDoctor.isNotEmpty()) {
                intent.putExtra("DoctorId", getDoctorId(selectedDoctor))
            } else {
                intent.putExtra("DoctorId", "")
            }
            intent.putExtra("SpecialityId", specialityId)
            intent.putExtra("Speciality", speciality)
            intent.putExtra("Date", selectedDate)
            intent.putExtra("startDate", startDate)
            intent.putExtra("CompanyName", companyName)
            intent.putExtra("CompanyId", companyId)
            intent.putExtra(LOGGED_IN_MODE, isLoggedInMode)
            intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicine)
            startActivityForResult(intent, 111)
        }


        dateOfBirthEdt1.setOnClickListener {
            date_of_booking_apt_et.requestFocus()
            val datePicker = DatePickerDialog(
                this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            )

            datePicker.datePicker.minDate = System.currentTimeMillis()
            datePicker.setCanceledOnTouchOutside(false)
            datePicker.show()
        }

        date =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                val myFormat = "EEEE, dd MMMM yyyy" //In which you need put here
                val startDateFormat = "yyyy-MM-dd"
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                //    val startSdf = SimpleDateFormat(startDateFormat, Locale.getDefault())
                val startSdf = SimpleDateFormat(startDateFormat, Locale.ENGLISH)
                startDate = startSdf.format(myCalendar.time)
                selectedDate = startDate
                dateAppointment.setText(sdf.format(myCalendar.time))
            }

        fuzzySearchRecyclerView(fuzzySearchList)
        favouriteDoctorRecyclerView(favouriteDoctorsList)
        initSpecialityList()

        if (isLoggedInMode) {
            getFavouriteDoctors()
        }
    }




    fun openChatBot(view: View?) {
        chatBotBg!!.visibility = View.VISIBLE
        isChatBotVisible = true
        loadChatBot()
        /*   Intent intent = new Intent(this, BrowserActivityPayment.class);
        intent.putExtra("calendar", false);
        intent.putExtra("url", "https://myclinic.hellotars.com/conv/Rg2PDe/");
        //  intent.putExtra("url", getString(R.string.contact_us_url));
        intent.putExtra(Const.TERMS_CONDITIONS_KEY, termsConditionTxt);
        startActivity(intent);*/
    }

    fun openChatBotBg(view: View?) {
        chatBotBg!!.visibility = View.GONE
        isChatBotVisible = false
    }


    private fun loadChatBot() {
        chatBotWebView!!.loadUrl("https://myclinic.hellotars.com/conv/Rg2PDe/")
        chatBotWebView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }
        }
    }

    fun getCurrentDate() {
        val c = Calendar.getInstance().time
        //  val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        startDate = df.format(c)

        //set text view date
        val myFormat = "EEEE, dd MMMM yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        dateAppointment.setText(sdf.format(c))
    }

    var speciality = ""
    var selectedSpeciality = ""
    var selectedDoctor = ""
    var selectedLocationSt = ""
    private fun initSpecialityList() {

        // it converts the item in the list to a string shown in EditText.

        // it converts the item in the list to a string shown in EditText.
        // setOnItemSelectedListener

        select_specialities_spinner.setItemConverter(ItemConverter { selectedItem ->
            val selectedItem1 = selectedItem.toString() as String
            isSpecialitySelected = true
            speciality = selectedItem1
            selectedSpeciality = selectedItem1
            getDoctorBySpecilityId(getSpecialitiesId(selectedItem1))
            hideKeyboard(select_specialities_spinner)
            selectedItem1
        })

        select_doctor_spinner.setItemConverter(ItemConverter { selectedItem ->
            isDoctorSelected = true
            selectedDoctor = selectedItem.toString()
            hideKeyboard(select_doctor_spinner)
            selectedItem.toString()
        })

        location_spiiner.setItemConverter(ItemConverter { selectedItem ->

            val selectedItem1 = selectedItem.toString() as String
            isLocationSelected = true
            if (selectedItem1 == getString(R.string.prince_sultan)) {
                companyId = "nc01"
                companyName = selectedItem1
                selectedLocationSt = selectedItem1
            } else if (selectedItem1 == getString(R.string.safa)) {
                companyId = "safa"
                companyName = selectedItem1
                selectedLocationSt = selectedItem1
            }
            else if(selectedItem1 == getString(R.string.prc)){
                companyId = "prc"
                companyName = selectedItem1
                selectedLocationSt = selectedItem1
            }else {
                companyId = ""
                companyName = ""
                selectedLocationSt = selectedItem1
            }
            hideKeyboard(location_spiiner)
            selectedItem1
        })

        select_specialities_spinner.dropDownHeight = 585
        select_doctor_spinner.dropDownHeight = 585
    }

    fun getSpecialitiesId(specialitiesStr: String): String? {
        var specialitiesId = ""
        for (specialtiesModel in specialtiesList!!) {
            if (TextUtil.getEnglish(this)) {
                if (!ValidationHelper.isNullOrEmpty(specialtiesModel!!.nameEn)) {
                    if (specialitiesStr.trim { it <= ' ' }.equals(specialtiesModel.nameEn.trim { it <= ' ' }, ignoreCase = true)) {
                        specialitiesId = specialtiesModel!!.id.toString()
                    }
                }
            } else if (TextUtil.getArabic(this)) {
                if (!ValidationHelper.isNullOrEmpty(specialtiesModel!!.nameAr)) {
                    if (specialitiesStr.trim { it <= ' ' }.equals(specialtiesModel.nameAr.trim { it <= ' ' }, ignoreCase = true)) {
                        specialitiesId = specialtiesModel.id.toString()
                    }
                }
            }
        }
        return specialitiesId
    }

    private fun changeLanguage() {
        if (TextUtil.getArabic(this)) {
            val locale = Locale("ar")
            Locale.setDefault(locale)
            val config: Configuration = baseContext.resources.configuration
            config.setLocale(locale)
            createConfigurationContext(config)
        }
    }

    var specialtiesList: ArrayList<SpecialtiesModel?>? = null

    fun getSpecilities() {
        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog()
            val securityToken = tinyDB.getString(Const.TOKEN_KEY)
            var companyId = tinyDB.getString(Const.COMPANY_ID)
            /* if (isTelemedicine) {
                 companyId = ""
             }*/

            companyId = ""
            val getRefSpecialtiesDTO =
                GetRefSpecialtiesDTO(securityToken, isTelemedicine, companyId)
            RestClient.getInstance()
                .getRefSpecialties(getRefSpecialtiesDTO) { result, status, errorMessage ->
                    hideWaitDialog()
                    val getRefSpecialtiesModel = result as GetRefSpecialtiesModel
                    if (getRefSpecialtiesModel != null) {
                        val mobileOpResult = getRefSpecialtiesModel.mobileOpResult
                        if (mobileOpResult != null) {
                            if (mobileOpResult.result == Success.SUCCESSCODE.value) {
                                specialtiesList = getRefSpecialtiesModel.specialtiesList
                                if (!ValidationHelper.isNullOrEmpty(specialtiesList)) {
                                    setSpecialities(specialtiesList)
                                }
                            } else {
                                var errorMesg = ""
                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.businessErrorMessageEn)) {
                                    if (TextUtil.getEnglish(this@SearchAppointmentFilterActivity)) {
                                        errorMesg = """
                                    ${mobileOpResult.businessErrorMessageEn}
                                    
                                    """.trimIndent()
                                    } else if (TextUtil.getArabic(this@SearchAppointmentFilterActivity)) {
                                        errorMesg = """
                                    ${mobileOpResult.businessErrorMessageAr}
                                    
                                    """.trimIndent()
                                    }
                                }
                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.technicalErrorMessage)) {
                                    errorMesg = """
                                ${errorMesg}Technical Info : 
                                ${mobileOpResult.technicalErrorMessage}
                                """.trimIndent()
                                }
                                ErrorMessage.getInstance()
                                    .showError(this@SearchAppointmentFilterActivity, errorMesg)
                            }
                        } else ErrorMessage.getInstance()
                            .showError(this@SearchAppointmentFilterActivity, errorMessage)
                    } else ErrorMessage.getInstance()
                        .showError(this@SearchAppointmentFilterActivity, errorMessage)
                }
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection))
        }
    }

    private var companiesListTemp: ArrayList<String>? = ArrayList()
    private fun setCompanies(companiesList: ArrayList<Company>?) {
        val companiesStr = ArrayList<String>()

        //companiesStr.add(getString(R.string.all_locations))
        for (companyModel in companiesList!!) {
            if (TextUtil.getEnglish(this)) {
                if (!ValidationHelper.isNullOrEmpty(companyModel!!.companyFullEn)) {
                    companiesStr.add(companyModel!!.companyFullEn)
                }
            } else if (TextUtil.getArabic(this)) {
                if (!ValidationHelper.isNullOrEmpty(companyModel!!.companyFullAr)) {
                    companiesStr.add(companyModel.companyFullAr)
                }
            }
        }

        companiesListTemp = companiesStr
        val cityAdapter: ListAdapter =
            SpinnerCustomAdapter(this, R.layout.spinner_item_list, companiesStr)
        location_spiiner.setAdapter(cityAdapter)
    }

    fun getCompaniesFilter(charSequence: CharSequence?): ArrayList<String>? {
        val filterResultsData = ArrayList<String>()
        if (charSequence == null || charSequence.isEmpty()) {
            return null
        } else {
            for (data in companiesListTemp!!) {
                //In this loop, you'll filter through originalData and compare each item to charSequence.
                //If you find a match, add it to your new ArrayList
                //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional

                val splitStr: Array<String> = data.split("\\s+".toRegex()).toTypedArray()
                for (name in splitStr) {
                    if (name.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filterResultsData.add(data)
                        break
                    }
                }

                /* if (data.toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                     filterResultsData.add(data)
                 }*/
            }
        }
        return filterResultsData
    }

    fun getDoctorsFilter(charSequence: CharSequence?): ArrayList<String>? {
        val filterResultsData = ArrayList<String>()
        if (charSequence == null || charSequence.isEmpty()) {
            return null
        } else {
            for (data in doctorsListTemp!!) {
                //In this loop, you'll filter through originalData and compare each item to charSequence.
                //If you find a match, add it to your new ArrayList
                //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                val splitStr: Array<String> = data.split("\\s+".toRegex()).toTypedArray()
                for (name in splitStr) {
                    if (name.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filterResultsData.add(data)
                        break
                    }
                }
                /*if (data.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                    filterResultsData.add(data)
                }*/
            }
        }
        return filterResultsData
    }


    fun getSpecialitiesFilter(charSequence: CharSequence?): ArrayList<String>? {
        val filterResultsData = ArrayList<String>()
        if (charSequence == null || charSequence.isEmpty()) {
            return null
        } else {
            for (data in specialitiesTemp!!) {
                //In this loop, you'll filter through originalData and compare each item to charSequence.
                //If you find a match, add it to your new ArrayList
                //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                if (data.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                    filterResultsData.add(data)
                }
            }
        }
        return filterResultsData
    }

    private var specialitiesTemp: ArrayList<String>? = ArrayList()
    fun setSpecialities(specialtiesList: ArrayList<SpecialtiesModel?>?) {
        val specialitiesStr = ArrayList<String>()
        val gender = tinyDB.getString(Const.PATIENT_GENDER)
        for (specialtiesModel in specialtiesList!!) {
            if (TextUtil.getEnglish(this)) {
                if (!ValidationHelper.isNullOrEmpty(specialtiesModel!!.nameEn)) {
                    specialitiesStr.add(specialtiesModel!!.nameEn)
                }
            } else if (TextUtil.getArabic(this)) {
                if (!ValidationHelper.isNullOrEmpty(specialtiesModel!!.nameAr)) {
                    specialitiesStr.add(specialtiesModel.nameAr)
                }
            }
            if (gender.equals("1", ignoreCase = true)) {
                if (specialtiesModel!!.id.toString().equals("5637144584", ignoreCase = true) ||
                    specialtiesModel!!.id.toString().equals("41", ignoreCase = true)
                ) {
                    if (TextUtil.getEnglish(this)) {
                        if (!ValidationHelper.isNullOrEmpty(specialtiesModel.nameEn)) {
                            specialitiesStr.remove(specialtiesModel.nameEn)
                        }
                    } else if (TextUtil.getArabic(this)) {
                        if (!ValidationHelper.isNullOrEmpty(specialtiesModel.nameAr)) {
                            specialitiesStr.remove(specialtiesModel.nameAr)
                        }
                    }
                }
            }
        }

        specialitiesTemp = specialitiesStr
        val cityAdapter: ListAdapter =
            SpinnerCustomAdapter(this, R.layout.spinner_item_list, specialitiesStr)
        select_specialities_spinner.setAdapter(cityAdapter)

    }

    private fun fuzzySearchRecyclerView(list: ArrayList<FuzzySearchMinModel?>?) {

        fuzzySearchRvAdapter =
            FuzzySearchRvAdapter(this, list, object :
                FuzzySearchRvAdapter.ClickItemListener {
                override fun onClicked(position: Int, model: FuzzySearchMinModel) {
                    val intent = Intent(
                        this@SearchAppointmentFilterActivity,
                        SearchAppointmentResultActivity::class.java
                    )

                    // hideKeyboard(member_ship_no)
                    intent.putExtra("startDate", startDate)
                    when {
                        model.description.equals("Doctor") -> {
                            intent.putExtra("SpecialityId", model.specialtyId)
                            intent.putExtra("DoctorId", model.id)
                            intent.putExtra("Date", "")
                            intent.putExtra("CompanyId", "")
                            intent.putExtra(LOGGED_IN_MODE, isLoggedInMode)
                            intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicine)
                            hideKeyboard(this@SearchAppointmentFilterActivity, member_ship_no)
                            startActivityForResult(intent, 111)
                        }
                        model.description.equals("Speciality") -> {
                            intent.putExtra("SpecialityId", model.id)
                            intent.putExtra("Speciality", model.name)
                            intent.putExtra("DoctorId", "")
                            intent.putExtra("Date", "")
                            intent.putExtra("CompanyId", "")
                            intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicine)
                            intent.putExtra(LOGGED_IN_MODE, isLoggedInMode)
                            hideKeyboard(this@SearchAppointmentFilterActivity, member_ship_no)
                            startActivityForResult(intent, 111)
                        }
                        model.description.equals("Location") -> {
                            intent.putExtra("SpecialityId", "")
                            intent.putExtra("DoctorId", "")
                            intent.putExtra("Date", "")
                            intent.putExtra("CompanyId", model.id)
                            intent.putExtra("CompanyName", model.name)
                            intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicine)
                            intent.putExtra(LOGGED_IN_MODE, isLoggedInMode)
                            hideKeyboard(this@SearchAppointmentFilterActivity, member_ship_no)
                            startActivityForResult(intent, 111)
                        }
                    }
                }
            })

        fuzzySearchRv!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        fuzzySearchRv!!.adapter = fuzzySearchRvAdapter
    }

    private fun favouriteDoctorRecyclerView(list: ArrayList<FavouriteDoctorsMiniModel?>?) {

        favouriteDoctorsRvAdapter =
            FavouriteDoctorsRvAdapter(this, list, object :
                FavouriteDoctorsRvAdapter.ClickItemListener {
                override fun onClicked(
                    position: Int,
                    model: FavouriteDoctorsMiniModel,
                    isFav: Boolean
                ) {
                    if (isFav) {
                        if (model.isFavorite) {
                            updateFavouriteDoctor(model.doctorId, false)
                        } else {
                            updateFavouriteDoctor(model.doctorId, true)
                        }
                    } else {
                        val intent = Intent(
                            this@SearchAppointmentFilterActivity,
                            SearchAppointmentResultActivity::class.java
                        )

                        // hideKeyboard(member_ship_no)
                        intent.putExtra("startDate", startDate)
                        intent.putExtra("SpecialityId", model.specialtyMasterId.toString())
                        intent.putExtra("DoctorId", model.doctorId.toString())
                        intent.putExtra("Date", "")
                        intent.putExtra("CompanyId", "")
                        intent.putExtra(Const.LOGGED_IN_MODE, true)
                        intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicine)
                        startActivityForResult(intent, 111)
                    }
                }
            })

        pastDoctorsRv!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        pastDoctorsRv!!.adapter = favouriteDoctorsRvAdapter
    }

    private fun getFuzzySearchResult(keyword: String) {
        //showWaitDialog()
        val securityToken = tinyDB.getString(Const.TOKEN_KEY)
        val fuzzySearchDTO = FuzzySearchDTO(securityToken, keyword)
        RestClient.getInstance()
            .apptGetFuzzySearch(fuzzySearchDTO) { result, status, errorMessage ->
                //val payStage2Result = result as MobileOpResult
                // hideWaitDialog()
                val fuzzySearchResponseModel = result as FuzzySearchResponseModel?

                if (fuzzySearchResponseModel != null) {
                    val mobileOpResult = fuzzySearchResponseModel.mobileOpResult
                    if (mobileOpResult != null) {
                        if (mobileOpResult.result == Success.SUCCESSCODE.value) {
                            if (fuzzySearchResponseModel.fuzzySearchList != null) {

                                loadFuzzySearchList(fuzzySearchResponseModel.fuzzySearchList)
                                // fuzzySearchRecyclerView(fuzzySearchResponseModel.fuzzySearchList)
                                exl_type_afternoon.setExpanded(true, true)
                                recycler_view_layout.visibility = View.VISIBLE
                            } else {
                                recycler_view_layout.visibility = View.GONE
                                exl_type_afternoon.collapse(true)
                                loadFuzzySearchList(fuzzySearchListTemp!!)
                                /*fuzzySearchList?.clear()
                                fuzzySearchRvAdapter.notifyDataSetChanged()*/
                            }

                        } else {
                            //   hideWaitDialog()
                            // UtilProgressBar.disMissDialogue();
                            var errorMesg = ""
                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.businessErrorMessageEn)) {
                                if (TextUtil.getEnglish(this)) errorMesg =
                                    """ ${mobileOpResult.businessErrorMessageEn}
     """.trimIndent() else if (TextUtil.getArabic(this)) errorMesg =
                                    """ ${mobileOpResult.businessErrorMessageAr}
      """.trimIndent()
                            }
                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.technicalErrorMessage)) {
                                errorMesg = """
                        ${errorMesg}Technical Info : 
                        ${mobileOpResult.technicalErrorMessage}
                        """.trimIndent()
                            }
                            ErrorMessage.getInstance().showError(this, errorMesg)
                        }
                    } else {
                        //   hideWaitDialog()
                        // UtilProgressBar.disMissDialogue();
                        ErrorMessage.getInstance().showError(this, errorMessage)
                    }
                } else {
                    ErrorMessage.getInstance().showError(this, errorMessage)
                }

            }
    }


    private fun updateFavouriteDoctor(mDoctorId: Long, favourite: Boolean) {
        showWaitDialog()
        val securityToken = TinyDB(this).getString(Const.TOKEN_KEY)
        val patientId = TinyDB(this).getLong(Const.PATIENT_ID, 0)
        val updateFavouriteDTO =
            UpdateFavouriteDTO(securityToken, patientId.toString(), mDoctorId.toString(), favourite)
        RestClient.getInstance()
            .updateFavDoctor(updateFavouriteDTO) { result, status, errorMessage ->
                //val payStage2Result = result as MobileOpResult
                hideWaitDialog()
                val mobileOpResult = result as MobileOpResult?

                if (mobileOpResult != null) {
                    if (mobileOpResult.result == Success.SUCCESSCODE.value) {
                        getFavouriteDoctors()
                    } else {
                        //   hideWaitDialog()
                        // UtilProgressBar.disMissDialogue();
                        var errorMesg = ""
                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.businessErrorMessageEn)) {
                            if (TextUtil.getEnglish(this)) errorMesg =
                                """ ${mobileOpResult.businessErrorMessageEn}
     """.trimIndent() else if (TextUtil.getArabic(this)) errorMesg =
                                """ ${mobileOpResult.businessErrorMessageAr}
      """.trimIndent()
                        }
                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.technicalErrorMessage)) {
                            errorMesg = """
                        ${errorMesg}Technical Info : 
                        ${mobileOpResult.technicalErrorMessage}
                        """.trimIndent()
                        }
                        ErrorMessage.getInstance().showError(this, errorMesg)
                    }
                } else {
                    //   hideWaitDialog()
                    // UtilProgressBar.disMissDialogue();
                    ErrorMessage.getInstance().showError(this, errorMessage)
                }

            }
    }

    private fun getFavouriteDoctors() {
        //showWaitDialog()
        val securityToken = TinyDB(this).getString(Const.TOKEN_KEY)
        val patientId = TinyDB(this).getLong(Const.PATIENT_ID, 0)

        val getFavouriteDTO = GetFavouriteDTO(securityToken, patientId.toString(), "0")
        RestClient.getInstance()
            .getFavouriteDoctors(getFavouriteDTO) { result, status, errorMessage ->
                //val payStage2Result = result as MobileOpResult
                // hideWaitDialog()
                val favouriteDoctorsResponseModel = result as FavouriteDoctorsResponseModel?

                if (favouriteDoctorsResponseModel != null) {
                    val mobileOpResult = favouriteDoctorsResponseModel.mobileOpResult
                    if (mobileOpResult != null) {
                        if (mobileOpResult.result == Success.SUCCESSCODE.value) {
                            if (favouriteDoctorsResponseModel.favouriteDoctorsMiniModels != null) {
                                pastDoctorsRv.visibility = View.VISIBLE
                                past_doctor_placeholder.visibility = View.GONE
                                loadFavouriteDoctors(favouriteDoctorsResponseModel.favouriteDoctorsMiniModels)

                            } else {
                                pastDoctorsRv.visibility = View.GONE
                                past_doctor_placeholder.visibility = View.VISIBLE
                            }

                        } else {
                            //   hideWaitDialog()
                            // UtilProgressBar.disMissDialogue();
                            var errorMesg = ""
                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.businessErrorMessageEn)) {
                                if (TextUtil.getEnglish(this)) errorMesg =
                                    """ ${mobileOpResult.businessErrorMessageEn}
     """.trimIndent() else if (TextUtil.getArabic(this)) errorMesg =
                                    """ ${mobileOpResult.businessErrorMessageAr}
      """.trimIndent()
                            }
                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.technicalErrorMessage)) {
                                errorMesg = """
                        ${errorMesg}Technical Info : 
                        ${mobileOpResult.technicalErrorMessage}
                        """.trimIndent()
                            }
                            ErrorMessage.getInstance().showError(this, errorMesg)
                        }
                    } else {
                        //   hideWaitDialog()
                        // UtilProgressBar.disMissDialogue();
                        ErrorMessage.getInstance().showError(this, errorMessage)
                    }
                } else {
                    ErrorMessage.getInstance().showError(this, errorMessage)
                }

            }
    }

    private fun loadFavouriteDoctors(favouriteDoctorsMiniModels: ArrayList<FavouriteDoctorsMiniModel>?) {
        favouriteDoctorsList?.clear()
        favouriteDoctorsList?.addAll(favouriteDoctorsMiniModels!!)
        favouriteDoctorsRvAdapter.notifyDataSetChanged()
    }

    private fun loadFuzzySearchList(fuzzySearchMinListModel: ArrayList<FuzzySearchMinModel?>) {
        /*  fuzzySearchList?.clear()
          fuzzySearchList?.addAll(fuzzySearchMinListModel)
          DiffUtil.calculateDiff(DiffUtil.Callback())
          fuzzySearchRvAdapter.notifyDataSetChanged()
  */

        val diffCallback = FuzzySearchCallback(fuzzySearchList!!, fuzzySearchMinListModel)
        val diffCourses = DiffUtil.calculateDiff(diffCallback)
        fuzzySearchList?.clear()
        fuzzySearchList?.addAll(fuzzySearchMinListModel)
        diffCourses.dispatchUpdatesTo(fuzzySearchRvAdapter)
    }

    //speciality list

    var doctorsProfiles: ArrayList<DoctorsProfile>? = null
    fun getDoctorBySpecilityId(specialityId: String?) {
        this.specialityId = specialityId ?: ""
        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog()
            val securityToken = tinyDB.getString(Const.TOKEN_KEY)
            var companyId = tinyDB.getString(Const.COMPANY_ID)
            if (isTelemedicine) {
                companyId = ""
            }

            val getRefSpecialtiesDTO =
                GetRefBySpecialtyIdDTO(securityToken, specialityId, isTelemedicine, companyId)
            RestClient.getInstance()
                .getRefDoctorsBySpecialty(getRefSpecialtiesDTO) { result, status, errorMessage ->
                    hideWaitDialog()
                    val getRefDoctorsBySpecialtyModel = result as GetRefDoctorsBySpecialtyModel
                    if (getRefDoctorsBySpecialtyModel != null) {
                        val mobileOpResult = getRefDoctorsBySpecialtyModel.mobileOpResult
                        if (mobileOpResult != null) {
                            if (mobileOpResult.result == Success.SUCCESSCODE.value) {
                                doctorsProfiles =
                                    getRefDoctorsBySpecialtyModel.doctorsProfileArrayList
                                if (!ValidationHelper.isNullOrEmpty(doctorsProfiles)) {
                                    setDoctors(doctorsProfiles)
                                } else {
                                    clearDoctorsSpinner()
                                }
                            } else {
                                var errorMesg = ""
                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.businessErrorMessageEn)) {
                                    if (TextUtil.getEnglish(this@SearchAppointmentFilterActivity)) {
                                        errorMesg = """
                                    ${mobileOpResult.businessErrorMessageEn}
                                    
                                    """.trimIndent()
                                    } else if (TextUtil.getArabic(this@SearchAppointmentFilterActivity)) {
                                        errorMesg = """
                                    ${mobileOpResult.businessErrorMessageAr}
                                    
                                    """.trimIndent()
                                    }
                                }
                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.technicalErrorMessage)) {
                                    errorMesg = """
                                ${errorMesg}Technical Info : 
                                ${mobileOpResult.technicalErrorMessage}
                                """.trimIndent()
                                }
                                ErrorMessage.getInstance()
                                    .showError(this@SearchAppointmentFilterActivity, errorMesg)
                            }
                        } else ErrorMessage.getInstance()
                            .showError(this@SearchAppointmentFilterActivity, errorMessage)
                    } else ErrorMessage.getInstance()
                        .showError(this@SearchAppointmentFilterActivity, errorMessage)
                }
        } else {
            ErrorMessage.getInstance().showWarning(this, "Internet is not available")
        }
    }

    private fun clearDoctorsSpinner() {
        val doctorsProfiles = ArrayList<DoctorsProfile>()
        val doctorsProfileAdapter =
            DoctorsProfileAdapter(this@SearchAppointmentFilterActivity, doctorsProfiles)
        //    listOfDoctors.adapter = doctorsProfileAdapter
        val doctorsSpeciality = ArrayList<String>()
        val cityAdapter: ListAdapter = SpinnerCustomAdapter(
            this@SearchAppointmentFilterActivity,
            R.layout.spinner_item_list,
            doctorsSpeciality
        )
        select_doctor_spinner.setAdapter(cityAdapter)
        select_doctor_spinner.setText("")
    }

    private var doctorsListTemp: ArrayList<String>? = ArrayList()
    fun setDoctors(doctorsProfiles: ArrayList<DoctorsProfile>?) {
        val doctorsSpeciality = ArrayList<String>()
        doctorsSpeciality.add(getString(R.string.all_doctors))
        for (doctorsProfile in doctorsProfiles!!) {
            if (TextUtil.getEnglish(this)) {
                if (!ValidationHelper.isNullOrEmpty(doctorsProfile.nameEn)) {
                    doctorsSpeciality.add(doctorsProfile.nameEn)
                }
            } else if (TextUtil.getArabic(this)) {
                if (!ValidationHelper.isNullOrEmpty(doctorsProfile.nameAr)) {
                    doctorsSpeciality.add(doctorsProfile.nameAr)
                }
            }
        }
        doctorsListTemp = doctorsSpeciality
        val cityAdapter: ListAdapter =
            SpinnerCustomAdapter(this, R.layout.spinner_item_list, doctorsSpeciality)
        select_doctor_spinner.setAdapter(cityAdapter)
        select_doctor_spinner.setText("")
        setDoctorOnList(doctorsProfiles)
    }

    var doctorsProfileAdapter: DoctorsProfileAdapter? = null

    fun setDoctorOnList(doctorsProfiles: ArrayList<DoctorsProfile>) {
        if (!ValidationHelper.isNullOrEmpty(doctorsProfiles)) {
            //  container_.visibility = View.VISIBLE
            doctorsProfileAdapter = DoctorsProfileAdapter(this, doctorsProfiles)
            // listOfDoctors.adapter = doctorsProfileAdapter
            //doctorsProfileAdapter!!.setOnCardClickListner(this)
            if (doctorsProfiles.size == 1) {
                // if (TextUtil.getEnglish(this)) select_doctor_spinner.setText(doctorsProfiles[0]!!.nameEn) else if (TextUtil.getArabic(this)) select_doctor_spinner.setText(doctorsProfiles[0]!!.nameAr)
                // navigationInVisible()
            } else {
                // navigationVisible()
            }
        } else {
            // navigationInVisible()
        }
    }

    fun getDoctorId(doctorName: String): String? {
        var doctorId = ""
        for (doctorsProfile in doctorsProfiles!!) {
            if (doctorsProfile != null) {
                if (TextUtil.getEnglish(this)) {
                    if (doctorsProfile.nameEn.trim { it <= ' ' }
                            .equals(doctorName.trim { it <= ' ' }, ignoreCase = true)) {
                        doctorId = doctorsProfile.doctorId.toString()
                    }
                } else if (TextUtil.getArabic(this)) {
                    if (doctorsProfile.nameAr.trim { it <= ' ' }
                            .equals(doctorName.trim { it <= ' ' }, ignoreCase = true)) {
                        doctorId = doctorsProfile.doctorId.toString()
                    }
                }
            }
        }
        return doctorId
    }

    private fun LightSpinnerEdittext.disableCopyPaste() {
        isLongClickable = false
        setTextIsSelectable(false)
        customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu): Boolean {
                return false
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {}
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == RESULT_OK) {
            finish()
        }
    }

    private fun transformView() {
        if (TextUtil.getArabic(this)) {
            dateAppointment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0);
            // select_specialities_spinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);
            // select_doctor_spinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);
            // location_spiiner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);
            confirmBooking_btn.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.arrow_left,
                0,
                0,
                0
            );
            dateRelLayout.layoutDirection = View.LAYOUT_DIRECTION_RTL
            specialityRelLayout.layoutDirection = View.LAYOUT_DIRECTION_RTL
            doctorRelLayout.layoutDirection = View.LAYOUT_DIRECTION_RTL
            search_location_layout.layoutDirection = View.LAYOUT_DIRECTION_RTL
            //select_doctor_spinner.keyListener = DigitsKeyListener.getInstance("0123456789.");
        }
    }

}

public class FuzzySearchCallback(
    private val oldList: ArrayList<FuzzySearchMinModel?>,
    private val newList: ArrayList<FuzzySearchMinModel?>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]?.id === newList.get(newItemPosition)?.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition]?.id == newList[newItemPosition]?.id -> true
            oldList[oldItemPosition]?.name == newList[newItemPosition]?.name -> true
            oldList[oldItemPosition]?.description == newList[newItemPosition]?.description -> true
            oldList[oldItemPosition]?.arabicName == newList[newItemPosition]?.arabicName -> true
            else -> false
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}