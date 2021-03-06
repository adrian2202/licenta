package com.example.magazinonline.AuthenticationPart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.magazinonline.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText emailAddress;
    private Button resetPasswordButton;
    private ProgressBar progressBarr;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setVariables();
        setOnClickListeners();
    }

    private void setVariables() {
        auth = FirebaseAuth.getInstance();
        emailAddress = findViewById(R.id.emailAddress);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        progressBarr = findViewById(R.id.progressBarr);
    }

    private void setOnClickListeners() {
        resetPasswordButton.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String email = emailAddress.getText().toString().trim();

        if (email.isEmpty()) {
            emailAddress.setError(getResources().getString(R.string.email_required));
            emailAddress.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailAddress.setError(getResources().getString(R.string.please_provide_valid_email));
            emailAddress.requestFocus();
            return;
        }

        progressBarr.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ForgotPassword.this,
                        getResources().getString(R.string.check_email_reset_password),
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ForgotPassword.this,
                        getResources().getString(R.string.try_again_something_happened),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}