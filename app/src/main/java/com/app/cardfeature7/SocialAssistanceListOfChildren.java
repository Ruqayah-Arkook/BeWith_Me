package com.app.cardfeature7;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
// SocialAssistance displayed window
public class SocialAssistanceListOfChildren extends AppCompatActivity {

    private RecyclerView recyclerViewChildren;
    private ChildAdapter childAdapter;
    private List<User.ChildInfo> childrenList;
    private String socialAssistanceUID;
    private DatabaseReference socialAssistanceRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SocialAssistanceListOfChildren", "onCreate called");
        setContentView(R.layout.recycler_view_children);
        recyclerViewChildren = findViewById(R.id.recyclerViewChildren);
        recyclerViewChildren.setLayoutManager(new LinearLayoutManager(this));
        socialAssistanceUID = getIntent().getStringExtra("socialAssistanceUID");
        findViewById(R.id.report).setVisibility(View.GONE);
        if (socialAssistanceUID == null || socialAssistanceUID.isEmpty()) {
            // Handle the case where socialAssistanceUID is missing or empty
            Log.e("SocialAssistanceListOfChildren", "Invalid socialAssistanceUID");
            socialAssistanceUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        socialAssistanceRef = FirebaseDatabase.getInstance().getReference().child("users").child("SocialAssistance").child(socialAssistanceUID).child("children");
        SharedPreferences preferences = getSharedPreferences("ChildPrefs", MODE_PRIVATE);
        childrenList = new ArrayList<>();
        // Initialize and set the adapter
        childAdapter = new ChildAdapter(childrenList);
        recyclerViewChildren.setAdapter(childAdapter);
        // Setting up the Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Children);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.Children:
                    return true;
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), SocialAssistanceAccount.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.News:
                    startActivity(new Intent(getApplicationContext(), SocialAssistance.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
            }
            return false;
        });


        // Query the children data
        queryChildrenData();
        // Example: Retrieve child information and update the adapter
        retrieveChildInformation();
        // Initialize the "Add Children" button
        Button btnAddChildren = findViewById(R.id.btnAddChildren);

        // Set OnClickListener to navigate to SocialAssistance_Children activity
        btnAddChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SocialAssistanceListOfChildren.this, SocialAssistance_Children.class);
                intent.putExtra("socialAssistanceUID", socialAssistanceUID);
                startActivity(intent);
            }
        });
    }
    private void queryChildrenData() {
        socialAssistanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                        System.out.println(dataSnapshot);
//                        // Retrieve and log or process the child data
//                        String childName = childSnapshot.child("name").getValue(String.class);
//                        String childAge = childSnapshot.child("age").getValue(String.class);
//                        Log.d("ChildData", "Name: " + childName + ", Age: " + childAge);
//                    }
                    String name=dataSnapshot.child("name").getValue(String.class);
                   childrenList.add(new User.ChildInfo(name));

                } else {
                    Log.d("ChildData", "No children data found.");
                }
                System.out.println(childrenList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("ChildData", "Error: " + databaseError.getMessage());
            }
        });
    }
    private void retrieveChildInformation() {
        DatabaseReference childrenRef = FirebaseDatabase.getInstance().getReference().child("users").child("Children");
        // Assuming you have the socialAssistanceUID available
        Query query = childrenRef.orderByChild("socialAssistanceUID").equalTo(socialAssistanceUID);
        if (socialAssistanceUID == null || socialAssistanceUID.isEmpty()) {
            // Handle the case where socialAssistanceUID is missing or empty
            Log.e("SocialAssistanceListOfChildren", "Invalid socialAssistanceUID");
            // You may want to show an error message or return from the method
            return;
        }
        Log.d("SocialAssistanceListOfChildren", "Querying for socialAssistanceUID: " + socialAssistanceUID);
        Log.d("SocialAssistanceListOfChildren", "Query: " + query.toString());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                childrenList.clear(); // Clear the existing list
                Log.d("SocialAssistanceListOfChildren", "DataSnapshot size: " + dataSnapshot.getChildrenCount());

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Convert the dataSnapshot to a ChildInfo object
                    User.ChildInfo childInfo = childSnapshot.getValue(User.ChildInfo.class);
                    // Add the childInfo to the list
                    if (childInfo != null) {
                        childInfo.snapshot=childSnapshot;
                        childInfo.context=SocialAssistanceListOfChildren.this;
                        childrenList.add(childInfo);
                    }
                }
                // Log the size of the updated list
                Log.d("SocialAssistanceListOfChildren", "Updated childrenList size: " + childrenList.size());

                // Notify the adapter that the dataset has changed
                childAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("SocialAssistanceListOfChildren", "Error retrieving child information: " + databaseError.getMessage());
            }
        });
    }

}