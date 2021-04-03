package com.example.magazinonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LogIn extends AppCompatActivity implements View.OnClickListener {
    private EditText Email;
    private EditText Password;
    private TextView CreateAccount;
    private TextView ForgotPassword;
    private Button Login;
    private ProgressBar ProgressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        Email = (EditText) findViewById(R.id.editEmailAddress);
        Password = (EditText) findViewById(R.id.editTextPassword);
        CreateAccount = (TextView) findViewById(R.id.textCreeate);
        ForgotPassword = (TextView) findViewById(R.id.textForgot);
        ProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        Login = (Button) findViewById(R.id.button);
        CreateAccount.setOnClickListener(this);
        Login.setOnClickListener(this);
        ForgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.textCreeate:
                startActivity(new Intent(this ,RegisterUser.class ) );
                break;
            case R.id.button:
                userLogin();
                break;
            case R.id.textForgot:
                startActivity(new Intent(this,  ForgotPassword.class));
                break;

        }
    }

    private void userLogin() {
        String email= Email.getText().toString().trim();
        String password=Password.getText().toString().trim();

        if(email.isEmpty())
        {
            Email.setError("Email is required!");
            Email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Email.setError("Please enter a valid email !");
            Email.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            Password.setError("Password is required");
            Password.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            Password.setError("Min password in 6 characters!");
            Password.requestFocus();
            return;
        }
        ProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    startActivity(new Intent (LogIn.this, ProfileActivity.class));

                }
                else
                {
                    Toast.makeText(LogIn.this, "Failed to login!Please check your credentials", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
