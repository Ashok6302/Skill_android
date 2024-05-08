package com.example.signuploginrealtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.signuploginrealtime.AdminFragments.HomeAdminFragment;
import com.example.signuploginrealtime.AdminFragments.ProfileFragment2;
import com.example.signuploginrealtime.AdminFragments.StudentHistoryFragment;
import com.example.signuploginrealtime.Fragments.HistoryFragment;
import com.example.signuploginrealtime.Fragments.HomeUserFragment;
import com.example.signuploginrealtime.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class mainhome extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    Fragment HomeAdminFragment;
    Fragment StudentHistoryFragment;
    Fragment ProfileFragment2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainhome);
        bottomNavigationView = findViewById(R.id.bottomNavBar);
        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView = findViewById(R.id.side_navigation_view);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        HomeAdminFragment = new HomeAdminFragment();
        StudentHistoryFragment = new StudentHistoryFragment();
        ProfileFragment2 = new ProfileFragment2();

        switchFragment(HomeAdminFragment);
        bottomNavigationView.setSelectedItemId(R.id.home);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            int id = menuItem.getItemId();

            if (id == R.id.bt1) {
                Intent intent = new Intent(mainhome.this, studreg.class);
                startActivity(intent);
            } else if (id == R.id.bt2) {
                Intent intent = new Intent(mainhome.this, adminchanpas.class);
                startActivity(intent);
            }  else if (id == R.id.bt4) {
                    Intent intent = new Intent(mainhome.this, studentscore.class);
                    startActivity(intent);
            }  else if (id == R.id.bt3) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainhome.this);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Logout the user and navigate to the home activity
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(mainhome.this, home.class);
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
                switchFragment(StudentHistoryFragment);
            }
            if(menu==R.id.home){
                switchFragment(HomeAdminFragment);
            }
            if(menu==R.id.profile){
                switchFragment(ProfileFragment2);
            }
            return true;
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

}
