package com.app.cardfeature7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportGenerate extends AppCompatActivity {
    private FirebaseServices services;
    private Button todayButton;
    private Button weeklyButton;
    private Button monthlyButton;

    String childUID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_generate);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Facial_Recognition);




        findViewById(R.id.checkEmotions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FirebaseDatabase.getInstance().getReference("users").child("Children").child(childUID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ExtraVat.snapshot=snapshot;
                                Intent intent=new Intent(getApplicationContext(), SocialAssistantChildEmotionsList.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });





            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.Facial_Recognition:
                    return true;
                case R.id.account:
                    startActivity(new Intent(getApplicationContext(), MotherAccount.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.News:
                    startActivity(new Intent(getApplicationContext(), DisplayNewsActivity.class));
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

        // Retrieve the child's UID from SharedPreferences


        Intent intent = getIntent();
        if (intent != null) {
            String data = intent.getStringExtra("from");
            if (data != null) {
//                ProgressDialog progressDialog=new ProgressDialog(ReportGenerate.this);
//                progressDialog.setMessage("Please wait");
//                progressDialog.show();


//                System.out.println(ExtraVat.snapshot);


                childUID=ExtraVat.snapshot.getKey();

                findViewById(R.id.bottomNavigationView).setVisibility(View.GONE);
                findViewById(R.id.checkEmotions).setVisibility(View.GONE);
//                FirebaseDatabase.getInstance().getReference("users").child("SocialAssistance").child(FirebaseAuth.getInstance().getUid())
//                        .child("children").addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                for (DataSnapshot snapshot1:snapshot.getChildren()){
//                                    if(snapshot1.getKey().length()>4){
//                                        childUID=snapshot1.getKey();
//                                    }
//                                }
//                                progressDialog.dismiss();
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                            }
//                        });
            }else{
                childUID = getChildUID();
            }
        }else{

        }


        todayButton = findViewById(R.id.TodayReportPDFtButton);
        weeklyButton = findViewById(R.id.WeeklyReportPDFtButton);
        monthlyButton = findViewById(R.id.MonthlyReportPDFtButton);




//        childbutton = findViewById(R.id.childEmotion);
//        childbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ReportGenerate.this, MainChildInterface.class);
//                startActivity(intent);
//            }
//        });

        services = new FirebaseServices(ReportGenerate.this);

        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the child's UID to the createReport method
                services.createReport(ReportGenerate.this, "Your Child Today's Emotion Report! ", "today", childUID);
            }
        });

        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the child's UID to the createReport method
                services.createReport(ReportGenerate.this, "Your Child Weekly Emotion Report! ", "weekly", childUID);
            }
        });

        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the child's UID to the createReport method
                services.createReport(ReportGenerate.this, "Your Child Monthly Emotion Report! ", "monthly", childUID);
            }
        });

    }
    private String getChildUID() {
        // Retrieve the child's UID from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("ChildPreferences", MODE_PRIVATE);
        return preferences.getString("childUID", "");
    }
}
