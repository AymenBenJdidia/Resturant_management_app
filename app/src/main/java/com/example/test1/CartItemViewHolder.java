package com.example.test1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CartItemViewHolder extends RecyclerView.ViewHolder {
    private TextView itemNameTextView;
    private TextView itemPriceTextView;
    private TextView itemTotalPriceTextView;
    private TextView quantityTextView;
    private ImageView itemImageView;

    private ImageButton buttonPlus;
    private ImageButton buttonMinus;
    private ImageButton buttonDelete;
    private CartItemAdapter adapter;


    MenuDbHelper DataBase;

    private Context context; // Add context field
    private ShoppingCart shoppingCart; // Add shopping cart field

    public CartItemViewHolder(View itemView, Context context, CartItemAdapter cartItemAdapter) {
        super(itemView);
        this.adapter = cartItemAdapter;
        this.context = context;
        this.shoppingCart = ShoppingCart.getInstance();
        DataBase = new MenuDbHelper(context);
        itemNameTextView = itemView.findViewById(R.id.item_name_text_view);
        itemPriceTextView = itemView.findViewById(R.id.item_price_text_view);
        itemTotalPriceTextView = itemView.findViewById(R.id.item_total_price_text_view);
        itemImageView = itemView.findViewById(R.id.item_image_view);
        quantityTextView = itemView.findViewById(R.id.quantity_text_view);
        buttonPlus = itemView.findViewById(R.id.increment_button);
        buttonMinus = itemView.findViewById(R.id.decrement_button);
        buttonDelete = itemView.findViewById(R.id.delete_button);





    }

    public void bind(CartItem cartItem) {

        FoodItem item = DataBase.getItemById(cartItem.getId());
        itemNameTextView.setText(item.getName());
        itemPriceTextView.setText(String.valueOf(item.getPrice())+" DT");
        itemTotalPriceTextView.setText("Total: " +String.valueOf(cartItem.getQuantity()*item.getPrice())+" DT");
        updateBadge();
        // Optionally, set a background color for the badge
        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingCart.addToShoppingCart(cartItem.getId());
                adapter.notifyDataSetChanged();
                //adapter.updateTotal(); // Update the total
            }
        });

        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingCart.decrementQuantity(cartItem.getId());
                adapter.notifyDataSetChanged();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingCart.removeFromShoppingCart(cartItem.getId());
                updateBadge();
                adapter.notifyDataSetChanged();
            }
        });

        if (item.getImage() != null) {
            // If you have a byte array representation of the image
            Bitmap bitmap = BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length);
            itemImageView.setImageBitmap(bitmap);
        }
        quantityTextView.setText(String.valueOf(cartItem.getQuantity()));
    }

    void updateBadge() {

        if (this.adapter.bottomNavigationView != null) {
            BadgeDrawable badgeDrawable = this.adapter.bottomNavigationView.getOrCreateBadge(R.id.Cart);
            badgeDrawable.setNumber(ShoppingCart.getInstance().getShoppingCartSize());
        }
    }

}