package io.bitbucket.technorex.pigeo.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import io.bitbucket.technorex.pigeo.Domain.Profile;
import io.bitbucket.technorex.pigeo.R;
import io.bitbucket.technorex.pigeo.Repository.DatabaseProfileRepository;

public class ProfileDetailsActivity extends Activity {
    private TextView nameLabel;
    private TextView emailLabel;
    private TextView nationalIdLabel;
    private TextView phoneNoLabel;
    private Profile profile;

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
}
