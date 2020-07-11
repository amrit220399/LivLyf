package com.apsinnovations.livlyf.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apsinnovations.livlyf.R;
import com.apsinnovations.livlyf.models.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyOrderItemsAdapter extends RecyclerView.Adapter<MyOrderItemsAdapter.MyOrderItemsHolder> {
    Context context;
    int resource;
    ArrayList<Products> products;

    public MyOrderItemsAdapter(Context context, int resource, ArrayList<Products> products) {
        this.context = context;
        this.resource = resource;
        this.products = products;
    }

    @NonNull
    @Override
    public MyOrderItemsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyOrderItemsHolder(LayoutInflater.from(context).inflate(resource, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderItemsHolder holder, int position) {
        Picasso.get().load(products.get(position).getUrl()).fit().into(holder.imgProduct);
        holder.txtName.setText(products.get(position).getName());
        holder.txtPrice.setText("\u20B9".concat(String.valueOf(products.get(position).getPrice())));
        holder.txtMrp.setText("\u20B9".concat(String.valueOf(products.get(position).getMrp())));
        holder.txtMrp.setPaintFlags(holder.txtMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txtDis.setText("-".concat(String.valueOf(products.get(position).getDiscount())).concat("%"));
        holder.txtQty.setText(String.valueOf(products.get(position).getQty()));
        holder.txtShipping.setText("\u20B9".concat(String.valueOf(products.get(position).getShipping())));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyOrderItemsHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtPrice, txtMrp, txtDis, txtQty, txtShipping;

        public MyOrderItemsHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.item_img_Product);
            txtName = itemView.findViewById(R.id.item_product_name);
            txtPrice = itemView.findViewById(R.id.item_product_price);
            txtMrp = itemView.findViewById(R.id.item_product_mrp);
            txtDis = itemView.findViewById(R.id.item_product_discount);
            txtShipping = itemView.findViewById(R.id.item_txtShipping);
            txtQty = itemView.findViewById(R.id.item_txtQty);
        }
    }
}
