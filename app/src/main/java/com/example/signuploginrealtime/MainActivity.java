package com.example.signuploginrealtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.signuploginrealtime.Fragments.HistoryFragment;
import com.example.signuploginrealtime.Fragments.HomeUserFragment;
import com.example.signuploginrealtime.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    Fragment HomeUserFragment;
    Fragment HistoryFragment;
    Fragment ProfileFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavBar);
        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView = findViewById(R.id.side_navigation_view);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        HomeUserFragment = new HomeUserFragment();
        HistoryFragment = new HistoryFragment();
        ProfileFragment = new ProfileFragment();

        switchFragment(HomeUserFragment);
        bottomNavigationView.setSelectedItemId(R.id.home);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.bt1) {
                    Intent intent = new Intent(MainActivity.this, enrolcourse.class);
                    startActivity(intent);
                } else if (id == R.id.bt2) {
                    Intent intent = new Intent(MainActivity.this, uchanpasword.class);
                    startActivity(intent);
                } else if (id == R.id.bt3) {
                    Intent intent = new Intent(MainActivity.this, tutorial.class);
                    startActivity(intent);
                } else if (id == R.id.bt4) {
                    Intent intent = new Intent(MainActivity.this, mocktest.class);
                    startActivity(intent);
                } else if (id == R.id.bt5) {
                    Intent intent = new Intent(MainActivity.this, feedback.class);
                    startActivity(intent);
                } else if (id == R.id.bt6) {
                    Intent intent = new Intent(MainActivity.this, scoreboard.class);
                    startActivity(intent);
                } else if (id == R.id.bt7) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Logout");
                    builder.setMessage("Are you sure you want to logout?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Logout the user and navigate to the home activity
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(MainActivity.this, home.class);
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

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int menu=menuItem.getItemId();
                if(menu==R.id.time){
                    switchFragment(HistoryFragment);
                    return true;
                }
                if(menu==R.id.home){
                    switchFragment(HomeUserFragment);
                    return true;
                }
                if(menu==R.id.profile){
                    switchFragment(ProfileFragment);
                    return true;
                }
                return false;
            }
        });

    }

    public void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit();
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
        finishAffinity();
    }
}