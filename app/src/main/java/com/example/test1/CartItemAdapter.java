package com.example.test1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemViewHolder> {
    private List<CartItem> cartItemList;
    private Context context; // Add context field
    private MenuDbHelper DataBase;

    BottomNavigationView bottomNavigationView;

    public CartItemAdapter(List<CartItem> cartItemList, Context context, BottomNavigationView bottomNavigationView) {
        this.cartItemList = cartItemList;
        this.context = context; // Initialize context
        this.bottomNavigationView = bottomNavigationView;

    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_row, parent, false);
        return new CartItemViewHolder(view, context,this); // Pass context to ViewHolder constructor
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        holder.bind(cartItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public double calculateTotal() {
        double total = 0;
        for (CartItem item : cartItemList) {
            total += item.getQuantity() * DataBase.getItemById(item.getId()).getPrice();
        }
        return total;
    }

}
