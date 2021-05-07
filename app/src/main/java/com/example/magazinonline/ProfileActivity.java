package com.example.magazinonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity  {
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private CircleImageView profileImageView;
    private TextView txtName;
    private ImageView imgView1;
    private FirebaseAuth mAuth;

//    private DrawerLayout drawer;



  private Toolbar toolbar1;
    //      private Button logout;
    private ImageView log_out, productImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        log_out = (ImageView) findViewById(R.id.imgView4);
        productImg=(ImageView) findViewById(R.id.imgView3) ;
        profileImageView = findViewById(R.id.dp);
        productImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, IncarcareProduse.class);
                startActivity(intent);
            }
        });

//         toolbar1 =findViewById(R.id.toolbar1);
//        setSupportActionBar(toolbar1);
//
//        drawer=findViewById(R.id.drawer_layout1);
//        NavigationView navigationView=findViewById(R.id.nav_view1);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar1,
//                R.string.navigation_drawe_open, R.string.navigation_drawe_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        if(savedInstanceState==null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
//                    new HomeFragment()).commit();
//            navigationView.setCheckedItem(R.id.nav_profile);
//        }


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
        getUserinfo();
//        profileImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

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

        final TextView greetingTextView=(TextView) findViewById(R.id.greeting);
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

                    greetingTextView.setText(  fullName);
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

    private void getUserinfo() {

        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0)
                {
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
    public boolean onCreateOptionsMenu( Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.drawer_menu2,menu);
        inflater.inflate(R.menu.drawer_menu,menu);
        return true;


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.search:
                Toast.makeText(this," Search selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.shopping:
                Toast.makeText(this,"Shopping selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.item3:
                Toast.makeText(this," Item 3 selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.item4:
                Toast.makeText(this," Item 4 selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.subitem1:
                Toast.makeText(this," Subitem 1 selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.subitem2:
                Toast.makeText(this," Subitem 2 selected", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//        Toolbar toolbar =findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        drawer=findViewById(R.id.drawer_layout);
//        NavigationView navigationView=findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,
//                R.string.navigation_drawe_open, R.string.navigation_drawe_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        if(savedInstanceState==null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new HomeFragment()).commit();
//            navigationView.setCheckedItem(R.id.nav_home);
//        }
//
//
//
//    }

//    @Override
//    public void onBackPressed() {
//        if(drawer.isDrawerOpen(GravityCompat.START))
//        {
//            drawer.closeDrawer(GravityCompat.START);
//        }else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId())
//        {
//            case R.id.nav_home:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new HomeFragment()).commit();
//                break;
//            case R.id.nav_profile:
//                startActivity(new Intent(this ,LogIn.class ) );;
//                break;
//            case R.id.nav_producatori:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new ProducerFragment()).commit();
//                break;
//            case R.id.nav_message:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new MessageFragment()).commit();
//                break;
//            case R.id.nav_chat:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new ChatFragment()).commit();
//                break;
//            case R.id.nav_map:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new MapFragment()).commit();
//                break;
//            case R.id.nav_share:
//                Toast.makeText(this, "Share",Toast.LENGTH_LONG).show();
//            case R.id.nav_send:
//                Toast.makeText(this, "Send",Toast.LENGTH_LONG).show();
//
//        }
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

}