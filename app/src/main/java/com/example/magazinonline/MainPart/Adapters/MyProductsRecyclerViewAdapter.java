package com.example.magazinonline.MainPart.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magazinonline.Classes.Product;
import com.example.magazinonline.MainPart.Activities.Home;
import com.example.magazinonline.MainPart.Fragments.EditMyProductFragment;
import com.example.magazinonline.MainPart.ViewModels.HomeViewModel;
import com.example.magazinonline.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyProductsRecyclerViewAdapter extends RecyclerView
        .Adapter<MyProductsRecyclerViewAdapter
        .MyProductsRecyclerViewAdapterViewHolder> {
    private Context context;
    private List<Product> productList;
    private RecyclerView recyclerView;

    public MyProductsRecyclerViewAdapter(Context context,
                                         List<Product> productList,
                                         RecyclerView recyclerView) {
        this.context = context;
        this.productList = productList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MyProductsRecyclerViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                      int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.my_product_custom_item,
                        parent,
                        false);

        return new MyProductsRecyclerViewAdapter
                .MyProductsRecyclerViewAdapterViewHolder(view, productList, recyclerView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductsRecyclerViewAdapterViewHolder holder,
                                 int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getNumeProdus());
        holder.productEdit.setImageResource(R.drawable.ic_edit);
        holder.productRemove.setImageResource(R.drawable.ic_remove);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class MyProductsRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {
        private HomeViewModel viewModel;
        private DatabaseReference databaseReference;
        private Context context;
        private TextView productName;
        private ImageView productEdit;
        private ImageView productRemove;
        private List<Product> productList;
        private RecyclerView recyclerView;
        private Product myProduct;

        public MyProductsRecyclerViewAdapterViewHolder(@NonNull View itemView,
                                                       List<Product> productList,
                                                       RecyclerView recyclerView,
                                                       Context context) {
            super(itemView);

            this.viewModel = new ViewModelProvider((ViewModelStoreOwner) context)
                    .get(HomeViewModel.class);
            this.databaseReference = FirebaseDatabase.getInstance().getReference();
            this.context = context;
            this.productList = productList;
            this.recyclerView = recyclerView;

            setVariables(itemView);
            setOnClickListeners();
        }

        private void setVariables(View v) {
            productName = v.findViewById(R.id.my_product_name);
            productEdit = v.findViewById(R.id.my_product_edit);
            productRemove = v.findViewById(R.id.my_product_remove);
        }

        private void setOnClickListeners() {
            productEdit.setOnClickListener(view -> {
                myProduct = productList.get(getBindingAdapterPosition());

                viewModel.setSelectedProduct(myProduct);

                ((Home) context).setFragment(new EditMyProductFragment());

                Log.d("edit", myProduct
                        .getNumeProdus());
            });

            productRemove.setOnClickListener(view -> {
                myProduct = productList.get(getBindingAdapterPosition());

                MyProductsRecyclerViewAdapter adapter =
                        (MyProductsRecyclerViewAdapter) recyclerView.getAdapter();

                if (adapter != null) {
                    databaseReference.addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists() &&
                                            snapshot.hasChild("Product") &&
                                            snapshot.hasChild("User")) {
                                        // stergem produsul din lista de produse din baza de date
                                        // si din cosurile de cumparaturi
                                        // in cazul in care prima stergere
                                        // a fost realizata cu succes
                                        removeProduct(snapshot, myProduct);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                    productList.remove(getBindingAdapterPosition());

                    adapter.notifyItemRemoved(getBindingAdapterPosition());
                }
            });
        }

        private void removeProduct(DataSnapshot snapshot, Product myProduct) {
            // stergem produsul mai intai din lista de produse din baza de date
            if (snapshot.child("Product").hasChildren())
                for (DataSnapshot product : snapshot.child("Product").getChildren())
                    if (product.hasChild("idProducator") &&
                            String.valueOf(product.child("idProducator").getValue())
                                    .equals(myProduct.getIdProducator()))
                        if (product.hasChild("NumeProdus") &&
                                String.valueOf(product.child("NumeProdus").getValue())
                                        .equals(myProduct.getNumeProdus())) {
                            Product productToBeRemoved = product.getValue(Product.class);

                            if (productToBeRemoved != null &&
                                    String.valueOf(productToBeRemoved.getIdProdus()
                                            .equals(myProduct.getIdProdus()))
                                            .equals("true") &&
                                    String.valueOf(productToBeRemoved.getIdProducator()
                                            .equals(myProduct.getIdProducator()))
                                            .equals("true") &&
                                    String.valueOf(productToBeRemoved.getNumeProdus()
                                            .equals(myProduct.getNumeProdus()))
                                            .equals("true") &&
                                    String.valueOf(productToBeRemoved.getNumeProdus()
                                            .equals(myProduct.getNumeProdus()))
                                            .equals("true") &&
                                    String.valueOf(productToBeRemoved.getPretProdus()
                                            .equals(myProduct.getPretProdus()))
                                            .equals("true") &&
                                    String.valueOf(productToBeRemoved.getAdresaProducator()
                                            .equals(myProduct.getAdresaProducator()))
                                            .equals("true") &&
                                    String.valueOf(productToBeRemoved.getLatitudineProducator() ==
                                            myProduct.getLatitudineProducator())
                                            .equals("true") &&
                                    String.valueOf(productToBeRemoved.getLongitudineProducator() ==
                                            myProduct.getLongitudineProducator())
                                            .equals("true") &&
                                    String.valueOf(productToBeRemoved.getDataAdaugareProdus()
                                            .getYear() ==
                                            myProduct.getDataAdaugareProdus().getYear())
                                            .equals("true") &&
                                    String.valueOf(productToBeRemoved.getDataAdaugareProdus()
                                            .getMonth() ==
                                            myProduct.getDataAdaugareProdus().getMonth())
                                            .equals("true") &&
                                    String.valueOf(productToBeRemoved.getDataAdaugareProdus()
                                            .getMonthName()
                                            .equals(myProduct
                                                    .getDataAdaugareProdus()
                                                    .getMonthName()))
                                            .equals("true") &&
                                    String.valueOf(productToBeRemoved.getDataAdaugareProdus()
                                            .getDay() ==
                                            myProduct
                                                    .getDataAdaugareProdus()
                                                    .getDay())
                                            .equals("true") &&
                                    String.valueOf(productToBeRemoved.getDataAdaugareProdus()
                                            .getDayName()
                                            .equals(myProduct.getDataAdaugareProdus().getDayName()))
                                            .equals("true") &&
                                    String.valueOf(productToBeRemoved.getDataAdaugareProdus()
                                            .getHour() ==
                                            myProduct
                                                    .getDataAdaugareProdus()
                                                    .getHour())
                                            .equals("true") &&
                                    String.valueOf(productToBeRemoved.getDataAdaugareProdus()
                                            .getMinute() ==
                                            myProduct
                                                    .getDataAdaugareProdus()
                                                    .getMinute())
                                            .equals("true") &&
                                    String.valueOf(productToBeRemoved.getDataAdaugareProdus()
                                            .getSecond() ==
                                            myProduct.
                                                    getDataAdaugareProdus()
                                                    .getSecond())
                                            .equals("true") &&
                                    String.valueOf(productToBeRemoved.getCategorie()
                                            .equals(myProduct.getCategorie()))
                                            .equals("true") &&
                                    String.valueOf(productToBeRemoved.getImage()
                                            .equals(myProduct.getImage()))
                                            .equals("true")) {
                                databaseReference
                                        .child("Product")
                                        .child(myProduct.getIdProdus())
                                        .removeValue()
                                        // stergem produsul din toate cosurile de cumparaturi
                                        // in cazul in care stergerea din lista
                                        // a fost realizata cu succes
                                        .addOnCompleteListener(task ->
                                                removeProductFromShoppingCarts(snapshot,
                                                        myProduct));
                            }
                        }
        }

        private void removeProductFromShoppingCarts(DataSnapshot snapshot, Product myProduct) {
            if (snapshot.child("User").hasChildren())
                for (DataSnapshot user : snapshot.child("User").getChildren())
                    if (user.hasChild("shoppingCart") &&
                            user.child("shoppingCart").hasChildren())
                        for (DataSnapshot userShoppingCartProductCategory :
                                user.child("shoppingCart").getChildren())
                            if (String.valueOf(userShoppingCartProductCategory
                                    .getKey()).equals(myProduct.getCategorie()))
                                if (userShoppingCartProductCategory.hasChildren())
                                    for (DataSnapshot userShoppingCartProductID :
                                            userShoppingCartProductCategory.getChildren())
                                        if (String.valueOf(userShoppingCartProductID.getKey())
                                                .equals(myProduct.getIdProdus())) {
                                            databaseReference
                                                    .child("User")
                                                    .child(String.valueOf(user.getKey()))
                                                    .child("shoppingCart")
                                                    .child(myProduct.getCategorie())
                                                    .child(myProduct.getIdProdus())
                                                    .removeValue()
                                                    .addOnCompleteListener(task ->
                                                            Toast.makeText(context,
                                                                    "Produsul a fost sters" +
                                                                            " cu succes",
                                                                    Toast.LENGTH_SHORT).show());
                                        }
        }
    }
}