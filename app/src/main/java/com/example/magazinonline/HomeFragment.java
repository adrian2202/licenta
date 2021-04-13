package com.example.magazinonline;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

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
        Integer[] langLogo = {R.drawable.poza2, R.drawable.poza3, R.drawable.poza4};
        String[] langName = {"Mancare", "Bauturi", "Locuri"};

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


        ImageSlider imageSlider= v.findViewById(R.id.slider);
        List<SlideModel> slideModels=new ArrayList<>();
        slideModels.add(new SlideModel("https://revistaprogresiv.ro/sites/default/files/article/images/gusturi_romanesti_4.jpg", "1 Image" ));
        slideModels.add(new SlideModel("https://www.bioshopromania.com/images/thumbnails/770/709/detailed/4/cos_traditional_romanesc_mare_2_BioShopRomania.JPG?t=1602080293", "2 Image" ));
        slideModels.add(new SlideModel("https://www.ziromania.ro/wp-content/uploads/2018/12/b%C4%83uturi-tradi%C8%9Bionale-rom%C3%A2ne%C8%99ti-vazute-la-crama-1777-3.jpg", "3 Image" ));
        slideModels.add(new SlideModel("https://www.banateanul.ro/wp-content/uploads/2019/07/bauturi-traditionale-romanesti.jpg", "4 Image" ));
        slideModels.add(new SlideModel("https://medisf.traasgpu.com/ifis/62277a094eff337f-1024x576.jpg", "5 Image" ));

        imageSlider.setImageList(slideModels,true);
        return v;
    }



}
