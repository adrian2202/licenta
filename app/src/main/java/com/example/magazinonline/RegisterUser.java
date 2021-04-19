package com.example.magazinonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{
    private EditText NumeEditText, emailEditText,passwordEditText,prenumeEditText,nrTelEditText;
    private TextView Create_new_account, homePageTextView;
    private ProgressBar progressBar;
    private Button button_create_acount;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        homePageTextView=(TextView) findViewById(R.id.homePageTextView);
        homePageTextView.setOnClickListener(this);
        button_create_acount=(Button) findViewById(R.id.button_create_acount);
        button_create_acount.setOnClickListener(this);

        NumeEditText=(EditText) findViewById(R.id.NumeEditText);
        prenumeEditText=(EditText) findViewById(R.id.prenumeEditText);
        nrTelEditText=(EditText) findViewById(R.id.nrTelEditText);
        emailEditText=(EditText) findViewById(R.id.emailEditText);
        passwordEditText=(EditText) findViewById(R.id.passwordEditText);
        Create_new_account=(TextView) findViewById(R.id.Create_new_account);

        progressBar=(ProgressBar) findViewById(R.id.progressBar);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.homePageTextView:
                startActivity(new Intent(this, Home.class));
                break;
            case R.id.button_create_acount:
                button_create_acount();
                break;
        }

    }

    private void button_create_acount() {
         String email=emailEditText.getText().toString().trim();
         String prenume=prenumeEditText.getText().toString().trim();
         String password=passwordEditText.getText().toString().trim();
         String nrtel=nrTelEditText.getText().toString().trim();
         String name=NumeEditText.getText().toString().trim();

         if(name.isEmpty())
         {
             NumeEditText.setError("Full name is required!");
             NumeEditText.requestFocus();
             return;
         }
        if(prenume.isEmpty())
        {
            prenumeEditText.setError(" Prenume is required!");
           prenumeEditText.requestFocus();
            return;
        }
        if(nrtel.isEmpty())
        {
            nrTelEditText.setError("Nr phone is required!");
            nrTelEditText.requestFocus();
            return;
        }
         if(email.isEmpty())
         {
             emailEditText.setText("Email is required!");
             emailEditText.requestFocus();
             return;
         }
         if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
         {
             emailEditText.setError("Please provide valid email!");
             emailEditText.requestFocus();
             return;
         }
         if(password.isEmpty())
         {
             passwordEditText.setError("Password is required!");
             passwordEditText.requestFocus();
             return;
         }
         if(password.length()<6)
         {
             passwordEditText.setError("Min password length should be 6 characters !");
             passwordEditText.requestFocus();
             return;
         }

         progressBar.setVisibility(View.VISIBLE);
         mAuth.createUserWithEmailAndPassword(email, password)
                 .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful())
                         {
                             User user=new User( name,email,prenume,nrtel);

                             FirebaseDatabase.getInstance().getReference("User")
                                     .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                     .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if(task.isSuccessful())
                                     {
                                         Toast.makeText(RegisterUser.this,"User has been registred successfully",Toast.LENGTH_LONG).show();
                                         progressBar.setVisibility(View.VISIBLE);
                                     }
                                     else
                                         Toast.makeText(RegisterUser.this , "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                     progressBar.setVisibility(View.GONE);

                                 }
                             });


                         } else {
                             Toast.makeText(RegisterUser.this , "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                             progressBar.setVisibility(View.GONE);
                         }

                     }
                 });
    }
}