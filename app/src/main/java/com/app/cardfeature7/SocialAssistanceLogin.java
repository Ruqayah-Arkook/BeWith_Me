package com.app.cardfeature7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SocialAssistanceLogin extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Button buttonLogin;
    private TextView forgotPasswordLink;
    private TextView signUpLink;
    private TextView TextSignup;
    private DatabaseReference databaseReference; // Initialize the database reference


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_assistance_login);
        // Initialize the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        // Initialize UI elements
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        buttonLogin = findViewById(R.id.buttonLogin);
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);
        signUpLink = findViewById(R.id.signUpLink);
        TextSignup = findViewById(R.id.TextSignup);
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);

        // Set OnClickListener for the Login Button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                databaseReference = FirebaseDatabase.getInstance().getReference();
//                 Validate email and password
                if (isValidEmail(email) && isValidPassword(password)) {
                    signInSocialAssistance(email, password);
//                    Intent intent = new Intent(SocialAssistanceLogin.this, SocialAssistanceListOfChildren.class);
//                    startActivity(intent);
                } else {
                    Toast.makeText(SocialAssistanceLogin.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
//         Set OnClickListener for the Forgot Password link
//        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Open the ForgotPasswordActivity when the link is clicked
//                Intent intent = new Intent(SocialAssistanceLogin.this, ForgotPasswordActivtiy.class);
//                startActivity(intent);
//            }
//        });
        // Set OnClickListener for the "Sign Up" link (Optional)


        TextSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the SignUpActivity
                Intent intent = new Intent(SocialAssistanceLogin.this, SocialAssistanceSignup.class);
                startActivity(intent);
            }
        });
    }

    // Helper method to validate email using a regular expression.
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Helper method to validate the password (you can customize your own rules).
    private boolean isValidPassword(String password) {
        // For example, you can check the length.
        return password.length() >= 6;
    }
    // Inside signInSocialAssistance method
    private void signInSocialAssistance(String email, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                                String socialAssistanceUID = user.getUid();
                                System.out.println(socialAssistanceUID);
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child("SocialAssistance").child(socialAssistanceUID);
                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            Intent intent = new Intent(SocialAssistanceLogin.this, SocialAssistanceListOfChildren.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(SocialAssistanceLogin.this, "User data not found.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Handle errors
                                        Toast.makeText(SocialAssistanceLogin.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        }
                    } else {
                        Toast.makeText(SocialAssistanceLogin.this, "Sign-in failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
