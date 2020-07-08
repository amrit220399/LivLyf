package com.apsinnovations.livlyf.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.apsinnovations.livlyf.R;
import com.apsinnovations.livlyf.models.Category;

import java.util.ArrayList;

public class HomeCategoriesAdapter extends RecyclerView.Adapter<HomeCategoriesAdapter.MyHomeCategoriesVH> {
    Context context;
    int resource;
    ArrayList<Category> categories;

    public HomeCategoriesAdapter(Context context, int resource) {
        this.context = context;
        this.resource = resource;
        this.categories = new ArrayList<>();
        addCategories();
    }

    private void addCategories() {
        this.categories.add(new Category(R.drawable.ic_plant, "Plants", R.color.colorTransPink));
        this.categories.add(new Category(R.drawable.ic_plant_pot, "Pots", R.color.colorTransYellow));
        this.categories.add(new Category(R.drawable.ic_sesame, "Seeds", R.color.colorTransOrange));
        this.categories.add(new Category(R.drawable.ic_pebble, "Pebbles", R.color.colorTransGreen));
        this.categories.add(new Category(R.drawable.ic_tool, "Tools", R.color.colorTransBlue));
        this.categories.add(new Category(R.drawable.ic_dwarf, "Decor", R.color.colorTransPurple));
    }

    @NonNull
    @Override
    public MyHomeCategoriesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHomeCategoriesVH(LayoutInflater.from(context).inflate(resource, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHomeCategoriesVH holder, int position) {
        holder.txtCategory.setText(categories.get(position).getName());
        holder.imgCategory.setImageResource(categories.get(position).getDrawable());
        holder.cardView.setCardBackgroundColor(context.getResources().getColor(categories.get(position).getColor()));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyHomeCategoriesVH extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imgCategory;
        TextView txtCategory;

        public MyHomeCategoriesVH(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardHomeCategory);
            imgCategory = itemView.findViewById(R.id.imgHomeCategory);
            txtCategory = itemView.findViewById(R.id.txtHomeCategory);
        }
    }
}
