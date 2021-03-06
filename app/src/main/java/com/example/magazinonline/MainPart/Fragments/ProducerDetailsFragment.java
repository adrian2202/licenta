package com.example.magazinonline.MainPart.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.magazinonline.Classes.Product;
import com.example.magazinonline.Classes.User;
import com.example.magazinonline.MainPart.Activities.Home;
import com.example.magazinonline.MainPart.Adapters.ProducerProductsRecyclerViewAdapter;
import com.example.magazinonline.MainPart.ViewModels.HomeViewModel;
import com.example.magazinonline.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProducerDetailsFragment extends Fragment {
    private HomeViewModel viewModel;
    private DatabaseReference databaseReference;
    private User selectedProducer;
    private ImageView goBack;
    private Button sendMessageButton;
    private CircleImageView producerImage;
    private TextView producerFullName;
    private TextView producerEmail;
    private TextView producerPhone;
    private RecyclerView producerProductsRecyclerView;
    private ProducerProductsRecyclerViewAdapter adapter;
    private List<Product> productList;
    private TextView noProductsText;

    public ProducerDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_producer_details, container, false);

        setVariables(view);
        setRecyclerView();
        setOnClickListeners();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (selectedProducer != null) {
            ((Home) requireActivity()).hideToolbar();
            setProducerDetails();
            setProducerProducts();
        }
    }

    private void setVariables(View v) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        productList = new ArrayList<>();
        selectedProducer = viewModel.getSelectedProducer();
        goBack = v.findViewById(R.id.producer_details_go_back);
        producerImage = v.findViewById(R.id.producer_image);
        producerFullName = v.findViewById(R.id.producer_full_name);
        producerEmail = v.findViewById(R.id.producer_email);
        producerPhone = v.findViewById(R.id.producer_phone);
        sendMessageButton = v.findViewById(R.id.producer_send_message);
        noProductsText = v.findViewById(R.id.producer_no_products_text);
        producerProductsRecyclerView = v.findViewById(R.id.producer_details_recyclerview);
        adapter = new ProducerProductsRecyclerViewAdapter(requireContext(),
                productList,
                getActivity());
    }

    private void setRecyclerView() {
        producerProductsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        producerProductsRecyclerView.setAdapter(adapter);
    }

    private void setOnClickListeners() {
        goBack.setOnClickListener(view -> requireActivity().onBackPressed());

        sendMessageButton.setOnClickListener(view -> {
            ((Home) requireActivity()).setFragment(new SendProducerEmailFragment());
        });
    }

    // metoda pentru setarea informatiilor producatorului selectat
    private void setProducerDetails() {
        String producerFullNameText = selectedProducer.getPrenume() + " " +
                selectedProducer.getName();

        Glide.with(requireContext()).load(selectedProducer.getImage()).into(producerImage);
        producerFullName.setText(producerFullNameText);
        producerEmail.setText(selectedProducer.getEmail());
        producerPhone.setText(selectedProducer.getNrtel());
    }

    // metoda pentru afisarea produselor detinute de producatorul selectat
    private void setProducerProducts() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // golim lista de produse de fiecare data cand datele din baza de date
                // se modifica si lista contine deja produse
                if (!productList.isEmpty())
                    productList.clear();

                // adaugarea in lista a fiecarui produs detinut de producatorul selectat
                if (snapshot.exists() && snapshot.hasChild("Product"))
                    if (snapshot.child("Product").hasChildren())
                        for (DataSnapshot product : snapshot.child("Product").getChildren())
                            if (product.hasChild("idProducator") &&
                                    String.valueOf(product.child("idProducator").getValue())
                                            .equals(selectedProducer.getId())) {
                                Product producerProduct = product.getValue(Product.class);

                                if (producerProduct != null)
                                    productList.add(producerProduct);
                            }

                // notificam adapter-ul la final pentru a actualiza produsele de pe ecran
                adapter.notifyDataSetChanged();

                // in cazul in care in lista nu exista vreun produs,
                // ascundem recyclerview-ul si afisam textul ce ne aduce la cunostinta faptul ca
                // producatorul nu are niciun produs adaugat
                if (productList.isEmpty()) {
                    producerProductsRecyclerView.setVisibility(View.GONE);
                    noProductsText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}