package com.example.signuploginrealtime;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileNameTextView, profileEmailTextView, profilePasswordTextView;
    private View coursesTableView;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private TextView scoreTV;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Views
        profileNameTextView = findViewById(R.id.profileName);
        profileEmailTextView = findViewById(R.id.profileEmail);
        profilePasswordTextView = findViewById(R.id.profilePassword);
        coursesTableView = findViewById(R.id.tab);
        scoreTV = findViewById(R.id.scoreTV);
        // Initialize Firebase components
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        // Get the current authenticated user
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // Fetch user details using the current user's ID
            fetchUserDetails(currentUser.getUid());
            fetchUserCourses(currentUser.getUid());
        }
    }

    private void fetchUserDetails(String userId) {
        // Create a query to find the user with the provided user ID
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User exists, retrieve user details
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String password = dataSnapshot.child("password").getValue(String.class);

                    // Update UI with user details
                    profileNameTextView.setText(username);
                    profileEmailTextView.setText("Email: " + email);
                    profilePasswordTextView.setText("Password: " + password);
                } else {
                    // User does not exist
                    Toast.makeText(ProfileActivity.this, "User not found", Toast.LENGTH_SHORT).show();

                    // Clear UI fields
                    profileNameTextView.setText("");
                    profileEmailTextView.setText("");
                    profilePasswordTextView.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(ProfileActivity.this, "Error fetching user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserCourses(String userId) {
        DatabaseReference userCoursesRef = databaseReference.child(userId).child("enrolments");
        userCoursesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> courses = new ArrayList<>();
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    String courseName = courseSnapshot.child("coursename").getValue(String.class);
                    courses.add(courseName);
                }
                // Populate TableView with course names
                populateCoursesTableView(courses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(ProfileActivity.this, "Error fetching user courses: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateCoursesTableView(List<String> courses) {
        // Add your logic to populate the TableView with course names here
        // For example, you can create a custom adapter for TableView or directly manipulate TableView.
        // Here, we assume you have already implemented the logic for TableView.
    }

    private class TableView {
    }
}
