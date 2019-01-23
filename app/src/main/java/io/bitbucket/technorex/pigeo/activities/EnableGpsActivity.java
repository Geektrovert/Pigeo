package io.bitbucket.technorex.pigeo.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.bitbucket.technorex.pigeo.R;

public class EnableGpsActivity extends Activity {
    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    @SuppressWarnings("FieldCanBeLocal")
    private LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_gps);
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        locationManager
                = (LocationManager) EnableGpsActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if(getGpsStatus(locationManager)){
            FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            Intent intent;
            if(firebaseUser != null){
                intent = new Intent(EnableGpsActivity.this,MapsActivity.class);
            } else{
                intent = new Intent(EnableGpsActivity.this,LoginActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else{
            finishAffinity();
        }
    }

    @Override
    public void onBackPressed() { }

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
