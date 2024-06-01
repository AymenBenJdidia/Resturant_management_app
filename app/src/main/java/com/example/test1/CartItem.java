package com.example.test1;

public class CartItem {
    private long id;
    private int quantity;

    public CartItem(long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void incQuantity() {
        this.quantity++;
    }
    public void decQuantity() {
        if(this.quantity>1){
            this.quantity--;
        }
    }
}
