package com.example.magazinonline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class myAdapterUpProduct extends FirebaseRecyclerAdapter <model,myAdapterUpProduct.myviewholder>{
    public myAdapterUpProduct(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull model model) {

        holder.nume.setText(model.getNumeProdus());
        holder.descriere.setText(model.getDescriereProdus());
        holder.pret.setText(model.getPretProdus());
        holder.locatie.setText(model.getAdresaProducator());
        holder.categorie.setText(model.getCategorie());
        Glide.with(holder.img.getContext()).load(model.getImage()).into(holder.img);

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
       return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView nume, descriere,pret,locatie,categorie;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img=(ImageView) itemView.findViewById(R.id.img1);
            nume=(TextView)itemView.findViewById(R.id.nameProdus) ;
            descriere=(TextView) itemView.findViewById(R.id.descriptionProdus);
            pret=(TextView)itemView.findViewById(R.id.priceProduct);
            locatie=(TextView)itemView.findViewById(R.id.locationProduct);
            categorie=(TextView)itemView.findViewById(R.id.catProduct);


        }
    }
}
