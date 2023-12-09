package com.app.cardfeature7;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
// ..
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button loginAsParentButton = findViewById(R.id.asParentButton);
        Button loginAsChildButton = findViewById(R.id.asChildButton);
        Button loginAsSocialButton = findViewById(R.id.asSocialButton);

        loginAsParentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the MotherSignIn activity directly
                Intent intent = new Intent(LoginActivity.this, MotherSignIn.class);
                startActivity(intent);
            }
        });

        loginAsChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ChildLoginInterface activity for children
                Intent intent = new Intent(LoginActivity.this, ChildLoginInterface.class);
                startActivity(intent);
            }
        });

        loginAsSocialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, SocialAssistanceLogin.class);
                startActivity(intent);
            }
        });
    }

    private void navigateToSocialAssistance() {
        Intent intent = new Intent(LoginActivity.this, SocialAssistance.class);
        startActivity(intent);
    }
}

