package com.example.signuploginrealtime;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class tutorial extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        Button btnC = findViewById(R.id.btn3);
        Button btnPHP = findViewById(R.id.btn1);
        Button btnJava = findViewById(R.id.btn2);
        Button btnPython = findViewById(R.id.btn4);


        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView = findViewById(R.id.side_navigation_view);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new activity for C tutorial when btnC is clicked
                Intent intent = new Intent(tutorial.this, Ctutorial.class);
                startActivity(intent);
            }
        });

        btnPHP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new activity for PHP tutorial when btnPHP is clicked
                Intent intent = new Intent(tutorial.this, item_php.class);
                startActivity(intent);
            }
        });

        btnJava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new activity for Java tutorial when btnJava is clicked
                Intent intent = new Intent(tutorial.this, item_java.class);
                startActivity(intent);
            }
        });

        btnPython.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new activity for Python tutorial when btnPython is clicked
                Intent intent = new Intent(tutorial.this, item_python.class);
                startActivity(intent);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.bt1) {
                    Intent intent = new Intent(tutorial.this, enrolcourse.class);
                    startActivity(intent);
                } else if (id == R.id.bt2) {
                    Intent intent = new Intent(tutorial.this, uchanpasword.class);
                    startActivity(intent);
                } else if (id == R.id.bt3) {
                    Intent intent = new Intent(tutorial.this, tutorial.class);
                    startActivity(intent);
                } else if (id == R.id.bt4) {
                    Intent intent = new Intent(tutorial.this, feedback.class);
                    startActivity(intent);
                } else if (id == R.id.bt5) {
                    Intent intent = new Intent(tutorial.this, scoreboard.class);
                    startActivity(intent);
                } else if (id == R.id.bt6) {
                    Intent intent = new Intent(tutorial.this, scoreboard.class);
                    startActivity(intent);
                } else if (id == R.id.bt7) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tutorial.this);
                    builder.setTitle("Logout");
                    builder.setMessage("Are you sure you want to logout?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Logout the user and navigate to the home activity
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(tutorial.this, home.class);
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
