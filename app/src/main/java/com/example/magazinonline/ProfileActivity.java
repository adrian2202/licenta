package com.example.magazinonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private  String userID;

      private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout=(Button) findViewById(R.id.signOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
               startActivity(new Intent(ProfileActivity.this, LogIn.class));
            }
        });

        user=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("User");
        userID=user.getUid();

        final TextView greetingTextView=(TextView) findViewById(R.id.greeting);
        final TextView fullNameTextView=(TextView) findViewById(R.id.fullName);
        final TextView emailTextView=(TextView) findViewById(R.id.emailAddress);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 User userProfile= snapshot.getValue(User.class);
                 if(userProfile!= null)
                 {
                 //    Log.e(" onDataChange", "intra aici ");
                     String fullName = userProfile.fullName;
                     String email = userProfile.email;

                     greetingTextView.setText("Welcome," + fullName+ "!");
                     fullNameTextView.setText(fullName);
                     emailTextView.setText(email);
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