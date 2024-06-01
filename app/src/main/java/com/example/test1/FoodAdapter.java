package com.example.test1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FoodAdapter extends ArrayAdapter<FoodItem> {

    private Context mContext;
    private List<FoodItem> mFoodList;

    public FoodAdapter(Context context, List<FoodItem> foodList) {
        super(context, 0, foodList);
        mContext = context;
        mFoodList = foodList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_food, parent, false);
        }

        FoodItem currentFood = mFoodList.get(position);

        ImageView imageViewFood = listItem.findViewById(R.id.imageViewFood);
        TextView textViewNameCategory = listItem.findViewById(R.id.textViewNameCategory);
        TextView textViewPrice = listItem.findViewById(R.id.textViewPrice);
        TextView textViewDescription = listItem.findViewById(R.id.textViewDescription);

        // Load and display image
        if (currentFood.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(currentFood.getImage(), 0, currentFood.getImage().length);
            imageViewFood.setImageBitmap(bitmap);
        } else {
            imageViewFood.setImageResource(R.drawable.no_image_icon); // Default image
        }

        textViewNameCategory.setText(currentFood.getName() + " - " + currentFood.getCategory());
        textViewPrice.setText(currentFood.getPrice() + " DT");
        textViewDescription.setText(currentFood.getDescription());

        return listItem;
    }
}

