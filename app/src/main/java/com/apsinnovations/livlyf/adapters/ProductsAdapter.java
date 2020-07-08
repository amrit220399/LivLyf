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
import com.apsinnovations.livlyf.utils.CartPrefMananger;
import com.apsinnovations.livlyf.utils.MyCartListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyProductsHolder> {
    Context context;
    int resource;
    ArrayList<Products> products;
    MyCartListener myCartListener;
    CartPrefMananger cartPrefMananger;

    public ProductsAdapter(Context context, int resource, ArrayList<Products> products) {
        this.context = context;
        this.resource = resource;
        this.products = products;
        try {
            myCartListener = (MyCartListener) context;
        } catch (ClassCastException e) {
            Log.i("Exception Caught", "" + e.getMessage());
        }
        cartPrefMananger = new CartPrefMananger(context);
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
        holder.txtDisocunt.setText("-".concat(String.valueOf(products.get(position).getDiscount())).concat("%"));
        holder.quantityPicker.setMaxQuantity(products.get(position).getQty());
        holder.quantityPicker.setMinQuantity(1);
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
                        myCartListener.setItems(quantityPicker.getQuantity());
                        cartPrefMananger.setItemsCount(quantityPicker.getQuantity());
                    } else {
                        Toast.makeText(context, "Items Can't Be 0!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
