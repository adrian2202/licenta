package com.example.magazinonline.MainPart.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magazinonline.Classes.Product;
import com.example.magazinonline.MainPart.Activities.Home;
import com.example.magazinonline.MainPart.Adapters.MyProductsRecyclerViewAdapter;
import com.example.magazinonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyProductsFragment extends Fragment {
    private ImageView goBack;
    private RecyclerView myProductsRecyclerView;
    private MyProductsRecyclerViewAdapter adapter;
    private List<Product> productList;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_products, container, false);

        setVariables(view);
        ((Home) requireActivity()).hideToolbar();
        setRecyclerView();
        setOnClickListeners();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        populateRecyclerView();
    }

    private void setVariables(View v) {
        goBack = v.findViewById(R.id.my_products_go_back);
        myProductsRecyclerView = v.findViewById(R.id.my_products_recyclerview);
        productList = new ArrayList<>();
        adapter = new MyProductsRecyclerViewAdapter(requireContext(),
                productList,
                myProductsRecyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void setOnClickListeners() {
        goBack.setOnClickListener(view -> requireActivity().onBackPressed());
    }

    private void setRecyclerView() {
        myProductsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        myProductsRecyclerView.setAdapter(adapter);
    }

    private void populateRecyclerView() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!productList.isEmpty())
                    productList.clear();

                if (snapshot.exists() && snapshot.hasChild("Product") && currentUser != null)
                    if (snapshot.child("Product").hasChildren())
                        for (DataSnapshot product : snapshot.child("Product").getChildren())
                            if (product.hasChild("idProducator") &&
                                    String.valueOf(product.child("idProducator").getValue())
                                            .equals(currentUser.getUid())) {
                                Product myProduct = product.getValue(Product.class);

                                if (myProduct != null) {
                                    if (product.hasChild("LocatieProducator") &&
                                            product.child("LocatieProducator")
                                                    .hasChild("Latitudine"))
                                        myProduct.setLatitudineProducator(Double
                                                .parseDouble(String.valueOf(product
                                                        .child("LocatieProducator")
                                                        .child("Latitudine").getValue())));
                                    if (product.hasChild("LocatieProducator") &&
                                            product.child("LocatieProducator").
                                                    hasChild("Longitudine"))
                                        myProduct.setLongitudineProducator(Double.
                                                parseDouble(String.valueOf(product
                                                        .child("LocatieProducator")
                                                        .child("Longitudine").getValue())));

                                    productList.add(myProduct);
                                }
                            }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}