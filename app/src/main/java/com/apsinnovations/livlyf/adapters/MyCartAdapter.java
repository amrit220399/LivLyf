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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apsinnovations.livlyf.R;
import com.apsinnovations.livlyf.models.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartHolder> {
    Context context;
    int resource;
    ArrayList<Products> products;

    private static final String TAG = "MyCartAdapter";
    public MyCartAdapter(Context context, int resource,ArrayList<Products> products) {
        this.context = context;
        this.resource = resource;
        this.products=products;
    }

    @NonNull
    @Override
    public MyCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCartHolder(LayoutInflater.from(context).inflate(resource,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartHolder holder, int position) {
        Picasso.get().load(products.get(position).getUrl()).fit().into(holder.imgProduct);
        holder.txtName.setText(products.get(position).getName());
        holder.txtPrice.setText("\u20B9".concat(String.valueOf(products.get(position).getPrice())));
        holder.txtMrp.setText("\u20B9".concat(String.valueOf(products.get(position).getMrp())));
        holder.txtMrp.setPaintFlags(holder.txtMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txtDis.setText("-".concat(String.valueOf(products.get(position).getDiscount())).concat("%"));
        holder.txtQty.setText(String.valueOf(products.get(position).getQty()));
    }

    @Override
    public int getItemCount() {
        if(products==null){
            Log.i(TAG, "getItemCount: NULL ");
            return 0;
        }else{
            return products.size();
        }
    }

    public class MyCartHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct,imgDelete;
        TextView txtName,txtPrice,txtMrp,txtDis,txtQty;
        Button btnBuyNow;

        public MyCartHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct=itemView.findViewById(R.id.cart_img_Product);
            imgDelete=itemView.findViewById(R.id.imgDeleteCart);
            txtName=itemView.findViewById(R.id.cart_product_name);
            txtPrice=itemView.findViewById(R.id.cart_product_price);
            txtMrp=itemView.findViewById(R.id.cart_product_mrp);
            txtDis=itemView.findViewById(R.id.cart_product_discount);
            txtQty=itemView.findViewById(R.id.cart_txtQty);
            btnBuyNow=itemView.findViewById(R.id.btnBuyNow);
        }

    }
}
