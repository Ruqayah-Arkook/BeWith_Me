package com.app.cardfeature7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class SocialAssistance extends AppCompatActivity {

    private EditText editTextNews;
    private Button buttonAddNews;
    private ListView listViewNews;
    private ArrayList<NewsItem> newsItemList;
    private NewsAdapter newsAdapter;
    private DatabaseReference newsItemReference; // Firebase reference



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_assistance);

        editTextNews = findViewById(R.id.editTextNews);
        buttonAddNews = findViewById(R.id.buttonAddNews);
        listViewNews = findViewById(R.id.listViewNews);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        newsItemReference = database.getReference("News").child(FirebaseAuth.getInstance().getUid());


        newsItemList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsItemList,this, newsItemReference);
        listViewNews.setAdapter(newsAdapter);

        // Setting up the Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.News);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.News:
                    return true;
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(),SocialAssistanceAccount.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                case R.id.Children:
                    startActivity(new Intent(getApplicationContext(),SocialAssistanceListOfChildren.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
            }
            return false;
        });// Attach a ValueEventListener to populate the newsItemList
        newsItemReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newsItemList.clear(); // Clear the existing data

                // Iterate through the dataSnapshot to get updated data
                for (DataSnapshot newsSnapshot : dataSnapshot.getChildren()) {
                    String id = newsSnapshot.getKey();
                    String text = newsSnapshot.child("text").getValue(String.class);
                    newsItemList.add(new NewsItem(id, text));
                }

                // Notify the adapter that the data has changed
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during data retrieval
                Toast.makeText(SocialAssistance.this, "Failed to read data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonAddNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newsText = editTextNews.getText().toString();
                if (!newsText.isEmpty()) {
                    NewsItem newsItem = new NewsItem(newsText);

                    // Add the news item to the list for immediate display
                    newsItemList.add(newsItem);
                    newsAdapter.notifyDataSetChanged();

                    // Push the news item to the Firebase database
                    String key = newsItemReference.push().getKey();
                    if (key != null) {
                        newsItem.setId(key);
                        newsItemReference.child(key).setValue(newsItem);
                    }
                    editTextNews.setText(""); // Clear the input field
                }

            }
        });
    }

}
