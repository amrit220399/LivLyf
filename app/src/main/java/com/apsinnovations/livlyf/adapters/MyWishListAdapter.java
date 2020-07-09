package com.apsinnovations.livlyf.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apsinnovations.livlyf.R;
import com.apsinnovations.livlyf.models.Products;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyWishListAdapter extends RecyclerView.Adapter<MyWishListAdapter.MyWishListHolder> {
    Context context;
    int resource;
    ArrayList<Products> products;

    public MyWishListAdapter(Context context, int resource, ArrayList<Products> products) {
        this.context = context;
        this.resource = resource;
        this.products = products;
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
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyWishListHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, imgDelete;
        TextView txtName, txtPrice, txtMrp, txtDis;
        Button btnBuyNow;

        public MyWishListHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.wish_img_Product);
            imgDelete = itemView.findViewById(R.id.wish_imgDelete);
            txtName = itemView.findViewById(R.id.wish_product_name);
            txtPrice = itemView.findViewById(R.id.wish_product_price);
            txtMrp = itemView.findViewById(R.id.wish_product_mrp);
            txtDis = itemView.findViewById(R.id.wish_product_discount);

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteMyItemFromwWishList();
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
