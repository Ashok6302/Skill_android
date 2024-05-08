package com.example.signuploginrealtime;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class enrolcourse extends AppCompatActivity {

    private Spinner spinnerCourses;
    private Button submitButton;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrolcourse);

        spinnerCourses = findViewById(R.id.spinner_courses);
        submitButton = findViewById(R.id.bt);
        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView = findViewById(R.id.side_navigation_view);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // Initialize Firebase components
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Fetch courses from Firebase and populate spinner
        fetchCourses();

        // Set click listener for submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enrollCurrentUserInCourse();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
if (id == R.id.bt1) {
                    Intent intent = new Intent(enrolcourse.this, enrolcourse.class);
                    startActivity(intent);
                } else if (id == R.id.bt2) {
                    Intent intent = new Intent(enrolcourse.this, uchanpasword.class);
                    startActivity(intent);
                } else if (id == R.id.bt3) {
                    Intent intent = new Intent(enrolcourse.this, tutorial.class);
                    startActivity(intent);
                } else if (id == R.id.bt4) {
                    Intent intent = new Intent(enrolcourse.this, feedback.class);
                    startActivity(intent);
                } else if (id == R.id.bt5) {
                    Intent intent = new Intent(enrolcourse.this, scoreboard.class);
                    startActivity(intent);
                } else if (id == R.id.bt6) {
                    Intent intent = new Intent(enrolcourse.this, scoreboard.class);
                    startActivity(intent);
                } else if (id == R.id.bt7) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(enrolcourse.this);
                    builder.setTitle("Logout");
                    builder.setMessage("Are you sure you want to logout?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Logout the user and navigate to the home activity
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(enrolcourse.this, home.class);
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

    private void fetchCourses() {
        DatabaseReference adcourseRef = database.getReference("adcourse");

        adcourseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> courses = new ArrayList<>();
                for (DataSnapshot courseSnapshot : snapshot.getChildren()) {
                    String courseName = courseSnapshot.child("courseName").getValue(String.class);
                    courses.add(courseName);
                }

                ArrayAdapter<String> coursesAdapter = new ArrayAdapter<>(enrolcourse.this,
                        android.R.layout.simple_spinner_dropdown_item, courses);
                spinnerCourses.setAdapter(coursesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("EnrolCourseActivity", "Error fetching courses", error.toException());
            }
        });
    }

    private void enrollCurrentUserInCourse() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String courseName = spinnerCourses.getSelectedItem().toString();

            DatabaseReference userEnrolmentsRef = database.getReference("users").child(userId).child("enrolments");

            userEnrolmentsRef.orderByChild("coursename").equalTo(courseName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User is already enrolled in the selected course
                        Toast.makeText(enrolcourse.this, "You are already enrolled in this course!", Toast.LENGTH_SHORT).show();
                    } else {
                        // User is not enrolled in the selected course, proceed with enrollment
                        DatabaseReference newEnrolmentRef = userEnrolmentsRef.push();
                        newEnrolmentRef.child("coursename").setValue(courseName)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(enrolcourse.this, "Enrolled successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(enrolcourse.this, userdb.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(enrolcourse.this, "Enrollment failed!", Toast.LENGTH_SHORT).show();
                                        Log.e("EnrolCourseActivity", "Enrollment failed", e);
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("EnrolCourseActivity", "Error checking enrollment", databaseError.toException());
                    Toast.makeText(enrolcourse.this, "Error checking enrollment. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // User is not logged in
            Toast.makeText(enrolcourse.this, "User not logged in!", Toast.LENGTH_SHORT).show();
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
