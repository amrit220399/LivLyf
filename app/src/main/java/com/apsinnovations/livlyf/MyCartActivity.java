package com.apsinnovations.livlyf;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apsinnovations.livlyf.adapters.MyCartAdapter;
import com.apsinnovations.livlyf.models.Products;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyCartActivity extends AppCompatActivity {
    RecyclerView recyclerCart;
    MyCartAdapter myCartAdapter;
    ArrayList<Products> products;
    TextView txtAmount, txtTaxes, txtShipping;
    Button btnPay;
    double amount = 0, shipping = 0;
    float taxes = 0;
    private static final String TAG = "MyCartActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("MyCart");

//        products=cartPrefMananger.getCartItems();
        recyclerCart = findViewById(R.id.recyclerCart);
        txtAmount = findViewById(R.id.txtTaxAmount);
        txtShipping = findViewById(R.id.txtTotalShipping);
        txtTaxes = findViewById(R.id.Taxes);
        btnPay = findViewById(R.id.btnPay);

        products = new ArrayList<>();
        setMyCartAdapter();

        new MyAsyncTask().execute("");
    }
    private void getMyCartFromFirebase(){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("myCart")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                    Products myProduct = snapshot.toObject(Products.class);
                    assert myProduct != null;
                    amount += myProduct.getPrice() * myProduct.getQty();
                    shipping += myProduct.getShipping();
                    products.add(myProduct);
                }
                myCartAdapter.notifyDataSetChanged();
                txtAmount.setText("\u20B9".concat(String.valueOf(amount)));
                txtShipping.setText("\u20B9".concat(String.valueOf(shipping)));
                double total = amount + shipping;
                taxes = (float) (0.05 * total);
                total += taxes;
                txtTaxes.setText("\u20B9".concat(String.valueOf(taxes)));
                btnPay.setText("Pay ".concat("\u20B9").concat(String.valueOf(total)));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    void setMyCartAdapter(){
        myCartAdapter = new MyCartAdapter(this, R.layout.card_mycart, products, txtAmount, txtShipping, txtTaxes, btnPay);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerCart.setHasFixedSize(true);
        recyclerCart.setAdapter(myCartAdapter);
    }
    class MyAsyncTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
           getMyCartFromFirebase();
            return null;
        }

    }
}