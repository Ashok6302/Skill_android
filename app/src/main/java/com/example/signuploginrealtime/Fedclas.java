package com.example.signuploginrealtime;

public class Fedclas {
    private String name;
    private String feedback;
    private float rating;

    public Fedclas() {
        // Default constructor required for Firebase
    }

    public Fedclas(String name, String feedback, float rating) {
        this.name = name;
        this.feedback = feedback;
        this.rating = rating;
    }

    // Getters and setters (optional, depending on your needs)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
