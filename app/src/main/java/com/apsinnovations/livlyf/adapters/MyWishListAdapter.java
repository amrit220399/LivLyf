package com.apsinnovations.livlyf.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andanhm.quantitypicker.QuantityPicker;
import com.apsinnovations.livlyf.R;
import com.apsinnovations.livlyf.models.Products;
import com.apsinnovations.livlyf.utils.MyCartListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class MyWishListAdapter extends RecyclerView.Adapter<MyWishListAdapter.MyWishListHolder> {
    Context context;
    int resource;
    ArrayList<Products> products;
    private static final String TAG = "MyWishListAdapter";
    ArrayList<Products> addedProducts;
    MyCartListener myCartListener;

    public MyWishListAdapter(Context context, int resource, ArrayList<Products> products) {
        this.context = context;
        this.resource = resource;
        this.products = products;
        addedProducts = new ArrayList<>();
        myCartListener = (MyCartListener) context;
        getMyCartFromFirebase();
    }

    private void getMyCartFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .collection("myCart")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                    Products myProduct = snapshot.toObject(Products.class);
                    assert myProduct != null;
                    myProduct.setID(snapshot.getId());
                    addedProducts.add(myProduct);
                    Log.i(TAG, "onSuccess: " + snapshot.toObject(Products.class));
                }
                notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: " + e.getMessage());
            }
        });
    }

    @NonNull
    @Override
    public MyWishListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyWishListAdapter.MyWishListHolder(LayoutInflater.from(context).inflate(resource, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyWishListHolder holder, int position) {
        Picasso.get().load(products.get(position).getUrl()).fit().into(holder.imgProduct);
        holder.txtName.setText(products.get(position).getName());
        holder.txtPrice.setText("\u20B9".concat(String.valueOf(products.get(position).getPrice())));
        holder.txtMrp.setText("\u20B9".concat(String.valueOf(products.get(position).getMrp())));
        holder.txtMrp.setPaintFlags(holder.txtMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txtDis.setText("-".concat(String.valueOf(products.get(position).getDiscount())).concat("%"));
        holder.quantityPicker.setMaxQuantity(products.get(position).getQty());
        holder.quantityPicker.setMinQuantity(1);

        for (Products myProduct : addedProducts) {
            if (myProduct.getID() != null) {
                if (myProduct.getID().equals(products.get(position).getID())) {
                    holder.btnAddToCart.setText(R.string.added);
                    holder.btnAddToCart.setClickable(false);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
            return products.size();
    }

    public class MyWishListHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, imgDelete;
        TextView txtName, txtPrice, txtMrp, txtDis;
        Button btnAddToCart;
        QuantityPicker quantityPicker;

        public MyWishListHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.wish_img_Product);
            imgDelete = itemView.findViewById(R.id.wish_imgDelete);
            txtName = itemView.findViewById(R.id.wish_product_name);
            txtPrice = itemView.findViewById(R.id.wish_product_price);
            txtMrp = itemView.findViewById(R.id.wish_product_mrp);
            txtDis = itemView.findViewById(R.id.wish_product_discount);
            quantityPicker = itemView.findViewById(R.id.wish_quantityPicker);
            btnAddToCart = itemView.findViewById(R.id.wish_btnAddToCart);

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteMyItemFromwWishList();
                }
            });

            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantityPicker.getQuantity() > 0) {
                        btnAddToCart.setText(R.string.added);
                        btnAddToCart.setClickable(false);

                        Products myProduct = products.get(getLayoutPosition());
                        myProduct.setQty(quantityPicker.getQuantity());

                        addItemToCartInFirebase(myProduct);

                    } else {
                        Toast.makeText(context, "Items Can't Be 0!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void addItemToCartInFirebase(Products myProduct) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .collection("myCart")
                    .document(myProduct.getID())
                    .set(myProduct)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            myCartListener.setItems(1);
                            Log.i(TAG, "onSuccess: Added to Cart");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "onFailure: " + e.getMessage());
                }
            });
        }

        private void deleteMyItemFromwWishList() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("myWishList")
                    .document(products.get(getLayoutPosition()).getID())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            products.remove(getLayoutPosition());
                            notifyItemRemoved(getLayoutPosition());
                        }
                    });
        }
    }
}
