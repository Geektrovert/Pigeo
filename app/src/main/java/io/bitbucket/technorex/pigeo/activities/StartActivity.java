package io.bitbucket.technorex.pigeo.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.bitbucket.technorex.pigeo.R;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //checkPermissions();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null)
            startActivity(new Intent(StartActivity.this, MapsActivity.class));
        else
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
    }

    private void checkPermissions() {
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.INTERNET,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_CONTACTS};
        String[] permission1 = {android.Manifest.permission.READ_CONTACTS};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for(String permission : permission1){
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{permission}, 3);
                }
                else{
                    //requestPermissions(new String[]{permission},10);
                }
            }
        }
    }
}
