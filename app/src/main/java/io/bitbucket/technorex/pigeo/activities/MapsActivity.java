package io.bitbucket.technorex.pigeo.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.*;
import io.bitbucket.technorex.pigeo.Domain.Contact;
import io.bitbucket.technorex.pigeo.Domain.MapGps;
import io.bitbucket.technorex.pigeo.Domain.Profile;
import io.bitbucket.technorex.pigeo.Domain.UserCount;
import io.bitbucket.technorex.pigeo.R;
import io.bitbucket.technorex.pigeo.Repository.DatabaseContactRepository;
import io.bitbucket.technorex.pigeo.Repository.DatabaseProfileRepository;

import java.util.ArrayList;
import java.util.List;

/**Project Pigeo
 * @author Sihan Tawsik, Samnan Rahee
 * Copyright TechnoRex Team
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    @SuppressWarnings("FieldCanBeLocal")
    private GoogleMap mMap;
    private Button contacts,onlineUsers, notificationButton;
    @SuppressWarnings("FieldCanBeLocal")
    private Profile profile;
    @SuppressWarnings("FieldCanBeLocal")
    private DatabaseProfileRepository databaseProfileRepository;

    MapGps mGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        bindWidgets();
        bindListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseContactRepository databaseContactRepository = new DatabaseContactRepository(this);
        contacts.setText(databaseContactRepository.getContactCount());

        //updating user activity status
        DatabaseReference onlineUserDatabaseReference
                = FirebaseDatabase.getInstance().getReference("/Online/");
        onlineUserDatabaseReference.child(profile.getPhoneNO()).setValue("true");
    }

    private void bindWidgets() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        BottomNavigationItemView userBottomNavigationItemView = findViewById(R.id.user_icon);
        userBottomNavigationItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this,ProfileDetailsActivity.class));
            }
        });

        BottomNavigationItemView sosBottomNavigationItemView = findViewById(R.id.sos);
        sosBottomNavigationItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSOS();
                startActivity(new Intent(MapsActivity.this,SOSActivity.class));
            }
        });

        mGPS = new MapGps(this);
        contacts = findViewById(R.id.contacts);
        onlineUsers = findViewById(R.id.active_users);
        notificationButton = findViewById(R.id.notifications);
        databaseProfileRepository = new DatabaseProfileRepository(this);
        profile = databaseProfileRepository.retrieveProfile();
    }

    private void sendSOS() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/");
        final List<Contact> contacts;
        DatabaseContactRepository databaseContactRepository
                = new DatabaseContactRepository(this);

        Location location = mGPS.getLocation();
        contacts = databaseContactRepository.listContacts();
        for(Contact contact : contacts){
            databaseReference
                    .child(contact.getContactNumber())
                    .child(profile.getPhoneNO())
                    .child("name").setValue(profile.getUserName());

            databaseReference
                    .child(contact.getContactNumber())
                    .child(profile.getPhoneNO())
                    .child("contactNumber").setValue(profile.getPhoneNO());
            databaseReference
                    .child(contact.getContactNumber())
                    .child(profile.getPhoneNO())
                    .child("latitude").setValue(location.getLatitude());
            databaseReference
                    .child(contact.getContactNumber())
                    .child(profile.getPhoneNO())
                    .child("longitude").setValue(location.getLongitude());
        }
    }

    private void bindListeners() {
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this,ContactListActivity.class));
            }
        });

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, NotificationActivity.class));
            }
        });

        onlineUsers.setClickable(false);

        DatabaseReference sosDatabaseReference
                = FirebaseDatabase.getInstance().getReference("/"+profile.getPhoneNO()+"/");
        sosDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for(DataSnapshot ignored : dataSnapshot.getChildren()){
                    count++;
                }
                notificationButton.setText(Integer.toString(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });@SuppressWarnings("unused")

        DatabaseReference onlineUserDatabaseReference
                = FirebaseDatabase.getInstance().getReference("/Online/");
        onlineUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for(DataSnapshot ignored : dataSnapshot.getChildren()){
                    count++;
                }
                onlineUsers.setText(Integer.toString(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        /*Dialogue when back button pressed*/
        new AlertDialog.Builder(this)
                .setTitle(R.string.exit_title)
                .setMessage(R.string.exit_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
/*        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/Online/");
        databaseReference.child(profile.getPhoneNO()).setValue(null);
    }
}
