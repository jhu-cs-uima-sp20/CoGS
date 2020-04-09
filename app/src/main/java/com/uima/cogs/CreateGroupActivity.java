package com.uima.cogs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateGroupActivity extends AppCompatActivity {

    private TextView groupName;
    private TextView groupCourse;
    private TextView shortBio;
    private Button createBtn;
    private FirebaseAuth auth;
    private DatabaseReference fireData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        shortBio = findViewById(R.id.shortDescription);
        groupCourse = findViewById(R.id.groupCourse);
        groupName = findViewById(R.id.groupName);
        createBtn = findViewById(R.id.createGroupBtn);

        auth = FirebaseAuth.getInstance();
        fireData = FirebaseDatabase.getInstance().getReference();

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeNewGroup();
                finish();
            }
        });
    }

    void storeNewGroup(){
        String shortDescription = shortBio.getText().toString();
        String courseName = groupCourse.getText().toString();
        String name = groupName.getText().toString();

        GroupDescription newGroup = new GroupDescription();
        newGroup.setShortDescription(shortDescription);
        newGroup.setCourseName(courseName);
        newGroup.setGroupName(name);
        newGroup.addMembers(auth.getUid());
        fireData.child("Groups").push().child("Group Description").setValue(newGroup).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "A New Group Has Been Created", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
