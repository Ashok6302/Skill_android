package com.example.signuploginrealtime;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class scoreboard extends AppCompatActivity {

    private TableLayout tableLayoutScoreboard;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        tableLayoutScoreboard = findViewById(R.id.tableLayoutScoreboard);

        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView = findViewById(R.id.side_navigation_view);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        // Retrieve data from Firebase and populate the scoreboard
        populateScoreboard();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.bt1) {
                    Intent intent = new Intent(scoreboard.this, enrolcourse.class);
                    startActivity(intent);
                } else if (id == R.id.bt2) {
                    Intent intent = new Intent(scoreboard.this, uchanpasword.class);
                    startActivity(intent);
                } else if (id == R.id.bt3) {
                    Intent intent = new Intent(scoreboard.this, tutorial.class);
                    startActivity(intent);
                } else if (id == R.id.bt4) {
                    Intent intent = new Intent(scoreboard.this, mocktest.class);
                    startActivity(intent);
                } else if (id == R.id.bt5) {
                    Intent intent = new Intent(scoreboard.this, feedback.class);
                    startActivity(intent);
                } else if (id == R.id.bt6) {
                    Intent intent = new Intent(scoreboard.this, scoreboard.class);
                    startActivity(intent);
                }  else if (id == R.id.bt7) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(scoreboard.this);
                    builder.setTitle("Logout");
                    builder.setMessage("Are you sure you want to logout?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Logout the user and navigate to the home activity
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(scoreboard.this, home.class);
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

    private void populateScoreboard() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Initialize a list to hold users and their scores
                List<UserScore> users = new ArrayList<>();

                // Iterate through the users in the dataSnapshot and add them to the list
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Get user data
                    String userName = userSnapshot.child("username").getValue(String.class);
                    Integer userScoreObject = userSnapshot.child("scores").child("totalScore").getValue(Integer.class);

                    // Check if userScoreObject is null
                    int userScore = (userScoreObject != null) ? userScoreObject : 0;

                    // Add user to the list
                    users.add(new UserScore(userName, userScore));
                }

                // Sort the users based on their scores (descending order)
                Collections.sort(users, Collections.reverseOrder());

                // Populate the scoreboard with the sorted users
                int sno = 1;
                for (UserScore user : users) {
                    addTableRow(sno, user.getUserName(), user.getUserScore());
                    sno++;
                }

//                // Set the top scorer in the TextViews
//                if (!users.isEmpty()) {
//                    UserScore topScorer = users.get(0);
//                    TextView textViewTopScorerName = findViewById(R.id.name);
//                    TextView textViewTopScore = findViewById(R.id.score);
//                    textViewTopScorerName.setText(topScorer.getUserName());
//                    textViewTopScore.setText(String.valueOf(topScorer.getUserScore()));
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ScoreboardActivity", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void addTableRow(int sno, String userName, int userScore) {
        TableRow tableRow = new TableRow(this);

        TextView textViewSno = new TextView(this);
        textViewSno.setText(String.valueOf(sno));
        textViewSno.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        TextView textViewUserName = new TextView(this);
        textViewUserName.setText(userName);
        textViewUserName.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3));

        TextView textViewUserScore = new TextView(this);
        textViewUserScore.setText(String.valueOf(userScore));
        textViewUserScore.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        // Add TextViews to the TableRow in the correct order
        tableRow.addView(textViewSno);
        tableRow.addView(textViewUserName);
        tableRow.addView(textViewUserScore);

        // Add the TableRow to the TableLayout
        tableLayoutScoreboard.addView(tableRow);
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