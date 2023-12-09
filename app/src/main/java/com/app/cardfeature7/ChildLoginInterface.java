package com.app.cardfeature7;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
// ..
public class ChildLoginInterface extends AppCompatActivity {
    private EditText childUsernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_log_in);
        childUsernameEditText = findViewById(R.id.childUsername);
        passwordEditText = findViewById(R.id.Password);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get child's entered credentials
                String childUsername = childUsernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                // Directly authenticate the child's login credentials
                authenticateChildLogin(childUsername, password);
            }
        });
    }

    private void authenticateChildLogin(String childUsername, String password) {
        DatabaseReference childRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child("Children");

        Query query = childRef.orderByChild("childUsername").equalTo(childUsername);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        // Retrieve child data from the Realtime Database
                        String storedPassword = child.child("password").getValue(String.class);
                        String motherId = child.child("motherId").getValue(String.class);

                        if (isPasswordCorrect(password, storedPassword)) {
                            // Password matches, you can log in successfully

                            // Save motherId and child UID in SharedPreferences for later use
                            saveMotherIdAndChildUIDInPreferences(motherId, child.getKey());

                            Toast.makeText(ChildLoginInterface.this, "Login successful!", Toast.LENGTH_SHORT).show();

                            // Now, you can navigate to the ChildRoutineInterface
                            Intent intent = new Intent(ChildLoginInterface.this, MainChildInterface.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ChildLoginInterface.this, "Login failed. Invalid password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(ChildLoginInterface.this, "Login failed. Child not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(ChildLoginInterface.this, "An error occurred.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Save motherId in SharedPreferences
    private void saveMotherIdAndChildUIDInPreferences(String motherId, String childUID) {
        SharedPreferences preferences = getSharedPreferences("ChildPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("motherId", motherId);
        editor.putString("childUID",childUID);
        editor.apply();
    }


    // Add your password hashing and verification methods here
    private boolean isPasswordCorrect(String enteredPassword, String storedPassword) {
        // Implement your password verification logic here
        // Compare enteredPassword with storedPassword (after hashing)
        return enteredPassword.equals(storedPassword);
    }
}