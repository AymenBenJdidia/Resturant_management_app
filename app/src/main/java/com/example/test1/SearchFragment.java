package com.example.test1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements FoodItemAdapter.OnItemClickListener  {

    private RecyclerView recyclerView;
    private FoodItemAdapter adapter;
    private List<FoodItem> foodItems;
    private MenuDbHelper menuDbHelper;
    private EditText searchBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        menuDbHelper = new MenuDbHelper(getContext());
        foodItems = menuDbHelper.getAllItems();

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new FoodItemAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        searchBar = view.findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                performSearch(s.toString());
            }
        });

        return view;
    }

    private void performSearch(String query) {
        List<FoodItem> filteredList = new ArrayList<>();
        for (FoodItem item : foodItems) {
            if (item.getName().toLowerCase().contains(query.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.setFoodItems(filteredList);
    }
    @Override
    public void onItemClick(long itemId) {
        // Add the item to the shopping cart
        ShoppingCart.getInstance().addToShoppingCart(itemId);
        // Optionally, update your UI to reflect that the item has been added to the cart
    }
}


