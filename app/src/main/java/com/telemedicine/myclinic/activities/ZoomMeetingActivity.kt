package com.telemedicine.myclinic.activities

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Base64.encodeToString
import android.view.View
import androidx.core.app.TaskStackBuilder
import butterknife.OnClick
import com.telemedicine.myclinic.eenum.Success
import com.telemedicine.myclinic.listeners.OnResultListener
import com.telemedicine.myclinic.models.teleCheckin.TeleCheckInModel
import com.telemedicine.myclinic.models.teleCheckin.TeleCheckInOutDTO
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.myapplication.SplashActivity
import com.telemedicine.myclinic.util.*
import com.telemedicine.myclinic.webservices.RestClient
import kotlinx.android.synthetic.main.activity_zoom_meeting.*
import us.zoom.sdk.*
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.roundToInt

class ZoomMeetingActivity : BaseActivity(), InMeetingServiceListener {

    override fun layout(): Int {
        return R.layout.activity_zoom_meeting
    }

    @OnClick(R.id.toolbar_left_iv)
    fun backPressed() {
        onBackPressed()
    }

    private var sessionId = ""
    private var tokenId = ""
    private var name = ""
    private var apptId = ""
    private var patientName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenId = intent.getStringExtra("tokenId") ?: ""
        sessionId = intent.getStringExtra("sessionId") ?: ""
        apptId = intent.getStringExtra("apptId") ?: ""
        patientName = intent.getStringExtra("PatientName") ?: ""
        // name = intent.getStringExtra("name")?:""

        //name = intent.getStringExtra("name")?:""
        // val patientName = tinyDB.getString(Const.PATIENT_NAME)

        /* if (!ValidationHelper.isNullOrEmpty(patientName)) {
             name = patientName
         }*/

        initializeSdk(this)
        closeNotification()

        startTimer()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        tokenId = intent?.getStringExtra("tokenId") ?: ""
        sessionId = intent?.getStringExtra("sessionId") ?: ""
        apptId = intent?.getStringExtra("apptId") ?: ""
        patientName = intent?.getStringExtra("PatientName") ?: ""
        closeNotification()
        launchZoomMeeting()
    }

    private fun startTimer() {
        Handler(Looper.getMainLooper()).postDelayed({
            showJoinAgainButton()
        }, 60000)
    }

    private fun showJoinAgainButton() {
        loadingLayout.visibility = View.GONE
        joinAgain.visibility = View.VISIBLE
    }

    private fun hideJoinAgainButton() {
        loadingLayout.visibility = View.VISIBLE
        joinAgain.visibility = View.GONE
    }

    fun joinAgain(view: View) {
        hideJoinAgainButton()
        startTimer()
        launchZoomMeeting()
    }

    private fun closeNotification() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(0)
        ValidationHelper.stopRingtone()
    }

    /**
     * Initialize the SDK with your credentials. This is required before accessing any of the
     * SDK's meeting-related functionality.
     */
    private fun initializeSdk(context: Context) {
        if (!ZoomSDK.getInstance().isInitialized) {
            val sdk = ZoomSDK.getInstance()

            // TODO: Do not use hard-coded values for your key/secret in your app in production!

            val params = ZoomSDKInitParams().apply {
                jwtToken = getJWTToken()
                // appKey =
                //  "9BnNLhepyMDUWARAc6Sm68GeZBYw0KPSkns6" /*//  Retrieve your SDK key and enter it here*/
                //  appSecret =
                //    "lttSXc7EeAHWdTvDfNL0AKROyWhCwgYkRjH6" // : Retrieve your SDK secret and enter it here
                domain = "zoom.us"
                enableLog = true // Optional: enable logging for debugging

            }

            // TODO (optional): Add functionality to this listener (e.g. logs for debugging)
            val listener = object : ZoomSDKInitializeListener {
                /**
                 * If the [errorCode] is [ZoomError.ZOOM_ERROR_SUCCESS], the SDK was initialized and can
                 * now be used to join/start a meeting.
                 */
                override fun onZoomSDKInitializeResult(errorCode: Int, internalErrorCode: Int) {
                    //  showToast("$errorCode")
                    if (errorCode == ZoomError.ZOOM_ERROR_SUCCESS) {
                        val appointmentId =
                            TinyDB(this@ZoomMeetingActivity).getString(Const.APPOINTMENT_ID_ZOOM)
                                ?: ""
                        if (appointmentId.isNotEmpty()) {
                            if (appointmentId != apptId) {
                                teleCheckIn(true, apptId)
                            } else {
                                joinMeeting(this@ZoomMeetingActivity, sessionId, tokenId)
                            }

                        } else {
                            teleCheckIn(true, apptId)
                        }
                    } else {
                       // showToast(errorCode.toString())
                    }

                }

                override fun onZoomAuthIdentityExpired(){
                    showToast("Token expiry is near")
                }
            }
            //sdk.initialize(context, listener, params)
            sdk.initialize(context, listener, params)
        }
    }

    private fun getJWTToken(): String? {
       /* val time = System.currentTimeMillis() / 1000 + 60 * 1000
        val expiryTime = System.currentTimeMillis() + 45000000 / 1000 + 60 * 1000
*/
        var iat = (Date().time / 1000).toDouble().roundToInt() - 30
        var exp = iat + 60 * 60 * 2
        val header = "{\"alg\": \"HS256\", \"typ\": \"JWT\"}"
        val payload =
            "{\"appKey\": \"9BnNLhepyMDUWARAc6Sm68GeZBYw0KPSkns6\", \"iat\": $iat, \"exp\": $exp, \"tokenExp\": $exp}"
        try {
            val headerBase64Str: String = Base64.encodeToString(
                header.toByteArray(charset("utf-8")),
                Base64.NO_WRAP or Base64.NO_PADDING or Base64.URL_SAFE
            )
            val payloadBase64Str: String = Base64.encodeToString(
                payload.toByteArray(charset("utf-8")),
                Base64.NO_WRAP or Base64.NO_PADDING or Base64.URL_SAFE
            )
            val mac: Mac = Mac.getInstance("HmacSHA256")
            val secretKeySpec = SecretKeySpec(
                "lttSXc7EeAHWdTvDfNL0AKROyWhCwgYkRjH6".toByteArray(charset("utf-8")),
                "HmacSHA256"
            )
            mac.init(secretKeySpec)
            val digest: ByteArray = mac.doFinal("$headerBase64Str.$payloadBase64Str".toByteArray())
            return "$headerBase64Str.$payloadBase64Str." + Base64.encodeToString(
                digest,
                Base64.NO_WRAP or Base64.NO_PADDING or Base64.URL_SAFE
            )
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Join a meeting without any login/authentication with the meeting's number & password
     */
    private fun joinMeeting(context: Context, meetingNumber: String, pw: String) {
        val meetingService = ZoomSDK.getInstance().meetingService
        val options = JoinMeetingOptions()

        val params = JoinMeetingParams().apply {
            displayName = patientName
            meetingNo = meetingNumber
            password = pw
        }
        JoinMeetingOptions().no_webinar_register_dialog
        ZoomSDK.getInstance().inMeetingService.addListener(this)
        meetingService.joinMeetingWithParams(context, params, options)
    }

    override fun onResume() {
        super.onResume()
        //  showToast("on resume")
    }

    override fun onStart() {
        super.onStart()
        // showToast("on start")
        launchZoomMeeting()
    }

    private fun launchZoomMeeting() {

        val appointmentId = TinyDB(this).getString(Const.APPOINTMENT_ID_ZOOM) ?: ""

        if (appointmentId.isNotEmpty()) {

            if (appointmentId != apptId) {
                teleCheckIn(true, apptId)
            } else {

                try {
                    if (ZoomSDK.getInstance().isInitialized) {
                        val mMeetingService = ZoomSDK.getInstance().meetingService

                        if (mMeetingService.meetingStatus != null) {
                            if (mMeetingService.meetingStatus != MeetingStatus.MEETING_STATUS_IDLE) {
                                finish()
                            } else {
                                joinMeeting(this@ZoomMeetingActivity, sessionId, tokenId)
                            }
                        } else {
                            joinMeeting(this@ZoomMeetingActivity, sessionId, tokenId)
                        }
                        // showToast("initialized")
                    } else {
                       // showToast("No Initialized")
                    }
                } catch (e: Exception) {
                   // showToast("exception ${e.message?.toString()}")
                }
            }

        } else {
            teleCheckIn(true, apptId)
        }

    }

    override fun onMeetingNeedPasswordOrDisplayName(
        p0: Boolean,
        p1: Boolean,
        p2: InMeetingEventHandler?
    ) {
    }

    override fun onWebinarNeedRegister(p0: String?) {
     //   showToast("onWebinarNeedRegister")
    }

    override fun onJoinWebinarNeedUserNameAndEmail(p0: InMeetingEventHandler?) {
     //   showToast("onJoinWebinarNeedUserNameAndEmail")
    }

    override fun onMeetingNeedCloseOtherMeeting(p0: InMeetingEventHandler?) {

    }

    override fun onMeetingFail(p0: Int, p1: Int) {
        showToast("meeting fail joins")
        finish()
    }

    override fun onMeetingLeaveComplete(p0: Long) {
        val backIntent = Intent(this, SplashActivity::class.java)
        finish()
        if (isTaskRoot) {
            TaskStackBuilder.create(this).addNextIntentWithParentStack(backIntent).startActivities()
        }
    }

    override fun onMeetingUserJoin(p0: MutableList<Long>?) {
          //showToast("meeting user joins")
    }

    override fun onMeetingUserLeave(p0: MutableList<Long>?) {

    }

    override fun onMeetingUserUpdated(p0: Long) {

    }

    override fun onInMeetingUserAvatarPathUpdated(p0: Long) {

    }


    override fun onMeetingHostChanged(p0: Long) {

    }

    override fun onMeetingCoHostChanged(p0: Long) {

    }

    override fun onMeetingCoHostChange(p0: Long, p1: Boolean) {

    }

    override fun onActiveVideoUserChanged(p0: Long) {

    }

    override fun onActiveSpeakerVideoUserChanged(p0: Long) {

    }

    override fun onHostVideoOrderUpdated(p0: MutableList<Long>?) {

    }

    override fun onFollowHostVideoOrderChanged(p0: Boolean) {

    }

    override fun onSpotlightVideoChanged(p0: Boolean) {

    }

    override fun onSpotlightVideoChanged(p0: MutableList<Long>?) {
    }

    override fun onUserVideoStatusChanged(p0: Long, p1: InMeetingServiceListener.VideoStatus?) {

    }

    override fun onUserNetworkQualityChanged(p0: Long) {

    }

    override fun onSinkMeetingVideoQualityChanged(p0: VideoQuality?, p1: Long) {

    }

    override fun onMicrophoneStatusError(p0: InMeetingAudioController.MobileRTCMicrophoneError?) {

    }

    override fun onUserAudioStatusChanged(p0: Long, p1: InMeetingServiceListener.AudioStatus?) {
        //showToast("onUserAudioStatusChanged")
    }

    override fun onHostAskUnMute(p0: Long) {
       // showToast("onHostAskUnMute")
    }

    override fun onHostAskStartVideo(p0: Long) {
      //  showToast("onHostAskStartVideo")
    }

    override fun onUserAudioTypeChanged(p0: Long) {
       // showToast("onUserAudioTypeChanged")
    }

    override fun onMyAudioSourceTypeChanged(p0: Int) {
      //  showToast("onMyAudioSourceTypeChanged")
    }

    override fun onLowOrRaiseHandStatusChanged(p0: Long, p1: Boolean) {
       // showToast("onLowOrRaiseHandStatusChanged")
    }

    override fun onChatMessageReceived(p0: InMeetingChatMessage?) {
     //   showToast("onChatMessageReceived")
    }

    override fun onChatMsgDeleteNotification(p0: String?, p1: ChatMessageDeleteType?) {
      //  showToast("onChatMsgDeleteNotification")
    }

    override fun onShareMeetingChatStatusChanged(p0: Boolean) {
       // showToast("onShareMeetingChatStatusChanged")
    }

    override fun onSilentModeChanged(p0: Boolean) {
      //  showToast("onSilentModeChanged")
    }

    override fun onFreeMeetingReminder(p0: Boolean, p1: Boolean, p2: Boolean) {
      //  showToast("onFreeMeetingReminder")
    }

    override fun onMeetingActiveVideo(p0: Long) {
      //  showToast("onMeetingActiveVideo")
    }

    override fun onSinkAttendeeChatPriviledgeChanged(p0: Int) {
      //  showToast("onSinkAttendeeChatPriviledgeChanged")
    }

    override fun onSinkAllowAttendeeChatNotification(p0: Int) {
     //   showToast("onSinkAllowAttendeeChatNotification")
    }

    override fun onSinkPanelistChatPrivilegeChanged(p0: InMeetingChatController.MobileRTCWebinarPanelistChatPrivilege?) {
      //  showToast("onSinkPanelistChatPrivilegeChanged")
    }

    override fun onUserNameChanged(p0: Long, p1: String?) {
      //  showToast("onUserNameChanged")
    }

    override fun onUserNamesChanged(p0: MutableList<Long>?) {
      //  showToast("onUserNamesChanged")
    }

    override fun onFreeMeetingNeedToUpgrade(p0: FreeMeetingNeedUpgradeType?, p1: String?) {
      //  showToast("onFreeMeetingNeedToUpgrade")
    }

    override fun onFreeMeetingUpgradeToGiftFreeTrialStart() {
     //   showToast("onFreeMeetingUpgradeToGiftFreeTrialStart")
    }

    override fun onFreeMeetingUpgradeToGiftFreeTrialStop() {
     //   showToast("onFreeMeetingUpgradeToGiftFreeTrialStop")
    }

    override fun onFreeMeetingUpgradeToProMeeting() {
      //  showToast("onFreeMeetingUpgradeToProMeeting")
    }

    override fun onClosedCaptionReceived(p0: String?, p1: Long) {
      //  showToast("onClosedCaptionReceived")
    }


    override fun onRecordingStatus(p0: InMeetingServiceListener.RecordingStatus?) {
      //  showToast("onRecordingStatus")
    }

    override fun onLocalRecordingStatus(p0: Long, p1: InMeetingServiceListener.RecordingStatus?) {
      //  showToast("onLocalRecordingStatus")
    }


    override fun onInvalidReclaimHostkey() {
       // showToast("onInvalidReclaimHostkey")
    }

    override fun onPermissionRequested(p0: Array<out String>?) {
       // showToast("onPermissionRequested")
    }

    override fun onAllHandsLowered() {
       // showToast("onAllHandsLowered")
    }

    override fun onLocalVideoOrderUpdated(p0: MutableList<Long>?) {
      //  showToast("onLocalVideoOrderUpdated")
    }

    override fun onLocalRecordingPrivilegeRequested(p0: IRequestLocalRecordingPrivilegeHandler?) {
      //  showToast("onLocalRecordingPrivilegeRequested")
    }

    override fun onSuspendParticipantsActivities() {
     //   showToast("onSuspendParticipantsActivities")
    }

    override fun onAllowParticipantsStartVideoNotification(p0: Boolean) {
       // showToast("onAllowParticipantsStartVideoNotification")
    }

    override fun onAllowParticipantsRenameNotification(p0: Boolean) {
     //   showToast("onAllowParticipantsRenameNotification")
    }

    override fun onAllowParticipantsUnmuteSelfNotification(p0: Boolean) {
     //   showToast("onAllowParticipantsUnmuteSelfNotification")
    }

    override fun onAllowParticipantsShareWhiteBoardNotification(p0: Boolean) {
//showToast("onAllowParticipantsShareWhiteBoardNotification")
    }

    override fun onMeetingLockStatus(p0: Boolean) {
       // showToast("onMeetingLockStatus")
    }

    override fun onRequestLocalRecordingPrivilegeChanged(p0: LocalRecordingRequestPrivilegeStatus?) {
      //  showToast("onRequestLocalRecordingPrivilegeChanged")
    }

    override fun onAICompanionActiveChangeNotice(p0: Boolean) {

    }

    override fun onParticipantProfilePictureStatusChange(p0: Boolean) {

    }

    override fun onDestroy() {
        super.onDestroy()
       // showToast("onDestroy")
        ZoomSDK.getInstance().inMeetingService.removeListener(this)
    }

    fun teleCheckIn(checkIn: Boolean, intentApptId: String) {
        if (intentApptId.isEmpty()) {
            launchZoomMeeting()
            showToast("Appointment id is empty  $intentApptId")
            return
        }

        if (ConnectionUtil.isInetAvailable(this)) {
            if (!checkIn) showWaitDialog()
            val securityToken = TinyDB(this).getString(Const.TOKEN_KEY)
            val companyId = TinyDB(this).getString(Const.COMPANY_ID)

            val teleCheckInOutDTO =
                TeleCheckInOutDTO(securityToken, intentApptId.toLong(), checkIn, "")
            RestClient.getInstance().teleCheckInOut(
                teleCheckInOutDTO,
                OnResultListener { result, status, errorMessage ->
                    hideWaitDialog()
                    val teleCheckInModel = result as TeleCheckInModel
                    if (teleCheckInModel != null) {
                        val mobileOpResult = teleCheckInModel.mobileOpResult
                        if (mobileOpResult != null) {
                            if (mobileOpResult.result == Success.SUCCESSCODE.value) {
                                TinyDB(this).putString(Const.APPOINTMENT_ID_ZOOM, intentApptId)
                                launchZoomMeeting()
                            } else {
                                var errorMesg = ""
                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.businessErrorMessageEn)) {
                                    if (TextUtil.getEnglish(this@ZoomMeetingActivity)) errorMesg =
                                        """
     ${mobileOpResult.businessErrorMessageEn}
     
     """.trimIndent() else if (TextUtil.getArabic(this@ZoomMeetingActivity)) errorMesg = """
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
                                    .showError(this@ZoomMeetingActivity, errorMesg)
                            }
                        } else {
                            ErrorMessage.getInstance()
                                .showError(this@ZoomMeetingActivity, errorMessage)
                        }
                    } else ErrorMessage.getInstance()
                        .showError(this@ZoomMeetingActivity, errorMessage)
                })
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection))
        }
    }
}