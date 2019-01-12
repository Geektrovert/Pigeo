package io.bitbucket.technorex.pigeo.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import io.bitbucket.technorex.pigeo.Domain.Profile;
import io.bitbucket.technorex.pigeo.R;
import io.bitbucket.technorex.pigeo.Repository.ProfileRepository;
import io.bitbucket.technorex.pigeo.Service.ProfileDatabaseService;
import io.bitbucket.technorex.pigeo.Service.ProfileServerService;

/**Project Pigeo
 * @author Sihan Tawsik, Samnan Rahee
 * Copyright TechnoRex Team
 */

public class LoginActivity extends Activity {
    private Button emailSignInButton;
    private EditText email,password;
    private TextView forgotPassword,signUp;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);

        //initializing variables
        bindVariables();

        /*Bind listeners to layout views*/
        bindListeners();

    }

    @Override
    protected void onStart() {
        super.onStart();
        email.setText("");
        password.setText("");
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
                .setNegativeButton(R.string.no,null)
                .show();
    }

    private void bindListeners() {
        /*Listener for email log in button*/
        emailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInput()){
                    String emailValue = email.getText().toString();
                    String passwordValue = password.getText().toString();
                    progressDialog.setTitle("Logging in...");
                    progressDialog.show();
                    emailSignIn(emailValue,passwordValue);
                }
            }
        });

        /*Listener for forgot password Text*/
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RetrievePassword.class));
            }
        });

        //Listener for sign up Button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity1.class));
            }
        });
    }

    /**
     * Method for signing in with email and password
     * @param email String
     * @param password String
     */

    private void emailSignIn(final String email, String password) {
        //signing in with email
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            final Profile[] profile = {null};
                            ProfileServerService profileServerService = new ProfileServerService();
                            profileServerService.retrieveProfile(email, new ProfileRepository.OnResultListener<Profile>(){
                                @Override
                                public void onResult(Profile data) {
                                    profile[0] =data;
                                    Log.e("***---ppp--->>>", profile[0].toString());
                                    ProfileDatabaseService profileDatabaseService = new ProfileDatabaseService(LoginActivity.this);
                                    profileDatabaseService.reset();
                                    profileDatabaseService.addProfile(data);
                                    progressDialog.dismiss();
                                    startActivity(new Intent(LoginActivity.this,MapsActivity.class));
                                }
                            });
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this,getString(R.string.error),Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }

    /**
     * Method for validating inputs
     * @return boolean
     */

    private boolean validateInput() {
        boolean flag=true;
        if(email.getText().toString().isEmpty()){
            flag=false;
            email.setError(getString(R.string.required));
        }
        if(password.getText().toString().isEmpty()){
            flag=false;
            password.setError(getString(R.string.required));
        }
        return flag;
    }

    /**
     * Method for initializing variables
     */

    private void bindVariables() {
        email=findViewById(R.id.logInEmail);
        password=findViewById(R.id.logInPassword);
        emailSignInButton=findViewById(R.id.email_sign_in);
        forgotPassword=findViewById(R.id.forgotPassword);
        signUp=findViewById(R.id.signUp);
        progressDialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
    }

}
