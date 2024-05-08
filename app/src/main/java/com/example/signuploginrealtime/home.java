package com.example.signuploginrealtime;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button bt1 = findViewById(R.id.bt1);
        Button bt = findViewById(R.id.bt);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the UserActivity when the USER button is clicked
                Intent intent = new Intent(home.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the AdminActivity when the ADMIN button is clicked
                Intent intent = new Intent(home.this, adminlogin.class);
                startActivity(intent);
            }
        });
    }
}
