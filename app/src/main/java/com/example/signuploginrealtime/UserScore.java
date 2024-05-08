package com.example.signuploginrealtime;

class UserScore implements Comparable<UserScore> {
    private String userName;
    private int userScore;

    public UserScore(String userName, int userScore) {
        this.userName = userName;
        this.userScore = userScore;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserScore() {
        return userScore;
    }

    @Override
    public int compareTo(UserScore other) {
        // Compare users based on their scores
        return Integer.compare(this.userScore, other.userScore);
    }
}