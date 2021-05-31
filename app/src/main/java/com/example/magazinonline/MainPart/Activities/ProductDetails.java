package com.example.magazinonline.MainPart.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.magazinonline.AuthenticationPart.LogIn;
import com.example.magazinonline.Classes.Model;
import com.example.magazinonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductDetails extends AppCompatActivity {
    private ImageView goBack;
    private ImageView productImage;
    private TextView productName;
    private TextView productDescription;
    private TextView productPrice;
    private Button addToCartButton;
    private Model selectedProduct;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private static final String currency = "RON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        setVariables();
        setOnClickListeners();

        if (selectedProduct != null) {
            setProductImage(selectedProduct.getImage());
            setProductName(selectedProduct.getNumeProdus());
            setProductDescription(selectedProduct.getDescriereProdus());
            setProductPrice(selectedProduct.getPretProdus() + " " + currency);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void setVariables() {
        selectedProduct = (Model) getIntent().getSerializableExtra("selected_product");
        goBack = findViewById(R.id.product_details_go_back);
        addToCartButton = findViewById(R.id.product_details_add_to_cart_button);
        productImage = findViewById(R.id.product_details_image);
        productName = findViewById(R.id.product_details_name);
        productDescription = findViewById(R.id.product_details_description);
        productPrice = findViewById(R.id.product_details_price);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void setOnClickListeners() {
        addToCartButton.setOnClickListener(view -> {
            if (currentUser != null) {
                addProductToCart(new Model(selectedProduct.getIdProdus(),
                        selectedProduct.getNumeProdus(),
                        selectedProduct.getDescriereProdus(),
                        selectedProduct.getPretProdus(),
                        selectedProduct.getAdresaProducator(),
                        selectedProduct.getData(),
                        selectedProduct.getImage(),
                        selectedProduct.getCategorie()));
                goBack.callOnClick();
            } else {
                Intent intent = new Intent(ProductDetails.this, LogIn.class);
                intent.putExtra("product_details", "true");
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        goBack.setOnClickListener(view -> onBackPressed());
    }

    public static String getCurrency() {
        return currency;
    }

    private void setProductDescription(String description) {
        productDescription.setText(description);
    }

    public void setProductName(String name) {
        productName.setText(name);
    }

    private void setProductImage(String image) {
        Glide.with(getApplicationContext()).load(image).into(productImage);
    }

    private void setProductPrice(String price) {
        productPrice.setText(price);
    }

    private void sendMessage(String message) {
        Toast.makeText(ProductDetails.this, message, Toast.LENGTH_SHORT).show();
    }

    // metoda pentru adaugarea unui produs in cos
    private void addProductToCart(Model product) {
        databaseReference.child("User").child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // verificam daca produsul exista deja
                        // si daca nu cumva cosul de cumparaturi este gol
                        boolean productAlreadyExists = false;
                        boolean shoppingCartIsEmpty = true;

                        if (snapshot.hasChild("shoppingCart")) {
                            // in cazul in care utilizatorul are deja cosul initializat
                            shoppingCartIsEmpty = false;

                            // parcurgem produsele din cos si verificam daca nu cumva produsul
                            // pe care dorim sa il adaugam exista deja
                            for (DataSnapshot productFromShoppingCart :
                                    snapshot.child("shoppingCart").getChildren())
                                if (String.valueOf(productFromShoppingCart.getKey())
                                        .equals(product.getIdProdus())) {
                                    productAlreadyExists = true;
                                    break;
                                }
                        }

                        // in cazul in care cosul este gol sau produsul nu exista deja in cos,
                        // il adaugam si afisam mesaj de succes
                        if (shoppingCartIsEmpty || !productAlreadyExists) {
                            databaseReference
                                    .child("User")
                                    .child(currentUser.getUid())
                                    .child("shoppingCart")
                                    .child(product.getIdProdus())
                                    .setValue(1);
                            sendMessage("Product added to your shopping cart");
                        }

                        // in cazul in care produsul exista deja in cos,
                        // nu il mai adaugam si afisam mesaj
                        else
                            sendMessage("Product already in your cart");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}