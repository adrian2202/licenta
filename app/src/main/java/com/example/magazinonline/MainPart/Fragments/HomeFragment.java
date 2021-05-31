package com.example.magazinonline.MainPart.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.example.magazinonline.Classes.App;
import com.example.magazinonline.MainPart.Adapters.CustomAdapter;
import com.example.magazinonline.MainPart.ViewModels.HomeViewModel;
import com.example.magazinonline.R;

import java.util.List;

public class HomeFragment extends Fragment {
    private HomeViewModel viewModel;
    private RecyclerView recyclerView;
    private List<App> appList;
    private CustomAdapter adapter;
    private ImageSlider imageSlider;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        setVariables(v);
        setRecyclerView();
        setImageSlider();

        return v;
    }

    private void setVariables(View view) {
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        appList = viewModel.getCategories();
        recyclerView = view.findViewById(R.id.recyclerView);
        imageSlider = view.findViewById(R.id.slider);
        adapter = new CustomAdapter(HomeFragment.this, appList);
    }

    private void setRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager((Context) HomeFragment.this.getActivity());

        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void setImageSlider() {
        imageSlider.setImageList(viewModel.getSlideModels(), true);
    }
}
