package com.example.magazinonline.MainPart.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.magazinonline.Classes.Model;
import com.example.magazinonline.MainPart.Activities.ProductDetails;
import com.example.magazinonline.R;

import java.util.List;
import java.util.Locale;

public class MyAdapterUpProduct extends RecyclerView.Adapter<MyAdapterUpProduct.modelViewholder> {
    private Context mCtx;
    private List<Model> modelList;
    private RecyclerView mRecyclerView;
    private Activity parentActivity;

    public MyAdapterUpProduct(Context mCtx, List<Model> modelList, RecyclerView recyclerView,
                              Activity parentActivity) {
        this.mCtx = mCtx;
        this.modelList = modelList;
        this.mRecyclerView = recyclerView;
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public MyAdapterUpProduct.modelViewholder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                 int viewType) {
        View view = LayoutInflater
                .from(mCtx)
                .inflate(R.layout.singlerow, parent, false);

        // in cazul in care dam click pe un produs,
        // vom fi trimisi catre activitatea ce arata detaliile produsului
        view.setOnClickListener(view1 -> {
            int itemPosition = mRecyclerView.getChildLayoutPosition(view1);
            Model item = modelList.get(itemPosition);
            Intent intent = new Intent(parent.getContext(), ProductDetails.class);

            intent.putExtra("selected_product", item);
            parent.getContext().startActivity(intent);
            parentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        return new modelViewholder(view);
    }

    // in metoda asta afisam datele produsului
    @Override
    public void onBindViewHolder(@NonNull MyAdapterUpProduct.modelViewholder holder,
                                 int position) {
        Model model = modelList.get(position);
        holder.nume.setText(model.getNumeProdus());
        holder.descriere.setText(model.getDescriereProdus());
        holder.pret.setText(model.getPretProdus());
        holder.locatie.setText(model.getAdresaProducator());
        holder.categorie.setText(translateCategoryFromRomanian(model.getCategorie()));
        Glide.with(holder.img.getContext()).load(model.getImage()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    private String translateCategoryFromRomanian(String category) {
        String translatedCategory = "";
        String[] categoryListFromStringArray = parentActivity
                .getResources().getStringArray(R.array.names);

        if (Locale.getDefault().getDisplayLanguage().equals("română")) {
            return category;
        } else {
            if (category.equals("Mancare traditionala")) {
                translatedCategory = categoryListFromStringArray[0];
            } else if (category.equals("Preparate bio")) {
                translatedCategory = categoryListFromStringArray[1];
            } else if (category.equals("Bauturi specifice")) {
                translatedCategory = categoryListFromStringArray[2];
            } else if (category.equals("Fructe si legume")) {
                translatedCategory = categoryListFromStringArray[3];
            }

            return translatedCategory;
        }
    }

    // clasa interioara ce are rol de viewholder (initializeaza view-urile ce afiseaza
    // datele produsului pe ecran)
    public static class modelViewholder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView nume;
        private TextView descriere;
        private TextView pret;
        private TextView locatie;
        private TextView categorie;

        public modelViewholder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img1);
            nume = itemView.findViewById(R.id.nameProdus);
            descriere = itemView.findViewById(R.id.descriptionProdus);
            pret = itemView.findViewById(R.id.priceProduct);
            locatie = itemView.findViewById(R.id.locationProduct);
            categorie = itemView.findViewById(R.id.catProduct);
        }
    }
}