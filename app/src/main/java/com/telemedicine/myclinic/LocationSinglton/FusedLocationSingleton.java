package com.telemedicine.myclinic.LocationSinglton;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.telemedicine.myclinic.App;


/**
 * author: Asad Abbas
 */
public class FusedLocationSingleton implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    /***********************************************************************************************
     * properties
     **********************************************************************************************/
    private static FusedLocationSingleton mInstance = null;
    protected GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public final static int FAST_LOCATION_FREQUENCY = 1 * 1000;
    public final static int LOCATION_FREQUENCY = 1 * 1000;

    private static final String CHANNEL_ID = "my_channel";
    private static final long UPDATE_INTERVAL_IN_MIL = 1000;
    private static final long FASTES_UPDATE_INTERVAL_IN_MIL = UPDATE_INTERVAL_IN_MIL / 2;
    private static final int NOTI_ID = 1223;

    /***********************************************************************************************
     * methods
     **********************************************************************************************/
    /**
     * constructor
     */
    public FusedLocationSingleton() {
        buildGoogleApiClient();
    }

    /**
     * destructor
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        stopLocationUpdates();
    }

    public static FusedLocationSingleton getInstance() {
        if (null == mInstance) {

            mInstance = new FusedLocationSingleton();
        }
        return mInstance;
    }

    ///////////// 1

    /**
     * builds a GoogleApiClient
     */
    private synchronized void buildGoogleApiClient() {
        // setup googleapi client
        mGoogleApiClient = new GoogleApiClient.Builder(App.getInstance())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        // setup location updates
        configRequestLocationUpdate();
    }

    ///////////// 2

    /**
     * config request location update
     */
    private void configRequestLocationUpdate() {
        mLocationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(LOCATION_FREQUENCY)
                .setFastestInterval(FAST_LOCATION_FREQUENCY);
    }

    ///////////// 3

    /**
     * request location updates
     */
    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(App.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(App.getInstance(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
        );
    }

    /**
     * start location updates
     */
    public void startLocationUpdates() {
        // connect and force the updates
        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    /**
     * removes location updates from the FusedLocationApi
     */
    public void stopLocationUpdates() {
        // stop updates, disconnect from google api
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * get last available location
     *
     * @return last known location
     */
    public Location getLastLocation() {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            // return last location
            if (ActivityCompat.checkSelfPermission(App.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(App.getInstance(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } else {
            startLocationUpdates(); // start the updates
            return null;
        }
    }

    /***********************************************************************************************
     * GoogleApiClient Callbacks
     **********************************************************************************************/
    @Override
    public void onConnected(Bundle bundle) {
        // do location updates
        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // connection to Google Play services was lost for some reason
        if (null != mGoogleApiClient) {
            mGoogleApiClient.connect(); // attempt to establish a new connection
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /***********************************************************************************************
     * Location Listener Callback
     **********************************************************************************************/
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {

            // send location in broadcast
            Intent intent = new Intent("location");
            intent.putExtra("location", location);
            LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(intent);
            stopLocationUpdates();
        }
    }

    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Handler serviceHandler;
    private Location mLocation;



}