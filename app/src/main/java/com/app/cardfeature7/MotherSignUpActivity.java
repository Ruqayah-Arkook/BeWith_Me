package com.app.cardfeature7;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MotherSignUpActivity extends Activity {
    public DatabaseReference databaseReference;
    private EditText firstNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private RadioGroup genderRadioGroup;
    private Spinner daySpinner;
    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private Button signUpButton;
    private TextView signInButton; // Changed "SignIn" to "signInButton" ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mother_signup);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        firstNameEditText = findViewById(R.id.firstName);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.Password);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        daySpinner = findViewById(R.id.daySpinner);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        signUpButton = findViewById(R.id.Register);
        signInButton = findViewById(R.id.Sign_in);

        // SharedPreferences code for successful sign-up (not necessary for your main functionality)
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isSignUpSuccessful", true);
        editor.apply();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MotherSignUpActivity.this, MotherSignIn.class);
                startActivity(intent);
            }
        });

        setUpDateOfBirthSpinners();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = firstNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String gender = getSelectedGender();
                String dateOfBirth = getSelectedDateOfBirth();

                if (name.isEmpty()) {
                    firstNameEditText.setError("Full Name is required");
                    firstNameEditText.requestFocus();
                } else if (email.isEmpty()) {
                    emailEditText.setError("Email is required");
                    emailEditText.requestFocus();
                } else if (!isEmailValid(email)) {
                    emailEditText.setError("Invalid email format");
                    emailEditText.requestFocus();
                } else if (password.isEmpty() || password.length() < 6) {
                    passwordEditText.setError("Password is required and must be at least 6 characters");
                    passwordEditText.requestFocus();
                } else {
                    // Register the user if input is valid
                    registerUser(name, email, password);
                }
            }
        });
    }


    public void registerUser(String name, String email, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String parentUID = user.getUid();

                        // Convert newParent to a Map
                        Map<String, Object> parentMap = new HashMap<>();
                        parentMap.put("name", name);
                        parentMap.put("userId", parentUID);
                        parentMap.put("email", email);
                        parentMap.put("dateOfBirth", getSelectedDateOfBirth());
                        parentMap.put("gender", getSelectedGender());

                        // Change the path to store user data under "Users/Mothers" node
                        DatabaseReference mothersRef = databaseReference.child("users").child("Mothers").child(parentUID);
                        mothersRef.setValue(parentMap);

                        Toast.makeText(MotherSignUpActivity.this, "Sign-Up Successful!", Toast.LENGTH_SHORT).show();

                        // After successful registration, navigate to the desired activity.
                        Intent intent = new Intent(MotherSignUpActivity.this, ChildAccountActivity.class);
                        // Pass the parent's UID to ChildAccountActivity
                        intent.putExtra("parentUID", parentUID);
                        startActivity(intent);
                        finish(); // Optional: Finish the activity to prevent going back.
                    } else {
                        Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    // Retrieve user data from the Realtime Database based on the user's UID
    private void getUserDataFromDatabase(String userId) {
        DatabaseReference usersRef = databaseReference.child("users").child(userId);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                Log.e("DatabaseError", "Error reading user data", databaseError.toException());
            }
        });
    }

    // Method to store user data in the database
    private void storeUserData(String userId, String email, String password) {
        DatabaseReference userRef = databaseReference.child("users").child(userId);
        userRef.child("email").setValue(email);
        userRef.child("password").setValue(password);
        // You can add other user-related data here as needed.
    }

    private void setUpDateOfBirthSpinners() {
        String[] daysArray = createSpinnerArray(1, 31);
        String[] monthsArray = {"Month", "January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"};
        String[] yearsArray = createSpinnerArray(1960, 2023);

        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, daysArray);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthsArray);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearsArray);

        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        daySpinner.setAdapter(dayAdapter);
        monthSpinner.setAdapter(monthAdapter);
        yearSpinner.setAdapter(yearAdapter);
    }


    private String getSelectedGender() {
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.femaleRadioButton) {
            return "Female";
        } else if (selectedId == R.id.maleRadioButton) {
            return "Male";
        } else {
            return "";
        }
    }

    private String getSelectedDateOfBirth() {
        String selectedDay = daySpinner.getSelectedItem().toString();
        String selectedMonth = monthSpinner.getSelectedItem().toString();
        String selectedYear = yearSpinner.getSelectedItem().toString();

        return selectedDay + " " + selectedMonth + " " + selectedYear;
    }

    public boolean isEmailValid(String email) {
        if (email == null) {
            return false; // Handle null email appropriately
        }

        // Use a custom regular expression for email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isSignUpValid(String name, String email, String password) {
        if (name.isEmpty()) {
            return false; // Empty name is not valid
        }

        if (email.isEmpty() || !isEmailValid(email)) {
            return false; // Empty or invalid email is not valid
        }

        if (password.isEmpty() || password.length() < 6) {
            return false; // Empty or short password is not valid
        }


        return true; // All checks passed, the sign-up is valid
    }

    private String[] createSpinnerArray(int start, int end) {
        String[] spinnerArray = new String[end - start + 2];
        spinnerArray[0] = "Day";
        for (int i = 1; i <= end - start + 1; i++) {
            spinnerArray[i] = String.valueOf(start + i - 1);
        }
        return spinnerArray;
    }
}