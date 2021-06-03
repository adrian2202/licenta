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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magazinonline.Classes.Product;
import com.example.magazinonline.MainPart.Activities.ProductDetails;
import com.example.magazinonline.MainPart.ViewModels.HomeViewModel;
import com.example.magazinonline.R;

import java.util.List;

public class ProducerProductsRecyclerViewAdapter extends RecyclerView
        .Adapter<ProducerProductsRecyclerViewAdapter.ProducerProductsRecyclerViewViewHolder> {
    private Context context;
    private List<Product> productList;
    private Product product;
    private Activity activity;

    public ProducerProductsRecyclerViewAdapter(Context context,
                                               List<Product> productList,
                                               Activity activity) {
        this.context = context;
        this.productList = productList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ProducerProductsRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                     int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.product_custom_item, parent, false);

        return new ProducerProductsRecyclerViewAdapter.
                ProducerProductsRecyclerViewViewHolder(view, productList, activity, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ProducerProductsRecyclerViewViewHolder holder,
                                 int position) {
        product = productList.get(position);
        holder.productNameText.setText(product.getNumeProdus());
        holder.productCheckImage.setImageResource(R.drawable.ic_arrow_forward);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProducerProductsRecyclerViewViewHolder extends RecyclerView.ViewHolder {
        private HomeViewModel viewModel;
        private TextView productNameText;
        private ImageView productCheckImage;
        private List<Product> productList;
        private Product product;
        private Context context;
        private Activity activity;

        public ProducerProductsRecyclerViewViewHolder(@NonNull View itemView,
                                                      List<Product> productList,
                                                      Activity activity,
                                                      Context context) {
            super(itemView);

            this.productList = productList;
            this.activity = activity;
            this.context = context;
            this.viewModel = new ViewModelProvider((ViewModelStoreOwner) context)
                    .get(HomeViewModel.class);

            setVariables(itemView);
            setOnClickListeners();
        }

        private void setVariables(View v) {
            productNameText = v.findViewById(R.id.product_name);
            productCheckImage = v.findViewById(R.id.product_check_products);
        }

        private void setOnClickListeners() {
            productCheckImage.setOnClickListener(view -> {
                product = productList.get(getBindingAdapterPosition());
                viewModel.setSelectedProducerProduct(product);

                if (product != null) {
                    Intent intent = new Intent(context, ProductDetails.class);
                    intent.putExtra("selected_product", product);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            });
        }
    }
}