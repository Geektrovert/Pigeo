package io.bitbucket.technorex.pigeo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import io.bitbucket.technorex.pigeo.R;

public class PasswordRetrievalEmailSuccessful extends AppCompatActivity {
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_retrieval_email_successful);
        bindWidget();
        bindListeners();
    }

    private void bindWidget() {
        backButton = findViewById(R.id.back);
    }

    private void bindListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PasswordRetrievalEmailSuccessful.this,LoginActivity.class));
    }
}
