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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartHolder> {
    Context context;
    int resource;
    ArrayList<Products> products;
    TextView txtAmt, txtShip, txtTax;
    Button btnPay;

    private static final String TAG = "MyCartAdapter";

    public MyCartAdapter(Context context, int resource, ArrayList<Products> products, TextView txtAmt, TextView txtShip, TextView txtTax, Button btnPay) {
        this.context = context;
        this.resource = resource;
        this.products = products;
        this.txtAmt = txtAmt;
        this.txtShip = txtShip;
        this.txtTax = txtTax;
        this.btnPay = btnPay;
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
        holder.txtShipping.setText("\u20B9".concat(String.valueOf(products.get(position).getShipping())));
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
        ImageView imgProduct, imgDelete;
        TextView txtName, txtPrice, txtMrp, txtDis, txtQty, txtShipping;
        Button btnBuyNow;

        public MyCartHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.cart_img_Product);
            imgDelete = itemView.findViewById(R.id.imgDeleteCart);
            txtName = itemView.findViewById(R.id.cart_product_name);
            txtPrice = itemView.findViewById(R.id.cart_product_price);
            txtMrp = itemView.findViewById(R.id.cart_product_mrp);
            txtDis = itemView.findViewById(R.id.cart_product_discount);
            txtShipping = itemView.findViewById(R.id.cart_txtShipping);
            txtQty = itemView.findViewById(R.id.cart_txtQty);
//            btnBuyNow=itemView.findViewById(R.id.btnBuyNow);

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteMyItemFromCart();
                }
            });
        }

        private void deleteMyItemFromCart(){
            FirebaseFirestore db=FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("myCart")
                    .document(products.get(getLayoutPosition()).getID())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            products.remove(getLayoutPosition());
                            notifyItemRemoved(getLayoutPosition());
                            updateTransaction();
                        }
                    });
        }

        private void updateTransaction() {
            double amt = 0, ship = 0, total = 0;
            float tax = 0;
            for (Products myProduct : products) {
                amt += myProduct.getPrice() * myProduct.getQty();
                ship += myProduct.getShipping();
            }
            total = amt + ship;
            tax = (float) (0.05 * total);
            txtAmt.setText("\u20B9".concat(String.valueOf(amt)));
            txtShip.setText("\u20B9".concat(String.valueOf(ship)));
            txtTax.setText("\u20B9".concat(String.valueOf(tax)));
            btnPay.setText("Pay ".concat("\u20B9").concat(String.valueOf(total)));
        }
    }
}
