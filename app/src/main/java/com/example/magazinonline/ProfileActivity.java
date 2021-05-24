package com.example.magazinonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private CircleImageView profileImageView;
    private ImageView editProfile;
    private FirebaseAuth mAuth;
    private ImageView goBack;
    private ImageView log_out;
    private ImageView productImg;
    private TextView greetingTextView;
    private TextView fullNameTextView;
    private TextView prenumeTextView;
    private TextView nrTELTextView;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setVariables();
        setOnClickListeners();
        getUserinfo();
        setUserInfo();
    }

    private void setVariables() {
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User");
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        log_out = findViewById(R.id.imgView4);
        productImg = findViewById(R.id.imgView3);
        profileImageView = findViewById(R.id.dp);
        editProfile = findViewById(R.id.imgView1);
        greetingTextView = findViewById(R.id.greeting);
        fullNameTextView = findViewById(R.id.Name);
        prenumeTextView = findViewById(R.id.Prenume);
        nrTELTextView = findViewById(R.id.NrTel);
        emailTextView = findViewById(R.id.emailAddress);
        goBack = findViewById(R.id.activity_profile_go_back);
    }

    private void setOnClickListeners() {
        goBack.setOnClickListener(view -> onBackPressed());

        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfile.class);
            startActivity(intent);
        });

        log_out.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LogIn.class);
            firebaseAuth.signOut();
            startActivity(intent);
            finish();
        });

        productImg.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, IncarcareProduse.class);
            startActivity(intent);
        });
    }

    private void getUserinfo() {
        if (mAuth.getCurrentUser() != null)
            databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                        if (dataSnapshot.hasChild("image")) {
                            String image = String.valueOf(dataSnapshot.child("image").getValue());
                            Picasso.get().load(image).into(profileImageView);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    private void setUserInfo() {
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    String fullName = userProfile.Name;
                    String email = userProfile.email;
                    String Prenume = userProfile.prenume;
                    String telefon = userProfile.nrtel;

                    greetingTextView.setText(fullName);
                    fullNameTextView.setText(fullName);
                    emailTextView.setText(email);
                    prenumeTextView.setText(Prenume);
                    nrTELTextView.setText(telefon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something wrong happened", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_menu2, menu);
        inflater.inflate(R.menu.drawer_menu, menu);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}