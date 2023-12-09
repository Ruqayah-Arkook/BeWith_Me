package com.app.cardfeature7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChildAccountActivity extends AppCompatActivity {

    private EditText childNameEditText;
    private EditText childAgeEditText;
    private EditText childUserNameEditText;
    private EditText passwordEditText;
    private Button createAccountButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private String password;

    private DatabaseReference childrenRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_child_account);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        childNameEditText = findViewById(R.id.childNameEditText);
        childAgeEditText = findViewById(R.id.childAgeEditText);
        childUserNameEditText = findViewById(R.id.childUserName);
        passwordEditText = findViewById(R.id.Password);
        createAccountButton = findViewById(R.id.createAccountButton);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidInput()) {
                    // Generate a key using KeyGenerator class
                    String generatedKey = KeyGenerator.generateKey();
                    // Proceed with creating the child account directly and pass the generated key
                    createChildAccount(generatedKey);
                    Toast.makeText(ChildAccountActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChildAccountActivity.this, CalendarActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    private void createChildAccount(String generatedKey) {
        Log.d("ChildAccountActivity", "createChildAccount: Method called");

        String childName = childNameEditText.getText().toString().trim();
        String childAge = childAgeEditText.getText().toString().trim();
        String childUsername = childUserNameEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();

        // Retrieve parent's UID and social assistance UID from intent
        String parentUID = getIntent().getStringExtra("parentUID");
        String socialAssistanceUID = getIntent().getStringExtra("socialAssistanceUID");
        // Generate a unique child UID
        String childUID = generateChildUID();

        // Link the child to the parent and store the mother's ID in the child's data
        DatabaseReference parentChildrenRef = databaseReference.child("users").child("Mothers").child(parentUID).child("children");
        parentChildrenRef.child(childUID).setValue(true);

        // Change the path to store child data under "Children" node
        DatabaseReference childrenRef = databaseReference.child("users").child("Children").child(childUID);
        // Store the mother's UID in the child's data
        childrenRef.child("motherId").setValue(parentUID);

        // Continue with the rest of the account creation logic
        DatabaseReference childDataRef = childrenRef;
        childDataRef.child("childName").setValue(childName);
        childDataRef.child("childAge").setValue(childAge);
        childDataRef.child("childUsername").setValue(childUsername);
        childDataRef.child("key").setValue(generatedKey);
        childDataRef.child("password").setValue(password);

        // Associate child with Social Assistance directly under the Child node
        childDataRef.child("SocialAssistanceUID").setValue(socialAssistanceUID);

        // Store the child UID in SharedPreferences
        saveChildUID(childUID);

        Log.d("ChildAccountActivity", "createChildAccount: Account created successfully");
    }

    // Retrieve the mother's UID from SharedPreferences
    private String getMotherUIDFromPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return preferences.getString("motherUID", "");
    }
    private void retrieveSharedKeyAndCreateChildAccount() {
        // Retrieve parent's UID from intent
        String parentUID = getIntent().getStringExtra("parentUID");

        // Retrieve the shared key from the mother's account
        DatabaseReference mothersRef = databaseReference.child("users").child("Mothers").child(parentUID);
        mothersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Generate a key using KeyGenerator class
                    String generatedKey = KeyGenerator.generateKey();

                    // Continue with creating the child account using the retrieved shared key
                    createChildAccount(generatedKey);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Log.e("ChildAccountActivity", "Error reading mother's data", databaseError.toException());
            }
        });
    }
    private void saveChildUID(String childUID) {
        // Save the child UID to SharedPreferences
        SharedPreferences preferences = getSharedPreferences("ChildPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("childUID", childUID);
        editor.apply();
    }
    // Generate a unique child UID
    private String generateChildUID() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder uidBuilder = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            int index = (int) (Math.random() * characters.length());
            uidBuilder.append(characters.charAt(index));
        }

        return uidBuilder.toString();
    }
    private boolean isValidInput() {
        boolean isValid = true;

        String childName = childNameEditText.getText().toString().trim();
        String childAge = childAgeEditText.getText().toString().trim();
        String childUserName = childUserNameEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();

        if (childName.isEmpty()) {
            childNameEditText.setError("Child's name is required");
            isValid = false;
        }

        if (childAge.isEmpty()) {
            childAgeEditText.setError("Child's age is required");
            isValid = false;
        }


        if (childUserName.isEmpty()) {
            childUserNameEditText.setError("Child's username is required");
            isValid = false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            isValid = false;
        }

        return isValid;
    }
}
