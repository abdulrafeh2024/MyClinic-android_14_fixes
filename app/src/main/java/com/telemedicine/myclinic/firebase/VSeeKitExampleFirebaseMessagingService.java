package com.telemedicine.myclinic.firebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.telemedicine.myclinic.activities.PhoneCallActivity;
import com.telemedicine.myclinic.activities.ZoomMeetingActivity;
import com.telemedicine.myclinic.broadcast.HandleBroadcastReceiver;
import com.telemedicine.myclinic.localnotification.LocalNotification;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.toxbox.ToxBoxActivity;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.zipow.videobox.ptapp.IMProtos;
//import com.vsee.kit.VSeeNotificationCenter;
//import com.vsee.notification.NotificationCenter;

import org.json.JSONObject;

import java.util.Map;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * Created by ruozhen on 4/17/17.
 */

public class VSeeKitExampleFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "MessagingService";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        Log.d(TAG,"onNewToken : "+token);
       // NotificationCenter.setPushNotificationRegistered(false);
        //VSeeNotificationCenter.instance().setFirebaseInstanceIDToken(token);

    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "onMessageReceived: " + remoteMessage.getFrom());
        Log.d(TAG, "Data: " + remoteMessage.getData().toString());
        // Necessary to feed the FCM message to VSeeKit

        try {
            Map<String, String> params = remoteMessage.getData();

            JSONObject object = new JSONObject(params);

            if (object != null) {

                Log.e("JSON OBJECT", object.toString());
                String specialty = null;
                String name = null;


                if (object.has("SessionId") && object.has("Token")) {

                    if (object.has("DoctorDetails")) {

                        String doctorDetails = object.getString("DoctorDetails");
                        JSONObject detail = new JSONObject(doctorDetails);
                        specialty = detail.getString("Specialty");
                        name = detail.getString("name");
                        Log.e("specialty", specialty);
                        Log.e("name", name);
                    }



                    String sessionId = object.getString("SessionId");
                    String tokenId = object.getString("Token");
                    String apptId = object.getString("ApptId");
                    String patientName = object.getString("PatientName");

                    //sessionId = meeting id
                    //ptTokenId = password
                    if (!ValidationHelper.isNullOrEmpty(sessionId) && !ValidationHelper.isNullOrEmpty(tokenId)) {
                        callToxBox(tokenId, sessionId, specialty, name, apptId, patientName);
                    }
                }

                if (object.has("alert")) {
                    String alert = object.getString("alert");
                    if (!ValidationHelper.isNullOrEmpty(alert)) {
                        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        LocalNotification localNotification = new LocalNotification(getApplicationContext());
                        localNotification.createNotification(notificationManager, "", alert, 1);
                    }
                }

                if(object.has("aps")){
                    String apsObj = object.getString("aps");
                    JSONObject apsJson = new JSONObject(apsObj);
                    String url = apsJson.getString("url");
                    String alertObj = apsJson.getString("alert");
                    JSONObject alertJson = new JSONObject(alertObj);
                    String title = alertJson.getString("title");
                    String body = alertJson.getString("body");
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    LocalNotification localNotification = new LocalNotification(getApplicationContext());
                    localNotification.createNotification(notificationManager, title, body, 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

       // VSeeNotificationCenter.instance().handleFirebaseNotification(remoteMessage.getData());
    }

/*    void callToxBox(String tokenId, String sessionId) {
        Intent intent = new Intent(this, PhoneCallActivity.class);
        intent.putExtra("tokenId", tokenId);
        intent.putExtra("sessionId", sessionId);
       startActivity(intent);
        Log.e("Called", "called activity");
    }*/

    void callToxBox(String tokenId, String sessionId, String speciality, String name, String apptId, String patientName) {

        Intent intent = new Intent(this, PhoneCallActivity.class);
        intent.putExtra("tokenId", tokenId);
        intent.putExtra("sessionId", sessionId);
        intent.putExtra("name", name);
        intent.putExtra("apptId", apptId);
        intent.putExtra("PatientName", patientName);
        TextUtil.name = name;
        TextUtil.speciality = speciality;

        Intent toxboxIntent = new Intent(this, ToxBoxActivity.class);
        toxboxIntent.putExtra("tokenId", tokenId);
        toxboxIntent.putExtra("sessionId", sessionId);
        toxboxIntent.putExtra("PatientName", patientName);

        Intent zoomIntent = new Intent(this, ZoomMeetingActivity.class);
        zoomIntent.putExtra("tokenId", tokenId);
        zoomIntent.putExtra("sessionId", sessionId);
        zoomIntent.putExtra("name", name);
        zoomIntent.putExtra("apptId", apptId);
        zoomIntent.putExtra("PatientName", patientName);

        //Create an Intent for the BroadcastReceiver
        Intent intentDismiss = new Intent(this, HandleBroadcastReceiver.class);

        //Create the PendingIntent
        PendingIntent pIntentDismiss;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pIntentDismiss = PendingIntent.getBroadcast(this, 0, intentDismiss, PendingIntent.FLAG_IMMUTABLE);
        }else{
            pIntentDismiss   = PendingIntent.getBroadcast(this, 0, intentDismiss, 0);
        }

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = getString(R.string.my_clininc);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "MyClinicNotification", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription(getString(R.string.app_name)+" "+getString(R.string.text_calling));
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Play ringtone on call arrival.
         ValidationHelper.defaultRing(this);

        // assuming your main activity
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this,
                    0, intent, PendingIntent.FLAG_IMMUTABLE);

        }else {
            pendingIntent = PendingIntent.getActivity(this,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
               // PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT  | PendingIntent.FLAG_IMMUTABLE);
     // PendingIntent toxBoxPendingIntent = PendingIntent.getActivity(this, 0, toxboxIntent, FLAG_UPDATE_CURRENT);

      /*  PendingIntent zoomPendingIntent;
        if(mMeetingService != null){
            if(mMeetingService.getMeetingStatus() == null || mMeetingService.getMeetingStatus() == MeetingStatus.MEETING_STATUS_IDLE){
                zoomPendingIntent = PendingIntent.getActivity(this, 0, zoomIntent, FLAG_UPDATE_CURRENT);
            }else{
                zoomPendingIntent =  PendingIntent.getBroadcast(this, 0, intentDismiss, 0);
            }
        }else{
            zoomPendingIntent = PendingIntent.getActivity(this, 0, zoomIntent, FLAG_UPDATE_CURRENT);
        }*/
        int requestID = (int) System.currentTimeMillis();
        PendingIntent toxBoxPendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            toxBoxPendingIntent  = PendingIntent.getActivity(this, requestID, zoomIntent, PendingIntent.FLAG_IMMUTABLE);
        }else{
            toxBoxPendingIntent  = PendingIntent.getActivity(this, requestID, zoomIntent, 0);
        }


       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setTimeoutAfter(20000);
        }*/
        notificationBuilder.setAutoCancel(true)
                .setCategory(Notification.CATEGORY_CALL)
                .setOngoing(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notification)
                .setTicker("Hearty365")//.setSound(defaultTone)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(name + getString(R.string.text_calling))
                .setContentText(speciality)
                .setFullScreenIntent(pendingIntent, true)

                .addAction(new NotificationCompat.Action.Builder(
                        R.drawable.ic_stat_call,
                        HtmlCompat.fromHtml("<font color=\"" + ContextCompat.getColor(this, R.color.ios_red) + "\">" + getString(R.string.reject) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY),
                        pIntentDismiss).build())
                .addAction(new NotificationCompat.Action.Builder(
                        R.drawable.ic_stat_call_end,
                        HtmlCompat.fromHtml("<font color=\"" + ContextCompat.getColor(this, R.color.colorGreen) + "\">" + getString(R.string.accept) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY),
                        toxBoxPendingIntent).build())
                .setContentInfo(getString(R.string.app_name)+" "+getString(R.string.text_calling));

////                .addAction(new NotificationCompat.Action.Builder(
////                        R.drawable.vsee_kit_reject_button_shape,
////                        HtmlCompat.fromHtml("<font color=\"" + ContextCompat.getColor(this, R.color.ios_red) + "\">" + getString(R.string.reject) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY),
////                        pIntentDismiss).build())
////                .addAction(new NotificationCompat.Action.Builder(
////                        R.drawable.vsee_kit_call_icon,
////                        HtmlCompat.fromHtml("<font color=\"" + ContextCompat.getColor(this, R.color.vsee_kit_green) + "\">" + getString(R.string.accept) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY),
////                        toxBoxPendingIntent).build())
//                .setContentInfo("MyClinic Call");
//>>>>>>> Stashed changes

        notificationManager.notify(0, notificationBuilder.build());

    }
}
