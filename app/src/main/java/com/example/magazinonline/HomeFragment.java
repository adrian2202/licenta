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
import java.util.PrimitiveIterator;

public class  HomeFragment extends Fragment  {
   Button sendLogin;
   RecyclerView recyclerView;
//   ArrayList<MainModel>mainModels;
//   MainAdapter mainAdapter;
    List<App> appList;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        sendLogin = v.findViewById(R.id.sendLogin);
        sendLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProductInfoFirebase.class));

            }
        });



        recyclerView=v.findViewById(R.id.recyclerView);

        appList=new ArrayList<>();

        appList.add(new App(R.drawable.mancaruri_traditionale, "Mancaruri traditionale"));
        appList.add(new App(R.drawable.produse_bio, "Produse bio "));
        appList.add(new App(R.drawable.bauturi_specifice, "Bauturi specifice"));
        appList.add(new App(R.drawable.fructe_legume, "Fructe si legume"));


        LinearLayoutManager manager=new LinearLayoutManager((Context) HomeFragment.this.getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        CustomAdapter adapter = new CustomAdapter( HomeFragment.this, appList);
        recyclerView.setAdapter(adapter);






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
