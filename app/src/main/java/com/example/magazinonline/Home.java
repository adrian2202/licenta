package com.example.magazinonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.drawer_menu2,menu);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawe_open, R.string.navigation_drawe_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }



    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                       new HomeFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_producatori:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProducerFragment()).commit();
                break;
            case R.id.nav_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MessageFragment()).commit();
                break;
            case R.id.nav_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChatFragment()).commit();
                break;
            case R.id.nav_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MapFragment()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share",Toast.LENGTH_LONG).show();
            case R.id.nav_send:
                Toast.makeText(this, "Send",Toast.LENGTH_LONG).show();

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}