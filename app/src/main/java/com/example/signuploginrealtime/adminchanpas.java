package com.example.signuploginrealtime;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminchanpas extends AppCompatActivity {

    EditText currentPassword, newPassword, confirmPassword;
    Button submitButton;
    TextView successMessage;
    DatabaseReference adminRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminchanpas);

        currentPassword = findViewById(R.id.cp1);
        newPassword = findViewById(R.id.cp2);
        confirmPassword = findViewById(R.id.cp3);
        submitButton = findViewById(R.id.cp4);
        successMessage = findViewById(R.id.success_message);
        adminRef = FirebaseDatabase.getInstance().getReference().child("admin");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
    }

    private void updatePassword() {
        final String currentPass = currentPassword.getText().toString().trim();
        final String newPass = newPassword.getText().toString().trim();
        final String confirmPass = confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(currentPass)) {
            currentPassword.setError("Enter current password");
            return;
        }

        if (TextUtils.isEmpty(newPass)) {
            newPassword.setError("Enter new password");
            return;
        }

        if (TextUtils.isEmpty(confirmPass)) {
            confirmPassword.setError("Confirm new password");
            return;
        }

        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String passwordFromDB = userSnapshot.child("password").getValue(String.class);
                    if (passwordFromDB != null && passwordFromDB.equals(currentPass)) {
                        // Update the password in the database
                        userSnapshot.getRef().child("password").setValue(newPass);
                        successMessage.setText("Password updated successfully");
                        return; // Exit the loop once password is updated
                    }
                }
                currentPassword.setError("Incorrect current password");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(adminchanpas.this, "Failed to update password: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
