package com.example.bmorales.test1;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.widget.Toast;

/**
 * Created by bmorales on 11/24/2016.
 */

public class Geo extends Service implements LocationListener {


    Boolean isGPSEnable = false;
    Boolean isNetworkEnable = false;
    Boolean canGetLocation = false;

    Location location;
    private double latitude;
    private double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    private static final int MY_PERMISSIONS = 1;

    protected LocationManager locationManager;
    public Geo(){};
    public Geo(Context context){
        getLocation(context);
    }

    public Location getLocation(Context context){
        try{
          locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            isGPSEnable = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);

            isNetworkEnable = locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);

            if(!isGPSEnable && !isNetworkEnable){
                Log.d("GPS", " off ");
            }
            else{


                this.canGetLocation = false;
                int status = context.getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, context.getPackageName());
                if (status == PackageManager.PERMISSION_GRANTED) {
                    if(isNetworkEnable) {

                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                this
                        );

                    }

                    if(locationManager != null){

                            location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                            Log.d("UPDATE fase 1", " UPDATE GPS LOCATION " );
                            if(location != null){
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Log.d("UPDATE", " UPDATE GPS LOCATION " );
                            }
                    }
                }

                if(isGPSEnable){
                        if(location == null){
                            locationManager.requestLocationUpdates(
                                    locationManager.NETWORK_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                    this
                            );
                            if(locationManager != null){
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return location;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
