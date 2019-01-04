package io.bitbucket.technorex.pigeo.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.bitbucket.technorex.pigeo.Domain.Profile;
import io.bitbucket.technorex.pigeo.R;
import io.bitbucket.technorex.pigeo.Service.ProfileDatabaseService;
import io.bitbucket.technorex.pigeo.Service.ProfileServerService;

import java.util.Objects;

public class EditProfileActivity extends Activity {
    private Button cancelButton, saveButton;
    private EditText name, phoneNO, nationalID, newPassword, confirmNewPassword, currentPassword;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        bindWidgets();
        bindListeners();
    }

    private void bindWidgets() {
        cancelButton = findViewById(R.id.cancel);
        saveButton = findViewById(R.id.save);
        name = findViewById(R.id.edit_name);
        phoneNO = findViewById(R.id.edit_phone_no);
        nationalID = findViewById(R.id.edit_national_id);
        newPassword = findViewById(R.id.edit_password);
        confirmNewPassword = findViewById(R.id.confirm_edit_password);
        currentPassword = findViewById(R.id.current_password);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ProfileDatabaseService profileDatabaseService = new ProfileDatabaseService(this);
        profile = profileDatabaseService.retrieveProfile();
        name.setText(profile.getUserName());
        phoneNO.setText((profile.getPhoneNO()));
        nationalID.setText(profile.getNationalID());
    }

    private void bindListeners() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditProfileActivity.this)
                        .setTitle("Confirm exit")
                        .setMessage("Discard unsaved changes?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });
    }

    private void onSave() {
        /// CHECK IF ANY DATA FIELD IS EMPTY
        if(validateInput(new EditText[]{name, phoneNO, nationalID, currentPassword})) {
            /// CHECK IF CURRENT PASSWORD IS CORRECT
            if(validateCurrentPassword(currentPassword)) {

                updateInfo();

                Log.e("-----VALIDATION--->>>", Integer.toString(validatePassword()));
                if (validatePassword() == 0) {
                    updatePassword(currentPassword.getText().toString());
                } else if (validatePassword() == 3)
                    Toast.makeText(this, "Passwords don't match", Toast.LENGTH_LONG).show();
                else if (validatePassword() == 2)
                    Toast.makeText(this, "One or more fields are empty!", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(this, "Changes saved successfully!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            else Toast.makeText(this, "Current password not correct!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "One or more fields are empty!", Toast.LENGTH_LONG).show();
        }
    }

    private void updateInfo() {
        ProfileServerService profileServerService = new ProfileServerService();
        profile.setNationalID(nationalID.getText().toString());
        profile.setPhoneNO(phoneNO.getText().toString());
        profile.setUserName(name.getText().toString());
        profileServerService.updateProfile(profile);
        ProfileDatabaseService profileDatabaseService = new ProfileDatabaseService(this);
        profileDatabaseService.updateProfile(profile);
    }

    private void updatePassword(String currentPassword) {
        final String pass = newPassword.getText().toString();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null) {
            AuthCredential authCredential = EmailAuthProvider.getCredential(Objects.requireNonNull(firebaseUser.getEmail()), currentPassword);
            firebaseUser.reauthenticate(authCredential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                firebaseUser.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            profile.setPasswordHash(Integer.toString(pass.hashCode()));

                                            /// Updating data in Firebase firestore
                                            ProfileServerService profileServerService = new ProfileServerService();
                                            profileServerService.updateProfile(profile);

                                            /// Updating data in SQLite
                                            ProfileDatabaseService profileDatabaseService = new ProfileDatabaseService(EditProfileActivity.this);
                                            profileDatabaseService.updateProfile(profile);

                                            Log.e("***-----SUCCESS--->>>", "Password updated");
                                            Toast.makeText(EditProfileActivity.this, "Changes saved successfully!", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                        else
                                            Log.e("***-----ERROR--->>>", "Not updated");
                                    }
                                });
                            }
                            else
                                Log.e("***-----ERROR--->>>", "Auth failed");
                        }
                    });
        }
    }

    private boolean validateInput(EditText[] editTexts) {
        boolean flag = true;
        for(EditText editText : editTexts)
            flag &= !editText.getText().toString().isEmpty();
        return flag;
    }

    private int validatePassword() {
        String pass = newPassword.getText().toString();
        String conPass = confirmNewPassword.getText().toString();
        if(pass.isEmpty() && conPass.isEmpty()) return 1;
        if(pass.isEmpty() || conPass.isEmpty()) return 2;
        if(!pass.equals(conPass)) return 3;
        return 0;
    }

    private boolean validateCurrentPassword(EditText editText) {
        Log.e("---PASS----->>>", Integer.toString(editText.getText().toString().hashCode()));
        return Integer.toString(editText.getText().toString().hashCode()).equals(profile.getPasswordHash());
    }
}
