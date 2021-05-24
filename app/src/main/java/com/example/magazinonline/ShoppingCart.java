package com.example.magazinonline;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Model> modelList;
    private ShoppingCartRecyclerViewAdapter adapter;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private ImageView goBack;
    private TextView totalPrice;
    private Button payButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        setVariables();
        setRecyclerView();
        setOnClickListeners();
        setProducts();
        setTotalPrice();
    }

    // metoda ce e apelata atunci cand dam click pe butonul de inapoi => activitatea curenta e finalizata si mergem inapoi la activitatea situata sub aceasta in stiva
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // metoda pentru initializarea variabilelor membre ale clasei
    private void setVariables() {
        recyclerView = findViewById(R.id.shopping_cart_recycler_view);
        modelList = new ArrayList<>();
        adapter = new ShoppingCartRecyclerViewAdapter(this, modelList, recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        goBack = findViewById(R.id.shopping_cart_go_back);
        totalPrice = findViewById(R.id.shopping_cart_bottom_layout_total);
        payButton = findViewById(R.id.shopping_cart_bottom_layout_pay_button);
    }

    // metoda pentru setarea listener-elor pentru detectarea click-ului pe anumite view-uri
    private void setOnClickListeners() {
        // daca apasam pe imaginea de mers inapoi, activitatea va fi finalizata si ne intoarcem la cea aflata sub ea in stiva
        goBack.setOnClickListener(view -> finish());

        payButton.setOnClickListener(view -> {
            String[] priceTextSplit = String.valueOf(totalPrice.getText()).split(" ");

            // daca suma totala de plata este mai mare ca 0, apelam metoda de plata
            if (Integer.parseInt(priceTextSplit[0]) > 0)
                payTheOrder();

                // daca suma totala de plata este 0, se afiseaza mesaj pe ecran si nu putem realiza plata
            else
                Toast.makeText(ShoppingCart.this, "Your shopping cart is empty", Toast.LENGTH_SHORT).show();
        });
    }

    // metoda pentru setarea recyclerview-ului
    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    // metoda pentru adaugarea produselor in lista ce va fi afisata in recyclerview
    private void setProducts() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // golim lista de produse in cazul in care ea contine deja produse
                if (!modelList.isEmpty())
                    modelList.clear();

                // verificam daca utilizatorul curent are creat un cos de cumparaturi si iteram printre produsele adaugate in cos
                if (snapshot.exists() && snapshot.hasChildren() && currentUser != null)
                    if (snapshot.hasChild("User"))
                        if (snapshot.child("User").hasChildren())
                            for (DataSnapshot user : snapshot.child("User").getChildren())
                                if (String.valueOf(user.getKey()).equals(currentUser.getUid()))
                                    if (user.hasChild("shoppingCart"))
                                        if (user.child("shoppingCart").hasChildren())
                                            for (DataSnapshot category : user.child("shoppingCart").getChildren())
                                                if (category.hasChildren())
                                                    for (DataSnapshot productId : category.getChildren()) {
                                                        // am ajuns la parcurgerea fiecarui produs din cos
                                                        // salvam cantitatea fiecarui produs din cosul de cumparaturi din baza de date
                                                        int quantity = Integer.parseInt(String.valueOf(productId.getValue()));

                                                        // iteram printre produsele existente din baza de date si verificam daca exista id-ul corespunzator fiecarui produs din cos
                                                        if (snapshot.hasChild("Product"))
                                                            if (snapshot.child("Product").hasChildren()) {
                                                                for (DataSnapshot product : snapshot.child("Product").getChildren())

                                                                    // daca am gasit produsul in lista de produse din baza de date, il salvam si il adaugam in lista ce va fi afisata in recyclerview
                                                                    if (String.valueOf(product.getKey()).equals(String.valueOf(productId.getKey()))) {
                                                                        Model model = product.getValue(Model.class);

                                                                        if (model != null) {
                                                                            model.setCantitate(quantity);
                                                                            modelList.add(model);
                                                                            break;
                                                                        }
                                                                    }

                                                                // actualizarea adapter-ului cu datele proaspat salvate din baza de date
                                                                adapter.notifyDataSetChanged();
                                                            }
                                                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // metoda pentru calcularea si afisarea sumei totale de plata din momentul curent
    private void setTotalPrice() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalSum = 0;
                String totalPriceText;

                // iteram printre produsele existente in cosul de cumparaturi al utilizatorului
                if (snapshot.exists() && snapshot.hasChildren() && currentUser != null)
                    if (snapshot.hasChild("User"))
                        if (snapshot.child("User").hasChildren())
                            for (DataSnapshot user : snapshot.child("User").getChildren())
                                if (String.valueOf(user.getKey()).equals(currentUser.getUid()))
                                    if (user.hasChild("shoppingCart"))
                                        if (user.child("shoppingCart").hasChildren())
                                            for (DataSnapshot category : user.child("shoppingCart").getChildren())
                                                if (category.hasChildren())
                                                    for (DataSnapshot order : category.getChildren()) {
                                                        // salvam id-ul si cantitatea produsului curent
                                                        String orderID = String.valueOf(order.getKey());
                                                        int orderQuantity = Integer.parseInt(String.valueOf(order.getValue()));

                                                        // iteram printre produsele din baza de date si ii cautam pretul produsului curent
                                                        if (snapshot.hasChild("Product"))
                                                            if (snapshot.child("Product").hasChildren())
                                                                for (DataSnapshot product : snapshot.child("Product").getChildren())
                                                                    if (String.valueOf(product.getKey()).equals(orderID))
                                                                        if (product.hasChild("PretProdus")) {
                                                                            // salvam pretul produsului curent si il adaugam la suma totala
                                                                            int productPrice = Integer.parseInt(String.valueOf(product.child("PretProdus").getValue()));

                                                                            totalSum += orderQuantity * productPrice;
                                                                            break;
                                                                        }
                                                    }

                // afisam suma totala de plata tocmai calculata (impreuna cu moneda in care se face plata)
                totalPriceText = totalSum + " " + ProductDetails.getCurrency();
                totalPrice.setText(totalPriceText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // metoda pentru plata
    private void payTheOrder() {
        // afisam mesaj de succes
        Toast.makeText(ShoppingCart.this, "Enjoy your meal!", Toast.LENGTH_SHORT).show();

        // stergem cosul de cumparaturi al utilizatorului din baza de date
        databaseReference.child("User").child(currentUser.getUid()).child("shoppingCart").removeValue();

        // apelam butonul de mers inapoi
        onBackPressed();
    }
}