package com.example.magazinonline.MainPart.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magazinonline.Classes.User;
import com.example.magazinonline.MainPart.Activities.Home;
import com.example.magazinonline.MainPart.Fragments.ProducerDetailsFragment;
import com.example.magazinonline.MainPart.ViewModels.HomeViewModel;
import com.example.magazinonline.R;

import java.util.List;

public class ProducersRecyclerViewAdapter extends RecyclerView.
        Adapter<ProducersRecyclerViewAdapter.ProducersRecyclerViewViewHolder> {
    private Context context;
    private List<User> producerList;
    private RecyclerView recyclerView;
    private User producer;
    private Activity activity;

    public ProducersRecyclerViewAdapter(Context context,
                                        List<User> producerList,
                                        RecyclerView recyclerView, Activity activity) {
        this.context = context;
        this.producerList = producerList;
        this.recyclerView = recyclerView;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ProducersRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                              int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.producer_custom_item, parent, false);

        return new ProducersRecyclerViewAdapter.
                ProducersRecyclerViewViewHolder(view, producerList, activity, context, recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProducersRecyclerViewViewHolder holder, int position) {
        String producerFullName;

        producer = producerList.get(position);
        producerFullName = producer.getPrenume() + " " + producer.getName();
        holder.producerNameText.setText(producerFullName);
        holder.producerCheckProductsImage.setImageResource(R.drawable.ic_arrow_forward);
    }

    @Override
    public int getItemCount() {
        return producerList.size();
    }

    public static class ProducersRecyclerViewViewHolder extends RecyclerView.ViewHolder {
        private HomeViewModel viewModel;
        private TextView producerNameText;
        private ImageView producerCheckProductsImage;
        private List<User> producerList;
        private RecyclerView recyclerView;
        private User producer;
        private Context context;
        private Activity activity;

        public ProducersRecyclerViewViewHolder(@NonNull View itemView,
                                               List<User> producerList,
                                               Activity activity,
                                               Context context,
                                               RecyclerView recyclerView) {
            super(itemView);

            this.producerList = producerList;
            this.recyclerView = recyclerView;
            this.activity = activity;
            this.context = context;
            this.viewModel = new ViewModelProvider((ViewModelStoreOwner) context)
                    .get(HomeViewModel.class);

            setVariables(itemView);
            setOnClickListeners();
        }

        private void setVariables(View v) {
            producerNameText = v.findViewById(R.id.producer_name);
            producerCheckProductsImage = v.findViewById(R.id.producer_check_products);
        }

        private void setOnClickListeners() {
            producerCheckProductsImage.setOnClickListener(view -> {
                // initializarea producatorului selectat de catre utilizator
                producer = producerList.get(getBindingAdapterPosition());

                // setarea producatorului in viewmodel
                viewModel.setSelectedProducer(producer);

                // setarea fragmentului ce prezinta detaliile producatorului
                // (se apeleaza metoda publica a clasei de baza -> Home)
                ((Home) activity).setFragment(new ProducerDetailsFragment());
            });
        }
    }
}
