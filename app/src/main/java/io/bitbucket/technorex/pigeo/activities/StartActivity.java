package io.bitbucket.technorex.pigeo.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.bitbucket.technorex.pigeo.R;

public class StartActivity extends Activity {
    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    @SuppressWarnings("FieldCanBeLocal")
    private LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(getGpsStatus(locationManager)){
            if(firebaseUser != null){
                startActivity(new Intent(StartActivity.this,MapsActivity.class));
            } else{
                startActivity(new Intent(StartActivity.this,LoginActivity.class));
            }
        } else{
            startActivity(new Intent(StartActivity.this,EnableGpsActivity.class));
        }
    }

    private boolean getGpsStatus(LocationManager locationManager) {
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return gps_enabled || network_enabled;
    }
}
