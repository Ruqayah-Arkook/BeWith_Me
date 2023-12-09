package com.app.cardfeature7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import pl.droidsonroids.gif.GifImageView;

public class MainChildInterface extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_child_interface);

        TextView headerTextView = findViewById(R.id.headerTextView);
        GifImageView detectEmotionGifImageView = findViewById(R.id.detectEmotionGifImageView);
        Button detectEmotionButton = findViewById(R.id.detectEmotionButton);
        Button trackRoutineButton = findViewById(R.id.trackRoutineButton);

        // Set up your GIF resource
        detectEmotionGifImageView.setImageResource(R.drawable.detectemotion);

        detectEmotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle "Detect Emotion" button click
                Intent intent = new Intent(MainChildInterface.this, ChildDetectingEmotion.class);
                startActivity(intent);
            }
        });

        trackRoutineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle "Track Routine" button click
                Intent intent = new Intent(MainChildInterface.this, ChildRoutineInterface.class);
                startActivity(intent);
            }
        });
    }
}
