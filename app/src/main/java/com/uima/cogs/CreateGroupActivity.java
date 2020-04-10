package com.uima.cogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        final String courseName = groupCourse.getText().toString();
        final String courseId = courseName.replace(".", "");
        final String name = groupName.getText().toString();
        if(courseName.isEmpty() || courseId.isEmpty() || name.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please Fill in All the Fields", Toast.LENGTH_SHORT).show();

        }
        else {
            GroupDescription newGroup = new GroupDescription();
            newGroup.setShortDescription(shortDescription);
            newGroup.setCourseName(courseName);
            newGroup.setGroupName(name);
            newGroup.addMembers(auth.getUid());
            fireData.child("Groups").child(name).child("Group Description").setValue(newGroup).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "A New Group Has Been Created", Toast.LENGTH_SHORT).show();
                    addGroupToCourse(courseId, name);
                }
            });
        }

    }

    void addGroupToCourse(final String courseName, final String groupName){


        DatabaseReference courseRef = fireData.child("Courses");
        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(courseName)){
                    updateCourse(courseName, groupName);
                }
                else{

                    createNewCourse(courseName, groupName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void createNewCourse(final String courseName, final String groupName){

        DatabaseReference courseRef = fireData.child("Courses").child(courseName);
        ArrayList<String> groupList = new ArrayList<>();
        groupList.add(groupName);
        courseRef.setValue(groupList);
    }

    public void updateCourse(final String courseName, final String groupName){

        final DatabaseReference courseRef = fireData.child("Courses").child(courseName);

        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<String> groupList = new ArrayList<>();
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                groupList =  dataSnapshot.getValue(t);
                groupList.add(groupName);
                addGroupList(groupList, courseName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void addGroupList(ArrayList<String> groupList, String courseName){
        fireData.child("Courses").child(courseName).setValue(groupList);
    }

}