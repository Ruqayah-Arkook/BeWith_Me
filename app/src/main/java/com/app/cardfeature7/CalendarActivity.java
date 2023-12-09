package com.app.cardfeature7;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Collections;
public class CalendarActivity extends AppCompatActivity implements MotherCardViewAdapter.OnItemClickListener {
    public static String selectedDate;
    DayScrollDatePicker dayDatePicker;
    boolean isDateSelected = false;
    private ArrayList<Task> taskArrayList;
    private ArrayList<Task> calender_taskArrayList;
    private RecyclerView recyclerView;
    private Button thePlusButton;
    private MotherCardViewAdapter motherCardViewAdapter;
    private String motherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_window);

        Button thePlusButton = findViewById(R.id.thePlusButton);
        retrieveAllDataFromFirebase();
        thePlusButton.setOnClickListener(view -> {
            if (isDateSelected) {
                // Replace this with the desired functionality when thePlusButton is clicked

                // For example, start a new activity or perform an action.
                Intent intent = new Intent(CalendarActivity.this, ClickableImageActivity.class);
                intent.putExtra("selectedDate", selectedDate);
                startActivity(intent);
            } else {
                Toast.makeText(CalendarActivity.this, "Please choose a date first", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting up the Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    return true;
                case R.id.Facial_Recognition:
                    startActivity(new Intent(getApplicationContext(), ReportGenerate.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.News:
                    startActivity(new Intent(getApplicationContext(), DisplayNewsActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.account:
                    startActivity(new Intent(getApplicationContext(), MotherAccount.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

            }
            return false;
        });

        // Initializing views and data
        taskArrayList = new ArrayList<>();
        calender_taskArrayList = new ArrayList<>();
        motherId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Replace the ListView with RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        motherCardViewAdapter = new MotherCardViewAdapter(calender_taskArrayList);
        recyclerView.setAdapter(motherCardViewAdapter);
        motherCardViewAdapter.setOnItemClickListener(this); // Set the click listener for RecyclerView items
        // Other UI components
        dayDatePicker = findViewById(R.id.dayDatePicker);

        dayDatePicker.getSelectedDate(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@Nullable Date date) {
                if (date != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1; // Add 1 to the month
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    // Use String.format to ensure two-digit month and day
                    selectedDate = String.format(Locale.US, "%04d%02d%02d", year, month, day);

                    // Fetch tasks for the selected date
                    fetchTasksForSelectedDate(selectedDate);
                } else {
                    // If no date is selected, do not clear the task list
                    calender_taskArrayList.clear();
                    motherCardViewAdapter.notifyDataSetChanged();
                    isDateSelected = false;
                }
            }
        });
    }


    private void fetchTasksForSelectedDate(String selectedDate) {
        calender_taskArrayList.clear();
        // Get the current logged-in mother's ID
        String motherId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tasksRef = database.getReference("Tasks");
        tasksRef.orderByChild("date").equalTo(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Task> uncompletedTasks = new ArrayList<>();
                ArrayList<Task> completedTasks = new ArrayList<>();
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    Task task = taskSnapshot.getValue(Task.class);
                    if (task != null && motherId.equals(task.getMotherId())) {
                        if (task.isCompleted()) {
                            completedTasks.add(task);
                        } else {
                            uncompletedTasks.add(task);
                        }
                    }
                }
                // Reverse the order of uncompleted tasks to show them at the top
                Collections.reverse(uncompletedTasks);
                calender_taskArrayList.addAll(uncompletedTasks);

                // Reverse the order of completed tasks to show them at the bottom
                Collections.reverse(completedTasks);
                calender_taskArrayList.addAll(completedTasks);

                motherCardViewAdapter.notifyDataSetChanged();
                isDateSelected = true;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CalendarActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemClick(Task task) {
        // Handle the item click here
        // Start SelectedItemActivity and pass task details
        Intent intent = new Intent(CalendarActivity.this, SelectedItemActivity.class);
        intent.putExtra("selectedItem", task.getName());
        intent.putExtra("description", task.getDescription());
        intent.putExtra("selectedImage", task.getSelectedImageUrl());
        intent.putExtra("time", task.getTime());
        intent.putExtra("recordPath", task.getVoiceRecordePath());
        intent.putExtra("id", task.getId());
        startActivity(intent);
    }

    void retrieveAllDataFromFirebase() {
        ProgressDialog dialog = new ProgressDialog(CalendarActivity.this);
        dialog.show();
        dialog.setCancelable(false);
// Get the current logged-in mother's ID
        String motherId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("MotherID", "Current Mother ID: " + motherId); // Add this line for debugging

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tasks");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taskArrayList.clear();  // Clear the existing data

                ArrayList<Task> uncompletedTasks = new ArrayList<>();
                ArrayList<Task> completedTasks = new ArrayList<>();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Task task = snapshot1.getValue(Task.class);
                    if (task != null) {
                        if (task.isCompleted()) {
                            completedTasks.add(task); // Add completed tasks to a separate list
                        } else {
                            uncompletedTasks.add(task); // Add uncompleted tasks to a separate list
                        }
                    }
                }

                // Reverse the order of uncompleted tasks to show them at the top
                Collections.reverse(uncompletedTasks);
                taskArrayList.addAll(uncompletedTasks);

                // Reverse the order of completed tasks to show them at the bottom
                Collections.reverse(completedTasks);
                taskArrayList.addAll(completedTasks);

                calender_taskArrayList.clear();

                // Filter tasks for the current mother ID and selected date if a date is selected
                for (Task task : taskArrayList) {
                    if (motherId.equals(task.getMotherId()) && (isDateSelected && selectedDate.equals(task.getDate()))) {
                        calender_taskArrayList.add(task);
                    }
                }

                motherCardViewAdapter.notifyDataSetChanged();  // Notify the adapter that the data has changed
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(CalendarActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }






}