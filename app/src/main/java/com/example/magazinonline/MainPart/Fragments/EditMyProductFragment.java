package com.example.magazinonline.MainPart.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
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

import com.bumptech.glide.Glide;
import com.example.magazinonline.Classes.Product;
import com.example.magazinonline.MainPart.Activities.Home;
import com.example.magazinonline.MainPart.ViewModels.HomeViewModel;
import com.example.magazinonline.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_edit_my_product,
                        container,
                        false);

        setVariables(view);
        hideToolbar();
        setTitle();
        setProductDetails();
        setOnClickListeners();
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

        goBack.setOnClickListener(view -> ((Home) requireActivity()).onBackPressed());

        productImage.setOnClickListener(view -> {
                    Intent gallery = new Intent();
                    gallery.setType("image/*");
                    gallery.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(gallery,
                            "Select picture"), viewModel.getPICK_IMAGE());
                }
        );

        save.setOnClickListener(view -> {
            // imaginea se modifica in storage, dar nu si in aplicatie
            if (viewModel.isEditProductImageModified()) {
                storageReference
                        .child("Product photos")
                        .child(selectedProduct.getIdProdus() + ".jpg")
                        .putFile(imageUri);
                viewModel.setEditProductImageModified(false);
            }

            if (viewModel.isProductIsModified()) {
                if (!String.valueOf(productNameField.getText())
                        .equals(selectedProduct.getNumeProdus()) &&
                        !String.valueOf(productNameField.getText()).trim().equals("")) {
                    databaseReference
                            .child("Product")
                            .child(selectedProduct.getIdProdus())
                            .child("NumeProdus")
                            .setValue(String.valueOf(productNameField.getText()));
                }

                if (!String.valueOf(productDescriptionField.getText())
                        .equals(selectedProduct.getDescriereProdus()) &&
                        !String.valueOf(productDescriptionField.getText()).trim().equals("")) {
                    databaseReference
                            .child("Product")
                            .child(selectedProduct.getIdProdus())
                            .child("descriereProdus")
                            .setValue(String.valueOf(productDescriptionField.getText()));
                }

                if (!String.valueOf(productPriceField.getText())
                        .equals(selectedProduct.getPretProdus()) &&
                        !String.valueOf(productPriceField.getText()).trim().equals("")) {
                    databaseReference
                            .child("Product")
                            .child(selectedProduct.getIdProdus())
                            .child("PretProdus")
                            .setValue(String.valueOf(productPriceField.getText()));
                }

                if (!String.valueOf(productCategorySpinner.getSelectedItem())
                        .equals(selectedProduct.getCategorie())) {
                    databaseReference
                            .child("Product")
                            .child(selectedProduct.getIdProdus())
                            .child("Categorie")
                            .setValue(String.valueOf(productCategorySpinner
                                    .getSelectedItem()));
                }

                if (!String.valueOf(productLocationLatitude.getText())
                        .equals(String.valueOf(selectedProduct
                                .getLatitudineProducator())) &&
                        !String.valueOf(productLocationLatitude.getText()).trim().equals("")) {
                    databaseReference
                            .child("Product")
                            .child(selectedProduct.getIdProdus())
                            .child("LocatieProducator")
                            .child("Latitudine")
                            .setValue(String.valueOf(productLocationLatitude
                                    .getText()));
                }

                if (!String.valueOf(productLocationLongitude.getText())
                        .equals(String.valueOf(selectedProduct
                                .getLongitudineProducator())) &&
                        !String.valueOf(productLocationLongitude.getText()).trim().equals("")) {
                    databaseReference
                            .child("Product")
                            .child(selectedProduct.getIdProdus())
                            .child("LocatieProducator")
                            .child("Longitudine")
                            .setValue(String.valueOf(productLocationLongitude
                                    .getText()));
                }

                Toast.makeText(requireContext(),
                        "Produsul a fost modificat cu succes",
                        Toast.LENGTH_SHORT).show();
            }

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
        String priceText = selectedProduct.getPretProdus() + " " + "RON";
        int productCategory = -1;

        Glide.with(requireActivity()).load(selectedProduct.getImage()).into(productImage);
        productNameField.setHint(selectedProduct.getNumeProdus());
        productDescriptionField.setHint(selectedProduct.getDescriereProdus());
        productPriceField.setHint(priceText);

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

    private void setListenersForModifications() {
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
        } else if (requestCode == viewModel.getPICK_IMAGE() &&
                resultCode == RESULT_OK &&
                data != null) {
            imageUri = data.getData();
            Glide.with(requireActivity()).load(imageUri).into(productImage);
            viewModel.setEditProductImageModified(true);
        }
    }
}