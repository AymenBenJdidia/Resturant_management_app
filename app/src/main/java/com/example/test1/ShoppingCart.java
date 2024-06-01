package com.example.test1;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private static ShoppingCart instance;
    List<CartItem> items;

    private ShoppingCart() {
        items = new ArrayList<>();
    }

    public static synchronized ShoppingCart getInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }
        return instance;
    }

    public void addToShoppingCart(long itemId) {
        // Check if an item with the same ID already exists
        for (CartItem item : items) {
            if (item.getId() == itemId) {
                item.incQuantity(); // If exists, increment quantity
                return;
            }
        }
        // If not exists, add a new item with quantity 1
        items.add(new CartItem(itemId, 1));
    }
    public int getShoppingCartSize() {
        try{
            return this.items.size();

        }catch (Exception e){
            return -1;

        }
    }
    public void removeFromShoppingCart(long itemId) {
        // Find and remove the item with the specified ID
        items.removeIf(item -> item.getId() == itemId);
    }

    public void decrementQuantity(long itemId) {
        // Find the item with the specified ID
        for (CartItem item : items) {
            if (item.getId() == itemId) {
                item.decQuantity(); // Decrement quantity if greater than 1
                return;
            }
        }
    }


    // Other methods to add, remove, or update items in the cart
}
