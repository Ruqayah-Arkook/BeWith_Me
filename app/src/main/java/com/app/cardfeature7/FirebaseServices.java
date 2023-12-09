package com.app.cardfeature7;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class FirebaseServices {
    final Context context;
    ProgressDialog progressDialog;
    FirebaseServices(Context context){
        this.context=context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Creating Report..."); // Set the message you want to display
        progressDialog.setIndeterminate(true);
    }
    void uploadData(String response, String childUID) {
        try {
            DatabaseReference childrenReference = FirebaseDatabase.getInstance().getReference("users").child("Children").child(childUID);

            // Retrieve the motherUID from the child's data
            childrenReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String motherUID = dataSnapshot.child("motherId").getValue(String.class);
                        if (motherUID != null) {
                            // Create a unique key for each emotion
                            String emotionKey = UUID.randomUUID().toString();
                            // Save the emotion for the child
                            DatabaseReference childEmotionReference = childrenReference.child("emotions").child(emotionKey);
                            Map<String, Object> childEmotionUpdates = new HashMap<>();
                            childEmotionUpdates.put("emotion", response);
                            final DateTime time = DateTime.now();
                            childEmotionUpdates.put("time", time.hourOfDay().get() + 1 + ":" + time.minuteOfHour().get());
                            childEmotionUpdates.put("date", time.dayOfMonth().get() + "/" + time.monthOfYear().get() + "/" + time.year().get());
                            childEmotionUpdates.put("childUID", childUID);
                            childEmotionReference.setValue(childEmotionUpdates);

                            // Save the same emotion information for the mother
                            DatabaseReference mothersReference = FirebaseDatabase.getInstance().getReference("users").child("Mothers").child(motherUID).child("emotions");
                            DatabaseReference motherEmotionReference = mothersReference.child(emotionKey);
                            motherEmotionReference.setValue(childEmotionUpdates);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error.
                }
            });
        } catch (Exception ignored) {
            // Handle exceptions
        }
    }
    void createReport(Context context, String reportName, String filterType, String childUID) {
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        System.out.println(childUID);
        DatabaseReference childReference = databaseReference.child("users").child("Children").child(childUID).child("emotions");
        Query query;
        String type;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//        Calendar calendar = Calendar.getInstance();
        switch (filterType) {
            case "today":
//                String today = dateFormat.format(new Date());
//                query = childReference.orderByChild("date").startAt(today);
                type="today";
                query = childReference;
                break;
            case "weekly":
//                calendar = Calendar.getInstance();
//                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
//                Date weekStartDate = calendar.getTime();
//                calendar.add(Calendar.DAY_OF_WEEK, 6);
//                Date weekEndDate = calendar.getTime();
//                String startOfWeek = dateFormat.format(weekStartDate);
//                String endOfWeek = dateFormat.format(weekEndDate);
//                query = childReference.orderByChild("date").startAt(startOfWeek);
                type="weekly";
                query = childReference;
                break;
            case "monthly":
//                calendar.set(Calendar.DAY_OF_MONTH, 1);
//                Date monthStartDate = calendar.getTime();
//                calendar.add(Calendar.MONTH, 1);
//                calendar.add(Calendar.DATE, -1);
//                Date monthEndDate = calendar.getTime();
//                String startOfMonth = dateFormat.format(monthStartDate);
//                String endOfMonth = dateFormat.format(monthEndDate);
//                query = childReference.orderByChild("date").startAt(startOfMonth).endAt(endOfMonth);
                type="monthly";
                query = childReference;
                break;
            default:
                // Invalid filter type
                return;
        }

        List<Map<String, String>> todayMap = new ArrayList<>();
        List<Map<String, String>> weeklyMap = new ArrayList<>();
        List<Map<String, String>> monthlyMap = new ArrayList<>();
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot emotionDataSnapshot : dataSnapshot.getChildren()) {
                    Map<String, String> map = new HashMap<>();
                    for (DataSnapshot grandchildDataSnapshot : emotionDataSnapshot.getChildren()) {
                        String key = grandchildDataSnapshot.getKey();
                        String value = grandchildDataSnapshot.getValue(String.class);
                        map.put(key, value);
                    }

                    switch (type) {
                        case "today":
                            String today = dateFormat.format(new Date());
                            if (today.contains(map.get("date"))) {
                                System.out.println(map);
                                todayMap.add(map);
                            }
                            break;
                        case "weekly":
                           Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                            Date weekStartDate = calendar.getTime();
                            calendar.add(Calendar.DAY_OF_WEEK, 6);
                            Date weekEndDate = calendar.getTime();
                            Date mapDate = null; // Assuming "date" is a string representing a date in your map
                            try {
                                mapDate = dateFormat.parse(map.get("date"));
                                if (mapDate.after(weekStartDate) && mapDate.before(weekEndDate)) {
                                    System.out.println(map);
                                    weeklyMap.add(map);
                                }
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }

                            break;
                        case "monthly":
                           Calendar calendar1 = Calendar.getInstance();
                            calendar1.set(Calendar.DAY_OF_MONTH, 1);
                            Date monthStartDate = calendar1.getTime();
                            calendar1.add(Calendar.MONTH, 1);
                            calendar1.add(Calendar.DATE, -1);
                            Date monthEndDate = calendar1.getTime();
                            Date mapDate1 = null; // Assuming "date" is a string representing a date in your map
                            try {
                                mapDate1 = dateFormat.parse(map.get("date"));
                                if (mapDate1.after(monthStartDate) && mapDate1.before(monthEndDate)) {
                                    System.out.println(map);
                                    monthlyMap.add(map);
                                }
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        default:

                            break;
                    }
//                    maps.add(map);
                }




                switch (type) {
                    case "today":
                        PDFGenerator.createPDF(todayMap, reportName, context);
                        progressDialog.dismiss();
                        break;
                    case "weekly":
                        PDFGenerator.createPDF(weeklyMap, reportName, context);
                        progressDialog.dismiss();
                        break;
                    case "monthly":
                        PDFGenerator.createPDF(monthlyMap, reportName, context);
                        progressDialog.dismiss();
                        break;
                    default:

                        break;
                }




//                PDFGenerator.createPDF(maps, reportName, context);
//                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error.
                progressDialog.dismiss();
                ApiClient.showErrorMessageDialog("Something went wrong! Try again", context);
            }
        });
    }
    private String getChildUID() {
        // Retrieve the child's UID from SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences("ChildPreferences", Context.MODE_PRIVATE);
        return preferences.getString("childUID", "");
    }








}
