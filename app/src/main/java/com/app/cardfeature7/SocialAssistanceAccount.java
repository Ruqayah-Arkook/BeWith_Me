package com.app.cardfeature7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SocialAssistanceAccount extends AppCompatActivity {

    private TextView textViewName;
    private TextView textViewEmail;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_assistance_account);

        // Initialize TextViews
        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        btnLogout = findViewById(R.id.btnLogout);

        // Setting up the Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.profile:
                    return true;
                case R.id.Children:
                    startActivity(new Intent(getApplicationContext(), SocialAssistanceListOfChildren.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.News:
                    startActivity(new Intent(getApplicationContext(), SocialAssistance.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();

            }
            return false;
        });
        // Get the currently signed-in user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Check if the user is signed in
        if (currentUser != null) {
            // Get the UID of the current user
            String socialAssistanceUID = currentUser.getUid();

            // Get a reference to the user's data in Firebase
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child("SocialAssistance").child(socialAssistanceUID);

            // Attach a listener to read the data at the user reference
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Get the SocialAssistance object from the snapshot
                    User.SocialAssistance socialAssistance = dataSnapshot.getValue(User.SocialAssistance.class);
                    if (socialAssistance != null) {
                        // Set the user's name and email to the TextViews
                        textViewName.setText("• Name: " + socialAssistance.getName());
                        textViewEmail.setText("• Email: " + socialAssistance.getEmail());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("SocialAssistanceAccount", "Failed to read user data.", databaseError.toException());
                }
            });
        }
        // Set up the log out button
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
    }
}
