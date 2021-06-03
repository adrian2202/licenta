package com.example.magazinonline.MainPart.Activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ProductInfoFirebase extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapterUpProduct adapter;
    private List<Model> modelList;
    private TextView pageTitle;
    private ImageView goBack;
    private Bundle extras;
    private DatabaseReference dbProduct;
    private ImageView search;
    private EditText searchField;
    private String value;
    private int numberOfSearchClicks = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info_firebase);
        setVariables();
        setRecyclerView();
        setListeners();

        // in cazul in care am primit categoria dorita selectata in activitatea precedenta,
        // setam titlul activitatii si produsele in recyclerview
        if (extras != null) {
            value = extras.getString("position");
            setPageTitle(value);
            setPageContent(value);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    // metoda pentru initializarea variabilelor membre ale clasei
    private void setVariables() {
        extras = getIntent().getExtras();
        dbProduct = FirebaseDatabase.getInstance().getReference("Product");
        pageTitle = findViewById(R.id.product_info_firebase_title);
        goBack = findViewById(R.id.product_info_firebase_go_back);
        recyclerView = findViewById(R.id.recyclerViewProduct);
        search = findViewById(R.id.product_info_firebase_search);
        searchField = findViewById(R.id.product_info_firebase_search_field);
        modelList = new ArrayList<>();
        adapter = new MyAdapterUpProduct(this, modelList, recyclerView,
                ProductInfoFirebase.this);
    }

    // metoda pentru adaugarea unor listenere
    private void setListeners() {
        // daca apasam pe imaginea de mers inapoi, activitatea va fi finalizata
        // si ne intoarcem la cea aflata sub ea in stiva
        goBack.setOnClickListener(view -> onBackPressed());

        search.setOnClickListener(view -> {
            ++numberOfSearchClicks;

            // in cazul in care numarul de click-uri pe butonul de search este impar,
            // ascundem titlul, afisam campul de cautare si punem focus pe el
            if (numberOfSearchClicks % 2 == 1) {
                pageTitle.setVisibility(View.INVISIBLE);
                searchField.setVisibility(View.VISIBLE);
                searchField.requestFocus();
            }

            // in cazul in care numarul de click-uri pe butonul de search este par
            // (dar mai mare ca 0), afisam titlul, ascundem campul de cautare,
            // scoatem focusul de pe el si ascundem tastatura in cazul in care e vizibila
            else if (numberOfSearchClicks > 0) {
                pageTitle.setVisibility(View.VISIBLE);
                searchField.setVisibility(View.INVISIBLE);
                searchField.clearFocus();
                closeTheKeyboard();
            }
        });

        // adaugam un listener a carei metoda onTextChanged va fi apelata de fiecare data cand
        // introducem sau stergem un caracter
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // in cazul in care sunt introduse caractere in campul de cautare
                if (charSequence.length() > 0) {
                    // golim lista ce va fi afisata in recyclerview in cazul in care ea contine
                    // deja elemente (pentru a nu randa produsele de mai multe ori)
                    if (modelList.size() > 0)
                        modelList.clear();

                    dbProduct.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // in cazul in care exista snapshot-ul si acesta are fii,
                            // iteram printre ei si cautam produsele ce apartin categoriei cautate
                            if (snapshot.exists() && snapshot.hasChildren())
                                for (DataSnapshot product : snapshot.getChildren())
                                    if (product.hasChild("Categorie") &&
                                            String.valueOf(product.child("Categorie").getValue())
                                                    .equals(value))

                                        //in cazul in care numele produsului respectiv contine
                                        // secventa de caractere introdusa de la tastatura,
                                        // il adaugam in lista de produse ce va fi afisata
                                        // in recyclerview
                                        if (product.hasChild("NumeProdus") &&
                                                String.valueOf(product.child("NumeProdus")
                                                        .getValue()).toLowerCase()
                                                        .contains(String.valueOf(charSequence)
                                                                .toLowerCase())) {
                                            Model model = product.getValue(Model.class);
                                            modelList.add(model);
                                        }

                            // actualizam adapter-ul
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                // in cazul in care nu este introdus niciun caracter in campul de cautare
                else {
                    // randam din nou pe ecran produsele din categoria cautata
                    setPageContent(value);

                    // ascunderea tastaturii in cazul in care e vizibila
                    closeTheKeyboard();

                    // se apasa pe butonul de cautare
                    search.callOnClick();

                    // actualizare adapter
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // listener pentru detectarea apasarii pe anumite taste
        // atunci cand scriem in campul de cautare
        searchField.setOnKeyListener((view, i, keyEvent) -> {
            // ascundem tastatura daca apasam enter si ea e vizibila
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                    (i == KeyEvent.KEYCODE_ENTER)) {
                closeTheKeyboard();
                return true;
            }

            return false;
        });
    }

    // metoda pentru setarea recyclerview-ului
    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    // metoda pentru setarea titlului activitatii
    private void setPageTitle(String title) {
        String translatedTitle = "";
        String[] categoriesTranslated = getResources().getStringArray(R.array.names);

        switch (title){
            case "Mancare traditionala":
                translatedTitle = categoriesTranslated[0];
                break;
            case "Preparate bio":
                translatedTitle = categoriesTranslated[1];
                break;
            case "Bauturi specifice":
                translatedTitle = categoriesTranslated[2];
                break;
            case "Fructe si legume":
                translatedTitle = categoriesTranslated[3];
                break;
        }

        pageTitle.setText(translatedTitle);
    }

    // metoda pentru afisarea produselor in recyclerview
    private void setPageContent(String value) {
        // setam un query ce va returna toate produsele ce fac parte din categoria dorita
        Query query = dbProduct.orderByChild("Categorie").equalTo(value);

        // adaugam un listener pentru adaugarea produselor in adapter
        // pentru a fi randate in recyclerview
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // golim lista de produse in cazul in care ea contine deja produse
                if (!modelList.isEmpty())
                    modelList.clear();

                if (dataSnapshot.exists()) {

                    // iteram printre produsele din categoria dorita si le adaugam
                    // in lista ce va fi randata in recyclerview
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Model model = snapshot.getValue(Model.class);
                        modelList.add(model);
                    }

                    // actualizam adapter-ul
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // metoda pentru ascunderea tastaturii in cazul in care e vizibila
    private void closeTheKeyboard() {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager manager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}