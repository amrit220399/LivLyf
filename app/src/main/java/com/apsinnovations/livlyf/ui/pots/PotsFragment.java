package com.apsinnovations.livlyf.ui.pots;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apsinnovations.livlyf.R;
import com.apsinnovations.livlyf.adapters.CategoriesAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;


public class PotsFragment extends Fragment {

    private static final String TAG = "PotsFragment";
    RecyclerView recyclerPots;
    ProgressBar progressBar;
    CategoriesAdapter categoriesAdapter;
    ArrayList<HashMap<String, String>> Names;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pots, container, false);
        recyclerPots = view.findViewById(R.id.recyclerPots);
        progressBar = view.findViewById(R.id.POF_progress);
        Names = new ArrayList<>();
//        setAdapter();
//        fetchCategories();
        new MyAsyncTask().execute("");
        return view;
    }


    private void setAdapter() {
        categoriesAdapter = new CategoriesAdapter(getContext(), R.layout.card_categories_two, Names, "pots");
        recyclerPots.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerPots.setAdapter(categoriesAdapter);
        progressBar.setVisibility(View.GONE);
    }

    private void fetchCategories() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("category")
                .document("Pots")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Names = (ArrayList<HashMap<String, String>>) documentSnapshot.get("Names");
                setAdapter();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }
        });
    }

    class MyAsyncTask extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {
            fetchCategories();
            return null;
        }

    }
}