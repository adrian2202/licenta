package com.example.magazinonline;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeFragment extends Fragment  {
   Button sendLogin;
   RecyclerView recyclerView;
   ArrayList<MainModel>mainModels;
   MainAdapter mainAdapter;


      public HomeFragment(){

      }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        sendLogin = v.findViewById(R.id.sendLogin);
        sendLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LogIn.class));

            }
        });

        recyclerView = v.findViewById(R.id.recyclerView);
        Integer[] langLogo = {R.drawable.poza2, R.drawable.poza3, R.drawable.poza4, R.drawable.poza5,  R.drawable.poza6};
        String[] langName = {"Mamaliga", "Tuica", "Sibiu","adrian","cont"};

        mainModels = new ArrayList<>();
        for (int i = 0; i < langLogo.length; i++) {
            MainModel model = new MainModel(langLogo[i], langName[i]);
            mainModels.add(model);
        }
        //Design horizontal Layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                (Context) HomeFragment.this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Initialize Main Adapter
        mainAdapter = new MainAdapter(HomeFragment.this, mainModels);
        // Set mainAdapter to RecyclerView
        recyclerView.setAdapter(mainAdapter);


        return v;
    }



}
