package com.example.magazinonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class IncarcareProduse extends AppCompatActivity implements View.OnClickListener {

    private EditText NumeProdus, DescriereProdus, pretProdus, adresaProducator;
    private Spinner spinner1;
    private Button btnReturn, btnSavee;
    private FirebaseAuth mAuth;
    SharedPreferences key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incarcare_produse);

        mAuth = FirebaseAuth.getInstance();

        key=getSharedPreferences("UID.txt", MODE_PRIVATE);

        NumeProdus = (EditText) findViewById(R.id.NumeProdus);
        DescriereProdus = (EditText) findViewById(R.id.DescriereProdus);
        pretProdus = (EditText) findViewById(R.id.pretProdus);
        adresaProducator = (EditText) findViewById(R.id.adresaProducator);
        btnReturn = (Button) findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(this);
        btnSavee = (Button) findViewById(R.id.btnSavee);
        btnSavee.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReturn:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.btnSavee:
                saveProduct();
                break;

        }

    }

    private void saveProduct() {
        String numeProdus = NumeProdus.getText().toString().trim();
        String descriereProdus = DescriereProdus.getText().toString().trim();
        String PretProdus = pretProdus.getText().toString().trim();
        String AdresaProducator = adresaProducator.getText().toString().trim();

        if (numeProdus.isEmpty()) {
            NumeProdus.setError("Product name is empty");
            NumeProdus.requestFocus();
            return;
        }

        if (descriereProdus.isEmpty()) {
            DescriereProdus.setError("Descriere is requried!");
            DescriereProdus.requestFocus();
            return;
        }
        if (PretProdus.isEmpty()) {
            pretProdus.setError("Price is requried");
            pretProdus.requestFocus();
            return;
        }
        if (AdresaProducator.isEmpty()) {
            adresaProducator.setError("Adress is requried!");
            adresaProducator.requestFocus();
            return;
        }
        String data = key.getString("UUID","");
    Product p=new Product(numeProdus,descriereProdus,PretProdus,AdresaProducator,data);
        FirebaseDatabase.getInstance().getReference("Product").child(UUID.randomUUID().toString()).setValue(p);



    }
}