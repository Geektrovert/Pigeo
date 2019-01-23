package io.bitbucket.technorex.pigeo.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
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
        new AlertDialog
                .Builder(this)
                .setTitle("Enable GPS")
                .setMessage("GPS is turned off. Turn on GPS?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EnableGpsActivity.this
                                .startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        locationManager
                                = (LocationManager) EnableGpsActivity.this.getSystemService(Context.LOCATION_SERVICE);
                        if(getGpsStatus(locationManager)){
                            FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                            if(firebaseUser != null){
                                startActivity(new Intent(EnableGpsActivity.this,MapsActivity.class));
                            } else{
                                startActivity(new Intent(EnableGpsActivity.this,LoginActivity.class));
                            }
                        } else{
                            finishAffinity();
                        }
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                })
                .show();
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
