package com.telemedicine.myclinic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.telemedicine.myclinic.base.BaseViewModel
import com.telemedicine.myclinic.eenum.Success
import com.telemedicine.myclinic.models.MobileOpResult
import com.telemedicine.myclinic.models.patientMiniModels.PatientsMiniModel
import com.telemedicine.myclinic.models.profileInsuranceValidate.InsuranceModel
import com.telemedicine.myclinic.models.profileInsuranceValidate.ProfileInsuranceValidateDTO
import com.telemedicine.myclinic.models.profileUpdate.GetProfileDTO
import com.telemedicine.myclinic.models.profileUpdate.ProfileUpdateModel
import com.telemedicine.myclinic.models.profilecreatoinmodels.*
import com.telemedicine.myclinic.networks.rest.services.ProfileServices
import com.telemedicine.myclinic.networks.rest.services.request.*
import com.telemedicine.myclinic.networks.rest.services.response.NonSaudiInfoResult
import com.telemedicine.myclinic.networks.rest.services.response.SaudiInfoResult
import com.telemedicine.myclinic.util.Const
import com.telemedicine.myclinic.util.Const.IS_TENT
import com.telemedicine.myclinic.util.TinyDB
import com.telemedicine.myclinic.util.ValidationHelper
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class ProfileRegistrationViewModel @Inject constructor(private val profiileServices: ProfileServices, private val tinyDB: TinyDB) : BaseViewModel() {

    val calender = Calendar.getInstance()
    var calenderHijri = UmmalquraCalendar()
    var calenderDateOfBirth = Calendar.getInstance()
    var yakeenDateOfBirth = ""

    var isHijriCalender = false
    var citiesList: ArrayList<Cities>? = null
    var districtsList: ArrayList<District>? = null
    var nationalitiesList: ArrayList<Nationalities>? = null
    var insuranceCarriersList: ArrayList<InsuranceCarriers>? = null
    var NonSaudiInfoResult: NonSaudiInfoResult? = null
    var saudiInfoResult: SaudiInfoResult? = null

    var hasProfileValidate = false
    var hasProfileInsuranceValidate = false

    var gender: String? = ""
    var birthDate: String? = ""
    var firstName: String? = ""
    var middleName: String? = ""
    var lastName: String? = ""

    var firstNameAr: String? = ""
    var middleNameAr: String? = ""
    var lastNameAr: String? = ""


    var nationalIdType: String? = ""
    var nationalId: String? = ""
    var nationalIdTypeInt: String? = ""
    var idCardScanBase64: String? = ""
    var nationality: String? = ""
    var country: String? = ""
    var nationalIdExpiryDate: String? = ""

    var memberShipNo: String? = null
    var policyNo: String? = null
    var insuranceCarrierType = 0L
    var insuranceCarrier: String? = null
    var insuranceCardBase64: String? = ""
    var insuranceExpiryDate: String? = ""
    var isAddPatient = false

    var isAddProfile = false
    var isFromRegister = false

    var patientsMiniModel: PatientsMiniModel? = null

    private val _localizeErrorMessage: MutableLiveData<MobileOpResult?> by lazy {
        MutableLiveData<MobileOpResult?>()
    }

    val localizeErrorMessage: LiveData<MobileOpResult?> = _localizeErrorMessage

    private val _yakeenErrorMessage: MutableLiveData<MobileOpResult?> by lazy {
        MutableLiveData<MobileOpResult?>()
    }

    val yakeenErrorMessage: LiveData<MobileOpResult?> = _yakeenErrorMessage


    private val _resultProfileRef: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val resultProfileRef: LiveData<Boolean> = _resultProfileRef

    //patient profile by id
    private val _resultProfileRefById: MutableLiveData<ProfileUpdateModel?> by lazy {
        MutableLiveData<ProfileUpdateModel?>()
    }

    val resultProfileRefById: LiveData<ProfileUpdateModel?> = _resultProfileRefById

    private val _resultProfileValidate: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val resultProfileValidate: LiveData<Boolean> = _resultProfileValidate


    private val _resultIntelliSearch: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val resultIntelliSearch: LiveData<Boolean> = _resultIntelliSearch

    private val _resultIntelliSearchSaudi: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val resultIntelliSearchSaudi: LiveData<Boolean> = _resultIntelliSearchSaudi


    private val _resultProfileInsuranceValidate: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val resultProfileInsuranceValidate: LiveData<Boolean> = _resultProfileInsuranceValidate


    private val _resultProfileCreate: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val resultProfileCreate: LiveData<Boolean> = _resultProfileCreate


    var insuranceModel: InsuranceModel? = null

    fun resetNationalIdLayout() {
        nationalId = null
        hasProfileValidate = false
        _resultProfileValidate.postValue(false)
    }

    fun isTentative(): Boolean {
        return tinyDB.getBoolean(IS_TENT)
    }

    fun setTentative(isTentative: Boolean) {
        tinyDB.putBoolean(IS_TENT, isTentative)
    }

    fun validateProfile(nationalId: String, memberShipNo: String = "null", yakeenBirthDay: String, isIqama: Boolean) {

        this.nationalId = nationalId
        this.memberShipNo = memberShipNo

        val securityToken: String = tinyDB.getString(Const.TOKEN_KEY)

        _loading.postValue(true)

        val bodyRequest = ProfileValidateRequest(memberShipNo, securityToken, nationalId)
        compositeDisposable.add(profiileServices.profileValidate(bodyRequest)
                .compose(applySchedulers())
                .subscribe({ profileValidateResponse ->
                    _loading.postValue(false)

                    val mobileOpResult = profileValidateResponse.mobileOpResult
                    hasProfileValidate = if (mobileOpResult != null && mobileOpResult.result == Success.SUCCESSCODE.value) {
                        if (isIqama) {
                            getIntelliSearchInfoForNonSaudi(nationalId, yakeenBirthDay)
                        } else {
                            getIntelliSearchInfoForSaudi(nationalId, yakeenBirthDay)
                        }
                        _resultProfileValidate.postValue(true)
                        true
                    } else {
                        _localizeErrorMessage.postValue(mobileOpResult)
                        false
                    }

                }, {
                    hasProfileValidate = false
                    _loading.postValue(false)
                    onHandleError(it)
                }))
    }

    fun getIntelliSearchInfoForNonSaudi(yakeenNationalId: String, yakeenBirthDay: String) {

        val bodyRequestYakeen = GetIntelliSearchForNonSaudi(yakeenNationalId, yakeenBirthDay)
        compositeDisposable.add(profiileServices.getIntelliSearchInfoForNonSaudi(bodyRequestYakeen)
            .compose(applySchedulers())
            .subscribe({ getIntelliSearchInfoForNonSaudiResponse ->
                _loading.postValue(false)

                val mobileOpResult = getIntelliSearchInfoForNonSaudiResponse.mobileOpResult
                val intelliSearchResponse = getIntelliSearchInfoForNonSaudiResponse.nonSaudiInfoResult
                if (mobileOpResult != null && mobileOpResult.result == Success.SUCCESSCODE.value) {
                    NonSaudiInfoResult = intelliSearchResponse
                    _resultIntelliSearch.postValue(true)
                } else {
                    NonSaudiInfoResult = null
                }

            }, {
                // hasProfileValidate = false
                // _loading.postValue(false)
                // onHandleError(it)
            }))
    }

    fun getIntelliSearchInfoForSaudi(yakeenNationalId: String, yakeenBirthDay: String) {

        val bodyRequestYakeen = GetIntelliSearchForSaudi(yakeenNationalId, yakeenBirthDay)
        compositeDisposable.add(profiileServices.getIntelliSearchInfoForSaudi(bodyRequestYakeen)
                .compose(applySchedulers())
                .subscribe({ getIntelliSearchInfoForSaudiResponse ->
                    _loading.postValue(false)

                    val mobileOpResult = getIntelliSearchInfoForSaudiResponse.mobileOpResult
                    val intelliSearchResponse = getIntelliSearchInfoForSaudiResponse.saudiInfoResult
                    if (mobileOpResult != null && mobileOpResult.result == Success.SUCCESSCODE.value) {
                        saudiInfoResult = intelliSearchResponse
                        _resultIntelliSearchSaudi.postValue(true)
                    } else {
                        saudiInfoResult = null
                    }
                }, {
                    // hasProfileValidate = false
                    // _loading.postValue(false)
                    // onHandleError(it)
                }))
    }

    fun getYakeenInfoForNonSaudi(iqamaNumber: String, nationalIdType: String, yakeenBirthDay: String, isIqama: Boolean) {
        val securityToken: String = tinyDB.getString(Const.TOKEN_KEY)

        _loading.postValue(true)

        val yakeenInfoRequestForNonSaudi = YakeenInfoRequestForNonSaudi(securityToken, iqamaNumber, yakeenBirthDay, nationalIdType)
        compositeDisposable.add(profiileServices.GetYakeenInfoNonSaudi(yakeenInfoRequestForNonSaudi)
            .compose(applySchedulers())
            .subscribe({ yakeenInfoRequestForNonSaudiResponse ->
                _loading.postValue(false)

                val mobileOpResult = yakeenInfoRequestForNonSaudiResponse.mobileOpResult
                val yaqeenVerified = yakeenInfoRequestForNonSaudiResponse.yaqeenVerified

                if (mobileOpResult != null && mobileOpResult.result == Success.SUCCESSCODE.value) {
                    if(yaqeenVerified!!){
                        validateProfile(nationalId!!, yakeenBirthDay = yakeenDateOfBirth, isIqama = isIqama)
                    }else{
                        _localizeErrorMessage.postValue(mobileOpResult)
                        false
                    }
                } else {
                    _localizeErrorMessage.postValue(mobileOpResult)
                    false
                }

            }, {
                // hasProfileValidate = false
                // _loading.postValue(false)
                // onHandleError(it)
            }))
    }

    fun getYakeenInfoForSaudi(nationalId: String, nationalIdType: String, yakeenBirthDay: String, isIqama: Boolean) {
        val securityToken: String = tinyDB.getString(Const.TOKEN_KEY)

        _loading.postValue(true)

        val yakeenInfoRequestForSaudi = YakeenInfoRequestForSaudi(securityToken, nationalId, yakeenBirthDay, nationalIdType)
        compositeDisposable.add(profiileServices.GetYakeenInfoSaudi(yakeenInfoRequestForSaudi)
            .compose(applySchedulers())
            .subscribe({ yakeenInfoRequestForSaudiResponse ->
                _loading.postValue(false)

                val mobileOpResult = yakeenInfoRequestForSaudiResponse.mobileOpResult
                val yakeenResultResponse = yakeenInfoRequestForSaudiResponse.yakeenResult
                val yaqeenVerified = yakeenInfoRequestForSaudiResponse.yaqeenVerified

                if (mobileOpResult != null && mobileOpResult.result == Success.SUCCESSCODE.value) {
                    if(yaqeenVerified!!){
                        validateProfile(nationalId, yakeenBirthDay = yakeenDateOfBirth, isIqama = isIqama)
                    }else{
                        _localizeErrorMessage.postValue(mobileOpResult)
                        false
                    }
                } else {
                    _localizeErrorMessage.postValue(mobileOpResult)
                    false
                }

            }, {
                // hasProfileValidate = false
                // _loading.postValue(false)
                // onHandleError(it)
            }))
    }

    fun loadRefPatientProfile() {

        val securityToken: String = tinyDB.getString(Const.TOKEN_KEY)

        _loading.postValue(true)

        compositeDisposable.add(profiileServices.refPatientProfile(SecurityTokenDTO(securityToken))
                .compose(applySchedulers())
                .subscribe({ refPatientProfileModel ->

                    _loading.postValue(false)

                    val mobileOpResult = refPatientProfileModel.mobileOpResult

                    if (mobileOpResult != null && mobileOpResult.result == Success.SUCCESSCODE.value) {
                        citiesList = refPatientProfileModel.refPatientProfile.cities
                        districtsList = refPatientProfileModel.refPatientProfile.districts
                        nationalitiesList = refPatientProfileModel.refPatientProfile.nationalities
                        insuranceCarriersList = refPatientProfileModel.refPatientProfile.insuranceCarriers
                        _resultProfileRef.postValue(true)
                    } else {
                        _localizeErrorMessage.postValue(mobileOpResult)
                    }

                }, {
                    _loading.postValue(false)
                    onHandleError(it)
                }))

    }

    fun createPatientProfile() {

        _loading.postValue(true)
        val oRegId = tinyDB.getString(Const.OREGID_KEY)
        val securityToken = tinyDB.getString(Const.TOKEN_KEY)
        val patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
        var patientRegistrationDTO: PatientRegistrationDTO? = null

        val bodyRequest = ProfileValidateRequest(memberShipNo!!, securityToken, nationalId!!)

        val requestSimpleProfile: Single<ProfileCreateModel>

        if (insuranceModel != null) {

            val insuranceDTO = InsuranceDTO(
                    insuranceModel?.contractId.toString(),
                    insuranceModel?.membershipNo,
                    insuranceCardBase64,
                    insuranceExpiryDate,
                    insuranceModel?.classPlanId?.toString(),
                    insuranceModel?.carrierName,
                    insuranceModel?.carrierId.toString()
            )

            patientRegistrationDTO = if (isTentative() && !isFromRegister && !isAddProfile) {
                PatientRegistrationDTO(country, nationalId, "UNKNOWN", "",
                        nationality, nationalIdExpiryDate, insuranceDTO, firstName, true,
                        middleName, idCardScanBase64, gender, birthDate, nationalIdType, "",
                        null, lastName, "", patientId.toString(), false,firstNameAr, middleNameAr, lastNameAr )
            } else {
                PatientRegistrationDTO(country, nationalId, "UNKNOWN", "",
                        nationality, nationalIdExpiryDate, insuranceDTO, firstName, true,
                        middleName, idCardScanBase64, gender, birthDate, nationalIdType, "",
                        "", lastName, "", "", false, firstNameAr, middleNameAr, lastNameAr)
            }

            val profileCreateDTO = ProfileCreateDTO(oRegId, securityToken, patientRegistrationDTO)

            requestSimpleProfile = profiileServices.profileValidate(bodyRequest)
                    .flatMap { profileValidateResponse ->

                        val mobileOpResult = profileValidateResponse.mobileOpResult

                        if (mobileOpResult != null && mobileOpResult.result == Success.SUCCESSCODE.value) {
                            hasProfileValidate = true
                            insuranceCarrier = insuranceCarrier?.replace(" ", "")?.replace(".", "")

                            val profileInsuranceValidateDTO = ProfileInsuranceValidateDTO(securityToken, insuranceCarrierType.toString(), memberShipNo, policyNo)

                            return@flatMap profiileServices.profileInsuranceValidate(profileInsuranceValidateDTO)
                        } else {
                            _localizeErrorMessage.postValue(mobileOpResult)
                            hasProfileValidate = false
                            return@flatMap Single.just(null)
                        }

                    }.flatMap { profileInsuranceValidateModel ->

                        profileInsuranceValidateModel?.let {
                            val mobileOpResult = profileInsuranceValidateModel.mobileOpResult

                            if (mobileOpResult != null && mobileOpResult.result == Success.SUCCESSCODE.value) {

                                hasProfileInsuranceValidate = true
                                //return@flatMap null
                                if (isTentative() && !isFromRegister && !isAddProfile) {
                                    return@flatMap profiileServices.profileUpdate(profileCreateDTO)
                                } else {
                                    return@flatMap profiileServices.profileCreate(profileCreateDTO)
                                }

                            } else {
                                insuranceModel = null
                                _localizeErrorMessage.postValue(mobileOpResult)
                                hasProfileInsuranceValidate = false
                                _resultProfileInsuranceValidate.postValue(false)
                                return@flatMap Single.just(null)
                            }
                        }
                    }
        } else {
            patientRegistrationDTO = if (isTentative() && !isFromRegister && !isAddProfile) {
                PatientRegistrationDTO(country, nationalId, "UNKNOWN", "",
                        nationality, nationalIdExpiryDate, null, firstName, false,
                        middleName, idCardScanBase64, gender, birthDate, nationalIdType, "",
                        null, lastName, "", patientId.toString(), false, firstNameAr, middleNameAr, lastNameAr )
            } else {
                PatientRegistrationDTO(country, nationalId, "UNKNOWN", "",
                        nationality, nationalIdExpiryDate, null, firstName, false,
                        middleName, idCardScanBase64, gender, birthDate, nationalIdType, "",
                        "", lastName, "", "", false, firstNameAr, middleNameAr, lastNameAr )
            }
            val profileCreateDTO = ProfileCreateDTO(oRegId, securityToken, patientRegistrationDTO)
            requestSimpleProfile = profiileServices.profileValidate(bodyRequest)
                    .flatMap { profileValidateResponse ->

                        val mobileOpResult = profileValidateResponse.mobileOpResult

                        if (mobileOpResult != null && mobileOpResult.result == Success.SUCCESSCODE.value) {
                            hasProfileValidate = true
                            //return@flatMap null
                            if (isTentative() && !isFromRegister && !isAddProfile) {
                                return@flatMap profiileServices.profileUpdate(profileCreateDTO)
                            } else {
                                return@flatMap profiileServices.profileCreate(profileCreateDTO)
                            }

                        } else {
                            _localizeErrorMessage.postValue(mobileOpResult)
                            hasProfileValidate = false
                            return@flatMap Single.just(null)
                        }

                    }
        }

        compositeDisposable.add(requestSimpleProfile.compose(applySchedulers()).subscribe({
            _loading.postValue(false)
            it?.let { profileCreateModel ->

                //  tinyDB.putBoolean(Const.IS_TENT, false);
                val mobileOpResult = profileCreateModel.mobileOpResult

                if (mobileOpResult != null && mobileOpResult.result == Success.SUCCESSCODE.value) {

                    // get the data
                    if (isTentative() && !isAddProfile && !isFromRegister) {
                        tinyDB.putBoolean(Const.IS_TENT, false);
                    }

                    val insuranceId: String = profileCreateModel.insuranceId
                    val mrnNo: String = profileCreateModel.mrn
                    val patientId: Long = profileCreateModel.patientId
                    val fullName = patientRegistrationDTO.firstName + " " + patientRegistrationDTO.middleName + " " + patientRegistrationDTO.lastName
                    val gender = patientRegistrationDTO.gender
                    // save the value
                    tinyDB.putString(Const.INSURANCE_ID, insuranceId)
                    if (!ValidationHelper.isNullOrEmpty(mrnNo))
                        tinyDB.putString(Const.MRN_NO, mrnNo)
                    else tinyDB.putString(Const.MRN_NO, "0")

                    tinyDB.putLong(Const.PATIENT_ID, patientId)

                    if (!ValidationHelper.isNullOrEmpty(fullName))
                        tinyDB.putString(Const.PATIENT_NAME, fullName)

                    if (!ValidationHelper.isNullOrEmpty(patientRegistrationDTO.gender))
                        tinyDB.putString(Const.PATIENT_GENDER, patientRegistrationDTO.gender)

                    val dob = patientRegistrationDTO.birthDate

                    if (!isAddPatient) {
                        patientsMiniModel = PatientsMiniModel()
                        patientsMiniModel?.nameEn = fullName
                        patientsMiniModel?.mrn = mrnNo
                        patientsMiniModel?.patientId = patientId
                        patientsMiniModel?.birthDate = dob
                        patientsMiniModel?.gender = gender
                        // return@let
                    }
                    if (!ValidationHelper.isNullOrEmpty(dob)) {
                        tinyDB.putString(Const.DATE_BIRTH, dob)
                    }
                    _resultProfileCreate.postValue(true)

                } else {
                    _localizeErrorMessage.postValue(mobileOpResult)
                    _resultProfileCreate.postValue(false)
                }
            }
        }, {
            _loading.postValue(false)
            onHandleError(it)
        })
        )
    }

    fun validateMemberShip() {

        _loading.postValue(true)

        val securityToken = tinyDB.getString(Const.TOKEN_KEY)

        insuranceCarrier = insuranceCarrier?.replace(" ", "")?.replace(".", "")

        val profileInsuranceValidateDTO = ProfileInsuranceValidateDTO(securityToken, insuranceCarrierType.toString(), memberShipNo, policyNo)

        compositeDisposable.add(
                profiileServices.profileInsuranceValidate(profileInsuranceValidateDTO)
                        .compose(applySchedulers())
                        .subscribe({ profileInsuranceValidateModel ->
                            _loading.postValue(false)
                            val mobileOpResult = profileInsuranceValidateModel.mobileOpResult

                            if (mobileOpResult != null && mobileOpResult.result == Success.SUCCESSCODE.value) {
                                _resultProfileInsuranceValidate.postValue(true)
                                insuranceModel = profileInsuranceValidateModel.insuranceModel
                                hasProfileInsuranceValidate = true
                            } else {
                                insuranceModel = null
                                _localizeErrorMessage.postValue(mobileOpResult)
                                hasProfileInsuranceValidate = false
                                _resultProfileInsuranceValidate.postValue(false)
                            }

                        }, {
                            insuranceModel = null
                            _loading.postValue(false)
                            onHandleError(it)
                        })
        )
    }

    fun loadPatientProfileById() {
        _loading.postValue(true)

        val patientId = tinyDB.getLong(Const.PATIENT_ID, 0)
        val securityToken = tinyDB.getString(Const.TOKEN_KEY)
        val oRegId = tinyDB.getString(Const.OREGID_KEY)

        compositeDisposable.add(profiileServices.profileGetById(GetProfileDTO(patientId.toString(), securityToken, oRegId))
                .compose(applySchedulers())
                .subscribe({ profileUpdateModel ->

                    _loading.postValue(false)

                    val mobileOpResult = profileUpdateModel.mobileOpResult

                    if (mobileOpResult != null && mobileOpResult.result == Success.SUCCESSCODE.value) {
                        _resultProfileRefById.postValue(profileUpdateModel)
                    } else {
                        _localizeErrorMessage.postValue(mobileOpResult)
                    }

                }, {
                    _loading.postValue(false)
                    onHandleError(it)
                }))
    }

    fun clearInsuranceData() {
        hasProfileInsuranceValidate = false
        insuranceModel = null
        insuranceCardBase64 = null
        memberShipNo = "null"
        gender = ""
        birthDate = ""
        firstName = ""
        middleName = ""
        lastName = ""
        _localizeErrorMessage.postValue(null)
        _resultProfileInsuranceValidate.postValue(false)
        _resultProfileCreate.postValue(false)
    }

    fun clearSelectedData() {

        isHijriCalender = false
        citiesList = null
        districtsList = null
        nationalitiesList = null
        hasProfileValidate = false
        hasProfileInsuranceValidate = false
        insuranceModel = null
        insuranceCardBase64 = null

        gender = ""
        birthDate = ""
        firstName = ""
        middleName = ""
        lastName = ""
        nationalIdType = ""
        nationalId = ""
        idCardScanBase64 = ""
        nationality = ""
        country = ""
        nationalIdExpiryDate = ""
        memberShipNo = "0"
        isAddPatient = false

        _localizeErrorMessage.postValue(null)
        _resultProfileValidate.postValue(false)
        _resultProfileCreate.postValue(false)
    }

    fun clearErrorMessage() {
        _localizeErrorMessage.postValue(null)
    }
}