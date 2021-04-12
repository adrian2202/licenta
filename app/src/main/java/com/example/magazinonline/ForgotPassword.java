package com.example.magazinonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

      private EditText emailAddress ;
      private Button  resetPasswordButton;
      private ProgressBar progressBarr;

      FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailAddress = (EditText) findViewById(R.id.emailAddress);
        resetPasswordButton= (Button) findViewById(R.id.resetPasswordButton);
        progressBarr=(ProgressBar) findViewById(R.id.progressBarr);
        auth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }

    private void resetPassword()
    {
        String email=emailAddress.getText().toString().trim();
        if(email.isEmpty())
        {
            emailAddress.setError("Email is required");
            emailAddress.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailAddress.setError("Please provide valid email!");
            emailAddress.requestFocus();
            return;

        }

        progressBarr.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
               {
                if(task.isSuccessful())
                {
                    Toast.makeText(ForgotPassword.this, "Check your email to reset your password!", Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(ForgotPassword.this, "Try again! Something wrong happened", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}