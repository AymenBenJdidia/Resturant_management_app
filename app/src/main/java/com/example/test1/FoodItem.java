package com.example.test1;

public class FoodItem {
    private long id;
    private String name;
    private String category;
    private double price;
    private String description;
    private byte[] image;

    public FoodItem(long id,String name, String category, double price, String description, byte[] image) {
        this.id=id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.image = image;
    }
    public FoodItem(String name, String category, double price, String description, byte[] image) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

