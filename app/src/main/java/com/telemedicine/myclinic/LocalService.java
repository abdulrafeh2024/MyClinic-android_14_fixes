package com.telemedicine.myclinic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.telemedicine.myclinic.activities.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

public class LocalService extends Service {
    private static Timer timer = new Timer();
    private Context ctx;
    long mili = 0;

    public IBinder onBind(Intent arg0) {


        return null;
    }

    public void onCreate() {
        super.onCreate();
        ctx = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mili = intent.getLongExtra("milli", 0);
        startService(mili);
        return super.onStartCommand(intent, flags, startId);
    }

    private void startService(long mili) {
        timer.scheduleAtFixedRate(new mainTask(), mili, mili);
    }

    private class mainTask extends TimerTask {
        public void run() {
            toastHandler.sendEmptyMessage(0);
        }
    }

    public void onDestroy() {
        super.onDestroy();

        if (timer != null)
            timer.cancel();
    }

    private final Handler toastHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //  Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LocalService.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("show", true);
            intent.putExtra("timeout", true);
            startActivity(intent);
        }
    };
}
