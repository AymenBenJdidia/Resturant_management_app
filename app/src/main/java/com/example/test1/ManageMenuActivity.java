package com.example.test1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ManageMenuActivity extends AppCompatActivity {
    private EditText editTextName, editTextPrice, editTextDescription;
    private Spinner categorySpinner;
    private Button buttonAdd;
    private ListView listViewFoods;
    private FoodAdapter adapter;
    private List<FoodItem> foodItems;

    private MenuDbHelper dbHelper;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri; // Variable to hold the URI of the selected image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        categorySpinner = findViewById(R.id.spinnerCategory);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonAdd = findViewById(R.id.buttonAdd);
        listViewFoods = findViewById(R.id.listViewFoods);

        // Initialize ArrayList for food items
        foodItems = new ArrayList<>();

        // Initialize adapter for the ListView
        adapter = new FoodAdapter(this, foodItems);
        listViewFoods.setAdapter(adapter);

        // Initialize database helper
        dbHelper = new MenuDbHelper(this);

        // Add food button click listener
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood();
            }
        });

        // Item click listener for ListView (delete item)
        listViewFoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteFood(position);
            }
        });
        loadFoods();
    }

    private void addFood() {
        // Get input values
        String name = editTextName.getText().toString().trim();
        String category = ((Spinner) findViewById(R.id.spinnerCategory)).getSelectedItem().toString();
        String priceStr = editTextPrice.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        // Validate input
        if (name.isEmpty() || priceStr.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate price format
        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) {
                editTextPrice.setError("Price cannot be negative");
                return;
            }
        } catch (NumberFormatException e) {
            editTextPrice.setError("Invalid price format");
            return;
        }

        // Convert image to byte array
        byte[] imageData = null;
        if (selectedImageUri != null) {
            try {
                imageData = getBytesFromUri(selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to read image", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Add food item to database
        long newRowId = dbHelper.insertFood(name, category, price, description, imageData);

        if (newRowId == -1) {
            Toast.makeText(this, "Failed to add food to database", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reload data from database
        loadFoods();

        // Clear input fields
        editTextName.setText("");
        editTextPrice.setText("");
        editTextDescription.setText("");
//        ImageView imageViewPreview = findViewById(R.id.imageViewPreview);
//        imageViewPreview.setImageResource(R.drawable.no_image_icon);

        Toast.makeText(this, "Food added successfully", Toast.LENGTH_SHORT).show();
    }

    private void deleteFood(int position) {
        // Get the name of the food item to be deleted
        String name = foodItems.get(position).getName();

        // Delete the food item from the database
        dbHelper.deleteFood(name);

        // Reload data from database
        loadFoods();

        Toast.makeText(this, "Food deleted successfully", Toast.LENGTH_SHORT).show();
    }

    private void loadFoods() {
        foodItems.clear(); // Clear the list before loading new data

        Cursor cursor = dbHelper.getAllFoods();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_NAME));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_CATEGORY));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_PRICE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_DESCRIPTION));
                byte[] imageData = cursor.getBlob(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_IMAGE));

                // Add food item to list
                foodItems.add(new FoodItem(name, category, price, description, imageData));
            } while (cursor.moveToNext());

            cursor.close();
        }

        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();
    }

    // Launch the image picker
    public void launchImagePicker(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of the image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            // Load selected image into the preview ImageView
            try {
                Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                int desiredWidth = 200; // Desired width for the resized image
                int desiredHeight = 200; // Desired height for the resized image
                Bitmap bitmap = Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, true);

                // Convert resized image to byte array
                ImageView imageViewPreview = findViewById(R.id.imageViewPreview);
                imageViewPreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Convert image URI to byte array
    private byte[] getBytesFromUri(Uri uri) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return outputStream.toByteArray();
    }
}
