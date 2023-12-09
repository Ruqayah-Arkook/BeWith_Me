package com.app.cardfeature7;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


// this java class dedicated to adding a child in the social assistance to monitor them
public class SocialAssistance_Children extends AppCompatActivity {

    private EditText editTextNameChild;
    private TextView textViewTitle;
    private EditText childUserName;
    private EditText sharedKey;
    private Button btnCreateChild;


    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_assistance_children); // Make sure to use the correct layout file

        // Initialize views
        childUserName = findViewById(R.id.childUserName);
        sharedKey = findViewById(R.id.sharedKey);
        btnCreateChild = findViewById(R.id.btnCreateChild);
        textViewTitle = findViewById(R.id.textViewTitle);

        String socialAssistanceUID = getIntent().getStringExtra("socialAssistanceUID");

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // Set OnClickListener to navigate to SocialAssistanceListOfChildren activity
        btnCreateChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidChildInput()) {
                    // Get child username and password
                    String childUsername = childUserName.getText().toString().trim();
                    String enteredSharedKey = sharedKey.getText().toString().trim();

                    // Check if the child with the given credentials exists
                    findChildAndAddToSocialAssistance(childUsername, enteredSharedKey, socialAssistanceUID);
                    Toast.makeText(SocialAssistance_Children.this,
                            "New Child created and associated with Social Assistance", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void saveChildInfoInSharedPreferences(User.ChildInfo childInfo) {
        // Example: Save child UID in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("ChildPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(childInfo.getChildUID(), childInfo.toString());
        editor.apply();
    }

    private void findChildAndAddToSocialAssistance(String childUsername, String enteredSharedKey, String socialAssistanceUID) {
        DatabaseReference childrenRef = databaseReference.child("users").child("Children");

        // Validate child credentials
        validateChildCredentials(childUsername, enteredSharedKey, socialAssistanceUID);
    }
    private void validateChildCredentials(String childUsername, String enteredSharedKey, String socialAssistanceUID) {
        DatabaseReference queryRef = databaseReference.child("users").child("Children");
        Query query = queryRef.orderByChild("childUsername").equalTo(childUsername);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean childFound = false;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String storedKey = childSnapshot.child("key").getValue(String.class);
                    // Check if the entered key matches the stored key
                    if (storedKey != null && storedKey.equals(enteredSharedKey)) {
                        String socialAssistanceUID = childSnapshot.child("socialAssistanceUID").getValue(String.class);
                        if (socialAssistanceUID == null || socialAssistanceUID.isEmpty()) {
                            // Child is not associated with any social assistance, proceed with association
                            String childUID = childSnapshot.getKey();
                            associateChildWithSocialAssistance(childUID, socialAssistanceUID);
                            childFound = true;
                            break;
                        } else {
                            Toast.makeText(SocialAssistance_Children.this,
                                    "Child is already associated with a social assistance", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
                if (!childFound) {
                    Toast.makeText(SocialAssistance_Children.this, "Invalid child credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if any
                Log.e("SocialAssistanceActivity", "Error validating child credentials: " + databaseError.getMessage());
            }
        });
    }
    private boolean isValidChildInput() {
        boolean isValid = true;

        String childUsername = childUserName.getText().toString().trim();  // Change to childUserName
        String enteredSharedKey = sharedKey.getText().toString().trim();

        if (childUsername.isEmpty()) {
            childUserName.setError("Child's username is required");  // Change to childUserName
            isValid = false;
        }

        if (enteredSharedKey.isEmpty()) {
            sharedKey.setError("Password is required");
            isValid = false;
        }

        return isValid;
    }
    private void associateChildWithSocialAssistance(String childUID, String socialAssistanceUID) {
        // Get social assistance UID from intent
        socialAssistanceUID = getIntent().getStringExtra("socialAssistanceUID");

        if (socialAssistanceUID != null) {
            // Create an association between the child and social assistance
            DatabaseReference childRef = databaseReference.child("users").child("Children").child(childUID);
            childRef.child("socialAssistanceUID").setValue(socialAssistanceUID);

            // Link the child to the social assistance and store the social assistance UID in the child's data
            DatabaseReference socialAssistanceChildrenRef = databaseReference.child("users").child("SocialAssistance").child(socialAssistanceUID).child("children");
            socialAssistanceChildrenRef.child(childUID).setValue(true);





            Map<String, Object> childEmotionUpdates = new HashMap<>();
//            childEmotionUpdates.put("childName",editTextNameChild.getText().toString());
            databaseReference.child("users").child("SocialAssistance").child(socialAssistanceUID).child("children").child("name").setValue(
                    childUserName.getText().toString()
            );
            ;
            // Start the SocialAssistanceListOfChildren activity
            Intent intent = new Intent(SocialAssistance_Children.this, SocialAssistanceListOfChildren.class);
            intent.putExtra("socialAssistanceUID", socialAssistanceUID);  // Pass the social assistance UID
            startActivity(intent);

            Toast.makeText(SocialAssistance_Children.this, "Child associated with Social Assistance", Toast.LENGTH_SHORT).show();
        } else {
            // Log an error or show a Toast indicating that socialAssistanceUID is null
            Log.e("SocialAssistance_Children", "socialAssistanceUID is null");
            Toast.makeText(SocialAssistance_Children.this, "Error: socialAssistanceUID is null", Toast.LENGTH_SHORT).show();
        }
    }

}
