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

public class MotherAccount extends AppCompatActivity {

    private TextView textViewName;
    private TextView textViewEmail, textViewChildKey;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_account);


        // Initialize TextViews
        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewChildKey = findViewById(R.id.textViewChildKey);
        btnLogout = findViewById(R.id.btnLogout);

        String childKey = getIntent().getStringExtra("childKey");

        // Setting up the Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.account);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.account:
                    return true;
                case R.id.Facial_Recognition:
                    startActivity(new Intent(getApplicationContext(), ReportGenerate.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.News:
                    startActivity(new Intent(getApplicationContext(), DisplayNewsActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });
        // Get the currently signed-in user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Check if the user is signed in
        if (currentUser != null) {
            // Get the UID of the current user
            String parentUID = currentUser.getUid();

            // Get a reference to the user's data in Firebase
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child("Mothers").child(parentUID);

            // Attach a listener to read the data at the user reference
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Get the Mother object from the snapshot
                    if (dataSnapshot.exists() && dataSnapshot.hasChild("children")) {
                        // Get the children data snapshot
                        DataSnapshot childrenSnapshot = dataSnapshot.child("children");

                        // Inside onDataChange method of MotherAccount class
                        for (DataSnapshot childSnapshot : childrenSnapshot.getChildren()) {
                            String childUID = childSnapshot.getKey();
                            // Display or use the child key as needed
                            displayChildKeyInMotherAccount(childUID);
                        }
                    }

                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        // Set the user's name and email to the TextViews
                        textViewName.setText("• Name: " + user.getName());
                        textViewEmail.setText("• Email: " + user.getEmail());
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("MotherAccount", "Failed to read user data.", databaseError.toException());
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
    // Add this method to display or use the child key
    // Add this method to display or use the child key
    private void displayChildKeyInMotherAccount(String childUID) {
        // Get a reference to the child's data in Firebase
        DatabaseReference childDataRef = FirebaseDatabase.getInstance().getReference().child("users").child("Children").child(childUID);

        // Attach a listener to read the data at the child's reference
        childDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("key")) {
                    String childKey = dataSnapshot.child("key").getValue(String.class);

                    // Display or use the child key as needed
                    textViewChildKey.setText("• Child Key: " + childKey);
                    Log.d("MotherAccount", "Child Key: " + childKey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MotherAccount", "Failed to read child data.", databaseError.toException());
            }
        });
    }

}