package com.example.test1;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserInfoFragment extends Fragment {
    private DatabaseUsersHelper dbHelper;
    private long userId;
    private Context context; // Add context field
    private static final int SIGN_UP_REQUEST_CODE = 1010; // Any unique integer value


    public UserInfoFragment(long userId) {
        super();
        this.userId=userId;
    }

    private TextView textViewUserName;
    private ImageView ImageViewUser;
    private TextView textViewUserCounty;
    private TextView textViewUserBirth;
    private Button modifyButton;
    private Button deleteButton;
    private Button logoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_info, container, false);
        context=getContext();
        dbHelper = new DatabaseUsersHelper(context);
        textViewUserName = rootView.findViewById(R.id.usernameTextView);
        ImageViewUser = rootView.findViewById(R.id.profileImageView);
        textViewUserCounty = rootView.findViewById(R.id.countryTextView);
        textViewUserBirth =  rootView.findViewById(R.id.birthTextView);
        modifyButton=rootView.findViewById(R.id.modify_button);
        deleteButton=rootView.findViewById(R.id.delete_button);
        logoutButton=rootView.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(),LoginPage.class);
                startActivity(intent);
            }
        });
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SignUpActivity for updating user details
                Intent intent = new Intent(getContext(), SignUpActivity.class);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, SIGN_UP_REQUEST_CODE);
            }
        });

        if (userId != -1) {
            deleteButton.setText("Delete account");
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHelper.removeUserById(userId);
                    Intent intent = new Intent();
                    intent.setClass(getContext(),LoginPage.class);
                    startActivity(intent);
                }
            });
            // Retrieve user details from database
            User user = dbHelper.getUserById(userId);
            if (user != null) {
                // Set user information in UserInfoFragment
                textViewUserName.setText( user.getUsername());
                textViewUserCounty.setText( user.getBirthPlace());
                textViewUserBirth.setText( user.getBirthDate());
                if (user.getPhoto() != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(user.getPhoto(), 0, user.getPhoto().length);
                    ImageViewUser.setImageBitmap(bitmap);
                } else {
                    ImageViewUser.setImageResource(R.drawable.default_profile_image); // Default image
                }
            }
        }else{
            deleteButton.setText("Delete users DB");
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHelper.removeAllUsers();
                    Intent intent = new Intent();
                    intent.setClass(getContext(),LoginPage.class);
                    startActivity(intent);
                }
            });
            textViewUserName.setText( "Admin mode");
        }
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Check if the result is from the SignUpActivity
            if (requestCode == SIGN_UP_REQUEST_CODE) {
                // Handle the result here
                if (data != null) {
                    // Retrieve any data sent back from the SignUpActivity
                    long userId = data.getLongExtra("userId", -1);
                    if (userId != -1) {
                        // Refresh user information in the fragment
                        User updatedUser = dbHelper.getUserById(userId);
                        if (updatedUser != null) {
                            // Update UI with the new user information
                            textViewUserName.setText(updatedUser.getUsername());
                            textViewUserCounty.setText(updatedUser.getBirthPlace());
                            textViewUserBirth.setText(updatedUser.getBirthDate());
                            if (updatedUser.getPhoto() != null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(updatedUser.getPhoto(), 0, updatedUser.getPhoto().length);
                                ImageViewUser.setImageBitmap(bitmap);
                            } else {
                                ImageViewUser.setImageResource(R.drawable.default_profile_image); // Default image
                            }
                        }
                    }
                }
            }
        }
    }




}
