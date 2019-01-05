package io.bitbucket.technorex.pigeo.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import io.bitbucket.technorex.pigeo.Domain.Profile;
import io.bitbucket.technorex.pigeo.Domain.UserCount;
import io.bitbucket.technorex.pigeo.R;
import io.bitbucket.technorex.pigeo.Repository.ProfileRepository;
import io.bitbucket.technorex.pigeo.Service.ProfileDatabaseService;
import io.bitbucket.technorex.pigeo.Service.ProfileServerService;

public class SignUpActivity2 extends Activity {
    private String[] strings;
    private EditText fullName, phoneNo, nationalID;
    private Button signUpButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);
        bindWidgets();
        bindListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getIntent().getExtras() != null) {
            strings = (String[]) getIntent().getExtras().get("EmailAndPass");
        }
    }

    private void bindWidgets() {
        fullName = findViewById(R.id.signUpFullName);
        phoneNo = findViewById(R.id.signUpPhoneNumber);
        nationalID = findViewById(R.id.signUpNationalId);
        signUpButton = findViewById(R.id.email_sign_up);
        progressDialog = new ProgressDialog(this);
    }

    private void bindListeners() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(strings[0],strings[1]).addOnCompleteListener(SignUpActivity2.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.setTitle("Completing registration and logging in...");
                            progressDialog.show();
                            logIn();
                        }
                        else{
                            Toast.makeText(SignUpActivity2.this,"Registration Failed!",Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

    private void logIn() {
        final Profile profile = new Profile();
        profile.setEmailID(strings[0]);
        profile.setUserName(fullName.getText().toString());
        profile.setPhoneNO(phoneNo.getText().toString());
        profile.setNationalID(nationalID.getText().toString());
        profile.setPasswordHash(Integer.toString(strings[1].hashCode()));
        final ProfileServerService profileServerService = new ProfileServerService();
        profileServerService.getUserCount(new UserCount(), new ProfileRepository.OnResultListener<UserCount>() {
            @Override
            public void onResult(UserCount data) {
                profile.setId(Integer.toString(data.getNumber() + 1));
                profileServerService.incrementUser(data);
                profileServerService.addProfile(profile);

                ProfileDatabaseService profileDatabaseService = new ProfileDatabaseService(SignUpActivity2.this);
                profileDatabaseService.reset();
                profileDatabaseService.addProfile(profile);
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                firebaseAuth.signInWithEmailAndPassword(profile.getEmailID(), strings[1])
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    startActivity(new Intent(SignUpActivity2.this, MapsActivity.class));
                                }
                                else{
                                    Toast.makeText(SignUpActivity2.this,getString(R.string.error),Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
            }
        });
    }
}
