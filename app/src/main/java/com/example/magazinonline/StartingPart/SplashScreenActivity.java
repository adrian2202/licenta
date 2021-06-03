package com.example.magazinonline.StartingPart;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.magazinonline.MainPart.Activities.Home;
import com.example.magazinonline.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

public class SplashScreenActivity extends AppCompatActivity {
    private FusedLocationProviderClient locationProviderClient;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setVariables();
        saveUserLocation();
        initializeSplashScreen();
    }

    private void setVariables() {
        preferences = getSharedPreferences("traditional_food_app", MODE_PRIVATE);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void saveUserLocation() {
        final int REQUEST_LOCATION_CODE = 99;
        if (ActivityCompat.checkSelfPermission(SplashScreenActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationProviderClient.getLastLocation().addOnSuccessListener(this, this::saveLocationToSharedPreferences);
        } else
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
    }

    private void initializeSplashScreen() {
        LogoLauncher launcher = new LogoLauncher();
        launcher.start();
    }

    private void saveLocationToSharedPreferences(Location location) {
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(location);

        editor.putString("currentLocation", json);
        editor.apply();
    }

    public class LogoLauncher extends Thread {
        public void run() {
            try {
                sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(SplashScreenActivity.this, Home.class);

            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }
}