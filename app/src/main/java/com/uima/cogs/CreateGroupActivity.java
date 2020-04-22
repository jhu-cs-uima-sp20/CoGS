package com.uima.cogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
    private Toolbar toolbar;

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

        toolbar = findViewById(R.id.createGroupToolbar);

        auth = FirebaseAuth.getInstance();
        fireData = FirebaseDatabase.getInstance().getReference();

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeNewGroup();
                //finish();
            }
        });

        toolbar.setTitle("Create Group");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        addGroupToUser(groupName);
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
                addGroupList(groupList, courseName);//Add group list under the specific course section
                addGroupToUser(groupName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Adds a the new group to the specific course under the course section
    void addGroupList(ArrayList<String> groupList, String courseName){
        fireData.child("Courses").child(courseName).setValue(groupList);
    }
    //Adds a group under the user group section
    public void addGroupToUser(final String groupNameStr){
        final DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid());

        memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("groups")){
                    ArrayList<String> userGroupList = new ArrayList<>();
                    userGroupList.add(groupNameStr);
                    addGroupToUser2(userGroupList);

                }
                else{
                    ArrayList<String> userGroupList = new ArrayList<>();
                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                    userGroupList = dataSnapshot.child("groups").getValue(t);
                    userGroupList.add(groupNameStr);
                    addGroupToUser2(userGroupList);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void addGroupToUser2(ArrayList<String> list){
        final DatabaseReference userGroupRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("groups");
        userGroupRef.setValue(list).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(getApplicationContext(), "Successfully Created Group", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(2, intent);
                finish();
            }
        });

    }

}
