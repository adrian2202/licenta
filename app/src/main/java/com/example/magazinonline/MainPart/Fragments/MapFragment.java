package com.example.magazinonline.MainPart.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.magazinonline.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        //initialize map fragment

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        //async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //when map is loaded
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        //when clicked on map
                        //initialize marker option
                        MarkerOptions markerOptions = new MarkerOptions();
                        //set position of marker
                        markerOptions.position(latLng);
                        //set title of marker
                        markerOptions.title(latLng.latitude + ":" + latLng.longitude);
                        //remove all marker
                        googleMap.clear();
                        //animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        //add marker on map
                        googleMap.addMarker(markerOptions);
                    }
                });

            }
        });


        //return view

        return view;
    }
}
