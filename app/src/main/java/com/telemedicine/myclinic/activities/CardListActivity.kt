package com.telemedicine.myclinic.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.BindView
import butterknife.OnClick
import com.telemedicine.myclinic.adapters.CardListRvAdapter
import com.telemedicine.myclinic.eenum.Success
import com.telemedicine.myclinic.hyperpay.CustomUIActivity
import com.telemedicine.myclinic.models.*
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.util.*
import com.telemedicine.myclinic.util.Const.*
import com.telemedicine.myclinic.webservices.RestClient
import kotlinx.android.synthetic.main.activity_card_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CardListActivity : BaseActivity() {

    @BindView(R.id.toolbar_left_iv)
    lateinit var toolbar_left_iv: ImageView

    private var cardList: ArrayList<CardModel>? = ArrayList()
    private lateinit var cardRvAdapter: CardListRvAdapter
    var apptPayServicesDTO = ArrayList<ApptPayServicesDTO>()
    var serviceModel: String? = null
    companion object {
        val cardsListStatic: ArrayList<CardModel>?= ArrayList()

        fun isCardExists(cardnubmer: String): Boolean{

            if(cardsListStatic?.isEmpty()!!){
                return false
            }
            val filteredList = cardsListStatic?.filter {
                it.cardNumber == cardnubmer
            }

            return filteredList?.size != 0
        }
    }

    override fun layout(): Int {
        return R.layout.activity_card_list
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
                    this@CardListActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameAr + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            } else {
                ErrorMessage.getInstance().showSuccessGreen(
                    this@CardListActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameEn + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            }
        } else {
            ErrorMessage.getInstance()
                .showErrorYellow(this@CardListActivity, appointmentEvent.errorMSg)
        }
    }

    var amountIntent = "";
    var apptIDIntent = 0L
    var checkoutIdIntent = ""
    var doctorNameIntent = ""
    var specialityIntent = ""
    var dateIntent = ""
    var specialityIdIntent = ""
    var doctorIdIntent = ""
    var isTelemedcineIntent = false
    var doctorImageUrlIntent = ""
    var apptBookTypeintent = 0L
    var isInsuranceIntent = false
    var insuranceIdIntent = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        amountIntent = intent.getStringExtra("amount") ?: ""
        apptIDIntent = intent.getLongExtra(APPT_ID, 0)
        checkoutIdIntent = intent.getStringExtra(CHECK_OUT_ID) ?: ""
        serviceModel = intent.getStringExtra(SERVICE_MODEL)
        doctorNameIntent = intent.getStringExtra(DOCTOR_NAME) ?: ""
        specialityIntent = intent.getStringExtra(DOCTOR_SPECIALITY) ?: ""
        dateIntent = intent.getStringExtra(DATE) ?: ""
        specialityIdIntent = intent.getStringExtra(SPECIALITY_ID) ?: ""
        doctorIdIntent = intent.getStringExtra(DOCTOR_ID) ?: ""
       isTelemedcineIntent =  intent.getBooleanExtra(ISTELEMEDICINE_KEY, false)
        apptBookTypeintent = intent.getLongExtra(APPT_BOOK_TYPE, 0)
        isInsuranceIntent = intent.getBooleanExtra(IS_INSURANCE_KEY, false)
        insuranceIdIntent = intent.getLongExtra(INSURANCE_ID, 0)

        transformViews()
        getCards()
        init()
    }

    private fun init() {
        cardRvAdapter =
                CardListRvAdapter(this, cardList, object :
                        CardListRvAdapter.ClickItemListener {
                    override fun onClicked(position: Int, id: Int, model: CardModel) {
                        val intent = Intent(this@CardListActivity, CustomUIActivity::class.java)
                        //intent.putExtra(Const.PAYMENT_TOKEN_ID, model.cardHolderRegID)
                        intent.putExtra(CARD_NUMBER, "**** **** **** ${model.last4Digit}")
                        intent.putExtra("card_number_intent", model.cardNumber)
                        intent.putExtra(SERVICE_MODEL,  serviceModel)
                        intent.putExtra(EXPIRY_MONTH, model.expiryMonth)
                        intent.putExtra(IS_FROM_CARD_LISTING, true);
                        intent.putExtra(EXPIRY_YEAR, model.expiryYear)
                        intent.putExtra(CARD_HOLDER_NAME, model.cardHolder)
                        intent.putExtra(CARD_BRAND_NAME, model.cardBrand)
                        intent.putExtra("amount", amountIntent)
                        intent.putExtra(APPT_ID, apptIDIntent)
                        intent.putExtra(CHECK_OUT_ID, checkoutIdIntent)

                        intent.putExtra(DOCTOR_NAME, doctorNameIntent)
                        intent.putExtra(DOCTOR_SPECIALITY, specialityIntent)
                        intent.putExtra(DATE, dateIntent)
                        intent.putExtra(SPECIALITY_ID, specialityIdIntent)
                        intent.putExtra(DOCTOR_ID, doctorIdIntent)
                        intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcineIntent)
                        intent.putExtra(APPT_BOOK_TYPE, apptBookTypeintent)
                        intent.putExtra(APPT_ID, apptIDIntent)
                        intent.putExtra(IS_INSURANCE_KEY, isInsuranceIntent)
                        intent.putExtra(INSURANCE_ID, insuranceIdIntent)
                        startActivityForResult(intent, 700)
                    }

                    override fun onDeleteCardClicked(id: Int, position: Int) {
                        deleteCardApi(id)
                    }
                })

        cardRv!!.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        cardRv!!.adapter = cardRvAdapter

        appCompatImageView3.setOnClickListener {
            val intent = Intent(this@CardListActivity, CustomUIActivity::class.java)
            intent.putExtra("amount", amountIntent)
            intent.putExtra(APPT_ID, apptIDIntent)
            intent.putExtra(SERVICE_MODEL,  serviceModel)
            intent.putExtra(IS_FROM_CARD_LISTING, true);
            intent.putExtra(CHECK_OUT_ID, checkoutIdIntent)
            intent.putExtra(DOCTOR_NAME, doctorNameIntent)
            intent.putExtra(DOCTOR_SPECIALITY, specialityIntent)
            intent.putExtra(DATE, dateIntent)
            intent.putExtra(SPECIALITY_ID, specialityIdIntent)
            intent.putExtra(DOCTOR_ID, doctorIdIntent)
            intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcineIntent)
            intent.putExtra(APPT_BOOK_TYPE, apptBookTypeintent)
            intent.putExtra(APPT_ID, apptIDIntent)
            intent.putExtra(IS_INSURANCE_KEY, isInsuranceIntent)
            intent.putExtra(INSURANCE_ID, insuranceIdIntent)
            startActivityForResult(intent, 700)
        }
    }

    private var isCardDeleted = false
    private fun deleteCardApi(id: Int) {
        RestClient.getInstance().deletePaymentCardById(id) { result, status, errorMessage ->
            //val payStage2Result = result as MobileOpResult
            hideWaitDialog()
            val mobileOpResult = result as MobileOpResult
            if (mobileOpResult != null) {
                if (mobileOpResult.result == Success.SUCCESSCODE.value) {
                    isCardDeleted = true
                    getCards()
                } else {
                    hideWaitDialog()
                    // UtilProgressBar.disMissDialogue();
                    var errorMesg = ""
                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.businessErrorMessageEn)) {
                        if (TextUtil.getEnglish(this)) errorMesg = """
     ${mobileOpResult.businessErrorMessageEn}
     
     """.trimIndent() else if (TextUtil.getArabic(this)) errorMesg = """
     ${mobileOpResult.businessErrorMessageAr}
     
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
                hideWaitDialog()
                // UtilProgressBar.disMissDialogue();
                ErrorMessage.getInstance().showError(this, errorMessage)
            }
        }
    }

    private fun getCards() {
        val tinyDB = TinyDB(this)

        val oRegId = tinyDB.getString(OREGID_KEY)
        val securityToken = tinyDB.getString(Const.TOKEN_KEY)
        val OregIdLong = oRegId.toLong()
        showWaitDialog()
        // UtilProgressBar.dialogueShow(this, getResources().getString(R.string.progress_message_please_wait));

        // UtilProgressBar.dialogueShow(this, getResources().getString(R.string.progress_message_please_wait));

        val cardDTO = CardDTO(securityToken, OregIdLong)
        RestClient.getInstance().paymentCardById(cardDTO) { result, status, errorMessage ->
            val payStage2Result = result as CardsListModel
            hideWaitDialog()
            if (payStage2Result != null) {
                val mobileOpResult = payStage2Result.mobileOpResult
                if (mobileOpResult != null) {
                    if (mobileOpResult.result == Success.SUCCESSCODE.value) {

                        cardList?.clear()
                        cardsListStatic?.clear()
                        var cards = payStage2Result.cardsModel
                        cardList?.addAll(cards)
                        cardsListStatic?.addAll(cards)
                        cardRvAdapter.notifyDataSetChanged()

                        if (cards.isEmpty() && !isCardDeleted) {
                            val intent = Intent(this@CardListActivity, CustomUIActivity::class.java)
                            intent.putExtra("amount", amountIntent)
                            intent.putExtra(APPT_ID, apptIDIntent)
                            intent.putExtra(SERVICE_MODEL,  serviceModel)
                            intent.putExtra(IS_FROM_CARD_LISTING, true);
                            intent.putExtra(CHECK_OUT_ID, checkoutIdIntent)
                            intent.putExtra(DOCTOR_NAME, doctorNameIntent)
                            intent.putExtra(DOCTOR_SPECIALITY, specialityIntent)
                            intent.putExtra(DATE, dateIntent)
                            intent.putExtra(SPECIALITY_ID, specialityIdIntent)
                            intent.putExtra(DOCTOR_ID, doctorIdIntent)
                            intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcineIntent)
                            intent.putExtra(APPT_BOOK_TYPE, apptBookTypeintent)
                            intent.putExtra(IS_INSURANCE_KEY, isInsuranceIntent)
                            intent.putExtra(INSURANCE_ID, insuranceIdIntent)
                            startActivityForResult(intent, 700)
                        }

                        if (cards.isEmpty() && isCardDeleted) {
                            showPlaceHolder()
                        } else {
                            hidePlaceHolder()
                        }

                        isCardDeleted = false

                    } else {
                        hideWaitDialog()
                        // UtilProgressBar.disMissDialogue();
                        var errorMesg = ""
                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.businessErrorMessageEn)) {
                            if (TextUtil.getEnglish(this)) errorMesg = """
     ${mobileOpResult.businessErrorMessageEn}
     
     """.trimIndent() else if (TextUtil.getArabic(this)) errorMesg = """
     ${mobileOpResult.businessErrorMessageAr}
     
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
                    hideWaitDialog()
                    // UtilProgressBar.disMissDialogue();
                    ErrorMessage.getInstance().showError(this, errorMessage)
                }
            } else {
                hideWaitDialog()
                //UtilProgressBar.disMissDialogue();
                ErrorMessage.getInstance().showError(this, errorMessage)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 700 && resultCode == RESULT_OK) {
            if (data != null) {
                val apptLong = data.getLongExtra(Const.APPT_ID, 0)
                if (apptLong != 0L) {
                    if (apptLong != -1L) {
                        val intent = Intent();
                        intent.putExtra(Const.CONFIRM, true);
                        intent.putExtra(APPT_ID, apptLong);
                        setResult(RESULT_OK, intent);
                        finish()
                    } else {
                        val intent = Intent();
                        setResult(RESULT_OK, intent);
                        finish()
                    }

                } else {
                    val intent = Intent();
                    setResult(RESULT_OK, intent);
                    finish()
                }
                /* val apptId = data.getIntExtra(Const.APPT_ID, 0)
                 if(apptId != -1){
                     val intent =  Intent();
                     intent.putExtra(Const.CONFIRM, true);
                     intent.putExtra(APPT_ID, apptId);
                     setResult(RESULT_OK, intent);
                     finish()
                 }else{
                     val intent =  Intent();
                     setResult(RESULT_OK, intent);
                     finish()
                 }*/
            }
        }
    }

    private fun showPlaceHolder() {
        cardEmptyPlaceholder.visibility = View.VISIBLE
    }

    private fun hidePlaceHolder() {
        cardEmptyPlaceholder.visibility = View.GONE
    }

    @OnClick(R.id.toolbar_left_iv)
    fun back() {
        onBackPressed()
    }

    private fun transformViews() {
        if (TextUtil.getArabic(this)) {
            val params = toolbar_left_iv.layoutParams as RelativeLayout.LayoutParams
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            toolbar_left_iv?.layoutParams = params
            toolbar_left_iv?.rotation = 180f
        }
    }
}