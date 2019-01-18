package io.bitbucket.technorex.pigeo.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import io.bitbucket.technorex.pigeo.Domain.Profile;
import io.bitbucket.technorex.pigeo.Domain.UserCount;
import io.bitbucket.technorex.pigeo.R;
import io.bitbucket.technorex.pigeo.Repository.DatabaseContactRepository;
import io.bitbucket.technorex.pigeo.Repository.DatabaseProfileRepository;

/**Project Pigeo
 * @author Sihan Tawsik, Samnan Rahee
 * Copyright TechnoRex Team
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    @SuppressWarnings("FieldCanBeLocal")
    private GoogleMap mMap;
    private Button contacts,onlineUsers, sosButton;
    @SuppressWarnings("FieldCanBeLocal")
    private ActiveUserThread activeUserThread;
    @SuppressWarnings("FieldCanBeLocal")
    private SOSCountThread sosCountThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        activeUserThread = new ActiveUserThread();
        sosCountThread = new SOSCountThread();
        try {
            activeUserThread.join();
            sosCountThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        bindWidgets();
        bindListeners();
        activeUserThread.start();
        sosCountThread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseContactRepository databaseContactRepository = new DatabaseContactRepository(this);
        contacts.setText(databaseContactRepository.getContactCount());
    }

    private void bindWidgets() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        BottomNavigationItemView bottomNavigationItemView = findViewById(R.id.user_icon);
        bottomNavigationItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this,ProfileDetailsActivity.class));
            }
        });
        contacts = findViewById(R.id.contacts);
        onlineUsers = findViewById(R.id.active_users);
        sosButton = findViewById(R.id.notifications);
    }

    private void bindListeners() {
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this,ContactListActivity.class));
            }
        });
        onlineUsers.setClickable(false);
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
            // TODO: Consider calling
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

    private class ActiveUserThread extends Thread{

        private DatabaseReference databaseReference;
        private boolean check=true;

        ActiveUserThread(){
            databaseReference= FirebaseDatabase.getInstance().getReference("/Online");
            updateOnlineUserCount();
        }

        @Override
        public void run() {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        UserCount userCount = ds.getValue(UserCount.class);
                        assert userCount != null;
                        onlineUsers.setText(userCount.getNumbers());
                        break;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        private void updateOnlineUserCount() {

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        UserCount onlineUserCount = ds.getValue(UserCount.class);
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("/Online/Users/");
                        assert onlineUserCount != null;
                        if(check) {
                            check = false;
                            databaseReference1.child("number").setValue(onlineUserCount.getNumber() + 1);
                        }
                        break;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


    private class SOSCountThread extends Thread{

        private DatabaseReference databaseReference;
        private boolean check=true;

        SOSCountThread(){
            DatabaseProfileRepository databaseProfileRepository = new DatabaseProfileRepository(MapsActivity.this);
            Profile profile = databaseProfileRepository.retrieveProfile();
            databaseReference= FirebaseDatabase.getInstance().getReference("/soscount/" + profile.getId());
        }

        @Override
        public void run() {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        UserCount userCount = ds.getValue(UserCount.class);
                        assert userCount != null;
                        sosButton.setText(userCount.getNumbers());
                        break;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/Online/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            private boolean check=true;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserCount onlineUserCount = ds.getValue(UserCount.class);
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("/Online/Users/");
                    assert onlineUserCount != null;
                    if (check) {
                        check = false;
                        databaseReference1.child("number").setValue(onlineUserCount.getNumber() - 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
