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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.bitbucket.technorex.pigeo.Domain.Profile;
import io.bitbucket.technorex.pigeo.R;
import io.bitbucket.technorex.pigeo.Repository.ProfileRepository;
import io.bitbucket.technorex.pigeo.Service.ProfileDatabaseService;
import io.bitbucket.technorex.pigeo.Service.ProfileServerService;
import org.jetbrains.annotations.NotNull;

public class LoginActivity extends Activity {
    private static int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private Button googleSignInButton,emailSignInButton;
    @SuppressWarnings("FieldCanBeLocal")
    private GoogleSignInOptions gso;
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

        //checking for a previous logged in session
        checkIfSignedIn();
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

    private void checkIfSignedIn() {
        /*Google Account*/
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        //if there is a previous session redirect to map activity
        if(account!=null){
            ProfileDatabaseService profileDatabaseService = new ProfileDatabaseService(this);
            Profile profile = profileDatabaseService.retrieveProfile();
            //profile.setmGoogleSignInClient(mGoogleSignInClient);
            startActivity(new Intent(this,MapsActivity.class).putExtra("profile",profile));
        }

        /*Email Account*/
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //if there is a previous session redirect to map activity
        if(firebaseUser!=null){
            ProfileDatabaseService profileDatabaseService = new ProfileDatabaseService(this);
            Profile profile = profileDatabaseService.retrieveProfile();
            Intent intent = new Intent(LoginActivity.this,MapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent.putExtra("profile",profile));
        }
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

            }
        });

        //Listener for sign up Button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity1.class));
            }
        });

        //Listener for sign in with google account button
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
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
                            Profile profile = getCurrentProfile(email);
                            progressDialog.dismiss();
                            startActivity(new Intent(LoginActivity.this,MapsActivity.class).putExtra("profile",profile));
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this,getString(R.string.error),Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }

    @NonNull
    private Profile getCurrentProfile(String email) {
        final Profile[] profile = {null};
        ProfileServerService profileServerService = new ProfileServerService();
        profileServerService.retrieveProfile(email, new ProfileRepository.OnResultListener<Profile>() {
            @Override
            public void onResult(Profile data) {
                profile[0] =data;
                ProfileDatabaseService profileDatabaseService = new ProfileDatabaseService(LoginActivity.this);
                profileDatabaseService.updateProfile(data);
            }
        });
        return profile[0];
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
        gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//        mGoogleApiClient.connect();
        googleSignInButton = findViewById(R.id.google_sign_in_button);
        email=findViewById(R.id.logInEmail);
        password=findViewById(R.id.logInPassword);
        emailSignInButton=findViewById(R.id.email_sign_in);
        forgotPassword=findViewById(R.id.forgotPassword);
        signUp=findViewById(R.id.signUp);
        progressDialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
    }


    /**
     * Methods for google sign in button
     */

    private void googleSignIn(){
        Intent googleSignInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(googleSignInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from mGoogleSignInClient.getSignInIntent(...);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(@NotNull Task<GoogleSignInAccount> completedTask) {
        try {
            //account variable is for passing information to the next intent
            //noinspection unused
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            assert account != null;
            Profile profile = new Profile(
                    account.getDisplayName(),
                    account.getEmail(),
                    "-1",
                    "-1",
                    "-1"
            );
            startActivity(new Intent(this,MapsActivity.class).putExtra("profile",profile));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("ApiException", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this,"signInResult:failed code=" + e.getStatusCode(),Toast.LENGTH_LONG).show();
        }
    }

}
