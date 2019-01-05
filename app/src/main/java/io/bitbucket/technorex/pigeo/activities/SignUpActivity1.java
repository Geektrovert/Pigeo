package io.bitbucket.technorex.pigeo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import io.bitbucket.technorex.pigeo.R;

public class SignUpActivity1 extends Activity {

    private EditText signUpEmail, signUpPassword, confirmPassword;
    private Button nextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);
        bindWidgets();
        bindListeners();
    }

    private void bindWidgets() {
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        nextPage = findViewById(R.id.next);
    }

    private void bindListeners() {
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput(new EditText[]{signUpEmail,signUpPassword,confirmPassword})){
                	if(signUpPassword.getText().toString().equals(confirmPassword.getText().toString())) {
	                    String email = signUpEmail.getText().toString();
	                    String pass = signUpPassword.getText().toString();
	                    String[] EmailAndPassword = {email, pass};
	                    startActivity(new Intent(SignUpActivity1.this, SignUpActivity2.class).putExtra("EmailAndPass", EmailAndPassword));
	                }
	                else{
	                	setError(signUpPassword,R.string.passwords_dont_match);
	                	Toast.makeText(SignUpActivity1.this, "Passwords don't match!", Toast.LENGTH_LONG).show();
	                }
                }
            }
        });
    }

    private boolean validateInput(EditText[] editTexts) {
        boolean flag = true;
        for(EditText editText : editTexts) {
            flag &= !editText.getText().toString().isEmpty();
            if(editText.getText().toString().isEmpty()){
                setError(editText,R.string.field_cant_be_empty);
            }
        }
        return flag;
    }

    private void setError(EditText field, int messageRes){
    	field.setError(getString(messageRes));
    }
}
