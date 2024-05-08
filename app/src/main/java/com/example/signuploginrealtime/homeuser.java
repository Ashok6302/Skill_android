package com.example.signuploginrealtime;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.signuploginrealtime.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class homeuser extends AppCompatActivity {

    LinearLayout coversContainer;
    String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeuser);
        ImageView timeImageView = findViewById(R.id.time);
        ImageView homeImageView = findViewById(R.id.home);
        ImageView profileImageView = findViewById(R.id.profile);
        ImageView menuImageView = findViewById(R.id.menu);
        ImageView logoutImageView = findViewById(R.id.logout);
        // Set click listeners for each ImageView
        timeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to TimeActivity
                Intent intent = new Intent(homeuser.this, userdb.class);
                startActivity(intent);
            }
        });
        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to TimeActivity
                Intent intent = new Intent(homeuser.this, userdb.class);
                startActivity(intent);
            }
        });
        logoutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display an alert dialog to confirm logout
                AlertDialog.Builder builder = new AlertDialog.Builder(homeuser.this);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Logout the user and navigate to the home activity
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(homeuser.this, home.class);
                        startActivity(intent);
                        finish(); // Finish current activity
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog if "No" is clicked
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        homeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to HomeActivity
                Intent intent = new Intent(homeuser.this, homeuser.class);
                startActivity(intent);
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ProfileActivity
                Intent intent = new Intent(homeuser.this, ProfileActivity.class);
                startActivity(intent);
            }
        });


        coversContainer = findViewById(R.id.coversContainer);

        // Show all covers for the specific shop
        showCoversForShop();

        // Button e1

    }

    private void showCoversForShop() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("adcourse");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    // No data found for the specific shop
                    Log.e("Covers", "No data found for covers in shop: " + shopName);
                    return;
                }

                for (DataSnapshot coversSnapshot : snapshot.getChildren()) {
                    Log.d("Covers", "Processing entry for covers");

                    String imageUrl = coversSnapshot.child("imageUrl").getValue(String.class);
                    String courseName = coversSnapshot.child("courseName").getValue(String.class);
                    String amount = coversSnapshot.child("amount").getValue(String.class);
                    String description = coversSnapshot.child("description").getValue(String.class); // Retrieve the nextpage value

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Log.d("Covers", "Image URL: " + imageUrl);

                        // Inflate the item_cover.xml layout
                        View coverItemView = getLayoutInflater().inflate(R.layout.item_courses, coversContainer, false);

                        // Find views in the inflated layout
                        ImageView coverImageView = coverItemView.findViewById(R.id.img1);
                        TextView descriptionn = coverItemView.findViewById(R.id.t1);
                        TextView price = coverItemView.findViewById(R.id.tt4);
                        Button button = coverItemView.findViewById(R.id.btn1);
                        // Set data for each view
                        Picasso.get().load(imageUrl).into(coverImageView); // Load image using Picasso
                        descriptionn.setText(" " + description);
                        price.setText(" " + amount);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(homeuser.this, enrolcourse.class);
                                startActivity(intent);
                            }
                        });

                        // Add the inflated layout to the coversContainer
                        coversContainer.addView(coverItemView);
                    } else {
                        Log.e("Covers", "Image URL is null or empty for an entry");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Covers", "Database error: " + error.getMessage());
            }
        });
    }
}
