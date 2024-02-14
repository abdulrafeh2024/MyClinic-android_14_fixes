package com.telemedicine.myclinic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;


import com.google.common.util.concurrent.ListenableFuture;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.profilecreatoinmodels.SecurityTokenDTO;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.webservices.RestClient;

import java.util.Calendar;
import java.util.Date;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static android.os.Build.VERSION.SDK_INT;
import static com.telemedicine.myclinic.util.Const.WORKER_Count;
import static com.telemedicine.myclinic.util.Const.WORKER_EXECUTED;

public class RefreshTokenWork extends ListenableWorker {

    private static final String TAG = "TestWorker";
public Context context;
    public RefreshTokenWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return CallbackToFutureAdapter.getFuture(completer -> {
            // Your method can call set() or setException() on the
            // Completer to signal completion

            TinyDB tinyDB = new TinyDB(getApplicationContext());
            String token = tinyDB.getString(Const.TOKEN_KEY);

          //  tinyDB.putBoolean(WORKER_EXECUTED, true);
/*
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Run your task here
                    tinyDB.putInt(WORKER_Count, 1);
                }
            }, 1000);*/
           // int count=  tinyDB.getInt(WORKER_Count);
            SecurityTokenDTO securityTokenDTO = new SecurityTokenDTO(token);
           // Toast.makeText(getApplicationContext(), "worker before", Toast.LENGTH_LONG).show();
            RestClient.getInstance().getRewelToken(securityTokenDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    MobileOpResult mobileOpResult = (MobileOpResult) result;

                    if (mobileOpResult != null) {

                        if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                            tinyDB.putString(Const.TOKEN_KEY, mobileOpResult.getSecurityToken());

                            String expiryDate = mobileOpResult.getExpiryDate();
                            tinyDB.putString(Const.TOKEN_EXPIRY_KEY, expiryDate);
                          //  tinyDB.putInt(WORKER_Count, count+1);

                         /*   Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Run your task here
                                    Toast.makeText(context, "worker "+expiryDate, Toast.LENGTH_LONG).show();
                                }
                            }, 1000);*/

                            long dateLong = LocaleDateHelper.convertDateFormatZone(expiryDate, "yyyy-MM-dd'T'HH:mm:ss");

                            Date date = Calendar.getInstance().getTime();

                            long mills = dateLong - date.getTime();
                            cancelAlarmIfExists();
                            createAlarmActivity(mills-300000);
                            completer.set(ListenableWorker.Result.success());
                        }
                    }
                }
            });

            // This value is used only for debug purposes: it will be used
            // in toString() of returned future or error cases.
            return "startSomeAsyncStuff";
        });
    }

/*
    @NonNull
    @Override
    public Result doWork() {

       TinyDB tinyDB = new TinyDB(getApplicationContext());
        String token = tinyDB.getString(Const.TOKEN_KEY);

        tinyDB.putBoolean(WORKER_EXECUTED, true);


        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Run your task here
                tinyDB.putBoolean(WORKER_EXECUTED, true);
               tinyDB.putInt(WORKER_Count, 1);
            }
        }, 1000);
        int count=  tinyDB.getInt(WORKER_Count);
        SecurityTokenDTO securityTokenDTO = new SecurityTokenDTO(token);
        Toast.makeText(getApplicationContext(), "worker before", Toast.LENGTH_LONG).show();
        RestClient.getInstance().getRewelToken(securityTokenDTO, new OnResultListener() {
            @Override
            public void onResult(Object result, boolean status, String errorMessage) {

                MobileOpResult mobileOpResult = (MobileOpResult) result;

                if (mobileOpResult != null) {

                    if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                        tinyDB.putString(Const.TOKEN_KEY, mobileOpResult.getSecurityToken());

                        String expiryDate = mobileOpResult.getExpiryDate();
                        tinyDB.putString(Const.TOKEN_EXPIRY_KEY, expiryDate);
                        tinyDB.putInt(WORKER_Count, count+1);

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Run your task here
                                Toast.makeText(context, "worker "+expiryDate, Toast.LENGTH_LONG).show();
                            }
                        }, 1000);

                        long dateLong = LocaleDateHelper.convertDateFormatZone(expiryDate, "yyyy-MM-dd'T'HH:mm:ss");

                        Date date = Calendar.getInstance().getTime();

                        long mills = dateLong - date.getTime();
                        cancelAlarmIfExists();
                        createAlarmActivity(mills-60000);

                    }
                }
            }
        });
        return Result.retry();
    }
*/

    static AlarmManager alarmManager = null;
    PendingIntent pendingIntent = null;
    private void createAlarmActivity(long millis) {

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadCastReceiver.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("show", true);
        intent.putExtra("timeout", true);
        intent.setAction("Alarm Activity");
        pendingIntent = PendingIntent.getBroadcast(context,
                100, intent, FLAG_CANCEL_CURRENT);
        if (SDK_INT < Build.VERSION_CODES.M) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + millis, pendingIntent);
        } else if (SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + millis, pendingIntent);
        }
    }


    public void cancelAlarmIfExists() {
        try {

            if (pendingIntent != null)
                pendingIntent.cancel();
            if (alarmManager != null)
                alarmManager.cancel(pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}