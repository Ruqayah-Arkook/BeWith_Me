package com.app.cardfeature7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class DisplayNewsActivity extends AppCompatActivity {

    private DatabaseReference newsItemReference;
    private RecyclerView recyclerView;
    private NewsDisplayAdapter newsDisplayAdapter;
    String childKey="";
    String socialAssistanceUID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_news);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        System.out.println(FirebaseAuth.getInstance().getUid());





        DatabaseReference firebaseDatabase= FirebaseDatabase.getInstance()
                .getReference("users").child("Mothers").child(FirebaseAuth.getInstance().getUid()).child("children");
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    childKey=snapshot1.getKey();
                }



                DatabaseReference ref2= FirebaseDatabase.getInstance()
                        .getReference("users").child("Children").child(childKey);
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        socialAssistanceUID=snapshot.child("socialAssistanceUID").getValue(String.class);


                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        newsItemReference = database.getReference("News").child(socialAssistanceUID);



                        newsItemReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // Clear existing data
                                newsDisplayAdapter.clear();

                                for (DataSnapshot newsSnapshot : dataSnapshot.getChildren()) {
                                    String text = newsSnapshot.child("text").getValue(String.class);
                                    newsDisplayAdapter.addNewsItem(new NewsItem(null, text)); // Use a default ID
                                }

                                // Notify the adapter that the data has changed
                                newsDisplayAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle any errors
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        newsDisplayAdapter = new NewsDisplayAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(newsDisplayAdapter);

        // Add a ValueEventListener to update news when changes occur

        // Setting up the Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.News);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.News:
                    return true;
                case R.id.Facial_Recognition:
                    startActivity(new Intent(getApplicationContext(), ReportGenerate.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.account:
                    startActivity(new Intent(getApplicationContext(), MotherAccount.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

            }
            return false;
        });

    }
}
