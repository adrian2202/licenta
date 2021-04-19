package com.example.magazinonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private DatabaseReference reference;
    private String userID;
    private CircleImageView profileImageView;
    private TextView txtName;
    private ImageView imgView1;
    private FirebaseAuth mAuth;


    //      private Button logout;
    private ImageView log_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        log_out = (ImageView) findViewById(R.id.imgView4);
        profileImageView = findViewById(R.id.dp);
        log_out.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, LogIn.class);
                startActivity(intent);
            }
        });
        imgView1 = (ImageView) findViewById(R.id.imgView1);
        imgView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfilePicture.class);
                startActivity(intent);
            }
        });
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

//        txtName=(TextView) findViewById(R.id.txtName);
//        txtName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(ProfileActivity.this, EditProfilePicture.class);
////                startActivity(intent);
//
//            }
//        });
        ;

//        logout=(Button) findViewById(R.id.signOut);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//               startActivity(new Intent(ProfileActivity.this, LogIn.class));
//            }
//        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User");
        userID = user.getUid();

        //final TextView greetingTextView=(TextView) findViewById(R.id.greeting);
        final TextView fullNameTextView = (TextView) findViewById(R.id.Name);
        final TextView prenumeTextView = (TextView) findViewById(R.id.Prenume);
        final TextView nrTELTextView = (TextView) findViewById(R.id.NrTel);
        final TextView emailTextView = (TextView) findViewById(R.id.emailAddress);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    //    Log.e(" onDataChange", "intra aici ");
                    String fullName = userProfile.Name;
                    String email = userProfile.email;
                    String Prenume = userProfile.prenume;
                    String telefon = userProfile.nrtel;

                    //greetingTextView.setText(  fullName);
                    fullNameTextView.setText(fullName);
                    emailTextView.setText(email);
                    prenumeTextView.setText(Prenume);
                    nrTELTextView.setText(telefon);
                }
                // Log.e(" onDataChange", "Nu intra aici ");
//                for(DataSnapshot ds :snapshot.getChildren())
//                {
//                    User u= new User(ds.child("email").getValue(String.class),ds.child("fullName").getValue(String.class));
//                    Log.e("onDataChange",u.getEmail());
//
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something wrong happened", Toast.LENGTH_LONG).show();

            }
        });

    }


}