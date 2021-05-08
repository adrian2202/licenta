package com.example.magazinonline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.UUID;

public class IncarcareProduse extends AppCompatActivity implements View.OnClickListener {

    private EditText NumeProdus, DescriereProdus, pretProdus, adresaProducator;
    private Spinner SPinner;
    private Button btnReturn, btnSavee;
    private FirebaseAuth mAuth;


    private ImageView  productImage;

    private TextView  upPhotos;
    private DatabaseReference databaseReference;
    private Uri imgUri;
    private String myUri="";
    private StorageTask  uploadTask;
    private StorageReference storageProfilePicsRef;

    SharedPreferences key;
    private Object Spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incarcare_produse);

        mAuth = FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Product");
        storageProfilePicsRef= FirebaseStorage.getInstance().getReference().child("Product photos");

        upPhotos=findViewById(R.id.uploadPhotos);
        productImage=findViewById(R.id.productImage);

        upPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).start(IncarcareProduse.this);

            }
        });

        //Spinner declaratie
         Spinner mySpinner= (Spinner)findViewById(R.id.spinner1);
         ArrayAdapter<String> myAdapter= new ArrayAdapter<String>(IncarcareProduse.this,
                 android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
         myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         mySpinner.setAdapter(myAdapter);

//key, sa putem lega doua noduri
        key=getSharedPreferences("UID.txt", MODE_PRIVATE);

        SPinner=(Spinner) findViewById(R.id.spinner1);
        NumeProdus = (EditText) findViewById(R.id.NumeProdus);
        DescriereProdus = (EditText) findViewById(R.id.DescriereProdus);
        pretProdus = (EditText) findViewById(R.id.pretProdus);
        adresaProducator = (EditText) findViewById(R.id.adresaProducator);
        btnReturn = (Button) findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(this);
        btnSavee = (Button) findViewById(R.id.btnSavee);
        btnSavee.setOnClickListener(this);

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data != null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imgUri =result.getUri();

           productImage.setImageURI(imgUri);
        }
        else
        {
            Toast.makeText(this,"Error, Try again", Toast.LENGTH_LONG).show();
        }
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

        String Categorie=SPinner.getSelectedItem().toString().trim();

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

        //incarcare in firebase date
        String data = key.getString("UUID","");
    Product p=new Product(numeProdus,descriereProdus,PretProdus,AdresaProducator,data,Categorie);
    String uidProduct= UUID.randomUUID().toString();
        FirebaseDatabase.getInstance().getReference("Product").child(uidProduct).setValue(p);


        // incarcaRe imagina in firebase

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Set your profile");
        progressDialog.setMessage("Please wait, while we are setting your data ");
        progressDialog.show();

        if(imgUri !=null)
        {
            final StorageReference fileRef=storageProfilePicsRef.child(uidProduct +".jpg");

            uploadTask= fileRef.putFile(imgUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task <Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl= task.getResult();
                        myUri=downloadUrl.toString();

                        HashMap<String, Object> userMap=new HashMap<>();
                        userMap.put("image", myUri);
                        Log.e("INCARCARE",uidProduct);

                        databaseReference.child(uidProduct).updateChildren(userMap);
                        progressDialog.dismiss();
                    }
                }
            });



    }
}}