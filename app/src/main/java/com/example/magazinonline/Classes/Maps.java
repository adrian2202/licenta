package com.example.magazinonline.Classes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.magazinonline.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Maps extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setVariables();

        // verificam daca avem permisiunea de acces la locatia dispozitivului
        if (ActivityCompat.checkSelfPermission(Maps.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // apelam metoda de gasire a locatiei curente in cazul in care
            // avem permisiune de acces
            getCurrentLocation();
        } else {
            // cerem permisiunea in cazul in care nu avem acces deja
            ActivityCompat.requestPermissions(Maps.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    44);
        }
    }

    // metoda pentru initializarea variabilelor membre
    private void setVariables() {
        // initializarea referintei la baza de date Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // setarea fragmentului ce afiseaza harta in activitatea curenta
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        // initializarea clientului ce furnizeaza locatia
        client = LocationServices.getFusedLocationProviderClient(this);
    }

    // metoda pentru gasirea locatiei curente
    private void getCurrentLocation() {
        // declararea sarcinii ce se ocupa de aflarea ultimei locatii
        Task<Location> task = client.getLastLocation();

        // in cazul in care sarcina a fost efectuata cu succes
        task.addOnSuccessListener(location -> {
            if (location != null) {
                // sincronizam harta
                supportMapFragment.getMapAsync((googleMap -> {
                    // initializam obiectul de tip LatLng ce va tine coordonatele locatiei curente
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    // setarea unui marker pentru afisarea locatiei curente
                    // markerul va fi setat pe coordonatele locatiei curente a utilizatorului,
                    // va avea titlul 'Locatie curenta' si culoarea verde
                    googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(getResources().getString(R.string.i_am_here))
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    // apelam metoda de afisare a produselor din baza de date pe harta
                    addNearbyProductsLocations(googleMap);

                    // setam zoom de 15x pe harta
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }));
            }

        });
    }

    // metoda de afisare a produselor din baza de date pe harta
    private void addNearbyProductsLocations(GoogleMap map) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // verificam daca exista produse in baza de date
                if (snapshot.exists() &&
                        snapshot.hasChild("Product") &&
                        snapshot.child("Product").hasChildren())
                    // iteram printre produsele din baza de date
                    for (DataSnapshot product : snapshot.child("Product").getChildren()) {
                        // cream un obiect produs cu datele produsului din baza de date
                        Product productFromDatabase = new Product();
                        String[] productCategories = getResources().getStringArray(R.array.names);
                        float hue;

                        if (product.hasChild("idProdus")) {
                            productFromDatabase.setIdProdus(String
                                    .valueOf(product.child("idProdus").getValue()));
                        }

                        if (product.hasChild("idProducator")) {
                            productFromDatabase.setIdProducator(String
                                    .valueOf(product.child("idProducator").getValue()));
                        }

                        if (product.hasChild("NumeProdus")) {
                            productFromDatabase.setNumeProdus(String
                                    .valueOf(product.child("NumeProdus").getValue()));
                        }

                        if (product.hasChild("descriereProdus")) {
                            productFromDatabase.setDescriereProdus(String
                                    .valueOf(product.child("descriereProdus").getValue()));
                        }

                        if (product.hasChild("PretProdus")) {
                            productFromDatabase.setPretProdus(String
                                    .valueOf(product.child("PretProdus").getValue()));
                        }

                        // adaugam locatia producatorului in produs (in cazul in care exista)
                        if (product.hasChild("AdresaProducator")) {
                            ProducerAddress producerAddress = new ProducerAddress();

                            if (product.child("AdresaProducator").hasChild("area")) {
                                producerAddress.setArea(String
                                        .valueOf(product.child("AdresaProducator")
                                                .child("area").getValue()));
                            }

                            if (product.child("AdresaProducator").hasChild("country")) {
                                producerAddress.setCountry(String
                                        .valueOf(product.child("AdresaProducator")
                                                .child("country").getValue()));
                            }

                            if (product.child("AdresaProducator").hasChild("locality")) {
                                producerAddress.setLocality(String
                                        .valueOf(product.child("AdresaProducator")
                                                .child("locality").getValue()));
                            }

                            if (product.child("AdresaProducator").hasChild("street")) {
                                producerAddress.setStreet(String
                                        .valueOf(product.child("AdresaProducator")
                                                .child("street").getValue()));
                            }

                            productFromDatabase.setAdresaProducator(producerAddress);
                        }

                        if (product.hasChild("dataAdaugareProdus")) {
                            productFromDatabase.setDataAdaugareProdus(product
                                    .child("dataAdaugareProdus").getValue(MyTime.class));
                        }

                        // setam coordonatele producatorului (in cazul in care exista)
                        if (product.hasChild("LocatieProducator") &&
                                product.child("LocatieProducator").hasChild("Latitudine") &&
                                product.child("LocatieProducator").hasChild("Longitudine")) {
                            productFromDatabase.setLatitudineProducator(Double
                                    .parseDouble(String.valueOf(product
                                            .child("LocatieProducator")
                                            .child("Latitudine")
                                            .getValue())));
                            productFromDatabase.setLongitudineProducator(Double
                                    .parseDouble(String.valueOf(product
                                            .child("LocatieProducator")
                                            .child("Longitudine")
                                            .getValue())));
                        }

                        if (product.hasChild("Categorie")) {
                            productFromDatabase.setCategorie(String
                                    .valueOf(product.child("Categorie").getValue()));
                        }

                        if (product.hasChild("image")) {
                            productFromDatabase.setImage(String
                                    .valueOf(product.child("image").getValue()));
                        }

                        if (productFromDatabase.getLatitudineProducator() != 0 &&
                                productFromDatabase.getLongitudineProducator() != 0) {
                            // adaugam coordonatele producatorului intr-un obiect de tip LatLng
                            LatLng productCoordinates =
                                    new LatLng(productFromDatabase.getLatitudineProducator(),
                                            productFromDatabase.getLongitudineProducator());

                            // setam culoare pentru marker in functie de categorie
                            if (productFromDatabase.getCategorie()
                                    .equals(productCategories[0])) {
                                // setam culoarea azure pentru mancarea traditionala
                                hue = BitmapDescriptorFactory.HUE_AZURE;
                            } else if (productFromDatabase.getCategorie()
                                    .equals(productCategories[1])) {
                                // setam culoarea magenta pentru preparatele bio
                                hue = BitmapDescriptorFactory.HUE_MAGENTA;
                            } else if (productFromDatabase.getCategorie()
                                    .equals(productCategories[2])) {
                                // setam culoarea cyan pentru bauturile specifice
                                hue = BitmapDescriptorFactory.HUE_CYAN;
                            } else if (productFromDatabase.getCategorie()
                                    .equals(productCategories[3])) {
                                // setam culoarea portocalie pentru fructe si legume
                                hue = BitmapDescriptorFactory.HUE_ORANGE;
                            } else {
                                // setam culoarea galbena pentru cazul default
                                hue = BitmapDescriptorFactory.HUE_YELLOW;
                            }

                            // setarea markerului pentru produs pe harta
                            // markerul are coordonatele produsului, titlul numele produsului si
                            // culoarea setata mai sus
                            map.addMarker(new MarkerOptions()
                                    .position(productCoordinates)
                                    .title(productFromDatabase.getNumeProdus())
                                    .icon(BitmapDescriptorFactory
                                            .defaultMarker(hue)));
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 44 &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // apelam metoda de obtinere a locatiei in cazul in care am primit dreptul de a o accesa
            getCurrentLocation();
        }
    }
}