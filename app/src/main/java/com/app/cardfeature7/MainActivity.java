
package com.app.cardfeature7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements gridAdapter.OnItemClickListener {
    public static String selectedImagePath = "";
    public ArrayList<String> dailyTaskImagePathArray;
    public gridAdapter gridAdapter;
    public RecyclerView recyclerView;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dailyTaskImagePathArray= new ArrayList<>();
        retrieveImageFromFirebase();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        gridAdapter = new gridAdapter(this, dailyTaskImagePathArray);
        recyclerView.setAdapter(gridAdapter);
        gridAdapter.setOnItemClickListener(this);
    }
@Override
public void onItemClick(View view, int position) {
    selectedImagePath = dailyTaskImagePathArray.get(position);
    Intent intent = new Intent(MainActivity.this, ClickableImageActivity.class);
    startActivity(intent);
}
    public void retrieveImageFromFirebase(){
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DailyTaskImages");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int i=0;i<snapshot.getChildrenCount();i++)
                {
                    int position = i+1;
                    String imagePath = snapshot.child("image"+position).getValue(String.class);
                    dailyTaskImagePathArray.add(imagePath);
                }
                if(dailyTaskImagePathArray.size()>0)
                {
                    gridAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Error"+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}


