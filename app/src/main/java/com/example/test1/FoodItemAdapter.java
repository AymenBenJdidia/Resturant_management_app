package com.example.test1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder> {

    private List<FoodItem> foodItems;
    private OnItemClickListener mListener; // Define the listener variable


    public void setFoodItems(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);
        // Example: Setting name and description text
        holder.itemNameTextView.setText(foodItem.getName());
        holder.itemCategoryTextView.setText(foodItem.getCategory());
        holder.itemPriceTextView.setText(String.valueOf(foodItem.getPrice())+ " DT");
        holder.itemDescriptionTextView.setText(foodItem.getDescription());
        // Load and display image
        if (foodItem.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(foodItem.getImage(), 0, foodItem.getImage().length);
            holder.itemImageView.setImageBitmap(bitmap);
        } else {
            holder.itemImageView.setImageResource(R.drawable.no_image_icon); // Default image
        }

    }

    @Override
    public int getItemCount() {
        return foodItems != null ? foodItems.size() : 0;
    }

    public static class FoodItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        TextView itemCategoryTextView;
        TextView itemPriceTextView;
        TextView itemDescriptionTextView;
        ImageView itemImageView;
        // Add other views as needed

        public FoodItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize ViewHolder components
            itemNameTextView = itemView.findViewById(R.id.textViewNameCategory);
            itemCategoryTextView = itemView.findViewById(R.id.textViewCategory);
            itemDescriptionTextView = itemView.findViewById(R.id.textViewDescription);
            itemPriceTextView = itemView.findViewById(R.id.textViewPrice);// Corrected ID
            itemImageView = itemView.findViewById(R.id.imageViewFood);
            // Initialize other views as needed
        }
    }
    // Define the interface
    public interface OnItemClickListener {
        void onItemClick(long itemId);
    }

    // Method to set the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
