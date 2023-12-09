//package com.app.cardfeature7;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//
//public class ForgotPasswordActivtiy extends AppCompatActivity {
//
//    Button btnReset,btnBack;
//    EditText edtEmail;
//    FirebaseAuth mAuth;
//    String strEmail;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_forgot_password);
//
//        btnBack = findViewById(R.id.buttonBack);
//        btnReset = findViewById(R.id.buttonResetPassword);
//        edtEmail = findViewById(R.id.editTextEmail);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        btnReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                 strEmail = edtEmail.getText().toString().trim();
//                if(!TextUtils.isEmpty(strEmail)){
//                    ResetPassword();
//                } else {
//                    edtEmail.setError("Email failed can't be empty");
//                }
//            }
//        });
//                 // Back Button
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//    }
//
//    private void ResetPassword() {
//        btnReset.setVisibility((View.INVISIBLE));
//
//        mAuth.sendPasswordResetEmail(strEmail)
//          .addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Toast.makeText(ForgotPasswordActivtiy.this,"Reset Password Link has been sent to your E-mail",Toast.LENGTH_SHORT).show();
//                Intent intent= new Intent(ForgotPasswordActivtiy.this,SocialAssistanceLogin.class);
//                startActivity(intent);
//                finish();
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(ForgotPasswordActivtiy.this, "Error:-" +e.getMessage(),Toast.LENGTH_SHORT).show();
//                        btnReset.setVisibility((View.VISIBLE));
//
//                    }
//                });
//
//        }
//
//    }