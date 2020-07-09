package com.apsinnovations.livlyf;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apsinnovations.livlyf.adapters.ProductsAdapter;
import com.apsinnovations.livlyf.models.Products;
import com.apsinnovations.livlyf.utils.MyCartListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class ProductsActivity extends AppCompatActivity implements MyCartListener {
    private static final String TAG = "ProductsActivity";
    RecyclerView recyclerView;
    ProductsAdapter productsAdapter;
    ArrayList<Products> products;
    int items;
    Menu menu;
//    boolean isFirstTymResume=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        getSupportActionBar().setElevation(0);
        recyclerView = findViewById(R.id.recyclerProducts);
        products = new ArrayList<>();
        setAdapter();
//        new MyAsyncTask().execute("");
//        fetchProducts();
    }

    private void fetchProducts() {
        if(!products.isEmpty()){
            products.clear();
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Objects.requireNonNull(getIntent().getStringExtra("name")))
                .whereEqualTo("category", getIntent().getStringExtra("category"))
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
//                    Products myproduct=snapshot.toObject(Products.class);
                    products.add(snapshot.toObject(Products.class));
                }
              setAdapter();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }
        });
    }

    private void getMyCartCount(){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .collection("myCart")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.i(TAG, "onSuccess: Cart SIZE"+queryDocumentSnapshots.size());
                items=queryDocumentSnapshots.size();
                updateCart();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
                updateCart();
            }
        });
    }
    void addProduct(Products products) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("plants")
                .add(products).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.i(TAG, "onSuccess: Updated");
                documentReference.update("ID", documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }
        });
    }

    private void setAdapter() {
        productsAdapter = new ProductsAdapter(this, R.layout.card_products, products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(productsAdapter);
    }

    @Override
    public void setItems(int itemsCount) {
        this.items += itemsCount;
        updateCart();
    }

    private void updateCart() {
        if(items>0) {
            final MenuItem item = menu.findItem(R.id.opt_cart);
            item.setActionView(R.layout.cart_notification_badge);
            View view = item.getActionView();
            TextView tv = view.findViewById(R.id.actionbar_notifcation_textview);
            tv.setText(String.valueOf(items));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(item);
                }
            });
        }else{
            MenuItem item = menu.findItem(R.id.opt_cart);
            item.setActionView(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        this.menu = menu;
//        if (items > 0) {
//            final MenuItem item = menu.findItem(R.id.opt_cart);
//            item.setActionView(R.layout.cart_notification_badge);
//            View view = item.getActionView();
//            TextView tv = view.findViewById(R.id.actionbar_notifcation_textview);
//            tv.setText(String.valueOf(items));
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onOptionsItemSelected(item);
//                }
//            });
//        }

//        MenuItemCompat.setActionView(item, R.layout.cart_notification_badge);
//        RelativeLayout notifCount = (RelativeLayout)   MenuItemCompat.getActionView(item);
//        RelativeLayout badgeLayout = (RelativeLayout)    menu.findItem(R.id.opt_cart).getActionView();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.i(TAG, "onOptionsItemSelected: Reached");
        if (id == R.id.opt_cart) {
            Log.i(TAG, "onOptionsItemSelected: Cart Selected");
            Intent intent1 = new Intent(this, MyCartActivity.class);
            startActivity(intent1);
        }
        return true;
    }

    class MyAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            fetchProducts();
            getMyCartCount();
//            products.add(new Products("Portulaca, 9 O Clock (Any Color)",
//                    "Flowering Plants",
//                    "",
//                    239,
//                    499,
//                    52,
//                    79,
//                    52
//                    ));
//            products.add(new Products("Rose (Any Color)",
//                    "Flowering Plants",
//                    "",
//                    289,
//                    499,
//                    22,
//                    79,
//                    42
//            ));
//            products.add(new Products("Miniature Rose, Button Rose (white)",
//                    "Flowering Plants",
//                    "",
//                    299,
//                    699,
//                    12,
//                    89,
//                    57
//            ));
//            products.add(new Products("Stachytarpheta (Any Color)",
//                    "Flowering Plants",
//                    "",
//                    339,
//                    799,
//                    18,
//                    99,
//                    57
//            ));
//            products.add(new Products("Hydrangea macrophylla (Any Color)",
//                    "Flowering Plants",
//                    "",
//                    339,
//                    799,
//                    4,
//                    79,
//                    57
//            ));
//            for(int i=0;i<products.size();i++){
//                addProduct(products.get(i));
//            }
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        isFirstTymResume=false;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(!isFirstTymResume){
//            getMyCartCount();
//        }
        new MyAsyncTask().execute("");
    }

}