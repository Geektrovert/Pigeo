package io.bitbucket.technorex.pigeo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import io.bitbucket.technorex.pigeo.R;

import java.util.Objects;

public class RetrievePassword extends Activity {
    private EditText email;
    private Button sendMailButton;
    private FirebaseAuth firebaseAuth;
    private String emailValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_password);
        setTitle("Retrieve Password");
        bindWidget();
        bindListeners();
    }

    private void bindListeners() {
        sendMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailValue = email.getText().toString();
                if(validateInputs(new EditText[] {email})) {
                    retrievePassword();
                }
            }
        });
    }

    private boolean validateInputs(EditText[] editTexts) {
        boolean flag = true;
        for(EditText editText : editTexts){
            if(TextUtils.isEmpty(editText.getText().toString())){
                flag = false;
                editText.setError(getString(R.string.required));
            }
        }
        return flag;
    }

    private void retrievePassword() {
        firebaseAuth.fetchProvidersForEmail(emailValue)
                .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        boolean check =
                                !Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getProviders()).isEmpty();
                        if(check){
                            sendResetEmail();
                        }
                        else{
                            Toast.makeText(RetrievePassword.this,"No associated Id found with this email!",Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }

    private void sendResetEmail() {
        firebaseAuth.sendPasswordResetEmail(emailValue)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(RetrievePassword.this,PasswordRetrievalEmailSuccessful.class));
                        }
                        else{
                            Toast.makeText(RetrievePassword.this,"No internet Connection",Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }

    private void bindWidget() {
        email = findViewById(R.id.recovery__email);
        sendMailButton = findViewById(R.id.recover_password);
        firebaseAuth = FirebaseAuth.getInstance();
    }


}
