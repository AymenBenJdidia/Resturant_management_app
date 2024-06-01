package com.example.test1;

public class User {
    private String username;
    private String password;
    private byte[] photo;
    private String birthDate;
    private String birthPlace;

    public User(String username, String password, byte[] photo, String birthDate, String birthPlace) {
        this.username = username;
        this.password = password;
        this.photo = photo;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }
// Add getters for all fields
}