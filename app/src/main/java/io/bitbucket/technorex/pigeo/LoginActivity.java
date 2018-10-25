package io.bitbucket.technorex.pigeo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import org.jetbrains.annotations.NotNull;

/**Project Pigeo
 * @author Sihan Tawsik, Samnan Rahee
 * Copyright TechnoRex Team
 */

public class LoginActivity extends Activity {
    private static int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    @SuppressWarnings("FieldCanBeLocal")
    private SignInButton googleSignInButton;
    @SuppressWarnings("FieldCanBeLocal")
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);

        //initializing variables
        gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton = findViewById(R.id.google_sign_in_button);
        /*Sign In with email button*/


        /*Google Sign In button*/
        //checking for a previous logged in session
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        //if there is a previous session redirect to map activity
        if(account!=null){
            startActivity(new Intent(this,MapsActivity.class));
        }

        //adding listener to google sign in button
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });
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
