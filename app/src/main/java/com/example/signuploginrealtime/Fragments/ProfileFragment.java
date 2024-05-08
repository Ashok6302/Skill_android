package com.example.signuploginrealtime.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signuploginrealtime.ProfileActivity;
import com.example.signuploginrealtime.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private TextView profileNameTextView, profileEmailTextView, profilePasswordTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileNameTextView = view.findViewById(R.id.profileName);
        profileEmailTextView = view.findViewById(R.id.profileEmail);
        profilePasswordTextView = view.findViewById(R.id.profilePassword);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        // Get the current authenticated user
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // Fetch user details using the current user's ID
            fetchUserDetails(currentUser.getUid());
            fetchUserCourses(currentUser.getUid());
        }
        return view;
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
                    Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();

                    // Clear UI fields
                    profileNameTextView.setText("");
                    profileEmailTextView.setText("");
                    profilePasswordTextView.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(requireContext(), "Error fetching user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(requireContext(), "Error fetching user courses: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}