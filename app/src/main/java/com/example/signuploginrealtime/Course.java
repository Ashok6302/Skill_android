package com.example.signuploginrealtime;

import android.net.Uri;

public class Course {
    private String courseName;
    private String description;
    private String amount;
    private String imageUrl;

    public Course(String courseName, String description, String amount, Uri imageUri) {
        // Default constructor required for Firebase deserialization
    }

    public Course(String courseName, String description, String amount, String imageUrl) {
        this.courseName = courseName;
        this.description = description;
        this.amount = amount;
        this.imageUrl = imageUrl;
    }

    // Getters and setters for all fields
    // You can generate these using your IDE or write them manually

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
