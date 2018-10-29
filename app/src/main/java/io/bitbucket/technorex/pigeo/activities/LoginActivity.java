package io.bitbucket.technorex.pigeo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import io.bitbucket.technorex.pigeo.R;
import org.jetbrains.annotations.NotNull;

public class LoginActivity extends Activity {
    private static int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private Button googleSignInButton,emailSignInButton;
    @SuppressWarnings("FieldCanBeLocal")
    private GoogleSignInOptions gso;
    private EditText email,password;
    private TextView forgotPassword,signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);

        //initializing variables
        bindVariables();

        /*Sign In with email button*/
        emailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        /*Google Sign In button*/
        //checking for a previous logged in session
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        //if there is a previous session redirect to map activity
        if(account!=null){
            startActivity(new Intent(this,MapsActivity.class));
        }

        //for signout from google id. Please do not delete this comment
        /*mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
         */

        //adding listener to google sign in button
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });
    }

    /**
     * Method for initializing variables
     */

    private void bindVariables() {
        gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton = findViewById(R.id.google_sign_in_button);
        email=findViewById(R.id.logInEmail);
        password=findViewById(R.id.logInPassword);
        emailSignInButton=findViewById(R.id.email_sign_in);
        forgotPassword=findViewById(R.id.forgotPassword);
        signUp=findViewById(R.id.signUp);
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
            startActivity(new Intent(this,MapsActivity.class));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("ApiException", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this,"signInResult:failed code=" + e.getStatusCode(),Toast.LENGTH_LONG).show();
        }
    }
}
