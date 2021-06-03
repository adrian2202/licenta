package com.example.magazinonline.MainPart.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magazinonline.Classes.User;
import com.example.magazinonline.MainPart.Activities.Home;
import com.example.magazinonline.MainPart.Adapters.ProducersRecyclerViewAdapter;
import com.example.magazinonline.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProducersFragment extends Fragment {
    private ImageView goBack;
    private RecyclerView producersRecyclerView;
    private ProducersRecyclerViewAdapter adapter;
    private List<User> producerList;
    private DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_producers, container, false);

        setVariables(v);
        ((Home) requireActivity()).hideToolbar();
        setRecyclerView();
        setOnClickListeners();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        populateRecyclerView();
    }

    private void setVariables(View v) {
        goBack = v.findViewById(R.id.producers_go_back);
        producersRecyclerView = v.findViewById(R.id.producers_recyclerview);
        producerList = new ArrayList<>();
        adapter = new ProducersRecyclerViewAdapter(requireContext(),
                producerList,
                producersRecyclerView, ((Home) requireActivity()));
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void setOnClickListeners() {
        goBack.setOnClickListener(view -> requireActivity().onBackPressed());
    }

    private void setRecyclerView() {
        producersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        producersRecyclerView.setAdapter(adapter);
    }

    private void populateRecyclerView() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!producerList.isEmpty())
                    producerList.clear();

                if (snapshot.exists() && snapshot.hasChild("User"))
                    if (snapshot.child("User").hasChildren())
                        for (DataSnapshot producerID : snapshot.child("User").getChildren()) {
                            User producer = producerID.getValue(User.class);

                            if (producer != null) {
                                producer.setId(String.valueOf(producerID.getKey()));
                                producerList.add(producer);
                            }
                        }

                // sortam list de producatori dupa numele de familie ascendent
                if (!producerList.isEmpty()) {
                    Collections.sort(producerList, (user1, user2)
                            -> user1.getName().compareTo(user2.getName()));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
