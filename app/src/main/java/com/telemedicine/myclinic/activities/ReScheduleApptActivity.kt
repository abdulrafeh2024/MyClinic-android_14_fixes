package com.telemedicine.myclinic.activities


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.github.marlonlom.utilities.timeago.TimeAgoMessages
import com.google.gson.GsonBuilder
import com.telemedicine.myclinic.fragments.CompanySelectionFragment
import com.telemedicine.myclinic.injection.ViewModelFactory
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel
import com.telemedicine.myclinic.models.company.Company
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.util.*
import com.telemedicine.myclinic.util.Const.*
import com.telemedicine.myclinic.viewmodels.ReScheduleApptViewModel
import kotlinx.android.synthetic.main.activity_re_schedule_appt.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class ReScheduleApptActivity : BaseActivity(), CompanySelectionFragment.OnCompanySelectListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: ReScheduleApptViewModel

    private lateinit var messages: TimeAgoMessages

    private val myCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (TextUtil.getEnglish(this)) {
            val localeBylanguageTag = Locale.forLanguageTag("en")
            messages = TimeAgoMessages.Builder().withLocale(localeBylanguageTag).build()
        } else if (TextUtil.getArabic(this)) {
            val localeBylanguageTag = Locale.forLanguageTag("ar")
            messages = TimeAgoMessages.Builder().withLocale(localeBylanguageTag).build()
        }


        viewModel = ViewModelProvider(this, viewModelFactory).get(ReScheduleApptViewModel::class.java)

        val apptsMiniModelJson = intent.getStringExtra(APPTMODEL)
      //  viewModel.setApptsMiniModel(intent.extras?.getParcelable<ApptsMiniModel>(Const.APPTMODEL))
        viewModel.setApptsMiniModel(GsonBuilder().create().fromJson(apptsMiniModelJson, ApptsMiniModel::class.java))

        viewModel.loadCompanies()

        if (TextUtil.getArabic(this)) {
            company_name.setText(tinyDB.getString(Const.COMPANY_NAME_AR))
        } else {
            company_name.setText(tinyDB.getString(Const.COMPANY_NAME_EN))
        }

        if (!viewModel.isTeleMed!!) {
            company_name.visibility = View.VISIBLE
            selectCompoanyLabel.visibility = View.VISIBLE
            company_til.visibility = View.VISIBLE
        } else {
            company_name.visibility = View.GONE
            selectCompoanyLabel.visibility = View.GONE
            company_til.visibility = View.GONE
        }

        subscribeObserver()
        transformView()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            tinyDB.putBoolean(Const.BACK_TO_HOME, true)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun inject() {
        super.inject()
        component.inject(this)
    }

    override fun layout(): Int {
        return R.layout.activity_re_schedule_appt
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
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
                    this@ReScheduleApptActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameAr + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            } else {
                ErrorMessage.getInstance().showSuccessGreen(
                    this@ReScheduleApptActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameEn + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            }
        } else {
            ErrorMessage.getInstance()
                .showErrorYellow(this@ReScheduleApptActivity, appointmentEvent.errorMSg)
        }
    }
    @OnClick(R.id.selectedCompany)
    fun onSelectCompanyClicked() {
        CompanySelectionFragment.newInstance(viewModel.companies)
                .show(supportFragmentManager, "CompanySelectionFragment")
    }

    @OnClick(R.id.toolbar_left_iv)
    fun backPressed() {
        onBackPressed()
    }

    var selectedDate = ""
    @OnClick(R.id.apptDateEdt)
    fun apptDatePickerClicked() {
        val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth

            //date for the time slot activity
            val selectedDateFormat = "yyyy-MM-dd'T'hh:mm:ss"
            val selectedDateSdf = SimpleDateFormat(selectedDateFormat, Locale.ENGLISH)
            selectedDate = selectedDateSdf.format(myCalendar.time)


            val myFormat = "EEEE, dd MMMM yyyy hh:mm a" //"EEEE, dd MMMM yyyy" //In which you need put here

            val sdf = SimpleDateFormat(myFormat, Locale.getDefault())

            apptDateEdt.setText(sdf.format(myCalendar.time), TextView.BufferType.EDITABLE)
            viewModel.apptDate = apptDateEdt?.text?.toString()

        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))

        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.setCanceledOnTouchOutside(false)
        datePicker.show()
    }

    @OnClick(R.id.reschedule)
    fun onResheduledClicked() {

        viewModel.apptsMiniModel.value?.let { apptsMiniModel ->

            val doctorsProfile: DoctorsProfile = apptsMiniModel.doctorProfile
            /* var docName: String? = ""
             var speciality: String? = ""
             val intent = Intent(this, BookAppointmentTwoActivity::class.java)

             if (TextUtil.getEnglish(this)) {
                 speciality = doctorsProfile.specialtyEn
                 docName = doctorsProfile.nameEn
             } else if (TextUtil.getArabic(this)) {
                 docName = doctorsProfile.nameAr
                 speciality = doctorsProfile.specialtyAr
             }*/

            /* intent.putExtra(Const.DOCTOR_NAME, docName)
             intent.putExtra(Const.DOCTOR_SPECIALITY, speciality)
             intent.putExtra(Const.DATE, viewModel.apptDate)
             intent.putExtra(Const.SPECIALITY_ID, viewModel.sepecilityModel?.id.toString())
             intent.putExtra(Const.DOCTOR_ID, doctorsProfile.doctorId.toString())
             intent.putExtra(Const.ISTELEMEDICINE_KEY, apptsMiniModel.isTelemedicine)
             intent.putExtra(Const.DOCTOR_IMAGE_URL, doctorsProfile.profilePicUrl)
             intent.putExtra(Const.APPT_ID, apptsMiniModel.apptId)*/
            val intent = Intent(this, TimeSlotActivity::class.java);
            intent.putExtra("DoctorId", apptsMiniModel.doctorProfile.doctorId);
            intent.putExtra("Company", apptsMiniModel.company);
             tinyDB.putString(Const.COMPANY_ID, apptsMiniModel.company);
            intent.putExtra("startDate", "");
            intent.putExtra(LOGGED_IN_MODE, true);
            intent.putExtra(Const.APPT_ID, apptsMiniModel.apptId)
            intent.putExtra(SPECIALITY_NAME_EN, apptsMiniModel.doctorProfile.specialtyEn);
            intent.putExtra(SPECIALITY_NAME_AR, apptsMiniModel.doctorProfile.specialtyAr);
            intent.putExtra(ISTELEMEDICINE_KEY, apptsMiniModel.isTelemedicine);
            if (TextUtil.getArabic(this)) {
                intent.putExtra("DoctorName", apptsMiniModel.doctorProfile.nameAr ?: "");
            } else {
                intent.putExtra("DoctorName", apptsMiniModel.doctorProfile.nameEn ?: "");
            }

            if(selectedDate.isEmpty()){
                intent.putExtra("week_Day", weekDay);
                intent.putExtra("Date", apptsMiniModel.apptDate);
            }else{
                intent.putExtra("Date", selectedDate);
                weekDay = LocaleDateHelper.convertDateStringFormatWeekDay(selectedDate,"yyyy-MM-dd'T'HH:mm:ss", "EEEE").toString()
                intent.putExtra("week_Day", weekDay);
            }

            intent.putExtra("Time", "");
            intent.putExtra("image", apptsMiniModel.doctorProfile.profilePicUrl);
            intent.putExtra("specialityId", apptsMiniModel.doctorProfile.specialtyId);
            startActivityForResult(intent, 111)
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
        }

    }

    var weekDay = ""
    private fun subscribeObserver() {
        viewModel.apptsMiniModel.observe(this, Observer {
            it?.let {
                //enable or disable company picker
                selectedCompany.isEnabled = viewModel.iSCompanyChangeAble()

                reschedule.isEnabled = true
                it.doctorProfile?.let { doctorProfile ->


                    //set doctor profile image
                    Glide.with(this)
                            .load(doctorProfile.profilePicUrl)
                            .placeholder(R.drawable.doctr)
                            .into(doctor_image)
                    //set doctor name and profession
                    if (TextUtil.getEnglish(this)) {
                        doctor_name.text = doctorProfile.nameEn
                        doctor_profession.text = doctorProfile.specialtyEn
                    } else if (TextUtil.getArabic(this)) {
                        doctor_name.text = doctorProfile.nameAr
                        doctor_profession.text = doctorProfile.specialtyAr
                    }

                    if (TextUtil.getArabic(this))
                        selectedCompany.setText(it.companyNameAr, TextView.BufferType.EDITABLE)
                    else
                        selectedCompany.setText(it.companyNameEn, TextView.BufferType.EDITABLE)

                }
                val date = LocaleDateHelper.convertDateStringFormat(it.apptDate, "yyyy-MM-dd'T'HH:mm:ss", "EEEE, dd MMMM yyyy hh:mm a")
                dateTime.text = date
                weekDay = LocaleDateHelper.convertDateStringFormatWeekDay(it.apptDate,"yyyy-MM-dd'T'HH:mm:ss", "EEEE").toString()
                viewModel.apptDate = date
                apptDateEdt.setText(date, TextView.BufferType.EDITABLE)

                val dateLong = LocaleDateHelper.convertDateFormat(date, "EEEE, dd MMMM yyyy hh:mm a")
                val dateAgo = TimeAgo.using(dateLong, messages)

                val dateIn = dateAgo.replace("within".toRegex(), getString(R.string.`in`))

                if (!ValidationHelper.isNullOrEmpty(dateIn)) {
                    val days = LocaleDateHelper.getCountOfDays("", it.apptDate)
                    if (days.equals("0", ignoreCase = true)) {
                        apptTime.setText(R.string.today)
                    } else if (days.equals("1", ignoreCase = true)) {
                        apptTime.setText(R.string.tomorrow)
                    } /*else if (days.equalsIgnoreCase("2")) {
                        apptTime.setText(R.string.in_2_days);
                    }*/ else {
                        var d = 0
                        if (!ValidationHelper.isNullOrEmpty(days)) d = Integer.valueOf(days)
                        if (d > 1) {
                            d = d - 1
                            val gy = getString(R.string.`in`) + " " + d + " " + getString(R.string.days)
                            apptTime.text = gy
                        } else apptTime.text = dateIn
                    }
                }
            }
        })


        viewModel.localizeErrorMessage.observe(this, Observer { mobileOpResult ->
            var errorMesg = ""
            mobileOpResult?.let {
                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.businessErrorMessageEn)) {
                    if (TextUtil.getEnglish(this@ReScheduleApptActivity)) errorMesg = mobileOpResult.businessErrorMessageEn + "\n" else if (TextUtil.getArabic(this@ReScheduleApptActivity)) errorMesg = mobileOpResult.businessErrorMessageAr + "\n"
                }

                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.technicalErrorMessage)) {
                    errorMesg = """
                    ${errorMesg}Technical Info : 
                    ${mobileOpResult.technicalErrorMessage}
                    """.trimIndent()
                }
                ErrorMessage.getInstance().showError(this@ReScheduleApptActivity, errorMesg)
            }
        })

    }

    fun transformView() {
        if (TextUtil.getArabic(this)) {
            toolbar_left_iv.setRotation(180f)
            reschedule.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0)
            selectedCompany.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0)
            apptDateEdt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0)
        }
    }

    override fun onCompanySelected(company: Company) {
        if (TextUtil.getArabic(this))
            selectedCompany.setText(company.companyFullAr, TextView.BufferType.EDITABLE)
        else
            selectedCompany.setText(company.companyFullEn, TextView.BufferType.EDITABLE)

        //set default company
        tinyDB.putString(Const.COMPANY_ID, company.companyId)
    }


    companion object {
        const val RC_RESCHEDULED = 123
        fun getLaunchIntent(context: Context, apptsModel: ApptsMiniModel): Intent {
            val jsonObject = GsonBuilder().create().toJson(apptsModel)
            return Intent(context, ReScheduleApptActivity::class.java)
                    .putExtra(Const.APPTMODEL, jsonObject)

            /*return Intent(context, ReScheduleApptActivity::class.java)
                .putExtra(Const.APPTMODEL, apptsModel)*/
        }
    }
}