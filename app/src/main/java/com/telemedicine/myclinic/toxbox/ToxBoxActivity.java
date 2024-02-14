package com.telemedicine.myclinic.toxbox;

import android.Manifest;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;
import com.telemedicine.myclinic.activities.BaseActivity;
import com.telemedicine.myclinic.extensions.ToastExtensionKt;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.DialogUtils;
import com.telemedicine.myclinic.util.ValidationHelper;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class ToxBoxActivity extends BaseActivity
        implements EasyPermissions.PermissionCallbacks,
        Session.SessionListener,
        PublisherKit.PublisherListener,
        SubscriberKit.SubscriberListener {

    private static final String LOG_TAG = ToxBoxActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;


    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private Stream mReceivingStream;
    ImageView video;
    ImageView mic;
    ImageView camera;

    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;
    boolean isVideoOn = true;
    boolean isMicOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        closeNotification();
        super.onCreate(savedInstanceState);
        initView();

        // initialize view objects from your layout
        mPublisherViewContainer = (FrameLayout) findViewById(R.id.publisher_container);
        mSubscriberViewContainer = (FrameLayout) findViewById(R.id.subscriber_container);
        video = findViewById(R.id.video);
        mic = findViewById(R.id.mic);
        camera = findViewById(R.id.camera);
        requestPermissions();
        wakeUpAndUnlock();
    }

    @Override
    protected int layout() {
        return R.layout.tox_box_activity;
    }

    private void initView() {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //hide navigationBar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    /* Activity lifecycle methods */

    @Override
    protected void onPause() {

        Log.d(LOG_TAG, "onPause");

        super.onPause();

        if (mSession != null) {
            mSession.onPause();
        }

    }

    @Override
    protected void onResume() {

        Log.d(LOG_TAG, "onResume");

        super.onResume();

        if (mSession != null) {
            mSession.onResume();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        Log.d(LOG_TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        Log.d(LOG_TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle(getString(R.string.title_settings_dialog))
                    .setRationale(getString(R.string.rationale_ask_again))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel))
                    .setRequestCode(RC_SETTINGS_SCREEN_PERM)
                    .build()
                    .show();
        }
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {

        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {

                if (OpenTokConfig.areHardCodedConfigsValid()) {
                    // OpenTokConfig.SESSION_ID =
                    // OpenTokConfig.TOKEN
                    String tokenId = getIntent().getStringExtra("tokenId");
                    String sessionId = getIntent().getStringExtra("sessionId");
                    initializeSession(OpenTokConfig.API_KEY, sessionId, tokenId);
                } else {
                    showConfigError("Configuration Error", OpenTokConfig.hardCodedConfigErrorMessage);
                }

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_video_app), RC_VIDEO_APP_PERM, perms);
        }
    }

    private void initializeSession(String apiKey, String sessionId, String token) {

        mSession = new Session.Builder(this, apiKey, sessionId).build();
        mSession.setSessionListener(this);
        mSession.connect(token);
        showWaitDialog();
    }



    boolean isCameraSwape = false;

    public void SwapCamera(View view) {
        mPublisher.cycleCamera();

        if (isCameraSwape) {
            isCameraSwape = false;
            camera.setImageDrawable(getResources().getDrawable(R.drawable.ic_switch_camera_white_24dp));
        } else {
            isCameraSwape = true;
            camera.setImageDrawable(getResources().getDrawable(R.drawable.ic_switch_camera_back_white_24dp));
        }
    }

    public void VideoAudio(View view) {
        if (isVideoOn) {
            isVideoOn = false;
            mPublisher.setPublishVideo(false);
            video.setImageDrawable(getResources().getDrawable(R.drawable.ic_videocam_off_white_24dp));
        } else {
            isVideoOn = true;
            mPublisher.setPublishVideo(true);
            video.setImageDrawable(getResources().getDrawable(R.drawable.ic_videocam_white_24dp));
        }
    }

    public void Mic(View view) {
        if (isMicOn) {
            isMicOn = false;
            mPublisher.setPublishAudio(false);
            mic.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic_off_white_24dp));
        } else {
            isMicOn = true;
            mPublisher.setPublishAudio(true);
            mic.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic_black_24dp));
        }

    }

    private void startSubscribing(){
        if (mReceivingStream != null && mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, mReceivingStream).build();
            mSubscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
            mSubscriber.setSubscriberListener(this);
            mSession.subscribe(mSubscriber);
            mSubscriberViewContainer.addView(mSubscriber.getView());
        }
    }

    private void startPublishing(){
        mPublisher = new Publisher.Builder(this)
                .resolution(Publisher.CameraCaptureResolution.MEDIUM)
                .build();
        mPublisher.setPublisherListener(this);

        // set publisher video style to fill view
        mPublisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);

        mPublisherViewContainer.addView(mPublisher.getView());
        if (mPublisher.getView() instanceof GLSurfaceView) {
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }
        if(mSession!=null){
            mSession.publish(mPublisher);
        }
    }
    /* Session Listener methods */

    @Override
    public void onConnected(Session session) {

        Log.d(LOG_TAG, "onConnected: Connected to session: " + session.getSessionId());
        hideWaitDialog();
        // initialize Publisher and set this object to listen to Publisher events
        startPublishing();
    }

    @Override
    public void onDisconnected(Session session) {
        hideWaitDialog();
        finishAndRemoveTask();
        Log.d(LOG_TAG, "onDisconnected: Disconnected from session: " + session.getSessionId());
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {

        Log.d(LOG_TAG, "onStreamReceived: New Stream Received " + stream.getStreamId() + " in session: " + session.getSessionId());
        mReceivingStream = stream;
        startSubscribing();
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

        Log.d(LOG_TAG, "onStreamDropped: Stream Dropped: " + stream.getStreamId() + " in session: " + session.getSessionId());

        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
        if (mSession != null) {
            mSession.disconnect();
        }

    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - " + opentokError.getMessage() + " in session: " + session.getSessionId());

        hideWaitDialog();
       if(opentokError.getErrorCode().getErrorCode() == OpentokError.ErrorCode.ConnectionFailed.getErrorCode()|| opentokError.getErrorCode().getErrorCode() == OpentokError.ErrorCode.SessionConnectionTimeout.getErrorCode() ){
           new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                   .setTitleText( getString(R.string.app_name))
                   .setContentText(getString(R.string.text_poor_internet))
                   .setConfirmText(getString(R.string.ok))
                   .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                       @Override
                       public void onClick(SweetAlertDialog sDialog) {
                           sDialog.dismissWithAnimation();
                           finishAndRemoveTask();
                       }
                   })
                   .show();
       }else{
           showOpenTokError(opentokError);
       }

    }

    /* Publisher Listener methods */

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

        Log.d(LOG_TAG, "onStreamCreated: Publisher Stream Created. Own stream " + stream.getStreamId());

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

        Log.d(LOG_TAG, "onStreamDestroyed: Publisher Stream Destroyed. Own stream " + stream.getStreamId());
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - " + opentokError.getMessage());
        hideWaitDialog();

        mPublisher = null;
        mPublisherViewContainer.removeAllViews();

        if(opentokError.getErrorCode().getErrorCode() == OpentokError.ErrorCode.PublisherTimeout.getErrorCode()){


            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText( getString(R.string.app_name))
                    .setContentText(getString(R.string.text_poor_internet))
                    .setConfirmText(getString(R.string.ok))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startPublishing();
                        }
                    })
                    .show();
        }else{
            showOpenTokError(opentokError);
        }

    }

    /*Subscriber callbacks*/
    @Override
    public void onConnected(SubscriberKit subscriberKit) {

        Log.d(LOG_TAG, "onConnected: Subscriber connected. Stream: " + subscriberKit.getStream().getStreamId());
    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {

        Log.d(LOG_TAG, " onDisconnected: Subscriber disconnected. Stream : " + subscriberKit.getStream().getStreamId());
        onBackPressed();
    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {

        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }

        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - " + opentokError.getMessage());
        if(opentokError.getErrorCode().getErrorCode() == OpentokError.ErrorCode.ConnectionTimedOut.getErrorCode()){


            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText( getString(R.string.app_name))
                    .setContentText(getString(R.string.text_poor_internet))
                    .setConfirmText(getString(R.string.ok))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startSubscribing();
                        }
                    })
                    .show();
        }else{
            showOpenTokError(opentokError);
        }

    }

    private void showOpenTokError(OpentokError opentokError) {
       // Toast.makeText(this, opentokError.getErrorDomain().name() + ": " + opentokError.getMessage() + " Please, see the logcat.", Toast.LENGTH_LONG).show();
        finishAndRemoveTask();
    }

    private void showConfigError(String alertTitle, final String errorMessage) {
        Log.e(LOG_TAG, "Error " + alertTitle + ": " + errorMessage);
        new AlertDialog.Builder(this)
                .setTitle(alertTitle)
                .setMessage(errorMessage)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ToxBoxActivity.this.finishAndRemoveTask();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void wakeUpAndUnlock() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        if (pm == null) {
            return;
        }

        boolean screenOn = pm.isInteractive();
        if (!screenOn) {
            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP, getPackageName() + "TAG");
            wl.acquire(10000);
            wl.release();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            if (keyguardManager == null) {
                return;
            }
            keyguardManager.requestDismissKeyguard(this, null);
        } else {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
    }

    void closeNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(0);
        ValidationHelper.stopRingtone();
    }

    public void Cancel(View view) {
        mSession.disconnect();
    }
}
