package com.apsinnovations.livlyf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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

    private static final String TAG = "MyCartActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("MyCart");

//        products=cartPrefMananger.getCartItems();
        recyclerCart=findViewById(R.id.recyclerCart);
        products=new ArrayList<>();
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
                for(DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments()){
                    products.add(snapshot.toObject(Products.class));
                }
                myCartAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    void setMyCartAdapter(){
        myCartAdapter =new MyCartAdapter(this,R.layout.cart_mycart,products);
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