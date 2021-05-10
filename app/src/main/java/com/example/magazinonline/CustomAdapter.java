package com.example.magazinonline;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item,parent,false);
        context2=parent.getContext();
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        App app=apps.get(position);
        holder.mName.setText(app.getName());
        holder.mImage.setImageResource(app.getImage());
//        holder.mSize.setText(app.getSize());

        if(position==0)
        {
            holder.itemView.setOnClickListener(v -> {
//            Log.d("EMAIL=", worker.getEmail());
                Intent intent = new Intent(context2,ProductInfoFirebase.class);

                context.startActivity(intent);
            });
        }




    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
//        TextView mSize;
        ImageView mImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mName=itemView.findViewById(R.id.name);
            mImage=itemView.findViewById(R.id.image);
//            mSize=itemView.findViewById(R.id.size);


        }
    }
}
