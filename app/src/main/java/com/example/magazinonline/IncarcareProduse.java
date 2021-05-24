package com.example.magazinonline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.UUID;

public class IncarcareProduse extends AppCompatActivity implements View.OnClickListener {

    private EditText NumeProdus;
    private EditText DescriereProdus;
    private EditText pretProdus;
    private EditText adresaProducator;
    private Spinner mySpinner;
    private Button btnReturn, btnSave;
    private ImageView productImage;
    private TextView upPhotos;
    private DatabaseReference databaseReference;
    private Uri imgUri;
    private String myUri = "";
    private StorageReference storageProfilePicsRef;
    SharedPreferences key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incarcare_produse);
        setVariables();
        setOnClickListeners();
        setSpinner();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setVariables() {
        key = getSharedPreferences("UID.txt", MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Product");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Product photos");
        upPhotos = findViewById(R.id.uploadPhotos);
        productImage = findViewById(R.id.productImage);
        mySpinner = findViewById(R.id.spinner1);
        NumeProdus = findViewById(R.id.NumeProdus);
        DescriereProdus = findViewById(R.id.DescriereProdus);
        pretProdus = findViewById(R.id.pretProdus);
        adresaProducator = findViewById(R.id.adresaProducator);
        btnReturn = findViewById(R.id.btnReturn);
        btnSave = findViewById(R.id.btnSavee);
    }

    private void setOnClickListeners() {
        btnReturn.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        upPhotos.setOnClickListener(v -> CropImage.activity().setAspectRatio(1, 1).start(IncarcareProduse.this));
    }

    private void setSpinner() {
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(IncarcareProduse.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imgUri = result.getUri();
            productImage.setImageURI(imgUri);
        } else Toast.makeText(this, "Error, Try again", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReturn:
                onBackPressed();
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
        String Categorie = mySpinner.getSelectedItem().toString().trim();
        String userId = "";
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (numeProdus.isEmpty()) {
            NumeProdus.setError("Product name is empty");
            NumeProdus.requestFocus();
            return;
        }

        if (descriereProdus.isEmpty()) {
            DescriereProdus.setError("Description is required!");
            DescriereProdus.requestFocus();
            return;
        }

        if (PretProdus.isEmpty()) {
            pretProdus.setError("Price is required");
            pretProdus.requestFocus();
            return;
        }

        if (AdresaProducator.isEmpty()) {
            adresaProducator.setError("Address is required!");
            adresaProducator.requestFocus();
            return;
        }

        //incarcare date in firebase
        String data = key.getString("UUID", "");
        String uidProduct = UUID.randomUUID().toString();

        if (currentUser != null)
            userId = currentUser.getUid();

        Product p = new Product(uidProduct, userId, numeProdus, descriereProdus, PretProdus, AdresaProducator, data, Categorie);
        databaseReference.child(p.getIdProdus()).child("AdresaProducator").setValue(p.getAdresaProducator());
        databaseReference.child(p.getIdProdus()).child("Categorie").setValue(p.getCategorie());
        databaseReference.child(p.getIdProdus()).child("NumeProdus").setValue(p.getNumeProdus());
        databaseReference.child(p.getIdProdus()).child("PretProdus").setValue(p.getPretProdus());
        databaseReference.child(p.getIdProdus()).child("data").setValue(p.getData());
        databaseReference.child(p.getIdProdus()).child("descriereProdus").setValue(p.getDescriereProdus());
        databaseReference.child(p.getIdProdus()).child("idProdus").setValue(p.getIdProdus());
        databaseReference.child(p.getIdProdus()).child("idProducator").setValue(p.getIdProducator());

        // incarcare imagine in firebase
        if (imgUri != null) {
            final StorageReference fileRef = storageProfilePicsRef.child(p.getIdProdus() + ".jpg");

            StorageTask uploadTask = fileRef.putFile(imgUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    myUri = downloadUrl.toString();

                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("image", myUri);

                    databaseReference.child(p.getIdProdus()).updateChildren(userMap);
                }
            });
        }
        Toast.makeText(this, "Product added successfully", Toast.LENGTH_LONG).show();
        onBackPressed();
    }
}