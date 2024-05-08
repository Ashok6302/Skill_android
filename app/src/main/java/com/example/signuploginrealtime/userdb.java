package com.example.signuploginrealtime;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class userdb extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdb);

        // Initialize buttons
        Button btCourseEnroll = findViewById(R.id.bt1);
        Button btChangePassword = findViewById(R.id.bt2);
        Button btTutorial = findViewById(R.id.bt3);
        Button btMockTest = findViewById(R.id.bt4);
        Button btQuery = findViewById(R.id.bt5);
        Button btscoreboard = findViewById(R.id.bt7);
        btCourseEnroll.setOnClickListener(new View.OnClickListener() {
            // Set click listeners for buttons
            @Override
            public void onClick(View view) {
                startActivity(new Intent(userdb.this, enrolcourse.class));
            }
        });

        btChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(userdb.this,uchanpasword.class));
            }
        });

        btTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(userdb.this, tutorial.class));
            }
        });

        btMockTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(userdb.this, mocktest  .class));
            }
        });

        btQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(userdb.this,feedback.class));

            }
        });


        btscoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(userdb.this, scoreboard.class));
            }
        });

    }


}
