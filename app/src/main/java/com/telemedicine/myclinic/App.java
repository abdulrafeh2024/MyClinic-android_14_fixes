package com.telemedicine.myclinic;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.FirebaseApp;
import com.telemedicine.myclinic.detect.AppVisibilityDetector;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.injection.AppComponent;
import com.telemedicine.myclinic.injection.AppModule;
import com.telemedicine.myclinic.injection.DaggerAppComponent;
import com.telemedicine.myclinic.listeners.LogoutListener;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.profilecreatoinmodels.SecurityTokenDTO;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.LocaleUtils;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
//import com.telemedicine.myclinic.vseeActivities.NotificationCustomizer;
//import com.telemedicine.myclinic.vseeActivities.VideoCustomizer;
import com.telemedicine.myclinic.webservices.RestClient;
//import com.vsee.kit.VSeeKit;

import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.util.Calendar;
import java.util.Date;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static android.os.Build.VERSION.SDK_INT;


public class App extends Application {

    private static App mInstance;
    private static Activity currentActivity = null;
    LogoutListener logoutListener;
    boolean isBackground = false;

    public AppComponent component;


    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        mInstance = this;
        FirebaseApp.initializeApp(this);
        setupCurrentActivity();

        java.net.CookieManager cookieManager = new java.net.CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
       // VSeeKit.setContext(this);
        //VideoCustomizer.customize();
        //NotificationCustomizer.customize();

        AppVisibilityDetector.init(this, new AppVisibilityDetector.AppVisibilityCallback() {
            @Override
            public void onAppGotoForeground() {

                if (isBackground) {
                    TinyDB tinyDB = new TinyDB(App.this);
                    long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
                    if (patientId != 0) {
                        Log.v("MyMsg", "foreground");
                        foregroundFresh();
                    }

                    isBackground = false;
                }
            }

            @Override
            public void onAppGotoBackground() {
                TinyDB tinyDB = new TinyDB(App.this);
                long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
                if (patientId != 0) {
                    Log.v("MyMsg", "background");
                    isBackground = true;
                    cancelRenewToken();
                    //backgroundFresh();
                }
            }
        });

    }

    public static App getInstance() {

        return mInstance;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    private void setupCurrentActivity() {

        // Retrieve current activity
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (TextUtil.getEnglish(activity)) {
                    LocaleUtils.initialize(App.this, LocaleUtils.ENGLISH);
                    setTheme(R.style.FullScreenAppTheme);
                } else if (TextUtil.getArabic(activity)) {
                    LocaleUtils.initialize(App.this, LocaleUtils.ARABIC);
                    setTheme(R.style.FullScreenAppThemeAr);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {
                currentActivity = null;
            }

            @Override
            public void onActivityStopped(Activity activity) {
                // Do nothing
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                // Do nothing
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                // Do nothing
            }

        });
    }

    void cancelRenewTimer() {
        /*if (timerOfRenewToken != null)
            timerOfRenewToken.cancel();*/

        if (handler1 != null)
            handler1.removeCallbacks(renewTokenThread);

        // ALLOWED_TOKEN_CALL = true;
        // new TinyDB(this).putBoolean("session_inter", true);
    }

    public void registerSessionListner(LogoutListener logoutListener) {
        this.logoutListener = logoutListener;
    }

    Handler handler1 = null;

    public synchronized void RenewToken(long millis) {

        long millseconds = 0;

        millseconds = millis - 60000;

        cancelRenewToken();

        handler1 = new Handler();

        handler1.removeCallbacks(renewTokenThread);

        handler1.postDelayed(renewTokenThread, millseconds);

    }

    public void scheduleTask(long milli){
    /*    OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWork.class)
                .build();

        WorkManager.getInstance().enqueue(oneTimeWorkRequest);
*/
    }


    Runnable renewTokenThread = new Runnable() {
        @Override
        public void run() {

            TinyDB tinyDB = new TinyDB(App.this);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {

                    if (ConnectionUtil.isInetAvailable(App.this)) {

                        String token = tinyDB.getString(Const.TOKEN_KEY);

                        SecurityTokenDTO securityTokenDTO = new SecurityTokenDTO(token);

                        RestClient.getInstance().getRewelToken(securityTokenDTO, new OnResultListener() {
                            @Override
                            public void onResult(Object result, boolean status, String errorMessage) {

                                MobileOpResult mobileOpResult = (MobileOpResult) result;

                                if (mobileOpResult != null) {

                                    if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                        tinyDB.putString(Const.TOKEN_KEY, mobileOpResult.getSecurityToken());

                                        String expiryDate = mobileOpResult.getExpiryDate();
                                        tinyDB.putString(Const.TOKEN_EXPIRY_KEY, expiryDate);

                                        long dateLong = LocaleDateHelper.convertDateFormatZone(expiryDate, "yyyy-MM-dd'T'HH:mm:ss");
                                    //    Toast.makeText(getApplicationContext(), "thread "+expiryDate, Toast.LENGTH_LONG).show();
                                        Date date = Calendar.getInstance().getTime();

                                        long mills = dateLong - date.getTime();

                                        cancelAlarmIfExists();
                                        createAlarmActivity(mills);
                                        RenewToken(mills);
                                      /*  Toast.makeText(getApplicationContext(), mobileOpResult.getSecurityToken(), Toast.LENGTH_LONG).show();
                                        RenewToken(mills-60000);*/
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
    };

    public synchronized void cancelRenewToken() {
        cancelRenewTimer();
    }

    synchronized void foregroundFresh() {


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                TinyDB tinyDB = new TinyDB(App.this);

                if (ConnectionUtil.isInetAvailable(App.this)) {

                    String token = tinyDB.getString(Const.TOKEN_KEY);

                    SecurityTokenDTO securityTokenDTO = new SecurityTokenDTO(token);

                    RestClient.getInstance().getRewelToken(securityTokenDTO, new OnResultListener() {
                        @Override
                        public void onResult(Object result, boolean status, String errorMessage) {

                            MobileOpResult mobileOpResult = (MobileOpResult) result;

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                //    Toast.makeText(App.this, "renew token call", Toast.LENGTH_LONG).show();
                                    stopService(new Intent(App.this, LocalService.class));

                                    tinyDB.putString(Const.TOKEN_KEY, mobileOpResult.getSecurityToken());

                                    String expiryDate = mobileOpResult.getExpiryDate();
                                    tinyDB.putString(Const.TOKEN_EXPIRY_KEY, expiryDate);
                                  //  Toast.makeText(getApplicationContext(), "foreground "+expiryDate, Toast.LENGTH_LONG).show();
                                    long dateLong = LocaleDateHelper.convertDateFormatZone(expiryDate, "yyyy-MM-dd'T'HH:mm:ss");

                                    Date date = Calendar.getInstance().getTime();

                                    long mills = dateLong - date.getTime();

                                    cancelAlarmIfExists();
                                    createAlarmActivity(mills);
                                    RenewToken(mills);
                                  /*  Toast.makeText(getApplicationContext(), mobileOpResult.getSecurityToken(), Toast.LENGTH_LONG).show();
                                    RenewToken(mills-60000);*/


                                }

                            }
                        }
                    });
                }
            }
        });
    }

    synchronized void backgroundFresh() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                TinyDB tinyDB = new TinyDB(App.this);

                if (ConnectionUtil.isInetAvailable(App.this)) {

                    String token = tinyDB.getString(Const.TOKEN_KEY);

                    SecurityTokenDTO securityTokenDTO = new SecurityTokenDTO(token);

                    RestClient.getInstance().getRewelToken(securityTokenDTO, new OnResultListener() {
                        @Override
                        public void onResult(Object result, boolean status, String errorMessage) {

                            MobileOpResult mobileOpResult = (MobileOpResult) result;

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                    tinyDB.putString(Const.TOKEN_KEY, mobileOpResult.getSecurityToken());

                                    String expiryDate = mobileOpResult.getExpiryDate();
                                    tinyDB.putString(Const.TOKEN_EXPIRY_KEY, expiryDate);

                                    long dateLong = LocaleDateHelper.convertDateFormatZone(expiryDate, "yyyy-MM-dd'T'HH:mm:ss");

                                    Date date = Calendar.getInstance().getTime();

                                    long mills = dateLong - date.getTime();

                                    createAlarmActivity(mills);

                                }

                            }
                        }
                    });
                }
            }
        });
    }

    static AlarmManager alarmManager = null;
    PendingIntent pendingIntent = null;

    private void createAlarmActivity(long millis) {

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmBroadCastReceiver.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("show", true);
        intent.putExtra("timeout", true);
        intent.setAction("Alarm Activity");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(this,
                    100, intent,  PendingIntent.FLAG_IMMUTABLE);
        }else {
            pendingIntent = PendingIntent.getBroadcast(this,
                    100, intent, FLAG_CANCEL_CURRENT);
        }

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

    public void startAlarm(long mills) {
        createAlarmActivity(mills);
    }
}
