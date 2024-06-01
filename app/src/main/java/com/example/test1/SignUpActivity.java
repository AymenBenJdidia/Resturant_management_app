package com.example.test1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.test1.DatabaseUsersHelper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextBirthDate;
    private Spinner spinnerBirthPlace;
    private ImageView imageViewProfile;
    private Button buttonSignUp;
    private DatabaseUsersHelper dbHelper;
    private long userId;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new DatabaseUsersHelper(this);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextBirthDate = findViewById(R.id.editTextBirthDate);
        spinnerBirthPlace = findViewById(R.id.spinnerBirthPlace);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        // Populate the spinner with countries
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.countries_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBirthPlace.setAdapter(adapter);

        editTextBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        editTextBirthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    showDatePickerDialog();
                }
            }
        });

        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId != -1) {
                    // User ID is available, update user details
                    updateUser();
                } else {
                    // User ID is not available, register a new user
                    registerUser();
                }
            }
        });

        // Retrieve user's information from intent extras
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getLongExtra("userId", -1);
            if (userId != -1) {
                // User ID is valid, fetch user's details from the database and prepopulate the fields
                User user = dbHelper.getUserById(userId);
                if (user != null) {
                    editTextUsername.setText(user.getUsername());
                    editTextPassword.setText(user.getPassword());
                    editTextBirthDate.setText(user.getBirthDate());
                    // Set spinner selection to user's birth place
                    int index = adapter.getPosition(user.getBirthPlace());
                    spinnerBirthPlace.setSelection(index);
                    // Set user's profile image
                    if (user.getPhoto() != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(user.getPhoto(), 0, user.getPhoto().length);
                        imageViewProfile.setImageBitmap(bitmap);
                    }
                }
            }
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextBirthDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                },
                year, month, dayOfMonth
        );
        datePickerDialog.show();
    }

    private void dispatchTakePictureIntent() {
        Intent choosePictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        choosePictureIntent.setType("image/*");
        startActivityForResult(choosePictureIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageViewProfile.setImageBitmap(bitmap);
                Toast.makeText(this, "Image added", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerUser() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String birthDate = editTextBirthDate.getText().toString().trim();
        String birthPlace = spinnerBirthPlace.getSelectedItem().toString();
        byte[] photo = getPhotoByteArray(); // Get byte array of the photo

        // Validate input
        if (username.isEmpty() || password.isEmpty() || birthDate.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add user to database
        long id = dbHelper.addUser(username, password, photo, birthDate, birthPlace);
        if (id > 0) {
            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
            finish(); // Finish activity and go back to login screen
        } else {
            Toast.makeText(this, "Failed to register user", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUser() {
        // Retrieve updated user information from input fields
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String birthDate = editTextBirthDate.getText().toString().trim();
        String birthPlace = spinnerBirthPlace.getSelectedItem().toString();
        byte[] photo = getPhotoByteArray(); // Get byte array of the photo

        // Validate input
        if (username.isEmpty() || password.isEmpty() || birthDate.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update user in database
        boolean updated = dbHelper.updateUser(userId, username, password, photo, birthDate, birthPlace);
        if (updated) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("userId", userId);
            setResult(RESULT_OK, resultIntent);
            Toast.makeText(this, "User details updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // Finish activity and return to previous screen
        } else {
            Toast.makeText(this, "Failed to update user details", Toast.LENGTH_SHORT).show();
        }
    }


    private byte[] getPhotoByteArray() {
        Bitmap bitmap = ((BitmapDrawable) imageViewProfile.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
