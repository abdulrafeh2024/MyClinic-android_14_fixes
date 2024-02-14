package com.telemedicine.myclinic.viewmodels

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.telemedicine.myclinic.base.BaseViewModel
import com.telemedicine.myclinic.eenum.Success
import com.telemedicine.myclinic.models.MobileOpResult
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel
import com.telemedicine.myclinic.models.bookAppointment.GetRefSpecialtiesDTO
import com.telemedicine.myclinic.models.bookAppointment.SpecialtiesModel
import com.telemedicine.myclinic.models.company.Company
import com.telemedicine.myclinic.models.onlineConfig.OpsConfigsModel
import com.telemedicine.myclinic.networks.rest.services.ApptService
import com.telemedicine.myclinic.util.Const
import com.telemedicine.myclinic.util.TinyDB
import java.lang.Exception
import java.util.ArrayList
import javax.inject.Inject

class ReScheduleApptViewModel @Inject constructor(private val tinyDB: TinyDB,private val apptService: ApptService) : BaseViewModel() {

    private val _apptsMiniModel: MutableLiveData<ApptsMiniModel?> by lazy {
        MutableLiveData<ApptsMiniModel?>()
    }

    val apptsMiniModel: LiveData<ApptsMiniModel?> = _apptsMiniModel

    var isEnglish : Boolean = true

    var sepecilityModel :SpecialtiesModel? = null

    var apptDate : String ? = null
    var isTeleMed : Boolean ? = false


    private val _localizeErrorMessage: MutableLiveData<MobileOpResult?> by lazy {
        MutableLiveData<MobileOpResult?>()
    }

    val localizeErrorMessage: LiveData<MobileOpResult?> = _localizeErrorMessage

    var companies : ArrayList<Company> = ArrayList()

    fun loadCompanies(){
        companies =  tinyDB.getListCompany(Const.COMPANY_LIST)
    }

    fun setApptsMiniModel(apptsMiniModel:ApptsMiniModel?) {

        _apptsMiniModel.postValue(apptsMiniModel)

        apptsMiniModel?.let {
            //set selected company
            val sepecility = if(isEnglish){
                it.doctorProfile.specialtyEn
            }else{
                it.doctorProfile.specialtyAr
            }

            isTeleMed = it.isTelemedicine

            if(it.doctorProfile.specialtyId == null){
                getRefSpecialties(it.isTelemedicine,it.company,sepecility, "")
            }else{
                getRefSpecialties(it.isTelemedicine,it.company,sepecility, it.doctorProfile.specialtyId)
            }
        }
    }

    private fun getRefSpecialties(isTelemedicine : Boolean,companyID:String,specialty:String, specialityId: String){

         val securityToken = tinyDB.getString(Const.TOKEN_KEY)

         tinyDB.putString(Const.COMPANY_ID, companyID)

         val getRefSpecialtiesDTO = GetRefSpecialtiesDTO(securityToken,isTelemedicine, companyID)

         _loading.postValue(true)
         compositeDisposable.add(apptService.getRefSpecialties(getRefSpecialtiesDTO)
                 .compose(applySchedulers())
                 .subscribe({

                     _loading.postValue(false)

                     val mobileOpResult: MobileOpResult = it.mobileOpResult

                     if(mobileOpResult !=null){
                            if (mobileOpResult.result == Success.SUCCESSCODE.value) {
                                //
                                    try{
                                        if(isEnglish){
                                            sepecilityModel = it.specialtiesList.first{ it->
                                                it.nameEn.equals(specialty,false)
                                                || it.id.toString().equals(specialityId, false)
                                            }
                                        }else{
                                            sepecilityModel = it.specialtiesList.first { it->
                                                it.nameAr.equals(specialty,false)
                                                        || it.id.toString().equals(specialityId, false)
                                            }
                                        }
                                    }catch (e: Exception){
                                        e
                                    }

                            }else{
                                _localizeErrorMessage.postValue(mobileOpResult)
                            }
                        }

                 }, {
                     _loading.postValue(false)
                     onHandleError(it)
                 }))

     }

    fun iSCompanyChangeAble() : Boolean{
        val configsModels =   tinyDB.getListObject(Const.ONLINE_CONFIG_KEY, OpsConfigsModel::class.java)

        if(configsModels.size > 0){
           val configsModel =  configsModels.first { model -> (model as OpsConfigsModel)
                   .opsConfigId == 24L}
                   as  OpsConfigsModel

            return configsModel.`val`.equals("true",true)
        }
        return false
    }
}