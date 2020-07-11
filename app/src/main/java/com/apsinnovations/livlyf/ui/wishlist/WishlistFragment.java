package com.apsinnovations.livlyf.ui.wishlist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apsinnovations.livlyf.R;
import com.apsinnovations.livlyf.adapters.MyWishListAdapter;
import com.apsinnovations.livlyf.models.Products;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class WishlistFragment extends Fragment {
    private static final String TAG = "WishlistFragment";
    RecyclerView recyclerWishList;
    MyWishListAdapter myWishListAdapter;
    ArrayList<Products> products;
    TextView txtEmptyWishList;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        recyclerWishList = view.findViewById(R.id.recyclerWishList);
        txtEmptyWishList = view.findViewById(R.id.txtEmptyWishList);
        progressBar = view.findViewById(R.id.wish_progress);
        products = new ArrayList<>();
        setMyAdapter();
        new MyAsyncTask().execute("");
        return view;
    }

    private void setMyAdapter() {
        myWishListAdapter = new MyWishListAdapter(getContext(), R.layout.card_wishlist, products);
        recyclerWishList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerWishList.setAdapter(myWishListAdapter);
    }

    private void getMyWishListFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("myWishList")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() == 0) {
                    progressBar.setVisibility(View.GONE);
                    txtEmptyWishList.setVisibility(View.VISIBLE);
                    return;
                }
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                    products.add(snapshot.toObject(Products.class));
                }
                progressBar.setVisibility(View.GONE);
                txtEmptyWishList.setVisibility(View.GONE);
                myWishListAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: " + e.getMessage());
            }
        });
    }

    class MyAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            getMyWishListFromFirebase();
            return null;
        }

    }
}