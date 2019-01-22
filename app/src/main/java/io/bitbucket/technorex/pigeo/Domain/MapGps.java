package io.bitbucket.technorex.pigeo.Domain;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;

public class MapGps extends Service implements LocationListener{
    private final Context context;
    boolean isGPSEnabled=false;
    boolean isNetworkEnabled=false;
    boolean canGetLocation=false;
    Location location;
    protected LocationManager locationManager;
    public MapGps(Context context){
        this.context=context;
    }
    public Location getLocation(){
        try{
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            assert locationManager != null;
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                if(isGPSEnabled){
                    if(location==null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,2,this);
                        if(locationManager!=null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }
            if(location==null){
                if(isNetworkEnabled){
                    if(location==null){
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,2,this);
                        if(locationManager!=null){
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return location;
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){ }
    @Override
    public void onProviderEnabled(String provider) { }
    @Override
    public void onProviderDisabled(String provider) {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
    @Override
    public IBinder onBind(Intent arg0){
        return null;
    }

    @Override
    public void onLocationChanged(Location location) { }
}
