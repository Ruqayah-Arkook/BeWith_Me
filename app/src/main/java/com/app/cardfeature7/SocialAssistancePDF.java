package com.app.cardfeature7;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SocialAssistancePDF extends AppCompatActivity {
    private TextView textView_Social;
    private RecyclerView socialAssistantRecyclerView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_assistance_pdf);

        // Initialize the variables
        textView_Social = findViewById(R.id.textView_Social);
        socialAssistantRecyclerView = findViewById(R.id.socialAssistantrecyclerview);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        // Setting up the Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Facial_Recognition);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.Facial_Recognition:
                    return true;
                case R.id.News:
                    startActivity(new Intent(getApplicationContext(), SocialAssistance.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
            }
            return false;
        });
        // Rest of your code...
    }
}
