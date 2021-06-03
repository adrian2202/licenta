package com.example.magazinonline.MainPart.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.magazinonline.R;
import com.example.magazinonline.Classes.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private StorageReference storageProfilePicsRef;
    private FirebaseAuth mAuth;
    private CircleImageView profileImageView;
    private Button closeButton, saveButton;
    private TextView profileChangeBtn;
    private EditText nameEditPro;
    private EditText prenumeEditPro;
    private EditText emailEditPro;
    private EditText nrTelEditPro;
    private TextView txtName;
    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setVariables();
        setOnClickListeners();
        getUserInfo();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setVariables() {
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");
        profileImageView = findViewById(R.id.profile_image);
        closeButton = findViewById(R.id.btnClose);
        saveButton = findViewById(R.id.btnSave);
        nameEditPro = findViewById(R.id.nameEditPro);
        prenumeEditPro = findViewById(R.id.prenumeEditPro);
        nrTelEditPro = findViewById(R.id.nrTelEditPro);
        profileChangeBtn = findViewById(R.id.change_profile_btn);
    }

    private void setOnClickListeners() {
        closeButton.setOnClickListener(v -> onBackPressed());

        profileChangeBtn.setOnClickListener(v -> CropImage.activity()
                .setAspectRatio(1, 1).start(EditProfile.this));

        saveButton.setOnClickListener(v -> validateAndSave());
    }

    private void validateAndSave() {
        if (TextUtils.isEmpty(nameEditPro.getText().toString()))
            Toast.makeText(this,
                    getResources().getString(R.string.please_enter_last_name),
                    Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(prenumeEditPro.getText().toString()))
            Toast.makeText(this,
                    getResources().getString(R.string.please_enter_first_name),
                    Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(nrTelEditPro.getText().toString()))
            Toast.makeText(this,
                    getResources().getString(R.string.please_enter_phone),
                    Toast.LENGTH_SHORT).show();
        else {
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("Name", nameEditPro.getText().toString());
            userMap.put("prenume", prenumeEditPro.getText().toString());
            userMap.put("nrtel", nrTelEditPro.getText().toString());

            if (mAuth.getCurrentUser() != null)
                databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);

            uploadProfileImage();
        }
    }

    private void getUserInfo() {
        if (mAuth.getCurrentUser() != null)
            databaseReference.child(mAuth.getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                                User userProfile = dataSnapshot.getValue(User.class);

                                if (userProfile != null) {
                                    String name = userProfile.getName();
                                    String Prenume = userProfile.getPrenume();
                                    String telefon = userProfile.getNrtel();

                                    nameEditPro.setText(name);
                                    prenumeEditPro.setText(Prenume);
                                    nrTelEditPro.setText(telefon);

                                    if (dataSnapshot.hasChild("image")) {
                                        String image = String.valueOf(dataSnapshot
                                                .child("image").getValue());
                                        Picasso.get().load(image).into(profileImageView);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&
                resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this,
                    getResources().getString(R.string.error_try_again),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void uploadProfileImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.set_your_profile));
        progressDialog.setMessage(getResources().getString(R.string.please_wait_setting_date));
        progressDialog.show();

        if (imageUri != null && mAuth.getCurrentUser() != null) {
            final StorageReference fileRef = storageProfilePicsRef
                    .child(mAuth.getCurrentUser().getUid() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask((Continuation) task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileRef.getDownloadUrl();
            }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    myUri = downloadUrl.toString();

                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("image", myUri);

                    databaseReference.child(mAuth.getCurrentUser().getUid())
                            .updateChildren(userMap);
                    progressDialog.dismiss();
                }
            });
        }
    }
}