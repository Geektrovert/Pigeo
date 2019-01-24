package io.bitbucket.technorex.pigeo.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.bitbucket.technorex.pigeo.Domain.Contact;
import io.bitbucket.technorex.pigeo.R;
import io.bitbucket.technorex.pigeo.Repository.ContactRepository;
import io.bitbucket.technorex.pigeo.Service.ContactDatabaseService;
import io.bitbucket.technorex.pigeo.Service.ContactServerService;

import java.util.List;

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
//        if(getGpsStatus(locationManager)){
            if(firebaseUser != null) {
                retrieveContacts();
                startActivity(new Intent(StartActivity.this,MapsActivity.class));
            } else{
                startActivity(new Intent(StartActivity.this,LoginActivity.class));
            }
//        } else{
//            startActivity(new Intent(StartActivity.this,EnableGpsActivity.class));
//        }
    }

    private void retrieveContacts() {
        final ContactDatabaseService contactDatabaseService = new ContactDatabaseService(this);
        ContactServerService contactServerService = new ContactServerService();

        contactServerService.listCardsAsync(new ContactRepository.OnResultListener<List<Contact>>() {
            @Override
            public void onResult(List<Contact> data) {
                contactDatabaseService.resetContacts();
                contactDatabaseService.addToContacts(data);
            }
        });
    }
}
