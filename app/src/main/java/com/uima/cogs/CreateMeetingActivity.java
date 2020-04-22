package com.uima.cogs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class CreateMeetingActivity extends AppCompatActivity {

    private TextView location;
    private TextView meetingName;
    private TextView shortDescription;
    private TimePicker time;
    private DatePicker date;
    private Button createMeetingBtn;
    private Intent intent;
    private FirebaseAuth auth;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        location = findViewById(R.id.meetingLocation);
        meetingName = findViewById(R.id.meetingName);
        shortDescription = findViewById(R.id.meetingDescription);
        date = findViewById(R.id.datePicker2);
        time = findViewById(R.id.timePicker);
        createMeetingBtn = findViewById(R.id.createMeetingBtn);

        auth = FirebaseAuth.getInstance();

        createMeetingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locationStr = location.getText().toString();
                String name = meetingName.getText().toString();
                String shortDescriptionStr = shortDescription.getText().toString();
                int today_year = Calendar.getInstance().get(Calendar.YEAR);
                int today_month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                int today_day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                int year = date.getYear();
                int month = date.getMonth();
                int day = date.getDayOfMonth();

                if(year < today_year || (year == today_year && month < today_month)
                        || (year == today_year && month < today_month && day < today_day)) {
                    Toast.makeText(getApplication(), "Please Pick a Future Date", Toast.LENGTH_SHORT).show();
                }
                else if(locationStr.isEmpty() || name.isEmpty()|| shortDescriptionStr.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Fill or of the Fields", Toast.LENGTH_SHORT).show();
                }
                else{

                    Meetings newMeeting = new Meetings();
                    newMeeting.setName(name);
                    newMeeting.setLocation(locationStr);
                    newMeeting.setDecription(shortDescriptionStr);
                    newMeeting.setDay(date.getDayOfMonth());
                    newMeeting.setYear(date.getYear());
                    newMeeting.setMonth(date.getMonth());
                    newMeeting.setHour(time.getHour());
                    newMeeting.setMinute(time.getMinute());
                    Intent intent = getIntent();

                    String groupName  = intent.getStringExtra("Group Name");
                    saveMeeting( groupName, newMeeting );

                }

            }
        });


    }

    public void saveMeeting(String groupName, Meetings meeting){
        DatabaseReference meetingRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName).child("Group Meetings");
        meetingRef.push().setValue(meeting).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "New Meeting Has Been Successfully Created", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
