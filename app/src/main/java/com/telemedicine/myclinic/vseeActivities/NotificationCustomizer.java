//package com.telemedicine.myclinic.vseeActivities;
//
//import android.app.Notification;
//import android.content.Context;
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.telemedicine.myclinic.App;
//import com.telemedicine.myclinic.activities.LoginActivity;
//import com.telemedicine.myclinic.myapplication.R;
//import com.vsee.kit.VSeeNotificationCenter;
//import com.vsee.kit.VSeeNotificationType;
//
///**
// * When user receives a chat message or have a missed call, VSeeKit will prepare a
// * notification builder object that contains enough details so you can post it to attract
// * user attention. You can further customize the notification with different icon,
// * sound, vibration setting, posting frequency, etc based on different types of notification,
// * before posting the them.
// */
//public class NotificationCustomizer {
//    public static void customize() {
//        VSeeNotificationCenter.instance().customizeCallForegroundNotification(R.drawable.male, "In Call Telemedicine",
//                R.drawable.male, "End Call Telemedicine");
//
//        // Set the class for the notification intent used to process the incoming push notification
//        VSeeNotificationCenter.instance().setNotificationIntentClass(LoginActivity.class);
//
//        String currentToken = FirebaseInstanceId.getInstance().getToken();
//        if (currentToken != null) {
//            VSeeNotificationCenter.instance().setFirebaseInstanceIDToken(currentToken);
//        }
//
//        setupNotificationReceiver();
//    }
//
//    private static void setupNotificationReceiver() {
//        VSeeNotificationCenter.instance().addReceiver(new VSeeNotificationCenter.VSeeNotificationCenterReceiver() {
//
//            // doNotification is called whenever VSeeKit prepares a notification to notify user
//            // that a new message arrived or a call has missed. You can either ignore it,
//            // or further customize the notification with different icon, sound, vibration
//            // for each different type of notification and post it.
//            @Override
//            public void doNotification(int id, NotificationCompat.Builder builder, VSeeNotificationType type) {
//                switch (type) {
//                    case MISSED_CALL:
//                        String missedCallUsername = VSeeNotificationCenter.instance().getBundleMissedCallUserName(builder.getExtras());
//                        if (missedCallUsername != null) {
//                            Log.d("doNotification", type.toString() + " " + missedCallUsername);
//                        } else {
//                            Log.d("doNotification", type.toString());
//                        }
//                        break;
//                    case ONE_TO_ONE_CHAT:
//                    case IN_CALL_CHAT:
//                    case GROUP_CHAT:
//                        String chatId = VSeeNotificationCenter.instance().getBundleChatChatId(builder.getExtras());
//                        if (chatId != null) {
//                            Log.d("doNotification", type.toString() + " " + chatId);
//                        } else {
//                            Log.d("doNotification", type.toString());
//                        }
//                        break;
//                    default:
//                        Log.d("doNotification", type.toString());
//                }
//
//                handleDoNotification(id, builder);
//            }
//
//            // clearNotification is called whenever user has read the messages in a chat
//            // and the notification is no longer relevant. If you post the notification,
//            // you can clear it so user does not need to read outdated notification.
//            @Override
//            public void clearNotification(int id) {
//                handleClearNotification(id);
//            }
//
//            // clearAllNotification is called when user logout or clear all chats
//            @Override
//            public void clearAllNotifications() {
//                handleClearAllNotification();
//            }
//        });
//        VSeeNotificationCenter.instance().addDialingReceiver(new VSeeNotificationCenter.VSeeDialingReceiver() {
//            @Override
//            public void onChangeDialingStatus(VSeeNotificationCenter.VSeeDialingStatus status, String message, String accountName, String displayName) {
//              /*  doToast("VSeeDialingReceiver onChangeDialingStatus status: " +
//                        status.toString() + ", message: " + message
//                        + ", accountName: " + accountName + ", displayName: " + displayName);*/
//            }
//
//            @Override
//            public void onCancelDialing() {
//                // doToast("VSeeDialingReceiver onCancelDialing");
//            }
//        });
//    }
//
//    // Customize with different defaults and icons
//    private static void handleDoNotification(int id, NotificationCompat.Builder builder) {
//        Context context = App.getInstance();
//
//        int defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND;
//
//        builder.setDefaults(defaults);
//        builder.setSmallIcon(R.drawable.male);
//
//        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
//        manager.notify(id, builder.build());
//
//    }
//
//    private static void handleClearNotification(int id) {
//        Context context = App.getInstance();
//        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
//        manager.cancel(id);
//    }
//
//    private static void handleClearAllNotification() {
//        Context context = App.getInstance();
//        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
//        manager.cancelAll();
//    }
//
//    private static void doToast(String msg) {
//        Toast.makeText(App.getInstance(), msg, Toast.LENGTH_SHORT).show();
//    }
//}
