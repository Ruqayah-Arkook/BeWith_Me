package com.app.cardfeature7;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.app.cardfeature7.User;

public class SocialAssistanceSignup extends AppCompatActivity {

    private EditText emailField, nameField;
    private EditText passwordField;
    private Button buttonSignUp;

    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_assistance_signup);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize views
        nameField = findViewById(R.id.nameField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString().trim();
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                if (email.isEmpty()) {
                    emailField.setError("Email is required");
                    emailField.requestFocus();
                } else if (!isEmailValid(email)) {
                    emailField.setError("Invalid email format");
                    emailField.requestFocus();
                } else if (password.isEmpty() || password.length() < 6) {
                    passwordField.setError("Password is required and must be at least 6 characters");
                    passwordField.requestFocus();
                } else {
                    // Check if the email is already registered
                    mAuth.fetchSignInMethodsForEmail(email)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                                    if (!isNewUser) {
                                        Toast.makeText(SocialAssistanceSignup.this, "Email address already registered.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Continue with the registration process
                                        registerSocialAssistanceUser(name,email, password);
                                    }
                                } else {
                                    // Handle the exception
                                    Toast.makeText(SocialAssistanceSignup.this, "Failed to check email registration status.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
    private void registerSocialAssistanceUser(String name, String email, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String socialAssistanceUID = user.getUid();
                        // Create a SocialAssistance object with the user's email and password
                        User.SocialAssistance socialAssistance = new User.SocialAssistance(name,email, socialAssistanceUID);

// Store the social assistance user data under the "users/SocialAssistance" node
                        DatabaseReference socialAssistanceRef = databaseReference.child("SocialAssistance").child(socialAssistanceUID);
                        socialAssistanceRef.setValue(socialAssistance);

                        // Send email verification after successful registration
                        user.sendEmailVerification()
                                .addOnCompleteListener(this, emailVerificationTask -> {
                                    if (emailVerificationTask.isSuccessful()) {
                                        Toast.makeText(SocialAssistanceSignup.this, "Verification email sent. Please verify your email.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SocialAssistanceSignup.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        Toast.makeText(SocialAssistanceSignup.this, "Social Assistance Sign-Up Successful!", Toast.LENGTH_SHORT).show();

                        // After successful registration, navigate to the desired activity (replace SocialAssistanceActivity.class with the appropriate activity).
                        Intent intent = new Intent(SocialAssistanceSignup.this, SocialAssistance_Children.class);
                        // Pass the social assistance user's UID to SocialAssistanceActivity
                        intent.putExtra("socialAssistanceUID", socialAssistanceUID);
                        startActivity(intent);
                        finish(); // Optional: Finish the activity to prevent going back.
                    } else {
                        Toast.makeText(SocialAssistanceSignup.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Function to validate email format
    private boolean isEmailValid(String email) {
        // Implement your email format validation logic here
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}