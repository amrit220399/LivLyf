package com.apsinnovations.livlyf;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.apsinnovations.livlyf.models.Address;
import com.apsinnovations.livlyf.models.Order;
import com.apsinnovations.livlyf.models.Products;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class AddressActivity extends AppCompatActivity {
    private static final String TAG = "AddressActivity";
    TextInputEditText txtName, txtAdd1, txtAdd2, txtLandMark, txtMobile, txtPinCode;
    Button btnPay;
    Order order;
    Address address;
    HashMap<String, Object> hashMap;
    FirebaseFirestore db;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        getSupportActionBar().setTitle("Choose Address");
        getSupportActionBar().setElevation(0);
        txtName = findViewById(R.id.AA_name);
        txtAdd1 = findViewById(R.id.AA_Address1);
        txtAdd2 = findViewById(R.id.AA_Address2);
        txtLandMark = findViewById(R.id.AA_Landmark);
        txtMobile = findViewById(R.id.AA_mobile);
        txtPinCode = findViewById(R.id.AA_PinCode);
        btnPay = findViewById(R.id.AA_btnPay);
        hashMap = new HashMap<>();
        Intent rcv = getIntent();
        double orderVal = rcv.getDoubleExtra("orderVal", 0);
        ArrayList<Products> items = (ArrayList<Products>) rcv.getSerializableExtra("items");
        order = new Order(getRandomString(), new Timestamp(Calendar.getInstance().getTime()), items, orderVal, 1);
        db = FirebaseFirestore.getInstance();
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        btnPay.setText("Pay ".concat("\u20B9").concat(String.valueOf(orderVal)));
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddressDetails();
            }
        });
    }

    private void getAddressDetails() {
        String name = txtName.getText().toString().trim();
        String add1 = txtAdd1.getText().toString().trim();
        String add2 = txtAdd2.getText().toString().trim();
        String land = txtLandMark.getText().toString().trim();
        String pinCode = txtPinCode.getText().toString().trim();
        String mobile = txtMobile.getText().toString().trim();
        if (name.isEmpty() || add1.isEmpty() || add2.isEmpty() || land.isEmpty() || pinCode.isEmpty() || mobile.isEmpty() || mobile.length() < 10) {
            Toast.makeText(this, "Invalid Details", Toast.LENGTH_SHORT).show();
        } else {
            address = new Address(name, add1, add2, land, pinCode, mobile);
            hashMap.put("order", order);
            hashMap.put("address", address);
            new MyAsyncTask().execute("");
        }
    }

    private void placeOrder(HashMap<String, Object> hashMap) {

        db.collection("users")
                .document(uid)
                .collection("myOrders")
                .document(order.getOrderID())
                .set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "onSuccess: ORDERED");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: " + e.getMessage());
            }
        });

        hashMap.put("uid", uid);
        db.collection("orders")
                .document(order.getOrderID())
                .set(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "onSuccess: ORDER PLACED");
                        emptyMyCart();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: " + e.getMessage());
            }
        });
    }

    private void emptyMyCart() {
        db.collection("users")
                .document(uid)
                .collection("myCart")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                    snapshot.getReference().delete();
                }
                Intent intent = new Intent(AddressActivity.this, MainActivity.class);
                intent.putExtra("order", "success");
                startActivity(intent);
                finishAffinity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: " + e.getMessage());
            }
        });
    }

    private String getRandomString() {
        final String ALLOWED_CHARACTERS = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(14);
        for (int i = 0; i < 14; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }


    class MyAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            placeOrder(hashMap);
            return null;
        }
    }

}