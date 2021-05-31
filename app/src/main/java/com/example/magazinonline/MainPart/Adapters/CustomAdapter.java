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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        context2 = parent.getContext();
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        App app = apps.get(position);
        holder.mName.setText(app.getName());
        holder.mImage.setImageResource(app.getImage());

        switch(position)
        {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }

//     asocierea imaginilor cu lista de produse
        if (position == 0) {
            holder.itemView.setOnClickListener(v -> {
                String value = "Mancare traditionala";//trimitem pozitia lor
                Intent intent1 = new Intent(context2, ProductInfoFirebase.class);
                intent1.putExtra("position", value);
                context.startActivity(intent1);
            });
        } else if (position == 1) {
            holder.itemView.setOnClickListener(v -> {
                String value = "Preparate bio";
                Intent intent1 = new Intent(context2, ProductInfoFirebase.class);
                intent1.putExtra("position", value);
                context.startActivity(intent1);
            });
        } else if (position == 2) {
            holder.itemView.setOnClickListener(v -> {
                String value = "Bauturi specifice";
                Intent intent1 = new Intent(context2, ProductInfoFirebase.class);
                intent1.putExtra("position", value);
                context.startActivity(intent1);
            });
        } else if (position == 3) {
            holder.itemView.setOnClickListener(v -> {
                String value = "Fructe si legume";
                Intent intent1 = new Intent(context2, ProductInfoFirebase.class);
                intent1.putExtra("position", value);
                context.startActivity(intent1);
            });
        }
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
            mName = itemView.findViewById(R.id.name);
            mImage = itemView.findViewById(R.id.image);
        }
    }
}