package com.example.signuploginrealtime;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class feedback extends AppCompatActivity {

    private EditText editTextFeedback;
    private RatingBar ratingBar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Button buttonSubmit;

    // Firebase
    private DatabaseReference feedbackRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView = findViewById(R.id.side_navigation_view);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // Initialize Firebase
        feedbackRef = FirebaseDatabase.getInstance().getReference().child("users");
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        editTextFeedback = findViewById(R.id.editTextFeedback);
        ratingBar = findViewById(R.id.ratingBar);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Set onClickListener for the submit button
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.bt1) {
                    Intent intent = new Intent(feedback.this, enrolcourse.class);
                    startActivity(intent);
                } else if (id == R.id.bt2) {
                    Intent intent = new Intent(feedback.this, uchanpasword.class);
                    startActivity(intent);
                } else if (id == R.id.bt3) {
                    Intent intent = new Intent(feedback.this, tutorial.class);
                    startActivity(intent);
                } else if (id == R.id.bt4) {
                    Intent intent = new Intent(feedback.this, mocktest.class);
                    startActivity(intent);
                } else if (id == R.id.bt5) {
                    Intent intent = new Intent(feedback.this, feedback.class);
                    startActivity(intent);
                } else if (id == R.id.bt6) {
                    Intent intent = new Intent(feedback.this, scoreboard.class);
                    startActivity(intent);
                } else if (id == R.id.bt7) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(feedback.this);
                    builder.setTitle("Logout");
                    builder.setMessage("Are you sure you want to logout?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Logout the user and navigate to the home activity
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(feedback.this, home.class);
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
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private void submitFeedback() {
        // Get input values
        String feedback = editTextFeedback.getText().toString().trim();
        float rating = ratingBar.getRating();

        // Validate inputs
        if (feedback.isEmpty()) {
            Toast.makeText(this, "Please fill the feedback field", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Get the current user's ID
            String userId = user.getUid();

            // Create feedback object as a HashMap
            HashMap<String, Object> feedbackMap = new HashMap<>();
            feedbackMap.put("feedback", feedback);
            feedbackMap.put("rating", rating);

            // Add feedback to Firebase under the user's node
            feedbackRef.child(userId).child("feedback").push().setValue(feedbackMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(feedback.this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                            // Navigate to the userdb activity
                            Intent intent = new Intent(feedback.this, userdb.class);
                            startActivity(intent);
                            finish(); // finish current activity
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(feedback.this, "Failed to submit feedback: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // No user is signed in
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle ActionBarDrawerToggle clicks here
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}
