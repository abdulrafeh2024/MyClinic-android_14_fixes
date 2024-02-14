package com.telemedicine.myclinic.localnotification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;

import static android.os.Build.VERSION.SDK_INT;

public class NotificationHelper {


    private NotificationHelper() {

    }

    private static NotificationHelper notificationHelper = new NotificationHelper();

    public static synchronized NotificationHelper getInstance() {
        return notificationHelper;
    }


    public void scheduleNotification(Context context, long exactTime, int notificationID, String title, String body) {

        TinyDB tinyDB = new TinyDB(context);

        double futureInMillis = 0;

        String afterTime = TextUtil.getAfterTime(tinyDB);

        if (!ValidationHelper.isNullOrEmpty(afterTime)) {

            double time = Double.parseDouble(afterTime);

            time = (time * 60000);

            futureInMillis = exactTime - time;
        }

        Const.CANCELLED_ALARM = false;
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationID);
        notificationIntent.putExtra("title", title);
        notificationIntent.putExtra("body", body);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent    = PendingIntent.getBroadcast(context, notificationID, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        }else{
            pendingIntent    = PendingIntent.getBroadcast(context, notificationID, notificationIntent, 0);
        }


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (SDK_INT < Build.VERSION_CODES.M) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, (long) futureInMillis, pendingIntent);
        } else if (SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    (long) futureInMillis, pendingIntent);
        }
    }


    public static void stopAlarm(Context context, int notificationID) {
        Const.CANCELLED_ALARM = true;
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        PendingIntent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            intent = PendingIntent.getBroadcast(context, notificationID, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        }else{
            intent = PendingIntent.getBroadcast(context, notificationID, notificationIntent, 0);
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intent.cancel();
        alarmManager.cancel(intent);

    }

    public void cancelNotification(Context context, int notificationID) {
        // Cancel Notification

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationID);

        stopAlarm(context, notificationID);

    }


    private Notification getNotification(Context context, String content) {

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(context.getString(R.string.my_clininc));
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.my_clinic_icon);
        return builder.build();
    }

}
