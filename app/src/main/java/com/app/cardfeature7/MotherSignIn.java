package com.app.cardfeature7;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MotherSignIn extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button signing_button;
    private TextView textViewForgotPassword;
    private TextView buttonSignUp;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mother_sign_in);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        signing_button = findViewById(R.id.signing_button);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);


        // Check the sign-up flag in SharedPreferences ..
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isSignUpSuccessful = sharedPreferences.getBoolean("isSignUpSuccessful", false);

//        if (isSignUpSuccessful) {
//            // The user has successfully signed up, so allow them to proceed to CalendarActivity
//            Intent intent = new Intent(MotherSignIn.this, ChildAccountActivity.class);
//            startActivity(intent);
//        } else {
//            // The user has not successfully signed up, show an error message or prevent them from proceeding
//            Toast.makeText(this, "You must sign up successfully before proceeding.", Toast.LENGTH_SHORT).show();
//            // You can choose to disable the "Go" button or take other actions as needed.
//        }


        // Initialize the DatabaseReference here
        databaseReference = FirebaseDatabase.getInstance().getReference();

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the MotherSignUpActivity
                Intent intent = new Intent(MotherSignIn.this, MotherSignUpActivity.class);
                startActivity(intent);
            }
        });

        signing_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate email and password
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString();
                databaseReference = FirebaseDatabase.getInstance().getReference();
                Log.d("SignInActivity", "Sign-in button clicked.");

                if (isValidEmail(email) && isValidPassword(password)) {
                    // If email and password are valid, proceed with the sign-in logic.
                    signInUser(email, password);
                } else {
                    // Display an error message if email or password is invalid.
                    Toast.makeText(MotherSignIn.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String userId = "";
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            DatabaseReference userRef = databaseReference.child("users").child(userId);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User data exists, you can access it
                        User user = dataSnapshot.getValue(User.class);
                        // Use user data as needed
                    } else {
                        // User data doesn't exist
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "Forgot Password" action (e.g., show a password reset dialog).
            }
        });
    }


    // Helper method to validate email using a regular expression.
    public boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Helper method to validate the password (you can customize your own rules).
    public boolean isValidPassword(String password) {
        // For example, you can check the length.
        return password.length() >= 6;
    }


    // Sign in the user using Firebase Authentication
    protected void signInUser(String email, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        ProgressDialog dialog = new ProgressDialog(MotherSignIn.this);
        dialog.show();
        dialog.setCancelable(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    dialog.dismiss(); // Dismiss the progress dialog

                    if (task.isSuccessful()) {
                        // Sign-in was successful
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // User is authenticated, navigate to CalendarActivity
                            // Fetch the data for the current mother after authentication
                            retrieveDataForCurrentMother();
                            FirebaseDatabase.getInstance().getReference("users").child("Mothers").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("children").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                                if(snapshot1.getKey().length()>4){
                                                    SharedPreferences preferences = getSharedPreferences("ChildPreferences", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    editor.putString("childUID", snapshot1.getKey());
                                                    editor.apply();
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });



                            Intent intent = new Intent(MotherSignIn.this, CalendarActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        // Sign-in failed
                        runOnUiThread(() -> {
                            Toast.makeText(MotherSignIn.this, "Sign-in failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
    }




    // Fetch data for the current mother after authentication
    private void retrieveDataForCurrentMother() {
        String motherId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Implement your data retrieval logic here, similar to the existing retrieval logic in CalendarActivity
        // You can use FirebaseDatabase.getInstance().getReference().child("Tasks") and add the necessary listeners
    }


}