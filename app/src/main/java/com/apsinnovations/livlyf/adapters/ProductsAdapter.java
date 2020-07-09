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

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyProductsHolder> {
    Context context;
    int resource;
    ArrayList<Products> products;
    MyCartListener myCartListener;
    ArrayList<Products> addedProducts;
    private static final String TAG = "ProductsAdapter";

    public ProductsAdapter(Context context, int resource, ArrayList<Products> products) {
        this.context = context;
        this.resource = resource;
        this.products = products;
        try {
            myCartListener = (MyCartListener) context;
        } catch (ClassCastException e) {
            Log.i("Exception Caught", "" + e.getMessage());
        }
        addedProducts=new ArrayList<>();
        getMyCartFromFirebase();
    }

    @NonNull
    @Override
    public MyProductsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyProductsHolder(LayoutInflater.from(context).inflate(resource, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductsHolder holder, int position) {
        Picasso.get().load(products.get(position).getUrl()).fit().into(holder.imgProduct);
        holder.txtName.setText(products.get(position).getName());
        holder.txtPrice.setText("\u20B9".concat(String.valueOf(products.get(position).getPrice())));
        holder.txtMRP.setText("\u20B9".concat(String.valueOf(products.get(position).getMrp())));
        holder.txtMRP.setPaintFlags(holder.txtMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txtDisocunt.setText("-".concat(String.valueOf
                (products.get(position).getDiscount())).concat("%"));
        holder.quantityPicker.setMaxQuantity(products.get(position).getQty());
        holder.quantityPicker.setMinQuantity(1);

        for(Products myProduct:addedProducts){
            if(myProduct.getID()!=null){
            if(myProduct.getID().equals(products.get(position).getID())){
                holder.addToCart.setText("Added");
                holder.addToCart.setClickable(false);
            }
            }
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyProductsHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, imgFav;
        Button addToCart;
        TextView txtName, txtPrice, txtMRP, txtDisocunt;
        QuantityPicker quantityPicker;

        public MyProductsHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_Product);
            imgFav = itemView.findViewById(R.id.imgFav);
            txtName = itemView.findViewById(R.id.product_name);
            txtPrice = itemView.findViewById(R.id.product_price);
            txtDisocunt = itemView.findViewById(R.id.product_discount);
            txtMRP = itemView.findViewById(R.id.product_mrp);
            addToCart = itemView.findViewById(R.id.btnAddtoCart);
            quantityPicker = itemView.findViewById(R.id.quantityPicker);

            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantityPicker.getQuantity() > 0) {
                        addToCart.setText("Added");
                        addToCart.setClickable(false);

                        Products myProduct = products.get(getLayoutPosition());
                        myProduct.setQty(quantityPicker.getQuantity());

                        addItemToCartInFirebase(myProduct);

//                        cartPrefMananger.setCartItem(myProduct);
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
    }


    private void getMyCartFromFirebase(){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .collection("myCart")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments()){
                    Products myProduct=snapshot.toObject(Products.class);
                    assert myProduct != null;
                    myProduct.setID(snapshot.getId());
                    addedProducts.add(myProduct);
                    Log.i(TAG, "onSuccess: "+snapshot.toObject(Products.class));
                }
                notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

}
