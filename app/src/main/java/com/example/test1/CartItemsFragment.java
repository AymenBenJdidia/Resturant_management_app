package com.example.test1;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CartItemsFragment extends Fragment {
    private List<CartItem> cartItemList;
    private CartItemAdapter cartItemAdapter;
    private Context context; // Add context field
    private BottomNavigationView bottomNavigationView;

    public void setBottomNavigationView(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize sample cart items
        cartItemList = ShoppingCart.getInstance().items;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart_list, container, false);
        context = getContext(); // Initialize context

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        cartItemAdapter = new CartItemAdapter(cartItemList, context, bottomNavigationView); // Pass context and bottomNavigationView to adapter
        recyclerView.setAdapter(cartItemAdapter);



        return rootView;
    }
}
