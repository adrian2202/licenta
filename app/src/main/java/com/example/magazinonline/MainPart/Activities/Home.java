package com.example.magazinonline.MainPart.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.magazinonline.AuthenticationPart.LogIn;
import com.example.magazinonline.Classes.Maps;
import com.example.magazinonline.MainPart.Fragments.EditMyProductFragment;
import com.example.magazinonline.MainPart.Fragments.HomeFragment;
import com.example.magazinonline.MainPart.Fragments.MyProductsFragment;
import com.example.magazinonline.MainPart.Fragments.ProducerDetailsFragment;
import com.example.magazinonline.MainPart.Fragments.ProducersFragment;
import com.example.magazinonline.MainPart.ViewModels.HomeViewModel;
import com.example.magazinonline.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity implements NavigationView.
        OnNavigationItemSelectedListener {
    private HomeViewModel viewModel;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DatabaseReference databaseReference;
    private Intent intent;
    private FirebaseUser currentUser;

    // metoda ce se apeleaza atunci cand activitatea este creata
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setVariables();
        setToolbar();
        setToggle();

        // afisarea fragmentului de acasa (cu categoriile)
        if (savedInstanceState == null) {
            setFragment(new HomeFragment());
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    // metoda care se apeleaza atunci cand activitatea este vizibila pentru utilizator
    @Override
    protected void onStart() {
        super.onStart();
        setCurrentUser();
        setDrawerProfile();
    }

    // metoda pentru setarea variabilelor membre ale clasei
    private void setVariables() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setToggle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.shopping) {
            intent = new Intent(this, ShoppingCart.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (viewModel.getSelectedFragment() instanceof MyProductsFragment ||
                viewModel.getSelectedFragment() instanceof ProducersFragment) {
            setFragment(new HomeFragment());
            showToolbar();
        } else if (viewModel.getSelectedFragment() instanceof EditMyProductFragment)
            setFragment(new MyProductsFragment());
        else if (viewModel.getSelectedFragment() instanceof ProducerDetailsFragment) {
            setFragment(new ProducersFragment());
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                setFragment(new HomeFragment());
                break;
            case R.id.nav_profile:
                startActivity(new Intent(this, LogIn.class));
                break;
            case R.id.nav_producatori:
                setFragment(new ProducersFragment());
                break;
            case R.id.nav_my_products:
                if (currentUser != null) {
                    setFragment(new MyProductsFragment());
                } else {
                    Intent intent = new Intent(Home.this, LogIn.class);
                    intent.putExtra("currentUser", "notConnected");
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                break;
            case R.id.nav_map:
                startActivity(new Intent(this, Maps.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // metoda pentru setarea profilului utilizatorului din navigation drawer
    private void setDrawerProfile() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView profileImage = headerView.findViewById(R.id.nav_header_image);
        TextView profileName = headerView.findViewById(R.id.nav_header_name);
        TextView profileEmail = headerView.findViewById(R.id.nav_header_email);

        // in cazul in care utilizatorul este autentificat, ii setam datele din baza de date
        if (currentUser != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // iteram printre utilizatorii din baza de date si il gasim pe cel curent
                    if (snapshot.exists() && snapshot.hasChild("User"))
                        for (DataSnapshot user : snapshot.child("User").getChildren())
                            if (String.valueOf(user.getKey()).equals(currentUser.getUid())) {
                                // setarea imaginii
                                // (in cazul in care utilizatorul are o imagine setata)
                                if (user.hasChild("image")) {
                                    Glide.with(getApplicationContext())
                                            .load(String.valueOf(user.child("image").getValue()))
                                            .into(profileImage);
                                }

                                // setarea numelui (in cazul in care utilizatorul le-a setat)
                                if (user.hasChild("Name") &&
                                        user.hasChild("prenume")) {
                                    String fullName = user.child("Name").getValue() +
                                            " " + user.child("prenume").getValue();
                                    profileName.setText(fullName);
                                }

                                // setarea email-ului (in cazul in care utilizatorul l-a setat)
                                if (user.hasChild("email")) {
                                    profileEmail.setText(String.valueOf(user.child("email")
                                            .getValue()));
                                }
                            }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            headerView.setOnClickListener(view -> {
                Intent intent = new Intent(Home.this, EditProfile.class);
                startActivity(intent);
            });
        }

        // in cazul in care utilizatorul nu este autentificat, ii setam datele default
        else {
            Glide.with(getApplicationContext())
                    .load(R.mipmap.ic_launcher_round)
                    .into(profileImage);
            profileName.setText(getResources().getString(R.string.name));
            profileEmail.setText(getResources().getString(R.string.email));
        }
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
    }

    public void hideToolbar() {
        if (toolbar != null)
            toolbar.setVisibility(View.GONE);
    }

    public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
    }

    public void setFragment(Fragment selectedFragment) {
        viewModel.setSelectedFragment(selectedFragment);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit();
    }

    private void setCurrentUser() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }
}