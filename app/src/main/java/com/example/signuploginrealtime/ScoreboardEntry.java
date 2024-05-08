package com.example.signuploginrealtime;

public class ScoreboardEntry {
    private String username;
    private int score;

    // Empty constructor (required for Firebase)
    public ScoreboardEntry() {}

    public ScoreboardEntry(String username, int score) {
        this.username = username;
        this.score = score;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
