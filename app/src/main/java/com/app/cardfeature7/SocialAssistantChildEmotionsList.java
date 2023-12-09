package com.app.cardfeature7;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SocialAssistantChildEmotionsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_assistant_child_emotions_list);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child("Mothers").child(
                ExtraVat.snapshot.child("motherId").getValue(String.class)
        ).child("emotions");
        ListView listView = findViewById(R.id.listView);

        // Initialize adapter
        EmotionAdapter emotionAdapter = new EmotionAdapter(this, android.R.layout.simple_list_item_2);

        // Set adapter to ListView
        listView.setAdapter(emotionAdapter);

        // Fetch data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emotionAdapter.clear(); // Clear previous data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Emotion emotion = new Emotion(snapshot.child("emotion").getValue(String.class),snapshot.child("date").getValue(String.class));
                    emotionAdapter.add(emotion);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}