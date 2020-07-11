package com.apsinnovations.livlyf.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apsinnovations.livlyf.R;
import com.apsinnovations.livlyf.adapters.MyOrderAdapter;
import com.apsinnovations.livlyf.models.OrderDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class OrdersFragment extends Fragment {
    private static final String TAG = "OrdersFragment";
    RecyclerView recyclerOrders;
    TextView txtNoOrders;
    ArrayList<OrderDetails> orderDetailsArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        recyclerOrders = view.findViewById(R.id.recyclerOrders);
        txtNoOrders = view.findViewById(R.id.txtNoOrders);
        orderDetailsArrayList = new ArrayList<>();
        fetchOrders();
        return view;
    }

    private void fetchOrders() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("myOrders")
                .orderBy("order.timestamp", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                    orderDetailsArrayList.add(snapshot.toObject(OrderDetails.class));
                }
                setAdapter();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void setAdapter() {
        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(getContext(), R.layout.card_orders, orderDetailsArrayList);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerOrders.setAdapter(myOrderAdapter);
    }
}