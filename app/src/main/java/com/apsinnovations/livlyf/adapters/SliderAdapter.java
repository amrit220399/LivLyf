package com.apsinnovations.livlyf.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apsinnovations.livlyf.R;
import com.apsinnovations.livlyf.models.SliderItem;
import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.MySliderHolder> {
    Context context;
    private ArrayList<SliderItem> mSliderItems;

    public SliderAdapter(Context context) {
        this.context = context;
        this.mSliderItems = new ArrayList<>();
        addItem();
    }


    public void addItem() {
        this.mSliderItems.add(new SliderItem("Fight COVID with Medicinal Plants",
                "https://firebasestorage.googleapis.com/v0/b/livlyf.appspot.com/o/slider%2Fbowl_of_herbal_plants_to_highdefinition_picture_169115.jpg?alt=media&token=3e19d70c-67c3-4219-9d2d-2e8001ff9bac"));
        this.mSliderItems.add(new SliderItem("Air Purifying Plants",
                "https://firebasestorage.googleapis.com/v0/b/livlyf.appspot.com/o/slider%2FIndoor-Air-Purifying-Plants-India.jpg?alt=media&token=39ecd9dd-ee52-4ae5-9561-80117431ba41"));
        this.mSliderItems.add(new SliderItem("Fight Pollution with Plants",
                "https://firebasestorage.googleapis.com/v0/b/livlyf.appspot.com/o/slider%2Fcover-photo.jpg?alt=media&token=af22a398-ea71-438a-bd8e-ee3a2a5f0d4d"));
        this.mSliderItems.add(new SliderItem("Decor",
                "https://firebasestorage.googleapis.com/v0/b/livlyf.appspot.com/o/slider%2Fdecor.jpg?alt=media&token=5086e544-4cfb-42cd-8558-676d1c1eb722"));
        this.mSliderItems.add(new SliderItem("Urban Gardening",
                "https://firebasestorage.googleapis.com/v0/b/livlyf.appspot.com/o/slider%2Furban_gardening.png?alt=media&token=9fc04d41-622d-4df7-b7ce-1625c76aa4b4"));
        notifyDataSetChanged();
    }

    @Override
    public MySliderHolder onCreateViewHolder(ViewGroup parent) {
        return new MySliderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MySliderHolder viewHolder, final int position) {
        SliderItem sliderItem = mSliderItems.get(position);

        viewHolder.textViewDescription.setText(sliderItem.getDescription());
        viewHolder.textViewDescription.setTextSize(16);
        viewHolder.textViewDescription.setTextColor(Color.WHITE);
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImageUrl())
                .fitCenter()
                .into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    public class MySliderHolder extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public MySliderHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}
