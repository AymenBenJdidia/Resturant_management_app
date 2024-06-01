package com.example.test1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {
    private static final int SUBCATEGORY_REQUEST_CODE = 1001; // Choose any unique integer value

    private BottomNavigationView bottomNavigationView;
    private long userID;

    public HomeFragment(BottomNavigationView bottomNavigationView,long userID) {
        this.bottomNavigationView=bottomNavigationView;
        this.userID=userID;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Initialize the GridLayout
        GridLayout categoryGridLayout = rootView.findViewById(R.id.categoryGridLayout);


        // Create an array of category names and images
        String[] categoryNames = {"Appetizers", "Main Courses", "Side Dishes", "Desserts", "Beverages"};
        int[] categoryImages = {R.drawable.appetizers, R.drawable.main_courses, R.drawable.side_dishes, R.drawable.desserts, R.drawable.beverages};

        // Loop through the arrays to create and add CardViews for each category
        for (int i = 0; i < categoryNames.length; i++) {
            // Inflate the category item layout
            View categoryItemView = LayoutInflater.from(requireContext()).inflate(R.layout.item_category, categoryGridLayout, false);

            // Find views within the inflated layout
            ImageView imageView = categoryItemView.findViewById(R.id.background_image);
            TextView textView = categoryItemView.findViewById(R.id.text_caption);

            // Set data for the category item
            textView.setText(categoryNames[i]);
            imageView.setImageResource(categoryImages[i]);

            // Add click listener to launch subcategory screen
            final int categoryIndex = i;
            categoryItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Launch subcategory screen
                    Intent intent = new Intent(requireContext(), SubcategoryActivity.class);
                    intent.putExtra("categoryName", categoryNames[categoryIndex]);
                    startActivityForResult(intent, SUBCATEGORY_REQUEST_CODE);

                }
            });

            // Add the category item to the GridLayout
            categoryGridLayout.addView(categoryItemView);
        }

        FloatingActionButton fab = rootView.findViewById(R.id.floatingActionButton);
        if(userID==-1){
            fab.setVisibility(View.VISIBLE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to another activity
                Intent intent = new Intent(requireContext(), ManageMenuActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SUBCATEGORY_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            int selectedItemId = data.getIntExtra("selectedItemId", R.id.Home);
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).updateNavigationBar(selectedItemId);
            }

            updateBadge();
        }
    }

    private void updateBadge() {
        if (bottomNavigationView != null) {
            BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.Cart);
            badgeDrawable.setNumber(ShoppingCart.getInstance().getShoppingCartSize());
        }
    }
}
