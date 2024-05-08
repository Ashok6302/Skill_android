package com.example.signuploginrealtime;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class mocktest extends AppCompatActivity implements PaymentResultWithDataListener {
    private BroadcastReceiver razorpayReceiver;
    private LinearLayout coversContainer;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private DatabaseReference adCourseRef;
    private DatabaseReference userPaymentsRef;
//    private Button payNowButton,retestButton,statusButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mocktest);

        coversContainer = findViewById(R.id.coversContainer);

        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView = findViewById(R.id.side_navigation_view);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        adCourseRef = FirebaseDatabase.getInstance().getReference("adcourse");
        userPaymentsRef = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("payments");

        // Show enrolled courses for the current user
        showEnrolledCourses();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.bt1) {
                    Intent intent = new Intent(mocktest.this, enrolcourse.class);
                    startActivity(intent);
                } else if (id == R.id.bt2) {
                    Intent intent = new Intent(mocktest.this, uchanpasword.class);
                    startActivity(intent);
                } else if (id == R.id.bt3) {
                    Intent intent = new Intent(mocktest.this, tutorial.class);
                    startActivity(intent);
                } else if (id == R.id.bt4) {
                    Intent intent = new Intent(mocktest.this, mocktest.class);
                    startActivity(intent);
                } else if (id == R.id.bt5) {
                    Intent intent = new Intent(mocktest.this, feedback.class);
                    startActivity(intent);
                } else if (id == R.id.bt6) {
                    Intent intent = new Intent(mocktest.this, scoreboard.class);
                    startActivity(intent);
                } else if (id == R.id.bt7) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mocktest.this);
                    builder.setTitle("Logout");
                    builder.setMessage("Are you sure you want to logout?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Logout the user and navigate to the home activity
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(mocktest.this, home.class);
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


    private void showEnrolledCourses() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            DatabaseReference userEnrolmentsRef = FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(currentUserId);

            userEnrolmentsRef.child("enrolments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot enrolmentsSnapshot) {
                    for (DataSnapshot enrolment : enrolmentsSnapshot.getChildren()) {
                        String enrolledCourseName = enrolment.child("coursename").getValue(String.class);

                        adCourseRef.orderByChild("courseName").equalTo(enrolledCourseName).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot adCourseSnapshot) {
                                for (DataSnapshot adCourse : adCourseSnapshot.getChildren()) {

                                    String coursename = adCourse.child("courseName").getValue(String.class);
                                    String imageUrl = adCourse.child("imageUrl").getValue(String.class);
                                    String amount = adCourse.child("amount").getValue(String.class);

                                    if (imageUrl != null && !imageUrl.isEmpty() && amount != null && !amount.isEmpty()) {
                                        // Create and populate the layout for the enrolled course
                                        View coverItemView = getLayoutInflater().inflate(R.layout.item_mocktest, coversContainer, false);

                                        ImageView coverImageView = coverItemView.findViewById(R.id.img1);
                                        TextView priceTextView = coverItemView.findViewById(R.id.tt4);
                                        Button payNowButton = coverItemView.findViewById(R.id.btn1);
                                        Button retestButton = coverItemView.findViewById(R.id.bt1);
                                        Button statusButton = coverItemView.findViewById(R.id.status);

                                        Picasso.get().load(imageUrl).into(coverImageView);
                                        priceTextView.setText(amount);

                                        userPaymentsRef.child(enrolledCourseName).child("status").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    String status = dataSnapshot.getValue(String.class);

                                                    if (status != null && status.equals("paid")) {
                                                        // If the status is "paid", make the start test button visible
                                                        retestButton.setVisibility(View.VISIBLE);
                                                        payNowButton.setVisibility(View.GONE);
                                                        statusButton.setVisibility(View.GONE);
                                                    } else {
                                                        // If the status is not "paid" or null, hide the start test button
                                                        retestButton.setVisibility(View.GONE);
                                                        payNowButton.setVisibility(View.VISIBLE);
                                                        statusButton.setVisibility(View.GONE);
                                                    }
                                                } else {
                                                    // If the data snapshot doesn't exist, hide the start test button
                                                    retestButton.setVisibility(View.GONE);
                                                    payNowButton.setVisibility(View.VISIBLE);
                                                    statusButton.setVisibility(View.GONE);
                                                }

                                                userEnrolmentsRef.child("scores").child(enrolledCourseName).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        String completedStatus = "";

                                                        if (dataSnapshot.hasChild("status")) {
                                                            completedStatus = dataSnapshot.child("status").getValue(String.class);

                                                            if (completedStatus.equals("completed")) {
                                                                retestButton.setVisibility(View.GONE);
                                                                payNowButton.setVisibility(View.GONE);
                                                                statusButton.setVisibility(View.VISIBLE);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        Log.e("Firebase", "Error retrieving payment status: " + databaseError.getMessage());
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                // Handle any errors that may occur during the database operation
                                                Log.e("Firebase", "Error retrieving payment status: " + databaseError.getMessage());
                                            }
                                        });

                                        // Set OnClickListener for buttons
                                        retestButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Navigate to the test activity
                                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("courseName", enrolledCourseName); // Assuming courseName is the variable holding the course name
                                                editor.apply();
                                                if (Objects.equals(enrolledCourseName, "PHP")){
                                                    Intent intent = new Intent(mocktest.this, phpquiz.class);
                                                    startActivity(intent);
                                                } else if (Objects.equals(enrolledCourseName, "JAVA")) {
                                                    Intent intent = new Intent(mocktest.this, javaquiz.class);
                                                    startActivity(intent);
                                                } else if (Objects.equals(enrolledCourseName, "C Programming")) {
                                                    Intent intent = new Intent(mocktest.this, cquiz.class);
                                                    startActivity(intent);
                                                } else if (Objects.equals(enrolledCourseName, "PYTHON")) {
                                                    Intent intent = new Intent(mocktest.this, pyquiz.class);
                                                    startActivity(intent);
                                                } else if (Objects.equals(enrolledCourseName, "HTML")) {
                                                    Intent intent = new Intent(mocktest.this, htmlquiz.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                        payNowButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String samount = amount;
                                                int amount = Math.round(Float.parseFloat(samount) * 100);

                                                // Generate a unique filename using UUID
                                                String filename = "screenshot_" + System.currentTimeMillis() + ".png";

                                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("courseName", enrolledCourseName); // Assuming courseName is the variable holding the course name
                                                editor.apply();

                                                // Continue with Razorpay payment
                                                startRazorpayPayment(enrolledCourseName, amount, filename, payNowButton, retestButton, priceTextView);
                                            }
                                        });

                                        coversContainer.addView(coverItemView);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("EnrolledCourses", "Database error: " + error.getMessage());
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("EnrolledCourses", "Database error: " + error.getMessage());
                }
            });
        }
    }

    private void startRazorpayPayment(String courseName, int amount, String imageUrl, Button payNowButton, Button retestButton, TextView priceTextView) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_kxQ5RM3aUVo3Pk");
        checkout.setImage(R.drawable.logomate);

        JSONObject options = new JSONObject();
        try {
            options.put("name", "CodingSTUFF");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", amount);
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact", "9988776655");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            razorpayReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // Handle the broadcast if needed
                }
            };

            registerReceiver(razorpayReceiver, new IntentFilter("some_intent_filter"));

            checkout.open(mocktest.this, options);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Toast.makeText(mocktest.this, "Payment is successful : " + s, Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String courseName = sharedPreferences.getString("courseName", "");
//            courseName = (String) paymentData.getData().get("courseName");
        userPaymentsRef.child(courseName).child("courseName").setValue(courseName);
        userPaymentsRef.child(courseName).child("status").setValue("paid");

        // Store payment details under the user's node
//        String paymentId = userPaymentsRef.push().getKey();
//        userPaymentsRef.child(paymentId).child("amount").setValue(s);

        // Update UI or take any other action after successful payment
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(this, "Payment Failed due to error : " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (razorpayReceiver != null) {
            try {
                unregisterReceiver(razorpayReceiver);
            } catch (IllegalArgumentException e) {
                // Receiver was not registered
                razorpayReceiver = null;
            }
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
