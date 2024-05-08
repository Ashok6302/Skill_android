package com.example.signuploginrealtime.AdminFragments;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signuploginrealtime.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentHistoryFragment extends Fragment {

    private DatabaseReference usersRef;
    TableLayout tableLayout;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
             View view = inflater.inflate(R.layout.fragment_student_history, container, false);

        tableLayout = view.findViewById(R.id.tableLayoutScoreboard);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users"); // Assuming 'users' is your users node in Firebase

        // Fetch user data and populate the table layout
        addTableHeader();
        fetchUserData();

        return view;
    }

    private void addTableHeader() {
        // Create a new table row
        TableRow tableRow = new TableRow(requireContext());
        tableRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));

        // Create layout parameters for the text views
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );

        // Create and add text views for each heading
        String[] headings = {"S.No", "UserName", "Enrolled Courses", "Action"};
        for (String heading : headings) {
            TextView textView = new TextView(requireContext());
            textView.setText(heading);
            textView.setLayoutParams(layoutParams);
            textView.setPadding(16, 8, 16, 8); // Add padding for better appearance
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black)); // Use your color resource
            textView.setTypeface(null, Typeface.BOLD); // Make text bold
            tableRow.addView(textView);
        }

        // Add the table row to the table layout
        tableLayout.addView(tableRow);
    }

    private void fetchUserData() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int sno = 1;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String username = userSnapshot.child("username").getValue(String.class);
                    String userEmail = userSnapshot.child("email").getValue(String.class);
                    StringBuilder coursesBuilder = new StringBuilder();
                    for (DataSnapshot courseSnapshot : userSnapshot.child("enrolments").getChildren()) {
                        coursesBuilder.append(courseSnapshot.child("coursename").getValue()).append(", ");
                    }
                    String courses = coursesBuilder.toString().trim();

                    // Add fetched user data to the table
                    addTableRow(String.valueOf(sno++), username, courses, userEmail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", "Database Error: " + databaseError.getMessage());
            }
        });
    }

    private void addTableRow(String sno, String name, String courses, String userEmail) {

        // Create a new table row
        TableRow tableRow = new TableRow(requireContext());
        tableRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));

        // Create layout parameters for the text views
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );

        // Create and add text views for each data
        TextView textViewSno = new TextView(requireContext());
        textViewSno.setText(sno);
        textViewSno.setLayoutParams(layoutParams);
        textViewSno.setPadding(16, 8, 16, 8);
        tableRow.addView(textViewSno);

        TextView textViewName = new TextView(requireContext());
        textViewName.setText(name);
        textViewName.setLayoutParams(layoutParams);
        textViewName.setPadding(16, 8, 16, 8);
        tableRow.addView(textViewName);

        TextView textViewCourses = new TextView(requireContext());
        textViewCourses.setText(courses);
        textViewCourses.setLayoutParams(layoutParams);
        textViewCourses.setPadding(16, 8, 16, 8);
        tableRow.addView(textViewCourses);

        // Add the delete button
        Button buttonDelete = new Button(requireContext());
        buttonDelete.setText("Delete");
        buttonDelete.setLayoutParams(layoutParams);
        buttonDelete.setPadding(16, 0, 16, 0);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String uid = userSnapshot.getRef().getKey();
                            userSnapshot.getRef().removeValue(); // Remove the user node
                            ((ViewGroup) tableRow.getParent()).removeView(tableRow);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("StudentHistoryFragment", "Database Error: " + databaseError.getMessage());
                    }
                });
            }
        });
        tableRow.addView(buttonDelete);

        // Add the table row to the table layout
        tableLayout.addView(tableRow);
    }
}