package com.example.magazinonline.MainPart.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.magazinonline.Classes.Model;
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
    private Activity parentActivity;
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
                                           RecyclerView recyclerView,
                                           Activity parentActivity) {
        this.context = context;
        this.modelList = modelList;
        this.recyclerView = recyclerView;
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public ShoppingCartRecyclerViewAdapter.
            ShoppingCartRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                  int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.recycler_view_item_layout, parent, false);

        return new ShoppingCartRecyclerViewViewHolder(view, modelList, recyclerView, context,
                parentActivity);
    }

    // in metoda asta afisam datele produsului
    @Override
    public void onBindViewHolder(@NonNull ShoppingCartRecyclerViewAdapter
            .ShoppingCartRecyclerViewViewHolder holder, int position) {
        model = modelList.get(position);
        String priceText = model.getPretProdus() + " " + parentActivity
                .getResources().getString(R.string.currency);

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
        private ImageView productAdd;
        private ImageView productSubtract;
        private List<Model> modelList;
        private RecyclerView recyclerView;
        private Context context;
        private Activity parentActivity;

        public ShoppingCartRecyclerViewViewHolder(@NonNull View itemView,
                                                  List<Model> modelList,
                                                  RecyclerView recyclerView,
                                                  Context context,
                                                  Activity parentActivity) {
            super(itemView);

            this.modelList = modelList;
            this.recyclerView = recyclerView;
            this.context = context;
            this.parentActivity = parentActivity;

            setVariables(itemView);
            setOnClickListeners();
        }

        private void setVariables(View v) {
            productImage = v.findViewById(R.id.recycler_view_card_view_image);
            productName = v.findViewById(R.id.recycler_view_item_top_layout_product_name);
            productDescription = v
                    .findViewById(R.id.recycler_view_item_top_layout_product_description);
            productPrice = v.findViewById(R.id.recycler_view_item_top_layout_product_price);
            productQuantity = v
                    .findViewById(R.id.recycler_view_item_bottom_layout_item_details_item_count);
            productDelete = v.findViewById(R.id.recycler_view_item_bottom_layout_remove);
            productAdd = v.findViewById(R.id.recycler_view_item_bottom_layout_item_details_add);
            productSubtract = v
                    .findViewById(R.id.recycler_view_item_bottom_layout_item_details_subtract);
        }

        private void setOnClickListeners() {
            // setarea listener-ului pentru click pe butonul de incrementarea cantitatii produsului
            productAdd.setOnClickListener(view -> {
                if (ShoppingCartRecyclerViewAdapter.getModel() != null &&
                        getBindingAdapterPosition() > -1) {
                    // salvam produsul a carui cantitate dorim sa o incrementam
                    Model model = modelList.get(getBindingAdapterPosition());

                    if (model != null) {
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

                                            ShoppingCartRecyclerViewAdapter
                                                    .getDatabaseReference()
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists() &&
                                                                    snapshot.hasChild("Product") &&
                                                                    snapshot.child("Product").hasChildren())
                                                                for (DataSnapshot productID :
                                                                        snapshot.child("Product").
                                                                                getChildren())
                                                                    if (String.valueOf(productID.
                                                                            getKey())
                                                                            .equals(model.getIdProdus()) &&
                                                                            productID.hasChild("cantitateProdus")) {
                                                                        int productQuantityFromDatabase =
                                                                                Integer.parseInt(String.
                                                                                        valueOf(productID.
                                                                                                child("cantitateProdus")
                                                                                                .getValue()));

                                                                        if (productQuantityFromDatabase > 0) {
                                                                            // setam cantitatea
                                                                            // produsului in lista
                                                                            // ce va fi afisata
                                                                            // in recyclerview
                                                                            model.setCantitate(quantity + 1);

                                                                            // incrementarea
                                                                            // cantitatii produsului
                                                                            // in baza de date
                                                                            ShoppingCartRecyclerViewAdapter
                                                                                    .getDatabaseReference()
                                                                                    .child("User")
                                                                                    .child(ShoppingCartRecyclerViewAdapter
                                                                                            .getCurrentUser()
                                                                                            .getUid())
                                                                                    .child("shoppingCart")
                                                                                    .child(model.getIdProdus())
                                                                                    .setValue(quantity + 1);

                                                                            ShoppingCartRecyclerViewAdapter
                                                                                    .getDatabaseReference()
                                                                                    .child("Product")
                                                                                    .child(model.getIdProdus())
                                                                                    .child("cantitateProdus")
                                                                                    .setValue(productQuantityFromDatabase - 1);

                                                                            // actualizam adapter-ul recyclerview-ului
                                                                            // dupa modificarea cantitatii produsului
                                                                            if (adapter != null) {
                                                                                adapter.notifyItemChanged(getBindingAdapterPosition());
                                                                            }
                                                                        } else {
                                                                            Toast.makeText(context,
                                                                                    parentActivity.
                                                                                            getResources().
                                                                                            getString(R.string.
                                                                                                    product_no_longer_available),
                                                                                    Toast.LENGTH_SHORT).show();
                                                                        }

                                                                        break;
                                                                    }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }
                }
            });

            // setarea listener-ului pentru click pe butonul de stergere a produsului
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

                    ShoppingCartRecyclerViewAdapter
                            .getDatabaseReference()
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists() &&
                                            snapshot.hasChild("User") &&
                                            snapshot.child("User").hasChildren())
                                        for (DataSnapshot userID :
                                                snapshot.child("User").getChildren())
                                            if (String.valueOf(userID.getKey()).
                                                    equals(ShoppingCartRecyclerViewAdapter.
                                                            getCurrentUser().getUid())) {
                                                if (userID.hasChild("shoppingCart") &&
                                                        userID.child("shoppingCart").hasChildren())
                                                    for (DataSnapshot userProductID :
                                                            userID.child("shoppingCart").
                                                                    getChildren())
                                                        if (String.valueOf(userProductID.getKey())
                                                                .equals(model.getIdProdus())) {
                                                            int shoppingCartProductQuantity =
                                                                    Integer.parseInt(String.
                                                                            valueOf(userProductID
                                                                                    .getValue()));

                                                            if (snapshot.hasChild("Product") &&
                                                                    snapshot.child("Product")
                                                                            .hasChildren())
                                                                for (DataSnapshot productID :
                                                                        snapshot.child("Product")
                                                                                .getChildren())
                                                                    if (String.valueOf(productID
                                                                            .getKey()).equals(model
                                                                            .getIdProdus()))
                                                                        if (productID.hasChild("cantitateProdus")) {
                                                                            int productQuantity =
                                                                                    Integer.parseInt(String
                                                                                            .valueOf(productID
                                                                                                    .child("cantitateProdus")
                                                                                                    .getValue()));

                                                                            ShoppingCartRecyclerViewAdapter
                                                                                    .getDatabaseReference()
                                                                                    .child("Product")
                                                                                    .child(model.getIdProdus())
                                                                                    .child("cantitateProdus")
                                                                                    .setValue(productQuantity +
                                                                                            shoppingCartProductQuantity);

                                                                            break;
                                                                        }

                                                            break;
                                                        }

                                                break;
                                            }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

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

            // setarea listener-ului pentru click pe butonul de decrementarea cantitatii produsului
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

                                            ShoppingCartRecyclerViewAdapter
                                                    .getDatabaseReference()
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists() &&
                                                                    snapshot.hasChild("Product") &&
                                                                    snapshot.child("Product").hasChildren())
                                                                for (DataSnapshot productID :
                                                                        snapshot.child("Product")
                                                                                .getChildren())
                                                                    if (String.valueOf(productID
                                                                            .getKey()).equals(model
                                                                            .getIdProdus()) &&
                                                                            productID.hasChild("cantitateProdus")) {
                                                                        int productQuantityFromDatabase =
                                                                                Integer.parseInt(String.
                                                                                        valueOf(productID
                                                                                                .child("cantitateProdus")
                                                                                                .getValue()));

                                                                        ShoppingCartRecyclerViewAdapter
                                                                                .getDatabaseReference()
                                                                                .child("Product")
                                                                                .child(model.getIdProdus())
                                                                                .child("cantitateProdus")
                                                                                .setValue(productQuantityFromDatabase + 1);

                                                                        break;
                                                                    }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(context,
                                                    parentActivity.getResources().
                                                            getString(R.string.
                                                                    product_no_longer_exists_in_shopping_cart),
                                                    Toast.LENGTH_SHORT).show();
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