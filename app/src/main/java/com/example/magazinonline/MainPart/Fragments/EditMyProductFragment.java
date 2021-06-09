package com.example.magazinonline.MainPart.Fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.magazinonline.Classes.ProducerAddress;
import com.example.magazinonline.Classes.Product;
import com.example.magazinonline.MainPart.Activities.Home;
import com.example.magazinonline.MainPart.ViewModels.HomeViewModel;
import com.example.magazinonline.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class EditMyProductFragment extends Fragment {
    private HomeViewModel viewModel;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Product selectedProduct;
    private ImageView goBack;
    private TextView title;
    private ImageView productImage;
    private EditText productNameField;
    private EditText productDescriptionField;
    private EditText productPriceField;
    private EditText productQuantityField;
    private Spinner productCategorySpinner;
    private Button changeLocation;
    private TextView productLocationLatitude;
    private TextView productLocationLongitude;
    private Button cancel;
    private Button save;
    private WifiManager wifiManager;
    private Uri imageUri;

    public EditMyProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_edit_my_product,
                        container,
                        false);

        // initializam variabilele
        setVariables(view);

        // ascundem toolbar-ul din activitatea parinte
        hideToolbar();

        // setam titlul fragmentului (va fi vizibil in bara creata de noi)
        setTitle();

        // setam detaliile produsului
        setProductDetails();

        // setam listener-urile onclick
        setOnClickListeners();

        // setam listener-urile ce monitorizeaza fiecare modificare a campurilor produsului
        setListenersForModifications();

        return view;
    }

    private void setVariables(View v) {
        viewModel = new ViewModelProvider(requireActivity())
                .get(HomeViewModel.class);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        wifiManager = (WifiManager) requireContext()
                .getSystemService(Context.WIFI_SERVICE);
        selectedProduct = viewModel.getSelectedProduct();
        goBack = v.findViewById(R.id.edit_my_product_go_back);
        title = v.findViewById(R.id.edit_my_product_title);
        productImage = v.findViewById(R.id.edit_my_product_image);
        productNameField = v.findViewById(R.id.edit_my_product_name);
        productDescriptionField = v.findViewById(R.id.edit_my_product_description);
        productPriceField = v.findViewById(R.id.edit_my_product_price);
        productQuantityField = v.findViewById(R.id.edit_my_product_quantity);
        productCategorySpinner = v.findViewById(R.id.edit_my_product_category);
        changeLocation = v.findViewById(R.id.edit_my_product_change_location);
        productLocationLatitude = v.findViewById(R.id.edit_my_product_latitude);
        productLocationLongitude = v.findViewById(R.id.edit_my_product_longitude);
        cancel = v.findViewById(R.id.edit_my_product_cancel);
        save = v.findViewById(R.id.edit_my_product_save);
    }

    private void setOnClickListeners() {
        cancel.setOnClickListener(view -> ((Home) requireActivity()).onBackPressed());

        changeLocation.setOnClickListener(view -> {
            wifiManager.setWifiEnabled(false);

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            try {
                wifiManager.setWifiEnabled(true);
                startActivityForResult(builder.build(requireActivity()),
                        viewModel.getPLACE_PICKER_REQUEST());
            } catch (GooglePlayServicesRepairableException |
                    GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        });

        goBack.setOnClickListener(view -> requireActivity().onBackPressed());

        save.setOnClickListener(view -> {
            // verificam mai intai daca am facut vreo modificare
            if (viewModel.isProductIsModified()) {
                // daca am modificat numele produsului
                if (!String.valueOf(productNameField.getText())
                        .equals(selectedProduct.getNumeProdus()) &&
                        !String.valueOf(productNameField.getText()).trim().equals("")) {
                    databaseReference
                            .child("Product")
                            .child(selectedProduct.getIdProdus())
                            .child("NumeProdus")
                            .setValue(String.valueOf(productNameField.getText()));
                }

                // daca am modificat descrierea produsului
                if (!String.valueOf(productDescriptionField.getText())
                        .equals(selectedProduct.getDescriereProdus()) &&
                        !String.valueOf(productDescriptionField.getText()).trim().equals("")) {
                    databaseReference
                            .child("Product")
                            .child(selectedProduct.getIdProdus())
                            .child("descriereProdus")
                            .setValue(String.valueOf(productDescriptionField.getText()));
                }

                // daca am modificat pretul produsului
                if (!String.valueOf(productPriceField.getText())
                        .equals(selectedProduct.getPretProdus()) &&
                        !String.valueOf(productPriceField.getText()).trim().equals("")) {
                    databaseReference
                            .child("Product")
                            .child(selectedProduct.getIdProdus())
                            .child("PretProdus")
                            .setValue(String.valueOf(productPriceField.getText()));
                }

                // daca am modificat cantitatea produsului
                if (!String.valueOf(productQuantityField.getText())
                        .equals(String.valueOf(selectedProduct.getCantitateProdus())) &&
                        !String.valueOf(productQuantityField.getText()).trim().equals("")) {
                    databaseReference
                            .child("Product")
                            .child(selectedProduct.getIdProdus())
                            .child("cantitateProdus")
                            .setValue(Integer.parseInt(String.valueOf(productQuantityField
                                    .getText())));
                }

                // daca am modificat categoria produsului
                if (!String.valueOf(productCategorySpinner.getSelectedItem())
                        .equals(selectedProduct.getCategorie())) {
                    databaseReference
                            .child("Product")
                            .child(selectedProduct.getIdProdus())
                            .child("Categorie")
                            .setValue(getCategoryFromStringResource(String
                                    .valueOf(productCategorySpinner.getSelectedItem())));
                }

                // daca am modificat latitudinea produsului
                if (!String.valueOf(productLocationLatitude.getText())
                        .equals(String.valueOf(selectedProduct
                                .getLatitudineProducator())) &&
                        !String.valueOf(productLocationLatitude.getText()).trim().equals("")) {
                    databaseReference
                            .child("Product")
                            .child(selectedProduct.getIdProdus())
                            .child("LocatieProducator")
                            .child("Latitudine")
                            .setValue(Double.valueOf(String.valueOf(productLocationLatitude
                                    .getText())));
                }

                // daca am modificat longitudinea produsului
                if (!String.valueOf(productLocationLongitude.getText())
                        .equals(String.valueOf(selectedProduct
                                .getLongitudineProducator())) &&
                        !String.valueOf(productLocationLongitude.getText()).trim().equals("")) {
                    databaseReference
                            .child("Product")
                            .child(selectedProduct.getIdProdus())
                            .child("LocatieProducator")
                            .child("Longitudine")
                            .setValue(Double.valueOf(String.valueOf(productLocationLongitude
                                    .getText())));
                }

                // verificam daca locatia introdusa de utilizator difera de cea deja existenta
                if ((!String.valueOf(productLocationLatitude.getText())
                        .equals(String.valueOf(selectedProduct
                                .getLatitudineProducator())) &&
                        !String.valueOf(productLocationLatitude.getText()).trim().equals("")) &&
                        (!String.valueOf(productLocationLongitude.getText())
                                .equals(String.valueOf(selectedProduct
                                        .getLongitudineProducator())) &&
                                !String.valueOf(productLocationLongitude.getText()).trim()
                                        .equals(""))) {
                    double selectedLatitude = Double.parseDouble(String.
                            valueOf(productLocationLatitude.getText()));
                    double selectedLongitude = Double.parseDouble(String.
                            valueOf(productLocationLongitude.getText()));

                    // adaugam/modificam strada, localitatea, judetul/regiunea si tara produsului
                    try {
                        Geocoder geo = new Geocoder(requireContext(), Locale.getDefault());
                        List<Address> addresses = geo
                                .getFromLocation(selectedLatitude, selectedLongitude, 1);
                        if (!addresses.isEmpty()) {
                            ProducerAddress address = new ProducerAddress(addresses.get(0)
                                    .getFeatureName(),
                                    addresses.get(0).getLocality(),
                                    addresses.get(0).getAdminArea(),
                                    addresses.get(0).getCountryName());

                            if (addresses.get(0).getFeatureName() == null) {
                                address.setStreet("null");
                            }

                            if (addresses.get(0).getLocality() == null) {
                                address.setLocality("null");
                            }

                            if (addresses.get(0).getAdminArea() == null) {
                                address.setArea("null");
                            }

                            if (addresses.get(0).getCountryName() == null) {
                                address.setCountry("null");
                            }

                            databaseReference
                                    .child("Product")
                                    .child(selectedProduct.getIdProdus())
                                    .child("AdresaProducator")
                                    .setValue(address);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(requireContext(),
                        getResources().getString(R.string.product_successfully_modified),
                        Toast.LENGTH_SHORT).show();
            }

            // apelam listener-ul pentru click pe go back
            goBack.callOnClick();
        });
    }

    private void setTitle() {
        title.setText(selectedProduct.getNumeProdus());
    }

    private void hideToolbar() {
        ((Home) requireActivity()).hideToolbar();
    }

    private void setProductDetails() {
        String priceText = selectedProduct.getPretProdus() + " " +
                getResources().getString(R.string.currency);
        String quantityText = selectedProduct.getCantitateProdus() + " ";
        int productCategory = -1;

        if (selectedProduct.getCantitateProdus() == 1) {
            quantityText += getResources().getString(R.string.piece);
        } else {
            quantityText += getResources().getString(R.string.pieces);
        }

        Glide.with(requireActivity()).load(selectedProduct.getImage()).into(productImage);
        productNameField.setHint(selectedProduct.getNumeProdus());
        productDescriptionField.setHint(selectedProduct.getDescriereProdus());
        productPriceField.setHint(priceText);
        productQuantityField.setHint(quantityText);

        switch (selectedProduct.getCategorie()) {
            case "Mancare traditionala":
                productCategory = 0;
                break;
            case "Preparate bio":
                productCategory = 1;
                break;
            case "Bauturi specifice":
                productCategory = 2;
                break;
            case "Fructe si legume":
                productCategory = 3;
                break;
        }

        if (productCategory != -1)
            productCategorySpinner.setSelection(productCategory);

        productLocationLatitude.setText(String.valueOf(selectedProduct
                .getLatitudineProducator()));
        productLocationLongitude.setText(String.valueOf(selectedProduct
                .getLongitudineProducator()));
    }

    // metoda pentru detectarea modificarii datelor produsului
    private void setListenersForModifications() {
        // verificam daca am introdus un nume diferit de cel deja existent si setam variabila
        // ce tine minte daca am modificat sau nu produsul
        productNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 &&
                        !viewModel.isProductIsModified() &&
                        !charSequence.equals(selectedProduct.getNumeProdus()))
                    viewModel.setProductIsModified(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // verificam daca am introdus o descriere diferita de cea deja existenta si setam variabila
        // ce tine minte daca am modificat sau nu produsul
        productDescriptionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 &&
                        !viewModel.isProductIsModified() &&
                        !charSequence.equals(selectedProduct.getDescriereProdus()))
                    viewModel.setProductIsModified(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // verificam daca am introdus un pret diferit de cel deja existent si setam variabila
        // ce tine minte daca am modificat sau nu produsul
        productPriceField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 &&
                        !viewModel.isProductIsModified() &&
                        !charSequence.equals(selectedProduct.getPretProdus()))
                    viewModel.setProductIsModified(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // verificam daca am introdus o cantitate diferita de cea deja existenta si setam variabila
        // ce tine minte daca am modificat sau nu produsul
        productQuantityField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 &&
                        !viewModel.isProductIsModified() &&
                        !charSequence.equals(String.valueOf(selectedProduct.getCantitateProdus())))
                    viewModel.setProductIsModified(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // verificam daca am selectat o categorie diferita de cea deja existenta si setam variabila
        // ce tine minte daca am modificat sau nu produsul
        productCategorySpinner.setOnItemSelectedListener(new AdapterView
                .OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!String.valueOf(productCategorySpinner.getSelectedItem())
                        .equals(selectedProduct.getCategorie()) &&
                        !viewModel.isProductIsModified())
                    viewModel.setProductIsModified(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // metoda pentru traducerea categoriei
    private String getCategoryFromStringResource(String category) {
        String[] categoryListFromResources = getResources().getStringArray(R.array.names);
        String translatedCategory = "";

        if (Locale.getDefault().getDisplayLanguage().equals("română")) {
            return category;
        } else {
            if (category.equals(categoryListFromResources[0])) {
                translatedCategory = "Mancare traditionala";
            } else if (category.equals(categoryListFromResources[1])) {
                translatedCategory = "Preparate bio";
            } else if (category.equals(categoryListFromResources[2])) {
                translatedCategory = "Bauturi specifice";
            } else if (category.equals(categoryListFromResources[3])) {
                translatedCategory = "Fructe si legume";
            }

            return translatedCategory;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == viewModel.getPLACE_PICKER_REQUEST() &&
                resultCode == RESULT_OK &&
                data != null) {
            Place place = PlacePicker.getPlace(data, requireContext());
            double latitude = place.getLatLng().latitude;
            double longitude = place.getLatLng().longitude;

            if (selectedProduct.getLatitudineProducator() != latitude ||
                    selectedProduct.getLongitudineProducator() != longitude) {
                viewModel.setProductIsModified(true);

                productLocationLatitude.setText(String.valueOf(latitude));
                productLocationLongitude.setText(String.valueOf(longitude));
            }
        }
    }
}