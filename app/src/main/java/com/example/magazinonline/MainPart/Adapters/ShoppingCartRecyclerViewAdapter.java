package com.example.magazinonline.MainPart.Adapters;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

// adapter custom pentru recyclerview-ul din cosul de cumparaturi
public class ShoppingCartRecyclerViewAdapter extends RecyclerView
        .Adapter<ShoppingCartRecyclerViewAdapter.ShoppingCartRecyclerViewViewHolder> {
    private Context context;
    private List<Model> modelList;
    private RecyclerView recyclerView;
    private static DatabaseReference databaseReference = FirebaseDatabase
            .getInstance().getReference();
    private static FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private static Model model;

    public static Model getModel() {
        return model;
    }

    public static FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public static DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public ShoppingCartRecyclerViewAdapter(Context context,
                                           List<Model> modelList,
                                           RecyclerView recyclerView) {
        this.context = context;
        this.modelList = modelList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ShoppingCartRecyclerViewAdapter.
            ShoppingCartRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                  int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.recycler_view_item_layout, parent, false);

        return new ShoppingCartRecyclerViewViewHolder(view, modelList, recyclerView);
    }

    // in metoda asta afisam datele produsului
    @Override
    public void onBindViewHolder(@NonNull ShoppingCartRecyclerViewAdapter
            .ShoppingCartRecyclerViewViewHolder holder, int position) {
        model = modelList.get(position);
        String priceText = model.getPretProdus() + " " + ProductDetails.getCurrency();

        Glide.with(context).load(model.getImage()).into(holder.productImage);
        holder.productName.setText(model.getNumeProdus());
        holder.productDescription.setText(model.getDescriereProdus());
        holder.productPrice.setText(priceText);
        holder.productQuantity.setText(String.valueOf(model.getCantitate()));
        holder.productDelete.setImageResource(R.drawable.ic_remove);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    // clasa interioara ce are rol de viewholder
    // (initializeaza view-urile ce afiseaza datele produsului pe ecran
    // si seteaza listener-urile pentru clickuri)
    public static class ShoppingCartRecyclerViewViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName;
        private TextView productDescription;
        private TextView productPrice;
        private TextView productQuantity;
        private ImageView productDelete;

        public ShoppingCartRecyclerViewViewHolder(@NonNull View itemView,
                                                  List<Model> modelList,
                                                  RecyclerView recyclerView) {
            super(itemView);

            // initializarea view-urilor
            productImage = itemView.findViewById(R.id.recycler_view_card_view_image);
            productName = itemView.findViewById(R.id.recycler_view_item_top_layout_product_name);
            productDescription = itemView
                    .findViewById(R.id.recycler_view_item_top_layout_product_description);
            productPrice = itemView.findViewById(R.id.recycler_view_item_top_layout_product_price);
            productQuantity = itemView
                    .findViewById(R.id.recycler_view_item_bottom_layout_item_details_item_count);
            productDelete = itemView.findViewById(R.id.recycler_view_item_bottom_layout_remove);
            ImageView productAdd = itemView
                    .findViewById(R.id.recycler_view_item_bottom_layout_item_details_add);
            ImageView productSubtract = itemView
                    .findViewById(R.id.recycler_view_item_bottom_layout_item_details_subtract);

            // setarea listener-ului pentru click pe butonul de incrementarea cantitatii prodului
            productAdd.setOnClickListener(view -> {
                if (ShoppingCartRecyclerViewAdapter.getModel() != null) {
                    // salvam produsul a carui cantitate dorim sa o incrementam
                    Model model = modelList.get(getBindingAdapterPosition());

                    ShoppingCartRecyclerViewAdapter
                            .getDatabaseReference()
                            .child("User")
                            .child(ShoppingCartRecyclerViewAdapter.getCurrentUser().getUid())
                            .child("shoppingCart")
                            .child(model.getIdProdus())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // salvam cantitatea curenta a produsului din cos
                                        int quantity = Integer.parseInt(String.valueOf(snapshot
                                                .getValue()));

                                        // salvam adapter-ul recyclerview-ului
                                        ShoppingCartRecyclerViewAdapter adapter =
                                                (ShoppingCartRecyclerViewAdapter) recyclerView
                                                        .getAdapter();

                                        // setam cantitatea produsului in lista ce va fi afisata
                                        // in recyclerview
                                        model.setCantitate(quantity + 1);

                                        // incrementarea cantitatii produsului in baza de date
                                        ShoppingCartRecyclerViewAdapter
                                                .getDatabaseReference()
                                                .child("User")
                                                .child(ShoppingCartRecyclerViewAdapter
                                                        .getCurrentUser().getUid())
                                                .child("shoppingCart")
                                                .child(model.getIdProdus())
                                                .setValue(quantity + 1);

                                        // actualizam adapter-ul recyclerview-ului
                                        // dupa modificarea cantitatii produsului
                                        if (adapter != null)
                                            adapter.notifyItemChanged(getBindingAdapterPosition());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            });

            // setarea listener-ului pentru click pe butonul de stergere a prodului
            productDelete.setOnClickListener(view -> {
                if (ShoppingCartRecyclerViewAdapter.getModel() != null) {
                    // salvam produsul pe care dorim sa il stergem
                    Model model = modelList.get(getBindingAdapterPosition());

                    // salvam adapter-ul recyclerview-ului
                    ShoppingCartRecyclerViewAdapter adapter =
                            (ShoppingCartRecyclerViewAdapter) recyclerView.getAdapter();

                    // stergem produsul din lista ce este randata in recyclerview
                    modelList.remove(getBindingAdapterPosition());

                    // actualizam adapter-ul recyclerview-ului dupa stergerea produsului
                    if (adapter != null)
                        adapter.notifyItemRemoved(getBindingAdapterPosition());

                    // stergerea produsului din baza de date
                    ShoppingCartRecyclerViewAdapter
                            .getDatabaseReference()
                            .child("User")
                            .child(ShoppingCartRecyclerViewAdapter.getCurrentUser().getUid())
                            .child("shoppingCart")
                            .child(model.getIdProdus())
                            .removeValue();
                }
            });

            // setarea listener-ului pentru click pe butonul de decrementarea cantitatii prodului
            productSubtract.setOnClickListener(view -> {
                if (ShoppingCartRecyclerViewAdapter.getModel() != null) {
                    // salvam produsul a carui cantitate dorim sa o decrementam
                    Model model = modelList.get(getBindingAdapterPosition());

                    ShoppingCartRecyclerViewAdapter
                            .getDatabaseReference()
                            .child("User")
                            .child(ShoppingCartRecyclerViewAdapter.getCurrentUser().getUid())
                            .child("shoppingCart")
                            .child(model.getIdProdus())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // salvam cantitatea curenta a produsului din cos
                                        int quantity = Integer.parseInt(String.valueOf(snapshot
                                                .getValue()));

                                        // salvam adapter-ul recyclerview-ului
                                        ShoppingCartRecyclerViewAdapter adapter =
                                                (ShoppingCartRecyclerViewAdapter) recyclerView
                                                        .getAdapter();

                                        // decrementam cantitatea doar in cazul in care avem
                                        // cel putin un produs de acest tip in cos
                                        if (quantity > 0) {
                                            // setam cantitatea produsului in lista
                                            // ce va fi afisata in recyclerview
                                            model.setCantitate(quantity - 1);

                                            // decrementarea cantitatii produsului in baza de date
                                            ShoppingCartRecyclerViewAdapter
                                                    .getDatabaseReference()
                                                    .child("User")
                                                    .child(ShoppingCartRecyclerViewAdapter
                                                            .getCurrentUser().getUid())
                                                    .child("shoppingCart")
                                                    .child(model.getIdProdus())
                                                    .setValue(quantity - 1);
                                        }

                                        // actualizam adapter-ul recyclerview-ului
                                        // dupa modificarea cantitatii produsului
                                        if (adapter != null)
                                            adapter.notifyItemChanged(getBindingAdapterPosition());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            });
        }
    }
}