package com.example.signuploginrealtime;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.signuploginrealtime.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class adcourse extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText nameEditText,descriptionEditText, price;
    private ImageView wrapperImageView;
    private Button uploadButton,  uploadImageButton;

    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference wrappersReference;
    private String shopName; // Add this variable to store shopName

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adcourse);

        // Initialize Firebase storage and database references
        storageReference = FirebaseStorage.getInstance().getReference().child("adcourse");
        wrappersReference = FirebaseDatabase.getInstance().getReference("adcourse");




        nameEditText = findViewById(R.id.et1);
        descriptionEditText = findViewById(R.id.et2);
        price = findViewById(R.id.et3);
        wrapperImageView = findViewById(R.id.imageView2);
        uploadButton = findViewById(R.id.btn);

        wrapperImageView.setOnClickListener(v -> openFileChooser());


        uploadButton.setOnClickListener(v -> uploadWrapper());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadWrapper() {
        String amount = price.getText().toString();
        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        // Check if both image and price are provided
        if (imageUri != null ) {


            StorageReference fileReference = storageReference.child(name).child("adcourse" + System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL from the task snapshot
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Create a new Wrapper object with shopName
                            Course wrapper = new Course(name,description, amount,uri.toString());

                            // Add the wrapper to the database under shopName/coverX
                            DatabaseReference newWrapperRef = wrappersReference.child(name);
                            newWrapperRef.setValue(wrapper);

                            Toast.makeText(adcourse.this, "Course added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(adcourse.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Please provide both image and price", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            wrapperImageView.setImageURI(imageUri);
        }
    }
}