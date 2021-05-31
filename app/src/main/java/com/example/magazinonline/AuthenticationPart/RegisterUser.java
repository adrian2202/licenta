package com.example.magazinonline.AuthenticationPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magazinonline.MainPart.Activities.Home;
import com.example.magazinonline.R;
import com.example.magazinonline.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private EditText NumeEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText prenumeEditText;
    private EditText nrTelEditText;
    private TextView Create_new_account;
    private TextView homePageTextView;
    private ProgressBar progressBar;
    private Button button_create_acount;
    SharedPreferences key;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        setVariables();
        setOnCLickListeners();
    }

    private void setVariables() {
        mAuth = FirebaseAuth.getInstance();
        key = getSharedPreferences("UID.txt", MODE_PRIVATE);
        homePageTextView = findViewById(R.id.homePageTextView);
        button_create_acount = findViewById(R.id.button_create_acount);
        NumeEditText = findViewById(R.id.NumeEditText);
        prenumeEditText = findViewById(R.id.prenumeEditText);
        nrTelEditText = findViewById(R.id.nrTelEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Create_new_account = findViewById(R.id.Create_new_account);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setOnCLickListeners() {
        homePageTextView.setOnClickListener(this);
        button_create_acount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homePageTextView:
                startActivity(new Intent(this, Home.class));
                break;
            case R.id.button_create_acount:
                button_create_acount();
                break;
        }
    }

    private void button_create_acount() {
        String email = emailEditText.getText().toString().trim();
        String prenume = prenumeEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String nrtel = nrTelEditText.getText().toString().trim();
        String name = NumeEditText.getText().toString().trim();

        if (name.isEmpty()) {
            NumeEditText.setError("Full name is required!");
            NumeEditText.requestFocus();
            return;
        }
        if (prenume.isEmpty()) {
            prenumeEditText.setError(" Prenume is required!");
            prenumeEditText.requestFocus();
            return;
        }
        if (nrtel.isEmpty()) {
            nrTelEditText.setError("Nr phone is required!");
            nrTelEditText.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            emailEditText.setText("Email is required!");
            emailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please provide valid email!");
            emailEditText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Password is required!");
            passwordEditText.requestFocus();
            return;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Min password length should be 6 characters !");
            passwordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = new User(name, email, prenume, nrtel);
                        SharedPreferences.Editor editor = key.edit();

                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            editor.putString("UUID", FirebaseAuth.getInstance()
                                    .getCurrentUser().getUid());
                            editor.apply();
                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(RegisterUser.this,
                                            "User has been successfully registered",
                                            Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.VISIBLE);
                                } else
                                    Toast.makeText(RegisterUser.this,
                                            "Failed to register! Try again!",
                                            Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);

                            });
                        }
                    } else {
                        Toast.makeText(RegisterUser.this,
                                "Failed to register! Try again!",
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}