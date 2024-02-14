package com.telemedicine.myclinic.localnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.telemedicine.myclinic.util.Const;


public class NotificationPublisher extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {
        boolean cancel = Const.CANCELLED_ALARM;
        if (cancel) {
            Const.CANCELLED_ALARM = false;
        } else {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            int id = intent.getIntExtra(NOTIFICATION_ID, 0);
            String title = intent.getStringExtra("title");
            String body = intent.getStringExtra("body");
            LocalNotification localNotification = new LocalNotification(context);
            localNotification.createNotification(notificationManager, title, body, id);
        }
    }
}
