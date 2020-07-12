package com.apsinnovations.livlyf.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apsinnovations.livlyf.ProductsActivity;
import com.apsinnovations.livlyf.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "PlantCategoriesAdapter";
    private static int TYPE_ONE = 1;
    private static int TYPE_TWO = 2;
    Context context;
    int resource;
    ArrayList<HashMap<String, String>> categories;
    String colName;

    public CategoriesAdapter(Context context, int resource, ArrayList<HashMap<String, String>> categories, String colName) {
        this.context = context;
        this.resource = resource;
        this.categories = categories;
        this.colName = colName;
    }

    @Override
    public int getItemViewType(int position) {
        if (colName.equals("tools")) {
            return TYPE_ONE;
        } else {
            return TYPE_TWO;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(resource, parent, false);
        if (viewType == TYPE_ONE) {
            return new MyPlantsCategoriesHolder(view);
        } else {
            return new MyCategoriesHolderTwo(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        Categories mycategory=(Categories) categories.get(position);

        if (holder instanceof MyPlantsCategoriesHolder) {
            Glide.with(holder.itemView)
                    .load(categories.get(position).get("url"))
                    .fitCenter()
                    .into(((MyPlantsCategoriesHolder) holder).imgPlants);
//            Picasso.get().load(categories.get(position).get("url")).fit()
//                    .into(((MyPlantsCategoriesHolder)holder).imgPlants);
            ((MyPlantsCategoriesHolder) holder).txtName.setText(categories.get(position).get("name"));
        } else {
            Glide.with(holder.itemView)
                    .load(categories.get(position).get("url"))
                    .fitCenter()
                    .into(((MyCategoriesHolderTwo) holder).imgCategory);
//            Picasso.get().load(categories.get(position).get("url")).fit()
//                    .into(((MyCategoriesHolderTwo)holder).imgCategory);
            ((MyCategoriesHolderTwo) holder).txtName.setText(categories.get(position).get("name"));
        }

    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: " + categories.size());
        return categories.size();
    }

    class MyPlantsCategoriesHolder extends RecyclerView.ViewHolder {
        ImageView imgPlants;
        TextView txtName;
        View itemView;

        public MyPlantsCategoriesHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductsActivity.class);
                    intent.putExtra("name", colName);
                    intent.putExtra("category", categories.get(getLayoutPosition()).get("name"));
                    context.startActivity(intent);
                }
            });

            imgPlants = itemView.findViewById(R.id.CPC_imgPlants);
            txtName = itemView.findViewById(R.id.CPC_txtName);
        }
    }

    class MyCategoriesHolderTwo extends RecyclerView.ViewHolder {
        ImageView imgCategory;
        TextView txtName;
        View itemView;

        public MyCategoriesHolderTwo(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductsActivity.class);
                    intent.putExtra("name", colName);
                    intent.putExtra("category", categories.get(getLayoutPosition()).get("name"));
                    context.startActivity(intent);
                }
            });

            imgCategory = itemView.findViewById(R.id.CCT_imgCategory);
            txtName = itemView.findViewById(R.id.CCT_txtName);
        }
    }
}
