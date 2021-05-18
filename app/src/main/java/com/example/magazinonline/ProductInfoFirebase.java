package com.example.magazinonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductInfoFirebase extends AppCompatActivity {

//    private DatabaseReference databaseReference;
//    RecyclerView recyclerViewProduct;
//    myAdapterUpProduct adapter;
//    Intent intent;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_product_info_firebase);
//
////        databaseReference = FirebaseDatabase.getInstance().getReference();
//
//        recyclerViewProduct=(RecyclerView) findViewById(R.id.recyclerViewProduct);
//        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(this));
//
//        FirebaseRecyclerOptions<model> options=new FirebaseRecyclerOptions.Builder<model>()
//                .setQuery(FirebaseDatabase.getInstance().getReference().child("Product"),model.class).build();
//
//
//
//            adapter = new myAdapterUpProduct(options);
//            recyclerViewProduct.setAdapter(adapter);
//
//
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            String value = extras.getString("position");
//            //The key argument here must match that used in the other activity
//            Log.d(value,"position");
//        }
//
//    }
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }

    private  RecyclerView  recyclerView;
    private  myAdapterUpProduct adapter;
    private List<model> modelList;

    DatabaseReference dbProduct;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info_firebase);

        recyclerView = findViewById(R.id.recyclerViewProduct);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        modelList=new ArrayList<>();
        adapter=new myAdapterUpProduct(this,modelList);
        recyclerView.setAdapter(adapter);

        dbProduct=FirebaseDatabase.getInstance().getReference("Product");
//        dbProduct.addValueEventListener(valueEventListener);
//        dbProduct.addListenerForSingleValueEvent(valueEventListener);

          //primim pozitia lui value
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("position");
            //The key argument here must match that used in the other activity
            Log.d(value,"position");

            Query query = FirebaseDatabase.getInstance().getReference("Product")
                .orderByChild("Categorie")
                .equalTo(value);
        query.addListenerForSingleValueEvent(valueEventListener);}

    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            modelList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    model model = snapshot.getValue(model.class);
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