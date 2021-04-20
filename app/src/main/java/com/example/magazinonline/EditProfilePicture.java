package com.example.magazinonline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.theartofdev.edmodo.cropper.CropImageActivity;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfilePicture extends AppCompatActivity {

    private CircleImageView profileImageView;
    private Button closeButton,saveButton;
    private TextView profileChangeBtn;
    private EditText nameEditPro,prenumeEditPro,emailEditPro,nrTelEditPro;


    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private Uri imageUri;
    private String myUri="";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicsRef;
    private TextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_picture);

        //init
        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("User");
        storageProfilePicsRef= FirebaseStorage.getInstance().getReference().child("Profile Pic");
//        txtName=findViewById(R.id.txtName);

        profileImageView=findViewById(R.id.profile_image);

        closeButton=findViewById(R.id.btnClose);
        saveButton=findViewById(R.id.btnSave);

        nameEditPro=findViewById(R.id.nameEditPro);
        prenumeEditPro=findViewById(R.id.prenumeEditPro);
//        emailEditPro=findViewById(R.id.emailEditPro);
        nrTelEditPro=findViewById(R.id.nrTelEditPro);


        profileChangeBtn=findViewById(R.id.change_profile_btn);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfilePicture.this, ProfileActivity.class));

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSave();





            }
        });

        profileChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1, 1).start(EditProfilePicture.this);
            }
        });
        getUserinfo();


    }

    private void validateAndSave() {
        if (TextUtils.isEmpty(nameEditPro.getText().toString()))
        {
            Toast.makeText(this,"Please Enter your name",Toast.LENGTH_SHORT).show();
        }
        else
        if (TextUtils.isEmpty(prenumeEditPro.getText().toString()))
        {
            Toast.makeText(this,"Please Enter your prenume",Toast.LENGTH_SHORT).show();
        }
//        else
//        if (TextUtils.isEmpty(emailEditPro.getText().toString()))
//        {
//            Toast.makeText(this,"Please Enter your email",Toast.LENGTH_SHORT).show();
//        }
        else
        if (TextUtils.isEmpty(nrTelEditPro.getText().toString()))
        {
            Toast.makeText(this,"Please Enter your nr Tel",Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String,Object> userMap=new HashMap<>();
            userMap.put("Name",nameEditPro.getText().toString());
            userMap.put("prenume",prenumeEditPro.getText().toString());
//           userMap.put("email",emailEditPro.getText().toString());
            userMap.put("nrtel",nrTelEditPro.getText().toString());

            databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);
            uploadProfileimage();

        }

    }

    private void getUserinfo() {

        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0)
                {   User userProfile = dataSnapshot.getValue(User.class);
                    String name=userProfile.Name;
//                    String email = userProfile.email;
                    String Prenume = userProfile.prenume;
                    String telefon = userProfile.nrtel;


                    nameEditPro.setText(name);
                    prenumeEditPro.setText(Prenume);
//                    emailEditPro.setText(email);
                    nrTelEditPro.setText(telefon);


                    if(dataSnapshot.hasChild("image"))
                    {
                        String image= dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(profileImageView);
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

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data != null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri =result.getUri();

            profileImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this,"Error, Try again", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadProfileimage() {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Set your profile");
        progressDialog.setMessage("Please wait, while we are setting your data ");
        progressDialog.show();

        if(imageUri !=null)
        {
            final StorageReference fileRef=storageProfilePicsRef.child(mAuth.getCurrentUser().getUid()+".jpg");

            uploadTask= fileRef.putFile(imageUri);

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

                        databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);
                        progressDialog.dismiss();
                    }
                }
            });

        }
//        else {
//            progressDialog.dismiss();
//            Toast.makeText(this,"Image not selected",Toast.LENGTH_SHORT).show();
//
//        }
    }
}