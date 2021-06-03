package com.example.magazinonline.MainPart.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magazinonline.Classes.App;
import com.example.magazinonline.MainPart.Fragments.HomeFragment;
import com.example.magazinonline.MainPart.Activities.ProductInfoFirebase;
import com.example.magazinonline.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private HomeFragment context;
    private List<App> apps;
    private Context context2;

    public CustomAdapter(HomeFragment context, List<App> apps) {
        this.context = context;
        this.apps = apps;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_list_item, parent, false);

        context2 = parent.getContext();

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // initializarea variabilelor
        App app = apps.get(position);
        String categoryText = "";
        Intent intent = new Intent(context2, ProductInfoFirebase.class);

        holder.mName.setText(app.getName());
        holder.mImage.setImageResource(app.getImage());

        switch (position) {
            case 0:
                categoryText = "Mancare traditionala";
                break;
            case 1:
                categoryText = "Preparate bio";
                break;
            case 2:
                categoryText = "Bauturi specifice";
                break;
            case 3:
                categoryText = "Fructe si legume";
                break;
        }

        // salvarea unei copii a stringului ce contine categoria selectata
        // pentru a putea fi accesata in listener
        final String copyOfCategoryText = categoryText;

        // initializarea listenerului onclick
        holder.itemView.setOnClickListener(view -> {
            // adaugam intent-ului categoria selectata si pornim activitatea de afisare
            // a produselor pe categorii
            intent.putExtra("position", copyOfCategoryText);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private ImageView mImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializarea view-urilor (numele si imaginea categoriei)
            mName = itemView.findViewById(R.id.name);
            mImage = itemView.findViewById(R.id.image);
        }
    }
}