package com.telemedicine.myclinic;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.telemedicine.myclinic.activities.LoginActivity;
import com.telemedicine.myclinic.listeners.LogoutListener;
import com.telemedicine.myclinic.util.Const;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class AlarmBroadCastReceiver extends BroadcastReceiver {

    LogoutListener logoutListener;

    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {

       this.context = context;
      //  startServerWork();
        Const.ISLOGGOUT = true;
        //setInitialDelay(1, TimeUnit.MINUTES)
      /*  Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(false)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(false)
                .setRequiresStorageNotLow(false)
                .build();*/
      //  Toast.makeText(context, "broadcast ", Toast.LENGTH_LONG).show();
     /*   OneTimeWorkRequest mywork=
                new OneTimeWorkRequest.Builder(RefreshTokenWork.class)
                        .setConstraints(constraints).build();*/


      /*  if (intent != null) {
            boolean logout = intent.getBooleanExtra("logout", false);
            if (logout) {
                Const.ISLOGGOUT = logout;
                Log.e("Asad Called", "logincalled");
               *//* Log.e("Asad Called", "logincalled");
                logoutListener.onSessionLogout();
                Intent i = new Intent(context, LoginActivity.class);
              *//**//*  //i.setClassName("com.telemedicine.myclinic.myapplication", "com.telemedicine.myclinic.activities.LoginActivity");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("show", true);
                i.putExtra("timeout", true);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);*//*
            }

        }*/
    }


    private void createWorkRequest() {
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        OneTimeWorkRequest periodicWorkRequest = new OneTimeWorkRequest.Builder(RefreshTokenWork.class)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(context)
                .enqueueUniqueWork("sendLocation", ExistingWorkPolicy.KEEP, periodicWorkRequest);
    }

    // Then i'm learning the state of Work
    private WorkInfo.State getStateOfWork() {
        try {
            if (WorkManager.getInstance(context).getWorkInfosForUniqueWork("sendLocation").get().size() > 0) {
                return WorkManager.getInstance(context).getWorkInfosForUniqueWork("sendLocation")
                        .get().get(0).getState();
                // this can return WorkInfo.State.ENQUEUED or WorkInfo.State.RUNNING
                // you can check all of them in WorkInfo class.
            } else {
                return WorkInfo.State.CANCELLED;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            return WorkInfo.State.CANCELLED;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return WorkInfo.State.CANCELLED;
        }
    }

    // If work not ( ENQUEUED and RUNNING ) i'm running the work.
// You can check with other ways. It's up to you.
    private void startServerWork() {
        if (getStateOfWork() != WorkInfo.State.ENQUEUED && getStateOfWork() != WorkInfo.State.RUNNING) {
            createWorkRequest();
            Log.wtf("startLocationUpdates", ": server started");
        } else {
            Log.wtf("startLocationUpdates", ": server already working");
        }
    }

    public void registerSessionListner(LogoutListener logoutListener) {
        this.logoutListener = logoutListener;
    }
}
