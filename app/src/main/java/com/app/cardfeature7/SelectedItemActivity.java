package com.app.cardfeature7;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SelectedItemActivity extends AppCompatActivity {
    private String taskDescription;
    private String selectedImage;
    private String time;
    private String recordPath;
    private String id;
    TextView play_textview;
    TextView pause_textview;
    MediaPlayer mp;
    Button deleteTaskBtn;

    Button modifyTaskBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);
        TextView name_textView = findViewById(R.id.name_textView);
        TextView description_textView = findViewById(R.id.description_textView);
        TextView time_textview = findViewById(R.id.time_textView);
        ImageView imageView = findViewById(R.id.imageview);
        deleteTaskBtn = findViewById(R.id.deleteTaskBtn);
        play_textview = findViewById(R.id.play);
        pause_textview = findViewById(R.id.pause);
        String selectedItem = getIntent().getStringExtra("selectedItem");
        taskDescription = getIntent().getStringExtra("description");
        selectedImage = getIntent().getStringExtra("selectedImage");
        time = getIntent().getStringExtra("time");
        recordPath = getIntent().getStringExtra("recordPath");
        id = getIntent().getStringExtra("id");
        name_textView.setText("Name: "+selectedItem);
        description_textView.setText("Description: "+taskDescription);
        time_textview.setText(time);

        Button modifyTaskBtn = findViewById(R.id.modifyTaskBtn);
        play_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!recordPath.isEmpty()) {
                    mp = new MediaPlayer();
                    try {
                        mp.setDataSource(recordPath);
                        mp.prepare();
                        mp.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(SelectedItemActivity.this,"Empty Record",Toast.LENGTH_SHORT).show();
                }
            }
        });
        pause_textview.setOnClickListener(view -> {
            if (mp.isPlaying()) {
                mp.stop();
                mp.reset();
                mp.release();
                Toast.makeText(SelectedItemActivity.this, "Audio has been paused", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SelectedItemActivity.this, "Audio has not played", Toast.LENGTH_SHORT).show();
            }
        });
        if(!selectedImage.isEmpty()) {
            Picasso.get().load(selectedImage).into(imageView);
        }
        deleteTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Tasks");
                myRef.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SelectedItemActivity.this,"Removed successfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),CalendarActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        modifyTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modifyIntent = new Intent(SelectedItemActivity.this, ClickableImageActivity.class);
                modifyIntent.putExtra("id", id);
                modifyIntent.putExtra("name", selectedItem);
                modifyIntent.putExtra("description", taskDescription);
                modifyIntent.putExtra("selectedImage", selectedImage);
                modifyIntent.putExtra("time", time);
                modifyIntent.putExtra("recordPath", recordPath);
                startActivity(modifyIntent);
            }
        });
    }
}
