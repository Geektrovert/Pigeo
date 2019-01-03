package io.bitbucket.technorex.pigeo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.bitbucket.technorex.pigeo.Domain.Profile;
import io.bitbucket.technorex.pigeo.R;
import io.bitbucket.technorex.pigeo.Repository.DatabaseProfileRepository;

public class ProfileDetailsActivity extends Activity {
    private TextView nameLabel;
    private TextView emailLabel;
    private TextView nationalIdLabel;
    private TextView phoneNoLabel;
    private Profile profile;
    private Button logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        if(getIntent().getExtras()!=null){
            profile=(Profile) getIntent().getExtras().get("profile");
        }
        nameLabel=findViewById(R.id.name_label);
        emailLabel=findViewById(R.id.email_label);
        nationalIdLabel=findViewById(R.id.national_id_label);
        phoneNoLabel=findViewById(R.id.phone_number_label);
        logOutButton = findViewById(R.id.log_out_button);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null) {
                    profile.logOut();
                    startActivity(new Intent(ProfileDetailsActivity.this, LoginActivity.class));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        nameLabel.setText(profile.getUserName());
        emailLabel.setText(profile.getEmailID());
        nationalIdLabel.setText(profile.getNationalID());
        phoneNoLabel.setText(profile.getPhoneNO());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DatabaseProfileRepository databaseProfileRepository = new DatabaseProfileRepository(this);
        profile = databaseProfileRepository.retrieveProfile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.edit_profile)
            startActivity(new Intent(ProfileDetailsActivity.this, EditProfileActivity.class).putExtra("profile", profile));
        return super.onOptionsItemSelected(item);
    }
}
