package com.app.cardfeature7;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.widget.Toast;



public class ChildRoutineInterface extends AppCompatActivity implements ChildRoutineAdapter.OnCompleteListener {
    private RecyclerView recyclerView;
    private ChildRoutineAdapter adapter;
    private ArrayList<Task> taskList;

    private TextView todayTasksTextView;
    private String motherId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_routine_interface);

        // Initialize todayTasksTextView
        todayTasksTextView = findViewById(R.id.todayTasksTextView);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.childRecyclerView);

        // Retrieve motherId from SharedPreferences
        motherId = getMotherIdFromPreferences();

        // Use a LinearLayoutManager with reverseLayout set to false (default)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false); // Set to false
        layoutManager.setStackFromEnd(false); // Set to false
        recyclerView.setLayoutManager(layoutManager);

        // Initialize taskList before creating the adapter
        taskList = new ArrayList<>();
        adapter = new ChildRoutineAdapter(this, taskList, this, motherId);
        recyclerView.setAdapter(adapter);
        // Retrieve tasks from Firebase
        retrieveTasksFromFirebase();
    }

    // Retrieve motherId from SharedPreferences
    private String getMotherIdFromPreferences() {
        SharedPreferences preferences = getSharedPreferences("ChildPreferences", MODE_PRIVATE);
        return preferences.getString("motherId", "");
    }






    // Inside ChildRoutineInterface
    private void updateCompletionStatusInFirebase(String taskId, boolean completed) {
        DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference("Tasks").child(taskId);
        taskRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Check if the task belongs to the current child's associated mother
                    Task task = snapshot.getValue(Task.class);
                    if (task != null && motherId.equals(task.getMotherId())) {
                        taskRef.child("completed").setValue(completed);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
                Log.e("ChildRoutineInterface", "Error updating completion status: " + error.getMessage());
            }
        });
    }



    @Override
    public void onCompleteButtonClick(String taskId) {
        // Handle "Complete" button click
        Toast.makeText(this, "Task completed", Toast.LENGTH_SHORT).show();
        updateCompletionStatusInFirebase(taskId, true);
    }

    private void retrieveTasksFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tasksRef = database.getReference("Tasks");

        tasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taskList.clear(); // Clear existing data

                // Get the current date in the same format as your task date
                String currentDate = getCurrentDate();

                ArrayList<Task> completedTasks = new ArrayList<>();
                ArrayList<Task> incompleteTasks = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Task task = dataSnapshot.getValue(Task.class);

                    if (task != null) {
                        // Check if the task belongs to the current child's associated mother
                        if (currentDate.equals(task.getDate()) && motherId.equals(task.getMotherId())) {
                            if (task.isCompleted()) {
                                completedTasks.add(task);
                            } else {
                                incompleteTasks.add(task);
                            }
                        }
                    }
                }

                // Add incomplete tasks at the beginning of the list
                taskList.addAll(incompleteTasks);

                // Add completed tasks to the end of the list
                taskList.addAll(completedTasks);

                adapter.notifyDataSetChanged();

                // Update todayTasksTextView with the number of tasks for today
                int todayTasksCount = taskList.size();
                todayTasksTextView.setText("Today's Tasks: " + todayTasksCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error (log, show toast, etc.)
                Log.e("ChildRoutineInterface", "Data retrieval cancelled: " + error.getMessage());
            }
        });
    }




    private String getCurrentDate() {
        // Create a SimpleDateFormat with the desired date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        // Get the current date and format it using the SimpleDateFormat
        return sdf.format(new Date());
    }
}