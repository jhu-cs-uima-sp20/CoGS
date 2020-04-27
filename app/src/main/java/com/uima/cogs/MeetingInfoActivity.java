package com.uima.cogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MeetingInfoActivity extends AppCompatActivity {

    private TextView name;
    private TextView location;
    private TextView shortDes;
    private TextView time;
    private ArrayList<String> members = new ArrayList<>();
    private ArrayList<User> groupMembers;
    private MeetingCustomAdapter customAdapter;
    private GridView gridView;
    private Switch switchBtn;
    private FirebaseAuth auth;
    private String groupName;
    private String meeting_id;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_info);
        Intent intent = getIntent();
        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.meetingNameInfo);
        location = findViewById(R.id.meetingInfoLocation);
        shortDes = findViewById(R.id.meetingShortInfo);
        time = findViewById(R.id.meetingDateInfo);
        gridView = findViewById(R.id.meetingGridView);
        switchBtn = findViewById(R.id.switch2);
        toolbar = findViewById(R.id.meetingInfoToolbar);

        name.setText(intent.getStringExtra("Meeting Name"));
        location.setText(intent.getStringExtra("Meeting Location"));
        shortDes.setText(intent.getStringExtra("Meeting Description"));
        time.setText(intent.getStringExtra("Meeting Time"));
        ArrayList<String> list = intent.getStringArrayListExtra("List");
        if(list.contains(auth.getUid())){
            switchBtn.setChecked(true);
        }

        groupMembers = new ArrayList<>();
        customAdapter = new MeetingCustomAdapter(groupMembers, this);
        gridView.setAdapter(customAdapter);
        meeting_id = intent.getStringExtra("Meeting Id");
        groupName = intent.getStringExtra("Group Name");
        System.out.println("Group Name: "+groupName);
        System.out.println("Meeting Id: "+meeting_id);
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName).child("Group Meetings").child(meeting_id).child("attendess");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupMembers.clear();
                customAdapter.notifyDataSetChanged();

                if(dataSnapshot.exists()){
                    System.out.println("Hello");
                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                    members = dataSnapshot.getValue(t);
                    getUsers();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    addUser();
                }
                else{
                    removeUser();
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setTitle(intent.getStringExtra("Meeting Name"));

    }

    public void getUsers(){

        for(int i = 0; i< members.size(); i++){

            DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(members.get(i));
            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    groupMembers.add(user);
                    System.out.println("User!!!!!!!!!!!!!!: "+ user.getName());
                    customAdapter.notifyDataSetChanged();
                    gridView.setAdapter(customAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }


    public void addUser(){

        if(members!=null){
            int pos = groupMembers.indexOf(auth.getUid());
            if(!members.contains(auth.getUid())){
                members.add(auth.getUid());
                updateDatabase(members);
                //members.clear();
                //customAdapter.notifyDataSetChanged();
            }

        }
    }

    public void removeUser(){
        if(members!=null){
            int pos = groupMembers.indexOf(auth.getUid());
            if(members.contains(auth.getUid())){
                members.remove(auth.getUid());
                updateDatabase(members);
                customAdapter.notifyDataSetChanged();
            }

        }
    }

    public void updateDatabase(ArrayList<String> members){
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName).child("Group Meetings").child(meeting_id).child("attendess");
        dataRef.setValue(members);

    }

}

class MeetingCustomAdapter extends BaseAdapter {

    private ArrayList<User> users;
    private LayoutInflater inflter;

    public MeetingCustomAdapter(ArrayList<User> userList, Context applicationContext){

        users = userList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public int getCount() {
        return users.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view1 = inflter.inflate(R.layout.user_grid_view, null);
        TextView userName = view1.findViewById(R.id.gridViewUserName);
        CircularImageView userImage = view1.findViewById(R.id.gridViewUserImage);
        userName.setText(users.get(position).getName());
        Picasso.get().load(users.get(position).getImageUrl()).into(userImage);
        return view1;
    }

    public void clear(){
        users.clear();
    }
}
