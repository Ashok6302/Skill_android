package com.example.signuploginrealtime.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.signuploginrealtime.R;
import com.example.signuploginrealtime.enrolcourse;
import com.example.signuploginrealtime.homeuser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class HomeUserFragment extends Fragment {
    LinearLayout coversContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_home_user, container, false);

        coversContainer = view.findViewById(R.id.coversContainer);
        showCoversForShop();

        return view;
    }

    private void showCoversForShop() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("adcourse");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    // No data found for the specific shop
                    return;
                }

                for (DataSnapshot coversSnapshot : snapshot.getChildren()) {
                    Log.d("Covers", "Processing entry for covers");

                    String imageUrl = coversSnapshot.child("imageUrl").getValue(String.class);
                    String courseName = coversSnapshot.child("courseName").getValue(String.class);
                    String amount = coversSnapshot.child("amount").getValue(String.class);
                    String description = coversSnapshot.child("description").getValue(String.class); // Retrieve the nextpage value

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Log.d("Covers", "Image URL: " + imageUrl);

                        // Inflate the item_cover.xml layout
                        View coverItemView = getLayoutInflater().inflate(R.layout.item_courses, coversContainer, false);

                        // Find views in the inflated layout
                        ImageView coverImageView = coverItemView.findViewById(R.id.img1);
                        TextView descriptionn = coverItemView.findViewById(R.id.t1);
                        TextView price = coverItemView.findViewById(R.id.tt4);
                        Button button = coverItemView.findViewById(R.id.btn1);
                        // Set data for each view
                        Picasso.get().load(imageUrl).into(coverImageView); // Load image using Picasso
                        descriptionn.setText(" " + description);
                        price.setText(" " + amount);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(requireContext(), enrolcourse.class);
                                startActivity(intent);
                            }
                        });

                        // Add the inflated layout to the coversContainer
                        coversContainer.addView(coverItemView);
                    } else {
                        Log.e("Covers", "Image URL is null or empty for an entry");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Covers", "Database error: " + error.getMessage());
            }
        });
    }

}