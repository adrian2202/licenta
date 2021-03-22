package com.example.magazinonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogIn extends AppCompatActivity {
    private EditText Email;
    private EditText Password;
    private TextView CreateAccount;
    private TextView ForgotPassword;
    private Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Email=(EditText)findViewById(R.id.editEmailAddress);
        Password=(EditText)findViewById(R.id.editTextPassword);
        CreateAccount=(TextView)findViewById(R.id.textCreeate);
        ForgotPassword=(TextView)findViewById(R.id.textForgot);
        Login=(Button)findViewById(R.id.button);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Email.getText().toString(),Password.getText()
                .toString());
            }

        });
        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogIn();
            }
        });
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgotPassword();
            }
        });

    }
    private  void validate (String userEmail, String userPassword)
    {
        if((userEmail.equals("admin")) && (userPassword.equals("1234")))
        {
            Intent intent = new Intent(LogIn.this , Home.class);
            startActivity(intent);
        }
    }



    public void openLogIn(){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void openForgotPassword()
    {
        Intent intent= new Intent (this,ForgotPassword.class );
        startActivity(intent);
    }
}
