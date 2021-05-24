package com.example.magazinonline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity implements View.OnClickListener {
    private EditText Email;
    private EditText Password;
    private TextView CreateAccount;
    private TextView ForgotPassword;
    private Button Login;
    private ProgressBar ProgressBar;
    private FirebaseAuth mAuth;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // in cazul in care utilizatorul este deja autentificat, il ducem direct pe profil
        if (currentUser != null) {
            goToActivity(ProfileActivity.class);
        }

        // in cazul in care utilizatorul nu este autentificat, randam activitatea curenta
        else {
            setContentView(R.layout.activity_log_in);
            setVariables();
            setOnClickListeners();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setVariables() {
        mAuth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.editEmailAddress);
        Password = findViewById(R.id.editTextPassword);
        CreateAccount = findViewById(R.id.textCreeate);
        ForgotPassword = findViewById(R.id.textForgot);
        ProgressBar = findViewById(R.id.progressBar);
        Login = findViewById(R.id.button);
        drawer = findViewById(R.id.drawer_layout);
    }

    private void setOnClickListeners() {
        CreateAccount.setOnClickListener(this);
        Login.setOnClickListener(this);
        ForgotPassword.setOnClickListener(this);
    }

    // metoda pentru mers la o anumita activitate
    private void goToActivity(Class<? extends Activity> activity) {
        startActivity(new Intent(this, activity));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textCreeate:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.button:
                userLogin();
                break;
            case R.id.textForgot:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    private void userLogin() {
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if (email.isEmpty()) {
            Email.setError("Email is required!");
            Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Please enter a valid email !");
            Email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            Password.setError("Password is required");
            Password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            Password.setError("Min password in 6 characters!");
            Password.requestFocus();
            return;
        }
        ProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = getIntent();
                String productDetails = intent.getStringExtra("product_details");

                if (productDetails != null && productDetails.equals("true"))
                    onBackPressed();
                else goToActivity(ProfileActivity.class);
            } else {
                Toast.makeText(LogIn.this, "Failed to login!Please check your credentials", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(this, " Search selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.shopping:
                Toast.makeText(this, "Shopping selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.item3:
                Toast.makeText(this, " Item 3 selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.item4:
                Toast.makeText(this, " Item 4 selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.subitem1:
                Toast.makeText(this, " Subitem 1 selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.subitem2:
                Toast.makeText(this, " Subitem 2 selected", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}