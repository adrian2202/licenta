package com.example.magazinonline.MainPart.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.magazinonline.MainPart.Adapters.MyAdapterUpProduct;
import com.example.magazinonline.Classes.Model;
import com.example.magazinonline.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Lista2Categorii extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapterUpProduct adapter;
    private List<Model> modelList;
    DatabaseReference dbProduct;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista2_categorii);
        setVariables();
        setRecyclerView();

        dbProduct = FirebaseDatabase.getInstance().getReference("Product");

        Query query = FirebaseDatabase.getInstance().getReference("Product")
                .orderByChild("Categorie")
                .equalTo("Produse bio");
        query.addListenerForSingleValueEvent(valueEventListener);

    }

    private void setVariables()
    {
        modelList = new ArrayList<>();
        adapter = new MyAdapterUpProduct(this, modelList, recyclerView, Lista2Categorii.this);
        recyclerView = findViewById(R.id.recyclerViewProduct2);
    }

    private void setRecyclerView()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            modelList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Model model = snapshot.getValue(Model.class);
                    modelList.add(model);
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}