package com.telemedicine.myclinic.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.telemedicine.myclinic.adapters.MedicationDeliveriesAdapter
import com.telemedicine.myclinic.adapters.MyAddressAdapter
import com.telemedicine.myclinic.eenum.Success
import com.telemedicine.myclinic.models.FuzzySearchMinModel
import com.telemedicine.myclinic.models.MobileOpResult
import com.telemedicine.myclinic.models.homevisit.*
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.util.*
import com.telemedicine.myclinic.webservices.RestClient
import kotlinx.android.synthetic.main.activity_my_address_book.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import java.util.ArrayList

class MyAddressBookActivity : BaseActivity() {

    private var addressBookList: ArrayList<PatientAddressBookList> = ArrayList()
    private lateinit var myAddressAdapter: MyAddressAdapter
    private var label: String? = null
    private var id: Long? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        addBtn.setOnClickListener {
            val intent = Intent(this@MyAddressBookActivity, AddNewAddressActivity::class.java)
            startActivity(intent)
        }
        getPatientAddressBook()

        editAdress()

    }

    override fun onResume() {
        super.onResume()

        getPatientAddressBook()
    }

    private fun getPatientAddressBook() {
        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog()

            val securityToken = TinyDB(this).getString(Const.TOKEN_KEY)
            val patientId = TinyDB(this).getLong(Const.PATIENT_ID, 0)

            if (!ValidationHelper.isNullOrEmpty(securityToken) && patientId != 0L) {
                val getOrdersByPatientIdDTO =
                    GetOrdersByPatientIdDTO(securityToken, patientId.toString())
                RestClient.getInstance().getPatientAddressBook(
                    getOrdersByPatientIdDTO
                ) { result, status, errorMessage ->
                    hideWaitDialog()
                    val patientAddressBookResponse = result as PatientAddressBookResponse
                    if (patientAddressBookResponse != null) {
                        val mobileOpResult = patientAddressBookResponse.mobileOpResult
                        if (mobileOpResult != null) {
                            if (mobileOpResult.result == Success.SUCCESSCODE.value) {
                                val addressList = patientAddressBookResponse.patientAddressBookList
                                if (!ValidationHelper.isNullOrEmpty(addressList)) {
                                    loadAddressList(addressList)
                                }
                            } else {
                                var errorMesg = ""
                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.businessErrorMessageEn)) {
                                    if (TextUtil.getEnglish(this@MyAddressBookActivity)) errorMesg =
                                        """
     ${mobileOpResult.businessErrorMessageEn}
     
     """.trimIndent() else if (TextUtil.getArabic(
                                            this@MyAddressBookActivity
                                        )
                                    ) errorMesg = """
     ${mobileOpResult.businessErrorMessageAr}
     
     """.trimIndent()
                                }
                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.technicalErrorMessage)) {
                                    errorMesg = """
                                ${errorMesg}Technical Info : 
                                ${mobileOpResult.technicalErrorMessage}
                                """.trimIndent()
                                }
                                ErrorMessage.getInstance()
                                    .showError(this@MyAddressBookActivity, errorMesg)
                            }
                        } else {
                            ErrorMessage.getInstance()
                                .showError(this@MyAddressBookActivity, errorMessage)
                        }
                    } else {
                        ErrorMessage.getInstance()
                            .showError(this@MyAddressBookActivity, errorMessage)
                    }
                }
            }
        } else {
            ErrorMessage.getInstance().showError(this, getString(R.string.internet_connection))
        }
    }


    private fun deleteTheAddress(addressId: Long) {
        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog()

            val securityToken = TinyDB(this).getString(Const.TOKEN_KEY)

            if (!ValidationHelper.isNullOrEmpty(securityToken) && addressId != 0L) {
                val addressBookDeleteDTO =
                    AddressBookDeleteDTO(securityToken, addressId)
                RestClient.getInstance().AddressBookDelete(
                    addressBookDeleteDTO
                ) { result, status, errorMessage ->
                    hideWaitDialog()

                    val mobileOpResult = result as MobileOpResult
                        if (mobileOpResult != null) {
                            if (mobileOpResult.result == Success.SUCCESSCODE.value) {
                                getPatientAddressBook()
                            } else {
                                var errorMesg = ""
                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.businessErrorMessageEn)) {
                                    if (TextUtil.getEnglish(this@MyAddressBookActivity)) errorMesg =
                                        """
     ${mobileOpResult.businessErrorMessageEn}
     
     """.trimIndent() else if (TextUtil.getArabic(
                                            this@MyAddressBookActivity
                                        )
                                    ) errorMesg = """
     ${mobileOpResult.businessErrorMessageAr}
     
     """.trimIndent()
                                }
                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.technicalErrorMessage)) {
                                    errorMesg = """
                                ${errorMesg}Technical Info : 
                                ${mobileOpResult.technicalErrorMessage}
                                """.trimIndent()
                                }
                                ErrorMessage.getInstance()
                                    .showError(this@MyAddressBookActivity, errorMesg)
                            }
                        } else {
                            ErrorMessage.getInstance()
                                .showError(this@MyAddressBookActivity, errorMessage)
                        }
                }
            }
        } else {
            ErrorMessage.getInstance().showError(this, getString(R.string.internet_connection))
        }
    }

    private fun loadAddressList(addressList: List<PatientAddressBookList>) {
        addressBookList.clear()
        addressBookList.addAll(addressList)
        myAddressAdapter.notifyDataSetChanged()
    }

    override fun layout(): Int {
        return R.layout.activity_my_address_book
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().unregister(this)
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
                    this@MyAddressBookActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameAr + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            } else {
                ErrorMessage.getInstance().showSuccessGreen(
                    this@MyAddressBookActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameEn + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            }
        } else {
            ErrorMessage.getInstance()
                .showErrorYellow(this@MyAddressBookActivity, appointmentEvent.errorMSg)
        }
    }

    private fun init() {
        myAddressAdapter =
            MyAddressAdapter(this, addressBookList, object :
                MyAddressAdapter.ClickItemListener {
                override fun onClicked(position: Int, model: PatientAddressBookList, isDelete: Boolean) {
                    if(isDelete){
                        deleteTheAddress(model.id)
                    }else{
                        populateAddressDetail(model)
                    }
                }
            })

        address_book_rv!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        address_book_rv!!.adapter = myAddressAdapter
    }

    private fun populateAddressDetail(model: PatientAddressBookList) {
        district_tv.text = model.district?:"null"
        street_tv.text = model.street?:"null"
        building_name_tv.text = model.buildingName?:"null"
        building_no_tv.text = model.buildingNo.toString()?:"null"
        apt_no_tv.text = model.aprtNo.toString()?:"null"
        address_tv.text = model.addressNotes?:"null"
        label = model.lable?:"null"
        id = model.id?: 0
    }

    private fun passingDataThroughIntent(){
        val district = district_tv.text.toString()
        val street = street_tv.text.toString()
        val building_name = building_name_tv.text.toString()
        val building_no = building_no_tv.text.toString()
        val apt_no = apt_no_tv.text.toString()
        val address = address_tv.text.toString()

        val intent = Intent(this@MyAddressBookActivity, AddNewAddressActivity::class.java)
        intent.putExtra("district",district)
        intent.putExtra("street",street)
        intent.putExtra("building_name",building_name)
        intent.putExtra("building_no",building_no)
        intent.putExtra("apt_no",apt_no)
        intent.putExtra("address",address)
        intent.putExtra("label",label)
        intent.putExtra("id",id)
        startActivity(intent)
    }

    private fun editAdress(){
        fullScreenBtn.setOnClickListener{
            if(district_tv.text.toString().isNullOrEmpty()){

            }else {
                passingDataThroughIntent()
            }
        }
    }
}