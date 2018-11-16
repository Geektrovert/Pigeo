package io.bitbucket.technorex.pigeo.Domain;

import android.app.Activity;
import android.content.Context;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import io.bitbucket.technorex.pigeo.activities.LoginActivity;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class Profile implements Serializable {
    private String userName;
    private String emailID;
    private String passwordHash;
    private String nationalID;
    private String phoneNO;
    private GoogleSignInClient mGoogleSignInClient;

    public Profile(String userName, String emailID, String passwordHash, String nationalID, String phoneNO) {
        this.userName = userName;
        this.emailID = emailID;
        this.passwordHash = passwordHash;
        this.nationalID = nationalID;
        this.phoneNO = phoneNO;

    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getPhoneNO() {
        return phoneNO;
    }

    public void setPhoneNO(String phoneNO) {
        this.phoneNO = phoneNO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(userName, profile.userName) &&
                Objects.equals(emailID, profile.emailID) &&
                Objects.equals(passwordHash, profile.passwordHash) &&
                Objects.equals(nationalID, profile.nationalID) &&
                Objects.equals(phoneNO, profile.phoneNO);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userName, emailID, passwordHash, nationalID, phoneNO);
    }

    @NotNull
    @Override
    public String toString() {
        return "Profile{" +
                "userName='" + userName + '\'' +
                ", emailID='" + emailID + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", nationalID='" + nationalID + '\'' +
                ", phoneNO='" + phoneNO + '\'' +
                '}';
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public void logOut(final Activity activity,Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                        activity.finishAffinity();
                    }
                });
    }
}
