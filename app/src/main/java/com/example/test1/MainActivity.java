package com.example.test1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BadgeDrawable badgeDrawable;
    private BottomNavigationView bottomNavigationView;
    private static final int SUBCATEGORY_REQUEST_CODE = 1001; // Choose any unique integer value
    private static final int SIGN_UP_REQUEST_CODE = 1010; // Any unique integer value

    private UserInfoFragment userInfoFragment;
    private DatabaseUsersHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        long userId =-1;
        // Get user ID passed from LoginActivity

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USER_ID")) {
            userId = intent.getLongExtra("USER_ID", -1);
        }

        long finalUserId = userId;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Inside your main activity onCreate method or wherever you handle navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment(bottomNavigationView,userId))
                .commit();
        // Assuming you have a NavigationBarView with id navBar


// Create a BadgeDrawable
        badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.Cart);
// Set the number you want to display
        badgeDrawable.setNumber(ShoppingCart.getInstance().getShoppingCartSize()); // Example: Displayed number
// Optionally, set a background color for the badge
        badgeDrawable.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        badgeDrawable.setNumber(ShoppingCart.getInstance().getShoppingCartSize()); // Example: Displayed number

        // Create an instance of your fragment
        CartItemsFragment fragmentCART = new CartItemsFragment();

        // Pass the BottomNavigationView reference to the fragment
        fragmentCART.setBottomNavigationView(bottomNavigationView);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.Home) {
                    selectedFragment = new HomeFragment(bottomNavigationView,finalUserId);

                } else if (item.getItemId() == R.id.Search) {
                    selectedFragment = new SearchFragment();
                } else if (item.getItemId() == R.id.Cart) {
                    selectedFragment = fragmentCART;
                }else if (item.getItemId() == R.id.Profile) {
                    selectedFragment = new UserInfoFragment(finalUserId);
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true;
                }
                return false;
            }
        });

    }

    public void updateNavigationBar(int selectedItemId) {
        bottomNavigationView.setSelectedItemId(selectedItemId);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check if the result is from SignUpActivity
        if (requestCode == SIGN_UP_REQUEST_CODE && resultCode == RESULT_OK) {
            // Forward the result to UserInfoFragment if it is currently displayed
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment instanceof UserInfoFragment) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}