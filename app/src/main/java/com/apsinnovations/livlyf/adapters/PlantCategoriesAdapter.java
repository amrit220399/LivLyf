package com.apsinnovations.livlyf.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apsinnovations.livlyf.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class PlantCategoriesAdapter extends RecyclerView.Adapter<PlantCategoriesAdapter.MyPlantsCategoriesHolder> {
    private static final String TAG = "PlantCategoriesAdapter";
    Context context;
    int resource;
    ArrayList<HashMap<String, String>> categories;

    public PlantCategoriesAdapter(Context context, int resource, ArrayList<HashMap<String, String>> categories) {
        this.context = context;
        this.resource = resource;
        this.categories = categories;
    }

    @NonNull
    @Override
    public PlantCategoriesAdapter.MyPlantsCategoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        return new MyPlantsCategoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantCategoriesAdapter.MyPlantsCategoriesHolder holder, int position) {
//        Categories mycategory=(Categories) categories.get(position);

        Picasso.get().load(categories.get(position).get("url")).fit().into(holder.imgPlants);
        holder.txtName.setText(categories.get(position).get("name"));
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: " + categories.size());
        return categories.size();
    }

    static class MyPlantsCategoriesHolder extends RecyclerView.ViewHolder {
        ImageView imgPlants;
        TextView txtName;

        public MyPlantsCategoriesHolder(@NonNull View itemView) {
            super(itemView);
            imgPlants = itemView.findViewById(R.id.CPC_imgPlants);
            txtName = itemView.findViewById(R.id.CPC_txtName);
        }
    }
}
