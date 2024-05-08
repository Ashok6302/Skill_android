package com.example.signuploginrealtime.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.signuploginrealtime.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class HistoryFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private TableLayout tableLayoutScoreboard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        tableLayoutScoreboard = view.findViewById(R.id.tableLayoutScoreboard);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("scores");
        populateScoreboard();
        return view;
    }

    private void populateScoreboard() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int sno = 1; // Counter for serial number
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String courseName = userSnapshot.getKey(); // Get the user's name
                    int score = 0;
                    if (userSnapshot.hasChild("phpScore")) { // Check if PHP score exists for the user
                        score = userSnapshot.child("phpScore").getValue(Integer.class); // Get PHP score
                    } else if (userSnapshot.hasChild("htmlScore")) { // Check if PHP score exists for the user
                        score = userSnapshot.child("htmlScore").getValue(Integer.class); // Get PHP score
                    } else if (userSnapshot.hasChild("cProgrammingScore")) { // Check if PHP score exists for the user
                        score = userSnapshot.child("cProgrammingScore").getValue(Integer.class); // Get PHP score
                    } else if (userSnapshot.hasChild("javaScore")) { // Check if PHP score exists for the user
                        score = userSnapshot.child("javaScore").getValue(Integer.class); // Get PHP score
                    } else if (userSnapshot.hasChild("pythonScore")) { // Check if PHP score exists for the user
                        score = userSnapshot.child("pythonScore").getValue(Integer.class); // Get PHP score
                    }
                    if (!Objects.equals(courseName, "totalScore")) {
                        addTableRow(sno, courseName, score); // Add the row to the table
                    }
                    sno++; // Increment serial number
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ScoreboardActivity", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void addTableRow(int sno, String userName, int userScore) {
        TableRow tableRow = new TableRow(requireContext());

        TextView textViewSno = new TextView(requireContext());
        textViewSno.setText(String.valueOf(sno));
        textViewSno.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        TextView textViewUserName = new TextView(requireContext());
        textViewUserName.setText(userName);
        textViewUserName.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3));

        TextView textViewUserScore = new TextView(requireContext());
        textViewUserScore.setText(String.valueOf(userScore));
        textViewUserScore.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        // Add TextViews to the TableRow in the correct order
        tableRow.addView(textViewSno);
        tableRow.addView(textViewUserName);
        tableRow.addView(textViewUserScore);

        // Add the TableRow to the TableLayout
        tableLayoutScoreboard.addView(tableRow);
    }
}