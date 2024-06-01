package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.view.MenuItem;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class SubcategoryActivity extends AppCompatActivity {

    private GridLayout gridLayoutItems;
    private TextView textTitle;
    private MenuDbHelper dbHelper;
    private BadgeDrawable badgeDrawable;
    private List<CartItem> cartItemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ShoppingCart cart = ShoppingCart.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);
        cartItemList = ShoppingCart.getInstance().items;
        // Inside your main activity onCreate method or wherever you handle navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
// Create a BadgeDrawable
        badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.Cart);
// Set the number you want to display
        badgeDrawable.setNumber(ShoppingCart.getInstance().getShoppingCartSize()); // Example: Displayed number
// Optionally, set a background color for the badge
        badgeDrawable.setBackgroundColor(ContextCompat.getColor(this, R.color.white));


        // Initialize views
        gridLayoutItems = findViewById(R.id.gridLayoutItems);

        textTitle = findViewById(R.id.textView);
        // Initialize database helper
        dbHelper = new MenuDbHelper(this);

        // Retrieve selected category from intent
        String selectedCategory = getIntent().getStringExtra("categoryName");

        textTitle.setText(selectedCategory+":");

        bottomNavigationView.setSelectedItemId(R.id.NoItem);

        // Retrieve items from database based on selected category
        List<FoodItem> itemList = dbHelper.getItemsByCategory(selectedCategory);

        // Check if itemList is not empty
        if (itemList != null && !itemList.isEmpty()) {
            // Add CardViews for each item to the GridLayout
            for (int i = 0; i < itemList.size(); i++) {
                addCardView(itemList.get(i), i / 2, i % 2);
            }
        } else {
            // Display a message indicating no items found
            Toast.makeText(this, "No items found for the selected category", Toast.LENGTH_SHORT).show();
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = new Intent();
                Fragment selectedFragment = null;
                if (item.getItemId()>0) {
                    intent.putExtra("selectedItemId", item.getItemId()); // Change this to the appropriate ID
                    setResult(RESULT_OK, intent);
                    finish(); // Finish the subcategory activity and return to the main activity
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    private void addCardView(FoodItem foodItem, int row, int column) {
        // Create a new CardView
        CardView cardView = new CardView(this);
        cardView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        // Set the background color to transparent
        cardView.setCardBackgroundColor(Color.TRANSPARENT);

        cardView.setTag(foodItem.getId());


        cardView.setCardElevation(8);
        cardView.setContentPadding(16, 16, 16, 16); // Add padding to CardView contents

        // Inflate your card view layout
        View cardLayout = LayoutInflater.from(this).inflate(R.layout.card_food_item, null);

        // Customize CardView contents with data from FoodItem
        TextView textViewName = cardLayout.findViewById(R.id.textViewName);
        //TextView textViewCategory = cardLayout.findViewById(R.id.textViewCategory);
        TextView textViewPrice = cardLayout.findViewById(R.id.textViewPrice);
        TextView textViewDescription = cardLayout.findViewById(R.id.textViewDescription);
        ImageView imageViewFood = cardLayout.findViewById(R.id.imageViewFood);

        textViewName.setText(foodItem.getName());
        //textViewCategory.setText(foodItem.getCategory());
        textViewPrice.setText(String.valueOf(foodItem.getPrice())+" DT");
        textViewDescription.setText(foodItem.getDescription());
        // Set image bitmap or byte array to imageViewFood
        if (foodItem.getImage() != null) {
            // If you have a byte array representation of the image
            Bitmap bitmap = BitmapFactory.decodeByteArray(foodItem.getImage(), 0, foodItem.getImage().length);
            imageViewFood.setImageBitmap(bitmap);
        }

        // Calculate end row index based on start row and span
        int endRow = row + 1; // Assuming each card occupies one row

        // Ensure the GridLayout's row count is sufficient
        if (gridLayoutItems.getRowCount() <= endRow) {
            gridLayoutItems.setRowCount(endRow + 1);
        }

        // Create GridLayout.LayoutParams with columnSpec and rowSpec
        GridLayout.Spec columnSpec = GridLayout.spec(column); // Start column
        GridLayout.Spec rowSpec = GridLayout.spec(row, 1, 1f); // Start row, span with weight
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
        params.width = 0; // Set width to 0 to evenly distribute across columns
        params.rowSpec = GridLayout.spec(row, 1, 1f); // Span rows with weight
        params.columnSpec = GridLayout.spec(column, 1, 1f); // Span column with weight
        cardView.setLayoutParams(params);

        // Add the inflated card layout to the CardView
        cardView.addView(cardLayout);

        // Add the CardView to the GridLayout
        gridLayoutItems.addView(cardView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long itemId = (long) v.getTag();


                // Add the item to the shopping cart
                ShoppingCart.getInstance().addToShoppingCart(itemId);

                badgeDrawable.setNumber(cartItemList.size()); // Example: Displayed number
                // Optionally, update the UI to reflect the addition
                // (e.g., show a toast message)
                Toast.makeText(SubcategoryActivity.this, "Item added to cart" + itemId , Toast.LENGTH_SHORT).show();
            }
        });


    }


}

