package com.telemedicine.myclinic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.telemedicine.myclinic.base.BaseViewModel
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel
import com.telemedicine.myclinic.models.company.Company
import com.telemedicine.myclinic.models.profilecreatoinmodels.SecurityTokenDTO
import com.telemedicine.myclinic.networks.rest.services.CompanyService
import com.telemedicine.myclinic.networks.rest.services.response.CompanyListResponse
import com.telemedicine.myclinic.util.Const
import com.telemedicine.myclinic.util.LocaleDateHelper
import com.telemedicine.myclinic.util.TinyDB
import java.util.*
import javax.inject.Inject

class DashboardViewModel @Inject constructor(private val companyService: CompanyService,private  val db: TinyDB) : BaseViewModel() {

    var isTeleAppointment : Boolean = false

    var companies : ArrayList<Company> = ArrayList()

    private val _companyResponse: MutableLiveData<CompanyListResponse> by lazy {
        MutableLiveData<CompanyListResponse>()
    }

    val companyResponse: LiveData<CompanyListResponse> = _companyResponse

    fun loadCompanies(token: SecurityTokenDTO){

        compositeDisposable.add(companyService.getRefCompanies(token)
                .compose(applySchedulers())
                .subscribe({
                    companies = it.companyList
                    _companyResponse.postValue(it)
                    db.putListCompany(Const.COMPANY_LIST,it.companyList)
                },{
                }))
    }

     fun wipedOutAppointmentData(){
        val appointmentList = ArrayList<ApptsMiniModel>()
        if (db.getListAppointment(Const.APPOINTMENT_LIST).isNotEmpty()) {
            for (apptList in db.getListAppointment(Const.APPOINTMENT_LIST)) {
                if (!LocaleDateHelper.isAppointmentPassedTwoDays(apptList.apptDate)) {
                    appointmentList.add(apptList)
                }
            }
            db.putListAppointments(Const.APPOINTMENT_LIST, appointmentList)
        }
    }
}