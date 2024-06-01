package com.example.test1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseUsersHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Users.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHOTO = "photo"; // Added for storing photo
    private static final String COLUMN_BIRTH_DATE = "birth_date"; // Added for storing birth date
    private static final String COLUMN_BIRTH_PLACE = "birth_place"; // Added for storing birth place

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_PHOTO + " BLOB," // BLOB for storing images
            + COLUMN_BIRTH_DATE + " TEXT,"
            + COLUMN_BIRTH_PLACE + " TEXT"
            + ")";

    public DatabaseUsersHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public long addUser(String username, String password, byte[] photo, String birthDate, String birthPlace) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_PHOTO, photo); // Added for storing photo
        values.put(COLUMN_BIRTH_DATE, birthDate); // Added for storing birth date
        values.put(COLUMN_BIRTH_PLACE, birthPlace); // Added for storing birth place
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public boolean checkUser(String username, String password) {
        String[] columns = { COLUMN_ID };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = { username, password };
        Cursor cursor = db.query(TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }
    public long getUserId(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { COLUMN_ID };
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = { username, password };
        Cursor cursor = db.query(TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        long userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
        }
        cursor.close();
        db.close();
        return userId;
    }
    public User getUserById(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                COLUMN_USERNAME,
                COLUMN_PASSWORD,
                COLUMN_PHOTO,
                COLUMN_BIRTH_DATE,
                COLUMN_BIRTH_PLACE
        };
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };
        Cursor cursor = db.query(
                TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_PHOTO));
            String birthDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTH_DATE));
            String birthPlace = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTH_PLACE));
            user = new User(username, password, photo, birthDate, birthPlace);
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return user;
    }
    public boolean removeUserById(long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };
        int deletedRows = db.delete(TABLE_USERS, selection, selectionArgs);
        db.close();
        return deletedRows > 0;
    }

    public boolean removeAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_USERS, null, null);
        db.close();
        return deletedRows > 0;
    }
    public boolean updateUser(long userId, String username, String password, byte[] photo, String birthDate, String birthPlace) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("photo", photo);
        values.put("birth_date", birthDate);
        values.put("birth_place", birthPlace);
        int rowsAffected = db.update("users", values, "id=?", new String[]{String.valueOf(userId)});
        return rowsAffected > 0;
    }
}