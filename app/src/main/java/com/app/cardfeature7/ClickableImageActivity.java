 package com.app.cardfeature7;

import android.content.Context;
import android.content.Intent;


import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClickableImageActivity extends AppCompatActivity implements TimeSectionActivity.OnTimeSetListener {

    private TextView selectedTimeTextView;
    private TextView selectedvoice;
    private Button createTaskButton;
    private Button ChooseTimeButton;
    private Button RecordVoice;
    private List<String> textList;
    private static final int VOICE_RECORD_REQUEST_CODE = 2;
    private ArrayAdapter<String> textAdapter;
    private static final int GALLERY_REQUEST_CODE = 1;
    EditText editTextTaskName;
    EditText editTextTaskDescription;
    FirebaseDatabase database;
    DatabaseReference myRef;



    public static boolean validate(String image, String name, String time) {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clickable_image);
        Intent intent = getIntent();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Tasks");
        editTextTaskName = findViewById(R.id.editTextTaskName);
        editTextTaskDescription = findViewById(R.id.editTextDescription);
        ImageView clickableImage = findViewById(R.id.defaultImage);
        if(!MainActivity.selectedImagePath.isEmpty()) {
            Picasso.get().load(MainActivity.selectedImagePath).placeholder(R.drawable.bewithme).into(clickableImage);
        }
        selectedTimeTextView = findViewById(R.id.selectedTimeTextView);
        selectedvoice = findViewById(R.id.tvVoiceCreated);
        createTaskButton = findViewById(R.id.createTaskButton);
        ChooseTimeButton = findViewById(R.id.ChooseTimeButton);

        RecordVoice = findViewById(R.id.RecordVoice);
        textList = new ArrayList<>();
        textAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, textList);

        ChooseTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = editTextTaskName.getText().toString();
                String taskDescription = editTextTaskDescription.getText().toString();
                String selectedDate = CalendarActivity.selectedDate;
                String voiceRecordingPath = VoiceRecordActivity.path;
                String selectedImagePath = MainActivity.selectedImagePath;

                // Use the appropriate variable for child's ID
                String childId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                // Check if the task is being modified
                if (getIntent().hasExtra("id")) {
                    // Modify existing task
                    String id = getIntent().getStringExtra("id");

                    // Update the existing task with modified details
                    Task modifiedTask = new Task(id, taskName, taskDescription, selectedImagePath, selectedDate, voiceRecordingPath, selectedTimeTextView.getText().toString(), false, childId);
// Update the task using the correct reference
                    myRef.child(id).setValue(modifiedTask);

                    // Return to the previous activity
                    finish();
                } else {
                    // Create a new task
                    String id = myRef.push().getKey();

                    // Set the childId field when creating a new task
                    Task newTask = new Task(id, taskName, taskDescription, selectedImagePath, selectedDate, voiceRecordingPath, selectedTimeTextView.getText().toString(), false, childId);

                    myRef.child(id).setValue(newTask);

                    // Finish the current activity and return to the previous one (CalendarActivity)
                    finish();
                }
            }
        });







        RecordVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the VoiceRecordActivity for recording voice
                Intent voiceRecordIntent = new Intent(ClickableImageActivity.this, VoiceRecordActivity.class);
                startActivityForResult(voiceRecordIntent, VOICE_RECORD_REQUEST_CODE);
            }
        });

        clickableImage.setOnClickListener(v -> {
            // Start the MainActivity
            Intent intent1 = new Intent(ClickableImageActivity.this, MainActivity.class);
            startActivity(intent1);
        });
        editTextTaskName = findViewById(R.id.editTextTaskName);
        editTextTaskName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});

        if (intent.hasExtra("id")) {
            String taskId = intent.getStringExtra("id");
            String taskName = intent.getStringExtra("name");
            String taskDescription = intent.getStringExtra("description");
            String selectedImage = intent.getStringExtra("selectedImage");
            String time = intent.getStringExtra("time");
            String recordPath = intent.getStringExtra("recordPath");

            // Populate UI with existing task details
            editTextTaskName.setText(taskName);
            editTextTaskDescription.setText(taskDescription);
            selectedTimeTextView.setText(time);
            selectedvoice.setText("Recorded time: " + recordPath);

            if (!selectedImage.isEmpty()) {
                Picasso.get().load(selectedImage).placeholder(R.drawable.bewithme).into(clickableImage);
            }
        }
    }

    static boolean validate(Context context,String image, String name, String time) {
        if(image.isEmpty())
        {
            Toast.makeText(context,"Select Image First",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(name.isEmpty())
        {
            Toast.makeText(context,"Put a Task Name First",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(time.isEmpty())
        {
            Toast.makeText(context,"Select Time First",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showTimePickerDialog() {
        TimeSectionActivity timeSectionActivity = new TimeSectionActivity(this, 0, 0);
        timeSectionActivity.show(getSupportFragmentManager(), "time_picker");
    }
    @Override
    public void onTimeSet(int hourOfDay, int minute) {
        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
        String displayText = "Selected time: " + formattedTime;
        selectedTimeTextView.setText(displayText);
        selectedTimeTextView.setVisibility(View.VISIBLE);
    }

    public void onRecordingFinished(int durationInSeconds) {
        int minutes = durationInSeconds / 60;
        int seconds = durationInSeconds % 60;
        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        String displayText = "Recorded time: " + formattedTime;
        selectedvoice.setText(displayText);
        selectedvoice.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            int position = data.getIntExtra("position", -1);
            if (position != -1) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedImage", selectedImage);
                resultIntent.putExtra("position", position);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        }
        // new part of the code its strange :
        if (requestCode == VOICE_RECORD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Handle the recording result if needed
            }
        }
    }
}