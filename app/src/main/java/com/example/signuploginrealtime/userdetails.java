package com.example.signuploginrealtime;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userdetails extends AppCompatActivity {

    private DatabaseReference usersRef;

    private String usernameUser, emailUser, passwordUser;
    LinearLayout coversContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetails);
        coversContainer = findViewById(R.id.coversContainer);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        showData();
    }

    private void saveUserData(String username, String newName, String newEmail, String newPassword, String newPincode) {
        usersRef.child(username).child("username").setValue(usernameUser);
        usersRef.child(username).child("email").setValue(emailUser);
        usersRef.child(username).child("password").setValue(passwordUser);


        Toast.makeText(userdetails.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
    }

    private void showData() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                coversContainer.removeAllViews(); // Clear previous views
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    usernameUser = userSnapshot.child("username").getValue(String.class);
                    emailUser = userSnapshot.child("email").getValue(String.class);
                    passwordUser = userSnapshot.child("password").getValue(String.class);


                    View coverItemView = getLayoutInflater().inflate(R.layout.item_userdetails, coversContainer, false);

                    EditText user = coverItemView.findViewById(R.id.ed1);
                    EditText userEmail = coverItemView.findViewById(R.id.ed2);
                    EditText userPassword = coverItemView.findViewById(R.id.ed3);
                    Button saveButton = coverItemView.findViewById(R.id.btn1);

                    user.setText(usernameUser);
                    userEmail.setText(emailUser);
                    userPassword.setText(passwordUser);




                    coversContainer.addView(coverItemView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(userdetails.this, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
