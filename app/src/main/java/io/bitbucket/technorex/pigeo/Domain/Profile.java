package io.bitbucket.technorex.pigeo.Domain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    public Profile(){

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

    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public void setmGoogleSignInClient(GoogleSignInClient mGoogleSignInClient) {
        this.mGoogleSignInClient = mGoogleSignInClient;
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

    public void logOut(final Activity activity) {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        activity.finishAffinity();
                    }
        });
    }
}
