package com.example.test1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MenuDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Menu.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MenuContract.FoodEntry.TABLE_NAME + " (" +
                    MenuContract.FoodEntry._ID + " INTEGER PRIMARY KEY," +
                    MenuContract.FoodEntry.COLUMN_NAME + " TEXT," +
                    MenuContract.FoodEntry.COLUMN_CATEGORY + " TEXT," +
                    MenuContract.FoodEntry.COLUMN_PRICE + " REAL," +
                    MenuContract.FoodEntry.COLUMN_DESCRIPTION + " TEXT," +
                    MenuContract.FoodEntry.COLUMN_IMAGE + " BLOB)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MenuContract.FoodEntry.TABLE_NAME;

    public MenuDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public long insertFood(String name, String category, double price, String description, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MenuContract.FoodEntry.COLUMN_NAME, name);
        values.put(MenuContract.FoodEntry.COLUMN_CATEGORY, category);
        values.put(MenuContract.FoodEntry.COLUMN_PRICE, price);
        values.put(MenuContract.FoodEntry.COLUMN_DESCRIPTION, description);
        values.put(MenuContract.FoodEntry.COLUMN_IMAGE, image); // Assuming image is a byte array

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(MenuContract.FoodEntry.TABLE_NAME, null, values);

        db.close(); // Close the database connection

        return newRowId;
    }
    public Cursor getAllFoods() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                MenuContract.FoodEntry.COLUMN_NAME,
                MenuContract.FoodEntry.COLUMN_CATEGORY,
                MenuContract.FoodEntry.COLUMN_PRICE,
                MenuContract.FoodEntry.COLUMN_DESCRIPTION,
                MenuContract.FoodEntry.COLUMN_IMAGE
        };

        Cursor cursor = db.query(
                MenuContract.FoodEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        return cursor;
    }
    public void deleteFood(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = MenuContract.FoodEntry.COLUMN_NAME + " = ?";
        String[] selectionArgs = { name };
        db.delete(MenuContract.FoodEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    // Retrieve items from the database based on the category
    public List<FoodItem> getItemsByCategory(String category) {
        List<FoodItem> foodItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                MenuContract.FoodEntry._ID,
                MenuContract.FoodEntry.COLUMN_NAME,
                MenuContract.FoodEntry.COLUMN_CATEGORY,
                MenuContract.FoodEntry.COLUMN_PRICE,
                MenuContract.FoodEntry.COLUMN_DESCRIPTION,
                MenuContract.FoodEntry.COLUMN_IMAGE
        };
        String selection = MenuContract.FoodEntry.COLUMN_CATEGORY + " = ?";
        String[] selectionArgs = null;

        // Check if category is not null before setting selectionArgs
        if (category != null) {
            selectionArgs = new String[]{category};
        }

        Cursor cursor = db.query(
                MenuContract.FoodEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Extract food item data from the cursor and create FoodItem objects
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_PRICE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_DESCRIPTION));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_IMAGE));

                // Create and add FoodItem object to the list
                FoodItem foodItem = new FoodItem(id ,name, category, price, description, image);
                foodItemList.add(foodItem);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return foodItemList;
    }

    public List<FoodItem> getAllItems() {
        List<FoodItem> foodItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                MenuContract.FoodEntry._ID,
                MenuContract.FoodEntry.COLUMN_NAME,
                MenuContract.FoodEntry.COLUMN_CATEGORY,
                MenuContract.FoodEntry.COLUMN_PRICE,
                MenuContract.FoodEntry.COLUMN_DESCRIPTION,
                MenuContract.FoodEntry.COLUMN_IMAGE
        };

        Cursor cursor = db.query(
                MenuContract.FoodEntry.TABLE_NAME,
                projection,
                null, // No selection, retrieve all items
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Extract food item data from the cursor and create FoodItem objects
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_NAME));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_CATEGORY));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_PRICE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_DESCRIPTION));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_IMAGE));

                // Create and add FoodItem object to the list
                FoodItem foodItem = new FoodItem(id, name, category, price, description, image);
                foodItemList.add(foodItem);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return foodItemList;
    }

    public FoodItem getItemById(long itemId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Prepare the query to retrieve the item details based on its ID
        String[] projection = {
                MenuContract.FoodEntry._ID,
                MenuContract.FoodEntry.COLUMN_NAME,
                MenuContract.FoodEntry.COLUMN_CATEGORY,
                MenuContract.FoodEntry.COLUMN_PRICE,
                MenuContract.FoodEntry.COLUMN_DESCRIPTION,
                MenuContract.FoodEntry.COLUMN_IMAGE
        };
        String selection = MenuContract.FoodEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(itemId)};

        Cursor cursor = db.query(
                MenuContract.FoodEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        FoodItem foodItem = null;
        if (cursor != null && cursor.moveToFirst()) {
            // Extract item details from the cursor
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_NAME));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_CATEGORY));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_PRICE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_DESCRIPTION));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(MenuContract.FoodEntry.COLUMN_IMAGE));

            // Create a FoodItem object
            foodItem = new FoodItem(id ,name, category, price, description, image);

            cursor.close();
        }

        return foodItem;
    }
}



